package dev.zeddevstuff.mead.core.minecraft;

import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.core.MeadDOM;
import dev.zeddevstuff.mead.core.data.Property;
import dev.zeddevstuff.mead.core.parsing.MeadParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class MeadHotReloadScreen extends Screen
{
	private final MeadContext ctx;
	protected long start;
	protected long end;
	public long getCreationTime() { return end - start; }
	public float getCreationTimeMillis() { return (float) (end - start) / 1_000_000f; }
	protected MeadDOM dom;
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MeadHotReloadScreen.class);
	private final Path screenPath;

	private HashMap<String, Property<?>> variables = new HashMap<>();
	private HashMap<String, Callable<?>> actions = new HashMap<>();

	public MeadHotReloadScreen(Path screen, MeadContext ctx)
	{
		super(Component.literal("MeadScreen"));
		this.ctx = ctx;
		screenPath = screen;
		start = System.nanoTime();
		if(screen.toFile().exists())
		{
			var intermediary = MeadParser.parse(tryReadResource(screen));
            intermediary.ifPresentOrElse(
				intermediaryDOM -> this.dom = new MeadDOM(intermediaryDOM.build(ctx, null, null)),
				() -> this.dom = new MeadDOM(null));
		}
		else LOGGER.error("Mead file does not exist: {}", screen);
		end = System.nanoTime();
        LOGGER.info("Created MeadFileScreen from file '{}' in {}ms", screen, getCreationTimeMillis());
	}
	public MeadHotReloadScreen(Path screen, MeadContext ctx, HashMap<String, Property<?>> variables, HashMap<String, Callable<?>> actions) throws IOException
	{
		super(Component.literal("MeadScreen"));
		this.ctx = ctx;
		if(variables != null)
			this.variables = variables;
		if(actions != null)
			this.actions = actions;
		screenPath = screen;
		start = System.nanoTime();
		if(screen.toFile().exists())
		{
			var intermediary = MeadParser.parse(tryReadResource(screen));
            this.dom = intermediary.map(intermediaryDOM -> new MeadDOM(intermediaryDOM.build(ctx, variables, actions))).orElseGet(() -> new MeadDOM(null));
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

	public void reload()
	{
		var intermediary = MeadParser.parse(tryReadResource(screenPath));
        intermediary.ifPresent(intermediaryDOM -> this.dom = new MeadDOM(intermediaryDOM.build(ctx, variables, actions)));
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
		}
		catch (Exception e)
		{
			System.err.println("Failed to read Mead file: " + file);
			return "<Mead></Mead>"; // Fallback to an empty Mead XML structure
		}
	}
}
