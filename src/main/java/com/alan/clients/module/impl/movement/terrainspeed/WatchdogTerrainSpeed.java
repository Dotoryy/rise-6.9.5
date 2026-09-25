package com.alan.clients.module.impl.movement.terrainspeed;

import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.TerrainSpeed;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.potion.Potion;

public final class WatchdogTerrainSpeed extends Mode<TerrainSpeed> {
    private int offStairTicks;
    private int offSlabTicks;
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (PlayerUtil.o(aEg.thePlayer.posX, aEg.thePlayer.posY - 0.5, aEg.thePlayer.posZ) instanceof BlockSlab
            && aEg.thePlayer.posY == Math.floor(aEg.thePlayer.posY) + 0.5
            && !this.e(Scaffold.class).isEnabled()
            && !this.e(Speed.class).isEnabled()
            && this.e(KillAura.class).jE == null
            && !aEg.gameSettings.keyBindBack.isKeyDown()) {
            if (aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                double[] adouble = new double[]{0.276, 0.309, 0.349, 0.3967};
            }

            this.offSlabTicks = 0;
        } else {
            this.offSlabTicks++;
            if (this.offSlabTicks == 1) {
                var1x.setFriction(var1x.getFriction() * 0.9F);
            }
        }

        if (PlayerUtil.o(aEg.thePlayer.posX, aEg.thePlayer.posY - 0.5, aEg.thePlayer.posZ) instanceof BlockStairs
            && !this.e(Scaffold.class).isEnabled()
            && !this.e(Speed.class).isEnabled()
            && this.e(KillAura.class).jE == null
            && aEg.gameSettings.keyBindForward.isKeyDown()) {
            if (aEg.thePlayer.posY == Math.floor(aEg.thePlayer.posY) + 0.5 || this.offStairTicks == 0) {
                var1x.setFriction((float)(var1x.getFriction() * 3.98));
                this.offStairTicks = 0;
            }
        } else {
            this.offStairTicks++;
            if (this.offStairTicks == 1) {
                MoveUtil.stop();
            }
        }
    };
    @EventLink
    public final Listener<MoveEvent> onMove = var1x -> {
        if (this.offStairTicks < 2) {
            var1x.setPosY(-0.0784);
            aEg.gameSettings.keyBindJump.setPressed(false);
        }
    };

    public WatchdogTerrainSpeed(String var1, TerrainSpeed terrainSpeed) {
        super(var1, terrainSpeed);
    }
}
