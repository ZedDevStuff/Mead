package dev.zeddevstuff.mead.core.elements.parsing;

import dev.zeddevstuff.mead.core.data.Property;
import dev.zeddevstuff.mead.core.elements.MeadElement;
import dev.zeddevstuff.mead.utils.NullUtils;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class ImportElement extends MeadElement implements IParsingCompleteListener
{
	@Override
	public String getTagName() { return "import"; }
	private String src;

	public void setSrc(String src)
	{
		this.src = src;
		if(src.endsWith(".mss"))
		{
			var style = getCtx().getStyleSheet(src);
			style.ifPresent(meadStyle -> meadStyle.applyToTree(getRoot()));
		}
	}

	public ImportElement(HashMap<String, String> attributes, HashMap<String, Callable<?>> actions, @NotNull String textContent)
	{
		super(null, null, "");
		NullUtils.ifNotNull(attributes.get("src"), value -> src = value);
	}

	@Override
	public AbstractWidget getWidget()
	{
		return null;
	}

	public void parsingComplete()
	{
		if(src.endsWith(".mss"))
			setSrc(src);
	}
}
