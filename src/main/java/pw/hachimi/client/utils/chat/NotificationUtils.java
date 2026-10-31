package pw.hachimi.client.utils.chat;

import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;

public class NotificationUtils {
   public final Stopwatch stopwatch;
   public final String text;
   public final int c;
   public double d;
   public boolean e;
   public boolean f;
   public static boolean a;

   public NotificationUtils(String str, int n) {
      this.stopwatch = new StopwatchImpl();
      this.text = str;
      this.c = n;
   }
}
