package com.alan.clients.anticheat.check.impl.combat;

import com.alan.clients.anticheat.check.Check;
import com.alan.clients.anticheat.check.api.CheckInfo;
import com.alan.clients.anticheat.data.PlayerData;
import net.minecraft.network.Packet;

@CheckInfo(R = "Velocity", S = "Cancel", description = "Detects velocities")
public final class VelocityCancel extends Check {
    public VelocityCancel(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(Packet<?> packet) {
    }
}
