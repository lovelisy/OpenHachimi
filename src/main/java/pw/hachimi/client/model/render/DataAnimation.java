package pw.hachimi.client.model.render;

import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.world.BlockPosDirectionHelper;

public record DataAnimation(BlockPosDirectionHelper data, UnidentifiedHelper7 animation) {
   public static boolean q;

   public BlockPosDirectionHelper getData() {
      return this.data;
   }
}
