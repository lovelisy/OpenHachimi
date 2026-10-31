package pw.hachimi.client.event.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class GetCollisionShapeEvent extends Event {
   private final BlockPos pos;
   private final BlockState state;
   private VoxelShape voxelShape;

   public GetCollisionShapeEvent(VoxelShape voxelShape2, BlockPos pos2, BlockState state2) {
      this.pos = pos2;
      this.state = state2;
      this.voxelShape = voxelShape2;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public BlockState getState() {
      return this.state;
   }

   public Block asa() {
      return this.state.getBlock();
   }

   public VoxelShape getVoxelShape() {
      return this.voxelShape;
   }

   public void setVoxelShape(VoxelShape voxelShape2) {
      this.voxelShape = voxelShape2;
   }
}
