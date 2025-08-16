package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;

import java.util.HashMap;

public class ColorStylePropertyApplier implements IMeadStylePropertyApplier
{
	@Override
	public void applyStyleProperty(MeadStyleRule rule, MeadStyleRule.MeadStyleProperty prop, MeadElement target)
	{
		IHasColorProperties.applyStyleRule(rule, prop, target);
	}
}
