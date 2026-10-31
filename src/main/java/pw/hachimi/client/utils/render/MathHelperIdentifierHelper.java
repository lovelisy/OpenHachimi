package pw.hachimi.client.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.gui.DrawScreenScreen;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class MathHelperIdentifierHelper extends DrawBase {
   private final Identifier id = Identifier.of("hachimiclient", "textures/sync_clickgui.png");
   private final UnidentifiedHelper7 unidentifiedHelper7 = new UnidentifiedHelper7(false, '\u0000', 200.0F, 496665058, '楚', Easing.CUBIC_OUT);
   private final Stopwatch stopwatch = new StopwatchImpl();
   private boolean d;
   private float[] e;
   private boolean q;
   private char[] r;
   private boolean s;
   private int t;
   private boolean u;

   public MathHelperIdentifierHelper(CategoryPanel categoryPanel, DrawMatrixHelper drawMatrixHelper, Setting setting2, float f2, float f3) {
      super(categoryPanel, drawMatrixHelper, setting2, f2, f3);
      float[] fs = ((ColorSetting)setting2).TH();
      this.e = new float[]{fs[0], fs[1], 1.0F - fs[2], fs[3]};
      this.r = Integer.toHexString(((ColorSetting)setting2).TA()).toCharArray();
      EventBusImpl.INSTANCE.rD(this);
   }

   @Override
   public void Cs(DrawContext context, float f2, float f3, int n, int n2) {
      this.vy(RenderUtils.acG() + UI.atK().atT());
      this.j = f2;
      this.k = f3;
      int n3 = ((ColorSetting)this.setting).TA();
      boolean flag = this.vC(n, n2);
      this.vg(context, this.jj(false, flag).getRGB());
      vi(context, f2 + this.l - 11.0F, f3 + (this.m - 10.0F) / 2.0F, 10.0, 10.0, n3);
      int n4 = UI.auf() ? -16777216 : -1;
      this.ve(context, vf(this.setting.atu()), f2 + 2.0F, f3 + UI.atK().atU(), n4);
      if (this.unidentifiedHelper7.qW() <= 0.01F) {
         this.ay();
         return;
      }

      ColorSetting colorSetting = (ColorSetting)this.setting;
      if (colorSetting.TI()) {
         Color color = ColorHelper3.auO().auU();
         float[] fs = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
         this.e = new float[]{fs[0], fs[1], 1.0F - fs[2], this.e[3]};
      }

      if (DrawScreenScreen.colorBase == this) {
         if (this.t == 0) {
            float f4 = this.j + 1.0F;
            float f5 = this.j + this.l - 1.0F;
            float f6 = this.k + this.m + 0.5F;
            float f7 = this.k + this.m + this.l;
            float f8 = f5 - f4;
            this.e[1] = MathHelper.clamp((n - f4) / f8, 0.001F, 0.999F);
            float f9 = f7 - f6;
            this.e[2] = MathHelper.clamp((n2 - f6) / f9, 0.001F, 0.999F);
         }

         if (this.t == 1 && !colorSetting.TI()) {
            this.e[0] = MathHelper.clamp((n - (this.j + 1.0F)) / (this.l - 1.0F), 0.001F, 0.999F);
         }

         if (colorSetting.TF() && this.t == 2) {
            this.e[3] = MathHelper.clamp((n - (this.j + 1.0F)) / (this.l - 1.0F), 0.001F, 0.999F);
         }

         Color color2 = Color.getHSBColor(this.e[0], this.e[1], 1.0F - this.e[2]);
         color2 = new Color(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, MathHelper.clamp(this.e[3], 0.0F, 1.0F));
         this.q = true;
         colorSetting.aty(color2);
         this.q = false;
      }

      float[] fs2 = this.e;
      int n5 = Color.HSBtoRGB(fs2[0], 1.0F, 1.0F);
      this.vF(this.j, this.k + this.m, this.j + this.l, this.k + this.m + this.ax() * this.aB());

      for (float f10 = 0.0F; f10 < this.l - 1.0F; f10++) {
         float f11 = f10 / (this.l - 1.0F);
         vi(context, this.j + 0.5 + f10, this.k + this.m + 0.5 + this.l, 1.0, 8.0, Color.getHSBColor(f11, 1.0F, 1.0F).getRGB());
      }

      vi(context, this.j + (this.l - 1.0F) * fs2[0], this.k + this.m + 0.5 + this.l, 1.0, 8.0, -1);
      this.vq(context, this.j + 0.5F, this.k + this.m + 0.5F, this.j + this.l - 0.5F, this.k + this.m - 0.5F + this.l, -1, n5, true);
      this.vq(context, this.j + 0.5F, this.k + this.m + 0.5F, this.j + this.l - 0.5F, this.k + this.m - 0.5F + this.l, 0, -16777216, false);
      vi(context, this.j + this.l * fs2[1] - 1.0F, this.k + this.m - 0.5 + this.l * fs2[2], 2.0, 2.0, -1);
      if (colorSetting.TF()) {
         float f12 = 0.0F;

         while (f12 < this.l - 1.0) {
            float f13 = this.j + 0.5F + f12;
            double d = 4.0;
            if (f13 + 4.0 > this.l - 1.0) {
               d = this.l - 1.0 - f12;
            }

            vi(context, f13, this.k + this.m + 9.0F + this.l, d, 4.0, -7303024);
            vi(context, f13, this.k + this.m + 13.0F + this.l, d, 4.0, -1);
            f12 += 4.0F;
            if (f12 >= this.l - 1.0) {
               break;
            }

            float f14 = this.j + 0.5F + f12;
            double d2 = 4.0;
            if (f14 + 4.0 > this.l - 1.0) {
               d2 = this.l - 1.0 - f12;
            }

            vi(context, f14, this.k + this.m + 9.0F + this.l, d2, 4.0, -1);
            vi(context, f14, this.k + this.m + 13.0F + this.l, d2, 4.0, -7303024);
            f12 += 4.0F;
         }

         vn(context, this.j + 0.5, this.k + this.m + 9.0F + this.l, this.l - 1.0, 8.0, 0, n5);
         vi(context, this.j + (this.l - 1.0F) * fs2[3], this.k + this.m + 9.0F + this.l, 1.0, 8.0, -1);
      }

      String str = this.s ? new String(this.r) + this.az() : new String(this.r);
      float f15 = this.j + 1.0F + this.l - 14.0F;
      float f16 = this.k + this.m + (colorSetting.TF() ? 17.5F : 9.0F) + this.l;
      if (this.setting.atu().equalsIgnoreCase("Theme") || colorSetting.TI()) {
         vi(context, this.j + 1.0F + this.l - 15.0F, f16, 13.5, 12.0, UI.atK().atR(1.0F));
      }

      vi(context, this.j + 0.5, f16, this.l - 15.0F, 12.0, 814780560);
      this.ve(context, "#" + str, this.j + 1.0F, f16 + 3.0F, n4);
      RenderSystem.setShaderTexture(0, this.id);
      this.vr(context, f15 + 0.5F, f16 + 1.0F);
      this.drawMatrixHelper.anR((float)(this.ax() * this.unidentifiedHelper7.qW()));
      ((CategoryPanel)this.renderHelper2).bE((float)(this.ax() * this.unidentifiedHelper7.qW() * this.drawMatrixHelper.anT()));
      this.vG();
   }

   @Override
   public void UU(int n, int n2, int n3) {
      ColorSetting colorSetting = (ColorSetting)this.setting;
      float f2 = this.k + this.m + (colorSetting.TF() ? 17.5F : 9.0F) + this.l;
      boolean flag = this.vD(n, n2, this.j + 0.5, f2, this.l - 15.5F, 12.0);
      if (!flag) {
         this.ay();
      }

      if (this.vC(n, n2) && n3 == 1) {
         this.d = !this.d;
         this.unidentifiedHelper7.qV(this.d);
         if (UI.atK().auc()) {
            Managers.soundManager.jg(SoundManager.a);
         }
      }

      if (n3 == 0 && this.d) {
         if (this.vD(n, n2, this.j + 0.5, this.k + this.m + 0.5F, this.l - 1.0F, this.l - 1.0F)) {
            DrawScreenScreen.colorBase = this;
            this.t = 0;
         } else if (this.vD(n, n2, this.j + 0.5, this.k + this.m + 0.5F + this.l, this.l - 1.0F, 8.0)) {
            DrawScreenScreen.colorBase = this;
            this.t = 1;
         } else if (this.vD(n, n2, this.j + 0.5, this.k + this.m + 9.0F + this.l, this.l - 1.0F, 8.0)) {
            DrawScreenScreen.colorBase = this;
            this.t = 2;
         }

         if (flag) {
            if (this.s) {
               ColorSetting temp = colorSetting;

               try {
                  Color color = temp.TL((new String(this.r)).trim());
                  this.setting.aty(color);
               } catch (NumberFormatException numberFormatException) {
               }

               this.s = false;
            } else {
               this.s = true;
            }

            if (UI.atK().auc()) {
               Managers.soundManager.jg(SoundManager.a);
            }
         }

         if (!this.setting.getText().equalsIgnoreCase("Global") && this.vD(n, n2, this.j + this.l - 14.5F, f2, 13.5, 12.0)) {
            boolean flag2 = !colorSetting.TI();
            colorSetting.TJ(flag2);
            float[] fs = ((ColorSetting)this.setting).TH();
            this.e = new float[]{fs[0], fs[1], 1.0F - fs[2], fs[3]};
            if (UI.atK().auc()) {
               Managers.soundManager.jg(SoundManager.a);
            }
         }
      }
   }

   @EventListener
   public void av(UnknownEvent3 unknownEvent3) {
      if (unknownEvent3.getSetting() == this.getSetting() && unknownEvent3.getEventPhase() == EventPhase.POST) {
         if (!this.q) {
            float[] fs = ((ColorSetting)this.setting).TH();
            this.e = new float[]{fs[0], fs[1], 1.0F - fs[2], fs[3]};
         }

         this.r = Integer.toHexString(((ColorSetting)this.setting).TA()).toCharArray();
      }
   }

   @Override
   public void UV() {
   }

   @Override
   public void UW(int n) {
      if (this.s) {
         ColorSetting colorSetting = (ColorSetting)this.setting;
         switch (n) {
            case 256:
               this.r = Integer.toHexString(colorSetting.TA()).toCharArray();
               this.s = false;
               break;
            case 257:
               ColorSetting temp = colorSetting;

               try {
                  Color color = temp.TL((new String(this.r)).trim());
                  this.setting.aty(color);
               } catch (NumberFormatException numberFormatException) {
               }

               this.r = Integer.toHexString(colorSetting.TA()).toCharArray();
               this.s = false;
            case 258:
            default:
               break;
            case 259:
               if (this.r.length != 0) {
                  this.r = ArrayUtils.remove(this.r, this.r.length - 1);
               }
         }
      }
   }

   @Override
   public void UX(char c) {
      if (this.s && this.r.length < 8 && this.aw(c)) {
         this.r = ArrayUtils.add(this.r, c);
      }
   }

   public boolean aw(char c) {
      return c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f';
   }

   public float ax() {
      float f2 = 20.5F;
      if (((ColorSetting)this.setting).TF()) {
         f2 = 29.5F;
      }

      return f2 + this.l;
   }

   public void ay() {
      this.s = false;
   }

   public String az() {
      if (this.stopwatch.afq(250)) {
         this.u = !this.u;
         this.stopwatch.afr(29432, 5265492, (byte)40);
      }

      return this.u && this.s ? "_" : "";
   }

   public boolean aA() {
      return this.s;
   }

   public float aB() {
      return (float)this.unidentifiedHelper7.qW();
   }
}
