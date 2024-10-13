package com.zigythebird.zigys_particles.client;

import com.zigythebird.multiloaderutils.utils.NetworkManager;
import com.zigythebird.zigys_particles.AAAParticles;
import com.zigythebird.zigys_particles.client.installer.JarExtractor;
import com.zigythebird.zigys_particles.client.installer.NativePlatform;
import com.zigythebird.zigys_particles.client.registry.EffectRegistry;
import com.zigythebird.zigys_particles.api.client.effekseer.ParticleEmitter;
import com.zigythebird.zigys_particles.api.common.DynamicParameter;
import com.zigythebird.zigys_particles.api.common.ParticleEmitterInfo;
import com.zigythebird.zigys_particles.common.network.S2CAddParticle;
import com.zigythebird.zigys_particles.common.network.S2CSendEmitterTrigger;
import com.zigythebird.zigys_particles.common.network.S2CUpdateEmitterParam;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.zigythebird.zigys_particles.common.network.Packets.*;

public class AAAParticlesClient {
	public static void init() {
		installNativeLibrary();

		NetworkManager.registerReceiver(NetworkManager.Side.S2C, addParticlePacket, (buf, context) -> new S2CAddParticle(buf).handle(Minecraft.getInstance().player));
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, sendEmitterTriggerPacket, (buf, context) -> new S2CSendEmitterTrigger(buf).handle());
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, updateEmitterParamPacket, (buf, context) -> new S2CUpdateEmitterParam(buf).handle());
	}

	public static void setup() {
	}

	private static void installNativeLibrary() {
		var platform = NativePlatform.current();
		if (platform.isUnsupported()) {
			return;
		}
		var DLL_NAME = "EffekseerNativeForJava";
		var dll = platform.getNativeInstallPath(DLL_NAME);
		try {
			var resource = "assets/%s/%s".formatted(AAAParticles.MOD_ID, platform.formatFileName(DLL_NAME));
			if (!dll.isFile()) {
                AAAParticles.LOGGER.info("Installing Effekseer native library at {}", dll.getCanonicalPath());
				JarExtractor.extract(resource, dll);
			} else {
				var updated = JarExtractor.update(resource, dll);
				if (updated) {
                    AAAParticles.LOGGER.info("Updating Effekseer native library at {}", dll.getCanonicalPath());
				} else {
                    AAAParticles.LOGGER.info("Loading Effekseer native library at {}", dll.getCanonicalPath());
				}
			}
			System.load(dll.getCanonicalPath());
		} catch (IOException | UnsatisfiedLinkError e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static void addParticle(Level level, ParticleEmitterInfo info) {
		var player = Minecraft.getInstance().player;
		if (player != null && player.level() != level) {
			return;
		}
		info.spawnInWorld(level, player);
	}

	public static void setParam(ParticleEmitter.Type type, ResourceLocation effek, ResourceLocation emitterName, DynamicParameter[] params) {
		Optional.ofNullable(EffectRegistry.get(effek))
				.flatMap(mng -> mng.getNamedEmitter(type, emitterName))
				.ifPresent(emitter -> {
					for (var param : params) {
						emitter.setDynamicInput(param.index(), param.value());
					}
				});
	}

	public static void sendTrigger(ParticleEmitter.Type type, ResourceLocation effek, ResourceLocation emitterName, int[] triggers) {
		Optional.ofNullable(EffectRegistry.get(effek))
				.flatMap(mng -> mng.getNamedEmitter(type, emitterName))
				.ifPresent(emitter -> Arrays.stream(triggers).forEach(emitter::sendTrigger));
	}
}
