package com.alan.clients.component.impl.viamcp;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.MinimumMotionEvent;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;

public final class MinimumMotionFixComponent extends Component {
    @EventLink
    public final Listener<MinimumMotionEvent> onMinimumMotion = var0 -> ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8);

    public MinimumMotionFixComponent() {
    }
}
