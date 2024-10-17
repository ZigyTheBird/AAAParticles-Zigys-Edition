package com.zigythebird.zigys_particles.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.zigythebird.zigys_particles.client.internal.RenderContext;
import com.zigythebird.zigys_particles.client.internal.RenderStateCapture;
import com.zigythebird.zigys_particles.client.render.EffekRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.zigythebird.zigys_particles.client.render.RenderUtil.copyCurrentDepthTo;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void onRenderLevelLast(DeltaTracker deltaTracker, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local(ordinal = 0) PoseStack poseStack) {
        var capture = RenderStateCapture.LEVEL;
        var currentPose = poseStack.last();
        var capturedPose = capture.pose.last();
        capturedPose.pose().set(currentPose.pose());
        capturedPose.normal().set(currentPose.normal());
        capture.projection.set(matrix4f2);
        capture.camera = camera;
        capture.hasCapture = true;

        if (RenderContext.renderLevelDeferred()) {
            copyCurrentDepthTo(RenderStateCapture.CAPTURED_WORLD_DEPTH_BUFFER);
        } else {
            EffekRenderer.onRenderWorldLast(deltaTracker.getGameTimeDeltaTicks(), capture.pose, capture.projection, capture.camera);
        }
    }
}
