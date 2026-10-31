package pw.hachimi.client.utils.player;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.model.world.PosState;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.client.world.BlockPosDirectionBase;

public class DirectionPlayerHelper extends BlockPosDirectionBase {
   public static final DirectionPlayerHelper INSTANCE = new DirectionPlayerHelper();

   @Override
   public Direction Hs(BlockPos pos, PlayerEntity player2, long l, ClientWorld world2) {
      if (PlacementUtils.m) {
         Direction direction = null;
         double d = Double.MAX_VALUE;
         Direction[] directions = Direction.values();
         int n = directions.length;

         for (int n2 = 0; n2 < n; n2++) {
            Direction direction2 = directions[n2];
            Vec3d temp = player2.getEyePos();
            Vec3d vec = pos.toCenterPos().offset(direction2, 0.5);
            double d2 = temp.distanceTo(vec);
            if (d2 < d) {
               d = d2;
               direction = direction2;
            }
         }

         return direction;
      } else {
         return Direction.DOWN;
      }
   }

   @Override
   public boolean Ht(BlockPos pos, Direction direction, ClientWorld world2, PlayerEntity player2) {
      return true;
   }

   @Override
   public boolean Hu(BlockPos pos, Direction direction, ClientWorld world2, PlayerEntity player2, PosState posState) {
      return this.Ht(pos, direction, world2, player2);
   }
}
