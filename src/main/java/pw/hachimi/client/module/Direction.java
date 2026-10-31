package pw.hachimi.client.module;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Direction.AxisDirection;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.world.DirectionUtils;

public class Direction extends DrawRenderModule {
   public Direction() {
      super("Direction", "");
   }

   Setting postEffect = this.registerSetting(new BooleanSetting("Rotation", "Displays player yaw and pitch", false));

   @Override
   public void adb() {
      net.minecraft.util.math.Direction direction = client.player.getHorizontalFacing();
      String str = DirectionUtils.apY(direction);
      String str2 = DirectionUtils.apZ(direction.getAxis());
      String str3;
      Direction temp;
      String temp2;
      Object[] temp3;
      Object[] temp4;
      byte temp5;
      String temp6;
      if (direction.getDirection() == AxisDirection.POSITIVE) {
         Object[] objs = new Object[]{(int)MathHelper.wrapDegrees(Managers.movementTracker.Mm())};
         str3 = String.format(", %s", objs);
         temp = this;
         temp2 = "%s §7[§f%s%s%s§7]";
         temp3 = new Object[]{str, str2, null, null};
         temp4 = temp3;
         temp5 = 2;
         temp6 = "+";
      } else {
         Object[] objs2 = new Object[]{(int)MathHelper.wrapDegrees(Managers.movementTracker.Mm())};
         str3 = String.format(", %s", objs2);
         temp = this;
         temp2 = "%s §7[§f%s%s%s§7]";
         temp3 = new Object[]{str, str2, null, null};
         temp4 = temp3;
         temp5 = 2;
         temp6 = "-";
      }

      temp4[temp5] = temp6;
      temp3[3] = (Boolean)this.postEffect.atx() ? str3 : "";
      Object[] objs3 = temp3;
      temp.text = String.format(temp2, objs3);
      super.adb();
   }
}
