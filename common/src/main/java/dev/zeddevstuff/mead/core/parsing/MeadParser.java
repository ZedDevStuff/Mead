package dev.zeddevstuff.mead.core.parsing;

import dev.zeddevstuff.mead.core.IntermediaryDOM;
import dev.zeddevstuff.mead.core.data.Property;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;

public class MeadParser
{
	private final static Logger logger = LoggerFactory.getLogger(MeadParser.class);
	private static DocumentBuilder documentBuilder;

	static
	{
		initializeDocumentBuilder();
	}

	private static void initializeDocumentBuilder()
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
	public static Optional<IntermediaryDOM> parse(String xml)
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

	public interface IMeadElementFactory
	{
		MeadElement createElement(HashMap<String, String> attributes, HashMap<String, Property<?>> variables, HashMap<String, Callable<?>> actions, String textContent);
	}
}
