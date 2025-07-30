package dev.zeddevstuff.mead.core.elements.parsing;

import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.parsing.MeadStyleSheetsParser;
import dev.zeddevstuff.mead.styling.MeadStyle;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;

public class StyleElement extends MeadElement implements IParsingCompleteListener
{
    private final Logger LOGGER = LogUtils.getLogger();
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<MeadStyle> style = Optional.empty();
    private String src;

    public void setSrc(String src)
    {
        if(textContent.<String>get().isBlank())
        {
            this.src = src;
            style = getCtx().getStyleSheet(src);
            style.ifPresent(meadStyle -> meadStyle.applyToTree(getRoot()));
        }
        else
        {
            LOGGER.warn("Style element with src '{}' has text content, ignoring src attribute.", src);
        }
    }

    @Override
    public String getTagName() { return "null"; }
    public StyleElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
    {
        super(null, null, null);
        NullUtils.ifNotNull(attributes.get("src"), value -> src = value);
    }

    public Optional<MeadStyle> get() { return style; }

    @Override
    public AbstractWidget getWidget() { return null; }

    public void parsingComplete(Void ignored)
    {
        if(textContent.<String>get().isBlank())
        {
            if(!src.isBlank())
                setSrc(src);
        }
        else
        {
            style = getCtx().createStyleSheetsParser().parse(textContent.get());
            style.ifPresent(meadStyle -> meadStyle.applyToTree(getRoot()));
        }
    }
}
