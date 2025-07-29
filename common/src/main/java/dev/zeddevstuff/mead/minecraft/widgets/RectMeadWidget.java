package dev.zeddevstuff.mead.minecraft.widgets;

import dev.zeddevstuff.mead.core.elements.RectElement;
import net.minecraft.client.gui.GuiGraphics;
import org.appliedenergistics.yoga.YogaBoxSizing;

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
		if(div.getNode().getBoxSizing() == YogaBoxSizing.BORDER_BOX)
		{
			if (div.getLayout().width > 0 || div.getLayout().height > 0)
				guiGraphics.fill(div.getLayout().x, div.getLayout().y, div.getLayout().x + div.getLayout().width, div.getLayout().y + div.getLayout().height, div.colorProps().borderColor().get());
			guiGraphics.fill(div.getLayout().innerX, div.getLayout().innerY, div.getLayout().innerX + div.getLayout().innerWidth, div.getLayout().innerY + div.getLayout().innerHeight, div.colorProps().backgroundColor().get());
		}
		else
		{
			var x = div.getLayout().x - div.getLayout().borderLeft;
			var y = div.getLayout().y - div.getLayout().borderTop;
			var innerX = div.getLayout().innerX - div.getLayout().borderLeft;
			var innerY = div.getLayout().innerY - div.getLayout().borderTop;
			if (div.getLayout().width > 0 || div.getLayout().height > 0)
				guiGraphics.fill(x, y, x + div.getLayout().width, y + div.getLayout().height, div.colorProps().borderColor().get());
			guiGraphics.fill(innerX, innerY, innerX + div.getLayout().innerWidth, innerY + div.getLayout().innerHeight, div.colorProps().backgroundColor().get());
		}
	}
}
