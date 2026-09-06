package net.eaglerclient.mods;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class ModMenuScreen extends Screen {

    private static final int PANEL_WIDTH = 430;
    private static final int ROW_HEIGHT = 22;
    private static final int ROW_GAP = 5;
    private static final int PADDING = 14;
    private final Screen parent;
    private Category selectedCategory = Category.MOVEMENT;
    private Mod listeningMod;
    private int scrollOffset;
    private int panelLeft;
    private int panelTop;
    private int panelBottom;
    private int listTop;
    private int listBottom;

    public ModMenuScreen(Screen parent) {
        super(new StringTextComponent("EaglerClient Mods"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        rebuild();
    }

    private void rebuild() {
        this.buttons.clear();
        this.children.clear();
        int panelWidth = Math.min(PANEL_WIDTH, this.width - 24);
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = 24;
        panelBottom = this.height - 24;
        listTop = panelTop + 82;
        listBottom = panelBottom - 56;

        Category[] categories = Category.values();
        int tabWidth = (panelWidth - PADDING * 2) / categories.length;
        for (int i = 0; i < categories.length; ++i) {
            addCategoryButton(categories[i], panelLeft + PADDING + i * tabWidth, panelTop + 42, tabWidth - 3);
        }

        int contentHeight = ModManager.getMods(selectedCategory).size() * (ROW_HEIGHT + ROW_GAP);
        int maxScroll = Math.max(0, contentHeight - (listBottom - listTop));
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
        int index = 0;
        for (Mod mod : ModManager.getMods(selectedCategory)) {
            int rowY = listTop + index * (ROW_HEIGHT + ROW_GAP) - scrollOffset;
            if (rowY + ROW_HEIGHT >= listTop && rowY <= listBottom) {
                addModButton(mod, rowY, panelWidth);
            }
            ++index;
        }
        this.addButton(new Button(panelLeft + PADDING, panelBottom - 42, (panelWidth - PADDING * 2 - 6) / 2, 24, "Move HUD", button -> {
            this.mc.displayGuiScreen(new HudLayoutScreen(this));
        }));
        this.addButton(new Button(panelLeft + PADDING + (panelWidth - PADDING * 2 - 6) / 2 + 6, panelBottom - 42,
                (panelWidth - PADDING * 2 - 6) / 2, 24, "Done", button -> closeMenu()));
    }

    private void addCategoryButton(Category category, int x, int y, int width) {
        String label = category == selectedCategory ? "> " + categoryLabel(category) : categoryLabel(category);
        this.addButton(new Button(x, y, width, 24, label, button -> {
            selectedCategory = category;
            scrollOffset = 0;
            rebuild();
        }));
    }

    private String categoryLabel(Category category) {
        String text = category.name().toLowerCase();
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    private void addModButton(Mod mod, int y, int panelWidth) {
        final Button[] buttonRef = new Button[1];
        int rowWidth = panelWidth - PADDING * 2;
        int keyWidth = 118;
        buttonRef[0] = this.addButton(new Button(panelLeft + PADDING, y, rowWidth - keyWidth - 6, ROW_HEIGHT,
                getButtonText(mod), ignored -> {
                    mod.toggle();
                    buttonText(buttonRef[0], mod);
                }));
        this.addButton(new Button(panelLeft + PADDING + rowWidth - keyWidth, y, keyWidth, ROW_HEIGHT, "Key: " + mod.getKeyName(), ignored -> {
            listeningMod = mod;
            ignored.setMessage("Press key / Backspace clears");
        }));
    }

    private void buttonText(Button button, Mod mod) {
        if (button != null) {
            button.setMessage(getButtonText(mod));
        }
    }

    private String getButtonText(Mod mod) {
        return mod.getName() + ": " + (mod.isEnabled() ? "ON" : "OFF");
    }

    private void closeMenu() {
        this.mc.displayGuiScreen(this.parent);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningMod != null) {
            if (keyCode == 256) {
                listeningMod = null;
                rebuild();
                return true;
            }
            if (keyCode != 344) {
                listeningMod.bindKey(keyCode, scanCode);
                listeningMod = null;
                rebuild();
                return true;
            }
        }
        if (keyCode == 344) {
            closeMenu();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        this.fill(panelLeft, panelTop, panelLeft + PANEL_WIDTH, panelBottom, 0xE610121A);
        this.fill(panelLeft, panelTop, panelLeft + PANEL_WIDTH, panelTop + 3, 0xFF5CC8FF);
        this.hLine(panelLeft, panelLeft + PANEL_WIDTH, panelBottom, 0xFF263342);
        this.drawString(this.font, "EAGLERCLIENT", panelLeft + PADDING, panelTop + 14, 0xFF7DD3FC);
        this.drawString(this.font, "Modules", panelLeft + PADDING, panelTop + 29, 0xFFFFFFFF);
        this.drawString(this.font, "Scroll to browse", panelLeft + PANEL_WIDTH - 94, panelTop + 18, 0xFF8D9AAA);
        this.fill(panelLeft + PADDING, listTop - 7, panelLeft + PANEL_WIDTH - PADDING, listBottom + 4, 0x441C2633);
        this.drawString(this.font, "Right Shift closes", panelLeft + PADDING, panelBottom - 14, 0xFF8D9AAA);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int contentHeight = ModManager.getMods(selectedCategory).size() * (ROW_HEIGHT + ROW_GAP);
        int maxScroll = Math.max(0, contentHeight - (listBottom - listTop));
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int) (amount * (ROW_HEIGHT + ROW_GAP))));
        rebuild();
        return true;
    }
}