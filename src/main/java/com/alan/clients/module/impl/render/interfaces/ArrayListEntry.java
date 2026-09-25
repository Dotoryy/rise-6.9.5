package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.impl.render.ClickGUI;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.util.vector.Vector2d;
import java.awt.Color;
import lombok.Generated;

public final class ArrayListEntry {
    public Module module;
    public Vector2d position = new Vector2d(5000.0, 0.0);
    public Vector2d targetPosition = new Vector2d(5000.0, 0.0);
    public float animationTime;
    public String tag = "";
    public float nameWidth = 0.0F;
    public float tagWidth;
    public Color color = Color.WHITE;
    public String translatedName = "";
    public boolean atm = false;
    public String displayName = "";
    public String displayTag = "";
    public boolean hasTag;

    public float getTotalWidth() {
        return this.nameWidth + this.tagWidth;
    }

    public ArrayListEntry(Module module) {
        this.module = module;
    }

    public boolean shouldShow(Interface var1) {
        if (this.getModule() instanceof ClickGUI) {
            return false;
        }

        if (!this.getModule().getModuleInfo().allowDisable()) {
            return false;
        }

        String s = var1.getModulesToShow().wo().getName();
        switch (s) {
            case "All":
                return true;
            case "Exclude render":
                return !this.getModule().getModuleInfo().category().equals(Category.RENDER);
            case "Only bound":
                return this.getModule().getKey() != 0;
            default:
                return true;
        }
    }

    @Generated
    public Module getModule() {
        return this.module;
    }

    @Generated
    public Vector2d getPosition() {
        return this.position;
    }

    @Generated
    public Vector2d getTargetPosition() {
        return this.targetPosition;
    }

    @Generated
    public float getAnimationTime() {
        return this.animationTime;
    }

    @Generated
    public String getTag() {
        return this.tag;
    }

    @Generated
    public float getNameWidth() {
        return this.nameWidth;
    }

    @Generated
    public float getTagWidth() {
        return this.tagWidth;
    }

    @Generated
    public Color getColor() {
        return this.color;
    }

    @Generated
    public String getTranslatedName() {
        return this.translatedName;
    }

    @Generated
    public boolean ny() {
        return this.atm;
    }

    @Generated
    public String getDisplayName() {
        return this.displayName;
    }

    @Generated
    public String getDisplayTag() {
        return this.displayTag;
    }

    @Generated
    public boolean isHasTag() {
        return this.hasTag;
    }

    @Generated
    public void setModule(Module module) {
        this.module = module;
    }

    @Generated
    public void h(Vector2d var1) {
        this.position = var1;
    }

    @Generated
    public void setTargetPosition(Vector2d vector2d) {
        this.targetPosition = vector2d;
    }

    @Generated
    public void setAnimationTime(float var1) {
        this.animationTime = var1;
    }

    @Generated
    public void an(String var1) {
        this.tag = var1;
    }

    @Generated
    public void setNameWidth(float var1) {
        this.nameWidth = var1;
    }

    @Generated
    public void u(float var1) {
        this.tagWidth = var1;
    }

    @Generated
    public void setColor(Color color) {
        this.color = color;
    }

    @Generated
    public void setTranslatedName(String var1) {
        this.translatedName = var1;
    }

    @Generated
    public void x(boolean var1) {
        this.atm = var1;
    }

    @Generated
    public void setDisplayName(String var1) {
        this.displayName = var1;
    }

    @Generated
    public void setDisplayTag(String var1) {
        this.displayTag = var1;
    }

    @Generated
    public void y(boolean var1) {
        this.hasTag = var1;
    }
}
