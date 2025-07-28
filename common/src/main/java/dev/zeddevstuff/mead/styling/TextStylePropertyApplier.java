package dev.zeddevstuff.mead.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.interfaces.IHasTextProperties;
import dev.zeddevstuff.mead.interfaces.IStringParser;

public class TextStylePropertyApplier implements IMeadStylePropertyApplier
{
	@Override
	public void applyStyleProperty(MeadStyleRule.MeadStyleProperty property, MeadElement target)
	{
		if(target instanceof IHasTextProperties el)
		{
			switch (property.name())
			{
				case "textColor" ->
				{
					if(el.textProps().textColor().wasModifiedOnceAfterCreation()) break;
					el.textProps().textColor().set(IStringParser.HEX_COLOR_PARSER.parse(property.value()));
				}
				case "textSize" ->
				{
					if(el.textProps().textSize().wasModifiedOnceAfterCreation()) break;
					el.textProps().textSize().set(IStringParser.INTEGER_PARSER.parse(property.value()));
				}
				case "textShadowColor" ->
				{
					if(el.textProps().textShadowColor().wasModifiedOnceAfterCreation()) break;
					el.textProps().textShadowColor().set(IStringParser.HEX_COLOR_PARSER.parse(property.value()));
				}
				case "textShadow" ->
				{
					if(el.textProps().textShadow().wasModifiedOnceAfterCreation()) break;
					el.textProps().textShadow().set(IStringParser.BOOLEAN_PARSER.parse(property.value()));
				}
				case "textScale" ->
				{
					if(el.textProps().textScale().wasModifiedOnceAfterCreation()) break;
					el.textProps().textScale().set(IStringParser.INTEGER_PARSER.parse(property.value()));
				}
			}
		}
	}
}
