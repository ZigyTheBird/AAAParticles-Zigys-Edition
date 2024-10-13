package com.zigythebird.zigys_particles.client.loader;

import com.zigythebird.zigys_particles.AAAParticles;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class EffekAssetLoaderFabric extends EffekAssetLoader implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return AAAParticles.loc("effek");
    }
}
