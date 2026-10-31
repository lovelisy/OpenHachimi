package pw.hachimi.client.module.combat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.world.PosNeedPlaceSide;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.Stopwatch;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.client.world.ProjectileEntityVec3dProjectileEntity;
import pw.hachimi.eventbus.annotation.EventListener;

public class PearlBlocker extends Multitask {
   Setting numberSetting1 = this.registerSetting(new NumberSetting("ExtrapolationTicks", "Accounts for motion when calculating ender pearl positions", 0, 20, 500));
   Setting numberSetting2 = this.registerSetting(new NumberSetting("MinExtrapolationTicks", "Min motion predict ticks", 0, 4, 20));
   Setting numberSetting3 = this.registerSetting(new NumberSetting("Delay", "Delay between placements (ms)", 0, 60, 1000));
   Setting bpt = this.registerSetting(new NumberSetting("BPT", "in places, number of blocks", 1, 1, 8));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Rotates to block before placing", false));
   Setting pearlRange = this.registerSetting(new NumberSetting("PearlRange", "Valid ender pearl distance", 0.0, 4.5, 6.0));
   Setting ignoreRange = this.registerSetting(new NumberSetting("IgnoreRange", "Ignore ender pearl distance", 0.0, 0.0, 6.0));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "The distance of your block reach", 0.0, 4.5, 6.0));
   Setting breakCrystal = this.registerSetting(new BooleanSetting("BreakCrystal", "Break crystals when placing blocks", true));
   Setting breakRange = this.registerSetting(new NumberSetting("BreakRange", "The distance of your entity reach", 0.0, 3.0, 6.0));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting support = this.registerSetting(new NumberSetting("Support", "The maximum number of blocks for helper can place", 0, 2, 8));
   Setting multi = this.registerSetting(new BooleanSetting("Multi", "Allow multiple assignblocks", true, this::isMultiVisible));
   Setting ignoreFriends = this.registerSetting(new BooleanSetting("IgnoreFriends", "Ignore friend's ender pearl", true));
   private final Map map = new HashMap();
   private final Stopwatch stopwatch = new StopwatchImpl();
   public static boolean cd;

   public PearlBlocker() {
      super("PearlBlocker", "Block enemy's ender pearl", ModuleCategory.COMBAT);
   }

   @Override
   public void awe() {
      this.map.clear();
   }

   @EventListener
   public void avs(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (((Boolean)this.breakSetting.atx() || !this.RG()) && this.stopwatch.afq((Number)this.numberSetting3.atx())) {
         Set temp = this.map.entrySet();
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object tickEntitiesPreEvent) {
               return PearlBlocker.avt((Entry)tickEntitiesPreEvent);
            }
         };
         temp.removeIf(predicate);
         int n2 = 0;
         Vec3d vec = client.player.getEyePos();
         Iterator iterator2 = client.world.getEntities().iterator();

         label796:
         while (iterator2.hasNext()) {
            Object obj = iterator2.next();
            if (n2 <= (Integer)this.bpt.atx()) {
               if (!(obj instanceof EnderPearlEntity)) {
                  continue;
               }

               Entity entity2 = ((EnderPearlEntity)obj).getOwner();
               if (!(entity2 instanceof PlayerEntity)
                  || entity2 == client.player
                  || (Boolean)this.ignoreFriends.atx() && Managers.friendManager.awr((PlayerEntity)entity2)) {
                  continue;
               }

               BlockPos pos;
               BlockPosBlockItemHelper blockPosBlockItemHelper;
               label790: {
                  label789: {
                     label851: {
                        List list;
                        ProjectileEntityVec3dProjectileEntity projectileEntityVec3dProjectileEntity;
                        int n3;
                        label787: {
                           Mutable mutable;
                           BlockPosBlockItemHelper blockPosBlockItemHelper2;
                           List list2;
                           label805: {
                              label806: {
                                 projectileEntityVec3dProjectileEntity = new ProjectileEntityVec3dProjectileEntity((ProjectileEntity)obj);
                                 if (this.map.containsKey(obj)) {
                                    for (n3 = (Integer)this.numberSetting2.atx(); n3 < (Integer)this.numberSetting1.atx(); n3++) {
                                       HitResult hitResult = projectileEntityVec3dProjectileEntity.ajv();
                                       if (hitResult != null) {
                                          Vec3d vec2 = ((BlockPos)this.map.get(obj)).toCenterPos();
                                          Vec3d vec3 = hitResult.getPos();
                                          if (vec2.distanceTo(vec3) < 1.0) {
                                             pos = BlockPos.ORIGIN;
                                             if (pos != null) {
                                                break label789;
                                             }

                                             n3 = (Integer)this.numberSetting2.atx();

                                             while (true) {
                                                if (n3 >= (Integer)this.numberSetting1.atx()) {
                                                   break label789;
                                                }

                                                hitResult = projectileEntityVec3dProjectileEntity.ajv();
                                                if (hitResult != null) {
                                                   if (hitResult.getPos().distanceTo(vec) <= (Double)this.ignoreRange.atx()) {
                                                      continue label796;
                                                   }
                                                   break label789;
                                                }

                                                Vec3d vec4 = new Vec3d(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                                label771:
                                                if (!(vec4.distanceTo(vec) > (Double)this.pearlRange.atx())) {
                                                   mutable = new Mutable(vec4.x, vec4.y, vec4.z);
                                                   if (pos != null) {
                                                      Vec3d vec5 = ((BlockPos)mutable).toCenterPos();
                                                      double d = vec.distanceTo(vec5);
                                                      Vec3d vec6 = pos.toCenterPos();
                                                      if (!(d < vec.distanceTo(vec6))) {
                                                         break label771;
                                                      }
                                                   }

                                                   blockPosBlockItemHelper2 = new BlockPosBlockItemHelper(
                                                      mutable, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                                   );
                                                   if (blockPosBlockItemHelper2.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                                      break label806;
                                                   }

                                                   if (pos == null && blockPosBlockItemHelper2.m && blockPosBlockItemHelper2.n && (blockPosBlockItemHelper2.h || blockPosBlockItemHelper2.o) && blockPosBlockItemHelper2.stackSlotHelper.LL()) {
                                                      list2 = PlacementUtils.Fs(mutable, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                                      if (!list2.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list2.getLast()).getPos())) {
                                                         break label805;
                                                      }
                                                   }
                                                }

                                                n3++;
                                             }
                                          }
                                          break;
                                       }

                                       Mutable mutable2 = new Mutable(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                       if (((BlockPos)this.map.get(obj)).equals(mutable2)) {
                                          label837: {
                                             BlockPosBlockItemHelper blockPosBlockItemHelper3 = new BlockPosBlockItemHelper(
                                                mutable2, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                             );
                                             if (!blockPosBlockItemHelper3.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                                if (!blockPosBlockItemHelper3.m || !blockPosBlockItemHelper3.n || !blockPosBlockItemHelper3.h && !blockPosBlockItemHelper3.o || !blockPosBlockItemHelper3.stackSlotHelper.LL()) {
                                                   continue;
                                                }

                                                List list3 = PlacementUtils.Fs(mutable2, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                                if (list3.isEmpty() || !CollisionUtils.aiD(((PosNeedPlaceSide)list3.getLast()).getPos())) {
                                                   continue;
                                                }

                                                label729:
                                                while (true) {
                                                   pos = mutable2;
                                                   blockPosBlockItemHelper = blockPosBlockItemHelper3;
                                                   list = list3;

                                                   while (true) {
                                                      if (++n3 >= (Integer)this.numberSetting1.atx()) {
                                                         break label837;
                                                      }

                                                      hitResult = projectileEntityVec3dProjectileEntity.ajv();
                                                      if (hitResult != null) {
                                                         Vec3d vec7 = ((BlockPos)this.map.get(obj)).toCenterPos();
                                                         Vec3d vec8 = hitResult.getPos();
                                                         if (vec7.distanceTo(vec8) < 1.0) {
                                                            pos = BlockPos.ORIGIN;
                                                         }
                                                         break label837;
                                                      }

                                                      mutable2 = new Mutable(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                                      if (((BlockPos)this.map.get(obj)).equals(mutable2)) {
                                                         blockPosBlockItemHelper3 = new BlockPosBlockItemHelper(
                                                            mutable2, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                                         );
                                                         if (blockPosBlockItemHelper3.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                                            break label729;
                                                         }

                                                         if ((pos == null || list != null)
                                                            && blockPosBlockItemHelper3.m
                                                            && blockPosBlockItemHelper3.n
                                                            && (blockPosBlockItemHelper3.h || blockPosBlockItemHelper3.o)
                                                            && blockPosBlockItemHelper3.stackSlotHelper.LL()) {
                                                            list3 = PlacementUtils.Fs(mutable2, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                                            if (!list3.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list3.getLast()).getPos())) {
                                                               break;
                                                            }
                                                         }
                                                      }
                                                   }
                                                }
                                             }

                                             label667:
                                             while (true) {
                                                pos = mutable2;
                                                blockPosBlockItemHelper = blockPosBlockItemHelper3;

                                                while (++n3 < (Integer)this.numberSetting1.atx()) {
                                                   hitResult = projectileEntityVec3dProjectileEntity.ajv();
                                                   if (hitResult != null) {
                                                      Vec3d vec9 = ((BlockPos)this.map.get(obj)).toCenterPos();
                                                      Vec3d vec10 = hitResult.getPos();
                                                      if (vec9.distanceTo(vec10) < 1.0) {
                                                         pos = BlockPos.ORIGIN;
                                                      }
                                                      break;
                                                   }

                                                   mutable2 = new Mutable(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                                   if (((BlockPos)this.map.get(obj)).equals(mutable2)) {
                                                      blockPosBlockItemHelper3 = new BlockPosBlockItemHelper(
                                                         mutable2, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                                      );
                                                      if (blockPosBlockItemHelper3.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                                         continue label667;
                                                      }

                                                      if (pos == null && blockPosBlockItemHelper3.m && blockPosBlockItemHelper3.n && (blockPosBlockItemHelper3.h || blockPosBlockItemHelper3.o) && blockPosBlockItemHelper3.stackSlotHelper.LL()) {
                                                         List list4 = PlacementUtils.Fs(mutable2, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                                         if (!list4.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list4.getLast()).getPos())) {
                                                            label642:
                                                            while (true) {
                                                               pos = mutable2;
                                                               blockPosBlockItemHelper = blockPosBlockItemHelper3;
                                                               list = list4;

                                                               while (true) {
                                                                  if (++n3 >= (Integer)this.numberSetting1.atx()) {
                                                                     break label642;
                                                                  }

                                                                  hitResult = projectileEntityVec3dProjectileEntity.ajv();
                                                                  if (hitResult != null) {
                                                                     Vec3d vec11 = ((BlockPos)this.map.get(obj)).toCenterPos();
                                                                     Vec3d vec12 = hitResult.getPos();
                                                                     if (vec11.distanceTo(vec12) < 1.0) {
                                                                        pos = BlockPos.ORIGIN;
                                                                     }
                                                                     break label642;
                                                                  }

                                                                  mutable2 = new Mutable(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                                                  if (((BlockPos)this.map.get(obj)).equals(mutable2)) {
                                                                     blockPosBlockItemHelper3 = new BlockPosBlockItemHelper(
                                                                        mutable2, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                                                     );
                                                                     if (blockPosBlockItemHelper3.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                                                        continue label667;
                                                                     }

                                                                     if ((pos == null || list != null)
                                                                        && blockPosBlockItemHelper3.m
                                                                        && blockPosBlockItemHelper3.n
                                                                        && (blockPosBlockItemHelper3.h || blockPosBlockItemHelper3.o)
                                                                        && blockPosBlockItemHelper3.stackSlotHelper.LL()) {
                                                                        list4 = PlacementUtils.Fs(
                                                                           mutable2, (Double)this.placeRange.atx(), (Integer)this.support.atx()
                                                                        );
                                                                        if (!list4.isEmpty()
                                                                           && CollisionUtils.aiD(((PosNeedPlaceSide)list4.getLast()).getPos())) {
                                                                           break;
                                                                        }
                                                                     }
                                                                  }
                                                               }
                                                            }

                                                            if (pos != null) {
                                                               if (pos == null || blockPosBlockItemHelper == null) {
                                                                  continue label796;
                                                               }

                                                               Object obj2 = pos;
                                                               this.map.put(obj, obj2);
                                                               this.stopwatch.afr(29432, 5265492, (byte)40);
                                                               if (list == null) {
                                                                  blockPosBlockItemHelper.avN(
                                                                     (Boolean)this.rotate.atx(),
                                                                     (Boolean)this.breakCrystal.atx(),
                                                                     (Double)this.breakRange.atx()
                                                                  );
                                                                  n2++;
                                                                  continue label796;
                                                               }

                                                               if (!(Boolean)this.multi.atx()) {
                                                                  new BlockPosBlockItemHelper(
                                                                        ((PosNeedPlaceSide)list.getFirst()).getPos(),
                                                                        Items.OBSIDIAN,
                                                                        (Boolean)this.airPlace.atx(),
                                                                        (SwapMode)this.swapAction.atx()
                                                                     )
                                                                     .avM(
                                                                        (Double)this.placeRange.atx(),
                                                                        (Boolean)this.rotate.atx(),
                                                                        (Boolean)this.breakCrystal.atx(),
                                                                        (Double)this.breakRange.atx()
                                                                     );
                                                                  if (++n2 > (Integer)this.bpt.atx()) {
                                                                     return;
                                                                  }
                                                               } else {
                                                                  Iterator iterator3 = list.iterator();

                                                                  while (iterator3.hasNext()) {
                                                                     Object obj3 = iterator3.next();
                                                                     new BlockPosBlockItemHelper(
                                                                           ((PosNeedPlaceSide)obj3).getPos(),
                                                                           Items.OBSIDIAN,
                                                                           (Boolean)this.airPlace.atx(),
                                                                           (SwapMode)this.swapAction.atx()
                                                                        )
                                                                        .avM(
                                                                           (Double)this.placeRange.atx(),
                                                                           (Boolean)this.rotate.atx(),
                                                                           (Boolean)this.breakCrystal.atx(),
                                                                           (Double)this.breakRange.atx()
                                                                        );
                                                                     if (++n2 > (Integer)this.bpt.atx()) {
                                                                        return;
                                                                     }
                                                                  }
                                                               }

                                                               blockPosBlockItemHelper.avM(
                                                                  (Double)this.placeRange.atx(),
                                                                  (Boolean)this.rotate.atx(),
                                                                  (Boolean)this.breakCrystal.atx(),
                                                                  (Double)this.breakRange.atx()
                                                               );
                                                               if (++n2 <= (Integer)this.bpt.atx()) {
                                                                  continue label796;
                                                               }

                                                               return;
                                                            }

                                                            n3 = (Integer)this.numberSetting2.atx();
                                                            break label787;
                                                         }
                                                      }
                                                   }
                                                }

                                                if (pos != null) {
                                                   if (pos != null && blockPosBlockItemHelper != null) {
                                                      Object obj4 = pos;
                                                      this.map.put(obj, obj4);
                                                      this.stopwatch.afr(29432, 5265492, (byte)40);
                                                      blockPosBlockItemHelper.avN((Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
                                                      n2++;
                                                   }
                                                   continue label796;
                                                }

                                                n3 = (Integer)this.numberSetting2.atx();

                                                while (true) {
                                                   if (n3 >= (Integer)this.numberSetting1.atx()) {
                                                      break label790;
                                                   }

                                                   hitResult = projectileEntityVec3dProjectileEntity.ajv();
                                                   if (hitResult != null) {
                                                      if (hitResult.getPos().distanceTo(vec) <= (Double)this.ignoreRange.atx()) {
                                                         continue label796;
                                                      }
                                                      break label790;
                                                   }

                                                   Vec3d vec13 = new Vec3d(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                                   label612:
                                                   if (!(vec13.distanceTo(vec) > (Double)this.pearlRange.atx())) {
                                                      mutable = new Mutable(vec13.x, vec13.y, vec13.z);
                                                      if (pos != null) {
                                                         Vec3d vec14 = ((BlockPos)mutable).toCenterPos();
                                                         double d2 = vec.distanceTo(vec14);
                                                         Vec3d vec15 = pos.toCenterPos();
                                                         if (!(d2 < vec.distanceTo(vec15))) {
                                                            break label612;
                                                         }
                                                      }

                                                      blockPosBlockItemHelper2 = new BlockPosBlockItemHelper(
                                                         mutable, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                                      );
                                                      if (!blockPosBlockItemHelper2.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                                         if (pos == null && blockPosBlockItemHelper2.m && blockPosBlockItemHelper2.n && (blockPosBlockItemHelper2.h || blockPosBlockItemHelper2.o) && blockPosBlockItemHelper2.stackSlotHelper.LL()) {
                                                            list2 = PlacementUtils.Fs(mutable, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                                            if (!list2.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list2.getLast()).getPos())) {
                                                               pos = mutable;
                                                               blockPosBlockItemHelper = blockPosBlockItemHelper2;
                                                               list = list2;
                                                               n3++;
                                                               break label787;
                                                            }
                                                         }
                                                      } else {
                                                         pos = mutable;
                                                         blockPosBlockItemHelper = blockPosBlockItemHelper2;
                                                      }
                                                   }

                                                   n3++;
                                                }
                                             }
                                          }

                                          if (pos != null) {
                                             if (pos == null || blockPosBlockItemHelper == null) {
                                                break;
                                             }

                                             Object obj5 = pos;
                                             this.map.put(obj, obj5);
                                             this.stopwatch.afr(29432, 5265492, (byte)40);
                                             if (list == null) {
                                                blockPosBlockItemHelper.avN((Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
                                                n2++;
                                                break;
                                             }

                                             if (!(Boolean)this.multi.atx()) {
                                                new BlockPosBlockItemHelper(
                                                      ((PosNeedPlaceSide)list.getFirst()).getPos(),
                                                      Items.OBSIDIAN,
                                                      (Boolean)this.airPlace.atx(),
                                                      (SwapMode)this.swapAction.atx()
                                                   )
                                                   .avM(
                                                      (Double)this.placeRange.atx(),
                                                      (Boolean)this.rotate.atx(),
                                                      (Boolean)this.breakCrystal.atx(),
                                                      (Double)this.breakRange.atx()
                                                   );
                                                if (++n2 > (Integer)this.bpt.atx()) {
                                                   return;
                                                }
                                             } else {
                                                Iterator iterator4 = list.iterator();

                                                while (iterator4.hasNext()) {
                                                   Object obj6 = iterator4.next();
                                                   new BlockPosBlockItemHelper(
                                                         ((PosNeedPlaceSide)obj6).getPos(),
                                                         Items.OBSIDIAN,
                                                         (Boolean)this.airPlace.atx(),
                                                         (SwapMode)this.swapAction.atx()
                                                      )
                                                      .avM(
                                                         (Double)this.placeRange.atx(),
                                                         (Boolean)this.rotate.atx(),
                                                         (Boolean)this.breakCrystal.atx(),
                                                         (Double)this.breakRange.atx()
                                                      );
                                                   if (++n2 > (Integer)this.bpt.atx()) {
                                                      return;
                                                   }
                                                }
                                             }

                                             blockPosBlockItemHelper.avM(
                                                (Double)this.placeRange.atx(),
                                                (Boolean)this.rotate.atx(),
                                                (Boolean)this.breakCrystal.atx(),
                                                (Double)this.breakRange.atx()
                                             );
                                             if (++n2 <= (Integer)this.bpt.atx()) {
                                                break;
                                             }

                                             return;
                                          }

                                          n3 = (Integer)this.numberSetting2.atx();
                                          break label787;
                                       }
                                    }
                                 }

                                 for (n3 = (Integer)this.numberSetting2.atx(); n3 < (Integer)this.numberSetting1.atx(); n3++) {
                                    HitResult hitResult2 = projectileEntityVec3dProjectileEntity.ajv();
                                    if (hitResult2 != null) {
                                       if (!(hitResult2.getPos().distanceTo(vec) <= (Double)this.ignoreRange.atx())) {
                                       }
                                       continue label796;
                                    }

                                    Vec3d vec16 = new Vec3d(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                    if (!(vec16.distanceTo(vec) > (Double)this.pearlRange.atx())) {
                                       mutable = new Mutable(vec16.x, vec16.y, vec16.z);
                                       blockPosBlockItemHelper2 = new BlockPosBlockItemHelper(
                                          mutable, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                       );
                                       if (blockPosBlockItemHelper2.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                          break label806;
                                       }

                                       if (blockPosBlockItemHelper2.m && blockPosBlockItemHelper2.n && (blockPosBlockItemHelper2.h || blockPosBlockItemHelper2.o) && blockPosBlockItemHelper2.stackSlotHelper.LL()) {
                                          list2 = PlacementUtils.Fs(mutable, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                          if (!list2.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list2.getLast()).getPos())) {
                                             break label805;
                                          }
                                       }
                                    }
                                 }
                                 continue;
                              }

                              pos = mutable;
                              blockPosBlockItemHelper = blockPosBlockItemHelper2;
                              n3++;

                              while (true) {
                                 if (n3 >= (Integer)this.numberSetting1.atx()) {
                                    break label851;
                                 }

                                 HitResult hitResult3 = projectileEntityVec3dProjectileEntity.ajv();
                                 if (hitResult3 != null) {
                                    if (hitResult3.getPos().distanceTo(vec) <= (Double)this.ignoreRange.atx()) {
                                       continue label796;
                                    }
                                    break label851;
                                 }

                                 Vec3d vec17 = new Vec3d(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                 label681:
                                 if (!(vec17.distanceTo(vec) > (Double)this.pearlRange.atx())) {
                                    mutable = new Mutable(vec17.x, vec17.y, vec17.z);
                                    if (pos != null) {
                                       Vec3d vec18 = ((BlockPos)mutable).toCenterPos();
                                       double d3 = vec.distanceTo(vec18);
                                       Vec3d vec19 = pos.toCenterPos();
                                       if (!(d3 < vec.distanceTo(vec19))) {
                                          break label681;
                                       }
                                    }

                                    blockPosBlockItemHelper2 = new BlockPosBlockItemHelper(mutable, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx());
                                    if (!blockPosBlockItemHelper2.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                       if (pos == null && blockPosBlockItemHelper2.m && blockPosBlockItemHelper2.n && (blockPosBlockItemHelper2.h || blockPosBlockItemHelper2.o) && blockPosBlockItemHelper2.stackSlotHelper.LL()) {
                                          list2 = PlacementUtils.Fs(mutable, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                          if (!list2.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list2.getLast()).getPos())) {
                                             pos = mutable;
                                             blockPosBlockItemHelper = blockPosBlockItemHelper2;
                                             list = list2;
                                             n3++;
                                             break label787;
                                          }
                                       }
                                    } else {
                                       pos = mutable;
                                       blockPosBlockItemHelper = blockPosBlockItemHelper2;
                                    }
                                 }

                                 n3++;
                              }
                           }

                           pos = mutable;
                           blockPosBlockItemHelper = blockPosBlockItemHelper2;
                           list = list2;
                           n3++;
                        }

                        label576:
                        while (n3 < (Integer)this.numberSetting1.atx()) {
                           HitResult hitResult4 = projectileEntityVec3dProjectileEntity.ajv();
                           if (hitResult4 != null) {
                              if (hitResult4.getPos().distanceTo(vec) <= (Double)this.ignoreRange.atx()) {
                                 continue label796;
                              }
                              break;
                           }

                           Vec3d vec20 = new Vec3d(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                           label570:
                           if (!(vec20.distanceTo(vec) > (Double)this.pearlRange.atx())) {
                              Mutable mutable3 = new Mutable(vec20.x, vec20.y, vec20.z);
                              if (pos != null) {
                                 Vec3d vec21 = ((BlockPos)mutable3).toCenterPos();
                                 double d4 = vec.distanceTo(vec21);
                                 Vec3d vec22 = pos.toCenterPos();
                                 if (!(d4 < vec.distanceTo(vec22))) {
                                    break label570;
                                 }
                              }

                              BlockPosBlockItemHelper blockPosBlockItemHelper4 = new BlockPosBlockItemHelper(
                                 mutable3, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                              );
                              if (blockPosBlockItemHelper4.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                 Mutable mutable4 = mutable3;
                                 blockPosBlockItemHelper = blockPosBlockItemHelper4;
                                 n3++;

                                 for (; n3 < (Integer)this.numberSetting1.atx(); n3++) {
                                    hitResult4 = projectileEntityVec3dProjectileEntity.ajv();
                                    if (hitResult4 != null) {
                                       if (hitResult4.getPos().distanceTo(vec) <= (Double)this.ignoreRange.atx()) {
                                          continue label796;
                                       }
                                       break;
                                    }

                                    vec20 = new Vec3d(projectileEntityVec3dProjectileEntity.prevX, projectileEntityVec3dProjectileEntity.prevY, projectileEntityVec3dProjectileEntity.prevZ);
                                    if (!(vec20.distanceTo(vec) > (Double)this.pearlRange.atx())) {
                                       mutable3 = new Mutable(vec20.x, vec20.y, vec20.z);
                                       if (mutable4 != null) {
                                          Vec3d vec23 = ((BlockPos)mutable3).toCenterPos();
                                          double d5 = vec.distanceTo(vec23);
                                          Vec3d vec24 = ((BlockPos)mutable4).toCenterPos();
                                          if (!(d5 < vec.distanceTo(vec24))) {
                                             continue;
                                          }
                                       }

                                       blockPosBlockItemHelper4 = new BlockPosBlockItemHelper(
                                          mutable3, Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                       );
                                       if (!blockPosBlockItemHelper4.avK((Double)this.placeRange.atx(), (Boolean)this.breakCrystal.atx())) {
                                          if (mutable4 == null && blockPosBlockItemHelper4.m && blockPosBlockItemHelper4.n && (blockPosBlockItemHelper4.h || blockPosBlockItemHelper4.o) && blockPosBlockItemHelper4.stackSlotHelper.LL()) {
                                             List list5 = PlacementUtils.Fs(mutable3, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                             if (!list5.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list5.getLast()).getPos())) {
                                                pos = mutable3;
                                                blockPosBlockItemHelper = blockPosBlockItemHelper4;
                                                list = list5;
                                                n3++;
                                                continue label576;
                                             }
                                          }
                                       } else {
                                          mutable4 = mutable3;
                                          blockPosBlockItemHelper = blockPosBlockItemHelper4;
                                       }
                                    }
                                 }

                                 if (mutable4 != null && blockPosBlockItemHelper != null) {
                                    Mutable mutable5 = mutable4;
                                    this.map.put(obj, mutable5);
                                    this.stopwatch.afr(29432, 5265492, (byte)40);
                                    blockPosBlockItemHelper.avN((Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
                                    n2++;
                                 }
                                 continue label796;
                              }

                              if ((pos == null || list != null) && blockPosBlockItemHelper4.m && blockPosBlockItemHelper4.n && (blockPosBlockItemHelper4.h || blockPosBlockItemHelper4.o) && blockPosBlockItemHelper4.stackSlotHelper.LL()) {
                                 List list6 = PlacementUtils.Fs(mutable3, (Double)this.placeRange.atx(), (Integer)this.support.atx());
                                 if (!list6.isEmpty() && CollisionUtils.aiD(((PosNeedPlaceSide)list6.getLast()).getPos())) {
                                    pos = mutable3;
                                    blockPosBlockItemHelper = blockPosBlockItemHelper4;
                                    list = list6;
                                 }
                              }
                           }

                           n3++;
                        }

                        if (pos == null || blockPosBlockItemHelper == null) {
                           continue;
                        }

                        Object obj7 = pos;
                        this.map.put(obj, obj7);
                        this.stopwatch.afr(29432, 5265492, (byte)40);
                        if (list == null) {
                           blockPosBlockItemHelper.avN((Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
                           n2++;
                           continue;
                        }

                        if (!(Boolean)this.multi.atx()) {
                           new BlockPosBlockItemHelper(
                                 ((PosNeedPlaceSide)list.getFirst()).getPos(), Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                              )
                              .avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
                           if (++n2 > (Integer)this.bpt.atx()) {
                              return;
                           }
                        } else {
                           Iterator iterator5 = list.iterator();

                           while (iterator5.hasNext()) {
                              Object obj8 = iterator5.next();
                              new BlockPosBlockItemHelper(
                                    ((PosNeedPlaceSide)obj8).getPos(), Items.OBSIDIAN, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                 )
                                 .avM(
                                    (Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx()
                                 );
                              if (++n2 > (Integer)this.bpt.atx()) {
                                 return;
                              }
                           }
                        }

                        blockPosBlockItemHelper.avM((Double)this.placeRange.atx(), (Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
                        if (++n2 <= (Integer)this.bpt.atx()) {
                           continue;
                        }

                        return;
                     }

                     if (pos != null && blockPosBlockItemHelper != null) {
                        Mutable mutable6 = (Mutable)pos;
                        this.map.put(obj, mutable6);
                        this.stopwatch.afr(29432, 5265492, (byte)40);
                        blockPosBlockItemHelper.avN((Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
                        n2++;
                     }
                     continue;
                  }

                  if (pos == null) {
                  }
                  continue;
               }

               if (pos != null && blockPosBlockItemHelper != null) {
                  Object obj9 = pos;
                  this.map.put(obj, obj9);
                  this.stopwatch.afr(29432, 5265492, (byte)40);
                  blockPosBlockItemHelper.avN((Boolean)this.rotate.atx(), (Boolean)this.breakCrystal.atx(), (Double)this.breakRange.atx());
                  n2++;
               }
               continue;
            }
            break;
         }

         return;
      }
   }

   static boolean avt(Entry entry) {
      boolean flag;
      if (!((EnderPearlEntity)entry.getKey()).isAlive()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isMultiVisible() {
      boolean flag;
      if ((Integer)this.support.atx() > 0) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
