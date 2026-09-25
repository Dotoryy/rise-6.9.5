package com.alan.clients.module.impl.player;

import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.BlockBreakEvent;
import com.alan.clients.newevent.impl.other.BlockDamageEvent;
import com.alan.clients.util.player.SlotUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

@ModuleInfo(aliases = "module.player.autotool.name", description = "module.player.autotool.description", category = Category.PLAYER)
public class AutoTool extends Module {
    private int blockBreak;
    private BlockPos blockPos;
    @EventLink(value = 4)
    public final Listener<BlockDamageEvent> onBlockDamage = var1 -> {
        if (!this.e(Scaffold.class).isEnabled()) {
            if (var1.getPlayer() == aEg.thePlayer) {
                if (!(aEg.thePlayer.getDistanceSq(var1.getBlockPos()) > 49.0)) {
                    if (this.isMiningBlock()) {
                        if (var1.getBlockPos().equals(aEg.objectMouseOver.getBlockPos())) {
                            this.blockBreak = 15;
                            this.blockPos = var1.getBlockPos();
                            this.switchTool();
                        }
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<BlockBreakEvent> onBlockBreak = var1 -> {
        this.blockBreak = 0;
        this.blockPos = null;
    };
    @EventLink
    public final Listener<PreUpdateEvent> abs = var1 -> this.switchTool();

    public AutoTool() {
    }

    private boolean isMiningBlock() {
        return aEg.playerController != null
            && (aEg.playerController.isHittingBlock || aEg.gameSettings.cgK.isKeyDown())
            && aEg.objectMouseOver != null
            && aEg.objectMouseOver.typeOfHit == MovingObjectType.BLOCK;
    }

    public void switchTool() {
        if (!this.e(Scaffold.class).isEnabled()) {
            if (!this.isMiningBlock()) {
                this.blockBreak = 0;
            } else if (aEg.objectMouseOver != null && this.blockBreak > 0 && this.blockPos != null) {
                if (aEg.objectMouseOver.typeOfHit == MovingObjectType.BLOCK && this.blockPos.equals(aEg.objectMouseOver.getBlockPos())) {
                    this.blockBreak--;
                    int i = SlotUtil.findTool(this.blockPos);
                    if (i != -1) {
                        SlotComponent slotcomponent = this.d(SlotComponent.class);
                        SlotComponent.setSlot(i);
                    }
                } else {
                    this.blockBreak = 0;
                }
            }
        }
    }
}
