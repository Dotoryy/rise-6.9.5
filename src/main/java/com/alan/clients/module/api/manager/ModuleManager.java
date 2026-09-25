package com.alan.clients.module.api.manager;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.impl.combat.AntiBot;
import com.alan.clients.module.impl.combat.ComboOneHit;
import com.alan.clients.module.impl.combat.Criticals;
import com.alan.clients.module.impl.combat.Fences;
import com.alan.clients.module.impl.combat.KeepRange;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.combat.LagBreak;
import com.alan.clients.module.impl.combat.LegitReach;
import com.alan.clients.module.impl.combat.Piercing;
import com.alan.clients.module.impl.combat.Regen;
import com.alan.clients.module.impl.combat.TeleportAura;
import com.alan.clients.module.impl.combat.ThrowableAura;
import com.alan.clients.module.impl.combat.TickBase;
import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.module.impl.combat.WatchdogTPAura;
import com.alan.clients.module.impl.exploit.BlockTracker;
import com.alan.clients.module.impl.exploit.ConsoleSpammer;
import com.alan.clients.module.impl.exploit.Crasher;
import com.alan.clients.module.impl.exploit.Disabler;
import com.alan.clients.module.impl.exploit.GhostHand;
import com.alan.clients.module.impl.exploit.GodMode;
import com.alan.clients.module.impl.exploit.LightningTracker;
import com.alan.clients.module.impl.exploit.NoRotate;
import com.alan.clients.module.impl.exploit.PingSpoof;
import com.alan.clients.module.impl.exploit.StaffDetector;
import com.alan.clients.module.impl.ghost.AimAssist;
import com.alan.clients.module.impl.ghost.AimBacktrack;
import com.alan.clients.module.impl.ghost.AutoClicker;
import com.alan.clients.module.impl.ghost.ClickAssist;
import com.alan.clients.module.impl.ghost.FastPlace;
import com.alan.clients.module.impl.ghost.GuiClicker;
import com.alan.clients.module.impl.ghost.HitBox;
import com.alan.clients.module.impl.ghost.KeepSprint;
import com.alan.clients.module.impl.ghost.LegitScaffold;
import com.alan.clients.module.impl.ghost.ManualKBDisplacement;
import com.alan.clients.module.impl.ghost.NoClickDelay;
import com.alan.clients.module.impl.ghost.Reach;
import com.alan.clients.module.impl.ghost.SafeWalk;
import com.alan.clients.module.impl.ghost.WTap;
import com.alan.clients.module.impl.movement.AutoMLG;
import com.alan.clients.module.impl.movement.AutoStuck;
import com.alan.clients.module.impl.movement.Clipper;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.InventoryMove;
import com.alan.clients.module.impl.movement.Jesus;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.NoClip;
import com.alan.clients.module.impl.movement.NoJumpDelay;
import com.alan.clients.module.impl.movement.NoSlow;
import com.alan.clients.module.impl.movement.Phase;
import com.alan.clients.module.impl.movement.PotionExtender;
import com.alan.clients.module.impl.movement.ResourcePackSpoof;
import com.alan.clients.module.impl.movement.SnapTap;
import com.alan.clients.module.impl.movement.Sneak;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.Sprint;
import com.alan.clients.module.impl.movement.Step;
import com.alan.clients.module.impl.movement.Strafe;
import com.alan.clients.module.impl.movement.Stuck;
import com.alan.clients.module.impl.movement.TargetStrafe;
import com.alan.clients.module.impl.movement.Teleport;
import com.alan.clients.module.impl.movement.TerrainSpeed;
import com.alan.clients.module.impl.movement.WallClimb;
import com.alan.clients.module.impl.other.AntiAFK;
import com.alan.clients.module.impl.other.AntiCrash;
import com.alan.clients.module.impl.other.AutoGG;
import com.alan.clients.module.impl.other.AutoGroomer;
import com.alan.clients.module.impl.other.BedwarsUtils;
import com.alan.clients.module.impl.other.ChatBypass;
import com.alan.clients.module.impl.other.CheatDetector;
import com.alan.clients.module.impl.other.ClickSounds;
import com.alan.clients.module.impl.other.ClientSpoofer;
import com.alan.clients.module.impl.other.Debugger;
import com.alan.clients.module.impl.other.HypixelAutoPlay;
import com.alan.clients.module.impl.other.IRC;
import com.alan.clients.module.impl.other.Insults;
import com.alan.clients.module.impl.other.MurderMystery;
import com.alan.clients.module.impl.other.NoGuiClose;
import com.alan.clients.module.impl.other.Nuker;
import com.alan.clients.module.impl.other.PlayerNotifier;
import com.alan.clients.module.impl.other.SamplerDev;
import com.alan.clients.module.impl.other.Spammer;
import com.alan.clients.module.impl.other.Spotify;
import com.alan.clients.module.impl.other.Test;
import com.alan.clients.module.impl.other.Timer;
import com.alan.clients.module.impl.other.Translator;
import com.alan.clients.module.impl.player.AntiFireBall;
import com.alan.clients.module.impl.player.AntiSuffocate;
import com.alan.clients.module.impl.player.AntiVoid;
import com.alan.clients.module.impl.player.AutoHead;
import com.alan.clients.module.impl.player.AutoPot;
import com.alan.clients.module.impl.player.AutoTool;
import com.alan.clients.module.impl.player.Blink;
import com.alan.clients.module.impl.player.Breaker;
import com.alan.clients.module.impl.player.Clutch;
import com.alan.clients.module.impl.player.FastBreak;
import com.alan.clients.module.impl.player.FastUse;
import com.alan.clients.module.impl.player.HealthBypass;
import com.alan.clients.module.impl.player.InventorySync;
import com.alan.clients.module.impl.player.Manager;
import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.module.impl.player.OldManager;
import com.alan.clients.module.impl.player.PolarDetector;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.module.impl.player.Stealer;
import com.alan.clients.module.impl.player.Twerk;
import com.alan.clients.module.impl.player.WatchdogBlink;
import com.alan.clients.module.impl.render.Ambience;
import com.alan.clients.module.impl.render.Animations;
import com.alan.clients.module.impl.render.AppleSkin;
import com.alan.clients.module.impl.render.BPSCounter;
import com.alan.clients.module.impl.render.BedPlates;
import com.alan.clients.module.impl.render.BlackHoleOrbit;
import com.alan.clients.module.impl.render.BossBar;
import com.alan.clients.module.impl.render.BreadCrumbs;
import com.alan.clients.module.impl.render.CPSCounter;
import com.alan.clients.module.impl.render.Chat;
import com.alan.clients.module.impl.render.ChestESP;
import com.alan.clients.module.impl.render.ClickGUI;
import com.alan.clients.module.impl.render.ESP;
import com.alan.clients.module.impl.render.FPSCounter;
import com.alan.clients.module.impl.render.FreeCam;
import com.alan.clients.module.impl.render.FreeLook;
import com.alan.clients.module.impl.render.FullBright;
import com.alan.clients.module.impl.render.Glint;
import com.alan.clients.module.impl.render.HotBar;
import com.alan.clients.module.impl.render.HurtCamera;
import com.alan.clients.module.impl.render.HurtColor;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.module.impl.render.ItemPhysics;
import com.alan.clients.module.impl.render.JumpCircles;
import com.alan.clients.module.impl.render.KeyStrokes;
import com.alan.clients.module.impl.render.KillEffect;
import com.alan.clients.module.impl.render.M2DESP;
import com.alan.clients.module.impl.render.NameTags;
import com.alan.clients.module.impl.render.NoCameraClip;
import com.alan.clients.module.impl.render.OreESP;
import com.alan.clients.module.impl.render.Particles;
import com.alan.clients.module.impl.render.ScoreBoard;
import com.alan.clients.module.impl.render.SessionStats;
import com.alan.clients.module.impl.render.Streamer;
import com.alan.clients.module.impl.render.TargetInfo;
import com.alan.clients.module.impl.render.Tracers;
import com.alan.clients.module.impl.render.UnlimitedChat;
import com.alan.clients.module.impl.render.ViewBobbing;
import com.alan.clients.util.RegistryMap;
import java.util.ArrayList;
import java.util.Arrays;

