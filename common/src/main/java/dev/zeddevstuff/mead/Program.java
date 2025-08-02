package dev.zeddevstuff.mead;

import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.core.MeadContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Program
{
	/**
	 * The main entry point of the Mead program.
	 * This method is called when the program starts.
	 *
	 * @param args Command line arguments passed to the program.
	 */
	public static void main(String[] args) throws IOException
    {
		var ctx = new MeadContext("null", Mead.class);
		File file = new File("C:\\Users\\kouam\\Documents\\Minecraft Mods\\Mead\\common\\src\\main\\resources\\assets\\mead\\ui\\test.mead");
		String fileContent = Files.readString(file.toPath());
		var parsed = ctx.createParser().parse(fileContent);
	}
}
