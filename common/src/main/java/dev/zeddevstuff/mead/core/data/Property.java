package dev.zeddevstuff.mead.core.data;

public class Property<T>
{
	protected T value;
	public T get() { return value; }
	public void set(T value) { this.value = value; }

	public Property(T value)
	{
		this.value = value;
	}
}
