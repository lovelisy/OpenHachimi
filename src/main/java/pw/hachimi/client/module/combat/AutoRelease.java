package pw.hachimi.client.module.combat;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoRelease extends ClientModule {
   Setting crossbow = this.registerSetting(new BooleanSetting("Crossbow", "Automatically releases crossbow when fully charged", false));
   Setting ticks = this.registerSetting(new NumberSetting("Ticks", "Ticks before releasing the bow charge", 3, 5, 20));
   Setting tPSSync = this.registerSetting(new BooleanSetting("TPS-Sync", "Sync bow release to server ticks", false));
   public static boolean af;

   public AutoRelease() {
      super("AutoRelease", "Automatically releases a charged bow", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (!SelfBow.aiZ().awg()) {
         if (tickPreEvent.getEventPhase() == EventPhase.POST) {
            ItemStack stack = client.player.getMainHandStack();
            if (stack.getItem() == Items.BOW) {
               float f;
               if ((Boolean)this.tPSSync.atx()) {
                  f = 20.0F - Managers.entityManager.rW();
               } else {
                  f = 0.0F;
               }

               if (client.player.getItemUseTime() + f >= ((Integer)this.ticks.atx()).intValue()) {
                  Managers.serverManager.iO('\uf560', new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
                  client.player.stopUsingItem();
               }
            } else if ((Boolean)this.crossbow.atx() && stack.getItem() == Items.CROSSBOW) {
               float temp = client.player.getItemUseTime();
               ItemStack temp2 = client.player.getMainHandStack();
               ClientPlayerEntity player2 = client.player;
               if (temp / CrossbowItem.getPullTime(temp2, (LivingEntity)player2) > 1.0F) {
                  Managers.serverManager.iO('\uf560', new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
                  client.player.stopUsingItem();
                  Hand hand = Hand.MAIN_HAND;
                  ClientPlayerEntity player3 = client.player;
                  client.interactionManager.interactItem((PlayerEntity)player3, hand);
               }
            }
         }
      }
   }
}
