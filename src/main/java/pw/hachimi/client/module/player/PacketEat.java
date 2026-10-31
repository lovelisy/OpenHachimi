package pw.hachimi.client.module.player;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper22;
import pw.hachimi.eventbus.annotation.EventListener;

public class PacketEat extends ClientModule {
   private static PacketEat INSTANCE;
   Setting eatDesync = this.registerSetting(new BooleanSetting("EatDesync", "Spam send use packet", true));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "The delay of packet", 0, 3, 10));
   Setting silentEat = this.registerSetting(new BooleanSetting("SilentEat", "Cancel the RELEASE_USE_ITEM Packet", false));
   UnidentifiedHelper22 unidentifiedHelper22 = new UnidentifiedHelper22();
   public static boolean cs;

   public PacketEat() {
      super("PacketEat", "Send eat packet", ModuleCategory.PLAYER);
      INSTANCE = this;
   }

   public static PacketEat PN() {
      return INSTANCE;
   }

   @EventListener
   public void PO(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if ((Boolean)this.eatDesync.atx() && this.unidentifiedHelper22.afq((Number)this.delay.atx())) {
         if (client.player.isUsingItem()) {
            ComponentMap temp = client.player.getActiveItem().getItem().getComponents();
            ComponentType componentType = DataComponentTypes.FOOD;
            if (temp.contains(componentType)) {
               Managers.serverManager.iQ(PacketEat::PT);
               this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
               return;
            }
         }

         if (client.options.useKey.isPressed()) {
            ComponentMap componentMap = Managers.inventoryTracker.JW().getItem().getComponents();
            ComponentType componentType2 = DataComponentTypes.FOOD;
            if (componentMap.contains(componentType2)) {
               Managers.serverManager.iQ(PacketEat::PS);
               this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
            } else {
               componentMap = client.player.getOffHandStack().getItem().getComponents();
               ComponentType componentType3 = DataComponentTypes.FOOD;
               if (componentMap.contains(componentType3)) {
                  Managers.serverManager.iQ(PacketEat::PR);
                  this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
               }
            }
         }
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if ((Boolean)this.silentEat.atx()) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof PlayerActionC2SPacket && ((PlayerActionC2SPacket)packet2).getAction() == Action.RELEASE_USE_ITEM) {
            ComponentMap temp = client.player.getActiveItem().getItem().getComponents();
            ComponentType componentType = DataComponentTypes.FOOD;
            if (temp.contains(componentType)) {
               sendImmediatelyEvent.aim();
            }
         }
      }
   }

   public boolean PQ() {
      return (Boolean)this.eatDesync.atx();
   }

   private static Packet PR(int n) {
      return new PlayerInteractItemC2SPacket(Hand.OFF_HAND, n, client.player.getYaw(), client.player.getPitch());
   }

   private static Packet PS(int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, client.player.getYaw(), client.player.getPitch());
   }

   private static Packet PT(int n) {
      return new PlayerInteractItemC2SPacket(client.player.getActiveHand(), n, client.player.getYaw(), client.player.getPitch());
   }
}
