package pw.hachimi.client.utils.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;

public class BoxUtils {
   public static final Direction[] a_ = Direction.values();
   public static final Direction[] b = new Direction[]{Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH};
   public static final Direction[] c = new Direction[]{Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.DOWN};
   public static final Direction[] d = new Direction[]{Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.UP};
   private static final long a = 0L;

   public static Vec3d jx(Vec3d vec, double d2, double d3, double d4, double d5, double d6, double d7) {
      double d8 = Math.clamp(vec.x, d2, d5);
      double d9 = Math.clamp(vec.y, d3, d6);
      double d10 = Math.clamp(vec.z, d4, d7);
      return new Vec3d(d8, d9, d10);
   }

   public static Vec3d jy(Vec3d vec, Box box) {
      return jx(vec, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
   }

   public static Vec3d jz(Box box) {
      return jy(Mc.client.player.getEyePos(), box);
   }

   /**
    * Smallest axis-aligned box covering every BlockPos in the list.
    * The empty list retains the original Integer.MAX_VALUE/MIN_VALUE sentinels.
    * Recovered from the flattened XOR-state dispatcher; no packet or rendering side effects.
    */
   public static Box jA(List list) {
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int minZ = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      int maxZ = Integer.MIN_VALUE;
      Iterator iterator = list.iterator();

      while (iterator.hasNext()) {
         BlockPos pos = (BlockPos)iterator.next();
         if (pos.getX() < minX) {
            minX = pos.getX();
         }
         if (pos.getY() < minY) {
            minY = pos.getY();
         }
         if (pos.getZ() < minZ) {
            minZ = pos.getZ();
         }
         if (pos.getX() > maxX) {
            maxX = pos.getX();
         }
         if (pos.getY() > maxY) {
            maxY = pos.getY();
         }
         if (pos.getZ() > maxZ) {
            maxZ = pos.getZ();
         }
      }
      return new Box(minX, minY, minZ, maxX, maxY, maxZ);
   }

   public static boolean jB(Box box, BlockPos pos) {
      Stream temp = jD(box, pos).stream();
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object pos) {
            return BlockUtils.TP((BlockPos)pos);
         }
      };
      return temp.anyMatch(predicate);
   }

   public static Direction jC(BlockPos pos, LivingEntity living) {
      if (Math.abs(living.getX() - (pos.getX() + 0.5)) < 2.0 && Math.abs(living.getZ() - (pos.getZ() + 0.5)) < 2.0) {
         EntityPose entityPose = living.getPose();
         double d2 = living.getY() + living.getEyeHeight(entityPose);
         if (d2 - pos.getY() > 2.0) {
            return Direction.UP;
         }

         if (pos.getY() - d2 > 0.0) {
            return Direction.DOWN;
         }
      }

      return living.getHorizontalFacing().getOpposite();
   }

   public static List jD(Box box, BlockPos pos) {
      ArrayList arrayList = new ArrayList();

      for (int n = (int)Math.floor(box.minX); n < Math.ceil(box.maxX); n++) {
         for (int n2 = (int)Math.floor(box.minZ); n2 < Math.ceil(box.maxZ); n2++) {
            ArrayList temp = arrayList;
            BlockPos pos2 = new BlockPos(n, pos.getY(), n2);
            temp.add(pos2);
         }
      }

      return arrayList;
   }

   public static List jE(Box box) {
      ArrayList arrayList = new ArrayList();

      for (int n = (int)Math.floor(box.minX); n < Math.ceil(box.maxX); n++) {
         for (int n2 = (int)Math.floor(box.minY); n2 < Math.ceil(box.maxY); n2++) {
            for (int n3 = (int)Math.floor(box.minZ); n3 < Math.ceil(box.maxZ); n3++) {
               ArrayList temp = arrayList;
               BlockPos pos = new BlockPos(n, n2, n3);
               temp.add(pos);
            }
         }
      }

      return arrayList;
   }

   public static List jF(Box box) {
      ArrayList arrayList = new ArrayList();
      int n = MathHelper.floor(box.minX);
      int n2 = MathHelper.floor(box.minY);
      int n3 = MathHelper.floor(box.minZ);
      int n4 = MathHelper.floor(box.maxX);
      int n5 = MathHelper.floor(box.maxY);
      int n6 = MathHelper.floor(box.maxZ);

      for (int n7 = n; n7 <= n4; n7++) {
         for (int n8 = n2; n8 <= n5; n8++) {
            for (int n9 = n3; n9 <= n6; n9++) {
               ArrayList temp = arrayList;
               BlockPos pos = new BlockPos(n7, n8, n9);
               temp.add(pos);
            }
         }
      }

      return arrayList;
   }
}
