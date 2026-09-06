package net.eaglerclient.mods.impl;

import java.util.List;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityESPMod extends Mod {

    public EntityESPMod() {
        super("EntityESP", Category.RENDER, 0);
    }

    @Override
    public void onRenderOverlay(Minecraft mc, int scaledWidth, int scaledHeight) {
        if (mc.player == null || mc.world == null) return;
        List<Entity> entities = mc.world.getEntitiesInAABBexcluding(mc.player,
                mc.player.getBoundingBox().grow(32.0D), entity -> !(entity instanceof PlayerEntity));
        int y = scaledHeight / 2 - 40;
        for (Entity entity : entities) {
            if (!entity.isAlive() || y > scaledHeight - 20) continue;
            double distance = mc.player.getDistance(entity);
            String text = entity.getName().getString() + " " + (int) distance + "m";
            mc.fontRenderer.drawStringWithShadow(text, 6.0F, y, 0xFFFFC857);
            y += 10;
        }
    }

    @Override
    public void onRenderWorld(Minecraft mc, ActiveRenderInfo camera, float partialTicks) {
        if (mc.player == null || mc.world == null) return;
        List<Entity> entities = mc.world.getEntitiesInAABBexcluding(mc.player,
                mc.player.getBoundingBox().grow(48.0D), entity -> !(entity instanceof PlayerEntity));
        double cx = camera.getProjectedView().x;
        double cy = camera.getProjectedView().y;
        double cz = camera.getProjectedView().z;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture();
        GlStateManager.disableDepthTest();
        GlStateManager.enableBlend();
        GlStateManager.lineWidth(2.0F);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        for (Entity entity : entities) {
            if (!entity.isAlive()) continue;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - cx;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - cy;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - cz;
            buffer.pos(0.0D, 0.0D, 0.0D).color(1.0F, 0.85F, 0.2F, 0.9F).endVertex();
            buffer.pos(x, y + entity.getHeight() * 0.5D, z).color(1.0F, 0.35F, 0.15F, 0.9F).endVertex();
        }
        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
        GlStateManager.enableDepthTest();
        GlStateManager.enableTexture();
        GlStateManager.popMatrix();
    }
}