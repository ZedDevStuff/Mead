package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;

public interface IMeadStylePropertyApplier
{
	void applyStyleProperty(MeadStyleRule rule, MeadStyleRule.MeadStyleProperty prop, MeadElement target);
}
