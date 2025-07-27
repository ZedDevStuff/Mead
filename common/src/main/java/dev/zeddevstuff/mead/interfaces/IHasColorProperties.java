package dev.zeddevstuff.mead.interfaces;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.HashMap;

public interface IHasColorProperties
{
	ColorProperties colorProps();

	public class ColorProperties
	{
		public Binding<Integer> backgroundColor = new Binding<>(0xFFFFFFFF);
		public Binding<Integer> borderColor = new Binding<>(0xFF000000);
	}

	static void applyAttributes(IHasColorProperties element, HashMap<String, String> attributes)
	{
		if (attributes == null || element == null) return;
		NullUtils.ifNotNull(attributes.get("backgroundColor"), color -> {
			element.colorProps().backgroundColor.set(IStringParser.HEX_COLOR_PARSER.parse(color));
		});
		NullUtils.ifNotNull(attributes.get("borderColor"), color -> {
			element.colorProps().borderColor.set(IStringParser.HEX_COLOR_PARSER.parse(color));
		});
	}
}
