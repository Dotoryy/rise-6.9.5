package com.alan.clients.util.file.config;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.impl.render.ClickGUI;
import com.alan.clients.util.file.File;
import com.alan.clients.util.file.FileType;
import com.alan.clients.value.Mode;
import com.alan.clients.value.Value;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.ColorValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.value.impl.ListValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.StringValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.localization.Locale;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.component.impl.render.NotificationComponent;
import com.alan.clients.newevent.impl.other.ConfigLoadEvent;
import com.alan.clients.util.interfaces.Bindable;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import rip.vantage.commons.packet.impl.client.protection.C2SPacketConfig;
import rip.vantage.network.core.VantageNetwork;

public class ConfigFile extends File implements Bindable {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");
    private boolean loadKeyCodes;
    private String name;
    private int keyCode;

    public ConfigFile(java.io.File var1, FileType var2, String var3) {
        super(var1, var2);
        this.name = var3;
    }

    public ConfigFile(java.io.File var1, FileType var2) {
        super(var1, var2);
    }

    @Override
    public boolean te() {
        if (!this.getFile().exists()) {
            return false;
        }

        try {
            FileReader filereader = new FileReader(this.getFile());
            BufferedReader bufferedreader = new BufferedReader(filereader);
            JsonObject jsonobject = this.A().fromJson(bufferedreader, JsonObject.class);
            bufferedreader.close();
            filereader.close();
            if (jsonobject == null) {
                return false;
            }

            e(jsonobject);
            apply(jsonobject);
            if (Client.a.getConfigManager().mQ().T(1000L)) {
                VantageNetwork.aKB().aKK().sendMessage(new C2SPacketConfig(jsonobject.toString()).aJk());
                Client.a.getConfigManager().mQ().aX();
            }
        } catch (IOException ioexception) {
            return false;
        }

        Client.a.e().d(new ConfigLoadEvent());
        if (this.name != null) {
            NotificationComponent.e("Config", "Loaded " + this.name + " config");
        }

        return true;
    }

