package com.alan.clients.keybind;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class InputKeys {
    public static final int aKg = 18;
    public static final int aKh = 0;
    public static final int aKi = 0;
    public static final int aKj = 1;
    public static final int aKk = 2;
    public static final int aKl = 3;
    public static final int aKm = 4;
    public static final int aKn = 5;
    public static final int aKo = 6;
    public static final int aKp = 7;
    public static final int aKq = 8;
    public static final int aKr = 9;
    public static final int aKs = 10;
    public static final int aKt = 11;
    public static final int aKu = 12;
    public static final int aKv = 13;
    public static final int aKw = 14;
    public static final int aKx = 15;
    public static final int aKy = 16;
    public static final int aKz = 17;
    public static final int aKA = 18;
    public static final int aKB = 19;
    public static final int aKC = 20;
    public static final int aKD = 21;
    public static final int aKE = 22;
    public static final int aKF = 23;
    public static final int aKG = 24;
    public static final int aKH = 25;
    public static final int aKI = 26;
    public static final int aKJ = 27;
    public static final int aKK = 28;
    public static final int aKL = 29;
    public static final int aKM = 30;
    public static final int aKN = 31;
    public static final int aKO = 32;
    public static final int aKP = 33;
    public static final int aKQ = 34;
    public static final int aKR = 35;
    public static final int aKS = 36;
    public static final int aKT = 37;
    public static final int aKU = 38;
    public static final int aKV = 39;
    public static final int aKW = 40;
    public static final int aKX = 41;
    public static final int aKY = 42;
    public static final int aKZ = 43;
    public static final int aLa = 44;
    public static final int aLb = 45;
    public static final int aLc = 46;
    public static final int aLd = 47;
    public static final int aLe = 48;
    public static final int aLf = 49;
    public static final int aLg = 50;
    public static final int aLh = 51;
    public static final int aLi = 52;
    public static final int aLj = 53;
    public static final int aLk = 54;
    public static final int aLl = 55;
    public static final int aLm = 56;
    public static final int aLn = 57;
    public static final int aLo = 58;
    public static final int aLp = 59;
    public static final int aLq = 60;
    public static final int aLr = 61;
    public static final int aLs = 62;
    public static final int aLt = 63;
    public static final int aLu = 64;
    public static final int aLv = 65;
    public static final int aLw = 66;
    public static final int aLx = 67;
    public static final int aLy = 68;
    public static final int aLz = 69;
    public static final int aLA = 70;
    public static final int aLB = 71;
    public static final int aLC = 72;
    public static final int aLD = 73;
    public static final int aLE = 74;
    public static final int aLF = 75;
    public static final int aLG = 76;
    public static final int aLH = 77;
    public static final int aLI = 78;
    public static final int aLJ = 79;
    public static final int aLK = 80;
    public static final int aLL = 81;
    public static final int aLM = 82;
    public static final int aLN = 83;
    public static final int aLO = 87;
    public static final int aLP = 88;
    public static final int aLQ = 100;
    public static final int aLR = 101;
    public static final int aLS = 102;
    public static final int aLT = 103;
    public static final int aLU = 104;
    public static final int aLV = 105;
    public static final int aLW = 112;
    public static final int aLX = 113;
    public static final int aLY = 121;
    public static final int aLZ = 123;
    public static final int aMa = 125;
    public static final int aMb = 141;
    public static final int aMc = 144;
    public static final int aMd = 145;
    public static final int aMe = 146;
    public static final int aMf = 147;
    public static final int aMg = 148;
    public static final int aMh = 149;
    public static final int aMi = 150;
    public static final int aMj = 151;
    public static final int aMk = 156;
    public static final int aMl = 157;
    public static final int aMm = 167;
    public static final int aMn = 179;
    public static final int aMo = 181;
    public static final int aMp = 183;
    public static final int aMq = 184;
    public static final int aMr = 196;
    public static final int aMs = 197;
    public static final int aMt = 199;
    public static final int aMu = 200;
    public static final int aMv = 201;
    public static final int aMw = 203;
    public static final int aMx = 205;
    public static final int aMy = 207;
    public static final int aMz = 208;
    public static final int aMA = 209;
    public static final int aMB = 210;
    public static final int aMC = 211;
    public static final int aMD = 218;
    public static final int aME = 219;
    public static final int aMF = 220;
    public static final int aMG = 221;
    public static final int aMH = 222;
    public static final int aMI = 223;
    public static final int aMJ = 224;
    public static final int aMK = 225;
    public static final int aML = 226;
    public static final int aMM = 227;
    public static final int aMN = 228;
    public static final int aMO = 229;
    private static final String[] keyName = new String[256];
    private static final Map<String, Integer> keyMap = new HashMap<>(253);

    public InputKeys() {
    }

    public static synchronized String getKeyName(int var0) {
        return keyName[var0];
    }

    public static synchronized int getKeyIndex(String var0) {
        Integer integer = keyMap.get(var0);
        return integer == null ? 0 : integer;
    }

    static {
        Field[] afield = InputKeys.class.getFields();

        try {
            Field[] afield1 = afield;
            int i = afield.length;

            for (int j = 0; j < i; j++) {
                Field field = afield1[j];
                if (Modifier.isStatic(field.getModifiers())
                    && Modifier.isPublic(field.getModifiers())
                    && Modifier.isFinal(field.getModifiers())
                    && field.getType().equals(int.class)
                    && field.getName().startsWith("INPUT_")
                    && !field.getName().endsWith("WIN")) {
                    int k = field.getInt(null);
                    String s = field.getName().substring(10);
                    keyName[k] = s;
                    keyMap.put(s, k);
                }
            }
        } catch (Exception exception) {
        }
    }
}
