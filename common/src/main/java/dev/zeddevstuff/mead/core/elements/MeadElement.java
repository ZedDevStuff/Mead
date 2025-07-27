package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.elements.flow.IfElement;
import dev.zeddevstuff.mead.core.elements.interactive.ButtonElement;
import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.interfaces.IStringParser;
import dev.zeddevstuff.mead.parsing.MeadParser;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaNode;
import org.appliedenergistics.yoga.node.LayoutResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * The base class for all Mead elements.
 */
public abstract class MeadElement
{
	protected YogaNode yogaNode = new YogaNode();
	public YogaNode getNode() { return yogaNode; }
	public LayoutResults getLayout()
	{
		return yogaNode.getLayout();
	}

	protected MeadElement parent;
	public MeadElement getParent() { return parent; }
	public void setParent(MeadElement parent)
	{
		parent.getNode().addChildAt(yogaNode, parent.getNode().getLayoutChildCount());
		this.parent = parent;
	}
	protected ArrayList<MeadElement> children = new ArrayList<>();
	public ArrayList<MeadElement> getChildren() { return children; }
	public void setChildren(List<MeadElement> children)
	{
		for(MeadElement child : this.children)
		{
			parent.removeChild(child);
		}
		this.children.clear();
		for (MeadElement child : children)
		{
			addChild(child);
		}
	}
	public void addChild(MeadElement child)
	{
		children.add(child);
		child.setParent(this);
	}
	public void removeChild(MeadElement child)
	{
		yogaNode.removeChild(child.yogaNode);
		children.remove(child);
	}

	/**
	 * Returns the X position of the element.
	 * @return
	 */
	public int getX()
	{
		int x = (int) (yogaNode.getLayoutX());
		if (parent != null)
			x += parent.getX();
		return x;
	}
	/**
	 * Returns the X position of the element including borders.
	 * @return
	 */
	public int getTrueX()
	{
		int x = (int) (yogaNode.getLayoutX());
		x += (int) yogaNode.getLayoutBorder(YogaEdge.LEFT);
		if (parent != null)
			x += parent.getX();
		return x;
	}
	/**
	 * Returns the Y position of the element.
	 * @return
	 */
	public int getY()
	{
		int y = (int) (yogaNode.getLayoutY());
		if (parent != null)
			y += parent.getY();
		return y;
	}

	/**
	 * Returns the Y position of the element including borders.
	 * @return
	 */
	public int getTrueY()
	{
		int y = (int) (yogaNode.getLayoutY());
		y += (int) yogaNode.getLayoutBorder(YogaEdge.TOP);
		if (parent != null)
			y += parent.getY();
		return y;
	}
	/**
	 * Returns the width of the element.
	 * @return
	 */
	public int getWidth()
	{
		return (int) (yogaNode.getLayoutWidth());
	}

	/**
	 * Returns the width of the element including borders.
	 * @return
	 */
	public int getTrueWidth()
	{
		int width = (int) (yogaNode.getLayoutWidth());
		width += (int) yogaNode.getLayoutBorder(YogaEdge.LEFT);
		width += (int) yogaNode.getLayoutBorder(YogaEdge.RIGHT);
		return width;
	}
	/**
	 * Returns the height of the element.
	 * @return
	 */
	public int getHeight()
	{
		return (int) (yogaNode.getLayoutHeight());
	}
	/**
	 * Returns the height of the element including borders.
	 * @return
	 */
	public int getHeightWithBorders()
	{
		int height = (int) (yogaNode.getLayoutHeight());
		height += (int) yogaNode.getLayoutBorder(YogaEdge.TOP);
		height += (int) yogaNode.getLayoutBorder(YogaEdge.BOTTOM);
		return height;
	}

