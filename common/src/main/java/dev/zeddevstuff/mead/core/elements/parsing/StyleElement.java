package dev.zeddevstuff.mead.core.elements.parsing;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.parsing.MeadStyleSheetsParser;
import dev.zeddevstuff.mead.styling.MeadStyle;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;

public class StyleElement extends MeadElement
{
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<MeadStyle> style = Optional.empty();
    @Override
    public String getTagName() { return "null"; }
    public StyleElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
    {
        super(null, null, null);
    }

    public Optional<MeadStyle> get() { return style; }

    @Override
    public AbstractWidget getWidget() { return null; }

    public void parsingComplete(Void ignored)
    {
        if(textContent.<String>get().isBlank()) return;
        style = getCtx().createStyleSheetsParser().parse(textContent.get());
        style.ifPresent(meadStyle -> meadStyle.applyToTree(getRoot()));
    }
}
