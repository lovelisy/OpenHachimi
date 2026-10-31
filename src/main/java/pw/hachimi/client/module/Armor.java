package pw.hachimi.client.module;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.ColorUtils;

public class Armor extends RenderModule {
   Setting postEffect = this.registerSetting(new BooleanSetting("Durability", "Displays player equipped armor durability", false));
   Setting durability = this.registerSetting(new BooleanSetting("AutoPositioning", "Yes.", true));

   public Armor() {
      super("Armor", "");
   }

   @Override
   public void adb() {
   }

   @Override
   public void adc(DrawContext context) {
      Entity entity2 = client.player.getVehicle();
      int n;
      int n2;
      if ((Boolean)this.durability.atx()) {
         n = client.getWindow().getScaledWidth() / 2 + 11;
         n2 = client.getWindow().getScaledHeight();
         int n3 = client.player.getMaxAir();
         int n4 = Math.min(client.player.getAir(), n3);
         TagKey tagKey = FluidTags.WATER;
         if (client.player.isSubmergedIn(tagKey) || n4 < n3) {
            n2 -= 65;
         } else if (entity2 instanceof LivingEntity) {
            n2 -= 45 + (int)Math.ceil((((LivingEntity)entity2).getMaxHealth() - 1.0F) / 20.0F) * 10;
         } else if (client.player.isCreative()) {
            n2 -= 45;
         } else {
            n2 -= 55;
         }
      } else {
         n = (int)this.add();
         n2 = (int)this.ade();
      }

      int n5 = 3;

      do {
         int n6 = n5;
         ItemStack stack = (ItemStack)client.player.getInventory().armor.get(n6);
         int n7 = stack.getMaxDamage();
         int n8 = stack.getDamage();
         context.drawItem(stack, n, n2);
         TextRenderer textRenderer2 = client.textRenderer;
         context.drawItemInSlot(textRenderer2, stack, n, n2);
         if ((Boolean)this.postEffect.atx() && !stack.isEmpty() && stack.isDamageable()) {
            String str = String.valueOf((int)Math.floor((float)(n7 - n8) / n7 * 100.0F));
            this.adr(
               context,
               str,
               n + 8.0F - RenderUtils.acF(str) / 2.0F,
               n2 - 8.0F,
               ColorUtils.iz((float)(n7 - n8) / n7 * 120.0F, 100.0F, 50.0F, 1.0F).getRGB()
            );
         }

         n += 20;
      } while (--n5 >= 0);
   }

   @Override
   public void acY(DrawContext context) {
      if (!(Boolean)this.durability.atx()) {
         super.acY(context);
      }
   }

   @Override
   public float adl() {
      return 78.0F + super.adl();
   }

   @Override
   public float adm() {
      return 16.0F + super.adm();
   }
}
