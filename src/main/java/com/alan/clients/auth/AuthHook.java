package com.alan.clients.auth;

//add code
public final class AuthHook {

    private static volatile AuthProvider provider = new DefaultAuthProvider();

    private AuthHook() {
    }

    public static AuthProvider provider() {
        return provider;
    }

    public static void install(AuthProvider replacement) {
        provider = replacement != null ? replacement : new DefaultAuthProvider();
    }

    private static final class DefaultAuthProvider implements AuthProvider {

        private static final boolean AUTO_LOGIN =
                !"false".equalsIgnoreCase(System.getProperty("rise.auth.autologin"));

        private volatile boolean authenticated;
        private volatile String name = System.getProperty("rise.auth.username");

        @Override
        public String username() {
            if (name == null || name.isEmpty()) {
                name = sessionName();
            }
            return name;
        }

        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }

        @Override
        public boolean login(String credential) {
            if (!AUTO_LOGIN) {
                return false;
            }
            authenticated = true;
            return true;
        }

        @Override
        public void logout() {
            authenticated = false;
        }

        private static String sessionName() {
            try {
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
                if (mc != null && mc.getSession() != null) {
                    String s = mc.getSession().getUsername();
                    if (s != null && !s.isEmpty()) {
                        return s;
                    }
                }
            } catch (Throwable ignored) {
            }
            return "Player";
        }
    }
}
