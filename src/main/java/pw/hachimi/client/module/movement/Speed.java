package pw.hachimi.client.module.movement;

import java.util.Iterator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.UnknownEvent3;
import pw.hachimi.client.event.movement.MoveEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.movement.SpeedStrafeStrafeStrictMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.misc.Timer;
import pw.hachimi.client.module.player.Freecam;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.combat.UnidentifiedUtils3;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Speed extends ClientModule {
   private static final float a = 159.077F;
   private static Speed INSTANCE;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "Speed mode", SpeedStrafeStrafeStrictMode.STRAFE, SpeedStrafeStrafeStrictMode.values()));
   Setting strict = this.registerSetting(new BooleanSetting("Strict", "The distance to apply collision speed", true, this::isStrictVisible));
   Setting collideSpeed = this.registerSetting(new NumberSetting("CollideSpeed", "Grim collide speed", 0.0F, 0.08F, 0.08F, this::isCollideSpeedVisible));
   Setting speed = this.registerSetting(new NumberSetting("Speed", "The speed for alternative modes", 0.1F, 4.0F, 50.0F, this::isSpeedVisible));
   Setting useTimer = this.registerSetting(new BooleanSetting("UseTimer", "Uses timer to increase acceleration", false, this::isUseTimerVisible));
   Setting fast = this.registerSetting(new BooleanSetting("Fast", "Fast timer speed", false, this::isFastVisible));
   Setting strafeBoost = this.registerSetting(new BooleanSetting("StrafeBoost", "Uses explosion velocity to boost Strafe", false, this::isUseTimerVisible));
   Setting boostTicks = this.registerSetting(new NumberSetting("BoostTicks", "The number of ticks to boost strafe", 10, 20, 40, this::isBoostTicksVisible));
   Setting speedInWater = this.registerSetting(new BooleanSetting("SpeedInWater", "Applies speed even in water and lava", false));
   private int k = 4;
   private boolean l;
   private int m;
   private int n;
   private int o;
   private double p;
   private double q;
   private double r;
   private boolean s;
   public static boolean bv;

   public Speed() {
      super("Speed", "Move faster", ModuleCategory.MOVEMENT);
      INSTANCE = this;
   }

   public static Speed lq() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      return this.mode.atx() == SpeedStrafeStrafeStrictMode.GRIM_COLLIDE ? "Grim" : DirectionUtils.apW((Enum)this.mode.atx());
   }

   @Override
   public void awd() {
      this.s = Timer.fP().awg();
      if ((Boolean)this.useTimer.atx() && !this.s && this.isUseTimerVisible()) {
         Timer.fP().awb();
      }
   }

   @Override
   public void awe() {
      this.lB();
      if (Timer.fP().awg()) {
         Timer.fP().fU();
         if (!this.s) {
            Timer.fP().awc();
         }
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         this.o++;
         if (this.o > (Integer)this.boostTicks.atx()) {
            this.q = 0.0;
         }

         double d = client.player.getX() - client.player.prevX;
         double d2 = client.player.getZ() - client.player.prevZ;
         this.r = Math.sqrt(d * d + d2 * d2);
         if (this.mode.atx() == SpeedStrafeStrafeStrictMode.GRIM_COLLIDE && MovementUtils.awH()) {
            int n2 = 0;
            Box box;
            if ((Boolean)this.strict.atx()) {
               box = client.player.getBoundingBox();
            } else {
               box = client.player.getBoundingBox().expand(1.0);
            }

            Iterator iterator2 = client.world.getEntities().iterator();

            while (iterator2.hasNext()) {
               Entity entity2 = (Entity)iterator2.next();
               Box box2 = entity2.getBoundingBox();
               if (box.intersects(box2) && this.ls(entity2)) {
                  n2++;
               }
            }

            double d3 = Math.toRadians(Sprint.mD().mL(client.player.getYaw()));
            double d4 = (Float)this.collideSpeed.atx() * n2;
            ClientPlayerEntity temp = client.player;
            double temp2 = -Math.sin(d3) * d4;
            double d5 = Math.cos(d3) * d4;
            double d6 = temp2;
            temp.addVelocity(d6, 0.0, d5);
         }
      }
   }

   private boolean ls(Entity entity2) {
      boolean flag;
      if (entity2 != client.player && entity2 instanceof LivingEntity && !(entity2 instanceof ArmorStandEntity)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   @EventListener
   public void onMove(MoveEvent moveEvent) {
      if (client.player != null && client.world != null && moveEvent.getMovementType() == MovementType.SELF) {
         if (!MovementUtils.awH()
            || Flight.adS().awg()
            || LongJump.rs().awg()
            || Freecam.gP().awg() && !Freecam.gP().e
            || client.player.getAbilities().flying
            || client.player.isRiding()
            || client.player.isFallFlying()
            || client.player.isHoldingOntoLadder()
            || client.player.fallDistance > 2.0F
            || (client.player.isInLava() || client.player.isTouchingWater()) && !(Boolean)this.speedInWater.atx()) {
            this.lB();
            Timer.fP().fT(1.0F);
            return;
         }

         moveEvent.aim();
         double d = 1.0;
         double d2 = 1.0;
         RegistryEntry registryEntry = StatusEffects.SPEED;
         if (client.player.hasStatusEffect(registryEntry)) {
            RegistryEntry registryEntry2 = StatusEffects.SPEED;
            double d3 = client.player.getStatusEffect(registryEntry2).getAmplifier();
            d = 1.0 + 0.2 * (d3 + 1.0);
         }

         RegistryEntry registryEntry3 = StatusEffects.SLOWNESS;
         if (client.player.hasStatusEffect(registryEntry3)) {
            RegistryEntry registryEntry4 = StatusEffects.SLOWNESS;
            double d4 = client.player.getStatusEffect(registryEntry4).getAmplifier();
            d2 = 1.0 + 0.2 * (d4 + 1.0);
         }

         double d5 = 0.2873F * d / d2;
         float f2 = 0.0F;
         RegistryEntry registryEntry5 = StatusEffects.JUMP_BOOST;
         if (client.player.hasStatusEffect(registryEntry5)) {
            RegistryEntry registryEntry6 = StatusEffects.JUMP_BOOST;
            f2 = 0.0F + (client.player.getStatusEffect(registryEntry6).getAmplifier() + 1) * 0.1F;
         }

         if (this.mode.atx() != SpeedStrafeStrafeStrictMode.STRAFE && this.mode.atx() != SpeedStrafeStrafeStrictMode.STRAFE_B_HOP) {
            if (this.mode.atx() != SpeedStrafeStrafeStrictMode.STRAFE_STRICT) {
               if (this.mode.atx() == SpeedStrafeStrafeStrictMode.LOW_HOP) {
                  if (!Managers.antiCheatManager.Uh(100L)) {
                     return;
                  }

                  if ((Boolean)this.useTimer.atx()) {
                     Timer.fP().fT(1.0888F);
                  }

                  if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.4, 3)) {
                     Managers.playerStateManager.rJ(0.31 + f2);
                     moveEvent.yo(0.31 + f2);
                  } else if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.71, 3)) {
                     Managers.playerStateManager.rJ(0.04 + f2);
                     moveEvent.yo(0.04 + f2);
                  } else if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.75, 3)) {
                     Managers.playerStateManager.rJ(-0.2 - f2);
                     moveEvent.yo(-0.2 - f2);
                  } else if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.55, 3)) {
                     Managers.playerStateManager.rJ(-0.14 + f2);
                     moveEvent.yo(-0.14 + f2);
                  } else if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.41, 3)) {
                     Managers.playerStateManager.rJ(-0.2 + f2);
                     moveEvent.yo(-0.2 + f2);
                  }

                  if (this.k == 1) {
                     this.p = 1.35F * d5 - 0.01F;
                  } else if (this.k == 2) {
                     double d6;
                     if (this.ly()) {
                        d6 = 0.2;
                     } else {
                        d6 = 0.3999;
                     }

                     double d7 = d6 + f2;
                     Managers.playerStateManager.rJ(d7);
                     moveEvent.yo(d7);
                     boolean flag = this.l;
                     d6 = this.p;
                     double d8;
                     if (flag) {
                        d6 = 1.5685;
                        d8 = d6;
                     } else {
                        d6 = 1.3445;
                        d8 = d6;
                     }

                     this.p = d8 * d6;
                  } else if (this.k == 3) {
                     double d9 = 0.66 * (this.r - d5);
                     this.p = this.r - d9;
                     boolean flag2;
                     if (!this.l) {
                        flag2 = true;
                     } else {
                        flag2 = false;
                     }

                     this.l = flag2;
                  } else {
                     if (client.player.isOnGround() && this.k > 0) {
                        byte b;
                        if (MovementUtils.awH()) {
                           b = 1;
                        } else {
                           b = 0;
                        }

                        this.k = b;
                     }

                     this.p = this.r - this.r / 159.077F;
                  }

                  this.p = Math.max(this.p, d5);
                  Vec2f vec2f = this.lv((float)this.p);
                  moveEvent.ym(vec2f.x);
                  moveEvent.yq(vec2f.y);
                  this.k++;
               } else if (this.mode.atx() == SpeedStrafeStrafeStrictMode.GAY_HOP) {
                  if (!Managers.antiCheatManager.Uh(100L)) {
                     this.k = 1;
                     return;
                  }

                  if (this.k == 1 && client.player.verticalCollision && MovementUtils.awH()) {
                     this.p = 1.25 * d5 - 0.01F;
                  } else if (this.k == 2 && client.player.verticalCollision && MovementUtils.awH()) {
                     float f3;
                     if (this.ly()) {
                        f3 = 0.2F;
                     } else {
                        f3 = 0.4F;
                     }

                     float f4 = f3 + f2;
                     moveEvent.yo(f4);
                     Managers.playerStateManager.rJ(f4);
                     this.p *= 2.149;
                  } else if (this.k == 3) {
                     double d10 = 0.66 * (this.r - d5);
                     this.p = this.r - d10;
                  } else {
                     if (client.player.isOnGround() && this.k > 0) {
                        if (1.35 * d5 - 0.01 > this.p) {
                           this.k = 0;
                        } else {
                           byte b2;
                           if (MovementUtils.awH()) {
                              b2 = 1;
                           } else {
                              b2 = 0;
                           }

                           this.k = b2;
                        }
                     }

                     this.p = this.r - this.r / 159.077F;
                  }

                  this.p = Math.max(this.p, d5);
                  if (this.k > 0) {
                     Vec2f vec2f2 = this.lu((float)this.p);
                     moveEvent.ym(vec2f2.x);
                     moveEvent.yq(vec2f2.y);
                  }

                  this.k++;
               } else if (this.mode.atx() == SpeedStrafeStrafeStrictMode.V_HOP) {
                  if (!Managers.antiCheatManager.Uh(100L)) {
                     this.k = 1;
                     return;
                  }

                  if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.4, 3)) {
                     Managers.playerStateManager.rJ(0.31 + f2);
                     moveEvent.yo(0.31 + f2);
                  } else if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.71, 3)) {
                     Managers.playerStateManager.rJ(0.04 + f2);
                     moveEvent.yo(0.04 + f2);
                  } else if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.75, 3)) {
                     Managers.playerStateManager.rJ(-0.2 - f2);
                     moveEvent.yo(-0.2 - f2);
                  }

                  ClientWorld world2 = client.world;
                  Box box = client.player.getBoundingBox().offset(0.0, -0.56, 0.0);
                  if (!world2.isSpaceEmpty((Entity)null, box)
                     && UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.55, 3)) {
                     Managers.playerStateManager.rJ(-0.14 + f2);
                     moveEvent.yo(-0.14 + f2);
                  }

                  if (this.k != 1 || !client.player.verticalCollision || client.player.forwardSpeed == 0.0F && client.player.sidewaysSpeed == 0.0F) {
                     if (this.k != 2 || !client.player.verticalCollision || client.player.forwardSpeed == 0.0F && client.player.sidewaysSpeed == 0.0F) {
                        if (this.k == 3) {
                           double d11 = 0.66 * (this.r - d5);
                           this.p = this.r - d11;
                        } else {
                           if (client.player.isOnGround() && this.k > 0) {
                              if (1.35 * d5 - 0.01 > this.p) {
                                 this.k = 0;
                              } else {
                                 byte b3;
                                 if (MovementUtils.awH()) {
                                    b3 = 1;
                                 } else {
                                    b3 = 0;
                                 }

                                 this.k = b3;
                              }
                           }

                           this.p = this.r - this.r / 159.077F;
                        }
                     } else {
                        double d12;
                        if (this.ly()) {
                           d12 = 0.2;
                        } else {
                           d12 = 0.4;
                        }

                        double d13 = d12 + f2;
                        Managers.playerStateManager.rJ(d13);
                        moveEvent.yo(d13);
                        this.p *= 2.149;
                     }
                  } else {
                     this.p = 2.0 * d5 - 0.01;
                  }

                  if (this.k > 8) {
                     this.p = d5;
                  }

                  this.p = Math.max(this.p, d5);
                  Vec2f vec2f3 = this.lu((float)this.p);
                  moveEvent.ym(vec2f3.x);
                  moveEvent.yq(vec2f3.y);
                  this.k++;
               } else if (this.mode.atx() == SpeedStrafeStrafeStrictMode.B_HOP) {
                  if (!Managers.antiCheatManager.Uh(100L)) {
                     this.k = 4;
                     return;
                  }

                  if (UnidentifiedUtils3.rx(client.player.getY() - (int)client.player.getY(), 3) == UnidentifiedUtils3.rx(0.138, 3)) {
                     Managers.playerStateManager.rJ(client.player.getVelocity().y - (0.08 + f2));
                     moveEvent.yo(moveEvent.yn() - (0.0931 + f2));
                     Managers.vec3dPlayerHelper.BK(client.player.getY() - (0.0931 + f2));
                  }

                  if (this.k != 2 || client.player.forwardSpeed == 0.0F && client.player.sidewaysSpeed == 0.0F) {
                     if (this.k == 3) {
                        double d14 = 0.66 * (this.r - d5);
                        this.p = this.r - d14;
                     } else {
                        if (client.player.isOnGround()) {
                           this.k = 1;
                        }

                        this.p = this.r - this.r / 159.077F;
                     }
                  } else {
                     double d15;
                     if (this.ly()) {
                        d15 = 0.2;
                     } else {
                        d15 = 0.4;
                     }

                     double d16 = d15 + f2;
                     Managers.playerStateManager.rJ(d16);
                     moveEvent.yo(d16);
                     this.p *= 2.149;
                  }

                  this.p = Math.max(this.p, d5);
                  Vec2f vec2f4 = this.lu((float)this.p);
                  moveEvent.ym(vec2f4.x);
                  moveEvent.yq(vec2f4.y);
                  this.k++;
               } else if (this.mode.atx() == SpeedStrafeStrafeStrictMode.VANILLA) {
                  Vec2f vec2f5 = this.lu((Float)this.speed.atx() / 10.0F);
                  moveEvent.ym(vec2f5.x);
                  moveEvent.yq(vec2f5.y);
               }
            } else {
               if (!Managers.antiCheatManager.Uh(100L)) {
                  return;
               }

               if ((Boolean)this.useTimer.atx()) {
                  if ((Boolean)this.fast.atx()) {
                     this.n++;
                     if (this.n > 10) {
                        this.n = 0;
                     }

                     float f5 = 1.0F + this.n / 100.0F;
                     Timer.fP().fT(Math.max(1.0F, f5));
                  } else {
                     Timer.fP().fT(1.0888F);
                  }
               }

               if (this.k == 1) {
                  this.p = 1.35F * d5 - 0.01F;
               } else if (this.k == 2) {
                  if (client.player.input.jumping || !client.player.isOnGround()) {
                     return;
                  }

                  float f6 = 0.39999995F + f2;
                  moveEvent.yo(f6);
                  Managers.playerStateManager.rJ(f6);
                  this.p *= 2.149;
               } else if (this.k == 3) {
                  double d17 = 0.66 * (this.r - d5);
                  this.p = this.r - d17;
               } else {
                  ClientWorld world3 = client.world;
                  ClientPlayerEntity player2 = client.player;
                  Box box2 = client.player.getBoundingBox();
                  ClientPlayerEntity player3 = client.player;
                  Box box3 = box2;
                  ClientPlayerEntity player4 = player2;
                  ClientWorld world4 = world3;
                  double d18 = player3.getVelocity().getY();
                  world3 = world4;
                  Box box4 = box3.offset(0.0, d18, 0.0);
                  if ((!world3.isSpaceEmpty((Entity)player4, box4) || client.player.verticalCollision) && this.k > 0) {
                     byte b4;
                     if (MovementUtils.awH()) {
                        b4 = 1;
                     } else {
                        b4 = 0;
                     }

                     this.k = b4;
                  }

                  this.p = this.r - this.r / 159.077F;
               }

               this.m++;
               this.p = Math.max(this.p, d5);
               if ((Boolean)this.useTimer.atx()) {
                  Timer.fP().fT(1.0888F);
               }

               double d19 = 0.465 * d / d2;
               double d20 = 0.44 * d / d2;
               int n2 = this.m;
               double d21 = this.p;
               double d22;
               if (n2 > 25) {
                  d21 = d19;
                  d22 = d21;
               } else {
                  d21 = d20;
                  d22 = d21;
               }

               this.p = Math.min(d22, d21);
               if ((Boolean)this.strafeBoost.atx()) {
                  this.p = this.p + this.q;
               }

               if (this.m > 50) {
                  this.m = 0;
               }

               Vec2f vec2f6 = this.lu((float)this.p);
               moveEvent.ym(vec2f6.x);
               moveEvent.yq(vec2f6.y);
               this.k++;
            }
         } else {
            if (!Managers.antiCheatManager.Uh(100L)) {
               return;
            }

            if ((Boolean)this.useTimer.atx()) {
               Timer.fP().fT(1.0888F);
            }

            if (this.k == 1) {
               this.p = 1.35F * d5 - 0.01F;
            } else if (this.k == 2) {
               if (client.player.input.jumping || !client.player.isOnGround()) {
                  return;
               }

               float f7;
               if (this.mode.atx() == SpeedStrafeStrafeStrictMode.STRAFE_B_HOP) {
                  f7 = 0.4F;
               } else {
                  f7 = 0.39999995F;
               }

               float f8 = f7 + f2;
               moveEvent.yo(f8);
               Managers.playerStateManager.rJ(f8);
               double temp = this.p;
               Object temp2 = this.mode.atx();
               SpeedStrafeStrafeStrictMode speedStrafeStrafeStrictMode = SpeedStrafeStrafeStrictMode.STRAFE_B_HOP;
               Object obj = temp2;
               double d23 = temp;
               double d24;
               if (obj == speedStrafeStrafeStrictMode) {
                  d23 = 1.535;
                  d24 = d23;
               } else {
                  boolean flag3 = this.l;
                  d23 = d23;
                  if (flag3) {
                     d23 = 1.6835;
                     d24 = d23;
                  } else {
                     d23 = 1.395;
                     d24 = d23;
                  }
               }

               this.p = d24 * d23;
            } else if (this.k == 3) {
               double d25 = 0.66 * (this.r - d5);
               this.p = this.r - d25;
               boolean flag4;
               if (!this.l) {
                  flag4 = true;
               } else {
                  flag4 = false;
               }

               this.l = flag4;
            } else {
               ClientWorld world5 = client.world;
               ClientPlayerEntity player5 = client.player;
               Box box5 = client.player.getBoundingBox();
               ClientPlayerEntity player6 = client.player;
               Box box6 = box5;
               ClientPlayerEntity player7 = player5;
               ClientWorld world6 = world5;
               double d26 = player6.getVelocity().getY();
               world5 = world6;
               Box box7 = box6.offset(0.0, d26, 0.0);
               if ((!world5.isSpaceEmpty((Entity)player7, box7) || client.player.verticalCollision) && this.k > 0) {
                  byte b5;
                  if (MovementUtils.awH()) {
                     b5 = 1;
                  } else {
                     b5 = 0;
                  }

                  this.k = b5;
               }

               this.p = this.r - this.r / 159.077F;
            }

            this.p = Math.max(this.p, d5);
            if ((Boolean)this.strafeBoost.atx()) {
               this.p = this.p + this.q;
            }

            Vec2f vec2f7 = this.lu((float)this.p);
            moveEvent.ym(vec2f7.x);
            moveEvent.yq(vec2f7.y);
            this.k++;
         }
      }
   }

   public Vec2f lu(float f2) {
      float f3 = client.player.input.movementForward;
      float f4 = client.player.input.movementSideways;
      float f5 = client.player.prevYaw + (client.player.getYaw() - client.player.prevYaw) * client.getRenderTickCounter().getTickDelta(true);
      if (f3 == 0.0F && f4 == 0.0F) {
         return Vec2f.ZERO;
      }

      if (f3 != 0.0F) {
         if (f4 >= 1.0F) {
            float f6;
            if (f3 > 0.0F) {
               f6 = -45.0F;
            } else {
               f6 = 45.0F;
            }

            f5 += f6;
            f4 = 0.0F;
         } else if (f4 <= -1.0F) {
            float f7;
            if (f3 > 0.0F) {
               f7 = 45.0F;
            } else {
               f7 = -45.0F;
            }

            f5 += f7;
            f4 = 0.0F;
         }

         if (f3 > 0.0F) {
            f3 = 1.0F;
         } else if (f3 < 0.0F) {
            f3 = -1.0F;
         }
      }

      float f8 = (float)Math.cos(Math.toRadians(f5));
      float f9 = (float)(-Math.sin(Math.toRadians(f5)));
      return new Vec2f(f3 * f2 * f9 + f4 * f2 * f8, f3 * f2 * f8 - f4 * f2 * f9);
   }

   public Vec2f lv(float f2) {
      float f3 = client.player.input.movementForward;
      float f4 = client.player.input.movementSideways;
      if (f3 == 0.0F && f4 == 0.0F) {
         return Vec2f.ZERO;
      }

      if (f3 != 0.0F && f4 != 0.0F) {
         Math.sin(Math.PI / 4);
         f3 *= 0.70710677F;
         Math.cos(Math.PI / 4);
         f4 *= 0.70710677F;
      }

      return new Vec2f(
         (float)(f3 * f2 * -Math.sin(Math.toRadians(client.player.getYaw())) + f4 * f2 * Math.cos(Math.toRadians(client.player.getYaw()))),
         (float)(f3 * f2 * Math.cos(Math.toRadians(client.player.getYaw())) - f4 * f2 * -Math.sin(Math.toRadians(client.player.getYaw())))
      );
   }

   @EventListener
   public void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         if (channelRead0Event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            this.lB();
         }
      }
   }

   @EventListener
   public void lx(UnknownEvent3 unknownEvent3) {
      if (unknownEvent3.getSetting() == this.useTimer && unknownEvent3.getEventPhase() == EventPhase.POST && this.isUseTimerVisible()) {
         if ((Boolean)this.useTimer.atx()) {
            this.s = Timer.fP().awg();
            if (!this.s) {
               Timer.fP().awb();
            }
         } else if (Timer.fP().awg()) {
            Timer.fP().fU();
            if (!this.s) {
               Timer.fP().awc();
            }
         }
      }
   }

   public boolean ly() {
      ClientWorld temp = client.world;
      ClientPlayerEntity temp2 = client.player;
      Box box = client.player.getBoundingBox().offset(0.0, 0.21, 0.0);
      ClientPlayerEntity player2 = temp2;
      boolean flag;
      if (!temp.isSpaceEmpty((Entity)player2, box)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public void lz() {
      boolean flag;
      if (!this.s) {
         flag = true;
      } else {
         flag = false;
      }

      this.s = flag;
   }

   public boolean lA() {
      boolean flag;
      if (this.awg() && (Boolean)this.useTimer.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public void lB() {
      this.k = 4;
      this.m = 0;
      this.n = 0;
      this.p = 0.0;
      this.r = 0.0;
      this.l = false;
   }

   public boolean isUseTimerVisible() {
      boolean flag;
      if (this.mode.atx() != SpeedStrafeStrafeStrictMode.GRIM_COLLIDE && this.mode.atx() != SpeedStrafeStrafeStrictMode.VANILLA) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isBoostTicksVisible() {
      boolean flag;
      if (this.isUseTimerVisible() && (Boolean)this.strafeBoost.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isFastVisible() {
      boolean flag;
      if (this.mode.atx() == SpeedStrafeStrafeStrictMode.STRAFE_STRICT && (Boolean)this.useTimer.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSpeedVisible() {
      boolean flag;
      if (this.mode.atx() == SpeedStrafeStrafeStrictMode.VANILLA) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isCollideSpeedVisible() {
      boolean flag;
      if (this.mode.atx() == SpeedStrafeStrafeStrictMode.GRIM_COLLIDE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isStrictVisible() {
      boolean flag;
      if (this.mode.atx() == SpeedStrafeStrafeStrictMode.GRIM_COLLIDE) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
