package com.alan.clients.module.impl.render.breadcrumbs;

import net.minecraft.util.Vec3;

public class BreadCrumb {
    public Vec3 position;
    public long creationTime;

    public BreadCrumb(Vec3 vec, long var2) {
        this.position = vec;
        this.creationTime = var2;
    }
}
