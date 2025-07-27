package dev.zeddevstuff.mead.core;

import java.util.ArrayList;

public class Binding<T>
{
	private T value;
	private final ArrayList<IObserver<T>> observers = new ArrayList<>();

	public Binding(T initialValue)
	{
		this.value = initialValue;
	}

	public T get()
	{
		return value;
	}

	public void set(T newValue)
	{
		if (!value.equals(newValue)) {
			value = newValue;
			notifyObservers();
		}
	}

	private void notifyObservers()
	{
		for (IObserver<T> observer : observers)
			observer.onChange(value);
	}

	public void addObserver(IObserver<T> observer)
	{
		observers.add(observer);
	}
	public void removeObserver(IObserver<T> observer)
	{
		observers.remove(observer);
	}

	public interface IObserver<T>
	{
		void onChange(T newValue);
	}
}
