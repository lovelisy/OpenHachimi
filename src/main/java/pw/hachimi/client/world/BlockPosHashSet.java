package pw.hachimi.client.world;

import java.util.HashSet;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.module.combat.AutoCrystal;

public class BlockPosHashSet extends HashSet {
   final BlockPos pos;
   final AutoCrystal autoCrystal;

   public BlockPosHashSet(AutoCrystal autoCrystal2, BlockPos pos2) {
      this.pos = pos2;
      this.autoCrystal = autoCrystal2;
      BlockPos pos3 = this.pos.up();
      this.add(pos3);
   }
}
