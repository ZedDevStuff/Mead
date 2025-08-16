package dev.zeddevstuff.mead.core.minecraft.widgets;

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
import org.appliedenergistics.yoga.style.StyleSizeLength;

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
			var width = meadElement.layout().width().get();
			var height = meadElement.layout().height().get();
			if(width == StyleSizeLength.AUTO)
				meadElement.layout().width().set(StyleSizeLength.points(font.width(this.getMessage())));
			if(height == StyleSizeLength.AUTO)
				meadElement.layout().height().set(StyleSizeLength.points(font.lineHeight));
		});
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
		Component component = textElement.textProps().text().get();
		int width = textElement.computedLayout().innerWidth;
		int fontWidth = font.width(component);
		int x = textElement.computedLayout().innerX + Math.round(this.alignX * (float)(width - fontWidth));
		int y = textElement.computedLayout().innerY + (textElement.computedLayout().innerHeight - 9) / 2;
		FormattedCharSequence formattedCharSequence = fontWidth > width ? this.clipText(component, width) : component.getVisualOrderText();
		guiGraphics.drawString(this.font, formattedCharSequence, x, y, textElement.getTextColor(isActive(), isHovered(), isFocused()));
	}

	private FormattedCharSequence clipText(Component component, int i)
	{
		Font font = this.font;
		FormattedText formattedText = font.substrByWidth(component, i - font.width(CommonComponents.ELLIPSIS));
		return Language.getInstance().getVisualOrder(FormattedText.composite(formattedText, CommonComponents.ELLIPSIS));
	}
}
