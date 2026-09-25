package com.alan.clients.util;

import com.alan.clients.util.OS;
import lombok.Generated;

public final class OSUtil {
    public static OS rV() {
        String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win")
            ? OS.WINDOWS
            : (
                s.contains("mac")
                    ? OS.MACOS
                    : (
                        s.contains("solaris")
                            ? OS.SOLARIS
                            : (s.contains("sunos") ? OS.SOLARIS : (s.contains("linux") ? OS.LINUX : (s.contains("unix") ? OS.LINUX : OS.UNKNOWN)))
                    )
            );
    }

    @Generated
    private OSUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
