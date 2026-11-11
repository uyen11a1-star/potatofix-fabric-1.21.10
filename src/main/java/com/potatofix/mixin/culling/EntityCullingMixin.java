package com.potatofix.mixin.culling;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderManager.class, priority = 950)
public class EntityCullingMixin {

    // 1.21.10: render() dùng render state, không còn Entity -> chặn ở shouldRender (x,y,z = vị trí camera)
    @Inject(method = "shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/Frustum;DDD)Z",
            at = @At("HEAD"), cancellable = true)
    private void onShouldRender(Entity entity, Frustum frustum, double x, double y, double z,
                                CallbackInfoReturnable<Boolean> cir) {
        if (!PotatoFixMod.CONFIG.entityCullingEnabled) return;
        if (entity == MinecraftClient.getInstance().player) return;

        double maxDist = PotatoFixMod.CONFIG.entityCullingDistance;
        if (entity.squaredDistanceTo(x, y, z) > maxDist * maxDist) {
            cir.setReturnValue(false);
        }
    }
}
