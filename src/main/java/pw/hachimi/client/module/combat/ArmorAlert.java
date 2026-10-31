package pw.hachimi.client.module.combat;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.render.RenderEvent;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class ArmorAlert extends ClientModule {
   Setting percent = this.registerSetting(new NumberSetting("Percent", "yes.", 1, 20, 100));
   Setting hud = this.registerSetting(new BooleanSetting("HUD", "Render warning on your screen", true));
   Setting offset = this.registerSetting(new NumberSetting("Offset", "Indicator offset", -200, 0, 200, this.hud::atx));
   Setting color = this.registerSetting(new ColorSetting("Color", "Indicator color", Color.RED, this.hud::atx));
   boolean[] e = new boolean[]{false, false, false, false};
   int f;
   public static boolean dg;

   public ArmorAlert() {
      super("ArmorAlert", "", ModuleCategory.COMBAT);
   }

   @Override
   protected void awd() {
      this.e = new boolean[]{false, false, false, false};
   }

   @EventListener
   public void jl(TickEntitiesPreEvent tickEntitiesPreEvent) {
      int n = -1;
      Iterator iterator2 = client.player.getInventory().armor.iterator();

      while (iterator2.hasNext()) {
         ItemStack stack = (ItemStack)iterator2.next();
         if (++n >= 4) {
            break;
         }

         if (!stack.isEmpty()) {
            Item item2 = stack.getItem();
            if (item2 instanceof ArmorItem) {
               int n2 = this.jn(stack);
               if (n2 >= (Integer)this.percent.atx()) {
                  this.e[n] = false;
                  continue;
               }

               label50:
               while (true) {
                  this.f = n2;

                  label48:
                  while (true) {
                     if (!this.e[n]) {
                        ChatUtils.UC("§4Your " + ((ArmorItem)item2).getSlotType().getName() + " is at " + n2 + "%!", this.hashCode() + n);
                        this.e[n] = true;
                     }

                     while (iterator2.hasNext()) {
                        stack = (ItemStack)iterator2.next();
                        if (++n >= 4) {
                           return;
                        }

                        if (!stack.isEmpty()) {
                           item2 = stack.getItem();
                           if (item2 instanceof ArmorItem) {
                              n2 = this.jn(stack);
                              if (n2 < (Integer)this.percent.atx()) {
                                 if (n2 < this.f) {
                                    continue label50;
                                 }
                                 continue label48;
                              }

                              this.e[n] = false;
                              continue;
                           }
                        }

                        this.e[n] = false;
                     }

                     return;
                  }
               }
            }
         }

         this.e[n] = false;
      }
   }

   @EventListener
   public void onRender(RenderEvent renderEvent) {
      if ((Boolean)this.hud.atx()) {
         boolean[] flags = this.e;
         int n = flags.length;

         for (int n2 = 0; n2 < n; n2++) {
            if (flags[n2]) {
               String str = "Low durability (" + this.f + "%)";
               RenderUtils.acE(
                  renderEvent.getContext(),
                  str,
                  client.getWindow().getScaledWidth() / 2.0F - RenderUtils.acF(str) / 2.0F,
                  client.getWindow().getScaledHeight() / 2.0F + RenderUtils.acG() - ((Integer)this.offset.atx()).intValue(),
                  ((Color)this.color.atx()).getRGB()
               );
               break;
            }
         }
      }
   }

   int jn(ItemStack stack) {
      return (int)((stack.getMaxDamage() - stack.getDamage()) / Math.max(0.1, stack.getMaxDamage()) * 100.0);
   }
}
