package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.player.nofall.ChunkLoadNoFall;
import com.alan.clients.module.impl.player.nofall.ClutchNoFall;
import com.alan.clients.module.impl.player.nofall.FlagNoFall;
import com.alan.clients.module.impl.player.nofall.GrimServer19NoFall;
import com.alan.clients.module.impl.player.nofall.HeypixelNoFall;
import com.alan.clients.module.impl.player.nofall.MatrixNoFall;
import com.alan.clients.module.impl.player.nofall.NoGroundNoFall;
import com.alan.clients.module.impl.player.nofall.PacketNoFall;
import com.alan.clients.module.impl.player.nofall.RoundNoFall;
import com.alan.clients.module.impl.player.nofall.SpoofNoFall;
import com.alan.clients.module.impl.player.nofall.VulcanNoFall;
import com.alan.clients.module.impl.player.nofall.WatchdogBlinkNoFall;
import com.alan.clients.module.impl.player.nofall.WatchdogPacketNoFall;
import com.alan.clients.module.impl.player.nofall.WatchdogPredictionNoFall;
import com.alan.clients.value.impl.ModeValue;

@ModuleInfo(aliases = "module.player.nofall.name", description = "module.player.nofall.description", category = Category.PLAYER)
public class NoFall extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new SpoofNoFall("Spoof", this))
        .add(new FlagNoFall("Flag", this))
        .add(new NoGroundNoFall("No Ground", this))
        .add(new RoundNoFall("Round", this))
        .add(new VulcanNoFall("Vulcan", this))
        .add(new PacketNoFall("Packet", this))
        .add(new GrimServer19NoFall("Grim Server 1.9+", this))
        .add(new ChunkLoadNoFall("Chunk Load", this))
        .add(new HeypixelNoFall("Heypixel", this))
        .add(new ClutchNoFall("Clutch", this))
        .add(new MatrixNoFall("Matrix", this))
        .add(new WatchdogPredictionNoFall("Watchdog Prediction", this))
        .add(new WatchdogBlinkNoFall("Watchdog Blink", this))
        .add(new WatchdogPacketNoFall("Watchdog Packet", this))
        .setDefault("Spoof");

    public NoFall() {
    }
}
