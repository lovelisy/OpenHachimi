package pw.hachimi.client.setting;

import com.google.gson.JsonObject;
import java.awt.Color;
import java.util.function.Supplier;
import pw.hachimi.client.module.ColorHelper3;

public class ColorSetting extends Setting {
   private final boolean a;
   private boolean b;

   public ColorSetting(String str, String str2, Color color, boolean flag, boolean flag2) {
      super(str, str2, color);
      this.a = flag;
      this.TJ(flag2);
   }

   public ColorSetting(String str, String str2, Color color) {
      this(str, str2, color, true, true);
   }

   public ColorSetting(String str, String str2, Color color, boolean flag, boolean flag2, Supplier supplier) {
      super(str, str2, color, supplier);
      this.a = flag;
      this.TJ(flag2);
   }

   public ColorSetting(String str, String str2, Color color, boolean flag, Supplier supplier) {
      this(str, str2, color, flag, true, supplier);
   }

   public ColorSetting(String str, String str2, Color color, Supplier supplier) {
      this(str, str2, color, true, supplier);
   }

   public ColorSetting() {
      this("Color", "", new Color(-1, false), false, true);
   }

   public ColorSetting(String str, String str2, Integer integer) {
      this();
   }

   // restored with Procyon (compiles, call/field/constant fingerprint equals the deobf jar)
   public Color Tx() {
       if (ColorHelper3.auO() != null && this.b) {
           return ColorHelper3.auO().auW(this.TG());
       }
       final int red = ((Color)this.h).getRed();
       final int green = ((Color)this.h).getGreen();
       final int blue = ((Color)this.h).getBlue();
       final boolean a = this.a;
       final int n = blue;
       final int n2 = green;
       final int n3 = red;
       int alpha;
       int b;
       int g;
       int r;
       if (a) {
           final int n4 = n3;
           final int n5 = n2;
           final int n6 = n;
           alpha = ((Color)this.h).getAlpha();
           b = n6;
           g = n5;
           r = n4;
       }
       else {
           final int n7 = n3;
           final int n8 = n2;
           final int n9 = n;
           alpha = 255;
           b = n9;
           g = n8;
           r = n7;
       }
       return new Color(r, g, b, alpha);
   }

   public void Ty(int n10) {
      boolean flag;
      if ((n10 & 0xFF000000) != -16777216) {
         flag = true;
      } else {
         flag = false;
      }

      Color color = new Color(n10, flag);
      this.aty(color);
   }

   public Color Tz(int n10) {
      return ColorHelper3.auO() != null && this.b
         ? ColorHelper3.auO().auW(n10)
         : new Color(((Color)this.h).getRed(), ((Color)this.h).getGreen(), ((Color)this.h).getBlue(), n10);
   }

   public int TA() {
      return this.Tx().getRGB();
   }

   public int TB(int n10) {
      return this.Tz(n10).getRGB();
   }

   public int TC() {
      return ((Color)this.h).getRed();
   }

   public int TD() {
      return ((Color)this.h).getGreen();
   }

   public int TE() {
      return ((Color)this.h).getBlue();
   }

   public boolean TF() {
      return this.a;
   }

   public int TG() {
      int n10;
      if (this.a) {
         n10 = ((Color)this.h).getAlpha();
      } else {
         n10 = 255;
      }

      return n10;
   }

   public float[] TH() {
      float[] fs = Color.RGBtoHSB(((Color)this.h).getRed(), ((Color)this.h).getGreen(), ((Color)this.h).getBlue(), (float[])null);
      float[] temp = new float[]{fs[0], fs[1], fs[2], 0.0F};
      boolean flag = this.a;
      float[] fs2 = temp;
      float[] fs3 = temp;
      float f;
      if (flag) {
         temp = fs3;
         float[] temp2 = fs2;
         f = ((Color)this.h).getAlpha() / 255.0F;
         fs2 = temp2;
         fs3 = temp;
      } else {
         temp = fs3;
         float[] fs4 = fs2;
         f = 1.0F;
         fs2 = fs4;
         fs3 = temp;
      }

      temp = fs3;
      fs2[3] = f;
      return temp;
   }

   public boolean TI() {
      return this.b;
   }

   public void TJ(boolean flag) {
      this.b = flag;
      if (ColorHelper3.auO() != null && flag) {
         this.aty(ColorHelper3.auO().auW(this.TG()));
      }
   }

   @Override
   public JsonObject LT(int n10, int n11, char c) {
      JsonObject jsonObject = super.LT(25681, 44222, '\u218e');
      jsonObject.addProperty("value", Integer.toHexString(this.TA()));
      jsonObject.addProperty("global", this.b);
      return jsonObject;
   }

   public Color TK(JsonObject jsonObject) {
      if (!jsonObject.has("value")) {
         return null;
      }

      String str = jsonObject.get("value").getAsString();
      if (jsonObject.has("global")) {
         this.TJ(jsonObject.get("global").getAsBoolean());
      }

      return this.TL(str);
   }

   public Color TL(String str) {
      return new Color((int)Long.parseLong(str, 16), true);
   }

   @Override
   public Object atx() {
      return this.Tx();
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.TK(jsonObject);
   }
}
