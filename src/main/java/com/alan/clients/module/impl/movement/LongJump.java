package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.longjump.BloxdLongJump;
import com.alan.clients.module.impl.movement.longjump.FireBallLongJump;
import com.alan.clients.module.impl.movement.longjump.GrimLongJump;
import com.alan.clients.module.impl.movement.longjump.Matrix2LongJump;
import com.alan.clients.module.impl.movement.longjump.NCPLongJump;
import com.alan.clients.module.impl.movement.longjump.VanillaLongJump;
import com.alan.clients.module.impl.movement.longjump.Vulcan2LongJump;
import com.alan.clients.module.impl.movement.longjump.VulcanLongJump;
import com.alan.clients.module.impl.movement.longjump.Watchdog2LongJump;
import com.alan.clients.module.impl.movement.longjump.WatchdogFireBall2LongJump;
import com.alan.clients.module.impl.movement.longjump.WatchdogFireBallLongJump;
import com.alan.clients.module.impl.movement.longjump.WatchdogGlideLongJump;
import com.alan.clients.module.impl.movement.longjump.WatchdogLongJump;
import com.alan.clients.module.impl.movement.longjump.WatchdogWater118LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.PacketlessDamageComponent;

@ModuleInfo(aliases = "module.movement.longjump.name", description = "module.movement.longjump.description", category = Category.MOVEMENT)
public class LongJump extends Module {
    public final ModeValue mode = new ModeValue("Mode", this)
        .add(new VanillaLongJump("Vanilla", this))
        .add(new Matrix2LongJump("Matrix 2", this))
        .add(new NCPLongJump("NCP", this))
        .add(new VulcanLongJump("Vulcan", this))
        .add(new WatchdogWater118LongJump("Watchdog Water 1.18+", this))
        .add(new Watchdog2LongJump("Watchdog 2", this))
        .add(new FireBallLongJump("Fire Ball", this))
        .add(new WatchdogLongJump("Watchdog", this))
        .add(new WatchdogGlideLongJump("Watchdog Glide", this))
        .add(new WatchdogFireBallLongJump("Watchdog Fire Ball", this))
        .add(new WatchdogFireBall2LongJump("Watchdog Fire Ball 2", this))
        .add(new GrimLongJump("Grim", this))
        .add(new BloxdLongJump("Bloxd", this))
        .add(new Vulcan2LongJump("Vulcan 2", this))
        .setDefault("Vanilla");
    public final BooleanValue autoDisable = new BooleanValue("Auto Disable", this, true);
    private final BooleanValue fakeDamage = new BooleanValue("Fake Damage", this, false);
    private boolean inAir;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (this.autoDisable.wo()
            && this.inAir
            && aEg.thePlayer.onGround
            && !this.mode.wo().getName().equals("Watchdog")
            && !this.mode.wo().getName().equals("Watchdog 2")) {
            this.toggle();
        }

        this.inAir = !aEg.thePlayer.onGround && !PacketlessDamageComponent.bd();
    };

    public LongJump() {
    }

    @Override
    public void onEnable() {
        if (this.fakeDamage.wo() && aEg.thePlayer.ticksExisted > 1) {
            PlayerUtil.fakeDamage();
        }
    }

    @Override
    public void onDisable() {
        aEg.timer.dzD = 1.0F;
        this.inAir = false;
    }
}
