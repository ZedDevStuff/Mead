package dev.zeddevstuff.mead.core.data;

import java.util.ArrayList;

public class ObservableProperty<T> extends Property<T> implements Cloneable
{
	private final ArrayList<IObserver<T>> observers = new ArrayList<>();
	private boolean wasModifiedOnceAfterCreation = false;
	public boolean wasModifiedOnceAfterCreation()
	{
		return wasModifiedOnceAfterCreation;
	}
	private boolean wasModifiedTwiceAfterCreation = false;
	public boolean wasModifiedTwiceAfterCreation()
	{
		return wasModifiedTwiceAfterCreation;
	}

	public ObservableProperty(T initialValue)
	{
		super(initialValue);
	}

	@Override
	public void set(T value)
	{
		if(value == null && this.value == null)
			return;
		if(value != null && value.equals(this.value))
			return;
		super.set(value);
		if(wasModifiedOnceAfterCreation && !wasModifiedTwiceAfterCreation)
		{
			wasModifiedTwiceAfterCreation = true;
		}
		else if(!wasModifiedOnceAfterCreation)
		{
			wasModifiedOnceAfterCreation = true;
		}
		notifyObservers();
	}

	private void notifyObservers()
	{
		for (IObserver<T> observer : observers)
			observer.onChange(value);
	}

	/**
	 * Adds an observer to the binding.
	 */
	public void addObserver(IObserver<T> observer)
	{
		observers.add(observer);
	}
	/**
	 * Removes an observer from the binding.
	 */
	public void removeObserver(IObserver<T> observer)
	{
		observers.remove(observer);
	}

	/**
	 * Copies all observers from this binding to the target binding.
	 */
	public void copyObserversTo(ObservableProperty<T> target)
	{
		for (IObserver<T> observer : observers)
			target.addObserver(observer);
	}

	/**
	 * Copies all observers from the source binding to this binding.
	 */
	public void copyObserversFrom(ObservableProperty<T> source)
	{
		for (IObserver<T> observer : source.observers)
			this.addObserver(observer);
	}

	/**
	 * Migrates (copy then clear) all observers from this binding to the target binding.
	 */
	public void migrateObserversTo(ObservableProperty<T> target)
	{
		this.copyObserversTo(target);
		this.clearObservers();
	}

	/**
	 * Migrates (copy then clear) all observers from the source binding to this binding.
	 */
	public void migrateObserversFrom(ObservableProperty<T> source)
	{
		this.copyObserversFrom(source);
		source.clearObservers();
	}
	/**
	 * Clears all observers from this binding.
	 */
	public void clearObservers()
	{
		observers.clear();
	}

	@SuppressWarnings("unckecked")
    public ObservableProperty<T> clone()
	{
		try
		{
			long start = System.nanoTime();
			var clone = (ObservableProperty<T>) super.clone();
			long end = System.nanoTime();
			System.out.println("Cloned Observable in " + (end - start) + " ns");
			return clone;
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException("Failed to clone Observable", e);
		}
	}

    public interface IObserver<T>
	{
		void onChange(T newValue);
	}

	@Override
	public String toString()
	{
		return "Observable{" +
				"value=" + value +
				", observers=" + observers.size() +
				'}';
	}
}
