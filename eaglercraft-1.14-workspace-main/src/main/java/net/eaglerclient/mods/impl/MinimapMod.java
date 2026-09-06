package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Mod;
import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.HudLayout;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

/**
 * Lightweight top-down minimap drawn in the top-right corner of the HUD.
 * Samples the world's motion-blocking heightmap around the player and
 * draws one square per column using that column's vanilla map color
 * (the same colors used on in-game maps/item frames), so it costs almost
 * nothing to render and needs no separate map-item/paper data.
 */
public class MinimapMod extends Mod {

    private static final int RADIUS_BLOCKS = 64;   // how far around the player to sample
    private static final int PIXEL_SIZE = 2;        // on-screen pixels per world column
    private static final int MARGIN = 8;

    // key code 78 = 'N'
    public MinimapMod() {
        super("Minimap", Category.RENDER, 78);
    }

    @Override
    public void onRenderOverlay(Minecraft mc, int scaledWidth, int scaledHeight) {
        World world = mc.world;
        if (world == null || mc.player == null) {
            return;
        }

        int diameter = (RADIUS_BLOCKS * 2 + 1) * PIXEL_SIZE / 4; // downsample 4 blocks -> 1 pixel row/col
        int size = diameter;
        int mapX = HudLayout.minimapX < 0 ? scaledWidth - MARGIN - size : HudLayout.minimapX;
        int mapY = HudLayout.minimapY;

        int px = (int) Math.floor(mc.player.posX);
        int pz = (int) Math.floor(mc.player.posZ);

        // background
        AbstractGui.fill(mapX - 2, mapY - 2, mapX + size + 2, mapY + size + 2, 0xA0000000);
        AbstractGui.fill(mapX - 1, mapY - 1, mapX + size + 1, mapY, 0xFFFFFFFF);
        AbstractGui.fill(mapX - 1, mapY + size, mapX + size + 1, mapY + size + 1, 0xFFFFFFFF);
        AbstractGui.fill(mapX - 1, mapY, mapX, mapY + size, 0xFFFFFFFF);
        AbstractGui.fill(mapX + size, mapY, mapX + size + 1, mapY + size, 0xFFFFFFFF);

        int step = 4; // sample every 4 blocks to keep this cheap
        int cells = (RADIUS_BLOCKS * 2) / step;
        int cellPixels = Math.max(1, size / cells);

        for (int cx = 0; cx <= cells; cx++) {
            for (int cz = 0; cz <= cells; cz++) {
                int worldX = px - RADIUS_BLOCKS + cx * step;
                int worldZ = pz - RADIUS_BLOCKS + cz * step;
                if (!world.isBlockLoaded(new BlockPos(worldX, 0, worldZ))) {
                    continue;
                }
                int topY = world.getHeight(Heightmap.Type.MOTION_BLOCKING, worldX, worldZ) - 1;
                if (topY < 0) {
                    continue;
                }
                BlockPos pos = new BlockPos(worldX, topY, worldZ);
                BlockState state = world.getBlockState(pos);
                MaterialColor color = state.getMaterialColor(world, pos);
                int argb = 0xFF000000 | (color.colorValue & 0xFFFFFF);

                int drawX = mapX + cx * cellPixels;
                int drawY = mapY + cz * cellPixels;
                AbstractGui.fill(drawX, drawY, drawX + cellPixels, drawY + cellPixels, argb);
            }
        }

        // player marker (centered, since the map is player-centered)
        int centerX = mapX + size / 2;
        int centerY = mapY + size / 2;
        AbstractGui.fill(centerX - 1, centerY - 1, centerX + 2, centerY + 2, 0xFFFFFFFF);
        AbstractGui.fill(centerX - 5, centerY, centerX + 6, centerY + 1, 0xFFFFFFFF);
        AbstractGui.fill(centerX, centerY - 5, centerX + 1, centerY + 6, 0xFFFFFFFF);
        markBounds(mapX - 2, mapY - 2, size + 4, size + 4);
    }
}
