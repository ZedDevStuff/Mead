package dev.zeddevstuff.mead.core.data;

import dev.zeddevstuff.mead.utils.SafeReflection;

public class ReflectedBinding implements IGetter<Object>, ISetter<Object>
{
	private final Object source;
	public String targetPath = null;

	public ReflectedBinding(Object source)
	{
		this.source = source;
	}

	@Override
	public Object get()
	{
		if(targetPath == null || targetPath.isEmpty())
			return source;
		String[] pathParts = targetPath.split("\\.");
		Object source = this.source;
		for(String part : pathParts)
		{
			if (source == null) return null;
			SafeReflection.getField(source.getClass(), part);
		}

		return null;
	}

	@Override
	public void set(Object value)
	{

	}

	@Override
	public Class<Object> getType()
	{
		return null;
	}
}
