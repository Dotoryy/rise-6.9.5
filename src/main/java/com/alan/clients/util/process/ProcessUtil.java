package com.alan.clients.util.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtil {
    public ProcessUtil() {
    }

    public static boolean running(String var0) {
        try {
            StringBuilder stringbuilder = new StringBuilder();
            Process process = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\tasklist.exe");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s;
            while ((s = bufferedreader.readLine()) != null) {
                stringbuilder.append(s);
            }

            bufferedreader.close();
            return stringbuilder.toString().toLowerCase().contains(var0.toLowerCase());
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return false;
        }
    }
}
