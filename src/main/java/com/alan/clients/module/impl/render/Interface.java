package com.alan.clients.module.impl.render;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.ModuleToggleEvent;
import com.alan.clients.newevent.impl.other.ServerJoinEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Value;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.component.impl.render.NotificationComponent;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.module.impl.render.InterfaceModeValue;
import com.alan.clients.module.impl.render.ModulesToShowModeValue;
import com.alan.clients.module.impl.render.InformationTypeModeValue;
import com.alan.clients.module.impl.render.interfaces.ArrayListEntry;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.render.interface.name", description = "module.render.interface.description", category = Category.RENDER, autoEnabled = true)
public final class Interface extends Module {
    private final ModeValue mode = new InterfaceModeValue(this, "Mode", this);
    private final ModeValue modulesToShow = new ModulesToShowModeValue(this, "Modules to Show", this);
    public BooleanValue suffix = new BooleanValue("Suffix", this, true);
    public BooleanValue lowercase = new BooleanValue("Lowercase", this, false);
    public BooleanValue removeSpaces = new BooleanValue("Remove Spaces", this, false);
    public BooleanValue aoc = new BooleanValue("Shaders", this, false);
    public BooleanValue toggleNotifications = new BooleanValue("Toggle Notifications", this, false);
    private final NumberValue blurRadius = new NumberValue("Blur Radius", this, 12, 1, 30, 1);
    private final NumberValue blurCompression = new NumberValue("Blur Compression", this, 3.0, 1.0, 10.0, 0.1);
    private final NumberValue bloomRadius = new NumberValue("Bloom Radius", this, 14, 1, 30, 1);
    private final NumberValue bloomCompression = new NumberValue("Bloom Compression", this, 2.0, 1.0, 10.0, 0.1);
    private final NumberValue backgroundAlpha = new NumberValue("Background Alpha", this, 110, 0, 255, 1);
    private final NumberValue roundingRadius = new NumberValue("Rounding Radius", this, 5, 0, 20, 0.5);
    private List<ArrayListEntry> allEntries = new ArrayList<>();
    private List<ArrayListEntry> activeEntries = new ArrayList<>();
    private ModeValue informationType = new InformationTypeModeValue(this, "Information Type", this);
    private final StopWatch animationStopWatch = new StopWatch();
    private final StopWatch preUpdateStopWatch = new StopWatch();
    public Font widthComparator = FontManager.MAIN.a(20, FontWeight.MEDIUM);
    public float moduleSpacing = 12.0F;
    public float edgeOffset;
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> this.rebuildEntries();
    @EventLink
    public final Listener<ServerJoinEvent> onServerJoin = var1 -> this.rebuildEntries();
    @EventLink
    public final Listener<ModuleToggleEvent> onModuleToggle = var1 -> {
        if (this.toggleNotifications.wo()) {
            NotificationComponent.a("Toggled", "Toggled " + var1.getModule().getName() + " " + (var1.getModule().isEnabled() ? "on" : "off"), 900);
        }
    };
    StopWatch nameUpdateStopWatch = new StopWatch();
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> aMR.execute(() -> {
        Themes.c(new Color(0, 0, 0, this.aoc.wo() ? this.getBackgroundAlpha() : 150));
        if (this.nameUpdateStopWatch.T(15000L)) {
            this.updateEntryNames();
            this.nameUpdateStopWatch.aX();
        }

        this.preUpdateStopWatch.aX();

        for (ArrayListEntry zc : this.activeEntries) {
            if (zc.animationTime != 0.0F) {
                for (Value value : zc.getModule().getValues()) {
                    if (value instanceof ModeValue) {
                        String s = ((ModeValue)value).wo().getName();
                        if ("Bloxd".equalsIgnoreCase(s)) {
                            zc.an("Blocks");
                        } else {
                            zc.an(s);
                        }
                        break;
                    }

                    zc.an("");
                }
            }
        }

        this.createArrayList();
    });
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        for (ArrayListEntry zcx : this.allEntries) {
            float f = this.mode.wo().getName().equals("Classic") ? 10.0F : 100.0F;
            if (zcx.getModule().isEnabled()) {
                zcx.animationTime = Math.min(zcx.animationTime + (float)this.animationStopWatch.getElapsedTime() / f, 10.0F);
            } else {
                zcx.animationTime = Math.max(zcx.animationTime - (float)this.animationStopWatch.getElapsedTime() / f, 0.0F);
            }
        }

        float f1 = var1.getScaledResolution().getScaledWidth();
        Vector2f vector2f = new Vector2f(0.0F, 0.0F);

        for (ArrayListEntry zc : this.activeEntries) {
            if (zc.animationTime != 0.0F) {
                zc.targetPosition = new Vector2d(f1 - zc.getNameWidth() - zc.getTagWidth(), vector2f.getY());
                if (!zc.getModule().isEnabled() && zc.animationTime < 10.0F) {
                    zc.targetPosition = new Vector2d(f1 + zc.getNameWidth() + zc.getTagWidth(), vector2f.getY());
                } else {
                    vector2f.setY(vector2f.getY() + this.moduleSpacing);
                }

                float f2 = this.edgeOffset;
                float f3 = this.edgeOffset;
                zc.targetPosition.x -= f2;
                zc.targetPosition.y += f3;
                if (!(Math.abs(zc.getPosition().getX() - zc.targetPosition.x) > 0.5) && !(Math.abs(zc.getPosition().getY() - zc.targetPosition.y) > 0.5) && (zc.animationTime == 0.0F || zc.animationTime == 10.0F)) {
                    zc.position = zc.targetPosition;
                } else {
                    zc.position.x = MathUtil.m(zc.position.x, zc.targetPosition.x, 0.015F * (float)this.animationStopWatch.getElapsedTime());
                    zc.position.y = MathUtil.m(zc.position.y, zc.targetPosition.y, 0.015F * (float)this.animationStopWatch.getElapsedTime());
                }
            }
        }

