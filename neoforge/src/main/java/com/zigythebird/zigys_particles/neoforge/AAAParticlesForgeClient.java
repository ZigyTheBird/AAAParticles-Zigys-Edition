package com.zigythebird.zigys_particles.neoforge;

import com.zigythebird.zigys_particles.AAAParticles;
import com.zigythebird.zigys_particles.client.AAAParticlesClient;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class AAAParticlesForgeClient extends AAAParticles {
    public static void onClientInit() {}

    @SuppressWarnings("unused")
    static void onClientSetup(FMLClientSetupEvent e) {
        AAAParticlesClient.setup();
    }
}
