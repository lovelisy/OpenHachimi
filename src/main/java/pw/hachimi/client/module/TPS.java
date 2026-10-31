package pw.hachimi.client.module;

import java.text.DecimalFormat;
import pw.hachimi.client.manager.Managers;

public class TPS extends DrawRenderModule {
   public TPS() {
      super("TPS", "");
   }

   private final DecimalFormat decimalFormat = new DecimalFormat("0.0#");

   @Override
   public void adb() {
      float f2 = Managers.entityManager.rX();
      float f3 = Managers.entityManager.rW();
      Object[] temp = new Object[2];
      double d = f2;
      temp[0] = this.decimalFormat.format(d) + (Managers.entityManager.rZ() ? "" : "*");
      double d2 = f3;
      temp[1] = this.decimalFormat.format(d2);
      Object[] objs = temp;
      this.text = String.format("TPS §f%s §7[§f%s§7]", objs);
      super.adb();
   }
}
