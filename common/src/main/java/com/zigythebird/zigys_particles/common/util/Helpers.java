package com.zigythebird.zigys_particles.common.util;

import com.zigythebird.zigys_particles.client.installer.NativePlatform;

import java.util.function.Supplier;

public class Helpers {
    public static <T> T checkPlatform(Supplier<T> constructor) {
        if (NativePlatform.isRunningOnUnsupportedPlatform()) {
            throw new UnsupportedOperationException("Unsupported platform");
        }
        return constructor.get();
    }
}
