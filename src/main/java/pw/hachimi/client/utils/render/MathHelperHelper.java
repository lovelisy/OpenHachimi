package pw.hachimi.client.utils.render;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.ArrayUtils;
import pw.hachimi.client.gui.CategoryPanel;
import pw.hachimi.client.gui.DrawScreenScreen;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.manager.SoundManager;
import pw.hachimi.client.module.client.UI;
import pw.hachimi.client.render.DrawBase;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;

public class MathHelperHelper extends DrawBase {
   private final int a;
   private final Stopwatch stopwatch = new StopwatchImpl();
   private char[] c;
   private boolean d;
   private boolean e;

   public MathHelperHelper(CategoryPanel categoryPanel, DrawMatrixHelper drawMatrixHelper, Setting setting2, float f, float f2) {
      super(categoryPanel, drawMatrixHelper, setting2, f, f2);
      this.a = ((NumberSetting)setting2).abo();
      if (setting2.atx() instanceof Integer) {
         this.c = String.valueOf(((Number)setting2.atx()).intValue()).toCharArray();
      } else if (setting2.atx() instanceof Float) {
         this.c = String.valueOf(((Number)setting2.atx()).floatValue()).toCharArray();
      } else if (setting2.atx() instanceof Double) {
         this.c = String.valueOf(((Number)setting2.atx()).doubleValue()).toCharArray();
      }
   }

