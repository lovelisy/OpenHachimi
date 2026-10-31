package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.AutoMineWhitelistBlacklistMode;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.player.BlockPosBoxMultitask;
import pw.hachimi.client.setting.BlockListSetting;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BlockUtils;
import pw.hachimi.client.utils.world.BoxEntityUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.PlayerBlockPosHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoMine extends Multitask {
   private static AutoMine INSTANCE;
   private final List list = new ArrayList();
   private final List list2 = new ArrayList();
   Setting selection = this.registerSetting(new EnumSetting("Selection", "How to use the blocks setting", AutoMineWhitelistBlacklistMode.All, AutoMineWhitelistBlacklistMode.values()));
   Setting whitelist = this.registerSetting(
      new BlockListSetting("Whitelist", "Valid block whitelist", 
         Blocks.OBSIDIAN,
         Blocks.CRYING_OBSIDIAN,
         Blocks.ANCIENT_DEBRIS,
         Blocks.NETHERITE_BLOCK,
         Blocks.RESPAWN_ANCHOR,
         Blocks.ANVIL,
         Blocks.ENCHANTING_TABLE,
         Blocks.ENDER_CHEST,
         Blocks.BEACON
      )
   );
   Setting blacklist = this.registerSetting(new BlockListSetting("Blacklist", "Valid block blacklist", Blocks.COBWEB));
   Setting targetRange = this.registerSetting(new NumberSetting("TargetRange", "The maximum range the entity can be to attack it", 0.0, 5.0, 8.0));
   Setting mineRange = this.registerSetting(new NumberSetting("MineRange", "The maximum range the block can be to break it", 0.0, 4.0, 6.0));
   Setting sort = this.registerSetting(new EnumSetting("Sort", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting wait = this.registerSetting(new BooleanSetting("Wait", "Waiting for mining to finish", false));
   Setting burrow = this.registerSetting(new BooleanSetting("Burrow", "Auto mine enemy's burrow", true));
   Setting face = this.registerSetting(new BooleanSetting("Face", "Auto mine enemy's face", false));
   Setting surround = this.registerSetting(new BooleanSetting("Surround", "Auto mine enemy's surround", true));
   Setting checkSelf = this.registerSetting(new BooleanSetting("CheckSelf", "Skip your surround block", false, this::isCheckSelfVisible));
   Setting friendSync = this.registerSetting(new BooleanSetting("FriendSync", "Skip the target block when friends mine", false, AutoMine::isFriendSyncVisible));
   Setting priority = this.registerSetting(new NumberSetting("Priority", "Priority of mining", 0, 5, 10));
   BlockPos pos = null;
   private Entity entity;

   public AutoMine() {
      super("AutoMine", "Auto mine target blocks", ModuleCategory.COMBAT);
      INSTANCE = this;
   }

   public static AutoMine ars() {
      return INSTANCE;
   }

   private static List art(Vec3d vec) {
      Mutable temp = new Mutable(vec.getX() + 0.25, vec.getY(), vec.getZ() + 0.25);
      Mutable mutable = new Mutable(vec.getX() - 0.25, vec.getY(), vec.getZ() - 0.25);
      temp = temp;
      Mutable temp2 = mutable;
      mutable = new Mutable(vec.getX() + 0.25, vec.getY(), vec.getZ() - 0.25);
      Mutable mutable2 = temp2;
      temp = temp;
      temp2 = mutable2;
      Mutable temp3 = mutable;
      Mutable mutable3 = new Mutable(vec.getX() - 0.25, vec.getY(), vec.getZ() + 0.25);
      mutable = temp3;
      mutable2 = temp2;
      return List.of(temp, mutable2, mutable, mutable3);
   }

   private static List aru(Vec3d vec) {
      return BoxUtils.jF(new Box(vec.getX() - 0.25, vec.getY() + 1.5, vec.getZ() - 0.25, vec.getX() + 0.25, vec.getY() + 1.5, vec.getZ() + 0.25));
   }

   double arv(BlockPos pos2) {
      Vec3d vec = client.player.getPos();
      return pos2.getSquaredDistance((Position)vec);
   }

   @Override
   public void awe() {
      this.entity = null;
      this.list.clear();
      this.list2.clear();
   }

   public Entity getEntity() {
      return this.entity;
   }

   @EventListener
   public void arx(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (!Blink.bm()) {
         BlockPosBoxMultitask blockPosBoxMultitask = BlockPosBoxMultitask.alv();
         final AutoMine autoMine = this;
         TargetUtils.WY(this.list2, new Predicate() {
            @Override
            public boolean test(Object tickEntitiesPreEvent) {
               return autoMine.arB((Entity)tickEntitiesPreEvent);
            }
         }, (TargetPriority)this.sort.atx(), 1);
         if (!this.list2.isEmpty()) {
            this.entity = (Entity)this.list2.getFirst();
            if ((Boolean)this.wait.atx() && blockPosBoxMultitask.blockPosPlayerHelper2 != null && blockPosBoxMultitask.blockPosPlayerHelper != null) {
               BlockPos pos2 = blockPosBoxMultitask.blockPosPlayerHelper.pos;
               if (!blockPosBoxMultitask.blockPosPlayerHelper2.pos.equals(pos2)) {
                  BlockPos pos3 = blockPosBoxMultitask.blockPosPlayerHelper.pos;
                  if (!client.world.isAir(pos3)) {
                     return;
                  }
               }
            }

            if ((Boolean)this.breakSetting.atx() || !client.player.isUsingItem()) {
               if ((Boolean)this.burrow.atx()) {
                  Iterator iterator2 = art(this.entity.getPos()).iterator();

                  while (iterator2.hasNext()) {
                     Object obj = iterator2.next();
                     if (this.arC((BlockPos)obj)) {
                        int n = (Integer)this.priority.atx();
                        if (blockPosBoxMultitask.alH((BlockPos)obj, n)) {
                           return;
                        }
                     }
                  }
               }

               if ((Boolean)this.face.atx()) {
                  Iterator iterator3 = aru(this.entity.getPos()).iterator();

                  while (iterator3.hasNext()) {
                     Object obj2 = iterator3.next();
                     if (this.arC((BlockPos)obj2)) {
                        int n2 = (Integer)this.priority.atx();
                        if (blockPosBoxMultitask.alH((BlockPos)obj2, n2)) {
                           return;
                        }
                     }
                  }
               }

               if ((Boolean)this.surround.atx()
                  && (this.entity.isOnGround() || this.entity.prevY == this.entity.getY())
                  && BlockUtils.TM(this.entity.getBlockPos())) {
                  this.list.clear();
                  List list3 = WorldUtils.MZ(this.entity.getPos());
                  Iterator iterator4 = list3.iterator();

                  while (iterator4.hasNext()) {
                     Object obj3 = iterator4.next();
                     Direction[] directions = BoxUtils.b;
                     int temp = directions.length;
                     int n3 = 0;

                     do {
                        Direction direction = directions[n3];
                        BlockPos pos4 = ((BlockPos)obj3).offset(direction);
                        if (!(Boolean)this.checkSelf.atx()) {
                           if (!list3.contains(pos4) && this.ary(pos4, false)) {
                              this.list.add(pos4);
                           }
                        } else if (!list3.contains(pos4) && this.ary(pos4, true)) {
                           this.list.add(pos4);
                        } else if (this.list.isEmpty() && !list3.contains(pos4) && this.ary(pos4, false)) {
                           this.list.add(pos4);
                        }
                     } while (++n3 < 4);
                  }

                  if (!this.list.isEmpty()) {
                     if (this.pos != null) {
                        BlockPos pos5 = this.pos;
                        if (this.list.contains(pos5)) {
                           blockPosBoxMultitask.alI(this.pos, (Integer)this.priority.atx());
                           this.pos = null;
                           return;
                        }
                     }

                     if (blockPosBoxMultitask.blockPosPlayerHelper2 != null) {
                        BlockPos pos6 = blockPosBoxMultitask.blockPosPlayerHelper2.pos;
                        if (this.list.contains(pos6)) {
                           return;
                        }
                     }

                     List list4 = this.list;
                     final AutoMine autoMine2 = this;
                     Comparator comparator = Comparator.comparingDouble(new ToDoubleFunction() {
                        @Override
                        public double applyAsDouble(Object tickEntitiesPreEvent) {
                           return autoMine2.arv((BlockPos)tickEntitiesPreEvent);
                        }
                     });
                     list4.sort(comparator);
                     Object obj4 = this.list.getFirst();
                     if (this.list.size() >= 2 && blockPosBoxMultitask.blockPosPlayerHelper == null) {
                        Object temp2 = this.list.get(1);
                        int n4 = (Integer)this.priority.atx();
                        blockPosBoxMultitask.alI((BlockPos)temp2, n4);
                        this.pos = (BlockPos)obj4;
                     } else {
                        int n5 = (Integer)this.priority.atx();
                        blockPosBoxMultitask.alI((BlockPos)obj4, n5);
                     }
                  }
               }
            }
         } else {
            this.entity = null;
         }
      }
   }

   @Override
   public String hy() {
      return this.entity != null ? this.entity.getName().getString() : super.hy();
   }

   private boolean ary(BlockPos pos2, boolean flag) {
      ClientWorld temp = client.world;
      BlockPos pos3 = pos2.down();
      Block block2 = temp.getBlockState(pos3).getBlock();
      Block block3 = client.world.getBlockState(pos2).getBlock();
      if (block2 == Blocks.BEDROCK || block2 == Blocks.OBSIDIAN) {
         Vec3d vec = client.player.getEyePos();
         Vec3d vec2 = pos2.toCenterPos();
         if (vec.distanceTo(vec2) <= (Double)this.mineRange.atx()
            && (block3 instanceof AirBlock || block3 instanceof FireBlock || this.arC(pos2))
            && !this.arA(new Box(pos2.getX(), pos2.getY(), pos2.getZ(), pos2.getX() + 1, pos2.getY() + 2, pos2.getZ() + 1))
            && (!flag || !WorldUtils.Nb(client.player, pos2, true, true) || this.arz())) {
            return true;
         }
      }

      return false;
   }

   private boolean arz() {
      BlockPos pos2 = WorldUtils.MX(client.player, false);
      if (BlockUtils.TP(pos2)) {
         return true;
      }

      boolean flag;
      if (BlockUtils.TR(pos2) && !Managers.breakManager.Ru(pos2)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private boolean arA(Box box) {
      return BoxEntityUtils.kK(box, new Predicate() {
         @Override
         public boolean test(Object box) {
            return AutoMine.arD((Entity)box);
         }
      });
   }

   boolean arB(Entity entity2) {
      if (entity2 instanceof PlayerEntity) {
         ClientPlayerEntity player2 = client.player;
         if (!entity2.equals(player2)) {
            Entity entity3 = client.cameraEntity;
            if (!entity2.equals(entity3)) {
               if (!((PlayerEntity)entity2).isDead() && entity2.isAlive()) {
                  if (client.player.distanceTo(entity2) > (Double)this.targetRange.atx()) {
                     return false;
                  }

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
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean arC(BlockPos pos2) {
      Block block2 = client.world.getBlockState(pos2).getBlock();
      if (block2 instanceof AirBlock) {
         return false;
      }

      if (!block2.getDefaultState().getFluidState().isEmpty()) {
         return false;
      }

      if (this.selection.atx() == AutoMineWhitelistBlacklistMode.Blacklist) {
         boolean flag;
         if (!((List)this.blacklist.atx()).contains(block2)) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         if (this.selection.atx() == AutoMineWhitelistBlacklistMode.Whitelist) {
            return ((List)this.whitelist.atx()).contains(block2);
         }

         if ((Boolean)this.friendSync.atx()) {
            Iterator iterator2 = Managers.breakManager.concurrentHashMap.values().iterator();

            while (iterator2.hasNext()) {
               PlayerBlockPosHelper playerBlockPosHelper = (PlayerBlockPosHelper)iterator2.next();
               PlayerEntity player2 = playerBlockPosHelper.MT();
               if (player2 != null && playerBlockPosHelper.pos.equals(pos2) && Managers.friendManager.awr(player2)) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   static boolean arD(Entity entity2) {
      boolean flag;
      if (!entity2.isSpectator() && !(entity2 instanceof ItemEntity) && !(entity2 instanceof EndCrystalEntity)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static Boolean isFriendSyncVisible() {
      return true;
   }

   private Boolean isCheckSelfVisible() {
      return (Boolean)this.surround.atx();
   }
}
