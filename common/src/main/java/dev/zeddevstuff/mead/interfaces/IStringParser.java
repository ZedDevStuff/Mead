package dev.zeddevstuff.mead.interfaces;

import dev.zeddevstuff.mead.core.ElementFlavor;
import net.minecraft.util.Tuple;
import org.appliedenergistics.yoga.*;
import org.appliedenergistics.yoga.style.StyleLength;
import org.appliedenergistics.yoga.style.StyleSizeLength;

public interface IStringParser<T>
{
	T parse(String input);

	IStringParser<String> STRING_PARSER = input -> input;
	IStringParser<Integer> INTEGER_PARSER = input -> {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return 0; // or throw an exception, or return a default value
		}
	};
	IStringParser<Long> LONG_PARSER = input -> {
		try {
			return Long.parseLong(input);
		} catch (NumberFormatException e) {
			return 0L; // or throw an exception, or return a default value
		}
	};
	IStringParser<Float> FLOAT_PARSER = input -> {
		try {
			return Float.parseFloat(input);
		} catch (NumberFormatException e) {
			return 0.0f; // or throw an exception, or return a default value
		}
	};
	IStringParser<Double> DOUBLE_PARSER = input -> {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException e) {
			return 0.0; // or throw an exception, or return a default value
		}
	};
	IStringParser<Boolean> BOOLEAN_PARSER = input -> {
		if (input == null) return false;
        return switch (input.toLowerCase())
        {
            case "true", "yes", "1" -> true;
            case "false", "no", "0" -> false;
            default -> false; // or throw an exception, or return a default value
        };
	};

	// region Yoga stuff
	IStringParser<Float> ASPECT_RATIO_PARSER = input -> {
		try {
			if (input.contains("/")) {
				String[] parts = input.split("/");
				if (parts.length == 2) {
					float width = Float.parseFloat(parts[0].trim());
					float height = Float.parseFloat(parts[1].trim());
					return width / height;
				}
			} else {
				return Float.parseFloat(input);
			}
		} catch (NumberFormatException e) {
			return 1.0f;
		}
		return 1.0f;
	};
	IStringParser<YogaValue> YOGA_VALUE_PARSER = input -> {
		try
		{
			if(input.endsWith("%"))
			{
				return YogaValue.percent(Float.parseFloat(input.substring(0, input.length() - 1)));
			}
			if(input.endsWith("px") || input.endsWith("dp"))
			{
				return YogaValue.point(Float.parseFloat(input.substring(0, input.length() - 2)));
			}
			if(input.equals("auto") || input.isEmpty())
			{
				return YogaValue.AUTO;
			}
			return YogaValue.point(Float.parseFloat(input));
		}
		catch (Exception e)
		{
			return YogaValue.ZERO;
		}
	};
	IStringParser<YogaBoxSizing> YOGA_BOX_SIZING_PARSER = input ->
	{
		if("border".equals(input)) return YogaBoxSizing.BORDER_BOX;
		else return YogaBoxSizing.CONTENT_BOX;
	};
	IStringParser<YogaFlexDirection> YOGA_FLEX_DIRECTION_PARSER = input -> switch (input)
	{
		case "row" -> YogaFlexDirection.ROW;
		case "column" -> YogaFlexDirection.COLUMN;
		case "row-reverse" -> YogaFlexDirection.ROW_REVERSE;
		case "column-reverse" -> YogaFlexDirection.COLUMN_REVERSE;
		default -> YogaFlexDirection.COLUMN; // Default to COLUMN if not recognized
	};
	IStringParser<StyleLength> STYLE_LENGTH_PARSER = input -> {
		try
		{
			if(input.endsWith("%"))
				return StyleLength.percent(Float.parseFloat(input.substring(0, input.length() - 1)));
			if(input.endsWith("px") || input.endsWith("dp") || input.endsWith("pt"))
				return StyleLength.points(Float.parseFloat(input.substring(0, input.length() - 2)));
			if(input.equals("auto") || input.isEmpty())
				return StyleLength.ofAuto();
			return StyleLength.points(Float.parseFloat(input));
		}
		catch (Exception e)
		{
			return StyleLength.undefined();
		}
	};
	IStringParser<StyleSizeLength> STYLE_SIZE_LENGTH_PARSER = input -> {
		try
		{
			if(input.endsWith("%"))
				return StyleSizeLength.percent(Float.parseFloat(input.substring(0, input.length() - 1)));
			if(input.endsWith("px") || input.endsWith("dp") || input.endsWith("pt"))
				return StyleSizeLength.points(Float.parseFloat(input.substring(0, input.length() - 2)));
			if(input.equals("auto") || input.isEmpty())
				return StyleSizeLength.AUTO;
			return switch (input)
			{
				case "stretch" -> StyleSizeLength.STRETCH;
				case "fit" -> StyleSizeLength.FIT_CONTENT;
				case "max" -> StyleSizeLength.MAX_CONTENT;
				default -> StyleSizeLength.points(Float.parseFloat(input));
			};
		}
		catch (Exception e)
		{
			return StyleSizeLength.UNDEFINED;
		}
	};
	IStringParser<YogaAlign> YOGA_ALIGN_PARSER = input -> switch (input.toLowerCase())
	{
		case "auto" -> YogaAlign.AUTO;
		case "baseline" -> YogaAlign.BASELINE;
		case "center" -> YogaAlign.CENTER;
		case "end" -> YogaAlign.FLEX_END;
		case "start" -> YogaAlign.FLEX_START;
		case "stretch" -> YogaAlign.STRETCH;
		case "space-around" -> YogaAlign.SPACE_AROUND;
		case "space-between" -> YogaAlign.SPACE_BETWEEN;
		case "space-evenly" -> YogaAlign.SPACE_EVENLY;
		default -> YogaAlign.FLEX_START; // Default to FLEX_START if not recognized
	};
	IStringParser<YogaJustify> YOGA_JUSTIFY_PARSER = input -> switch (input.toLowerCase())
	{
		case "start" -> YogaJustify.FLEX_START;
		case "end" -> YogaJustify.FLEX_END;
		case "center" -> YogaJustify.CENTER;
		case "space-between" -> YogaJustify.SPACE_BETWEEN;
		case "space-around" -> YogaJustify.SPACE_AROUND;
		case "space-evenly" -> YogaJustify.SPACE_EVENLY;
		default -> YogaJustify.FLEX_START; // Default to FLEX_START if not recognized
	};
	@SuppressWarnings("unchecked")
	IStringParser<Tuple<YogaEdge, StyleLength>[]> YOGA_EDGE_LENGTH_PARSER = input -> {

		String[] parts = input.split(",");
		if(parts.length == 1)
		{
			return new Tuple[]{
				new Tuple<>(YogaEdge.ALL, STYLE_LENGTH_PARSER.parse(parts[0].trim()))
			};
		}
		if(parts.length == 2)
		{
			var horizontal = STYLE_LENGTH_PARSER.parse(parts[0].trim());
			var vertical = STYLE_LENGTH_PARSER.parse(parts[1].trim());
			return new Tuple[]{
				new Tuple<>(YogaEdge.HORIZONTAL, horizontal),
				new Tuple<>(YogaEdge.VERTICAL, vertical)
			};
		}
		if(parts.length == 4)
		{
			var left = STYLE_LENGTH_PARSER.parse(parts[0].trim());
			var top = STYLE_LENGTH_PARSER.parse(parts[1].trim());
			var right = STYLE_LENGTH_PARSER.parse(parts[2].trim());
			var bottom = STYLE_LENGTH_PARSER.parse(parts[3].trim());
			return new Tuple[]{
				new Tuple<>(YogaEdge.LEFT, left),
				new Tuple<>(YogaEdge.TOP, top),
				new Tuple<>(YogaEdge.RIGHT, right),
				new Tuple<>(YogaEdge.BOTTOM, bottom)
			};
		}
		else
		{
			return new Tuple[0];
		}
	};
	@SuppressWarnings("unchecked")
	IStringParser<Tuple<YogaEdge, Float>[]> YOGA_BORDER_PARSER = input -> {
		String[] parts = input.split(",");
		if(parts.length == 1)
		{
			return new Tuple[]{
				new Tuple<>(YogaEdge.ALL, FLOAT_PARSER.parse(parts[0].trim()))
			};
		}
		if(parts.length == 2)
		{
			var horizontal = FLOAT_PARSER.parse(parts[0].trim());
			var vertical = FLOAT_PARSER.parse(parts[1].trim());
			return new Tuple[]{
				new Tuple<>(YogaEdge.HORIZONTAL, horizontal),
				new Tuple<>(YogaEdge.VERTICAL, vertical)
			};
		}
		if(parts.length == 4)
		{
			var left = FLOAT_PARSER.parse(parts[0].trim());
			var top = FLOAT_PARSER.parse(parts[1].trim());
			var right = FLOAT_PARSER.parse(parts[2].trim());
			var bottom = FLOAT_PARSER.parse(parts[3].trim());
			return new Tuple[]{
				new Tuple<>(YogaEdge.LEFT, left),
				new Tuple<>(YogaEdge.TOP, top),
				new Tuple<>(YogaEdge.RIGHT, right),
				new Tuple<>(YogaEdge.BOTTOM, bottom)
			};
		}
		else
		{
			return new Tuple[0];
		}
	};
	IStringParser<YogaPositionType> YOGA_POSITION_TYPE_PARSER = input -> switch (input.toLowerCase())
	{
		case "absolute" -> YogaPositionType.ABSOLUTE;
		case "relative" -> YogaPositionType.RELATIVE;
		case "static" -> YogaPositionType.STATIC;
		default -> YogaPositionType.RELATIVE; // Default to RELATIVE if not recognized
	};
	IStringParser<YogaOverflow> YOGA_OVERFLOW_PARSER = input -> switch (input.toLowerCase())
	{
		case "visible" -> YogaOverflow.VISIBLE;
		case "hidden" -> YogaOverflow.HIDDEN;
		case "scroll" -> YogaOverflow.SCROLL;
		default -> YogaOverflow.VISIBLE; // Default to VISIBLE if not recognized
	};
	IStringParser<YogaDisplay> YOGA_DISPLAY_PARSER = input -> switch (input.toLowerCase())
	{
		case "flex" -> YogaDisplay.FLEX;
		case "none" -> YogaDisplay.NONE;
		case "contents" -> YogaDisplay.CONTENTS;
		default -> YogaDisplay.FLEX; // Default to FLEX if not recognized
	};
	// endregion Yoga stuff

	IStringParser<ElementFlavor> FLAVOR_PARSER = input -> {
		if (input == null || input.isEmpty()) {
			return ElementFlavor.VANILLA; // Default to VANILLA if input is null or empty
		}
		try {
			return ElementFlavor.valueOf(input.toUpperCase());
		} catch (IllegalArgumentException e) {
			return ElementFlavor.VANILLA; // Default to VANILLA if parsing fails
		}
	};

	IStringParser<Integer> HEX_COLOR_PARSER = input -> {
		try {
			var sanitizedInput = input.trim().replace("#", "");
			if(sanitizedInput.length() != 3 && sanitizedInput.length() != 6 && sanitizedInput.length() != 8) {
				return 0xFFFFFFFF; // Default to white if not a valid hex color
			}
			if(sanitizedInput.length() == 3) {
				sanitizedInput = "" + sanitizedInput.charAt(0) + sanitizedInput.charAt(0) +
						sanitizedInput.charAt(1) + sanitizedInput.charAt(1) +
						sanitizedInput.charAt(2) + sanitizedInput.charAt(2); // Expand shorthand hex
			}
			if (sanitizedInput.length() == 6) {
				sanitizedInput = "FF" + sanitizedInput; // Add alpha channel if missing
			}
			long colorValue = Long.parseLong(sanitizedInput, 16);
			if(colorValue > Integer.MAX_VALUE) {
				colorValue -= 0x100000000L; // Adjust for negative values
			}
			return (int) colorValue; // Convert to int
		} catch (NumberFormatException e) {
			return 0xFFFFFFFF; // Default to white if parsing fails
		}
	};
	IStringParser<Integer> RGB_COLOR_PARSER = input -> {
		try {
			String[] rgbValues = input.replace("rgb(", "").replace(")", "").split(",");
			if (rgbValues.length != 3) {
				return 0xFFFFFFFF; // Default to white if not a valid RGB format
			}
			int r = Integer.parseInt(rgbValues[0].trim());
			int g = Integer.parseInt(rgbValues[1].trim());
			int b = Integer.parseInt(rgbValues[2].trim());
			return (r << 16) | (g << 8) | b; // Combine RGB values into a single integer
		} catch (NumberFormatException e) {
			return 0xFFFFFFFF; // Default to white if parsing fails
		}
	};
	IStringParser<Integer> RGBA_COLOR_PARSER = input -> {
		try {
			String[] rgbaValues = input.replace("rgba(", "").replace(")", "").split(",");
			if (rgbaValues.length != 4) {
				return 0xFFFFFFFF; // Default to white if not a valid RGBA format
			}
			int r = Integer.parseInt(rgbaValues[0].trim());
			int g = Integer.parseInt(rgbaValues[1].trim());
			int b = Integer.parseInt(rgbaValues[2].trim());
			int a = Integer.parseInt(rgbaValues[3].trim());
			return (a << 24) | (r << 16) | (g << 8) | b; // Combine RGBA values into a single integer
		} catch (NumberFormatException e) {
			return 0xFFFFFFFF; // Default to white if parsing fails
		}
	};
	IStringParser<Integer> ARGB_COLOR_PARSER = input -> {
		try {
			String[] argbValues = input.replace("argb(", "").replace(")", "").split(",");
			if (argbValues.length != 4) {
				return 0xFFFFFFFF; // Default to white if not a valid ARGB format
			}
			int a = Integer.parseInt(argbValues[0].trim());
			int r = Integer.parseInt(argbValues[1].trim());
			int g = Integer.parseInt(argbValues[2].trim());
			int b = Integer.parseInt(argbValues[3].trim());
			return (a << 24) | (r << 16) | (g << 8) | b; // Combine ARGB values into a single integer
		} catch (NumberFormatException e) {
			return 0xFFFFFFFF; // Default to white if parsing fails
		}
	};
	IStringParser<Integer> COLOR_PARSER = input -> {
		if(input.startsWith("#")) return HEX_COLOR_PARSER.parse(input);
		else if(input.startsWith("rgb(")) return RGB_COLOR_PARSER.parse(input);
		else if(input.startsWith("rgba(")) return RGBA_COLOR_PARSER.parse(input);
		else if(input.startsWith("argb(")) return ARGB_COLOR_PARSER.parse(input);
		else return INTEGER_PARSER.parse(input); // Fallback to integer parsing
	};
}
