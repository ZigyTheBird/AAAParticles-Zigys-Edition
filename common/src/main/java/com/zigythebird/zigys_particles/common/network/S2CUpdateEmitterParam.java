package com.zigythebird.zigys_particles.common.network;

import com.zigythebird.zigys_particles.api.client.effekseer.ParticleEmitter;
import com.zigythebird.zigys_particles.api.common.DynamicParameter;
import com.zigythebird.zigys_particles.client.AAAParticlesClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class S2CUpdateEmitterParam {
    private final ParticleEmitter.Type type;
    private final ResourceLocation effek;
    private final ResourceLocation emitterName;
    private final DynamicParameter[] parameters;

    public S2CUpdateEmitterParam(ParticleEmitter.Type type, ResourceLocation effek, ResourceLocation emitterName, DynamicParameter[] parameters) {
        this.type = type;
        this.effek = effek;
        this.emitterName = emitterName;
        this.parameters = parameters;
    }

    public S2CUpdateEmitterParam(FriendlyByteBuf buf) {
        type = buf.readEnum(ParticleEmitter.Type.class);
        effek = buf.readResourceLocation();
        emitterName = buf.readResourceLocation();

        this.parameters = new DynamicParameter[buf.readVarInt()];
        for (int i = 0; i < this.parameters.length; i++) {
            var index = buf.readVarInt();
            var value = buf.readFloat();
            parameters[i] = new DynamicParameter(index, value);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
        buf.writeResourceLocation(effek);
        buf.writeResourceLocation(emitterName);

        buf.writeVarInt(parameters.length);
        for (var parameter : parameters) {
            buf.writeVarInt(parameter.index());
            buf.writeFloat(parameter.value());
        }
    }

    public void handle() {
        AAAParticlesClient.setParam(type, effek, emitterName, parameters);
    }
}
