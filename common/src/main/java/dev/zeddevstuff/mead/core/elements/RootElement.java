package dev.zeddevstuff.mead.core.elements;

import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.Mead;
import dev.zeddevstuff.mead.core.ElementFlavor;
import dev.zeddevstuff.mead.core.data.ObservableProperty;
import dev.zeddevstuff.mead.core.data.Property;
import dev.zeddevstuff.mead.core.elements.parsing.IParsingCompleteListener;
import dev.zeddevstuff.mead.core.minecraft.widgets.BasicMeadWidget;
import dev.zeddevstuff.mead.core.parsing.IStringParser;
import dev.zeddevstuff.mead.core.styling.IHasFlavorProperty;
import dev.zeddevstuff.mead.utils.NullUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class RootElement extends MeadElement implements IHasFlavorProperty, IParsingCompleteListener
{
	@Override
	public String getTagName() { return "element"; }

	private final ObservableProperty<ElementFlavor> flavor = new ObservableProperty<>(ElementFlavor.STYLED);
	public ObservableProperty<ElementFlavor> flavor() { return flavor; }

	public RootElement(HashMap<String, String> attributes, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(attributes, actions, textContent);
		NullUtils.ifNotNull(attributes.get("flavor"), attr -> {
			flavor.set(IStringParser.FLAVOR_PARSER.parse(attr));
		});
		widget = new BasicMeadWidget(this);
	}

	@Override
	public void parsingComplete()
	{
		LogUtils.getLogger().info("Root element flavor: {}", flavor.get());
		setFlavor(flavor.get());
	}
}
