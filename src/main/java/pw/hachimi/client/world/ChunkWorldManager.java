package pw.hachimi.client.world;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReferenceArray;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.world.chunk.Chunk;
import pw.hachimi.client.Mc;
import pw.hachimi.client.mixin.ClientChunkManagerClientChunkMapMixin;
import pw.hachimi.client.mixin.ClientChunkManagerMixin;

public class ChunkWorldManager implements Iterator {
   private final ClientChunkManagerClientChunkMapMixin clientChunkManagerClientChunkMapMixin = (ClientChunkManagerClientChunkMapMixin)(Object)((ClientChunkManagerMixin)Mc.client
         .world
         .getChunkManager())
      .getChunks();
   private final boolean b;
   private int c = 0;
   private Chunk chunk;

   public ChunkWorldManager() {
      this.b = false;
      this.avb();
   }

   private Chunk avb() {
      Chunk chunk2 = this.chunk;
      this.chunk = null;

      while (this.c < this.clientChunkManagerClientChunkMapMixin.getChunks().length()) {
         AtomicReferenceArray temp = this.clientChunkManagerClientChunkMapMixin.getChunks();
         int n = this.c++;
         this.chunk = (Chunk)temp.get(n);
         if (this.chunk != null && (!this.b || this.avc(this.chunk))) {
            break;
         }
      }

      return chunk2;
   }

   private boolean avc(Chunk chunk2) {
      int n = chunk2.getPos().x;
      int n2 = chunk2.getPos().z;
      ClientChunkManager temp = Mc.client.world.getChunkManager();
      int n3 = n + 1;
      if (temp.isChunkLoaded(n3, n2)) {
         temp = Mc.client.world.getChunkManager();
         int n4 = n - 1;
         if (temp.isChunkLoaded(n4, n2)) {
            temp = Mc.client.world.getChunkManager();
            int n5 = n2 + 1;
            if (temp.isChunkLoaded(n, n5)) {
               temp = Mc.client.world.getChunkManager();
               int n6 = n2 - 1;
               if (temp.isChunkLoaded(n, n6)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Override
   public boolean hasNext() {
      boolean flag;
      if (this.chunk != null) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public Chunk avd() {
      return this.avb();
   }

   @Override
   public Object next() {
      return this.avd();
   }
}
