package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.value.Mode;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.util.BlockPos;

public class ElytraNoFall extends Mode<NoFall> {
    private boolean jumpWasDown = false;
    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = var1x -> {
        this.jumpWasDown = aEg.gameSettings.keyBindJump.isKeyDown();
        if (aEg.thePlayer.fallDistance > 2.5) {
            aEg.gameSettings.keyBindJump.setPressed(true);
            int i = (int)Math.floor(aEg.thePlayer.posX);
            int j = (int)Math.floor(aEg.thePlayer.posY);
            int k = (int)Math.floor(aEg.thePlayer.posZ);
            BlockPos blockpos = new BlockPos(i, j - 1, k);
            if (!aEg.theWorld.getBlockState(blockpos).getBlock().getMaterial().isReplaceable()) {
                this.startFallFlying();
                aEg.thePlayer.jump();
                aEg.gameSettings.keyBindJump.setPressed(this.jumpWasDown);
            }
        }
    };

    public ElytraNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }

    @Override
    public void onDisable() {
        aEg.gameSettings.keyBindJump.setPressed(false);
    }

    private void startFallFlying() {
        try {
            if (!ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_9)) {
                return;
            }

            UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
            PacketWrapper packetwrapper = PacketWrapper.create(ServerboundPackets1_9.PLAYER_COMMAND, userconnection);
            packetwrapper.write(Types.VAR_INT, aEg.thePlayer.getEntityId());
            packetwrapper.write(Types.VAR_INT, 8);
            packetwrapper.write(Types.VAR_INT, 0);
            packetwrapper.sendToServer(Protocol1_9To1_8.class);
        } catch (Exception exception) {
        }
    }
}
