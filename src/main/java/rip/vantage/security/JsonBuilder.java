package rip.vantage.security;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Map;

public class JsonBuilder {
    public Map<String, Object> eSp = new LinkedHashMap<>();

    public void g(String var1, long var2) {
        this.eSp.put(var1, var2);
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        int k_hi = 1;
        Iterator iterator = this.eSp.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            if (k_hi == 0) {
                stringbuilder.append(",");
            }

            k_hi = 0;
            stringbuilder.append("\"").append((String)entry.getKey()).append("\":");
            Object object3 = entry.getValue();
            if (object3 instanceof JsonBuilder) {
                stringbuilder.append(object3.toString());
            } else if (object3 instanceof String) {
                stringbuilder.append("\"").append(h((String)object3)).append("\"");
            } else if (object3 instanceof Number) {
                stringbuilder.append(object3.toString());
            } else {
                stringbuilder.append("null");
            }
        }

        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    public static String h(String var0) {
        return var0 == null ? "" : var0.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }


    public void U(String var1, int var2) {
        this.eSp.put(var1, var2);
    }

    static {
    }

    public void a(String var1, JsonBuilder var2) {
        this.eSp.put(var1, var2);
    }

    JsonBuilder() {
    }

    public void aK(String var1, String var2) {
        this.eSp.put(var1, var2);
    }
}
