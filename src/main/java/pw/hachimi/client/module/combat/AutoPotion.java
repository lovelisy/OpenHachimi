package pw.hachimi.client.module.combat;

import java.util.Iterator;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.movement.Velocity;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoPotion extends Module5 {
   public static AutoPotion INSTANCE;
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Interval between throwing potions (seconds)", 0.0F, 2.0F, 10.0F));
   Setting swapAlternative = this.registerSetting(new BooleanSetting("SwapAlternative", "Uses inventory swap for swapping to potions", true));
   Setting always = this.registerSetting(new BooleanSetting("Always", "Always inventory swap", false, this.swapAlternative::atx));
   Setting swingMode = this.registerSetting(new EnumSetting("Swing", "Swing side for throw", SwingMode.Normal, SwingMode.values()));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Speed", "Throw out the Speed potion", false));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("Resistance", "Throw out the Resistance potion", false));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("Strength", "Throw out the Strength potion", false));
   Setting booleanSetting4 = this.registerSetting(new BooleanSetting("MultiTask", "Allows throwing while using items", true));
   Setting booleanSetting5 = this.registerSetting(new BooleanSetting("GroundOnly", "Only throw potions while on ground", false));
   StopwatchImpl stopwatchImpl = new StopwatchImpl();

   public AutoPotion() {
      super("AutoPotion", "Automatically throws pot silently.", ModuleCategory.COMBAT, 900);
      INSTANCE = this;
      Setting setting2 = this.registerSetting(new KeybindSetting("ResistanceKey", "Press to throw out the Resistance potion", g(this.Kn() + "-resistance", this::AU)));
      Setting setting3 = this.registerSetting(new KeybindSetting("StrengthKey", "Press to throw out the Strength potion", g(this.Kn() + "-strength", this::AT)));
      Setting setting4 = this.registerSetting(new KeybindSetting("SpeedKey", "Press to throw out the Speed potion", g(this.Kn() + "-speed", this::AS)));
      Managers.playerScreenManager.akq((UnidentifiedHelper17)setting4.atx(), (UnidentifiedHelper17)setting3.atx(), (UnidentifiedHelper17)setting2.atx());
   }

   public int AO(RegistryEntry registryEntry) {
      int n = 0;
      if ((Boolean)this.swapAlternative.atx()) {
      }

      int n2;
      do {
         PlayerInventory temp = client.player.getInventory();
         int n3 = n;
         ItemStack stack = temp.getStack(n3);
         if (stack.getItem() == Items.SPLASH_POTION) {
            ItemStack stack2 = stack;
            PotionContentsComponent potionContentsComponent = PotionContentsComponent.DEFAULT;
            ComponentType componentType = DataComponentTypes.POTION_CONTENTS;
            Iterator iterator2 = ((PotionContentsComponent)stack2.getOrDefault(componentType, potionContentsComponent)).getEffects().iterator();

            while (iterator2.hasNext()) {
               if (((StatusEffectInstance)iterator2.next()).getEffectType() == registryEntry) {
                  if (n < 9) {
                     n2 = n + 36;
                  } else {
                     n2 = n;
                  }

                  return n2;
               }
            }
         }

         n++;
         if ((Boolean)this.swapAlternative.atx()) {
            n2 = 45;
         } else {
            n2 = 9;
         }
      } while (n < n2);

      return -1;
   }

   @EventListener
   public void AP(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (this.stopwatchImpl.afq((Float)this.delay.atx() * 1000.0F)
         && ((Boolean)this.booleanSetting4.atx() || !client.player.isUsingItem())
         && !Blink.bm()
         && (
            !(Boolean)this.booleanSetting5.atx()
               || client.player.isOnGround()
               || WorldUtils.Ng(1.0) && Velocity.amZ(client.player, new Box(client.player.getBlockPos().down()))
         )) {
         label49:
         if ((Boolean)this.booleanSetting2_1.atx()) {
            RegistryEntry registryEntry = StatusEffects.RESISTANCE;
            if (client.player.hasStatusEffect(registryEntry)) {
               RegistryEntry registryEntry2 = StatusEffects.RESISTANCE;
               if (client.player.getStatusEffect(registryEntry2).getAmplifier() >= 1) {
                  break label49;
               }
            }

            if (this.AQ(StatusEffects.RESISTANCE)) {
               this.stopwatchImpl.afr(29432, 5265492, (byte)40);
               return;
            }
         }

         if ((Boolean)this.booleanSetting1_1.atx()) {
            RegistryEntry registryEntry3 = StatusEffects.SPEED;
            if (!client.player.hasStatusEffect(registryEntry3) && this.AQ(StatusEffects.SPEED)) {
               this.stopwatchImpl.afr(29432, 5265492, (byte)40);
               return;
            }
         }

         if ((Boolean)this.booleanSetting3.atx()) {
            RegistryEntry registryEntry4 = StatusEffects.STRENGTH;
            if (!client.player.hasStatusEffect(registryEntry4) && this.AQ(StatusEffects.STRENGTH)) {
               this.stopwatchImpl.afr(29432, 5265492, (byte)40);
            }
         }
      }
   }

   public boolean AQ(RegistryEntry registryEntry) {
      if (!(Boolean)this.booleanSetting4.atx() && client.player.isUsingItem()) {
         return false;
      }

      int n = this.AO(registryEntry);
      if (n == -1) {
         return false;
      }

      int n2 = Managers.inventoryTracker.JS();
      float f2 = client.player.getYaw();
      if ((Boolean)this.swapAlternative.atx() && (Boolean)this.always.atx()) {
         Managers.inventoryTracker.JK(n, Managers.inventoryTracker.JS());
      } else if (n >= 9) {
         if ((Boolean)this.swapAlternative.atx()) {
            Managers.inventoryTracker.JK(n, Managers.inventoryTracker.JS());
         }
      } else {
         Managers.inventoryTracker.JF(n);
      }

      RotationUtils.nH(f2, 90.0F);
      final float f3 = f2;
      Managers.serverManager.iQ(new SequencedPacketCreator() {
         public Packet predict(int registryEntry) {
            return AutoPotion.AR(f3, registryEntry);
         }
      });
      ((SwingMode)this.swingMode.atx()).avX(Hand.MAIN_HAND);
      if ((Boolean)this.swapAlternative.atx() && (Boolean)this.always.atx()) {
         Managers.inventoryTracker.JK(n, Managers.inventoryTracker.JS());
      } else if (n >= 9) {
         if ((Boolean)this.swapAlternative.atx()) {
            Managers.inventoryTracker.JK(n, Managers.inventoryTracker.JS());
         }
      } else {
         Managers.inventoryTracker.JF(n2);
      }

      RotationUtils.nK();
      return true;
   }

   static Packet AR(float f2, int n) {
      return f(Hand.MAIN_HAND, n, f2);
   }

   private void AS() {
      this.AQ(StatusEffects.SPEED);
   }

   private void AT() {
      this.AQ(StatusEffects.STRENGTH);
   }

   private void AU() {
      this.AQ(StatusEffects.RESISTANCE);
   }

   private static UnidentifiedHelper17 g(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, -1, runnable);
   }

   private static PlayerInteractItemC2SPacket f(Hand hand, int n, float f2) {
      return new PlayerInteractItemC2SPacket(hand, n, f2, 90.0F);
   }
}
