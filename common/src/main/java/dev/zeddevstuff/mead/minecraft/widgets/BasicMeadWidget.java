package dev.zeddevstuff.mead.minecraft.widgets;

import dev.zeddevstuff.mead.core.elements.MeadElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/**
 * This widget is the base of all visual elements in Mead.
 * Ignore all vanilla methods for sizing and such.
 */
public class BasicMeadWidget extends AbstractWidget
{
	protected MeadElement meadElement;
	public BasicMeadWidget(MeadElement element)
	{
		super(0, 0, 0, 0, Component.literal(element.getClass().getSimpleName()));
		meadElement = element;
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f)
	{

	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
	{

	}
}
