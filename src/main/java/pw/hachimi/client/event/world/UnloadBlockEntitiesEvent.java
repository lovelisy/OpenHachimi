package pw.hachimi.client.event.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import pw.hachimi.client.event.Event;

public class UnloadBlockEntitiesEvent extends Event {
   private final WorldChunk worldChunk;

   public UnloadBlockEntitiesEvent(WorldChunk worldChunk2) {
      this.worldChunk = worldChunk2;
   }

   public boolean yr(BlockPos pos) {
      int n = pos.getX() >> 4;
      int n2 = pos.getZ() >> 4;
      boolean flag;
      if (n == this.worldChunk.getPos().x && n2 == this.worldChunk.getPos().z) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
