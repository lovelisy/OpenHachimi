package pw.hachimi.client.module.player;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.model.player.UnidentifiedInterface5;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoFish extends ClientModule {
   Setting openInventory = this.registerSetting(new BooleanSetting("OpenInventory", "Allows you to fish while in the inventory", true));
   Setting castingDelay = this.registerSetting(new NumberSetting("CastingDelay", "The delay between fishing rod casts", 10, 15, 25));
   Setting maxSoundDist = this.registerSetting(new NumberSetting("MaxSoundDist", "The maximum distance from the splash sound", 0.0F, 2.0F, 5.0F));
   private boolean d;
   private int e;
   private int f;
   public static boolean dv;

   public AutoFish() {
      super("AutoFish", "Automatically casts and reels fishing rods", ModuleCategory.PLAYER);
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof PlaySoundS2CPacket) {
            PlaySoundS2CPacket playSoundS2CPacket = (PlaySoundS2CPacket)packet2;
            if (((PlaySoundS2CPacket)packet2).getSound().value() == SoundEvents.ENTITY_FISHING_BOBBER_SPLASH
               && client.player.getMainHandStack().getItem() == Items.FISHING_ROD) {
               FishingBobberEntity fishingBobberEntity = client.player.fishHook;
               if (fishingBobberEntity == null || fishingBobberEntity.getPlayerOwner() != client.player) {
                  return;
               }

               double temp = playSoundS2CPacket.getX();
               double d2 = playSoundS2CPacket.getZ();
               double d3 = playSoundS2CPacket.getY();
               double d4 = temp;
               if (fishingBobberEntity.squaredDistanceTo(d4, d3, d2) <= ((Float)this.maxSoundDist.atx()).floatValue()) {
                  this.d = true;
                  this.e = 4;
               }
            }
         }
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (client.currentScreen == null || client.currentScreen instanceof ChatScreen || (Boolean)this.openInventory.atx()) {
            if (client.player.getMainHandStack().getItem() != Items.FISHING_ROD) {
               return;
            }

            FishingBobberEntity fishingBobberEntity = client.player.fishHook;
            if ((fishingBobberEntity == null || fishingBobberEntity.getHookedEntity() != null) && this.f <= 0) {
               ((UnidentifiedInterface5)client).Bv();
               this.f = (Integer)this.castingDelay.atx();
               return;
            }

            if (this.d) {
               if (this.e <= 0) {
                  ((UnidentifiedInterface5)client).Bv();
                  this.d = false;
                  return;
               }

               this.e--;
            }
         }

         this.f--;
      }
   }
}
