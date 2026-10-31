package pw.hachimi.client.config;

import com.google.gson.JsonObject;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;

public class Vec3dConfigContainer extends ConfigContainer {
   private final String text;
   private final int b;
   private final Stopwatch stopwatch;
   double d;
   double e;
   double f;
   public static boolean fz;

   public Vec3dConfigContainer(String str, String str2, int n, double d2, double d3, short s, int n2, int n3, double d4) {
      super(str);
      this.b = n;
      this.text = str2;
      this.d = d2;
      this.e = d3;
      this.f = d4;
      this.stopwatch = new StopwatchImpl();
   }

   private boolean xL(long l) {
      return this.stopwatch.afq(l);
   }

   public String getText() {
      return this.text;
   }

   @Override
   public JsonObject LT(int n, int n2, char c) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("tag", this.getText2());
      jsonObject.addProperty("ip", this.getText());
      jsonObject.addProperty("dimension", this.xR());
      jsonObject.addProperty("x", this.xN());
      jsonObject.addProperty("y", this.xO());
      jsonObject.addProperty("z", this.xP());
      return jsonObject;
   }

   public double xN() {
      return this.d;
   }

   public double xO() {
      return this.e;
   }

   public double xP() {
      return this.f;
   }

   public Vec3d xQ() {
      return new Vec3d(this.xN(), this.xO(), this.xP());
   }

   public int xR() {
      return this.b;
   }
}
