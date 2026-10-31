package pw.hachimi.client.module.combat;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.AntiCrawlCrawlingMiningMode;
import pw.hachimi.client.model.combat.AntiCrawlWhitelistBlacklistMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class AntiCrawl extends ClientModule {
   Setting selection = this.registerSetting(new EnumSetting("Selection", "How to use the blocks setting", AntiCrawlWhitelistBlacklistMode.All, AntiCrawlWhitelistBlacklistMode.values()));
   Setting whitelist = this.registerSetting(new BlockListSetting("Whitelist", "Valid block whitelist", Blocks.OBSIDIAN, Blocks.ENDER_CHEST, Blocks.CRYING_OBSIDIAN, Blocks.NETHERITE_BLOCK));
   Setting blacklist = this.registerSetting(new BlockListSetting("Blacklist", "Valid block blacklist", Blocks.FIRE));
   Setting whileSetting = this.registerSetting(new EnumSetting("While", "Mining in some case", AntiCrawlCrawlingMiningMode.Crawling, AntiCrawlCrawlingMiningMode.values()));
   Setting priority = this.registerSetting(new NumberSetting("Priority", "Priority of anti crawl mining", 0, 10, 10));
   public static boolean el;

   public AntiCrawl() {
      super("AntiCrawl", "Auto mine target blocks", ModuleCategory.COMBAT);
   }

   @EventListener
   public void ahH(TickEntitiesPreEvent tickEntitiesPreEvent) {
      BlockPosBoxMultitask blockPosBoxMultitask = BlockPosBoxMultitask.alv();
      Iterator iterator2 = WorldUtils.MY().iterator();

      while (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         if (BlockUtils.TP(pos) && CollisionUtils.aiy(client.player, new Box(pos))) {
            return;
         }

         if (this.whileSetting.atx() == AntiCrawlCrawlingMiningMode.Always
            || client.player.isCrawling()
            || this.whileSetting.atx() == AntiCrawlCrawlingMiningMode.Smart) {
            Iterator iterator3 = WorldUtils.Na().iterator();

            while (iterator3.hasNext()) {
               BlockPos pos2 = (BlockPos)iterator3.next();
               if (this.ahI(pos2)) {
                  ClientWorld temp = client.world;
                  BlockPos pos3 = pos2.down();
                  if (temp.isAir(pos3) && blockPosBoxMultitask.alH(pos2, (Integer)this.priority.atx())) {
                     return;
                  }
               }
            }
         }

         if (this.whileSetting.atx() == AntiCrawlCrawlingMiningMode.Always || client.player.isCrawling()) {
            Iterator iterator4 = WorldUtils.Na().iterator();

            while (iterator4.hasNext()) {
               BlockPos pos4 = (BlockPos)iterator4.next();
               if (this.ahI(pos4) && blockPosBoxMultitask.alH(pos4, (Integer)this.priority.atx())) {
                  return;
               }
            }
         } else if (this.whileSetting.atx() == AntiCrawlCrawlingMiningMode.Mining) {
            Iterator iterator5 = WorldUtils.Na().iterator();

            while (iterator5.hasNext()) {
               BlockPos pos5 = (BlockPos)iterator5.next();
               if (this.ahI(pos5) && Managers.breakManager.Ru(pos5.down()) && blockPosBoxMultitask.alH(pos5, (Integer)this.priority.atx())) {
                  return;
               }
            }
         }
      }
   }

   public boolean ahI(BlockPos pos) {
      if (!CollisionUtils.aiy(client.player, new Box(pos))) {
         return false;
      }

      Block block2 = client.world.getBlockState(pos).getBlock();
      if (block2 instanceof AirBlock) {
         return false;
      }

      if (!block2.getDefaultState().getFluidState().isEmpty()) {
         return false;
      }

      if (this.selection.atx() == AntiCrawlWhitelistBlacklistMode.Blacklist) {
         boolean flag;
         if (!((List)this.blacklist.atx()).contains(block2)) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         return this.selection.atx() == AntiCrawlWhitelistBlacklistMode.Whitelist ? ((List)this.whitelist.atx()).contains(block2) : true;
      }
   }
}
