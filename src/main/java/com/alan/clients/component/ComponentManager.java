package com.alan.clients.component;

import com.alan.clients.Client;
import com.alan.clients.component.impl.event.EntityKillEventComponent;
import com.alan.clients.component.impl.hud.DragComponent;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.component.impl.player.PingSpoofComponent;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.render.ParticleComponent;
import com.alan.clients.component.impl.render.ProjectionComponent;
import com.alan.clients.component.impl.viamcp.BlockPlacementFixComponent;
import com.alan.clients.component.impl.viamcp.HitboxFixComponent;
import com.alan.clients.component.impl.viamcp.MinimumMotionFixComponent;
import com.alan.clients.component.impl.event.EntityTickComponent;
import com.alan.clients.component.impl.event.MouseButtonComponent;
import com.alan.clients.component.impl.hud.AdaptiveRefreshRateComponent;
import com.alan.clients.component.impl.hypixel.LimboComponent;
import com.alan.clients.component.impl.hypixel.APIKeyComponent;
import com.alan.clients.component.impl.packetlog.PacketLogComponent;
import com.alan.clients.component.impl.patches.GuiClosePatchComponent;
import com.alan.clients.component.impl.performance.ParticleDistanceComponent;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.component.impl.player.PacketQueueComponent;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import com.alan.clients.component.impl.player.GUIDetectionComponent;
import com.alan.clients.component.impl.community.UserLookupComponent;
import com.alan.clients.component.impl.player.ItemDamageComponent;
import com.alan.clients.component.impl.player.FlyingBlinkComponent;
import com.alan.clients.component.impl.player.PacketlessDamageComponent;
import com.alan.clients.component.impl.monitoring.ReplayRecordingComponent;
import com.alan.clients.component.impl.monitoring.ScreenCaptureComponent;
import com.alan.clients.component.impl.player.SelectorDetectionComponent;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.component.impl.player.WatchdogJumpComponent;
import com.alan.clients.component.impl.render.ESPComponent;
import com.alan.clients.component.impl.render.NotificationComponent;
import com.alan.clients.component.impl.render.ProgressBarComponent;
import com.alan.clients.component.impl.render.SmoothCameraComponent;
import com.alan.clients.component.impl.monitoring.MonitoringComponent;
import com.alan.clients.component.impl.viamcp.ProtocolVersionComponent;
import com.alan.clients.component.impl.viamcp.FlyingPacketFixComponent;
import com.alan.clients.component.impl.viamcp.ProtocolFixComponent;
import com.alan.clients.component.impl.viamcp.LadderFixComponent;
import com.alan.clients.component.impl.player.LocalhostInputComponent;
import com.alan.clients.component.impl.viamcp.SpeedPotionFixComponent;
import com.alan.clients.component.impl.viamcp.TransactionFixComponent;
import java.util.HashMap;
import java.util.Map;

public class ComponentManager {
    public Map<Class<Component>, Component> bx = new HashMap<>();

    public ComponentManager() {
    }

    public void init() {
        this.a(new EntityKillEventComponent());
        this.a(new EntityTickComponent());
        this.a(new AdaptiveRefreshRateComponent());
        this.a(new DragComponent());
        this.a(new APIKeyComponent());
        this.a(new LimboComponent());
        this.a(new PacketLogComponent());
        this.a(new GuiClosePatchComponent());
        this.a(new ParticleDistanceComponent());
        this.a(new BadPacketsComponent());
        this.a(new PacketQueueComponent());
        this.a(new GUIDetectionComponent());
        this.a(new ItemDamageComponent());
        this.a(new LastConnectionComponent());
        this.a(new FlyingBlinkComponent());
        this.a(new PingSpoofComponent());
        this.a(new BlinkComponent());
        this.a(new RotationComponent());
        this.a(new SelectorDetectionComponent());
        this.a(new SlotComponent());
        this.a(new ESPComponent());
        this.a(new NotificationComponent());
        this.a(new UserLookupComponent());
        this.a(new ParticleComponent());
        this.a(new ProgressBarComponent());
        this.a(new ProjectionComponent());
        this.a(new SmoothCameraComponent());
        this.a(new BlockPlacementFixComponent());
        this.a(new FlyingPacketFixComponent());
        this.a(new HitboxFixComponent());
        this.a(new LadderFixComponent());
        this.a(new MinimumMotionFixComponent());
        this.a(new FallDistanceComponent());
        this.a(new TargetComponent());
        this.a(new MouseButtonComponent());
        this.a(new PacketlessDamageComponent());
        this.a(new ProtocolVersionComponent());
        this.a(new TransactionFixComponent());
        this.a(new SpeedPotionFixComponent());
        this.a(new LocalhostInputComponent());
        this.a(new WatchdogJumpComponent());
        this.a(new ProtocolFixComponent());
        this.a(new ReplayRecordingComponent());
        this.a(new ScreenCaptureComponent());
        this.a(new MonitoringComponent());
        this.bx.forEach((var0, var1) -> Client.a.e().b(var1));
        this.bx.forEach((var0, var1) -> var1.aT());
    }

    public void a(Component component) {
        this.bx.put((Class<Component>)component.getClass(), component);
    }

    public <T extends Component> T b(Class<T> type) {
        return (T)this.bx.get(type);
    }
}
