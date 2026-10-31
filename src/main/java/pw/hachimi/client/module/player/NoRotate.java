package pw.hachimi.client.module.player;

import java.util.Set;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.player.PlayerPositionLookEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.UnidentifiedHelper21;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoRotate extends ClientModule {
   Setting applyRotate = this.registerSetting(new BooleanSetting("ApplyRotate", "Apply server rotate silent", false));
   float[] b;
   public static boolean cp;

   public NoRotate() {
      super("NoRotate", "Prevents server from forcing rotations", ModuleCategory.PLAYER);
   }

   @EventListener
   public void onPlayerPositionLook(PlayerPositionLookEvent playerPositionLookEvent) {
      playerPositionLookEvent.aim();
      float f = Managers.movementTracker.Mo();
      float f2 = Managers.movementTracker.Mt();
      PlayerPositionLookS2CPacket playerPositionLookS2CPacket = playerPositionLookEvent.getPlayerPositionLookS2CPacket();
      Vec3d vec = client.player.getVelocity();
      Set temp = playerPositionLookS2CPacket.getFlags();
      PositionFlag positionFlag = PositionFlag.X;
      boolean flag = temp.contains(positionFlag);
      temp = playerPositionLookS2CPacket.getFlags();
      PositionFlag positionFlag2 = PositionFlag.Y;
      boolean flag2 = temp.contains(positionFlag2);
      temp = playerPositionLookS2CPacket.getFlags();
      PositionFlag positionFlag3 = PositionFlag.Z;
      boolean flag3 = temp.contains(positionFlag3);
      double d;
      double d2;
      if (flag) {
         d2 = vec.getX();
         d = client.player.getX() + playerPositionLookS2CPacket.getX();
         ClientPlayerEntity player2 = client.player;
         double d3 = client.player.lastRenderX + playerPositionLookS2CPacket.getX();
         player2.lastRenderX = d3;
         ClientPlayerEntity player3 = client.player;
         double d4 = client.player.prevX + playerPositionLookS2CPacket.getX();
         player3.prevX = d4;
      } else {
         d2 = 0.0;
         ClientPlayerEntity player4 = client.player;
         double d5 = d = playerPositionLookS2CPacket.getX();
         player4.lastRenderX = d5;
         client.player.prevX = d;
      }

      double d6;
      double d7;
      if (flag2) {
         d7 = vec.getY();
         d6 = client.player.getY() + playerPositionLookS2CPacket.getY();
         ClientPlayerEntity player5 = client.player;
         double d8 = client.player.lastRenderY + playerPositionLookS2CPacket.getY();
         player5.lastRenderY = d8;
         ClientPlayerEntity player6 = client.player;
         double d9 = client.player.prevY + playerPositionLookS2CPacket.getY();
         player6.prevY = d9;
      } else {
         d7 = 0.0;
         ClientPlayerEntity player7 = client.player;
         double d10 = d6 = playerPositionLookS2CPacket.getY();
         player7.lastRenderY = d10;
         client.player.prevY = d6;
      }

      double d11;
      double d12;
      if (flag3) {
         d12 = vec.getZ();
         d11 = client.player.getZ() + playerPositionLookS2CPacket.getZ();
         ClientPlayerEntity player8 = client.player;
         double d13 = client.player.lastRenderZ + playerPositionLookS2CPacket.getZ();
         player8.lastRenderZ = d13;
         ClientPlayerEntity player9 = client.player;
         double d14 = client.player.prevZ + playerPositionLookS2CPacket.getZ();
         player9.prevZ = d14;
      } else {
         d12 = 0.0;
         ClientPlayerEntity player10 = client.player;
         double d15 = d11 = playerPositionLookS2CPacket.getZ();
         player10.lastRenderZ = d15;
         client.player.prevZ = d11;
      }

      float f3 = playerPositionLookS2CPacket.getYaw();
      float f4 = playerPositionLookS2CPacket.getPitch();
      temp = playerPositionLookS2CPacket.getFlags();
      PositionFlag positionFlag4 = PositionFlag.X_ROT;
      if (temp.contains(positionFlag4)) {
         f2 += f4;
      } else {
         f2 = f4;
      }

      temp = playerPositionLookS2CPacket.getFlags();
      PositionFlag positionFlag5 = PositionFlag.Y_ROT;
      if (temp.contains(positionFlag5)) {
         f += f3;
      } else {
         f = f3;
      }

      this.b = new float[]{f, f2};
      client.player.setPosition(d, d6, d11);
      double d16 = d12;
      double d17 = d7;
      double d18 = d2;
      client.player.setVelocity(d18, d17, d16);
      ClientPlayNetworkHandler network2 = client.player.networkHandler;
      TeleportConfirmC2SPacket teleportConfirmC2SPacket = new TeleportConfirmC2SPacket(playerPositionLookS2CPacket.getTeleportId());
      network2.sendPacket((Packet)teleportConfirmC2SPacket);
      ClientPlayNetworkHandler network3 = client.player.networkHandler;
      double temp2 = client.player.getX();
      double temp3 = client.player.getY();
      ClientPlayerEntity player11 = client.player;
      double d19 = temp3;
      double d20 = temp2;
      ClientPlayNetworkHandler network4 = network3;
      temp3 = player11.getZ();
      boolean flag4 = Float.isNaN(f);
      double d21 = temp3;
      double d22 = d19;
      double d23 = d20;
      float f5;
      if (flag4) {
         f5 = client.player.getYaw();
         d19 = d21;
         d20 = d22;
         d23 = d23;
      } else {
         f5 = f;
         d19 = d21;
         d20 = d22;
         d23 = d23;
      }

      flag4 = Float.isNaN(f2);
      f5 = f5;
      d19 = d19;
      d20 = d20;
      d23 = d23;
      float f6;
      if (flag4) {
         f5 = client.player.getPitch();
         f6 = f5;
         d19 = d19;
         d20 = d20;
         d23 = d23;
      } else {
         f5 = f2;
         f6 = f5;
         d19 = d19;
         d20 = d20;
         d23 = d23;
      }

      ClientPlayNetworkHandler network5 = network4;
      Full full = b(d23, d20, d19, f6, f5);
      network5.sendPacket((Packet)full);
   }

   @EventListener(priority = -99999)
   public void FF(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if ((Boolean)this.applyRotate.atx() && this.b != null) {
         Managers.movementTracker.Mh(new UnidentifiedHelper21(114514, this.b[0], this.b[1]));
         this.b = null;
      }
   }

   private static Full b(double d, double d2, double d3, float f, float f2) {
      return new Full(d, d2, d3, f, f2, false);
   }
}
