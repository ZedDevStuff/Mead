package dev.zeddevstuff.mead.parsing;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.IntermediaryDOM;
import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.core.elements.Element;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.elements.parsing.IParsingCompleteListener;
import dev.zeddevstuff.mead.utils.SingleEvent;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.Callable;

public class MeadParser
{
	private final Logger logger = LoggerFactory.getLogger(MeadParser.class);
	private final MeadContext ctx;
	private DocumentBuilder documentBuilder;

	private HashMap<String, IMeadElementFactory> elements = new HashMap<>();
	private HashMap<String, Binding<?>> variables = new HashMap<>();
	private HashMap<String, Callable<?>> actions = new HashMap<>();
	private final UUID eventKey = UUID.randomUUID();
	private final SingleEvent<Void> parsingCompleteEvent = new SingleEvent<>(eventKey);

	public MeadParser(@NotNull MeadContext ctx)
	{
		this.ctx = ctx;
		this.elements = ctx.elementFactories.toMap();
		initializeDocumentBuilder();
	}
	public MeadParser(@NotNull MeadContext ctx, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		this.ctx = ctx;
		this.elements = ctx.elementFactories.toMap();
		this.variables = variables;
		this.actions = actions;
		initializeDocumentBuilder();
	}
	public MeadParser(@NotNull MeadContext ctx, HashMap<String, IMeadElementFactory> elements, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		this.ctx = ctx;
		this.elements = ctx.elementFactories.toMap();
		for(String key : elements.keySet())
		{
			var res = this.elements.putIfAbsent(key, elements.get(key));
			if(res != null)
			{
				logger.warn("Element factory for '{}' already exists, ignoring new one.", key);
			}
		}
		this.variables = variables;
		this.actions = actions;
		initializeDocumentBuilder();
	}

	private void initializeDocumentBuilder()
	{
		try
		{
			var factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringComments(true);
			documentBuilder = factory.newDocumentBuilder();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Failed to initialize DocumentBuilder", e);
		}
	}
	public Optional<IntermediaryDOM> parseIntermediary(String xml)
	{
		try
		{
			Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
			var intermediary = new IntermediaryDOM(document.getDocumentElement());
			return Optional.of(intermediary);
		}
		catch (IOException | SAXException e)
        {
			logger.error("Failed to parse Intermediary DOM from content: {}", e.getMessage());
			return Optional.empty();
        }
    }
	public Optional<MeadElement> parse(String input)
	{
		try
		{
			Document document = documentBuilder.parse(new ByteArrayInputStream(input.getBytes()));
			var rootElement = document.getDocumentElement();
			if(!Objects.equals(rootElement.getTagName(), "Mead"))
			{
				logger.warn("Root element is not 'Mead', found: {}", rootElement.getTagName());
				return Optional.empty();
			}
			if(!rootElement.hasChildNodes())
			{
				logger.warn("Root element 'Mead' is empty.");
				return Optional.of(new Element(null, variables, actions, ""));
			}
			MeadElement root = new Element(null, variables, actions, "");
			root.setCtx(ctx);
			root.getNode().setFlexDirection(YogaFlexDirection.COLUMN);
			buildHierarchy(root, rootElement);
			parsingCompleteEvent.fire(eventKey, null);
			return Optional.of(root);
		}
		catch (Exception e)
		{
			logger.error("Failed to parse Mead input: {}", e.getMessage());
			return Optional.empty();
		}
	}

	private void buildHierarchy(MeadElement parent, org.w3c.dom.Element element)
	{
		String tagName = element.getTagName();
		HashMap<String, String> attributes = new HashMap<>();
		for (int i = 0; i < element.getAttributes().getLength(); i++)
		{
			var attrNode = element.getAttributes().item(i);
			if (attrNode instanceof org.w3c.dom.Attr attr)
				attributes.put(attr.getName(), attr.getValue());
		}
		IMeadElementFactory factory = elements.get(tagName);
		if (factory == null)
		{
			logger.warn("Unknown element type: {}. Could not find factory for it.", tagName);
			return;
		}
		MeadElement meadElement = null;
		String textContent = element.getTextContent().trim();
		if("Mead".equals(tagName)) meadElement = parent;
		else meadElement = factory.createElement(attributes, variables, actions, textContent);
		if (meadElement == null)
		{
			logger.warn("Factory for element '{}' returned null.", tagName);
			return;
		}
		meadElement.setCtx(ctx);
		if(meadElement instanceof IParsingCompleteListener el) parsingCompleteEvent.addListener(el::parsingComplete);
		if(!"Mead".equals(tagName)) parent.addChild(meadElement);
		for (int i = 0; i < element.getChildNodes().getLength(); i++)
		{
			var childNode = element.getChildNodes().item(i);
			if (childNode instanceof org.w3c.dom.Element)
			{
				buildHierarchy(meadElement, (org.w3c.dom.Element) childNode);
			}
			else if (childNode instanceof org.w3c.dom.Text)
			{
				String textContent1 = childNode.getTextContent().trim();
				if (!textContent.isEmpty())
				{
					meadElement.textContent.set(textContent1);
				}
			}
		}
	}

	public interface IMeadElementFactory
	{
		MeadElement createElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions, String textContent);
	}
}
