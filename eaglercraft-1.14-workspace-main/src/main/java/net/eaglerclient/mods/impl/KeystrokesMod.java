package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.HudLayout;
import net.eaglerclient.mods.Mod;
import net.lax1dude.eaglercraft.Mouse;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.Minecraft;

public class KeystrokesMod extends Mod {

    private static final int SIZE = 20;
    private static final int GAP = 2;

    public KeystrokesMod() {
        super("Keystrokes", Category.RENDER, 0);
    }

    @Override
    public void onRenderOverlay(Minecraft mc, int scaledWidth, int scaledHeight) {
        int x = HudLayout.keystrokesX < 0 ? 8 : HudLayout.keystrokesX;
        int y = HudLayout.keystrokesY < 0 ? scaledHeight - 110 : HudLayout.keystrokesY;
        drawKey(mc, x + SIZE + GAP, y, SIZE, "W", mc.gameSettings.keyBindForward.isKeyDown());
        drawKey(mc, x, y + SIZE + GAP, SIZE, "A", mc.gameSettings.keyBindLeft.isKeyDown());
        drawKey(mc, x + SIZE + GAP, y + SIZE + GAP, SIZE, "S", mc.gameSettings.keyBindBack.isKeyDown());
        drawKey(mc, x + (SIZE + GAP) * 2, y + SIZE + GAP, SIZE, "D", mc.gameSettings.keyBindRight.isKeyDown());
        drawKey(mc, x, y + (SIZE + GAP) * 2, SIZE * 3 + GAP * 2, "SPACE", mc.gameSettings.keyBindJump.isKeyDown());
        drawKey(mc, x, y + (SIZE + GAP) * 3 + 3, SIZE + 20, "LMB", Mouse.isButtonDown(0));
        drawKey(mc, x + SIZE + 22, y + (SIZE + GAP) * 3 + 3, SIZE + 20, "RMB", Mouse.isButtonDown(1));
        markBounds(x, y, SIZE * 3 + GAP * 2, (SIZE + GAP) * 3 + 23);
    }

    private void drawKey(Minecraft mc, int x, int y, int width, String label, boolean pressed) {
        AbstractGui.fill(x, y, x + width, y + SIZE, 0xFF000000);
        AbstractGui.fill(x + 1, y + 1, x + width - 1, y + SIZE - 1, pressed ? 0xCC3DA5F5 : 0x99202028);
        int textWidth = mc.fontRenderer.getStringWidth(label);
        mc.fontRenderer.drawStringWithShadow(label, x + (width - textWidth) / 2, y + 6, 0xFFFFFFFF);
    }
}