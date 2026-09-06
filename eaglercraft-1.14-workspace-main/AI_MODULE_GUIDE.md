# Module guide for GitHub AI

## Project rules

- This is an Eaglercraft 1.14.4 client. Keep browser compatibility in mind.
- Put modules in `src/main/java/net/eaglerclient/mods/impl/`.
- Use `Mod` as the base class and assign a `Category`.
- Register every module in `ModManager`.
- Use `onTick` for state changes and `onRenderOverlay` for HUD work.
- Guard `mc.player`, `mc.world`, and any renderer access that may be unavailable during loading screens.
- Do not add packet spoofing, anti-cheat bypasses, or combat automation targeting other players.
- Build with Java 17 and one Gradle worker in this Codespace:

```bash
./gradlew --no-daemon --max-workers=1 -Dorg.gradle.jvmargs='-Xmx2G -Xms512M' :target_teavm_javascript:makeMainOfflineDownload
```

## Module template

```java
public class ExampleMod extends Mod {
    public ExampleMod() {
        super("Example", Category.RENDER, 0);
    }

    @Override
    public void onTick(Minecraft mc) {
        super.onTick(mc);
        if (!isEnabled() || mc.player == null || mc.world == null) return;
    }

    @Override
    public void onRenderOverlay(Minecraft mc, int width, int height) {
        if (mc.player == null || mc.world == null) return;
    }
}
```

## GUI and HUD

- Right Shift opens `ModMenuScreen`.
- `ModMenuScreen` provides category tabs and the `Move HUD` button.
- `HudLayoutScreen` provides drag positioning for HUD elements.
- Use `HudPanel.draw(...)` for dark translucent information panels.
- Add shared HUD coordinates to `HudLayout` so the mover can edit them.

## Existing modules

- Movement: Fly, AutoSprint
- Render: Xray, Minimap, Fullbright, Coordinates, FPS, StatusPanel

When adding a module, keep the change focused, compile `:target_teavm_javascript:compileJava` first, then build the browser bundle.
