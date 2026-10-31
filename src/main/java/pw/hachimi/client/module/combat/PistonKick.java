package pw.hachimi.client.module.combat;

import java.util.Iterator;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction.Axis;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper22;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class PistonKick extends Multitask {
   private final Vec3d[] vecs = new Vec3d[]{q(0.3, 0.3), q(0.3, -0.3), q(-0.3, 0.3), q(-0.3, -0.3)};
   Setting multitask = this.registerSetting(new NumberSetting("Delay", "Delay between place", 0, 2, 20));
   Setting delay = this.registerSetting(new BooleanSetting("1.12", "Use 1.12 placement", false));
   Setting targetRange = this.registerSetting(new NumberSetting("TargetRange", "Range of target", 0.0F, 6.0F, 12.0F));
   Setting inAirTarget = this.registerSetting(new BooleanSetting("InAirTarget", "Regard people in the air as targets", true));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotate before placing and breaking", true));
   Setting breakCrystal = this.registerSetting(new BooleanSetting("BreakCrystal", "Break crystal before place", false));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "Interact block reach", 0.0, 4.5, 8.0));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "Break crystal reach", 0.0, 3.0, 8.0));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting outOfBlock = this.registerSetting(new BooleanSetting("OutOfBlock", "Push the enemy out of the burrow", true));
   Setting consecutive = this.registerSetting(new BooleanSetting("Consecutive", "Allow multiple pushes", true, this.outOfBlock::atx));
   Setting merge = this.registerSetting(new BooleanSetting("Merge", "Push enemies from multiple squares into one square", false));
   Setting ignoreNakeds = this.registerSetting(new BooleanSetting("IgnoreNakeds", "Ignore players with no items", false));
   UnidentifiedHelper22 unidentifiedHelper22 = new UnidentifiedHelper22();
   public static boolean cv;

   public PistonKick() {
      super("PistonKick", "Use pistons to push enemies out of their comfort zone", ModuleCategory.COMBAT);
   }

   @EventListener
   public void Aq(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (this.unidentifiedHelper22.afq((Number)this.multitask.atx()) && !Blink.bm() && ((Boolean)this.breakSetting.atx() || !this.RG())) {
         Iterator iterator2 = client.world.getPlayers().iterator();

         label270:
         while (iterator2.hasNext()) {
            Object obj = iterator2.next();
            if (!((AbstractClientPlayerEntity)obj).getAbilities().creativeMode
               && obj != client.player
               && (!(Boolean)this.ignoreNakeds.atx() || !WorldUtils.NO((LivingEntity)obj))
               && ((AbstractClientPlayerEntity)obj).isAlive()
               && !Managers.friendManager.awt(((AbstractClientPlayerEntity)obj).getName())) {
               ClientPlayerEntity player2 = client.player;
               if (!(((AbstractClientPlayerEntity)obj).squaredDistanceTo((Entity)player2) > (Float)this.targetRange.atx() * (Float)this.targetRange.atx())
                  && ((Boolean)this.inAirTarget.atx() || ((AbstractClientPlayerEntity)obj).isOnGround())) {
                  Box box = ((AbstractClientPlayerEntity)obj).getBoundingBox().contract(0.07);
                  boolean flag = CollisionUtils.aiy((Entity)obj, box);
                  if (flag && (Boolean)this.outOfBlock.atx()) {
                     Vec3d[] vecs2 = this.vecs;
                     int n2 = vecs2.length;
                     int n3 = 0;
                     if (0 < n2) {
                        Vec3d vec = vecs2[0];
                        Vec3d vec2 = ((AbstractClientPlayerEntity)obj).getPos().add(vec);
                        Mutable mutable = new Mutable(vec2.x, vec2.y, vec2.z);
                        Direction[] directions = BoxUtils.b;
                        int temp = directions.length;
                        int n4 = 0;

                        label209:
                        while (true) {
                           Direction direction3 = directions[n4];
                           double d = direction3.getAxis() != Axis.X
                              ? ((AbstractClientPlayerEntity)obj).getX()
                              : ((BlockPos)mutable).getX() + 0.5 + direction3.getOffsetX() * 0.8;
                           double d2 = direction3.getAxis() != Axis.Z
                              ? ((AbstractClientPlayerEntity)obj).getZ()
                              : ((BlockPos)mutable).getZ() + 0.5 + direction3.getOffsetZ() * 0.8;
                           if (!CollisionUtils.aiy(
                              client.player,
                              new Box(
                                 d - 0.3,
                                 ((AbstractClientPlayerEntity)obj).getY(),
                                 d2 - 0.3,
                                 d + 0.3,
                                 ((AbstractClientPlayerEntity)obj).getY() + (!((AbstractClientPlayerEntity)obj).isCrawling() ? 1.8 : 0.6),
                                 d2 + 0.3
                              )
                           )) {
                              Direction direction4 = direction3.getOpposite();
                              if (this.Ar(((BlockPos)mutable).offset(direction4), direction3, (Boolean)this.breakCrystal.atx())) {
                                 this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                                 return;
                              }

                              if (!((AbstractClientPlayerEntity)obj).isCrawling()) {
                                 Direction direction5 = direction3.getOpposite();
                                 if (this.Ar(((BlockPos)mutable).offset(direction5).up(), direction3, (Boolean)this.breakCrystal.atx())) {
                                    this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                                    return;
                                 }
                              }
                           }

                           n4++;

                           while (n4 >= 4) {
                              if (++n3 >= n2) {
                                 break label209;
                              }

                              vec = vecs2[n3];
                              vec2 = ((AbstractClientPlayerEntity)obj).getPos().add(vec);
                              mutable = new Mutable(vec2.x, vec2.y, vec2.z);
                              directions = BoxUtils.b;
                              temp = directions.length;
                              n4 = 0;
                           }
                        }
                     }
                  }

                  if (flag && (Boolean)this.outOfBlock.atx() && (Boolean)this.consecutive.atx()) {
                     Vec3d[] vecs3 = this.vecs;
                     int n5 = vecs3.length;
                     int n6 = 0;
                     if (0 < n5) {
                        Vec3d vec3 = vecs3[0];
                        Vec3d vec4 = ((AbstractClientPlayerEntity)obj).getPos().add(vec3);
                        Mutable mutable2 = new Mutable(vec4.x, vec4.y, vec4.z);
                        Direction[] directions2 = BoxUtils.b;
                        int n7 = directions2.length;
                        int n8 = 0;

                        label245:
                        while (true) {
                           Direction direction6 = directions2[n8];
                           double d3 = direction6.getAxis() != Axis.X
                              ? ((AbstractClientPlayerEntity)obj).getX()
                              : ((BlockPos)mutable2).getX() + 0.5 + direction6.getOffsetX() * 0.8;
                           double d4 = direction6.getAxis() != Axis.Z
                              ? ((AbstractClientPlayerEntity)obj).getZ()
                              : ((BlockPos)mutable2).getZ() + 0.5 + direction6.getOffsetZ() * 0.8;
                           Mutable mutable3 = new Mutable(d3, ((BlockPos)mutable2).getY(), d4);
                           Box box2 = new Box(mutable3);
                           Box box3 = ((AbstractClientPlayerEntity)obj).getBoundingBox().contract(0.07);
                           label236:
                           if (box2.intersects(box3)) {
                              if (client.world.getBlockState((BlockPos)mutable3).getPistonBehavior() != PistonBehavior.DESTROY
                                 && !client.world.isAir((BlockPos)mutable3)) {
                                 ClientWorld world2 = client.world;
                                 BlockPos pos2 = ((BlockPos)mutable3).up();
                                 if (world2.getBlockState(pos2).getPistonBehavior() != PistonBehavior.DESTROY) {
                                    ClientWorld world3 = client.world;
                                    BlockPos pos3 = ((BlockPos)mutable3).up();
                                    if (!world3.isAir(pos3)) {
                                       break label236;
                                    }
                                 }
                              }

                              Direction[] directions3 = BoxUtils.b;
                              n7 = directions3.length;
                              int n9 = 0;

                              do {
                                 Direction direction7 = directions3[n9];
                                 if (direction7 != direction6.getOpposite()) {
                                    double d5 = direction7.getAxis() != Axis.X ? d3 : ((BlockPos)mutable3).getX() + 0.5 + direction7.getOffsetX() * 0.8;
                                    double d6 = direction7.getAxis() != Axis.Z ? d4 : ((BlockPos)mutable3).getZ() + 0.5 + direction7.getOffsetZ() * 0.8;
                                    if (!CollisionUtils.aiy(
                                       client.player,
                                       new Box(
                                          d5 - 0.3,
                                          ((AbstractClientPlayerEntity)obj).getY(),
                                          d6 - 0.3,
                                          d5 + 0.3,
                                          ((AbstractClientPlayerEntity)obj).getY() + (!((AbstractClientPlayerEntity)obj).isCrawling() ? 1.8 : 0.6),
                                          d6 + 0.3
                                       )
                                    )) {
                                       Direction direction8 = direction6.getOpposite();
                                       if (this.Ar(((BlockPos)mutable2).offset(direction8), direction6, (Boolean)this.breakCrystal.atx())) {
                                          this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                                          return;
                                       }

                                       if (!((AbstractClientPlayerEntity)obj).isCrawling()) {
                                          Direction direction9 = direction6.getOpposite();
                                          if (this.Ar(((BlockPos)mutable2).offset(direction9).up(), direction6, (Boolean)this.breakCrystal.atx())) {
                                             this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                                             return;
                                          }
                                       }
                                       break;
                                    }
                                 }
                              } while (++n9 < 4);
                           }

                           n8++;

                           while (n8 >= 4) {
                              if (++n6 >= n5) {
                                 break label245;
                              }

                              vec3 = vecs3[n6];
                              vec4 = ((AbstractClientPlayerEntity)obj).getPos().add(vec3);
                              mutable2 = new Mutable(vec4.x, vec4.y, vec4.z);
                              directions2 = BoxUtils.b;
                              n7 = directions2.length;
                              n8 = 0;
                           }
                        }
                     }
                  }

                  if (flag && (Boolean)this.merge.atx()) {
                     Vec3d[] vecs4 = this.vecs;
                     int n10 = vecs4.length;
                     int n11 = 0;
                     if (0 < n10) {
                        Vec3d vec5 = vecs4[0];
                        Vec3d vec6 = ((AbstractClientPlayerEntity)obj).getPos().add(vec5);
                        Mutable mutable4 = new Mutable(vec6.x, vec6.y, vec6.z);
                        Direction[] directions4 = BoxUtils.b;
                        int n12 = directions4.length;
                        int n13 = 0;

                        while (true) {
                           Direction direction10 = directions4[n13];
                           double d7 = direction10.getAxis() != Axis.X
                              ? ((AbstractClientPlayerEntity)obj).getX()
                              : ((BlockPos)mutable4).getX() + 0.5 + direction10.getOffsetX() * 0.8;
                           double d8 = direction10.getAxis() != Axis.Z
                              ? ((AbstractClientPlayerEntity)obj).getZ()
                              : ((BlockPos)mutable4).getZ() + 0.5 + direction10.getOffsetZ() * 0.8;
                           Box box4 = new Box(new Mutable(d7, ((AbstractClientPlayerEntity)obj).getY(), d8));
                           Box box5 = ((AbstractClientPlayerEntity)obj).getBoundingBox().contract(0.07);
                           if (box4.intersects(box5)) {
                              Direction direction11 = direction10.getOpposite();
                              if (this.Ar(((BlockPos)mutable4).offset(direction11), direction10, (Boolean)this.breakCrystal.atx())) {
                                 this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                                 return;
                              }

                              if (!((AbstractClientPlayerEntity)obj).isCrawling()) {
                                 Direction direction12 = direction10.getOpposite();
                                 if (this.Ar(((BlockPos)mutable4).offset(direction12).up(), direction10, (Boolean)this.breakCrystal.atx())) {
                                    this.unidentifiedHelper22.afr(29432, 5265492, (byte)40);
                                    return;
                                 }
                              }
                           }

                           n13++;

                           while (n13 >= 4) {
                              if (++n11 >= n10) {
                                 continue label270;
                              }

                              vec5 = vecs4[n11];
                              vec6 = ((AbstractClientPlayerEntity)obj).getPos().add(vec5);
                              mutable4 = new Mutable(vec6.x, vec6.y, vec6.z);
                              directions4 = BoxUtils.b;
                              n12 = directions4.length;
                              n13 = 0;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean Ar(BlockPos pos2, Direction direction3, boolean flag) {
      ClientWorld temp = client.world;
      BlockPos pos3 = pos2.offset(direction3);
      if (temp.getBlockState(pos3).getPistonBehavior() != PistonBehavior.DESTROY) {
         temp = client.world;
         BlockPos pos4 = pos2.offset(direction3);
         if (!temp.isAir(pos4)) {
            return false;
         }
      }

      Direction[] directions = BoxUtils.a_;
      int n2 = directions.length;
      int n3 = 0;

      while (true) {
         label243: {
            boolean flag2;
            BlockPosBlockItemHelper blockPosBlockItemHelper;
            if (n3 < n2) {
               Direction direction4 = directions[n3];
               if (direction4 == direction3) {
                  break label243;
               }

               temp = client.world;
               BlockPos pos5 = pos2.offset(direction4);
               Block block2 = temp.getBlockState(pos5).getBlock();
               if (block2 != Blocks.REDSTONE_BLOCK && block2 != Blocks.REDSTONE_TORCH) {
                  break label243;
               }

               if (client.world.getBlockState(pos2).getBlock() instanceof PistonBlock) {
                  return false;
               }

               if ((Boolean)this.delay.atx()) {
                  Direction direction5 = BoxUtils.jC(pos2, client.player);
                  if (direction5 == Direction.UP || direction5 == Direction.DOWN) {
                     return false;
                  }
               }

               Predicate predicate = new Predicate() {
                  @Override
                  public boolean test(Object pos2) {
                     return PistonKick.At((ItemStack)pos2);
                  }
               };
               Setting setting2 = this.airPlace;
               Predicate predicate2 = predicate;
               boolean temp2 = (Boolean)setting2.atx();
               setting2 = this.swapAction;
               boolean flag3 = temp2;
               Predicate predicate3 = predicate2;
               blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos2, predicate3, flag3, (SwapMode)setting2.atx());
               blockPosBlockItemHelper.avK((Double)this.placeRange.atx(), flag);
               if (blockPosBlockItemHelper.m && blockPosBlockItemHelper.n && (flag || blockPosBlockItemHelper.o) && blockPosBlockItemHelper.stackSlotHelper.LL()) {
                  flag3 = true;
               } else {
                  flag3 = false;
               }

               if (!flag3) {
                  return false;
               }

               if (blockPosBlockItemHelper.direction == null && blockPosBlockItemHelper.direction2 == null) {
                  flag3 = false;
               } else {
                  flag3 = true;
               }

               flag2 = flag3;
               if (flag2) {
                  if ((Boolean)this.rotate.atx()) {
                     if (blockPosBlockItemHelper.direction2 != null) {
                        Direction direction6 = blockPosBlockItemHelper.direction2;
                        RotationUtils.nJ(pos2.offset(direction6), blockPosBlockItemHelper.direction2.getOpposite());
                     } else {
                        RotationUtils.nJ(pos2, blockPosBlockItemHelper.direction);
                     }
                  }

                  this.As(direction3);
                  blockPosBlockItemHelper.avM((Double)this.placeRange.atx(), false, flag, (Double)this.breakRange.atx());
                  if ((Boolean)this.rotate.atx()) {
                     if (blockPosBlockItemHelper.direction2 != null) {
                        Direction direction7 = blockPosBlockItemHelper.direction2;
                        RotationUtils.nJ(pos2.offset(direction7), blockPosBlockItemHelper.direction2.getOpposite());
                     } else if (blockPosBlockItemHelper.direction != null) {
                        RotationUtils.nJ(pos2, blockPosBlockItemHelper.direction);
                     }
                  }

                  return true;
               }
            } else {
               BlockState state2 = client.world.getBlockState(pos2);
               if (state2.getBlock() instanceof PistonBlock) {
                  BlockState state3 = state2;
                  DirectionProperty directionProperty = FacingBlock.FACING;
                  if (state3.get((Property)directionProperty) == direction3) {
                     Direction[] directions2 = BoxUtils.a_;
                     n3 = directions2.length;

                     for (int n4 = 0; n4 < n3; n4++) {
                        Direction direction8 = directions2[n4];
                        if (direction8 != direction3
                           && (new BlockPosBlockItemHelper(
                                 pos2.offset(direction8), Items.REDSTONE_BLOCK, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                              ))
                              .avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), flag, (Double)this.breakRange.atx())) {
                           return true;
                        }
                     }

                     return false;
                  }

                  return false;
               }

               if ((Boolean)this.delay.atx()) {
                  Direction direction9 = BoxUtils.jC(pos2, client.player);
                  if (direction9 == Direction.UP || direction9 == Direction.DOWN) {
                     return false;
                  }
               }

               Predicate predicate4 = new Predicate() {
                  @Override
                  public boolean test(Object pos2) {
                     return PistonKick.At((ItemStack)pos2);
                  }
               };
               Setting setting3 = this.airPlace;
               Predicate predicate5 = predicate4;
               boolean flag4 = (Boolean)setting3.atx();
               setting3 = this.swapAction;
               boolean flag5 = flag4;
               Predicate predicate6 = predicate5;
               blockPosBlockItemHelper = new BlockPosBlockItemHelper(pos2, predicate6, flag5, (SwapMode)setting3.atx());
               blockPosBlockItemHelper.avK((Double)this.placeRange.atx(), flag);
               if (blockPosBlockItemHelper.m && blockPosBlockItemHelper.n && (flag || blockPosBlockItemHelper.o) && blockPosBlockItemHelper.stackSlotHelper.LL()) {
                  flag5 = true;
               } else {
                  flag5 = false;
               }

               if (!flag5) {
                  return false;
               }

               if (blockPosBlockItemHelper.direction == null && blockPosBlockItemHelper.direction2 == null) {
                  flag5 = false;
               } else {
                  flag5 = true;
               }

               flag2 = flag5;
            }

            Direction[] directions3 = BoxUtils.a_;
            int n5 = directions3.length;

            for (int n6 = 0; n6 < n5; n6++) {
               Direction direction10 = directions3[n6];
               if (direction10 != direction3) {
                  BlockPos temp3 = pos2.offset(direction10);
                  Setting setting4 = this.airPlace;
                  Item item2 = Items.REDSTONE_BLOCK;
                  temp3 = temp3;
                  Item temp4 = item2;
                  boolean temp5 = (Boolean)setting4.atx();
                  setting4 = this.swapAction;
                  boolean flag6 = temp5;
                  item2 = temp4;
                  BlockPosBlockItemHelper blockPosBlockItemHelper2 = new BlockPosBlockItemHelper(temp3, item2, flag6, (SwapMode)setting4.atx());
                  if (blockPosBlockItemHelper2.avK((Double)this.placeRange.atx(), flag)
                     ? flag2 || PlacementUtils.Fr(pos2, direction10)
                     : blockPosBlockItemHelper2.m && blockPosBlockItemHelper2.n && (flag || blockPosBlockItemHelper2.o) && blockPosBlockItemHelper2.stackSlotHelper.LL() && PlacementUtils.Fr(blockPosBlockItemHelper2.pos, direction10.getOpposite()) && flag2) {
                     if (blockPosBlockItemHelper2 != null) {
                        if (!flag2) {
                           blockPosBlockItemHelper2.avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), flag, (Double)this.breakRange.atx());
                           if (!blockPosBlockItemHelper.avK((Double)this.placeRange.atx(), flag)) {
                              return true;
                           }

                           if ((Boolean)this.rotate.atx()) {
                              if (blockPosBlockItemHelper.direction2 != null) {
                                 Direction direction11 = blockPosBlockItemHelper.direction2;
                                 RotationUtils.nJ(pos2.offset(direction11), blockPosBlockItemHelper.direction2.getOpposite());
                              } else if (blockPosBlockItemHelper.direction != null) {
                                 RotationUtils.nJ(pos2, blockPosBlockItemHelper.direction);
                              }
                           }

                           this.As(direction3);
                           blockPosBlockItemHelper.avN(false, flag, (Double)this.breakRange.atx());
                           if ((Boolean)this.rotate.atx()) {
                              if (blockPosBlockItemHelper.direction2 != null) {
                                 Direction direction12 = blockPosBlockItemHelper.direction2;
                                 RotationUtils.nJ(pos2.offset(direction12), blockPosBlockItemHelper.direction2.getOpposite());
                              } else if (blockPosBlockItemHelper.direction != null) {
                                 RotationUtils.nJ(pos2, blockPosBlockItemHelper.direction);
                              }
                           }
                        } else {
                           if ((Boolean)this.rotate.atx()) {
                              if (blockPosBlockItemHelper.direction2 != null) {
                                 Direction direction13 = blockPosBlockItemHelper.direction2;
                                 RotationUtils.nJ(pos2.offset(direction13), blockPosBlockItemHelper.direction2.getOpposite());
                              } else {
                                 RotationUtils.nJ(pos2, blockPosBlockItemHelper.direction);
                              }
                           }

                           this.As(direction3);
                           blockPosBlockItemHelper.avN(false, flag, (Double)this.breakRange.atx());
                           if ((Boolean)this.rotate.atx()) {
                              if (blockPosBlockItemHelper.direction2 != null) {
                                 Direction direction14 = blockPosBlockItemHelper.direction2;
                                 RotationUtils.nJ(pos2.offset(direction14), blockPosBlockItemHelper.direction2.getOpposite());
                              } else {
                                 RotationUtils.nJ(pos2, blockPosBlockItemHelper.direction);
                              }
                           }

                           blockPosBlockItemHelper2.avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), flag, (Double)this.breakRange.atx());
                        }

                        return true;
                     }
                     break;
                  }
               }
            }

            return false;
         }

         n3++;
      }
   }

   private void As(Direction direction3) {
      Direction direction4 = direction3.getOpposite();
      if (direction4 == Direction.EAST) {
         RotationUtils.nH(-90.0F, 5.0F);
      } else if (direction4 == Direction.WEST) {
         RotationUtils.nH(90.0F, 5.0F);
      } else if (direction4 == Direction.NORTH) {
         RotationUtils.nH(180.0F, 5.0F);
      } else if (direction4 == Direction.SOUTH) {
         RotationUtils.nH(0.0F, 5.0F);
      }
   }

   static boolean At(ItemStack stack) {
      Item item2 = stack.getItem();
      boolean flag;
      if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof PistonBlock) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static Vec3d q(double d, double d2) {
      return new Vec3d(d, 0.0, d2);
   }
}
