package pw.hachimi.client.utils.world;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;

public class Vec3dLivingUtils {
   public static boolean a;

   private static boolean qO(BlockPos pos) {
      ClientWorld temp = Mc.client.world;
      BlockPos pos2 = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
      Block block2 = temp.getBlockState(pos2).getBlock();
      boolean flag;
      if (block2 != Blocks.AIR
         && !(block2 instanceof PlantBlock)
         && block2 != Blocks.VINE
         && block2 != Blocks.LADDER
         && block2 != Blocks.WATER
         && block2 != Blocks.WATER_CAULDRON
         && !(block2 instanceof WallSignBlock)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static ArrayList qP(LivingEntity living, LivingEntity living2) {
      return qQ(new Vec3d(living.getX(), living.getY(), living.getZ()), new Vec3d(living2.getX(), living2.getY(), living2.getZ()));
   }

   public static ArrayList qQ(Vec3d vec, Vec3d vec2) {
      int n;
      Vec3d vec3;
      Vec3d vec4;
      ArrayList arrayList;
      ArrayList arrayList2;
      Iterator iterator2;
      Vec3d vec5;
      label99: {
         if (!qO(BlockPos.ofFloored((Position)vec))) {
            vec = vec.add(0.0, 1.0, 0.0);
            Vec3dWorldHelper vec3dWorldHelper = new Vec3dWorldHelper(vec, vec2);
            vec3dWorldHelper.rl();
            n = 0;
            arrayList = new ArrayList();
            arrayList2 = vec3dWorldHelper.getArrayList3();
            iterator2 = arrayList2.iterator();
            if (!iterator2.hasNext()) {
               return arrayList;
            }

            vec5 = (Vec3d)iterator2.next();
         } else {
            Vec3dWorldHelper vec3dWorldHelper2 = new Vec3dWorldHelper(vec, vec2);
            vec3dWorldHelper2.rl();
            n = 0;
            vec3 = null;
            vec4 = null;
            arrayList = new ArrayList();
            arrayList2 = vec3dWorldHelper2.getArrayList3();
            iterator2 = arrayList2.iterator();
            if (!iterator2.hasNext()) {
               return arrayList;
            }

            vec5 = (Vec3d)iterator2.next();
            if (false) {
               break label99;
            }
         }

         ArrayList temp = arrayList;
         Vec3d vec6 = vec5.add(0.5, 0.0, 0.5);
         temp.add(vec6);
         vec4 = vec5;

         while (true) {
            vec3 = vec5;
            n++;
            if (!iterator2.hasNext()) {
               return arrayList;
            }

            vec5 = (Vec3d)iterator2.next();
            if (n != 0 && n != arrayList2.size() - 1) {
               break;
            }

            if (vec3 != null) {
               temp = arrayList;
               Vec3d vec7 = vec3.add(0.5, 0.0, 0.5);
               temp.add(vec7);
            }

            temp = arrayList;
            vec6 = vec5.add(0.5, 0.0, 0.5);
            temp.add(vec6);
            vec4 = vec5;
         }
      }

      while (true) {
         label71: {
            Vec3d vec8 = vec5;
            Vec3d vec9 = vec4;
            if (!(vec8.squaredDistanceTo(vec9) > 25.0)) {
               double d = Math.min(vec4.getX(), vec5.getX());
               double d2 = Math.min(vec4.getY(), vec5.getY());
               double d3 = Math.min(vec4.getZ(), vec5.getZ());
               double d4 = Math.max(vec4.getX(), vec5.getX());
               double d5 = Math.max(vec4.getY(), vec5.getY());
               double d6 = Math.max(vec4.getZ(), vec5.getZ());
               int n2 = (int)d;

               label68:
               while (true) {
                  if (!(n2 <= d4)) {
                     break label71;
                  }

                  for (int n3 = (int)d2; n3 <= d5; n3++) {
                     for (int n4 = (int)d3; n4 <= d6; n4++) {
                        if (!Vec3dWorldHelper.rh(n2, n3, n4)) {
                           break label68;
                        }
                     }
                  }

                  n2++;
               }
            }

            ArrayList arrayList3 = arrayList;
            Vec3d vec10 = vec3.add(0.5, 0.0, 0.5);
            arrayList3.add(vec10);
            vec4 = vec3;
         }

         while (true) {
            vec3 = vec5;
            n++;
            if (!iterator2.hasNext()) {
               return arrayList;
            }

            vec5 = (Vec3d)iterator2.next();
            if (n != 0 && n != arrayList2.size() - 1) {
               break;
            }

            if (vec3 != null) {
               ArrayList arrayList4 = arrayList;
               Vec3d vec11 = vec3.add(0.5, 0.0, 0.5);
               arrayList4.add(vec11);
            }

            ArrayList arrayList5 = arrayList;
            Vec3d vec12 = vec5.add(0.5, 0.0, 0.5);
            arrayList5.add(vec12);
            vec4 = vec5;
         }
      }
   }
}
