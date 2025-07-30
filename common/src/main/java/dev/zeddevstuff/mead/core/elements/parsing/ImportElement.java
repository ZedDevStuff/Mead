package dev.zeddevstuff.mead.core.elements.parsing;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class ImportElement extends MeadElement implements IParsingCompleteListener
{
	@Override
	public String getTagName() { return "null"; }
	public ImportElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(null, null, null, "");
	}

	@Override
	public AbstractWidget getWidget()
	{
		return null;
	}

	public void parsingComplete(Void ignored)
	{

	}
}
