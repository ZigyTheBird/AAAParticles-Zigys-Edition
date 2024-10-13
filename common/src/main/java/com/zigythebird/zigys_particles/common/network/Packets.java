package com.zigythebird.zigys_particles.common.network;

import com.zigythebird.zigys_particles.AAAParticles;
import net.minecraft.resources.ResourceLocation;

public class Packets {
    public static final ResourceLocation addParticlePacket = AAAParticles.loc("s2c_add_particle");
    public static final ResourceLocation sendEmitterTriggerPacket = AAAParticles.loc("s2c_send_emitter_trigger");
    public static final ResourceLocation updateEmitterParamPacket = AAAParticles.loc("s2c_update_emitter_param");
}
