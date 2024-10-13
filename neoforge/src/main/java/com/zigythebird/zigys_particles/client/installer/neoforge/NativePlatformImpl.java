package com.zigythebird.zigys_particles.client.installer.neoforge;

import net.neoforged.neoforge.data.loading.DatagenModLoader;

@SuppressWarnings("unused")
public class NativePlatformImpl {
    public static boolean isDataGen() {
        return DatagenModLoader.isRunningDataGen() ;
    }
}
