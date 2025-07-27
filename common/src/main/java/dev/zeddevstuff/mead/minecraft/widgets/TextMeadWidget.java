package dev.zeddevstuff.mead.minecraft.widgets;

import dev.zeddevstuff.mead.core.elements.TextElement;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.appliedenergistics.yoga.YogaUnit;

public class TextMeadWidget extends BasicMeadWidget
{
	private Font font;
	private float alignX = 0.5F;

	public TextMeadWidget(TextElement element)
	{
		super(element);
		NullUtils.ifNotNull(Minecraft.getInstance(), minecraft ->
		{
			this.font = minecraft.font;
			var width = meadElement.getNode().getWidth();
			var height = meadElement.getNode().getHeight();
			if(width.unit == YogaUnit.AUTO)
				meadElement.getNode().setWidth(font.width(this.getMessage()));
			if(height.unit == YogaUnit.AUTO)
				meadElement.getNode().setHeight(font.lineHeight);
		});
	}

	public TextMeadWidget setColor(int i)
	{
		if(meadElement instanceof TextElement textElement)
		{
			textElement.textProps().textColor.set(i);
		}
		return this;
	}

	private TextMeadWidget horizontalAlignment(float f)
	{
		this.alignX = f;
		return this;
	}

	public TextMeadWidget alignLeft()
	{
		return this.horizontalAlignment(0.0F);
	}

	public TextMeadWidget alignCenter()
	{
		return this.horizontalAlignment(0.5F);
	}

	public TextMeadWidget alignRight()
	{
		return this.horizontalAlignment(1.0F);
	}

	@Override
	public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f)
	{
		if(!(meadElement instanceof TextElement textElement))
			return;
		Component component = textElement.textProps().text.get();
		int width = textElement.getWidth();
		int fontWidth = font.width(component);
		int x = textElement.getX() + Math.round(this.alignX * (float)(width - fontWidth));
		int y = textElement.getY() + ((int) textElement.getHeight() - 9) / 2;
		FormattedCharSequence formattedCharSequence = fontWidth > width ? this.clipText(component, width) : component.getVisualOrderText();
		guiGraphics.drawString(this.font, formattedCharSequence, x, y, textElement.textProps().textColor.get());
	}

	private FormattedCharSequence clipText(Component component, int i)
	{
		Font font = this.font;
		FormattedText formattedText = font.substrByWidth(component, i - font.width(CommonComponents.ELLIPSIS));
		return Language.getInstance().getVisualOrder(FormattedText.composite(formattedText, CommonComponents.ELLIPSIS));
	}
}
