package dev.zeddevstuff.mead.interfaces;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.ElementFlavor;
import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.HashMap;

public interface IHasFlavorProperty
{
    /**
     * Returns the flavor of the element.
     * @return The flavor of the element.
     */
    Binding<ElementFlavor> getFlavor();

    static void applyAttributes(IHasFlavorProperty element, HashMap<String, String> attributes)
    {
        if (attributes == null || element == null) return;
        NullUtils.ifNotNull(attributes.get("flavor"), flavor -> {
            element.getFlavor().set(IStringParser.FLAVOR_PARSER.parse(flavor));
        });
    }
}
