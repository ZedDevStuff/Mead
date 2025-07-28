package dev.zeddevstuff.mead.styling;

import net.minecraft.util.Tuple;

import java.util.List;

public class MeadStyleRule
{
	public final TargetType targetType;
	public final String target;
	public final MeadStyleProperty[] properties;

	public MeadStyleRule(TargetType targetType, String target, List<MeadStyleProperty> properties)
	{
		this.targetType = targetType;
		this.target = target;
		this.properties = properties.toArray(new MeadStyleProperty[0]);
	}

	public enum TargetType
	{
		TAG,
		STYLE
	}

	public record MeadStyleProperty(String name, String value) {}
}
