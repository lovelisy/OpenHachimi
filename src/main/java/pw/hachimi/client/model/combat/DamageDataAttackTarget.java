package pw.hachimi.client.model.combat;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;

public record DamageDataAttackTarget(
   Object damageData, Entity attackTarget, double damage, double selfDamage, BlockPos blockPos, boolean antiSurround, BlockPosBlockItemHelper support
) {
   public static boolean v;
}
