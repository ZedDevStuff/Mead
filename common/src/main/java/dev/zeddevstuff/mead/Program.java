package dev.zeddevstuff.mead;

import dev.zeddevstuff.mead.parsing.MeadParser;

import java.io.Console;

public class Program
{
	/**
	 * The main entry point of the Mead program.
	 * This method is called when the program starts.
	 *
	 * @param args Command line arguments passed to the program.
	 */
	public static void main(String[] args)
	{
		MeadParser parser = new MeadParser();
		var el = parser.parse("""
		<Mead>
			<Text>Hello World!</Text>
		</Mead>
		""");
		if (el.isPresent())
		{
			System.out.println("Parsed successfully: " + el.get());
		}
		else
		{
			System.out.println("Failed to parse Mead XML.");
		}
	}
}
