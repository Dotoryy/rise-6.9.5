package com.alan.clients.module.impl.player;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.player.autopot.name", description = "module.player.autopot.description", category = Category.PLAYER)
public class AutoPot extends Module {
    private final NumberValue health = new NumberValue("Health", this, 15, 1, 20, 1);
    private final BoundsNumberValue delay = new BoundsNumberValue("Delay", this, 500, 1000, 50, 5000, 50);
    private final BoundsNumberValue rotationSpeed = new BoundsNumberValue("Rotation Speed", this, 5, 10, 0, 10, 1);
    private final StopWatch stopWatch = new StopWatch();
    private int attackTicks;
    private long nextThrow;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        this.attackTicks++;
        if (aEg.currentScreen != null) {
            this.attackTicks = 0;
        }

        if (aEg.thePlayer.cqL > 1 && this.stopWatch.T(this.nextThrow) && this.attackTicks >= 10 && !this.e(Scaffold.class).isEnabled()) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemstack = aEg.thePlayer.inventory.getStackInSlot(i);
                if (itemstack != null) {
                    Item item = itemstack.getItem();
                    if (item instanceof ItemPotion) {
                        List list = ((ItemPotion)item).getEffects(itemstack);
                        if (!list.isEmpty()) {
                            PotionEffect potioneffect = (PotionEffect)list.get(0);
                            if (ItemPotion.isSplash(itemstack.getMetadata())
                                && PlayerUtil.aw(potioneffect.getPotionID())
                                && (
                                    potioneffect.getPotionID() != Potion.regeneration.id && potioneffect.getPotionID() != Potion.heal.id
                                        || !(aEg.thePlayer.getHealth() > this.health.wo().floatValue())
                                )
                                && (
                                    !aEg.thePlayer.isPotionActive(potioneffect.potionID)
                                        || aEg.thePlayer.getActivePotionEffect(potioneffect.potionID).duration == 0
                                )) {
                                double d0 = this.rotationSpeed.wo().doubleValue();
                                double d1 = this.rotationSpeed.wA().doubleValue();
                                float f = (float)MathUtil.l(d0, d1);
                                RotationComponent.setRotations(
                                    new Vector2f((float)(aEg.thePlayer.pl + (Math.random() - 0.5) * 3.0), (float)(87.0 + Math.random() * 3.0)),
                                    f,
                                    MovementFix.NORMAL
                                );
                                SlotComponent slotcomponent = this.d(SlotComponent.class);
                                SlotComponent.setSlot(i);
                                if (RotationComponent.fk.y > 85.0F && !BadPacketsComponent.bad(false, true, false, true, false)) {
                                    aEg.playerController.syncCurrentPlayItem();
                                    SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                                    PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
                                    this.nextThrow = Math.round(MathUtil.l(this.delay.wo().longValue(), this.delay.wA().longValue()));
                                    this.stopWatch.aX();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1 -> this.attackTicks = 0;

    public AutoPot() {
    }
}
