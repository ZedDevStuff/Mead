package dev.zeddevstuff.mead.core.minecraft.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zeddevstuff.mead.core.ElementFlavor;
import dev.zeddevstuff.mead.core.elements.interactive.ButtonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.appliedenergistics.yoga.YogaBoxSizing;

public class ButtonMeadWidget extends BasicMeadWidget
{
	public ButtonMeadWidget(ButtonElement element)
	{
		super(element);
	}

	public static final int SMALL_WIDTH = 120;
	public static final int DEFAULT_WIDTH = 150;
	public static final int BIG_WIDTH = 200;
	public static final int DEFAULT_HEIGHT = 20;
	public static final int DEFAULT_SPACING = 8;
	protected static final Button.CreateNarration DEFAULT_NARRATION = supplier -> (MutableComponent)supplier.get();
	protected static final int TEXT_MARGIN = 2;
	private static final WidgetSprites SPRITES = new WidgetSprites(
		ResourceLocation.withDefaultNamespace("widget/button"),
		ResourceLocation.withDefaultNamespace("widget/button_disabled"),
		ResourceLocation.withDefaultNamespace("widget/button_highlighted")
	);

	public void onPress()
	{
		if(!(meadElement instanceof ButtonElement buttonElement))
			return;
		try
		{
			buttonElement.onClick.call();
		}
		catch (Exception ignored)
		{
			// I'll figure out later what to do with this
		}
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float deltaTime)
	{
		if(!(meadElement instanceof ButtonElement buttonElement))
			return;
		this.setMessage(buttonElement.textProps().text().get());
		this.setPosition(buttonElement.computedLayout().x, buttonElement.computedLayout().y);
		this.setSize(buttonElement.computedLayout().width, buttonElement.computedLayout().height);
		Minecraft minecraft = Minecraft.getInstance();
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		if(buttonElement.flavor().get() == ElementFlavor.VANILLA)
		{
			guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
		else
		{
			if(buttonElement.layout().boxSizing().get() == YogaBoxSizing.BORDER_BOX)
			{
				if (buttonElement.computedLayout().width > 0 || buttonElement.computedLayout().height > 0)
					guiGraphics.fill(
						buttonElement.computedLayout().x, buttonElement.computedLayout().y,
						buttonElement.computedLayout().x + buttonElement.computedLayout().width, buttonElement.computedLayout().y + buttonElement.computedLayout().height,
						buttonElement.getBorderColor(isActive(), isHovered(), isFocused()));
				guiGraphics.fill(
					buttonElement.computedLayout().innerX, buttonElement.computedLayout().innerY,
					buttonElement.computedLayout().innerX + buttonElement.computedLayout().innerWidth, buttonElement.computedLayout().innerY + buttonElement.computedLayout().innerHeight,
					buttonElement.getBackgroundColor(isActive(), isHovered(), isFocused()));
			}
			else
			{
				var x = buttonElement.computedLayout().x - buttonElement.computedLayout().borderLeft;
				var y = buttonElement.computedLayout().y - buttonElement.computedLayout().borderTop;
				var innerX = buttonElement.computedLayout().innerX - buttonElement.computedLayout().borderLeft;
				var innerY = buttonElement.computedLayout().innerY - buttonElement.computedLayout().borderTop;
				if (buttonElement.computedLayout().width > 0 || buttonElement.computedLayout().height > 0)
					guiGraphics.fill(
						x, y,
						x + buttonElement.computedLayout().width, y + buttonElement.computedLayout().height,
						buttonElement.getBorderColor(isActive(), isHovered(), isFocused()));
				guiGraphics.fill(
					innerX, innerY,
					innerX + buttonElement.computedLayout().innerWidth, innerY + buttonElement.computedLayout().innerHeight,
					buttonElement.getBackgroundColor(isActive(), isHovered(), isFocused()));
			}
		}
		
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		int k = this.active ? 16777215 : 10526880;
		this.renderString(guiGraphics, minecraft.font, k | Mth.ceil(this.alpha * 255.0F) << 24);
	}

	public void renderString(GuiGraphics guiGraphics, Font font, int i) {
		this.renderScrollingString(guiGraphics, font, 2, i);
	}

	@Override
	public void onClick(double d, double e) {
		this.onPress();
	}

	@Override
	public void onRelease(double d, double e)
	{
		setFocused(false);
	}

	@Override
	public void setFocused(boolean v)
	{
		super.setFocused(v);
		meadElement.setElementState(this.visible, this.isHovered, this.isFocused());
	}

	@Override
	public boolean keyPressed(int i, int j, int k) {
		if (!this.active || !this.visible) {
			return false;
		} else if (CommonInputs.selected(i)) {
			this.playDownSound(Minecraft.getInstance().getSoundManager());
			this.onPress();
			return true;
		} else {
			return false;
		}
	}
}
