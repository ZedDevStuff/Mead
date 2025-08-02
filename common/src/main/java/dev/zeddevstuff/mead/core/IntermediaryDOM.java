package dev.zeddevstuff.mead.core;

import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.elements.parsing.IParsingCompleteListener;
import dev.zeddevstuff.mead.parsing.MeadParser;
import dev.zeddevstuff.mead.utils.SingleEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;

public class IntermediaryDOM
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private IntermediaryElement root;
    public IntermediaryElement getRoot() { return root; }
    private final UUID key = UUID.randomUUID();
    private final SingleEvent<Void> parsingCompleteEvent = new SingleEvent<>(key);

    public IntermediaryDOM(org.w3c.dom.Element rootElement)
    {
        if(rootElement != null)
            this.root = buildElementTree(rootElement);
    }

    public IntermediaryElement buildElementTree(org.w3c.dom.Element el)
    {
        IntermediaryElement element = new IntermediaryElement(
            el.getTagName(),
            getAttributes(el),
            el.getTextContent()
        );
        for(int i = 0; i < el.getChildNodes().getLength(); i++)
        {
            var childNode = el.getChildNodes().item(i);
            if(childNode instanceof org.w3c.dom.Element childElement)
            {
                IntermediaryElement child = buildElementTree(childElement);
                child.parent = element;
                element.children.add(child);
            }
        }
        return element;
    }

    public MeadElement build(MeadContext ctx, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
    {
        if(root == null)
        {
            LOGGER.error("IntermediaryDOM has no root element. Cannot build MeadElement tree.");
            return null;
        }
        MeadElement rootElement = buildRecursive(ctx, root, null, variables, actions);
        if(rootElement == null)
        {
            LOGGER.error("Failed to build MeadElement tree from IntermediaryDOM. Root element is null.");
            return null;
        }
        parsingCompleteEvent.fire(key, null);
        return rootElement;
    }
    private MeadElement buildRecursive(MeadContext ctx, IntermediaryElement el, MeadElement parent, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
    {
        MeadParser.IMeadElementFactory factory = ctx.elementFactories.get(el.getTagName());
        if(factory == null)
        {
            LOGGER.error("Unknown element type: {}. Could not find factory for it. Aborting build.", el.getTagName());
            return null;
        }
        MeadElement meadElement = factory.createElement(el.getAttributes(), variables, actions, el.getTextContent());
        if(meadElement == null)
        {
            LOGGER.error("Factory for element '{}' returned null. Aborting build.", el.getTagName());
            return null;
        }
        meadElement.setCtx(ctx);
        if(meadElement instanceof IParsingCompleteListener elListener)
            parsingCompleteEvent.addListener(elListener::parsingComplete);
        if(parent != null)
            parent.addChild(meadElement);
        for(IntermediaryElement child : el.children)
        {
            buildRecursive(ctx, child, meadElement, variables, actions);
        }
        return meadElement;
    }

    private HashMap<String, String> getAttributes(org.w3c.dom.Element el)
    {
        var attributeMap = el.getAttributes();
        if(attributeMap == null || attributeMap.getLength() == 0)
            return new HashMap<>();
        HashMap<String, String> attributes = new HashMap<>();
        for(int i = 0; i < el.getAttributes().getLength(); i++)
        {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) el.getAttributes().item(i);
            attributes.put(attr.getName(), attr.getValue());
        }
        return attributes;
    }

    public static class IntermediaryElement
    {
        private final String tagName;
        public String getTagName() { return tagName; }
        private final HashMap<String, String> attributes;
        public HashMap<String, String> getAttributes() { return attributes; }
        private final String textContent;
        public String getTextContent() { return textContent; }

        private IntermediaryElement parent;
        private final ArrayList<IntermediaryElement> children = new ArrayList<>();

        public IntermediaryElement(String tagName, HashMap<String, String> attributes, String textContent)
        {
            this.tagName = tagName;
            this.attributes = attributes;
            this.textContent = textContent;
        }

    }
}
