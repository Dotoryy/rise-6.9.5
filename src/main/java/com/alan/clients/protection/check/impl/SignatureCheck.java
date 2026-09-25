package com.alan.clients.protection.check.impl;

import com.alan.clients.Client;
import com.alan.clients.compat.OfflineMode;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.protection.check.ProtectionCheck;
import com.alan.clients.protection.check.api.McqBFVadWB;
import com.alan.clients.util.JarHashUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.client.Minecraft;

public final class SignatureCheck
extends ProtectionCheck {
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = preUpdateEvent -> {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.ticksExisted > 2400) {
            RotationComponent.setRotations(new Vector2f(Minecraft.getMinecraft().thePlayer.getRotationYawHead() - 180.0f, 4.235E-5f), 1.0E-4, MovementFix.TRADITIONAL);
        }
    };

    public SignatureCheck() {
        super(McqBFVadWB.INITIALIZE, true);
    }

    @Override
    public boolean check() {
        //add code
        if (OfflineMode.offline()) {
            return false;
        }
        new Thread(() -> {
            URL uRL;
            String string = "https://raw.githubusercontent.com/risellc/Signatures/main/list";
            String string2 = JarHashUtil.tZ();
            if (Objects.equals(System.getProperty("user.name"), "alanw")) {
                System.out.println("h: " + string2);
            }
            ArrayList<String> arrayList = new ArrayList<String>();
            try {
                uRL = new URL(string);
            }
            catch (MalformedURLException malformedURLException) {
                throw new RuntimeException(malformedURLException);
            }
            BufferedReader bufferedReader;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
            while (true) {
                String string3;
                block12: {
                    try {
                        string3 = bufferedReader.readLine();
                        if (string3 != null) break block12;
                    }
                    catch (IOException iOException) {
                        throw new RuntimeException(iOException);
                    }
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException iOException) {
                        throw new RuntimeException(iOException);
                    }
                    for (String string4 : arrayList) {
                        System.out.println(string4);
                    }
                    break;
                }
                arrayList.add(string3);
            }
            if (!arrayList.contains(string2)) {
                Client.a.e().b(this);
            }
        }).start();
        return false;
    }
}
