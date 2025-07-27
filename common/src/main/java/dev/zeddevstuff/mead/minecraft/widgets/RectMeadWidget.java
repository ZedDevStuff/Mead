package dev.zeddevstuff.mead.minecraft.widgets;

import dev.zeddevstuff.mead.core.elements.RectElement;
import net.minecraft.client.gui.GuiGraphics;

public class RectMeadWidget extends BasicMeadWidget
{
	public RectMeadWidget(RectElement meadElement)
	{
		super(meadElement);
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta)
	{
		if (!(meadElement instanceof RectElement div))
			return;
		var x = div.getX();
		var borderAwareX = div.getTrueX();
		var y = div.getY();
		var borderAwareY = div.getTrueY();
		var width = div.getWidth();
		var borderAwareWidth = div.getTrueWidth();
		var height = div.getHeight();
		var borderAwareHeight = div.getHeightWithBorders();
		if(borderAwareWidth > 0 || borderAwareHeight > 0)
			guiGraphics.fill(x, y, x + borderAwareWidth, y + borderAwareHeight, div.colorProps().borderColor.get());
		guiGraphics.fill(borderAwareX, borderAwareY, borderAwareX + width, borderAwareY + height, div.colorProps().backgroundColor.get());
	}
}
