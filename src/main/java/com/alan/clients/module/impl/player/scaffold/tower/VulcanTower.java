package com.alan.clients.module.impl.player.scaffold.tower;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.item.ItemStack;

public class VulcanTower extends Mode<Scaffold> {
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> {
        if (aEg.gameSettings.keyBindJump.isKeyDown() && PlayerUtil.ay(2) && aEg.thePlayer.tR > 3) {
            ItemStack itemstack = aEg.thePlayer.inventory.mainInventory[aEg.thePlayer.inventory.currentItem];
            if (itemstack != null) {
                ;
            }

            if (!MoveUtil.isMoving()) {
                var0.setOnGround(true);
                aEg.thePlayer.onGround = true;
                aEg.thePlayer.motionY = 0.5;
                if (aEg.thePlayer.ticksExisted % 2 == 0) {
                    var0.setPosX(var0.getPosX() + 0.1);
                }
            }
        }
    };

    public VulcanTower(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }
}
