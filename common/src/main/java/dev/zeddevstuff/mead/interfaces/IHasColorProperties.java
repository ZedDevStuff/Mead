package dev.zeddevstuff.mead.interfaces;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.HashMap;

public interface IHasColorProperties
{
	ColorProperties colorProps();

	public class ColorProperties
	{
		private final Binding<Integer> backgroundColor = new Binding<>(0xFFFFFFFF);
		public Binding<Integer> backgroundColor() { return backgroundColor; }
		private final Binding<Integer> backgroundHoverColor = new Binding<>(0xFFCCCCCC);
		public Binding<Integer> backgroundHoverColor() { return backgroundHoverColor; }
		private final Binding<Integer> backgroundActiveColor = new Binding<>(0xFFBBBBBB);
		public Binding<Integer> backgroundActiveColor() { return backgroundActiveColor; }
		private final Binding<Integer> backgroundDisabledColor = new Binding<>(0xFFAAAAAA);
		public Binding<Integer> backgroundDisabledColor() { return backgroundDisabledColor; }

		private final Binding<Integer> borderColor = new Binding<>(0xFF000000);
		public Binding<Integer> borderColor() { return borderColor; }
		private final Binding<Integer> borderHoverColor = new Binding<>(0xFF888888);
		public Binding<Integer> borderHoverColor() { return borderHoverColor; }
		private final Binding<Integer> borderActiveColor = new Binding<>(0xFF777777);
		public Binding<Integer> borderActiveColor() { return borderActiveColor; }
		private final Binding<Integer> borderDisabledColor = new Binding<>(0xFF555555);
		public Binding<Integer> borderDisabledColor() { return borderDisabledColor; }

		private final Binding<Integer> textColor = new Binding<>(0xFFFFFFFF);
		public Binding<Integer> textColor() { return textColor; }
		private final Binding<Integer> textHoverColor = new Binding<>(0xFFFFFFFF);
		public Binding<Integer> textHoverColor() { return textHoverColor; }
		private final Binding<Integer> textActiveColor = new Binding<>(0xFFFFFFFF);
		public Binding<Integer> textActiveColor() { return textActiveColor; }
		private final Binding<Integer> textDisabledColor = new Binding<>(0xFFAAAAAA);
		public Binding<Integer> textDisabledColor() { return textDisabledColor; }

		private final Binding<Integer> textShadowColor = new Binding<>(0xFF000000);
		public Binding<Integer> textShadowColor() { return textShadowColor; }
		private final Binding<Integer> textShadowHoverColor = new Binding<>(0xFF000000);
		public Binding<Integer> textShadowHoverColor() { return textShadowHoverColor; }
		private final Binding<Integer> textShadowActiveColor = new Binding<>(0xFF000000);
		public Binding<Integer> textShadowActiveColor() { return textShadowActiveColor; }
		private final Binding<Integer> textShadowDisabledColor = new Binding<>(0xFFAAAAAA);
		public Binding<Integer> textShadowDisabledColor() { return textShadowDisabledColor; }




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
