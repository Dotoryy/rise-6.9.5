package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.value.impl.ColorValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.module.impl.render.WeatherModeValue;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;

@ModuleInfo(aliases = "module.render.ambience.name", description = "module.render.ambience.description", category = Category.RENDER)
public final class Ambience extends Module {
    private final NumberValue time = new NumberValue("Time", this, 0, 0, 22999, 1);
    private final NumberValue speed = new NumberValue("Time Speed", this, 0, 0, 20, 1);
    private final ModeValue weather = new WeatherModeValue(this, "Weather", this);
    public final ColorValue snowColor = new ColorValue(
        "Snow Color", this, Color.WHITE, () -> !this.weather.wo().getName().equals("Heavy Snow") && !this.weather.wo().getName().equals("Light Snow")
    );
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> aEg.theWorld
        .setWorldTime(this.time.wo().intValue() + System.currentTimeMillis() * this.speed.wo().intValue());
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (aEg.thePlayer.ticksExisted % 20 == 0) {
            {
                String s = this.weather.wo().getName();
                switch (s) {
                    case "Clear":
                        aEg.theWorld.setRainStrength(0.0F);
                        aEg.theWorld.getWorldInfo().setCleanWeatherTime(Integer.MAX_VALUE);
                        aEg.theWorld.getWorldInfo().setRainTime(0);
                        aEg.theWorld.getWorldInfo().setThunderTime(0);
                        aEg.theWorld.getWorldInfo().setRaining(false);
                        aEg.theWorld.getWorldInfo().setThundering(false);
                        return;
                    case "Heavy Snow":
                    case "Rain":
                    case "Nether Particles":
                    case "Light Snow":
                        break;
                    default:
                        return;
                }
            }

            aEg.theWorld.setRainStrength(1.0F);
            aEg.theWorld.getWorldInfo().setCleanWeatherTime(0);
            aEg.theWorld.getWorldInfo().setRainTime(Integer.MAX_VALUE);
            aEg.theWorld.getWorldInfo().setThunderTime(Integer.MAX_VALUE);
            aEg.theWorld.getWorldInfo().setRaining(true);
            aEg.theWorld.getWorldInfo().setThundering(false);
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1 -> {
        if (var1.getPacket() instanceof S03PacketTimeUpdate) {
            var1.setCancelled();
        } else if (var1.getPacket() instanceof S2BPacketChangeGameState && !this.weather.wo().getName().equals("Unchanged")) {
            S2BPacketChangeGameState s2bpacketchangegamestate = (S2BPacketChangeGameState)var1.getPacket();
            if (s2bpacketchangegamestate.getGameState() == 1 || s2bpacketchangegamestate.getGameState() == 2) {
                var1.setCancelled();
            }
        }
    };

    public Ambience() {
    }

    @Override
    public void onDisable() {
        aEg.theWorld.setRainStrength(0.0F);
        aEg.theWorld.getWorldInfo().setCleanWeatherTime(Integer.MAX_VALUE);
        aEg.theWorld.getWorldInfo().setRainTime(0);
        aEg.theWorld.getWorldInfo().setThunderTime(0);
        aEg.theWorld.getWorldInfo().setRaining(false);
        aEg.theWorld.getWorldInfo().setThundering(false);
    }

    public float getFloatTemperature(BlockPos pos, BiomeGenBase biomeGenBase) {
        if (this.isEnabled()) {
            String s = this.weather.wo().getName();
            switch (s) {
                case "Nether Particles":
                case "Heavy Snow":
                case "Light Snow":
                    return 0.1F;
                case "Rain":
                    return 0.2F;
            }
        }

        return biomeGenBase.getFloatTemperature(pos);
    }

    public boolean kF() {
        String s = this.weather.wo().getName();
        return this.isEnabled() && s.equals("Light Snow") || s.equals("Heavy Snow") || s.equals("Nether Particles");
    }

    @Generated
    public NumberValue getTime() {
        return this.time;
    }

    @Generated
    public NumberValue getSpeed() {
        return this.speed;
    }

    @Generated
    public ModeValue kH() {
        return this.weather;
    }

    @Generated
    public ColorValue getSnowColor() {
        return this.snowColor;
    }

    @Generated
    public Listener<Render3DEvent> getOnRender3D() {
        return this.onRender3D;
    }

    @Generated
    public Listener<PreMotionEvent> getOnPreMotionEvent() {
        return this.onPreMotionEvent;
    }

    @Generated
    public Listener<PacketReceiveEvent> getOnPacketReceiveEvent() {
        return this.onPacketReceiveEvent;
    }
}
