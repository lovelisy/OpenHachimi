package pw.hachimi.client.manager;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.utils.UnidentifiedHelper5;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class Vec3dPlayerHelper {
   private double a_;
   private double b;
   private double c;
   private BlockPos pos;
   private boolean e;
   private boolean f;
   private boolean g;
   public static boolean a;

   public Vec3dPlayerHelper() {
      EventBusImpl.INSTANCE.rD(this);
   }

   public void BF(Vec3d vec) {
      this.BH(vec.getX(), vec.getY(), vec.getZ());
   }

   public void BG(double d, double d2, double d3, float f2, float f3) {
      this.BI(d, d2, d3);
      Managers.serverManager.iO('\uf560', new Full(d, d2, d3, f2, f3, this.BX()));
   }

   public void BH(double d, double d2, double d3) {
      this.BI(d, d2, d3);
      Managers.serverManager.iO('\uf560', new PositionAndOnGround(d, d2, d3, this.BX()));
   }

   public void BI(double d, double d2, double d3) {
      if (Mc.client.player.isRiding()) {
         Mc.client.player.getVehicle().setPosition(d, d2, d3);
      } else {
         Mc.client.player.setPosition(d, d2, d3);
      }
   }

   public void BJ(double d, double d2) {
      this.BH(d, this.b, d2);
   }

   public void BK(double d) {
      this.BH(this.a_, d, this.c);
   }

   public Vec3d BL() {
      return new Vec3d(this.BR(), this.BS(), this.BT());
   }

   public Vec3d BM() {
      Vec3d temp = this.BL();
      double d = Mc.client.player.getStandingEyeHeight();
      return temp.add(0.0, d, 0.0);
   }

   public final Vec3d BN() {
      double d = MathHelper.lerp(1.0, Mc.client.player.prevX, this.BR());
      double d2 = MathHelper.lerp(1.0, Mc.client.player.prevY, this.BS()) + Mc.client.player.getStandingEyeHeight();
      double d3 = MathHelper.lerp(1.0, Mc.client.player.prevZ, this.BT());
      return new Vec3d(d, d2, d3);
   }

   public double BO(Entity entity2) {
      float f2 = (float)(this.BR() - entity2.getX());
      float f3 = (float)(this.BS() - entity2.getY());
      float f4 = (float)(this.BT() - entity2.getZ());
      return MathHelper.squaredMagnitude(f2, f3, f4);
   }

   public double BP(Entity entity2) {
      Vec3d vec = this.BN();
      float f2 = (float)(vec.getX() - entity2.getX());
      float f3 = (float)(vec.getY() - entity2.getY());
      float f4 = (float)(vec.getZ() - entity2.getZ());
      return MathHelper.squaredMagnitude(f2, f3, f4);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (Mc.client.player != null && Mc.client.world != null) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof PlayerMoveC2SPacket) {
            this.g = ((PlayerMoveC2SPacket)packet2).isOnGround();
            if (((PlayerMoveC2SPacket)packet2).changesPosition()) {
               PlayerMoveC2SPacket temp = (PlayerMoveC2SPacket)packet2;
               double d = this.a_;
               this.a_ = temp.getX(d);
               temp = (PlayerMoveC2SPacket)packet2;
               double d2 = this.b;
               this.b = temp.getY(d2);
               temp = (PlayerMoveC2SPacket)packet2;
               double d3 = this.c;
               this.c = temp.getZ(d3);
               this.pos = BlockPos.ofFloored(this.a_, this.b, this.c);
            }
         } else {
            packet2 = sendImmediatelyEvent.getPacket();
            if (packet2 instanceof ClientCommandC2SPacket) {
               switch (UnidentifiedHelper5.a[((ClientCommandC2SPacket)packet2).getMode().ordinal()]) {
                  case 1:
                     this.f = true;
                     break;
                  case 2:
                     this.f = false;
                     break;
                  case 3:
                     this.e = true;
                     break;
                  case 4:
                     this.e = false;
               }
            }
         }
      }
   }

   public double BR() {
      return this.a_;
   }

   public double BS() {
      return this.b;
   }

   public double BT() {
      return this.c;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public boolean BV() {
      return this.e;
   }

   public boolean BW() {
      return this.f;
   }

   public boolean BX() {
      return this.g;
   }
}
