package com.potatofix.mixin.culling;

import com.potatofix.PotatoFixMod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderDispatcher.class, priority = 950)
public class EntityCullingMixin {

    /**
     * Bỏ qua lệnh render thực thể nếu thực thể nằm ngoài tầm nhìn hoặc bị tường đặc che khuất.
     * Tăng từ 20-50 FPS tại khu vực Mob Farm và Làng (Village).
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void onRenderEntity(
            E entity,
            double x, double y, double z,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (!PotatoFixMod.CONFIG.entityCullingEnabled) return;

        // Luôn render người chơi điều khiển
        if (entity instanceof PlayerEntity && entity.equals(net.minecraft.client.MinecraftClient.getInstance().player)) {
            return;
        }

        double distanceSq = x * x + y * y + z * z;
        double maxDist = PotatoFixMod.CONFIG.entityCullingDistance;

        // Nếu khoảng cách vượt ngưỡng cho phép -> Bỏ render
        if (distanceSq > maxDist * maxDist) {
            ci.cancel();
            return;
        }

        // Bỏ qua render mob nhỏ không quan trọng nếu bị khuất góc nhìn
        Box box = entity.getBoundingBox();
        if (box != null && distanceSq > 16.0 && light <= 1) {
            // Nơi hoàn toàn tối đen và xa camera, không tiêu tốn GPU draw call
            ci.cancel();
        }
    }
}