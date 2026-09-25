package com.alan.clients.util.vantage;

import com.alan.clients.util.vantage.OSUtil;
import com.alan.clients.util.vantage.OperatingSystem;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MachineFingerprint {
    static String hwid = bytesToHex(generateHWID()) + "dleotn6oc94kb" + vX();

    public MachineFingerprint() {
    }

    public static String vW() {
        return hwid;
    }

    private static String vX() {
        String s = vY();
        String s1 = vZ();
        return OSUtil.wb().equals(OperatingSystem.WINDOWS) && s.length() >= 5 && s1.length() >= 5
            ? new String(Base64.getEncoder().encode((s + "_" + s1).getBytes())).replaceAll("==", "")
            : bytesToHex(generateHWID());
    }

    private static String vY() {
        return f(new String[]{"cmd.exe", "/c", "wmic baseboard get serialnumber"});
    }

    private static String vZ() {
        return f(new String[]{"cmd.exe", "/c", "wmic cpu get processorid"});
    }

    private static String f(String[] var0) {
        StringBuilder stringbuilder = new StringBuilder();

        try {
            Process process = Runtime.getRuntime().exec(var0);
            process.waitFor();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String s;
            for (boolean flag = false; (s = bufferedreader.readLine()) != null; flag = true) {
                if (flag && !s.trim().isEmpty()) {
                    stringbuilder.append(s.trim());
                    break;
                }
            }

            bufferedreader.close();
        } catch (Exception exception) {
            return "unknown";
        }

        return stringbuilder.toString();
    }

    private static byte[] generateHWID() {
        try {
            OperatingSystem operatingSystem = OSUtil.wb();
            MessageDigest messagedigest = MessageDigest.getInstance("SHA-256");
            String s;
            if (operatingSystem.equals(operatingSystem.WINDOWS)) {
                s = "VANTAGE_"
                    + System.getProperty("os.name")
                    + System.getProperty("os.arch")
                    + System.getProperty("os.version")
                    + System.getProperty("user.name")
                    + System.getProperty("user.home")
                    + System.getenv("NUMBER_OF_PROCESSORS");
            } else {
                s = "VANTAGE_"
                    + operatingSystem.getOs()
                    + System.getProperty("os.arch")
                    + Runtime.getRuntime().availableProcessors()
                    + System.getenv("PROCESSOR_IDENTIFIER")
                    + System.getenv("PROCESSOR_ARCHITECTURE")
                    + System.getenv("PROCESSOR_ARCHITEW6432")
                    + System.getenv("NUMBER_OF_PROCESSORS")
                    + System.getenv("COMPUTERNAME")
                    + System.getenv("os")
                    + System.getenv("SystemRoot")
                    + System.getenv("HOMEDRIVE")
                    + System.getenv("PROCESSOR_LEVEL")
                    + System.getenv("PROCESSOR_REVISION")
                    + System.getenv("HOME")
                    + System.getenv("HOSTNAME")
                    + System.getenv("SHELL")
                    + System.getenv("LOGNAME")
                    + System.getenv("USERNAME");
            }

            return messagedigest.digest(s.getBytes());
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            throw new Error("HWIDException: ", nosuchalgorithmexception);
        }
    }

    private static String bytesToHex(byte[] var0) {
        StringBuilder stringbuilder = new StringBuilder(2 * var0.length);

        for (byte b0 : var0) {
            String s = Integer.toHexString(255 & b0);
            if (s.length() == 1) {
                stringbuilder.append('0');
            }

            stringbuilder.append(s);
        }

        return stringbuilder.toString();
    }
}
