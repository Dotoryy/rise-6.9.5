package com.alan.clients.component.impl.combat;

import com.alan.clients.Client;
import com.alan.clients.component.Component;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.social.FriendManager;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;

public class TargetComponent extends Component implements InstanceAccess {
    private static KillAura gj;

    public TargetComponent() {
    }

    public static EntityLivingBase e(double var0) {
        return f(var0).stream().findFirst().orElse(null);
    }

    public static List<EntityLivingBase> f(double var0) {
        if (gj == null) {
            gj = Client.a.g().c(KillAura.class);
        }

        return b(gj.player.wo(), gj.invisibles.wo(), gj.animals.wo(), gj.mobs.wo(), gj.playerTeammates.wo()).stream().filter(var2 -> PlayerUtil.v(var2) <= var0).collect(Collectors.toList());
    }

    public static List<EntityLivingBase> a(double var0, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
        return b(var2, var3, var4, var5, var6).stream().filter(var2x -> PlayerUtil.v(var2x) <= var0).collect(Collectors.toList());
    }

    public static List<EntityLivingBase> bR() {
        if (gj == null) {
            gj = Client.a.g().c(KillAura.class);
        }

        return b(gj.player.wo(), gj.invisibles.wo(), gj.animals.wo(), gj.mobs.wo(), gj.playerTeammates.wo());
    }

    public static List<EntityLivingBase> b(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4) {
        return b(var0, var1, var2, var3, var4, false);
    }

    public static List<EntityLivingBase> b(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5) {
        return aEg.theWorld
            .loadedEntityList
            .stream()
            .filter(
                var5x -> var5x instanceof EntityLivingBase
                    && !Client.a.getBotManager().a(var5x)
                    && var5x != aEg.getRenderViewEntity()
                    && (!FriendManager.isFriend(var5x.getName()) || var5)
                    && (!(var5x instanceof EntityPlayer) || var0)
                    && (!(var5x instanceof IMob) && !(var5x instanceof INpc) || var3)
                    && (!(var5x instanceof EntityAnimal) && !(var5x instanceof EntityAmbientCreature) && !(var5x instanceof EntityWaterMob) || var2)
                    && (!var5x.isInvisible() || var1)
                    && !(var5x instanceof EntityArmorStand)
            )
            .map(var0x -> (EntityLivingBase)var0x)
            .filter(var1x -> !(var1x instanceof EntityPlayer) || !PlayerUtil.sameTeam(var1x) || var4)
            .collect(Collectors.toList());
    }
}
