package pw.hachimi.client.utils.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pw.hachimi.client.model.world.PosState;
import pw.hachimi.client.world.BlockPosDirectionBase;
import pw.hachimi.client.utils.world.BlockPosDirectionHelper2;

public class BlockPosDirectionHelper2 extends BlockPosDirectionBase {
   public static final BlockPosDirectionHelper2 INSTANCE = new BlockPosDirectionHelper2();
   private static final long b = 0L;

   @Nullable
   @Override
   public Direction Hs(BlockPos pos, PlayerEntity player2, long l, ClientWorld world2) {
      BlockPos pos2 = BlockPos.ofFloored(player2.getX(), player2.getEyeY(), player2.getZ());
      if (this.Kd(pos, pos2)) {
         return Direction.DOWN;
      }

      List list = this.Ke(pos, pos2, true);
      if (PlacementUtils.m) {
         Direction direction = null;
         double d = Double.MAX_VALUE;
         Iterator iterator2 = list.iterator();

         while (iterator2.hasNext()) {
            Direction direction2 = (Direction)iterator2.next();
            Vec3d temp = player2.getEyePos();
            Vec3d vec = pos.toCenterPos().offset(direction2, 0.5);
            double d2 = temp.distanceTo(vec);
            if (d2 < d && !this.Kg(pos, world2, list, direction2, true)) {
               d = d2;
               direction = direction2;
            }
         }

         return direction;
      } else {
         Iterator iterator3 = list.iterator();

         while (iterator3.hasNext()) {
            Direction direction3 = (Direction)iterator3.next();
            if (!this.Kg(pos, world2, list, direction3, true)) {
               return direction3;
            }
         }

         return null;
      }
   }

