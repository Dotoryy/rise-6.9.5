package com.alan.clients.module.impl.movement.terrainspeed;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.TerrainSpeed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.component.impl.viamcp.FlyingPacketFixComponent;
import com.alan.clients.module.impl.movement.terrainspeed.PhysicsIntegrator;
import com.alan.clients.module.impl.movement.terrainspeed.PhysicsVector3;
import net.minecraft.block.BlockChest;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

public final class BloxdTerrainSpeed extends Mode<TerrainSpeed> {
    private int SE;
    private int SF;
    private final BooleanValue maximiseSpeed = new BooleanValue("Maximise Speed", this, true);
    private final NumberValue sneakTimer = new NumberValue("Sneak Timer", this, 2.5, 1.6, 10, 0.1);
    private final NumberValue longjumpSpeed = new NumberValue("Longjump Speed", this, 3, 0.1, 3, 0.05);
    private final NumberValue height = new NumberValue("Height", this, 25, 10, 50, 1);
    private double currentSpeed;
    private boolean lastOnGround;
    private static final double TIME_STEP = 0.03333333333333333;
    private final PhysicsIntegrator integrator = new PhysicsIntegrator();
    private int groundTicks = 0;
    private int jumpStacks = 0;
    private long velocityEndTime = 0L;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        EntityPlayerSP entityplayersp = aEg.thePlayer;
        if (aEg.thePlayer.onGround && this.e(Flight.class).isEnabled()) {
            aEg.thePlayer.jump();
        }

        aEg.timer.dzD = 1.5F;
        if (entityplayersp.onGround && this.integrator.velocity.y < 0.0) {
            this.integrator.velocity.set(0.0, 0.0, 0.0);
        }

        if (entityplayersp.onGround && entityplayersp.motionY == MoveUtil.jumpMotion()) {
            this.jumpStacks = Math.min(this.jumpStacks + 1, 3);
            this.integrator.impulse.add(new PhysicsVector3(0.0, 8.0, 0.0));
        }

        this.groundTicks = entityplayersp.onGround ? this.groundTicks + 1 : 0;
        if (this.groundTicks > 5) {
            this.jumpStacks = 0;
        }

        if (entityplayersp.isCollidedHorizontally) {
            this.integrator.velocity.set(0.0, 8.6, 0.0);
        }

