package com.alan.clients.component.impl.viamcp;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.MouseOverEvent;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;

public final class HitboxFixComponent extends Component {
    @EventLink
    public final Listener<MouseOverEvent> onMouseOver = var0 -> ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8);

    public HitboxFixComponent() {
    }
}
