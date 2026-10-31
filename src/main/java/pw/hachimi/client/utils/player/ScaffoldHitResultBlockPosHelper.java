package pw.hachimi.client.utils.player;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ScaffoldHitResultBlockPosHelper {
   private BlockHitResult blockHitResult;
   private float[] b;
   public static boolean a;

   public ScaffoldHitResultBlockPosHelper(BlockHitResult blockHitResult2) {
      this.blockHitResult = blockHitResult2;
      this.b = null;
   }

   public static ScaffoldHitResultBlockPosHelper alm(BlockPos pos, Direction direction) {
      return new ScaffoldHitResultBlockPosHelper(a(pos.toCenterPos(), direction, pos));
   }

   public BlockHitResult getBlockHitResult() {
      return this.blockHitResult;
   }

   public void setBlockHitResult(BlockHitResult blockHitResult2) {
      this.blockHitResult = blockHitResult2;
   }

   public BlockPos alp() {
      Direction direction = this.blockHitResult.getSide();
      return this.blockHitResult.getBlockPos().offset(direction);
   }

   public float[] alq() {
      return this.b;
   }

   public void alr(float[] fs) {
      this.b = fs;
   }

   private static BlockHitResult a(Vec3d vec, Direction direction, BlockPos pos) {
      return new BlockHitResult(vec, direction, pos, false);
   }
}
