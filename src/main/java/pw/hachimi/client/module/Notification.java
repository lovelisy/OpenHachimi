package pw.hachimi.client.module;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import pw.hachimi.client.gui.DrawBufferScreen;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.chat.NotificationLeftRightMode;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.LeftMiddleMode;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.chat.NotificationUtils;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.UnidentifiedHelper9;

public class Notification extends RenderModule {
   public static Notification INSTANCE;
   private final List list = new CopyOnWriteArrayList();
   UnidentifiedHelper9 unidentifiedHelper9_1 = new UnidentifiedHelper9();
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("OnChat", "Client messages", true));
   Setting leftRightMode = this.registerSetting(new EnumSetting("X-Location", "", NotificationLeftRightMode.Auto, NotificationLeftRightMode.values()));
   double e = 300.0;
   double f = 200.0;
   double g = 2000.0;
   float h;
   float i;
   Easing easing = Easing.CUBIC_IN_OUT;

   public Notification() {
      super("Notification", "Notification");
      INSTANCE = this;
      this.awb();
      this.numberSetting1.aty(0.0F);
      this.numberSetting2.aty(10.0F);
      this.leftMiddleMode.aty(LeftMiddleMode.MIDDLE);
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   public void ZS(String str, int n) {
      if (this.awg()) {
         if (n != 0) {
            Iterator iterator2 = this.list.iterator();

            while (iterator2.hasNext()) {
               NotificationUtils notificationUtils = (NotificationUtils)iterator2.next();
               if (notificationUtils.c == n) {
                  notificationUtils.f = true;
               }
            }
         }

         List temp = this.list;
         NotificationUtils notificationUtils2 = new NotificationUtils(str, n);
         temp.add(notificationUtils2);
      }
   }

   public boolean ZT() {
      return (Boolean)this.booleanSetting1_1.atx();
   }

   @Override
   public void adb() {
      this.i = 0.0F;
      this.h = 0.0F;
      int n = RenderUtils.acG() + 1;
      List temp = this.list;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return Notification.ZV((NotificationUtils)obj);
         }
      };
      temp.removeIf(predicate);
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         NotificationUtils notificationUtils = (NotificationUtils)iterator2.next();
         if (notificationUtils.stopwatch.afq(this.g) || !notificationUtils.e && notificationUtils.f) {
            notificationUtils.e = true;
            notificationUtils.stopwatch.afr(29432, 5265492, (byte)40);
         }

         if (notificationUtils.e) {
            notificationUtils.d = 1.0 - this.easing.Yy(Math.min(notificationUtils.stopwatch.afs() / this.f, 1.0));
         } else {
            notificationUtils.d = this.easing.Yy(Math.min(notificationUtils.stopwatch.afs() / this.e, 1.0));
         }

         if (!(notificationUtils.d < 0.03)) {
            this.h = Math.max(RenderUtils.acF(notificationUtils.text), this.h);
            this.i = this.i + (float)(notificationUtils.d * n);
         }
      }
   }

   @Override
   public void adn(DrawContext context) {
      MatrixStack temp = context.getMatrices();
      float temp2 = this.add();
      float temp3 = this.ade();
      float temp4 = this.adl();
      float temp5 = this.adm();
      float temp6 = (Float)this.numberSetting3.atx();
      Color temp7 = (Color)this.colorSetting1.atx();
      double temp8 = ((Color)this.colorSetting1.atx()).getAlpha();
      UnidentifiedHelper9 temp9 = this.unidentifiedHelper9_1;
      double temp10 = this.i >= RenderUtils.acG() ? 1.0 : 0.0;
      Easing easing2 = Easing.BACK_OUT;
      RenderUtils.acH(temp, temp2, temp3, temp4, temp5, temp6, ColorUtils.iE(temp7, (int)(temp8 * temp9.MH(temp10, 300L, easing2))));
   }

   @Override
   public float adl() {
      return this.h < 5.0F && client.currentScreen instanceof DrawBufferScreen ? 5.0F : this.h + super.adl();
   }

   @Override
   public float adm() {
      return this.i < 5.0F && client.currentScreen instanceof DrawBufferScreen ? 5.0F : this.i + super.adm();
   }

   @Override
   public void adc(DrawContext context) {
      float f2 = this.add();
      int n = RenderUtils.acG() + 2;
      float f3 = this.ade() - n;
      List temp = this.list;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object context) {
            return Notification.ZU((NotificationUtils)context);
         }
      };
      temp.removeIf(predicate);
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         NotificationUtils notificationUtils = (NotificationUtils)iterator2.next();
         if (notificationUtils.stopwatch.afq(this.g) || !notificationUtils.e && notificationUtils.f) {
            notificationUtils.e = true;
            notificationUtils.stopwatch.afr(29432, 5265492, (byte)40);
         }

         if (notificationUtils.e) {
            notificationUtils.d = 1.0 - this.easing.Yy(Math.min(notificationUtils.stopwatch.afs() / this.f, 1.0));
         } else {
            notificationUtils.d = this.easing.Yy(Math.min(notificationUtils.stopwatch.afs() / this.e, 1.0));
         }

         if (!(notificationUtils.d < 0.03)) {
            context.getMatrices().push();
            MatrixStack matrices = context.getMatrices();
            float f4 = f3 + n;
            matrices.translate(0.0F, f4, 0.0F);
            f3 += (float)(notificationUtils.d * n);
            float f5 = RenderUtils.acF(notificationUtils.text);
            float f6;
            if (this.leftRightMode.atx() != NotificationLeftRightMode.Right
               && (
                  this.leftRightMode.atx() != NotificationLeftRightMode.Auto
                     || this.ads() != LeftMiddleMode.RIGHT
                        && this.ads() != LeftMiddleMode.RIGHT_OFFSET
                        && (!this.adi().isEmpty() || !(f2 > client.getWindow().getScaledWidth() / 2.0))
               )) {
               f6 = this.leftRightMode.atx() != NotificationLeftRightMode.Middle
                     && (this.leftRightMode.atx() != NotificationLeftRightMode.Auto || this.ads() != LeftMiddleMode.MIDDLE)
                  ? f2
                  : f2 + this.h / 2.0F - f5 / 2.0F;
            } else {
               if (this.leftRightMode.atx() != NotificationLeftRightMode.Middle
                  && this.leftRightMode.atx() == NotificationLeftRightMode.Auto
                  && this.ads() == LeftMiddleMode.MIDDLE) {
               }

               f6 = f2 + this.h - f5;
            }

            float f7 = f6;
            this.adr(context, notificationUtils.text, f7, 0.0F, (new Color(255, 255, 255, (int)(255.0 * notificationUtils.d))).getRGB());
            context.getMatrices().pop();
         }
      }
   }

   static boolean ZU(NotificationUtils notificationUtils) {
      return notificationUtils.d == 0.0 && notificationUtils.e;
   }

   static boolean ZV(NotificationUtils notificationUtils) {
      return notificationUtils.d == 0.0 && notificationUtils.e;
   }
}
