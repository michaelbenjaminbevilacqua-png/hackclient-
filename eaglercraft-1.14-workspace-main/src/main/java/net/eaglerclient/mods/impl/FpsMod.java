package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.Mod;
import net.eaglerclient.mods.HudLayout;
import net.minecraft.client.Minecraft;

public class FpsMod extends Mod {

    public FpsMod() {
        super("FPS", Category.RENDER, 0);
    }

    @Override
    public void onRenderOverlay(Minecraft mc, int scaledWidth, int scaledHeight) {
        String text = "FPS " + Minecraft.getDebugFPS();
        mc.fontRenderer.drawStringWithShadow(text, HudLayout.fpsX, HudLayout.fpsY, 0xFFFFFFFF);
        markBounds(HudLayout.fpsX, HudLayout.fpsY, mc.fontRenderer.getStringWidth(text), 10);
    }
}