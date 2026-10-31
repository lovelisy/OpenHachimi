package pw.hachimi.client.module.player;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import pw.hachimi.client.event.player.SwingHandEvent;
import pw.hachimi.client.model.player.SwingMainhandOffhandMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Swing extends ClientModule {
   private static Swing INSTANCE;
   Setting hand = this.registerSetting(new EnumSetting("Hand", "The player swinging hand", SwingMainhandOffhandMode.MAINHAND, SwingMainhandOffhandMode.values()));
   private Hand hand2;
   private Hand hand3;

   public Swing() {
      super("Swing", "Modifies the swinging hand", ModuleCategory.PLAYER);
      INSTANCE = this;
   }

   public static Swing bP() {
      return INSTANCE;
   }

   @Override
   public void awd() {
      if (client.player != null) {
         this.hand2 = client.player.preferredHand;
         if (this.hand.atx() != SwingMainhandOffhandMode.PACKET) {
            this.bR((SwingMainhandOffhandMode)this.hand.atx());
         }
      }
   }

   @Override
   public void awe() {
      if (client.player != null) {
         Hand hand4 = this.hand2;
         client.player.preferredHand = hand4;
      }
   }

   @EventListener
   public void onSwingHand(SwingHandEvent swingHandEvent) {
      this.bR((SwingMainhandOffhandMode)this.hand.atx());
   }

   private void bR(SwingMainhandOffhandMode swingMainhandOffhandMode) {
      this.hand3 = switch ((SwingMainhandOffhandMode)swingMainhandOffhandMode) {
         case MAINHAND -> Hand.MAIN_HAND;
         case OFFHAND -> Hand.OFF_HAND;
         case SWAP -> {
            if (client.player.handSwinging && client.player.handSwingTicks < this.bS() / 2 && client.player.handSwingTicks >= 0) {
               yield this.hand3;
            } else if (this.hand3 != Hand.MAIN_HAND) {
               yield Hand.MAIN_HAND;
            } else {
               yield Hand.OFF_HAND;
            }
         }
         case PACKET -> {
            client.player.handSwingTicks = 0;
            client.player.handSwinging = false;
            yield null;
         }
      };
      if (this.hand3 != null) {
         Hand hand4 = this.hand3;
         client.player.preferredHand = hand4;
      }
   }

   public int bS() {
      if (StatusEffectUtil.hasHaste((LivingEntity)client.player)) {
         return 6 - (1 + StatusEffectUtil.getHasteAmplifier((LivingEntity)client.player));
      }

      RegistryEntry registryEntry = StatusEffects.MINING_FATIGUE;
      int n;
      if (client.player.hasStatusEffect(registryEntry)) {
         RegistryEntry registryEntry2 = StatusEffects.MINING_FATIGUE;
         n = 6 + (1 + client.player.getStatusEffect(registryEntry2).getAmplifier()) * 2;
      } else {
         n = 6;
      }

      return n;
   }

   public Hand getHand3() {
      return this.hand3;
   }

   public Hand getHand2() {
      return this.hand2;
   }

   private static MatchException l() {
      return new MatchException(null, null);
   }
}
