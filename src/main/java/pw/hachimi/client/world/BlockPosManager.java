package pw.hachimi.client.world;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.manager.BlockPosWorldManager;
import pw.hachimi.client.utils.world.BlockPosBoxHelper;

public class BlockPosManager implements Callable {
   private final List list;
   final BlockPosWorldManager blockPosWorldManager;
   public static boolean a;

   public BlockPosManager(BlockPosWorldManager blockPosWorldManager2, List list2) {
      this.blockPosWorldManager = blockPosWorldManager2;
      this.list = list2;
   }

   public Set Vc() throws Exception {
      HashSet hashSet = new HashSet();
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         BlockPosBoxHelper blockPosBoxHelper = this.blockPosWorldManager.Ay(pos);
         if (blockPosBoxHelper != null) {
            hashSet.add(blockPosBoxHelper);
         }
      }

      return hashSet;
   }

   @Override
   public Object call() throws Exception {
      return this.Vc();
   }
}
