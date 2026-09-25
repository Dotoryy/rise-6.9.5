package com.alan.clients.anticheat.check.impl.combat;

import com.alan.clients.anticheat.check.Check;
import com.alan.clients.anticheat.check.api.CheckInfo;
import com.alan.clients.anticheat.data.PlayerData;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.ad;

@CheckInfo(R = "AutoBlock", S = "B", description = "Impossible 'Sprinting' and 'Use-Item' status combination.")
public final class AutoBlockB extends Check {
    public AutoBlockB(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(Packet<?> packet) {
        if (packet instanceof ad ad
            && ad.getEntityId() == this.data.getPlayer().getEntityId()
            && ad.func_149376_c() != null
            && this.data.W() > 1
            && this.data.isUsingItem()
            && this.data.isSprinting()) {
            ItemStack itemstack = this.data.getPlayer().getHeldItem();
            if (itemstack != null && itemstack.getItem() instanceof ItemSword && this.increaseBufferBy(1.0) >= 4.0) {
                this.J();
                this.M();
            }
        }
    }
}
