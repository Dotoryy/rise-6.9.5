package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class WatchdogPredictionNoFall extends Mode<NoFall> {
    private boolean Ti;
    @EventLink(value = 0)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        boolean flag = false;
        if (!this.e(Flight.class).isEnabled()) {
            for (int i = 0; i <= 200; i++) {
                WorldClient worldclient = PlayerUtil.aEg.theWorld;
                BlockPos blockpos = new BlockPos(aEg.thePlayer.posX, aEg.thePlayer.posY - i, aEg.thePlayer.posZ);
                if (worldclient.getBlockState(blockpos).getBlock() != Blocks.air || aEg.thePlayer.onGround) {
                    flag = false;
                    break;
                }

                flag = true;
            }

            if (aEg.thePlayer.fallDistance > 3.0F && !flag) {
                aEg.timer.dzD = 0.5F;
                Vec3 vec3 = aEg.thePlayer.getPositionEyes(1.0F);
                Vec3 vec31 = new Vec3(vec3.xCoord, 0.0, vec3.zCoord);
                MovingObjectPosition movingobjectposition = aEg.theWorld.rayTraceBlocks(vec3, vec31, false, false, false);
                if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.BLOCK && movingobjectposition.hitVec != null) {
                    PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, movingobjectposition.hitVec.yCoord, aEg.thePlayer.posZ, true));
                    aEg.thePlayer.fallDistance = 0.0F;
                }
            }
        }
    };

    public WatchdogPredictionNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }
}
