package com.alan.clients.module.impl.movement;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.movement.flight.AirWalkFlight;
import com.alan.clients.module.impl.movement.flight.BlockFlight;
import com.alan.clients.module.impl.movement.flight.BloxdFlight;
import com.alan.clients.module.impl.movement.flight.CubeCraftFlight;
import com.alan.clients.module.impl.movement.flight.DamageDeprecatedFlight;
import com.alan.clients.module.impl.movement.flight.FuncraftFlight;
import com.alan.clients.module.impl.movement.flight.Grim191181Flight;
import com.alan.clients.module.impl.movement.flight.Grim2Flight;
import com.alan.clients.module.impl.movement.flight.Grim3Flight;
import com.alan.clients.module.impl.movement.flight.LatestNCPFlight;
import com.alan.clients.module.impl.movement.flight.MMCFireballFlight;
import com.alan.clients.module.impl.movement.flight.MMCFlight;
import com.alan.clients.module.impl.movement.flight.MatrixDamageFlight;
import com.alan.clients.module.impl.movement.flight.MatrixFlight;
import com.alan.clients.module.impl.movement.flight.MatrixHighJumpFlight;
import com.alan.clients.module.impl.movement.flight.MineLandFlight;
import com.alan.clients.module.impl.movement.flight.MiniBloxFlight;
import com.alan.clients.module.impl.movement.flight.OldNCPFlight;
import com.alan.clients.module.impl.movement.flight.SkyCaveFlight;
import com.alan.clients.module.impl.movement.flight.VanillaFlight;
import com.alan.clients.module.impl.movement.flight.VerusDamageNewFlight;
import com.alan.clients.module.impl.movement.flight.VerusFlight;
import com.alan.clients.module.impl.movement.flight.Vulcan2Flight;
import com.alan.clients.module.impl.movement.flight.VulcanDamageFlight;
import com.alan.clients.module.impl.movement.flight.VulcanDeprecatedFlight;
import com.alan.clients.module.impl.movement.flight.Watchdog2Flight;
import com.alan.clients.module.impl.movement.flight.WatchdogFlight;
import com.alan.clients.module.impl.movement.flight.WatchdogPredictionFlight;
import com.alan.clients.module.impl.movement.flight.ZoneCraftFlight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.render.SmoothCameraComponent;
import com.alan.clients.module.impl.movement.flight.AirJumpFlight;
import com.alan.clients.module.impl.movement.flight.BufferAbuseFlight;
import com.alan.clients.module.impl.movement.flight.SlimeNCPFlight;
import javax.vecmath.Vector3d;
import net.minecraft.entity.boss.EntityDragon;

@ModuleInfo(aliases = {"module.movement.flight.name", "Fly"}, description = "module.movement.flight.description", category = Category.MOVEMENT)
public class Flight extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new VanillaFlight("Vanilla", this))
        .add(new MatrixFlight("Matrix", this))
        .add(new MatrixHighJumpFlight("Matrix High Jump", this))
        .add(new MatrixDamageFlight("Matrix Damage", this))
        .add(new Grim191181Flight("Grim 1.9-1.18.1", this))
        .add(new Grim2Flight("Grim 2", this))
        .add(new Grim3Flight("Grim 3", this))
        .add(new WatchdogPredictionFlight("Watchdog Prediction", this))
        .add(new SlimeNCPFlight("Watchdog Prediction 2", this))
        .add(new Watchdog2Flight("Watchdog 2", this))
        .add(new MiniBloxFlight("MiniBlox", this))
        .add(new Vulcan2Flight("Vulcan 2", this))
        .add(new AirWalkFlight("Air Walk", this))
        .add(new OldNCPFlight("Old NCP", this))
        .add(new FuncraftFlight("Funcraft", this))
        .add(new SkyCaveFlight("SkyCave", this))
        .add(new LatestNCPFlight("Latest NCP", this))
        .add(new VerusFlight("Verus", this))
        .add(new BlockFlight("Block", this))
        .add(new MMCFlight("MMC", this))
        .add(new BufferAbuseFlight("Buffer Abuse", this))
        .add(new ZoneCraftFlight("Zone Craft", this))
        .add(new SlimeNCPFlight("Slime NCP", this))
        .add(new AirJumpFlight("Air Jump", this))
        .add(new CubeCraftFlight("CubeCraft", this))
        .add(new MineLandFlight("MineLand", this))
        .add(new VulcanDeprecatedFlight("Vulcan (Deprecated)", this))
        .add(new BloxdFlight("Bloxd", this))
        .add(new VerusDamageNewFlight("Verus Damage New", this))
        .add(new WatchdogFlight("Watchdog", this))
        .add(new DamageDeprecatedFlight("Damage (Deprecated)", this))
        .add(new MMCFireballFlight("MMC Fireball", this))
        .add(new VulcanDamageFlight("Vulcan Damage", this))
        .add(new AirJumpFlight("Spartan (Deprecated)", this))
        .add(new BufferAbuseFlight("Vicnix (Deprecated)", this))
        .setDefault("Vanilla");
    private final BooleanValue disableOnTeleport = new BooleanValue("Disable on Teleport", this, false);
    private final BooleanValue viewBobbing = new BooleanValue("View Bobbing", this, false);
    private final BooleanValue fakeDamage = new BooleanValue("Fake Damage", this, false);
    private final BooleanValue smoothCamera = new BooleanValue("Smooth Camera", this, false);
    private final BooleanValue dragon = new BooleanValue("Visual Dragon", this, false);
    private EntityDragon entityDragon;
    private boolean teleported;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (this.viewBobbing.wo()) {
            aEg.thePlayer.cameraYaw = 0.1F;
        }

        if (this.smoothCamera.wo()) {
            SmoothCameraComponent.cn();
        }
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> {
        if (this.dragon.wo()) {
            if (this.entityDragon == null) {
                this.entityDragon = new EntityDragon(aEg.theWorld);
                aEg.theWorld.addEntityToWorld(-1, this.entityDragon);
                Client.a.getBotManager().b(this, this.entityDragon);
            }

            Vector3d vector3d = new Vector3d(
                aEg.thePlayer.lastTickPosX + (aEg.thePlayer.posX - aEg.thePlayer.lastTickPosX) * aEg.timer.bWm,
                aEg.thePlayer.lastTickPosY + (aEg.thePlayer.posY - aEg.thePlayer.lastTickPosY) * aEg.timer.bWm,
                aEg.thePlayer.lastTickPosZ + (aEg.thePlayer.posZ - aEg.thePlayer.lastTickPosZ) * aEg.timer.bWm
            );
            this.entityDragon.setPositionAndRotation(vector3d.x, vector3d.y - 3.0, vector3d.z, aEg.thePlayer.pl - 180.0F, aEg.thePlayer.rotationPitch);
        }
    };
    @EventLink
    public final Listener<AttackEvent> onAttack = var1 -> {
        if (var1.getLiving() == this.entityDragon) {
            var1.setCancelled();
        }
    };
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var1 -> {
        if (this.disableOnTeleport.wo()) {
            this.toggle();
        }
    };

    public Flight() {
    }

    @Override
    public void onEnable() {
        if (this.fakeDamage.wo() && aEg.thePlayer.ticksExisted > 1) {
            PlayerUtil.fakeDamage();
        }

        this.teleported = false;
    }

    @Override
    public void onDisable() {
        if (this.entityDragon != null) {
            aEg.theWorld.removeEntity(this.entityDragon);
            this.entityDragon = null;
        }

        aEg.timer.dzD = 1.0F;
    }
}
