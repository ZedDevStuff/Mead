package dev.zeddevstuff.mead;

import dev.zeddevstuff.mead.core.MeadContext;

public final class Mead
{
	public static final String MOD_ID = "mead";
	private static MeadContext context;

	public static void init()
	{
		context = new MeadContext();
	}

	public static MeadContext ctx()
	{
		return context;
	}
}
