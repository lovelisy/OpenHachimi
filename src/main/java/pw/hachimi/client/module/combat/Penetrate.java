package pw.hachimi.client.module.combat;

import java.util.Iterator;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.world.BlockPosEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.PenetrateGlassLeafMode;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Penetrate extends Multitask {
   Setting glassLeafMode = this.registerSetting(new EnumSetting("Block", "Block to use", PenetrateGlassLeafMode.Glass, PenetrateGlassLeafMode.values()));
   Setting numberSetting1 = this.registerSetting(new NumberSetting("Delay", "Delay, in ticks, between placements", 0, 0, 20));
   Setting wait = this.registerSetting(new NumberSetting("Wait", "Wait for item entity push", 0, 4, 20));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the obsidian being placed", true));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "The distance of your block reach", 0.0, 4.5, 6.0));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting groundOnly = this.registerSetting(new BooleanSetting("GroundOnly", "Works only when you are standing on blocks", false));
   Setting pauseOnFlying = this.registerSetting(new BooleanSetting("PauseOnFlying", "Pauses the current process while you are flying", false));
   Setting pauseInFluid = this.registerSetting(new BooleanSetting("PauseInFluid", "Pauses the current process while you are swimming", false));
   private int k;
   private int l;

   public Penetrate() {
      super("Penetrate", "2024 Magic", ModuleCategory.COMBAT);
   }

   @EventListener
   public void avz(TickEntitiesPreEvent tickEntitiesPreEvent) {
      this.k--;
      this.l--;
   }

   @EventListener
   public void avA(BlockPosEvent blockPosEvent) {
      this.l--;
      if ((Boolean)this.groundOnly.atx() && !client.player.isOnGround()) {
         ClientWorld temp = client.world;
         BlockPos pos = client.player.getBlockPos();
         if (!(temp.getBlockState(pos).getBlock() instanceof LadderBlock)) {
            return;
         }
      }

      if ((!(Boolean)this.pauseOnFlying.atx() || !client.player.isFallFlying())
         && (!(Boolean)this.pauseInFluid.atx() || !client.player.isInFluid())
         && ((Boolean)this.breakSetting.atx() || !client.player.isUsingItem())) {
         Iterator iterator2 = client.world.getPlayers().iterator();

         while (iterator2.hasNext()) {
            Object obj = iterator2.next();
            if (((PlayerEntity)obj).isAlive() && client.player != obj && !Managers.friendManager.awt(((PlayerEntity)obj).getName())) {
               Iterator iterator3 = WorldUtils.MZ(((PlayerEntity)obj).getPos()).iterator();

               while (iterator3.hasNext()) {
                  Object obj2 = iterator3.next();

                  for (Direction direction : Direction.values()) {
                     BlockPos pos2 = ((BlockPos)obj2).offset(direction);
                     BlockPos pos3 = blockPosEvent.getPos();
                     if (pos2.equals(pos3)) {
                        if (this.glassLeafMode.atx() != PenetrateGlassLeafMode.Anchor) {
                           ClientWorld world2 = client.world;
                           BlockPos pos4 = pos2.down();
                           BlockState state = world2.getBlockState(pos4);
                           Block block2 = Blocks.BEDROCK;
                           if (!state.isOf(block2)) {
                              world2 = client.world;
                              BlockPos pos5 = pos2.down();
                              BlockState state2 = world2.getBlockState(pos5);
                              Block block3 = Blocks.OBSIDIAN;
                              if (!state2.isOf(block3)) {
                                 continue;
                              }
                           }
                        }

                        if (blockPosEvent.getEventPhase() != EventPhase.PRE) {
                           final Penetrate penetrate = this;
                           BlockPosBlockItemHelper blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos2, new Predicate() {
                              @Override
                              public boolean test(Object blockPosEvent) {
                                 return penetrate.avD((ItemStack)blockPosEvent);
                              }
                           }, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx());
                           if (!this.avB(new Box(pos2.getX(), pos2.getY(), pos2.getZ(), pos2.getX() + 1, pos2.getY() + 2, pos2.getZ() + 1))) {
                              StackSlotHelper stackSlotHelper = new StackSlotHelper(Items.END_CRYSTAL, (SwapMode)this.swapAction.atx()).LK();
                              if (stackSlotHelper.LL()) {
                                 int n = Managers.inventoryTracker.JS();
                                 stackSlotHelper.LO(n);
                                 PlacementUtils.Fe(pos2.down(), (Boolean)this.rotate.atx());
                                 stackSlotHelper.LP(n);
                              }

                              return;
                           }

                           boolean flag = blockPosBlockItemHelper.avL((Double)this.placeRange.atx(), false);
                           if (this.k <= 0 && flag) {
                              blockPosBlockItemHelper.avO((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), false, 0.0);
                              this.k = (Integer)this.numberSetting1.atx();
                              this.l = (Integer)this.wait.atx() + 1;
                              return;
                           }
                        } else if (this.glassLeafMode.atx() != PenetrateGlassLeafMode.Anchor) {
                           if ((
                                 client.world.getBlockState(pos2).getBlock() instanceof TransparentBlock
                                    || client.world.getBlockState(pos2).getBlock() instanceof LeavesBlock
                              )
                              && (
                                 this.avB(new Box(pos2.getX(), pos2.getY(), pos2.getZ(), pos2.getX() + 1, pos2.getY() + 2, pos2.getZ() + 1))
                                    || this.l > 0
                              )) {
                              blockPosEvent.aim();
                              return;
                           }
                        } else if (client.world.getBlockState(pos2).getBlock() == Blocks.RESPAWN_ANCHOR) {
                           blockPosEvent.aim();
                           return;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   boolean avB(Box box) {
      ClientWorld temp = client.world;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object box) {
            return Penetrate.avC((Entity)box);
         }
      };
      boolean flag;
      if (!temp.getOtherEntities((Entity)null, box, predicate).isEmpty()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static boolean avC(Entity entity2) {
      return entity2 instanceof ItemEntity;
   }

   boolean avD(ItemStack stack) {
      if (stack.getItem() == Items.RESPAWN_ANCHOR && this.glassLeafMode.atx() == PenetrateGlassLeafMode.Anchor) {
         return true;
      }

      Item item2 = stack.getItem();
      if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof LeavesBlock && this.glassLeafMode.atx() == PenetrateGlassLeafMode.Leaf) {
         return true;
      }

      item2 = stack.getItem();
      return (Item)item2 instanceof BlockItem
            && ((BlockItem)item2).getBlock() instanceof TransparentBlock
            && this.glassLeafMode.atx() == PenetrateGlassLeafMode.Glass
         ? true
         : (stack.getItem() == Items.BLUE_ICE || stack.getItem() == Items.PACKED_ICE) && this.glassLeafMode.atx() == PenetrateGlassLeafMode.ICE;
   }
}
