package dev.zeddevstuff.mead;

import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaGutter;
import org.appliedenergistics.yoga.YogaNode;

public class YogaNodePrinter
{
	public static String stringify(YogaNode node)
	{
		StringBuilder sb = new StringBuilder();
		var style = node.getStyle();
		sb.append("YogaNode {\n");
		// Basic properties (i.e., width, height, etc.)
		sb.append(" width: ").append(node.getWidth()).append(",\n ");
		sb.append("height: ").append(node.getHeight()).append(",\n ");
		sb.append("minWidth: ").append(node.getMinWidth()).append(",\n ");
		sb.append("minHeight: ").append(node.getMinHeight()).append(",\n ");
		sb.append("maxWidth: ").append(node.getMaxWidth()).append(",\n ");
		sb.append("maxHeight: ").append(node.getMaxHeight()).append(",\n ");
		var marginLeft = node.getMargin(YogaEdge.LEFT);
		var marginTop = node.getMargin(YogaEdge.TOP);
		var marginRight = node.getMargin(YogaEdge.RIGHT);
		var marginBottom = node.getMargin(YogaEdge.BOTTOM);
		sb.append("margin: ").append(marginLeft).append(" ").append(marginTop)
		  .append(" ").append(marginRight).append(" ").append(marginBottom).append(",\n ");
		var paddingLeft = node.getPadding(YogaEdge.LEFT);
		var paddingTop = node.getPadding(YogaEdge.TOP);
		var paddingRight = node.getPadding(YogaEdge.RIGHT);
		var paddingBottom = node.getPadding(YogaEdge.BOTTOM);
		sb.append("padding: ").append(paddingLeft).append(" ").append(paddingTop)
		  .append(" ").append(paddingRight).append(" ").append(paddingBottom).append(",\n ");
		var borderLeft = node.getBorder(YogaEdge.LEFT);
		var borderTop = node.getBorder(YogaEdge.TOP);
		var borderRight = node.getBorder(YogaEdge.RIGHT);
		var borderBottom = node.getBorder(YogaEdge.BOTTOM);
		sb.append("gap: ").append(node.getGap(YogaGutter.ROW)).append(" ")
		  .append(node.getGap(YogaGutter.COLUMN)).append(",\n ");
		sb.append("border: ").append(borderLeft).append(" ").append(borderTop)
		  .append(" ").append(borderRight).append(" ").append(borderBottom).append(",\n ");
		sb.append("boxSizing: ").append(node.getBoxSizing()).append(",\n ");
		sb.append("display: ").append(node.getDisplay()).append(",\n ");
		sb.append("flex: ").append(node.getFlex()).append(",\n ");
		sb.append("flexBasis: ").append(node.getFlexBasis()).append(",\n ");
		sb.append("flexDirection: ").append(node.getFlexDirection()).append(",\n ");
		sb.append("flexGrow: ").append(node.getFlexGrow()).append(",\n ");
		sb.append("flexShrink: ").append(node.getFlexShrink()).append(",\n ");
		sb.append("flexWrap: ").append(node.getWrap()).append(",\n ");
		sb.append("alignContent: ").append(node.getAlignContent()).append(",\n ");
		sb.append("alignItems: ").append(node.getAlignItems()).append(",\n ");
		sb.append("alignSelf: ").append(node.getAlignSelf()).append(",\n ");
		sb.append("aspectRatio: ").append(node.getAspectRatio()).append(",\n ");
		sb.append("justifyContent: ").append(node.getJustifyContent()).append(",\n ");
		sb.append("positionType: ").append(node.getPositionType()).append(",\n ");
		sb.append("position: [")
		  .append(node.getPosition(YogaEdge.LEFT)).append(", ")
		  .append(node.getPosition(YogaEdge.TOP)).append(", ")
		  .append(node.getPosition(YogaEdge.RIGHT)).append(", ")
		  .append(node.getPosition(YogaEdge.BOTTOM)).append("],\n ");

		return sb.toString();
	}
}
