package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Mod;
import net.eaglerclient.mods.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;

/**
 * Grants creative-style flight in survival/adventure mode.
 *
 * This reuses vanilla's own double-space-to-toggle-flight logic
 * (ClientPlayerEntity#onLivingUpdate / abilities.allowFlying) instead of
 * re-implementing movement, so behavior (speed, ascend/descend keys,
 * landing auto-disables it, etc.) matches creative mode exactly and the
 * client automatically keeps the server in sync via sendPlayerAbilities().
 *
 * Note: on servers with server-side anti-cheat/movement validation, flying
 * outside creative/spectator will likely get you flagged or kicked. This is
 * intended for singleplayer, LAN, or a server you control/administrate.
 */
public class FlyMod extends Mod {

    // key code 71 = 'G'
    public FlyMod() {
        super("Fly", Category.MOVEMENT, 71);
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            PlayerAbilities abilities = player.abilities;
            abilities.allowFlying = false;
            if (abilities.isFlying) {
                abilities.isFlying = false;
                player.sendPlayerAbilities();
            }
        }
    }

    @Override
    public void onTick(Minecraft mc) {
        super.onTick(mc);
        if (!isEnabled()) {
            return;
        }
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return;
        }
        // Creative/spectator already fly natively; only force it for
        // survival/adventure. Re-asserted every tick because vanilla
        // clears allowFlying automatically once you land.
        if (!player.isCreative() && !player.isSpectator()) {
            player.abilities.allowFlying = true;
        }
    }
}
