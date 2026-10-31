package pw.hachimi.client.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;

public class Glint extends ClientModule {
   private static Glint INSTANCE;
   Setting color = this.registerSetting(createColorSetting("Color", "The color of the enchantment glint", q()));
   Setting strength = this.registerSetting(new NumberSetting("Strength", "The strength/intensity of the enchantment glint", 0.0F, 0.75F, 5.0F));
   Setting entityStrength = this.registerSetting(new NumberSetting("EntityStrength", "The strength/intensity of the enchantment glint", 0.0F, 0.75F, 5.0F));
   Setting speed = this.registerSetting(new NumberSetting("Speed", "The animation speed of the enchantment glint", 0.0, 0.75, 5.0));
   Setting override = this.registerSetting(new BooleanSetting("Override", "Override the vanilla texture", true));
   private static final Identifier f = Identifier.of("hachimiclient", "textures/enchanted_glint_entity.png");
   private static final Identifier g = Identifier.of("hachimiclient", "textures/enchanted_glint_item.png");

   public Glint() {
      super("Glint", "Custom glint", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static boolean OU() {
      boolean flag;
      if (INSTANCE != null && INSTANCE.awg()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static void OV(boolean flag) {
      if (OU()) {
         MinecraftClient client2 = MinecraftClient.getInstance();
         if (client2 != null && client2.getTextureManager() != null) {
            if ((Boolean)INSTANCE.override.atx()) {
               if (flag) {
                  TextureManager temp = client2.getTextureManager();
                  Identifier id = f;
                  AbstractTexture abstractTexture = temp.getTexture(id);
                  if (abstractTexture != null) {
                     abstractTexture.setFilter(true, false);
                  }

                  RenderSystem.setShaderTexture(0, f);
               } else {
                  TextureManager textureManager = client2.getTextureManager();
                  Identifier id2 = g;
                  AbstractTexture abstractTexture2 = textureManager.getTexture(id2);
                  if (abstractTexture2 != null) {
                     abstractTexture2.setFilter(true, false);
                  }

                  RenderSystem.setShaderTexture(0, g);
               }
            }

            Color color2 = (Color)INSTANCE.color.atx();
            RenderSystem.setShaderColor(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, color2.getAlpha() / 255.0F);
            Float floatValue;
            if (flag) {
               floatValue = (Float)INSTANCE.entityStrength.atx();
            } else {
               floatValue = (Float)INSTANCE.strength.atx();
            }

            RenderSystem.setShaderGlintAlpha(floatValue);
         }
      }
   }

   public static void OW() {
      if (OU()) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.setShaderGlintAlpha(1.0F);
      }
   }

   public static double OX() {
      return (Double)INSTANCE.speed.atx();
   }

   private static ColorSetting createColorSetting(String str, String str2, Color color2) {
      return new ColorSetting(str, str2, color2, true, true);
   }

   private static Color q() {
      return new Color(0, 0, 0, 255);
   }
}
