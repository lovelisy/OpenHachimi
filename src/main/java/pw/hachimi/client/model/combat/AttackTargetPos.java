package pw.hachimi.client.model.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;

public record AttackTargetPos(Entity attackTarget, BlockPos pos, BlockPosBlockItemHelper anchorPlacement, boolean antiSurround, double damage, double selfDamage) {
   public static boolean l;

   public BlockPos getPos() {
      return this.pos;
   }
}
