package pw.hachimi.client.module.misc;

import java.util.Set;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoSoundLag extends ClientModule {
   private static final Set a;

   public NoSoundLag() {
      super("NoSoundLag", "Prevents sound effects from lagging the game", ModuleCategory.MISC);
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      label20: {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof PlaySoundFromEntityS2CPacket) {
            Set temp = a;
            Object obj = ((PlaySoundFromEntityS2CPacket)packet2).getSound().value();
            if (temp.contains(obj)) {
               break label20;
            }
         }

         packet2 = channelRead0Event.getPacket();
         if (!(packet2 instanceof PlaySoundS2CPacket)) {
            return;
         }

         Set set = a;
         Object obj2 = ((PlaySoundS2CPacket)packet2).getSound().value();
         if (!set.contains(obj2)) {
            return;
         }
      }

      channelRead0Event.aim();
   }

   static {
      Object var25 = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC.value();
      Object var27 = SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA.value();
      Object var29 = SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE.value();
      Object var31 = SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND.value();
      Object var33 = SoundEvents.ITEM_ARMOR_EQUIP_IRON.value();
      Object var35 = SoundEvents.ITEM_ARMOR_EQUIP_GOLD.value();
      Object var37 = SoundEvents.ITEM_ARMOR_EQUIP_CHAIN.value();
      Object var39 = SoundEvents.ITEM_ARMOR_EQUIP_LEATHER.value();
      a = Set.of(
         (SoundEvent)var25, (SoundEvent)var27, (SoundEvent)var29, (SoundEvent)var31, (SoundEvent)var33, (SoundEvent)var35, (SoundEvent)var37, (SoundEvent)var39
      );
   }
}
