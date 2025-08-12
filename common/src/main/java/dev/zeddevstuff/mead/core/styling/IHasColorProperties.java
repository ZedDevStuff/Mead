package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.data.ObservableProperty;
import dev.zeddevstuff.mead.core.parsing.IStringParser;
import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.HashMap;

public interface IHasColorProperties
{
	ColorProperties colorProps();

	public class ColorProperties
	{
		private final ObservableProperty<Integer> backgroundColor = new ObservableProperty<>(0xFFFFFFFF);
		public ObservableProperty<Integer> backgroundColor() { return backgroundColor; }
		private final ObservableProperty<Integer> backgroundHoverColor = new ObservableProperty<>(0xFFCCCCCC);
		public ObservableProperty<Integer> backgroundHoverColor() { return backgroundHoverColor; }
		private final ObservableProperty<Integer> backgroundActiveColor = new ObservableProperty<>(0xFFBBBBBB);
		public ObservableProperty<Integer> backgroundActiveColor() { return backgroundActiveColor; }
		private final ObservableProperty<Integer> backgroundDisabledColor = new ObservableProperty<>(0xFFAAAAAA);
		public ObservableProperty<Integer> backgroundDisabledColor() { return backgroundDisabledColor; }

		private final ObservableProperty<Integer> borderColor = new ObservableProperty<>(0xFF000000);
		public ObservableProperty<Integer> borderColor() { return borderColor; }
		private final ObservableProperty<Integer> borderHoverColor = new ObservableProperty<>(0xFF888888);
		public ObservableProperty<Integer> borderHoverColor() { return borderHoverColor; }
		private final ObservableProperty<Integer> borderActiveColor = new ObservableProperty<>(0xFF777777);
		public ObservableProperty<Integer> borderActiveColor() { return borderActiveColor; }
		private final ObservableProperty<Integer> borderDisabledColor = new ObservableProperty<>(0xFF555555);
		public ObservableProperty<Integer> borderDisabledColor() { return borderDisabledColor; }

		private final ObservableProperty<Integer> textColor = new ObservableProperty<>(0xFFFFFFFF);
		public ObservableProperty<Integer> textColor() { return textColor; }
		private final ObservableProperty<Integer> textHoverColor = new ObservableProperty<>(0xFFFFFFFF);
		public ObservableProperty<Integer> textHoverColor() { return textHoverColor; }
		private final ObservableProperty<Integer> textActiveColor = new ObservableProperty<>(0xFFFFFFFF);
		public ObservableProperty<Integer> textActiveColor() { return textActiveColor; }
		private final ObservableProperty<Integer> textDisabledColor = new ObservableProperty<>(0xFFAAAAAA);
		public ObservableProperty<Integer> textDisabledColor() { return textDisabledColor; }

		private final ObservableProperty<Integer> textShadowColor = new ObservableProperty<>(0xFF000000);
		public ObservableProperty<Integer> textShadowColor() { return textShadowColor; }
		private final ObservableProperty<Integer> textShadowHoverColor = new ObservableProperty<>(0xFF000000);
		public ObservableProperty<Integer> textShadowHoverColor() { return textShadowHoverColor; }
		private final ObservableProperty<Integer> textShadowActiveColor = new ObservableProperty<>(0xFF000000);
		public ObservableProperty<Integer> textShadowActiveColor() { return textShadowActiveColor; }
		private final ObservableProperty<Integer> textShadowDisabledColor = new ObservableProperty<>(0xFFAAAAAA);
		public ObservableProperty<Integer> textShadowDisabledColor() { return textShadowDisabledColor; }




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
