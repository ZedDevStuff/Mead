package dev.zeddevstuff.mead.utils;

import java.util.function.Consumer;

public class NullUtils
{
	public static <T> void ifNotNull(T object, Consumer<T> then)
	{
		if (object != null)
		{
			then.accept(object);
		}
	}

	public static <T> void ifNull(T object, Runnable then)
	{
		if (object == null)
		{
			then.run();
		}
	}
}
