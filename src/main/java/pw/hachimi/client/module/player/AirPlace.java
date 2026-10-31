package pw.hachimi.client.module.player;

import java.util.Iterator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.player.DoItemUseEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.mixin.MinecraftClientMixin2;
import pw.hachimi.client.model.DefaultDegreesMode;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;

public final class AirPlace extends ClientModule {
   private static AirPlace INSTANCE;
   Setting click = this.registerSetting(new BooleanSetting("Click", "Allow manual air place", true));
   Setting range = this.registerSetting(new NumberSetting("Range", "The range to air place", 1.0F, 4.0F, 6.0F, DefaultDegreesMode.DEFAULT));
   Setting fluids = this.registerSetting(new BooleanSetting("Fluids", "Place against fluids", false));
   private int d;

   public AirPlace() {
      super("AirPlace", "Allows you to place blocks in the air", ModuleCategory.PLAYER);
      INSTANCE = this;
   }

   public static AirPlace acT() {
      return INSTANCE;
   }

   @Override
   public void awd() {
      this.d = 0;
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (this.d > 0) {
         this.d--;
      }

      if (client.player != null && client.interactionManager != null && (Boolean)this.click.atx() && tickPreEvent.getEventPhase() == EventPhase.PRE) {
         HitResult hitResult = client.crosshairTarget;
         if (hitResult instanceof BlockHitResult) {
            ClientWorld temp = client.world;
            BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
            if (!temp.isAir(pos)) {
               return;
            }
         }

         ItemStack stack = Managers.inventoryTracker.JW();
         if (!stack.isEmpty() && stack.getItem() instanceof BlockItem && (client.options.useKey.isPressed() || client.options.useKey.wasPressed())) {
            ClientPlayerEntity player2 = client.player;
            double temp2 = ((Float)this.range.atx()).floatValue();
            boolean flag = (Boolean)this.fluids.atx();
            double d2 = temp2;
            hitResult = player2.raycast(d2, 1.0F, flag);
            if (this.d == 0 && !client.player.isUsingItem() && hitResult instanceof BlockHitResult) {
               BlockPos pos2 = BlockPos.ofFloored((Position)((BlockHitResult)hitResult).getPos());
               if (!client.world.isAir(pos2) || this.acX(pos2)) {
                  return;
               }

               ((MinecraftClientMixin2)client).hookSetItemUseCooldown(4);
               this.d = 5;
               if (HitResultWorldHelper.Sq().SG()) {
                  Managers.serverManager.iO('\uf560', new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
                  Hand hand = Hand.OFF_HAND;
                  ClientPlayerEntity player3 = client.player;
                  client.interactionManager.interactBlock(player3, hand, (BlockHitResult)hitResult);
                  Hand hand2 = Hand.OFF_HAND;
                  client.player.swingHand(hand2, false);
                  Managers.serverManager.iO('\uf560', new HandSwingC2SPacket(Hand.OFF_HAND));
                  Managers.serverManager.iO('\uf560', new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
               } else {
                  Hand hand3 = Hand.MAIN_HAND;
                  ClientPlayerEntity player4 = client.player;
                  client.interactionManager.interactBlock(player4, hand3, (BlockHitResult)hitResult);
                  Hand hand4 = Hand.MAIN_HAND;
                  client.player.swingHand(hand4);
               }
            }
         } else {
            this.d = 0;
         }
      }
   }

   @EventListener
   public void onDoItemUse(DoItemUseEvent doItemUseEvent) {
      if (this.d > 0 && (Boolean)this.click.atx()) {
         doItemUseEvent.aim();
      }
   }

   @EventListener
   public void acW(Render3DContext render3DContext) {
      if (client.player != null && (Boolean)this.click.atx()) {
         HitResult hitResult = client.crosshairTarget;
         if (hitResult instanceof BlockHitResult) {
            ClientWorld temp = client.world;
            BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
            if (!temp.isAir(pos)) {
               return;
            }
         }

         ItemStack stack = Managers.inventoryTracker.JW();
         if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
            ClientPlayerEntity player2 = client.player;
            double temp2 = ((Float)this.range.atx()).floatValue();
            boolean flag = (Boolean)this.fluids.atx();
            double d2 = temp2;
            hitResult = player2.raycast(d2, 1.0F, flag);
            if (hitResult instanceof BlockHitResult) {
               BlockPos pos2 = BlockPos.ofFloored((Position)((BlockHitResult)hitResult).getPos());
               if (client.world.isAir(pos2) && !this.acX(pos2)) {
                  render3DContext.boxRenderer.Zc(pos2, ColorHelper3.auO().auW(40), ColorHelper3.auO().auW(145), ShapeMode.Both);
               }
            }
         }
      }
   }

   private boolean acX(BlockPos pos) {
      Iterator iterator2 = client.world.getEntities().iterator();

      while (iterator2.hasNext()) {
         Box temp = ((Entity)iterator2.next()).getBoundingBox();
         Box box = new Box(pos);
         if (temp.intersects(box)) {
            return true;
         }
      }

      return false;
   }
}
