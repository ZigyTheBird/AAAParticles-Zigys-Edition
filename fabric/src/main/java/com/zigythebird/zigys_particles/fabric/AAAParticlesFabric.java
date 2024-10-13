package com.zigythebird.zigys_particles.fabric;

import com.zigythebird.zigys_particles.AAAParticles;
import com.zigythebird.zigys_particles.client.loader.EffekAssetLoaderFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class AAAParticlesFabric extends AAAParticles implements ModInitializer {
    @Override
    public void onInitialize() {
        AAAParticles.init();
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new EffekAssetLoaderFabric());
    }
}