	public MeadElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		applyLayoutProperties(this, sanitizeAttributes(attributes));
	}

	/**
	 * Override this method to sanitize layout related attributes before applying them to the element.
	 * @param attributes
	 * @return A sanitized map of attributes. If the input is null, an empty map is returned.
	 */
	public HashMap<String, String> sanitizeAttributes(HashMap<String, String> attributes)
	{
		return attributes == null ? new HashMap<>() : attributes;
	}
	/**
	 * Text content of the element. This is set by the MeadParser. Ignore it if you're not using it.
	 */
	public Binding<String> textContent = new Binding<>("");
	/**
	 * Use this as a cache
	 */
	protected AbstractWidget widget;
	public AbstractWidget getWidget() { return widget; }

	public static void applyLayoutProperties(MeadElement element, HashMap<String, String> attributes)
	{
		if (attributes == null || element == null)
			return;
		NullUtils.ifNotNull(attributes.get("width"), value -> {
			element.yogaNode.setWidth(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("height"), value -> {
			element.yogaNode.setHeight(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("flexDirection"), value -> {
			element.yogaNode.setFlexDirection(IStringParser.YOGA_FLEX_DIRECTION_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("alignItems"), value -> {
			element.yogaNode.setAlignItems(IStringParser.YOGA_ALIGN_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("alignSelf"), value -> {
			element.yogaNode.setAlignSelf(IStringParser.YOGA_ALIGN_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("justifyContent"), value -> {
			element.yogaNode.setJustifyContent(IStringParser.YOGA_JUSTIFY_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("flexGrow"), value -> {
			element.yogaNode.setFlexGrow(IStringParser.FLOAT_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("flexShrink"), value -> {
			element.yogaNode.setFlexShrink(IStringParser.FLOAT_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("flexBasis"), value -> {
			element.yogaNode.setFlexBasis(IStringParser.FLOAT_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("margin"), value -> {
			for(var el : IStringParser.YOGA_EDGE_LENGTH_PARSER.parse(value))
				element.yogaNode.setMargin(el.getA(), el.getB());
		});
		NullUtils.ifNotNull(attributes.get("padding"), value -> {
			for(var el : IStringParser.YOGA_EDGE_LENGTH_PARSER.parse(value))
				element.yogaNode.setPadding(el.getA(), el.getB());
		});
		NullUtils.ifNotNull(attributes.get("border"), value -> {
			for(var el : IStringParser.YOGA_BORDER_PARSER.parse(value))
				element.yogaNode.setBorder(el.getA(), el.getB());
		});
		NullUtils.ifNotNull(attributes.get("position"), value -> {
			element.yogaNode.setPositionType(IStringParser.YOGA_POSITION_TYPE_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("left"), value -> {
			element.yogaNode.setPosition(YogaEdge.LEFT, IStringParser.STYLE_LENGTH_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("top"), value -> {
			element.yogaNode.setPosition(YogaEdge.TOP, IStringParser.STYLE_LENGTH_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("right"), value -> {
			element.yogaNode.setPosition(YogaEdge.RIGHT, IStringParser.STYLE_LENGTH_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("bottom"), value -> {
			element.yogaNode.setPosition(YogaEdge.BOTTOM, IStringParser.STYLE_LENGTH_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("aspectRatio"), value -> {
			element.yogaNode.setAspectRatio(IStringParser.ASPECT_RATIO_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("overflow"), value -> {
			element.yogaNode.setOverflow(IStringParser.YOGA_OVERFLOW_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("display"), value -> {
			element.yogaNode.setDisplay(IStringParser.YOGA_DISPLAY_PARSER.parse(value));
		});
	}

	private static final HashMap<String, MeadParser.IMeadElementFactory> elementFactories = new HashMap<>();
	public static HashMap<String, MeadParser.IMeadElementFactory> getDefaultElements()
	{
		if (elementFactories.isEmpty())
		{
			elementFactories.put("Mead", Element::new);
			elementFactories.put("Rect", RectElement::new);
			elementFactories.put("Text", TextElement::new);

			elementFactories.put("Button", ButtonElement::new);

			elementFactories.put("If", IfElement::new);
			//elementFactories.put("image", ImageElement::new);
			//elementFactories.put("button", ButtonElement::new);
			// Add more default elements as needed
		}
		return elementFactories;
	}
}
