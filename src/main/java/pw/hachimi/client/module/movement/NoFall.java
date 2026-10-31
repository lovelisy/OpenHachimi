package pw.hachimi.client.module.movement;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.mixin.PlayerMoveC2SPacketMixin;
import pw.hachimi.client.model.movement.NoFallAntiLimitMode;
import pw.hachimi.client.model.movement.UnidentifiedInterface14;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.PacketFly;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.UnidentifiedHelper20;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class NoFall extends ClientModule {
   Setting mode = this.registerSetting(new EnumSetting("Mode", "The mode to prevent fall damage", NoFallAntiLimitMode.ANTI, NoFallAntiLimitMode.values()));
   Setting fallDistance = this.registerSetting(new NumberSetting("FallDistance", "Min fall distance for triggering", 0, 3, 20));
   Setting groundSpoof = this.registerSetting(new BooleanSetting("GroundSpoof", "More secure, but more lag", false, this::isGroundSpoofVisible));
   Setting timeout = this.registerSetting(new NumberSetting("Timeout", "Anti fall failed time (tick)", 0, 30, 60, this::isTimeoutVisible));
   private double e;
   private int f;
   private double g = 0.0;
   private boolean h = false;
   private boolean i = false;
   private boolean j;
   private int k;

   public NoFall() {
      super("NoFall", "Prevents all fall damage", ModuleCategory.MOVEMENT);
   }

   @Override
   public String hy() {
      return DirectionUtils.apW((Enum)this.mode.atx());
   }

   @Override
   public void awe() {
      this.j = false;
      this.k = 0;
   }

   @EventListener(priority = -1)
   public void awQ(UnidentifiedHelper20 unidentifiedHelper20) {
      if (this.mode.atx() == NoFallAntiLimitMode.GRIM_JUMP) {
         if (!client.player.isTouchingWater()) {
            if (!unidentifiedHelper20.aqY()) {
               this.k = !(client.player.fallDistance > ((Integer)this.fallDistance.atx()).intValue()) ? 0 : ++this.k;
            }

            if (unidentifiedHelper20.aqY() && this.k > 0 && !this.j) {
               unidentifiedHelper20.aqR(unidentifiedHelper20.aqQ() + 10.0);
               unidentifiedHelper20.are();
               this.j = true;
            }
         } else {
            this.k = 0;
            this.j = false;
         }
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (this.mode.atx() == NoFallAntiLimitMode.GRIM_JUMP && sendImmediatelyEvent.getPacket() instanceof PlayerMoveC2SPacket && this.j && this.k == 0) {
         sendImmediatelyEvent.aim();
      }
   }

   @EventListener(priority = -100)
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if (this.i) {
         client.player.input.movementSideways = 0.0F;
         client.player.input.movementForward = 0.0F;
      }

      if (this.j && tickPreEvent.getEventPhase() == EventPhase.POST) {
         client.player.input.jumping = true;
         this.j = false;
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (this.mode.atx() != NoFallAntiLimitMode.GRIM_LATEST) {
            if (this.awX()) {
               if (this.mode.atx() != NoFallAntiLimitMode.LIMIT) {
                  if (this.mode.atx() == NoFallAntiLimitMode.GRIM) {
                     ServerManager temp = Managers.serverManager;
                     Full full = new Full(
                        client.player.getX(), client.player.getY() + 1.0E-9, client.player.getZ(), client.player.getYaw(), client.player.getPitch(), false
                     );
                     temp.iO('\uf560', full);
                     client.player.onLanding();
                  }
               } else {
                  if (client.world.getRegistryKey() != World.NETHER) {
                     ServerManager serverManager2 = Managers.serverManager;
                     PositionAndOnGround positionAndOnGround = new PositionAndOnGround(0.0, 64.0, 0.0, true);
                     serverManager2.iO('\uf560', positionAndOnGround);
                  } else {
                     ServerManager serverManager3 = Managers.serverManager;
                     PositionAndOnGround positionAndOnGround2 = new PositionAndOnGround(client.player.getX(), 0.0, client.player.getZ(), true);
                     serverManager3.iO('\uf560', positionAndOnGround2);
                  }

                  client.player.onLanding();
               }
            }
         } else if (!this.i) {
            if (!client.player.isTouchingWater()) {
               this.g = client.player.fallDistance;
            } else {
               this.g = 0.0;
            }
         } else {
            if (client.player.isOnGround()) {
               client.player.jump();
               this.e = client.player.getY();
            }

            this.f++;
            if (client.player.getY() - 1.0 > this.e || this.f > (Integer)this.timeout.atx()) {
               this.i = false;
               this.f = 0;
               this.h = true;
               KeyBinding[] keyBindings = new KeyBinding[]{
                  client.options.jumpKey, client.options.forwardKey, client.options.backKey, client.options.rightKey, client.options.leftKey
               };
               int n = keyBindings.length;

               for (int n2 = 0; n2 < n; n2++) {
                  ((UnidentifiedInterface14)keyBindings[n2]).WK();
               }
            }
         }
      }
   }

   @EventListener
   public void onSendImmediately2(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null) {
         if (this.mode.atx() != NoFallAntiLimitMode.GRIM_LATEST) {
            if (this.awX()) {
               Packet packet2 = sendImmediatelyEvent.getPacket();
               if (packet2 instanceof PlayerMoveC2SPacket) {
                  if (this.mode.atx() != NoFallAntiLimitMode.PACKET) {
                     if (this.mode.atx() != NoFallAntiLimitMode.ANTI) {
                        if (this.mode.atx() == NoFallAntiLimitMode.STRICT
                           && this.awW()
                           && client.player.fallDistance >= ((Integer)this.fallDistance.atx()).intValue()) {
                           Managers.playerStateManager.rJ(0.0);
                           ((PlayerMoveC2SPacketMixin)packet2).hookSetY(this.e);
                           client.player.fallDistance = 0.0F;
                        }
                     } else {
                        double d = client.player.getY();
                        double d2 = ((PlayerMoveC2SPacket)packet2).getY(d);
                        ((PlayerMoveC2SPacketMixin)packet2).hookSetY(d2 + 0.1F);
                     }
                  } else {
                     ((PlayerMoveC2SPacketMixin)packet2).hookSetOnGround(true);
                  }
               }
            }
         } else {
            Packet packet3 = sendImmediatelyEvent.getPacket();
            if (packet3 instanceof PlayerMoveC2SPacket && !client.player.isTouchingWater()) {
               if (!this.h && ((PlayerMoveC2SPacket)packet3).isOnGround() && this.g > ((Integer)this.fallDistance.atx()).intValue()) {
                  double d3 = this.e;
                  this.e = ((PlayerMoveC2SPacket)packet3).getY(d3);
                  Managers.serverManager.iP(new ClientCommandC2SPacket(client.player, Mode.START_FALL_FLYING));
                  if (!client.player.isSneaking()) {
                     ServerManager temp = Managers.serverManager;
                     ClientCommandC2SPacket clientCommandC2SPacket = new ClientCommandC2SPacket(client.player, Mode.PRESS_SHIFT_KEY);
                     temp.iO('\uf560', clientCommandC2SPacket);
                     temp = Managers.serverManager;
                     clientCommandC2SPacket = new ClientCommandC2SPacket(client.player, Mode.RELEASE_SHIFT_KEY);
                     temp.iO('\uf560', clientCommandC2SPacket);
                  }

                  this.i = true;
                  if ((Boolean)this.groundSpoof.atx()) {
                     ((PlayerMoveC2SPacketMixin)packet3).hookSetOnGround(false);
                  }
               } else {
                  this.h = ((PlayerMoveC2SPacket)packet3).isOnGround();
               }
            }
         }
      }
   }

   public double awV() {
      for (int n = (int)Math.round(client.player.getY()); n > 0; n--) {
         Box box = client.player.getBoundingBox();
         Box box2 = new Box(box.minX, n - 1.0, box.minZ, box.maxX, n, box.maxZ);
         if (client.world.isSpaceEmpty(box2) && !(box2.minY > client.player.getY())) {
            return n;
         }
      }

      return 0.0;
   }

   public boolean awW() {
      this.e = this.awV() - 0.1;
      boolean flag;
      if (client.player.getY() - this.e < 3.0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean awX() {
      boolean flag;
      if (client.player.fallDistance > ((Integer)this.fallDistance.atx()).intValue()
         && !client.player.isOnGround()
         && !client.player.isFallFlying()
         && !Flight.adS().awg()
         && !PacketFly.Gj().awg()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isTimeoutVisible() {
      boolean flag;
      if (this.mode.atx() == NoFallAntiLimitMode.GRIM_LATEST) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isGroundSpoofVisible() {
      boolean flag;
      if (this.mode.atx() == NoFallAntiLimitMode.GRIM_LATEST) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
