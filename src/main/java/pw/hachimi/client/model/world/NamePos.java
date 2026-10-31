package pw.hachimi.client.model.world;

import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.utils.StopwatchImpl;

public record NamePos(String name, BlockPos pos, Color color, StopwatchImpl timer) {
   public String getName() {
      return this.name;
   }

   public BlockPos getPos() {
      return this.pos;
   }
}
