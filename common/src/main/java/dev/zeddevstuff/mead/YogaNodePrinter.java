package dev.zeddevstuff.mead;

import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaNode;

public class YogaNodePrinter
{
	public static String stringify(YogaNode node)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("YogaNode {");
		// Basic properties (i.e., width, height, etc.)
		sb.append(" width: ").append(node.getWidth()).append(", ");
		sb.append("height: ").append(node.getHeight()).append(", ");
		var marginLeft = node.getMargin(YogaEdge.LEFT);
		var marginTop = node.getMargin(YogaEdge.TOP);
		var marginRight = node.getMargin(YogaEdge.RIGHT);
		var marginBottom = node.getMargin(YogaEdge.BOTTOM);
		sb.append("margin: ").append(marginLeft).append(" ").append(marginTop)
		  .append(" ").append(marginRight).append(" ").append(marginBottom).append(", ");
		var paddingLeft = node.getPadding(YogaEdge.LEFT);
		var paddingTop = node.getPadding(YogaEdge.TOP);
		var paddingRight = node.getPadding(YogaEdge.RIGHT);
		var paddingBottom = node.getPadding(YogaEdge.BOTTOM);
		sb.append("padding: ").append(paddingLeft).append(" ").append(paddingTop)
		  .append(" ").append(paddingRight).append(" ").append(paddingBottom).append(", ");
		var borderLeft = node.getBorder(YogaEdge.LEFT);
		var borderTop = node.getBorder(YogaEdge.TOP);
		var borderRight = node.getBorder(YogaEdge.RIGHT);
		var borderBottom = node.getBorder(YogaEdge.BOTTOM);
		sb.append("border: ").append(borderLeft).append(" ").append(borderTop)
		  .append(" ").append(borderRight).append(" ").append(borderBottom).append(", ");
		// Flex properties
		sb.append("flexDirection: ").append(node.getFlexDirection()).append(", ");
		sb.append("flexGrow: ").append(node.getFlexGrow()).append(", ");
		sb.append("flexShrink: ").append(node.getFlexShrink()).append(", ");
		sb.append("flexBasis: ").append(node.getFlexBasis()).append(", ");
		sb.append("alignItems: ").append(node.getAlignItems()).append(", ");
		sb.append("alignSelf: ").append(node.getAlignSelf()).append(", ");
		sb.append("alignContent: ").append(node.getAlignContent()).append(", ");
		sb.append("positionType: ").append(node.getPositionType()).append(", ");

		return sb.toString();
	}
}
