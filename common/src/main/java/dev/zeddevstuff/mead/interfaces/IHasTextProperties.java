package dev.zeddevstuff.mead.interfaces;

import dev.zeddevstuff.mead.core.data.Observable;
import net.minecraft.network.chat.Component;

import java.util.HashMap;

public interface IHasTextProperties
{
	TextProperties textProps();
	public class TextProperties
	{
		private final Observable<Component> text = new Observable<>(Component.empty());
		public Observable<Component> text() { return text; }
		private final Observable<Integer> textSize = new Observable<>(12);
		public Observable<Integer> textSize() { return textSize; }
		private final Observable<Boolean> localized = new Observable<>(false);
		public Observable<Boolean> localized() { return localized; }
		private final Observable<Boolean> textShadow = new Observable<>(false);
		public Observable<Boolean> textShadow() { return textShadow; }
		private final Observable<Integer> textScale = new Observable<>(1);
		public Observable<Integer> textScale() { return textScale; }
		private final Observable<Boolean> textCentered = new Observable<>(false);
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
