package dev.zeddevstuff.mead.core.elements.parsing;

import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.core.data.Property;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.parsing.MeadStyleSheetsParser;
import dev.zeddevstuff.mead.core.styling.MeadStyle;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;

public class StyleElement extends MeadElement implements IParsingCompleteListener
{
    private final Logger logger = LogUtils.getLogger();
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
            logger.warn("Style element with src '{}' has text content, ignoring src attribute.", src);
        }
    }

    @Override
    public String getTagName() { return "null"; }
    public StyleElement(HashMap<String, String> attributes, HashMap<String, Property<?>> variables, HashMap<String, Callable<?>> actions, @NotNull String textContent)
    {
        super(null, null, null, textContent);
        NullUtils.ifNotNull(attributes.get("src"), value -> src = value);
    }

    public Optional<MeadStyle> get() { return style; }

    @Override
    public AbstractWidget getWidget() { return null; }

    public void parsingComplete()
    {
        if(textContent.<String>get().isBlank())
        {
            if(!src.isBlank())
                setSrc(src);
        }
        else
        {
            style = MeadStyleSheetsParser.parse(ctx, textContent.get());
            style.ifPresent(meadStyle -> meadStyle.applyToTree(getRoot()));
        }
    }
}
