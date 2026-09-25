package de.florianmichael.vialoadingbase.platform.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.vialoadingbase.provider.VLBBaseVersionProvider;

public class VLBViaProviders implements ViaPlatformLoader {
    public VLBViaProviders() {
    }

    @Override
    public void load() {
        ViaProviders viaproviders = Via.getManager().getProviders();
        viaproviders.use(VersionProvider.class, new VLBBaseVersionProvider());
        if (ViaLoadingBase.getInstance().getProviders() != null) {
            ViaLoadingBase.getInstance().getProviders().accept(viaproviders);
        }
    }

    @Override
    public void unload() {
    }
}
