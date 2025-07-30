package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.interfaces.*;
import dev.zeddevstuff.mead.minecraft.widgets.RectMeadWidget;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class RectElement extends MeadElement implements IHasColorProperties
{
	@Override
	public String getTagName() { return "rect"; }

	protected final ColorProperties colorProps = new ColorProperties();
	public ColorProperties colorProps() { return colorProps; }
	public RectElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(attributes, variables, actions, textContent);
		yogaNode.setFlexDirection(YogaFlexDirection.COLUMN);
		IHasColorProperties.applyAttributes(this, attributes);
		widget = new RectMeadWidget(this);
	}
}
