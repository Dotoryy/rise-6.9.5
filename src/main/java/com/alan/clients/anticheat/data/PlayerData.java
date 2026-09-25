package com.alan.clients.anticheat.data;

import com.alan.clients.Client;
import com.alan.clients.anticheat.check.Check;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.anticheat.check.manager.CheckManager;
import com.alan.clients.anticheat.util.PacketUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.ad;
import net.minecraft.network.play.server.z;
import net.minecraft.util.MathHelper;
import rip.vantage.commons.util.time.StopWatch;

public final class PlayerData {
    private final EntityOtherPlayerMP player;
    private final List<Check> checks;
    private int serverPosX;
    private int serverPosY;
    private int serverPosZ;
    private int ar;
    private int as;
    private double x;
    private double y;
    private double z;
    private double at;
    private double au;
    private double av;
    private double aw;
    private double ax;
    private double ay;
    private double az;
    private double aA;
    private double aB;
    private double aC;
    private double aD;
    private double aE;
    private double aF;
    private double aG;
    private double aH;
    private double aI;
    private double aJ;
    private double aK;
    private double pitch;
    private double aM;
    private double aN;
    private boolean onGround;
    private boolean aP;
    private boolean aQ;
    private int groundTicks;
    private boolean aS;
    private boolean aT;
    private final StopWatch aU = new StopWatch();
    private final StopWatch aV = new StopWatch();
    private int aW = 20;
    private int aX = 1;
    private int aY;
    private int aZ;
    private EntityPlayer lastAttackEntity;
    private final Map<Check, Float> bb = new HashMap<>();
    private boolean bc;
    private final StopWatch bd = new StopWatch();
    private final StopWatch be = new StopWatch();
    private final StopWatch bf = new StopWatch();
    private boolean sneaking;
    private boolean sprinting;
    private boolean usingItem;
    private int bj;
    private Item bk;

    public PlayerData(EntityOtherPlayerMP player) {
        this.player = player;
        this.serverPosX = player.serverPosX;
        this.serverPosY = player.serverPosY;
        this.serverPosZ = player.serverPosZ;
        this.checks = CheckManager.loadChecks(this);
    }

