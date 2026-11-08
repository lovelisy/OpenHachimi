package pw.hachimi.client.module.combat;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos.Mutable;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.SelfTrapAlwaysMiningMode;
import pw.hachimi.client.model.combat.SelfTrapNeverOnActivateMode;
import pw.hachimi.client.model.combat.SelfTrapOption;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.world.PosNeedPlaceSide;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.client.utils.world.PlayerBlockPosHelper;
import pw.hachimi.client.utils.world.Vec3dEntityUtils;
import pw.hachimi.eventbus.annotation.EventListener;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.HashMap;

public class SelfTrap extends Multitask {
   private static final List a = new ArrayList();
   private static final Set b_ = new LinkedHashSet();
   private static final Set c = new LinkedHashSet();
   private static final Set d = new LinkedHashSet();
   private static SelfTrap INSTANCE;
   Setting timing = this.registerSetting(new EnumSetting("Timing", "Timing for replacing blocks", SelfTrapOption.a_, SelfTrapOption.values()));
   Setting explosions = this.registerSetting(new BooleanSetting("Explosions", "Pre places before explosions", false, this::isExplosionsVisible));
   Setting spawn = this.registerSetting(new BooleanSetting("Spawn", "Pre places before end crystal spawn", false, this::isSpawnVisible));
   Setting broke = this.registerSetting(new BooleanSetting("Broke", "Pre places when block broke", false, this::isBrokeVisible));
   Setting surround = this.registerSetting(new BooleanSetting("Surround", "Surround your self", true));
   Setting trap = this.registerSetting(new BooleanSetting("Trap", "Trap your self", false));
   Setting standHead = this.registerSetting(new BooleanSetting("StandHead", "Trap your head when you standing", false));
   Setting crawlHead = this.registerSetting(new BooleanSetting("CrawlHead", "Trap your head when you crawling", false));
   Setting mineDownward = this.registerSetting(new BooleanSetting("MineDownward", "Allow you to mine down", false));
   Setting blocks = this.registerSetting(new BlockListSetting("Blocks", "What blocks to use for surround", Blocks.OBSIDIAN, Blocks.ENDER_CHEST));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Delay, in ticks, between placements", 0, 2, 20));
   Setting bpt = this.registerSetting(new NumberSetting("BPT", "in places, number of blocks", 1, 1, 8));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting center = this.registerSetting(new EnumSetting("Center", "Teleports you to the center of the block", SelfTrapNeverOnActivateMode.OnActivate, SelfTrapNeverOnActivateMode.values()));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the obsidian being placed", true));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "The distance of your block reach", 0.0, 4.5, 6.0));
   Setting protect = this.registerSetting(new BooleanSetting("Protect", "Attempts to break crystals around surround positions to prevent surround break", true));
   Setting breakCrystal = this.registerSetting(new BooleanSetting("BreakCrystal", "Break crystals when placing blocks", true));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "The distance of your entity reach", 0.0, 3.0, 6.0));
   Setting assumeBreak = this.registerSetting(new BooleanSetting("AssumeBreak", "Assuming it has been broken", true));
   Setting breakTime = this.registerSetting(new NumberSetting("BreakTime", "Break time", 0.0, 0.7, 2.0, this::isBreakTimeVisible));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting support = this.registerSetting(new NumberSetting("Support", "The maximum number of blocks for helper can place", 0, 2, 8));
   Setting calcDelay = this.registerSetting(new NumberSetting("CalcDelay", "Delay, in ticks, between support calculations", 0, 3, 20, this::isCalcDelayVisible));
   Setting multi = this.registerSetting(new BooleanSetting("Multi", "Allow multiple assignblocks", false, this::isMultiVisible));
   Setting extend = this.registerSetting(new EnumSetting("Extend", "Extends surround (Mining: if the block is being mined)", SelfTrapAlwaysMiningMode.Disable, SelfTrapAlwaysMiningMode.values()));
   Setting trapExtend = this.registerSetting(new EnumSetting("TrapExtend", "Extends trap (Mining: if the block is being mined)", SelfTrapAlwaysMiningMode.Disable, SelfTrapAlwaysMiningMode.values()));
   Setting groundOnly = this.registerSetting(new BooleanSetting("GroundOnly", "Works only when you are standing on blocks", false));
   Setting pauseOnFlying = this.registerSetting(new BooleanSetting("PauseOnFlying", "Pauses the current process while you are flying", false));
   Setting pauseInFluid = this.registerSetting(new BooleanSetting("PauseInFluid", "Pauses the current process while you are swimming", false));
   Setting disableOnYDiff = this.registerSetting(new BooleanSetting("DisableOnYDiff", "Automatically disables when your y level changes", false));
   Setting disableOnJump = this.registerSetting(new BooleanSetting("DisableOnJump", "Automatically disables when you jumping", true));
   Setting disableOnDeath = this.registerSetting(new BooleanSetting("DisableOnDeath", "Toggles off when you die", true));
   private int L;
   private int M;
   private int N;
   private static final long b = 0L;
   private static final String[] j = null;
   private static final String[] k = null;
   private static final Map l = new HashMap();

   public SelfTrap() {
      super("SelfTrap", "Surrounds you in blocks to prevent massive crystal damage", ModuleCategory.COMBAT, 950);
      INSTANCE = this;
   }

   public static SelfTrap Zf() {
      return INSTANCE;
   }

   @Override
   public void awd() {
      if (client.player != null) {
         if (this.center.atx() == SelfTrapNeverOnActivateMode.OnActivate) {
            WorldUtils.Ni();
         }

         this.L = 0;
         this.M = 0;
      }
   }

   @EventListener
   private void Zg(TickEntitiesPreEvent tickEntitiesPreEvent) {
      this.Zm();
      if ((Boolean)this.disableOnYDiff.atx() && client.player.prevY < client.player.getY()) {
         this.awc();
      } else if ((Boolean)this.disableOnJump.atx() && client.player.input.jumping) {
         this.awc();
      } else if (this.L > 0) {
         this.L--;
      } else {
         if ((Boolean)this.groundOnly.atx() && !client.player.isOnGround()) {
            ClientWorld temp = client.world;
            BlockPos pos = client.player.getBlockPos();
            if (!(temp.getBlockState(pos).getBlock() instanceof LadderBlock)) {
               return;
            }
         }

         if (!(Boolean)this.pauseOnFlying.atx() || !client.player.isFallFlying()) {
            if (!(Boolean)this.pauseInFluid.atx() || !client.player.isInFluid()) {
               if ((Boolean)this.breakSetting.atx() || !client.player.isUsingItem()) {
                  if (!Blink.bm()) {
                     if (this.center.atx() == SelfTrapNeverOnActivateMode.Always) {
                        WorldUtils.Ni();
                     }

                     this.N = 0;
                     Set set = d;
                     final SelfTrap selfTrap = this;
                     Predicate predicate = new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return selfTrap.Zy((BlockPos)tickEntitiesPreEvent);
                        }
                     };
                     set.removeIf(predicate);
                     set = c;
                     final char c2 = 11465;
                     final int n = 1641317894;
                     final SelfTrap selfTrap2 = this;
                     Predicate predicate2 = new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return selfTrap2.Zh(n, c2, (BlockPos)tickEntitiesPreEvent);
                        }
                     };
                     set.removeIf(predicate2);
                     if (!b_.isEmpty()) {
                        set = b_;
                        final char c3 = 11465;
                        final int n2 = 1641317894;
                        final SelfTrap selfTrap3 = this;
                        Predicate predicate3 = new Predicate() {
                           @Override
                           public boolean test(Object tickEntitiesPreEvent) {
                              return selfTrap3.Zh(n2, c3, (BlockPos)tickEntitiesPreEvent);
                           }
                        };
                        set.removeIf(predicate3);
                        set = d;
                        final SelfTrap selfTrap4 = this;
                        Predicate predicate4 = new Predicate() {
                           @Override
                           public boolean test(Object tickEntitiesPreEvent) {
                              return selfTrap4.Zx((BlockPos)tickEntitiesPreEvent);
                           }
                        };
                        set.removeIf(predicate4);
                        set = c;
                        final char c4 = 11465;
                        final int n3 = 1641317894;
                        final SelfTrap selfTrap5 = this;
                        Predicate predicate5 = new Predicate() {
                           @Override
                           public boolean test(Object tickEntitiesPreEvent) {
                              return selfTrap5.Zh(n3, c4, (BlockPos)tickEntitiesPreEvent);
                           }
                        };
                        set.removeIf(predicate5);
                     }
                  }
               }
            }
         }
      }
   }

   boolean Zh(int n, char c2, BlockPos pos) {
      if (this.N < ((Integer)this.bpt.atx()).intValue() && this.Zi(pos, false)) {
         this.N++;
         this.L = ((Integer)this.delay.atx()).intValue();
         return true;
      }

      return false;
   }

   private boolean Zi(BlockPos pos, boolean flag) {
      BlockPosBlockItemHelper blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos, (List)this.blocks.atx(), (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx());
      PlayerBlockPosHelper playerBlockPosHelper = Managers.breakManager.Rt(pos);
      boolean flag2;
      if ((!(Boolean)this.assumeBreak.atx() || playerBlockPosHelper == null || !playerBlockPosHelper.stopwatchImpl.afq((Double)this.breakTime.atx() * 1000.0) && !playerBlockPosHelper.f) && !flag) {
         flag2 = blockPosBlockItemHelper.avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
      } else {
         flag2 = blockPosBlockItemHelper.avO((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
      }

      if ((Boolean)this.protect.atx() && Managers.antiCheatManager.Uc() && (client.world.getBlockState(pos).isReplaceable() || playerBlockPosHelper != null)) {
         Box box = new Box(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object pos) {
               return SelfTrap.Zw((Entity)pos);
            }
         };
         Iterator iterator2 = client.world.getOtherEntities((Entity)null, box, predicate).iterator();

         while (iterator2.hasNext()) {
            PlacementUtils.Fp((Entity)iterator2.next(), (Double)this.breakRange.atx(), (Boolean)this.rotate.atx());
         }
      }

      return flag2;
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof BundleS2CPacket) {
            Iterator iterator2 = ((BundleS2CPacket)packet2).getPackets().iterator();

            while (iterator2.hasNext()) {
               Packet packet3 = (Packet)iterator2.next();
               this.Zk(packet3);
            }
         } else {
            this.Zk(channelRead0Event.getPacket());
         }
      }
   }

   private void Zk(Packet packet2) {
      if (this.timing.atx() == SelfTrapOption.b) {
         MinecraftClient temp = client;
         final Packet packet3 = packet2;
         final SelfTrap selfTrap = this;
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
               selfTrap.Zv(packet3);
            }
         };
         temp.execute(runnable);
      }
   }

   @EventListener
   private void onChannelRead02(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof DeathMessageS2CPacket) {
            ClientWorld temp = client.world;
            int n = ((DeathMessageS2CPacket)packet2).playerId();
            if (temp.getEntityById(n) == client.player && (Boolean)this.disableOnDeath.atx()) {
               this.awc();
            }
         }
      }
   }

   private void Zm() {
      List list;
      label131: {
         this.Zn();
         Iterator iterator2;
         if ((Boolean)this.standHead.atx() && !client.player.isCrawling() || (Boolean)this.crawlHead.atx() && client.player.isCrawling()) {
            BlockPos pos = new Mutable(client.player.getX(), client.player.getBoundingBox().getLengthY() + client.player.getY(), client.player.getZ()).up();
            c.add(pos);
            if (this.Zq()) {
               this.Zr(pos);
            }

            list = WorldUtils.MZ(client.player.getPos());
            iterator2 = list.iterator();

            while (true) {
               if (!iterator2.hasNext()) {
                  break label131;
               }

               BlockPos pos2 = (BlockPos)iterator2.next();
               if ((Boolean)this.surround.atx()) {
                  Direction[] directions = BoxUtils.b;
                  int n = directions.length;
                  int n2 = 0;

                  do {
                     Direction direction = directions[n2];
                     BlockPos pos3 = pos2.offset(direction);
                     if (!list.contains(pos3)) {
                        this.Zo(pos3, c, true, false);
                     }
                  } while (++n2 < 4);

                  List list2 = list;
                  BlockPos pos4 = pos2.down();
                  if (!list2.contains(pos4)) {
                     this.Zo(pos2.down(), d, false, false);
                  }
                  break;
               }

               List temp = list;
               BlockPos pos5 = pos2.down();
               if (!temp.contains(pos5)) {
                  this.Zo(pos2.down(), d, false, false);
               }
            }
         } else {
            list = WorldUtils.MZ(client.player.getPos());
            iterator2 = list.iterator();
         }

         while (iterator2.hasNext()) {
            BlockPos pos6 = (BlockPos)iterator2.next();
            if ((Boolean)this.surround.atx()) {
               Direction[] directions2 = BoxUtils.b;
               int n3 = directions2.length;
               int n4 = 0;
               if (true) {
                  do {
                     Direction direction2 = directions2[n4];
                     BlockPos pos7 = pos6.offset(direction2);
                     if (!list.contains(pos7)) {
                        this.Zo(pos7, c, true, false);
                     }
                  } while (++n4 < 4);

                  List list3 = list;
                  BlockPos pos8 = pos6.down();
                  if (!list3.contains(pos8)) {
                     this.Zo(pos6.down(), d, false, false);
                  }
                  continue;
               }
            }

            List list4 = list;
            BlockPos pos9 = pos6.down();
            if (!list4.contains(pos9)) {
               this.Zo(pos6.down(), d, false, false);
            }
         }
      }

      if ((Boolean)this.trap.atx()) {
         Iterator iterator3 = list.iterator();

         while (iterator3.hasNext()) {
            BlockPos pos10 = (BlockPos)iterator3.next();
            Direction[] directions3 = BoxUtils.b;
            int n5 = directions3.length;

            for (int n6 = 0; n6 < 4; n6++) {
               Direction direction3 = directions3[n6];
               BlockPos pos11 = pos10.offset(direction3);
               if (!list.contains(pos11)) {
                  this.Zo(pos11.up(), c, false, true);
               }
            }
         }
      }

      this.Zt();
      int n7;
      if (this.M > 0) {
         n7 = this.M - 1;
      } else {
         n7 = (Integer)this.calcDelay.atx();
      }

      this.M = n7;
   }

   private void Zn() {
      c.clear();
      b_.clear();
      d.clear();
      if (this.M <= 0) {
         a.clear();
      }
   }

   private void Zo(BlockPos pos, Set set, boolean flag, boolean flag2) {
      set.add(pos);
      if (flag) {
         if (this.extend.atx() == SelfTrapAlwaysMiningMode.Always || Managers.breakManager.Rv(pos) && this.extend.atx() == SelfTrapAlwaysMiningMode.Mining) {
            this.Zp(pos);
         }
      } else if (flag2
         && (
            this.trapExtend.atx() == SelfTrapAlwaysMiningMode.Always
               || Managers.breakManager.Rv(pos) && this.trapExtend.atx() == SelfTrapAlwaysMiningMode.Mining
         )) {
         this.Zp(pos);
      }

      if (this.Zq()) {
         this.Zr(pos);
      }
   }

   private void Zp(BlockPos pos) {
      Direction[] directions = BoxUtils.b;
      int temp = directions.length;
      int n = 0;

      do {
         Direction direction = directions[n];
         BlockPos pos2 = pos.offset(direction);
         this.Zo(pos2, c, false, false);
      } while (++n < 4);

      this.Zo(pos.up(), c, false, false);
   }

   private boolean Zq() {
      boolean flag;
      if (this.M <= 0 && (Integer)this.support.atx() > 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private void Zr(BlockPos pos) {
      if (client.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
         if (PlacementUtils.Fk(pos, (Double)this.placeRange.atx()) == null) {
            double temp = (Double)this.placeRange.atx();
            int n = (Integer)this.support.atx();
            double d2 = temp;
            List list = PlacementUtils.Fs(pos, d2, n);
            if (this.Zs(list)) {
               a.add(list);
            }
         }
      }
   }

   private boolean Zs(List list) {
      boolean flag;
      if (!list.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list.getLast()).getPos())) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private void Zt() {
      if (!a.isEmpty()) {
         if ((Boolean)this.multi.atx()) {
            Iterator iterator2 = a.iterator();

            while (iterator2.hasNext()) {
               List list = (List)iterator2.next();
               Set temp = b_;
               Stream temp2 = list.stream();
               Function function2 = new Function() {
                  @Override
                  public Object apply(Object obj) {
                     return ((PosNeedPlaceSide)obj).getPos();
                  }
               };
               Stream stream2 = temp2;
               temp = temp;
               temp2 = stream2.map(function2);
               Supplier supplier = HashSet::new;
               stream2 = temp2;
               Set set = temp;
               Stream stream3 = stream2;
               Collector collector = Collectors.toCollection(supplier);
               Collection collection = (Collection)stream3.collect(collector);
               set.addAll(collection);
            }
         } else {
            Stream stream4 = a.stream();
            ToIntFunction toIntFunction = new ToIntFunction() {
               @Override
               public int applyAsInt(Object obj) {
                  return ((List)obj).size();
               }
            };
            stream4 = stream4;
            Comparator comparator = Comparator.comparingInt(toIntFunction);
            Optional optional = stream4.min(comparator);
            Consumer consumer = new Consumer() {
               @Override
               public void accept(Object obj) {
                  SelfTrap.Zu((List)obj);
               }
            };
            optional.ifPresent(consumer);
         }
      }
   }

   static void Zu(List list) {
      Set temp = b_;
      Stream temp2 = list.stream();
      Function function2 = new Function() {
         @Override
         public Object apply(Object function2) {
            return ((PosNeedPlaceSide)function2).getPos();
         }
      };
      Stream stream2 = temp2;
      temp = temp;
      temp2 = stream2.map(function2);
      Supplier supplier = HashSet::new;
      stream2 = temp2;
      Set set = temp;
      Stream stream3 = stream2;
      Collector collector = Collectors.toCollection(supplier);
      Collection collection = (Collection)stream3.collect(collector);
      set.addAll(collection);
   }

   void Zv(Packet packet2) {
      if (packet2 instanceof BlockUpdateS2CPacket && (Boolean)this.broke.atx()) {
         BlockPos pos = ((BlockUpdateS2CPacket)packet2).getPos();
         if (c.contains(pos) && ((BlockUpdateS2CPacket)packet2).getState().isReplaceable()) {
            this.Zi(pos, true);
         }
      }

      if (packet2 instanceof ExplosionS2CPacket && (Boolean)this.explosions.atx()) {
         BlockPos pos2 = BlockPos.ofFloored(((ExplosionS2CPacket)packet2).getX(), ((ExplosionS2CPacket)packet2).getY(), ((ExplosionS2CPacket)packet2).getZ());
         if (c.contains(pos2)) {
            this.Zh(1641317894, 'ⳉ', pos2);
         }
      }

      if (packet2 instanceof EntitySpawnS2CPacket && ((EntitySpawnS2CPacket)packet2).getEntityType().equals(EntityType.END_CRYSTAL) && (Boolean)this.spawn.atx()) {
         Iterator iterator2 = c.iterator();

         while (iterator2.hasNext()) {
            BlockPos pos3 = (BlockPos)iterator2.next();
            BlockPos temp = pos3;
            BlockPos pos4 = BlockPos.ofFloored(((EntitySpawnS2CPacket)packet2).getX(), ((EntitySpawnS2CPacket)packet2).getY(), ((EntitySpawnS2CPacket)packet2).getZ());
            if (temp.equals(pos4) && this.Zh(1641317894, 'ⳉ', pos3)) {
               break;
            }
         }
      }
   }

   static boolean Zw(Entity entity2) {
      boolean flag;
      if (entity2 instanceof EndCrystalEntity && Vec3dEntityUtils.vK(client.player, entity2.getPos(), true) < WorldUtils.NL()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   boolean Zx(BlockPos pos) {
      if ((Boolean)this.mineDownward.atx()) {
         BlockPos pos2 = WorldUtils.MX(client.player, false).down();
         if (pos.equals(pos2) && Managers.breakManager.Rx(pos)) {
            return false;
         }
      }

      return this.Zh(1641317894, 'ⳉ', pos);
   }

   boolean Zy(BlockPos pos) {
      if ((Boolean)this.mineDownward.atx()) {
         BlockPos pos2 = WorldUtils.MX(client.player, false).down();
         if (pos.equals(pos2) && Managers.breakManager.Rx(pos)) {
            return false;
         }
      }

      return this.Zh(1641317894, 'ⳉ', pos);
   }

   private Boolean isMultiVisible() {
      boolean flag;
      if ((Integer)this.support.atx() > 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isCalcDelayVisible() {
      boolean flag;
      if ((Integer)this.support.atx() > 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBreakTimeVisible() {
      return (Boolean)this.assumeBreak.atx();
   }

   private Boolean isBrokeVisible() {
      boolean flag;
      if (this.timing.atx() == SelfTrapOption.b) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSpawnVisible() {
      boolean flag;
      if (this.timing.atx() == SelfTrapOption.b) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isExplosionsVisible() {
      boolean flag;
      if (this.timing.atx() == SelfTrapOption.b) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
