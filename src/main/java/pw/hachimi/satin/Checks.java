package pw.hachimi.satin;

import java.lang.management.ManagementFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Checks {
   public static void warn() {
      JFrame jFrame = new JFrame();
      jFrame.setAlwaysOnTop(true);
      JOptionPane.showMessageDialog(
         jFrame, "请购买BETA版\nThis is earlyacess build.\nYou do not have permissions to access.", "Warning", 2, UIManager.getIcon("OptionPane.warningIcon")
      );
      jFrame.dispose();
   }

   public static void check() {
      boolean flag = false;

      for (String str : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
         if (Integer.valueOf("-noverify".hashCode()).equals(str.hashCode())
            || Integer.valueOf("-Xverify:none".hashCode()).equals(str.hashCode())
            || Integer.valueOf("-Xverify:false".hashCode()).equals(str.hashCode())) {
            flag = true;
            break;
         }
      }

      if (!flag) {
         JFrame jFrame = new JFrame();
         jFrame.setAlwaysOnTop(true);
         JOptionPane.showMessageDialog(
            jFrame, "你没有添加虚拟机参数 -noverify!\nYou did not add the JVM parameter -noverify", "Warning", 2, UIManager.getIcon("OptionPane.warningIcon")
         );
         jFrame.dispose();
      }
   }
}
