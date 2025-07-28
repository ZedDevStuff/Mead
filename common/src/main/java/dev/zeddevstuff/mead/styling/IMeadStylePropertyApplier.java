package dev.zeddevstuff.mead.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;

public interface IMeadStylePropertyApplier
{
	void applyStyleProperty(MeadStyleRule.MeadStyleProperty rule, MeadElement target);
}
