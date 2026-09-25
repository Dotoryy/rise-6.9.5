package com.alan.clients.util.shader.impl;

import com.alan.clients.util.shader.base.ShaderRenderType;

class OutlineShaderSwitchMap {
    static final int[] aQk = new int[ShaderRenderType.values().length];

    static {
        try {
            aQk[ShaderRenderType.CAMERA.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            aQk[ShaderRenderType.OVERLAY.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
