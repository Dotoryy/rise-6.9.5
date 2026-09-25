package com.alan.clients.anticheat.check.manager;

import com.alan.clients.anticheat.check.Check;
import com.alan.clients.anticheat.check.impl.combat.AutoBlockA;
import com.alan.clients.anticheat.check.impl.combat.AutoBlockB;
import com.alan.clients.anticheat.check.impl.combat.VelocityCancel;
import com.alan.clients.anticheat.check.impl.movement.FlightPrediction;
import com.alan.clients.anticheat.check.impl.movement.SpeedLimit;
import com.alan.clients.anticheat.check.impl.movement.TowerWatchdog;
import com.alan.clients.anticheat.data.PlayerData;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public final class CheckManager {
    public static final Class<?>[] CHECKS = new Class[]{
        SpeedLimit.class, FlightPrediction.class, AutoBlockA.class, AutoBlockB.class, VelocityCancel.class, TowerWatchdog.class
    };
    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public CheckManager() {
    }

    public static List<Check> loadChecks(PlayerData playerData) {
        ArrayList arraylist = new ArrayList();

        for (Constructor constructor : CONSTRUCTORS) {
            try {
                arraylist.add((Check)constructor.newInstance(playerData));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return arraylist;
    }

    public static void setup() {
        for (Class oclass : CHECKS) {
            try {
                CONSTRUCTORS.add(oclass.getConstructor(PlayerData.class));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
