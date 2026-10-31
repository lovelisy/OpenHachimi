package pw.hachimi.client.module;

import java.util.Iterator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import pw.hachimi.client.gui.DrawBufferScreen;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;

public class ModuleManagerScreenHelper extends DrawRenderModule {
   Setting setting_1;
   Setting setting2;
   int c;

   public ModuleManagerScreenHelper() {
      super("EffectIndicator", "");
      this.setting_1 = this.registerSetting(new BooleanSetting("PotionCount", "", true));
      this.setting2 = this.registerSetting(new BooleanSetting("LevelCheck", "", true, this.setting_1::atx));
      this.c = 0;
   }

   @Override
   public void adb() {
      StringBuilder stringBuilder = new StringBuilder();
      if ((Boolean)this.setting_1.atx()) {
         this.c = this.oP(StatusEffects.RESISTANCE);
         if (this.c > 0) {
            stringBuilder.append("Pots: ");
            stringBuilder.append(this.c);
         }
      }

      if (client.player.hasStatusEffect(StatusEffects.RESISTANCE)
         && (!(Boolean)this.setting2.atx() || client.player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() > 0)) {
         stringBuilder.append(" ");
         stringBuilder.append("Resistance: ");
         stringBuilder.append(client.player.getStatusEffect(StatusEffects.RESISTANCE).getDuration() / 20 + 1);
      }

      if (client.player.hasStatusEffect(StatusEffects.STRENGTH)) {
         stringBuilder.append(" ");
         stringBuilder.append("Strength: ");
         stringBuilder.append(client.player.getStatusEffect(StatusEffects.STRENGTH).getDuration() / 20 + 1);
      }

      if (client.player.hasStatusEffect(StatusEffects.SPEED)) {
         stringBuilder.append(" ");
         stringBuilder.append("Speed: ");
         stringBuilder.append(client.player.getStatusEffect(StatusEffects.SPEED).getDuration() / 20 + 1);
      }

      this.text = stringBuilder.toString().trim();
      super.adb();
   }

   @Override
   public void adn(DrawContext context) {
      if (!(this.adl() < 6.0F)) {
         super.adn(context);
      }
   }

   @Override
   public float adl() {
      float f = super.adl();
      return f < 10.0F && client.currentScreen instanceof DrawBufferScreen ? 10.0F : f;
   }

   @Override
   public float adm() {
      float f = super.adm();
      return f < 10.0F && client.currentScreen instanceof DrawBufferScreen ? 10.0F : f;
   }

   public int oP(RegistryEntry registryEntry) {
      int n = 0;
      int n2 = 0;

      do {
         PlayerInventory temp = client.player.getInventory();
         int n3 = n2;
         ItemStack stack = temp.getStack(n3);
         if (stack.getItem() == net.minecraft.item.Items.SPLASH_POTION) {
            PotionContentsComponent potionContentsComponent = PotionContentsComponent.DEFAULT;
            ComponentType componentType = DataComponentTypes.POTION_CONTENTS;
            Iterator iterator2 = ((PotionContentsComponent)stack.getOrDefault(componentType, potionContentsComponent)).getEffects().iterator();

            while (iterator2.hasNext()) {
               if (((StatusEffectInstance)iterator2.next()).getEffectType() == registryEntry) {
                  n++;
                  break;
               }
            }
         }
      } while (++n2 < 45);

      return n;
   }
}