   @Override
   public void Cs(DrawContext context, float f, float f2, int n, int n2) {
      this.vy(RenderUtils.acG() + UI.atK().atT());
      this.j = f;
      this.k = f2;
      boolean flag = this.vC(n, n2);
      Number number = ((NumberSetting)this.setting).abk();
      Number number2 = ((NumberSetting)this.setting).abl();
      if (DrawScreenScreen.colorBase == this) {
         float f3 = (n - f) / this.l;
         if (this.setting.atx() instanceof Integer) {
            int n3 = (int)MathHelper.clamp(number.floatValue() + f3 * (number2.intValue() - number.intValue()), number.intValue(), number2.intValue());
            ((NumberSetting)this.setting).abr(n3);
         } else if (this.setting.atx() instanceof Float) {
            float f4 = MathHelper.clamp(number.floatValue() + f3 * (number2.floatValue() - number.floatValue()), number.floatValue(), number2.floatValue());
            BigDecimal temp = new BigDecimal(f4);
            RoundingMode roundingMode = RoundingMode.HALF_UP;
            int n4 = this.a;
            f4 = temp.setScale(n4, roundingMode).floatValue();
            ((NumberSetting)this.setting).abr(f4);
         } else if (this.setting.atx() instanceof Double) {
            double d2 = MathHelper.clamp(number.doubleValue() + f3 * (number2.doubleValue() - number.doubleValue()), number.doubleValue(), number2.doubleValue());
            BigDecimal bigDecimal = new BigDecimal(d2);
            RoundingMode roundingMode2 = RoundingMode.HALF_UP;
            int n5 = this.a;
            d2 = bigDecimal.setScale(n5, roundingMode2).doubleValue();
            ((NumberSetting)this.setting).abr(d2);
         }

         float f5 = MathHelper.ceil(this.j + 1.0F);
         float f6 = MathHelper.floor(this.j + this.l - 1.0F);
         if (n < f5) {
            this.setting.aty(number);
         } else if (n >= f6) {
            this.setting.aty(number2);
         }
      }

      if (!this.d) {
         if (this.setting.atx() instanceof Integer) {
            this.c = String.valueOf(((Number)this.setting.atx()).intValue()).toCharArray();
         } else if (this.setting.atx() instanceof Float) {
            this.c = String.valueOf(((Number)this.setting.atx()).floatValue()).toCharArray();
         } else if (this.setting.atx() instanceof Double) {
            this.c = String.valueOf(((Number)this.setting.atx()).doubleValue()).toCharArray();
         }

         float f7 = (((Number)this.setting.atx()).floatValue() - number.floatValue()) / (number2.floatValue() - number.floatValue());
         vh(context, f, f2, f7 * this.l, this.m, this.jj(true, flag).getRGB());
      }

      int n6 = UI.auf() ? -16777216 : -1;
      String temp2 = this.d ? new String(this.c) + this.nV() : vf(this.setting.atu());
      float temp3 = f + 2.0F;
      float temp4 = f2 + UI.atK().atU();
      int n7 = n6;
      float f8 = temp4;
      float f9 = temp3;
      String str = temp2;
      this.ve(context, str, f9, f8, n7);
      if (!this.d) {
         String str2 = String.valueOf(this.setting.atx());
         this.ve(context, str2, f + this.l - 2.0F - RenderUtils.acF(str2), f2 + UI.atK().atU(), -5592406);
      }
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   @Override
   public void UU(int n, int n2, int n3) {
      if (this.vC(n, n2)) {
         if (n3 != 1) {
            if (n3 == 0) {
               DrawScreenScreen.colorBase = this;
            }
         } else if (this.d) {
            String str = new String(this.c);
            MathHelperHelper temp = this;

            label52: {
               try {
                  if (temp.setting.atx() instanceof Integer) {
                     ((NumberSetting)this.setting).abr(Integer.parseInt(str));
                     break label52;
                  }
               } catch (NumberFormatException numberFormatException) {
                  break label52;
               }

               temp = this;

               try {
                  if (temp.setting.atx() instanceof Float) {
                     ((NumberSetting)this.setting).abr(Float.parseFloat(str));
                  } else if (this.setting.atx() instanceof Double) {
                     ((NumberSetting)this.setting).abr(Double.parseDouble(str));
                  }
               } catch (NumberFormatException numberFormatException2) {
               }
            }

            this.d = false;
         } else {
            this.d = true;
         }

         if (UI.atK().auc()) {
            Managers.soundManager.jg(SoundManager.a);
         }
      } else {
         this.d = false;
      }
   }

   @Override
   public void UV() {
   }

   // $VF: Duplicated exception handlers to handle obfuscated exceptions
   @Override
   public void UW(int n) {
      if (this.d) {
         switch (n) {
            case 256:
               if (this.setting.atx() instanceof Integer) {
                  this.c = String.valueOf(((Number)this.setting.atx()).intValue()).toCharArray();
               } else if (this.setting.atx() instanceof Float) {
                  this.c = String.valueOf(((Number)this.setting.atx()).floatValue()).toCharArray();
               } else if (this.setting.atx() instanceof Double) {
                  this.c = String.valueOf(((Number)this.setting.atx()).doubleValue()).toCharArray();
               }

               this.d = false;
               break;
            case 257:
               String str = new String(this.c);
               MathHelperHelper temp = this;

               label62: {
                  try {
                     if (temp.setting.atx() instanceof Integer) {
                        ((NumberSetting)this.setting).abr(Integer.parseInt(str));
                        break label62;
                     }
                  } catch (NumberFormatException numberFormatException) {
                     break label62;
                  }

                  temp = this;

                  try {
                     if (temp.setting.atx() instanceof Float) {
                        ((NumberSetting)this.setting).abr(Float.parseFloat(str));
                     } else if (this.setting.atx() instanceof Double) {
                        ((NumberSetting)this.setting).abr(Double.parseDouble(str));
                     }
                  } catch (NumberFormatException numberFormatException2) {
                  }
               }

               if (this.setting.atx() instanceof Integer) {
                  this.c = String.valueOf(((Number)this.setting.atx()).intValue()).toCharArray();
               } else if (this.setting.atx() instanceof Float) {
                  this.c = String.valueOf(((Number)this.setting.atx()).floatValue()).toCharArray();
               } else if (this.setting.atx() instanceof Double) {
                  this.c = String.valueOf(((Number)this.setting.atx()).doubleValue()).toCharArray();
               }

               this.d = false;
            case 258:
            default:
               break;
            case 259:
               if (this.c.length != 0) {
                  this.c = ArrayUtils.remove(this.c, this.c.length - 1);
               }
         }
      }
   }

   @Override
   public void UX(char c2) {
      if (this.d && (Character.isDigit(c2) || c2 == '.' || c2 == '-')) {
         this.c = ArrayUtils.add(this.c, c2);
      }
   }

   public String nV() {
      if (this.stopwatch.afq(250)) {
         this.e = !this.e;
         this.stopwatch.afr(29432, 5265492, (byte)40);
      }

      return this.e && this.d ? "_" : "";
   }

   public void nW() {
      this.d = false;
   }

   public boolean nX() {
      return this.d;
   }
}
