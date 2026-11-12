package com.potatofix.mixin.culling;

import com.potatofix.PotatoFixMod;
import com.potatofix.util.ShaderCompat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderManager;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockEntityRenderManager.class, priority = 900)
public class TileEntityThrottleMixin {

    @Inject(method = "render(Lnet/minecraft/client/render/block/entity/state/BlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At("HEAD"), cancellable = true)
    private void onRenderBlockEntity(BlockEntityRenderState state, MatrixStack matrices,
                                     OrderedRenderCommandQueue queue, CameraRenderState camera,
                                     CallbackInfo ci) {
        if (!PotatoFixMod.CONFIG.tileEntityThrottling) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        double distSq = state.pos.getSquaredDistance(player.getX(), player.getY(), player.getZ());
        double limitDist = PotatoFixMod.CONFIG.tileEntityDistance * ShaderCompat.distanceScale();
        if (distSq > limitDist * limitDist) {
            ci.cancel();
        }
    }
}
