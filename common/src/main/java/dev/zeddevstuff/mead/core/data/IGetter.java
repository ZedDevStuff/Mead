package dev.zeddevstuff.mead.core.data;

public interface IGetter<T>
{
	/**
	 * Returns the current value of the getter.
	 * @return The current value or null if any error occurs.
	 */
	T get();

	/**
	 * Returns the type of the value returned by this getter.
	 * @return The class of the value type.
	 */
	Class<T> getType();
}
