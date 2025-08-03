package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.data.Observable;
import dev.zeddevstuff.mead.minecraft.widgets.BasicMeadWidget;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class Element extends MeadElement
{
	@Override
	public String getTagName() { return "element"; }

	public Element(HashMap<String, String> attributes, HashMap<String, Observable<?>> variables, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(attributes, variables, actions, textContent);
		widget = new BasicMeadWidget(this);
	}
}
