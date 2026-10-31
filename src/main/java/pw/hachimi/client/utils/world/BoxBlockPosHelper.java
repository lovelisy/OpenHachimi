package pw.hachimi.client.utils.world;

import java.util.Iterator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pw.hachimi.client.model.world.PosState;
import pw.hachimi.client.world.BlockPosDirectionBase;
import pw.hachimi.client.utils.world.BoxBlockPosHelper;

public class BoxBlockPosHelper extends BlockPosDirectionBase {
   public static final BoxBlockPosHelper INSTANCE = new BoxBlockPosHelper();
   private static final double a = 0.4;
   private static final double b_ = 1.62;
   private static final double c = 2.0E-4;
   private static final long b = 0L;

   private static boolean alj(Box box, Box box2) {
      boolean flag;
      if (box2.maxX - 1.0E-7 > box.minX
         && box2.minX + 1.0E-7 < box.maxX
         && box2.maxY - 1.0E-7 > box.minY
         && box2.minY + 1.0E-7 < box.maxY
         && box2.maxZ - 1.0E-7 > box.minZ
         && box2.minZ + 1.0E-7 < box.maxZ) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @Nullable
   @Override
   public Direction Hs(BlockPos pos, PlayerEntity player2, long l, ClientWorld world2) {
      if (PlacementUtils.m) {
         Direction direction = null;
         double d = Double.MAX_VALUE;
         Direction[] directions = Direction.values();
         int n = directions.length;

         for (int n2 = 0; n2 < n; n2++) {
            Direction direction2 = directions[n2];
            Vec3d temp = player2.getEyePos();
            Vec3d vec = pos.toCenterPos().offset(direction2, 0.5);
            double d2 = temp.distanceTo(vec);
            if (d2 < d && this.Ht(pos, direction2, world2, player2)) {
               d = d2;
               direction = direction2;
            }
         }

         return direction;
      } else {
         Direction[] directions2 = Direction.values();
         int n3 = directions2.length;

         for (int n4 = 0; n4 < n3; n4++) {
            Direction direction3 = directions2[n4];
            if (this.Ht(pos, direction3, world2, player2)) {
               return direction3;
            }
         }

         return null;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public boolean Ht(BlockPos pos, Direction direction, ClientWorld world2, PlayerEntity player2) {
      Box box = this.alk(pos, world2);
      Box box2 = (new Box(player2.getX(), player2.getY() + 0.4, player2.getZ(), player2.getX(), player2.getY() + 1.62, player2.getZ())).expand(2.0E-4);
      if (alj(box2, box)) {
         return true;
      }

      switch (UnidentifiedHelper4.a[direction.ordinal()]) {
         case 1:
            if (box2.minZ > box.minZ) {
               return false;
            }
            break;
         case 2:
            if (box2.maxZ < box.maxZ) {
               return false;
            }
            break;
         case 3:
            if (box2.maxX < box.maxX) {
               return false;
            }
            break;
         case 4:
            if (box2.minX > box.minX) {
               return false;
            }
            break;
         case 5:
            if (box2.maxY < box.maxY) {
               return false;
            }
            break;
         case 6:
            if (box2.minY > box.minY) {
               return false;
            }
            break;
         default:
            throw a();
      }

      return true;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public boolean Hu(BlockPos pos, Direction direction, ClientWorld world2, PlayerEntity player2, PosState posState) {
      Box box = this.all(pos, world2, posState);
      Box box2 = (new Box(player2.getX(), player2.getY() + 0.4, player2.getZ(), player2.getX(), player2.getY() + 1.62, player2.getZ())).expand(2.0E-4);
      if (alj(box2, box)) {
         return true;
      }

      switch (UnidentifiedHelper4.a[direction.ordinal()]) {
         case 1:
            if (box2.minZ > box.minZ) {
               return false;
            }
            break;
         case 2:
            if (box2.maxZ < box.maxZ) {
               return false;
            }
            break;
         case 3:
            if (box2.maxX < box.maxX) {
               return false;
            }
            break;
         case 4:
            if (box2.minX > box.minX) {
               return false;
            }
            break;
         case 5:
            if (box2.maxY < box.maxY) {
               return false;
            }
            break;
         case 6:
            if (box2.minY > box.minY) {
               return false;
            }
            break;
         default:
            throw a();
      }

      return true;
   }

   private Box alk(BlockPos pos, World world2) {
      VoxelShape temp = world2.getBlockState(pos).getCollisionShape((BlockView)world2, pos);
      double temp2 = pos.getX();
      double d = pos.getZ();
      double d2 = pos.getY();
      double d3 = temp2;
      VoxelShape voxelShape = temp.offset(d3, d2, d);
      Box box = new Box(pos);
      Iterator iterator2 = voxelShape.getBoundingBoxes().iterator();

      while (iterator2.hasNext()) {
         Box box2 = (Box)iterator2.next();
         double d4 = Math.max(box2.minX, box.minX);
         double d5 = Math.max(box2.minY, box.minY);
         double d6 = Math.max(box2.minZ, box.minZ);
         double d7 = Math.min(box2.maxX, box.maxX);
         double d8 = Math.min(box2.maxY, box.maxY);
         double d9 = Math.min(box2.maxZ, box.maxZ);
         box = new Box(d4, d5, d6, d7, d8, d9);
      }

      return box;
   }

   private Box all(BlockPos pos, World world2, PosState posState) {
      VoxelShape voxelShape;
      if (posState.getPos().equals(pos)) {
         VoxelShape temp = posState.state().getCollisionShape((BlockView)world2, pos);
         double temp2 = pos.getX();
         double d = pos.getZ();
         double d2 = pos.getY();
         double d3 = temp2;
         voxelShape = temp.offset(d3, d2, d);
      } else {
         VoxelShape voxelShape2 = world2.getBlockState(pos).getCollisionShape((BlockView)world2, pos);
         double d4 = pos.getX();
         double d5 = pos.getZ();
         double d6 = pos.getY();
         double d7 = d4;
         voxelShape = voxelShape2.offset(d7, d6, d5);
      }

      Box box = new Box(pos);
      Iterator iterator2 = voxelShape.getBoundingBoxes().iterator();

      while (iterator2.hasNext()) {
         Box box2 = (Box)iterator2.next();
         double d8 = Math.max(box2.minX, box.minX);
         double d9 = Math.max(box2.minY, box.minY);
         double d10 = Math.max(box2.minZ, box.minZ);
         double d11 = Math.min(box2.maxX, box.maxX);
         double d12 = Math.min(box2.maxY, box.maxY);
         double d13 = Math.min(box2.maxZ, box.maxZ);
         box = new Box(d8, d9, d10, d11, d12, d13);
      }

      return box;
   }

   private static MatchException a() {
      return new MatchException(null, null);
   }
}
