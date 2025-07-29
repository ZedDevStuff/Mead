package dev.zeddevstuff.mead.mixin;

import dev.zeddevstuff.mead.Mead;
import dev.zeddevstuff.mead.TestMeadScreen;
import dev.zeddevstuff.mead.minecraft.MeadFileScreen;
import dev.zeddevstuff.mead.minecraft.MeadScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.Callable;

@Mixin(net.minecraft.client.gui.screens.TitleScreen.class)
public abstract class TitleScreenMixin extends Screen
{
	public TitleScreenMixin()
	{
		super(Component.literal("Mead Title Screen Mixin"));
	}

	@Inject(method = "init", at = @org.spongepowered.asm.mixin.injection.At("HEAD"))
	public void onInit(CallbackInfo ci)
	{
		addRenderableWidget(Button.builder(Component.literal("M"), button -> {
				try
				{
					var actions = new HashMap<String, Callable<?>>();
					actions.put("sayHello", () -> { System.out.println("Hello from Mead!"); return null; });
					Minecraft.getInstance().setScreen(new MeadFileScreen(
						Path.of("C:\\Users\\kouam\\Documents\\Minecraft Mods\\Mead\\common\\src\\main\\resources\\assets\\mead\\ui\\test.mead"),
						Mead.ctx(),
						null,
						actions));
				} catch (IOException e)
				{
					throw new RuntimeException(e);
				}
			}).pos(10, 10)
			.size(20,20)
			.build());
	}
}
