package com.alan.clients.module.impl.render;

import com.alan.clients.component.impl.render.ProjectionComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.render.targetinfo.BMSTargetInfo;
import com.alan.clients.module.impl.render.targetinfo.CompactTargetInfo;
import com.alan.clients.module.impl.render.targetinfo.CreidaModernTargetInfo;
import com.alan.clients.module.impl.render.targetinfo.ExhibitionTargetInfo;
import com.alan.clients.module.impl.render.targetinfo.GodlyTargetInfo;
import com.alan.clients.module.impl.render.targetinfo.ModernTargetInfo;
import com.alan.clients.module.impl.render.targetinfo.NovolineTargetInfo;
import com.alan.clients.module.impl.render.targetinfo.WurstTargetInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.value.impl.ModeValue;
import javax.vecmath.Vector4d;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.Entity;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.render.targetinfo.name", description = "module.render.targetinfo.description", category = Category.RENDER)
public final class TargetInfo extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new BMSTargetInfo("BMS", this))
        .add(new CompactTargetInfo("Compact", this))
        .add(new ModernTargetInfo("Modern", this))
        .add(new NovolineTargetInfo("Novoline", this))
        .add(new GodlyTargetInfo("Godly", this))
        .add(new CreidaModernTargetInfo("Creida Modern", this))
        .add(new ExhibitionTargetInfo("Exhibition", this))
        .add(new WurstTargetInfo("Wurst", this))
        .setDefault("Modern");
    public final DragValue positionValue = new DragValue("Position", this, new Vector2d(200.0, 200.0));
    public final BooleanValue followPlayer = new BooleanValue("Follow Player", this, false);
    public Vector2d position = new Vector2d(0.0, 0.0);
    public Entity target;
    public double distanceSq;
    public boolean inWorld;
    public StopWatch stopwatch = new StopWatch();
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (aEg.currentScreen instanceof GuiChat) {
            this.stopwatch.aX();
            this.target = aEg.thePlayer;
        }

        if (this.target == null) {
            this.inWorld = false;
        } else {
            this.distanceSq = aEg.thePlayer.getDistanceSqToEntity(this.target);
            this.inWorld = aEg.theWorld.loadedEntityList.contains(this.target);
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1 -> {
        if (var1.getLiving() instanceof AbstractClientPlayer) {
            this.target = var1.getLiving();
            this.stopwatch.aX();
        }
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (this.target != null) {
            if (this.followPlayer.wo() && this.target != aEg.thePlayer) {
                Vector4d vector4d = ProjectionComponent.e(this.target);
                if (vector4d == null) {
                    return;
                }

                this.position.x = vector4d.z;
                this.position.y = vector4d.w - (vector4d.w - vector4d.y) / 2.0 - this.positionValue.aHe.y / 2.0;
            } else {
                this.position = this.positionValue.apP;
            }
        }
    };

    public TargetInfo() {
    }
}
