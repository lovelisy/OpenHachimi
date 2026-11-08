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
import pw.hachimi.client.model.combat.SurroundAlwaysMiningMode;
import pw.hachimi.client.model.combat.SurroundNeverOnActivateMode;
import pw.hachimi.client.model.combat.SurroundVanillaSequentialMode;
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
import java.util.ArrayList;
import java.util.HashMap;

public class Surround extends Multitask {
   private static final List a = new ArrayList();
   private static final Set b_ = new HashSet();
   private static final Set c = new HashSet();
   private static final Set d = new HashSet();
   private static Surround INSTANCE;
   Setting timing = this.registerSetting(new EnumSetting("Timing", "Timing for replacing blocks", SurroundVanillaSequentialMode.VANILLA, SurroundVanillaSequentialMode.values()));
   Setting explosions = this.registerSetting(new BooleanSetting("Explosions", "Pre places before explosions", false, this::isExplosionsVisible));
   Setting spawn = this.registerSetting(new BooleanSetting("Spawn", "Pre places before end crystal spawn", false, this::isSpawnVisible));
   Setting broke = this.registerSetting(new BooleanSetting("Broke", "Pre places when block broke", false, this::isBrokeVisible));
   Setting surround = this.registerSetting(new BooleanSetting("Surround", "Surround your self", true));
   Setting standHead = this.registerSetting(new BooleanSetting("StandHead", "Trap your head when you standing", false));
   Setting crawlHead = this.registerSetting(new BooleanSetting("CrawlHead", "Trap your head when you crawling", false));
   Setting mineDownward = this.registerSetting(new BooleanSetting("MineDownward", "Allow you to mine down", false));
   Setting blocks = this.registerSetting(new BlockListSetting("Blocks", "What blocks to use for surround", Blocks.OBSIDIAN, Blocks.ENDER_CHEST));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Delay, in ticks, between placements", 0, 2, 20));
   Setting bpt = this.registerSetting(new NumberSetting("BPT", "in places, number of blocks", 1, 1, 8));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting center = this.registerSetting(new EnumSetting("Center", "Teleports you to the center of the block", SurroundNeverOnActivateMode.Never, SurroundNeverOnActivateMode.values()));
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
   Setting multi = this.registerSetting(new BooleanSetting("Multi", "Allow multiple assignblocks", true, this::isMultiVisible));
   Setting extend = this.registerSetting(new EnumSetting("Extend", "Extends surround (Mining: if the block is being mined)", SurroundAlwaysMiningMode.Disable, SurroundAlwaysMiningMode.values()));
   Setting groundOnly = this.registerSetting(new BooleanSetting("GroundOnly", "Works only when you are standing on blocks", false));
   Setting pauseOnFlying = this.registerSetting(new BooleanSetting("PauseOnFlying", "Pauses the current process while you are flying", false));
   Setting pauseInFluid = this.registerSetting(new BooleanSetting("PauseInFluid", "Pauses the current process while you are swimming", false));
   Setting disableOnYDiff = this.registerSetting(new BooleanSetting("DisableOnYDiff", "Automatically disables when your y level changes", false));
   Setting disableOnJump = this.registerSetting(new BooleanSetting("DisableOnJump", "Automatically disables when you jumping", true));
   Setting disableOnDeath = this.registerSetting(new BooleanSetting("DisableOnDeath", "Toggles off when you die", true));
   private int J;
   private int K;
   private int L;
   private static final long b = 0L;
   private static final String[] j = null;
   private static final String[] k = null;
   private static final Map l = new HashMap();

   public Surround() {
      super("Surround", "Surrounds you in blocks to prevent massive crystal damage", ModuleCategory.COMBAT, 950);
      INSTANCE = this;
   }

   public static Surround zD() {
      return INSTANCE;
   }

   @Override
   public void awd() {
      if (this.center.atx() == SurroundNeverOnActivateMode.OnActivate) {
         WorldUtils.Ni();
      }

      this.J = 0;
      this.K = 0;
   }

