package com.alan.clients.anticheat.check.impl.movement;

import com.alan.clients.anticheat.check.Check;
import com.alan.clients.anticheat.check.api.CheckInfo;
import com.alan.clients.anticheat.check.impl.combat.VelocityCancel;
import com.alan.clients.anticheat.data.PlayerData;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.type.EvictingList;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.anticheat.util.PacketUtil;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.z;
import net.minecraft.util.Vec3;
import rip.vantage.commons.util.time.StopWatch;

@CheckInfo(R = "Flight", S = "Prediction", description = "Detects flight")
public final class FlightPrediction extends Check {
    private EvictingList<Vec3> V = new EvictingList<>(144);
    private ArrayList<Double> possibleJumpMotions = new ArrayList<>();
    private double MINIMUM_OFFSET_TO_FLAG = 1.0E-5;
    private int Y = 60;
    private int Z = 10;
    private int aa = 5;
    private int ab;
    private StopWatch ac = new StopWatch();
    private int ad;
    private int ae;
    private boolean af;
    private boolean ag;

    public FlightPrediction(PlayerData playerData) {
        super(playerData);

        for (int i = 0; i < this.aa; i++) {
            double d0 = 0.42F;
            if (i != 0) {
                d0 += (i + 1) * 0.1F;
            }

            this.possibleJumpMotions.add(d0);
        }

        this.possibleJumpMotions.add(0.0);
    }

    @Override
    public void handle(Packet<?> packet) {
        if (PacketUtil.isRelMove(packet) && ((S14PacketEntity)packet).entityId == this.data.getPlayer().getEntityId() || packet instanceof z && ((z)packet).cqK == this.data.getPlayer().getEntityId()) {
            EntityOtherPlayerMP entityotherplayermp = this.data.getPlayer();
            if (!this.af && this.data.ae() <= 20) {
                this.af = true;
                this.ae = 0;
                this.ag = false;
                this.M();
            }

            if (entityotherplayermp.ticksExisted <= 100) {
                this.M();
            }

            this.ae++;
            if (entityotherplayermp.isInvisible()) {
                return;
            }

            if (this.data.aD() || this.data.aC() || this.data.isOnGround() || this.data.aA()) {
                this.V.removeIf(var1x -> Math.abs(this.data.getX() - var1x.xCoord) + Math.abs(this.data.getZ() - var1x.zCoord) > 10.0);
            }

            for (double d0 = 0.0; d0 >= -0.5; d0 -= 0.5) {
                for (int i = -3; i <= 3; i++) {
                    for (int j = -2; j <= 2; j++) {
                        for (int k = -3; k <= 3; k++) {
                            Block block = PlayerUtil.o(entityotherplayermp.posX + i, entityotherplayermp.posY + j, entityotherplayermp.posZ + k);
                            if (block.getMaterial() != Blocks.air.getMaterial()) {
                                double d1 = Math.floor(entityotherplayermp.posX + i);
                                double d2 = Math.floor(entityotherplayermp.posY + j) + block.getBlockBoundsMaxY() + d0;
                                double d3 = Math.floor(entityotherplayermp.posZ + k);
                                Vec3 vec3 = new Vec3(d1, d2, d3);
                                if (this.V.stream().noneMatch(var2 -> var2.yCoord == d2)) {
                                    this.V.add(vec3);
                                }
                            }
                        }
                    }
                }
            }

            if (this.V.size() <= 2) {
                return;
            }

            if (this.data.getY() < this.data.ar() - this.Z) {
                return;
            }

            double d4 = Double.MAX_VALUE;
            double d5 = Double.MAX_VALUE;
            int l = Integer.MAX_VALUE;

            for (Vec3 vec : this.V) {
                for (double d7 : this.possibleJumpMotions) {
                    double d8 = vec.yCoord;

                    for (int i1 = 0; i1 < this.Y; i1++) {
                        d8 += MoveUtil.predictedMotion(d7, i1);
                        double d9 = Math.abs(d8 - this.data.getY());
                        if (d9 < d4) {
                            d4 = d9;
                            l = i1;
                            double d10 = vec.yCoord;
                        }

                        if (d8 < d5) {
                            d5 = d8;
                        }
                    }
                }
            }

            if (this.data.getY() < d5) {
                return;
            }

            if (this.ac.T(250L) && this.data.aC()) {
                this.ac.aX();
                this.ad = 0;
            }

            if (this.af) {
                if (d4 > 0.001
                    && d4 != 0.015485600668696975
                    && d4 != 0.013749987F
                    && d4 != 0.002786007340908725
                    && d4 != 0.0013359791121487774
                    && d4 != 0.0013735326446351337
                    && d4 != 0.005455650520339361
                    && d4 != 0.026450877005913753
                    && d4 != 0.006179987693194278
                    && d4 != 0.0029422088919233147
                    && d4 != 0.0031678198715923145) {
                    this.ag = true;
                }

                if (this.data.aC() && this.data.isOnGround() && d4 <= this.MINIMUM_OFFSET_TO_FLAG && this.ae >= 30) {
                    this.af = false;
                    this.M();
                }

                if (this.ae == 25) {
                    VelocityCancel velocitycancel = (VelocityCancel)this.data.getCheck(VelocityCancel.class);
                    if (velocitycancel == null) {
                        return;
                    }

                    if (this.ag) {
                        velocitycancel.L();
                    } else {
                        velocitycancel.K();
                        if (velocitycancel.Q() > 1.0) {
                            velocitycancel.J();
                        }
                    }

                    this.ag = true;
                }
            } else {
                if (l <= this.ab && !this.data.aC() && l > 2 && this.ab > 2) {
                    this.ad++;
                    if (this.ad > 1) {
                        this.ad = 0;
                        this.increaseBufferBy(0.11);
                    }
                }

                if (d4 < this.MINIMUM_OFFSET_TO_FLAG) {
                    this.decreaseBufferBy(0.1F);
                }

                this.increaseBufferBy(d4);
                if (this.Q() > 0.3) {
                    this.J();
                    this.M();
                }
            }

            this.ab = l;
        }
    }
}
