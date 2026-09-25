package com.alan.clients.anticheat.check.impl.combat;

import com.alan.clients.anticheat.check.Check;
import com.alan.clients.anticheat.check.api.CheckInfo;
import com.alan.clients.anticheat.data.PlayerData;
import com.alan.clients.anticheat.util.PacketUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.ad;

@CheckInfo(R = "AutoBlock", S = "A", description = "Unlikely sword blocking/unblocking")
public final class AutoBlockA extends Check {
    public AutoBlockA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(Packet<?> packet) {
        if (packet instanceof ad ad && ad.getEntityId() == this.data.getPlayer().getEntityId() && ad.func_149376_c() != null) {
            int i = this.a(this.data.getPlayer());
            int j = this.data.W();
            if (i != -1 && j > i) {
                if (this.increaseBufferBy(1.0) > 2.0) {
                    this.J();
                }
            } else {
                this.decreaseBufferBy(0.1F);
            }
        }

        if (PacketUtil.isRelMove(packet) && ((S14PacketEntity)packet).entityId == this.data.getPlayer().getEntityId() && !this.data.V()) {
            this.decreaseBufferBy(0.985F);
        }
    }

    private int a(EntityOtherPlayerMP other) {
        ItemStack itemstack = other.getHeldItem();
        if (itemstack == null) {
            return -1;
        }
        Item item = itemstack.getItem();
        if (item == Items.bow) {
            return 4;
        }
        return !(item instanceof ItemSword)
                && !(item instanceof ItemFood)
                && (!(item instanceof ItemPotion) || ItemPotion.isSplash(itemstack.getMetadata()))
            ? -1
            : 1;
    }
}
