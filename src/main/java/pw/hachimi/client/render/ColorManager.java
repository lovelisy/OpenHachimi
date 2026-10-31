package pw.hachimi.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.awt.Color;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.utils.render.RenderUtils2;

public class ColorManager {
   public static ColorManager colorManager;
   private final int b;
   private final Object2IntMap object2IntMap;

   public ColorManager(String str, String str2) {
      this.object2IntMap = new Object2IntOpenHashMap();
      int n = RenderUtils2.II(35633);
      RenderUtils2.IJ(n, this.ajA(str));
      String str3 = RenderUtils2.IK(n);
      if (str3 != null) {
         Bootstrap.Ob("Failed to compile vertex shader (" + str + "): " + str3);
         throw new RuntimeException("Failed to compile vertex shader (" + str + "): " + str3);
      }

      int n2 = RenderUtils2.II(35632);
      RenderUtils2.IJ(n2, this.ajA(str2));
      String str4 = RenderUtils2.IK(n2);
      if (str4 != null) {
         Bootstrap.Ob("Failed to compile fragment shader (" + str2 + "): " + str4);
         throw new RuntimeException("Failed to compile fragment shader (" + str2 + "): " + str4);
      }

      this.b = RenderUtils2.IL();
      String str5 = RenderUtils2.IM(this.b, n, n2);
      if (str5 != null) {
         Bootstrap.Ob("Failed to link program: " + str5);
         throw new RuntimeException("Failed to link program: " + str5);
      }

      RenderUtils2.Iw(n);
      RenderUtils2.Iw(n2);
   }

   private String ajA(String str) {
      try {
         return IOUtils.toString(Bootstrap.Od("assets/hachimiclient/shaders/" + str), StandardCharsets.UTF_8);
      } catch (Exception exception) {
         throw new IllegalStateException("Could not read shader '" + str + "'", exception);
      }
   }

   public void ajB() {
      RenderUtils2.IN(this.b);
      colorManager = this;
   }

   private int ajC(String str) {
      if (this.object2IntMap.containsKey(str)) {
         return this.object2IntMap.getInt(str);
      }

      int n = RenderUtils2.IP(this.b, str);
      this.object2IntMap.put(str, n);
      return n;
   }

   public void ajD(String str, boolean flag) {
      RenderUtils2.IQ(this.ajC(str), flag ? 1 : 0);
   }

   public void ajE(String str) {
      RenderUtils2.IQ(this.ajC(str), 0);
   }

   public void ajF(String str, double d) {
      RenderUtils2.IR(this.ajC(str), (float)d);
   }

   public void ajG(String str, double d, double d2) {
      RenderUtils2.IS(this.ajC(str), (float)d, (float)d2);
   }

   public void ajH(String str, Color color) {
      RenderUtils2.IU(this.ajC(str), color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
   }

   public void ajI(String str, Matrix4f matrix4f) {
      RenderUtils2.IW(this.ajC(str), matrix4f);
   }

   public void ajJ() {
      this.ajI("u_Proj", RenderSystem.getProjectionMatrix());
      this.ajI("u_ModelView", RenderSystem.getModelViewStack());
   }
}
