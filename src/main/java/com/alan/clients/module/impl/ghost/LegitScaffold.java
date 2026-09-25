package com.alan.clients.module.impl.ghost;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemBlock;

@ModuleInfo(aliases = {"module.ghost.legitscaffold.name", "Eagle"}, description = "module.ghost.eagle.description", category = Category.GHOST)
public class LegitScaffold extends Module {
    private final BoundsNumberValue length = new BoundsNumberValue("Length", this, 1, 1, 1, 4, 1);
    private final NumberValue slow = new NumberValue("Sneak speed multiplier", this, 0.3, 0.2, 1, 0.05);
    private final BooleanValue groundOnly = new BooleanValue("Only on ground", this, false);
    private final BooleanValue blocksOnly = new BooleanValue("Only when holding blocks", this, false);
    private final BooleanValue backwardsOnly = new BooleanValue("Only when moving backwards", this, false);
    private final BooleanValue onlyOnSneak = new BooleanValue("Only on Sneak", this, true);
    private boolean overAir;
    private int overAirTicks;
    private int sneakTicks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        if (aEg.thePlayer.getHeldItem() != null && !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemBlock) && this.blocksOnly.wo()) {
            if (this.overAir) {
                this.overAir = false;
            }
        } else {
            if ((aEg.thePlayer.onGround || !this.groundOnly.wo())
                && PlayerUtil.p(0.0, -1.0, 0.0) instanceof BlockAir
                && (!aEg.gameSettings.keyBindForward.isKeyDown() || !this.backwardsOnly.wo())) {
                if (!this.overAir) {
                    this.overAir = true;
                }
            } else if (this.overAir) {
                this.overAir = false;
            }

            if (this.overAir) {
                this.sneakTicks = this.length.wv().intValue();
                aEg.gameSettings.cgG.setPressed(false);
            }

            if (this.overAir) {
                this.overAirTicks++;
            } else {
                this.overAirTicks = 0;
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1 -> {
        SlotComponent slotcomponent = this.d(SlotComponent.class);
        if (SlotComponent.getItemStack() != null) {
            slotcomponent = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack().getItem() instanceof ItemBlock) {
                var1.setSneak(
                    this.sneakTicks > 0 && (aEg.gameSettings.keyBindSneak.isKeyDown() || !this.onlyOnSneak.wo()) || aEg.gameSettings.keyBindSneak.isKeyDown() && !this.onlyOnSneak.wo()
                );
            }
        }

        this.sneakTicks--;
        if (this.sneakTicks > 0 && this.overAirTicks <= 2) {
            var1.setSneakSlowDownMultiplier(this.slow.wo().doubleValue());
        }
    };

    public LegitScaffold() {
    }

    @Override
    public void onDisable() {
        if (this.overAir) {
            this.overAir = false;
        }
    }
}