public class ModuleManager {
    public RegistryMap<Class<Module>, Module> modules = new RegistryMap<>();

    public void remove(Module module) {
        this.modules.h(module);
        this.updateArraylistCache();
    }

    public boolean add(Module module) {
        this.modules.g(module);
        this.updateArraylistCache();
        return true;
    }

    static {
    }

    public ArrayList<Module> getAll() {
        return this.modules.rP();
    }

    public <T extends Module> T get(String var1) {
        return (T)this.getAll()
            .stream()
            .filter(
                var1x -> Arrays.stream(var1x.getAliases())
                    .anyMatch(var1xx -> var1xx.replace(" ", "").equalsIgnoreCase(var1.replace(" ", "")))
            )
            .findAny()
            .orElse(null);
    }

    public void updateArraylistCache() {
        Interface interfaceModule = this.c(Interface.class);
        if (interfaceModule != null) {
            interfaceModule.rebuildEntries();
        }
    }

    public <T extends Module> T c(Class<T> type) {
        return (T)this.modules.get(type);
    }


    public ModuleManager() {
    }

    public void init() {
        this.modules = new RegistryMap<>();
        this.register(AntiBot.class, new AntiBot());
        this.register(ComboOneHit.class, new ComboOneHit());
        this.register(Criticals.class, new Criticals());
        this.register(Fences.class, new Fences());
        this.register(KillAura.class, new KillAura());
        this.register(LagBreak.class, new LagBreak());
        this.register(LegitReach.class, new LegitReach());
        this.register(Piercing.class, new Piercing());
        this.register(Regen.class, new Regen());
        this.register(TeleportAura.class, new TeleportAura());
        this.register(WatchdogTPAura.class, new WatchdogTPAura());
        this.register(TickBase.class, new TickBase());
        this.register(ThrowableAura.class, new ThrowableAura());
        this.register(Velocity.class, new Velocity());
        this.register(WTap.class, new WTap());
        this.register(KeepRange.class, new KeepRange());
        this.register(BlockTracker.class, new BlockTracker());
        this.register(ConsoleSpammer.class, new ConsoleSpammer());
        this.register(Crasher.class, new Crasher());
        this.register(Disabler.class, new Disabler());
        this.register(GodMode.class, new GodMode());
        this.register(GhostHand.class, new GhostHand());
        this.register(LightningTracker.class, new LightningTracker());
        this.register(NoRotate.class, new NoRotate());
        this.register(PingSpoof.class, new PingSpoof());
        this.register(StaffDetector.class, new StaffDetector());
        this.register(AimAssist.class, new AimAssist());
        this.register(AimBacktrack.class, new AimBacktrack());
        this.register(AutoClicker.class, new AutoClicker());
        this.register(ClickAssist.class, new ClickAssist());
        this.register(LegitScaffold.class, new LegitScaffold());
        this.register(FastPlace.class, new FastPlace());
        this.register(GuiClicker.class, new GuiClicker());
        this.register(HitBox.class, new HitBox());
        this.register(KeepSprint.class, new KeepSprint());
        this.register(NoClickDelay.class, new NoClickDelay());
        this.register(Reach.class, new Reach());
        this.register(SafeWalk.class, new SafeWalk());
        this.register(ManualKBDisplacement.class, new ManualKBDisplacement());
        this.register(Flight.class, new Flight());
        this.register(InventoryMove.class, new InventoryMove());
        this.register(Jesus.class, new Jesus());
        this.register(LongJump.class, new LongJump());
        this.register(NoClip.class, new NoClip());
        this.register(NoSlow.class, new NoSlow());
        this.register(Phase.class, new Phase());
        this.register(PotionExtender.class, new PotionExtender());
        this.register(Sneak.class, new Sneak());
        this.register(SnapTap.class, new SnapTap());
        this.register(Speed.class, new Speed());
        this.register(Sprint.class, new Sprint());
        this.register(Step.class, new Step());
        this.register(Strafe.class, new Strafe());
        this.register(AutoStuck.class, new AutoStuck());
        this.register(Stuck.class, new Stuck());
        this.register(TargetStrafe.class, new TargetStrafe());
        this.register(Teleport.class, new Teleport());
        this.register(WallClimb.class, new WallClimb());
        this.register(TerrainSpeed.class, new TerrainSpeed());
        this.register(NoJumpDelay.class, new NoJumpDelay());
        this.register(Clipper.class, new Clipper());
        this.register(AutoMLG.class, new AutoMLG());
        this.register(ResourcePackSpoof.class, new ResourcePackSpoof());
        this.register(SamplerDev.class, new SamplerDev());
        this.register(AntiAFK.class, new AntiAFK());
        this.register(AutoGG.class, new AutoGG());
        this.register(AutoGroomer.class, new AutoGroomer());
        this.register(ClickSounds.class, new ClickSounds());
        this.register(ClientSpoofer.class, new ClientSpoofer());
        this.register(Debugger.class, new Debugger());
        this.register(HypixelAutoPlay.class, new HypixelAutoPlay());
        this.register(Insults.class, new Insults());
        this.register(MurderMystery.class, new MurderMystery());
        this.register(NoGuiClose.class, new NoGuiClose());
        this.register(Nuker.class, new Nuker());
        this.register(PlayerNotifier.class, new PlayerNotifier());
        this.register(AntiCrash.class, new AntiCrash());
        this.register(Spammer.class, new Spammer());
        this.register(Spotify.class, new Spotify());
        this.register(Test.class, new Test());
        this.register(Timer.class, new Timer());
        this.register(Translator.class, new Translator());
        this.register(BedwarsUtils.class, new BedwarsUtils());
        this.register(ChatBypass.class, new ChatBypass());
        this.register(AntiFireBall.class, new AntiFireBall());
        this.register(AntiSuffocate.class, new AntiSuffocate());
        this.register(AntiVoid.class, new AntiVoid());
        this.register(AutoHead.class, new AutoHead());
        this.register(AutoPot.class, new AutoPot());
        this.register(AutoTool.class, new AutoTool());
        this.register(Blink.class, new Blink());
        this.register(WatchdogBlink.class, new WatchdogBlink());
        this.register(Breaker.class, new Breaker());
        this.register(Clutch.class, new Clutch());
        this.register(FastBreak.class, new FastBreak());
        this.register(FastUse.class, new FastUse());
        this.register(NoFall.class, new NoFall());
        this.register(InventorySync.class, new InventorySync());
        this.register(Manager.class, new Manager());
        this.register(OldManager.class, new OldManager());
        this.register(NoFall.class, new NoFall());
        this.register(Scaffold.class, new Scaffold());
        this.register(Stealer.class, new Stealer());
        this.register(Twerk.class, new Twerk());
        this.register(PolarDetector.class, new PolarDetector());
        this.register(CheatDetector.class, new CheatDetector());
        this.register(HealthBypass.class, new HealthBypass());
        this.register(Ambience.class, new Ambience());
        this.register(Animations.class, new Animations());
        this.register(Chat.class, new Chat());
        this.register(AppleSkin.class, new AppleSkin());
        this.register(BedPlates.class, new BedPlates());
        this.register(BPSCounter.class, new BPSCounter());
        this.register(ChestESP.class, new ChestESP());
        this.register(ClickGUI.class, new ClickGUI());
        this.register(CPSCounter.class, new CPSCounter());
        this.register(FPSCounter.class, new FPSCounter());
        this.register(FreeCam.class, new FreeCam());
        this.register(FreeLook.class, new FreeLook());
        this.register(FullBright.class, new FullBright());
        this.register(Glint.class, new Glint());
        this.register(BossBar.class, new BossBar());
        this.register(HotBar.class, new HotBar());
        this.register(HurtCamera.class, new HurtCamera());
        this.register(HurtColor.class, new HurtColor());
        this.register(Interface.class, new Interface());
        this.register(ItemPhysics.class, new ItemPhysics());
        this.register(KeyStrokes.class, new KeyStrokes());
        this.register(KillEffect.class, new KillEffect());
        this.register(NameTags.class, new NameTags());
        this.register(NoCameraClip.class, new NoCameraClip());
        this.register(Particles.class, new Particles());
        this.register(M2DESP.class, new M2DESP());
        this.register(ESP.class, new ESP());
        this.register(ScoreBoard.class, new ScoreBoard());
        this.register(Streamer.class, new Streamer());
        this.register(TargetInfo.class, new TargetInfo());
        this.register(Tracers.class, new Tracers());
        this.register(UnlimitedChat.class, new UnlimitedChat());
        this.register(ViewBobbing.class, new ViewBobbing());
        this.register(IRC.class, new IRC());
        this.register(SessionStats.class, new SessionStats());
        this.register(BreadCrumbs.class, new BreadCrumbs());
        this.register(JumpCircles.class, new JumpCircles());
        this.register(BlackHoleOrbit.class, new BlackHoleOrbit());
        this.register(OreESP.class, new OreESP());
        this.getAll().stream().filter(var0 -> var0.getModuleInfo().autoEnabled()).forEach(var0 -> var0.setEnabled(true));
        Client.a.e().b(this);
    }

    public void register(Class type, Module module) {
        this.modules.put(type, module);
    }
}
