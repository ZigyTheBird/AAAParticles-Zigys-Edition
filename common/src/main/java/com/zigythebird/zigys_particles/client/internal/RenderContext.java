package com.zigythebird.zigys_particles.client.internal;

import com.zigythebird.multiloaderutils.misc.ModLoader;
import com.zigythebird.multiloaderutils.utils.Platform;

public class RenderContext {
    public static final boolean IRIS_MODE = Platform.isModLoaded("iris") || Platform.isModLoaded("oculus");
    public static final boolean ON_FABRIC = Platform.getLoader() == ModLoader.Fabric;

    public static boolean renderLevelDeferred() {
        return !IRIS_MODE || ON_FABRIC;
    }

    public static boolean renderHandDeferred() {
        return !IRIS_MODE || (ON_FABRIC || isIrisShaderEnabled());
    }

    public static boolean captureHandDepth() {
        return !IRIS_MODE || !isIrisShaderEnabled();
    }

    public static boolean isIrisShaderEnabled() {
        return IRIS_MODE && IrisProxy.isIrisShaderEnabled();
    }

    private RenderContext() {}
}
