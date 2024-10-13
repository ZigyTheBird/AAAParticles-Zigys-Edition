package com.zigythebird.zigys_particles.common.network;

import com.zigythebird.zigys_particles.api.client.effekseer.ParticleEmitter;
import com.zigythebird.zigys_particles.client.AAAParticlesClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class S2CSendEmitterTrigger {
    private final ParticleEmitter.Type type;
    private final ResourceLocation effek;
    private final ResourceLocation emitterName;
    private final int[] triggers;

    public S2CSendEmitterTrigger(ParticleEmitter.Type type, ResourceLocation effek, ResourceLocation emitterName, int[] triggers) {
        this.type = type;
        this.effek = effek;
        this.emitterName = emitterName;
        this.triggers = triggers;
    }

    public S2CSendEmitterTrigger(FriendlyByteBuf buf) {
        type = buf.readEnum(ParticleEmitter.Type.class);
        effek = buf.readResourceLocation();
        emitterName = buf.readResourceLocation();
        this.triggers = buf.readVarIntArray();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
        buf.writeResourceLocation(effek);
        buf.writeResourceLocation(emitterName);
        buf.writeVarIntArray(triggers);
    }

    public void handle() {
        AAAParticlesClient.sendTrigger(type, effek, emitterName, triggers);
    }
}
