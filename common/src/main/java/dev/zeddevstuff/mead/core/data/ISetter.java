package dev.zeddevstuff.mead.core.data;

public interface ISetter<T>
{
	/**
	 * Sets the value of the setter.
	 * @param value The new value to set.
	 */
	void set(T value);

	/**
	 * Returns the type of the value that can be set by this setter.
	 * @return The class of the value type.
	 */
	Class<T> getType();
}
