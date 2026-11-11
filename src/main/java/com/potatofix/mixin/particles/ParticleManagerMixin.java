package com.potatofix.mixin.particles;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Shadow
    private Map<?, Queue<Particle>> particles;

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(Particle particle, CallbackInfo ci) {
        if (particle == null) return;

        int limit = PotatoFixMod.CONFIG.particleLimit;
        if (limit <= 0) {
            ci.cancel(); // Tắt toàn bộ hạt
            return;
        }

        // Đếm sơ bộ số lượng hạt hiện có
        int currentCount = 0;
        if (particles != null) {
            for (Queue<Particle> queue : particles.values()) {
                currentCount += queue.size();
                if (currentCount >= limit) {
                    ci.cancel(); // Đã chạm trần cấu hình máy yếu, bỏ qua hạt mới
                    return;
                }
            }
        }
    }
}