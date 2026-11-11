package com.potatofix.dynamic;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.MinecraftClient;

public class DynamicRenderDistanceHandler {
    private static int tickCounter = 0;

    public static void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        tickCounter++;
        // Kiểm tra mỗi 100 ticks (5 giây)
        if (tickCounter % 100 != 0) return;

        int currentFps = client.getCurrentFps();
        int currentDistance = client.options.getViewDistance().getValue();

        // Nếu FPS rơi xuống dưới 20 -> Tự động giảm 1 chunk để cứu khung hình
        if (currentFps < 20 && currentDistance > PotatoFixMod.CONFIG.minRenderDistance) {
            client.options.getViewDistance().setValue(currentDistance - 1);
            PotatoFixMod.LOGGER.info("[PotatoFix Dynamic] FPS thấp ({} FPS), giảm Render Distance xuống {} chunks",
                    currentFps, currentDistance - 1);
        } else if (currentFps > 55 && currentDistance < PotatoFixMod.CONFIG.maxRenderDistance) {
            // Khi máy đã mượt trở lại -> Tăng dần
            client.options.getViewDistance().setValue(currentDistance + 1);
        }
    }
}