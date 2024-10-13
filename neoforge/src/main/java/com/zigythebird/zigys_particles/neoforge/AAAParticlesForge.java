package com.zigythebird.zigys_particles.neoforge;

import com.zigythebird.zigys_particles.AAAParticles;
import com.zigythebird.zigys_particles.client.AAAParticlesClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforgespi.Environment;

@Mod(AAAParticles.MOD_ID)
public class AAAParticlesForge extends AAAParticles  {
    public AAAParticlesForge(IEventBus modbus) {
        AAAParticles.init();

        if (Environment.get().getDist().isClient()) {
            AAAParticlesForgeClient.onClientInit();
            AAAParticlesClient.init();
            modbus.addListener(AAAParticlesForgeClient::onClientSetup);
        }
    }
}