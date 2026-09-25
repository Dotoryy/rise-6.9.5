package com.alan.clients.util.account;

import com.alan.clients.util.file.alt.AltManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;

public class UsernameGenerator {
    private static final String aGe = "https://raw.githubusercontent.com/jeanphorn/wordlist/master/usernames.txt";
    //add code
    // fetching raw.githubusercontent on EVERY single click? STOP DDOS GITHUB CACHE THIS!!! billionaire????
    private static final File NAMES_FILE = new File(AltManager.ALT_DIRECTORY, "names.txt");
    private static volatile String[] wordList;
    private static volatile boolean downloading;

    public UsernameGenerator() {
    }

    public static boolean isDownloading() {
        return downloading;
    }

    private static String[] readNamesFile() {
        if (!NAMES_FILE.isFile()) {
            return null;
        }

        try {
            List<String> list = Files.readAllLines(NAMES_FILE.toPath(), StandardCharsets.UTF_8);
            // a torn write can leave a readable file with nothing usable in it
            return usable(list) ? list.toArray(new String[0]) : null;
        } catch (IOException ioexception) {
            return null;
        }
    }

    private static boolean usable(List<String> var0) {
        for (String s : var0) {
            if (s.length() >= 3 && s.length() <= 6) {
                return true;
            }
        }

        return false;
    }

    private static void writeNamesFile(String var0) {
        try {
            File file = NAMES_FILE.getParentFile();
            if (file != null && !file.isDirectory()) {
                file.mkdirs();
            }

            Files.write(NAMES_FILE.toPath(), var0.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    public static String[] sx() {
        String[] astring = wordList;
        if (astring != null) {
            return astring;
        }

        astring = readNamesFile();
        if (astring != null) {
            wordList = astring;
            return astring;
        }

        downloading = true;

        try {
            HttpsURLConnection httpsurlconnection = (HttpsURLConnection)new URL("https://raw.githubusercontent.com/jeanphorn/wordlist/master/usernames.txt")
                .openConnection();
            httpsurlconnection.addRequestProperty("User-Agent", "Mozilla/5.0");
            int i = httpsurlconnection.getResponseCode();
            boolean flag = i / 100 != 2 && i / 100 != 3;
            InputStream inputstream = flag ? httpsurlconnection.getErrorStream() : httpsurlconnection.getInputStream();
            if (inputstream == null) {
                return null;
            }

            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            StringBuilder stringbuilder = new StringBuilder();

            String s;
            while ((s = bufferedreader.readLine()) != null) {
                stringbuilder.append(s).append(System.lineSeparator());
            }

            bufferedreader.close();
            String s1 = stringbuilder.toString();
            String[] astring1 = s1.split(System.lineSeparator());
            writeNamesFile(s1);
            wordList = astring1;
            return astring1;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return null;
        } finally {
            downloading = false;
        }
    }

    public static String sy() {
        String[] astring = ak(1);
        return astring == null ? null : astring[0];
    }

    public static String[] ak(int var0) {
        String[] astring = sx();
        if (astring == null) {
            return null;
        }

        String[] astring1 = new String[var0];
        List list = Arrays.stream(astring).filter(var0x -> var0x.length() >= 3 && var0x.length() <= 6).collect(Collectors.toList());
        //add code
        if (list.isEmpty()) {
            return null;
        }

        for (int i = 0; i < var0; i++) {
            String s = (String)list.get((int)(Math.random() * list.size()));
            String s1 = (String)list.get((int)(Math.random() * list.size()));
            String s2 = B(s, s1);
            astring1[i] = bx(s2);
        }

        return astring1;
    }

    private static String B(String var0, String var1) {
        switch ((int)(Math.random() * 4.0)) {
            case 0:
                return var0 + "_" + var1;
            case 1:
                return var0 + var1.substring(0, 2) + (int)(Math.random() * 100.0);
            case 2:
                int i = (int)(Math.random() * Math.min(var0.length(), var1.length()));
                return var0.substring(0, i) + "_" + var1.substring(i);
            case 3:
                StringBuilder stringbuilder = new StringBuilder(var0).append(var1);
                int j = (int)(Math.random() * stringbuilder.length());
                int k = (int)(Math.random() * stringbuilder.length());
                stringbuilder.insert(j, "_");
                stringbuilder.insert(k, (int)((int)(Math.random() * 100.0)));
                return stringbuilder.toString();
            default:
                return var0 + var1;
        }
    }

    private static String bx(String var0) {
        double d0 = 0.125;
        double d1 = 0.25;
        char[] achar = var0.toCharArray();

        for (int i = 0; i < achar.length; i++) {
            char c0 = achar[i];
            if ((i == 0 || (achar[i - 1] == '_' || Character.isDigit(achar[i - 1])) && Character.isLetter(c0)) && Math.random() < d1) {
                achar[i] = Character.toUpperCase(c0);
            } else {
                char c1 = Character.toLowerCase(c0);
                char c2 = a(c1);
                if (c2 != c1 && Math.random() < d0) {
                    achar[i] = c2;
                    d0 *= 0.5;
                }
            }
        }

        return new String(achar);
    }

    private static char a(char var0) {
        if (var0 == 'a') {
            return '4';
        } else if (var0 == 'e') {
            return '3';
        } else if (var0 == 'i') {
            return '1';
        } else if (var0 == 'o') {
            return '0';
        }
        return var0 == 't' ? '7' : var0;
    }
}
