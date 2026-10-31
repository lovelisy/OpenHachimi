package pw.hachimi.client.module.combat;

import com.google.common.collect.Lists;
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
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.Vec3dEntityUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Box;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
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
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.client.utils.world.PlayerBlockPosHelper;
import pw.hachimi.eventbus.annotation.EventListener;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.HashMap;

public final class AutoTrap extends Multitask {
   private static final List a = new ArrayList();
   private static final Set b_ = new LinkedHashSet();
   private static final Set c = new LinkedHashSet();
   private static final Set d = new LinkedHashSet();
   private static final Set e = new LinkedHashSet();
   private static AutoTrap INSTANCE;
   Setting multitask = this.registerSetting(new BooleanSetting("Multitask", "Allows placing while eating", true));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Delay, in ticks, between placements", 0, 2, 20));
   Setting bpt = this.registerSetting(new NumberSetting("BPT", "in places, number of blocks", 1, 1, 8));
   Setting blocks = this.registerSetting(new BlockListSetting("Blocks", "What blocks to use for trap", Blocks.OBSIDIAN));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotates to block before placing", false));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "The distance of your block reach", 0.0, 4.5, 6.0));
   Setting breakCrystal = this.registerSetting(new BooleanSetting("BreakCrystal", "Break crystals when placing blocks", true));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "The distance of your entity reach", 0.0, 3.0, 6.0));
   Setting assumeBreak = this.registerSetting(new BooleanSetting("AssumeBreak", "Assuming it has been broken", true));
   Setting breakTime = this.registerSetting(new NumberSetting("BreakTime", "Break time", 0.0, 0.7, 2.0, this::isBreakTimeVisible));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting protect = this.registerSetting(new BooleanSetting("Protect", "Attempts to break crystals around surround positions to prevent surround break", true));
   Setting support = this.registerSetting(new NumberSetting("Support", "The maximum number of blocks for helper can place", 0, 2, 8));
   Setting calcDelay = this.registerSetting(new NumberSetting("CalcDelay", "Delay, in ticks, between support calculations", 0, 3, 20, this::isCalcDelayVisible));
   Setting multi = this.registerSetting(new BooleanSetting("Multi", "Allow multiple assignblocks", true, this::isMultiVisible));
   Setting standHead = this.registerSetting(new BooleanSetting("StandHead", "Place a block at targets head", true));
   Setting crawlHead = this.registerSetting(new BooleanSetting("CrawlHead", "Place a block at targets head", true));
   Setting feet = this.registerSetting(new BooleanSetting("Feet", "Place a block at targets feet", true));
   Setting chest = this.registerSetting(new BooleanSetting("Chest", "Place a block at targets chest", true));
   Setting antiStep = this.registerSetting(new BooleanSetting("AntiStep", "Prevents target from stepping out of the trap", false));
   Setting floor = this.registerSetting(new BooleanSetting("Floor", "Place a block at targets floor", false));
   int B;
   private int C;
   private int D;
   private static final long b = 0L;
   private static final String[] j = null;
   private static final String[] k = null;
   private static final Map l = new HashMap();

   public AutoTrap() {
      super("AutoTrap", "Fully traps enemies with blocks", ModuleCategory.COMBAT, 900);
      INSTANCE = this;
   }

   public static AutoTrap of() {
      return INSTANCE;
   }

   @Override
   public void awe() {
      this.D = 0;
   }

   @EventListener
   public void og(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (!Blink.bm()) {
         if ((Boolean)this.multitask.atx() || !client.player.isUsingItem()) {
            PlayerEntity player2 = this.ol();
            if (player2 != null) {
               this.om(player2.getBoundingBox(), player2);
               if (this.D > 0) {
                  this.D--;
               } else {
                  this.D = (Integer)this.delay.atx();
                  this.B = 0;
                  Set temp = d;
                  final AutoTrap autoTrap = this;
                  Predicate predicate = new Predicate() {
                     @Override
                     public boolean test(Object tickEntitiesPreEvent) {
                        return autoTrap.oj((BlockPos)tickEntitiesPreEvent);
                     }
                  };
                  temp.removeIf(predicate);
                  temp = e;
                  final AutoTrap autoTrap2 = this;
                  Predicate predicate2 = new Predicate() {
                     @Override
                     public boolean test(Object tickEntitiesPreEvent) {
                        return autoTrap2.oj((BlockPos)tickEntitiesPreEvent);
                     }
                  };
                  temp.removeIf(predicate2);
                  temp = c;
                  final AutoTrap autoTrap3 = this;
                  Predicate predicate3 = new Predicate() {
                     @Override
                     public boolean test(Object tickEntitiesPreEvent) {
                        return autoTrap3.oj((BlockPos)tickEntitiesPreEvent);
                     }
                  };
                  temp.removeIf(predicate3);
                  if (!b_.isEmpty()) {
                     temp = b_;
                     final AutoTrap autoTrap4 = this;
                     Predicate predicate4 = new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return autoTrap4.oj((BlockPos)tickEntitiesPreEvent);
                        }
                     };
                     temp.removeIf(predicate4);
                     temp = d;
                     final AutoTrap autoTrap5 = this;
                     Predicate predicate5 = new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return autoTrap5.oj((BlockPos)tickEntitiesPreEvent);
                        }
                     };
                     temp.removeIf(predicate5);
                     temp = e;
                     final AutoTrap autoTrap6 = this;
                     Predicate predicate6 = new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return autoTrap6.oj((BlockPos)tickEntitiesPreEvent);
                        }
                     };
                     temp.removeIf(predicate6);
                     temp = c;
                     final AutoTrap autoTrap7 = this;
                     Predicate predicate7 = new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return autoTrap7.oj((BlockPos)tickEntitiesPreEvent);
                        }
                     };
                     temp.removeIf(predicate7);
                  }
               }
            }
         }
      }
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof BundleS2CPacket) {
            Iterator iterator2 = ((BundleS2CPacket)packet2).getPackets().iterator();

            while (iterator2.hasNext()) {
               Packet packet3 = (Packet)iterator2.next();
               this.oi(packet3);
            }
         } else {
            this.oi(channelRead0Event.getPacket());
         }
      }
   }

   private void oi(Packet packet2) {
      if (packet2 instanceof BlockUpdateS2CPacket) {
         BlockState state = ((BlockUpdateS2CPacket)packet2).getState();
         BlockPos pos = ((BlockUpdateS2CPacket)packet2).getPos();
         if ((e.contains(pos) || c.contains(pos)) && state.isReplaceable()) {
            this.ok(pos, true);
         }
      }
   }

   boolean oj(BlockPos pos) {
      if (this.B >= (Integer)this.bpt.atx()) {
         return false;
      } else if (this.ok(pos, false)) {
         this.B++;
         return true;
      } else {
         return false;
      }
   }

   private boolean ok(BlockPos pos, boolean flag) {
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
               return AutoTrap.oy((Entity)pos);
            }
         };
         Iterator iterator2 = client.world.getOtherEntities((Entity)null, box, predicate).iterator();

         while (iterator2.hasNext()) {
            PlacementUtils.Fp((Entity)iterator2.next(), (Double)this.breakRange.atx(), (Boolean)this.rotate.atx());
         }
      }

      return flag2;
   }

   private PlayerEntity ol() {
      Stream temp = Lists.newArrayList(client.world.getEntities()).stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return AutoTrap.ox((Entity)obj);
         }
      };
      temp = temp.filter(predicate);
      final AutoTrap autoTrap = this;
      predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return autoTrap.ow((Entity)obj);
         }
      };
      temp = temp.filter(predicate);
      ToDoubleFunction toDoubleFunction = new ToDoubleFunction() {
         @Override
         public double applyAsDouble(Object obj) {
            return AutoTrap.ov((Entity)obj);
         }
      };
      temp = temp;
      Comparator comparator = Comparator.comparingDouble(toDoubleFunction);
      return (PlayerEntity)temp.min(comparator).orElse(null);
   }

   private void om(Box box, PlayerEntity player2) {
      this.or();
      List list = WorldUtils.MZ(player2.getPos());
      Iterator iterator2 = list.iterator();

      while (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         if ((Boolean)this.standHead.atx() && !player2.isCrawling() || (Boolean)this.crawlHead.atx() && player2.isCrawling()) {
            BlockPos pos2 = new Mutable((double)pos.getX(), box.getLengthY() + (double)pos.getY(), (double)pos.getZ()).up();
            this.on(pos2, d);
            if ((Boolean)this.antiStep.atx()) {
               this.on(pos2.up(), d);
            }

            if (this.oq()) {
               this.oo(pos2);
            }
         }

         if ((Boolean)this.floor.atx() && !list.contains(pos.down())) {
            this.on(pos.down(), e);
         }

         if ((Boolean)this.feet.atx()) {
            Direction[] directions = BoxUtils.b;

            for (int n = 0; n < directions.length; n++) {
               BlockPos pos3 = pos.offset(directions[n]);
               if (!list.contains(pos3)) {
                  this.on(pos3, c);
               }
            }
         }

         if ((Boolean)this.chest.atx()) {
            Direction[] directions2 = BoxUtils.b;

            for (int n2 = 0; n2 < directions2.length; n2++) {
               BlockPos pos4 = pos.offset(directions2[n2]);
               if (!list.contains(pos4)) {
                  this.on(pos4.up(), c);
               }
            }
         }
      }

      this.os();
      this.C = this.C > 0 ? this.C - 1 : (Integer)this.calcDelay.atx();
   }

   private void on(BlockPos pos, Set set) {
      set.add(pos);
      if (this.oq()) {
         this.oo(pos);
      }
   }

   private void oo(BlockPos pos) {
      if (client.world.getBlockState(pos).getBlock() != Blocks.BEDROCK) {
         if (PlacementUtils.Fk(pos, (Double)this.placeRange.atx()) == null) {
            double temp = (Double)this.placeRange.atx();
            int n = (Integer)this.support.atx();
            double d2 = temp;
            List list = PlacementUtils.Fs(pos, d2, n);
            if (this.op(list)) {
               a.add(list);
            }
         }
      }
   }

   private boolean op(List list) {
      boolean flag;
      if (!list.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list.getLast()).getPos())) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean oq() {
      boolean flag;
      if (this.C <= 0 && (Integer)this.support.atx() > 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private void or() {
      c.clear();
      b_.clear();
      e.clear();
      d.clear();
      if (this.C <= 0) {
         a.clear();
      }
   }

   private void os() {
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
                  AutoTrap.ou((List)obj);
               }
            };
            optional.ifPresent(consumer);
         }
      }
   }

   public boolean ot() {
      boolean flag;
      if (b_.isEmpty() && c.isEmpty() && e.isEmpty() && d.isEmpty()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   static void ou(List list) {
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

   static double ov(Entity entity2) {
      return client.player.squaredDistanceTo(entity2);
   }

   boolean ow(Entity entity2) {
      boolean flag;
      if (client.player.squaredDistanceTo(entity2) <= ((NumberSetting)this.placeRange).abq()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean ox(Entity entity2) {
      boolean flag;
      if (entity2 instanceof PlayerEntity && entity2.isAlive() && client.player != entity2 && !Managers.friendManager.awt(entity2.getName())) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean oy(Entity entity2) {
      return entity2 instanceof EndCrystalEntity && Vec3dEntityUtils.vK(client.player, entity2.getPos(), false) < (double)WorldUtils.NL();
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

   private static String e(byte[] bs) {
      int n = 0;
      int n2;
      char[] cs = new char[n2 = bs.length];

      for (int n3 = 0; n3 < n2; n3++) {
         int n4;
         if ((n4 = 255 & bs[n3]) < 192) {
            cs[n++] = (char)n4;
         } else if (n4 < 224) {
            char c2 = (char)((char)(n4 & 31) << 6);
            byte b2 = bs[++n3];
            c2 = (char)(c2 | (char)(b2 & 63));
            cs[n++] = c2;
         } else if (n3 < n2 - 2) {
            char c3 = (char)((char)(n4 & 15) << '\f');
            byte b3 = bs[++n3];
            c3 = (char)(c3 | (char)(b3 & 63) << 6);
            b3 = bs[++n3];
            c3 = (char)(c3 | (char)(b3 & 63));
            cs[n++] = c3;
         }
      }

      return new String(cs, 0, n);
   }

   private static String e(int n, long l2) throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
      int n2 = n ^ (int)(l2 & 32767L) ^ 20219;
      if (k[n2] == null) {
         Object[] objs;
         try {
            Long longValue = Thread.currentThread().threadId();
            objs = (Object[])l.get(longValue);
            if (objs == null) {
               objs = new Object[]{Cipher.getInstance("DES/CBC/PKCS5Padding"), SecretKeyFactory.getInstance("DES"), new IvParameterSpec(new byte[8])};
               l.put(longValue, objs);
            }
         } catch (Exception exception) {
            throw new RuntimeException("pw/hachimi/client/\u0000dE\u200e", exception);
         }

         byte[] bs = new byte[8];
         bs[0] = (byte)(l2 >>> 56);

         for (int n3 = 1; n3 < 8; n3++) {
            bs[n3] = (byte)(l2 << n3 * 8 >>> 56);
         }

         DESKeySpec desKeySpec = new DESKeySpec(bs);
         SecretKey secretKey = ((SecretKeyFactory)objs[1]).generateSecret(desKeySpec);
         ((Cipher)objs[0]).init(2, secretKey, (IvParameterSpec)objs[2]);
         byte[] bs2 = j[n2].getBytes("ISO-8859-1");
         k[n2] = e(((Cipher)objs[0]).doFinal(bs2));
      }

      return k[n2];
   }

   private static Object e(MutableCallSite mutableCallSite, Object[] objs) throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
      int n = (Integer)objs[0];
      long l2 = (Long)objs[1];
      String str = e(n, l2);
      MethodHandle methodHandle = MethodHandles.constant(String.class, str);
      mutableCallSite.setTarget(MethodHandles.dropArguments(methodHandle, 0, int.class, long.class));
      return str;
   }

   private static CallSite e(Lookup lookup, String str, MethodType methodType) {
      MutableCallSite mutableCallSite = new MutableCallSite(methodType);

      try {
         MethodHandle methodHandle = lookup.findStatic(AutoTrap.class, "e", MethodType.methodType(Object.class, MutableCallSite.class, Object[].class));
         methodHandle = methodHandle.asCollector(Object[].class, methodType.parameterCount());
         methodHandle = MethodHandles.insertArguments(methodHandle, 0, mutableCallSite);
         mutableCallSite.setTarget(MethodHandles.explicitCastArguments(methodHandle, methodType));
      } catch (Exception exception) {
         throw new RuntimeException("pw/hachimi/client/\u0000dE\u200e : " + str + " : " + methodType, exception);
      }

      return mutableCallSite;
   }
}
