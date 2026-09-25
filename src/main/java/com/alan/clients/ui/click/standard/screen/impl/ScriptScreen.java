package com.alan.clients.ui.click.standard.screen.impl;

import com.alan.clients.Client;
import com.alan.clients.compat.OfflineMode;
import com.alan.clients.module.api.Category;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.ui.click.standard.RiseClickGUI;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.vector.Vector2f;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.alan.clients.ui.click.standard.components.ModuleComponent;
import com.alan.clients.ui.click.standard.UIColors;
import com.alan.clients.ui.click.standard.screen.Screen;
import com.alan.clients.ui.click.standard.components.ConfigCard;
import com.alan.clients.ui.click.standard.components.ConfigCardSection;
import com.alan.clients.util.NetworkUtil;
import com.alan.clients.util.gui.ScrollUtil;
import com.alan.clients.util.tuples.Triple;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.stream.Collectors;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import rip.vantage.commons.packet.impl.client.community.C2SPacketConfigRequest;
import rip.vantage.commons.packet.impl.client.protection.C2SPacketConfig;
import rip.vantage.network.core.VantageNetwork;

public final class ScriptScreen
implements Screen,
InstanceAccess {
    public ScrollUtil scrollUtil = new ScrollUtil();
    public static int auk = 10;
    public static boolean azE;
    private ConfigCardSection azF = new ConfigCardSection(Arrays.asList(new ConfigCard("", "Loading"), new ConfigCard("", "Loading"), new ConfigCard("", "Loading")), "Featured Configs");
    private ConfigCardSection azG = new ConfigCardSection(Arrays.asList(new ConfigCard("", "Loading"), new ConfigCard("", "Loading"), new ConfigCard("", "Loading")), "Community Configs");
    private ConfigCardSection azH = new ConfigCardSection(Arrays.asList(new ConfigCard("", ""), new ConfigCard("", ""), new ConfigCard("", "")), "Your Configs");
    private ArrayList<ModuleComponent> azI = new ArrayList();
    public ConfigCardSection[] azJ = new ConfigCardSection[]{this.azF, this.azH};
    private boolean azK;
    private Animation animation = new Animation(Easing.EASE_OUT_EXPO, 400L);
    @EventLink
    public final Listener<BackendS2CEvent> azL = er2 -> {
        if (er2.dd() instanceof rip.vantage.commons.packet.impl.server.community.S2CPacketConfigList) {
            JSONArray jSONArray = ((rip.vantage.commons.packet.impl.server.community.S2CPacketConfigList)er2.dd()).getConfigs();
            this.azG.clear();
            for (int i2 = 0; i2 < jSONArray.length(); ++i2) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                String string = StringUtils.capitalize(jSONObject.getString("a"));
                jSONObject.getString("b");
                String string2 = jSONObject.getString("c");
                this.azG.add(new ConfigCard("Click to load", string, () -> VantageNetwork.aKB().aKK().sendMessage(new C2SPacketConfigRequest(string2).aJk())));
            }
        }
    };

    @Override
    public void onRender(int n2, int n3, float f2) {
        if (!this.azK) {
            Client.a.e().b(this);
            this.azK = true;
        }
        RiseClickGUI riseClickGUI = this.getStandardClickGUI();
        Vector2f vector2f = new Vector2f(this.getStandardClickGUI().getScale().x, this.getStandardClickGUI().getScale().y);
        Vector2f vector2f2 = new Vector2f(this.getStandardClickGUI().getPosition().x, this.getStandardClickGUI().getPosition().y);
        double d2 = riseClickGUI.axS - 0.99;
        azE = true;
        for (ConfigCardSection acj2 : this.azJ) {
            acj2.qF();
        }
        this.scrollUtil.E(azE);
        vector2f.x = (float)((double)vector2f.x + (riseClickGUI.sidebar.aym + (double)10));
        vector2f.y = (float)((double)vector2f.y + (this.scrollUtil.tE() + (double)10));
        vector2f2.x = (float)((double)vector2f2.x + ((double)-20 - riseClickGUI.sidebar.aym));
        Vector2f vector2f3 = new Vector2f(vector2f2.x, 110.0f);
        RenderUtil.roundedRectangle(vector2f.x, vector2f.y, vector2f3.x, vector2f3.y, 10.0, UIColors.OVERLAY.Y(UIColors.OVERLAY.pV().getAlpha() * 2));
        vector2f.y += vector2f3.y + (float)20;
        for (ConfigCardSection acj3 : this.azJ) {
            try {
                acj3.j(vector2f);
                vector2f.y += (float)(20 + acj3.getHeight());
            }
            catch (ConcurrentModificationException concurrentModificationException) {}
        }
        FontManager.MAIN.a(18, FontWeight.REGULAR).a("Your Scripts", vector2f.x, vector2f.y, Color.WHITE.getRGB());
        vector2f.y += (float)10 + FontManager.MAIN.a(18, FontWeight.REGULAR).height();
        Iterator<ModuleComponent> iterator = this.azI.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                double d3 = 7.0;
                double d4 = riseClickGUI.getScale().getX() + riseClickGUI.getPosition().getX() - 4.0f;
                double d5 = (double)riseClickGUI.getScale().getY() + d3;
                this.scrollUtil.a(new Vector2d(d4, d5), (double)this.getStandardClickGUI().position.y - d3 * 2.0);
                this.scrollUtil.V(-((double)vector2f.y - this.scrollUtil.tE() - (double)riseClickGUI.axI.y) + (double)riseClickGUI.position.y - 7.0);
                double d6 = riseClickGUI.axS - 0.99;
                return;
            }
            ModuleComponent moduleComponent = iterator.next();
            moduleComponent.draw(new Vector2d((double)riseClickGUI.axI.x + riseClickGUI.sidebar.aym + 8.0, vector2f.y), n2, n3, f2);
            vector2f.y += moduleComponent.scale.y + 7.0f;
        }
    }

    @Override
    public void onKey(char c2, int n2) {
        Iterator<ModuleComponent> iterator = this.azI.iterator();
        while (iterator.hasNext()) {
            iterator.next().key(c2, n2);
        }
    }

    @Override
    public void f(int n2, int n3, int n4) {
        for (ConfigCardSection acj2 : this.azJ) {
            try {
                acj2.f(n2, n3, n4);
            }
            catch (ConcurrentModificationException concurrentModificationException) {
                concurrentModificationException.printStackTrace();
            }
        }
        Iterator<ModuleComponent> iterator = this.azI.iterator();
        while (iterator.hasNext()) {
            ModuleComponent moduleComponent = iterator.next();
            moduleComponent.click(n2, n3, n4);
        }
        return;
    }

    @Override
    public void oG() {
        Iterator<ModuleComponent> iterator = this.azI.iterator();
        while (iterator.hasNext()) {
            iterator.next().pz();
        }
    }

    @Override
    public void pY() {
        Iterator<ModuleComponent> iterator = this.azI.iterator();
        while (iterator.hasNext()) {
            iterator.next().ci();
        }
    }

    @Override
    public void aT() {
        if (!this.azK) {
            Client.a.e().b(this);
            this.azK = true;
        }
        ConfigCardSection[] acjArray = this.azJ;
        int length = acjArray.length;
        for (int i2 = 0; i2 < length; ++i2) {
            acjArray[i2].init();
        }
        Client.a.getConfigManager().update();
        this.azH.clear();
        Client.a.getConfigManager().forEach(configFile -> this.azH.add(new ConfigCard("Click to load", configFile.getName(), configFile::te)));
        this.azI = Client.a.getStandardClickGUI().getModuleList().stream().filter(abd2 -> {
            if (abd2.getModule().getModuleInfo().category() != Category.SCRIPT) return false;
            return true;
        }).collect(Collectors.toCollection(ArrayList::new));
        VantageNetwork.aKB().aKK().sendMessage(new rip.vantage.commons.packet.impl.client.community.C2SPacketConfigListRequest().aJk());
        new Thread(() -> {
            ArrayList<Triple<String, String, String>> arrayList = this.qi();
            this.azF.clear();
            arrayList.forEach(ajt2 -> this.azF.add(new ConfigCard("Click to load", (String)ajt2.getFirst(), () -> VantageNetwork.aKB().aKK().sendMessage(new C2SPacketConfig(NetworkUtil.aY("https://raw.githubusercontent.com/risellc/RiseOnlineConfigs/main/" + ((String)ajt2.getFirst()).toLowerCase() + ".json")).aJk()))));
        }).start();
    }

    public ArrayList<Triple<String, String, String>> qi() {
        //add code
        if (OfflineMode.offline()) {
            return new ArrayList<Triple<String, String, String>>();
        }
        JsonArray jsonArray = NetworkUtil.aZ("https://raw.githubusercontent.com/risellc/RiseOnlineConfigs/main/index.json");
        ArrayList<Triple<String, String, String>> arrayList = new ArrayList<Triple<String, String, String>>();
        Iterator<JsonElement> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonObject jsonObject = iterator.next().getAsJsonObject();
            String string = jsonObject.get("name").getAsString();
            String string2 = jsonObject.get("ip").getAsString();
            String string3 = jsonObject.get("last updated").getAsString();
            arrayList.add(new Triple<String, String, String>(string, string2, string3));
        }
        return arrayList;
    }

    @Generated
    public ScrollUtil getScrollUtil() {
        return this.scrollUtil;
    }

    @Generated
    public ConfigCardSection qj() {
        return this.azF;
    }

    @Generated
    public ConfigCardSection qk() {
        return this.azG;
    }

    @Generated
    public ConfigCardSection ql() {
        return this.azH;
    }

    @Generated
    public ArrayList<ModuleComponent> qm() {
        return this.azI;
    }

    @Generated
    public ConfigCardSection[] qn() {
        return this.azJ;
    }

    @Generated
    public boolean qo() {
        return this.azK;
    }

    @Generated
    public Animation getAnimation() {
        return this.animation;
    }

    @Generated
    public Listener<BackendS2CEvent> qp() {
        return this.azL;
    }

    @Generated
    public void a(ScrollUtil scrollUtil) {
        this.scrollUtil = scrollUtil;
    }

    @Generated
    public void a(ConfigCardSection acj2) {
        this.azF = acj2;
    }

    @Generated
    public void b(ConfigCardSection acj2) {
        this.azG = acj2;
    }

    @Generated
    public void c(ConfigCardSection acj2) {
        this.azH = acj2;
    }

    @Generated
    public void c(ArrayList<ModuleComponent> arrayList) {
        this.azI = arrayList;
    }

    @Generated
    public void a(ConfigCardSection[] acjArray) {
        this.azJ = acjArray;
    }

    @Generated
    public void z(boolean bl) {
        this.azK = bl;
    }

    @Generated
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
