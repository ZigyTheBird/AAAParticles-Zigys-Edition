package com.zigythebird.zigys_particles.client.internal;

import net.irisshaders.iris.api.v0.IrisApi;

class IrisProxy {
    public static boolean isIrisShaderEnabled() {
        return IrisApi.getInstance().isShaderPackInUse();
    }

    private IrisProxy() {
    }
}
