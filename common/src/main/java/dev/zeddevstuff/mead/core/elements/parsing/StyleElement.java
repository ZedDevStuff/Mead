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

    @Override
    public String getTagName() { return "null"; }
    public StyleElement(HashMap<String, String> attributes, HashMap<String, Callable<?>> actions, @NotNull String textContent)
    {
        super(null, null, textContent);
    }

    public Optional<MeadStyle> get() { return style; }

    @Override
    public AbstractWidget getWidget() { return null; }

    public void parsingComplete()
    {
        style = MeadStyleSheetsParser.parse(ctx, textContent.get());
        style.ifPresent(meadStyle -> meadStyle.applyToTree(getRoot()));
    }
}
