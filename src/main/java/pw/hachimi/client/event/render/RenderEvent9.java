package pw.hachimi.client.event.render;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.event.Event;

public class RenderEvent9 extends Event {
   private final BlockEntity blockEntity;
   public static boolean el;

   public RenderEvent9(BlockEntity blockEntity2) {
      this.blockEntity = blockEntity2;
   }

   public BlockPos TZ() {
      return this.blockEntity.getPos();
   }

   public BlockEntity getBlockEntity() {
      return this.blockEntity;
   }

   public BlockState Ub() {
      return this.blockEntity.getCachedState();
   }
}
