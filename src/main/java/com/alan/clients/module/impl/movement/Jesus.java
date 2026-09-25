package com.alan.clients.module.impl.movement;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.jesus.KarhuJesus;
import com.alan.clients.module.impl.movement.jesus.NCPJesus;
import com.alan.clients.module.impl.movement.jesus.VanillaJesus;
import com.alan.clients.module.impl.movement.jesus.VulcanDolphinJesus;
import com.alan.clients.module.impl.movement.jesus.VulcanGravityJesus;
import com.alan.clients.module.impl.movement.jesus.WatchdogDolphin118Jesus;
import com.alan.clients.module.impl.movement.jesus.WatchdogJesus;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.util.player.PlayerUtil;

@ModuleInfo(aliases = "module.movement.jesus.name", description = "module.movement.jesus.description", category = Category.MOVEMENT)
public class Jesus extends Module {
    public final ModeValue mode = new ModeValue("Mode", this)
        .add(new VanillaJesus("Vanilla", this))
        .add(new VulcanGravityJesus("Vulcan Gravity", this))
        .add(new KarhuJesus("Karhu", this))
        .add(new NCPJesus("NCP", this))
        .add(new WatchdogJesus("Watchdog", this))
        .add(new VulcanDolphinJesus("Vulcan Dolphin", this))
        .add(new WatchdogDolphin118Jesus("Watchdog Dolphin 1.18+", this))
        .setDefault("Vanilla");
    private final BooleanValue allowJump = new BooleanValue("Allow Jump", this, true);
    @EventLink
    public final Listener<JumpEvent> onJump = var1 -> {
        if (!this.allowJump.wo() && PlayerUtil.vl()) {
            var1.setCancelled();
        }
    };

    public Jesus() {
    }
}
