package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.data.ObservableProperty;
import net.minecraft.network.chat.Component;

import java.util.HashMap;

public interface IHasTextProperties
{
	TextProperties textProps();
	public class TextProperties
	{
		private final ObservableProperty<Component> text = new ObservableProperty<>(Component.empty());
		public ObservableProperty<Component> text() { return text; }
		private final ObservableProperty<Integer> textSize = new ObservableProperty<>(12);
		public ObservableProperty<Integer> textSize() { return textSize; }
		private final ObservableProperty<Boolean> localized = new ObservableProperty<>(false);
		public ObservableProperty<Boolean> localized() { return localized; }
		private final ObservableProperty<Boolean> textShadow = new ObservableProperty<>(false);
		public ObservableProperty<Boolean> textShadow() { return textShadow; }
		private final ObservableProperty<Integer> textScale = new ObservableProperty<>(1);
		public ObservableProperty<Integer> textScale() { return textScale; }
		private final ObservableProperty<Boolean> textCentered = new ObservableProperty<>(false);
	}

	static void applyAttributes(IHasTextProperties element, HashMap<String, String> attributes)
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
		element.textProps().textShadow.set(Boolean.parseBoolean(attributes.get("shadow")));
		if(attributes.containsKey("scale"))
			element.textProps().textScale.set(Integer.parseInt(attributes.get("scale")));
		if(attributes.containsKey("centered"))
			element.textProps().textCentered.set(Boolean.parseBoolean(attributes.get("centered")));
	}
}
