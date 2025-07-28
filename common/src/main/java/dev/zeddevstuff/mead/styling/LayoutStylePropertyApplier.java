package dev.zeddevstuff.mead.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.interfaces.IStringParser;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaValue;

public class LayoutStylePropertyApplier implements IMeadStylePropertyApplier
{
	@Override
	public void applyStyleProperty(MeadStyleRule.MeadStyleProperty property, MeadElement target)
	{
		switch (property.name())
		{
			case "width" ->
			{
				if(target.getNode().getWidth() != YogaValue.UNDEFINED) break;
				target.getNode().setWidth(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));
			}
			case "height" ->
			{
				if(target.getNode().getHeight() != YogaValue.UNDEFINED) break;
				target.getNode().setHeight(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));
			}
			case "margin" ->
			{
				if(target.getNode().getMargin(YogaEdge.ALL) != YogaValue.UNDEFINED) break;
				var margin = IStringParser.YOGA_EDGE_LENGTH_PARSER.parse(property.value());
				for (var el : margin)
					target.getNode().setMargin(el.getA(), el.getB());
			}
			case "padding" ->
			{
				if(target.getNode().getPadding(YogaEdge.ALL) != YogaValue.UNDEFINED) break;
				var padding = IStringParser.YOGA_EDGE_LENGTH_PARSER.parse(property.value());
				for (var el : padding)
					target.getNode().setPadding(el.getA(), el.getB());
			}
			case "border" ->
			{
				if(!Float.isNaN(target.getNode().getBorder(YogaEdge.ALL))) break;
				var border = IStringParser.YOGA_BORDER_PARSER.parse(property.value());
				for (var el : border)
					target.getNode().setBorder(el.getA(), el.getB());
			}
			case "flexDirection" ->
			{
				target.getNode().setFlexDirection(IStringParser.YOGA_FLEX_DIRECTION_PARSER.parse(property.value()));
			}
			case "alignItems" ->
			{
				target.getNode().setAlignItems(IStringParser.YOGA_ALIGN_PARSER.parse(property.value()));
			}
			case "alignSelf" ->
			{
				target.getNode().setAlignSelf(IStringParser.YOGA_ALIGN_PARSER.parse(property.value()));
			}
			case "justifyContent" ->
			{
				target.getNode().setJustifyContent(IStringParser.YOGA_JUSTIFY_PARSER.parse(property.value()));
			}
			case "flexGrow" ->
			{
				if(!Float.isNaN(target.getNode().getFlexGrow())) break;
				target.getNode().setFlexGrow(IStringParser.FLOAT_PARSER.parse(property.value()));
			}
			case "flexShrink" ->
			{
				if(!Float.isNaN(target.getNode().getFlexShrink())) break;
				target.getNode().setFlexShrink(IStringParser.FLOAT_PARSER.parse(property.value()));
			}
			case "flexBasis" ->
			{
				if(target.getNode().getFlexBasis() != YogaValue.UNDEFINED) break;
				target.getNode().setFlexBasis(IStringParser.FLOAT_PARSER.parse(property.value()));
			}
			case "position" ->
			{
				target.getNode().setPositionType(IStringParser.YOGA_POSITION_TYPE_PARSER.parse(property.value()));
			}
			case "left" ->
			{
				if(target.getNode().getPosition(YogaEdge.LEFT) != YogaValue.UNDEFINED) break;
				target.getNode().setPosition(YogaEdge.LEFT, IStringParser.STYLE_LENGTH_PARSER.parse(property.value()));
			}
			case "top" ->
			{
				if(target.getNode().getPosition(YogaEdge.TOP) != YogaValue.UNDEFINED) break;
				target.getNode().setPosition(YogaEdge.TOP, IStringParser.STYLE_LENGTH_PARSER.parse(property.value()));
			}
			case "right" ->
			{
				if(target.getNode().getPosition(YogaEdge.RIGHT) != YogaValue.UNDEFINED) break;
				target.getNode().setPosition(YogaEdge.RIGHT, IStringParser.STYLE_LENGTH_PARSER.parse(property.value()));
			}
			case "bottom" ->
			{
				if(target.getNode().getPosition(YogaEdge.BOTTOM) != YogaValue.UNDEFINED) break;
				target.getNode().setPosition(YogaEdge.BOTTOM, IStringParser.STYLE_LENGTH_PARSER.parse(property.value()));
			}
			case "aspectRatio" ->
			{
				target.getNode().setAspectRatio(IStringParser.ASPECT_RATIO_PARSER.parse(property.value()));
			}

		}
	}
}
