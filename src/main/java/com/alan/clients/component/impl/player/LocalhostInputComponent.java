package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import java.util.Objects;
import net.minecraft.network.play.client.C0CPacketInput;

public final class LocalhostInputComponent extends Component {
    @EventLink
    public final Listener<PreUpdateEvent> iE = var0 -> {};
    @EventLink
    public final Listener<PreUpdateEvent> iF = var0 -> {
        if (Objects.equals(LastConnectionComponent.ip, "localhost")) {
            aEg.getNetHandler()
                .addToSendQueue(
                    new C0CPacketInput(
                        aEg.thePlayer.moveStrafing, aEg.thePlayer.moveForward, aEg.thePlayer.movementInput.jump, aEg.thePlayer.movementInput.sneak
                    )
                );
        }
    };

    public LocalhostInputComponent() {
    }
}
