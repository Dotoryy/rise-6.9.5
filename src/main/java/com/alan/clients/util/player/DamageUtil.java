package com.alan.clients.util.player;

import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.DamageType;
import lombok.Generated;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public final class DamageUtil implements InstanceAccess {
    public static void damagePlayer(double var0) {
        damagePlayer(DamageType.POSITION, var0, true, true);
    }

    public static void damagePlayer(DamageType var0, double var1, boolean var3, boolean var4) {
        if ((!var3 || aEg.thePlayer.onGround) && (!var4 || aEg.thePlayer.hurtTime == 0)) {
            double d0 = aEg.thePlayer.posX;
            double d1 = aEg.thePlayer.posY;
            double d2 = aEg.thePlayer.posZ;
            double d3 = 3.1;
            if (aEg.thePlayer.isPotionActive(Potion.jump)) {
                int i = aEg.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
                d3 += i + 1;
            }

            int j = (int)Math.ceil(d3 / var1);

            for (int k = 0; k < j; k++) {
                switch (var0) {
                    case POSITION_ROTATION:
                        PacketUtil.send(new C06PacketPlayerPosLook(d0, d1 + var1, d2, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false));
                        PacketUtil.send(new C06PacketPlayerPosLook(d0, d1, d2, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false));
                        break;
                    case POSITION:
                        PacketUtil.send(new C04PacketPlayerPosition(d0, d1 + var1, d2, false));
                        PacketUtil.send(new C04PacketPlayerPosition(d0, d1, d2, false));
                }
            }

            PacketUtil.send(new C03PacketPlayer(true));
        }
    }

    public static void damagePlayer(DamageType var0, double var1, int var3, boolean var4, boolean var5) {
        if ((!var4 || aEg.thePlayer.onGround) && (!var5 || aEg.thePlayer.hurtTime == 0)) {
            double d0 = aEg.thePlayer.posX;
            double d1 = aEg.thePlayer.posY;
            double d2 = aEg.thePlayer.posZ;

            for (int i = 0; i < var3; i++) {
                switch (var0) {
                    case POSITION_ROTATION:
                        PacketUtil.send(new C06PacketPlayerPosLook(d0, d1 + var1, d2, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false));
                        PacketUtil.send(new C06PacketPlayerPosLook(d0, d1, d2, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, false));
                        break;
                    case POSITION:
                        PacketUtil.send(new C04PacketPlayerPosition(d0, d1 + var1, d2, false));
                        PacketUtil.send(new C04PacketPlayerPosition(d0, d1, d2, false));
                }
            }

            PacketUtil.send(new C03PacketPlayer(true));
        }
    }

    @Generated
    private DamageUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
