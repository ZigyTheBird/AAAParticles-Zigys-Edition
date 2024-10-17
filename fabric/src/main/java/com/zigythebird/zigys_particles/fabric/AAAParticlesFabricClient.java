package com.zigythebird.zigys_particles.fabric;

import com.zigythebird.zigys_particles.AAAParticles;
import com.zigythebird.zigys_particles.client.AAAParticlesClient;
import com.zigythebird.zigys_particles.client.render.EffekRenderer;
import net.fabricmc.api.ClientModInitializer;

public class AAAParticlesFabricClient extends AAAParticles implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AAAParticlesClient.init();
        AAAParticlesClient.setup();
        EffekRenderer.init();
    }
}