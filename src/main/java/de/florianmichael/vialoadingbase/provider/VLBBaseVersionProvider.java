package de.florianmichael.vialoadingbase.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;
import de.florianmichael.vialoadingbase.ViaLoadingBase;

public class VLBBaseVersionProvider extends BaseVersionProvider {
    public VLBBaseVersionProvider() {
    }

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection userConnection) throws Exception {
        return userConnection.isClientSide() ? ViaLoadingBase.getInstance().getTargetVersion() : super.getClosestServerProtocol(userConnection);
    }
}
