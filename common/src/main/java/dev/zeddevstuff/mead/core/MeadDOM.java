package dev.zeddevstuff.mead.core;

import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.utils.NullUtils;

import java.util.ArrayList;
import java.util.List;

public class MeadDOM
{
	private MeadElement root;
	public MeadElement getRoot() { return root; }
	public void setRoot(MeadElement root)
	{
		if (root != null)
		{
			this.root = root;
			root.getNode().setWidth(width);
			root.getNode().setHeight(height);
		}
	}
	private int width = 0;
	private int height = 0;


	public MeadDOM(MeadElement root)
	{
		this.root = root;
	}

	public void resize(int width, int height)
	{
		this.width = width;
		this.height = height;
		NullUtils.ifNotNull(root, rootNode -> {
			root.getNode().setWidth(width);
			root.getNode().setHeight(height);
		});
	}

	public void calculateLayout()
	{
		if (root != null)
			root.calculateLayout();
	}

	public List<MeadElement> getAllElements()
	{
		ArrayList<MeadElement> elements = new ArrayList<>();
		getAllElementsRecursive(root, elements);
		return elements.stream().filter(el -> el.getWidget() != null).toList();
	}

	private void getAllElementsRecursive(MeadElement element, ArrayList<MeadElement> elements)
	{
		if(element == null)
			return;
		elements.add(element);
		for (MeadElement child : element.getChildren())
		{
			getAllElementsRecursive(child, elements);
		}
	}


}
