package net.eaglerclient.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.renderer.ActiveRenderInfo;

/**
 * Base class for a toggleable client-side mod/feature.
 * Each mod owns a KeyBinding (self-registers with vanilla's KeyBinding
 * system, so it's polled automatically every tick with no other wiring
 * needed) and a simple enabled flag.
 */
public abstract class Mod {

    private final String name;
    private final Category category;
    private final KeyBinding toggleKey;
    private boolean enabled = false;
    private boolean unbound;
    private int lastX;
    private int lastY;
    private int lastWidth;
    private int lastHeight;

    public Mod(String name, int defaultKeyCode) {
        this(name, Category.MISC, defaultKeyCode);
    }

    public Mod(String name, Category category, int defaultKeyCode) {
        this.name = name;
        this.category = category;
        // "key.categories.misc" re-uses a vanilla category so the bind
        // doesn't crash the controls screen if it ever gets listed there.
        this.unbound = defaultKeyCode == 0;
        this.toggleKey = new KeyBinding("key.eaglerclient." + name.toLowerCase(),
            InputMappings.Type.KEYSYM, this.unbound ? -1 : defaultKeyCode, "key.categories.misc");
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getKeyName() {
        return unbound ? "None" : toggleKey.getLocalizedName();
    }

    public boolean isKeyDown() {
        return toggleKey.isKeyDown();
    }

    public void bindKey(int keyCode, int scanCode) {
        if (keyCode < 0 || keyCode == 259 || keyCode == 256) {
            unbound = true;
            toggleKey.bind(InputMappings.INPUT_INVALID);
            KeyBinding.resetKeyBindingArrayAndHash();
            if (!ClientConfig.isLoading()) ClientConfig.save();
            return;
        }
        unbound = false;
        toggleKey.bind(InputMappings.getInputByCode(keyCode, scanCode));
        KeyBinding.resetKeyBindingArrayAndHash();
        if (!ClientConfig.isLoading()) ClientConfig.save();
    }

    public boolean isUnbound() {
        return unbound;
    }

    public int getKeyCode() {
        return toggleKey.getKeyCode();
    }

    protected void markBounds(int x, int y, int width, int height) {
        lastX = x;
        lastY = y;
        lastWidth = width;
        lastHeight = height;
    }

    public int getLastX() { return lastX; }
    public int getLastY() { return lastY; }
    public int getLastWidth() { return lastWidth; }
    public int getLastHeight() { return lastHeight; }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        if (value == enabled) {
            return;
        }
        enabled = value;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!enabled);
        if (!ClientConfig.isLoading()) ClientConfig.save();
    }

    /** Called once when the mod is switched on. */
    protected void onEnable() {
    }

    /** Called once when the mod is switched off. */
    protected void onDisable() {
    }

    /** Called every client tick while the game is running, regardless of enabled state. */
    public void onTick(Minecraft mc) {
        if (!unbound && toggleKey.isPressed()) {
            toggle();
        }
    }

    /** Called every frame after the HUD is drawn, for mods that draw their own overlay. */
    public void onRenderOverlay(Minecraft mc, int scaledWidth, int scaledHeight) {
    }

    public void onRenderWorld(Minecraft mc, ActiveRenderInfo camera, float partialTicks) {
    }
}
