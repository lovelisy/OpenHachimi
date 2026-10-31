package pw.hachimi.client.world;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.entity.BlockEntity;
import pw.hachimi.client.mixin.ChunkMixin;

public class BlockEntityManager implements Iterator {
   private final Iterator iterator = new ChunkWorldManager();
   private Iterator iterator2;

   public BlockEntityManager() {
      this.ZD();
   }

   private void ZD() {
      while (this.iterator.hasNext()) {
         Map map = ((ChunkMixin)this.iterator.next()).getBlockEntities();
         if (!map.isEmpty()) {
            this.iterator2 = map.values().iterator();
            break;
         }
      }
   }

   @Override
   public boolean hasNext() {
      if (this.iterator2 == null) {
         return false;
      }

      if (this.iterator2.hasNext()) {
         return true;
      }

      this.ZD();
      return this.iterator2.hasNext();
   }

   public BlockEntity ZE() {
      return (BlockEntity)this.iterator2.next();
   }

   @Override
   public Object next() {
      return this.ZE();
   }
}
