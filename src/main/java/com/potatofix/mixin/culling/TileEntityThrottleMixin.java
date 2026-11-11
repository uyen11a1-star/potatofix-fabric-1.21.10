package com.potatofix.mixin.culling;

import com.potatofix.PotatoFixMod;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockEntityRenderDispatcher.class, priority = 900)
public class TileEntityThrottleMixin {

    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",
            at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity> void onRenderBlockEntity(
            E blockEntity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            CallbackInfo ci
    ) {
        if (!PotatoFixMod.CONFIG.tileEntityThrottling) return;

        net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
        if (client.player == null) return;

        double distSq = blockEntity.getPos().getSquaredDistance(client.player.getPos());
        double limitDist = PotatoFixMod.CONFIG.tileEntityDistance;

        // Nếu rương hoặc cờ nằm ngoài bán kính thiết lập -> không dựng hình phức tạp
        if (distSq > limitDist * limitDist) {
            ci.cancel();
        }
    }
}