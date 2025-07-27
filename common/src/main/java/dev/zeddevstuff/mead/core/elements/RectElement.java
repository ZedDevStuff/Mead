package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.interfaces.*;
import dev.zeddevstuff.mead.minecraft.widgets.RectMeadWidget;
import org.appliedenergistics.yoga.YogaFlexDirection;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class RectElement extends MeadElement implements IHasColorProperties
{
	protected final ColorProperties colorProps = new ColorProperties();
	public ColorProperties colorProps() { return colorProps; }
	public RectElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		super(attributes, variables, actions);
		yogaNode.setFlexDirection(YogaFlexDirection.COLUMN);
		IHasColorProperties.applyAttributes(this, attributes);
		widget = new RectMeadWidget(this);
	}
}
