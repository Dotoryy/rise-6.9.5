package com.alan.clients.module.impl.movement.jesus;

import com.alan.clients.module.impl.movement.Jesus;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import net.minecraft.potion.Potion;

public class VulcanGravityJesus extends Mode<Jesus> {
    private boolean shouldDrop = true;
    private boolean jumpHeld = true;
    private int ticksSinceWater = 0;
    private double targetY = 50.0;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (!aEg.gameSettings.keyBindJump.isPressed()
            && aEg.thePlayer.isInWater()
            && this.shouldDrop
            && aEg.thePlayer.posY % 1.0 != 0.0
            && aEg.thePlayer.posY * 2.0 % 1.0 != 0.0) {
            aEg.thePlayer.setPosition(aEg.thePlayer.posX, aEg.thePlayer.posY - 0.85, aEg.thePlayer.posZ);
            this.shouldDrop = false;
        }

        if (aEg.thePlayer.isInWater() && !aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.posY % 1.0 != 0.0 && aEg.thePlayer.posY * 2.0 % 1.0 != 0.0) {
            MoveUtil.strafe(0.34 - Math.random() / 1000.0);
            aEg.thePlayer.motionY = 0.0;
        }

        if (this.shouldDrop) {
            this.targetY = aEg.thePlayer.posY - 1.6;
        }

        if (aEg.thePlayer.isInWater()) {
            this.ticksSinceWater = 0;
        }

        if (this.ticksSinceWater < 20) {
            MoveUtil.strafe();
        }

        this.ticksSinceWater++;
        if (!aEg.gameSettings.keyBindJump.isKeyDown() && !aEg.gameSettings.keyBindJump.isPressed()) {
            this.jumpHeld = false;
        } else {
            this.jumpHeld = true;
        }

        if (this.jumpHeld && aEg.thePlayer.isInWater() && !aEg.gameSettings.keyBindJump.isKeyDown() && !aEg.gameSettings.keyBindJump.isPressed()) {
            aEg.thePlayer.setPosition(aEg.thePlayer.posX, this.targetY, aEg.thePlayer.posZ);
        }

        if (aEg.thePlayer.isPotionActive(Potion.moveSpeed) && aEg.thePlayer.isInWater() && !aEg.gameSettings.keyBindJump.isKeyDown()) {
            MoveUtil.strafe(0.05 * (1 + aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()) + 0.34 - Math.random() / 1000.0);
        }

        if (!aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.isInWater() && aEg.thePlayer.posY % 1.0 != 0.0 && aEg.thePlayer.posY * 2.0 % 1.0 != 0.0) {
            aEg.thePlayer.setPosition(aEg.thePlayer.posX, this.targetY, aEg.thePlayer.posZ);
        }

        if (aEg.gameSettings.keyBindJump.isKeyDown() && aEg.thePlayer.isInWater()) {
            aEg.thePlayer.motionY = 0.6;
            MoveUtil.strafe(0.1);
        }

        if (aEg.thePlayer.isInWater()
            && !aEg.gameSettings.keyBindJump.isKeyDown()
            && !aEg.gameSettings.keyBindJump.isPressed()
            && aEg.thePlayer.posY % 1.0 != 0.0
            && aEg.thePlayer.posY * 2.0 % 1.0 != 0.0) {
            aEg.thePlayer.setPosition(aEg.thePlayer.posX, this.targetY - 0.2, aEg.thePlayer.posZ);
        }

        if (!aEg.thePlayer.isInWater() && !this.shouldDrop && aEg.thePlayer.posY % 1.0 != 0.0 && aEg.thePlayer.posY * 2.0 % 1.0 != 0.0) {
            this.shouldDrop = true;
        }
    };

    public VulcanGravityJesus(String var1, Jesus jesus) {
        super(var1, jesus);
    }
}
