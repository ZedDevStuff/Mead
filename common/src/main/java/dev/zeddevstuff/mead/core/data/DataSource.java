package dev.zeddevstuff.mead.core.data;

import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.HashMap;

public abstract class DataSource
{
	protected final HashMap<String, Property<?>> properties = new HashMap<>();

	/**
	 * Call {@code super} or {@link DataSource#registerProperties} in your constructor.
	 */
	protected DataSource()
	{
		registerProperties(properties);
	}

	protected abstract void registerProperties(HashMap<String, Property<?>> properties);

	public Object getValue(String path)
	{
		return properties.getOrDefault(path, Property.NULL).get();
	}
	public Property<?> getProperty(String path)
	{
		return properties.get(path);
	}

	public void setValue(String path, Object value)
	{
		NullUtils.ifNotNull(properties.get(path), prop -> {
			prop.setRaw(value);
		});
	}
}
