package com.alan.clients.util.shader.impl;

import com.alan.clients.util.shader.base.ShaderRenderType;

class BloomShaderSwitchMap {
    static final int[] aQb = new int[ShaderRenderType.values().length];

    static {
        try {
            aQb[ShaderRenderType.CAMERA.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            aQb[ShaderRenderType.OVERLAY.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
