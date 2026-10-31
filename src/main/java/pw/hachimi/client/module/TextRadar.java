package pw.hachimi.client.module;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.LeftRightMode;
import pw.hachimi.client.model.render.LeftMiddleMode;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.module.misc.PotionTracker;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.WorldUtils;

public class TextRadar extends RenderModule {
   private static TextRadar INSTANCE;
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Red", "", false));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("Self", "", false));
   Setting booleanSetting3 = this.registerSetting(new BooleanSetting("Health", "", true));
   Setting booleanSetting4 = this.registerSetting(new BooleanSetting("Pops", "", true));
   Setting booleanSetting5 = this.registerSetting(new BooleanSetting("Distance", "", true));
   Setting colorSetting1_1 = this.registerSetting(new ColorSetting("Color", "The color for player", new Color(16777215), false, false));
   Setting ignoreNakeds = this.registerSetting(new BooleanSetting("IgnoreNakeds", "Ignore players with no items", false));
   Setting xLocation = this.registerSetting(new EnumSetting("X-Location", "", LeftRightMode.Auto, LeftRightMode.values()));
   List list = new ArrayList();
   int j;

   public TextRadar() {
      super("TextRadar", "");
      INSTANCE = this;
   }

   @Override
   public void adc(DrawContext context) {
      float f = this.ade();
      float f2 = this.add();
      float f3 = RenderUtils.acF(" ");
      if (this.xLocation.atx() == LeftRightMode.Right
         || this.xLocation.atx() == LeftRightMode.Auto
            && (
               this.ads() == LeftMiddleMode.RIGHT
                  || this.ads() == LeftMiddleMode.RIGHT_OFFSET
                  || this.adi().isEmpty() && f2 > client.getWindow().getScaledWidth() / 2.0
            )) {
         f2 += this.j;

         for (Iterator iterator2 = this.list.iterator(); iterator2.hasNext(); f += RenderUtils.acG()) {
            List list2 = (List)iterator2.next();
            float f4 = 0.0F;
            Iterator iterator3 = list2.iterator();

            while (iterator3.hasNext()) {
               Pair pair = (Pair)iterator3.next();
               float f5 = RenderUtils.acF(((String)pair.getLeft()).trim());
               this.adr(context, ((String)pair.getLeft()).trim(), f2 - f5 - f4, f + 1.0F, (Integer)pair.getRight());
               f4 += f5 + f3;
            }
         }
      } else {
         for (Iterator iterator4 = this.list.iterator(); iterator4.hasNext(); f += RenderUtils.acG()) {
            List list3 = (List)iterator4.next();
            float f6 = 0.0F;
            Iterator iterator5 = list3.iterator();

            while (iterator5.hasNext()) {
               Pair pair2 = (Pair)iterator5.next();
               float f7 = RenderUtils.acF(((String)pair2.getLeft()).trim());
               this.adr(context, ((String)pair2.getLeft()).trim(), f2 + f6, f + 1.0F, (Integer)pair2.getRight());
               f6 += f7 + f3;
            }
         }
      }
   }

