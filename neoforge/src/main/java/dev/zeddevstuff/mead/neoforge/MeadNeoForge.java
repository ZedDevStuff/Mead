package dev.zeddevstuff.mead.neoforge;

import dev.zeddevstuff.mead.Mead;
import net.neoforged.fml.common.Mod;

@Mod(Mead.MOD_ID)
public final class MeadNeoForge
{
	public MeadNeoForge()
	{
		// Run our common setup.
		Mead.init();
	}
}
