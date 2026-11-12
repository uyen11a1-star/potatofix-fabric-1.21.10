package com.potatofix.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.potatofix.PotatoFixMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PotatoConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "potatofix.json");

    // Các thiết lập tối ưu hóa cho máy yếu
    public boolean entityCullingEnabled = true;
    public int entityCullingDistance = 24;
    public boolean tileEntityThrottling = true;
    public int tileEntityDistance = 16;
    public int particleLimit = 250;
    public boolean fastMath = true;
    public boolean dynamicRenderDistance = true;
    public int minRenderDistance = 4;
    public int maxRenderDistance = 8;
    public boolean disableEntityShadows = true;
    public boolean simplifiedWeather = true;
    public boolean fastLightEngine = true;
    public boolean gcAggressiveEviction = true;
    public boolean miniHudEnabled = true;
    // Khi Iris bật shader pack: siết khoảng cách cull và giới hạn hạt
    public boolean shaderAwareProfile = true;
    public float shaderDistanceScale = 0.75f;
    public float shaderParticleScale = 0.5f;
    // Giám sát RAM: thiếu RAM thì tự hạ render distance + giảm hạt; cảnh báo nếu heap quá thấp
    public boolean memoryGuard = true;
    public int lowHeapWarnMb = 1536;

    public static PotatoConfig loadOrCreate() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                PotatoConfig cfg = GSON.fromJson(reader, PotatoConfig.class);
                if (cfg != null) return cfg;
            } catch (Exception e) {
                PotatoFixMod.LOGGER.error("[PotatoFix] Lỗi đọc file config, tiến hành khôi phục mặc định:", e);
            }
        }
        PotatoConfig defaultConfig = new PotatoConfig();
        defaultConfig.save();
        return defaultConfig;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            PotatoFixMod.LOGGER.error("[PotatoFix] Không thể lưu file potatofix.json:", e);
        }
    }
}