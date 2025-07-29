package dev.zeddevstuff.mead.minecraft;

import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.core.MeadDOM;
import dev.zeddevstuff.mead.core.Binding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class MeadScreen extends BaseMeadScreen
{
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MeadScreen.class);
	public MeadScreen(ResourceLocation screen, MeadContext ctx)
	{
		super(tryReadResource(screen), ctx, null, null);
	}

	public MeadScreen(ResourceLocation screen, MeadContext ctx, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		super(tryReadResource(screen), ctx, variables, actions);
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

    private static String tryReadResource(ResourceLocation resourceLocation)
	{
		var res = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
		if(res.isPresent())
		{
			try (InputStream inputStream = res.get().open())
			{
				return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			} catch (Exception ignored)
			{
				LOGGER.error("Failed to read Mead resource: {}", resourceLocation);
				return "<Mead></Mead>"; // Fallback to an empty Mead XML structure
			}
		}
		else
		{
            LOGGER.error("Failed to read Mead resource: {}", resourceLocation);
			return "<Mead></Mead>"; // Fallback to an empty Mead XML structure
		}
	}
}
