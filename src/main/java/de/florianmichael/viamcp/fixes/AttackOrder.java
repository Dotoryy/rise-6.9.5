package de.florianmichael.viamcp.fixes;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;

public class AttackOrder {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public AttackOrder() {
    }

    public static void sendConditionalSwing(MovingObjectPosition hit) {
        if (hit != null && hit.typeOfHit != MovingObjectType.ENTITY) {
            mc.thePlayer.swingItem();
        }
    }

    public static void sendFixedAttack(EntityPlayer player, Entity entity) {
        if (ViaLoadingBase.getInstance().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8)) {
            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(player, entity);
        } else {
            mc.playerController.attackEntity(player, entity);
            mc.thePlayer.swingItem();
        }
    }
}
