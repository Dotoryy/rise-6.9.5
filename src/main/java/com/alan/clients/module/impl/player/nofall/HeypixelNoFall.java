package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.BlockPos;

public class HeypixelNoFall extends Mode<NoFall> {
    private boolean spoofingFall;
    private boolean holdingJump;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (this.holdingJump) {
            aEg.gameSettings.keyBindJump.setPressed(true);
        }

        if (this.spoofingFall) {
            aEg.thePlayer.motionY = 0.0;
            var1x.setOnGround(false);
            var1x.setPosY(var1x.getPosY() - 0.098F);
            aEg.thePlayer.setPositionAndUpdate(aEg.thePlayer.posX, var1x.getPosY(), aEg.thePlayer.posZ);
        } else if (!(aEg.thePlayer.motionY > 0.0) && !(FallDistanceComponent.cY <= 3.0F)) {
            if (PlayerUtil.block(new BlockPos(var1x.getPosX(), var1x.getPosY() + aEg.thePlayer.motionY, var1x.getPosZ())).getMaterial().isSolid()) {
                FallDistanceComponent.cY = 0.0F;
                this.spoofingFall = true;
                this.holdingJump = true;
                aEg.gameSettings.keyBindJump.setPressed(true);
            }
        }
    };
    @EventLink(value = -1)
    public final Listener<MoveInputEvent> onMoveInput = var1x -> {
        if (this.holdingJump) {
            var1x.setJump(true);
        }
    };
    @EventLink(value = -1)
    public final Listener<JumpEvent> onJump = var1x -> {
        if (this.holdingJump && !this.spoofingFall && !var1x.isCancelled()) {
            this.releaseJump();
        }
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1x -> this.spoofingFall = false;

    public HeypixelNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }

    @Override
    public void onEnable() {
        this.spoofingFall = false;
        this.holdingJump = false;
    }

    @Override
    public void onDisable() {
        this.spoofingFall = false;
        this.releaseJump();
    }

    private void releaseJump() {
        this.holdingJump = false;
        aEg.gameSettings.keyBindJump.setPressed(GameSettings.isKeyDown(aEg.gameSettings.keyBindJump));
    }
}
