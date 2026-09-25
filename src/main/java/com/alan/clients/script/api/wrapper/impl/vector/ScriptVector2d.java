package com.alan.clients.script.api.wrapper.impl.vector;

public class ScriptVector2d {
    private double x;
    private double y;

    public ScriptVector2d(double var1, double var3) {
        this.x = var1;
        this.y = var3;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double var1) {
        this.x = var1;
    }

    public void setY(double var1) {
        this.y = var1;
    }
}
