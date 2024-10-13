package com.zigythebird.zigys_particles.neoforge;

import com.zigythebird.zigys_particles.AAAParticles;
import com.zigythebird.zigys_particles.client.loader.EffekAssetLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

public class ClientEvents {

    @EventBusSubscriber(modid = AAAParticles.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class modEventBus {
        @SubscribeEvent
        public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new EffekAssetLoader());
        }
    }
}
