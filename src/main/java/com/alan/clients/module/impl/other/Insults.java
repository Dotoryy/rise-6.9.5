package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.RandomUtils;

@ModuleInfo(aliases = "module.other.insults.name", description = "module.other.insults.description", category = Category.PLAYER)
public final class Insults extends Module {
    public final ModeValue mode = new ModeValue("Mode", this)
        .add(new SubMode("Default"))
        .add(new SubMode("Watchdog"))
        .add(new SubMode("WhatsApp"))
        .add(new SubMode("CSGO"))
        .add(new SubMode("NerdyAss"))
        .setDefault("Default");
    public final Map<String, List<String>> map = new HashMap<>();
    private final StringValue prefix = new StringValue("Prefix", this, "");
    private final NumberValue delay = new NumberValue("Delay", this, 0, 0, 50, 1);
    private final BooleanValue randomizer = new BooleanValue("Randomizer", this, false);
    private final String[] defaultInsults = new String[]{
        "Wow! My combo is Rise'n!",
        "Why would someone as bad as you not use Rise 6.0?",
        "Here's your ticket to spectator from Rise 6.0!",
        "I see you're a pay to lose player, huh?",
        "Do you need some PvP advice? Well Rise 6.0 is all you need.",
        "Hey! Wise up, don't waste another day without Rise.",
        "You didn't even stand a chance against Rise.",
        "We regret to inform you that your free trial of life has unfortunately expired.",
        "RISE against other cheaters by getting Rise!",
        "You can pay for that loss by getting Rise.",
        "Remember to use hand sanitizer to get rid of bacteria like you!",
        "Hey, try not to drown in your own salt.",
        "Having problems with forgetting to left click? Rise 6.0 can fix it!",
        "Come on, is that all you have against Rise 6.0?",
        "Rise up today by getting Rise 6.0!",
        "Get Rise, you need it.",
        "how about you rise up to heaven by ending it",
        "Did you know Watchdog has banned 6346 players in the last 7 days."
    };
    private final String[] whatsAppInsults = new String[]{"Add me on WhatsApp "};
    private final String nerdyAssInsult = "LOL %s GOT SNIPED BY NERDYASS ON YOUTUBE";
    private final String[] csgoInsults = new String[]{
        "Missed %s due to correction",
        "Missed %s due to spread",
        "Missed %s due to prediction error",
        "Missed %s due to invalid backtrack",
        "Missed %s due to ?",
        "Shot at head, and missed head, but hit anyways because of spread (lol)",
        "Missed %s due to resolver"
    };
    private EntityPlayer target;
    private int ticks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (this.target != null && !aEg.theWorld.playerEntities.contains(this.target)) {
            if (this.ticks >= this.delay.wo().intValue() + Math.random() * 2.0 && !BadPacketsComponent.aW()) {
                String s;
                label65: {
                    label64: {
                        label63: {
                            label62: {
                                {
                                    s = "";
                                    String s1 = this.mode.wo().getName();
                                    switch (s1) {
                                        case "Default":
                                            s = this.defaultInsults[RandomUtils.nextInt(0, this.defaultInsults.length)];
                                            break label65;
                                        case "Watchdog":
                                            break label64;
                                        case "WhatsApp":
                                            break label63;
                                        case "CSGO":
                                            break label62;
                                        case "NerdyAss":
                                            break;
                                        default:
                                            break label65;
                                    }
                                }

                                s = "LOL %s GOT SNIPED BY NERDYASS ON YOUTUBE";
                                break label65;
                            }

                            s = this.csgoInsults[RandomUtils.nextInt(0, this.csgoInsults.length)];
                            break label65;
                        }

                        s = this.whatsAppInsults[RandomUtils.nextInt(0, this.whatsAppInsults.length)];
                        break label65;
                    }

                    s = "[STAFF] [WATCHDOG] %s reeled in.";
                }

                String s2 = String.format(s, PlayerUtil.g(this.target));
                if (!this.prefix.wo().isEmpty()) {
                    s2 = this.prefix.wo() + " " + s2;
                }

                String s3 = new Random().ints(97, 123).limit(10L).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
                if (this.randomizer.wo()) {
                    aEg.thePlayer.sendChatMessage(s2 + " " + s3);
                } else {
                    aEg.thePlayer.sendChatMessage(s2);
                }

                this.target = null;
            }

            this.ticks++;
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1 -> {
        EntityLivingBase entitylivingbase = var1.getLiving();
        if (entitylivingbase instanceof EntityPlayer) {
            this.target = (EntityPlayer)entitylivingbase;
            this.ticks = 0;
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> {
        this.target = null;
        this.ticks = 0;
    };

    public Insults() {
    }
}
