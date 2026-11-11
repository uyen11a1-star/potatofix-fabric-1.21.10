package com.potatofix.dynamic;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

public class DynamicRenderDistanceHandler {
    private static final int CHECK_INTERVAL_TICKS = 100;   // 5 giây
    private static final int RAISE_NEEDED_CHECKS = 3;      // FPS tốt liên tục 15 giây mới tăng
    private static final int RAISE_BLOCK_CHECKS = 12;      // sau khi giảm, chặn tăng 60 giây (tránh dao động)

    private static int tickCounter = 0;
    private static int baseDistance = -1;  // render distance người chơi tự chọn = trần tối đa
    private static int lastSet = -1;       // giá trị mod đặt gần nhất
    private static int badChecks = 0;
    private static int goodChecks = 0;
    private static int raiseBlock = 0;

    public static void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            tickCounter = 0;
            baseDistance = -1;
            lastSet = -1;
            badChecks = goodChecks = raiseBlock = 0;
            return;
        }

        SimpleOption<Integer> option = client.options.getViewDistance();
        int current = option.getValue();

        // Lần đầu, hoặc người chơi tự đổi trong menu -> lấy giá trị đó làm trần mới
        if (lastSet == -1 || current != lastSet) {
            baseDistance = current;
            lastSet = current;
        }

        if (++tickCounter % CHECK_INTERVAL_TICKS != 0) return;

        int fps = client.getCurrentFps();
        int floor = PotatoFixMod.CONFIG.minRenderDistance;
        if (raiseBlock > 0) raiseBlock--;

        if (fps < 20) {
            goodChecks = 0;
            // Phải thấp 2 lần liên tiếp mới giảm (tránh giật tạm thời lúc load chunk)
            if (++badChecks >= 2 && current > floor) {
                badChecks = 0;
                raiseBlock = RAISE_BLOCK_CHECKS;
                setDistance(option, current - 1, fps);
            }
        } else if (fps > 55) {
            badChecks = 0;
            if (++goodChecks >= RAISE_NEEDED_CHECKS && raiseBlock == 0 && current < baseDistance) {
                goodChecks = 0;
                setDistance(option, current + 1, fps);
            }
        } else {
            badChecks = 0;
            goodChecks = 0;
        }
    }

    private static void setDistance(SimpleOption<Integer> option, int value, int fps) {
        option.setValue(value);
        lastSet = value;
        PotatoFixMod.LOGGER.info("[PotatoFix Dynamic] {} FPS -> render distance {} chunks", fps, value);
    }
}
