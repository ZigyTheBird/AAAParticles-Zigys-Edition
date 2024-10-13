package com.zigythebird.zigys_particles.fabric;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import com.zigythebird.zigys_particles.AAAParticles;
import com.zigythebird.zigys_particles.client.AAAParticlesClient;
import mod.chloeprime.zigys_particles.client.ModClientConfig;
import com.zigythebird.zigys_particles.client.render.EffekRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraftforge.fml.config.ModConfig;

public class AAAParticlesFabricClient extends AAAParticles implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.CLIENT, ModClientConfig.SPEC);
        AAAParticlesClient.init();
        AAAParticlesClient.setup();
        EffekRenderer.init();
    }
}