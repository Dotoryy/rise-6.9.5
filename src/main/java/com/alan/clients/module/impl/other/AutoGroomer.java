package com.alan.clients.module.impl.other;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.impl.BooleanValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;

@ModuleInfo(aliases = "module.other.autogroomer.name", description = "Helps you groom people.", category = Category.PLAYER)
public final class AutoGroomer extends Module {
    private final Random random = new Random();
    private final char[] chars = new char[]{'⛍', '⛌', '⛗', '⛗', '⛟'};
    private final List<Integer> ids = new ArrayList<>();
    private final List<String> messages = Arrays.asList(
        "can I have some tittie pics?",
        "do you wanna be above or below?",
        "I am gonna be pounding you 24/7",
        "I am gonna send you something okay? no sharing :wink:",
        "I am fine below or above",
        "you are gonna be riding this dick all night",
        "oh I am creaming just looking at you",
        "I want to make you cum.",
        "my balls are gonna be dry tonight thanks to you",
        "I am gonna relieve you all night",
        "daddy is ready.",
        "you will be screaming my name tonight",
        "fly up here and you can have as much as you want",
        "lick it off like that, until I ram your mouth.",
        "daddy wants your mouth on all of this tonight",
        "I bet you like daddy pounding you so hard that your knees give out and drag your face forward as I literally pound you flat into the bed.",
        "I'm so hard rn",
        "I'm dripping",
        "Just wait until we get home",
        "I want to taste you",
        "I love how hard you can make me come",
        "Get your ass on that bed.",
        "Daddy's gonna eat that pussy tonight.",
        "Daddy's all bricked up now."
    );
    private final BooleanValue chatBypass = new BooleanValue("Chat Bypass", this, false);
    private final BooleanValue dm = new BooleanValue("DM Messages", this, false);
    private final BooleanValue spam = new BooleanValue("Spam", this, false);
    @EventLink
    private final Listener<PreMotionEvent> pre = var1 -> {
        if (!this.dm.wo() && aEg.thePlayer.ticksExisted % 69 == 0) {
            String s = this.messages.get(this.random.nextInt(this.messages.size()));
            if (this.chatBypass.wo()) {
                StringBuilder stringbuilder = new StringBuilder(s.length() * 2);

                for (int i = 0; i < s.length(); i++) {
                    char c0 = s.charAt(i);
                    char c1 = this.chars[this.random.nextInt(this.chars.length)];
                    stringbuilder.append(c0).append(c1);
                }

                s = stringbuilder.toString();
            }

            aEg.thePlayer.sendChatMessage(s);
        } else if (!this.spam.wo() || aEg.thePlayer.ticksExisted % 20 == 0) {
            for (EntityPlayer entityplayer : aEg.theWorld.playerEntities) {
                if (entityplayer != aEg.thePlayer
                    && !entityplayer.isInvisible()
                    && !Client.a.getBotManager().a(entityplayer)
                    && entityplayer.getDistanceSqToEntity(aEg.thePlayer) < 64.0
                    && (!this.ids.contains(entityplayer.getEntityId()) || this.spam.wo())) {
                    String s1 = this.messages.get(this.random.nextInt(this.messages.size()));
                    if (this.chatBypass.wo()) {
                        StringBuilder stringbuilder1 = new StringBuilder(s1.length() * 2);

                        for (int j = 0; j < s1.length(); j++) {
                            char c2 = s1.charAt(j);
                            char c3 = this.chars[this.random.nextInt(this.chars.length)];
                            stringbuilder1.append(c2).append(c3);
                        }

                        s1 = stringbuilder1.toString();
                    }

                    aEg.getNetHandler().addToSendQueue(new C01PacketChatMessage("/msg " + entityplayer.getName() + " " + s1));
                    this.ids.add(entityplayer.getEntityId());
                }
            }
        }
    };
    @EventLink
    private final Listener<WorldChangeEvent> worldChange = var1 -> this.ids.clear();

    public AutoGroomer() {
    }

    @Override
    public void onDisable() {
        this.ids.clear();
    }

    @Override
    public void onEnable() {
        this.ids.clear();
    }
}
