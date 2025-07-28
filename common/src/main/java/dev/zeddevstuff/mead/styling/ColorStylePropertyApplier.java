package dev.zeddevstuff.mead.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.interfaces.IHasColorProperties;
import dev.zeddevstuff.mead.interfaces.IStringParser;

public class ColorStylePropertyApplier implements IMeadStylePropertyApplier
{
	@Override
	public void applyStyleProperty(MeadStyleRule.MeadStyleProperty property, MeadElement target)
	{
		if(target instanceof IHasColorProperties el)
		{
			switch (property.name())
			{
				case "backgroundColor" ->
				{
					if(el.colorProps().borderColor().wasModifiedOnceAfterCreation()) break;
					el.colorProps().backgroundColor().set(IStringParser.HEX_COLOR_PARSER.parse(property.value()));
				}
				case "borderColor" ->
				{
					if(el.colorProps().borderColor().wasModifiedOnceAfterCreation()) break;
					el.colorProps().borderColor().set(IStringParser.HEX_COLOR_PARSER.parse(property.value()));
				}

			}
		}
	}
}