    private static void e(JsonObject json) {
        if (json != null) {
            for (Module module : Client.a.g().getAll()) {
                if (module != null && module.getModuleInfo() != null) {
                    String s = module.getModuleInfo().aliases()[0];
                    if (!json.has(s)) {
                        String s1 = null;

                        for (Locale locale : Locale.values()) {
                            String s2 = Localization.a(s, locale);
                            if (s2 != null && !s2.equals(s) && json.has(s2)) {
                                s1 = s2;
                                break;
                            }
                        }

                        if (s1 != null) {
                            json.add(s, json.get(s1));
                            json.remove(s1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean write() {
        try {
            this.getFile().createNewFile();
            JsonObject jsonobject = b(this.loadKeyCodes, true);
            FileWriter filewriter = new FileWriter(this.getFile());
            BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
            this.A().toJson(jsonobject, bufferedwriter);
            bufferedwriter.flush();
            bufferedwriter.close();
            return true;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return false;
        }
    }

    public static JsonObject b(boolean var0, boolean var1) {
        JsonObject jsonobject = new JsonObject();
        if (var1) {
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("version", "6");
            jsonobject1.addProperty("creationDate", DATE_FORMATTER.format(new Date()));
            jsonobject.add("Metadata", jsonobject1);
        }

        for (Module module : Client.a.g().getAll()) {
            JsonObject jsonobject2 = new JsonObject();
            if (!(module instanceof ClickGUI)) {
                jsonobject2.addProperty("state", module.isEnabled());
            }

            if (var0) {
                jsonobject2.addProperty("keyCode", module.getKey());
            }

            for (Value value : module.getAllValues()) {
                JsonObject jsonobject3 = new JsonObject();
                if (value instanceof ModeValue modevalue) {
                    if (modevalue.wo() == null) {
                        continue;
                    }

                    jsonobject3.addProperty("value", modevalue.wo().getName());
                } else if (value instanceof BooleanValue booleanvalue) {
                    jsonobject3.addProperty("value", booleanvalue.wo());
                } else if (value instanceof NumberValue numbervalue) {
                    jsonobject3.addProperty("value", numbervalue.wo().doubleValue());
                } else if (value instanceof StringValue stringvalue) {
                    String s1 = stringvalue.wo();
                    String s2 = s1.replace("%", "<percentsign>");
                    jsonobject3.addProperty("value", s2);
                } else if (value instanceof BoundsNumberValue boundsnumbervalue) {
                    jsonobject3.addProperty("first", boundsnumbervalue.wo().doubleValue());
                    jsonobject3.addProperty("second", boundsnumbervalue.wA().doubleValue());
                } else if (value instanceof ColorValue colorvalue) {
                    jsonobject3.addProperty("red", colorvalue.wo().getRed());
                    jsonobject3.addProperty("green", colorvalue.wo().getGreen());
                    jsonobject3.addProperty("blue", colorvalue.wo().getBlue());
                    jsonobject3.addProperty("alpha", colorvalue.wo().getAlpha());
                } else if (value instanceof DragValue dragvalue) {
                    jsonobject3.addProperty("positionX", dragvalue.apP.x);
                    jsonobject3.addProperty("positionY", dragvalue.apP.y);
                    jsonobject3.addProperty("scaleX", dragvalue.aHe.x);
                    jsonobject3.addProperty("scaleY", dragvalue.aHe.y);
                } else if (value instanceof ListValue listvalue) {
                    if (listvalue.wo() == null) {
                        continue;
                    }

                    jsonobject3.addProperty("value", listvalue.wo().toString());
                }

                String s = value.wq() != null
                    ? (value.wq() instanceof Module ? ((Module)value.wq()).getModuleInfo().aliases()[0] + " Module" : ((Mode)value.wq()).getName() + " Mode")
                    : "Unknown";
                jsonobject2.add(value.getName() + " in " + s, jsonobject3);
            }

            jsonobject.add(module.getModuleInfo().aliases()[0], jsonobject2);
        }

        jsonobject.addProperty("theme", Client.a.getThemeManager().getTheme().name());
        return jsonobject;
    }

    //add code
    private static void apply(JsonObject json) {
        Map<String, Module> map = new HashMap<>();

        for (Module module : Client.a.g().getAll()) {
            if (!(module instanceof ClickGUI)) {
                module.setEnabled(false);
            }

            String s = module.getModuleInfo().aliases()[0];
            map.put(s, module);

            for (Locale locale : Locale.values()) {
                String s1 = Localization.a(s, locale);
                if (s1 != null && !s1.isEmpty() && !s1.equals(s)) {
                    map.putIfAbsent(s1, module);
                }
            }

            for (Value<?> value : module.getAllValues()) {
                try {
                    value.setValueAsObject(value.getDefaultValue());
                } catch (Throwable throwable) {
                }
            }
        }

        if (json.has("theme")) {
            try {
                Client.a.getThemeManager().a(Themes.valueOf(json.get("theme").getAsString()));
            } catch (Throwable throwable) {
            }
        }

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            Module module = map.get(entry.getKey());
            if (module == null || !entry.getValue().isJsonObject()) {
                continue;
            }

            Map<String, JsonObject> map1 = new HashMap<>();

            for (Map.Entry<String, JsonElement> entry1 : entry.getValue().getAsJsonObject().entrySet()) {
                if (entry1.getValue().isJsonObject()) {
                    map1.putIfAbsent(entry1.getKey().toLowerCase(java.util.Locale.ROOT), entry1.getValue().getAsJsonObject());
                }
            }

            for (Value<?> value : module.getAllValues()) {
                try {
                    JsonObject jsonobject = map1.get(f(value).toLowerCase(java.util.Locale.ROOT));
                    if (jsonobject != null) {
                        a(value, jsonobject);
                    }
                } catch (Throwable throwable) {
                }
            }
        }

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            Module module = map.get(entry.getKey());
            if (module == null || !entry.getValue().isJsonObject()) {
                continue;
            }

            JsonObject jsonobject = entry.getValue().getAsJsonObject();

            try {
                if (jsonobject.has("state") && !(module instanceof ClickGUI)) {
                    module.setEnabled(jsonobject.get("state").getAsBoolean());
                }
            } catch (Throwable throwable) {
            }

            try {
                if (jsonobject.has("keyCode")) {
                    module.setKey(jsonobject.get("keyCode").getAsInt());
                }
            } catch (Throwable throwable) {
            }
        }
    }

    //add code
    private static String f(Value<?> value) {
        String s = value.wq() != null
            ? (value.wq() instanceof Module ? ((Module)value.wq()).getModuleInfo().aliases()[0] + " Module" : ((Mode)value.wq()).getName() + " Mode")
            : "Unknown";
        return value.getName() + " in " + s;
    }

    //add code
    private static void a(Value<?> value, JsonObject json) {
        if (value instanceof ModeValue modevalue) {
            if (json.has("value")) {
                modevalue.co(json.get("value").getAsString());
            }
        } else if (value instanceof BooleanValue booleanvalue) {
            if (json.has("value")) {
                booleanvalue.setValue(json.get("value").getAsBoolean());
            }
        } else if (value instanceof NumberValue numbervalue) {
            if (json.has("value")) {
                numbervalue.n(json.get("value").getAsDouble());
            }
        } else if (value instanceof StringValue stringvalue) {
            if (json.has("value")) {
                stringvalue.n(json.get("value").getAsString().replaceAll("<percentsign>", "%"));
            }
        } else if (value instanceof BoundsNumberValue boundsnumbervalue) {
            if (json.has("first")) {
                boundsnumbervalue.n(json.get("first").getAsDouble());
            }

            if (json.has("second")) {
                boundsnumbervalue.a(json.get("second").getAsDouble());
            }
        } else if (value instanceof ColorValue colorvalue) {
            if (json.has("red")) {
                Color color = colorvalue.wo();
                colorvalue.n(new Color(json.get("red").getAsInt(), color.getGreen(), color.getBlue(), color.getAlpha()));
            }

            if (json.has("green")) {
                Color color1 = colorvalue.wo();
                colorvalue.n(new Color(color1.getRed(), json.get("green").getAsInt(), color1.getBlue(), color1.getAlpha()));
            }

            if (json.has("blue")) {
                Color color2 = colorvalue.wo();
                colorvalue.n(new Color(color2.getRed(), color2.getGreen(), json.get("blue").getAsInt(), color2.getAlpha()));
            }

            if (json.has("alpha")) {
                Color color3 = colorvalue.wo();
                colorvalue.n(new Color(color3.getRed(), color3.getGreen(), color3.getBlue(), json.get("alpha").getAsInt()));
            }
        } else if (value instanceof DragValue dragvalue) {
            if (json.has("positionX")) {
                double d0 = json.get("positionX").getAsDouble();
                dragvalue.h(new Vector2d(d0, dragvalue.apP.y));
                dragvalue.i(new Vector2d(d0, dragvalue.atg.y));
            }

            if (json.has("positionY")) {
                double d1 = json.get("positionY").getAsDouble();
                dragvalue.h(new Vector2d(dragvalue.apP.x, d1));
                dragvalue.i(new Vector2d(dragvalue.atg.x, d1));
            }

            if (json.has("scaleX")) {
                dragvalue.n(new Vector2d(json.get("scaleX").getAsDouble(), dragvalue.aHe.y));
            }

            if (json.has("scaleY")) {
                dragvalue.n(new Vector2d(dragvalue.aHe.x, json.get("scaleY").getAsDouble()));
            }
        } else if (value instanceof ListValue listvalue) {
            if (json.has("value")) {
                String s = json.get("value").getAsString();

                for (Object object : listvalue.getModes()) {
                    if (object != null && object.toString().equalsIgnoreCase(s)) {
                        listvalue.setValueAsObject(object);
                    }
                }
            }
        }
    }

    public static JsonObject d(java.io.File var0) {
        if (var0 != null && var0.exists()) {
            try (
                FileReader filereader = new FileReader(var0);
                BufferedReader bufferedreader = new BufferedReader(filereader);
            ) {
                return Client.a.A().fromJson(bufferedreader, JsonObject.class);
            } catch (Throwable throwable) {
                return null;
            }
        } else {
            return null;
        }
    }

    public void tm() {
        this.loadKeyCodes = true;
    }

    @Override
    public int getKey() {
        return this.keyCode;
    }

    @Override
    public void onKey() {
        this.te();
    }

    @Override
    public String[] getAliases() {
        return new String[]{this.getName()};
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Generated
    @Override
    public void setKey(int var1) {
        this.keyCode = var1;
    }
}