   @Override
   public void adb() {
      this.list.clear();
      this.j = 0;
      ArrayList arrayList = new ArrayList(client.world.getPlayers());
      Comparator comparator = Comparator.comparingDouble(new ToDoubleFunction() {
         @Override
         public double applyAsDouble(Object obj) {
            return TextRadar.aad((AbstractClientPlayerEntity)obj);
         }
      });
      arrayList.sort(comparator);
      Iterator iterator2 = arrayList.iterator();

      while (iterator2.hasNext()) {
         PlayerEntity player2 = (PlayerEntity)iterator2.next();
         if ((!(Boolean)this.ignoreNakeds.atx() || !WorldUtils.NO(player2)) && (player2 != client.player || (Boolean)this.booleanSetting2_1.atx())) {
            ArrayList arrayList2 = new ArrayList();
            this.list.add(arrayList2);
            StringBuilder stringBuilder = new StringBuilder();
            if ((Boolean)this.booleanSetting3.atx()) {
               Formatting formatting = aab(player2);
               stringBuilder.append(formatting);
               DecimalFormat temp = bo;
               double d = player2.getHealth() + player2.getAbsorptionAmount();
               String str = temp.format(d);
               stringBuilder.append(str);
               stringBuilder.append(" ");
            }

            Formatting formatting2 = Formatting.RESET;
            stringBuilder.append(formatting2);
            String str2 = player2.getName().getString();
            stringBuilder.append(str2);
            if ((Boolean)this.booleanSetting5.atx()) {
               stringBuilder.append(" ");
               Formatting formatting3 = Formatting.WHITE;
               stringBuilder.append(formatting3);
               DecimalFormat decimalFormat = bo;
               double d2 = client.player.distanceTo((Entity)player2);
               String str3 = decimalFormat.format(d2);
               stringBuilder.append(str3);
               stringBuilder.append("m");
            }

            if ((Boolean)this.booleanSetting4.atx()) {
               int n = Managers.entityEntityStatusS2CPacketManager.acp(player2);
               if (n > 0) {
                  stringBuilder.append(" ");
                  Formatting formatting4 = this.aac(n);
                  stringBuilder.append(formatting4);
                  stringBuilder.append("-");
                  stringBuilder.append(n);
               }
            }

            int n2 = Managers.friendManager.awr(player2) ? ColorHelper.wx().wB().getRGB() : ((Color)this.colorSetting1_1.atx()).getRGB();
            Pair pair = new Pair(stringBuilder.toString(), n2);
            arrayList2.add(pair);
            PotionTracker potionTracker = PotionTracker.Ec();
            if (potionTracker != null && potionTracker.Ef()) {
               List list2 = (List)potionTracker.getMap2().get(player2);
               if (list2 != null && !list2.isEmpty()) {
                  HashMap hashMap = new HashMap();
                  Iterator iterator3 = list2.iterator();

                  while (iterator3.hasNext()) {
                     StatusEffectInstance statusEffectInstance = (StatusEffectInstance)iterator3.next();
                     RegistryEntry registryEntry = statusEffectInstance.getEffectType();
                     StatusEffectInstance statusEffectInstance2 = (StatusEffectInstance)hashMap.get(registryEntry);
                     if (statusEffectInstance2 == null
                        || statusEffectInstance.getAmplifier() >= statusEffectInstance2.getAmplifier()
                           && (statusEffectInstance.getAmplifier() != statusEffectInstance2.getAmplifier() || statusEffectInstance.getDuration() >= statusEffectInstance2.getDuration())) {
                        RegistryEntry registryEntry2 = statusEffectInstance.getEffectType();
                        hashMap.put(registryEntry2, statusEffectInstance);
                     }
                  }

                  iterator3 = hashMap.values().iterator();

                  while (iterator3.hasNext()) {
                     StatusEffectInstance statusEffectInstance3 = (StatusEffectInstance)iterator3.next();
                     StringBuilder stringBuilder2 = new StringBuilder();
                     StatusEffect statusEffect = (StatusEffect)statusEffectInstance3.getEffectType().value();
                     stringBuilder2.append(" ");
                     String str4 = statusEffect.getName().getString();
                     stringBuilder2.append(str4);
                     stringBuilder2.append(" ");
                     int n3 = statusEffectInstance3.getAmplifier() + 1;
                     stringBuilder2.append(n3);
                     stringBuilder2.append(" §7");
                     DecimalFormat decimalFormat2 = bo;
                     double d3 = statusEffectInstance3.getDuration() / 20.0;
                     String str5 = decimalFormat2.format(d3);
                     stringBuilder2.append(str5);
                     stringBuilder.append(stringBuilder2);
                     Pair pair2 = new Pair(stringBuilder2.toString(), ColorUtils.iE(new Color(statusEffect.getColor()), 255).getRGB());
                     arrayList2.add(pair2);
                  }
               }
            }

            this.j = Math.max(this.j, RenderUtils.acF(stringBuilder.toString()));
         }
      }
   }

   @Override
   public float adl() {
      return this.j + super.adl();
   }

   @Override
   public float adm() {
      return this.list.size() * RenderUtils.acG() + super.adm();
   }

   public static Formatting aab(PlayerEntity player2) {
      double d = player2.getHealth() + player2.getAbsorptionAmount();
      if (d > 18.0) {
         return Formatting.GREEN;
      } else if (d > 16.0) {
         return Formatting.DARK_GREEN;
      } else if (d > 12.0) {
         return Formatting.YELLOW;
      } else if (d > 8.0) {
         return Formatting.GOLD;
      } else {
         return d > 4.0 ? Formatting.RED : Formatting.DARK_RED;
      }
   }

   public Formatting aac(int n) {
      if ((Boolean)INSTANCE.booleanSetting1_1.atx()) {
         return Formatting.RED;
      } else if (n > 10) {
         return Formatting.DARK_RED;
      } else if (n > 8) {
         return Formatting.RED;
      } else if (n > 6) {
         return Formatting.GOLD;
      } else if (n > 4) {
         return Formatting.YELLOW;
      } else {
         return n > 2 ? Formatting.DARK_GREEN : Formatting.GREEN;
      }
   }

   static double aad(AbstractClientPlayerEntity player2) {
      return client.player.distanceTo((Entity)player2);
   }
}
