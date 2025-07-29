package dev.zeddevstuff.mead.neoforge;

import dev.zeddevstuff.mead.Mead;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Mead.MOD_ID)
public final class MeadNeoForge
{
	public MeadNeoForge(IEventBus modBus, ModContainer container)
	{
		Mead.init();
	}
}
