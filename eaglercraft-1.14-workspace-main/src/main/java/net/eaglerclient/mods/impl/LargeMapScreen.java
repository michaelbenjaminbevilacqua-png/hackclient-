package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.HudLayout;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class LargeMapScreen extends Screen {

    private static final int SIZE = 320;
    private static final int RADIUS = 128;

    public LargeMapScreen(Screen parent) {
        super(new StringTextComponent("Large Minimap"));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        Minecraft mc = this.mc;
        World world = mc.world;
        if (world != null && mc.player != null) {
            int left = (this.width - SIZE) / 2;
            int top = (this.height - SIZE) / 2;
            AbstractGui.fill(left - 3, top - 3, left + SIZE + 3, top + SIZE + 3, 0xFF101014);
            int centerX = (int) mc.player.posX;
            int centerZ = (int) mc.player.posZ;
            int step = Math.max(1, RADIUS * 2 / SIZE);
            for (int x = 0; x < SIZE; ++x) {
                for (int z = 0; z < SIZE; ++z) {
                    int worldX = centerX - RADIUS + x * step;
                    int worldZ = centerZ - RADIUS + z * step;
                    if (!world.isBlockLoaded(new BlockPos(worldX, 0, worldZ))) continue;
                    int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING, worldX, worldZ) - 1;
                    if (y < 0) continue;
                    BlockPos pos = new BlockPos(worldX, y, worldZ);
                    BlockState state = world.getBlockState(pos);
                    MaterialColor color = state.getMaterialColor(world, pos);
                    AbstractGui.fill(left + x, top + z, left + x + 1, top + z + 1,
                            0xFF000000 | (color.colorValue & 0xFFFFFF));
                }
            }
            AbstractGui.fill(left + SIZE / 2 - 2, top + SIZE / 2 - 2, left + SIZE / 2 + 3, top + SIZE / 2 + 3, 0xFFFFFFFF);
            this.drawCenteredString(this.font, "Large Minimap - Escape to close", this.width / 2, top - 18, 0xFFFFFFFF);
        }
    }
}