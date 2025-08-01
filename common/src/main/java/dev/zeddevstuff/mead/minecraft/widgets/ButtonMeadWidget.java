package dev.zeddevstuff.mead.minecraft.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zeddevstuff.mead.core.ElementFlavor;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.elements.interactive.ButtonElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

import java.util.function.Supplier;

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
		this.setPosition(buttonElement.getLayout().x, buttonElement.getLayout().y);
		this.setSize(buttonElement.getLayout().width, buttonElement.getLayout().height);
		Minecraft minecraft = Minecraft.getInstance();
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		if(buttonElement.getFlavor().get() == ElementFlavor.VANILLA)
		{
			guiGraphics.blitSprite(SPRITES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
		else
		{
			if(buttonElement.getNode().getBoxSizing() == YogaBoxSizing.BORDER_BOX)
			{
				if (buttonElement.getLayout().width > 0 || buttonElement.getLayout().height > 0)
					guiGraphics.fill(
						buttonElement.getLayout().x, buttonElement.getLayout().y,
						buttonElement.getLayout().x + buttonElement.getLayout().width, buttonElement.getLayout().y + buttonElement.getLayout().height,
						buttonElement.colorProps().getBorderColor(isActive(), isHovered(), isFocused()));
				guiGraphics.fill(
					buttonElement.getLayout().innerX, buttonElement.getLayout().innerY,
					buttonElement.getLayout().innerX + buttonElement.getLayout().innerWidth, buttonElement.getLayout().innerY + buttonElement.getLayout().innerHeight,
					buttonElement.colorProps().getBackgroundColor(isActive(), isHovered(), isFocused()));
			}
			else
			{
				var x = buttonElement.getLayout().x - buttonElement.getLayout().borderLeft;
				var y = buttonElement.getLayout().y - buttonElement.getLayout().borderTop;
				var innerX = buttonElement.getLayout().innerX - buttonElement.getLayout().borderLeft;
				var innerY = buttonElement.getLayout().innerY - buttonElement.getLayout().borderTop;
				if (buttonElement.getLayout().width > 0 || buttonElement.getLayout().height > 0)
					guiGraphics.fill(
						x, y,
						x + buttonElement.getLayout().width, y + buttonElement.getLayout().height,
						buttonElement.colorProps().getBorderColor(isActive(), isHovered(), isFocused()));
				guiGraphics.fill(
					innerX, innerY,
					innerX + buttonElement.getLayout().innerWidth, innerY + buttonElement.getLayout().innerHeight,
					buttonElement.colorProps().getBackgroundColor(isActive(), isHovered(), isFocused()));
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
