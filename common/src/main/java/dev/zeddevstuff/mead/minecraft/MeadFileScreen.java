package dev.zeddevstuff.mead.minecraft;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.core.MeadDOM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class MeadFileScreen extends Screen
{
	private final MeadContext ctx;
	protected long start = 0;
	protected long end = 0;
	public long getCreationTime() { return end - start; }
	public float getCreationTimeMillis() { return (float) (end - start) / 1_000_000f; }
	protected MeadDOM dom;
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MeadFileScreen.class);
	private final Path screenPath;


	public MeadFileScreen(Path screen, MeadContext ctx)
	{
		super(Component.literal("MeadScreen"));
		this.ctx = ctx;
		screenPath = screen;
		start = System.nanoTime();
		if(screen.toFile().exists())
		{
			ctx.createParser().parse(tryReadResource(screen)).ifPresent(
				root -> this.dom = new MeadDOM(root)
			);
		}
		else LOGGER.error("Mead file does not exist: {}", screen);
		end = System.nanoTime();
        LOGGER.info("Created MeadFileScreen from file '{}' in {}ms", screen, getCreationTimeMillis());
	}
	public MeadFileScreen(Path screen, MeadContext ctx, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions) throws IOException
	{
		super(Component.literal("MeadScreen"));
		this.ctx = ctx;
		if(variables == null)
			variables = new HashMap<>();
		if(actions == null)
			actions = new HashMap<>();
		screenPath = screen;
		start = System.nanoTime();
		if(screen.toFile().exists())
		{
			ctx.createParser(variables, actions).parse(tryReadResource(screen)).ifPresent(
				root -> this.dom = new MeadDOM(root)
			);
		}
		else LOGGER.error("Mead file does not exist: {}", screen);
		end = System.nanoTime();
        LOGGER.info("Created MeadFileScreen from file '{}' in {}ms", screen, getCreationTimeMillis());
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

	private static String tryReadResource(Path file)
	{
		try
		{
			return java.nio.file.Files.readString(file);
		} catch (Exception e)
		{
			System.err.println("Failed to read Mead file: " + file);
			return "<Mead></Mead>"; // Fallback to an empty Mead XML structure
		}
	}
}
