package dev.zeddevstuff.mead.styling;

import dev.zeddevstuff.mead.core.Registries;
import dev.zeddevstuff.mead.core.elements.MeadElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeadStyle
{
	private final List<MeadStyleRule> rules;
	public List<MeadStyleRule> getRules()
	{
		return Collections.unmodifiableList(rules);
	}
	public MeadStyle()
	{
		this.rules = new ArrayList<>();
	}
	public MeadStyle(List<MeadStyleRule> rules)
	{
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
					Registries.stylePropertyAppliers.values()
						.forEach(applier -> applier.applyStyleProperty(property, element));
				}
			}
			else if(rule.targetType == MeadStyleRule.TargetType.STYLE && element.hasStyle(rule.target))
			{
				for(var property : rule.properties)
				{
					Registries.stylePropertyAppliers.values()
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