   @Override
   public boolean Ht(BlockPos pos, Direction direction, ClientWorld world2, PlayerEntity player2) {
      BlockPos pos2 = BlockPos.ofFloored(player2.getX(), player2.getEyeY(), player2.getZ());
      if (this.Kd(pos, pos2)) {
         return true;
      }

      boolean flag;
      if (world2.getBlockState(pos).getOutlineShape((BlockView)world2, pos) == VoxelShapes.fullCube()) {
         flag = true;
      } else {
         flag = false;
      }

      boolean flag2 = flag;
      List list = this.Ke(pos, pos2, flag2);
      if (!list.contains(direction)) {
         return false;
      }

      if (!this.Kg(pos, world2, list, direction, flag2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @Override
   public boolean Hu(BlockPos pos, Direction direction, ClientWorld world2, PlayerEntity player2, PosState posState) {
      BlockPos pos2 = BlockPos.ofFloored(player2.getX(), player2.getEyeY(), player2.getZ());
      if (this.Kd(pos, pos2)) {
         return true;
      }

      BlockState state2;
      label29: {
         if (posState.getPos().equals(pos)) {
            BlockState state3 = posState.state();
            if (state3 != null) {
               state2 = state3;
               break label29;
            }
         }

         state2 = world2.getBlockState(pos);
      }

      boolean flag;
      if (state2.getOutlineShape((BlockView)world2, pos) == VoxelShapes.fullCube()) {
         flag = true;
      } else {
         flag = false;
      }

      boolean flag2 = flag;
      List list = this.Ke(pos, pos2, flag2);
      if (!list.contains(direction)) {
         return false;
      }

      if (!this.Kh(pos, world2, list, direction, flag2, posState)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean Kd(BlockPos pos, BlockPos pos2) {
      boolean flag;
      if (pos2.getX() == pos.getX() && pos2.getY() == pos.getY() && pos2.getZ() == pos.getZ()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private List Ke(BlockPos pos, BlockPos pos2, boolean flag) {
      int n = pos2.getX();
      int n2 = pos2.getY();
      int n3 = pos2.getZ();
      int n4 = pos.getX();
      int n5 = pos.getY();
      int n6 = pos.getZ();
      return this.Kf(n - n4, n2 - n5, n3 - n6, flag);
   }

   private List Kf(int n, int n2, int n3, boolean flag) {
      ArrayList arrayList = a();
      if (!flag) {
         if (n == 0) {
            ArrayList temp = arrayList;
            Direction direction = Direction.EAST;
            temp.add(direction);
            temp = arrayList;
            Direction direction2 = Direction.WEST;
            temp.add(direction2);
         }

         if (n3 == 0) {
            ArrayList arrayList2 = arrayList;
            Direction direction3 = Direction.SOUTH;
            arrayList2.add(direction3);
            arrayList2 = arrayList;
            Direction direction4 = Direction.NORTH;
            arrayList2.add(direction4);
         }
      }

      if (n2 == 0) {
         ArrayList arrayList3 = arrayList;
         Direction direction5 = Direction.UP;
         arrayList3.add(direction5);
         arrayList3 = arrayList;
         Direction direction6 = Direction.DOWN;
         arrayList3.add(direction6);
      } else {
         Direction direction7;
         if (n2 > 0) {
            direction7 = Direction.UP;
         } else {
            direction7 = Direction.DOWN;
         }

         arrayList.add(direction7);
      }

      if (n != 0) {
         Direction direction8;
         if (n > 0) {
            direction8 = Direction.EAST;
         } else {
            direction8 = Direction.WEST;
         }

         arrayList.add(direction8);
      }

      if (n3 != 0) {
         Direction direction9;
         if (n3 > 0) {
            direction9 = Direction.SOUTH;
         } else {
            direction9 = Direction.NORTH;
         }

         arrayList.add(direction9);
      }

      return arrayList;
   }

   private boolean Kg(BlockPos pos, ClientWorld world2, List list, Direction direction, boolean flag) {
      if (flag) {
         BlockPos pos2 = pos.offset(direction);
         BlockState state2 = world2.getBlockState(pos2);
         boolean flag2;
         if (state2.getOutlineShape((BlockView)world2, pos2) == VoxelShapes.fullCube() && !state2.getCollisionShape((BlockView)world2, pos2).isEmpty()) {
            flag2 = true;
         } else {
            flag2 = false;
         }

         return flag2;
      } else {
         Iterator iterator2 = list.iterator();

         while (iterator2.hasNext()) {
            Direction direction2 = (Direction)iterator2.next();
            BlockPos pos3 = pos.offset(direction2);
            BlockState state3 = world2.getBlockState(pos3);
            if (state3.getOutlineShape((BlockView)world2, pos3) != VoxelShapes.fullCube() || !state3.getCollisionShape((BlockView)world2, pos3).isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean Kh(BlockPos pos, ClientWorld world2, List list, Direction direction, boolean flag, PosState posState) {
      if (flag) {
         BlockPos pos2;
         BlockState state2;
         label35: {
            pos2 = pos.offset(direction);
            if (posState.getPos().equals(pos2)) {
               BlockState state3 = posState.state();
               if (state3 != null) {
                  state2 = state3;
                  break label35;
               }
            }

            state2 = world2.getBlockState(pos2);
         }

         boolean flag2;
         if (state2.getOutlineShape((BlockView)world2, pos2) == VoxelShapes.fullCube() && !state2.getCollisionShape((BlockView)world2, pos2).isEmpty()) {
            flag2 = true;
         } else {
            flag2 = false;
         }

         return flag2;
      } else {
         Iterator iterator2 = list.iterator();

         while (iterator2.hasNext()) {
            BlockPos pos3;
            BlockState state4;
            label42: {
               Direction direction2 = (Direction)iterator2.next();
               pos3 = pos.offset(direction2);
               if (posState.getPos().equals(pos3)) {
                  BlockState state5 = posState.state();
                  if (state5 != null) {
                     state4 = state5;
                     break label42;
                  }
               }

               state4 = world2.getBlockState(pos3);
            }

            if (state4.getOutlineShape((BlockView)world2, pos3) != VoxelShapes.fullCube() || !state4.getCollisionShape((BlockView)world2, pos3).isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }

   private static ArrayList a() {
      return new ArrayList(6);
   }
}
