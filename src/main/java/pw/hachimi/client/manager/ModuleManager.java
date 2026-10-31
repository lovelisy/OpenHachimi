package pw.hachimi.client.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.module.Armor;
import pw.hachimi.client.module.Chests;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.Coords;
import pw.hachimi.client.module.Crosshair;
import pw.hachimi.client.module.Direction;
import pw.hachimi.client.module.DrawStackRenderModule;
import pw.hachimi.client.module.Durability;
import pw.hachimi.client.module.FPS;
import pw.hachimi.client.module.HUDLegacy;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.module.Items;
import pw.hachimi.client.module.ModuleBase;
import pw.hachimi.client.module.ModuleManagerColorHelper;
import pw.hachimi.client.module.ModuleManagerExplosionS2CPacketPlayerPositionLookS2CPacketHelper;
import pw.hachimi.client.module.ModuleManagerScreenHelper;
import pw.hachimi.client.module.Modules;
import pw.hachimi.client.module.Notification;
import pw.hachimi.client.module.Ping;
import pw.hachimi.client.module.ServerBrand;
import pw.hachimi.client.module.Speeds;
import pw.hachimi.client.module.StackPotionContentsComponentManager;
import pw.hachimi.client.module.TPS;
import pw.hachimi.client.module.TextRadar;
import pw.hachimi.client.module.Watermark;
import pw.hachimi.client.module.client.Font;
import pw.hachimi.client.module.client.HUD;
import pw.hachimi.client.module.client.PlayerModule;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.module.combat.AntiCrawl;
import pw.hachimi.client.module.combat.AntiPhase;
import pw.hachimi.client.module.combat.AntiRegear;
import pw.hachimi.client.module.combat.AntiWeakness;
import pw.hachimi.client.module.combat.ArmorAlert;
import pw.hachimi.client.module.combat.Aura;
import pw.hachimi.client.module.combat.AutoAnchor;
import pw.hachimi.client.module.combat.AutoCrystal;
import pw.hachimi.client.module.combat.AutoLog;
import pw.hachimi.client.module.combat.AutoMine;
import pw.hachimi.client.module.combat.AutoPotion;
import pw.hachimi.client.module.combat.AutoRelease;
import pw.hachimi.client.module.combat.AutoSpear;
import pw.hachimi.client.module.combat.AutoTrap;
import pw.hachimi.client.module.combat.AutoWeb;
import pw.hachimi.client.module.combat.AutoXP;
import pw.hachimi.client.module.combat.Blocker;
import pw.hachimi.client.module.combat.CrawlTrap;
import pw.hachimi.client.module.combat.Criticals;
import pw.hachimi.client.module.combat.Flatten;
import pw.hachimi.client.module.combat.MainHand;
import pw.hachimi.client.module.combat.NoHitDelay;
import pw.hachimi.client.module.combat.Offhand;
import pw.hachimi.client.module.combat.PearlBlocker;
import pw.hachimi.client.module.combat.Penetrate;
import pw.hachimi.client.module.combat.PistonCrystal;
import pw.hachimi.client.module.combat.PistonKick;
import pw.hachimi.client.module.combat.SelfBow;
import pw.hachimi.client.module.combat.SelfTrap;
import pw.hachimi.client.module.combat.ShieldBreaker;
import pw.hachimi.client.module.combat.SpearAura;
import pw.hachimi.client.module.combat.Surround;
import pw.hachimi.client.module.exploit.AntiHunger;
import pw.hachimi.client.module.exploit.AntiKick;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.exploit.ChorusControl;
import pw.hachimi.client.module.exploit.Crasher;
import pw.hachimi.client.module.exploit.Disabler;
import pw.hachimi.client.module.exploit.DurableElytra;
import pw.hachimi.client.module.exploit.FakeLatency;
import pw.hachimi.client.module.exploit.FakeVanilla;
import pw.hachimi.client.module.exploit.FastLatency;
import pw.hachimi.client.module.exploit.FastProjectile;
import pw.hachimi.client.module.exploit.GodMode;
import pw.hachimi.client.module.exploit.HandlerRevision;
import pw.hachimi.client.module.exploit.MaceSpoof;
import pw.hachimi.client.module.exploit.NoGlitch;
import pw.hachimi.client.module.exploit.NoMineAnimation;
import pw.hachimi.client.module.exploit.PacketCanceler;
import pw.hachimi.client.module.exploit.PacketFly;
import pw.hachimi.client.module.exploit.Phase;
import pw.hachimi.client.module.exploit.RocketExtend;
import pw.hachimi.client.module.exploit.ScreenKeeper;
import pw.hachimi.client.module.exploit.SpearFix;
import pw.hachimi.client.module.exploit.TridentTweaks;
import pw.hachimi.client.module.exploit.XCarry;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.module.misc.AdaptiveFPS;
import pw.hachimi.client.module.misc.AntiAFK;
import pw.hachimi.client.module.misc.AntiSpam;
import pw.hachimi.client.module.misc.AnvilRename;
import pw.hachimi.client.module.misc.AutoEZ;
import pw.hachimi.client.module.misc.AutoLogin;
import pw.hachimi.client.module.misc.AutoReconnect;
import pw.hachimi.client.module.misc.BetterChat;
import pw.hachimi.client.module.misc.ChatNotifier;
import pw.hachimi.client.module.misc.ExtraTab;
import pw.hachimi.client.module.misc.FakePlayer;
import pw.hachimi.client.module.misc.NameProtect;
import pw.hachimi.client.module.misc.NoPacketKick;
import pw.hachimi.client.module.misc.NoSoundLag;
import pw.hachimi.client.module.misc.PMSound;
import pw.hachimi.client.module.misc.PacketLogger;
import pw.hachimi.client.module.misc.Ping2;
import pw.hachimi.client.module.misc.PotionTracker;
import pw.hachimi.client.module.misc.SkinBlink;
import pw.hachimi.client.module.misc.Spammer;
import pw.hachimi.client.module.misc.SpinBot;
import pw.hachimi.client.module.misc.Timer;
import pw.hachimi.client.module.misc.Tooltips;
import pw.hachimi.client.module.misc.TrueDurability;
import pw.hachimi.client.module.movement.AntiLevitation;
import pw.hachimi.client.module.movement.AutoWalk;
import pw.hachimi.client.module.movement.Avoid;
import pw.hachimi.client.module.movement.ElytraAcclerate;
import pw.hachimi.client.module.movement.ElytraFly;
import pw.hachimi.client.module.movement.ElytraRoam;
import pw.hachimi.client.module.movement.EntityControl;
import pw.hachimi.client.module.movement.EntitySpeed;
import pw.hachimi.client.module.movement.FakeFly;
import pw.hachimi.client.module.movement.FastFall;
import pw.hachimi.client.module.movement.FastSwim;
import pw.hachimi.client.module.movement.Flight;
import pw.hachimi.client.module.movement.Follower;
import pw.hachimi.client.module.movement.LongJump;
import pw.hachimi.client.module.movement.NoFall;
import pw.hachimi.client.module.movement.NoJumpDelay;
import pw.hachimi.client.module.movement.NoSlow;
import pw.hachimi.client.module.movement.Parkour;
import pw.hachimi.client.module.movement.RocketBoost;
import pw.hachimi.client.module.movement.SafeWalk;
import pw.hachimi.client.module.movement.Speed;
import pw.hachimi.client.module.movement.Sprint;
import pw.hachimi.client.module.movement.Step;
import pw.hachimi.client.module.movement.Strafe;
import pw.hachimi.client.module.movement.Teleport;
import pw.hachimi.client.module.movement.TickShift;
import pw.hachimi.client.module.movement.Velocity;
import pw.hachimi.client.module.movement.Yaw;
import pw.hachimi.client.module.player.AccuratePotion;
import pw.hachimi.client.module.player.AirPlace;
import pw.hachimi.client.module.player.AutoArmor;
import pw.hachimi.client.module.player.AutoEat;
import pw.hachimi.client.module.player.AutoFish;
import pw.hachimi.client.module.player.AutoMount;
import pw.hachimi.client.module.player.AutoRespawn;
import pw.hachimi.client.module.player.AutoTool;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.module.player.ChestStealer;
import pw.hachimi.client.module.player.FastPlace;
import pw.hachimi.client.module.player.FreeLook;
import pw.hachimi.client.module.player.Freecam;
import pw.hachimi.client.module.player.GhostHand;
import pw.hachimi.client.module.player.InventoryManager;
import pw.hachimi.client.module.player.MultiTask2;
import pw.hachimi.client.module.player.NoInteract;
import pw.hachimi.client.module.player.NoRotate;
import pw.hachimi.client.module.player.NoSlotSet;
import pw.hachimi.client.module.player.PacketEat;
import pw.hachimi.client.module.player.Reach;
import pw.hachimi.client.module.player.Replenish;
import pw.hachimi.client.module.player.Scaffold;
import pw.hachimi.client.module.player.Swing;
import pw.hachimi.client.module.player.TweakMarco;
import pw.hachimi.client.module.render.Ambience;
import pw.hachimi.client.module.render.Animations;
import pw.hachimi.client.module.render.AntiAliasing;
import pw.hachimi.client.module.render.AspectRatio;
import pw.hachimi.client.module.render.BetterHotbar;
import pw.hachimi.client.module.render.BlockHighlight;
import pw.hachimi.client.module.render.Breadcrumbs;
import pw.hachimi.client.module.render.BreakESP;
import pw.hachimi.client.module.render.Chams;
import pw.hachimi.client.module.render.CrystalModel;
import pw.hachimi.client.module.render.ESP;
import pw.hachimi.client.module.render.Fullbright;
import pw.hachimi.client.module.render.Glint;
import pw.hachimi.client.module.render.HoleESP;
import pw.hachimi.client.module.render.KillEffects;
import pw.hachimi.client.module.render.Nametags;
import pw.hachimi.client.module.render.NoRender;
import pw.hachimi.client.module.render.Particles;
import pw.hachimi.client.module.render.PhantomView;
import pw.hachimi.client.module.render.PhaseESP;
import pw.hachimi.client.module.render.PlaceEcho;
import pw.hachimi.client.module.render.PopChams;
import pw.hachimi.client.module.render.Search;
import pw.hachimi.client.module.render.Shaders;
import pw.hachimi.client.module.render.Skybox;
import pw.hachimi.client.module.render.StorageESP;
import pw.hachimi.client.module.render.TargetESP;
import pw.hachimi.client.module.render.Tracers;
import pw.hachimi.client.module.render.Trajectories;
import pw.hachimi.client.module.render.TrueSight;
import pw.hachimi.client.module.render.ViewModel;
import pw.hachimi.client.module.render.Waypoints;
import pw.hachimi.client.module.render.Zoom;

