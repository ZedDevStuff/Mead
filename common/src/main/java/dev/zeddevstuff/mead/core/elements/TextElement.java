package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.data.Property;
import dev.zeddevstuff.mead.core.styling.IHasColorProperties;
import dev.zeddevstuff.mead.core.styling.IHasTextProperties;
import dev.zeddevstuff.mead.core.parsing.IStringParser;
import dev.zeddevstuff.mead.core.minecraft.widgets.TextMeadWidget;
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
	protected final ColorProperties hoverColorProps = new ColorProperties();
	public ColorProperties hoverColorProps() { return hoverColorProps; }
	protected final ColorProperties activeColorProps = new ColorProperties();
	public ColorProperties activeColorProps() { return activeColorProps; }
	protected final ColorProperties disabledColorProps = new ColorProperties();
	public ColorProperties disabledColorProps() { return disabledColorProps; }

	protected final TextProperties textProps = new TextProperties();
	public TextProperties textProps() { return textProps; }

	public TextElement(HashMap<String, String> attributes, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(attributes, actions, textContent);
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
		if(yogaNode.getLayoutWidth() == 0 || Float.isNaN(yogaNode.getLayoutWidth()))
			yogaNode.setWidth(100);
		if(yogaNode.getLayoutWidth() == 0 || Float.isNaN(yogaNode.getLayoutWidth()))
			yogaNode.setHeight(20);
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
