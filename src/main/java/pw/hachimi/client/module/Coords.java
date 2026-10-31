package pw.hachimi.client.module;

import net.minecraft.world.World;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;

public class Coords extends DrawRenderModule {
   public Coords() {
      super("Coords", "");
   }

   Setting postEffect = this.registerSetting(new BooleanSetting("NetherCoords", "Displays nether coordinates", true));

   @Override
   public void adb() {
      double d = client.player.getX();
      double d2 = client.player.getY();
      double d3 = client.player.getZ();
      Coords temp;
      String temp2;
      Object[] temp3;
      Object[] temp4;
      byte temp5;
      String temp6;
      if (client.world.getRegistryKey() == World.NETHER) {
         temp = this;
         String str = (Boolean)this.postEffect.atx() ? "§7[§f%s, %s§7]" : "";
         temp2 = "XYZ §f%s, %s, %s " + str;
         temp3 = new Object[]{bo.format(d), bo.format(d2), bo.format(d3), null, null};
         double d4 = d * 8.0;
         temp3[3] = bo.format(d4);
         temp4 = temp3;
         temp5 = 4;
         double d5 = d3 * 8.0;
         temp6 = bo.format(d5);
      } else {
         temp = this;
         String str2 = (Boolean)this.postEffect.atx() ? "§7[§f%s, %s§7]" : "";
         temp2 = "XYZ §f%s, %s, %s " + str2;
         temp3 = new Object[]{bo.format(d), bo.format(d2), bo.format(d3), null, null};
         double d6 = d / 8.0;
         temp3[3] = bo.format(d6);
         temp4 = temp3;
         temp5 = 4;
         double d7 = d3 / 8.0;
         temp6 = bo.format(d7);
      }

      temp4[temp5] = temp6;
      Object[] objs = temp3;
      temp.text = String.format(temp2, objs);
      super.adb();
   }
}
