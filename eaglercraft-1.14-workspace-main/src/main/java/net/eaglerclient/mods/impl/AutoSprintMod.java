package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class AutoSprintMod extends Mod {

    public AutoSprintMod() {
        super("AutoSprint", Category.MOVEMENT, 0);
    }

    @Override
    public void onTick(Minecraft mc) {
        super.onTick(mc);
        if (!isEnabled() || mc.player == null || mc.player.movementInput == null) {
            return;
        }
        ClientPlayerEntity player = mc.player;
        if (player.movementInput.moveForward > 0.0F && !player.movementInput.sneak && !player.isHandActive()) {
            player.setSprinting(true);
        }
    }
}