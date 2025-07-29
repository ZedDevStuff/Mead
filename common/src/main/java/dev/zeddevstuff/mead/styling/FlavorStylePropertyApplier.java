package dev.zeddevstuff.mead.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.interfaces.IHasFlavorProperty;
import dev.zeddevstuff.mead.interfaces.IStringParser;

public class FlavorStylePropertyApplier implements IMeadStylePropertyApplier
{
    @Override
    public void applyStyleProperty(MeadStyleRule.MeadStyleProperty rule, MeadElement target)
    {
        if(target instanceof IHasFlavorProperty el)
        {
            if("flavor".equals(rule.name()))
            {
                if(el.getFlavor().wasModifiedOnceAfterCreation()) return;
                el.getFlavor().set(IStringParser.FLAVOR_PARSER.parse(rule.value()));
            }
        }
    }
}
