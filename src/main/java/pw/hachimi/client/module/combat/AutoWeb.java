package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.ChannelRead0Event;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.AutoWebAllOnGroundMode;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.world.BlockPosBlockItemHelper;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.client.utils.world.EntityVec3dUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class AutoWeb extends Multitask {
   private static final Set a = new HashSet();
   private static final Set b = new HashSet();
   private static final Set c = new HashSet();
   private final List list = new ArrayList();
   private final Vec3d[] vecs = new Vec3d[]{h(0.3, 0.3), h(0.3, -0.3), h(-0.3, 0.3), h(-0.3, -0.3)};
   Setting target = this.registerSetting(new EnumSetting("Target", "Target selector", AutoWebAllOnGroundMode.All, AutoWebAllOnGroundMode.values()));
   Setting targetRange = this.registerSetting(new NumberSetting("TargetRange", "The maximum range the entity can be to attack it", 0.0, 5.0, 8.0));
   Setting sort = this.registerSetting(new EnumSetting("Sort", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting feet = this.registerSetting(new BooleanSetting("Feet", "Trap the enemy's feet", true));
   Setting face = this.registerSetting(new BooleanSetting("Face", "Trap the enemy's face", true));
   Setting head = this.registerSetting(new BooleanSetting("Head", "Trap the enemy's head", true));
   Setting headGroundOnly = this.registerSetting(new BooleanSetting("HeadGroundOnly", "Only players on the ground", true));
   Setting down = this.registerSetting(new BooleanSetting("Down", "Place webs beneath the enemy", false));
   Setting delay = this.registerSetting(new NumberSetting("Delay", "Delay, in ticks, between placements", 0, 2, 20));
   Setting bpt = this.registerSetting(new NumberSetting("BPT", "in places, number of blocks", 1, 1, 8));
   Setting maxPlacements = this.registerSetting(new NumberSetting("MaxPlacements", "Maximum placement of webs", 1, 1, 8));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the obsidian being placed", true));
   Setting placeRange = this.registerSetting(new NumberSetting("PlaceRange", "The distance of your block reach", 0.0, 4.5, 6.0));
   Setting airPlace = this.registerSetting(new BooleanSetting("AirPlace", "Replace block exploit allow you place block at air", false));
   Setting extrapolationTicks = this.registerSetting(new NumberSetting("ExtrapolationTicks", "Accounts for motion when calculating enemy positions, not fully accurate.", 0, 0, 10));
   Setting groundOnly = this.registerSetting(new BooleanSetting("GroundOnly", "Works only when you are standing on blocks", false));
   Setting pauseOnFlying = this.registerSetting(new BooleanSetting("PauseOnFlying", "Pauses the current process while you are flying", false));
   Setting pauseInFluid = this.registerSetting(new BooleanSetting("PauseInFluid", "Pauses the current process while you are swimming", false));
   Setting disableOnDeath = this.registerSetting(new BooleanSetting("DisableOnDeath", "Toggles off when you die", true));
   private int z;

   public AutoWeb() {
      super("AutoWeb", "Automatically traps nearby entities in webs", ModuleCategory.COMBAT);
   }

   @Override
   public void awd() {
      this.z = 0;
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   @EventListener
   private void La(TickEntitiesPreEvent tickEntitiesPreEvent) {
      a.clear();
      b.clear();
      c.clear();
      this.list.clear();
      if (this.z > 0) {
         this.z--;
      } else {
         this.z = (Integer)this.delay.atx();
         if ((Boolean)this.groundOnly.atx() && !client.player.isOnGround()) {
            ClientWorld temp = client.world;
            BlockPos pos = client.player.getBlockPos();
            if (!(temp.getBlockState(pos).getBlock() instanceof LadderBlock)) {
               return;
            }
         }

         if (!(Boolean)this.pauseOnFlying.atx() || !client.player.isFallFlying()) {
            if (!(Boolean)this.pauseInFluid.atx() || !client.player.isInFluid()) {
               if ((Boolean)this.breakSetting.atx() || !client.player.isUsingItem()) {
                  if (!Blink.bm()) {
                     final AutoWeb autoWeb = this;
                     TargetUtils.WY(this.list, new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return autoWeb.Lc((Entity)tickEntitiesPreEvent);
                        }
                     }, (TargetPriority)this.sort.atx(), 1);
                     if (!this.list.isEmpty()) {
                        Entity entity2 = (Entity)this.list.getFirst();
                        int n = 0;
                        Vec3d[] vecs2 = this.vecs;
                        int n2 = vecs2.length;

                        for (int n3 = 0; n3 < n2; n3++) {
                           Vec3d vec = vecs2[n3];
                           Vec3d vec2 = entity2.getPos().add(vec);
                           Vec3d vec3 = EntityVec3dUtils.nT(entity2, (Integer)this.extrapolationTicks.atx());
                           Vec3d vec4 = vec2.add(vec3);
                           Mutable mutable = new Mutable(vec4.x, vec4.y, vec4.z);
                           if ((Boolean)this.down.atx()
                              && !CollisionUtils.aiy(entity2, new Box(entity2.getPos().add(-0.3, 0.0, -0.3), entity2.getPos().add(0.3, -0.5, 0.3)))) {
                              Set set = b;
                              BlockPos pos2 = mutable.down();
                              set.add(pos2);
                           }

                           if ((Boolean)this.feet.atx()) {
                              a.add(mutable);
                           }

                           if ((Boolean)this.face.atx()) {
                              Set set2 = a;
                              BlockPos pos3 = mutable.up();
                              set2.add(pos3);
                           }

                           if ((Boolean)this.head.atx() && (!(Boolean)this.headGroundOnly.atx() || entity2.isOnGround())) {
                              Set set3 = c;
                              BlockPos pos4 = mutable.up(2);
                              set3.add(pos4);
                           }
                        }

                        if (!c.isEmpty()) {
                           boolean flag = true;
                           Iterator iterator2 = c.iterator();

                           while (iterator2.hasNext()) {
                              BlockPos pos5 = (BlockPos)iterator2.next();
                              BlockState state = client.world.getBlockState(pos5);
                              Block block2 = Blocks.COBWEB;
                              if (state.isOf(block2)) {
                                 flag = false;
                                 break;
                              }
                           }

                           if (flag) {
                              iterator2 = c.iterator();

                              while (iterator2.hasNext()) {
                                 BlockPosBlockItemHelper blockPosBlockItemHelper = new BlockPosBlockItemHelper(
                                    (BlockPos)iterator2.next(), Items.COBWEB, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                 );
                                 Setting setting2 = this.placeRange;
                                 if (blockPosBlockItemHelper.avM((Double)setting2.atx(), (Boolean)this.rotate.atx(), false, 3.0)) {
                                    n++;
                                    break;
                                 }
                              }
                           }
                        }

                        if (!b.isEmpty()) {
                           boolean flag2 = true;
                           Iterator iterator3 = b.iterator();

                           while (iterator3.hasNext()) {
                              BlockPos pos6 = (BlockPos)iterator3.next();
                              BlockState state2 = client.world.getBlockState(pos6);
                              Block block3 = Blocks.COBWEB;
                              if (state2.isOf(block3)) {
                                 flag2 = false;
                                 break;
                              }
                           }

                           if (flag2) {
                              iterator3 = b.iterator();

                              while (iterator3.hasNext()) {
                                 BlockPosBlockItemHelper blockPosBlockItemHelper2 = new BlockPosBlockItemHelper(
                                    (BlockPos)iterator3.next(), Items.COBWEB, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                                 );
                                 Setting setting3 = this.placeRange;
                                 if (blockPosBlockItemHelper2.avM((Double)setting3.atx(), (Boolean)this.rotate.atx(), false, 3.0)) {
                                    n++;
                                    break;
                                 }
                              }
                           }
                        }

                        if (!a.isEmpty()) {
                           int n4 = 0;
                           Iterator iterator4 = a.iterator();

                           while (iterator4.hasNext()) {
                              BlockPos pos7 = (BlockPos)iterator4.next();
                              BlockState state3 = client.world.getBlockState(pos7);
                              Block block4 = Blocks.COBWEB;
                              if (state3.isOf(block4)) {
                                 n4++;
                              }
                           }

                           if (n4 >= (Integer)this.maxPlacements.atx()) {
                              return;
                           }

                           iterator4 = a.iterator();

                           while (iterator4.hasNext()) {
                              BlockPosBlockItemHelper blockPosBlockItemHelper3 = new BlockPosBlockItemHelper(
                                 (BlockPos)iterator4.next(), Items.COBWEB, (Boolean)this.airPlace.atx(), (SwapMode)this.swapAction.atx()
                              );
                              Setting setting4 = this.placeRange;
                              if (blockPosBlockItemHelper3.avM((Double)setting4.atx(), (Boolean)this.rotate.atx(), false, 3.0)) {
                                 if (++n >= (Integer)this.bpt.atx()) {
                                    return;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventListener
   private void onChannelRead0(ChannelRead0Event channelRead0Event) {
      if (client.player != null && client.world != null) {
         Packet packet2 = channelRead0Event.getPacket();
         if (packet2 instanceof DeathMessageS2CPacket) {
            ClientWorld temp = client.world;
            int n = ((DeathMessageS2CPacket)packet2).playerId();
            if (temp.getEntityById(n) == client.player && (Boolean)this.disableOnDeath.atx()) {
               this.awc();
            }
         }
      }
   }

   boolean Lc(Entity entity2) {
      if (entity2 instanceof PlayerEntity) {
         ClientPlayerEntity player2 = client.player;
         if (!entity2.equals(player2)) {
            Entity entity3 = client.cameraEntity;
            if (!entity2.equals(entity3)) {
               if (!((PlayerEntity)entity2).isDead() && entity2.isAlive()) {
                  if (this.target.atx() == AutoWebAllOnGroundMode.OnGround && !entity2.isOnGround()) {
                     return false;
                  }

                  if (this.target.atx() == AutoWebAllOnGroundMode.InAir && entity2.isOnGround()) {
                     return false;
                  }

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

   private static Vec3d h(double d, double d2) {
      return new Vec3d(d, 0.3, d2);
   }
}
