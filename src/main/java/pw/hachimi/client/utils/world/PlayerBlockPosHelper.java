package pw.hachimi.client.utils.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.Mc;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.player.StackStateUtils;

public class PlayerBlockPosHelper {
   public final StopwatchImpl stopwatchImpl;
   private final int b;
   public BlockPos pos;
   public double d;
   public boolean e;
   public boolean f;
   public static boolean a;

   public PlayerBlockPosHelper(BlockPos pos2, int n, boolean flag) {
      this.pos = pos2;
      this.b = n;
      this.d = Math.max(StackStateUtils.ald(pos2, flag), 50.0);
      this.stopwatchImpl = new StopwatchImpl();
   }

   public PlayerEntity MT() {
      int n = this.b;
      Entity entity2 = Mc.client.world.getEntityById(n);
      return (Entity)entity2 instanceof PlayerEntity ? (PlayerEntity)entity2 : null;
   }

   public double MU() {
      return Math.min(this.stopwatchImpl.afs() / this.d, 1.0);
   }
}
