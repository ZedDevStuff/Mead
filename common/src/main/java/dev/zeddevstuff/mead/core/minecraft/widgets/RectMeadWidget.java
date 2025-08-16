package dev.zeddevstuff.mead.core.minecraft.widgets;

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
		if(div.layout().boxSizing().get() == YogaBoxSizing.BORDER_BOX)
		{
			if (div.computedLayout().width > 0 || div.computedLayout().height > 0)
				guiGraphics.fill(div.computedLayout().x, div.computedLayout().y, div.computedLayout().x + div.computedLayout().width, div.computedLayout().y + div.computedLayout().height, div.colorProps().borderColor().get());
			guiGraphics.fill(div.computedLayout().innerX, div.computedLayout().innerY, div.computedLayout().innerX + div.computedLayout().innerWidth, div.computedLayout().innerY + div.computedLayout().innerHeight, div.colorProps().backgroundColor().get());
		}
		else
		{
			var x = div.computedLayout().x - div.computedLayout().borderLeft;
			var y = div.computedLayout().y - div.computedLayout().borderTop;
			var innerX = div.computedLayout().innerX - div.computedLayout().borderLeft;
			var innerY = div.computedLayout().innerY - div.computedLayout().borderTop;
			if (div.computedLayout().width > 0 || div.computedLayout().height > 0)
				guiGraphics.fill(x, y, x + div.computedLayout().width, y + div.computedLayout().height, div.colorProps().borderColor().get());
			guiGraphics.fill(innerX, innerY, innerX + div.computedLayout().innerWidth, innerY + div.computedLayout().innerHeight, div.colorProps().backgroundColor().get());
		}
	}
}
