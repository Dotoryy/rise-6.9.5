package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

@ModuleInfo(aliases = "module.player.healthbypass.name", description = "module.player.healthbypass.description", category = Category.PLAYER)
public final class HealthBypass extends Module {
    @EventLink
    public final Listener<TickEvent> onTick = var0 -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            for (Entity entity : aEg.theWorld.loadedEntityList) {
                if (entity instanceof EntityPlayer entityplayer && entityplayer != aEg.thePlayer) {
                    float f = getScoreboardHealth(entityplayer);
                    if ((!(f <= 0.0F) || entityplayer.isDead) && !Float.isNaN(f) && !Float.isInfinite(f)) {
                        float f1 = entityplayer.getMaxHealth();
                        if (f1 > 0.0F) {
                            f = Math.min(f, f1);
                        }

                        entityplayer.setHealth(f);
                    }
                }
            }
        }
    };

    public HealthBypass() {
    }

    public static float getScoreboardHealth(EntityLivingBase living) {
        if (!(living instanceof EntityPlayer entityplayer)) {
            return living.getHealth();
        }
        Scoreboard scoreboard = entityplayer.getWorldScoreboard();
        if (scoreboard == null) {
            return living.getHealth();
        }

        ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
        return scoreobjective == null ? living.getHealth() : scoreboard.getValueFromObjective(entityplayer.getName(), scoreobjective).getScorePoints();
    }
}
