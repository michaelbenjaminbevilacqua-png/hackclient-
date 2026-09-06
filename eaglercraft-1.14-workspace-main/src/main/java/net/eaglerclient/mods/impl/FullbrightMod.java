package net.eaglerclient.mods.impl;

import net.eaglerclient.mods.Category;
import net.eaglerclient.mods.Mod;
import net.minecraft.client.Minecraft;

public class FullbrightMod extends Mod {

    private double previousGamma;

    public FullbrightMod() {
        super("Fullbright", Category.RENDER, 0);
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getInstance();
        previousGamma = mc.gameSettings.gamma;
        mc.gameSettings.gamma = 16.0D;
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getInstance();
        mc.gameSettings.gamma = previousGamma;
    }
}