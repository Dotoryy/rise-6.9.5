package com.alan.clients.component.impl.viamcp;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;

public final class SpeedPotionFixComponent extends Component {
    @EventLink(value = 1)
    public final Listener<StrafeEvent> onStrafe = var0 -> {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17)) {
            if (!aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                return;
            }

            if (PlayerUtil.p(0.0, -1.0, 0.0) == Blocks.packed_ice || PlayerUtil.p(0.0, -1.0, 0.0) == Blocks.ice) {
                return;
            }

            float[][] afloat = new float[][]{{0.11999998F, 0.15599997F}, {0.13999997F, 0.18199998F}};
            Math.min(aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier(), 1);
            aEg.thePlayer.isSprinting();
        }
    };

    public SpeedPotionFixComponent() {
    }
}
