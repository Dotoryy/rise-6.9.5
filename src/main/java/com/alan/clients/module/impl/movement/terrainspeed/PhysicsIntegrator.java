package com.alan.clients.module.impl.movement.terrainspeed;

public final class PhysicsIntegrator {
    public PhysicsVector3 impulse = new PhysicsVector3(0.0, 0.0, 0.0);
    public PhysicsVector3 force = new PhysicsVector3(0.0, 0.0, 0.0);
    public PhysicsVector3 velocity = new PhysicsVector3(0.0, 0.0, 0.0);
    PhysicsVector3 gravity = new PhysicsVector3(0.0, -10.0, 0.0);
    double forceScale = 2.0;
    double mass = 1.0;

    public PhysicsIntegrator() {
    }

    public PhysicsVector3 integrate() {
        double d0 = 1.0 / this.mass;
        this.force.scale(d0);
        this.force.add(this.gravity).scale(this.forceScale);
        this.impulse.scale(d0);
        this.force.scale(0.03333333333333333);
        this.impulse.add(this.force);
        this.velocity.add(this.impulse);
        this.force.set(0.0, 0.0, 0.0);
        this.impulse.set(0.0, 0.0, 0.0);
        return this.velocity;
    }
}
