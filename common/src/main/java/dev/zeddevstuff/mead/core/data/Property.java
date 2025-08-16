package dev.zeddevstuff.mead.core.data;

public class Property<T>
{
	protected Class<T> clazz;
	public Class<T> getType() { return clazz; }
	protected T value;
	public T get() { return value; }
	@SuppressWarnings("unchecked")
	public void set(T value)
	{
		if(!readOnly)
		{
			this.value = value;
			if(clazz == null && value != null)
				clazz = (Class<T>) value.getClass();
		}
	}
	@SuppressWarnings("unchecked")
	public void setRaw(Object value)
	{
		if(!readOnly && value.getClass() == clazz)
		{
			set((T) value);
		}
	}
	protected boolean readOnly;

	@SuppressWarnings("unchecked")
	public Property(T value)
	{
		if(value != null)
			this.clazz = (Class<T>) value.getClass();
		this.value = value;
		this.readOnly = false;
	}
	@SuppressWarnings("unchecked")
	public Property(T value, boolean readOnly)
	{
		if(value != null)
			this.clazz = (Class<T>) value.getClass();
		this.value = value;
		this.readOnly = readOnly;
	}

	public static Property<Object> NULL = new Property<>(null, true);
}