public final class ModuleManager {
   private final Map map;
   private static final String c = "Registered {} modules!";

   public ModuleManager() {
      LinkedHashMap linkedHashMap = new LinkedHashMap();
      this.map = Collections.synchronizedMap(linkedHashMap);
      this.XG();
   }

   private void XG() {
      this.XH(
         new PearlBlocker(),
         new PotionTracker(),
         new AutoEZ(),
         new BetterHotbar(),
         new HitResultWorldHelper(),
         new UI(),
         new ColorHelper3(),
         new PlayerModule(),
         new Font(),
         new HUD(),
         new ColorHelper(),
         new AntiAliasing(),
         new AntiCrawl(),
         new AntiRegear(),
         new CrawlTrap(),
         new AntiWeakness(),
         new Aura(),
         new SpearAura(),
         new ArmorAlert(),
         new AutoMine(),
         new AutoAnchor(),
         new AutoArmor(),
         new AutoRelease(),
         new AutoCrystal(),
         new AutoLog(),
         new Glint(),
         new AutoPotion(),
         new Offhand(),
         new MainHand(),
         new AutoSpear(),
         new AutoTrap(),
         new AutoWeb(),
         new AutoXP(),
         new Penetrate(),
         new AntiPhase(),
         new Criticals(),
         new SelfTrap(),
         new Surround(),
         new Flatten(),
         new NoHitDelay(),
         new PistonKick(),
         new ShieldBreaker(),
         new PistonCrystal(),
         new Replenish(),
         new SelfBow(),
         new AntiHunger(),
         new ChorusControl(),
         new FakeVanilla(),
         new Crasher(),
         new Disabler(),
         new RocketExtend(),
         new ScreenKeeper(),
         new FakeLatency(),
         new FastLatency(),
         new FastProjectile(),
         new GodMode(),
         new HandlerRevision(),
         new MaceSpoof(),
         new NoMineAnimation(),
         new PacketCanceler(),
         new PacketFly(),
         new Phase(),
         new Reach(),
         new AntiAFK(),
         new SpinBot(),
         new AntiSpam(),
         new AnvilRename(),
         new AutoEat(),
         new AutoFish(),
         new AutoMount(),
         new AutoReconnect(),
         new AutoRespawn(),
         new BetterChat(),
         new GhostHand(),
         new InventoryManager(),
         new ChatNotifier(),
         new ChestStealer(),
         new TweakMarco(),
         new DurableElytra(),
         new FakePlayer(),
         new StackPotionContentsComponentManager(),
         new NoPacketKick(),
         new NoSoundLag(),
         new PMSound(),
         new ModuleManagerExplosionS2CPacketPlayerPositionLookS2CPacketHelper(),
         new SkinBlink(),
         new Spammer(),
         new Swing(),
         new Timer(),
         new TrueDurability(),
         new AdaptiveFPS(),
         new XCarry(),
         new AntiLevitation(),
         new AutoWalk(),
         new ElytraFly(),
         new EntityControl(),
         new EntitySpeed(),
         new Blink(),
         new FastFall(),
         new FastSwim(),
         new RocketBoost(),
         new Flight(),
         new Follower(),
         new LongJump(),
         new Strafe(),
         new NoFall(),
         new NoJumpDelay(),
         new NoSlow(),
         new ElytraRoam(),
         new Parkour(),
         new SafeWalk(),
         new Speed(),
         new Sprint(),
         new Step(),
         new TickShift(),
         new TridentTweaks(),
         new Ping2(),
         new Velocity(),
         new Yaw(),
         new Ambience(),
         new Animations(),
         new AspectRatio(),
         new BlockHighlight(),
         new Breadcrumbs(),
         new BreakESP(),
         new CrystalModel(),
         new Chams(),
         new ESP(),
         new ExtraTab(),
         new Freecam(),
         new FreeLook(),
         new Fullbright(),
         new HoleESP(),
         new KillEffects(),
         new NameProtect(),
         new Nametags(),
         new NoRender(),
         new NoRotate(),
         new Particles(),
         new PhaseESP(),
         new PlaceEcho(),
         new TargetESP(),
         new PopChams(),
         new Search(),
         new Shaders(),
         new Skybox(),
         new StorageESP(),
         new Tooltips(),
         new Tracers(),
         new Teleport(),
         new Trajectories(),
         new TrueSight(),
         new PhantomView(),
         new ViewModel(),
         new Waypoints(),
         new AccuratePotion(),
         new Zoom(),
         new AirPlace(),
         new NoInteract(),
         new BlockPosBoxMultitask(),
         new AutoTool(),
         new AntiKick(),
         new Avoid(),
         new FastPlace(),
         new MultiTask2(),
         new NoGlitch(),
         new NoSlotSet(),
         new Scaffold(),
         new PacketEat(),
         new AutoLogin(),
         new FakeFly(),
         new ElytraAcclerate(),
         new PacketLogger(),
         new Blocker(),
         new SpearFix()
      );
      this.XI(
         new HUDLegacy(),
         new Speeds(),
         new Chests(),
         new FPS(),
         new Modules(),
         new TPS(),
         new DrawStackRenderModule(),
         new Armor(),
         new Notification(),
         new Ping(),
         new Crosshair(),
         new Durability(),
         new Coords(),
         new ServerBrand(),
         new Direction(),
         new Watermark(),
         new TextRadar(),
         new ModuleManagerScreenHelper(),
         new Items()
      );
      if (Bootstrap.NV()) {
         this.XJ(new ModuleManagerColorHelper());
      }

      Iterator iterator2 = this.XM().iterator();

      while (iterator2.hasNext()) {
         Object obj = iterator2.next();
         if (obj instanceof ClientModule) {
            Managers.playerScreenManager.akr(((ClientModule)obj).awi());
         }
      }

      Bootstrap.Oa("Registered {} modules!", this.map.size());
   }

   private void XH(ModuleBase... value) {
      Comparator comparator = Comparator.comparing(new Function() {
         @Override
         public Object apply(Object value) {
            return ((ConfigContainer)value).getText2();
         }
      });
      Arrays.sort(value, comparator);

      for (ModuleBase moduleBase : value) {
         this.XJ(moduleBase);
      }
   }

   private void XI(RenderModule... value) {
      Comparator comparator = Comparator.comparing(new Function() {
         @Override
         public Object apply(Object value) {
            return ((ConfigContainer)value).getText2();
         }
      });
      Arrays.sort(value, comparator);

      for (RenderModule renderModule : value) {
         this.XJ(renderModule);
         Managers.drawMatrixManager.Hx(renderModule);
      }
   }

   public void XJ(ModuleBase moduleBase) {
      Map temp = this.map;
      String str = moduleBase.Kn();
      temp.put(str, moduleBase);
   }

   public void XK(ModuleBase moduleBase) {
      Map temp = this.map;
      String str = moduleBase.Kn();
      temp.remove(str);
   }

   public ModuleBase XL(String str) {
      return (ModuleBase)this.map.get(str);
   }

   public List XM() {
      return new ArrayList(this.map.values());
   }
}
