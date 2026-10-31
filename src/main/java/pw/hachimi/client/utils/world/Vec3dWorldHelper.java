package pw.hachimi.client.utils.world;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import pw.hachimi.client.Mc;

public class Vec3dWorldHelper {
   private static final Vec3d[] a_ = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 0.0, -1.0)};
   private final Vec3d vec;
   private final Vec3d vec2;
   private final ArrayList arrayList;
   private final ArrayList arrayList2;
   private ArrayList arrayList3;
   private static final long a = 0L;

   public Vec3dWorldHelper(Vec3d vec3, Vec3d vec4) {
      this.arrayList = new ArrayList();
      this.vec = this.rp(vec3);
      this.arrayList2 = new ArrayList();
      this.vec2 = this.rp(vec4);
      this.arrayList3 = new ArrayList();
   }

   public static boolean rg(Vec3d vec3) {
      return rh((int)vec3.getX(), (int)vec3.getY(), (int)vec3.getZ());
   }

   public static boolean rh(int n, int n2, int n3) {
      BlockPos pos = new BlockPos(n, n2, n3);
      BlockPos pos2 = new BlockPos(n, n2 + 1, n3);
      BlockPos pos3 = new BlockPos(n, n2 - 1, n3);
      if (!ri(pos) && !ri(pos2)) {
         if (!ri(pos3)) {
         }

         if (rj(pos3)) {
            return true;
         }
      }

      return false;
   }

   private static boolean ri(BlockPos pos) {
      BlockState temp = Mc.client.world.getBlockState(pos);
      ClientWorld world2 = Mc.client.world;
      boolean flag;
      if (!temp.isFullCube((BlockView)world2, pos)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof SlabBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof StairsBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof CactusBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof ChestBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof EnderChestBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof SkullBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof PaneBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof FenceBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof WallBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof StainedGlassBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof PistonBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof PistonExtensionBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof PistonHeadBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof StainedGlassBlock)
         && !(Mc.client.world.getBlockState(pos).getBlock() instanceof TrapdoorBlock)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private static boolean rj(BlockPos pos) {
      boolean flag;
      if (!(Mc.client.world.getBlockState(pos).getBlock() instanceof FenceBlock) && !(Mc.client.world.getBlockState(pos).getBlock() instanceof WallBlock)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public ArrayList getArrayList3() {
      return this.arrayList3;
   }

   public void rl() {
      this.rm();
   }

   public void rm() {
      this.arrayList3.clear();
      this.arrayList2.clear();
      ArrayList arrayList4 = new ArrayList();
      Vec3d vec3 = this.vec;
      arrayList4.add(vec3);
      ArrayList temp = this.arrayList2;
      Vec3d vec4 = this.vec2;
      Vec3dHelper vec3dHelper = a(this.vec, null, arrayList4, this.vec.squaredDistanceTo(vec4), 0.0, 0.0);
      temp.add(vec3dHelper);
      int n = 0;
      temp = this.arrayList2;
      UnidentifiedHelper13 unidentifiedHelper13 = new UnidentifiedHelper13();
      temp.sort(unidentifiedHelper13);
      int n2 = 0;
      if (this.arrayList2.isEmpty()) {
         temp = this.arrayList;
         UnidentifiedHelper13 unidentifiedHelper132 = new UnidentifiedHelper13();
         temp.sort(unidentifiedHelper132);
         this.arrayList3 = ((Vec3dHelper)this.arrayList.getFirst()).getArrayList();
      } else {
         label104: {
            Iterator iterator2 = new ArrayList(this.arrayList2).iterator();
            Vec3dHelper vec3dHelper2;
            Vec3d[] vecs;
            int n3;
            if (iterator2.hasNext()) {
               vec3dHelper2 = (Vec3dHelper)iterator2.next();
               n2++;
               this.arrayList2.remove(vec3dHelper2);
               this.arrayList.add(vec3dHelper2);
               vecs = a_;
               int n4 = vecs.length;
               n3 = 0;
               Vec3d vec5 = vecs[0];
               Vec3d vec6 = this.rp(vec3dHelper2.getVec().add(vec5));
               if (rg(vec6) && this.ro(vec3dHelper2, vec6)) {
                  temp = this.arrayList;
                  UnidentifiedHelper13 unidentifiedHelper133 = new UnidentifiedHelper13();
                  temp.sort(unidentifiedHelper133);
                  this.arrayList3 = ((Vec3dHelper)this.arrayList.getFirst()).getArrayList();
                  return;
               }

               n3++;
            } else {
               while (true) {
                  if (++n >= 1000) {
                     break label104;
                  }

                  temp = this.arrayList2;
                  UnidentifiedHelper13 unidentifiedHelper134 = new UnidentifiedHelper13();
                  temp.sort(unidentifiedHelper134);
                  n2 = 0;
                  if (this.arrayList2.isEmpty()) {
                     break label104;
                  }

                  iterator2 = new ArrayList(this.arrayList2).iterator();
                  if (iterator2.hasNext()) {
                     vec3dHelper2 = (Vec3dHelper)iterator2.next();
                     if (++n2 <= 4) {
                        this.arrayList2.remove(vec3dHelper2);
                        this.arrayList.add(vec3dHelper2);
                        vecs = a_;
                        int n5 = vecs.length;
                        n3 = 0;
                        break;
                     }
                  }
               }
            }

            label88:
            while (true) {
               while (n3 < 4) {
                  Vec3d vec7 = vecs[n3];
                  Vec3d vec8 = this.rp(vec3dHelper2.getVec().add(vec7));
                  if (rg(vec8) && this.ro(vec3dHelper2, vec8)) {
                     temp = this.arrayList;
                     UnidentifiedHelper13 unidentifiedHelper135 = new UnidentifiedHelper13();
                     temp.sort(unidentifiedHelper135);
                     this.arrayList3 = ((Vec3dHelper)this.arrayList.getFirst()).getArrayList();
                     return;
                  }

                  n3++;
               }

               Vec3d vec9 = this.rp(vec3dHelper2.getVec().add(0.0, 1.0, 0.0));
               Vec3d vec10;
               if ((!rg(vec9) || !this.ro(vec3dHelper2, vec9)) && (!rg(vec10 = this.rp(vec3dHelper2.getVec().add(0.0, -1.0, 0.0))) || !this.ro(vec3dHelper2, vec10))) {
                  if (iterator2.hasNext()) {
                     vec3dHelper2 = (Vec3dHelper)iterator2.next();
                     if (++n2 <= 4) {
                        this.arrayList2.remove(vec3dHelper2);
                        this.arrayList.add(vec3dHelper2);
                        vecs = a_;
                        int n6 = vecs.length;
                        n3 = 0;
                        continue;
                     }
                  }

                  while (++n < 1000) {
                     temp = this.arrayList2;
                     UnidentifiedHelper13 unidentifiedHelper136 = new UnidentifiedHelper13();
                     temp.sort(unidentifiedHelper136);
                     n2 = 0;
                     if (this.arrayList2.isEmpty()) {
                        break;
                     }

                     iterator2 = new ArrayList(this.arrayList2).iterator();
                     if (iterator2.hasNext()) {
                        vec3dHelper2 = (Vec3dHelper)iterator2.next();
                        if (++n2 <= 4) {
                           this.arrayList2.remove(vec3dHelper2);
                           this.arrayList.add(vec3dHelper2);
                           vecs = a_;
                           int n7 = vecs.length;
                           n3 = 0;
                           continue label88;
                        }
                     }
                  }

                  temp = this.arrayList;
                  UnidentifiedHelper13 unidentifiedHelper137 = new UnidentifiedHelper13();
                  temp.sort(unidentifiedHelper137);
                  this.arrayList3 = ((Vec3dHelper)this.arrayList.getFirst()).getArrayList();
                  return;
               }

               temp = this.arrayList;
               UnidentifiedHelper13 unidentifiedHelper138 = new UnidentifiedHelper13();
               temp.sort(unidentifiedHelper138);
               this.arrayList3 = ((Vec3dHelper)this.arrayList.getFirst()).getArrayList();
               return;
            }
         }

         temp = this.arrayList;
         UnidentifiedHelper13 unidentifiedHelper139 = new UnidentifiedHelper13();
         temp.sort(unidentifiedHelper139);
         this.arrayList3 = ((Vec3dHelper)this.arrayList.getFirst()).getArrayList();
      }
   }

   public Vec3dHelper rn(Vec3d vec3) {
      Iterator iterator2 = this.arrayList.iterator();

      while (iterator2.hasNext()) {
         Vec3dHelper vec3dHelper;
         if ((vec3dHelper = (Vec3dHelper)iterator2.next()).getVec().getX() == vec3.getX() && vec3dHelper.getVec().getY() == vec3.getY() && vec3dHelper.getVec().getZ() == vec3.getZ()) {
            return vec3dHelper;
         }
      }

      iterator2 = this.arrayList2.iterator();

      while (iterator2.hasNext()) {
         Vec3dHelper vec3dHelper2;
         if ((vec3dHelper2 = (Vec3dHelper)iterator2.next()).getVec().getX() == vec3.getX() && vec3dHelper2.getVec().getY() == vec3.getY() && vec3dHelper2.getVec().getZ() == vec3.getZ()) {
            return vec3dHelper2;
         }
      }

      return null;
   }

   public boolean ro(Vec3dHelper vec3dHelper, Vec3d vec3) {
      Vec3dHelper vec3dHelper2 = this.rn(vec3);
      double d = 0.0;
      if (vec3dHelper != null) {
         d = 0.0 + vec3dHelper.Hi();
      }

      label27: {
         if (vec3dHelper2 == null) {
            if (vec3.getX() == this.vec2.getX() && vec3.getY() == this.vec2.getY() && vec3.getZ() == this.vec2.getZ()) {
               break label27;
            }

            Vec3d vec4 = this.vec2;
            if (vec3.squaredDistanceTo(vec4) <= 9.0) {
               break label27;
            }

            ArrayList arrayList5 = new ArrayList(vec3dHelper.getArrayList());
            arrayList5.add(vec3);
            ArrayList temp = this.arrayList2;
            Vec3d vec5 = this.vec2;
            Vec3dHelper vec3dHelper3 = a(vec3, vec3dHelper, arrayList5, vec3.squaredDistanceTo(vec5), 0.0, d);
            temp.add(vec3dHelper3);
         } else if (vec3dHelper2.Hg() > 0.0) {
            ArrayList arrayList4 = new ArrayList(vec3dHelper.getArrayList());
            arrayList4.add(vec3);
            vec3dHelper2.setVec(vec3);
            vec3dHelper2.setVec3dHelper(vec3dHelper);
            vec3dHelper2.setArrayList(arrayList4);
            Vec3dHelper vec3dHelper4 = vec3dHelper2;
            Vec3d vec6 = this.vec2;
            vec3dHelper4.Hf(vec3.squaredDistanceTo(vec6));
            vec3dHelper2.Hh();
            vec3dHelper2.Hj(d);
         }

         return false;
      }

      this.arrayList3.clear();
      this.arrayList3 = vec3dHelper.getArrayList();
      this.arrayList3.add(vec3);
      return true;
   }

   public Vec3d rp(Vec3d vec3) {
      return new Vec3d(Math.floor(vec3.x), Math.floor(vec3.y), Math.floor(vec3.z));
   }

   private static Vec3dHelper a(Vec3d vec3, Vec3dHelper vec3dHelper, ArrayList arrayList4, double d, double d2, double d3) {
      return new Vec3dHelper(vec3, vec3dHelper, arrayList4, d, d2, d3);
   }
}
