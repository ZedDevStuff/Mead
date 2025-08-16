package dev.zeddevstuff.mead.core;

import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.elements.parsing.IParsingCompleteListener;
import dev.zeddevstuff.mead.core.parsing.MeadParser;
import dev.zeddevstuff.mead.utils.SingleEvent;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.Callable;

public class IntermediateDOM
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private IntermediateElement root;
    public IntermediateElement getRoot() { return root; }
    private final UUID key = UUID.randomUUID();
    private final SingleEvent<Void> parsingCompleteEvent = new SingleEvent<>(key);

    public IntermediateDOM(org.w3c.dom.Element rootElement)
    {
        if(rootElement != null)
            this.root = buildElementTree(rootElement);
    }

    public IntermediateElement buildElementTree(org.w3c.dom.Element el)
    {
        IntermediateElement element = new IntermediateElement(
            this,
            el.getTagName(),
            getAttributes(el),
            el.getTextContent()
        );
        for(int i = 0; i < el.getChildNodes().getLength(); i++)
        {
            var childNode = el.getChildNodes().item(i);
            if(childNode instanceof org.w3c.dom.Element childElement)
            {
                IntermediateElement child = buildElementTree(childElement);
                child.parent = element;
                element.children.add(child);
            }
        }
        element.built();
        return element;
    }

    public MeadElement build(MeadContext ctx, HashMap<String, Callable<?>> actions)
    {
        if(root == null)
        {
            LOGGER.error("IntermediateDOM has no root element. Cannot build MeadElement tree.");
            return null;
        }
        MeadElement rootElement = buildRecursive(ctx, root, null, actions);
        if(rootElement == null)
        {
            LOGGER.error("Failed to build MeadElement tree from IntermediateDOM. Root element is null.");
            return null;
        }
        parsingCompleteEvent.fire(key, null);
        return rootElement;
    }
    private MeadElement buildRecursive(MeadContext ctx, IntermediateElement el, MeadElement parent, HashMap<String, Callable<?>> actions)
    {
        MeadParser.IMeadElementFactory factory = ctx.elementFactories.get(el.getTagName());
        if(factory == null)
        {
            LOGGER.error("Unknown element type: {}. Could not find factory for it. Aborting build.", el.getTagName());
            return null;
        }
        MeadElement meadElement = factory.createElement(el.getAttributes(), actions, el.getTextContent());
        if(meadElement == null)
        {
            LOGGER.error("Factory for element '{}' returned null. Aborting build.", el.getTagName());
            return null;
        }
        meadElement.setCtx(ctx);
        if(meadElement instanceof IParsingCompleteListener elListener)
            parsingCompleteEvent.addListener((ignored) -> elListener.parsingComplete());
        if(parent != null)
            parent.addChild(meadElement);
        for(IntermediateElement child : el.children)
        {
            buildRecursive(ctx, child, meadElement, actions);
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

    public static class IntermediateElement
    {
        private final IntermediateDOM dom;
        private final String tagName;
        public String getTagName() { return tagName; }
        private final HashMap<String, String> attributes;
        public HashMap<String, String> getAttributes() { return attributes; }
        private final String textContent;
        public String getTextContent() { return textContent; }

        private IntermediateElement parent;
        private List<IntermediateElement> children = new ArrayList<>();
        public List<IntermediateElement> getChildren()
        {
            return children;
        }

        private IntermediateElement(IntermediateDOM dom, String tagName, HashMap<String, String> attributes, String textContent)
        {
            this.dom = dom;
            this.tagName = tagName;
            this.attributes = attributes;
            this.textContent = textContent;
        }

        public MeadElement build(MeadContext ctx, HashMap<String, Callable<?>> actions)
        {
            return buildRecursive(ctx, null, actions);
        }
        private MeadElement buildRecursive(MeadContext ctx, MeadElement parent, HashMap<String, Callable<?>> actions)
        {
            MeadParser.IMeadElementFactory factory = ctx.elementFactories.get(getTagName());
            if(factory == null)
            {
                LOGGER.error("Unknown element type: {}. Could not find factory for it. Aborting build.", getTagName());
                return null;
            }
            MeadElement meadElement = factory.createElement(getAttributes(), actions, getTextContent());
            if(meadElement == null)
            {
                LOGGER.error("Factory for element '{}' returned null. Aborting build.", getTagName());
                return null;
            }
            meadElement.setCtx(ctx);
            if(meadElement instanceof IParsingCompleteListener elListener)
                dom.parsingCompleteEvent.addListener((ignored) -> elListener.parsingComplete());
            if(parent != null)
                parent.addChild(meadElement);
            for(IntermediateElement child : children)
            {
                buildRecursive(ctx, meadElement, actions);
            }
            return meadElement;
        }

        void built()
        {
            children = Collections.unmodifiableList(children);
            for(var child : children)
            {
                child.built();
            }
        }

    }
}
