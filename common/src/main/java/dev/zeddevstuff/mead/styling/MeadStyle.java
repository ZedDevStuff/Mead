package dev.zeddevstuff.mead.styling;

import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeadStyle
{
	private final MeadContext ctx;
	private final List<MeadStyleRule> rules;
	public List<MeadStyleRule> getRules()
	{
		return Collections.unmodifiableList(rules);
	}
	public MeadStyle(@NotNull MeadContext ctx)
	{
		this.ctx = ctx;
		this.rules = new ArrayList<>();
	}
	public MeadStyle(@NotNull MeadContext ctx, List<MeadStyleRule> rules)
	{
		this.ctx = ctx;
		this.rules = new ArrayList<>(rules);
	}

	public void addRule(MeadStyleRule rule)
	{
		if (rule == null)
		{
			throw new IllegalArgumentException("Rule cannot be null.");
		}
		rules.add(rule);
	}
	// This isn't by any means the most efficient way to apply styles, but it is simple and works
	public void applyTo(MeadElement element)
	{
		if(element == null) return;
		for (var rule : rules)
		{
			if(rule.targetType == MeadStyleRule.TargetType.TAG && rule.target.equals(element.getTagName()))
			{
				for(var property : rule.properties)
				{
					ctx.stylePropertyAppliers.values()
						.forEach(applier -> applier.applyStyleProperty(property, element));
				}
			}
			else if(rule.targetType == MeadStyleRule.TargetType.STYLE && element.hasStyle(rule.target))
			{
				for(var property : rule.properties)
				{
					ctx.stylePropertyAppliers.values()
						.forEach(applier -> applier.applyStyleProperty(property, element));
				}
			}
		}
	}
	public void applyToTree(MeadElement element)
	{
		if (element == null) return;
		applyTo(element);
		for (MeadElement child : element.getChildren())
		{
			applyToTree(child);
		}
	}
}
