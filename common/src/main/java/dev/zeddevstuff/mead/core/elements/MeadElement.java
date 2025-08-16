package dev.zeddevstuff.mead.core.elements;

import dev.zeddevstuff.mead.core.ElementFlavor;
import dev.zeddevstuff.mead.core.ElementState;
import dev.zeddevstuff.mead.core.MeadContext;
import dev.zeddevstuff.mead.core.MeadDOM;
import dev.zeddevstuff.mead.core.data.DataSource;
import dev.zeddevstuff.mead.core.data.ObservableProperty;
import dev.zeddevstuff.mead.core.parsing.IStringParser;
import dev.zeddevstuff.mead.core.styling.IHasFlavorProperty;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import org.appliedenergistics.yoga.*;
import org.appliedenergistics.yoga.style.StyleLength;
import org.appliedenergistics.yoga.style.StyleSizeLength;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * The base class for all Mead elements.
 */
public abstract class MeadElement implements Cloneable
{
	protected YogaNode yogaNode = new YogaNode();
	//public YogaNode getNode() { return yogaNode; }

	protected MeadContext ctx;
	public MeadContext getCtx() { return ctx; }
	public void setCtx(MeadContext ctx) { this.ctx = ctx; }

	protected DataSource dataSource;
	public DataSource getDataSource() { return dataSource; }
	public void setDataSource(DataSource source)
	{
		if(dataSource == source) return;
		var old = dataSource;
		dataSource = source;
		setDataSource(old, source);
	}
	private void setDataSource(DataSource oldSource, DataSource newSource)
	{
		if(dataSource == oldSource)
			dataSource = newSource;
		for(var child : children)
		{
			child.setDataSource(oldSource, newSource);
		}
	}

	/**
	 * Text content of the element. This is set by the MeadParser. Ignore it if you're not using it.
	 */
	public ObservableProperty<String> textContent = new ObservableProperty<>("");

	//public abstract void parsingComplete();

	/**
	 * Use this as a cache
	 */
	protected AbstractWidget widget;

	public abstract String getTagName();

