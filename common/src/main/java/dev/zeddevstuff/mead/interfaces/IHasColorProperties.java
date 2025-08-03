package dev.zeddevstuff.mead.interfaces;

import dev.zeddevstuff.mead.core.data.Observable;
import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.HashMap;

public interface IHasColorProperties
{
	ColorProperties colorProps();

	public class ColorProperties
	{
		private final Observable<Integer> backgroundColor = new Observable<>(0xFFFFFFFF);
		public Observable<Integer> backgroundColor() { return backgroundColor; }
		private final Observable<Integer> backgroundHoverColor = new Observable<>(0xFFCCCCCC);
		public Observable<Integer> backgroundHoverColor() { return backgroundHoverColor; }
		private final Observable<Integer> backgroundActiveColor = new Observable<>(0xFFBBBBBB);
		public Observable<Integer> backgroundActiveColor() { return backgroundActiveColor; }
		private final Observable<Integer> backgroundDisabledColor = new Observable<>(0xFFAAAAAA);
		public Observable<Integer> backgroundDisabledColor() { return backgroundDisabledColor; }

		private final Observable<Integer> borderColor = new Observable<>(0xFF000000);
		public Observable<Integer> borderColor() { return borderColor; }
		private final Observable<Integer> borderHoverColor = new Observable<>(0xFF888888);
		public Observable<Integer> borderHoverColor() { return borderHoverColor; }
		private final Observable<Integer> borderActiveColor = new Observable<>(0xFF777777);
		public Observable<Integer> borderActiveColor() { return borderActiveColor; }
		private final Observable<Integer> borderDisabledColor = new Observable<>(0xFF555555);
		public Observable<Integer> borderDisabledColor() { return borderDisabledColor; }

		private final Observable<Integer> textColor = new Observable<>(0xFFFFFFFF);
		public Observable<Integer> textColor() { return textColor; }
		private final Observable<Integer> textHoverColor = new Observable<>(0xFFFFFFFF);
		public Observable<Integer> textHoverColor() { return textHoverColor; }
		private final Observable<Integer> textActiveColor = new Observable<>(0xFFFFFFFF);
		public Observable<Integer> textActiveColor() { return textActiveColor; }
		private final Observable<Integer> textDisabledColor = new Observable<>(0xFFAAAAAA);
		public Observable<Integer> textDisabledColor() { return textDisabledColor; }

		private final Observable<Integer> textShadowColor = new Observable<>(0xFF000000);
		public Observable<Integer> textShadowColor() { return textShadowColor; }
		private final Observable<Integer> textShadowHoverColor = new Observable<>(0xFF000000);
		public Observable<Integer> textShadowHoverColor() { return textShadowHoverColor; }
		private final Observable<Integer> textShadowActiveColor = new Observable<>(0xFF000000);
		public Observable<Integer> textShadowActiveColor() { return textShadowActiveColor; }
		private final Observable<Integer> textShadowDisabledColor = new Observable<>(0xFFAAAAAA);
		public Observable<Integer> textShadowDisabledColor() { return textShadowDisabledColor; }




		public int getBackgroundColor(boolean isEnabled, boolean isHovered, boolean isFocused)
		{
			if (!isEnabled) return backgroundDisabledColor.get();
			if(isFocused) return backgroundActiveColor.get();
			if (isHovered) return backgroundHoverColor.get();
			return backgroundColor.get();
		}

		public int getBorderColor(boolean isEnabled, boolean isHovered, boolean isFocused)
		{
			if (!isEnabled) return borderDisabledColor.get();
			if(isFocused) return borderActiveColor.get();
			if (isHovered) return borderHoverColor.get();
			return borderColor.get();
		}

		public int getTextColor(boolean isEnabled, boolean isHovered, boolean isFocused)
		{
			if (!isEnabled) return textDisabledColor.get();
			if(isFocused) return textActiveColor.get();
			if (isHovered) return textHoverColor.get();
			return textColor.get();
		}

		public int getTextShadowColor(boolean isEnabled, boolean isHovered, boolean isFocused)
		{
			if (!isEnabled) return textShadowDisabledColor.get();
			if(isFocused) return textShadowActiveColor.get();
			if (isHovered) return textShadowHoverColor.get();
			return textShadowColor.get();
		}

	}

	static void applyAttributes(IHasColorProperties element, HashMap<String, String> attributes)
	{
		if (attributes == null || element == null) return;
		NullUtils.ifNotNull(attributes.get("backgroundColor"), color -> {
			element.colorProps().backgroundColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("backgroundHoverColor"), color -> {
			element.colorProps().backgroundHoverColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("backgroundActiveColor"), color -> {
			element.colorProps().backgroundActiveColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("backgroundDisabledColor"), color -> {
			element.colorProps().backgroundDisabledColor.set(IStringParser.COLOR_PARSER.parse(color));
		});

		NullUtils.ifNotNull(attributes.get("borderColor"), color -> {
			element.colorProps().borderColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("borderHoverColor"), color -> {
			element.colorProps().borderHoverColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("borderActiveColor"), color -> {
			element.colorProps().borderActiveColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("borderDisabledColor"), color -> {
			element.colorProps().borderDisabledColor.set(IStringParser.COLOR_PARSER.parse(color));
		});

		NullUtils.ifNotNull(attributes.get("textColor"), color -> {
			element.colorProps().textColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("textHoverColor"), color -> {
			element.colorProps().textHoverColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("textActiveColor"), color -> {
			element.colorProps().textActiveColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("textDisabledColor"), color -> {
			element.colorProps().textDisabledColor.set(IStringParser.COLOR_PARSER.parse(color));
		});

		NullUtils.ifNotNull(attributes.get("textShadowColor"), color -> {
			element.colorProps().textShadowColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("textShadowHoverColor"), color -> {
			element.colorProps().textShadowHoverColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("textShadowActiveColor"), color -> {
			element.colorProps().textShadowActiveColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("textShadowDisabledColor"), color -> {
			element.colorProps().textShadowDisabledColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
	}
}
