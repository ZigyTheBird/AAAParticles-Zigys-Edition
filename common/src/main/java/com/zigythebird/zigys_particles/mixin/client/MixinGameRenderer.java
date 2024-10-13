package com.zigythebird.zigys_particles.mixin.client;

import com.zigythebird.zigys_particles.api.client.effekseer.EffekRenderHelper;
import com.zigythebird.zigys_particles.client.internal.EffekFpvRenderer;
import com.zigythebird.zigys_particles.client.internal.RenderContext;
import com.zigythebird.zigys_particles.client.internal.RenderStateCapture;
import com.zigythebird.zigys_particles.client.render.EffekRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.zigythebird.zigys_particles.client.render.RenderUtil.pasteToCurrentDepthFrom;
import static org.lwjgl.opengl.GL11.*;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Shadow @Final public ItemInHandRenderer itemInHandRenderer;
    @Shadow @Final Minecraft minecraft;
    @Shadow private boolean renderHand;

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void renderLevelTail(DeltaTracker deltaTracker, CallbackInfo ci) {
        glDepthMask(true);
        glDepthFunc(GL_LEQUAL);

        if (RenderContext.renderLevelDeferred() && RenderStateCapture.LEVEL.hasCapture) {
            RenderStateCapture.LEVEL.hasCapture = false;

            pasteToCurrentDepthFrom(RenderStateCapture.CAPTURED_WORLD_DEPTH_BUFFER);
            EffekRenderer.onRenderWorldLast(deltaTracker.getGameTimeDeltaTicks(), RenderStateCapture.LEVEL.pose, RenderStateCapture.LEVEL.projection, RenderStateCapture.LEVEL.camera);
        }
        if (RenderContext.renderHandDeferred() && renderHand) {
            if (RenderContext.captureHandDepth()) {
                pasteToCurrentDepthFrom(RenderStateCapture.CAPTURED_HAND_DEPTH_BUFFER);
            }
            ((EffekFpvRenderer) itemInHandRenderer).aaaParticles$renderFpvEffek(deltaTracker.getGameTimeDeltaTicks(), minecraft.player);
        }
        EffekRenderHelper.deleteInactiveEffeks();
    }
}
