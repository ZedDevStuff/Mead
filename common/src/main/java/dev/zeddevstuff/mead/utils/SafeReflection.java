package dev.zeddevstuff.mead.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public class SafeReflection
{
	public static Optional<Field> getField(Class<?> clazz, String fieldName)
	{
		try
		{
			Field field = clazz.getField(fieldName);
			field.setAccessible(true);
			return Optional.of(field);
		}
		catch (Exception e)
		{
			return Optional.empty();
		}
	}
	public static Optional<Field> getDeclaredField(Class<?> clazz, String fieldName)
	{
		try
		{
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return Optional.of(field);
		}
		catch (Exception e)
		{
			return Optional.empty();
		}
	}
	public static Optional<Method> getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes)
	{
		try
		{
			Method method = clazz.getMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return Optional.of(method);
		}
		catch (Exception e)
		{
			return Optional.empty();
		}
	}
	public static Optional<Method> getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes)
	{
		try
		{
			Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return Optional.of(method);
		}
		catch (Exception e)
		{
			return Optional.empty();
		}
	}

}
