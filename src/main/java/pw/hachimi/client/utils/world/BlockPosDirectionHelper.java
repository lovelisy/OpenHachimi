package pw.hachimi.client.utils.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.module.player.AutoTool;

public class BlockPosDirectionHelper {
   private final BlockPos pos;
   private final Direction direction;
   private int c;
   private float d;
   private float e;

   public BlockPosDirectionHelper(BlockPos pos2, Direction direction2) {
      this.pos = pos2;
      this.direction = direction2;
   }

   public static BlockPosDirectionHelper cz() {
      return new BlockPosDirectionHelper(BlockPos.ORIGIN, Direction.UP);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj instanceof BlockPosDirectionHelper) {
         BlockPos temp = ((BlockPosDirectionHelper)obj).getPos();
         BlockPos pos2 = this.pos;
         if (temp.equals(pos2)) {
            return true;
         }
      }

      return false;
   }

   public void cA() {
      this.c = 0;
   }

   public void cB() {
      this.c++;
   }

   public void cC(float f) {
      this.e = this.d;
      this.d += f;
   }

   public void cD(float f, float f2) {
      this.d = f;
      this.e = f2;
   }

   public BlockPosDirectionHelper cE() {
      BlockPosDirectionHelper blockPosDirectionHelper = new BlockPosDirectionHelper(this.pos, this.direction);
      blockPosDirectionHelper.cD(this.d, this.e);
      return blockPosDirectionHelper;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public int cH() {
      return this.c;
   }

   public float cI() {
      return this.d;
   }

   public float cJ() {
      return this.e;
   }

   public BlockState cK() {
      BlockPos pos2 = this.pos;
      return ConfigContainer.client.world.getBlockState(pos2);
   }

   public int cL() {
      return AutoTool.Va(this.cK());
   }
}
