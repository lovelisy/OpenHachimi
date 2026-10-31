package pw.hachimi.client.module;

import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.utils.movement.UnidentifiedHelper21;

public class Module5 extends ClientModule {
   private final int bt;
   private static int[] a;

   public Module5(String str, String str2, ModuleCategory moduleCategory) {
      super(str, str2, moduleCategory);
      this.bt = 100;
   }

   public Module5(String str, String str2, ModuleCategory moduleCategory, int n) {
      super(str, str2, moduleCategory);
      this.bt = n;
   }

   public void ajM(float f2, float f3) {
      Managers.movementTracker.Mh(f(this.ajP(), f2, f3));
   }

   public void ajN(float f2, float f3) {
      Managers.movementTracker.Mi(f2, f3);
   }

   public boolean ajO() {
      return Managers.movementTracker.Ml(this.ajP());
   }

   protected int ajP() {
      return this.bt;
   }

   private static UnidentifiedHelper21 f(int n, float f2, float f3) {
      return new UnidentifiedHelper21(n, f2, f3);
   }

   public static void b(int[] ns) {
      a = ns;
   }

   public static int[] b_1() {
      return a;
   }
}