        entityplayersp.motionX = entityplayersp.motionZ = 0.0;
        entityplayersp.posY = 0.0;
        boolean flag = System.currentTimeMillis() < this.velocityEndTime;
        double d0 = entityplayersp.isUsingItem() ? 0.06 : 0.26 + 0.025 * this.jumpStacks;
        if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
            if (this.maximiseSpeed.wo()) {
                d0 += 0.105 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.04;
            } else {
                d0 += 0.1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() * 0.04;
            }
        }

        if (this.maximiseSpeed.wo()) {
            d0 += 0.00698;
        }

        var1x.setSpeed(flag ? 1.0 : d0);
        if (aEg.thePlayer.ae == 1) {
            this.currentSpeed = this.longjumpSpeed.wo().doubleValue();
        }

        entityplayersp.motionY = this.integrator.integrate().y * 0.03333333333333333;
        if (aEg.thePlayer.Zl == 1) {
            ChatUtil.c(aEg.thePlayer.ae);
        }

        if (aEg.thePlayer.ae < 41 && this.e(LongJump.class).isEnabled()) {
            aEg.timer.dzD = 1.5F;
            this.currentSpeed = this.currentSpeed - this.currentSpeed / 109.0;
            aEg.thePlayer.motionY = 0.0;
            this.integrator.velocity.y = -0.1;
            this.integrator.impulse.y = -0.1;
            this.integrator.force.y = -0.1;
            if ((!MoveUtil.isMoving() || aEg.thePlayer.isCollidedHorizontally) && this.e(LongJump.class).isEnabled()) {
                var1x.setSpeed(0.3);
            } else if (this.e(LongJump.class).isEnabled()) {
                var1x.setSpeed(this.currentSpeed);
            }
        }

        if (aEg.thePlayer.isCollidedHorizontally && this.e(Flight.class).isEnabled()) {
            this.integrator.velocity.y = this.height.wo().intValue();
            this.integrator.impulse.y = this.height.wo().intValue();
            this.integrator.force.y = this.height.wo().intValue();
        }

        if (this.e(Flight.class).isEnabled() && aEg.thePlayer.tR < 12 && this.jumpStacks < 3
            || this.e(Flight.class).isEnabled() && aEg.gameSettings.keyBindSneak.isKeyDown()) {
            this.integrator.force.y = -10.0;
            this.integrator.impulse.y = -0.01;
            aEg.timer.dzD = 1.6F;
        }

        if (this.e(Flight.class).isEnabled() && !aEg.gameSettings.keyBindSneak.isKeyDown() && aEg.thePlayer.motionY < 0.0 && aEg.thePlayer.tR > 30) {
            this.integrator.velocity.y += 0.06;
        }

        if (this.jumpStacks < 3 && this.e(Speed.class).isEnabled() && aEg.thePlayer.tR < 12) {
            this.integrator.force.y = -10.0;
            this.integrator.impulse.y = -0.01;
        }

        if (aEg.thePlayer.ae == 13) {
            this.e(LongJump.class).isEnabled();
        }

        if (!aEg.gameSettings.keyBindSneak.isKeyDown() || !(FlyingPacketFixComponent.il > -390.0) || this.e(Flight.class).isEnabled() && aEg.thePlayer.tR > 30) {
            if (aEg.gameSettings.keyBindSneak.isKeyDown()
                && aEg.thePlayer.ticksExisted % 5 == 0
                && (!this.e(Flight.class).isEnabled() || aEg.thePlayer.tR <= 30)) {
                ChatUtil.b("Your Balance has ran out");
            }
        } else {
            aEg.timer.dzD = this.sneakTimer.wo().floatValue();
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> {
        if (aEg.thePlayer.ae < 41) {
            MoveUtil.isMoving();
        }

        var0.setSneak(false);
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity s12packetentityvelocity = (S12PacketEntityVelocity)var1x.getPacket();
            if (aEg.thePlayer != null && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                this.velocityEndTime = System.currentTimeMillis() + 1300L;
            }
        } else if (var1x.getPacket() instanceof S3FPacketCustomPayload) {
            S3FPacketCustomPayload s3fpacketcustompayload = (S3FPacketCustomPayload)var1x.getPacket();
            if ("bloxd:resyncphysics".equals(s3fpacketcustompayload.getChannelName())) {
                PacketBuffer packetbuffer = s3fpacketcustompayload.getBufferData();
                this.jumpStacks = 0;
                this.integrator.force.set(0.0, 0.0, 0.0);
                this.integrator.impulse.set(0.0, 0.0, 0.0);
                this.integrator.velocity.set(packetbuffer.readFloat(), packetbuffer.readFloat(), packetbuffer.readFloat());
            }
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (var1x.dq() instanceof C03PacketPlayer c03packetplayer) {
            C03PacketPlayer c03packetplayer1 = (C03PacketPlayer)var1x.dq();
            if (!(c03packetplayer1 instanceof C06PacketPlayerPosLook) && (c03packetplayer.isMoving() || !aEg.thePlayer.onGround)) {
                var1x.setCancelled(true);
                double d0 = aEg.thePlayer.posX;
                double d1 = aEg.thePlayer.posY;
                double d2 = aEg.thePlayer.posZ;
                float f = aEg.thePlayer.pl;
                float f1 = aEg.thePlayer.rotationPitch;
                boolean flag = aEg.thePlayer.onGround;
                C06PacketPlayerPosLook c06packetplayerposlook = new C06PacketPlayerPosLook(d0, d1, d2, f, f1, flag);
                this.lastOnGround = c03packetplayer.aO;
                aEg.getNetHandler().addToSendQueue(c06packetplayerposlook);
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.ticksExisted < 2) {
            aEg.getNetHandler().addToSendQueue(new C01PacketChatMessage("/servername Rise"));
        }

        if (aEg.thePlayer.ae < 41) {
            this.e(LongJump.class).isEnabled();
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        if (var0.getBlock() instanceof BlockChest) {
            double d0 = var0.getBlockPos().getX();
            double d1 = var0.getBlockPos().getY();
            double d2 = var0.getBlockPos().getZ();
            var0.setBoundingBox(AxisAlignedBB.fromBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(d0, d1, d2));
        }

        AxisAlignedBB axisalignedbb = var0.dh();
    };

    public BloxdTerrainSpeed(String var1, TerrainSpeed terrainSpeed) {
        super(var1, terrainSpeed);
    }

    @Override
    public void onEnable() {
        aEg.getNetHandler().addToSendQueue(new C01PacketChatMessage("/servername Rise"));
    }
}
