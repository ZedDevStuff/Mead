package dev.zeddevstuff.mead;

import com.mojang.logging.LogUtils;
import dev.zeddevstuff.mead.core.data.DataSource;
import dev.zeddevstuff.mead.core.data.LambdaProperty;
import dev.zeddevstuff.mead.core.data.Property;
import dev.zeddevstuff.mead.core.minecraft.MeadScreen;

import java.util.HashMap;

public class TestMeadScreen extends MeadScreen
{
	public TestMeadScreen()
	{
		super("test.mead", Mead.ctx(),  null);
		dom.getRoot().setDataSource(new Data());
	}

	public static class Data extends DataSource
	{
		private boolean isNeoForge;
		public boolean isNeoForge() { return isNeoForge; }
		public void isNeoForge(boolean value) { isNeoForge = value; }

		public Data()
		{
			super();
			try
			{
				Class.forName("net.neoforged.bus.api.IEventBus");
				isNeoForge = true;
			}
			catch(Exception ignored)
			{
				isNeoForge = false;
			}
		}

		@Override
		protected void registerProperties(HashMap<String, Property<?>> properties)
		{
			properties.put("isNeoForge", new LambdaProperty<>(this::isNeoForge, this::isNeoForge));
		}
	}
}
