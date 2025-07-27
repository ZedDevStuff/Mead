package dev.zeddevstuff.mead.minecraft;

import dev.zeddevstuff.mead.core.MeadDOM;
import dev.zeddevstuff.mead.core.Binding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class MeadScreen extends BaseMeadScreen
{
	protected MeadDOM dom;
	public MeadScreen(ResourceLocation screen)
	{
		super(tryReadResource(screen), null, null);
	}

	public MeadScreen(ResourceLocation screen, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		super(tryReadResource(screen), variables, actions);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int i, int j, float f)
	{
		dom.calculateLayout();
		super.render(guiGraphics, i, j, f);
	}

	@Override
	protected void init()
	{
		dom.getAllElements().forEach(el -> addRenderableWidget(el.getWidget()));
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height)
	{
		this.width = width;
		this.height = height;
		dom.resize(width, height);
	}

	/**
	 * Overridden so widgets don't get rebuilt on every resize.
	 */
	@Override
	protected void repositionElements() {}

	private static String tryReadResource(ResourceLocation resourceLocation) {
		try (InputStream inputStream = Minecraft.getInstance().getResourceManager().getResource(resourceLocation).get().open()) {
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (Exception ignored) {
			return "<Mead></Mead>"; // Fallback to an empty Mead XML structure
		}
	}
}
