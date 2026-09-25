package com.alan.clients.module.impl.combat.mimic;

import com.alan.clients.module.impl.combat.Mimic;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.m;
import net.minecraft.util.Tuple;

public class ClickerMimic extends Mode<Mimic> {
    private long nextClickTime;
    private HashMap<String, Tuple<ArrayList<Integer>, Integer>> recordedDelays = new HashMap<>();
    @EventLink(value = 0)
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        Packet packet = var1x.getPacket();
        if (packet instanceof m) {
            int i = ((m)packet).getEntityID();
            if (i == aEg.thePlayer.getEntityId()) {
                return;
            }

            String s = aEg.theWorld.getEntityByID(i).getName();
            int j = aEg.thePlayer.ticksExisted;
            if (!this.recordedDelays.containsKey(s)) {
                this.recordedDelays.put(s, new Tuple<>(new ArrayList<>(), j));
            }

            Tuple tuple = this.recordedDelays.get(s);
            ChatUtil.b("Recorded " + (j - (Integer)tuple.getSecond()) + " von " + s);
            ((ArrayList)tuple.getFirst()).add(j - (Integer)tuple.getSecond());
            tuple.k(j);
        }
    };
    @EventLink(value = 0)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (aEg.gameSettings.cgK.isKeyDown()) {
            if (aEg.gameSettings.keyBindSneak.isKeyDown()) {
                this.recordedDelays = new HashMap<>();
            }

            if (System.currentTimeMillis() > this.nextClickTime) {
                long i = 0L;

                while (i == 0L || i >= 450L) {
                    if (i != 0L) {
                        ChatUtil.b("Running again prev " + i);
                    }

                    Optional optional = this.recordedDelays.keySet().stream().findFirst();
                    if (!optional.isPresent()) {
                        ChatUtil.b("Empty");
                        return;
                    }

                    Tuple tuple = this.recordedDelays.get(optional.get());
                    if (((ArrayList)tuple.getFirst()).isEmpty()) {
                        this.recordedDelays.remove(optional.get());
                        return;
                    }

                    i = (Integer)((ArrayList)tuple.getFirst()).get(0) * 50;
                    ((ArrayList)tuple.getFirst()).remove(0);
                    ChatUtil.b("Running " + (String)optional.get() + " " + i);
                }

                this.nextClickTime = System.currentTimeMillis() + i / 2L;
                aEg.Ay();
                aEg.leftClickCounter = 1;
            }
        }
    };

    public ClickerMimic(String var1, Mimic mimic) {
        super(var1, mimic);
    }
}
