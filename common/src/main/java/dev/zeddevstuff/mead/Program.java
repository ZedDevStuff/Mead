package dev.zeddevstuff.mead;

import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.parsing.*;

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
		var ctx = new MeadContext("null", Mead.class);
		MeadStyleSheetsParser parser = new MeadStyleSheetsParser(ctx);
		var el = parser.parse("""
		btn {
		    textColor: #FFFFFF;
		    backgroundColor: #ff0000;
		}
		""");
	}
}
