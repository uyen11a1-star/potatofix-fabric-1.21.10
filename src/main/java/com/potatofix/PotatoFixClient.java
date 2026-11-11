package com.potatofix;

import com.potatofix.dynamic.DynamicRenderDistanceHandler;
import com.potatofix.hud.PerformanceMiniHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PotatoFixClient implements ClientModInitializer {
    public static KeyBinding openConfigKey;
    public static KeyBinding toggleHudKey;

    @Override
    public void onInitializeClient() {
        PotatoFixMod.LOGGER.info("[PotatoFix Client] Đăng ký phím tắt & vòng lặp giám sát FPS/TPS...");

        // Phím tắt mở menu cấu hình nhanh (Mặc định: Phím O)
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.potatofix.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                "category.potatofix"
        ));

        // Phím tắt bật/tắt Mini HUD (Mặc định: Phím F8)
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.potatofix.toggle_hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F8,
                "category.potatofix"
        ));

        // Giám sát Client Tick để tự hạ render distance nếu lag
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (PotatoFixMod.CONFIG.dynamicRenderDistance) {
                DynamicRenderDistanceHandler.tick(client);
            }
        });
    }
}