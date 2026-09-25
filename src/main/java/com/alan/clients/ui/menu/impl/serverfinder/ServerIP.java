package com.alan.clients.ui.menu.impl.serverfinder;

import lombok.Generated;

public class ServerIP {
    private final int first;
    private final int second;
    private final int third;
    private final int fourth;
    private final String[] split;

    public int getPart(int var1) {
        return Integer.parseInt(this.split[var1]);
    }

    public void setPart(int var1, int var2) {
        this.split[var1] = String.valueOf(var2);
    }

    public ServerIP(int var1, int var2, int var3, int var4) {
        this.split = new String[]{String.valueOf(var1), String.valueOf(var2), String.valueOf(var3), String.valueOf(var4)};
        this.first = var1;
        this.second = var2;
        this.third = var3;
        this.fourth = var4;
    }

    public ServerIP(String var1) {
        this.split = var1.split("\\.");
        this.first = Integer.parseInt(this.split[0]);
        this.second = Integer.parseInt(this.split[1]);
        this.third = Integer.parseInt(this.split[2]);
        this.fourth = Integer.parseInt(this.split[3]);
    }

    public static ServerIP min(ServerIP serverIP, ServerIP var1) {
        int i = Math.min(serverIP.first, var1.first);
        int j = Math.min(serverIP.second, var1.second);
        int k = Math.min(serverIP.third, var1.third);
        int l = Math.min(serverIP.fourth, var1.fourth);
        return new ServerIP(i, j, k, l);
    }

    public static ServerIP max(ServerIP serverIP, ServerIP var1) {
        int i = Math.max(serverIP.first, var1.first);
        int j = Math.max(serverIP.second, var1.second);
        int k = Math.max(serverIP.third, var1.third);
        int l = Math.max(serverIP.fourth, var1.fourth);
        return new ServerIP(i, j, k, l);
    }

    @Override
    public String toString() {
        return this.split[0] + "." + this.split[1] + "." + this.split[2] + "." + this.split[3];
    }

    @Generated
    public int getFirst() {
        return this.first;
    }

    @Generated
    public int getSecond() {
        return this.second;
    }

    @Generated
    public int rw() {
        return this.third;
    }

    @Generated
    public int getThird() {
        return this.fourth;
    }

    @Generated
    public String[] getSplit() {
        return this.split;
    }
}
