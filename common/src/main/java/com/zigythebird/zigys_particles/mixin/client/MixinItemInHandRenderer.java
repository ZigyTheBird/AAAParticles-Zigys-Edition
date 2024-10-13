package com.zigythebird.zigys_particles.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zigythebird.zigys_particles.client.internal.EffekFpvRenderer;
import com.zigythebird.zigys_particles.client.internal.RenderContext;
import com.zigythebird.zigys_particles.client.internal.RenderStateCapture;
import com.zigythebird.zigys_particles.client.render.EffekRenderer;
import com.zigythebird.zigys_particles.client.render.ItemTransformHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.Objects;

import static com.zigythebird.zigys_particles.client.render.RenderUtil.copyCurrentDepthTo;

@Mixin(value = ItemInHandRenderer.class, priority = 1005)
public class MixinItemInHandRenderer implements EffekFpvRenderer {
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private ItemRenderer itemRenderer;
    @Unique private final EnumMap<InteractionHand, RenderStateCapture> aaaParticles$captures = new EnumMap<>(InteractionHand.class);
    @Unique private final boolean aaaParticles$DISABLE_FPV_RENDERING = Boolean.getBoolean("mod.chloeprime.aaaparticles.disableFpvRendering");

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    private void resetCaptureState(AbstractClientPlayer player, float f, float g, InteractionHand hand, float h, ItemStack itemStack, float i, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo ci) {
        var capture = aaaParticles$captures.computeIfAbsent(hand, arg -> new RenderStateCapture());
        capture.hasCapture = false;
        capture.item = null;
    }

    @Inject(
            method = "renderArmWithItem",
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    )
    private void setFpvRenderState(AbstractClientPlayer player, float partial, float g, InteractionHand hand, float h, ItemStack stack, float i, PoseStack poseStack, MultiBufferSource buffer, int j, CallbackInfo ci) {
        var stackTop = poseStack.last();
        var capture = Objects.requireNonNull(aaaParticles$captures.get(hand));
        capture.hasCapture = true;
        capture.pose.last().pose().set(stackTop.pose());
        capture.pose.last().normal().set(stackTop.normal());
        capture.projection.set(RenderSystem.getProjectionMatrix());
        capture.item = stack;
    }

    @Inject(method = "renderHandsWithItems", at = @At("RETURN"))
    private void captureHandDepth(float partial, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LocalPlayer player, int i, CallbackInfo ci) {
        if (RenderContext.renderHandDeferred()) {
            if (RenderContext.captureHandDepth()) {
                copyCurrentDepthTo(RenderStateCapture.CAPTURED_HAND_DEPTH_BUFFER);
            }
        } else {
            aaaParticles$renderFpvEffek(partial, player);
        }
    }

    @Override
    public void aaaParticles$renderFpvEffek(float partial, LocalPlayer player) {
        if (aaaParticles$DISABLE_FPV_RENDERING) {
            return;
        }
        var oldProjection = RenderSystem.getProjectionMatrix();
        var oldVertexSort = RenderSystem.getVertexSorting();
        try {
            var camera = minecraft.gameRenderer.getMainCamera();
            aaaParticles$captures.forEach((hand, capture) -> {
                if (capture.hasCapture && capture.item != null) {
                    RenderSystem.setProjectionMatrix(capture.projection, oldVertexSort);

                    var arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                    var tran = switch (arm) {
                        case LEFT -> ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
                        case RIGHT -> ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
                    };
                    var poseStack = capture.pose;
                    poseStack.pushPose();

                    var model = itemRenderer.getModel(capture.item, player.level(), player, player.getId() + tran.ordinal());
                    ItemTransformHooks.applyItemTransform(poseStack, model, tran, arm == HumanoidArm.LEFT);
                    poseStack.translate(-0.5, -0.5, -0.5);
                    EffekRenderer.onRenderHand(partial, hand, poseStack, capture.projection, camera);

                    poseStack.popPose();
                }

                capture.item = null;
            });
        } finally {
            RenderSystem.setProjectionMatrix(oldProjection, oldVertexSort);
        }
    }
}
