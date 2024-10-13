package com.zigythebird.zigys_particles;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class AAAParticles {
	public static final String MOD_ID = "zigys_particles";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {}

    public static ResourceLocation loc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}