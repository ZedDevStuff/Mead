package dev.zeddevstuff.mead.core.data;

import org.burningwave.core.assembler.StaticComponentContainer;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ReflectedProperty extends Property<Object>
{
	protected Function<Object, Object> getMethod;
	protected BiConsumer<Object, Object> setMethod;

	public ReflectedProperty(Object source, String propertyPath)
	{
		super(source);
		setPropertyPath(propertyPath);
	}
	public Object getSource() { return this.value; }
	public void setSource(Object source)
	{
		this.value = source;
		setPropertyPath("");
	}
	protected Object targetParent;
	protected String propertyPath;
	public String getPropertyPath() { return propertyPath; }
	public void setPropertyPath(String path)
	{
		this.propertyPath = path;
		var propertyPathParts = path.split("\\.");
		if(this.value == null) return;
		Class<?> clazz;
		Object target = this.value;
		for(int i = 0; i < propertyPathParts.length; i++)
		{
			var part = propertyPathParts[i];
			try
			{
				clazz = target.getClass();
				if(part.endsWith("()"))
				{
					var name = part.replace("()", "");
					var method = clazz.getMethod(name);
					method.setAccessible(true);
					if(i == propertyPathParts.length - 1)
					{
						this.getMethod = (t) ->
						{
							try
							{
								return method.invoke(t);
							}
							catch (Exception e)
							{
								return null;
							}
						};
						this.setMethod = null;
						this.targetParent = target;
					}
					else target = method.invoke(target);
				}
				else
				{
					var field = clazz.getField(part);
					field.setAccessible(true);
					if(i == propertyPathParts.length - 1)
					{
						this.getMethod = (t) ->
						{
							try
							{
								return field.get(t);
							}
							catch (Exception e)
							{
								return null;
							}
						};
						this.setMethod = (t, v) ->
						{
							try
							{
								field.set(t, v);
							}
							catch (Exception ignored) {}
						};
						this.targetParent = target;
					}
					else target = field.get(target);
				}
			}
			catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
			{
				this.propertyPath = "";
				this.getMethod = null;
				this.setMethod = null;
			}

		}
	}

	@Override
	public Object get()
	{
		if(getMethod != null && targetParent != null)
			return getMethod.apply(targetParent);
		else
			return null;
	}

	@Override
	public void set(Object value)
	{
		if(setMethod != null && targetParent != null)
			setMethod.accept(targetParent, value);
	}

	static
	{
		StaticComponentContainer.Modules.exportAllToAll();
	}
}
