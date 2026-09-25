package com.alan.clients.module.impl.combat.antibot;

import com.alan.clients.Client;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.bots.BotManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class MiddleClickBotAntiBot extends Mode<AntiBot> {
    private boolean clicked;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (Mouse.isButtonDown(2) || Keyboard.isKeyDown(29) && aEg.gameSettings.cgK.isKeyDown()) {
            if (this.clicked) {
                return;
            }

            this.clicked = true;
            if (aEg.objectMouseOver.typeOfHit == MovingObjectType.ENTITY) {
                BotManager r = Client.a.getBotManager();
                Entity entity = aEg.objectMouseOver.entityHit;
                if (r.a(this, entity)) {
                    Client.a.getBotManager().c(this, entity);
                } else {
                    Client.a.getBotManager().b(this, entity);
                }
            }
        } else {
            this.clicked = false;
        }
    };

    public MiddleClickBotAntiBot(String var1, AntiBot antiBot) {
        super(var1, antiBot);
    }

    @Override
    public void onDisable() {
        Client.a.getBotManager().a(this);
    }
}
