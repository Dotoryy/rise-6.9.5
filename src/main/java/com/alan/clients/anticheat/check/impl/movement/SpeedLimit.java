package com.alan.clients.anticheat.check.impl.movement;

import com.alan.clients.anticheat.check.Check;
import com.alan.clients.anticheat.check.api.CheckInfo;
import com.alan.clients.anticheat.data.PlayerData;
import com.alan.clients.anticheat.util.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.z;
import rip.vantage.commons.util.time.StopWatch;

@CheckInfo(R = "Speed", S = "Limit", description = "Detects speeds")
public final class SpeedLimit extends Check {
    private double ah;
    private double ai;
    private StopWatch aj = new StopWatch();

    public SpeedLimit(PlayerData playerData) {
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

            double d0 = 0.356;
            double d1 = Math.abs(this.data.aw() - this.data.ay());
            if (!this.data.aB()) {
                d0 *= 0.7;
            } else if (this.data.aI() <= 2) {
                d0 *= 0.75;
            }

            if (d1 < 180.0 && d1 > 20.0 && !this.data.isOnGround()) {
                d0 *= 1.0 - d1 / 300.0;
            }

            if (this.data.getPlayer().isBlocking()) {
                d0 *= 0.7;
            }

            this.ah = this.ah + this.data.ao();
            this.ai = this.ai + (float)this.aj.getElapsedTime() / 50.0F * d0;
            this.ai = Math.min(this.ai, this.ah + 2.0);
            if (this.data.aj() == 0.65625 && this.data.am() == 0.75) {
                this.J();
            }

            if (this.ah > this.ai) {
                if (this.Q() > 6.0) {
                    this.J();
                }

                this.K();
                this.ai = this.ah;
            } else {
                this.decreaseBufferBy(0.2F);
            }

            this.aj.aX();
        }
    }
}
