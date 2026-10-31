package pw.hachimi.client.module;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.world.GameMode;
import pw.hachimi.client.event.render.RenderCrosshairEvent;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.LeftMiddleMode;
import pw.hachimi.client.model.render.TopMiddleMode;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Crosshair extends RenderModule {
   private final UnidentifiedHelper7 unidentifiedHelper7 = new UnidentifiedHelper7(false, '\u0000', 100.0F, 496665058, '楚', Easing.LINEAR);
   Setting numberSetting1_1 = this.registerSetting(new NumberSetting("Length", "The crosshair length", 0.0F, 1.0F, 2.5F));
   Setting numberSetting2_1 = this.registerSetting(new NumberSetting("Thickness", "The crosshair thickness", 0.1F, 0.5F, 2.0F));
   Setting numberSetting3_1 = this.registerSetting(new NumberSetting("Gap", "The gap between the lines", 0, 2, 5));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Dynamic", "Indicates when the player is moving", false));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("Outline", "Outlines the crosshair", true));
   Setting numberSetting4 = this.registerSetting(new NumberSetting("OutlineThickness", "The width of the outline", 0.1F, 0.3F, 0.5F, this::isNumberSetting4Visible));
   Setting numberSetting5 = this.registerSetting(new NumberSetting("Opacity", "The crosshair opacity", 0.1F, 1.0F, 1.0F));
   Setting color = this.registerSetting(new ColorSetting("Color", "The crosshair color", Color.WHITE));

   public Crosshair() {
      super("Crosshair", "Custom crosshairs");
      this.numberSetting1_1.aty(0.0F);
      this.numberSetting2_1.aty(0.0F);
      this.leftMiddleMode.aty(LeftMiddleMode.MIDDLE);
      this.topMiddleMode.aty(TopMiddleMode.MIDDLE);
   }

   @EventListener
   public void onRenderCrosshair(RenderCrosshairEvent renderCrosshairEvent) {
      renderCrosshairEvent.aim();
   }

   @Override
   public float adl() {
      return 6.0F;
   }

   @Override
   public float adm() {
      return 6.0F;
   }

   @Override
   public void adb() {
   }

   @Override
   public void adc(DrawContext context) {
      if (client.options.getPerspective().isFirstPerson() && client.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
         float f = this.add() + 3.0F;
         float f2 = this.ade() + 3.0F;
         float f3 = (Float)this.numberSetting1_1.atx() * 10.0F / 2.0F;
         float f4 = (Float)this.numberSetting4.atx();
         float f5 = f4 * 2.0F;
         float f6 = f4 * 3.0F;
         boolean flag = MovementUtils.awI() || client.player.isSneaking() || client.player.isClimbing() || !client.player.isOnGround();
         double d = ((Integer)this.numberSetting3_1.atx()).intValue();
         if ((Boolean)this.booleanSetting1_1.atx()) {
            this.unidentifiedHelper7.qV(flag);
            d += 2.0 * this.unidentifiedHelper7.qW();
         }

         if ((Boolean)this.booleanSetting2_1.atx()) {
            RenderUtils.acz(
               context.getMatrices(),
               f - f3 - d - f5,
               f2 - (Float)this.numberSetting2_1.atx() - f4,
               f3 + f6,
               (Float)this.numberSetting2_1.atx() * 2.0F + f5,
               ColorUtils.iF(Color.BLACK.getRGB(), (Float)this.numberSetting5.atx())
            );
            RenderUtils.acz(
               context.getMatrices(),
               f + d - f5,
               f2 - (Float)this.numberSetting2_1.atx() - f4,
               f3 + f6,
               (Float)this.numberSetting2_1.atx() * 2.0F + f5,
               ColorUtils.iF(Color.BLACK.getRGB(), (Float)this.numberSetting5.atx())
            );
         }

         if ((Boolean)this.booleanSetting2_1.atx()) {
            RenderUtils.acz(
               context.getMatrices(),
               f - (Float)this.numberSetting2_1.atx() - f4,
               f2 - f3 - d - f4,
               (Float)this.numberSetting2_1.atx() * 2.0F + f5,
               f3 + f6,
               ColorUtils.iF(Color.BLACK.getRGB(), (Float)this.numberSetting5.atx())
            );
            RenderUtils.acz(
               context.getMatrices(),
               f - (Float)this.numberSetting2_1.atx() - f4,
               f2 + d - f4,
               (Float)this.numberSetting2_1.atx() * 2.0F + f5,
               f3 + f6,
               ColorUtils.iF(Color.BLACK.getRGB(), (Float)this.numberSetting5.atx())
            );
         }

         RenderUtils.acz(
            context.getMatrices(),
            f - f3 - d,
            f2 - (Float)this.numberSetting2_1.atx(),
            f3,
            (Float)this.numberSetting2_1.atx() * 2.0F,
            ColorUtils.iF(((Color)this.color.atx()).getRGB(), (Float)this.numberSetting5.atx())
         );
         RenderUtils.acz(
            context.getMatrices(),
            f + d,
            f2 - (Float)this.numberSetting2_1.atx(),
            f3,
            (Float)this.numberSetting2_1.atx() * 2.0F,
            ColorUtils.iF(((Color)this.color.atx()).getRGB(), (Float)this.numberSetting5.atx())
         );
         RenderUtils.acz(
            context.getMatrices(),
            f - (Float)this.numberSetting2_1.atx(),
            f2 - f3 - d,
            (Float)this.numberSetting2_1.atx() * 2.0F,
            f3,
            ColorUtils.iF(((Color)this.color.atx()).getRGB(), (Float)this.numberSetting5.atx())
         );
         RenderUtils.acz(
            context.getMatrices(),
            f - (Float)this.numberSetting2_1.atx(),
            f2 + d,
            (Float)this.numberSetting2_1.atx() * 2.0F,
            f3,
            ColorUtils.iF(((Color)this.color.atx()).getRGB(), (Float)this.numberSetting5.atx())
         );
      }
   }

   private Boolean isNumberSetting4Visible() {
      return (Boolean)this.booleanSetting2_1.atx();
   }
}
