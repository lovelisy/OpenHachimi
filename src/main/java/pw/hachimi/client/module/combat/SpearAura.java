package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.ServerManager;
import pw.hachimi.client.model.combat.SpearAuraBabyAdultMode;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.exploit.SpearFix;
import pw.hachimi.client.module.movement.Sprint;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.StopwatchImpl;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.player.StackSlotHelper;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.client.utils.world.PlacementUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class SpearAura extends ClientModule {
   private static SpearAura spearAura;
   private final List list = new ArrayList();
   Setting missMode = this.registerSetting(new BooleanSetting("MissMode", "Magic.", true));
   Setting widthLimit = this.registerSetting(new NumberSetting("WidthLimit", "Magic.", 0.0, 2.0, 4.0));
   Setting heightLimit = this.registerSetting(new NumberSetting("HeightLimit", "Magic.", 0.0, 3.5, 6.0));
   Setting fallDistance = this.registerSetting(new NumberSetting("FallDistance", "Min fall distance to active", 0.0, 2.0, 10.0));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the target", true));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting sortPriority = this.registerSetting(new EnumSetting("SortPriority", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting range = this.registerSetting(new NumberSetting("Range", "The maximum range the entity can be to attack it", 0.0, 3.0, 6.0));
   Setting wallsRange = this.registerSetting(new NumberSetting("WallsRange", "The maximum range the entity can be attacked through walls", 0.0, 3.0, 6.0));
   Setting mobAgeFilter = this.registerSetting(new EnumSetting("MobAgeFilter", "Determines the age of the mobs to target (baby, adult, or both)", SpearAuraBabyAdultMode.Adult, SpearAuraBabyAdultMode.values()));
   Setting ignoreNamed = this.registerSetting(new BooleanSetting("IgnoreNamed", "Whether or not to attack mobs with a name", false));
   Setting ignorePassive = this.registerSetting(new BooleanSetting("IgnorePassive", "Will only attack sometimes passive mobs if they are targeting you", true));
   Setting ignoreTamed = this.registerSetting(new BooleanSetting("IgnoreTamed", "Will avoid attacking mobs you tamed", false));
   Setting players = this.registerSetting(new BooleanSetting("Players", "Entities to attack", true));
   Setting mobs = this.registerSetting(new BooleanSetting("Mobs", "Entities to attack", false));
   Setting animals = this.registerSetting(new BooleanSetting("Animals", "Entities to attack", false));
   Setting villagers = this.registerSetting(new BooleanSetting("Villagers", "Entities to attack", false));
   Setting slimes = this.registerSetting(new BooleanSetting("Slimes", "Entities to attack", false));
   StopwatchImpl stopwatchImpl = new StopwatchImpl();

   public SpearAura() {
      super("SpearAura", "Attacks specified entities around you", ModuleCategory.COMBAT, 700);
      spearAura = this;
   }

   public static SpearAura avm() {
      return spearAura;
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   @EventListener
   public void avn(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (TargetUtils.Xa(client.player) != GameMode.SPECTATOR && !Blink.bm()) {
         this.list.clear();
         final SpearAura spearAura2 = this;
         TargetUtils.WY(this.list, new Predicate() {
            @Override
            public boolean test(Object tickEntitiesPreEvent) {
               return spearAura2.avo((Entity)tickEntitiesPreEvent);
            }
         }, (TargetPriority)this.sortPriority.atx(), 1);
         if (!this.list.isEmpty()
            && SpearFix.nt(client.player.getMainHandStack())
            && client.player.fallDistance >= (Double)this.fallDistance.atx()
            && this.stopwatchImpl.afq(500)) {
            int n = Managers.inventoryTracker.JS();
            StackSlotHelper stackSlotHelper = new StackSlotHelper(Items.MACE, (SwapMode)this.swapAction.atx()).LK();
            if (stackSlotHelper.LL()) {
               if ((Boolean)this.missMode.atx()) {
                  Object obj = this.list.getFirst();
                  if (obj instanceof LivingEntity
                     && (
                        WorldUtils.Nl(client.player.getX(), client.player.getZ(), ((LivingEntity)obj).getX(), ((LivingEntity)obj).getZ())
                              > (Double)this.widthLimit.atx()
                           || client.player.getEyeY() - ((LivingEntity)obj).getEyeY() > (Double)this.heightLimit.atx()
                     )) {
                     return;
                  }
               }

               boolean flag = client.player.isUsingItem();
               if (flag) {
                  ServerManager temp = Managers.serverManager;
                  PlayerActionC2SPacket playerActionC2SPacket = new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN);
                  temp.iO('\uf560', playerActionC2SPacket);
               }

               stackSlotHelper.LO(n);
               this.avp((Entity)this.list.getFirst());
               stackSlotHelper.LP(n);
               if (flag) {
                  PlayerInteractItemC2SPacket playerInteractItemC2SPacket = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, client.player.getYaw(), client.player.getPitch());
                  client.getNetworkHandler().sendPacket((Packet)playerInteractItemC2SPacket);
               }

               this.stopwatchImpl.afr(29432, 5265492, (byte)40);
            }
         }
      }
   }

   boolean avo(Entity entity2) {
      ClientPlayerEntity player2 = client.player;
      if (!entity2.equals(player2)) {
         Entity entity3 = client.cameraEntity;
         if (!entity2.equals(entity3)) {
            if ((!(entity2 instanceof LivingEntity) || !((LivingEntity)entity2).isDead()) && entity2.isAlive()) {
               Vec3d vec = BoxUtils.jy(client.player.getEyePos(), entity2.getBoundingBox());
               if (client.player.getEyePos().distanceTo(vec) > (Double)this.range.atx()) {
                  return false;
               }

               if (!this.avr(entity2)) {
                  return false;
               }

               if ((Boolean)this.ignoreNamed.atx() && entity2.hasCustomName()) {
                  return false;
               }

               if (!WorldUtils.Nk(entity2) && client.player.getEyePos().distanceTo(vec) > (Double)this.wallsRange.atx()) {
                  return false;
               }

               if ((Boolean)this.ignoreTamed.atx() && entity2 instanceof Tameable && ((Tameable)entity2).getOwnerUuid() != null) {
                  UUID temp = ((Tameable)entity2).getOwnerUuid();
                  UUID uuid = client.player.getUuid();
                  if (temp.equals(uuid)) {
                     return false;
                  }
               }

               if ((Boolean)this.ignorePassive.atx()) {
                  byte b = 0;

                  label105:
                  while (true) {
                     byte b2 = b;
                     switch (entity2 == null
                        ? -1
                        : (
                           b2 <= 0 && entity2 instanceof EndermanEntity
                              ? 0
                              : (b2 <= 1 && entity2 instanceof ZombifiedPiglinEntity ? 1 : (b2 <= 2 && entity2 instanceof WolfEntity ? 2 : 3))
                        )) {
                        case 0:
                           if (!((EndermanEntity)entity2).isAngry()) {
                              return false;
                           }

                           b = 1;
                           break;
                        case 1:
                           if (!((ZombifiedPiglinEntity)entity2).isAttacking()) {
                              return false;
                           }

                           b = 2;
                           break;
                        case 2:
                           if (!((WolfEntity)entity2).isAttacking()) {
                              return false;
                           }

                           b = 3;
                           break;
                        default:
                           break label105;
                     }
                  }
               }

               if (entity2 instanceof PlayerEntity) {
                  if (((PlayerEntity)entity2).isCreative()) {
                     return false;
                  }

                  if (Managers.friendManager.awr((PlayerEntity)entity2)) {
                     return false;
                  }
               }

               if (entity2 instanceof AnimalEntity) {
                  return switch ((SpearAuraBabyAdultMode)this.mobAgeFilter.atx()) {
                     case Baby -> ((AnimalEntity)entity2).isBaby();
                     case Adult -> {
                        if (!((AnimalEntity)entity2).isBaby()) {
                           yield true;
                        } else {
                           yield false;
                        }
                     }
                     case Both -> true;
                  };
               }

               return true;
            }

            return false;
         }
      }

      return false;
   }

   private void avp(Entity entity2) {
      Vec3d vec = BoxUtils.jz(entity2.getBoundingBox());
      if (!(client.player.getEyePos().distanceTo(vec) > (Double)this.range.atx())) {
         if ((Boolean)this.rotate.atx()) {
            float[] fs = RotationUtils.nM(vec);
            RotationUtils.nH(fs[0], fs[1]);
         }

         if (PlacementUtils.k) {
            ClientPlayNetworkHandler temp = client.player.networkHandler;
            boolean flag = client.player.isSneaking();
            PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.attack(entity2, flag);
            temp.sendPacket((Packet)playerInteractEntityC2SPacket);
            client.player.resetLastAttackedTicks();
         } else {
            ClientPlayerEntity player2 = client.player;
            client.interactionManager.attackEntity((PlayerEntity)player2, entity2);
         }

         Hand hand = Hand.MAIN_HAND;
         client.player.swingHand(hand);
         Sprint.a = true;
         if ((Boolean)this.rotate.atx()) {
            Managers.movementTracker.Mk();
         }
      }
   }

   public Entity avq() {
      return !this.list.isEmpty() ? (Entity)this.list.getFirst() : null;
   }

   @Override
   public String hy() {
      return !this.list.isEmpty() ? this.avq().getName().getString() : super.hy();
   }

   private boolean avr(Entity entity2) {
      if (entity2 instanceof SlimeEntity) {
         return (Boolean)this.slimes.atx();
      } else if (entity2 instanceof PlayerEntity) {
         return (Boolean)this.players.atx();
      } else if (entity2 instanceof VillagerEntity || entity2 instanceof WanderingTraderEntity) {
         return (Boolean)this.villagers.atx();
      } else if (entity2 instanceof AnimalEntity) {
         return (Boolean)this.animals.atx();
      } else {
         return entity2 instanceof MobEntity ? (Boolean)this.mobs.atx() : false;
      }
   }

   private static MatchException a() {
      return new MatchException(null, null);
   }
}
