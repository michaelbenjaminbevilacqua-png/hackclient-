package net.eaglerclient.mods;

import net.eaglerclient.mods.impl.CoordinatesMod;
import net.eaglerclient.mods.impl.FpsMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class HudLayoutScreen extends Screen {

    private final Screen parent;
    private String dragging;
    private int dragOffsetX;
    private int dragOffsetY;
    private int draggingWidth;

    public HudLayoutScreen(Screen parent) {
        super(new StringTextComponent("Move HUD Elements"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        HudLayout.initialize(this.width, this.height);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = hitTest(mouseX, mouseY);
            if (dragging != null) {
                Mod mod = findMod(dragging);
                dragOffsetX = (int) mouseX - mod.getLastX();
                dragOffsetY = (int) mouseY - mod.getLastY();
                draggingWidth = mod.getLastWidth();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (dragging != null && button == 0) {
            setPosition(dragging, (int) mouseX - dragOffsetX, (int) mouseY - dragOffsetY, draggingWidth);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private String hitTest(double mouseX, double mouseY) {
        for (Mod mod : ModManager.getMods()) {
            if (isEditorVisible(mod) && mouseX >= mod.getLastX() && mouseX <= mod.getLastX() + mod.getLastWidth()
                    && mouseY >= mod.getLastY() && mouseY <= mod.getLastY() + mod.getLastHeight()) {
                return mod.getName();
            }
        }
        return null;
    }

    private boolean isEditorVisible(Mod mod) {
        return mod.isEnabled() && mod.getLastWidth() > 0
                && !(mod instanceof FpsMod && ModManager.STATUS_PANEL.isEnabled())
                && !(mod instanceof CoordinatesMod && ModManager.STATUS_PANEL.isEnabled());
    }

    private Mod findMod(String name) {
        for (Mod mod : ModManager.getMods()) {
            if (mod.getName().equals(name)) return mod;
        }
        return null;
    }

    private void setPosition(String name, int x, int y, int width) {
        if (name.equals("StatusPanel")) {
            HudLayout.statusPanelX = x + width;
            HudLayout.statusPanelY = y;
        } else if (name.equals("Coordinates")) {
            HudLayout.coordinatesX = x;
            HudLayout.coordinatesY = y;
        } else if (name.equals("FPS")) {
            HudLayout.fpsX = x;
            HudLayout.fpsY = y;
        } else if (name.equals("Keystrokes")) {
            HudLayout.keystrokesX = x;
            HudLayout.keystrokesY = y;
        } else if (name.equals("Minimap")) {
            HudLayout.minimapX = x;
            HudLayout.minimapY = y;
        }
        HudLayout.save();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        HudLayout.initialize(this.width, this.height);
        this.drawCenteredString(this.font, this.title.getString(), this.width / 2, 18, 0xFFFFFF);
        this.drawCenteredString(this.font, "Drag the live widgets. Escape when finished.", this.width / 2, 32, 0xFFB8C0CC);

        for (Mod mod : ModManager.getMods()) {
            if (!isEditorVisible(mod)) continue;
            mod.onRenderOverlay(this.mc, this.width, this.height);
            int x = mod.getLastX();
            int y = mod.getLastY();
            int width = mod.getLastWidth();
            int height = mod.getLastHeight();
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
            int color = hovered ? 0xFF5CC8FF : 0xFF8A96A3;
            this.hLine(x, x + width, y, color);
            this.hLine(x, x + width, y + height, color);
            this.vLine(x, y, y + height, color);
            this.vLine(x + width, y, y + height, color);
            this.drawString(this.font, mod.getName(), x, Math.max(42, y - 10), 0xFFFFFFFF);
        }
    }

    @Override
    public void onClose() {
        this.mc.displayGuiScreen(this.parent);
    }
}