   @EventListener
   private void zE(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (!SelfTrap.Zf().awg()) {
         this.zK();
         if ((Boolean)this.disableOnYDiff.atx() && client.player.prevY < client.player.getY()) {
            this.awc();
         } else if ((Boolean)this.disableOnJump.atx() && client.player.input.jumping) {
            this.awc();
         } else if (this.J > 0) {
            this.J--;
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
                        if (this.center.atx() == SurroundNeverOnActivateMode.Always) {
                           WorldUtils.Ni();
                        }

                        this.L = 0;
                        Set set = d;
                        final Surround surround2 = this;
                        Predicate predicate = new Predicate() {
                           @Override
                           public boolean test(Object tickEntitiesPreEvent) {
                              return surround2.zW((BlockPos)tickEntitiesPreEvent);
                           }
                        };
                        set.removeIf(predicate);
                        set = c;
                        final Surround surround3 = this;
                        Predicate predicate2 = new Predicate() {
                           @Override
                           public boolean test(Object tickEntitiesPreEvent) {
                              return surround3.zF((BlockPos)tickEntitiesPreEvent);
                           }
                        };
                        set.removeIf(predicate2);
                        if (!b_.isEmpty()) {
                           set = b_;
                           final Surround surround4 = this;
                           Predicate predicate3 = new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return surround4.zF((BlockPos)tickEntitiesPreEvent);
                              }
                           };
                           set.removeIf(predicate3);
                           set = d;
                           final Surround surround5 = this;
                           Predicate predicate4 = new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return surround5.zV((BlockPos)tickEntitiesPreEvent);
                              }
                           };
                           set.removeIf(predicate4);
                           set = c;
                           final Surround surround6 = this;
                           Predicate predicate5 = new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return surround6.zF((BlockPos)tickEntitiesPreEvent);
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
   }

   boolean zF(BlockPos pos) {
      if (this.L >= (Integer)this.bpt.atx()) {
         return false;
      } else if (this.zG(pos, false)) {
         this.L++;
         this.J = (Integer)this.delay.atx();
         return true;
      } else {
         return false;
      }
   }

   private boolean zG(BlockPos pos, boolean flag) {
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
               return Surround.zU((Entity)pos);
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
               this.zI(packet3);
            }
         } else {
            this.zI(channelRead0Event.getPacket());
         }
      }
   }

   private void zI(Packet packet2) {
      if (this.timing.atx() == SurroundVanillaSequentialMode.SEQUENTIAL) {
         MinecraftClient temp = client;
         final Packet packet3 = packet2;
         final Surround surround2 = this;
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
               surround2.zT(packet3);
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

   private void zK() {
      this.zL();
      List list;
      Iterator iterator2;
      if ((Boolean)this.standHead.atx() && !client.player.isCrawling() || (Boolean)this.crawlHead.atx() && client.player.isCrawling()) {
         BlockPos pos = new Mutable(client.player.getX(), client.player.getBoundingBox().getLengthY() + client.player.getY(), client.player.getZ()).up();
         c.add(pos);
         if (this.zO()) {
            this.zP(pos);
         }

         list = WorldUtils.MZ(client.player.getPos());
         iterator2 = list.iterator();

         while (true) {
            if (!iterator2.hasNext()) {
               this.zR();
               int n;
               if (this.K > 0) {
                  n = this.K - 1;
               } else {
                  n = (Integer)this.calcDelay.atx();
               }

               this.K = n;
               return;
            }

            BlockPos pos2 = (BlockPos)iterator2.next();
            if ((Boolean)this.surround.atx()) {
               Direction[] directions = BoxUtils.b;
               int n2 = directions.length;
               int n3 = 0;

               do {
                  Direction direction = directions[n3];
                  BlockPos pos3 = pos2.offset(direction);
                  if (!list.contains(pos3)) {
                     this.zM(pos3, c, true);
                  }
               } while (++n3 < 4);

               List list2 = list;
               BlockPos pos4 = pos2.down();
               if (!list2.contains(pos4)) {
                  this.zM(pos2.down(), d, false);
               }
               break;
            }

            List temp = list;
            BlockPos pos5 = pos2.down();
            if (!temp.contains(pos5)) {
               this.zM(pos2.down(), d, false);
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
            int n4 = directions2.length;
            int n5 = 0;
            if (true) {
               do {
                  Direction direction2 = directions2[n5];
                  BlockPos pos7 = pos6.offset(direction2);
                  if (!list.contains(pos7)) {
                     this.zM(pos7, c, true);
                  }
               } while (++n5 < 4);

               List list3 = list;
               BlockPos pos8 = pos6.down();
               if (!list3.contains(pos8)) {
                  this.zM(pos6.down(), d, false);
               }
               continue;
            }
         }

         List list4 = list;
         BlockPos pos9 = pos6.down();
         if (!list4.contains(pos9)) {
            this.zM(pos6.down(), d, false);
         }
      }

      this.zR();
      int n6;
      if (this.K > 0) {
         n6 = this.K - 1;
      } else {
         n6 = (Integer)this.calcDelay.atx();
      }

      this.K = n6;
   }

   private void zL() {
      c.clear();
      b_.clear();
      d.clear();
      if (this.K <= 0) {
         a.clear();
      }
   }

   private void zM(BlockPos pos, Set set, boolean flag) {
      set.add(pos);
      if (flag
         && (this.extend.atx() == SurroundAlwaysMiningMode.Always || Managers.breakManager.Rv(pos) && this.extend.atx() == SurroundAlwaysMiningMode.Mining)) {
         this.zN(pos);
      }

      if (this.zO()) {
         this.zP(pos);
      }
   }

   private void zN(BlockPos pos) {
      Direction[] directions = BoxUtils.b;
      int temp = directions.length;
      int n = 0;

      do {
         Direction direction = directions[n];
         BlockPos pos2 = pos.offset(direction);
         this.zM(pos2, c, false);
      } while (++n < 4);

      this.zM(pos.up(), c, false);
   }

   private boolean zO() {
      boolean flag;
      if (this.K <= 0 && (Integer)this.support.atx() > 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private void zP(BlockPos pos) {
      if (client.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
         if (PlacementUtils.Fk(pos, (Double)this.placeRange.atx()) == null) {
            double temp = (Double)this.placeRange.atx();
            int n = (Integer)this.support.atx();
            double d2 = temp;
            List list = PlacementUtils.Fs(pos, d2, n);
            if (this.zQ(list)) {
               a.add(list);
            }
         }
      }
   }

   private boolean zQ(List list) {
      boolean flag;
      if (!list.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list.getLast()).getPos())) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private void zR() {
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
                  Surround.zS((List)obj);
               }
            };
            optional.ifPresent(consumer);
         }
      }
   }

   static void zS(List list) {
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

   void zT(Packet packet2) {
      if (packet2 instanceof BlockUpdateS2CPacket && (Boolean)this.broke.atx()) {
         BlockPos pos = ((BlockUpdateS2CPacket)packet2).getPos();
         if (c.contains(pos) && ((BlockUpdateS2CPacket)packet2).getState().isReplaceable()) {
            this.zG(pos, true);
         }
      }

      if (packet2 instanceof ExplosionS2CPacket && (Boolean)this.explosions.atx()) {
         BlockPos pos2 = BlockPos.ofFloored(((ExplosionS2CPacket)packet2).getX(), ((ExplosionS2CPacket)packet2).getY(), ((ExplosionS2CPacket)packet2).getZ());
         if (c.contains(pos2)) {
            this.zF(pos2);
         }
      }

      if (packet2 instanceof EntitySpawnS2CPacket && ((EntitySpawnS2CPacket)packet2).getEntityType().equals(EntityType.END_CRYSTAL) && (Boolean)this.spawn.atx()) {
         Iterator iterator2 = c.iterator();

         while (iterator2.hasNext()) {
            BlockPos pos3 = (BlockPos)iterator2.next();
            BlockPos temp = pos3;
            BlockPos pos4 = BlockPos.ofFloored(((EntitySpawnS2CPacket)packet2).getX(), ((EntitySpawnS2CPacket)packet2).getY(), ((EntitySpawnS2CPacket)packet2).getZ());
            if (temp.equals(pos4) && this.zF(pos3)) {
               break;
            }
         }
      }
   }

   static boolean zU(Entity entity2) {
      boolean flag;
      if (entity2 instanceof EndCrystalEntity && Vec3dEntityUtils.vK(client.player, entity2.getPos(), true) < WorldUtils.NL()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   boolean zV(BlockPos pos) {
      if ((Boolean)this.mineDownward.atx()) {
         BlockPos pos2 = WorldUtils.MX(client.player, false).down();
         if (pos.equals(pos2) && Managers.breakManager.Rx(pos)) {
            return false;
         }
      }

      return this.zF(pos);
   }

   boolean zW(BlockPos pos) {
      if ((Boolean)this.mineDownward.atx()) {
         BlockPos pos2 = WorldUtils.MX(client.player, false).down();
         if (pos.equals(pos2) && Managers.breakManager.Rx(pos)) {
            return false;
         }
      }

      return this.zF(pos);
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
      if (this.timing.atx() == SurroundVanillaSequentialMode.SEQUENTIAL) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSpawnVisible() {
      boolean flag;
      if (this.timing.atx() == SurroundVanillaSequentialMode.SEQUENTIAL) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isExplosionsVisible() {
      boolean flag;
      if (this.timing.atx() == SurroundVanillaSequentialMode.SEQUENTIAL) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