    public void handle(Packet<?> packet) {
        if (this.player.ticksExisted <= 80) {
            this.aV.aX();
        } else if (!Client.a.getBotManager().a(this.player) && this.player.adr && !this.player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
            if (PacketUtil.isRelMove(packet)) {
                S14PacketEntity s14packetentity = (S14PacketEntity)packet;
                if (s14packetentity.entityId == this.player.getEntityId()) {
                    if (this.player.hurtTime != 0 && this.as > 9 && this.aZ > 40) {
                        this.as = 0;
                    }

                    this.serverPosX = this.serverPosX + s14packetentity.posX;
                    this.serverPosY = this.serverPosY + s14packetentity.posY;
                    this.serverPosZ = this.serverPosZ + s14packetentity.posZ;
                    this.at = this.x;
                    this.au = this.y;
                    this.av = this.z;
                    this.x = this.serverPosX / 32.0;
                    this.y = this.serverPosY / 32.0;
                    this.z = this.serverPosZ / 32.0;
                    this.aM = this.aK;
                    this.aN = this.pitch;
                    if (packet instanceof S17PacketEntityLookMove) {
                        this.aK = s14packetentity.yaw;
                        this.pitch = s14packetentity.pitch;
                    }

                    this.az = this.aw;
                    this.aA = this.ax;
                    this.aB = this.ay;
                    this.aw = this.x - this.at;
                    this.ax = this.y - this.au;
                    this.ay = this.z - this.av;
                    this.aD = this.aC;
                    this.aC = MathHelper.sqrt_double(this.aw * this.aw + this.ay * this.ay);
                    double d0 = (float)this.aV.getElapsedTime() / 50.0F * 0.2;
                    if (this.aV.getElapsedTime() / 50L > this.aX && (this.aC > d0 || this.aD > d0)) {
                        this.aX = (int)(this.aV.getElapsedTime() / 50L);
                    }

                    this.aV.aX();
                    this.aP = this.onGround;
                    this.onGround = !(PlayerUtil.o(this.x - 0.5, this.y - 0.43, this.z - 0.5) instanceof BlockAir)
                        || !(PlayerUtil.o(this.x + 0.5, this.y - 0.43, this.z - 0.5) instanceof BlockAir)
                        || !(PlayerUtil.o(this.x + 0.5, this.y - 0.43, this.z + 0.5) instanceof BlockAir)
                        || !(PlayerUtil.o(this.x - 0.5, this.y - 0.43, this.z + 0.5) instanceof BlockAir);
                    this.aT = this.aS;
                    this.aS = !(PlayerUtil.o(this.x - 0.5, this.y - 0.99, this.z - 0.5) instanceof BlockAir)
                        || !(PlayerUtil.o(this.x + 0.5, this.y - 0.99, this.z - 0.5) instanceof BlockAir)
                        || !(PlayerUtil.o(this.x + 0.5, this.y - 0.99, this.z + 0.5) instanceof BlockAir)
                        || !(PlayerUtil.o(this.x - 0.5, this.y - 0.99, this.z + 0.5) instanceof BlockAir);
                    if (this.onGround) {
                        this.aH = this.aE;
                        this.aI = this.aF;
                        this.aJ = this.aG;
                        this.aE = this.x;
                        this.aF = this.y;
                        this.aG = this.z;
                        this.groundTicks++;
                    } else {
                        this.groundTicks = 0;
                    }

                    if (this.aF - this.y > 2.5) {
                        this.aZ = 0;
                    }

                    double d1 = (MathHelper.wrapAngleTo180_double(Math.toDegrees(Math.atan2(this.at - this.x, this.av - this.z))) + 180.0) * -1.0;
                    double d2 = Math.abs(MathHelper.wrapAngleTo180_double(this.aw()) - d1);
                    this.aQ = d2 < 70.0 || d2 > 290.0;
                }
            } else if (packet instanceof z zx) {
                if (zx.getEntityId() == this.player.getEntityId()) {
                    this.serverPosX = zx.we();
                    this.serverPosY = zx.wf();
                    this.serverPosZ = zx.wi();
                    this.at = this.x;
                    this.au = this.y;
                    this.av = this.z;
                    this.x = this.serverPosX / 32.0;
                    this.y = this.serverPosY / 32.0;
                    this.z = this.serverPosZ / 32.0;
                    this.ar = 0;
                    this.aD = this.aC;
                    this.aC = MathHelper.sqrt_double(this.aw * this.aw + this.ay * this.ay);
                    double d3 = (float)this.aV.getElapsedTime() / 50.0F * 0.2;
                    if (this.aV.getElapsedTime() / 50L > this.aX && (this.aC > d3 || this.aD > d3)) {
                        this.aX = (int)(this.aV.getElapsedTime() / 50L);
                    }
                }
            } else if (this.player.isSwingInProgress) {
                ArrayList arraylist = new ArrayList<>(Minecraft.getMinecraft().theWorld.playerEntities);
                List list = arraylist.stream().filter(var1x -> ((Entity)var1x).getDistanceSqToEntity(this.player) < 36.0).toList();
                if (list.size() == 2) {
                    this.lastAttackEntity = (EntityPlayer)list.get(0);
                    this.aY = 0;
                }
            }

            if (packet instanceof ad ad && ad.getEntityId() == this.player.getEntityId() && ad.func_149376_c() != null) {
                ad.func_149376_c().forEach(var1x -> {
                    if (var1x.getDataValueId() == 0 && var1x.getObject() instanceof Byte) {
                        byte b0 = (Byte)var1x.getObject();
                        boolean flag = (b0 & 2) != 0;
                        boolean flag1 = (b0 & 8) != 0;
                        boolean flag2 = (b0 & 16) != 0;
                        this.sneaking = flag;
                        this.sprinting = flag1;
                        if (flag2) {
                            if (!this.usingItem) {
                                this.usingItem = true;
                            }

                            this.bj++;
                        } else {
                            this.usingItem = false;
                            this.bj = 0;
                        }
                    }
                });
            }

            this.checks.forEach(var1x -> var1x.handle(packet));
        }
    }

    public void incrementTick() {
        this.as++;
        this.ar++;
        this.aY++;
        this.aZ++;
    }

