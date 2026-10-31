package pw.hachimi.client.utils.player;

import net.minecraft.item.ItemStack;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;

public class InventoryTrackerStackHelper {
   private final ItemStack[] stacks;
   private final int b;
   private final int c;
   private final Stopwatch stopwatch = new StopwatchImpl();
   public static boolean a;

   public InventoryTrackerStackHelper(ItemStack[] stacks2, int n, int n2) {
      this.stacks = stacks2;
      this.b = n;
      this.c = n2;
   }

   public void QV(int n, short s, int n2) {
      long temp = ((long)n << 32 | (long)s << 48 >>> 32 | (long)n2 << 48 >>> 48) ^ 96190761548628L ^ 76836392300344L;
      int n3 = (int)((((long)n << 32 | (long)s << 48 >>> 32 | (long)n2 << 48 >>> 48) ^ 96190761548628L ^ 76836392300344L) >>> 32);
      int n4 = (int)((((long)n << 32 | (long)s << 48 >>> 32 | (long)n2 << 48 >>> 48) ^ 96190761548628L ^ 76836392300344L) << 32 >>> 40);
      int n5 = (int)(temp << 56 >>> 56);
      this.stopwatch.afr(n3, n4, (byte)n5);
   }

   public boolean QW() {
      return this.stopwatch.afq(300);
   }

   public ItemStack QX(int n) {
      return this.stacks[n];
   }

   public int QY() {
      return this.b;
   }

   public int QZ() {
      return this.c;
   }
}
