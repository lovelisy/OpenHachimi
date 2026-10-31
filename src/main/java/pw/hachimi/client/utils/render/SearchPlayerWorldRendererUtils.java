package pw.hachimi.client.utils.render;

import pw.hachimi.client.Mc;

public class SearchPlayerWorldRendererUtils {
   public static boolean a;

   public static void UA(boolean flag) {
      if (flag) {
         int n = (int)Mc.client.player.getX();
         int n2 = (int)Mc.client.player.getY();
         int n3 = (int)Mc.client.player.getZ();
         int n4 = (Integer)Mc.client.options.getViewDistance().getValue() * 16;
         int temp = n - n4;
         int temp2 = n2 - n4;
         int temp3 = n3 - n4;
         int temp4 = n + n4;
         int temp5 = n2 + n4;
         int n5 = n3 + n4;
         int n6 = temp5;
         int n7 = temp4;
         int n8 = temp3;
         int n9 = temp2;
         int n10 = temp;
         Mc.client.worldRenderer.scheduleBlockRenders(n10, n9, n8, n7, n6, n5);
      } else {
         Mc.client.worldRenderer.reload();
      }
   }
}
