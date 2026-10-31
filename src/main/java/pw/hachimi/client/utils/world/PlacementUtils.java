package pw.hachimi.client.utils.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Direction.Axis;
import pw.hachimi.client.Mc;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.model.world.PosNeedPlaceSide;
import pw.hachimi.client.model.world.PosState;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.module.movement.Sprint;
import pw.hachimi.client.utils.player.DirectionPlayerHelper;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.world.BlockPosDirectionBase;

public class PlacementUtils {
   public static final List a_ = new ArrayList();
   private static final BlockState b = Blocks.OBSIDIAN.getDefaultState();
   private static final List c = new ArrayList();
   public static boolean d;
   public static BlockPosDirectionBase blockPosDirectionBase = DirectionPlayerHelper.INSTANCE;
   public static SwingMode swingMode = SwingMode.Normal;
   public static SwingMode swingMode2 = SwingMode.Normal;
   public static boolean h;
   public static boolean i;
   public static boolean j;
   public static boolean k;
   public static boolean l;
   public static boolean m;
   public static boolean n;
   public static boolean o = true;
   public static boolean p;
   private static final long a = 0L;

   public static void EZ() {
      a_.clear();
      blockPosDirectionBase = BlockPosDirectionBase.Hp(HitResultWorldHelper.Sq().Sw());
      swingMode = HitResultWorldHelper.Sq().Sx();
      swingMode2 = HitResultWorldHelper.Sq().Sy();
      i = HitResultWorldHelper.Sq().SG();
      j = HitResultWorldHelper.Sq().Sv();
      k = HitResultWorldHelper.Sq().SA();
      m = HitResultWorldHelper.Sq().SF();
      n = HitResultWorldHelper.Sq().SE();
      p = HitResultWorldHelper.Sq().SB();
      l = HitResultWorldHelper.Sq().SD();
      h = HitResultWorldHelper.Sq().SC();
      d = false;
   }

   public static void Fa(BlockPos pos, Hand hand, boolean flag) {
      Direction direction = Fl(pos);
      if (direction != null) {
         Fd(pos, direction, hand, true, flag);
      }
   }

   public static void Fb(BlockPos pos, Hand hand, boolean flag, boolean flag2, boolean flag3) {
      Direction direction = Fl(pos);
      if (direction != null) {
         Fd(pos, direction, hand, flag, flag2);
      } else if (flag3) {
         Direction direction2 = Fh(pos);
         if (direction2 != null) {
            Fc(pos, direction2, hand, flag, flag2);
         }
      }
   }

