package com.alan.clients.module.impl.player;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import java.util.HashSet;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.m;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.player.antifireball.name", description = "module.player.antifireball.description", category = Category.PLAYER)
public class AntiFireBall extends Module {
    private final BooleanValue rotations = new BooleanValue("Rotate", this, true);
    private final BooleanValue movementCorrection = new BooleanValue("Movement Correction", this, true, () -> !this.rotations.wo());
    private final BooleanValue badPacketsCheck = new BooleanValue("Bad Packets Check", this, false);
    public final StopWatch cooldownStopWatch = new StopWatch();
    public int cooldownMs = 0;
    private final HashSet<UUID> attackedFireballs = new HashSet<>();
    @EventLink(value = -100)
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> this.handleFireballs();
    @EventLink(value = -100)
    public final Listener<TickEvent> onTick = var1 -> this.handleFireballs();

    public AntiFireBall() {
    }

    public final void handleFireballs() {
        if ((!BadPacketsComponent.aW() || !this.badPacketsCheck.wo()) && this.cooldownStopWatch.T(this.cooldownMs)) {
            for (Entity entity : aEg.theWorld.loadedEntityList) {
                if (entity instanceof EntityFireball && entity.getDistanceToEntity(aEg.thePlayer) < 6.0F) {
                    if (this.rotations.wo()) {
                        RotationComponent.setRotations(RotationUtil.y(entity), 10.0, this.movementCorrection.wo() ? MovementFix.NORMAL : MovementFix.OFF);
                    }

                    MoveUtil.strafe(0.0);
                    if (entity.getDistanceToEntity(aEg.thePlayer) <= 3.0F && !this.attackedFireballs.contains(entity.getUniqueID())) {
                        PacketUtil.send(new m());
                        PacketUtil.send(new C02PacketUseEntity(entity, Action.ATTACK));
                        this.attackedFireballs.add(entity.getUniqueID());
                        break;
                    }

                    PlayerUtil.sendClick(0, true);
                    PlayerUtil.sendClick(0, false);
                    break;
                }
            }
        }
    }
}
