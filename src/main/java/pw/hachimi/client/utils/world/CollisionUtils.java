package pw.hachimi.client.utils.world;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import pw.hachimi.client.Mc;
import pw.hachimi.client.world.BlockEntityManager;
import pw.hachimi.client.world.ChunkWorldManager;

public class CollisionUtils {
   public static Iterable ait() {
      return CollisionUtils::aiH;
   }

   public static Iterable aiu() {
      return ait();
   }

   public static Iterable aiv() {
      return BlockEntityManager::new;
   }

   public static boolean aiw(BlockPos pos) {
      if (Mc.client.world.isAir(pos)) {
         ClientWorld temp = Mc.client.world;
         BlockPos pos2 = pos.add(0, -1, 0);
         if (!temp.isAir(pos2)) {
            temp = Mc.client.world;
            BlockPos pos3 = pos.add(0, 1, 0);
            if (temp.isAir(pos3)) {
               temp = Mc.client.world;
               BlockPos pos4 = pos.add(0, 2, 0);
               if (temp.isAir(pos4)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static boolean aix(double d, double d2) {
      ClientChunkManager clientChunkManager = Mc.client.world.getChunkManager();
      if (clientChunkManager != null) {
         int temp = ChunkSectionPos.getSectionCoord(d);
         int n = ChunkSectionPos.getSectionCoord(d2);
         int n2 = temp;
         return clientChunkManager.isChunkLoaded(n2, n);
      } else {
         return false;
      }
   }

   public static boolean aiy(Entity entity2, Box box) {
      BlockCollisionSpliterator blockCollisionSpliterator = a(Mc.client.world, entity2, box, new BiFunction() {
         @Override
         public Object apply(Object box, Object var2x) {
            return CollisionUtils.aiG((Mutable)box, (VoxelShape)var2x);
         }
      });

      while (blockCollisionSpliterator.hasNext()) {
         if (!((VoxelShape)blockCollisionSpliterator.next()).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   public static boolean aiz(Box box) {
      boolean flag;
      if (aiA(box) == null) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static Entity aiA(Box box) {
      Iterator iterator2 = aiB(box).iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (!entity2.isRemoved() && entity2.intersectionChecked) {
            return entity2;
         }
      }

      return null;
   }

   public static List aiB(Box box) {
      ClientWorld temp = Mc.client.world;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return CollisionUtils.aiF((Entity)obj);
         }
      };
      return temp.getOtherEntities((Entity)null, box, predicate);
   }

   public static boolean aiC(BlockPos pos) {
      return aiE(pos, true, Blocks.OBSIDIAN);
   }

   public static boolean aiD(BlockPos pos) {
      return aiC(pos);
   }

   public static boolean aiE(BlockPos pos, boolean flag, Block block2) {
      if (pos == null) {
         return false;
      }

      if (!World.isValid(pos)) {
         return false;
      }

      if (!Mc.client.world.getBlockState(pos).isReplaceable()) {
         return false;
      }

      if (flag) {
         ClientWorld temp = Mc.client.world;
         BlockState temp2 = block2.getDefaultState();
         ShapeContext shapeContext = ShapeContext.absent();
         BlockState state = temp2;
         if (!temp.canPlace(state, pos, shapeContext)) {
            return false;
         }
      }

      return true;
   }

   static boolean aiF(Entity entity2) {
      boolean flag;
      if (!entity2.isSpectator() && !(entity2 instanceof EndCrystalEntity)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static VoxelShape aiG(Mutable mutable, VoxelShape voxelShape) {
      return voxelShape;
   }

   private static Iterator aiH() {
      return new ChunkWorldManager();
   }

   private static BlockCollisionSpliterator a(CollisionView collisionView, Entity entity2, Box box, BiFunction biFunction) {
      return new BlockCollisionSpliterator(collisionView, entity2, box, false, biFunction);
   }
}
