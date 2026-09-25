package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.player.ServerUtil;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.other.spammer.name", description = "module.other.spammer.description", category = Category.PLAYER)
public final class Spammer extends Module {
    private final StringValue message = new StringValue("Message", this, "Buy Rise at riseclient.com!");
    private final NumberValue delay = new NumberValue("Delay", this, 3000, 0, 20000, 1);
    private final StopWatch stopWatch = new StopWatch();
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (ServerUtil.cg("loyisa.cn") && this.message.wo().startsWith("/")) {
            ChatUtil.b("Upon a request from Loyisa we have blacklisted Loyisa's Test Server from Spammer.");
            this.toggle();
        } else {
            if (this.stopWatch.T(this.delay.wo().longValue())) {
                if (this.message.wo().startsWith("#")) {
                    ChatUtil.b("Spammer message cannot contain #. You're not spamming IRC Skid.");
                    return;
                }

                aEg.thePlayer.sendChatMessage(this.message.wo());
                this.stopWatch.aX();
            }
        }
    };

    public Spammer() {
    }
}
