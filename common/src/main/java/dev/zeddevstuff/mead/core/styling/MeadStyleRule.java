package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.ElementState;

import java.util.List;

public class MeadStyleRule
{
	public final TargetType targetType;
	public final String target;
	public final ElementState state;
	public final MeadStyleProperty[] properties;

	public MeadStyleRule(TargetType targetType, String target, ElementState state, List<MeadStyleProperty> properties)
	{
		this.targetType = targetType;
		this.target = target;
		this.state = state;
		this.properties = properties.toArray(new MeadStyleProperty[0]);
	}

	public enum TargetType
	{
		TAG,
		STYLE
	}

	public record MeadStyleProperty(String name, String value) {}
}
