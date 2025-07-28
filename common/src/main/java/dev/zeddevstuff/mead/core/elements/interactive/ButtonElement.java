package dev.zeddevstuff.mead.core.elements.interactive;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.interfaces.IHasTextProperties;
import dev.zeddevstuff.mead.minecraft.widgets.ButtonMeadWidget;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class ButtonElement extends MeadElement implements IHasTextProperties
{
	@Override
	public String getTagName() { return "button"; }
	protected final TextProperties textProps = new TextProperties();
	public TextProperties textProps() { return textProps; }
	public Callable<?> onClick = () -> null;
	public ButtonElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		super(attributes, variables, actions);
		IHasTextProperties.applyTextProperties(this, attributes);
		NullUtils.ifNotNull(attributes.get("onClick"), onClick -> {
			NullUtils.ifNotNull(actions.get(onClick), action -> {
				this.onClick = action;
			});
		});
		NullUtils.ifNotNull(attributes.get("action"), actionName -> {
			NullUtils.ifNotNull(actions.get(actionName), action -> {
				this.onClick = action;
			});
		});
		textContent.addObserver(this::updateText);
		widget = new ButtonMeadWidget(this);
	}

	@Override
	public HashMap<String, String> sanitizeAttributes(HashMap<String, String> attributes)
	{
		NullUtils.ifNull(attributes.get("width"), () -> attributes.put("width", "100"));
		NullUtils.ifNull(attributes.get("height"), () -> attributes.put("height", "20"));
		return attributes;
	}

	private void updateText(String newText)
	{
		if(this.textProps.localized().get())
			this.textProps.text().set(Component.translatable(newText));
		else
			this.textProps.text().set(Component.literal(newText));
	}
}
