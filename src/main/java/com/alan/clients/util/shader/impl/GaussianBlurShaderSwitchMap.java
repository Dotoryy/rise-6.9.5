package com.alan.clients.util.shader.impl;

import com.alan.clients.util.shader.base.ShaderRenderType;

class GaussianBlurShaderSwitchMap {
    static final int[] aQh = new int[ShaderRenderType.values().length];

    static {
        try {
            aQh[ShaderRenderType.CAMERA.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            aQh[ShaderRenderType.OVERLAY.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
