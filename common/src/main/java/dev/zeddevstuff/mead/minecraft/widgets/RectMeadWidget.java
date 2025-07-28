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
		if(div.getLayout().width > 0 || div.getLayout().height > 0)
			guiGraphics.fill(div.getLayout().x, div.getLayout().y, div.getLayout().x + div.getLayout().width, div.getLayout().y + div.getLayout().height, div.colorProps().borderColor().get());
		guiGraphics.fill(div.getLayout().innerX, div.getLayout().innerY, div.getLayout().innerX + div.getLayout().innerWidth, div.getLayout().innerY + div.getLayout().innerHeight, div.colorProps().backgroundColor().get());
	}
}