	protected MeadElement parent;
	public MeadElement getParent() { return parent; }
	public void setParent(MeadElement parent)
	{
		parent.yogaNode.addChildAt(yogaNode, parent.yogaNode.getLayoutChildCount());
		this.parent = parent;
	}
	protected ArrayList<MeadElement> children = new ArrayList<>();
	public ArrayList<MeadElement> getChildren() { return children; }
	public void setChildren(List<MeadElement> children)
	{
		clearChildren();
		for (MeadElement child : children)
			addChild(child);
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
	public void clearChildren()
	{
		for(MeadElement child : this.children)
			parent.removeChild(child);
		this.children.clear();
	}
	public MeadElement getRoot()
	{
		if(parent != null) return parent.getRoot();
		else return this;
	}
	private ElementState state;
	public ElementState getElementState() { return state; }
	public void setElementState(boolean isEnabled, boolean isHovered, boolean isFocused)
	{
		boolean changed = false;
		if (!isEnabled && state != ElementState.DISABLED)
		{
			state = ElementState.DISABLED;
			disabledLayout().applyTo(this);

		}
		else if(isFocused && state != ElementState.ACTIVE)
		{
			state = ElementState.ACTIVE;
			activeLayout().applyTo(this);
		}
		else if (isHovered && state != ElementState.HOVER)
		{
			state = ElementState.HOVER;
			hoverLayout().applyTo(this);
		}
		else if(state != ElementState.NORMAL)
		{
			state = ElementState.NORMAL;
			layout().applyTo(this);
		}
	}

	private final LayoutProperties layoutProps = new LayoutProperties();
	public LayoutProperties layout() { return layoutProps; }
	private final LayoutProperties hoverLayoutProps = new LayoutProperties();
	public LayoutProperties hoverLayout() { return hoverLayoutProps; }
	private final LayoutProperties activeLayoutProps = new LayoutProperties();
	public LayoutProperties activeLayout() { return activeLayoutProps; }
	private final LayoutProperties disabledLayoutProps = new LayoutProperties();
	public LayoutProperties disabledLayout() { return disabledLayoutProps; }

	public LayoutProperties getCurrentLayout(boolean isEnabled, boolean isHovered, boolean isFocused)
	{
		if (!isEnabled) return disabledLayoutProps;
		if(isFocused) return activeLayoutProps;
		if (isHovered) return hoverLayoutProps;
		return layoutProps;
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

	public MeadElement(HashMap<String, String> attributes, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		this.textContent.set(textContent);
		setupProperties();
		applyBaseProperties(this, sanitizeAttributes(attributes));
	}

	/**
	 * Override this method to sanitize layout related attributes before applying them to the element.
	 * @return A sanitized map of attributes. If the input is null, an empty map is returned.
	 */
	protected HashMap<String, String> sanitizeAttributes(HashMap<String, String> attributes)
	{
		return attributes == null ? new HashMap<>() : attributes;
	}

	protected void setFlavor(ElementFlavor flavor)
	{
		if(this instanceof IHasFlavorProperty flavorable)
		{
			flavorable.flavor().set(flavor);
		}
		for(var child : children)
			child.setFlavor(flavor);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

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

		var style = element.yogaNode.getStyle();

		NullUtils.ifNotNull(attributes.get("width"), value -> {
			element.yogaNode.setWidth(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("height"), value -> {
			element.yogaNode.setHeight(IStringParser.STYLE_SIZE_LENGTH_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("flex-direction"), value -> {
			element.yogaNode.setFlexDirection(IStringParser.YOGA_FLEX_DIRECTION_PARSER.parse(value));
			element.yogaNode.calculateLayout(element.computedLayout().width, element.computedLayout().height);
		});
		NullUtils.ifNotNull(attributes.get("align-content"), value -> {
			element.yogaNode.setAlignContent(IStringParser.YOGA_ALIGN_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("align-items"), value -> {
			element.yogaNode.setAlignItems(IStringParser.YOGA_ALIGN_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("align-self"), value -> {
			element.yogaNode.setAlignSelf(IStringParser.YOGA_ALIGN_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("justify-content"), value -> {
			element.yogaNode.setJustifyContent(IStringParser.YOGA_JUSTIFY_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("flex-grow"), value -> {
			element.yogaNode.setFlexGrow(IStringParser.FLOAT_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("flex-Shrink"), value -> {
			element.yogaNode.setFlexShrink(IStringParser.FLOAT_PARSER.parse(value));
		});
		NullUtils.ifNotNull(attributes.get("flex-basis"), value -> {
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
		NullUtils.ifNotNull(attributes.get("box-sizing"), value -> {
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
		NullUtils.ifNotNull(attributes.get("aspect-ratio"), value -> {
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
	public ComputedLayoutData computedLayout() { return computedLayoutData; }
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
			child.calculateLayout();
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
				x += element.parent.computedLayout().x;
			return x;
		}
		private int calculateY(MeadElement element)
		{
			int y = (int) (element.yogaNode.getLayoutY());
			if (element.parent != null)
				y += element.parent.computedLayout().y;
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

	private void setupProperties()
	{
		layoutProps.alignContent.addObserver(yogaNode::setAlignContent);
		layoutProps.alignItems.addObserver(yogaNode::setAlignItems);
		layoutProps.alignSelf.addObserver(yogaNode::setAlignSelf);
		layoutProps.aspectRatio.addObserver(yogaNode::setAspectRatio);
		layoutProps.borderLeft.addObserver(v -> yogaNode.setBorder(YogaEdge.LEFT, v));
		layoutProps.borderTop.addObserver(v -> yogaNode.setBorder(YogaEdge.TOP, v));
		layoutProps.borderRight.addObserver(v -> yogaNode.setBorder(YogaEdge.RIGHT, v));
		layoutProps.borderBottom.addObserver(v -> yogaNode.setBorder(YogaEdge.BOTTOM, v));
		layoutProps.boxSizing.addObserver(yogaNode::setBoxSizing);
		layoutProps.width.addObserver(yogaNode::setWidth);
		layoutProps.height.addObserver(yogaNode::setHeight);
		layoutProps.direction.addObserver(yogaNode::setDirection);
		layoutProps.display.addObserver(yogaNode::setDisplay);
		layoutProps.flex.addObserver(yogaNode::setFlex);
		layoutProps.flexBasis.addObserver(yogaNode.getStyle()::setFlexBasis);
		layoutProps.flexDirection.addObserver(yogaNode::setFlexDirection);
		layoutProps.flexGrow.addObserver(yogaNode::setFlexGrow);
		layoutProps.flexShrink.addObserver(yogaNode::setFlexShrink);
		layoutProps.flexWrap.addObserver(yogaNode::setWrap);
		layoutProps.columnGap.addObserver(v -> yogaNode.setGap(YogaGutter.COLUMN, v));
		layoutProps.rowGap.addObserver(v -> yogaNode.setGap(YogaGutter.ROW, v));
		layoutProps.justifyContent.addObserver(yogaNode::setJustifyContent);
		layoutProps.marginLeft.addObserver(v -> yogaNode.setMargin(YogaEdge.LEFT, v));
		layoutProps.marginTop.addObserver(v -> yogaNode.setMargin(YogaEdge.TOP, v));
		layoutProps.marginRight.addObserver(v -> yogaNode.setMargin(YogaEdge.RIGHT, v));
		layoutProps.marginBottom.addObserver(v -> yogaNode.setMargin(YogaEdge.BOTTOM, v));
		layoutProps.maxWidth.addObserver(yogaNode::setMaxWidth);
		layoutProps.maxHeight.addObserver(yogaNode::setMaxHeight);
		layoutProps.minWidth.addObserver(yogaNode::setMinWidth);
		layoutProps.minHeight.addObserver(yogaNode::setMinHeight);
		layoutProps.overflow.addObserver(yogaNode::setOverflow);
		layoutProps.paddingLeft.addObserver(v -> yogaNode.setPadding(YogaEdge.LEFT, v));
		layoutProps.paddingTop.addObserver(v -> yogaNode.setPadding(YogaEdge.TOP, v));
		layoutProps.paddingRight.addObserver(v -> yogaNode.setPadding(YogaEdge.RIGHT, v));
		layoutProps.paddingBottom.addObserver(v -> yogaNode.setPadding(YogaEdge.BOTTOM, v));
		layoutProps.positionLeft.addObserver(v -> yogaNode.setPosition(YogaEdge.LEFT, v));
		layoutProps.positionTop.addObserver(v -> yogaNode.setPosition(YogaEdge.TOP, v));
		layoutProps.positionRight.addObserver(v -> yogaNode.setPosition(YogaEdge.RIGHT, v));
		layoutProps.positionBottom.addObserver(v -> yogaNode.setPosition(YogaEdge.BOTTOM, v));
		layoutProps.positionType.addObserver(yogaNode::setPositionType);
	}

	/**
	 * Mirror properties of the YogaNode style. Made for data binding and other purposes.
	 */
	public static class LayoutProperties
	{
		private final ObservableProperty<YogaAlign> alignContent = new ObservableProperty<>(YogaAlign.FLEX_START);
		public ObservableProperty<YogaAlign> alignContent() { return alignContent; }
		private final ObservableProperty<YogaAlign> alignItems = new ObservableProperty<>(YogaAlign.STRETCH);
		public ObservableProperty<YogaAlign> alignItems() { return alignItems; }
		private final ObservableProperty<YogaAlign> alignSelf = new ObservableProperty<>(YogaAlign.AUTO);
		public ObservableProperty<YogaAlign> alignSelf() { return alignSelf; }
		private final ObservableProperty<Float> aspectRatio = new ObservableProperty<>(Float.NaN);
		public ObservableProperty<Float> aspectRatio() { return aspectRatio; }

		private final ObservableProperty<Float> borderLeft = new ObservableProperty<>(Float.NaN);
		public ObservableProperty<Float> borderLeft() { return borderLeft; }
		private final ObservableProperty<Float> borderTop = new ObservableProperty<>(Float.NaN);
		public ObservableProperty<Float> borderTop() { return borderTop; }
		private final ObservableProperty<Float> borderRight = new ObservableProperty<>(Float.NaN);
		public ObservableProperty<Float> borderRight() { return borderRight; }
		private final ObservableProperty<Float> borderBottom = new ObservableProperty<>(Float.NaN);
		public ObservableProperty<Float> borderBottom() { return borderBottom; }

		private final ObservableProperty<YogaBoxSizing> boxSizing = new ObservableProperty<>(YogaBoxSizing.BORDER_BOX);
		public ObservableProperty<YogaBoxSizing> boxSizing() { return boxSizing; }

		private final ObservableProperty<StyleSizeLength> width = new ObservableProperty<>(StyleSizeLength.AUTO);
		public ObservableProperty<StyleSizeLength> width() { return width; }
		private final ObservableProperty<StyleSizeLength> height = new ObservableProperty<>(StyleSizeLength.AUTO);
		public ObservableProperty<StyleSizeLength> height() { return height; }

		private final ObservableProperty<YogaDirection> direction = new ObservableProperty<>(YogaDirection.LTR);
		public ObservableProperty<YogaDirection> direction() { return direction; }
		private final ObservableProperty<YogaDisplay> display = new ObservableProperty<>(YogaDisplay.FLEX);
		public ObservableProperty<YogaDisplay> display() { return display; }

		private final ObservableProperty<Float> flex = new ObservableProperty<>(Float.NaN);
		public ObservableProperty<Float> flex() { return flex; }
		private final ObservableProperty<StyleSizeLength> flexBasis = new ObservableProperty<>(StyleSizeLength.AUTO);
		public ObservableProperty<StyleSizeLength> flexBasis() { return flexBasis; }
		private final ObservableProperty<YogaFlexDirection> flexDirection = new ObservableProperty<>(YogaFlexDirection.COLUMN);
		public ObservableProperty<YogaFlexDirection> flexDirection() { return flexDirection; }
		private final ObservableProperty<Float> flexGrow = new ObservableProperty<>(0f);
		public ObservableProperty<Float> flexGrow() { return flexGrow; }
		private final ObservableProperty<Float> flexShrink = new ObservableProperty<>(0f);
		public ObservableProperty<Float> flexShrink() { return flexShrink; }
		private final ObservableProperty<YogaWrap> flexWrap = new ObservableProperty<>(YogaWrap.NO_WRAP);
		public ObservableProperty<YogaWrap> flexWrap() { return flexWrap; }

		private final ObservableProperty<StyleLength> columnGap = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> columnGap() { return columnGap; }
		private final ObservableProperty<StyleLength> rowGap = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> rowGap() { return rowGap; }

		private final ObservableProperty<YogaJustify> justifyContent = new ObservableProperty<>(YogaJustify.FLEX_START);
		public ObservableProperty<YogaJustify> justifyContent() { return justifyContent; }

		private final ObservableProperty<StyleLength> marginLeft = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> marginLeft() { return marginLeft; }
		private final ObservableProperty<StyleLength> marginTop = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> marginTop() { return marginTop; }
		private final ObservableProperty<StyleLength> marginRight = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> marginRight() { return marginRight; }
		private final ObservableProperty<StyleLength> marginBottom = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> marginBottom() { return marginBottom; }
		private final ObservableProperty<StyleSizeLength> maxWidth = new ObservableProperty<>(StyleSizeLength.undefined());
		public ObservableProperty<StyleSizeLength> maxWidth() { return maxWidth; }
		private final ObservableProperty<StyleSizeLength> maxHeight = new ObservableProperty<>(StyleSizeLength.undefined());
		public ObservableProperty<StyleSizeLength> maxHeight() { return maxHeight; }
		private final ObservableProperty<StyleSizeLength> minWidth = new ObservableProperty<>(StyleSizeLength.undefined());
		public ObservableProperty<StyleSizeLength> minWidth() { return minWidth; }
		private final ObservableProperty<StyleSizeLength> minHeight = new ObservableProperty<>(StyleSizeLength.undefined());
		public ObservableProperty<StyleSizeLength> minHeight() { return minHeight; }

		private final ObservableProperty<YogaOverflow> overflow = new ObservableProperty<>(YogaOverflow.VISIBLE);
		public ObservableProperty<YogaOverflow> overflow() { return overflow; }

		private final ObservableProperty<StyleLength> paddingLeft = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> paddingLeft() { return paddingLeft; }
		private final ObservableProperty<StyleLength> paddingTop = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> paddingTop() { return paddingTop; }
		private final ObservableProperty<StyleLength> paddingRight = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> paddingRight() { return paddingRight; }
		private final ObservableProperty<StyleLength> paddingBottom = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> paddingBottom() { return paddingBottom; }
		private final ObservableProperty<StyleLength> positionLeft = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> left() { return positionLeft; }
		private final ObservableProperty<StyleLength> positionTop = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> top() { return positionTop; }
		private final ObservableProperty<StyleLength> positionRight = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> right() { return positionRight; }
		private final ObservableProperty<StyleLength> positionBottom = new ObservableProperty<>(StyleLength.undefined());
		public ObservableProperty<StyleLength> bottom() { return positionBottom; }
		private final ObservableProperty<YogaPositionType> positionType = new ObservableProperty<>(YogaPositionType.RELATIVE);
		public ObservableProperty<YogaPositionType> positionType() { return positionType; }

		public void applyTo(MeadElement element)
		{
			long time = System.nanoTime();
			element.yogaNode.setAlignContent(this.alignContent.get());
			element.yogaNode.setAlignItems(this.alignItems.get());
			element.yogaNode.setAlignSelf(this.alignSelf.get());
			element.yogaNode.setAspectRatio(this.aspectRatio.get());

			element.yogaNode.setBorder(YogaEdge.LEFT, this.borderLeft.get());
			element.yogaNode.setBorder(YogaEdge.TOP, this.borderTop.get());
			element.yogaNode.setBorder(YogaEdge.RIGHT, this.borderRight.get());
			element.yogaNode.setBorder(YogaEdge.BOTTOM, this.borderBottom.get());
			element.yogaNode.setBoxSizing(this.boxSizing.get());

			element.yogaNode.setWidth(this.width.get());
			element.yogaNode.setHeight(this.height.get());

			element.yogaNode.setDirection(this.direction.get());
			element.yogaNode.setDisplay(this.display.get());

			element.yogaNode.setFlex(this.flex.get());
			element.yogaNode.getStyle().setFlexBasis(this.flexBasis.get());
			element.yogaNode.setFlexDirection(this.flexDirection.get());
			element.yogaNode.setFlexGrow(this.flexGrow.get());
			element.yogaNode.setFlexShrink(this.flexShrink.get());
			element.yogaNode.setWrap(this.flexWrap.get());

			element.yogaNode.setGap(YogaGutter.COLUMN, this.columnGap.get());
			element.yogaNode.setGap(YogaGutter.ROW, this.rowGap.get());

			element.yogaNode.setMargin(YogaEdge.LEFT, this.marginLeft.get());
			element.yogaNode.setMargin(YogaEdge.TOP, this.marginTop.get());
			element.yogaNode.setMargin(YogaEdge.RIGHT, this.marginRight.get());
			element.yogaNode.setMargin(YogaEdge.BOTTOM, this.marginBottom.get());

			element.yogaNode.setMaxWidth(this.maxWidth.get());
			element.yogaNode.setMaxHeight(this.maxHeight.get());
			element.yogaNode.setMinWidth(this.minWidth.get());
			element.yogaNode.setMinHeight(this.minHeight.get());

			element.yogaNode.setOverflow(this.overflow.get());

			element.yogaNode.setPadding(YogaEdge.LEFT, this.paddingLeft.get());
			element.yogaNode.setPadding(YogaEdge.TOP, this.paddingTop.get());
			element.yogaNode.setPadding(YogaEdge.RIGHT, this.paddingRight.get());
			element.yogaNode.setPadding(YogaEdge.BOTTOM, this.paddingBottom.get());

			element.yogaNode.setPosition(YogaEdge.LEFT, this.positionLeft.get());
			element.yogaNode.setPosition(YogaEdge.TOP, this.positionTop.get());
			element.yogaNode.setPosition(YogaEdge.RIGHT, this.positionRight.get());
			element.yogaNode.setPosition(YogaEdge.BOTTOM, this.positionBottom.get());

			element.yogaNode.setPositionType(this.positionType.get());

			time = System.nanoTime() - time;
			System.out.println("Took " + (time / 1_000_000f) + "ms to apply properties");
		}
	}
}
