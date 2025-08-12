package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;

import java.util.HashMap;

public class ColorStylePropertyApplier implements IMeadStylePropertyApplier
{
	@Override
	public void applyStyleProperty(MeadStyleRule.MeadStyleProperty property, MeadElement target)
	{
		if(target instanceof IHasColorProperties el)
		{
			IHasColorProperties.applyAttributes(el, new HashMap<>() {{
				put(property.name(), property.value());
			}});
		}
	}
}
