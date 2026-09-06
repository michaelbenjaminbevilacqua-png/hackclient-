package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;

public class LargeMapMod extends Mod {

    private final KeyBinding openKey = new KeyBinding("key.eaglerclient.largemap", 77, "key.categories.misc");

    public LargeMapMod() {
        super("LargeMap", Category.RENDER, 0);
    }

    @Override
    public void onTick(Minecraft mc) {
        super.onTick(mc);
        if (isEnabled() && openKey.isPressed() && mc.currentScreen == null) {
            mc.displayGuiScreen(new LargeMapScreen(null));
        }
    }
}