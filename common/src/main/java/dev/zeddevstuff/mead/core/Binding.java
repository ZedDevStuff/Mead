package dev.zeddevstuff.mead.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Binding<T>
{
	private T value;
	private final Class<T> type;
	private String targetMemberName = null;
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
	private static HashMap<Class<?>, HashMap<String, Function<Object, Object>>> memberGetters = null;
	private static HashMap<Class<?>, HashMap<String, BiConsumer<Object, Object>>> memberSetters = null;
	@SuppressWarnings("unchecked")
	public Binding(T initialValue)
	{
		this.value = initialValue;
		this.type = (Class<T>) initialValue.getClass();
		initializeMemberAccess(initialValue.getClass());
	}
	public Binding(T initialValue, String targetMemberName)
	{
		this(initialValue);
		this.targetMemberName = targetMemberName;
	}

	/**
	 * Returns the current value of the binding.
	 */
	@SuppressWarnings("unchecked")
	public <T1> T1 get()
	{
		if(targetMemberName == null || targetMemberName.isEmpty())
			return (T1) value;
		else
		{
			if (memberGetters.get(value.getClass()).containsKey(targetMemberName)) {
				return (T1) memberGetters.get(value.getClass()).get(targetMemberName).apply(value);
			}
			return null;
		}
	}

	/**
	 * Returns the value of a member of the bound object (field or getter method). If the member name is null or empty, the value of the binding itself is returned.
	 */
	public Optional<Object> getMember(String memberName)
	{
		if(memberName == null || memberName.isEmpty())
			return Optional.of(value);
		if (memberGetters.get(value.getClass()).containsKey(memberName)) {
			return Optional.of(memberGetters.get(value.getClass()).get(memberName).apply(value));
		}
		return Optional.empty();
	}
	/**
	 * Sets the value of the binding and notifies all observers.
	 * If the new value is the same as the current value, no notification is sent.
	 */
	@SuppressWarnings("unchecked")
	public boolean set(Object newValue)
	{
		if(targetMemberName == null || targetMemberName.isEmpty())
		{
			if (!value.equals(newValue) && newValue.getClass() == type)
			{
				if(wasModifiedOnceAfterCreation && !wasModifiedTwiceAfterCreation)
					wasModifiedTwiceAfterCreation = true;
				if(!wasModifiedOnceAfterCreation) wasModifiedOnceAfterCreation = true;

				value = (T) newValue;
				notifyObservers();
				return true;
			}
			else
				return false;
		}
		else
		{
			return setMember(targetMemberName, newValue);
		}
	}
	/**
	 * Sets the value of a member of the bound object (field or setter method). If the member name is null or empty, the value of the binding itself is set.
	 * @return true if the member was set successfully, false if no setter exists for the member or other error occurs.
	 */
	public boolean setMember(String memberName, Object newValue)
	{
		if(memberName == null || memberName.isEmpty() && newValue.getClass() == type)
			set( newValue);
		if (memberSetters.get(value.getClass()).containsKey(memberName)) {
			memberSetters.get(value.getClass()).get(memberName).accept(value, newValue);
			notifyObservers();
			return true;
		} else {
			return false;
		}
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
	public void copyObserversTo(Binding<T> target)
	{
		for (IObserver<T> observer : observers)
			target.addObserver(observer);
	}

	/**
	 * Copies all observers from the source binding to this binding.
	 */
	public void copyObserversFrom(Binding<T> source)
	{
		for (IObserver<T> observer : source.observers)
			this.addObserver(observer);
	}

	/**
	 * Migrates (copy then clear) all observers from this binding to the target binding.
	 */
	public void migrateObserversTo(Binding<T> target)
	{
		this.copyObserversTo(target);
		this.clearObservers();
	}

	/**
	 * Migrates (copy then clear) all observers from the source binding to this binding.
	 */
	public void migrateObserversFrom(Binding<T> source)
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

	public interface IObserver<T>
	{
		void onChange(T newValue);
	}

	@Override
	public String toString()
	{
		return "Binding{" +
				"value=" + value +
				", observers=" + observers.size() +
				'}';
	}

	// I hate how Java forces me to do a lot of stuff like this. I wish i was born in an alternate universe where Minecraft was made in C#
	static void initializeMemberAccess(Class<?> bindingClass)
	{
		if(memberGetters == null || memberSetters == null)
		{
			memberGetters = new HashMap<>();
			memberSetters = new HashMap<>();
		}
		if (memberGetters.containsKey(bindingClass) && memberSetters.containsKey(bindingClass))
		{
			return;
		}
		var getters = new HashMap<String, Function<Object, Object>>();
		var setters = new HashMap<String, BiConsumer<Object, Object>>();
		Arrays.stream(bindingClass.getFields())
			.filter(field -> field.getModifiers() == java.lang.reflect.Modifier.PUBLIC)
			.forEach(field ->
			{
				String fieldName = field.getName();
				getters.put(fieldName, obj -> {
					try {
						return field.get(obj);
					} catch (IllegalAccessException e) {
						// Throw an insignificant exception to avoid breaking the flow
						throw new RuntimeException("Failed to get field: " + fieldName, e);
					}
				});
				setters.put(fieldName, (obj, value) -> {
					try {
						field.set(obj, value);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("Failed to set field: " + fieldName, e);
					}
				});
			});
		Arrays.stream(bindingClass.getMethods())
			.filter(method -> method.getModifiers() == java.lang.reflect.Modifier.PUBLIC && method.getName().startsWith("get") && method.getParameterCount() == 0)
			.forEach(method ->
			{
				String methodName = method.getName().substring(3).toLowerCase();
				getters.put(methodName, obj -> {
					try {
						return method.invoke(obj);
					} catch (Exception e) {
						throw new RuntimeException("Failed to invoke getter: " + methodName, e);
					}
				});
			});
		Arrays.stream(bindingClass.getMethods())
			.filter(method -> method.getModifiers() == java.lang.reflect.Modifier.PUBLIC && method.getName().startsWith("set") && method.getParameterCount() == 1)
			.forEach(method ->
			{
				String methodName = method.getName().substring(3).toLowerCase();
				setters.put(methodName, (obj, value) -> {
					try {
						method.invoke(obj, value);
					} catch (Exception e) {
						throw new RuntimeException("Failed to invoke setter: " + methodName, e);
					}
				});
			});
		memberGetters.put(bindingClass, getters);
		memberSetters.put(bindingClass, setters);
	}
}
