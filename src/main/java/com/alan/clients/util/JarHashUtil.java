package com.alan.clients.util;

import com.alan.clients.util.ReflectionUtil;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class JarHashUtil {
    public JarHashUtil() {
    }

    public static String tZ() {
        try {
            File file1 = new File(ReflectionUtil.rX());
            return Files.asByteSource(file1).hash(Hashing.md5()).toString();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return "";
        } catch (URISyntaxException urisyntaxexception) {
            throw new RuntimeException(urisyntaxexception);
        }
    }
}
