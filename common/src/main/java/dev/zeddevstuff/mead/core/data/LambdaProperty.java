package dev.zeddevstuff.mead.core.data;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LambdaProperty<T> extends Property<T>
{
	private final Supplier<T> getter;
	private final Consumer<T> setter;
	public LambdaProperty(@NotNull Supplier<T> getter)
	{
		super(null);
		this.getter = getter;
		this.setter = null;
		this.readOnly = true;
	}
	public LambdaProperty(@NotNull Supplier<T> getter, @NotNull Consumer<T> setter)
	{
		super(null);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public T get()
	{
		return this.getter.get();
	}
	@Override
	public void set(T value)
	{
		if(!this.readOnly && setter != null)
			setter.accept(value);
	}
}
