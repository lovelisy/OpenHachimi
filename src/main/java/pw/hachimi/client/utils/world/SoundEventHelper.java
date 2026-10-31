package pw.hachimi.client.utils.world;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.DrawMatrixHelper;

public class SoundEventHelper extends DrawBase {
   private final UnidentifiedHelper7 unidentifiedHelper7;
   private int b_;
   private int c;
   private boolean d;
   private float e;
   private static final long b = 0L;

   public SoundEventHelper(CategoryPanel categoryPanel, DrawMatrixHelper drawMatrixHelper2, Setting setting2, float f, float f2) {
      super(categoryPanel, drawMatrixHelper2, setting2, f, f2);
      this.unidentifiedHelper7 = new UnidentifiedHelper7(false, '\u0000', 200.0F, 496665058, '楚', Easing.CUBIC_OUT);
      this.b_ = -1;
   }

   @Override
   public void Cs(DrawContext context, float f, float f2, int n, int n2) {
      if (this.d != this.unidentifiedHelper7.qU()) {
         this.unidentifiedHelper7.qV(this.d);
      }

      this.e = this.c * (RenderUtils.acG() + 1.0F) + 3.0F;
      this.vy(RenderUtils.acG() + UI.atK().atT());
      this.j = f;
      this.k = f2;
      String str = DirectionUtils.apW((Enum)this.setting.atx());
      boolean flag = this.vC(n, n2);
      int n3 = this.jj(true, flag).getRGB();
      this.vg(context, n3);
      int n4 = UI.auf() ? -16777216 : -1;
      String str2 = vf(this.setting.atu());
      this.ve(context, str2, f + 2.0F, f2 + UI.atK().atU(), n4);
      float f3 = RenderUtils.acF(vf(str));
      this.ve(context, vf(str), f + this.l - f3 - 2.0F, f2 + UI.atK().atU(), -5592406);
      if (this.unidentifiedHelper7.qW() > 0.01F) {
         this.vF(this.j, this.k + this.m, this.j + this.l, this.k + this.m + this.xT() * this.xS());
         vi(context, this.j, this.k + this.m, 0.5, this.xT() * this.xS() - 0.5, n3);
         vi(context, this.j + this.l - 0.5, this.k + this.m, 0.5, this.xT() * this.xS() - 0.5, n3);
         vi(context, this.j + 0.5, this.k + this.m + this.xT() * this.xS() - 1.0, this.l - 1.0F, 0.5, n3);
         int n5 = 0;

         for (Enum enumValue : (Enum[])((Enum)this.setting.atx()).getDeclaringClass().getEnumConstants()) {
            String temp = vf(DirectionUtils.apX(enumValue.name()));
            float temp2 = f + UI.atK().atU();
            float temp3 = f2 + (this.m + 2.0F) + n5 * (RenderUtils.acG() + 1);
            int n6 = n5 == this.b_ ? n4 : -5592406;
            float f4 = temp3;
            float f5 = temp2;
            String str3 = temp;
            this.ve(context, str3, f5, f4, n6);
            n5++;
         }

         this.drawMatrixHelper.anR(this.xT() * this.xS());
         ((CategoryPanel)this.renderHelper2).bE(this.xT() * this.xS() * this.drawMatrixHelper.anT());
         this.vG();
      }
   }

   @Override
   public void UU(int n, int n2, int n3) {
      if (this.vC(n, n2)) {
         Enum enumValue = (Enum)this.setting.atx();
         Stream temp = Arrays.stream((Enum[])enumValue.getDeclaringClass().getEnumConstants());
         Function function2 = new Function() {
            @Override
            public Object apply(Object n) {
               return ((Enum)n).name();
            }
         };
         temp = temp.map(function2);
         IntFunction intFunction = SoundEventHelper::xV;
         String[] strs = (String[])temp.toArray(intFunction);
         int n4 = 0;

         for (Enum enumValue2 : (Enum[])enumValue.getDeclaringClass().getEnumConstants()) {
            if (enumValue.equals(enumValue2)) {
               this.b_ = n4;
            }

            n4++;
         }

         if (n3 == 0) {
            this.b_ = this.b_ + 1 > strs.length - 1 ? 0 : this.b_ + 1;
            Setting setting2 = this.setting;
            Class temp2 = enumValue.getDeclaringClass();
            String str = strs[this.b_];
            setting2.aty(Enum.valueOf(temp2, str));
         } else if (n3 == 1) {
            this.c = strs.length;
            this.d = !this.d;
         }

         if (UI.atK().auc()) {
            Managers.soundManager.jg(SoundManager.a);
         }
      } else if (this.d && this.vD(n, n2, this.j, this.k + this.m, this.l, this.xT() - 1.0F)) {
         if (n3 == 0) {
            Enum enumValue3 = (Enum)this.setting.atx();
            Stream stream2 = Arrays.stream((Enum[])enumValue3.getDeclaringClass().getEnumConstants());
            Function function3 = new Function() {
               @Override
               public Object apply(Object n) {
                  return ((Enum)n).name();
               }
            };
            stream2 = stream2.map(function3);
            IntFunction intFunction2 = SoundEventHelper::xU;
            String[] strs2 = (String[])stream2.toArray(intFunction2);
            int n5 = 0;

            for (String str2 : strs2) {
               if (this.vD(n, n2, this.j, this.k + this.m + n5 * (RenderUtils.acG() + 1), this.l, RenderUtils.acG() + 1)) {
                  this.b_ = n5;
                  break;
               }

               n5++;
            }

            Setting setting3 = this.setting;
            Class classValue = enumValue3.getDeclaringClass();
            String str3 = strs2[this.b_];
            setting3.aty(Enum.valueOf(classValue, str3));
         }

         if (UI.atK().auc()) {
            Managers.soundManager.jg(SoundManager.a);
         }
      }
   }

   public float xS() {
      return (float)this.unidentifiedHelper7.qW();
   }

   public float xT() {
      return this.e;
   }

   @Override
   public void UV() {
   }

   @Override
   public void UW(int n) {
   }

   @Override
   public void UX(char c2) {
   }

   private static String[] xU(int n) {
      return new String[n];
   }

   private static String[] xV(int n) {
      return new String[n];
   }
}
