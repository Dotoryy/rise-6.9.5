package com.alan.clients.script.api;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.script.api.wrapper.impl.ScriptEntity;
import com.alan.clients.script.api.wrapper.impl.ScriptItemStack;
import com.alan.clients.script.api.wrapper.impl.packet.ScriptPacket;
import com.alan.clients.script.api.wrapper.impl.vector.ScriptVector3d;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.a;
import net.minecraft.network.play.client.l;
import net.minecraft.network.play.client.m;
import net.minecraft.network.play.client.q;
import net.minecraft.network.play.client.u;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class PacketAPI extends API {
    public PacketAPI() {
    }

    public void receivePacket(ScriptPacket<?> scriptPacket) {
        if (scriptPacket != null && scriptPacket.getWrapped() != null) {
            PacketUtil.receive(scriptPacket.getWrapped());
        }
    }

    public void sendBlockPlacement(int var1, int var2, int var3, int var4, float var5, float var6, float var7) {
        BlockPos blockpos = new BlockPos(var1, var2, var3);
        SlotComponent slotcomponent = Client.a.h().b(SlotComponent.class);
        PacketUtil.send(new C08PacketPlayerBlockPlacement(blockpos, var4, SlotComponent.getItemStack(), var5, var6, var7));
    }

    public void sendKeepAlive(int var1) {
        PacketUtil.send(new a(var1));
    }

    public void sendMessage(String var1) {
        PacketUtil.send(new C01PacketChatMessage(var1));
    }

    public void sendUseEntity(ScriptEntity scriptEntity, String var2) {
        this.sendUseEntity(scriptEntity.getEntityId(), var2);
    }

    public void sendUseEntity(int var1, String var2) {
        PacketUtil.send(new C02PacketUseEntity(Minecraft.getMinecraft().theWorld.getEntityByID(var1), Action.valueOf(var2)));
    }

    public void sendUseEntity(int var1, ScriptVector3d scriptVector3d) {
        PacketUtil.send(new C02PacketUseEntity(Minecraft.getMinecraft().theWorld.getEntityByID(var1), new Vec3(scriptVector3d.getX(), scriptVector3d.getY(), scriptVector3d.getZ())));
    }

    public void sendUseEntity(ScriptEntity scriptEntity, ScriptVector3d scriptVector3d) {
        this.sendUseEntity(scriptEntity.getEntityId(), scriptVector3d);
    }

    public void sendPosition(boolean var1) {
        PacketUtil.send(new C03PacketPlayer(var1));
    }

    public void sendPosition(double var1, double var3, double var5, boolean var7) {
        PacketUtil.send(new C04PacketPlayerPosition(var1, var3, var5, var7));
    }

    public void sendPosition(double var1, double var3, double var5, float var7, float var8, boolean var9) {
        PacketUtil.send(new C06PacketPlayerPosLook(var1, var3, var5, var7, var8, var9));
    }

    public void sendDigging(String var1, ScriptVector3d scriptVector3d, String var3) {
        PacketUtil.send(
            new C07PacketPlayerDigging(
                net.minecraft.network.play.client.C07PacketPlayerDigging.Action.valueOf(var1),
                new BlockPos(scriptVector3d.getX(), scriptVector3d.getY(), scriptVector3d.getZ()),
                EnumFacing.valueOf(var3)
            )
        );
    }

    public void sendPlacement() {
        SlotComponent slotcomponent = Client.a.h().b(SlotComponent.class);
        PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
    }

    public void sendPlacement(ScriptVector3d scriptVector3d, int var2, float var3, float var4, float var5) {
        BlockPos blockpos = new BlockPos(scriptVector3d.getX(), scriptVector3d.getY(), scriptVector3d.getZ());
        SlotComponent slotcomponent = Client.a.h().b(SlotComponent.class);
        PacketUtil.send(new C08PacketPlayerBlockPlacement(blockpos, var2, SlotComponent.getItemStack(), var3, var4, var5));
    }

    public void sendPlacement(ScriptVector3d scriptVector3d, int var2, ScriptItemStack scriptItemStack, float var4, float var5, float var6) {
        PacketUtil.send(new C08PacketPlayerBlockPlacement(new BlockPos(scriptVector3d.getX(), scriptVector3d.getY(), scriptVector3d.getZ()), var2, scriptItemStack.getWrapped(), var4, var5, var6));
    }

    public void sendChangeItem(int var1) {
        PacketUtil.send(new l(var1));
    }

    public void sendAnimation(int var1, String var2) {
        PacketUtil.send(new m());
    }

    public void sendEntityAction(int var1, String var2) {
        PacketUtil.send(
            new C0BPacketEntityAction(
                Minecraft.getMinecraft().theWorld.getEntityByID(var1), net.minecraft.network.play.client.C0BPacketEntityAction.Action.valueOf(var2)
            )
        );
    }

    public void sendInput(float var1, float var2, boolean var3, boolean var4) {
        PacketUtil.send(new C0CPacketInput(var1, var2, var3, var4));
    }

    public void sendCloseWindow(int var1) {
        PacketUtil.send(new q(var1));
    }

    public void sendCloseWindow() {
        PacketUtil.send(new q(Minecraft.getMinecraft().thePlayer.openContainer.windowId));
    }

    public void sendEnchantItem(int var1, int var2) {
        PacketUtil.send(new u(var1, var2));
    }

    public void sendEnchantItem(int var1) {
        PacketUtil.send(new u(Minecraft.getMinecraft().thePlayer.openContainer.windowId, var1));
    }

    public void sendTransaction(int var1, short var2, boolean var3) {
        PacketUtil.send(new C0FPacketConfirmTransaction(var1, var2, var3));
    }

    public void sendAbilities() {
        PacketUtil.send(new C13PacketPlayerAbilities(Minecraft.getMinecraft().thePlayer.capabilities));
    }

    public void sendAbilities(boolean var1, boolean var2, boolean var3) {
        PlayerCapabilities playercapabilities = new PlayerCapabilities();
        playercapabilities.isFlying = var1;
        playercapabilities.allowFlying = var2;
        playercapabilities.isCreativeMode = var3;
        PacketUtil.send(new C13PacketPlayerAbilities(playercapabilities));
    }

    public void sendTabComplete(String var1) {
        PacketUtil.send(new C14PacketTabComplete(var1));
    }

    public void sendTabComplete(String var1, ScriptVector3d scriptVector3d) {
        PacketUtil.send(new C14PacketTabComplete(var1, new BlockPos(scriptVector3d.getX(), scriptVector3d.getY(), scriptVector3d.getZ())));
    }

    public void sendStatus(String var1) {
        PacketUtil.send(new C16PacketClientStatus(EnumState.valueOf(var1)));
    }

    public void sendSettings() {
        PacketUtil.send(new C15PacketClientSettings());
    }
}
