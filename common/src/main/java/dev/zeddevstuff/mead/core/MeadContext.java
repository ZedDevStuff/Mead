package dev.zeddevstuff.mead.core;

import dev.zeddevstuff.mead.core.elements.Element;
import dev.zeddevstuff.mead.core.elements.RectElement;
import dev.zeddevstuff.mead.core.elements.TextElement;
import dev.zeddevstuff.mead.core.elements.flow.IfElement;
import dev.zeddevstuff.mead.core.elements.interactive.ButtonElement;
import dev.zeddevstuff.mead.core.elements.parsing.ImportElement;
import dev.zeddevstuff.mead.core.elements.parsing.StyleElement;
import dev.zeddevstuff.mead.parsing.MeadParser;
import dev.zeddevstuff.mead.parsing.MeadStyleSheetsParser;
import dev.zeddevstuff.mead.styling.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

/**
 * The centralized interface to Mead. It is recommended for you to use this instead of directly using Mead classes.
 */
public class MeadContext
{
    private final UUID key = UUID.randomUUID();
    public final Registry<MeadParser.IMeadElementFactory> elementFactories = new Registry<>(key);
    public final Registry<IMeadStylePropertyApplier> stylePropertyAppliers = new Registry<>(key);
    private final String modid;
    private final HashMap<String, MeadStyle> styleSheets = new HashMap<>();

    public MeadContext(String modid)
    {
        this.modid = modid;
        registerDefaults();
    }

    public MeadParser createParser()
    {
        return new MeadParser(this);
    }
    public MeadStyleSheetsParser createStyleSheetsParser()
    {
        return new MeadStyleSheetsParser(this);
    }

    public void registerDefaults()
    {
        registerDefaultFactories();
        registerDefaultStylePropertyAppliers();
        registerMeadStyle();
    }
    private void registerDefaultFactories()
    {
        elementFactories.register("Mead", Element::new);
        // Parsing
        elementFactories.register("Import", ImportElement::new);
        elementFactories.register("Style", StyleElement::new);
        // Elements
        elementFactories.register("Rect", RectElement::new);
        elementFactories.register("Text", TextElement::new);
        // Interactive elements
        elementFactories.register("Button", ButtonElement::new);
        // Flow control elements
        elementFactories.register("If", IfElement::new);
    }
    private void registerDefaultStylePropertyAppliers()
    {
        stylePropertyAppliers.register("Layout", new LayoutStylePropertyApplier());
        stylePropertyAppliers.register("Color", new ColorStylePropertyApplier());
        stylePropertyAppliers.register("Text", new TextStylePropertyApplier());
    }
    private void registerMeadStyle()
    {
        MeadStyle style = null;
    }
    public void loadStyleSheets()
    {
        // TODO: Find a way to load all style sheets in the current mod's resources.
    }
}
