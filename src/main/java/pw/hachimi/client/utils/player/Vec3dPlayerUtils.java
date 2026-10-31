package pw.hachimi.client.utils.player;

import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;
import pw.hachimi.client.model.world.SetPositionVanillaMode;
import pw.hachimi.client.utils.world.Vec3dLivingUtils;

public class Vec3dPlayerUtils {
   public static void dZ(Vec3d vec, SetPositionVanillaMode setPositionVanillaMode) {
      switch ((SetPositionVanillaMode)setPositionVanillaMode) {
         case SetPosition:
            ClientPlayNetworkHandler network2 = Mc.client.player.networkHandler;
            PositionAndOnGround positionAndOnGround = a(vec.getX(), vec.getY(), vec.getZ());
            network2.sendPacket((Packet)positionAndOnGround);
            Mc.client.player.setPosition(vec);
            ClientPlayerEntity player2 = Mc.client.player;
            double d = vec.getX();
            player2.lastX = d;
            ClientPlayerEntity player3 = Mc.client.player;
            double d2 = vec.getY();
            player3.lastBaseY = d2;
            ClientPlayerEntity player4 = Mc.client.player;
            double d3 = vec.getZ();
            player4.lastZ = d3;
            break;
         case Vanilla:
            ea(vec);
            Mc.client.player.setPosition(vec);
            ClientPlayerEntity player5 = Mc.client.player;
            double d4 = vec.getX();
            player5.lastX = d4;
            player5 = Mc.client.player;
            double d5 = vec.getY();
            player5.lastBaseY = d5;
            player5 = Mc.client.player;
            double d6 = vec.getZ();
            player5.lastZ = d6;
            break;
         case Path:
            ArrayList temp = Vec3dLivingUtils.qQ(Mc.client.player.getPos(), vec);
            Consumer consumer = new Consumer() {
               @Override
               public void accept(Object setPositionVanillaMode) {
                  Vec3dPlayerUtils.eb((Vec3d)setPositionVanillaMode);
               }
            };
            temp.forEach(consumer);
      }
   }

   public static void ea(Vec3d vec) {
      int n = (int)Math.ceil(Mc.client.player.getPos().distanceTo(vec) / 10.0) - 1;

      for (int n2 = 0; n2 < n; n2++) {
         ClientPlayNetworkHandler temp = Mc.client.getNetworkHandler();
         OnGroundOnly onGroundOnly = a();
         temp.sendPacket((Packet)onGroundOnly);
      }

      ClientPlayNetworkHandler network2 = Mc.client.getNetworkHandler();
      PositionAndOnGround positionAndOnGround = a(vec.x, vec.y, vec.z);
      network2.sendPacket((Packet)positionAndOnGround);
   }

   static void eb(Vec3d vec) {
      ClientPlayNetworkHandler temp = Mc.client.player.networkHandler;
      PositionAndOnGround positionAndOnGround = a(vec.getX(), vec.getY(), vec.getZ());
      temp.sendPacket((Packet)positionAndOnGround);
      Mc.client.player.setPosition(vec);
      ClientPlayerEntity player2 = Mc.client.player;
      double d = vec.getX();
      player2.lastX = d;
      ClientPlayerEntity player3 = Mc.client.player;
      double d2 = vec.getY();
      player3.lastBaseY = d2;
      ClientPlayerEntity player4 = Mc.client.player;
      double d3 = vec.getZ();
      player4.lastZ = d3;
   }

   private static OnGroundOnly a() {
      return new OnGroundOnly(true);
   }

   private static PositionAndOnGround a(double d, double d2, double d3) {
      return new PositionAndOnGround(d, d2, d3, true);
   }
}
