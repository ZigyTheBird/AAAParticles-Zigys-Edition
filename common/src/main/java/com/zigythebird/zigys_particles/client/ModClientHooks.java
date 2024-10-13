package com.zigythebird.zigys_particles.client;

import com.zigythebird.zigys_particles.AAAParticles;
import com.zigythebird.zigys_particles.api.common.AAALevel;
import com.zigythebird.zigys_particles.api.common.ParticleEmitterInfo;
import net.minecraft.world.entity.LightningBolt;

public class ModClientHooks {
    public static final ParticleEmitterInfo LIGHTNING_EFFEK_TEMPLATE = new ParticleEmitterInfo(AAAParticles.loc("lightning"));

    public static void playLightningEffek(LightningBolt bolt) {
        var info = LIGHTNING_EFFEK_TEMPLATE.clone().position(bolt.position());
        AAALevel.addParticle(bolt.level(), true, info);
    }
}
