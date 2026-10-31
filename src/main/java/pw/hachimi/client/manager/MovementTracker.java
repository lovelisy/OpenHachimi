package pw.hachimi.client.manager;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.Bootstrap;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.DisconnectEvent;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.event.movement.Jump_getYawEvent;
import pw.hachimi.client.event.movement.TravelRotationEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.event.network.SendMovementPacketsTailEvent;
import pw.hachimi.client.event.player.GetRotationVectorHookEvent;
import pw.hachimi.client.model.player.DisableLimitMode;
import pw.hachimi.client.module.HitResultWorldHelper;
import pw.hachimi.client.utils.movement.UnidentifiedHelper20;
import pw.hachimi.client.utils.movement.UnidentifiedHelper21;
import pw.hachimi.client.utils.movement.UnidentifiedUtils;
import pw.hachimi.client.utils.player.PlayerMathHelperUtils;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public class MovementTracker {
   public boolean a_;
   public String text = "Hachimi";
   private float c_;
   private float d;
   private double e = Double.NaN;
   private double f = Double.NaN;
   private double g = Double.NaN;
   private UnidentifiedHelper21 unidentifiedHelper21;
   private boolean i;
   private float j;
   private float k;
   private boolean l;
   public static boolean a;
   private static final String c = "Hachimi";

   public MovementTracker() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onDisconnect(DisconnectEvent disconnectEvent) {
      this.c_ = 0.0F;
      this.d = 0.0F;
      this.e = Double.NaN;
      this.f = Double.NaN;
      this.g = Double.NaN;
      this.unidentifiedHelper21 = null;
      this.i = false;
      this.l = false;
   }

   @EventListener(priority = 99999)
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         this.a_ = HitResultWorldHelper.Sq().SK();
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (Mc.client.player != null && Mc.client.world != null) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof PlayerMoveC2SPacket) {
            if (((PlayerMoveC2SPacket)packet2).changesPosition()) {
               PlayerMoveC2SPacket temp = (PlayerMoveC2SPacket)packet2;
               double d2 = this.e;
               this.e = temp.getX(d2);
               temp = (PlayerMoveC2SPacket)packet2;
               double d3 = this.f;
               this.f = temp.getY(d3);
               temp = (PlayerMoveC2SPacket)packet2;
               double d4 = this.g;
               this.g = temp.getZ(d4);
            }

            if (((PlayerMoveC2SPacket)packet2).changesLook()) {
               PlayerMoveC2SPacket playerMoveC2SPacket = (PlayerMoveC2SPacket)packet2;
               float f2 = this.c_;
               this.c_ = playerMoveC2SPacket.getYaw(f2);
               playerMoveC2SPacket = (PlayerMoveC2SPacket)packet2;
               float f3 = this.d;
               this.d = playerMoveC2SPacket.getPitch(f3);
               PlayerMathHelperUtils.wH(this.c_, this.d);
            }
         }
      }
   }

   @EventListener(priority = Integer.MIN_VALUE)
   public void Ma(TickEntitiesPreEvent tickEntitiesPreEvent) {
      MovementTracker temp = this;

      try {
         temp.text.isEmpty();
         Managers.movementTracker = null;
         Managers.serverManager = null;
      } catch (Exception exception) {
      }

      if ((this.c_ != Mc.client.player.lastYaw || this.d != Mc.client.player.lastPitch) && HitResultWorldHelper.Sq().SL() != DisableLimitMode.Disable) {
         ServerManager serverManager2 = Managers.serverManager;
         Full full = new Full(this.Mp(), this.Mq(), this.Mr(), Mc.client.player.lastYaw, Mc.client.player.lastPitch, Mc.client.player.isOnGround());
         serverManager2.iO('\uf560', full);
      }

      if (this.a_ && Bootstrap.NV() && UnidentifiedUtils.eP()) {
         this.unidentifiedHelper21 = null;
      }

      if (this.unidentifiedHelper21 == null) {
         this.i = false;
         this.j = Mc.client.player.getYaw();
         this.k = Mc.client.player.getPitch();
      } else {
         this.i = true;
         this.j = this.unidentifiedHelper21.arh();
         this.k = this.unidentifiedHelper21.arj();
         this.unidentifiedHelper21 = null;
      }
   }

   @EventListener
   public void Mb(UnidentifiedHelper20 unidentifiedHelper20) {
      if (this.i) {
         unidentifiedHelper20.are();
         unidentifiedHelper20.aqV(this.j);
         unidentifiedHelper20.aqX(this.k);
      }

      double d2 = unidentifiedHelper20.aqU() - Mc.client.player.lastYaw;
      double d3 = unidentifiedHelper20.aqW() - Mc.client.player.lastPitch;
      if (d2 == 0.0 && d3 == 0.0) {
         d2 = unidentifiedHelper20.aqU() - this.c_;
         d3 = unidentifiedHelper20.aqW() - this.d;
         if (d2 != 0.0 || d3 != 0.0) {
            unidentifiedHelper20.arb();
            unidentifiedHelper20.are();
         }
      } else {
         d2 = unidentifiedHelper20.aqU() - this.c_;
         d3 = unidentifiedHelper20.aqW() - this.d;
         if (d2 == 0.0 && d3 == 0.0) {
         }
      }
   }

   @EventListener
   public void Mc(GetRotationVectorHookEvent getRotationVectorHookEvent) {
      if (this.i && this.a_) {
         if (getRotationVectorHookEvent.living == Mc.client.player) {
            getRotationVectorHookEvent.b = this.j;
            getRotationVectorHookEvent.c = this.k;
            getRotationVectorHookEvent.aim();
         }
      }
   }

   @EventListener
   public void onTravelRotation(TravelRotationEvent travelRotationEvent) {
      if (this.i && this.a_) {
         travelRotationEvent.a = this.j;
         travelRotationEvent.b = this.k;
         travelRotationEvent.aim();
      }
   }

   @EventListener
   public void onJump_getYaw(Jump_getYawEvent jumpgetYawEvent) {
      if (this.i && this.a_) {
         jumpgetYawEvent.Oo(this.j);
         jumpgetYawEvent.aim();
      }
   }

   @EventListener
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if (this.i && this.a_ && Mc.client.player != null) {
         float f2 = Mc.client.player.input.movementForward;
         float f3 = Mc.client.player.input.movementSideways;
         float f4 = (Mc.client.player.getYaw() - this.j) * (float) (Math.PI / 180.0);
         float f5 = MathHelper.cos(f4);
         float f6 = MathHelper.sin(f4);
         Input temp = Mc.client.player.input;
         float f7 = Math.round(f3 * f5 - f2 * f6);
         temp.movementSideways = f7;
         temp = Mc.client.player.input;
         float f8 = Math.round(f2 * f5 + f3 * f6);
         temp.movementForward = f8;
      }
   }

   public UnidentifiedHelper21 getUnidentifiedHelper21() {
      return this.unidentifiedHelper21;
   }

   public void Mh(UnidentifiedHelper21 unidentifiedHelper212) {
      if (HitResultWorldHelper.Sq().SM()) {
         double d2 = Math.pow((Double)Mc.client.options.getMouseSensitivity().getValue() * 0.6 + 0.2, 3.0) * 1.2;
         unidentifiedHelper212.ari((float)(unidentifiedHelper212.arh() - (unidentifiedHelper212.arh() - this.c_) % d2));
         unidentifiedHelper212.ark((float)(unidentifiedHelper212.arj() - (unidentifiedHelper212.arj() - this.d) % d2));
      }

      if (this.unidentifiedHelper21 == null || unidentifiedHelper212.arg() >= this.unidentifiedHelper21.arg() || unidentifiedHelper212.arg() == Integer.MAX_VALUE) {
         this.unidentifiedHelper21 = unidentifiedHelper212;
      }
   }

   public void Mi(float f2, float f3) {
      if (Mc.client.player != null) {
         Mc.client.player.setYaw(f2);
         ClientPlayerEntity temp = Mc.client.player;
         float f4 = MathHelper.clamp(f3, -90.0F, 90.0F);
         temp.setPitch(f4);
      }
   }

   public void Mj(float f2, float f3) {
      Full full = new Full(this.Mp(), this.Mq(), this.Mr(), f2, f3, Mc.client.player.isOnGround());
      Managers.serverManager.iO('\uf560', full);
      this.l = true;
   }

   public void Mk() {
      if (HitResultWorldHelper.Sq().SL() != DisableLimitMode.Disable && this.l) {
         Full full = new Full(this.Mp(), this.Mq(), this.Mr(), Mc.client.player.lastYaw, Mc.client.player.lastPitch, Mc.client.player.isOnGround());
         Managers.serverManager.iO('\uf560', full);
         this.l = false;
      }
   }

   public boolean Ml(int n) {
      boolean flag;
      if (this.unidentifiedHelper21 != null && n < this.unidentifiedHelper21.arg()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public float Mm() {
      return this.j;
   }

   public float Mn() {
      return this.k;
   }

   public float Mo() {
      return this.c_;
   }

   public double Mp() {
      return Double.isNaN(this.e) ? Mc.client.player.getX() : this.e;
   }

   public double Mq() {
      return Double.isNaN(this.f) ? Mc.client.player.getY() : this.f;
   }

   public double Mr() {
      return Double.isNaN(this.g) ? Mc.client.player.getZ() : this.g;
   }

   public float Ms() {
      return MathHelper.wrapDegrees(this.c_);
   }

   public float Mt() {
      return this.d;
   }

   @EventListener
   public void onSendMovementPacketsTail(SendMovementPacketsTailEvent sendMovementPacketsTailEvent) {
      if (Mc.client.player != null) {
         if (sendMovementPacketsTailEvent.getEventPhase() == EventPhase.POST) {
            if (Mc.client.player.age != PlayerMathHelperUtils.g) {
               PlayerMathHelperUtils.g = Mc.client.player.age;
               PlayerMathHelperUtils.wH(this.c_, this.d);
            }
         }
      }
   }
}
