package pw.hachimi.client.module.combat;

import java.util.Iterator;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.BlockView;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.event.network.SendImmediately_2Event;
import pw.hachimi.client.handler.EntityHandler;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.mixin.PlayerMoveC2SPacketMixin;
import pw.hachimi.client.model.combat.CriticalsPacketPacketStrictMode;
import pw.hachimi.client.model.world.EntityInteractType;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.movement.Velocity;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.player.StackPlayerUtils2;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BoxEntityUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;
import pw.hachimi.client.module.combat.AutoCrystal;
import pw.hachimi.client.module.combat.AutoTrap;
import pw.hachimi.client.module.combat.AutoXP;
import pw.hachimi.client.utils.movement.MovementUtils;

public class Criticals extends ClientModule {
   private static final double[] a = new double[]{0.3, -0.3};
   private static final int[] b = new int[]{1, 0, -1};
   private static final double c = 2.0E-4;
   private static Criticals INSTANCE;
   private final Stopwatch stopwatch = new StopwatchImpl();
   Setting multitask = this.registerSetting(new BooleanSetting("Multitask", "Allows crits when other combat modules are enabled", true));
   Setting packetPacketStrictMode = this.registerSetting(new EnumSetting("Mode", "Mode for critical attack modifier", CriticalsPacketPacketStrictMode.PACKET, CriticalsPacketPacketStrictMode.values()));
   private final Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("MovingDisable", "Automatically disables when you are moving", true, this::isBooleanSetting1Visible));
   private final Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("CrawlingDisable", "Automatically disables when you are crawling", true, this::isBooleanSetting2Visible));
   private final Setting booleanSetting3 = this.registerSetting(new BooleanSetting("BlinkingDisable", "Automatically disables when you are blinking", true, this::isBooleanSetting3Visible));
   private final Setting booleanSetting4 = this.registerSetting(new BooleanSetting("WebDisable", "Automatically disables when you in web", true, this::isBooleanSetting4Visible));
   private final Setting booleanSetting5 = this.registerSetting(new BooleanSetting("AutoJump", "Automatically jump once when enabled", true, this::isBooleanSetting5Visible));
   private final Setting booleanSetting6 = this.registerSetting(new BooleanSetting("OnlyTrapped", "Only jump when you are trapped (to prevent jumping out of the block)", true, this::isBooleanSetting6Visible));
   private final Setting booleanSetting7 = this.registerSetting(new BooleanSetting("LowJump", "Use mini jumps (to prevent you from jumping out of blocks)", true, this::isBooleanSetting7Visible));
   private final Setting booleanSetting8 = this.registerSetting(new BooleanSetting("1.21+", "Applicable minijump bypass 1.21+ grim server", false, this::isBooleanSetting8Visible));
   private final Setting booleanSetting9 = this.registerSetting(new BooleanSetting("Static", "Keep you static in the air", true, this::isBooleanSetting9Visible));
   private final Setting booleanSetting10 = this.registerSetting(new BooleanSetting("GroundSpoof", "Set all move packet on ground to false", false, this::isBooleanSetting10Visible));
   Setting booleanSetting11 = this.registerSetting(new BooleanSetting("BoatKiller", "Crit boat", true));
   Setting booleanSetting12 = this.registerSetting(new BooleanSetting("StopSprint", "Stop sprinting (Sprinting cant crit)", false));
   Setting booleanSetting13 = this.registerSetting(new BooleanSetting("PhasedOnly", "Only attempts criticals when phased", false, this::isBooleanSetting13Visible));
   Setting booleanSetting14 = this.registerSetting(new BooleanSetting("WallsOnly", "Only attempts criticals in walls", false, this::isBooleanSetting14Visible));
   Setting booleanSetting15 = this.registerSetting(new BooleanSetting("MoveFix", "Pauses crits when moving", false, this::isBooleanSetting15Visible));
   Setting booleanSetting16 = this.registerSetting(new BooleanSetting("CrawlPause", "Only attempts criticals when standing", false, this::isBooleanSetting16Visible));
   private boolean w;
   private boolean x;
   private boolean y;
   public static boolean q;

   public Criticals() {
      super("Criticals", "Modifies attacks to always land critical hits", ModuleCategory.COMBAT);
      INSTANCE = this;
   }

   public static Criticals bZ() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      return DirectionUtils.apW((Enum)this.packetPacketStrictMode.atx());
   }

   @Override
   public void awe() {
      this.w = false;
      this.x = false;
   }

   @Override
   protected void awd() {
      this.y = true;
   }

   @EventListener
   public void ca(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         if ((Boolean)this.booleanSetting2_1.atx() && client.player.isCrawling()) {
            this.awc();
         } else if (!(Boolean)this.booleanSetting1_1.atx() || client.player.input.movementForward == 0.0F && client.player.input.movementSideways == 0.0F) {
            if ((Boolean)this.booleanSetting3.atx() && Blink.bl().awg()) {
               this.awc();
            } else if (!WorldUtils.Ng(0.0)) {
               if ((Boolean)this.booleanSetting9.atx() && client.player.fallDistance > 0.0F) {
                  client.player.setVelocity(0.0, 0.0, 0.0);
                  this.y = false;
               } else if ((Boolean)this.booleanSetting5.atx()) {
                  if (!(Boolean)this.booleanSetting7.atx() || (Boolean)this.booleanSetting8.atx() && this.ch() == null) {
                     if (!(Boolean)this.booleanSetting6.atx() || this.ci()) {
                        client.player.jump();
                        this.y = false;
                     }
                  } else if (client.player.isOnGround() && (Boolean)this.booleanSetting5.atx() && ((Boolean)this.booleanSetting9.atx() || this.y)) {
                     Vec3d vec = client.player.getVelocity();
                     if (vec.y < 0.05) {
                        double d5 = vec.z;
                        double d6 = vec.x;
                        client.player.setVelocity(d6, 0.05, d5);
                        this.y = false;
                     }
                  }
               }
            } else if ((Boolean)this.booleanSetting4.atx()) {
               this.awc();
            }
         } else {
            this.awc();
         }
      }
   }

   @EventListener
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (client.player != null
         && client.world != null
         && !AutoCrystal.sc().sn()
         && !AutoCrystal.sc().so()
         && ((Boolean)this.multitask.atx() || !AutoTrap.of().ot() && !AutoXP.Ra().awg())) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (!(packet2 instanceof EntityHandler) || ((EntityHandler)packet2).Qd() != EntityInteractType.ATTACK) {
            packet2 = sendImmediatelyEvent.getPacket();
            if (packet2 instanceof PlayerMoveC2SPacket
               && this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID
               && (Boolean)this.booleanSetting10.atx()) {
               ((PlayerMoveC2SPacketMixin)packet2).hookSetOnGround(false);
            }
         } else if (!client.player.isRiding()
            && !client.player.isFallFlying()
            && !client.player.isTouchingWater()
            && !client.player.isInLava()
            && !client.player.isHoldingOntoLadder()) {
            RegistryEntry registryEntry = StatusEffects.BLINDNESS;
            if (!client.player.hasStatusEffect(registryEntry) && !StackPlayerUtils2.abF()) {
               Entity entity2 = ((EntityHandler)packet2).Qc();
               if (entity2 != null && entity2.isAlive() && entity2 instanceof LivingEntity) {
                  if (!BoxEntityUtils.kJ(entity2)) {
                     this.x = (Boolean)this.booleanSetting12.atx() && client.player.isSprinting();
                     if (this.x) {
                        ServerManager temp = Managers.serverManager;
                        ClientCommandC2SPacket clientCommandC2SPacket = new ClientCommandC2SPacket(client.player, Mode.STOP_SPRINTING);
                        temp.iO('\uf560', clientCommandC2SPacket);
                     }

                     this.cc(entity2);
                  } else if ((Boolean)this.booleanSetting11.atx()) {
                     int n3 = 0;

                     do {
                        ServerManager serverManager2 = Managers.serverManager;
                        boolean flag = Managers.vec3dPlayerHelper.BV();
                        serverManager2.iP(PlayerInteractEntityC2SPacket.attack(entity2, flag));
                        serverManager2 = Managers.serverManager;
                        HandSwingC2SPacket handSwingC2SPacket = new HandSwingC2SPacket(Hand.MAIN_HAND);
                        serverManager2.iO('\uf560', handSwingC2SPacket);
                     } while (++n3 < 5);
                  }
               }
            }
         }
      }
   }

   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public void cc(Entity entity) {
       double d = Managers.vec3dPlayerHelper.BR();
       double d2 = Managers.vec3dPlayerHelper.BS();
       double d3 = Managers.vec3dPlayerHelper.BT();
       int n = ((CriticalsPacketPacketStrictMode)((Object)this.packetPacketStrictMode.atx())).ordinal();
       if (n != 0) {
           if (n != 1) {
               if (n == 2) return;
               if (n != 3) {
                   if (n != 4) {
                       if (n != 5) {
                           if (n != 6) {
                               return;
                           }
                           Managers.playerStateManager.rJ(0.3425);
                           return;
                       }
                       if (((Boolean)this.booleanSetting13.atx()).booleanValue()) {
                           if (!((Boolean)this.booleanSetting14.atx()).booleanValue()) {
                               if (!this.cg()) return;
                           } else if (!this.cf()) return;
                       }
                       if (((Boolean)this.booleanSetting15.atx()).booleanValue()) {
                           if (MovementUtils.awI()) return;
                       }
                       if (((Boolean)this.booleanSetting16.atx()).booleanValue()) {
                           if (Criticals.client.player.isCrawling()) return;
                       }
                       if (!Criticals.client.player.isOnGround()) {
                           return;
                       }
                       PlayerMoveC2SPacket.PositionAndOnGround positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2, d3, true);
                       Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
                       positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2 + 0.0625, d3, false);
                       Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
                       positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2 + (double)0.04535f, d3, false);
                       Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
                       return;
                   }
                   if (((Boolean)this.booleanSetting13.atx()).booleanValue()) {
                       if (!((Boolean)this.booleanSetting14.atx()).booleanValue()) {
                           if (!this.cg()) return;
                       } else if (!this.cf()) return;
                   }
                   if (((Boolean)this.booleanSetting15.atx()).booleanValue()) {
                       if (MovementUtils.awI()) return;
                   }
                   if (((Boolean)this.booleanSetting16.atx()).booleanValue()) {
                       if (Criticals.client.player.isCrawling()) return;
                   }
                   if (!this.stopwatch.afq(250)) {
                       return;
                   }
                   if (!Criticals.client.player.isOnGround()) {
                       return;
                   }
                   float f = Managers.movementTracker.Mo();
                   float f2 = Managers.movementTracker.Mt();
                   PlayerMoveC2SPacket.Full full = new PlayerMoveC2SPacket.Full(d, d2 + 0.0625, d3, f, f2, false);
                   Managers.serverManager.iO('\uf560', (Packet)full);
                   full = new PlayerMoveC2SPacket.Full(d, d2 + 0.0625013579, d3, f, f2, false);
                   Managers.serverManager.iO('\uf560', (Packet)full);
                   full = new PlayerMoveC2SPacket.Full(d, d2 + 1.3579E-6, d3, f, f2, false);
                   Managers.serverManager.iO('\uf560', (Packet)full);
                   this.stopwatch.afr(29432, 5265492, (byte)40);
                   return;
               }
               if (!Criticals.client.player.isOnGround()) {
                   return;
               }
               if (Criticals.client.player.input.jumping) {
                   return;
               }
               int n2 = !random2.nextBoolean() ? 43 : 34;
               double d4 = 1.0E-7 + 1.0E-7 * (1.0 + (double)random2.nextInt(n2));
               PlayerMoveC2SPacket.PositionAndOnGround positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2 + (double)0.1016f + d4 * 3.0, d3, false);
               Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
               positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2 + (double)0.0202f + d4 * 2.0, d3, false);
               Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
               positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2 + 3.239E-4 + d4, d3, false);
               Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
               Criticals.client.player.addCritParticles(entity);
               return;
           }
           if (!this.stopwatch.afq(500)) {
               return;
           }
           if (!Criticals.client.player.isOnGround()) {
               return;
           }
           if (Criticals.client.player.input.jumping) {
               return;
           }
           PlayerMoveC2SPacket.PositionAndOnGround positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2 + (double)1.1E-7f, d3, false);
           Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
           positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2 + (double)1.0E-8f, d3, false);
           Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
           this.w = true;
           this.stopwatch.afr(29432, 5265492, (byte)40);
           return;
       }
       if (!Criticals.client.player.isOnGround()) {
           return;
       }
       if (Criticals.client.player.input.jumping) {
           return;
       }
       PlayerMoveC2SPacket.PositionAndOnGround positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2 + 0.0625, d3, false);
       Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
       positionAndOnGround = new PlayerMoveC2SPacket.PositionAndOnGround(d, d2, d3, false);
       Managers.serverManager.iO('\uf560', (Packet)positionAndOnGround);
       Criticals.client.player.addCritParticles(entity);
   }

   @EventListener
   public void onSendImmediately_2(SendImmediately_2Event sendImmediately2Event) {
      if (client.player != null) {
         if (sendImmediately2Event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            if (this.w) {
               Managers.serverManager.iO('\uf560', b(client.player.getX(), client.player.getY(), client.player.getZ()));
               this.w = false;
            }

            if (this.x) {
               Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.START_SPRINTING));
               this.x = false;
            }
         }
      }
   }

   public boolean ce(int n3, short s, int n4) {
      if (n3 > 0) {
         if (s >= 0 && n3 >= 0) {
            if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.GRIM) {
               if (n3 > 0) {
                  if (n3 > 0) {
                     boolean flag = true;
                     if (n4 >= 0 && n3 > 0) {
                        return flag;
                     }

                     return false;
                  }

                  if (n4 >= 0) {
                     return false;
                  }
               }

               if (s >= 0) {
               }
            } else if ((n4 >= 0 || s >= 0) && s >= 0) {
               boolean flag2 = false;
               if (n3 >= 0 && n3 >= 0) {
                  return flag2;
               }
            }
         }
      } else if (n3 >= 0) {
      }

      return false;
   }

   public boolean cf() {
      Iterator iterator2 = BoxUtils.jD(client.player.getBoundingBox(), client.player.getBlockPos()).iterator();

      while (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         BlockState state = client.world.getBlockState(pos);
         ClientWorld temp = client.world;
         BlockPos pos2 = pos.up();
         BlockState state2 = temp.getBlockState(pos2);
         if (state.blocksMovement() && state2.blocksMovement()) {
            return true;
         }
      }

      return false;
   }

   public boolean cg() {
      Iterator iterator2 = BoxUtils.jE(client.player.getBoundingBox()).iterator();

      while (iterator2.hasNext()) {
         BlockPos pos = (BlockPos)iterator2.next();
         if (client.world.getBlockState(pos).blocksMovement()) {
            return true;
         }
      }

      return false;
   }

   public BlockPos ch() {
      Box box = client.player.getBoundingBox().expand(2.0E-4);
      double[] ds = a;
      int temp = ds.length;
      int n3 = 0;
      double d5 = ds[0];
      double[] ds2 = a;
      temp = ds2.length;
      int n4 = 0;
      double d6 = ds2[0];
      Mutable mutable = new Mutable(client.player.getX() + d5, client.player.getY(), client.player.getZ() + d6);
      int n5 = 1;
      int[] ns;
      int n6;
      if (true) {
         ns = b;
         temp = ns.length;
         n6 = 0;
         int n7 = ns[0];
         int[] ns2 = b;
         temp = ns2.length;
         int n8 = 0;

         do {
            int n9 = ns2[n8];
            BlockPos pos = mutable.add(n7, 0, n9).up(1);
            if (!client.world.isAir(pos) && (new Box(pos)).intersects(box)) {
               BlockState state = client.world.getBlockState(pos);
               ClientWorld world2 = client.world;
               if (state.isFullCube((BlockView)world2, pos)) {
                  return pos;
               }
            }
         } while (++n8 < 3);

         n6++;
      } else {
         do {
            n4++;

            while (n4 >= 2) {
               if (++n3 >= 2) {
                  return null;
               }

               d5 = ds[n3];
               ds2 = a;
               temp = ds2.length;
               n4 = 0;
            }

            d6 = ds2[n4];
            mutable = new Mutable(client.player.getX() + d5, client.player.getY(), client.player.getZ() + d6);
            n5 = 1;
         } while (false);

         ns = b;
         temp = ns.length;
         n6 = 0;
      }

      while (true) {
         while (n6 >= 3) {
            if (++n5 <= 2) {
               ns = b;
               temp = ns.length;
               n6 = 0;
            } else {
               do {
                  n4++;

                  while (n4 >= 2) {
                     if (++n3 >= 2) {
                        return null;
                     }

                     d5 = ds[n3];
                     ds2 = a;
                     temp = ds2.length;
                     n4 = 0;
                  }

                  d6 = ds2[n4];
                  mutable = new Mutable(client.player.getX() + d5, client.player.getY(), client.player.getZ() + d6);
                  n5 = 1;
               } while (false);

               ns = b;
               temp = ns.length;
               n6 = 0;
            }
         }

         int n10 = ns[n6];
         int[] ns3 = b;
         temp = ns3.length;
         int n11 = 0;
         if (true) {
            do {
               int n12 = ns3[n11];
               BlockPos pos2 = mutable.add(n10, 0, n12);
               int n13 = n5;
               BlockPos pos3 = pos2.up(n13);
               if (!client.world.isAir(pos3) && (new Box(pos3)).intersects(box)) {
                  BlockState state2 = client.world.getBlockState(pos3);
                  ClientWorld world3 = client.world;
                  if (state2.isFullCube((BlockView)world3, pos3)) {
                     return pos3;
                  }
               }
            } while (++n11 < 3);

            n6++;
         } else {
            n6++;
         }
      }
   }

   private boolean ci() {
      Vec3d vec = client.player.getEyePos();
      return Velocity.amZ(client.player, new Box(vec.x - 0.3, vec.y + 0.5, vec.z - 0.3, vec.x + 0.3, vec.y + 1.0, vec.z + 0.3));
   }

   private Boolean isBooleanSetting16Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() != CriticalsPacketPacketStrictMode.GRIM_V3
         && this.packetPacketStrictMode.atx() != CriticalsPacketPacketStrictMode.GRIM) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private Boolean isBooleanSetting15Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() != CriticalsPacketPacketStrictMode.GRIM_V3
         && this.packetPacketStrictMode.atx() != CriticalsPacketPacketStrictMode.GRIM) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private Boolean isBooleanSetting14Visible() {
      boolean flag;
      if ((
            this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.GRIM_V3
               || this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.GRIM
         )
         && (Boolean)this.booleanSetting13.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting13Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() != CriticalsPacketPacketStrictMode.GRIM_V3
         && this.packetPacketStrictMode.atx() != CriticalsPacketPacketStrictMode.GRIM) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   private Boolean isBooleanSetting10Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting9Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting8Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID && (Boolean)this.booleanSetting7.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting7Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting6Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting5Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting4Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting3Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting2Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBooleanSetting1Visible() {
      boolean flag;
      if (this.packetPacketStrictMode.atx() == CriticalsPacketPacketStrictMode.RIGID) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static PositionAndOnGround b(double d5, double d6, double d7) {
      return new PositionAndOnGround(d5, d6, d7, false);
   }
}
