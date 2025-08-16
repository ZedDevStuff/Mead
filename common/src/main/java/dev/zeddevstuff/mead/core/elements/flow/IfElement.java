package dev.zeddevstuff.mead.core.elements.flow;

import dev.zeddevstuff.mead.core.IntermediateDOM;
import dev.zeddevstuff.mead.core.data.ObservableProperty;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.elements.parsing.IParsingCompleteListener;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class IfElement extends MeadElement implements IParsingCompleteListener
{
	private ObservableProperty<Boolean> condition = new ObservableProperty<>(true);
	private IntermediateDOM.IntermediateElement snippet;

	@Override
	public String getTagName() { return "if"; }
	@SuppressWarnings("unchecked")
	public IfElement(HashMap<String, String> attributes, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(attributes, actions, textContent);
		NullUtils.ifNotNull(attributes.get("condition"), condition -> {
			if(!condition.isEmpty())
			{
				var cond = dataSource.getProperty(condition);
				if(cond instanceof ObservableProperty<?> && cond.getType() == Boolean.class)
				{
					this.condition.migrateObserversTo((ObservableProperty<Boolean>) cond);
					var previous = this.condition;
					this.condition = (ObservableProperty<Boolean>) cond;
					if(previous.get() != this.condition.get())
						valueChanged(this.condition.get());
				}
			}
		});
		condition.addObserver(this::valueChanged);
	}

	private void valueChanged(Boolean value)
	{
		if(value)
		{
			for(var element : snippet.getChildren())
			{
				addChild(element.build(ctx, null));
			}
		}
		else
		{
			clearChildren();
		}
	}

	@Override
	public AbstractWidget getWidget()
	{
		return children.isEmpty() ? null : children.getFirst().getWidget();
	}

	@Override
	public void parsingComplete()
	{

	}
}
