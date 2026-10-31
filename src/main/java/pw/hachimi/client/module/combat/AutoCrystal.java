package pw.hachimi.client.module.combat;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import pw.hachimi.client.utils.combat.UnidentifiedUtils3;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pw.hachimi.client.combat.AutoCrystalVec3dBlockPosManager;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.RunEvent;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.event.world.AddEntity_2Event;
import pw.hachimi.client.event.world.BlockPosEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.DefaultDegreesMode;
import pw.hachimi.client.model.combat.AutoCrystalFullSemiMode;
import pw.hachimi.client.model.combat.AutoCrystalNativeProtocolMode;
import pw.hachimi.client.model.combat.AutoCrystalNoneBypassMode;
import pw.hachimi.client.model.combat.AutoCrystalNonePlaceMode;
import pw.hachimi.client.model.combat.AutoCrystalNormalSilentMode;
import pw.hachimi.client.model.combat.AutoCrystalNormalStrictMode;
import pw.hachimi.client.model.combat.AutoCrystalPlaceBreakMode;
import pw.hachimi.client.model.combat.DamageDataAttackTarget;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.model.render.SliderFadeMode;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.exploit.FastLatency;
import pw.hachimi.client.module.movement.Sprint;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.ConcurrentLinkedDeque;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.UnidentifiedHelper22;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.combat.UnidentifiedHelper11;
import pw.hachimi.client.utils.player.EnchantmentUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.BoxEntityUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.client.utils.world.Vec3dEntityUtils;
import pw.hachimi.client.world.BlockPosHashSet;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoCrystal extends Multitask {
   private static final Box a = new Box(-1.0, 0.0, -1.0, 1.0, 2.0, 1.0);
   private static AutoCrystal INSTANCE;
   public final Stopwatch stopwatch = new StopwatchImpl();
   public final Map map = new HashMap();
   private final StopwatchImpl stopwatchImpl = new StopwatchImpl();
   private final Stopwatch stopwatch2 = new StopwatchImpl();
   private final Stopwatch stopwatch3 = new StopwatchImpl();
   private final Stopwatch stopwatch4 = new UnidentifiedHelper22();
   private final Deque deque = new ConcurrentLinkedDeque();
   private final Map map2 = Collections.synchronizedMap(new ConcurrentHashMap());
   private final Map map3 = Collections.synchronizedMap(new ConcurrentHashMap());
   private final UnidentifiedHelper11 unidentifiedHelper11 = new UnidentifiedHelper11();
   private final Map map4 = new HashMap();
   private final ExecutorService executorService = Executors.newSingleThreadExecutor();
   public BlockPos pos;
   public double o;
   public double p;
   Setting page = this.registerSetting(new EnumSetting("Page", "The pages", AutoCrystalPlaceBreakMode.PLACE, AutoCrystalPlaceBreakMode.values()));
   Setting place = this.registerSetting(new BooleanSetting("Place", "Places crystals to damage enemies. Place settings will only function if this setting is enabled", true, this::isPlaceVisible));
   Setting placeDelay = this.registerSetting(new NumberSetting("PlaceDelay", "Delay to place crystals", 0.0F, 100.0F, 1000.0F, this::isPlaceDelayVisible));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "Range to place crystals", 0.1F, 4.0F, 6.0F, this::isPlaceRangeVisible));
   Setting placeWallRange = this.registerSetting(new NumberSetting("PlaceWallRange", "Range to place crystals through walls", 0.1F, 4.0F, 6.0F, this::isPlaceWallRangeVisible));
   Setting swap = this.registerSetting(new EnumSetting("Swap", "Swaps to an end crystal before placing if the player is not holding one", AutoCrystalNormalSilentMode.OFF, AutoCrystalNormalSilentMode.values(), this::isSwapVisible));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values(), this::isSwapActionVisible));
   Setting placements = this.registerSetting(
      new EnumSetting("Placements", "Version standard for placing end crystals", AutoCrystalNativeProtocolMode.NATIVE, AutoCrystalNativeProtocolMode.values(), this::isPlacementsVisible)
   );
   Setting sequential = this.registerSetting(new EnumSetting("Sequential", "Places a crystal after spawn", AutoCrystalNormalStrictMode.NONE, AutoCrystalNormalStrictMode.values(), this::isSequentialVisible));
   Setting forcePlace = this.registerSetting(new BooleanSetting("ForcePlace", "Allow min damage", false, this::isForcePlaceVisible));
   Setting minHealth = this.registerSetting(new NumberSetting("MinHealth", "If target's health lower than this", 0.1F, 4.0F, 36.0F, this::isMinHealthVisible));
   Setting forceDamage = this.registerSetting(new NumberSetting("ForceDamage", "Turned to this", 0.1F, 2.0F, 36.0F, this::isForceDamageVisible));
   Setting placeSwing = this.registerSetting(new EnumSetting("PlaceSwing", "Swing side for place crystal", SwingMode.Normal, SwingMode.values(), this::isPlaceSwingVisible));
   Setting instant = this.registerSetting(new BooleanSetting("Instant", "Instantly attacks crystals when they spawn", false, this::isInstantVisible));
   Setting instantDelay = this.registerSetting(new NumberSetting("InstantDelay", "Delay to break crystals", 0.0F, 50.0F, 1000.0F, this::isInstantDelayVisible));
   Setting inhibit = this.registerSetting(new BooleanSetting("Inhibit", "Prevents excessive attacks", true, this::isInhibitVisible));
   Setting breakDelay = this.registerSetting(new NumberSetting("BreakDelay", "Delay to break crystals", 0.0F, 50.0F, 1000.0F, this::isBreakDelayVisible));
   Setting attackDelay = this.registerSetting(new NumberSetting("AttackDelay", "Added delays", 0.0F, 0.0F, 5.0F, this::isAttackDelayVisible));
   Setting attackFactor = this.registerSetting(new NumberSetting("AttackFactor", "Factor of attack delay", 0, 0, 3, this::isAttackFactorVisible));
   Setting attackLimit = this.registerSetting(new NumberSetting("AttackLimit", "The number of attacks before considering a crystal unbreakable", 0.5F, 1.5F, 20.0F, this::isAttackLimitVisible));
   Setting timeout = this.registerSetting(new BooleanSetting("Timeout", "Uses attack latency to calculate break delays", false, this::isTimeoutVisible));
   Setting breakTimeout = this.registerSetting(new NumberSetting("BreakTimeout", "Time after waiting for the average break time before considering a crystal attack failed", 0.0F, 3.0F, 10.0F, this::isBreakTimeoutVisible));
   Setting minTimeout = this.registerSetting(new NumberSetting("MinTimeout", "Minimum time before considering a crystal break/place failed", 0.0F, 5.0F, 20.0F, this::isMinTimeoutVisible));
   Setting ticksExisted = this.registerSetting(new NumberSetting("TicksExisted", "Minimum ticks alive to consider crystals for attack", 0, 0, 10, this::isTicksExistedVisible));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "Range to break crystals", 0.1F, 4.0F, 6.0F, this::isBreakRangeVisible));
   Setting breakWallRange = this.registerSetting(new NumberSetting("BreakWallRange", "Range to break crystals through walls", 0.1F, 4.0F, 6.0F, this::isBreakWallRangeVisible));
   Setting breakSwing = this.registerSetting(new EnumSetting("BreakSwing", "Swing side for attack crystal", SwingMode.Normal, SwingMode.values(), this::isBreakSwingVisible));
   Setting rotate = this.registerSetting(new EnumSetting("Rotate", "Rotate before placing and breaking", AutoCrystalNonePlaceMode.NORMAL, AutoCrystalNonePlaceMode.values(), this::isRotateVisible));
   Setting yawStep = this.registerSetting(new EnumSetting("YawStep", "Rotates yaw over multiple ticks to prevent certain rotation flags in NCP", AutoCrystalFullSemiMode.OFF, AutoCrystalFullSemiMode.values(), this::isYawStepVisible));
   Setting yawStepLimit = this.registerSetting(new NumberSetting("YawStep-Limit", "Maximum yaw rotation in degrees for one tick", 1, 180, 180, DefaultDegreesMode.DEGREES, this::isYawStepLimitVisible));
   Setting whileUsing = this.registerSetting(new BooleanSetting("WhileUsing", "Allows actions while using items", false, this::isWhileUsingVisible));
   Setting whileMining = this.registerSetting(new BooleanSetting("WhileMining", "Allows attacking while mining blocks", true, this::isWhileMiningVisible));
   Setting swapPenalty = this.registerSetting(new NumberSetting("SwapPenalty", "Delay for attacking after swapping items which prevents NCP flags", 0.0F, 0.0F, 10.0F, this::isSwapPenaltyVisible));
   Setting shieldMode = this.registerSetting(new EnumSetting("ShieldMode", "How to consider shields", AutoCrystalNoneBypassMode.None, AutoCrystalNoneBypassMode.values(), this::isShieldModeVisible));
   Setting surroundFucker = this.registerSetting(new BooleanSetting("SurroundFucker", "That seems to be effective, right?", false, this::isSurroundFuckerVisible));
   Setting antiSurround = this.registerSetting(new BooleanSetting("AntiSurround", "Places on mining blocks that when broken, can be placed on to damage enemies. Instantly destroys items spawned from breaking block and allows faster placing", false, this::isAntiSurroundVisible));
   Setting prePlacement = this.registerSetting(new BooleanSetting("PrePlacement", "Places on surrounding blocks to block place", false, this::isPrePlacementVisible));
   Setting cevBomber = this.registerSetting(new BooleanSetting("CevBomber", "Places on mining blocks", false, this::isCevBomberVisible));
   Setting cevDamage = this.registerSetting(new NumberSetting("CevDamage", "Minimum damage", 1.0F, 8.0F, 10.0F, this::isCevDamageVisible));
   Setting miningProgress = this.registerSetting(new NumberSetting("MiningProgress", "The progress of mining", 0.0, 80.0, 100.0, this::isMiningProgressVisible));
   Setting resetCrystal = this.registerSetting(new BooleanSetting("ResetCrystal", "Attack blocker crystal", false, this::isResetCrystalVisible));
   Setting armorBreaker = this.registerSetting(new BooleanSetting("ArmorBreaker", "Attempts to break enemy armor with crystals", true, this::isArmorBreakerVisible));
   Setting armorScale = this.registerSetting(new NumberSetting("ArmorScale", "Armor damage scale before attempting to break enemy armor with crystals", 1.0F, 5.0F, 20.0F, DefaultDegreesMode.PERCENT, this::isArmorScaleVisible));
   Setting asynchronous = this.registerSetting(new BooleanSetting("Asynchronous", "Performs calculations on separate threads", true, this::isAsynchronousVisible));
   Setting enemyRange = this.registerSetting(new NumberSetting("EnemyRange", "Range to search for potential enemies", 1.0F, 10.0F, 13.0F, this::isEnemyRangeVisible));
   Setting ignoreNakeds = this.registerSetting(new BooleanSetting("IgnoreNakeds", "Ignore players with no items", false, this::isIgnoreNakedsVisible));
   Setting minDamage = this.registerSetting(new NumberSetting("MinDamage", "Minimum damage required to consider attacking or placing an end crystal", 1.0F, 4.0F, 10.0F, this::isMinDamageVisible));
   Setting maxLocalDamage = this.registerSetting(new NumberSetting("MaxLocalDamage", "The maximum player damage", 4.0F, 12.0F, 20.0F, this::isMaxLocalDamageVisible));
   Setting damageRatio = this.registerSetting(new NumberSetting("DamageRatio", "Max self damage ratio", 0.1F, 1.2F, 2.0F, this::isDamageRatioVisible));
   Setting instantCalc = this.registerSetting(new BooleanSetting("Instant-Calc", "Calculates a crystal when it spawns and attacks if it meets MINIMUM requirements, this will result in non-ideal crystal attacks", false, this::isInstantCalcVisible));
   Setting instantDamage = this.registerSetting(new NumberSetting("InstantDamage", "Minimum damage to attack crystals instantly", 1.0F, 6.0F, 10.0F, this::isInstantDamageVisible));
   Setting instantMax = this.registerSetting(new BooleanSetting("InstantMax", "Attacks crystals instantly if they exceed the previous max attack damage (Note: This is still not a perfect check because the next tick could have better damages)", true, this::isInstantMaxVisible));
   Setting ignoreItem = this.registerSetting(new BooleanSetting("IgnoreItem", "Attempts to replace crystals in surrounds", false, this::isIgnoreItemVisible));
   Setting assumeBestArmor = this.registerSetting(new BooleanSetting("AssumeBestArmor", "Assumes Prot 0 armor is max armor", false, this::isAssumeBestArmorVisible));
   Setting lethalMultiplier = this.registerSetting(new NumberSetting("LethalMultiplier", "If we can kill an enemy with this many crystals, disregard damage values", 0.0F, 1.5F, 4.0F, this::isLethalMultiplierVisible));
   Setting lethalTotem = this.registerSetting(new BooleanSetting("Lethal-Totem", "Predicts totems and places crystals to instantly double pop and kill the target", false, this::isLethalTotemVisible));
   Setting lethalDamageTick = this.registerSetting(new BooleanSetting("Lethal-DamageTick", "Places lethal crystals only on ticks where they damage entities", false, this::isLethalDamageTickVisible));
   Setting safety = this.registerSetting(new BooleanSetting("Safety", "Accounts for total player safety when attacking and placing crystals", true, this::isSafetyVisible));
   Setting safetyOverride = this.registerSetting(new BooleanSetting("SafetyOverride", "Overrides the safety checks if the crystal will kill an enemy", false, this::isSafetyOverrideVisible));
   Setting blockDestruction = this.registerSetting(new BooleanSetting("BlockDestruction", "Accounts for explosion block destruction when calculating damages", false, this::isBlockDestructionVisible));
   Setting selfExtrapolate = this.registerSetting(new BooleanSetting("SelfExtrapolate", "Accounts for motion when calculating self damage", false, this::isSelfExtrapolateVisible));
   Setting extrapolationTicks = this.registerSetting(new NumberSetting("ExtrapolationTicks", "Accounts for motion when calculating enemy positions, not fully accurate", 0, 0, 10, this::isExtrapolationTicksVisible));
   Setting players = this.registerSetting(new BooleanSetting("Players", "Target players", true, this::isPlayersVisible));
   Setting monsters = this.registerSetting(new BooleanSetting("Monsters", "Target monsters", false, this::isMonstersVisible));
   Setting neutrals = this.registerSetting(new BooleanSetting("Neutrals", "Target neutrals", false, this::isNeutralsVisible));
   Setting animals = this.registerSetting(new BooleanSetting("Animals", "Target animals", false, this::isAnimalsVisible));
   Setting shulkers = this.registerSetting(new BooleanSetting("Shulkers", "Target shulker boxes", false, this::isShulkersVisible));
   Setting support = this.registerSetting(new BooleanSetting("Support", "Auto place of crystal base(obsidian)", false, this::isSupportVisible));
   Setting baseRotate = this.registerSetting(new BooleanSetting("Base-Rotate", "Place block with rotation", false, this::isBaseRotateVisible));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false, this::isAirPlaceVisible));
   Setting baseDelay = this.registerSetting(new NumberSetting("Base-Delay", "Delay to place base", 1, 4, 20, this::isBaseDelayVisible));
   Setting maxOverride = this.registerSetting(new NumberSetting("MaxOverride", "Base can be placed when the crystal damage is below the set value", 0.0F, 6.0F, 36.0F, this::isMaxOverrideVisible));
   Setting baseSwapAction = this.registerSetting(new EnumSetting("Base-SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values(), this::isBaseSwapActionVisible));
   public Setting render = this.registerSetting(new EnumSetting("Render", "Renders the current placement", SliderFadeMode.FADE, SliderFadeMode.values(), this::isRenderVisible));
   public Setting boxMode = this.registerSetting(new EnumSetting("BoxMode", "Box rendering mode", ShapeMode.Both, ShapeMode.values(), this::isBoxModeVisible));
   public Setting line = this.registerSetting(new ColorSetting("Line", "The box color", c(100), this::isLineVisible));
   public Setting side = this.registerSetting(new ColorSetting("Side", "The fill color", c(70), this::isSideVisible));
   Setting width = this.registerSetting(new NumberSetting("Width", "The line width of the highlight", 0.1F, 1.0F, 5.0F, this::isWidthVisible));
   public Setting shrink = this.registerSetting(new BooleanSetting("Shrink", "Shrink", false, this::isShrinkVisible));
   public Setting sliderSpeed = this.registerSetting(new NumberSetting("SliderSpeed", "Slider speed", 0.1, 0.5, 1.0, this::isSliderSpeedVisible));
   public Setting startFade = this.registerSetting(new NumberSetting("StartFade", "Time of waiting for fade", 0.1, 0.2, 1.0, this::isStartFadeVisible));
   public Setting fadeSpeed = this.registerSetting(new NumberSetting("FadeSpeed", "Fade speed", 0.1, 0.3, 1.0, this::isFadeSpeedVisible));
   public Setting fadeTime = this.registerSetting(new NumberSetting("Fade-Time", "Timer for the fade", 0, 250, 1000, this::isFadeTimeVisible));
   public Setting renderDamage = this.registerSetting(new BooleanSetting("RenderDamage", "Renders damage", false, this::isRenderDamageVisible));
   public Setting targetDamage = this.registerSetting(new BooleanSetting("TargetDamage", "Target's damage", true, this::isTargetDamageVisible));
   public Setting selfDamage = this.registerSetting(new BooleanSetting("SelfDamage", "My damage", false, this::isSelfDamageVisible));
   Setting targetName = this.registerSetting(new BooleanSetting("TargetName", "Displayed content", true, this::isTargetNameVisible));
   Setting damage = this.registerSetting(new BooleanSetting("Damage", "Displayed content", true, this::isDamageVisible));
   Setting breakMs = this.registerSetting(new BooleanSetting("BreakMs", "Displayed content", false, this::isBreakMsVisible));
   Setting suffix = this.registerSetting(new BooleanSetting("Suffix", ":)", true, this::isSuffixVisible));
   Setting cps = this.registerSetting(new BooleanSetting("CPS", "crystal per second", false, this::isCpsVisible));
   StackSlotHelper stackSlotHelper = null;
   private DamageDataAttackTarget damageDataAttackTarget;
   private DamageDataAttackTarget damageDataAttackTarget2;
   private Vec3d vec;
   private boolean bl;
   private boolean bm;

   public AutoCrystal() {
      super("AutoCrystal", "Attacks entities with end crystals", ModuleCategory.COMBAT, 750);
      this.OK(this.breakSetting);
      EventBusImpl.INSTANCE.rD(new AutoCrystalVec3dBlockPosManager());
      INSTANCE = this;
   }

   public static AutoCrystal sc() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      Entity entity2 = this.sd();
      if (entity2 == null) {
         return super.hy();
      }

      StringBuilder stringBuilder = new StringBuilder();
      if ((Boolean)this.targetName.atx()) {
         stringBuilder.append(entity2.getName().getString());
      }

      if ((Boolean)this.damage.atx()) {
         if (!stringBuilder.isEmpty()) {
            stringBuilder.append(", ");
         }

         stringBuilder.append(UnidentifiedUtils3.rx(this.o, 1));
      }

      if ((Boolean)this.breakMs.atx()) {
         if (!stringBuilder.isEmpty()) {
            stringBuilder.append(", ");
         }

         Object obj;
         if (this.stopwatchImpl.afq(((Float)this.breakDelay.atx()).floatValue() + 2000.0F)) {
            obj = Integer.valueOf(0);
         } else {
            obj = this.tg() + ((Boolean)this.suffix.atx() ? "ms" : "");
         }

         stringBuilder.append(obj);
      }

      if ((Boolean)this.cps.atx()) {
         if (!stringBuilder.isEmpty()) {
            stringBuilder.append(", ");
         }

         stringBuilder.append(this.unidentifiedHelper11.Lo());
      }

      return stringBuilder.toString();
   }

   public Entity sd() {
      DamageDataAttackTarget damageDataAttackTarget3 = this.damageDataAttackTarget2;
      if (this.damageDataAttackTarget != null) {
         return this.damageDataAttackTarget.attackTarget();
      } else {
         return (DamageDataAttackTarget)damageDataAttackTarget3 != null ? damageDataAttackTarget3.attackTarget() : null;
      }
   }

   @Override
   public void awe() {
      this.pos = null;
      this.damageDataAttackTarget = null;
      this.damageDataAttackTarget2 = null;
      this.vec = null;
      this.stackSlotHelper = null;
      this.map2.clear();
      this.map4.clear();
      this.map3.clear();
      this.deque.clear();
      this.map.clear();
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.awe();
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (client.player != null && client.world != null && !client.player.isSpectator() && tickPreEvent.getEventPhase() == EventPhase.POST) {
         if ((Boolean)this.asynchronous.atx()) {
            ExecutorService temp = this.executorService;
            Runnable runnable = this::tp;
            temp.submit(runnable);
         }
      }
   }

   @EventListener
   public void sg(BlockPosEvent blockPosEvent) {
      if (blockPosEvent.getEventPhase() != EventPhase.POST || !(Boolean)this.surroundFucker.atx()) {
         return;
      }

      this.stackSlotHelper = new StackSlotHelper(Items.END_CRYSTAL, (SwapMode)this.swapAction.atx()).LK();
      if (!this.stackSlotHelper.LL()) {
         return;
      }

      Vec3d vec = blockPosEvent.getPos().toBottomCenterPos();
      BlockPos pos2 = blockPosEvent.getPos().down();
      if (!this.sX(pos2) || !this.sH(pos2)) {
         return;
      }

      BlockPos pos3 = pos2.up();
      if (this.placements.atx() == AutoCrystalNativeProtocolMode.PROTOCOL && !client.world.isAir(pos3.up())) {
         return;
      }

      Box box = new Box(pos2.up().toBottomCenterPos(), pos2.up(3).toBottomCenterPos());
      Iterator iterator2 = client.world.getOtherEntities((Entity)null, box).iterator();
      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (!entity2.isAlive() || entity2 instanceof ItemEntity) {
            continue;
         }

         if (!(entity2 instanceof EndCrystalEntity)) {
            return;
         }

         EndCrystalEntity endCrystalEntity = (EndCrystalEntity)entity2;
         Integer integer = (Integer)this.map4.get(Integer.valueOf(endCrystalEntity.getId()));
         double d = this.sw(client.player, endCrystalEntity.getPos(), (Boolean)this.blockDestruction.atx(), (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0, false);
         if ((Boolean)this.safety.atx() && this.sO(d)) {
            return;
         }

         if (!this.sD(endCrystalEntity)) {
            return;
         }

         if (integer != null && (float)integer.intValue() > (Float)this.attackLimit.atx() * 10.0F) {
            return;
         }
      }

      if (!this.sC(vec)) {
         return;
      }

      double d2 = this.sy(client.player, pos2, (Boolean)this.blockDestruction.atx(), (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0, false);
      if (this.sO(d2)) {
         return;
      }

      Iterator iterator3 = client.world.getEntities().iterator();
      while (iterator3.hasNext()) {
         Entity entity3 = (Entity)iterator3.next();
         if (!(entity3 instanceof LivingEntity)) {
            continue;
         }

         LivingEntity living = (LivingEntity)entity3;
         if (!entity3.isAlive() || entity3 == client.player || !this.sW(entity3)) {
            continue;
         }

         if (Managers.friendManager.awt(entity3.getName())) {
            continue;
         }

         if ((Boolean)this.ignoreNakeds.atx() && WorldUtils.NO(living)) {
            continue;
         }

         if (vec.squaredDistanceTo(entity3.getPos()) > 144.0) {
            continue;
         }

         if (client.player.squaredDistanceTo(entity3) > (double)((Float)this.enemyRange.atx() * (Float)this.enemyRange.atx())) {
            continue;
         }

         double d3 = this.sy(entity3, pos2, (Boolean)this.blockDestruction.atx(), (Integer)this.extrapolationTicks.atx(), (Boolean)this.assumeBestArmor.atx());
         if (this.damageDataAttackTarget2 != null && d3 <= this.damageDataAttackTarget2.damage() - 1.0) {
            continue;
         }

         if (d3 <= (double)((Float)this.minDamage.atx()).floatValue()) {
            continue;
         }

         this.th(living, pos2);
         return;
      }
   }

   @EventListener
   public void sh(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (client.player == null || client.world == null) {
         return;
      }

      if (client.player.isSpectator()) {
         return;
      }

      this.stackSlotHelper = new StackSlotHelper(Items.END_CRYSTAL, (SwapMode)this.swapAction.atx()).LK();
      this.pos = null;
      ArrayList arrayList = Lists.newArrayList(client.world.getEntities());
      this.damageDataAttackTarget = this.sB(arrayList);
      if (!(Boolean)this.asynchronous.atx()) {
         if ((Boolean)this.place.atx() && this.sU() && AntiWeakness.INSTANCE.js()) {
            this.damageDataAttackTarget2 = this.sG(this.tc(this.ti()), arrayList);
         } else {
            this.damageDataAttackTarget2 = null;
         }
      }

      DamageDataAttackTarget damageDataAttackTarget3 = this.damageDataAttackTarget2;
      if (this.damageDataAttackTarget == null && damageDataAttackTarget3 != null && (!damageDataAttackTarget3.antiSurround() || (Boolean)this.resetCrystal.atx())) {
         EndCrystalEntity endCrystalEntity = this.tb(damageDataAttackTarget3.blockPos().up());
         if (endCrystalEntity == null) {
            endCrystalEntity = this.tb(damageDataAttackTarget3.blockPos().up(2));
         }

         if (endCrystalEntity != null && !endCrystalEntity.getBlockPos().equals(damageDataAttackTarget3.blockPos().up())) {
            double d = this.sw(
               client.player, endCrystalEntity.getPos(), (Boolean)this.blockDestruction.atx(), (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0, false
            );
            if (!(Boolean)this.safety.atx() || !this.sO(d)) {
               this.damageDataAttackTarget = new DamageDataAttackTarget(
                  endCrystalEntity, damageDataAttackTarget3.attackTarget(), damageDataAttackTarget3.damage(), d, endCrystalEntity.getBlockPos().down(), false, null
               );
            }
         }
      }

      if ((Boolean)this.inhibit.atx() && this.damageDataAttackTarget != null && this.map2.containsKey(Integer.valueOf(((EndCrystalEntity)this.damageDataAttackTarget.damageData()).getId()))) {
         float f2;
         if ((double)((Float)this.attackDelay.atx()).floatValue() > 0.0) {
            float f3 = 50.0F / Math.max(1.0F, (float)((Integer)this.attackFactor.atx()).intValue());
            f2 = ((Float)this.attackDelay.atx()).floatValue() * f3;
         } else {
            f2 = ((Float)this.breakDelay.atx()).floatValue();
         }

         this.stopwatchImpl.afP(Float.valueOf(f2 + 100.0F));
         this.map2.remove(Integer.valueOf(((EndCrystalEntity)this.damageDataAttackTarget.damageData()).getId()));
      }

      float f4 = this.tf();
      if ((Boolean)this.timeout.atx()) {
         f4 = Math.max(((Float)this.minTimeout.atx()).floatValue() * 50.0F, (float)this.tg() + ((Float)this.breakTimeout.atx()).floatValue() * 50.0F);
      }

      this.bl = this.damageDataAttackTarget != null && (double)((Float)this.attackDelay.atx()).floatValue() <= 0.0 && this.stopwatchImpl.afq(Float.valueOf(f4));
      if (this.damageDataAttackTarget != null && ((AutoCrystalNonePlaceMode)this.rotate.atx()).afz()) {
         Vec3d vec2 = this.sF(((EndCrystalEntity)this.damageDataAttackTarget.damageData()).getBoundingBox());
         if (vec2 == null) {
            vec2 = ((EndCrystalEntity)this.damageDataAttackTarget.damageData()).getPos().add(0.0, 0.5, 0.0);
         }

         this.vec = vec2;
      } else if (damageDataAttackTarget3 != null && ((AutoCrystalNonePlaceMode)this.rotate.atx()).afy()) {
         Direction direction = PlacementUtils.Fi((BlockPos)damageDataAttackTarget3.damageData(), (double)((Float)this.placeRange.atx()).floatValue());
         Vec3d vec3 = this.sI(damageDataAttackTarget3.blockPos(), direction);
         if (vec3 == null) {
            vec3 = damageDataAttackTarget3.blockPos().toCenterPos();
         }

         this.vec = vec3;
      }

      if (this.sT()) {
         return;
      }

      if (((AutoCrystalNonePlaceMode)this.rotate.atx()).afx() && this.vec != null && (damageDataAttackTarget3 == null || this.sU())) {
         float[] fs = RotationUtils.nL(this.ti(), this.vec);
         if (this.yawStep.atx() == AutoCrystalFullSemiMode.FULL || this.yawStep.atx() == AutoCrystalFullSemiMode.SEMI && this.bl) {
            float f5 = Managers.movementTracker.Ms();
            float f6 = f5 - fs[0];
            float f7 = Math.abs(f6);
            if (f7 > 180.0F) {
               f6 += f6 > 0.0F ? -360.0F : 360.0F;
            }

            int n = f6 > 0.0F ? -1 : 1;
            float f8 = (float)(n * ((Integer)this.yawStepLimit.atx()).intValue());
            float f9;
            if (f7 <= (float)((Integer)this.yawStepLimit.atx()).intValue()) {
               f9 = fs[0];
               this.bm = true;
               this.vec = null;
            } else {
               f9 = f5 + f8;
               this.bm = false;
            }

            fs[0] = f9;
         } else {
            this.bm = true;
            this.vec = null;
         }

         this.ajM(fs[0], fs[1]);
      }

      if ((this.ajO() || !this.bm) && ((AutoCrystalNonePlaceMode)this.rotate.atx()).afx()) {
         return;
      }

      Hand hand = this.te();
      if (this.damageDataAttackTarget != null) {
         this.pos = this.damageDataAttackTarget.blockPos();
         this.o = this.damageDataAttackTarget.damage();
         this.p = this.damageDataAttackTarget.selfDamage();
         if (this.bl) {
            this.sp((EndCrystalEntity)this.damageDataAttackTarget.damageData(), hand);
            this.stopwatchImpl.afr(29432, 5265492, (byte)40);
         }
      }

      boolean flag = this.stopwatch2.afq((Number)this.placeDelay.atx());
      if (damageDataAttackTarget3 == null || !flag) {
         return;
      }

      if (!this.sX((BlockPos)damageDataAttackTarget3.damageData())) {
         if (damageDataAttackTarget3.support() == null) {
            return;
         }

         if (!this.stopwatch4.afq((Number)this.baseDelay.atx())) {
            return;
         }

         damageDataAttackTarget3.support().avN((Boolean)this.baseRotate.atx(), false, 3.0);
         this.stopwatch4.afr(29432, 5265492, (byte)40);
      }

      this.pos = (BlockPos)damageDataAttackTarget3.damageData();
      this.o = damageDataAttackTarget3.damage();
      this.p = damageDataAttackTarget3.selfDamage();
      this.st((BlockPos)damageDataAttackTarget3.damageData(), hand);
      this.stopwatch2.afr(29432, 5265492, (byte)40);
   }

   @EventListener
   public void onRun(RunEvent runEvent) {
      if (client.player != null) {
         Hand hand = this.te();
         if (((Float)this.attackDelay.atx()).floatValue() > 0.0) {
            float f = 50.0F / Math.max(1.0F, ((Integer)this.attackFactor.atx()).intValue());
            if (this.damageDataAttackTarget != null && this.stopwatchImpl.afq((Float)this.attackDelay.atx() * f)) {
               this.sp((EndCrystalEntity)this.damageDataAttackTarget.damageData(), hand);
               this.stopwatchImpl.afr(29432, 5265492, (byte)40);
            }
         }
      }
   }

   @EventListener(priority = Integer.MAX_VALUE)
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof BundleS2CPacket) {
            Iterator iterator2 = ((BundleS2CPacket)packet2).getPackets().iterator();

            while (iterator2.hasNext()) {
               Packet packet3 = (Packet)iterator2.next();
               this.sk(packet3);
            }
         } else {
            this.sk(channelRead0Event.getPacket());
         }
      }
   }

   private void sk(Packet packet2) {
      if (packet2 instanceof ExplosionS2CPacket) {
         Iterator iterator2 = Lists.newArrayList(client.world.getEntities()).iterator();

         while (iterator2.hasNext()) {
            Entity entity2 = (Entity)iterator2.next();
            if (entity2 instanceof EndCrystalEntity) {
               Entity temp = entity2;
               double temp2 = ((ExplosionS2CPacket)packet2).getX();
               double d2 = ((ExplosionS2CPacket)packet2).getZ();
               double d3 = ((ExplosionS2CPacket)packet2).getY();
               double d4 = temp2;
               if (temp.squaredDistanceTo(d4, d3, d2) < 144.0) {
                  MinecraftClient client2 = client;
                  final Entity entity3 = entity2;
                  Runnable runnable = new Runnable() {
                     @Override
                     public void run() {
                        AutoCrystal.to(entity3);
                     }
                  };
                  client2.executeSync(runnable);
                  Map map5 = this.map4;
                  Integer integer = entity2.getId();
                  map5.remove(integer);
                  Map map6 = this.map2;
                  Integer integer2 = entity2.getId();
                  Long longValue2 = (Long)map6.remove(integer2);
                  if (longValue2 != null) {
                     Deque deque2 = this.deque;
                     Long longValue3 = System.currentTimeMillis() - longValue2;
                     deque2.add(longValue3);
                  }
               }
            }
         }
      }

      if (packet2 instanceof PlaySoundS2CPacket
         && ((PlaySoundS2CPacket)packet2).getSound().value() == SoundEvents.ENTITY_GENERIC_EXPLODE.value()
         && ((PlaySoundS2CPacket)packet2).getCategory() == SoundCategory.BLOCKS) {
         Iterator iterator3 = Lists.newArrayList(client.world.getEntities()).iterator();

         while (iterator3.hasNext()) {
            Entity entity4 = (Entity)iterator3.next();
            if (entity4 instanceof EndCrystalEntity) {
               Entity entity5 = entity4;
               double d5 = ((PlaySoundS2CPacket)packet2).getX();
               double d6 = ((PlaySoundS2CPacket)packet2).getZ();
               double d7 = ((PlaySoundS2CPacket)packet2).getY();
               double d8 = d5;
               if (entity5.squaredDistanceTo(d8, d7, d6) < 144.0) {
                  MinecraftClient client3 = client;
                  final Entity entity6 = entity4;
                  Runnable runnable2 = new Runnable() {
                     @Override
                     public void run() {
                        AutoCrystal.tn(entity6);
                     }
                  };
                  client3.executeSync(runnable2);
                  Map map7 = this.map4;
                  Integer integer3 = entity4.getId();
                  map7.remove(integer3);
                  Map map8 = this.map2;
                  Integer integer4 = entity4.getId();
                  Long longValue4 = (Long)map8.remove(integer4);
                  if (longValue4 != null) {
                     Deque deque3 = this.deque;
                     Long longValue5 = System.currentTimeMillis() - longValue4;
                     deque3.add(longValue5);
                  }
               }
            }
         }
      }

      if (packet2 instanceof EntitiesDestroyS2CPacket) {
         IntListIterator intListIterator = ((EntitiesDestroyS2CPacket)packet2).getEntityIds().iterator();

         while (intListIterator.hasNext()) {
            int n2 = (Integer)intListIterator.next();
            Map map9 = this.map4;
            Integer integer5 = n2;
            map9.remove(integer5);
            map9 = this.map2;
            Integer integer6 = n2;
            Long longValue6 = (Long)map9.remove(integer6);
            if (longValue6 != null) {
               Deque deque4 = this.deque;
               Long longValue7 = System.currentTimeMillis() - longValue6;
               deque4.add(longValue7);
            }
         }
      }
   }

   @EventListener
   public void onAddEntity_2(AddEntity_2Event addEntity2Event) {
      Entity entity2 = addEntity2Event.getEntity();
      if (!(entity2 instanceof EndCrystalEntity)) {
         return;
      }

      EndCrystalEntity endCrystalEntity = (EndCrystalEntity)entity2;
      Vec3d vec = endCrystalEntity.getPos();
      BlockPos pos2 = BlockPos.ofFloored(vec.add(0.0, -1.0, 0.0));
      Long long2 = (Long)this.map3.remove(pos2);
      this.bl = long2 != null;
      if (this.bl) {
         this.unidentifiedHelper11.Ln();
      }

      if (!this.stopwatchImpl.afq((Number)this.instantDelay.atx())) {
         return;
      }

      if (!(Boolean)this.instant.atx()) {
         return;
      }

      if (this.bl && this.sd() != null) {
         Hand hand = this.te();
         this.sq(endCrystalEntity, hand);
         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
         if (this.sequential.atx() == AutoCrystalNormalStrictMode.NORMAL) {
            this.ss(hand);
         }

         return;
      }

      if (!(Boolean)this.instantCalc.atx()) {
         return;
      }

      if (!this.sC(vec)) {
         return;
      }

      double d = this.sw(client.player, vec, (Boolean)this.blockDestruction.atx(), (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0, false);
      if (this.sO(d)) {
         return;
      }

      Iterator iterator2 = client.world.getEntities().iterator();
      while (iterator2.hasNext()) {
         Entity entity3 = (Entity)iterator2.next();
         if (entity3 == null || !entity3.isAlive() || entity3 == client.player || !this.sW(entity3)) {
            continue;
         }

         if (Managers.friendManager.awt(entity3.getName())) {
            continue;
         }

         if (entity3 instanceof LivingEntity && (Boolean)this.ignoreNakeds.atx() && WorldUtils.NO((LivingEntity)entity3)) {
            continue;
         }

         if (vec.squaredDistanceTo(entity3.getPos()) > 144.0) {
            continue;
         }

         if (client.player.squaredDistanceTo(entity3) > (double)((Float)this.enemyRange.atx() * (Float)this.enemyRange.atx())) {
            continue;
         }

         double d2 = this.sw(entity3, vec, (Boolean)this.blockDestruction.atx(), (Integer)this.extrapolationTicks.atx(), (Boolean)this.assumeBestArmor.atx());
         DamageDataAttackTarget damageDataAttackTarget3 = new DamageDataAttackTarget(endCrystalEntity, entity3, d2, d, endCrystalEntity.getBlockPos().down(), false, null);
         boolean flag;
         if (d2 > (double)((Float)this.instantDamage.atx()).floatValue()) {
            flag = true;
         } else if (this.damageDataAttackTarget != null && d2 >= this.damageDataAttackTarget.damage() && (Boolean)this.instantMax.atx()) {
            flag = true;
         } else {
            flag = entity3 instanceof LivingEntity && this.sQ(damageDataAttackTarget3, (LivingEntity)entity3);
         }

         this.bl = flag;
         if (!this.bl) {
            continue;
         }

         Hand hand2 = this.te();
         this.sq(endCrystalEntity, hand2);
         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
         if (this.sequential.atx() == AutoCrystalNormalStrictMode.NORMAL) {
            this.ss(hand2);
         }

         return;
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null) {
         if (sendImmediatelyEvent.getPacket() instanceof UpdateSelectedSlotC2SPacket) {
            this.stopwatch3.afr(29432, 5265492, (byte)40);
         }
      }
   }

   public boolean sn() {
      boolean flag;
      if (this.damageDataAttackTarget != null) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean so() {
      boolean flag;
      if (this.damageDataAttackTarget2 != null && this.sU()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public void sp(EndCrystalEntity endCrystalEntity, Hand hand) {
      if (!this.sS(hand)) {
         if (AntiWeakness.INSTANCE.js()) {
            this.sq(endCrystalEntity, hand);
            if (this.sequential.atx() == AutoCrystalNormalStrictMode.STRICT) {
               this.ss(hand);
            }
         }
      }
   }

   private void sq(EndCrystalEntity endCrystalEntity, Hand hand) {
      if (((AutoCrystalNonePlaceMode)this.rotate.atx()).afA()) {
         Vec3d vec2;
         if (this.sF(endCrystalEntity.getBoundingBox()) != null) {
            vec2 = this.sF(endCrystalEntity.getBoundingBox());
         } else {
            vec2 = endCrystalEntity.getPos().add(0.0, 0.5, 0.0);
         }

         RotationUtils.nI(vec2);
      }

      this.sr(endCrystalEntity.getId(), hand);
   }

   private void sr(int n2, Hand hand) {
      ItemStack stack = Managers.inventoryTracker.JW();
      if (!HitResultWorldHelper.Sq().SA() && stack.getItem() instanceof SwordItem && EnchantmentUtils.VL(stack, Enchantments.KNOCKBACK) > 0) {
         ClientPlayerEntity temp = client.player;
         Vec3d vec2 = client.player.getVelocity().multiply(0.6, 1.0, 0.6);
         temp.setVelocity(vec2);
      }

      this.stopwatch.afr(29432, 5265492, (byte)40);
      Hand hand2;
      if (hand != null) {
         hand2 = hand;
      } else {
         hand2 = Hand.MAIN_HAND;
      }

      hand = hand2;
      EndCrystalEntity endCrystalEntity = a(client.world);
      endCrystalEntity.setId(n2);
      EndCrystalEntity endCrystalEntity2 = endCrystalEntity;
      boolean flag = client.player.isSneaking();
      PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.attack((Entity)endCrystalEntity2, flag);
      Managers.serverManager.iO('\uf560', playerInteractEntityC2SPacket);
      Sprint.a = true;
      ((SwingMode)this.breakSwing.atx()).avX(hand);
      Map map5 = this.map2;
      Integer temp2 = n2;
      Long longValue2 = System.currentTimeMillis();
      Integer integer = temp2;
      map5.put(integer, longValue2);
      Map map6 = this.map4;
      Integer integer2 = n2;
      Integer integer3 = (Integer)map6.get(integer2);
      if (integer3 != null) {
         Map map7 = this.map4;
         temp2 = n2;
         Integer integer4 = integer3 + 1;
         Integer integer5 = temp2;
         map7.replace(integer5, integer4);
      } else {
         Map map8 = this.map4;
         temp2 = n2;
         Integer integer6 = 1;
         Integer integer7 = temp2;
         map8.put(integer7, integer6);
      }

      Managers.antiCheatManager.stopwatchImpl.afr(29432, 5265492, (byte)40);
      PlacementUtils.d = true;
   }

   private void ss(Hand hand) {
      DamageDataAttackTarget damageDataAttackTarget3 = this.damageDataAttackTarget2;
      if (damageDataAttackTarget3 != null) {
         if (damageDataAttackTarget3.support() == null) {
            int n2;
            if (FastLatency.Ov().awg()) {
               n2 = (int)FastLatency.Ov().Oz();
            } else {
               n2 = Managers.serverManager.iS();
            }

            if (!Managers.serverManager.iY() || n2 >= 50) {
               this.st(damageDataAttackTarget3.blockPos(), hand);
            }
         }
      }
   }

   private void st(BlockPos pos2, Hand hand) {
      if (!this.ajO() && this.bm || !((AutoCrystalNonePlaceMode)this.rotate.atx()).afx()) {
         if (AntiWeakness.INSTANCE.js()) {
            this.su(pos2, hand);
         }
      }
   }

   public void su(BlockPos pos2, Hand hand) {
      if (!this.sT()) {
         Direction direction = PlacementUtils.Fi(pos2, ((Float)this.placeRange.atx()).floatValue());
         if (direction != null) {
            Vec3d vec2;
            if (this.sI(pos2, direction) != null) {
               vec2 = this.sI(pos2, direction);
            } else {
               vec2 = pos2.toCenterPos();
            }

            BlockHitResult blockHitResult = a(vec2, direction, pos2);
            if (this.te() != null) {
               this.sv(blockHitResult, hand);
               Map map5 = this.map3;
               Long longValue2 = System.currentTimeMillis();
               map5.put(pos2, longValue2);
            } else if (this.sz((AutoCrystalNormalSilentMode)this.swap.atx()) && hand != Hand.OFF_HAND && this.te() == null
               || this.sA((SwapMode)this.swapAction.atx())) {
               StackSlotHelper stackSlotHelper2 = this.stackSlotHelper;
               if (stackSlotHelper2 == null || !stackSlotHelper2.LK().LL()) {
                  return;
               }

               int n2 = Managers.inventoryTracker.JS();
               stackSlotHelper2.LO(n2);
               if (this.swap.atx() == AutoCrystalNormalSilentMode.NORMAL && this.swapAction.atx() == SwapMode.Hotbar) {
                  PlayerInventory temp = client.player.getInventory();
                  int n3 = stackSlotHelper2.LM();
                  temp.selectedSlot = n3;
               }

               this.sv(blockHitResult, Hand.MAIN_HAND);
               Map map6 = this.map3;
               Long longValue3 = System.currentTimeMillis();
               map6.put(pos2, longValue3);
               if (this.swap.atx() == AutoCrystalNormalSilentMode.SILENT) {
                  stackSlotHelper2.LP(n2);
               }
            }
         }
      }
   }

   private void sv(BlockHitResult blockHitResult, Hand hand) {
      if (hand != null) {
         this.stopwatch.afr(29432, 5265492, (byte)40);
         if (((AutoCrystalNonePlaceMode)this.rotate.atx()).afA()) {
            RotationUtils.nI(blockHitResult.getPos());
         }

         final BlockHitResult blockHitResult2 = blockHitResult;
         final Hand hand2 = hand;
         Managers.serverManager.iQ(new SequencedPacketCreator() {
            public Packet predict(int blockHitResult) {
               return AutoCrystal.tm(hand2, blockHitResult2, blockHitResult);
            }
         });
         ((SwingMode)this.placeSwing.atx()).avX(hand);
      }
   }

   private double sw(Entity entity2, Vec3d vec2, boolean flag, int n2, boolean flag2) {
      return Vec3dEntityUtils.vM(entity2, vec2, flag, n2, flag2);
   }

   private double sx(Entity entity2, BlockPos pos2, boolean flag, int n2, boolean flag2) {
      return Vec3dEntityUtils.vN(entity2, this.sV(pos2), flag, n2, flag2, pos2);
   }

   private double sy(Entity entity2, BlockPos pos2, boolean flag, int n2, boolean flag2) {
      return Vec3dEntityUtils.vS(entity2, this.sV(pos2), flag, new BlockPosHashSet(this, pos2), n2, flag2);
   }

   private boolean sz(AutoCrystalNormalSilentMode autoCrystalNormalSilentMode) {
      if (autoCrystalNormalSilentMode == AutoCrystalNormalSilentMode.OFF) {
         return false;
      }

      boolean flag;
      if (autoCrystalNormalSilentMode != AutoCrystalNormalSilentMode.SILENT && MainHand.hz().hB()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private boolean sA(SwapMode swapMode) {
      boolean flag;
      if (swapMode != SwapMode.Screen && swapMode != SwapMode.Pick) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private DamageDataAttackTarget sB(List list) {
      if (list.isEmpty()) {
         return null;
      }

      DamageDataAttackTarget damageDataAttackTarget3 = null;
      Iterator iterator2 = list.iterator();
      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (!(entity2 instanceof EndCrystalEntity)) {
            continue;
         }

         EndCrystalEntity endCrystalEntity = (EndCrystalEntity)entity2;
         if (!entity2.isAlive()) {
            continue;
         }

         Long long2 = (Long)this.map2.get(Integer.valueOf(entity2.getId()));
         boolean flag = long2 != null && long2.longValue() < (long)this.tg();
         if (endCrystalEntity.age < (Integer)this.ticksExisted.atx()) {
            continue;
         }

         if (flag && (Boolean)this.inhibit.atx()) {
            continue;
         }

         if (!this.sD(endCrystalEntity)) {
            continue;
         }

         double d = this.sw(client.player, entity2.getPos(), (Boolean)this.blockDestruction.atx(), (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0, false);
         boolean flag2 = this.sO(d);
         if (flag2 && !(Boolean)this.safetyOverride.atx()) {
            continue;
         }

         Iterator iterator3 = list.iterator();
         while (iterator3.hasNext()) {
            Entity entity3 = (Entity)iterator3.next();
            if (entity3 == null || !entity3.isAlive() || entity3 == client.player || !this.sW(entity3)) {
               continue;
            }

            if (Managers.friendManager.awt(entity3.getName())) {
               continue;
            }

            if (entity3 instanceof LivingEntity && (Boolean)this.ignoreNakeds.atx() && WorldUtils.NO((LivingEntity)entity3)) {
               continue;
            }

            if (entity2.squaredDistanceTo(entity3) > 144.0) {
               continue;
            }

            if (client.player.squaredDistanceTo(entity3) > (double)((Float)this.enemyRange.atx() * (Float)this.enemyRange.atx())) {
               continue;
            }

            if (entity3 instanceof PlayerEntity && this.sK((PlayerEntity)entity3, entity2.getPos())) {
               continue;
            }

            double d2 = this.sw(entity3, entity2.getPos(), (Boolean)this.blockDestruction.atx(), (Integer)this.extrapolationTicks.atx(), (Boolean)this.assumeBestArmor.atx());
            if (entity3 instanceof LivingEntity) {
               boolean flag3 = this.sL(flag2, d2, (LivingEntity)entity3);
               if (this.sJ(d2, d, flag3) || flag3) {
                  continue;
               }
            }

            DamageDataAttackTarget damageDataAttackTarget4 = new DamageDataAttackTarget(endCrystalEntity, entity3, d2, d, endCrystalEntity.getBlockPos().down(), false, null);
            if (damageDataAttackTarget3 == null || d2 > damageDataAttackTarget3.damage()) {
               damageDataAttackTarget3 = damageDataAttackTarget4;
            }
         }
      }

      return damageDataAttackTarget3 != null && !this.sM(damageDataAttackTarget3) ? damageDataAttackTarget3 : null;
   }

   private boolean sC(Vec3d vec2) {
      Vec3d vec3 = this.sE(vec2);
      double d2 = this.ti().distanceTo(vec3);
      if (WorldUtils.Nj(vec3)) {
         boolean flag;
         if (d2 <= ((Float)this.breakRange.atx()).floatValue()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         boolean flag2;
         if (d2 <= ((Float)this.breakWallRange.atx()).floatValue()) {
            flag2 = true;
         } else {
            flag2 = false;
         }

         return flag2;
      }
   }

   private boolean sD(EndCrystalEntity endCrystalEntity) {
      Vec3d vec2 = this.sF(endCrystalEntity.getBoundingBox());
      double d2 = this.ti().distanceTo(vec2);
      if (WorldUtils.Nk(endCrystalEntity)) {
         boolean flag;
         if (d2 <= ((Float)this.breakRange.atx()).floatValue()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         boolean flag2;
         if (d2 <= ((Float)this.breakWallRange.atx()).floatValue()) {
            flag2 = true;
         } else {
            flag2 = false;
         }

         return flag2;
      }
   }

   public Vec3d sE(Vec3d vec2) {
      Box box = a.offset(vec2);
      return BoxUtils.jy(this.ti(), box);
   }

   public Vec3d sF(Box box) {
      Vec3d vec2 = BoxUtils.jz(new Box(box.minX + 0.5, box.minY, box.minZ + 0.5, box.maxX - 0.5, box.minY, box.maxZ - 0.5));
      return this.ti().distanceTo(vec2) <= ((Float)this.breakRange.atx()).floatValue() ? vec2 : BoxUtils.jz(box);
   }

   private DamageDataAttackTarget sG(List list, List list2) {
      if (list.isEmpty() || list2.isEmpty()) {
         return null;
      }

      ArrayList<DamageDataAttackTarget> arrayList = new ArrayList<>();
      DamageDataAttackTarget damageDataAttackTarget3 = null;
      Iterator iterator2 = list.iterator();
      while (iterator2.hasNext()) {
         BlockPos pos2 = (BlockPos)iterator2.next();
         if (!this.sY(pos2)) {
            continue;
         }

         boolean flag = this.sX(pos2);
         boolean flag2 = false;
         BlockPosBlockItemHelper blockPosBlockItemHelper = null;
         if (!flag) {
            if (!(Boolean)this.support.atx()) {
               continue;
            }

            if (!this.stopwatch4.afq((Number)this.baseDelay.atx())) {
               continue;
            }

            blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos2, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.baseSwapAction.atx());
            if (!blockPosBlockItemHelper.avK((double)((Float)this.placeRange.atx()).floatValue(), false)) {
               continue;
            }

            flag2 = true;
         }

         double d = this.sx(client.player, pos2, (Boolean)this.blockDestruction.atx(), (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0, false);
         boolean flag3 = this.sO(d);
         if (flag3 && !(Boolean)this.safetyOverride.atx()) {
            continue;
         }

         Iterator iterator3 = list2.iterator();
         while (iterator3.hasNext()) {
            Entity entity2 = (Entity)iterator3.next();
            if (entity2 == null || !entity2.isAlive() || entity2 == client.player || !this.sW(entity2)) {
               continue;
            }

            if (Managers.friendManager.awt(entity2.getName())) {
               continue;
            }

            if (entity2 instanceof LivingEntity && (Boolean)this.ignoreNakeds.atx() && WorldUtils.NO((LivingEntity)entity2)) {
               continue;
            }

            if (pos2.getSquaredDistance(entity2.getPos()) > 144.0) {
               continue;
            }

            if (client.player.squaredDistanceTo(entity2) > (double)((Float)this.enemyRange.atx() * (Float)this.enemyRange.atx())) {
               continue;
            }

            boolean flag4 = false;
            if ((Boolean)this.prePlacement.atx() && entity2 instanceof PlayerEntity) {
               PlayerEntity player2 = (PlayerEntity)entity2;
               if (!BlockUtils.TP(player2.getBlockPos()) && WorldUtils.Nb(player2, pos2.up(), false, false) && this.tb(pos2.up()) == null) {
                  flag4 = true;
               }
            }

            if ((Boolean)this.antiSurround.atx() && entity2 instanceof PlayerEntity) {
               PlayerEntity player3 = (PlayerEntity)entity2;
               if (!BlockUtils.TP(player3.getBlockPos())) {
                  HashSet hashSet = new HashSet();
                  BlockPos pos3 = BlockPosBoxMultitask.alv().alG();
                  if (BlockPosBoxMultitask.alv().awg() && pos3 != null) {
                     hashSet.add(pos3);
                  }

                  Iterator iterator4 = hashSet.iterator();
                  while (iterator4.hasNext()) {
                     BlockPos pos4 = (BlockPos)iterator4.next();
                     boolean flag5 = WorldUtils.Nb(player3, pos4, true, true);
                     if (pos2.equals(pos4) && flag5 && (Boolean)this.cevBomber.atx()) {
                        double d2 = Vec3dEntityUtils.vS(player3, this.sV(pos4), (Boolean)this.blockDestruction.atx(), Set.of(pos4), (Integer)this.extrapolationTicks.atx(), (Boolean)this.assumeBestArmor.atx());
                        if (!(d2 < (double)((Float)this.cevDamage.atx()).floatValue())
                           && this.tb(pos4.up()) == null
                           && !((double)BlockPosBoxMultitask.alv().blockPosPlayerHelper2.d < (Double)this.miningProgress.atx() / 100.0)) {
                           this.th(player3, pos4);
                           break;
                        }
                     }

                     if (!WorldUtils.Nb(player3, pos4, false, false)) {
                        continue;
                     }

                     Direction[] directions = BoxUtils.a_;
                     for (int n2 = 0; n2 < directions.length; n2++) {
                        BlockPos pos5 = pos4.offset(directions[n2]);
                        if (pos2.equals(pos5.down())
                           && !((double)BlockPosBoxMultitask.alv().blockPosPlayerHelper2.d < (Double)this.miningProgress.atx() / 100.0)
                           && this.tb(pos2.up()) == null) {
                           flag4 = true;
                           break;
                        }

                        BlockPos pos6 = pos4.down();
                        Direction[] directions2 = BoxUtils.b;
                        for (int n3 = 0; n3 < directions2.length; n3++) {
                           BlockPos pos7 = pos6.offset(directions2[n3]);
                           if (pos2.equals(pos7.down())
                              && !((double)BlockPosBoxMultitask.alv().blockPosPlayerHelper2.d < (Double)this.miningProgress.atx() / 100.0)
                              && this.tb(pos2.up()) == null) {
                              flag4 = true;
                              break;
                           }
                        }
                     }
                  }
               }
            }

            if (entity2 instanceof PlayerEntity && this.sK((PlayerEntity)entity2, pos2.up().toBottomCenterPos())) {
               continue;
            }

            double d3 = this.sx(entity2, pos2, (Boolean)this.blockDestruction.atx(), (Integer)this.extrapolationTicks.atx(), (Boolean)this.assumeBestArmor.atx());
            if (entity2 instanceof LivingEntity) {
               boolean flag6 = this.sL(flag3, d3, (LivingEntity)entity2);
               if (flag6 || !flag4 && this.sJ(d3, d, flag6)) {
                  continue;
               }
            }

            if (!flag2) {
               DamageDataAttackTarget damageDataAttackTarget4 = new DamageDataAttackTarget(pos2, entity2, d3, d, pos2, flag4, null);
               arrayList.add(damageDataAttackTarget4);
               if (damageDataAttackTarget3 == null
                  || d3 > damageDataAttackTarget3.damage()
                  || damageDataAttackTarget3.support() != null && d3 > (double)((Float)this.maxOverride.atx()).floatValue()) {
                  damageDataAttackTarget3 = damageDataAttackTarget4;
               }
            } else if (damageDataAttackTarget3 == null || d3 > damageDataAttackTarget3.damage()) {
               damageDataAttackTarget3 = new DamageDataAttackTarget(pos2, entity2, d3, d, pos2, flag4, blockPosBlockItemHelper);
            }
         }
      }

      if (damageDataAttackTarget3 != null && !this.sM(damageDataAttackTarget3)) {
         return damageDataAttackTarget3;
      }

      if (!(Boolean)this.antiSurround.atx()) {
         return null;
      }

      Optional<DamageDataAttackTarget> optional = arrayList.stream().filter(DamageDataAttackTarget::antiSurround).min(Comparator.comparingDouble(AutoCrystal::tl));
      return optional.orElse(null);
   }

   private boolean sH(BlockPos pos2) {
      if (!this.sC(pos2.up().toBottomCenterPos())) {
         return false;
      }

      Direction direction = PlacementUtils.Fi(pos2, ((Float)this.placeRange.atx()).floatValue());
      if (direction == null) {
         return false;
      }

      Vec3d vec2 = PlacementUtils.Fn(pos2, direction);
      double d2 = this.ti().distanceTo(vec2);
      if (WorldUtils.Nj(vec2)) {
         boolean flag;
         if (d2 <= ((Float)this.placeRange.atx()).floatValue()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         boolean flag2;
         if (d2 <= ((Float)this.placeWallRange.atx()).floatValue()) {
            flag2 = true;
         } else {
            flag2 = false;
         }

         return flag2;
      }
   }

   private Vec3d sI(BlockPos pos2, Direction direction) {
      return direction == null ? null : PlacementUtils.Fn(pos2, direction);
   }

   private boolean sJ(double d2, double d3, boolean flag) {
      if (!flag && (Boolean)this.safetyOverride.atx()) {
         return false;
      }

      boolean flag2;
      if (d2 / d3 < ((Float)this.damageRatio.atx()).floatValue()) {
         flag2 = true;
      } else {
         flag2 = false;
      }

      return flag2;
   }

   private boolean sK(PlayerEntity player2, Vec3d vec2) {
      if (this.shieldMode.atx() == AutoCrystalNoneBypassMode.None) {
         return false;
      }

      DamageSource damageSource = client.world.getDamageSources().badRespawnPoint(vec2);
      return player2.blockedByShield(damageSource);
   }

   private boolean sL(boolean flag, double d2, LivingEntity living) {
      if ((Boolean)this.forcePlace.atx() && !(BoxEntityUtils.kE(living) > (Float)this.minHealth.atx()) && !PistonCrystal.z().awg()) {
         if ((Boolean)this.armorBreaker.atx()) {
            Iterator iterator2 = living.getArmorItems().iterator();

            while (iterator2.hasNext()) {
               ItemStack stack = (ItemStack)iterator2.next();
               int n2 = stack.getDamage();
               int n3 = stack.getMaxDamage();
               if ((float)(n3 - n2) / n3 * 100.0F > (Float)this.armorScale.atx()) {
                  return false;
               }
            }
         }

         boolean flag2;
         if ((Boolean)this.safetyOverride.atx() && flag && d2 < BoxEntityUtils.kE(living) + 0.5) {
            flag2 = true;
         } else {
            flag2 = false;
         }

         return flag2;
      } else {
         return false;
      }
   }

   private boolean sM(DamageDataAttackTarget damageDataAttackTarget3) {
      double d2 = ((Float)this.minDamage.atx()).floatValue();
      Entity entity2 = damageDataAttackTarget3.attackTarget();
      if (entity2 instanceof LivingEntity) {
         d2 = this.sN((LivingEntity)entity2);
         if (this.sQ(damageDataAttackTarget3, (LivingEntity)entity2)) {
            d2 = 1.5;
         }
      }

      boolean flag;
      if (damageDataAttackTarget3.damage() < d2) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private double sN(LivingEntity living) {
      if ((Boolean)this.forcePlace.atx() && BoxEntityUtils.kE(living) <= (Float)this.minHealth.atx() && !PistonCrystal.z().awg()) {
         return ((Float)this.forceDamage.atx()).floatValue();
      }

      if ((Boolean)this.armorBreaker.atx()) {
         Iterator iterator2 = living.getArmorItems().iterator();

         while (iterator2.hasNext()) {
            ItemStack stack = (ItemStack)iterator2.next();
            int n2 = stack.getDamage();
            int n3 = stack.getMaxDamage();
            if ((float)(n3 - n2) / n3 * 100.0F < (Float)this.armorScale.atx()) {
               return 1.5;
            }
         }
      }

      return ((Float)this.minDamage.atx()).floatValue();
   }

   private boolean sO(double d2) {
      if (!client.player.isCreative()) {
         float f = client.player.getHealth() + client.player.getAbsorptionAmount();
         if ((Boolean)this.safety.atx() && d2 >= f - 0.5F) {
            return true;
         }

         boolean flag;
         if (d2 > ((Float)this.maxLocalDamage.atx()).floatValue()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         return false;
      }
   }

   private boolean sP(double d2, LivingEntity living) {
      if (living instanceof PlayerEntity) {
         float f = BoxEntityUtils.kE((PlayerEntity)living);
         if (f <= 2.0F && f - d2 < 0.5) {
            long l = Managers.entityEntityStatusS2CPacketManager.acq((PlayerEntity)living);
            if (l != -1L) {
               boolean flag;
               if (System.currentTimeMillis() - l <= 500L) {
                  flag = true;
               } else {
                  flag = false;
               }

               return flag;
            }
         }
      }

      return false;
   }

   private boolean sQ(DamageDataAttackTarget damageDataAttackTarget3, LivingEntity living) {
      return this.sR(damageDataAttackTarget3.damage(), living);
   }

   private boolean sR(double d2, LivingEntity living) {
      if ((Boolean)this.lethalDamageTick.atx() && this.stopwatchImpl.afq(500)) {
         return true;
      }

      if ((Boolean)this.lethalTotem.atx() && this.sP(d2, living)) {
         return true;
      }

      float f = living.getHealth() + living.getAbsorptionAmount();
      if (d2 * (1.0F + (Float)this.lethalMultiplier.atx()) >= f + 0.5F) {
         return true;
      }

      if ((Boolean)this.shulkers.atx() && living instanceof PlayerEntity) {
         Iterator iterator2 = this.td(3.0, living.getPos()).iterator();

         while (iterator2.hasNext()) {
            BlockPos pos2 = (BlockPos)iterator2.next();
            if (client.world.getBlockState(pos2).getBlock() instanceof ShulkerBoxBlock) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean sS(Hand hand) {
      if (!this.stopwatch3.afq((Float)this.swapPenalty.atx() * 25.0F)) {
         return true;
      } else {
         return hand == Hand.MAIN_HAND ? this.sT() : false;
      }
   }

   private boolean sT() {
      boolean flag;
      if (((Boolean)this.whileUsing.atx() || !this.RG()) && ((Boolean)this.whileMining.atx() || !client.interactionManager.isBreakingBlock()) && !Blink.bm()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public boolean sU() {
      if (!this.sT() && this.sz((AutoCrystalNormalSilentMode)this.swap.atx()) && this.stackSlotHelper != null && this.stackSlotHelper.LL()) {
         return true;
      }

      boolean flag;
      if (this.te() != null) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Vec3d sV(BlockPos pos2) {
      return pos2.up().toBottomCenterPos();
   }

   private boolean sW(Entity entity2) {
      boolean flag;
      if ((!(entity2 instanceof PlayerEntity) || !(Boolean)this.players.atx())
         && (!BoxEntityUtils.kF(entity2) || !(Boolean)this.monsters.atx())
         && (!BoxEntityUtils.kH(entity2) || !(Boolean)this.neutrals.atx())
         && (!BoxEntityUtils.kI(entity2) || !(Boolean)this.animals.atx())) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public boolean sX(BlockPos pos2) {
      BlockState state = client.world.getBlockState(pos2);
      BlockState temp = state;
      Block block2 = Blocks.OBSIDIAN;
      if (!temp.isOf(block2)) {
         temp = state;
         Block block3 = Blocks.BEDROCK;
         if (!temp.isOf(block3)) {
            return false;
         }
      }

      return true;
   }

   public boolean sY(BlockPos pos2) {
      boolean flag;
      if (this.sZ(pos2) && this.sH(pos2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean sZ(BlockPos pos2) {
      BlockPos pos3 = pos2.up();
      BlockState state = client.world.getBlockState(pos3);
      if (this.placements.atx() == AutoCrystalNativeProtocolMode.PROTOCOL) {
         ClientWorld temp = client.world;
         BlockPos pos4 = pos3.up();
         if (!temp.isAir(pos4)) {
            return false;
         }
      }

      EndCrystalEntity endCrystalEntity = this.tb(pos3);
      if (!client.world.isAir(pos3)) {
         BlockState state2 = state;
         Block block2 = Blocks.FIRE;
         if (!state2.isOf(block2)) {
            return false;
         }

         state2 = state;
         Block block3 = Blocks.FIRE;
         if (state2.isOf(block3) && endCrystalEntity == null || endCrystalEntity != null && !endCrystalEntity.getBlockPos().equals(pos3)) {
            return false;
         }
      }

      boolean flag;
      if (this.ta(pos2.up()) && this.ta(pos2.up(2))) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean ta(BlockPos pos2) {
      Box box = new Box(pos2);
      List list = client.world.getOtherEntities((Entity)null, box);
      Iterator iterator2 = list.iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2.isAlive() && (!(entity2 instanceof ItemEntity) || !(Boolean)this.ignoreItem.atx() || entity2.age > 10)) {
            if (!(entity2 instanceof EndCrystalEntity)) {
               return false;
            }

            EndCrystalEntity endCrystalEntity = (EndCrystalEntity)entity2;
            Integer integer = (Integer)this.map4.get(endCrystalEntity.getId());
            boolean flag = (Boolean)this.blockDestruction.atx();
            int n = (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0;
            double d = this.sw(client.player, endCrystalEntity.getPos(), flag, n, false);
            if ((Boolean)this.safety.atx() && this.sO(d)) {
               return false;
            }

            if (!this.sD(endCrystalEntity)) {
               return false;
            }

            if (integer != null && (float)integer.intValue() > (Float)this.attackLimit.atx() * 10.0F) {
               return false;
            }
         }
      }

      return true;
   }

   private EndCrystalEntity tb(BlockPos pos2) {
      ClientWorld temp = client.world;
      Box box = new Box(pos2);
      Stream stream2 = temp.getOtherEntities((Entity)null, box).stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object pos2) {
            return AutoCrystal.tk((Entity)pos2);
         }
      };
      Stream stream3 = stream2.filter(predicate);
      Comparator comparator = Comparator.comparingDouble(new ToDoubleFunction() {
         @Override
         public double applyAsDouble(Object pos2) {
            return AutoCrystal.tj((Entity)pos2);
         }
      });
      return (EndCrystalEntity)stream3.min(comparator).orElse(null);
   }

   private List tc(Vec3d vec2) {
      double d2 = Math.ceil((Float)this.placeRange.atx() + 1.0F);
      return this.td(d2, vec2);
   }

   private List td(double d2, Vec3d vec2) {
      ArrayList arrayList = new ArrayList();

      for (double d3 = -d2; d3 <= d2; d3++) {
         for (double d4 = -d2; d4 <= d2; d4++) {
            for (double d5 = -d2; d5 <= d2; d5++) {
               BlockPos pos2 = new BlockPos(new Vec3i((int)(vec2.getX() + d3), (int)(vec2.getY() + d4), (int)(vec2.getZ() + d5)));
               arrayList.add(pos2);
            }
         }
      }

      return arrayList;
   }

   private Hand te() {
      ItemStack stack = client.player.getOffHandStack();
      ItemStack stack2 = Managers.inventoryTracker.JW();
      if (stack.getItem() instanceof EndCrystalItem) {
         return Hand.OFF_HAND;
      } else {
         return stack2.getItem() instanceof EndCrystalItem ? Hand.MAIN_HAND : null;
      }
   }

   public float tf() {
      return (Float)this.breakDelay.atx();
   }

   public int tg() {
      if (this.deque.isEmpty()) {
         return 0;
      }

      float f = 0.0F;
      ArrayList arrayList = Lists.newArrayList(this.deque);
      if (!arrayList.isEmpty()) {
         Iterator iterator2 = arrayList.iterator();

         while (iterator2.hasNext()) {
            float f2 = (float)((Long)iterator2.next()).longValue();
            f += f2;
         }

         f /= arrayList.size();
      }

      return (int)f;
   }

   public void th(LivingEntity living, BlockPos pos2) {
      if (living != null && !living.isDead() && this.sH(pos2) && this.sX(pos2)) {
         Vec3d vec = this.sV(pos2);
         boolean flag = (Boolean)this.blockDestruction.atx();
         Set set = Set.of(pos2);
         int n = (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0;
         double d = Vec3dEntityUtils.vS(client.player, vec, flag, set, n, false);
         if (!this.sO(d)) {
            Hand hand = this.te();
            Direction direction = PlacementUtils.Fi(pos2, ((Float)this.placeRange.atx()).floatValue());
            Vec3d vec2 = this.sI(pos2, direction);
            if (vec2 == null) {
               vec2 = pos2.toCenterPos();
            }

            RotationUtils.nI(vec2);
            this.su(pos2, hand);
            this.map.put(pos2, a((float)((Integer)this.fadeTime.atx()).intValue()));
            this.pos = pos2;
         }
      }
   }

   private Vec3d ti() {
      return client.player.getEyePos();
   }

   static double tj(Entity entity2) {
      return client.player.distanceTo(entity2);
   }

   static boolean tk(Entity entity2) {
      return entity2 instanceof EndCrystalEntity;
   }

   static double tl(DamageDataAttackTarget damageDataAttackTarget3) {
      ClientPlayerEntity temp = client.player;
      Vec3d vec2 = damageDataAttackTarget3.blockPos().toCenterPos();
      return temp.squaredDistanceTo(vec2);
   }

   static Packet tm(Hand hand, BlockHitResult blockHitResult, int n2) {
      return new PlayerInteractBlockC2SPacket(hand, blockHitResult, n2);
   }

   static void tn(Entity entity2) {
      ClientWorld temp = client.world;
      int temp2 = entity2.getId();
      RemovalReason removalReason = RemovalReason.DISCARDED;
      int n2 = temp2;
      temp.removeEntity(n2, removalReason);
   }

   static void to(Entity entity2) {
      ClientWorld temp = client.world;
      int temp2 = entity2.getId();
      RemovalReason removalReason = RemovalReason.DISCARDED;
      int n2 = temp2;
      temp.removeEntity(n2, removalReason);
   }

   private void tp() {
      ArrayList arrayList = Lists.newArrayList(client.world.getEntities());
      if ((Boolean)this.place.atx() && this.sU() && AntiWeakness.INSTANCE.js()) {
         this.damageDataAttackTarget2 = this.sG(this.tc(this.ti()), arrayList);
      } else {
         this.damageDataAttackTarget2 = null;
      }
   }

   private Boolean isCpsVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao();
   }

   private Boolean isSuffixVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao();
   }

   private Boolean isBreakMsVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao();
   }

   private Boolean isDamageVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao();
   }

   private Boolean isTargetNameVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao();
   }

   private Boolean isSelfDamageVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VG() && (Boolean)this.renderDamage.atx() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isTargetDamageVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VG() && (Boolean)this.renderDamage.atx() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isRenderDamageVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VG() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isFadeTimeVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VI() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isFadeSpeedVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VH() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isStartFadeVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VH() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSliderSpeedVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VH() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isShrinkVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VH() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isWidthVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VG() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSideVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VG() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isLineVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VG() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBoxModeVisible() {
      boolean flag;
      if (((SliderFadeMode)this.render.atx()).VG() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isRenderVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ao();
   }

   private Boolean isBaseSwapActionVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).An();
   }

   private Boolean isMaxOverrideVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).An();
   }

   private Boolean isBaseDelayVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).An();
   }

   private Boolean isAirPlaceVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).An();
   }

   private Boolean isBaseRotateVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).An();
   }

   private Boolean isSupportVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).An();
   }

   private Boolean isShulkersVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isAnimalsVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isNeutralsVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isMonstersVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isPlayersVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isExtrapolationTicksVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isSelfExtrapolateVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isBlockDestructionVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isSafetyOverrideVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isSafetyVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isLethalDamageTickVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isLethalTotemVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isLethalMultiplierVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isAssumeBestArmorVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isIgnoreItemVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isInstantMaxVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isInstantDamageVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isInstantCalcVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isDamageRatioVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isMaxLocalDamageVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isMinDamageVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isIgnoreNakedsVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isEnemyRangeVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isAsynchronousVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Am();
   }

   private Boolean isArmorScaleVisible() {
      boolean flag;
      if ((Boolean)this.armorBreaker.atx() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Al()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isArmorBreakerVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isResetCrystalVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isMiningProgressVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isCevDamageVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isCevBomberVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isPrePlacementVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isAntiSurroundVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isSurroundFuckerVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isShieldModeVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isSwapPenaltyVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isWhileMiningVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isWhileUsingVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Al();
   }

   private Boolean isYawStepLimitVisible() {
      boolean flag;
      if ((((AutoCrystalNonePlaceMode)this.rotate.atx()).afx() || this.rotate.atx() == AutoCrystalNonePlaceMode.GRIM)
         && this.yawStep.atx() != AutoCrystalFullSemiMode.OFF
         && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ak()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isYawStepVisible() {
      boolean flag;
      if ((((AutoCrystalNonePlaceMode)this.rotate.atx()).afx() || this.rotate.atx() == AutoCrystalNonePlaceMode.GRIM)
         && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ak()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isRotateVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ak();
   }

   private Boolean isBreakSwingVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isBreakWallRangeVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isBreakRangeVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isTicksExistedVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isMinTimeoutVisible() {
      boolean flag;
      if ((Boolean)this.timeout.atx() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBreakTimeoutVisible() {
      boolean flag;
      if ((Boolean)this.timeout.atx() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isTimeoutVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isAttackLimitVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isAttackFactorVisible() {
      boolean flag;
      if (((Float)this.attackDelay.atx()).floatValue() > 0.0 && ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isAttackDelayVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isBreakDelayVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isInhibitVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isInstantDelayVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isInstantVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Aj();
   }

   private Boolean isPlaceSwingVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private Boolean isForceDamageVisible() {
      boolean flag;
      if ((Boolean)this.forcePlace.atx() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isMinHealthVisible() {
      boolean flag;
      if ((Boolean)this.forcePlace.atx() && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isForcePlaceVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private Boolean isSequentialVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private Boolean isPlacementsVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private Boolean isSwapActionVisible() {
      boolean flag;
      if (this.swap.atx() != AutoCrystalNormalSilentMode.OFF && ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSwapVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private Boolean isPlaceWallRangeVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private Boolean isPlaceRangeVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private Boolean isPlaceDelayVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private Boolean isPlaceVisible() {
      return ((AutoCrystalPlaceBreakMode)this.page.atx()).Ai();
   }

   private static BlockHitResult a(Vec3d vec2, Direction direction, BlockPos pos2) {
      return new BlockHitResult(vec2, direction, pos2, false);
   }

   private static Color c(int n2) {
      return new Color(255, 255, 255, n2);
   }

   private static UnidentifiedHelper7 a(float f) {
      return new UnidentifiedHelper7(true, f);
   }

   private static EndCrystalEntity a(World world2) {
      return new EndCrystalEntity(world2, 0.0, 0.0, 0.0);
   }
}
