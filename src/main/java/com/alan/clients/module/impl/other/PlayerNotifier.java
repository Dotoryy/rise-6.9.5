package com.alan.clients.module.impl.other;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.player.PlayerUtil;
import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3i;

@ModuleInfo(aliases = "module.other.playernotifier.name", description = "module.other.playernotifier.description", category = Category.PLAYER)
public final class PlayerNotifier extends Module {
    private final NumberValue distance = new NumberValue("Notify Distance", this, 35, 10, 50, 1);
    private final BooleanValue notifyOnce = new BooleanValue("Notify Once", this, false);
    private Vec3i bedPosition = new Vec3i(0, 0, 0);
    private final String[] colors = new String[]{"§e", "§6", "§c", "§4"};
    private final ArrayList<EntityLivingBase> notifiedPlayers = new ArrayList<>();
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (aEg.thePlayer.ticksExisted % 25 == 0) {
            aMR.execute(() -> {
                for (EntityLivingBase entitylivingbase : aEg.theWorld.playerEntities) {
                    if (!PlayerUtil.sameTeam(entitylivingbase) && !Client.a.getBotManager().a(entitylivingbase) && entitylivingbase != aEg.thePlayer) {
                        int i = (int)entitylivingbase.getDistance(this.bedPosition.getX(), this.bedPosition.getY(), this.bedPosition.getZ());
                        if (i < this.distance.wo().intValue()) {
                            if (this.notifiedPlayers.contains(entitylivingbase) && this.notifyOnce.wo()) {
                                ChatUtil.b("Didn't display: " + entitylivingbase.getName());
                            } else {
                                ChatUtil.b(this.getColor(i) + entitylivingbase.getName() + " is " + i + " blocks away from your bed.");
                            }

                            this.notifiedPlayers.add(entitylivingbase);
                        } else {
                            this.notifiedPlayers.remove(entitylivingbase);
                        }
                    }
                }
            });
        }
    };

    public PlayerNotifier() {
    }

    @Override
    public void onEnable() {
        this.bedPosition = new Vec3i(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
    }

    public String getColor(double var1) {
        double d0 = this.distance.wo().intValue();
        if (var1 < 9.0) {
            return this.colors[this.colors.length - 1];
        }

        for (String s : this.colors) {
            d0 /= 2.0;
            if (var1 > d0) {
                return s;
            }
        }

        return this.colors[this.colors.length - 1];
    }
}
