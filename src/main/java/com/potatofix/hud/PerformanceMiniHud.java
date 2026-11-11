package com.potatofix.hud;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class PerformanceMiniHud {
    private static long lastUpdateTime = 0;
    private static String cachedHudText = "";

    public static void render(DrawContext context, float tickDelta) {
        if (!PotatoFixMod.CONFIG.miniHudEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.debugEnabled) return; // Nếu đang bật F3 to thì ẩn mini HUD

        long now = System.currentTimeMillis();
        if (now - lastUpdateTime > 500) { // Cập nhật text mỗi 500ms để không tốn CPU
            lastUpdateTime = now;
            int fps = client.getCurrentFps();
            long maxMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
            long totalMem = Runtime.getRuntime().totalMemory() / (1024 * 1024);
            long freeMem = Runtime.getRuntime().freeMemory() / (1024 * 1024);
            long usedMem = totalMem - freeMem;

            cachedHudText = String.format("PotatoFix 1.21.10 | %d FPS | RAM: %dMB/%dMB", fps, usedMem, maxMem);
        }

        // Vẽ text màu xanh dịu lên góc trái trên cùng
        context.drawTextWithShadow(client.textRenderer, cachedHudText, 4, 4, 0x00FF88);
    }
}