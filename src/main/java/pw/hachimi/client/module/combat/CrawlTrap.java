package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.render.PlaceEcho;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.world.BlockStateUtils;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class CrawlTrap extends Multitask {
   private final List list = new ArrayList();
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting sortPriority = this.registerSetting(new EnumSetting("SortPriority", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting maxTargets = this.registerSetting(new NumberSetting("MaxTargets", "How many entities to target at once", 1, 1, 10));
   Setting targetRange = this.registerSetting(new NumberSetting("TargetRange", "", 0.0F, 5.0F, 7.0F));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "Interact block reach", 0.0, 4.5, 8.0));
   Setting detectMining = this.registerSetting(new BooleanSetting("DetectMining", "", true));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "", true));
   Setting collideSkip = this.registerSetting(new BooleanSetting("CollideSkip", "", true));
   Setting inAirSkip = this.registerSetting(new BooleanSetting("InAirSkip", "", false));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "", 0, 100, 2000));
   StopwatchImpl stopwatchImpl = new StopwatchImpl();

   public CrawlTrap() {
      super("CrawlTrap", "", ModuleCategory.COMBAT);
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   @EventListener
   public void AH(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (((Boolean)this.breakSetting.atx() || !client.player.isUsingItem()) && !Blink.bm()) {
         this.list.clear();
         final CrawlTrap crawlTrap = this;
         TargetUtils.WY(this.list, new Predicate() {
            @Override
            public boolean test(Object tickEntitiesPreEvent) {
               return crawlTrap.AI((Entity)tickEntitiesPreEvent);
            }
         }, (TargetPriority)this.sortPriority.atx(), (Integer)this.maxTargets.atx());
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            Object obj = iterator2.next();
            if ((!(Boolean)this.inAirSkip.atx() || ((Entity)obj).isOnGround()) && this.stopwatchImpl.afq((Number)this.delay.atx())) {
               ClientWorld temp = client.world;
               BlockPos pos = ((Entity)obj).getBlockPos().up();
               BlockState state2 = temp.getBlockState(pos);
               if (!state2.isReplaceable()) {
                  if (state2.getBlock() instanceof TrapdoorBlock) {
                     BooleanProperty booleanProperty = TrapdoorBlock.OPEN;
                     if (!(Boolean)state2.get((Property)booleanProperty)) {
                        Direction direction = PlacementUtils.Fh(((Entity)obj).getBlockPos().up());
                        if (direction == null) {
                           break;
                        }

                        PlacementUtils.Ff(((Entity)obj).getBlockPos().up(), direction, (Boolean)this.rotate.atx());
                        this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                     }
                  }
               } else if (!((Entity)obj).isCrawling()) {
                  if ((Boolean)this.collideSkip.atx()) {
                     Box box = ((Entity)obj).getBoundingBox();
                     if (CollisionUtils.aiy((Entity)obj, box)) {
                        continue;
                     }
                  }

                  BlockPos pos2 = ((Entity)obj).getBlockPos().up();
                  if ((!(Boolean)this.detectMining.atx() || !Managers.breakManager.Ru(pos2))
                     && !AK(new Box(pos2.getX(), pos2.getY() + 0.8125, pos2.getZ(), pos2.getX() + 1, pos2.getY() + 1, pos2.getZ() + 1))) {
                     StackSlotHelper stackSlotHelper = new StackSlotHelper(new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return CrawlTrap.AN((ItemStack)tickEntitiesPreEvent);
                        }
                     }, (SwapMode)this.swapAction.atx()).LK();
                     if (stackSlotHelper.LL()) {
                        Direction direction2 = this.AJ(((Entity)obj).getEyePos());
                        if (direction2 != null) {
                           BlockPos pos3 = ((Entity)obj).getBlockPos().up().offset(direction2);
                           Vec3d vec = pos3.toCenterPos();
                           double d = direction2.getOpposite().getVector().getZ() * 0.5;
                           double d2 = direction2.getOpposite().getVector().getX() * 0.5;
                           Vec3d vec2 = vec.add(d2, 0.5, d);
                           if (client.player.getEyePos().distanceTo(vec2) <= (Double)this.placeRange.atx()) {
                              List list2 = PlacementUtils.a_;
                              BlockPos pos4 = ((Entity)obj).getBlockPos();
                              list2.add(pos4);
                              PlaceEcho.Mz().MB(((Entity)obj).getBlockPos());
                              int n = Managers.inventoryTracker.JS();
                              stackSlotHelper.LO(n);
                              PlacementUtils.Fg(pos3, direction2.getOpposite(), Hand.MAIN_HAND, true, (Boolean)this.rotate.atx());
                              if ((Boolean)this.rotate.atx()) {
                                 float[] fs = RotationUtils.nM(vec2);
                                 RotationUtils.nH(fs[0], fs[1]);
                              }

                              BlockHitResult blockHitResult = new BlockHitResult(vec2, direction2.getOpposite(), pos3, false);
                              final BlockHitResult blockHitResult2 = blockHitResult;
                              Managers.serverManager.iQ(new SequencedPacketCreator() {
                                 public Packet predict(int tickEntitiesPreEvent) {
                                    return CrawlTrap.AM(blockHitResult2, tickEntitiesPreEvent);
                                 }
                              });
                              if ((Boolean)this.rotate.atx()) {
                                 RotationUtils.nK();
                              }

                              PlacementUtils.swingMode2.avX(Hand.MAIN_HAND);
                              client.itemUseCooldown = 4;
                              stackSlotHelper.LP(n);
                              this.stopwatchImpl.afr(29432, 5265492, (byte)40);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   boolean AI(Entity entity2) {
      ClientPlayerEntity player2 = client.player;
      if (!entity2.equals(player2)) {
         Entity entity3 = client.cameraEntity;
         if (!entity2.equals(entity3)) {
            if ((!(entity2 instanceof LivingEntity) || !((LivingEntity)entity2).isDead()) && entity2.isAlive()) {
               Vec3d temp = client.player.getEyePos();
               Vec3d vec = entity2.getPos();
               if (temp.distanceTo(vec) > ((Float)this.targetRange.atx()).floatValue()) {
                  return false;
               }

               if (entity2 instanceof PlayerEntity) {
                  if (((PlayerEntity)entity2).isCreative()) {
                     return false;
                  }

                  boolean flag;
                  if (!Managers.friendManager.awr((PlayerEntity)entity2)) {
                     flag = true;
                  } else {
                     flag = false;
                  }

                  return flag;
               }

               return false;
            }

            return false;
         }
      }

      return false;
   }

   private Direction AJ(Vec3d vec) {
      Mutable mutable = new Mutable(vec.getX(), vec.getY(), vec.getZ());
      double d = Double.MAX_VALUE;
      Direction direction = null;
      Direction[] directions = Direction.values();
      int n = directions.length;

      for (int n2 = 0; n2 < n; n2++) {
         Direction direction2 = directions[n2];
         if (direction2 != Direction.UP && direction2 != Direction.DOWN) {
            BlockPos pos = mutable.offset(direction2);
            if (PlacementUtils.blockPosDirectionBase.Hr(pos, direction2.getOpposite())) {
               BlockState state2 = client.world.getBlockState(pos);
               if (!state2.isReplaceable() && (!BlockStateUtils.Wy(state2, pos) || client.player.isSneaking())) {
                  Box box = new Box(
                     direction2.getOffsetX() * 0.5 + mutable.getX() + 0.5,
                     mutable.getY(),
                     direction2.getOffsetZ() * 0.5 + mutable.getZ() + 0.5,
                     direction2.getOffsetX() * 0.3125 + mutable.getX() + 0.5,
                     mutable.getY() + 1,
                     direction2.getOffsetZ() * 0.3125 + mutable.getZ() + 0.5
                  );
                  Box temp = box;
                  Box box2 = new Box(vec.getX() - 0.27, vec.getY(), vec.getZ() - 0.27, vec.getX() + 0.27, vec.getY() - 1.8, vec.getZ() + 0.27);
                  if (temp.intersects(box2)) {
                     double d2 = box.getCenter().distanceTo(vec);
                     if (d2 < d) {
                        d = d2;
                        direction = direction2;
                     }
                  }
               }
            }
         }
      }

      return direction;
   }

   public static boolean AK(Box box) {
      ClientWorld temp = client.world;
      Predicate predicate = new Predicate() {
         @Override
         public boolean test(Object obj) {
            return CrawlTrap.AL((Entity)obj);
         }
      };
      Iterator iterator2 = temp.getOtherEntities((Entity)null, box, predicate).iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2.isAlive()
            && !(entity2 instanceof ItemEntity)
            && !(entity2 instanceof ExperienceOrbEntity)
            && !(entity2 instanceof ExperienceBottleEntity)
            && !(entity2 instanceof ArrowEntity)
            && !(entity2 instanceof ItemFrameEntity)) {
            Box box2 = entity2.getBoundingBox();
            if (box.intersects(box2)) {
               return true;
            }
         }
      }

      return false;
   }

   static boolean AL(Entity entity2) {
      boolean flag;
      if (!entity2.isSpectator()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   static Packet AM(BlockHitResult blockHitResult, int n) {
      return new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult, n);
   }

   static boolean AN(ItemStack stack) {
      Item item2 = stack.getItem();
      boolean flag;
      if (item2 instanceof BlockItem && ((BlockItem)item2).getBlock() instanceof TrapdoorBlock) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
