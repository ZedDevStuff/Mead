package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.interfaces.IHasColorProperties;
import dev.zeddevstuff.mead.interfaces.IHasTextProperties;
import dev.zeddevstuff.mead.interfaces.IStringParser;
import dev.zeddevstuff.mead.minecraft.widgets.TextMeadWidget;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.network.chat.Component;
import org.appliedenergistics.yoga.YogaNodeType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class TextElement extends MeadElement implements IHasColorProperties, IHasTextProperties
{
	@Override
	public String getTagName() { return "text"; }

	protected final ColorProperties colorProps = new ColorProperties();
	public ColorProperties colorProps() { return colorProps; }
	protected final TextProperties textProps = new TextProperties();
	public TextProperties textProps() { return textProps; }

	public TextElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(attributes, variables, actions, textContent);
		IHasColorProperties.applyAttributes(this, attributes);
		IHasTextProperties.applyAttributes(this, attributes);
		yogaNode.setNodeType(YogaNodeType.TEXT);
		if(attributes == null)
			return;
		NullUtils.ifNotNull(attributes.get("localized"), localized ->
			this.textProps.localized().set(Boolean.parseBoolean(localized)));
		NullUtils.ifNotNull(attributes.get("text"), text -> {
			if(this.textProps.localized().get())
				this.textProps.text().set(Component.translatable(text));
			else
				this.textProps.text().set(Component.literal(text));
		});
		NullUtils.ifNotNull(attributes.get("color"), color ->
			this.colorProps.textColor().set(IStringParser.COLOR_PARSER.parse(color)));
		this.textContent.addObserver(this::updateText);
		if(!textContent.isBlank())
			this.updateText(textContent);
		if(getNode().getLayoutWidth() == 0 || Float.isNaN(getNode().getLayoutWidth()))
			getNode().setWidth(100);
		if(getNode().getLayoutWidth() == 0 || Float.isNaN(getNode().getLayoutWidth()))
			getNode().setHeight(20);
		widget = new TextMeadWidget(this);
	}

	private void updateText(String newText)
	{
		if(this.textProps.localized().get())
			this.textProps.text().set(Component.translatable(newText));
		else
			this.textProps.text().set(Component.literal(newText));
	}
}
