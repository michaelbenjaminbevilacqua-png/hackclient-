package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.Mod;
import net.eaglerclient.mods.HudLayout;
import net.lax1dude.eaglercraft.HString;
import net.minecraft.client.Minecraft;

public class CoordinatesMod extends Mod {

    public CoordinatesMod() {
        super("Coordinates", Category.RENDER, 0);
    }

    @Override
    public void onRenderOverlay(Minecraft mc, int scaledWidth, int scaledHeight) {
        if (mc.player == null) {
            return;
        }
        String text = HString.format("XYZ %.1f / %.1f / %.1f", mc.player.posX, mc.player.posY, mc.player.posZ);
        int y = HudLayout.coordinatesY < 0 ? scaledHeight + HudLayout.coordinatesY : HudLayout.coordinatesY;
        mc.fontRenderer.drawStringWithShadow(text, HudLayout.coordinatesX, y, 0xFFFFFF);
        markBounds(HudLayout.coordinatesX, y, mc.fontRenderer.getStringWidth(text), 10);
    }
}