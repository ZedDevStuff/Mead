package dev.zeddevstuff.mead.core.elements.parsing;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class ImportElement extends MeadElement
{
	@Override
	public String getTagName() { return "null"; }
	public ImportElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		super(attributes, variables, actions);
	}

	@Override
	public AbstractWidget getWidget()
	{
		return null;
	}
}
