package dev.zeddevstuff.mead.core.elements.flow;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class IfElement extends MeadElement
{
	private Binding<Boolean> condition = new Binding<>(true); // Default to true if no condition is provided
	public IfElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		super(attributes, variables, actions);
		NullUtils.ifNotNull(attributes.get("condition"), condition -> {
			if(!condition.isEmpty())
			{
				var cond = variables.get(condition);
				if(cond != null && cond.get() instanceof Boolean)
				{
					try
					{
						this.condition = (Binding<Boolean>) cond;
						this.condition.addObserver(this::valueChanged);
					} catch (Exception e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		});
	}

	private void valueChanged(Boolean value)
	{
		if(value)
		{

		}
		else
		{

		}
	}

	@Override
	public AbstractWidget getWidget()
	{
		return children.isEmpty() ? null : children.getFirst().getWidget();
	}
}
