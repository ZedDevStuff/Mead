package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.MeadDOM;
import dev.zeddevstuff.mead.core.Binding;
import dev.zeddevstuff.mead.interfaces.IStringParser;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import org.appliedenergistics.yoga.YogaBoxSizing;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaNode;

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

	public abstract String getTagName();

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
	public MeadElement getRoot()
	{
		if(parent != null) return parent.getRoot();
		else return this;
	}

	private final List<String> styles = new ArrayList<>();
	public List<String> getStyles() { return styles; }
	public void setStyles(List<String> styles)
	{
		this.styles.clear();
		if(styles != null)
			this.styles.addAll(styles);
	}
	public boolean hasStyle(String style)
	{
		return styles.contains(style);
	}

	public MeadElement(HashMap<String, String> attributes, HashMap<String, Binding<?>> variables, HashMap<String, Callable<?>> actions)
	{
		applyBaseProperties(this, sanitizeAttributes(attributes));
	}

	/**
	 * Override this method to sanitize layout related attributes before applying them to the element.
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

	/**
	 * Returns the widget associated with this element. Should always return {@link MeadElement#widget} unless absolutely necessary and aware of what you're doing.
	 */
	public AbstractWidget getWidget() { return widget; }

	public static void applyBaseProperties(MeadElement element, HashMap<String, String> attributes)
	{
		if (attributes == null || element == null)
			return;
		NullUtils.ifNotNull(attributes.get("style"), value -> {
			String[] styles = value.split("\\s+");
			for (String style : styles)
				if (!style.isEmpty())
					element.styles.add(style);
		});
		// This is purely for convenience
		NullUtils.ifNotNull(attributes.get("class"), value -> {
			String[] styles = value.split("\\s+");
			for (String style : styles)
				if (!style.isEmpty())
					element.styles.add(style);
		});
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
		NullUtils.ifNotNull(attributes.get("boxSizing"), value -> {
			element.yogaNode.setBoxSizing(IStringParser.YOGA_BOX_SIZING_PARSER.parse(value));
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

	// region Layout data
	private ComputedLayoutData computedLayoutData = new ComputedLayoutData(this);

	/**
	 * Returns the computed layout data for this element. Note that this only gets calculated either when part of a {@link MeadDOM} or when {@link #calculateLayout()} is called on the root element.
	 */
	public ComputedLayoutData getLayout() { return computedLayoutData; }
	public void calculateLayout()
	{
		boolean isRoot = parent == null;
		if(isRoot && yogaNode.isDirty())
			yogaNode.calculateLayout(yogaNode.getWidth().value, yogaNode.getHeight().value);
		if(yogaNode.hasNewLayout())
		{
			computedLayoutData = new ComputedLayoutData(this);
			yogaNode.markLayoutSeen();
			yogaNode.setDirty(false);
		}
		for (var child : children)
		{
			child.calculateLayout();
		}
	}

	public static class ComputedLayoutData
	{
		public final int x;
		public final int innerX;
		public final int y;
		public final int innerY;
		public final int width;
		public final int innerWidth;
		public final int height;
		public final int innerHeight;
		public final int borderLeft;
		public final int borderTop;
		public final int borderRight;
		public final int borderBottom;

		public ComputedLayoutData(MeadElement element)
		{
			this.x = calculateX(element);
			this.innerX = calculateInnerX(element);
			this.y = calculateY(element);
			this.innerY = calculateInnerY(element);
			this.width = calculateWidth(element);
			this.innerWidth = calculateInnerWidth(element);
			this.height = calculateHeight(element);
			this.innerHeight = calculateInnerHeight(element);
			this.borderLeft = (int) element.yogaNode.getLayoutBorder(YogaEdge.LEFT);
			this.borderTop = (int) element.yogaNode.getLayoutBorder(YogaEdge.TOP);
			this.borderRight = (int) element.yogaNode.getLayoutBorder(YogaEdge.RIGHT);
			this.borderBottom = (int) element.yogaNode.getLayoutBorder(YogaEdge.BOTTOM);
		}

		private int calculateX(MeadElement element)
		{
			int x = (int) (element.yogaNode.getLayoutX());
			if (element.parent != null)
				x += element.parent.getLayout().x;
			return x;
		}
		private int calculateY(MeadElement element)
		{
			int y = (int) (element.yogaNode.getLayoutY());
			if (element.parent != null)
				y += element.parent.getLayout().y;
			return y;
		}
		private int calculateInnerX(MeadElement element)
		{
			return x + (int) element.yogaNode.getLayoutBorder(YogaEdge.LEFT);
		}
		private int calculateInnerY(MeadElement element)
		{
			return y + (int) element.yogaNode.getLayoutBorder(YogaEdge.TOP);
		}
		private int calculateWidth(MeadElement element)
		{
			if(element.yogaNode.getBoxSizing() == YogaBoxSizing.BORDER_BOX)
				return (int) (element.yogaNode.getLayoutWidth());
			else
				return (int) (element.yogaNode.getLayoutWidth() + element.yogaNode.getLayoutBorder(YogaEdge.LEFT) + element.yogaNode.getLayoutBorder(YogaEdge.RIGHT));
		}
		private int calculateHeight(MeadElement element)
		{
			if(element.yogaNode.getBoxSizing() == YogaBoxSizing.BORDER_BOX)
				return (int) (element.yogaNode.getLayoutHeight());
			else
				return (int) (element.yogaNode.getLayoutHeight() + element.yogaNode.getLayoutBorder(YogaEdge.TOP) + element.yogaNode.getLayoutBorder(YogaEdge.BOTTOM));
		}
		private int calculateInnerWidth(MeadElement element)
		{
			if(element.yogaNode.getBoxSizing() == YogaBoxSizing.BORDER_BOX)
				return (int) (element.yogaNode.getLayoutWidth() - element.yogaNode.getLayoutBorder(YogaEdge.LEFT) - element.yogaNode.getLayoutBorder(YogaEdge.RIGHT));
			else
				return (int) (element.yogaNode.getLayoutWidth());
		}
		private int calculateInnerHeight(MeadElement element)
		{
			if(element.yogaNode.getBoxSizing() == YogaBoxSizing.BORDER_BOX)
				return (int) (element.yogaNode.getLayoutHeight() - element.yogaNode.getLayoutBorder(YogaEdge.TOP) - element.yogaNode.getLayoutBorder(YogaEdge.BOTTOM));
			else
				return (int) (element.yogaNode.getLayoutHeight());
		}
	}
	// endregion Layout data
}
