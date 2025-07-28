package dev.zeddevstuff.mead.interfaces;

import dev.zeddevstuff.mead.core.Binding;
import net.minecraft.network.chat.Component;

import java.util.HashMap;

public interface IHasTextProperties
{
	TextProperties textProps();
	public class TextProperties
	{
		private final Binding<Component> text = new Binding<>(Component.empty());
		public Binding<Component> text() { return text; }
		private final Binding<Boolean> localized = new Binding<>(false);
		public Binding<Boolean> localized() { return localized; }
		private final Binding<Integer> textColor = new Binding<>(0xFFFFFF);
		public Binding<Integer> textColor() { return textColor; }
		private final Binding<Integer> textShadowColor = new Binding<>(0x000000);
		public Binding<Integer> textShadowColor() { return textShadowColor; }
		private final Binding<Boolean> textShadow = new Binding<>(false);
		public Binding<Boolean> textShadow() { return textShadow; }
		private final Binding<Integer> textScale = new Binding<>(1);
		public Binding<Integer> textScale() { return textScale; }
		private final Binding<Boolean> textCentered = new Binding<>(false);
	}

	public static void applyTextProperties(IHasTextProperties element, HashMap<String, String> attributes)
	{
		if(attributes == null)
			return;
		if(attributes.containsKey("text"))
		{
			if(attributes.get("localized").equals("true"))
				element.textProps().text.set(Component.translatable(attributes.get("text")));
			else
				element.textProps().text.set(Component.literal(attributes.get("text")));
		}
		if(attributes.containsKey("localized"))
			element.textProps().localized.set(Boolean.parseBoolean(attributes.get("localized")));
		if(attributes.containsKey("color"))
			element.textProps().textColor.set(Integer.parseInt(attributes.get("color"), 16));
		if(attributes.containsKey("shadowColor"))
			element.textProps().textShadowColor.set(Integer.parseInt(attributes.get("shadowColor"), 16));
		if(attributes.containsKey("shadow"))
			element.textProps().textShadow.set(Boolean.parseBoolean(attributes.get("shadow")));
		if(attributes.containsKey("scale"))
			element.textProps().textScale.set(Integer.parseInt(attributes.get("scale")));
		if(attributes.containsKey("centered"))
			element.textProps().textCentered.set(Boolean.parseBoolean(attributes.get("centered")));
	}
}
