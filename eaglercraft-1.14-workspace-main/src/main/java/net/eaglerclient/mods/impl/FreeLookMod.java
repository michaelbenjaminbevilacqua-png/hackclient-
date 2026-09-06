package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.Mod;
import net.minecraft.client.Minecraft;

public class FreeLookMod extends Mod {

    private static float cameraYaw;
    private static float cameraPitch;

    public FreeLookMod() {
        super("FreeLook", Category.RENDER, 0);
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            cameraYaw = mc.player.rotationYaw;
            cameraPitch = mc.player.rotationPitch;
        }
    }

    public static float getCameraYaw(float fallback) {
        return ModManagerAccess.enabled() ? cameraYaw : fallback;
    }

    public static float getCameraPitch(float fallback) {
        return ModManagerAccess.enabled() ? cameraPitch : fallback;
    }

    public static void rotate(double yawDelta, double pitchDelta) {
        cameraYaw += (float) yawDelta;
        cameraPitch += (float) pitchDelta;
        if (cameraPitch > 90.0F) cameraPitch = 90.0F;
        if (cameraPitch < -90.0F) cameraPitch = -90.0F;
    }

    private static final class ModManagerAccess {
        private static boolean enabled() {
            return net.eaglerclient.mods.ModManager.FREELOOK.isEnabled();
        }
    }
}