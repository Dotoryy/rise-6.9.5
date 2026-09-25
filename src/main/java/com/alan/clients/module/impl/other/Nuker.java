package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.pathfinding.unlegit.MainPathFinder;
import com.alan.clients.util.pathfinding.unlegit.Vec3;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.player.GUIDetectionComponent;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.c;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.other.nuker.name", description = "module.other.nuker.description", category = Category.PLAYER)
public final class Nuker extends Module {
    private final ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Nearby")).add(new SubMode("Teleport")).setDefault("Nearby");
    private final NumberValue delay = new NumberValue("Delay", this, 150, 0, 5000, 50);
    private final NumberValue range = new NumberValue("Range", this, 4, 1, 6, 0.5);
    private final BooleanValue rotations = new BooleanValue("Rotations", this, false);
    private final BooleanValue scatter = new BooleanValue("Scatter", this, false);
    private final BooleanValue swing = new BooleanValue("Swing", this, false);
    private final StopWatch delayStopWatch = new StopWatch();
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (!GUIDetectionComponent.inGUI()) {
            double d0;
            {
                d0 = this.range.wo().doubleValue();
                String s = this.mode.wo().getName();
                switch (s) {
                    case "Nearby":
                        if (this.delayStopWatch.T(this.delay.wo().longValue())) {
                            new Thread(() -> this.nuke(d0, var1.getPosX(), var1.getPosY(), var1.getPosZ())).start();
                            this.delayStopWatch.aX();
                        }

                        return;
                    case "Teleport":
                        break;
                    default:
                        return;
                }
            }

            if (aEg.gameSettings.cgI.isKeyDown() && this.delayStopWatch.T(this.delay.wo().longValue())) {
                BlockPos blockpos = aEg.thePlayer.rayTrace(999.0, 1.0F).getBlockPos();
                if (aEg.theWorld.getBlockState(blockpos).getBlock() instanceof BlockAir) {
                    return;
                }

                if (aEg.thePlayer.capabilities.allowFlying && !aEg.thePlayer.capabilities.isFlying) {
                    PlayerCapabilities playercapabilities = aEg.thePlayer.capabilities;
                    playercapabilities.isFlying = true;
                    PacketUtil.sendNoEvent(new C13PacketPlayerAbilities(playercapabilities));
                }

                new Thread(
                        () -> {
                            List list = MainPathFinder.a(
                                new Vec3(var1.getPosX(), var1.getPosY(), var1.getPosZ()), new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ()), false
                            );
                            if (list != null) {
                                for (Vec3 ahyx : (Iterable<Vec3>)list) {
                                    PacketUtil.sendNoEvent(new C04PacketPlayerPosition(ahyx.getX(), ahyx.getY(), ahyx.getZ(), false));
                                }

                                this.nuke(d0, blockpos.getX(), blockpos.getY(), blockpos.getZ());
                                Collections.reverse(list);

                                for (Vec3 ahy : (Iterable<Vec3>)list) {
                                    PacketUtil.sendNoEvent(new C04PacketPlayerPosition(ahy.getX(), ahy.getY(), ahy.getZ(), false));
                                }
                            }
                        }
                    )
                    .start();
                this.delayStopWatch.aX();
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var0 -> {
        if (var0.getPacket() instanceof c c && !c.isChat() && c.getChatComponent().getUnformattedText().equals("You can't build outside the plot!")) {
            var0.setCancelled();
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> this.toggle();

    public Nuker() {
    }

    private void nuke(double var1, double var3, double var5, double var7) {
        for (double d0 = -var1; d0 < var1; d0++) {
            for (double d1 = var1; d1 > -var1; d1--) {
                for (double d2 = -var1; d2 < var1; d2++) {
                    if (!this.scatter.wo() || (aEg.thePlayer.ticksExisted % 2 == 0 ? d0 : d2) % 2.0 == 0.0) {
                        BlockPos blockpos = new BlockPos(var3 + d0, var5 + d1, var7 + d2);
                        if (!(aEg.theWorld.getBlockState(blockpos).getBlock() instanceof BlockAir)) {
                            if (this.rotations.wo()) {
                                Vector2f vector2f = RotationUtil.d(new Vector3d(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
                                PacketUtil.sendNoEvent(new C05PacketPlayerLook(vector2f.x, vector2f.y, false));
                            }

                            PacketUtil.sendNoEvent(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockpos, EnumFacing.UP));
                            if (this.swing.wo()) {
                                aEg.thePlayer.swingItem();
                            }
                        }
                    }
                }
            }
        }
    }
}
