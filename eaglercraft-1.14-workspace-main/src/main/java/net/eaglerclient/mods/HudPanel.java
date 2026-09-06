package net.eaglerclient.mods;

import java.util.List;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;

public final class HudPanel {

    private static final int PADDING = 4;
    private static final int LINE_HEIGHT = 10;

    private HudPanel() {
    }

    public static int draw(FontRenderer font, int x, int y, List<String> lines, boolean alignRight) {
        int width = 0;
        for (String line : lines) {
            width = Math.max(width, font.getStringWidth(line));
        }
        int boxWidth = width + PADDING * 2;
        int boxHeight = lines.size() * LINE_HEIGHT + PADDING * 2;
        int drawX = alignRight ? x - boxWidth : x;
        AbstractGui.fill(drawX, y, drawX + boxWidth, y + boxHeight, 0x99101014);
        AbstractGui.fill(drawX, y, drawX + boxWidth, y + 1, 0xFF5CC8FF);
        for (int i = 0; i < lines.size(); ++i) {
            font.drawStringWithShadow(lines.get(i), drawX + PADDING, y + PADDING + i * LINE_HEIGHT, 0xFFFFFFFF);
        }
        return boxHeight;
    }
}