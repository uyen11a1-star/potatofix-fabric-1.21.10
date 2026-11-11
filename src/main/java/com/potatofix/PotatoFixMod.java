package com.potatofix;

import com.potatofix.config.PotatoConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PotatoFixMod implements ModInitializer {
    public static final String MOD_ID = "potatofix";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static PotatoConfig CONFIG;

    @Override
    public void onInitialize() {
        LOGGER.info("[PotatoFix 1.21.10] Đang khởi động lõi tối ưu hóa cho máy yếu...");
        CONFIG = PotatoConfig.loadOrCreate();
        LOGGER.info("[PotatoFix 1.21.10] Đã nạp cấu hình: Entity Culling = {}, RAM Opt = {}, Particle Limit = {}",
                CONFIG.entityCullingEnabled, CONFIG.gcAggressiveEviction, CONFIG.particleLimit);
    }
}