package com.alan.clients.util.vantage;

public class OSUtil {
    private static final String osName = System.getProperty("os.name").toLowerCase();

    public OSUtil() {
    }

    public static OperatingSystem wb() {
        if (osName.contains("win")) {
            return OperatingSystem.WINDOWS;
        } else if (osName.contains("mac")) {
            return OperatingSystem.MACOSX;
        } else if (!osName.contains("nix") && !osName.contains("nux") && !osName.contains("aix")) {
            System.exit(0);
            return null;
        }
        return OperatingSystem.LINUX;
    }
}
