package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.player.PlayerUtil;
import lombok.Generated;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

@ModuleInfo(aliases = "module.player.fastbreak.name", description = "module.player.fastbreak.description", category = Category.PLAYER)
public final class FastBreak extends Module {
    public final ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Percentage")).add(new SubMode("Ticks")).setDefault("Ticks");
    public final NumberValue speed = new NumberValue("Speed", this, 50, 0, 100, 1, () -> this.mode.wo().getName().equals("Ticks"));
    public final NumberValue ticks = new NumberValue("Ticks", this, 1, 1, 100, 1, () -> this.mode.wo().getName().equals("Percentage"));
    public final BooleanValue ignoreMiningFatigue = new BooleanValue("Ignore Mining Fatigue", this, false);
    public final BooleanValue equalAirGroundDig = new BooleanValue("Equal Air/Ground Dig", this, true);
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        if (this.ignoreMiningFatigue.wo()) {
            aEg.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
        }

        double d0;
        label60: {
            {
                aEg.playerController.blockHitDelay = 0;
                d0 = 0.0;
                String s = this.mode.wo().getName();
                switch (s) {
                    case "Percentage":
                        d0 = this.speed.wo().doubleValue() / 100.0;
                        if (aEg.thePlayer.tR == 1 && this.equalAirGroundDig.wo()) {
                            aEg.playerController.curBlockDamageMP /= 5.0F;
                            d0 = 0.8F;
                        }

                        if (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air && !aEg.thePlayer.onGround && this.equalAirGroundDig.wo()) {
                            aEg.playerController.curBlockDamageMP *= 5.0F;
                            d0 -= 0.8F;
                        }
                        break label60;
                    case "Ticks":
                        break;
                    default:
                        break label60;
                }
            }

            if (aEg.objectMouseOver != null && aEg.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
                BlockPos blockpos = aEg.objectMouseOver.getBlockPos();
                d0 = PlayerUtil.block(blockpos).getPlayerRelativeBlockHardness(aEg.thePlayer, aEg.theWorld, blockpos) * this.ticks.wo().intValue();
                if (aEg.thePlayer.tR == 1 && this.equalAirGroundDig.wo()) {
                    aEg.playerController.curBlockDamageMP /= 5.0F;
                    d0 = 0.81F;
                }

                if (PlayerUtil.p(0.0, aEg.thePlayer.motionY, 0.0) != Blocks.air && !aEg.thePlayer.onGround && this.equalAirGroundDig.wo()) {
                    aEg.playerController.curBlockDamageMP *= 5.0F;
                    d0 -= 0.81F;
                }
            }
        }

        if (aEg.playerController.curBlockDamageMP > 1.0 - d0 && aEg.playerController.curBlockDamageMP < 0.99F) {
            aEg.playerController.curBlockDamageMP = 0.99F;
        }
    };

    public FastBreak() {
    }

    @Generated
    public NumberValue getSpeed() {
        return this.speed;
    }
}
