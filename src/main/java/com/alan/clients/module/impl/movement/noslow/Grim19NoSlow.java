package com.alan.clients.module.impl.movement.noslow;

import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.value.Mode;
import com.viaversion.viabackwards.protocol.v1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_18_2to1_19.packet.ServerboundPackets1_19;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.chat.ChatUtil;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;

public class Grim19NoSlow extends Mode<NoSlow> {
    int useTicks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.isUsingItem() && ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_9)) {
            UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
            if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_19)) {
                PacketWrapper packetwrapper = PacketWrapper.create(ServerboundPackets1_19.USE_ITEM, userconnection);
                packetwrapper.write(Types.VAR_INT, 1);
                packetwrapper.write(Types.VAR_INT, aEg.playerController.GZ());
                packetwrapper.sendToServer(Protocol1_19To1_18_2.class);
            } else {
                PacketWrapper packetwrapper1 = PacketWrapper.create(ServerboundPackets1_9.USE_ITEM, userconnection);
                packetwrapper1.write(Types.VAR_INT, 1);
                packetwrapper1.sendToServer(Protocol1_9To1_8.class);
            }

            this.useTicks++;
        } else if (aEg.thePlayer.isUsingItem() && aEg.thePlayer.ticksExisted % 5 == 0) {
            ChatUtil.b("use 1.9+ for this bypass");
        } else {
            this.useTicks = 0;
        }
    };
    @EventLink
    public final Listener<SlowDownEvent> onSlowDown = var1x -> {
        if (this.getParent().food.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
            var1x.setCancelled();
        }

        if (this.getParent().potion.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemPotion) {
            var1x.setCancelled();
        }

        if (this.getParent().sword.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            var1x.setCancelled();
        }

        if (this.getParent().bow.wo() && aEg.thePlayer.isUsingItem() && aEg.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
            var1x.setCancelled();
        }
    };

    public Grim19NoSlow(String var1, NoSlow noSlow) {
        super(var1, noSlow);
    }
}
