package pw.hachimi.client.utils.world;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import pw.hachimi.client.Mc;

public class BlockUtils {
   private static final Set a_ = new ReferenceOpenHashSet(
      Set.of(
         Blocks.OBSIDIAN,
         Blocks.CRYING_OBSIDIAN,
         Blocks.ANCIENT_DEBRIS,
         Blocks.NETHERITE_BLOCK,
         Blocks.RESPAWN_ANCHOR,
         Blocks.ANVIL,
         Blocks.ENCHANTING_TABLE,
         Blocks.ENDER_CHEST,
         Blocks.BEACON
      )
   );
   private static final Set b = new ReferenceOpenHashSet(Set.of(Blocks.BEDROCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.END_PORTAL_FRAME, Blocks.BARRIER));
   private static final long a = 0L;

   public static boolean TM(BlockPos pos) {
      return Mc.client.world == null ? false : TN(Mc.client.world.getBlockState(pos).getBlock());
   }

   public static boolean TN(Block block2) {
      boolean flag;
      if (!b.contains(block2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static boolean TO(Block block2) {
      boolean flag;
      if (block2 instanceof AirBlock || block2.getHardness() != -1.0F && block2.getHardness() != 100.0F) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean TP(BlockPos pos) {
      return TO(Mc.client.world.getBlockState(pos).getBlock());
   }

   public static boolean TQ(BlockState state) {
      return TO(state.getBlock());
   }

   public static boolean TR(BlockPos pos) {
      return Mc.client.world == null ? false : TT(Mc.client.world.getBlockState(pos).getBlock());
   }

   public static boolean TS(BlockState state) {
      return TT(state.getBlock());
   }

   public static boolean TT(Block block2) {
      return a_.contains(block2);
   }
}
