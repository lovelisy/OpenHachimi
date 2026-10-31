package pw.hachimi.client.utils.world;

import java.util.Set;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.CartographyTableBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.block.LoomBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pw.hachimi.client.Mc;
import pw.hachimi.client.handler.BlockHandler;

public class BlockStateUtils {
   private static final Set a_ = Set.of(
      new Block[]{
         Blocks.CHEST,
         Blocks.ENDER_CHEST,
         Blocks.TRAPPED_CHEST,
         Blocks.CRAFTING_TABLE,
         Blocks.FURNACE,
         Blocks.BLAST_FURNACE,
         Blocks.FLETCHING_TABLE,
         Blocks.CARTOGRAPHY_TABLE,
         Blocks.ENCHANTING_TABLE,
         Blocks.SMITHING_TABLE,
         Blocks.STONECUTTER,
         Blocks.JUKEBOX,
         Blocks.NOTE_BLOCK,
         Blocks.SHULKER_BOX,
         Blocks.BLACK_SHULKER_BOX,
         Blocks.BLUE_SHULKER_BOX,
         Blocks.LIGHT_BLUE_SHULKER_BOX,
         Blocks.GREEN_SHULKER_BOX,
         Blocks.CYAN_SHULKER_BOX,
         Blocks.BROWN_SHULKER_BOX,
         Blocks.GRAY_SHULKER_BOX,
         Blocks.LIGHT_GRAY_SHULKER_BOX,
         Blocks.LIME_SHULKER_BOX,
         Blocks.MAGENTA_SHULKER_BOX,
         Blocks.ORANGE_SHULKER_BOX,
         Blocks.PINK_SHULKER_BOX,
         Blocks.PURPLE_SHULKER_BOX,
         Blocks.RED_SHULKER_BOX,
         Blocks.WHITE_SHULKER_BOX,
         Blocks.YELLOW_SHULKER_BOX,
         Blocks.ACACIA_TRAPDOOR,
         Blocks.BAMBOO_TRAPDOOR,
         Blocks.BIRCH_TRAPDOOR,
         Blocks.CHERRY_TRAPDOOR,
         Blocks.COPPER_TRAPDOOR,
         Blocks.EXPOSED_COPPER_TRAPDOOR,
         Blocks.OXIDIZED_COPPER_TRAPDOOR,
         Blocks.WAXED_COPPER_TRAPDOOR,
         Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR,
         Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR,
         Blocks.WEATHERED_COPPER_TRAPDOOR,
         Blocks.SPRUCE_TRAPDOOR,
         Blocks.WARPED_TRAPDOOR,
         Blocks.IRON_TRAPDOOR,
         Blocks.DARK_OAK_TRAPDOOR,
         Blocks.JUNGLE_TRAPDOOR,
         Blocks.MANGROVE_TRAPDOOR,
         Blocks.OAK_TRAPDOOR,
         Blocks.CRIMSON_TRAPDOOR
      }
   );
   private static final long a = 0L;

   public static boolean Ww(BlockState state) {
      return Wx(state.getBlock());
   }

   public static boolean Wx(Block block2) {
      return a_.contains(block2);
   }

   public static boolean Wy(BlockState state, BlockPos pos) {
      Block block2 = state.getBlock();
      boolean flag;
      if (!(block2 instanceof CraftingTableBlock)
         && !(block2 instanceof AnvilBlock)
         && !(block2 instanceof LoomBlock)
         && !(block2 instanceof CartographyTableBlock)
         && !(block2 instanceof GrindstoneBlock)
         && !(block2 instanceof StonecutterBlock)
         && !(block2 instanceof ButtonBlock)
         && !(block2 instanceof AbstractPressurePlateBlock)
         && !(block2 instanceof BlockWithEntity)
         && !(block2 instanceof BedBlock)
         && !(block2 instanceof FenceGateBlock)
         && !(block2 instanceof DoorBlock)
         && !(block2 instanceof NoteBlock)
         && !(block2 instanceof TrapdoorBlock)
         && (!(state.getBlock() instanceof RedstoneWireBlock) || !Wz(state, Mc.client.world, pos, Mc.client.player))) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean Wz(BlockState state, World world2, BlockPos pos, PlayerEntity player2) {
      return ((BlockHandler)Blocks.REDSTONE_WIRE).jo(state, world2, pos, player2);
   }
}
