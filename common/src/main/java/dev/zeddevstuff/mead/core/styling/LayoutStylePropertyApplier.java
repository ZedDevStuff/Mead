package dev.zeddevstuff.mead.core.styling;

import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.core.parsing.IStringParser;
import org.appliedenergistics.yoga.YogaEdge;

public class LayoutStylePropertyApplier implements IMeadStylePropertyApplier
{
	@Override
	public void applyStyleProperty(MeadStyleRule rule, MeadStyleRule.MeadStyleProperty property, MeadElement target)
	{
		switch (property.name())
		{
			case "align-content" -> target.layout()
				.alignContent()
				.set(IStringParser.YOGA_ALIGN_PARSER.parse(property.value()));
			case "align-items" -> target.layout()
				.alignItems()
				.set(IStringParser.YOGA_ALIGN_PARSER.parse(property.value()));
			case "align-self" -> target.layout()
				.alignSelf()
				.set(IStringParser.YOGA_ALIGN_PARSER.parse(property.value()));
			case "aspect-ratio" -> target.layout()
				.aspectRatio()
				.set(IStringParser.ASPECT_RATIO_PARSER.parse(property.value()));

			case "border" ->
			{
				var border = IStringParser.YOGA_BORDER_PARSER.parse(property.value());
				for (var el : border)
				{
					switch (el.getA())
					{
						case ALL ->
						{
							target.layout().borderLeft().set(el.getB());
							target.layout().borderTop().set(el.getB());
							target.layout().borderRight().set(el.getB());
							target.layout().borderBottom().set(el.getB());
						}
						case HORIZONTAL ->
						{
							target.layout().borderLeft().set(el.getB());
							target.layout().borderRight().set(el.getB());
						}
						case VERTICAL ->
						{
							target.layout().borderTop().set(el.getB());
							target.layout().borderBottom().set(el.getB());
						}
						case YogaEdge.LEFT -> target.layout().borderLeft().set(el.getB());
						case YogaEdge.TOP -> target.layout().borderTop().set(el.getB());
						case YogaEdge.RIGHT -> target.layout().borderRight().set(el.getB());
						case YogaEdge.BOTTOM -> target.layout().borderBottom().set(el.getB());
					}
				}
			}
			case "box-sizing" -> target.layout()
				.boxSizing()
				.set(IStringParser.YOGA_BOX_SIZING_PARSER.parse(property.value()));

			case "width" -> target.layout()
				.width()
				.set(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));
			case "height" -> target.layout()
				.height()
				.set(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));

			case "direction" -> target.layout()
				.direction()
				.set(IStringParser.YOGA_DIRECTION_PARSER.parse(property.value()));
			case "display" -> target.layout()
				.display()
				.set(IStringParser.YOGA_DISPLAY_PARSER.parse(property.value()));

			case "flex" ->
			{
				var basis = 0f;
				var grow = 0f;
				var shrink = 0f;
			}
			case "flex-basis" -> target.layout()
				.flexBasis()
				.set(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));
			case "flex-direction" -> target.layout()
				.flexDirection()
				.set(IStringParser.YOGA_FLEX_DIRECTION_PARSER.parse(property.value()));
			case "flex-grow" -> target.layout()
				.flexGrow()
				.set(IStringParser.FLOAT_PARSER.parse(property.value()));
			case "flex-shrink" -> target.layout()
				.flexShrink()
				.set(IStringParser.FLOAT_PARSER.parse(property.value()));
			case "flex-wrap" -> target.layout()
				.flexWrap()
				.set(IStringParser.FLEX_WRAP_PARSER.parse(property.value()));

			case "gap" ->
			{
				var values = IStringParser.YOGA_GAP_PARSER.parse(property.value());
				if(values.length == 0) return;
				target.layout()
					.rowGap()
					.set(values[0]);
				target.layout()
					.columnGap()
					.set(values[1]);
			}

			case "justify-content" -> target.layout()
				.justifyContent()
				.set(IStringParser.YOGA_JUSTIFY_PARSER.parse(property.value()));


			case "margin" ->
			{
				var margin = IStringParser.YOGA_EDGE_LENGTH_PARSER.parse(property.value());
				for (var el : margin)
				{
					switch (el.getA())
					{
						case ALL ->
						{
							target.layout().marginLeft().set(el.getB());
							target.layout().marginTop().set(el.getB());
							target.layout().marginRight().set(el.getB());
							target.layout().marginBottom().set(el.getB());
						}
						case HORIZONTAL ->
						{
							target.layout().marginLeft().set(el.getB());
							target.layout().marginRight().set(el.getB());
						}
						case VERTICAL ->
						{
							target.layout().marginTop().set(el.getB());
							target.layout().marginBottom().set(el.getB());
						}
						case YogaEdge.LEFT -> target.layout().marginLeft().set(el.getB());
						case YogaEdge.TOP -> target.layout().marginTop().set(el.getB());
						case YogaEdge.RIGHT -> target.layout().marginRight().set(el.getB());
						case YogaEdge.BOTTOM -> target.layout().marginBottom().set(el.getB());
					}
				}
			}
			case "max-width" -> target.layout()
				.maxWidth()
				.set(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));
			case "max-height" -> target.layout()
				.maxHeight()
				.set(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));
			case "min-width" -> target.layout()
				.minWidth()
				.set(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));
			case "min-height" -> target.layout()
				.minHeight()
				.set(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(property.value()));

			case "overflow" -> target.layout()
				.overflow()
				.set(IStringParser.YOGA_OVERFLOW_PARSER.parse(property.value()));

			case "padding" ->
			{
				var padding = IStringParser.YOGA_EDGE_LENGTH_PARSER.parse(property.value());
				for (var el : padding)
				{
					switch (el.getA())
					{
						case ALL ->
						{
							target.layout().paddingLeft().set(el.getB());
							target.layout().paddingTop().set(el.getB());
							target.layout().paddingRight().set(el.getB());
							target.layout().paddingBottom().set(el.getB());
						}
						case HORIZONTAL ->
						{
							target.layout().paddingLeft().set(el.getB());
							target.layout().paddingRight().set(el.getB());
						}
						case VERTICAL ->
						{
							target.layout().paddingTop().set(el.getB());
							target.layout().paddingBottom().set(el.getB());
						}
						case YogaEdge.LEFT -> target.layout().paddingLeft().set(el.getB());
						case YogaEdge.TOP -> target.layout().paddingTop().set(el.getB());
						case YogaEdge.RIGHT -> target.layout().paddingRight().set(el.getB());
						case YogaEdge.BOTTOM -> target.layout().paddingBottom().set(el.getB());
					}
				}
			}

			case "left" -> target.layout()
				.left()
				.set(IStringParser.STYLE_LENGTH_PARSER.parse(property.value()));
			case "top" -> target.layout()
				.top()
				.set(IStringParser.STYLE_LENGTH_PARSER.parse(property.value()));
			case "right" -> target.layout()
				.right()
				.set(IStringParser.STYLE_LENGTH_PARSER.parse(property.value()));
			case "bottom" -> target.layout()
				.bottom()
				.set(IStringParser.STYLE_LENGTH_PARSER.parse(property.value()));

			case "position" -> target.layout()
				.positionType()
				.set(IStringParser.YOGA_POSITION_TYPE_PARSER.parse(property.value()));
		}
	}
}
