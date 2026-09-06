package net.eaglerclient.mods;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.eaglerclient.mods.impl.FlyMod;
import net.eaglerclient.mods.impl.MinimapMod;
import net.eaglerclient.mods.impl.AutoSprintMod;
import net.eaglerclient.mods.impl.CoordinatesMod;
import net.eaglerclient.mods.impl.FullbrightMod;
import net.eaglerclient.mods.impl.FpsMod;
import net.eaglerclient.mods.impl.StatusPanelMod;
import net.eaglerclient.mods.impl.KeystrokesMod;
import net.eaglerclient.mods.impl.FreeLookMod;
import net.eaglerclient.mods.impl.LargeMapMod;
import net.eaglerclient.mods.impl.EntityESPMod;
import net.eaglerclient.mods.impl.XrayMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;

/**
 * Single entry point the vanilla client code calls into. Everything mod
 * related lives under net.eaglerclient.mods so it's easy to find/patch,
 * strip out, or extend later (e.g. wiring up a proper mod-menu GUI).
 *
 * Only 3 one-line hooks were added to vanilla code to support this:
 *   1. Minecraft#processKeyBinds()      -> ModManager.onTick(this)
 *   2. IngameGui#renderGameOverlay()    -> ModManager.onRenderOverlay(...)
 *   3. BlockRendererDispatcher#func_215330_a() -> xray visibility check
 */
public class ModManager {

    public static final FlyMod FLY = new FlyMod();
    public static final XrayMod XRAY = new XrayMod();
    public static final MinimapMod MINIMAP = new MinimapMod();
    public static final AutoSprintMod AUTO_SPRINT = new AutoSprintMod();
    public static final FullbrightMod FULLBRIGHT = new FullbrightMod();
    public static final CoordinatesMod COORDINATES = new CoordinatesMod();
    public static final FpsMod FPS = new FpsMod();
    public static final StatusPanelMod STATUS_PANEL = new StatusPanelMod();
    public static final KeystrokesMod KEYSTROKES = new KeystrokesMod();
    public static final FreeLookMod FREELOOK = new FreeLookMod();
    public static final LargeMapMod LARGE_MAP = new LargeMapMod();
    public static final EntityESPMod ENTITY_ESP = new EntityESPMod();
    private static final KeyBinding MENU_KEY = new KeyBinding("key.eaglerclient.menu", 344, "key.categories.misc");

    private static final List<Mod> MODS = new ArrayList<>();
    static {
        MODS.add(FLY);
        MODS.add(XRAY);
        MODS.add(MINIMAP);
        MODS.add(AUTO_SPRINT);
        MODS.add(FULLBRIGHT);
        MODS.add(COORDINATES);
        MODS.add(FPS);
        MODS.add(STATUS_PANEL);
        MODS.add(KEYSTROKES);
        MODS.add(FREELOOK);
        MODS.add(LARGE_MAP);
        MODS.add(ENTITY_ESP);
    }

    public static List<Mod> getMods() {
        return MODS;
    }

    public static List<Mod> getMods(Category category) {
        List<Mod> result = new ArrayList<>();
        for (Mod mod : MODS) {
            if (mod.getCategory() == category) {
                result.add(mod);
            }
        }
        return result;
    }

    public static void onTick(Minecraft mc) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) {
            return;
        }
        ClientConfig.load();
        if (MENU_KEY.isPressed()) {
            mc.displayGuiScreen(new ModMenuScreen(null));
            return;
        }
        for (Mod mod : MODS) {
            mod.onTick(mc);
        }
    }

    public static void onRenderOverlay(Minecraft mc, int scaledWidth, int scaledHeight) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null) {
            return;
        }
        HudLayout.initialize(scaledWidth, scaledHeight);
        for (Mod mod : MODS) {
            if (mod.isEnabled()) {
                if (STATUS_PANEL.isEnabled() && (mod == FPS || mod == COORDINATES)) {
                    continue;
                }
                mod.onRenderOverlay(mc, scaledWidth, scaledHeight);
            }
        }
        int y = HudLayout.moduleListY;
        int moduleX = HudLayout.moduleListX < 0 ? scaledWidth - 6 : HudLayout.moduleListX;
        for (Mod mod : MODS) {
            if (mod.isEnabled()) {
                String label = mod.getName();
                int width = mc.fontRenderer.getStringWidth(label);
                mc.fontRenderer.drawStringWithShadow(label, moduleX - width, y, categoryColor(mod.getCategory()));
                y += 10;
            }
        }
    }

    public static void onRenderWorld(Minecraft mc, ActiveRenderInfo camera, float partialTicks) {
        if (mc.player == null || mc.world == null || camera == null || !camera.isValid()) return;
        for (Mod mod : MODS) {
            if (mod.isEnabled()) mod.onRenderWorld(mc, camera, partialTicks);
        }
    }

    private static int categoryColor(Category category) {
        switch (category) {
        case MOVEMENT:
            return 0xFF5CC8FF;
        case RENDER:
            return 0xFFB58CFF;
        case PLAYER:
            return 0xFFFFC857;
        default:
            return 0xFF8DE28D;
        }
    }
}
