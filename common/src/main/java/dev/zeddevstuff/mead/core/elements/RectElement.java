package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.minecraft.widgets.RectMeadWidget;
import dev.zeddevstuff.mead.core.styling.IHasColorProperties;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class RectElement extends MeadElement implements IHasColorProperties
{
	@Override
	public String getTagName() { return "rect"; }

	protected final ColorProperties colorProps = new ColorProperties();
	public ColorProperties colorProps() { return colorProps; }
	protected final ColorProperties hoverColorProps = new ColorProperties();
	public ColorProperties hoverColorProps() { return hoverColorProps; }
	protected final ColorProperties activeColorProps = new ColorProperties();
	public ColorProperties activeColorProps() { return activeColorProps; }
	protected final ColorProperties disabledColorProps = new ColorProperties();
	public ColorProperties disabledColorProps() { return disabledColorProps; }

	public RectElement(HashMap<String, String> attributes, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(attributes, actions, textContent);
		IHasColorProperties.applyAttributes(this, attributes);
		widget = new RectMeadWidget(this);
	}
}
