package dev.zeddevstuff.mead.minecraft;

import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.core.MeadDOM;
import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.parsing.MeadParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.concurrent.Callable;

public abstract class MeadScreen extends Screen
{
	private static final Logger LOGGER = LogUtils.getLogger();
	private final MeadContext ctx;
	protected long start = 0;
	protected long end = 0;
	public long getCreationTime() { return end - start; }
	public float getCreationTimeMillis() { return (float) (end - start) / 1_000_000f; }
	protected MeadDOM dom;
	public MeadScreen(String path, MeadContext ctx, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		super(Component.literal("MeadScreen"));
		this.ctx = ctx;
		if(variables == null)
			variables = new HashMap<>();
		if(actions == null)
			actions = new HashMap<>();

		start = System.nanoTime();
		var intermediary = ctx.getIntermediaryDOM(path);
		if(intermediary.isPresent())
		{
			this.dom = new MeadDOM(intermediary.get().build(ctx, variables, actions));
		}
		else this.dom = new MeadDOM(null);
		end = System.nanoTime();
		LOGGER.info("Created MeadScreen from cache '{}' in {}ms", path, getCreationTimeMillis());
		resize(Minecraft.getInstance(), Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
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
}
