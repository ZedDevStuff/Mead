package dev.zeddevstuff.mead;

import dev.zeddevstuff.mead.parsing.MeadParser;
import dev.zeddevstuff.mead.parsing.MeadStyleSheetsParser;

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
		MeadStyleSheetsParser parser = new MeadStyleSheetsParser();
		var el = parser.parse("""
		btn {
		    textColor: #FFFFFF;
		    backgroundColor: #ff0000;
		}
		""");
	}
}
