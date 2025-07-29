package dev.zeddevstuff.mead.core;

import dev.zeddevstuff.mead.core.elements.*;
import dev.zeddevstuff.mead.core.elements.flow.*;
import dev.zeddevstuff.mead.core.elements.interactive.*;
import dev.zeddevstuff.mead.core.elements.parsing.*;
import dev.zeddevstuff.mead.parsing.MeadParser;
import dev.zeddevstuff.mead.styling.*;

import java.util.UUID;

public class Registries
{
	private static final UUID key = UUID.randomUUID();
	public static final Registry<MeadParser.IMeadElementFactory> elementFactories = new Registry<>(key);
	public static final Registry<IMeadStylePropertyApplier> stylePropertyAppliers = new Registry<>(key);

	public static void registerDefaults()
	{
		registerDefaultFactories();
		registerDefaultStylePropertyAppliers();

	}
	private static void registerDefaultFactories()
	{
		elementFactories.register("Mead", Element::new);

		elementFactories.register("Import", ImportElement::new);
		elementFactories.register("Style", StyleElement::new);

		elementFactories.register("Rect", RectElement::new);
		elementFactories.register("Text", TextElement::new);

		elementFactories.register("Button", ButtonElement::new);

		elementFactories.register("If", IfElement::new);
	}
	private static void registerDefaultStylePropertyAppliers()
	{
		stylePropertyAppliers.register("Layout", new LayoutStylePropertyApplier());
		stylePropertyAppliers.register("Color", new ColorStylePropertyApplier());
		stylePropertyAppliers.register("Text", new TextStylePropertyApplier());
	}
}
