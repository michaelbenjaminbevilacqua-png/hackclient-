package net.eaglerclient.mods;

import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.EagRuntime;

public final class ClientConfig {

    private static final String STORAGE_KEY = "eaglerclient_config";
    private static boolean loaded;
    private static boolean loading;

    private ClientConfig() {
    }

    public static boolean isLoading() {
        return loading;
    }

    public static void load() {
        if (loaded) return;
        loaded = true;
        byte[] data = EagRuntime.getStorage(STORAGE_KEY);
        if (data == null) return;
        loading = true;
        try {
            String[] entries = new String(data, StandardCharsets.UTF_8).split("\\n");
            for (String entry : entries) {
                String[] fields = entry.split("\\|", -1);
                if (fields.length != 3) continue;
                for (Mod mod : ModManager.getMods()) {
                    if (mod.getName().equals(fields[0])) {
                        mod.setEnabled(Boolean.parseBoolean(fields[1]));
                        mod.bindKey(Integer.parseInt(fields[2]), -1);
                        break;
                    }
                }
            }
        } catch (RuntimeException ignored) {
            // Ignore malformed or older client configuration data.
        } finally {
            loading = false;
        }
    }

    public static void save() {
        StringBuilder config = new StringBuilder();
        for (Mod mod : ModManager.getMods()) {
            config.append(mod.getName()).append('|').append(mod.isEnabled()).append('|')
                    .append(mod.isUnbound() ? -1 : mod.getKeyCode()).append('\n');
        }
        EagRuntime.setStorage(STORAGE_KEY, config.toString().getBytes(StandardCharsets.UTF_8));
    }
}
