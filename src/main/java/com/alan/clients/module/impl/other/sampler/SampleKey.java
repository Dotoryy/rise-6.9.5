package com.alan.clients.module.impl.other.sampler;

import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.util.vector.Vector2i;
import com.alan.clients.util.vector.Vector3i;
import com.alan.clients.util.vector.Vector3d;

public final class SampleKey {
    Vector3i deltaPos;
    Vector2i rotation;

    public SampleKey(Vector3d var1, Vector2f vec2) {
        this.deltaPos = new Vector3i((int)var1.x, (int)var1.y, (int)var1.z);
        this.rotation = new Vector2i((int)(vec2.getX() / 15.0F), (int)(vec2.getY() / 20.0F));
    }

    public String toKey() {
        return this.deltaPos.ald + " " + this.deltaPos.ale + " " + this.deltaPos.aQE + " " + this.rotation.ald + " " + this.rotation.ale;
    }
}
