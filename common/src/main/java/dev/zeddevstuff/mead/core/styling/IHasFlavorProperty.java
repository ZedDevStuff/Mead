package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.data.ObservableProperty;
import dev.zeddevstuff.mead.core.ElementFlavor;
import dev.zeddevstuff.mead.core.parsing.IStringParser;
import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.HashMap;

public interface IHasFlavorProperty
{
    /**
     * Returns the flavor of the element.
     * @return The flavor of the element.
     */
    ObservableProperty<ElementFlavor> flavor();

    static void applyAttributes(IHasFlavorProperty element, HashMap<String, String> attributes)
    {
        if (attributes == null || element == null) return;
        NullUtils.ifNotNull(attributes.get("flavor"), flavor -> {
            element.flavor().set(IStringParser.FLAVOR_PARSER.parse(flavor));
        });
    }
}
