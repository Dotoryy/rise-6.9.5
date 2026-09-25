package com.alan.clients.util.ime;

import com.alan.clients.util.ime.PinyinCandidate;
import com.alan.clients.util.ime.PinyinUsageStore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public final class PinyinDictionary {
    private final NavigableMap<String, List<String>> navigableMap;
    private static final int aJM = 64;
    private static final String[] aJN = new String[]{"rise/ime/rise.dict.yaml", "rise/ime/rise_extra_common_words.dict.yaml", "rise/ime/luna_pinyin.sogou2.dict.yaml"};

    private PinyinDictionary(NavigableMap<String, List<String>> navigableMap) {
        this.navigableMap = navigableMap;
    }

    public List<String> s(String string, int n) {
        if (string == null) {
            return Collections.emptyList();
        }
        String string2 = string.toLowerCase(Locale.ROOT);
        List list = (List)this.navigableMap.get(string2);
        if (list != null && !list.isEmpty()) {
            return PinyinDictionary.a(string2, list, n);
        }
        if (n <= 0) {
            return Collections.emptyList();
        }
        ArrayList<String> arrayList = new ArrayList<String>(n);
        String string3 = string2;
        String string4 = string2 + "\uffff";
        Iterator iterator = this.navigableMap.subMap(string3, true, string4, true).values().iterator();
        while (iterator.hasNext()) {
            for (String string5 : (Iterable<String>)(List)iterator.next()) {
                if (string5 == null || string5.isEmpty()) continue;
                arrayList.add(string5);
                if (arrayList.size() < n) continue;
                return arrayList;
            }
        }
        return PinyinDictionary.a(string2, arrayList, n);
    }

    private static List<String> a(String string, List<String> list, int n) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        if (list.size() <= 1) {
            return list;
        }
        PinyinUsageStore agy2 = PinyinUsageStore.uC();
        ArrayList<PinyinCandidate> arrayList = new ArrayList<PinyinCandidate>(list.size());
        for (int i = 0; i < list.size(); ++i) {
            String string2 = list.get(i);
            if (string2 == null || string2.isEmpty()) continue;
            int n2 = agy2.C(string, string2);
            int n3 = agy2.cb(string2);
            int n4 = n2 * 1000 + n3;
            arrayList.add(new PinyinCandidate(string2, n4, i));
        }
        arrayList.sort((agv2, agv3) -> {
            int n22 = Integer.compare(agv3.aJP, agv2.aJP);
            if (n22 != 0) {
                return n22;
            }
            return Integer.compare(agv2.aJQ, agv3.aJQ);
        });
        int n5 = n <= 0 ? arrayList.size() : Math.min(n, arrayList.size());
        ArrayList<String> arrayList2 = new ArrayList<String>(n5);
        int n6 = 0;
        while (n6 < n5) {
            arrayList2.add(((PinyinCandidate)arrayList.get(n6)).aJO);
            ++n6;
        }
        return arrayList2;
    }

    public static PinyinDictionary um() {
        TreeMap<String, List<String>> treeMap = new TreeMap<String, List<String>>();
        PinyinDictionary.a(treeMap, "a", "\u554a", "\u963f", "\u5416");
        PinyinDictionary.a(treeMap, "o", "\u54e6", "\u5662", "\u5594");
        PinyinDictionary.a(treeMap, "e", "\u5443", "\u989d", "\u9e45");
        PinyinDictionary.a(treeMap, "ai", "\u7231", "\u77ee", "\u6328", "\u54ce");
        PinyinDictionary.a(treeMap, "ei", "\u8bf6", "\u6b38");
        PinyinDictionary.a(treeMap, "ao", "\u5965", "\u71ac", "\u50b2", "\u51f9");
        PinyinDictionary.a(treeMap, "ou", "\u6b27", "\u5076", "\u54e6");
        PinyinDictionary.a(treeMap, "an", "\u5b89", "\u6309", "\u6848");
        PinyinDictionary.a(treeMap, "en", "\u55ef", "\u6069");
        PinyinDictionary.a(treeMap, "ang", "\u6602", "\u76ce");
        PinyinDictionary.a(treeMap, "eng", "\u55ef");
        PinyinDictionary.a(treeMap, "er", "\u4e8c", "\u800c", "\u8033");
        PinyinDictionary.a(treeMap, "ni", "\u4f60", "\u5c3c", "\u5462");
        PinyinDictionary.a(treeMap, "hao", "\u597d", "\u53f7", "\u6d69");
        PinyinDictionary.a(treeMap, "ma", "\u5417", "\u5988", "\u9a6c");
        PinyinDictionary.a(treeMap, "wo", "\u6211", "\u63e1", "\u7a9d");
        PinyinDictionary.a(treeMap, "shi", "\u662f", "\u65f6", "\u4e8b", "\u5341");
        PinyinDictionary.a(treeMap, "xian", "\u663e", "\u73b0", "\u5148", "\u7ebf");
        PinyinDictionary.a(treeMap, "xianshi", "\u663e\u793a");
        PinyinDictionary.a(treeMap, "xuan", "\u6e32", "\u9009", "\u5ba3");
        PinyinDictionary.a(treeMap, "ran", "\u67d3", "\u7136", "\u71c3");
        PinyinDictionary.a(treeMap, "xuanran", "\u6e32\u67d3");
        PinyinDictionary.a(treeMap, "bps", "BPS");
        PinyinDictionary.a(treeMap, "de", "\u7684", "\u5f97", "\u5fb7");
        PinyinDictionary.a(treeMap, "le", "\u4e86", "\u4e50", "\u52d2");
        PinyinDictionary.a(treeMap, "bu", "\u4e0d", "\u90e8", "\u6b65");
        PinyinDictionary.a(treeMap, "zai", "\u5728", "\u518d", "\u8f7d");
        PinyinDictionary.a(treeMap, "you", "\u6709", "\u53c8", "\u53cb", "\u53f3");
        PinyinDictionary.a(treeMap, "mei", "\u6ca1", "\u7f8e", "\u6bcf");
        PinyinDictionary.a(treeMap, "yao", "\u8981", "\u6447", "\u836f");
        PinyinDictionary.a(treeMap, "hen", "\u5f88", "\u72e0", "\u6068");
        PinyinDictionary.a(treeMap, "kan", "\u770b", "\u780d", "\u520a");
        PinyinDictionary.a(treeMap, "ting", "\u542c", "\u505c", "\u5385");
        PinyinDictionary.a(treeMap, "shuo", "\u8bf4", "\u7855", "\u6714");
        PinyinDictionary.a(treeMap, "xie", "\u5199", "\u4e9b", "\u8c22");
        PinyinDictionary.a(treeMap, "qing", "\u8bf7", "\u6e05", "\u60c5");
        PinyinDictionary.a(treeMap, "xiexie", "\u8c22\u8c22");
        PinyinDictionary.a(treeMap, "nihao", "\u4f60\u597d");
        PinyinDictionary.a(treeMap, "zaijian", "\u518d\u89c1");
        PinyinDictionary.a(treeMap, "shiwo", "\u662f\u6211");
        PinyinDictionary.a(treeMap, "li", "\u91cc", "\u674e", "\u7406", "\u529b", "\u793c", "\u5229", "\u79bb", "\u68a8");
        PinyinDictionary.a(treeMap, "liu", "\u516d", "\u6d41", "\u5218", "\u7559", "\u67f3", "\u6e9c");
        PinyinDictionary.a(treeMap, "lin", "\u6797", "\u4e34", "\u90bb", "\u7433", "\u6dcb");
        PinyinDictionary.a(treeMap, "ling", "\u96f6", "\u9886", "\u4ee4", "\u7075", "\u73b2");
        PinyinDictionary.a(treeMap, "lian", "\u8fde", "\u8138", "\u7ec3", "\u604b", "\u83b2");
        PinyinDictionary.a(treeMap, "liang", "\u4e24", "\u91cf", "\u4eae", "\u826f", "\u51c9");
        PinyinDictionary.a(treeMap, "lu", "\u8def", "\u5f55", "\u9732", "\u5362", "\u9c81");
        PinyinDictionary.a(treeMap, "lou", "\u697c", "\u55bd", "\u6f0f", "\u6402");
        PinyinDictionary.a(treeMap, "yi", "\u4e00", "\u4ebf", "\u4ee5");
        PinyinDictionary.a(treeMap, "er", "\u4e8c", "\u800c", "\u5c14");
        PinyinDictionary.a(treeMap, "san", "\u4e09", "\u6563", "\u4f1e");
        PinyinDictionary.a(treeMap, "si", "\u56db", "\u6b7b", "\u53f8");
        PinyinDictionary.a(treeMap, "wu", "\u4e94", "\u65e0", "\u5434");
        PinyinDictionary.a(treeMap, "liu", "\u516d", "\u6d41", "\u5218");
        PinyinDictionary.a(treeMap, "qi", "\u4e03", "\u8d77", "\u5176");
        PinyinDictionary.a(treeMap, "ba", "\u516b", "\u5427", "\u628a");
        PinyinDictionary.a(treeMap, "jiu", "\u4e5d", "\u4e45", "\u9152");
        PinyinDictionary.a(treeMap, "shi", "\u5341");
        PinyinDictionary.a(treeMap, "lv", "\u7eff", "\u5415", "\u65c5");
        PinyinDictionary.a(treeMap, "nve", "\u8650");
        PinyinDictionary.c(treeMap);
        PinyinDictionary.b(treeMap);
        return new PinyinDictionary(PinyinDictionary.a(treeMap));
    }

    private static void a(Map<String, List<String>> map, String string, String ... stringArray) {
        if (string == null || string.isEmpty() || stringArray == null || stringArray.length == 0) {
            return;
        }
        ArrayList<String> arrayList = new ArrayList<String>(stringArray.length);
        for (String string2 : stringArray) {
            if (string2 == null || string2.isEmpty()) continue;
            arrayList.add(string2);
        }
        if (!arrayList.isEmpty()) {
            map.put(string.toLowerCase(Locale.ROOT), Collections.unmodifiableList(arrayList));
        }
    }

    private static void b(Map<String, List<String>> map) {
        try {
            File file;
            File file2 = Minecraft.getMinecraft().mcDataDir;
            if (file2 == null) {
                return;
            }
            File file3 = new File(file2, "Rise");
            if (!file3.isDirectory()) {
                return;
            }
            File file4 = new File(file3, "pinyin_dict.properties");
            if (file4.isFile()) {
                PinyinDictionary.a(file4, map, true);
            }
            if ((file = PinyinDictionary.e(file3)) == null) {
                return;
            }
            File file5 = new File(file3, "pinyin_dict.cache.properties");
            if (file5.isFile() && file5.lastModified() >= file.lastModified()) {
                PinyinDictionary.a(file5, map, true);
                return;
            }
            LinkedHashMap<String, LinkedHashSet<String>> linkedHashMap = new LinkedHashMap<String, LinkedHashSet<String>>();
            PinyinDictionary.a(file, linkedHashMap);
            if (linkedHashMap.isEmpty()) {
                return;
            }
            PinyinDictionary.a(map, linkedHashMap, true);
            PinyinDictionary.b(file5, linkedHashMap);
            return;
        }
        catch (Throwable throwable) {
            return;
        }
    }

    private static NavigableMap<String, List<String>> a(NavigableMap<String, List<String>> navigableMap) {
        return Collections.unmodifiableNavigableMap(navigableMap);
    }

    private static void c(Map<String, List<String>> map) {
        try {
            BufferedReader bufferedReader = null;
            try {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft == null) return;
            if (minecraft.getResourceManager() == null) {
                return;
            }
            String[] stringArray = aJN;
            int n = stringArray.length;
            int n2 = 0;
            while (n2 < n) {
                String string = stringArray[n2];
                if (string != null && !string.isEmpty()) {
                    ResourceLocation resourceLocation = new ResourceLocation(string);
                    InputStream inputStream = minecraft.getResourceManager().getResource(resourceLocation).getInputStream();
                    if (inputStream != null) {
                        LinkedHashMap<String, LinkedHashSet<String>> linkedHashMap = new LinkedHashMap<String, LinkedHashSet<String>>();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        PinyinDictionary.a(bufferedReader, linkedHashMap);
                        bufferedReader.close();
                        PinyinDictionary.a(map, linkedHashMap, false);
                    }
                }
                ++n2;
            }
            return;
            }
            catch (Throwable throwable) {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                    throw throwable;
                }
                throw throwable;
            }
        }
        catch (Throwable throwable) {
            return;
        }
    }

    private static void a(Map<String, List<String>> map, Map<String, LinkedHashSet<String>> map2, boolean bl) {
        if (map == null || map2 == null || map2.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, LinkedHashSet<String>>> iterator = map2.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LinkedHashSet<String>> entry = iterator.next();
            String string = entry.getKey();
            LinkedHashSet<String> linkedHashSet = entry.getValue();
            if (string == null || string.isEmpty() || linkedHashSet == null || linkedHashSet.isEmpty()) continue;
            LinkedHashSet<String> linkedHashSet2 = new LinkedHashSet<String>();
            List<String> list = map.get(string);
            if (bl) {
                linkedHashSet2.addAll(linkedHashSet);
                if (list != null) {
                    linkedHashSet2.addAll(list);
                }
            } else {
                if (list != null) {
                    linkedHashSet2.addAll(list);
                }
                linkedHashSet2.addAll(linkedHashSet);
            }
            ArrayList<String> arrayList = new ArrayList<String>(Math.min(64, linkedHashSet2.size()));
            for (String string2 : linkedHashSet2) {
                if (string2 == null || string2.isEmpty()) continue;
                arrayList.add(string2);
                if (arrayList.size() < 64) continue;
            }
            if (arrayList.isEmpty()) continue;
            map.put(string, Collections.unmodifiableList(arrayList));
        }
        return;
    }

    private static void a(File file, Map<String, List<String>> map, boolean bl) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(file), StandardCharsets.UTF_8));){
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                int n;
                String string2 = string.trim();
                if (string2.isEmpty() || string2.startsWith("#") || (n = string2.indexOf(61)) <= 0) continue;
                String string3 = string2.substring(0, n).trim().toLowerCase(Locale.ROOT);
                String string4 = string2.substring(n + 1).trim();
                if (string3.isEmpty() || string4.isEmpty()) continue;
                String[] stringArray = string4.split(",");
                ArrayList<String> arrayList = new ArrayList<String>(Math.min(stringArray.length, 64));
                String[] stringArray2 = stringArray;
                int length = stringArray2.length;
                for (int i = 0; i < length; ++i) {
                    String string5 = stringArray2[i].trim();
                    if (string5.isEmpty()) continue;
                    arrayList.add(string5);
                    if (arrayList.size() >= 64) break;
                }
                if (arrayList.isEmpty()) continue;
                LinkedHashMap<String, LinkedHashSet<String>> linkedHashMap = new LinkedHashMap<String, LinkedHashSet<String>>();
                linkedHashMap.put(string3, new LinkedHashSet(arrayList));
                PinyinDictionary.a(map, linkedHashMap, bl);
            }
        }
        catch (Throwable throwable) {

        }
    }

    private static File e(File file2) {
        try {
            File file3 = new File(file2, "pinyin.dict.yaml");
            if (file3.isFile()) {
                return file3;
            }
            File file4 = new File(file2, "pinyin_dict.dict.yaml");
            if (file4.isFile()) {
                return file4;
            }
            File file5 = new File(file2, "pinyin_dict.tsv");
            if (file5.isFile()) {
                return file5;
            }
            File file6 = new File(file2, "pinyin_dict.txt");
            if (file6.isFile()) {
                return file6;
            }
            File[] fileArray = file2.listFiles((file, string) -> {
                if (string == null) return false;
                if (!string.toLowerCase(Locale.ROOT).endsWith(".dict.yaml")) return false;
                return true;
            });
            if (fileArray != null && fileArray.length > 0) {
                File file7 = fileArray[0];
                for (File file8 : fileArray) {
                    if (file8 == null || !file8.isFile() || file8.lastModified() <= file7.lastModified()) continue;
                    file7 = file8;
                }
                return file7;
            }
            return null;
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private static void a(File file, Map<String, LinkedHashSet<String>> map) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(file), StandardCharsets.UTF_8));){
            PinyinDictionary.a(bufferedReader, map);
        }
        catch (Throwable throwable) {

        }
    }

    private static void a(BufferedReader bufferedReader, Map<String, LinkedHashSet<String>> map) {
        try {
            String string2;
            while ((string2 = bufferedReader.readLine()) != null) {
                LinkedHashSet linkedHashSet;
                String string3;
                String[] stringArray;
                String string4 = string2.trim();
                if (string4.isEmpty() || string4.startsWith("#") || string4.indexOf(9) <= 0 || (stringArray = string4.split("\t")).length < 2) continue;
                String string5 = stringArray[0].trim();
                String string6 = stringArray[1].trim();
                if (string5.isEmpty() || string6.isEmpty() || (string3 = PinyinDictionary.bZ(string6)).isEmpty() || (linkedHashSet = map.computeIfAbsent(string3, string -> new LinkedHashSet())).size() >= 64) continue;
                linkedHashSet.add(string5);
            }
        } catch (java.io.IOException ioexception) {
        }
    }

    private static String bZ(String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }
        return string.trim().toLowerCase(Locale.ROOT).replace(" ", "").replace("'", "").replace("u:", "v").replace("\u00fc", "v");
    }

    private static void b(File file, Map<String, LinkedHashSet<String>> map) {
        try {
            if (file == null || map == null || map.isEmpty()) {
                return;
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file);){
                fileOutputStream.write("# Auto-generated cache. Source: user IME dict file\n# Do not edit; edit your *.dict.yaml / pinyin_dict.tsv instead.\n".getBytes(StandardCharsets.UTF_8));
                for (Map.Entry<String, LinkedHashSet<String>> entry : map.entrySet()) {
                    String string = entry.getKey();
                    LinkedHashSet<String> linkedHashSet = entry.getValue();
                    if (string == null || string.isEmpty() || linkedHashSet == null || linkedHashSet.isEmpty()) continue;
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean bl = true;
                    for (String string2 : linkedHashSet) {
                        if (string2 == null || string2.isEmpty()) continue;
                        if (!bl) {
                            stringBuilder.append(',');
                        }
                        stringBuilder.append(string2.replace(",", "\uff0c"));
                        bl = false;
                    }
                    if (bl) continue;
                    fileOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
                    fileOutputStream.write(61);
                    fileOutputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
                    fileOutputStream.write(10);
                }
            }
        }
        catch (Throwable throwable) {

        }
    }
}
