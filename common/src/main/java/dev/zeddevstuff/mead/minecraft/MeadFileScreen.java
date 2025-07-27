package dev.zeddevstuff.mead.minecraft;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.parsing.MeadParser;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class MeadFileScreen extends BaseMeadScreen
{
	private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MeadFileScreen.class);
	private MeadParser parser = new MeadParser();
	private Path screenPath;


	public MeadFileScreen(Path screen) throws IOException
	{
		super(tryReadResource(screen), null, null);
		screenPath = screen;
		if(screen.toFile().exists()) {
		} else {
			LOGGER.error("Mead file does not exist: {}", screen);
		}
		end = System.nanoTime();
		LOGGER.info("Created MeadFileScreen from file '" + screen + "' in " + getCreationTimeMillis() + "ms");
	}
	public MeadFileScreen(Path screen, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions) throws IOException
	{
		super(tryReadResource(screen), variables, actions);
		screenPath = screen;
		if(screen.toFile().exists()) {
		} else {
			LOGGER.error("Mead file does not exist: {}", screen);
		}
		end = System.nanoTime();
		LOGGER.info("Created MeadFileScreen from file '" + screen + "' in " + getCreationTimeMillis() + "ms");
	}

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
