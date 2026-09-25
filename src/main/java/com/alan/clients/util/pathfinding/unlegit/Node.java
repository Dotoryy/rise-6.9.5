package com.alan.clients.util.pathfinding.unlegit;

import java.util.ArrayList;
import lombok.Generated;

class Node {
    private Vec3 aNT;
    private Node aNU;
    private ArrayList<Vec3> aNM;
    private double aNV;
    private double aco;
    private double aNW;

    public Node(Vec3 var1, Node var2, ArrayList<Vec3> var3, double var4, double var6, double var8) {
        this.aNT = var1;
        this.aNU = var2;
        this.aNM = var3;
        this.aNV = var4;
        this.aco = var6;
        this.aNW = var8;
    }

    @Generated
    public Vec3 uS() {
        return this.aNT;
    }

    @Generated
    public Node uT() {
        return this.aNU;
    }

    @Generated
    public ArrayList<Vec3> uQ() {
        return this.aNM;
    }

    @Generated
    public double uU() {
        return this.aNV;
    }

    @Generated
    public double uV() {
        return this.aco;
    }

    @Generated
    public double uW() {
        return this.aNW;
    }

    @Generated
    public void b(Vec3 var1) {
        this.aNT = var1;
    }

    @Generated
    public void a(Node var1) {
        this.aNU = var1;
    }

    @Generated
    public void e(ArrayList<Vec3> var1) {
        this.aNM = var1;
    }

    @Generated
    public void Y(double var1) {
        this.aNV = var1;
    }

    @Generated
    public void Z(double var1) {
        this.aco = var1;
    }

    @Generated
    public void aa(double var1) {
        this.aNW = var1;
    }
}
