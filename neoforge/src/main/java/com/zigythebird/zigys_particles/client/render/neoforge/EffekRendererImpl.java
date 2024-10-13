package com.zigythebird.zigys_particles.client.render.neoforge;

import net.minecraft.client.renderer.RenderStateShard;

public class EffekRendererImpl {
    public static void setupRenderState() {
        RenderStateShard.PARTICLES_TARGET.setupRenderState();
    }

    public static void clearRenderState() {
        RenderStateShard.PARTICLES_TARGET.clearRenderState();
    }
}
