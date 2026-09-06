package net.eaglerclient.mods.impl;

import java.util.ArrayList;
import java.util.List;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.HudLayout;
import net.eaglerclient.mods.HudPanel;
import net.eaglerclient.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;

public class StatusPanelMod extends Mod {

    public StatusPanelMod() {
        super("StatusPanel", Category.RENDER, 0);
    }

    @Override
    public void onRenderOverlay(Minecraft mc, int scaledWidth, int scaledHeight) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        List<String> stats = new ArrayList<>();
        stats.add(Minecraft.getDebugFPS() + " FPS");
        stats.add("XYZ " + (int) mc.player.posX + " / " + (int) mc.player.posY + " / " + (int) mc.player.posZ);
        stats.add("Facing " + facing(mc.player.rotationYaw));
        stats.add("Biome " + mc.world.getBiome(new BlockPos(mc.player)).getDisplayName().getString());
        int panelX = HudLayout.statusPanelX < 0 ? scaledWidth - 6 : HudLayout.statusPanelX;
        int panelHeight = HudPanel.draw(mc.fontRenderer, panelX, HudLayout.statusPanelY, stats, true);
        int panelWidth = 0;
        for (String line : stats) panelWidth = Math.max(panelWidth, mc.fontRenderer.getStringWidth(line));
        markBounds(panelX - panelWidth - 8, HudLayout.statusPanelY, panelWidth + 8, panelHeight);
        if (!mc.player.getActivePotionEffects().isEmpty()) {
            List<String> effects = new ArrayList<>();
            for (net.minecraft.potion.EffectInstance effect : mc.player.getActivePotionEffects()) {
                effects.add(I18n.format(effect.getEffectName()) + " " + ((effect.getDuration() / 20) / 60) + ":"
                        + String.format("%02d", (effect.getDuration() / 20) % 60));
            }
            HudPanel.draw(mc.fontRenderer, 6, HudLayout.statusPanelY + 52, effects, false);
        }
    }

    private String facing(float yaw) {
        String[] directions = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
        return directions[Math.round(yaw / 45.0F) & 7];
    }
}