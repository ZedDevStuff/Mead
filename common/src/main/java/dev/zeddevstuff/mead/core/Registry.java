package dev.zeddevstuff.mead.core;

import java.util.*;

public class Registry<T>
{
	private final Object key;
	private final HashMap<String, T> registry;
	private boolean isFrozen = false;
	public boolean isFrozen() { return isFrozen; }
	public void freeze(Object key)
	{
		if (this.key != null && !this.key.equals(key))
		{
			throw new IllegalStateException("Invalid key.");
		}
		this.isFrozen = true;
	}


	public Registry()
	{
		this.key = null;
		this.registry = new HashMap<>();
	}
	public Registry(Object key)
	{
		this.key = key;
		this.registry = new HashMap<>();
	}

	public void register(String name, T item)
	{
		if (isFrozen)
		{
			throw new IllegalStateException("Cannot register items after the registry has been frozen.");
		}
		if (registry.containsKey(name))
		{
			throw new IllegalArgumentException("Item with name '" + name + "' already exists in the registry.");
		}
		registry.put(name, item);
	}
	public T get(String name)
	{
		if (!registry.containsKey(name))
		{
			throw new IllegalArgumentException("Item with name '" + name + "' does not exist in the registry.");
		}
		return registry.get(name);
	}
	public Optional<T> getOptional(String name)
	{
		return Optional.ofNullable(registry.get(name));
	}
	public List<String> keys()
	{
		return List.copyOf(registry.keySet());
	}
	public List<T> values()
	{
		return List.copyOf(registry.values());
	}
	public Set<Map.Entry<String, T>> entries()
	{
		return Set.copyOf(registry.entrySet());
	}
	public boolean contains(String name)
	{
		return registry.containsKey(name);
	}
	public HashMap<String, T> toMap()
	{
		return new HashMap<>(registry);
	}

}
