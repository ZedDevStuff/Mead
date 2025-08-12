package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.parsing.IStringParser;

public class FlavorStylePropertyApplier implements IMeadStylePropertyApplier
{
    @Override
    public void applyStyleProperty(MeadStyleRule.MeadStyleProperty rule, MeadElement target)
    {
        if(target instanceof IHasFlavorProperty el)
        {
            if("flavor".equals(rule.name()))
            {
                if(el.flavor().wasModifiedOnceAfterCreation()) return;
                el.flavor().set(IStringParser.FLAVOR_PARSER.parse(rule.value()));
            }
        }
    }
}
