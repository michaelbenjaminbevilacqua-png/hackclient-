package net.eaglerclient.mods.impl;

import java.util.HashSet;
import java.util.Set;

import net.eaglerclient.mods.Mod;
import net.eaglerclient.mods.Category;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

/**
 * Classic x-ray: while enabled, the chunk mesh builder simply skips drawing
 * any block that isn't on the whitelist below (see the one-line hook added
 * to BlockRendererDispatcher#func_215330_a). Non-whitelisted terrain becomes
 * fully invisible (still solid/collidable, just not rendered), leaving ores
 * and other "interesting" blocks floating visibly in the terrain.
 *
 * Known limitation: because normal face-culling still treats hidden blocks
 * as opaque neighbors, an ore face that's touching stone on a given side
 * may not draw on that side. Ores exposed to a visible face (air, another
 * ore, etc.) always render correctly. This is the standard trade-off of the
 * "skip the quad" approach and is good enough for spotting ore veins.
 */
public class XrayMod extends Mod {

    private final Set<Block> whitelist = new HashSet<>();

    // key code 88 = 'X'
    public XrayMod() {
        super("Xray", Category.RENDER, 88);
        whitelist.add(Blocks.COAL_ORE);
        whitelist.add(Blocks.IRON_ORE);
        whitelist.add(Blocks.GOLD_ORE);
        whitelist.add(Blocks.DIAMOND_ORE);
        whitelist.add(Blocks.EMERALD_ORE);
        whitelist.add(Blocks.LAPIS_ORE);
        whitelist.add(Blocks.REDSTONE_ORE);
        whitelist.add(Blocks.NETHER_QUARTZ_ORE);
        whitelist.add(Blocks.SPAWNER);
        whitelist.add(Blocks.CHEST);
        whitelist.add(Blocks.TRAPPED_CHEST);
    }

    public boolean isWhitelisted(Block block) {
        return whitelist.contains(block);
    }

    public void addBlock(Block block) {
        whitelist.add(block);
    }

    public void removeBlock(Block block) {
        whitelist.remove(block);
    }

    public Set<Block> getWhitelist() {
        return whitelist;
    }

    @Override
    protected void onEnable() {
        // Force all currently-loaded chunk meshes to rebuild so the effect
        // is immediate instead of only applying to newly-loaded chunks.
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.worldRenderer != null && mc.world != null) {
            mc.worldRenderer.loadRenderers();
        }
    }

    @Override
    protected void onDisable() {
        onEnable(); // same rebuild-everything logic works both ways
    }
}
