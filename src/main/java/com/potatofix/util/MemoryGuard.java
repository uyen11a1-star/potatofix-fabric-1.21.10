package com.potatofix.util;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/**
 * Giám sát RAM heap của game. Heap dùng nhiều nhất/ít nhất dao động theo chu kỳ GC,
 * nên ta lấy mức dùng THẤP NHẤT trong cửa sổ 15 giây (~ lượng dữ liệu còn sống sau GC).
 * Nếu mức đó vượt 80% heap tối đa -> game sắp hết RAM (GC liên tục = khựng, hết hẳn = văng).
 */
public final class MemoryGuard {
    private static final int SAMPLE_TICKS = 20;   // lấy mẫu mỗi 1 giây
    private static final int WINDOW = 15;         // cửa sổ 15 mẫu
    private static final double HIGH = 0.80;      // vào chế độ áp lực
    private static final double LOW = 0.60;       // thoát chế độ áp lực

    private static final long[] samples = new long[WINDOW];
    private static int count = 0;
    private static int idx = 0;
    private static int tickCounter = 0;
    private static boolean warned = false;
    private static volatile boolean pressure = false;

    private MemoryGuard() {}

    public static void tick(MinecraftClient client) {
        if (!PotatoFixMod.CONFIG.memoryGuard) {
            pressure = false;
            return;
        }

        Runtime rt = Runtime.getRuntime();
        long maxMb = rt.maxMemory() / (1024 * 1024);

        // Cảnh báo 1 lần/phiên nếu RAM cấp cho game quá thấp
        if (!warned && client.player != null) {
            warned = true;
            PotatoFixMod.LOGGER.info("[PotatoFix] Heap tối đa của game: {} MB", maxMb);
            if (maxMb < PotatoFixMod.CONFIG.lowHeapWarnMb) {
                client.player.sendMessage(Text.literal("[PotatoFix] RAM cấp cho game chỉ " + maxMb
                        + " MB, dễ khựng/văng. Hãy tăng RAM trong launcher (khoảng 2GB nếu máy 4GB)."), false);
            }
        }

        if (++tickCounter % SAMPLE_TICKS != 0) return;

        samples[idx] = rt.totalMemory() - rt.freeMemory();
        idx = (idx + 1) % WINDOW;
        if (count < WINDOW) count++;
        if (count < WINDOW) return;

        long min = Long.MAX_VALUE;
        for (long s : samples) min = Math.min(min, s);
        double ratio = (double) min / rt.maxMemory();

        if (!pressure && ratio > HIGH) {
            pressure = true;
            PotatoFixMod.LOGGER.warn("[PotatoFix] RAM áp lực cao: {}% heap (tối đa {} MB)", (int) (ratio * 100), maxMb);
        } else if (pressure && ratio < LOW) {
            pressure = false;
            PotatoFixMod.LOGGER.info("[PotatoFix] RAM đã ổn lại: {}% heap", (int) (ratio * 100));
        }
    }

    public static boolean underPressure() {
        return pressure;
    }

    /** Khi thiếu RAM, giảm một nửa giới hạn hạt (0 giữ nguyên). */
    public static int scaleParticleLimit(int limit) {
        if (limit <= 0 || !pressure) return limit;
        return Math.max(1, limit / 2);
    }
}