    public Check getCheck(Class<?> type) {
        for (Check check : this.checks) {
            if (check.getClass() == type) {
                return check;
            }
        }

        return null;
    }

    public float c(Check check) {
        return this.a(check, 1);
    }

    public float a(Check check, int var2) {
        float f = this.bb.getOrDefault(check, 0.0F) + var2;
        this.bb.put(check, f);
        return f;
    }

    public float a(Check check, float var2) {
        float f = this.bb.getOrDefault(check, 0.0F) * var2;
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.bb.put(check, f);
        return f;
    }

    public boolean V() {
        return this.bc;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public boolean isUsingItem() {
        return this.usingItem;
    }

    public int W() {
        return this.bj;
    }

    public Item X() {
        return this.bk;
    }

    public void a(ItemStack stack) {
        this.bk = stack != null ? stack.getItem() : null;
    }

    @Generated
    public EntityOtherPlayerMP getPlayer() {
        return this.player;
    }

    @Generated
    public List<Check> getChecks() {
        return this.checks;
    }

    @Generated
    public int getServerPosX() {
        return this.serverPosX;
    }

    @Generated
    public int getServerPosY() {
        return this.serverPosY;
    }

    @Generated
    public int getServerPosZ() {
        return this.serverPosZ;
    }

    @Generated
    public int ad() {
        return this.ar;
    }

    @Generated
    public int ae() {
        return this.as;
    }

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public double getY() {
        return this.y;
    }

    @Generated
    public double getZ() {
        return this.z;
    }

    @Generated
    public double af() {
        return this.at;
    }

    @Generated
    public double ag() {
        return this.au;
    }

    @Generated
    public double ah() {
        return this.av;
    }

    @Generated
    public double ai() {
        return this.aw;
    }

    @Generated
    public double aj() {
        return this.ax;
    }

    @Generated
    public double ak() {
        return this.ay;
    }

    @Generated
    public double al() {
        return this.az;
    }

    @Generated
    public double am() {
        return this.aA;
    }

    @Generated
    public double an() {
        return this.aB;
    }

    @Generated
    public double ao() {
        return this.aC;
    }

    @Generated
    public double ap() {
        return this.aD;
    }

    @Generated
    public double aq() {
        return this.aE;
    }

    @Generated
    public double ar() {
        return this.aF;
    }

    @Generated
    public double as() {
        return this.aG;
    }

    @Generated
    public double at() {
        return this.aH;
    }

    @Generated
    public double au() {
        return this.aI;
    }

    @Generated
    public double av() {
        return this.aJ;
    }

    @Generated
    public double aw() {
        return this.aK;
    }

    @Generated
    public double getPitch() {
        return this.pitch;
    }

    @Generated
    public double ay() {
        return this.aM;
    }

    @Generated
    public double az() {
        return this.aN;
    }

    @Generated
    public boolean isOnGround() {
        return this.onGround;
    }

    @Generated
    public boolean aA() {
        return this.aP;
    }

    @Generated
    public boolean aB() {
        return this.aQ;
    }

    @Generated
    public int getGroundTicks() {
        return this.groundTicks;
    }

    @Generated
    public boolean aC() {
        return this.aS;
    }

    @Generated
    public boolean aD() {
        return this.aT;
    }

    @Generated
    public StopWatch aE() {
        return this.aU;
    }

    @Generated
    public StopWatch aF() {
        return this.aV;
    }

    @Generated
    public int aG() {
        return this.aW;
    }

    @Generated
    public int aH() {
        return this.aX;
    }

    @Generated
    public int aI() {
        return this.aY;
    }

    @Generated
    public int aJ() {
        return this.aZ;
    }

    @Generated
    public EntityPlayer getLastAttackEntity() {
        return this.lastAttackEntity;
    }

    @Generated
    public Map<Check, Float> aL() {
        return this.bb;
    }

    @Generated
    public StopWatch aM() {
        return this.bd;
    }

    @Generated
    public StopWatch aN() {
        return this.be;
    }

    @Generated
    public StopWatch aO() {
        return this.bf;
    }

    @Generated
    public boolean isSneaking() {
        return this.sneaking;
    }
}
