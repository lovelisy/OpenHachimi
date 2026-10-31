package pw.hachimi.client.model.player;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import pw.hachimi.client.Mc;

public enum SwingMode {
   Normal {
      public static boolean p;

      @Override
      public void avX(Hand var1) {
         Mc.client.player.swingHand(var1);
      }
   },
   Packet {
      public static boolean bj;

      @Override
      public void avX(Hand var1) {
         ClientPlayNetworkHandler var10000 = Mc.client.getNetworkHandler();
         HandSwingC2SPacket var4 = new HandSwingC2SPacket(var1);
         var10000.sendPacket((Packet)var4);
      }
   },
   Render {
      @Override
      public void avX(Hand var1) {
         Mc.client.player.swingHand(var1, false);
      }
   },
   None {
      public static boolean a;

      @Override
      public void avX(Hand var1) {
      }
   };

   private static final SwingMode[] e = avY();

   public abstract void avX(Hand var1);

   private static SwingMode[] avY() {
      return new SwingMode[]{Normal, Packet, Render, None};
   }
}
