package com.alan.clients.keybind;

import com.alan.clients.Client;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.KeyboardInputEvent;
import com.alan.clients.newevent.impl.input.MouseButtonEvent;
import com.alan.clients.util.interfaces.Bindable;
import com.alan.clients.util.ime.PinyinImeState;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.component.impl.render.NotificationComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class KeybindManager {
    @EventLink(value = 0)
    public final Listener<KeyboardInputEvent> onKeyboardInput = var1 -> {
        if (!var1.isCancelled()) {
            if (var1.getKeyCode() == 25 && GuiScreen.isCtrlKeyDown() && GuiScreen.isShiftKeyDown()) {
                if (Keyboard.isRepeatEvent()) {
                    var1.setCancelled();
                } else {
                    boolean flag = PinyinImeState.uB();
                    if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) {
                        NotificationComponent.a(Localization.ce("module.render.chat.pinyin_ime"), Localization.ce(flag ? "ui.chat.pinyin_ime.hint.on" : "ui.chat.pinyin_ime.hint.off"), 1500);
                    }

                    var1.setCancelled();
                }
            } else if (var1.getGuiScreen() == null) {
                this.aP().stream().filter(var1x -> var1x.getKey() == var1.getKeyCode()).forEach(Bindable::onKey);
            }
        }
    };
    @EventLink(value = 0)
    public final Listener<MouseButtonEvent> onMouseButton = var1 -> {
        if (Minecraft.getMinecraft().currentScreen == null) {
            int i = var1.cW() - 100;
            this.aP().stream().filter(var1x -> var1x.getKey() == i).forEach(Bindable::onKey);
        }
    };

    public KeybindManager() {
    }

    public void init() {
        Client.a.e().b(this);
    }

    public List<Bindable> aP() {
        ArrayList arraylist = new ArrayList();
        arraylist.addAll(Client.a.g().getAll());
        arraylist.addAll(Client.a.getConfigManager());
        return arraylist;
    }

    public <T extends Bindable> T a(String var1) {
        return (T)this.aP()
            .stream()
            .filter(var1x -> Arrays.stream(var1x.getAliases()).anyMatch(var1xx -> var1xx.replace(" ", "").equalsIgnoreCase(var1.replace(" ", ""))))
            .findAny()
            .orElse(null);
    }
}
