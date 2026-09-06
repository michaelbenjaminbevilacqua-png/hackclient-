package net.eaglerclient.mods;

import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.EagRuntime;

public final class HudLayout {

    public static int moduleListX = -1;
    public static int moduleListY = 6;
    public static int minimapX = -1;
    public static int minimapY = 8;
    public static int coordinatesX = 6;
    public static int coordinatesY = -18;
    public static int fpsX = 6;
    public static int fpsY = 6;
    public static int statusPanelX = -1;
    public static int statusPanelY = 36;
    public static int keystrokesX = 8;
    public static int keystrokesY = -110;
    private HudLayout() {
    }

    public static void initialize(int width, int height) {
        load();
        if (minimapX < 0) minimapX = Math.max(8, width - 190);
        if (moduleListX < 0) moduleListX = Math.max(8, width - 6);
        if (statusPanelX < 0) statusPanelX = Math.max(8, width - 6);
    }

    public static void save() {
        String value = moduleListX + "," + moduleListY + ";" + minimapX + "," + minimapY + ";"
                + coordinatesX + "," + coordinatesY + ";" + fpsX + "," + fpsY + ";"
                + statusPanelX + "," + statusPanelY + ";" + keystrokesX + "," + keystrokesY;
        EagRuntime.setStorage("eaglerclient_hud_layout", value.getBytes(StandardCharsets.UTF_8));
    }

    private static boolean loaded;

    private static void load() {
        if (loaded) return;
        loaded = true;
        byte[] data = EagRuntime.getStorage("eaglerclient_hud_layout");
        if (data == null) return;
        try {
            String[] positions = new String(data, StandardCharsets.UTF_8).split(";");
                int[][] values = {{moduleListX, moduleListY}, {minimapX, minimapY}, {coordinatesX, coordinatesY},
                    {fpsX, fpsY}, {statusPanelX, statusPanelY}, {keystrokesX, keystrokesY}};
            for (int i = 0; i < values.length && i < positions.length; ++i) {
                String[] pair = positions[i].split(",");
                if (pair.length == 2) {
                    values[i][0] = Integer.parseInt(pair[0]);
                    values[i][1] = Integer.parseInt(pair[1]);
                }
            }
            moduleListX = values[0][0]; moduleListY = values[0][1];
            minimapX = values[1][0]; minimapY = values[1][1];
            coordinatesX = values[2][0]; coordinatesY = values[2][1];
            fpsX = values[3][0]; fpsY = values[3][1];
            statusPanelX = values[4][0]; statusPanelY = values[4][1];
            keystrokesX = values[5][0]; keystrokesY = values[5][1];
        } catch (RuntimeException ignored) {
            // Ignore malformed old layout data and use defaults.
        }
    }
}