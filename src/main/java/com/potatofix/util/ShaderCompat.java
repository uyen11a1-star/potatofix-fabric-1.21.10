package com.potatofix.util;

import com.potatofix.PotatoFixMod;

import java.lang.reflect.Method;

/**
 * Phát hiện shader pack của Iris bằng reflection (IrisApi v0 -> isShaderPackInUse),
 * nên không cần Iris lúc compile và vẫn chạy bình thường khi không cài Iris.
 */
public final class ShaderCompat {
    private static boolean initDone = false;
    private static Object api;
    private static Method inUse;
    private static volatile boolean active = false;

    private ShaderCompat() {}

    /** Gọi 1 lần mỗi client tick; kết quả được cache cho các mixin dùng mỗi frame. */
    public static void tick() {
        if (!initDone) {
            initDone = true;
            try {
                Class<?> c = Class.forName("net.irisshaders.iris.api.v0.IrisApi", true, ShaderCompat.class.getClassLoader());
                api = c.getMethod("getInstance").invoke(null);
                inUse = c.getMethod("isShaderPackInUse");
                PotatoFixMod.LOGGER.info("[PotatoFix] Phát hiện Iris, bật chế độ nhận biết shader");
            } catch (Throwable t) {
                api = null;
            }
        }
        if (api == null) {
            active = false;
            return;
        }
        try {
            active = (boolean) inUse.invoke(api);
        } catch (Throwable t) {
            active = false;
        }
    }

    private static boolean shaderProfile() {
        return active && PotatoFixMod.CONFIG.shaderAwareProfile;
    }

    /** Hệ số nhân khoảng cách cull (1.0 nếu không bật shader). */
    public static double distanceScale() {
        return shaderProfile() ? PotatoFixMod.CONFIG.shaderDistanceScale : 1.0;
    }

    /** Giới hạn hạt đã điều chỉnh theo shader (0 giữ nguyên là tắt hết). */
    public static int scaleParticleLimit(int limit) {
        if (limit <= 0 || !shaderProfile()) return limit;
        return Math.max(1, (int) (limit * PotatoFixMod.CONFIG.shaderParticleScale));
    }
}
