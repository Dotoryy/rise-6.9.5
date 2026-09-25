package com.alan.clients.component.impl.community;

import lombok.Generated;
import net.minecraft.util.ResourceLocation;

public class ClientUser {
    private UserRank rank;
    private String tag;
    private String gr;
    private String[] capeUrls;
    private ResourceLocation[] capeTextures;
    private boolean capeLoaded = false;
    private int frame = 0;

    public ClientUser(String var1) {
        this.gr = var1;
    }

    public ResourceLocation bS() {
        return this.capeTextures[this.frame % this.capeTextures.length];
    }

    public boolean bT() {
        return this.capeUrls.length != 0;
    }

    public void setRank(UserRank var1) {
        this.rank = var1;
    }

    public void setTag(String var1) {
        this.tag = var1;
    }

    public String getColorCode() {
        return this.rank.getColorCode();
    }

    public UserRank bV() {
        return this.rank;
    }

    @Generated
    public String getTag() {
        return this.tag;
    }

    @Generated
    public String bX() {
        return this.gr;
    }

    @Generated
    public String[] bY() {
        return this.capeUrls;
    }

    @Generated
    public void setCapeUrls(String[] var1) {
        this.capeUrls = var1;
    }

    @Generated
    public ResourceLocation[] getCapeTextures() {
        return this.capeTextures;
    }

    @Generated
    public void setCapeTextures(ResourceLocation[] locations) {
        this.capeTextures = locations;
    }

    @Generated
    public boolean ca() {
        return this.capeLoaded;
    }

    @Generated
    public void setCapeLoaded(boolean var1) {
        this.capeLoaded = var1;
    }

    @Generated
    public int getFrame() {
        return this.frame;
    }

    @Generated
    public void setFrame(int var1) {
        this.frame = var1;
    }
}
