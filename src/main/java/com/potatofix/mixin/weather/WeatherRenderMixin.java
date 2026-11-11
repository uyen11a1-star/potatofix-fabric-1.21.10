package com.potatofix.mixin.weather;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WeatherRenderMixin {

    @Inject(method = "tickRainSplashing", at = @At("HEAD"), cancellable = true)
    private void onTickRainSplashing(CallbackInfo ci) {
        if (PotatoFixMod.CONFIG.simplifiedWeather) {
            // Hủy việc sinh ra hàng ngàn giọt nước bắn tung tóe dưới đất khi trời mưa
            ci.cancel();
        }
    }
}