package pw.hachimi.client.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.AttackTargetPos;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.combat.AutoAnchorVec3dBoxHelper;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.BoxEntityUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.client.utils.world.Vec3dEntityUtils;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoAnchor extends Multitask {
   private static AutoAnchor INSTANCE;
   public final Stopwatch stopwatch = new StopwatchImpl();
   private final Stopwatch stopwatch2 = new StopwatchImpl();
   private final ExecutorService executorService = Executors.newSingleThreadExecutor();
   public BlockPos pos;
   public double e;
   public double f;
   Setting enemyRange = this.registerSetting(new NumberSetting("EnemyRange", "Range to search for potential enemies", 1.0F, 10.0F, 13.0F));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotate before exploding", false));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting placeSwitch = this.registerSetting(new EnumSetting("PlaceSwitch", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting explodeSwitch = this.registerSetting(new EnumSetting("ExplodeSwitch", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "The distance of your block reach", 0.0, 4.5, 6.0));
   Setting breakCrystal = this.registerSetting(new BooleanSetting("BreakCrystal", "Break crystals when placing blocks", true));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "The distance of your entity reach", 0.0, 3.0, 6.0));
   Setting players = this.registerSetting(new BooleanSetting("Players", "Target players", true));
   Setting monsters = this.registerSetting(new BooleanSetting("Monsters", "Target monsters", false));
   Setting neutrals = this.registerSetting(new BooleanSetting("Neutrals", "Target neutrals", false));
   Setting animals = this.registerSetting(new BooleanSetting("Animals", "Target animals", false));
   Setting place = this.registerSetting(new BooleanSetting("Place", "Places anchors to damage enemies", true));
   Setting spamPlace = this.registerSetting(new BooleanSetting("SpamPlace", "Immediately place the anchor after explosion", true));
   Setting detectMining = this.registerSetting(new BooleanSetting("DetectMining", "Disable the spam place on mined pos", true));
   Setting placeDelay = this.registerSetting(new NumberSetting("PlaceDelay", "Delay to place anchors", 0.1F, 18.0F, 1000.0F, this::isPlaceDelayVisible));
   Setting assumeBestArmor = this.registerSetting(new BooleanSetting("AssumeBestArmor", "Assumes Prot 0 armor is max armor", false));
   Setting minDamage = this.registerSetting(new NumberSetting("MinDamage", "Minimum damage required to consider exploding anchors", 1.0F, 4.0F, 10.0F));
   Setting maxLocalDamage = this.registerSetting(new NumberSetting("MaxLocalDamage", "The maximum player damage", 4.0F, 12.0F, 20.0F));
   Setting damageRatio = this.registerSetting(new NumberSetting("DamageRatio", "Max self damage ratio", 0.1F, 1.2F, 2.0F));
   Setting asynchronous = this.registerSetting(new BooleanSetting("Asynchronous", "Performs calculations on separate threads", true));
   Setting safety = this.registerSetting(new BooleanSetting("Safety", "Accounts for total player safety when exploding anchors", true));
   Setting blockDestruction = this.registerSetting(new BooleanSetting("BlockDestruction", "Accounts for explosion block destruction when calculating damages", false));
   Setting selfExtrapolate = this.registerSetting(new BooleanSetting("SelfExtrapolate", "Accounts for motion when calculating self damage", false));
   Setting extrapolationTicks = this.registerSetting(new NumberSetting("ExtrapolationTicks", "Accounts for motion when calculating enemy positions, not fully accurate.", 0, 0, 10));
   Setting ignoreNakeds = this.registerSetting(new BooleanSetting("IgnoreNakeds", "Ignore players with no items", false));
   public Setting render = this.registerSetting(new BooleanSetting("Render", "Renders where anchors will be placed", true));
   public Setting boxMode = this.registerSetting(new EnumSetting("BoxMode", "Box rendering mode", ShapeMode.Both, ShapeMode.values(), this::isBoxModeVisible));
   public Setting line = this.registerSetting(new ColorSetting("Line", "The box color", m(100), this::isLineVisible));
   public Setting side = this.registerSetting(new ColorSetting("Side", "The fill color", m(70), this::isSideVisible));
   Setting width = this.registerSetting(new NumberSetting("Width", "The line width of the highlight", 0.1F, 1.0F, 5.0F, this::isWidthVisible));
   public Setting shrink = this.registerSetting(new BooleanSetting("Shrink", "Shrink", false, this::isShrinkVisible));
   public Setting sliderSpeed = this.registerSetting(new NumberSetting("SliderSpeed", "Slider speed", 0.1, 0.5, 1.0, this::isSliderSpeedVisible));
   public Setting startFade = this.registerSetting(new NumberSetting("StartFade", "Time of waiting for fade", 0.1, 0.2, 1.0, this::isStartFadeVisible));
   public Setting fadeSpeed = this.registerSetting(new NumberSetting("FadeSpeed", "Fade speed", 0.1, 0.3, 1.0, this::isFadeSpeedVisible));
   public Setting renderDamage = this.registerSetting(new BooleanSetting("RenderDamage", "Renders damage", false, this::isRenderDamageVisible));
   public Setting targetDamage = this.registerSetting(new BooleanSetting("TargetDamage", "Target's damage", true, this::isTargetDamageVisible));
   public Setting selfDamage = this.registerSetting(new BooleanSetting("SelfDamage", "My damage", false, this::isSelfDamageVisible));
   StackSlotHelper stackSlotHelper;
   StackSlotHelper stackSlotHelper2;
   private AttackTargetPos attackTargetPos;
   public static boolean dd;

   public AutoAnchor() {
      super("AutoAnchor", "Automatically places and explodes respawn anchors", ModuleCategory.COMBAT, 740);
      EventBusImpl.INSTANCE.rD(new AutoAnchorVec3dBoxHelper());
      INSTANCE = this;
   }

   public static AutoAnchor aaj() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      return this.attackTargetPos != null ? this.attackTargetPos.attackTarget().getName().getString() : super.hy();
   }

   public Entity aak() {
      return this.attackTargetPos != null ? this.attackTargetPos.attackTarget() : null;
   }

   @Override
   public void awe() {
      this.stopwatch2.afr(29432, 5265492, (byte)40);
      this.attackTargetPos = null;
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (client.player != null && client.world != null && !client.player.isSpectator() && tickPreEvent.getEventPhase() == EventPhase.POST) {
         if (Blink.bm()) {
            this.attackTargetPos = null;
         } else if (!(Boolean)this.breakSetting.atx() && this.RG()) {
            this.attackTargetPos = null;
         } else if (client.player.isSneaking()) {
            this.attackTargetPos = null;
         } else {
            if ((Boolean)this.asynchronous.atx()) {
               ExecutorService temp = this.executorService;
               Runnable runnable = this::aaz;
               temp.submit(runnable);
            }
         }
      }
   }

   @EventListener
   public void aam(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (client.player != null && client.world != null) {
         StackSlotHelper stackSlotHelper3 = new StackSlotHelper(Items.RESPAWN_ANCHOR, (SwapMode)this.placeSwitch.atx()).LK();
         this.stackSlotHelper = new StackSlotHelper(Items.GLOWSTONE, (SwapMode)this.placeSwitch.atx()).LK();
         this.stackSlotHelper2 = new StackSlotHelper(new Predicate() {
            @Override
            public boolean test(Object tickEntitiesPreEvent) {
               return AutoAnchor.aay((ItemStack)tickEntitiesPreEvent);
            }
         }, (SwapMode)this.explodeSwitch.atx()).LK();
         if (!stackSlotHelper3.LL() || !this.stackSlotHelper.LL() || !this.stackSlotHelper2.LL()) {
            this.attackTargetPos = null;
         } else if (Blink.bm()) {
            this.attackTargetPos = null;
         } else if (!(Boolean)this.breakSetting.atx() && this.RG()) {
            this.attackTargetPos = null;
         } else if (client.player.isSneaking()) {
            this.attackTargetPos = null;
         } else {
            if (!(Boolean)this.asynchronous.atx()) {
               this.attackTargetPos = this.aaq();
            }

            if (this.stopwatch2.afq((Number)this.placeDelay.atx())) {
               this.stopwatch2.afr(29432, 5265492, (byte)40);
               if (this.attackTargetPos != null) {
                  BlockPos pos2 = this.attackTargetPos.getPos();
                  BlockState state2 = client.world.getBlockState(pos2);
                  boolean flag = this.attackTargetPos.antiSurround();
                  BlockPosBlockItemHelper blockPosBlockItemHelper = this.attackTargetPos.anchorPlacement();
                  blockPosBlockItemHelper.p = true;
                  if (flag && !this.aap(pos2) && (Boolean)this.spamPlace.atx()) {
                     Direction direction = PlacementUtils.Fi(pos2, (Double)this.placeRange.atx());
                     if (direction == null) {
                        return;
                     }

                     int n = Managers.inventoryTracker.JS();
                     if (state2.getBlock() instanceof RespawnAnchorBlock) {
                        BlockState state3 = state2;
                        IntProperty intProperty = RespawnAnchorBlock.CHARGES;
                        if ((Integer)state3.get((Property)intProperty) < 1) {
                           this.aao(pos2, direction, this.stackSlotHelper, n);
                        }

                        this.aao(pos2, direction, this.stackSlotHelper2, n);
                        this.aan(blockPosBlockItemHelper);
                     } else if (this.aan(blockPosBlockItemHelper)) {
                        this.aao(pos2, direction, this.stackSlotHelper, n);
                        this.aao(pos2, direction, this.stackSlotHelper2, n);
                        this.aan(blockPosBlockItemHelper);
                     }
                  } else {
                     Direction direction2 = PlacementUtils.Fi(pos2, (Double)this.placeRange.atx());
                     if (direction2 == null) {
                        return;
                     }

                     int n2 = Managers.inventoryTracker.JS();
                     if (state2.getBlock() instanceof RespawnAnchorBlock) {
                        BlockState temp = state2;
                        IntProperty intProperty2 = RespawnAnchorBlock.CHARGES;
                        if ((Integer)temp.get((Property)intProperty2) < 1) {
                           this.aao(pos2, direction2, this.stackSlotHelper, n2);
                        }

                        this.aao(pos2, direction2, this.stackSlotHelper2, n2);
                     } else if (this.aan(blockPosBlockItemHelper)) {
                        this.aao(pos2, direction2, this.stackSlotHelper, n2);
                        this.aao(pos2, direction2, this.stackSlotHelper2, n2);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean aan(BlockPosBlockItemHelper blockPosBlockItemHelper) {
      AttackTargetPos attackTargetPos2 = this.attackTargetPos;
      if (blockPosBlockItemHelper.avO((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx()) && attackTargetPos2 != null) {
         this.pos = blockPosBlockItemHelper.pos;
         this.e = attackTargetPos2.damage();
         this.f = attackTargetPos2.selfDamage();
         this.stopwatch.afr(29432, 5265492, (byte)40);
         return true;
      } else {
         return false;
      }
   }

   private void aao(BlockPos pos2, Direction direction, StackSlotHelper stackSlotHelper3, int n) {
      if (this.attackTargetPos != null) {
         this.pos = pos2;
         this.e = this.attackTargetPos.damage();
         this.f = this.attackTargetPos.selfDamage();
         this.stopwatch.afr(29432, 5265492, (byte)40);
         stackSlotHelper3.LO(n);
         this.aar(pos2, direction);
         stackSlotHelper3.LP(n);
      }
   }

   private boolean aap(BlockPos pos2) {
      if (!(Boolean)this.detectMining.atx()) {
         return false;
      }

      boolean flag;
      if (Managers.breakManager.Rv(pos2) && Managers.breakManager.Rs(pos2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private AttackTargetPos aaq() {
      Entity entity2 = null;
      BlockPos pos2 = null;
      double d = 0.0;
      double d2 = 0.0;
      boolean flag = false;
      boolean flag2 = false;
      BlockPosBlockItemHelper blockPosBlockItemHelper = null;
      Iterator iterator2 = this.aau(client.player.getPos()).iterator();
      while (iterator2.hasNext()) {
         BlockPos pos3 = (BlockPos)iterator2.next();
         BlockState blockState = client.world.getBlockState(pos3);
         double d3 = client.player.getEyePos().distanceTo(pos3.toCenterPos());
         if (d3 > (Double)this.placeRange.atx() + 0.5) {
            continue;
         }

         boolean flag3 = blockState.getBlock() instanceof RespawnAnchorBlock;
         if (!blockState.isReplaceable() && !flag3) {
            continue;
         }

         BlockPosBlockItemHelper blockPosBlockItemHelper2 = new BlockPosBlockItemHelper(pos3, Items.RESPAWN_ANCHOR, (Boolean)this.airPlace.atx(), (SwapMode)this.placeSwitch.atx());
         if (!blockPosBlockItemHelper2.avL((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx()) && !flag3) {
            continue;
         }

         double d4 = Vec3dEntityUtils.vT(
            client.player, pos3.toCenterPos(), (Boolean)this.blockDestruction.atx(), 10.0F, Set.of(pos3), (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0, false
         );
         if (this.aas(d4)) {
            continue;
         }

         Iterator iterator3 = client.world.getEntities().iterator();
         while (iterator3.hasNext()) {
            Entity entity3 = (Entity)iterator3.next();
            if (!entity3.isAlive() || entity3 == client.player || !this.aaw(entity3)) {
               continue;
            }

            if (Managers.friendManager.awt(entity3.getName())) {
               continue;
            }

            if (entity3 instanceof PlayerEntity) {
               PlayerEntity player2 = (PlayerEntity)entity3;
               if ((Boolean)this.ignoreNakeds.atx() && WorldUtils.NO(player2)) {
                  continue;
               }

               flag = WorldUtils.Nb(player2, pos3, true, true);
            }

            if (pos3.getSquaredDistance(entity3.getPos()) > 144.0) {
               continue;
            }

            if (client.player.squaredDistanceTo(entity3) > (double)((Float)this.enemyRange.atx() * (Float)this.enemyRange.atx())) {
               continue;
            }

            double d5 = Vec3dEntityUtils.vT(
               entity3, pos3.toCenterPos(), (Boolean)this.blockDestruction.atx(), 10.0F, Set.of(pos3), (Integer)this.extrapolationTicks.atx(), (Boolean)this.assumeBestArmor.atx()
            );
            if (this.aat(d5, d4) || d5 <= d) {
               continue;
            }

            entity2 = entity3;
            pos2 = pos3;
            d = d5;
            d2 = d4;
            flag2 = flag;
            blockPosBlockItemHelper = blockPosBlockItemHelper2;
         }
      }

      if (pos2 == null || d < (double)((Float)this.minDamage.atx()).floatValue()) {
         return null;
      }

      return new AttackTargetPos(entity2, pos2, blockPosBlockItemHelper, flag2, d, d2);
   }

   private void aar(BlockPos pos2, Direction direction) {
      PlacementUtils.Fg(pos2, direction, Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
   }

   private boolean aas(double d) {
      if (!client.player.isCreative()) {
         float f2 = client.player.getHealth() + client.player.getAbsorptionAmount();
         if ((Boolean)this.safety.atx() && d >= f2 + 0.5F) {
            return true;
         }

         boolean flag;
         if (d > ((Float)this.maxLocalDamage.atx()).floatValue()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         return false;
      }
   }

   private boolean aat(double d, double d2) {
      boolean flag;
      if (d / d2 < ((Float)this.damageRatio.atx()).floatValue()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private List aau(Vec3d vec) {
      double d = Math.ceil((Double)this.placeRange.atx() + 1.0);
      return this.aav(d, vec);
   }

   private List aav(double d, Vec3d vec) {
      ArrayList arrayList = new ArrayList();

      for (double d2 = -d; d2 <= d; d2++) {
         for (double d3 = -d; d3 <= d; d3++) {
            for (double d4 = -d; d4 <= d; d4++) {
               BlockPos pos2 = new BlockPos(new Vec3i((int)(vec.getX() + d2), (int)(vec.getY() + d3), (int)(vec.getZ() + d4)));
               arrayList.add(pos2);
            }
         }
      }

      return arrayList;
   }

   private boolean aaw(Entity entity2) {
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

   public BlockPos aax() {
      return this.attackTargetPos == null ? null : this.attackTargetPos.getPos();
   }

   static boolean aay(ItemStack stack) {
      boolean flag;
      if (stack.getItem() != Items.GLOWSTONE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private void aaz() {
      this.attackTargetPos = this.aaq();
   }

   private Boolean isSelfDamageVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isTargetDamageVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isRenderDamageVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isFadeSpeedVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isStartFadeVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isSliderSpeedVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isShrinkVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isWidthVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isSideVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isLineVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isBoxModeVisible() {
      return (Boolean)this.render.atx();
   }

   private Boolean isPlaceDelayVisible() {
      return (Boolean)this.place.atx();
   }

   private static Color m(int n) {
      return new Color(255, 255, 255, n);
   }
}
