package pw.hachimi.client.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.lwjgl.glfw.GLFW;
import pw.hachimi.client.api.UnidentifiedInterface12;
import pw.hachimi.client.api.UnidentifiedInterface8;

public class UnidentifiedHelper17 implements UnidentifiedInterface8, UnidentifiedInterface12 {
   private final String text;
   private final Runnable runnable;
   public boolean c;
   public boolean d;
   private int e;

   public UnidentifiedHelper17(String str, int n, Runnable runnable2) {
      this.text = str;
      this.e = n;
      this.runnable = runnable2;
   }

   public UnidentifiedHelper17(String str, int n, Runnable runnable2, boolean flag) {
      this(str, n, runnable2);
      this.d = flag;
   }

   public void agq() {
      this.runnable.run();
   }

   public String getText() {
      return this.text;
   }

   public Runnable getRunnable() {
      return this.runnable;
   }

   public int agt() {
      return this.e;
   }

   public void agu(int n) {
      this.e = n;
   }

   @Override
   public String Kn() {
      Object[] objs = new Object[]{this.text.toLowerCase()};
      return String.format("%s-macro", objs);
   }

   public String agv() {
      if (this.e == -1) {
         return "NONE";
      }

      String str = UnidentifiedUtils5.Xd(this.e, GLFW.glfwGetKeyScancode(this.e));
      return str != null ? str.toUpperCase() : "NONE";
   }

   @Override
   public JsonObject LT(int n, int n2, char c2) {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty("id", this.Kn());
      jsonObject.addProperty("value", this.agt());
      jsonObject.addProperty("runOnRelease", this.d);
      return jsonObject;
   }

   public UnidentifiedHelper17 agw(JsonObject jsonObject) {
      boolean flag = false;
      if (jsonObject.has("runOnRelease")) {
         flag = jsonObject.getAsJsonPrimitive("runOnRelease").getAsBoolean();
      }

      if (!jsonObject.has("value")) {
         return null;
      }

      JsonElement jsonElement = jsonObject.get("value");
      return new UnidentifiedHelper17(this.Kn(), jsonElement.getAsInt(), this.getRunnable(), flag);
   }

   public void agx() {
      this.c = true;
   }

   public void agy() {
      this.c = false;
   }

   @Override
   public Object LU(JsonObject jsonObject) {
      return this.agw(jsonObject);
   }
}
