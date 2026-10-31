package pw.hachimi.client.module.combat;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import pw.hachimi.client.utils.world.Vec3dEntityUtils;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.world.AddEntityEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.FirstSecond;
import pw.hachimi.client.model.combat.PosDamage;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.movement.Sprint;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper22;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

public class PistonCrystal extends Multitask {
   Setting multitask = this.registerSetting(new BooleanSetting("Asynchronous", "Performs calculations on separate threads", true));
   Setting asynchronous = this.registerSetting(new NumberSetting("MinDamage", "Minimum damage required to consider attacking or placing an end crystal", 0.0F, 7.0F, 36.0F));
   Setting minDamage = this.registerSetting(new NumberSetting("MaxLocalDamage", "The maximum player damage", 4.0F, 12.0F, 20.0F));
   Setting maxLocalDamage = this.registerSetting(new NumberSetting("Delay", "Delay between place", 0, 2, 20));
   Setting delay = this.registerSetting(new NumberSetting("AttackWait", "Delay attack", 0, 2, 20));
   Setting attackWait = this.registerSetting(new BooleanSetting("1.12", "Use 1.12 placement", false));
   Setting targetRange = this.registerSetting(new NumberSetting("TargetRange", "Range of target", 0.0F, 6.0F, 12.0F));
   Setting sort = this.registerSetting(new EnumSetting("Sort", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting inAirTarget = this.registerSetting(new BooleanSetting("InAirTarget", "Regard people in the air as targets", true));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotate before placing and breaking", true));
   Setting grimDesync = this.registerSetting(new BooleanSetting("GrimDesync", "Grim hitbox check is fucking bad", false));
   Setting breakCrystal = this.registerSetting(new BooleanSetting("BreakCrystal", "Break crystal before place", false));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "Interact block reach", 0.0, 4.5, 8.0));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "Break crystal reach", 0.0, 3.0, 8.0));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting safety = this.registerSetting(new BooleanSetting("Safety", "Accounts for total player safety when attacking and placing crystals", true));
   Setting blockDestruction = this.registerSetting(new BooleanSetting("BlockDestruction", "Accounts for explosion block destruction when calculating damages", false));
   Setting selfExtrapolate = this.registerSetting(new BooleanSetting("SelfExtrapolate", "Accounts for motion when calculating self damage", false));
   Setting extrapolationTicks = this.registerSetting(new NumberSetting("ExtrapolationTicks", "Accounts for motion when calculating enemy positions, not fully accurate.", 0, 0, 10));
   Setting assumeBestArmor = this.registerSetting(new BooleanSetting("AssumeBestArmor", "Assumes Prot 0 armor is max armor", false));
   Setting ignoreNakeds = this.registerSetting(new BooleanSetting("IgnoreNakeds", "Ignore players with no items", false));
   private static final Box w = new Box(-1.0, 0.0, -1.0, 1.0, 2.0, 1.0);
   private static final Map x = new ConcurrentHashMap();
   private static PistonCrystal INSTANCE;
   private final List list = new ArrayList();
   private final UnidentifiedHelper22 unidentifiedHelper22 = new UnidentifiedHelper22();
   private final ExecutorService executorService = Executors.newSingleThreadExecutor();
   Vec3i[] vec3is = new Vec3i[]{h(0), h(1)};
   int[] C = new int[]{0, -1, 1};
   int[] D = new int[]{1, 2, 3};
   List list2 = new CopyOnWriteArrayList();
   public Entity entity;
   StackSlotHelper stackSlotHelper;
   Runnable runnable;
   private static final long b = 0L;
   private static final String[] j = null;
   private static final String[] k = null;
   private static final Map l = new HashMap();

   public PistonCrystal() {
      super("PistonCrystal", "Using a piston to push the crystal to damage player", ModuleCategory.COMBAT);
      INSTANCE = this;
   }

   public static PistonCrystal z() {
      return INSTANCE;
   }

   @Override
   public void awe() {
      x.clear();
      this.entity = null;
      this.list.clear();
   }

   @Override
   public String hy() {
      return this.entity != null ? this.entity.getName().getString() : super.hy();
   }

   @EventListener
   public void onAddEntity(AddEntityEvent addEntityEvent) {
      if (addEntityEvent.getEntity() instanceof EndCrystalEntity && (Boolean)this.grimDesync.atx()) {
         Map temp = x;
         BlockPos pos2 = addEntityEvent.getEntity().getBlockPos();
         Entity entity2 = addEntityEvent.getEntity();
         temp.put(entity2, pos2);
      }
   }

   public boolean B(Entity entity2, double d2, boolean flag) {
      Box box = entity2.getBoundingBox();
      if (x.containsKey(entity2)) {
         Box temp = w;
         Vec3d vec = ((BlockPos)x.get(entity2)).toBottomCenterPos();
         box = temp.offset(vec);
      }

      Vec3d vec2 = BoxUtils.jz(box);
      if (!(client.player.getEyePos().distanceTo(vec2) > d2)) {
         if (Managers.antiCheatManager.Uc()) {
            if (this.unidentifiedHelper22.afq((Number)this.delay.atx())) {
               if (flag) {
                  float[] fs = RotationUtils.nM(vec2);
                  RotationUtils.nH(fs[0], fs[1]);
               }

               if (!PlacementUtils.k) {
                  ClientPlayerEntity player2 = client.player;
                  client.interactionManager.attackEntity((PlayerEntity)player2, entity2);
               } else {
                  ClientPlayNetworkHandler network2 = client.player.networkHandler;
                  boolean flag2 = client.player.isSneaking();
                  PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.attack(entity2, flag2);
                  network2.sendPacket((Packet)playerInteractEntityC2SPacket);
                  client.player.resetLastAttackedTicks();
               }

               Sprint.a = true;
               PlacementUtils.swingMode.avX(Hand.MAIN_HAND);
               if (flag) {
                  RotationUtils.nK();
               }

               client.player.resetLastAttackedTicks();
               Managers.antiCheatManager.stopwatchImpl.afr(29432, 5265492, (byte)40);
               PlacementUtils.d = true;
               return true;
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @EventListener
   public void C(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (!(Boolean)this.breakSetting.atx() && this.RG()) {
         this.entity = null;
      } else if (!Blink.bm()) {
         if (!this.G()) {
            this.stackSlotHelper = new StackSlotHelper(Items.END_CRYSTAL, (SwapMode)this.swapAction.atx()).LK();
            if (this.stackSlotHelper.LL()) {
               if (!(Boolean)this.multitask.atx()) {
                  this.E_1();
               }

               if (!this.F() && this.runnable != null && this.unidentifiedHelper22.afq((Number)this.maxLocalDamage.atx())) {
                  this.runnable.run();
                  this.runnable = null;
               }
            } else {
               this.entity = null;
            }
         }
      } else {
         this.entity = null;
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (client.player != null && client.world != null && !client.player.isSpectator() && tickPreEvent.getEventPhase() == EventPhase.POST) {
         if ((Boolean)this.multitask.atx()) {
            ExecutorService temp = this.executorService;
            Runnable runnable2 = this::E_1;
            temp.submit(runnable2);
         }
      }
   }

   public void E_1() {
      if (this.stackSlotHelper != null) {
         if (!this.I()) {
            Iterator iterator2 = this.list2.iterator();
            if (iterator2.hasNext()) {
               PosDamage posDamage = (PosDamage)iterator2.next();
               Direction[] directions = BoxUtils.b;
               int temp = directions.length;
               int n2 = 0;

               while (true) {
                  Direction direction3 = directions[n2];
                  if (this.L(posDamage.getPos().down().offset(direction3))) {
                     Vec3i[] vec3is2 = this.vec3is;
                     int n3 = vec3is2.length;

                     for (int n4 = 0; n4 < n3; n4++) {
                        Vec3i vec3i = vec3is2[n4];
                        int[] ns = this.C;
                        int n5 = ns.length;

                        for (int n6 = 0; n6 < n5; n6++) {
                           int n7 = ns[n6];
                           int[] ns2 = this.D;
                           int n8 = ns2.length;

                           for (int n9 = 0; n9 < n8; n9++) {
                              int n10 = ns2[n9];
                              if ((n10 != 1 || n7 != 0) && this.H(posDamage, direction3, vec3i, n7, n10, this.stackSlotHelper)) {
                                 return;
                              }
                           }
                        }
                     }
                  }

                  n2++;

                  while (n2 >= 4) {
                     if (!iterator2.hasNext()) {
                        return;
                     }

                     posDamage = (PosDamage)iterator2.next();
                     directions = BoxUtils.b;
                     temp = directions.length;
                     n2 = 0;
                  }
               }
            }
         }
      }
   }

   private boolean F() {
      Iterator iterator2 = this.list2.iterator();

      while (iterator2.hasNext()) {
         PosDamage posDamage = (PosDamage)iterator2.next();
         List list = this.O(new Box(posDamage.getPos()));
         Iterator iterator3 = list.iterator();

         while (iterator3.hasNext()) {
            Entity entity2 = (Entity)iterator3.next();
            if (!entity2.isRemoved() && entity2.intersectionChecked) {
               int n = (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0;
               double d = Vec3dEntityUtils.vM(client.player, entity2.getPos(), (Boolean)this.blockDestruction.atx(), n, false);
               if (!this.Q(d)) {
                  double d2 = Vec3dEntityUtils.vM(
                     this.entity, entity2.getPos(), (Boolean)this.blockDestruction.atx(), (Integer)this.extrapolationTicks.atx(), (Boolean)this.assumeBestArmor.atx()
                  );
                  if (d2 >= ((Float)this.asynchronous.atx()).floatValue()) {
                     if (AutoCrystal.sc().awg()) {
                        return true;
                     }

                     if (this.B(entity2, (Double)this.breakRange.atx(), (Boolean)this.rotate.atx())) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean G() {
      final PistonCrystal pistonCrystal = this;
      TargetUtils.WY(this.list, new Predicate() {
         @Override
         public boolean test(Object var1x) {
            return pistonCrystal.U((Entity)var1x);
         }
      }, (TargetPriority)this.sort.atx(), 1);
      if (this.list.isEmpty()) {
         this.entity = null;
         return true;
      } else {
         this.entity = (Entity)this.list.getFirst();
         return false;
      }
   }

   private boolean H(PosDamage posDamage, Direction direction3, Vec3i vec3i, int n2, int n3, StackSlotHelper stackSlotHelper2) {
      BlockPos temp = posDamage.getPos().add(vec3i).offset(direction3, n3);
      int n4 = direction3.getOffsetX() * n2;
      int n5 = direction3.getOffsetZ() * n2;
      temp = temp.add(n5, 0, n4);
      Direction temp2 = direction3.getOpposite();
      boolean flag = (Boolean)this.breakCrystal.atx();
      Direction direction4 = temp2;
      BlockPos pos2 = temp;
      FirstSecond firstSecond = this.S(pos2, direction4, flag);
      if (firstSecond == null) {
         return false;
      }

      FirstSecond temp3 = firstSecond;
      final PosDamage posDamage2 = posDamage;
      final StackSlotHelper stackSlotHelper3 = stackSlotHelper2;
      final Direction direction5 = direction3;
      final FirstSecond firstSecond2 = temp3;
      final PistonCrystal pistonCrystal = this;
      this.runnable = new Runnable() {
         @Override
         public void run() {
            pistonCrystal.X(firstSecond2, direction5, stackSlotHelper3, posDamage2);
         }
      };
      return true;
   }

   private boolean I() {
      this.list2.clear();
      Iterator iterator2 = this.R(this.entity.getPos()).iterator();

      while (iterator2.hasNext()) {
         BlockPos pos2 = (BlockPos)iterator2.next();
         Vec3d vec = client.player.getEyePos();
         Vec3d vec2 = this.P(pos2.toBottomCenterPos());
         if (vec.distanceTo(vec2) <= (Double)this.breakRange.atx()) {
            int n = (Boolean)this.selfExtrapolate.atx() ? (Integer)this.extrapolationTicks.atx() : 0;
            double d = Vec3dEntityUtils.vM(client.player, pos2.toBottomCenterPos(), (Boolean)this.blockDestruction.atx(), n, false);
            if (!this.Q(d)) {
               double d2 = Vec3dEntityUtils.vM(
                  this.entity, pos2.toBottomCenterPos(), (Boolean)this.blockDestruction.atx(), (Integer)this.extrapolationTicks.atx(), (Boolean)this.assumeBestArmor.atx()
               );
               if (d2 >= ((Float)this.asynchronous.atx()).floatValue()) {
                  if (AutoCrystal.sc().awg() && this.L(pos2.down())) {
                     return true;
                  }

                  this.list2.add(new PosDamage(pos2, d2));
               }
            }
         }
      }

      ToDoubleFunction toDoubleFunction = obj -> ((PosDamage)obj).damage();
      Comparator comparator = Comparator.comparingDouble(toDoubleFunction).reversed();
      this.list2.sort(comparator);
      return false;
   }

   private boolean J(Vec3d vec) {
      Vec3d vec2 = this.P(vec);
      boolean flag;
      if (client.player.getEyePos().distanceTo(vec2) <= (Double)this.breakRange.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean K(BlockPos pos2) {
      if (!this.J(pos2.up().toBottomCenterPos())) {
         return false;
      }

      boolean flag;
      if (PlacementUtils.Fi(pos2, (Double)this.placeRange.atx()) != null) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean L(BlockPos pos2) {
      BlockState state2 = client.world.getBlockState(pos2);
      BlockState temp = state2;
      Block block2 = Blocks.OBSIDIAN;
      if (!temp.isOf(block2)) {
         temp = state2;
         Block block3 = Blocks.BEDROCK;
         if (!temp.isOf(block3)) {
            return false;
         }
      }

      boolean flag;
      if (this.M(pos2) && this.K(pos2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean M(BlockPos pos2) {
      BlockPos pos3 = pos2.up();
      BlockState state2 = client.world.getBlockState(pos3);
      if ((Boolean)this.attackWait.atx()) {
         ClientWorld temp = client.world;
         BlockPos pos4 = pos3.up();
         if (!temp.isAir(pos4)) {
            return false;
         }
      }

      if (!client.world.isAir(pos3)) {
         BlockState state3 = state2;
         Block block2 = Blocks.FIRE;
         if (!state3.isOf(block2)) {
            return false;
         }
      }

      boolean flag;
      if (this.N(pos2.up()) && this.N(pos2.up(2))) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean N(BlockPos pos2) {
      ClientWorld temp = client.world;
      Box box = new Box(pos2);
      Iterator iterator2 = (new CopyOnWriteArrayList(temp.getOtherEntities((Entity)null, box))).iterator();

      while (iterator2.hasNext()) {
         if (((Entity)iterator2.next()).isAlive()) {
            return false;
         }
      }

      return true;
   }

   List O(Box box) {
      ClientWorld temp = client.world;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object box) {
            return PistonCrystal.W((Entity)box);
         }
      };
      return temp.getOtherEntities((Entity)null, box, predicate);
   }

   private Vec3d P(Vec3d vec) {
      Box box = w.offset(vec);
      return BoxUtils.jy(client.player.getEyePos(), box);
   }

   private boolean Q(double d2) {
      if (!client.player.isCreative()) {
         float f2 = client.player.getHealth() + client.player.getAbsorptionAmount();
         if ((Boolean)this.safety.atx() && d2 >= f2 - 0.5F) {
            return true;
         }

         boolean flag;
         if (d2 > ((Float)this.minDamage.atx()).floatValue()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         return false;
      }
   }

   private List R(Vec3d vec) {
      return BoxUtils.jF(new Box(vec.getX() - 2.0, vec.getY() - 1.0, vec.getZ() - 2.0, vec.getX() + 2.0, vec.getY() + 3.0, vec.getZ() + 2.0));
   }

   private FirstSecond S(BlockPos pos2, Direction direction3, boolean flag) {
      ClientWorld temp = client.world;
      BlockPos pos3 = pos2.offset(direction3);
      if (temp.getBlockState(pos3).getPistonBehavior() != PistonBehavior.DESTROY) {
         temp = client.world;
         BlockPos pos4 = pos2.offset(direction3);
         if (!temp.isAir(pos4)) {
            return null;
         }
      }

      Direction[] directions = BoxUtils.a_;
      int n2 = directions.length;
      int n3 = 0;

      while (true) {
         label192: {
            boolean flag2;
            BlockPosBlockItemHelper blockPosBlockItemHelper;
            if (n3 < n2) {
               Direction direction4 = directions[n3];
               if (direction4 == direction3) {
                  break label192;
               }

               temp = client.world;
               BlockPos pos5 = pos2.offset(direction4);
               Block block2 = temp.getBlockState(pos5).getBlock();
               if (block2 != Blocks.REDSTONE_BLOCK && block2 != Blocks.REDSTONE_TORCH) {
                  break label192;
               }

               if (client.world.getBlockState(pos2).getBlock() instanceof PistonBlock) {
                  return null;
               }

               if ((Boolean)this.attackWait.atx()) {
                  Direction direction5 = BoxUtils.jC(pos2, client.player);
                  if (direction5 == Direction.UP || direction5 == Direction.DOWN) {
                     return null;
                  }
               }

               Predicate predicate = new Predicate() {
                  @Override
                  public boolean test(Object pos2) {
                     return PistonCrystal.V((ItemStack)pos2);
                  }
               };
               Setting setting2 = this.airPlace;
               Predicate predicate2 = predicate;
               boolean temp2 = (Boolean)setting2.atx();
               setting2 = this.swapAction;
               boolean flag3 = temp2;
               Predicate predicate3 = predicate2;
               blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos2, predicate3, flag3, (SwapMode)setting2.atx());
               blockPosBlockItemHelper.avK((Double)this.placeRange.atx(), flag);
               if (blockPosBlockItemHelper.m && blockPosBlockItemHelper.n && (flag || blockPosBlockItemHelper.o) && blockPosBlockItemHelper.stackSlotHelper.LL()) {
                  flag3 = true;
               } else {
                  flag3 = false;
               }

               if (!flag3) {
                  return null;
               }

               if (blockPosBlockItemHelper.direction == null && blockPosBlockItemHelper.direction2 == null) {
                  flag3 = false;
               } else {
                  flag3 = true;
               }

               flag2 = flag3;
               if (flag2) {
                  return new FirstSecond(blockPosBlockItemHelper, null);
               }
            } else {
               BlockState state2 = client.world.getBlockState(pos2);
               if (state2.getBlock() instanceof PistonBlock) {
                  BlockState state3 = state2;
                  DirectionProperty directionProperty = FacingBlock.FACING;
                  if (state3.get((Property)directionProperty) == direction3) {
                     Direction[] directions2 = BoxUtils.a_;
                     n3 = directions2.length;

                     for (int n4 = 0; n4 < n3; n4++) {
                        Direction direction6 = directions2[n4];
                        if (direction6 != direction3) {
                           BlockPosBlockItemHelper blockPosBlockItemHelper2 = new BlockPosBlockItemHelper(
                              pos2.offset(direction6), Items.REDSTONE_BLOCK, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                           );
                           if (blockPosBlockItemHelper2.avK((Double)this.placeRange.atx(), flag)) {
                              return new FirstSecond(blockPosBlockItemHelper2, null);
                           }
                        }
                     }

                     return null;
                  }

                  return null;
               }

               if ((Boolean)this.attackWait.atx()) {
                  Direction direction7 = BoxUtils.jC(pos2, client.player);
                  if (direction7 == Direction.UP || direction7 == Direction.DOWN) {
                     return null;
                  }
               }

               Predicate predicate4 = new Predicate() {
                  @Override
                  public boolean test(Object pos2) {
                     return PistonCrystal.V((ItemStack)pos2);
                  }
               };
               Setting setting3 = this.airPlace;
               Predicate predicate5 = predicate4;
               boolean flag4 = (Boolean)setting3.atx();
               setting3 = this.swapAction;
               boolean flag5 = flag4;
               Predicate predicate6 = predicate5;
               blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos2, predicate6, flag5, (SwapMode)setting3.atx());
               blockPosBlockItemHelper.avK((Double)this.placeRange.atx(), flag);
               if (blockPosBlockItemHelper.m && blockPosBlockItemHelper.n && (flag || blockPosBlockItemHelper.o) && blockPosBlockItemHelper.stackSlotHelper.LL()) {
                  flag5 = true;
               } else {
                  flag5 = false;
               }

               if (!flag5) {
                  return null;
               }

               if (blockPosBlockItemHelper.direction == null && blockPosBlockItemHelper.direction2 == null) {
                  flag5 = false;
               } else {
                  flag5 = true;
               }

               flag2 = flag5;
            }

            Direction[] directions3 = BoxUtils.a_;
            int n5 = directions3.length;

            for (int n6 = 0; n6 < n5; n6++) {
               Direction direction8 = directions3[n6];
               if (direction8 != direction3) {
                  BlockPos temp3 = pos2.offset(direction8);
                  Setting setting4 = this.airPlace;
                  Item item2 = Items.REDSTONE_BLOCK;
                  temp3 = temp3;
                  Item temp4 = item2;
                  boolean temp5 = (Boolean)setting4.atx();
                  setting4 = this.swapAction;
                  boolean flag6 = temp5;
                  item2 = temp4;
                  BlockPosBlockItemHelper blockPosBlockItemHelper3 = new BlockPosBlockItemHelper(temp3, item2, flag6, (SwapMode)setting4.atx());
                  if (blockPosBlockItemHelper3.avK((Double)this.placeRange.atx(), flag)
                     ? flag2 || PlacementUtils.Fr(pos2, direction8)
                     : blockPosBlockItemHelper3.m && blockPosBlockItemHelper3.n && (flag || blockPosBlockItemHelper3.o) && blockPosBlockItemHelper3.stackSlotHelper.LL() && PlacementUtils.Fr(blockPosBlockItemHelper3.pos, direction8.getOpposite()) && flag2) {
                     if (blockPosBlockItemHelper3 != null) {
                        if (!flag2) {
                           return new FirstSecond(blockPosBlockItemHelper3, blockPosBlockItemHelper);
                        }

                        return new FirstSecond(blockPosBlockItemHelper, blockPosBlockItemHelper3);
                     }
                     break;
                  }
               }
            }

            return null;
         }

         n3++;
      }
   }

   private void T(Direction direction3) {
      Direction direction4 = direction3.getOpposite();
      if (direction4 == Direction.EAST) {
         RotationUtils.nH(-90.0F, 5.0F);
      } else if (direction4 == Direction.WEST) {
         RotationUtils.nH(90.0F, 5.0F);
      } else if (direction4 == Direction.NORTH) {
         RotationUtils.nH(180.0F, 5.0F);
      } else if (direction4 == Direction.SOUTH) {
         RotationUtils.nH(0.0F, 5.0F);
      }
   }

   boolean U(Entity entity2) {
      if (entity2 instanceof PlayerEntity) {
         if ((Boolean)this.ignoreNakeds.atx() && WorldUtils.NO((PlayerEntity)entity2)) {
            return false;
         }

         ClientPlayerEntity player2 = client.player;
         if (!entity2.equals(player2)) {
            Entity entity3 = client.cameraEntity;
            if (!entity2.equals(entity3)) {
               if (!((PlayerEntity)entity2).isDead() && entity2.isAlive()) {
                  if (!(Boolean)this.inAirTarget.atx() && !((PlayerEntity)entity2).isOnGround()) {
                     return false;
                  }

                  if (client.player.distanceTo(entity2) > (Float)this.targetRange.atx()) {
                     return false;
                  }

                  if (((PlayerEntity)entity2).isCreative()) {
                     return false;
                  }

                  boolean flag;
                  if (!Managers.friendManager.awr((PlayerEntity)entity2)) {
                     flag = true;
                  } else {
                     flag = false;
                  }

                  return flag;
               }

               return false;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   static boolean V(ItemStack stack2) {
      Item item2 = stack2.getItem();
      boolean flag;
      if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof PistonBlock) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean W(Entity entity2) {
      return entity2 instanceof EndCrystalEntity;
   }

   void X(FirstSecond firstSecond, Direction direction3, StackSlotHelper stackSlotHelper2, PosDamage posDamage) {
      Item item2 = firstSecond.first().stackSlotHelper.stack.getItem();
      if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof PistonBlock) {
         BlockPosBlockItemHelper blockPosBlockItemHelper = firstSecond.first();
         if (!blockPosBlockItemHelper.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
            return;
         }

         if ((Boolean)this.rotate.atx()) {
            if (blockPosBlockItemHelper.direction2 != null) {
               Direction direction4 = blockPosBlockItemHelper.direction2;
               RotationUtils.nJ(blockPosBlockItemHelper.pos.offset(direction4), blockPosBlockItemHelper.direction2.getOpposite());
            } else {
               RotationUtils.nJ(blockPosBlockItemHelper.pos, blockPosBlockItemHelper.direction);
            }
         }

         this.T(direction3.getOpposite());
         blockPosBlockItemHelper.avN(false, (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
         if ((Boolean)this.rotate.atx()) {
            if (blockPosBlockItemHelper.direction2 != null) {
               Direction direction5 = blockPosBlockItemHelper.direction2;
               RotationUtils.nJ(blockPosBlockItemHelper.pos.offset(direction5), blockPosBlockItemHelper.direction2.getOpposite());
            } else if (blockPosBlockItemHelper.direction != null) {
               RotationUtils.nJ(blockPosBlockItemHelper.pos, blockPosBlockItemHelper.direction);
            }
         }

         BlockPosBlockItemHelper blockPosBlockItemHelper2 = firstSecond.second();
         if (blockPosBlockItemHelper2 instanceof BlockPosBlockItemHelper
            && !blockPosBlockItemHelper2.avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx())) {
            return;
         }
      } else {
         firstSecond.first().avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
         BlockPosBlockItemHelper blockPosBlockItemHelper3 = firstSecond.second();
         if (blockPosBlockItemHelper3 instanceof BlockPosBlockItemHelper) {
            if (!blockPosBlockItemHelper3.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
               return;
            }

            if ((Boolean)this.rotate.atx()) {
               if (blockPosBlockItemHelper3.direction2 != null) {
                  Direction direction6 = blockPosBlockItemHelper3.direction2;
                  RotationUtils.nJ(blockPosBlockItemHelper3.pos.offset(direction6), blockPosBlockItemHelper3.direction2.getOpposite());
               } else if (blockPosBlockItemHelper3.direction != null) {
                  RotationUtils.nJ(blockPosBlockItemHelper3.pos, blockPosBlockItemHelper3.direction);
               }
            }

            this.T(direction3.getOpposite());
            blockPosBlockItemHelper3.avN(false, (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
            if ((Boolean)this.rotate.atx()) {
               if (blockPosBlockItemHelper3.direction2 != null) {
                  Direction direction7 = blockPosBlockItemHelper3.direction2;
                  RotationUtils.nJ(blockPosBlockItemHelper3.pos.offset(direction7), blockPosBlockItemHelper3.direction2.getOpposite());
               } else if (blockPosBlockItemHelper3.direction != null) {
                  RotationUtils.nJ(blockPosBlockItemHelper3.pos, blockPosBlockItemHelper3.direction);
               }
            }
         }
      }

      int n2 = Managers.inventoryTracker.JS();
      stackSlotHelper2.LO(n2);
      PlacementUtils.Fe(posDamage.getPos().down().offset(direction3), (Boolean)this.rotate.atx());
      stackSlotHelper2.LP(n2);
      this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
   }

   private static Vec3i h(int n2) {
      return new Vec3i(0, n2, 0);
   }

   private static String e(byte[] bs) {
      int n2 = 0;
      int n3;
      char[] cs = new char[n3 = bs.length];

      for (int n4 = 0; n4 < n3; n4++) {
         int n5;
         if ((n5 = 255 & bs[n4]) < 192) {
            cs[n2++] = (char)n5;
         } else if (n5 < 224) {
            char c = (char)((char)(n5 & 31) << 6);
            byte b2 = bs[++n4];
            c = (char)(c | (char)(b2 & 63));
            cs[n2++] = c;
         } else if (n4 < n3 - 2) {
            char c2 = (char)((char)(n5 & 15) << '\f');
            byte b3 = bs[++n4];
            c2 = (char)(c2 | (char)(b3 & 63) << 6);
            b3 = bs[++n4];
            c2 = (char)(c2 | (char)(b3 & 63));
            cs[n2++] = c2;
         }
      }

      return new String(cs, 0, n2);
   }

   private static String e(int n2, long l2) throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
      int n3 = n2 ^ (int)(l2 & 32767L) ^ 21793;
      if (k[n3] == null) {
         Object[] objs;
         try {
            Long longValue = Thread.currentThread().threadId();
            objs = (Object[])l.get(longValue);
            if (objs == null) {
               objs = new Object[]{Cipher.getInstance("DES/CBC/PKCS5Padding"), SecretKeyFactory.getInstance("DES"), new IvParameterSpec(new byte[8])};
               l.put(longValue, objs);
            }
         } catch (Exception exception) {
            throw new RuntimeException("pw/hachimi/client/\u0000i\u200e", exception);
         }

         byte[] bs = new byte[8];
         bs[0] = (byte)(l2 >>> 56);

         for (int n4 = 1; n4 < 8; n4++) {
            bs[n4] = (byte)(l2 << n4 * 8 >>> 56);
         }

         DESKeySpec desKeySpec = new DESKeySpec(bs);
         SecretKey secretKey = ((SecretKeyFactory)objs[1]).generateSecret(desKeySpec);
         ((Cipher)objs[0]).init(2, secretKey, (IvParameterSpec)objs[2]);
         byte[] bs2 = j[n3].getBytes("ISO-8859-1");
         k[n3] = e(((Cipher)objs[0]).doFinal(bs2));
      }

      return k[n3];
   }

   private static Object e(MutableCallSite mutableCallSite, Object[] objs) throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
      int n2 = (Integer)objs[0];
      long l2 = (Long)objs[1];
      String str = e(n2, l2);
      MethodHandle methodHandle = MethodHandles.constant(String.class, str);
      mutableCallSite.setTarget(MethodHandles.dropArguments(methodHandle, 0, int.class, long.class));
      return str;
   }

   private static CallSite e(Lookup lookup, String str, MethodType methodType) {
      MutableCallSite mutableCallSite = new MutableCallSite(methodType);

      try {
         MethodHandle methodHandle = lookup.findStatic(PistonCrystal.class, "e", MethodType.methodType(Object.class, MutableCallSite.class, Object[].class));
         methodHandle = methodHandle.asCollector(Object[].class, methodType.parameterCount());
         methodHandle = MethodHandles.insertArguments(methodHandle, 0, mutableCallSite);
         mutableCallSite.setTarget(MethodHandles.explicitCastArguments(methodHandle, methodType));
      } catch (Exception exception) {
         throw new RuntimeException("pw/hachimi/client/\u0000i\u200e : " + str + " : " + methodType, exception);
      }

      return mutableCallSite;
   }
}
