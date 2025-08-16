package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.ElementState;
import dev.zeddevstuff.mead.core.data.ObservableProperty;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.parsing.IStringParser;
import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.HashMap;

public interface IHasColorProperties
{
	ColorProperties colorProps();
	ColorProperties hoverColorProps();
	ColorProperties activeColorProps();
	ColorProperties disabledColorProps();

	public class ColorProperties
	{
		private final ObservableProperty<Integer> backgroundColor = new ObservableProperty<>(0xFFFFFFFF);
		public ObservableProperty<Integer> backgroundColor() { return backgroundColor; }

		private final ObservableProperty<Integer> borderColor = new ObservableProperty<>(0xFF000000);
		public ObservableProperty<Integer> borderColor() { return borderColor; }

		private final ObservableProperty<Integer> textColor = new ObservableProperty<>(0xFFFFFFFF);
		public ObservableProperty<Integer> textColor() { return textColor; }

		private final ObservableProperty<Integer> textShadowColor = new ObservableProperty<>(0xFF000000);
		public ObservableProperty<Integer> textShadowColor() { return textShadowColor; }

	}

	static void applyAttributes(IHasColorProperties element, HashMap<String, String> attributes)
	{
		if (attributes == null || element == null) return;
		NullUtils.ifNotNull(attributes.get("background-color"), color -> {
			element.colorProps().backgroundColor.set(IStringParser.COLOR_PARSER.parse(color));
		});

		NullUtils.ifNotNull(attributes.get("border-color"), color -> {
			element.colorProps().borderColor.set(IStringParser.COLOR_PARSER.parse(color));
		});

		NullUtils.ifNotNull(attributes.get("text-color"), color -> {
			element.colorProps().textColor.set(IStringParser.COLOR_PARSER.parse(color));
		});

		NullUtils.ifNotNull(attributes.get("text-shadow-color"), color -> {
			element.colorProps().textShadowColor.set(IStringParser.COLOR_PARSER.parse(color));
		});
	}

	static void applyStyleRule(MeadStyleRule rule, MeadStyleRule.MeadStyleProperty prop, MeadElement target)
	{
		if(target instanceof IHasColorProperties el)
		{
			var colors = switch (rule.state)
			{
				case NORMAL -> el.colorProps();
				case HOVER -> el.hoverColorProps();
				case ACTIVE -> el.activeColorProps();
				case DISABLED -> el.disabledColorProps();
			};
			switch (prop.name())
			{
				case "background-color" ->
				{
					colors.backgroundColor()
						.set(IStringParser.COLOR_PARSER.parse(prop.value()));
					if(rule.state == ElementState.NORMAL)
					{
						el.hoverColorProps().backgroundColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
						el.activeColorProps().backgroundColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
						el.disabledColorProps().backgroundColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
					}
				}
				case "border-color" ->
				{
					colors.borderColor()
						.set(IStringParser.COLOR_PARSER.parse(prop.value()));
					if(rule.state == ElementState.NORMAL)
					{
						el.hoverColorProps().borderColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
						el.activeColorProps().borderColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
						el.disabledColorProps().borderColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
					}
				}
				case "color" ->
				{
					colors.textColor()
						.set(IStringParser.COLOR_PARSER.parse(prop.value()));
					if(rule.state == ElementState.NORMAL)
					{
						el.hoverColorProps().textColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
						el.activeColorProps().textColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
						el.disabledColorProps().textColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
					}
				}
				case "text-shadow" ->
				{
					colors.textShadowColor()
						.set(IStringParser.COLOR_PARSER.parse(prop.value()));
					if(rule.state == ElementState.NORMAL)
					{
						el.hoverColorProps().textShadowColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
						el.activeColorProps().textShadowColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
						el.disabledColorProps().textShadowColor()
							.set(IStringParser.COLOR_PARSER.parse(prop.value()));
					}
				}
			}
		}
	}

	default int getBackgroundColor(boolean isEnabled, boolean isHovered, boolean isFocused)
	{
		if (!isEnabled) return disabledColorProps().backgroundColor.get();
		if(isFocused) return activeColorProps().backgroundColor.get();
		if (isHovered) return hoverColorProps().backgroundColor.get();
		return colorProps().backgroundColor.get();
	}
	default int getBackgroundColor(ElementState state)
	{
		return switch (state)
		{
			case NORMAL -> colorProps().backgroundColor.get();
			case HOVER -> hoverColorProps().backgroundColor.get();
			case ACTIVE -> activeColorProps().backgroundColor.get();
			case DISABLED -> disabledColorProps().backgroundColor.get();
		};
	}

	default int getBorderColor(boolean isEnabled, boolean isHovered, boolean isFocused)
	{
		if (!isEnabled) return disabledColorProps().borderColor.get();
		if(isFocused) return activeColorProps().borderColor.get();
		if (isHovered) return hoverColorProps().borderColor.get();
		return colorProps().borderColor.get();
	}
	default int getBorderColor(ElementState state)
	{
		return switch (state)
		{
			case NORMAL -> colorProps().borderColor.get();
			case HOVER -> hoverColorProps().borderColor.get();
			case ACTIVE -> activeColorProps().borderColor.get();
			case DISABLED -> disabledColorProps().borderColor.get();
		};
	}

	default int getTextColor(boolean isEnabled, boolean isHovered, boolean isFocused)
	{
		if (!isEnabled) return disabledColorProps().textColor.get();
		if(isFocused) return activeColorProps().textColor.get();
		if (isHovered) return hoverColorProps().textColor.get();
		return colorProps().backgroundColor.get();
	}
	default int getTextColor(ElementState state)
	{
		return switch (state)
		{
			case NORMAL -> colorProps().textColor.get();
			case HOVER -> hoverColorProps().textColor.get();
			case ACTIVE -> activeColorProps().textColor.get();
			case DISABLED -> disabledColorProps().textColor.get();
		};
	}

	default int getTextShadowColor(boolean isEnabled, boolean isHovered, boolean isFocused)
	{
		if (!isEnabled) return disabledColorProps().textShadowColor.get();
		if(isFocused) return activeColorProps().textShadowColor.get();
		if (isHovered) return hoverColorProps().textShadowColor.get();
		return colorProps().backgroundColor.get();
	}
	default int getTextShadowColor(ElementState state)
	{
		return switch (state)
		{
			case NORMAL -> colorProps().textShadowColor.get();
			case HOVER -> hoverColorProps().textShadowColor.get();
			case ACTIVE -> activeColorProps().textShadowColor.get();
			case DISABLED -> disabledColorProps().textShadowColor.get();
		};
	}
}
