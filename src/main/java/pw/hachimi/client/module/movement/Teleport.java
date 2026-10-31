package pw.hachimi.client.module.movement;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.DefaultDegreesMode;
import pw.hachimi.client.model.movement.TeleportClickFreecamMode;
import pw.hachimi.client.model.movement.TeleportPearlSetPositionMode;
import pw.hachimi.client.model.movement.TeleportShortestAccurateMode;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.model.world.SetPositionVanillaMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.KeybindSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper17;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.player.Vec3dPlayerUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.EntityBoxUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public final class Teleport extends ClientModule {
   private static Teleport INSTANCE;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode", TeleportClickFreecamMode.Click, TeleportClickFreecamMode.values()));
   Setting type = this.registerSetting(new EnumSetting("Type", "Teleport type", TeleportPearlSetPositionMode.Vanilla, TeleportPearlSetPositionMode.values()));
   Setting range = this.registerSetting(new NumberSetting("Range", "The range to air place", 0.0F, 40.0F, 200.0F, DefaultDegreesMode.DEFAULT));
   Setting fluids = this.registerSetting(new BooleanSetting("Fluids", "Teleport against fluids", false));
   Setting accuracy = this.registerSetting(new NumberSetting("Accuracy", "Parabolic accuracy", 5.0F, 70.0F, 90.0F, this::isAccuracyVisible));
   Setting priority = this.registerSetting(
      new EnumSetting("Priority", "Priority throwing angle of pearls", TeleportShortestAccurateMode.Shortest, TeleportShortestAccurateMode.values(), this::isPriorityVisible)
   );
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values(), this::isSwapActionVisible));
   Setting swing = this.registerSetting(new EnumSetting("Swing", "Swing side for throw pearl", SwingMode.Normal, SwingMode.values(), this::isSwingVisible));
   Setting yawExtend = this.registerSetting(new NumberSetting("YawExtend", "Yaw extendw", 0.0F, 5.0F, 20.0F, this::isYawExtendVisible));
   Setting minPitch = this.registerSetting(new NumberSetting("MinPitch", "Min pitch", -90.0F, -90.0F, 90.0F, this::isMinPitchVisible));
   Setting maxPitch = this.registerSetting(new NumberSetting("MaxPitch", "Max pitch", -90.0F, 90.0F, 90.0F, this::isMaxPitchVisible));
   Setting key = this.registerSetting(new KeybindSetting("Key", "key", k(this.Kn() + "-key", this::il), this::isKeyVisible));
   public static boolean fc;

   public Teleport() {
      super("Teleport", "Teleport anyway", ModuleCategory.MOVEMENT);
      INSTANCE = this;
      Managers.playerScreenManager.akr((UnidentifiedHelper17)this.key.atx());
   }

   public static Teleport ik() {
      return INSTANCE;
   }

   public void il() {
      if (client.player != null && this.mode.atx() == TeleportClickFreecamMode.Click && this.awg()) {
         ClientPlayerEntity temp = client.player;
         double temp2 = ((Float)this.range.atx()).floatValue();
         boolean flag = (Boolean)this.fluids.atx();
         double d = temp2;
         HitResult hitResult = temp.raycast(d, 1.0F, flag);
         if (hitResult instanceof BlockHitResult) {
            BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
            if (client.world.getBlockState(pos).isAir()) {
               return;
            }

            BlockPos pos2 = ((BlockHitResult)hitResult).getBlockPos();
            Direction direction = Direction.UP;
            this.im(pos2.offset(direction).toBottomCenterPos());
         }
      }
   }

   public void im(Vec3d vec) {
      int n;
      if ((n = ((TeleportPearlSetPositionMode)this.type.atx()).ordinal()) != 0) {
         if (n != 1) {
            if (n != 2) {
               if (n == 3) {
                  Vec3dPlayerUtils.dZ(vec, SetPositionVanillaMode.Path);
               }
            } else {
               Vec3dPlayerUtils.dZ(vec, SetPositionVanillaMode.Vanilla);
            }
         } else {
            Vec3dPlayerUtils.dZ(vec, SetPositionVanillaMode.SetPosition);
         }
      } else {
         StackSlotHelper stackSlotHelper = new StackSlotHelper(Items.ENDER_PEARL, (SwapMode)this.swapAction.atx()).LK();
         if (stackSlotHelper.LL()) {
            float f = Float.NaN;
            float f2 = 0.0F;
            double d = 1.0E13F;
            float f3 = RotationUtils.nN(vec);
            float f4 = 0.0F;
            label378:
            if (0.0F <= (Float)this.yawExtend.atx()) {
               float[] fs;
               int n2;
               int n3;
               float f5;
               float f6;
               Vec3d vec2;
               Iterator iterator2;
               double d2;
               double d3;
               double d4;
               double d5;
               double d6;
               double d7;
               label496: {
                  label495: {
                     label561: {
                        fs = new float[]{1.0F, -1.0F};
                        n2 = fs.length;
                        n3 = 0;
                        if (0 < n2) {
                           float f7 = fs[0];
                           f5 = MathHelper.wrapDegrees(f3 + 0.0F * f7);
                           if (!Float.isNaN(Float.NaN) && Math.abs(Float.NaN - f3) < Math.abs(f5 - Float.NaN)) {
                              while (true) {
                                 if (!(++f4 <= (Float)this.yawExtend.atx())) {
                                    if (!Float.isNaN(Float.NaN)) {
                                       int n4 = Managers.inventoryTracker.JS();
                                       RotationUtils.nH(Float.NaN, 0.0F);
                                       stackSlotHelper.LO(n4);
                                       final float f8 = 0.0F;
                                       final float f9 = Float.NaN;
                                       Managers.serverManager.iQ(new SequencedPacketCreator() {
                                          public Packet predict(int vec) {
                                             return Teleport.io(f9, f8, vec);
                                          }
                                       });
                                       ((SwingMode)this.swing.atx()).avX(Hand.MAIN_HAND);
                                       stackSlotHelper.LP(n4);
                                       RotationUtils.nK();
                                    }

                                    return;
                                 }

                                 fs = new float[]{1.0F, -1.0F};
                                 n2 = fs.length;
                                 n3 = 0;
                                 if (0 < n2) {
                                    f7 = fs[0];
                                    f5 = MathHelper.wrapDegrees(f3 + f4 * f7);
                                    if (Float.isNaN(Float.NaN) || !(Math.abs(Float.NaN - f3) < Math.abs(f5 - Float.NaN))) {
                                       f6 = (Float)this.minPitch.atx();
                                       break;
                                    }
                                 }
                              }
                           } else {
                              f6 = (Float)this.minPitch.atx();
                           }
                        } else {
                           label492:
                           while (true) {
                              if (!(++f4 <= (Float)this.yawExtend.atx())) {
                                 break label378;
                              }

                              fs = new float[]{1.0F, -1.0F};
                              n2 = fs.length;
                              n3 = 0;
                              if (0 < n2) {
                                 float f10 = fs[0];
                                 f5 = MathHelper.wrapDegrees(f3 + f4 * f10);
                                 Float.isNaN(Float.NaN);
                                 f6 = (Float)this.minPitch.atx();
                                 if (f6 <= (Float)this.maxPitch.atx()) {
                                    float f11 = client.getRenderTickCounter().getTickDelta(true);
                                    d2 = EntityBoxUtils.BC(client.player.prevX, client.player.getX(), f11);
                                    d3 = EntityBoxUtils.BC(client.player.prevY, client.player.getY(), f11);
                                    d4 = EntityBoxUtils.BC(client.player.prevZ, client.player.getZ(), f11);
                                    ClientPlayerEntity player2 = client.player;
                                    EntityPose entityPose = client.player.getPose();
                                    d3 = d3 + player2.getEyeHeight(entityPose) - 0.1000000014901161;
                                    d2 -= MathHelper.cos(f5 / 180.0F * (float) Math.PI) * 0.16F;
                                    d4 -= MathHelper.sin(f5 / 180.0F * (float) Math.PI) * 0.16F;
                                    d5 = -MathHelper.sin(f5 / 180.0F * (float) Math.PI) * MathHelper.cos(f6 / 180.0F * (float) Math.PI) * 0.4F;
                                    d6 = -MathHelper.sin(f6 / 180.0F * (float) Math.PI) * 0.4F;
                                    d7 = MathHelper.cos(f5 / 180.0F * (float) Math.PI) * MathHelper.cos(f6 / 180.0F * (float) Math.PI) * 0.4F;
                                    float f12 = MathHelper.sqrt((float)(d5 * d5 + d6 * d6 + d7 * d7));
                                    d5 /= f12;
                                    d6 /= f12;
                                    d7 /= f12;
                                    d5 *= 1.5;
                                    d6 *= 1.5;
                                    d7 *= 1.5;
                                    if (client.player.isOnGround()) {
                                       d5 += client.player.getVelocity().getX();
                                       d7 += client.player.getVelocity().getZ();
                                       vec2 = new Vec3d(d2, d3, d4);
                                       d2 += d5;
                                       d3 += d6;
                                       d4 += d7;
                                       BlockPos pos = BlockPos.ofFloored(d2, d3, d4);
                                       if (client.world.getBlockState(pos).getBlock() != Blocks.WATER) {
                                          d5 *= 0.99;
                                          d6 *= 0.99;
                                          d7 *= 0.99;
                                          d6 -= 0.03F;
                                          Box box = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                                          iterator2 = client.world.getOtherEntities((Entity)null, box).iterator();
                                          break label496;
                                       }
                                       break label495;
                                    }
                                    break label561;
                                 }

                                 while (++n3 < n2) {
                                    f10 = fs[n3];
                                    f5 = MathHelper.wrapDegrees(f3 + f4 * f10);
                                    Float.isNaN(Float.NaN);
                                    f6 = (Float)this.minPitch.atx();
                                    if (f6 <= (Float)this.maxPitch.atx()) {
                                       float f13 = client.getRenderTickCounter().getTickDelta(true);
                                       d2 = EntityBoxUtils.BC(client.player.prevX, client.player.getX(), f13);
                                       d3 = EntityBoxUtils.BC(client.player.prevY, client.player.getY(), f13);
                                       d4 = EntityBoxUtils.BC(client.player.prevZ, client.player.getZ(), f13);
                                       ClientPlayerEntity temp = client.player;
                                       EntityPose entityPose2 = client.player.getPose();
                                       d3 = d3 + temp.getEyeHeight(entityPose2) - 0.1000000014901161;
                                       d2 -= MathHelper.cos(f5 / 180.0F * (float) Math.PI) * 0.16F;
                                       d4 -= MathHelper.sin(f5 / 180.0F * (float) Math.PI) * 0.16F;
                                       d5 = -MathHelper.sin(f5 / 180.0F * (float) Math.PI) * MathHelper.cos(f6 / 180.0F * (float) Math.PI) * 0.4F;
                                       d6 = -MathHelper.sin(f6 / 180.0F * (float) Math.PI) * 0.4F;
                                       d7 = MathHelper.cos(f5 / 180.0F * (float) Math.PI) * MathHelper.cos(f6 / 180.0F * (float) Math.PI) * 0.4F;
                                       float f14 = MathHelper.sqrt((float)(d5 * d5 + d6 * d6 + d7 * d7));
                                       d5 /= f14;
                                       d6 /= f14;
                                       d7 /= f14;
                                       d5 *= 1.5;
                                       d6 *= 1.5;
                                       d7 *= 1.5;
                                       if (!client.player.isOnGround()) {
                                          break label561;
                                       }

                                       d5 += client.player.getVelocity().getX();
                                       d7 += client.player.getVelocity().getZ();

                                       while (true) {
                                          vec2 = new Vec3d(d2, d3, d4);
                                          d2 += d5;
                                          d3 += d6;
                                          d4 += d7;
                                          BlockPos pos2 = BlockPos.ofFloored(d2, d3, d4);
                                          if (client.world.getBlockState(pos2).getBlock() == Blocks.WATER) {
                                             break label495;
                                          }

                                          d5 *= 0.99;
                                          d6 *= 0.99;
                                          d7 *= 0.99;
                                          d6 -= 0.03F;
                                          Box box2 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                                          iterator2 = client.world.getOtherEntities((Entity)null, box2).iterator();
                                          double d8;
                                          double d9;
                                          double d10;
                                          if (!iterator2.hasNext()) {
                                             Vec3d vec3 = new Vec3d(d2, d3, d4);
                                             ClientWorld temp2 = client.world;
                                             RaycastContext raycastContext = new RaycastContext(vec2, vec3, ShapeType.COLLIDER, FluidHandling.NONE, client.player);
                                             BlockHitResult blockHitResult = temp2.raycast(raycastContext);
                                             if (blockHitResult == null || blockHitResult.getType() != Type.BLOCK) {
                                                continue;
                                             }

                                             double d11 = vec2.x - vec.x;
                                             double d12 = vec2.z - vec.z;
                                             d8 = Math.sqrt(d11 * d11 + d12 * d12);
                                             d9 = Math.abs(vec2.y - vec.y);
                                             if (!(vec2.y > vec.y)) {
                                                if (!(d9 > 0.5) && !(d8 > 5.0F * (1.0F - ((Float)this.accuracy.atx() - 1.0F) / 100.0F))) {
                                                   int n5;
                                                   if ((n5 = ((TeleportShortestAccurateMode)this.priority.atx()).ordinal()) == 0) {
                                                      if (Float.isNaN(Float.NaN) || f6 > 0.0F) {
                                                         f = f5;
                                                         f2 = f6;
                                                      }

                                                      f6 += 2.0F;
                                                      break label492;
                                                   }

                                                   if (n5 == 1) {
                                                      if (d8 < 1.0E13F) {
                                                         f = f5;
                                                         f2 = f6;
                                                         d = d8;
                                                      }

                                                      f6 += 2.0F;
                                                      break label492;
                                                   }

                                                   if (n5 == 2) {
                                                      Float.isNaN(Float.NaN);
                                                      f = f5;
                                                      f2 = f6;
                                                      f6 += 2.0F;
                                                      break label492;
                                                   }
                                                }

                                                f6 += 2.0F;
                                                break label492;
                                             }

                                             d10 = 2.0;
                                          } else {
                                             Object obj = iterator2.next();
                                             if (obj == client.player || !((Entity)obj).canHit()) {
                                                break label496;
                                             }

                                             Vec3d vec4 = new Vec3d(d2, d3, d4);
                                             ClientWorld world2 = client.world;
                                             RaycastContext raycastContext2 = new RaycastContext(vec2, vec4, ShapeType.COLLIDER, FluidHandling.NONE, client.player);
                                             BlockHitResult blockHitResult2 = world2.raycast(raycastContext2);
                                             if (blockHitResult2 == null || blockHitResult2.getType() != Type.BLOCK) {
                                                f6 += 2.0F;
                                                break label492;
                                             }

                                             double d13 = vec2.x - vec.x;
                                             double d14 = vec2.z - vec.z;
                                             d8 = Math.sqrt(d13 * d13 + d14 * d14);
                                             d9 = Math.abs(vec2.y - vec.y);
                                             d10 = !(vec2.y > vec.y) ? 0.5 : 2.0;
                                          }

                                          label426:
                                          if (!(d9 > d10) && !(d8 > 5.0F * (1.0F - ((Float)this.accuracy.atx() - 1.0F) / 100.0F))) {
                                             int n6;
                                             if ((n6 = ((TeleportShortestAccurateMode)this.priority.atx()).ordinal()) != 0) {
                                                if (n6 == 1) {
                                                   if (d8 < 1.0E13F) {
                                                      f = f5;
                                                      f2 = f6;
                                                      d = d8;
                                                   }

                                                   f6 += 2.0F;
                                                   break label492;
                                                }

                                                if (n6 != 2 || !Float.isNaN(Float.NaN) && !(f6 < 0.0F)) {
                                                   break label426;
                                                }

                                                f = f5;
                                                f2 = f6;
                                                f6 += 2.0F;
                                                break label492;
                                             }

                                             if (Float.isNaN(Float.NaN) || f6 > 0.0F) {
                                                f = f5;
                                                f2 = f6;
                                             }

                                             f6 += 2.0F;
                                             break label492;
                                          }

                                          f6 += 2.0F;
                                          break label492;
                                       }
                                    }
                                 }
                              }
                           }
                        }

                        label402:
                        while (!(f6 <= (Float)this.maxPitch.atx())) {
                           if (++n3 < n2) {
                              float f15 = fs[n3];
                              f5 = MathHelper.wrapDegrees(f3 + f4 * f15);
                              if (Float.isNaN(f) || !(Math.abs(f - f3) < Math.abs(f5 - f))) {
                                 f6 = (Float)this.minPitch.atx();
                                 continue;
                              }
                           }

                           while (++f4 <= (Float)this.yawExtend.atx()) {
                              fs = new float[]{1.0F, -1.0F};
                              n2 = fs.length;
                              n3 = 0;
                              if (0 < n2) {
                                 float f16 = fs[0];
                                 f5 = MathHelper.wrapDegrees(f3 + f4 * f16);
                                 if (Float.isNaN(f) || !(Math.abs(f - f3) < Math.abs(f5 - f))) {
                                    f6 = (Float)this.minPitch.atx();
                                    continue label402;
                                 }
                              }
                           }

                           if (!Float.isNaN(f)) {
                              int n7 = Managers.inventoryTracker.JS();
                              RotationUtils.nH(f, f2);
                              stackSlotHelper.LO(n7);
                              float f17 = f;
                              float f18 = f2;
                              final float f19 = f18;
                              final float f20 = f17;
                              Managers.serverManager.iQ(new SequencedPacketCreator() {
                                 public Packet predict(int vec) {
                                    return Teleport.io(f20, f19, vec);
                                 }
                              });
                              ((SwingMode)this.swing.atx()).avX(Hand.MAIN_HAND);
                              stackSlotHelper.LP(n7);
                              RotationUtils.nK();
                           }

                           return;
                        }

                        float f21 = client.getRenderTickCounter().getTickDelta(true);
                        d2 = EntityBoxUtils.BC(client.player.prevX, client.player.getX(), f21);
                        d3 = EntityBoxUtils.BC(client.player.prevY, client.player.getY(), f21);
                        d4 = EntityBoxUtils.BC(client.player.prevZ, client.player.getZ(), f21);
                        ClientPlayerEntity player3 = client.player;
                        EntityPose entityPose3 = client.player.getPose();
                        d3 = d3 + player3.getEyeHeight(entityPose3) - 0.1000000014901161;
                        d2 -= MathHelper.cos(f5 / 180.0F * (float) Math.PI) * 0.16F;
                        d4 -= MathHelper.sin(f5 / 180.0F * (float) Math.PI) * 0.16F;
                        d5 = -MathHelper.sin(f5 / 180.0F * (float) Math.PI) * MathHelper.cos(f6 / 180.0F * (float) Math.PI) * 0.4F;
                        d6 = -MathHelper.sin(f6 / 180.0F * (float) Math.PI) * 0.4F;
                        d7 = MathHelper.cos(f5 / 180.0F * (float) Math.PI) * MathHelper.cos(f6 / 180.0F * (float) Math.PI) * 0.4F;
                        float f22 = MathHelper.sqrt((float)(d5 * d5 + d6 * d6 + d7 * d7));
                        d5 /= f22;
                        d6 /= f22;
                        d7 /= f22;
                        d5 *= 1.5;
                        d6 *= 1.5;
                        d7 *= 1.5;
                        if (client.player.isOnGround()) {
                           d5 += client.player.getVelocity().getX();
                           d7 += client.player.getVelocity().getZ();
                           vec2 = new Vec3d(d2, d3, d4);
                           d2 += d5;
                           d3 += d6;
                           d4 += d7;
                           BlockPos pos3 = BlockPos.ofFloored(d2, d3, d4);
                           if (client.world.getBlockState(pos3).getBlock() != Blocks.WATER) {
                              d5 *= 0.99;
                              d6 *= 0.99;
                              d7 *= 0.99;
                              d6 -= 0.03F;
                              Box box3 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                              iterator2 = client.world.getOtherEntities((Entity)null, box3).iterator();
                           } else {
                              d5 *= 0.8;
                              d6 *= 0.8;
                              d7 *= 0.8;
                              d6 -= 0.03F;
                              Box box4 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                              iterator2 = client.world.getOtherEntities((Entity)null, box4).iterator();
                           }
                        } else {
                           d6 += client.player.getVelocity().getY();
                           d5 += client.player.getVelocity().getX();
                           d7 += client.player.getVelocity().getZ();
                           vec2 = new Vec3d(d2, d3, d4);
                           d2 += d5;
                           d3 += d6;
                           d4 += d7;
                           BlockPos pos4 = BlockPos.ofFloored(d2, d3, d4);
                           if (client.world.getBlockState(pos4).getBlock() != Blocks.WATER) {
                              d5 *= 0.99;
                              d6 *= 0.99;
                              d7 *= 0.99;
                              d6 -= 0.03F;
                              Box box5 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                              iterator2 = client.world.getOtherEntities((Entity)null, box5).iterator();
                           } else {
                              d5 *= 0.8;
                              d6 *= 0.8;
                              d7 *= 0.8;
                              d6 -= 0.03F;
                              Box box6 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                              iterator2 = client.world.getOtherEntities((Entity)null, box6).iterator();
                           }
                        }
                        break label496;
                     }

                     d6 += client.player.getVelocity().getY();
                     d5 += client.player.getVelocity().getX();
                     d7 += client.player.getVelocity().getZ();
                     vec2 = new Vec3d(d2, d3, d4);
                     d2 += d5;
                     d3 += d6;
                     d4 += d7;
                     BlockPos pos5 = BlockPos.ofFloored(d2, d3, d4);
                     if (client.world.getBlockState(pos5).getBlock() != Blocks.WATER) {
                        d5 *= 0.99;
                        d6 *= 0.99;
                        d7 *= 0.99;
                        d6 -= 0.03F;
                        Box box7 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                        iterator2 = client.world.getOtherEntities((Entity)null, box7).iterator();
                     } else {
                        d5 *= 0.8;
                        d6 *= 0.8;
                        d7 *= 0.8;
                        d6 -= 0.03F;
                        Box box8 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                        iterator2 = client.world.getOtherEntities((Entity)null, box8).iterator();
                     }
                     break label496;
                  }

                  d5 *= 0.8;
                  d6 *= 0.8;
                  d7 *= 0.8;
                  d6 -= 0.03F;
                  Box box9 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                  iterator2 = client.world.getOtherEntities((Entity)null, box9).iterator();
               }

               while (true) {
                  while (true) {
                     double d15;
                     double d16;
                     double d17;
                     if (!iterator2.hasNext()) {
                        Vec3d vec5 = new Vec3d(d2, d3, d4);
                        ClientWorld world3 = client.world;
                        RaycastContext raycastContext3 = new RaycastContext(vec2, vec5, ShapeType.COLLIDER, FluidHandling.NONE, client.player);
                        BlockHitResult blockHitResult3 = world3.raycast(raycastContext3);
                        if (blockHitResult3 == null || blockHitResult3.getType() != Type.BLOCK) {
                           vec2 = new Vec3d(d2, d3, d4);
                           d2 += d5;
                           d3 += d6;
                           d4 += d7;
                           BlockPos pos6 = BlockPos.ofFloored(d2, d3, d4);
                           if (client.world.getBlockState(pos6).getBlock() != Blocks.WATER) {
                              d5 *= 0.99;
                              d6 *= 0.99;
                              d7 *= 0.99;
                              d6 -= 0.03F;
                              Box box10 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                              iterator2 = client.world.getOtherEntities((Entity)null, box10).iterator();
                           } else {
                              d5 *= 0.8;
                              d6 *= 0.8;
                              d7 *= 0.8;
                              d6 -= 0.03F;
                              Box box11 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                              iterator2 = client.world.getOtherEntities((Entity)null, box11).iterator();
                           }
                           continue;
                        }

                        double d18 = vec2.x - vec.x;
                        double d19 = vec2.z - vec.z;
                        d15 = Math.sqrt(d18 * d18 + d19 * d19);
                        d16 = Math.abs(vec2.y - vec.y);
                        d17 = !(vec2.y > vec.y) ? 0.5 : 2.0;
                     } else {
                        Object obj2 = iterator2.next();
                        if (obj2 == client.player || !((Entity)obj2).canHit()) {
                           continue;
                        }

                        Vec3d vec6 = new Vec3d(d2, d3, d4);
                        ClientWorld world4 = client.world;
                        RaycastContext raycastContext4 = new RaycastContext(vec2, vec6, ShapeType.COLLIDER, FluidHandling.NONE, client.player);
                        BlockHitResult blockHitResult4 = world4.raycast(raycastContext4);
                        if (blockHitResult4 == null || blockHitResult4.getType() != Type.BLOCK) {
                           f6 += 2.0F;
                           break;
                        }

                        double d20 = vec2.x - vec.x;
                        double d21 = vec2.z - vec.z;
                        d15 = Math.sqrt(d20 * d20 + d21 * d21);
                        d16 = Math.abs(vec2.y - vec.y);
                        d17 = !(vec2.y > vec.y) ? 0.5 : 2.0;
                     }

                     label355:
                     if (!(d16 > d17) && !(d15 > 5.0F * (1.0F - ((Float)this.accuracy.atx() - 1.0F) / 100.0F))) {
                        int n8;
                        if ((n8 = ((TeleportShortestAccurateMode)this.priority.atx()).ordinal()) != 0) {
                           if (n8 == 1) {
                              if (d15 < d) {
                                 f = f5;
                                 f2 = f6;
                                 d = d15;
                              }

                              f6 += 2.0F;
                              break;
                           }

                           if (n8 != 2 || !Float.isNaN(f) && !(f6 < f2)) {
                              break label355;
                           }

                           f = f5;
                           f2 = f6;
                           f6 += 2.0F;
                           break;
                        }

                        if (Float.isNaN(f) || f6 > f2) {
                           f = f5;
                           f2 = f6;
                        }

                        f6 += 2.0F;
                        break;
                     }

                     f6 += 2.0F;
                     break;
                  }

                  label336:
                  while (!(f6 <= (Float)this.maxPitch.atx())) {
                     if (++n3 < n2) {
                        float f23 = fs[n3];
                        f5 = MathHelper.wrapDegrees(f3 + f4 * f23);
                        if (Float.isNaN(f) || !(Math.abs(f - f3) < Math.abs(f5 - f))) {
                           f6 = (Float)this.minPitch.atx();
                           continue;
                        }
                     }

                     while (++f4 <= (Float)this.yawExtend.atx()) {
                        fs = new float[]{1.0F, -1.0F};
                        n2 = fs.length;
                        n3 = 0;
                        if (0 < n2) {
                           float f24 = fs[0];
                           f5 = MathHelper.wrapDegrees(f3 + f4 * f24);
                           if (Float.isNaN(f) || !(Math.abs(f - f3) < Math.abs(f5 - f))) {
                              f6 = (Float)this.minPitch.atx();
                              continue label336;
                           }
                        }
                     }

                     if (!Float.isNaN(f)) {
                        int n9 = Managers.inventoryTracker.JS();
                        RotationUtils.nH(f, f2);
                        stackSlotHelper.LO(n9);
                        float f25 = f;
                        float f26 = f2;
                        final float f27 = f26;
                        final float f28 = f25;
                        Managers.serverManager.iQ(new SequencedPacketCreator() {
                           public Packet predict(int vec) {
                              return Teleport.io(f28, f27, vec);
                           }
                        });
                        ((SwingMode)this.swing.atx()).avX(Hand.MAIN_HAND);
                        stackSlotHelper.LP(n9);
                        RotationUtils.nK();
                     }

                     return;
                  }

                  float f29 = client.getRenderTickCounter().getTickDelta(true);
                  d2 = EntityBoxUtils.BC(client.player.prevX, client.player.getX(), f29);
                  d3 = EntityBoxUtils.BC(client.player.prevY, client.player.getY(), f29);
                  d4 = EntityBoxUtils.BC(client.player.prevZ, client.player.getZ(), f29);
                  ClientPlayerEntity player4 = client.player;
                  EntityPose entityPose4 = client.player.getPose();
                  d3 = d3 + player4.getEyeHeight(entityPose4) - 0.1000000014901161;
                  d2 -= MathHelper.cos(f5 / 180.0F * (float) Math.PI) * 0.16F;
                  d4 -= MathHelper.sin(f5 / 180.0F * (float) Math.PI) * 0.16F;
                  d5 = -MathHelper.sin(f5 / 180.0F * (float) Math.PI) * MathHelper.cos(f6 / 180.0F * (float) Math.PI) * 0.4F;
                  d6 = -MathHelper.sin(f6 / 180.0F * (float) Math.PI) * 0.4F;
                  d7 = MathHelper.cos(f5 / 180.0F * (float) Math.PI) * MathHelper.cos(f6 / 180.0F * (float) Math.PI) * 0.4F;
                  float f30 = MathHelper.sqrt((float)(d5 * d5 + d6 * d6 + d7 * d7));
                  d5 /= f30;
                  d6 /= f30;
                  d7 /= f30;
                  d5 *= 1.5;
                  d6 *= 1.5;
                  d7 *= 1.5;
                  if (client.player.isOnGround()) {
                     d5 += client.player.getVelocity().getX();
                     d7 += client.player.getVelocity().getZ();
                     vec2 = new Vec3d(d2, d3, d4);
                     d2 += d5;
                     d3 += d6;
                     d4 += d7;
                     BlockPos pos7 = BlockPos.ofFloored(d2, d3, d4);
                     if (client.world.getBlockState(pos7).getBlock() != Blocks.WATER) {
                        d5 *= 0.99;
                        d6 *= 0.99;
                        d7 *= 0.99;
                        d6 -= 0.03F;
                        Box box12 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                        iterator2 = client.world.getOtherEntities((Entity)null, box12).iterator();
                     } else {
                        d5 *= 0.8;
                        d6 *= 0.8;
                        d7 *= 0.8;
                        d6 -= 0.03F;
                        Box box13 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                        iterator2 = client.world.getOtherEntities((Entity)null, box13).iterator();
                     }
                  } else {
                     d6 += client.player.getVelocity().getY();
                     d5 += client.player.getVelocity().getX();
                     d7 += client.player.getVelocity().getZ();
                     vec2 = new Vec3d(d2, d3, d4);
                     d2 += d5;
                     d3 += d6;
                     d4 += d7;
                     BlockPos pos8 = BlockPos.ofFloored(d2, d3, d4);
                     if (client.world.getBlockState(pos8).getBlock() != Blocks.WATER) {
                        d5 *= 0.99;
                        d6 *= 0.99;
                        d7 *= 0.99;
                        d6 -= 0.03F;
                        Box box14 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                        iterator2 = client.world.getOtherEntities((Entity)null, box14).iterator();
                     } else {
                        d5 *= 0.8;
                        d6 *= 0.8;
                        d7 *= 0.8;
                        d6 -= 0.03F;
                        Box box15 = new Box(d2 - 0.25, d3 - 0.25, d4 - 0.25, d2 + 0.25, d3 + 0.25, d4 + 0.25);
                        iterator2 = client.world.getOtherEntities((Entity)null, box15).iterator();
                     }
                  }
               }
            }

            Float.isNaN(Float.NaN);
         }
      }
   }

   @EventListener
   public void in(Render3DContext render3DContext) {
      if (client.player != null) {
         ClientPlayerEntity temp = client.player;
         double temp2 = ((Float)this.range.atx()).floatValue();
         boolean flag = (Boolean)this.fluids.atx();
         double d = temp2;
         HitResult hitResult = temp.raycast(d, 1.0F, flag);
         if (hitResult instanceof BlockHitResult) {
            BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
            if (client.world.getBlockState(pos).isAir()) {
               return;
            }

            BoxRenderer boxRenderer2 = render3DContext.boxRenderer;
            Color color = ColorHelper3.auO().auW(30);
            BoxRenderer boxRenderer3 = boxRenderer2;
            Color color2 = color;
            Color temp3 = ColorHelper3.auO().auW(145);
            ShapeMode shapeMode = ShapeMode.Both;
            Color color3 = temp3;
            Color color4 = color2;
            boxRenderer3.Zc(pos, color4, color3, shapeMode);
         }
      }
   }

   static Packet io(float f, float f2, int n) {
      return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, f, f2);
   }

   private Boolean isKeyVisible() {
      boolean flag;
      if (this.mode.atx() == TeleportClickFreecamMode.Click) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isMaxPitchVisible() {
      boolean flag;
      if (this.type.atx() == TeleportPearlSetPositionMode.Pearl) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isMinPitchVisible() {
      boolean flag;
      if (this.type.atx() == TeleportPearlSetPositionMode.Pearl) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isYawExtendVisible() {
      boolean flag;
      if (this.type.atx() == TeleportPearlSetPositionMode.Pearl) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSwingVisible() {
      boolean flag;
      if (this.type.atx() == TeleportPearlSetPositionMode.Pearl) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isSwapActionVisible() {
      boolean flag;
      if (this.type.atx() == TeleportPearlSetPositionMode.Pearl) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isPriorityVisible() {
      boolean flag;
      if (this.type.atx() == TeleportPearlSetPositionMode.Pearl) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isAccuracyVisible() {
      boolean flag;
      if (this.type.atx() == TeleportPearlSetPositionMode.Pearl) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static UnidentifiedHelper17 k(String str, Runnable runnable) {
      return new UnidentifiedHelper17(str, 76, runnable);
   }
}
