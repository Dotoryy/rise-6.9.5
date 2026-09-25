package com.alan.clients.compat;

//add code
public final class NetworkToggles {

    private static final boolean REMOTE_SCRIPTS =
            Boolean.getBoolean("rise.net.remotescripts");

    private static final boolean ALT_SERVICE =
            Boolean.getBoolean("rise.net.altservice");

    private static final boolean VERSION_CHECK =
            Boolean.getBoolean("rise.net.versioncheck");

    private NetworkToggles() {
    }

    public static boolean remoteScripts() {
        return !OfflineMode.offline() && REMOTE_SCRIPTS;
    }

    public static boolean altService() {
        return !OfflineMode.offline() && ALT_SERVICE;
    }

    public static boolean versionCheck() {
        return !OfflineMode.offline() && VERSION_CHECK;
    }
}