        this.animationStopWatch.aX();
    };

    public Interface() {
        this.rebuildEntries();
    }

    public void rebuildEntries() {
        this.allEntries.clear();
        Client.a
            .g()
            .getAll()
            .stream()
            .sorted(Comparator.comparingDouble(var1 -> -this.widthComparator.getStringWidth(var1.getName())))
            .forEach(var1 -> this.allEntries.add(new ArrayListEntry(var1)));
        this.updateEntryNames();
    }

    public void createArrayList() {
        this.activeEntries = this.allEntries
            .stream()
            .filter(var1 -> var1.shouldShow(this))
            .sorted(Comparator.comparingDouble(var0 -> -var0.getNameWidth() - var0.getTagWidth()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateEntryNames() {
        for (ArrayListEntry zc : this.allEntries) {
            zc.setTranslatedName(zc.getModule().getName());
        }
    }

    public int getBlurRadius() {
        return this.blurRadius.wo().intValue();
    }

    public float getBlurCompression() {
        return this.blurCompression.wo().floatValue();
    }

    public int getBloomRadius() {
        return this.bloomRadius.wo().intValue();
    }

    public float getBloomCompression() {
        return this.bloomCompression.wo().floatValue();
    }

    public int getBackgroundAlpha() {
        return this.backgroundAlpha.wo().intValue();
    }

    public double getRoundingRadius() {
        return this.roundingRadius.wo().doubleValue();
    }

    @Generated
    public ModeValue getMode() {
        return this.mode;
    }

    @Generated
    public ModeValue getModulesToShow() {
        return this.modulesToShow;
    }

    @Generated
    public BooleanValue getSuffix() {
        return this.suffix;
    }

    @Generated
    public BooleanValue getLowercase() {
        return this.lowercase;
    }

    @Generated
    public BooleanValue getRemoveSpaces() {
        return this.removeSpaces;
    }

    @Generated
    public BooleanValue getShaders() {
        return this.aoc;
    }

    @Generated
    public BooleanValue getToggleNotifications() {
        return this.toggleNotifications;
    }

    @Generated
    public List<ArrayListEntry> getAllEntries() {
        return this.allEntries;
    }

    @Generated
    public List<ArrayListEntry> getActiveEntries() {
        return this.activeEntries;
    }

    @Generated
    public ModeValue getInformationType() {
        return this.informationType;
    }

    @Generated
    public StopWatch getAnimationStopWatch() {
        return this.animationStopWatch;
    }

    @Generated
    public StopWatch getPreUpdateStopWatch() {
        return this.preUpdateStopWatch;
    }

    @Generated
    public Font getWidthComparator() {
        return this.widthComparator;
    }

    @Generated
    public float getModuleSpacing() {
        return this.moduleSpacing;
    }

    @Generated
    public float getEdgeOffset() {
        return this.edgeOffset;
    }

    @Generated
    public Listener<WorldChangeEvent> getOnWorldChange() {
        return this.onWorldChange;
    }

    @Generated
    public Listener<ServerJoinEvent> getOnServerJoin() {
        return this.onServerJoin;
    }

    @Generated
    public Listener<ModuleToggleEvent> getOnModuleToggle() {
        return this.onModuleToggle;
    }

    @Generated
    public StopWatch getNameUpdateStopWatch() {
        return this.nameUpdateStopWatch;
    }

    @Generated
    public Listener<PreUpdateEvent> getOnPreUpdate() {
        return this.onPreUpdate;
    }

    @Generated
    public Listener<Render2DEvent> getOnRender2D() {
        return this.onRender2D;
    }

    @Generated
    public void setSuffix(BooleanValue suffix) {
        this.suffix = suffix;
    }

    @Generated
    public void setLowercase(BooleanValue lowercase) {
        this.lowercase = lowercase;
    }

    @Generated
    public void setRemoveSpaces(BooleanValue removeSpaces) {
        this.removeSpaces = removeSpaces;
    }

    @Generated
    public void setShaders(BooleanValue booleanValue) {
        this.aoc = booleanValue;
    }

    @Generated
    public void setToggleNotifications(BooleanValue toggleNotifications) {
        this.toggleNotifications = toggleNotifications;
    }

    @Generated
    public void setAllEntries(List<ArrayListEntry> var1) {
        this.allEntries = var1;
    }

    @Generated
    public void setActiveEntries(List<ArrayListEntry> var1) {
        this.activeEntries = var1;
    }

    @Generated
    public void setInformationType(ModeValue modeValue) {
        this.informationType = modeValue;
    }

    @Generated
    public void setWidthComparator(Font var1) {
        this.widthComparator = var1;
    }

    @Generated
    public void setNameUpdateStopWatch(StopWatch var1) {
        this.nameUpdateStopWatch = var1;
    }

    @Generated
    public void n(float var1) {
        this.moduleSpacing = var1;
    }

    @Generated
    public void setEdgeOffset(float var1) {
        this.edgeOffset = var1;
    }

    private static void snapToTarget(ArrayListEntry var0) {
        var0.position = var0.targetPosition;
    }
}