   public static void Fc(BlockPos pos, Direction direction, Hand hand, boolean flag, boolean flag2) {
      if (hand == Hand.MAIN_HAND && i) {
         hand = Hand.OFF_HAND;
         ClientPlayNetworkHandler temp = Mc.client.getNetworkHandler();
         PlayerActionC2SPacket playerActionC2SPacket = new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN);
         temp.sendPacket((Packet)playerActionC2SPacket);
         Fg(pos, direction, hand, flag, flag2);
         temp = Mc.client.getNetworkHandler();
         PlayerActionC2SPacket playerActionC2SPacket2 = new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN);
         temp.sendPacket((Packet)playerActionC2SPacket2);
      } else {
         Fg(pos, direction, hand, flag, flag2);
      }
   }

   public static void Fd(BlockPos pos, Direction direction, Hand hand, boolean flag, boolean flag2) {
      ClientWorld temp = Mc.client.world;
      BlockPos pos2 = pos.offset(direction);
      if (BlockStateUtils.Wy(temp.getBlockState(pos2), pos.offset(direction)) && !Mc.client.player.isSneaking() && j) {
         ClientPlayNetworkHandler network2 = Mc.client.player.networkHandler;
         ClientCommandC2SPacket clientCommandC2SPacket = new ClientCommandC2SPacket(Mc.client.player, Mode.PRESS_SHIFT_KEY);
         network2.sendPacket((Packet)clientCommandC2SPacket);
         Fg(pos.offset(direction), direction.getOpposite(), hand, flag, flag2);
         ClientPlayNetworkHandler network3 = Mc.client.player.networkHandler;
         ClientCommandC2SPacket clientCommandC2SPacket2 = new ClientCommandC2SPacket(Mc.client.player, Mode.RELEASE_SHIFT_KEY);
         network3.sendPacket((Packet)clientCommandC2SPacket2);
      } else {
         Fg(pos.offset(direction), direction.getOpposite(), hand, flag, flag2);
      }
   }

   public static boolean Fe(BlockPos pos, boolean flag) {
      Direction direction = Fh(pos);
      return (Direction)direction == null ? false : Ff(pos, direction, flag);
   }

   public static boolean Ff(BlockPos pos, Direction direction, boolean flag) {
      Fg(pos, direction, Hand.MAIN_HAND, p, flag);
      return true;
   }

   public static void Fg(BlockPos pos, Direction direction, Hand hand, boolean flag, boolean flag2) {
      Vec3d vec = Fn(pos, direction);
      if (flag2) {
         float[] fs = RotationUtils.nM(vec);
         RotationUtils.nH(fs[0], fs[1]);
      }

      BlockHitResult blockHitResult = new BlockHitResult(vec, direction, pos, false);
      if (!flag) {
         ClientPlayerEntity player2 = Mc.client.player;
         Mc.client.interactionManager.interactBlock(player2, hand, blockHitResult);
      } else {
         final BlockHitResult blockHitResult2 = blockHitResult;
         final Hand hand2 = hand;
         Managers.serverManager.iQ(new SequencedPacketCreator() {
            public Packet predict(int direction) {
               return PlacementUtils.Fv(hand2, blockHitResult2, direction);
            }
         });
         if (h) {
            Item item2 = Managers.inventoryTracker.JW().getItem();
            if (item2 instanceof BlockItem) {
               BlockSoundGroup blockSoundGroup = ((BlockItem)item2).getBlock().getDefaultState().getSoundGroup();
               ClientWorld temp = Mc.client.world;
               ClientPlayerEntity temp2 = Mc.client.player;
               SoundEvent temp3 = blockSoundGroup.getPlaceSound();
               SoundCategory temp4 = SoundCategory.BLOCKS;
               float temp5 = (blockSoundGroup.getVolume() + 1.0F) / 2.0F;
               float f = blockSoundGroup.getPitch() * 0.8F;
               float f2 = temp5;
               SoundCategory soundCategory = temp4;
               SoundEvent soundEvent = temp3;
               ClientPlayerEntity player3 = temp2;
               temp.playSound((PlayerEntity)player3, pos, soundEvent, soundCategory, f2, f);
            }
         }
      }

      if (flag2) {
         RotationUtils.nK();
      }

      swingMode2.avX(hand);
      Mc.client.itemUseCooldown = 4;
   }

   public static Direction Fh(BlockPos pos) {
      return blockPosDirectionBase.Hq(pos, 57279261692486L);
   }

   public static Direction Fi(BlockPos pos, double d2) {
      Direction direction = blockPosDirectionBase.Hq(pos, 57279261692486L);
      if (direction != null) {
         Vec3d vec = Fn(pos, direction);
         if (Mc.client.player.getEyePos().distanceTo(vec) <= d2) {
            return direction;
         }
      }

      return null;
   }

   public static Direction Fj(BlockPos pos, double d2) {
      if (pos.getY() >= Mc.client.world.getBottomY() && pos.getY() < Mc.client.world.getTopY()) {
         if (m) {
            double d3 = d2;
            Direction direction = null;
            Direction[] directions = Direction.values();
            int n2 = directions.length;

            for (int n3 = 0; n3 < n2; n3++) {
               Direction direction2 = directions[n3];
               BlockPos pos2 = pos.offset(direction2);
               BlockState state = Mc.client.world.getBlockState(pos2);
               if (!Fm(state, pos2)
                  && !state.isReplaceable()
                  && state.getFluidState().isEmpty()
                  && blockPosDirectionBase.Hu(pos2, direction2.getOpposite(), Mc.client.world, Mc.client.player, new PosState(pos, Blocks.AIR.getDefaultState()))) {
                  Vec3d vec = Mc.client.player.getEyePos();
                  Vec3d vec2 = Fn(pos2, direction2.getOpposite());
                  double d4 = vec.distanceTo(vec2);
                  if (!(d4 > d3)) {
                     direction = direction2;
                     d3 = d4;
                  }
               }
            }

            return direction;
         } else {
            Direction[] directions2 = Direction.values();
            int n4 = directions2.length;

            for (int n5 = 0; n5 < n4; n5++) {
               Direction direction3 = directions2[n5];
               BlockPos pos3 = pos.offset(direction3);
               BlockState state2 = Mc.client.world.getBlockState(pos3);
               if (!Fm(state2, pos3)
                  && !state2.isReplaceable()
                  && state2.getFluidState().isEmpty()
                  && blockPosDirectionBase.Hu(pos3, direction3.getOpposite(), Mc.client.world, Mc.client.player, new PosState(pos, Blocks.AIR.getDefaultState()))) {
                  Vec3d temp = Mc.client.player.getEyePos();
                  Vec3d vec3 = Fn(pos3, direction3.getOpposite());
                  if (temp.distanceTo(vec3) <= d2) {
                     return direction3;
                  }
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public static Direction Fk(BlockPos pos, double d2) {
      if (pos.getY() >= Mc.client.world.getBottomY() && pos.getY() < Mc.client.world.getTopY()) {
         if (m) {
            double d3 = d2;
            Direction direction = null;
            Direction[] directions = Direction.values();
            int n2 = directions.length;

            for (int n3 = 0; n3 < n2; n3++) {
               Direction direction2 = directions[n3];
               BlockPos pos2 = pos.offset(direction2);
               BlockState state = Mc.client.world.getBlockState(pos2);
               if (l && a_.contains(pos2)
                  ? blockPosDirectionBase.Hu(
                     pos2, direction2.getOpposite(), Mc.client.world, Mc.client.player, new PosState(pos2, Blocks.OBSIDIAN.getDefaultState())
                  )
                  : !Fm(state, pos2)
                     && !state.isReplaceable()
                     && state.getFluidState().isEmpty()
                     && blockPosDirectionBase.Ht(pos2, direction2.getOpposite(), Mc.client.world, Mc.client.player)) {
                  Vec3d vec = Mc.client.player.getEyePos();
                  Vec3d vec2 = Fn(pos2, direction2.getOpposite());
                  double d4 = vec.distanceTo(vec2);
                  if (!(d4 > d3)) {
                     direction = direction2;
                     d3 = d4;
                  }
               }
            }

            return direction;
         } else {
            Direction[] directions2 = Direction.values();
            int n4 = directions2.length;

            for (int n5 = 0; n5 < n4; n5++) {
               Direction direction3 = directions2[n5];
               BlockPos pos3 = pos.offset(direction3);
               BlockState state2 = Mc.client.world.getBlockState(pos3);
               if (l && a_.contains(pos3)
                  ? blockPosDirectionBase.Hu(
                     pos3, direction3.getOpposite(), Mc.client.world, Mc.client.player, new PosState(pos3, Blocks.OBSIDIAN.getDefaultState())
                  )
                  : !Fm(state2, pos3)
                     && !state2.isReplaceable()
                     && state2.getFluidState().isEmpty()
                     && blockPosDirectionBase.Ht(pos3, direction3.getOpposite(), Mc.client.world, Mc.client.player)) {
                  Vec3d temp = Mc.client.player.getEyePos();
                  Vec3d vec3 = Fn(pos3, direction3.getOpposite());
                  if (temp.distanceTo(vec3) <= d2) {
                     return direction3;
                  }
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public static Direction Fl(BlockPos pos) {
      return Fk(pos, Double.MAX_VALUE);
   }

   public static boolean Fm(BlockState state, BlockPos pos) {
      boolean flag;
      if (!j && BlockStateUtils.Wy(state, pos) && !Mc.client.player.isSneaking()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static Vec3d Fn(BlockPos pos, Direction direction) {
      return n ? BoxUtils.jz(Fo(pos, direction)) : pos.toCenterPos().offset(direction, 0.5);
   }

   public static Box Fo(BlockPos pos, Direction direction) {
      Vec3d vec = pos.toCenterPos();
      Vec3i vec3i = direction.getVector();
      double d2 = vec3i.getX();
      double d3 = vec3i.getY();
      double d4 = vec3i.getZ();
      Axis axis = direction.getAxis();
      double d5;
      if (axis != Axis.X) {
         d5 = 0.5;
      } else {
         d5 = 0.0;
      }

      double d6 = d5;
      if (axis != Axis.Y) {
         d5 = 0.5;
      } else {
         d5 = 0.0;
      }

      double d7 = d5;
      if (axis != Axis.Z) {
         d5 = 0.5;
      } else {
         d5 = 0.0;
      }

      double d8 = d5;
      Vec3d vec2 = new Vec3d(vec.getX() + d2 / 2.0, vec.getY() + d3 / 2.0, vec.getZ() + d4 / 2.0);
      return new Box(vec2.getX() - d6, vec2.getY() - d7, vec2.getZ() - d8, vec2.getX() + d6, vec2.getY() + d7, vec2.getZ() + d8);
   }

   public static boolean Fp(Entity entity2, double d2, boolean flag) {
      return Fq(entity2, d2, flag);
   }

   public static boolean Fq(Entity entity2, double d2, boolean flag) {
      Vec3d vec = BoxUtils.jz(entity2.getBoundingBox());
      if (!(Mc.client.player.getEyePos().distanceTo(vec) > d2)) {
         if (flag) {
            float[] fs = RotationUtils.nM(vec);
            RotationUtils.nH(fs[0], fs[1]);
         }

         if (!k) {
            ClientPlayerEntity player2 = Mc.client.player;
            Mc.client.interactionManager.attackEntity((PlayerEntity)player2, entity2);
         } else {
            ClientPlayNetworkHandler temp = Mc.client.player.networkHandler;
            boolean flag2 = Mc.client.player.isSneaking();
            PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.attack(entity2, flag2);
            temp.sendPacket((Packet)playerInteractEntityC2SPacket);
            Mc.client.player.resetLastAttackedTicks();
         }

         Sprint.a = true;
         swingMode.avX(Hand.MAIN_HAND);
         if (flag) {
            RotationUtils.nK();
         }

         Mc.client.player.resetLastAttackedTicks();
         Managers.antiCheatManager.stopwatchImpl.afr(29432, 5265492, (byte)40);
         if (entity2 instanceof EndCrystalEntity) {
            d = true;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean Fr(BlockPos pos, Direction direction) {
      BlockPos pos2 = pos.offset(direction);
      return blockPosDirectionBase.Hu(pos2, direction.getOpposite(), Mc.client.world, Mc.client.player, new PosState(pos2, b));
   }

   public static List Fs(BlockPos pos, double d2, int n2) {
      List list = Ft(pos, d2, n2, null);
      Collections.reverse(list);
      return list;
   }

   public static List Ft(BlockPos pos, double d2, int n2, List list) {
      if (n2 > 0) {
         if (list == null) {
            list = new ArrayList();
         }

         BlockPos pos2 = Fu(pos, d2);
         if (pos2 != null) {
            PosNeedPlaceSide posNeedPlaceSide = new PosNeedPlaceSide(pos2, false);
            list.add(posNeedPlaceSide);
            return list;
         }

         if (n2 <= 1) {
            return c;
         }

         for (int n3 = n2 - 1; n3 < n2; n3++) {
            Direction[] directions = Direction.values();
            int n4 = directions.length;

            label53:
            for (int n5 = 0; n5 < n4; n5++) {
               Direction direction = directions[n5];
               ArrayList arrayList = new ArrayList(list);
               BlockPos pos3 = pos.offset(direction);
               Iterator iterator2 = arrayList.iterator();

               while (iterator2.hasNext()) {
                  if (((PosNeedPlaceSide)iterator2.next()).getPos().equals(pos3)) {
                     continue label53;
                  }
               }

               if (CollisionUtils.aiD(pos3) && blockPosDirectionBase.Hu(pos3, direction.getOpposite(), Mc.client.world, Mc.client.player, new PosState(pos3, b))
                  )
                {
                  if (Fk(pos3, d2) != null) {
                     ArrayList arrayList2 = arrayList;
                     PosNeedPlaceSide posNeedPlaceSide2 = new PosNeedPlaceSide(pos3, false);
                     arrayList2.add(posNeedPlaceSide2);
                     return arrayList;
                  }

                  ArrayList temp = arrayList;
                  PosNeedPlaceSide posNeedPlaceSide3 = new PosNeedPlaceSide(pos3, true);
                  temp.add(posNeedPlaceSide3);
                  List list2 = Ft(pos3, d2, n3, arrayList);
                  if (!list2.isEmpty() && !((PosNeedPlaceSide)list2.getLast()).needPlaceSide()) {
                     return list2;
                  }
               }
            }
         }
      }

      return c;
   }

   public static BlockPos Fu(BlockPos pos, double d2) {
      Direction[] directions = Direction.values();
      int n2 = directions.length;

      for (int n3 = 0; n3 < n2; n3++) {
         Direction direction = directions[n3];
         BlockPos pos2 = pos.offset(direction);
         if (CollisionUtils.aiD(pos2)
            && blockPosDirectionBase.Hu(pos2, direction.getOpposite(), Mc.client.world, Mc.client.player, new PosState(pos2, b))
            && Fk(pos2, d2) != null) {
            return pos2;
         }
      }

      return null;
   }

   static Packet Fv(Hand hand, BlockHitResult blockHitResult, int n2) {
      return new PlayerInteractBlockC2SPacket(hand, blockHitResult, n2);
   }
}
