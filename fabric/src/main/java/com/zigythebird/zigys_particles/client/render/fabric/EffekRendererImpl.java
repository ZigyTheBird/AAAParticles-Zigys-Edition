package com.zigythebird.zigys_particles.client.render.fabric;

import net.minecraft.client.renderer.RenderStateShard;

public class EffekRendererImpl {
    public static void setupRenderState() {
        ((RenderStateShard)RenderStateShard.PARTICLES_TARGET).setupRenderState();
    }

    public static void clearRenderState() {
        ((RenderStateShard)RenderStateShard.PARTICLES_TARGET).clearRenderState();
    }
}
