package com.potatofix.mixin.weather;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticlesMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherRendering.class)
public class WeatherRenderMixin {

    // 1.21.10: tickRainSplashing đã bị bỏ, logic hạt mưa + âm thanh nằm ở addParticlesAndSound
    @Inject(method = "addParticlesAndSound(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/render/Camera;ILnet/minecraft/particle/ParticlesMode;)V",
            at = @At("HEAD"), cancellable = true)
    private void onAddParticlesAndSound(ClientWorld world, Camera camera, int ticks, ParticlesMode mode, CallbackInfo ci) {
        if (PotatoFixMod.CONFIG.simplifiedWeather) {
            ci.cancel();
        }
    }
}
