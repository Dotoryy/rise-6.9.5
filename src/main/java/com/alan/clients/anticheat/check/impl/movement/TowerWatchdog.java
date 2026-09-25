package com.alan.clients.anticheat.check.impl.movement;

import com.alan.clients.anticheat.check.Check;
import com.alan.clients.anticheat.check.api.CheckInfo;
import com.alan.clients.anticheat.data.PlayerData;
import com.alan.clients.anticheat.util.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.z;

@CheckInfo(R = "Tower", S = "Watchdog", description = "Impossible Motion")
public final class TowerWatchdog extends Check {
    public TowerWatchdog(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(Packet<?> packet) {
        if (PacketUtil.isRelMove(packet) && ((S14PacketEntity)packet).entityId == this.data.getPlayer().getEntityId() || packet instanceof z && ((z)packet).cqK == this.data.getPlayer().getEntityId()) {
            if (this.data.getPlayer().isInvisible()) {
                return;
            }

            if (this.data.ae() <= 10 + this.data.aH()) {
                return;
            }

            if (this.data.aj() == 0.65625 && this.data.am() == 0.75) {
                this.J();
            }
        }
    }
}
