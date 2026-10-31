package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.MaceItem;
import net.minecraft.item.ToolItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.ArmorBreakerBabyAdultMode;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.exploit.Blink;
import pw.hachimi.client.module.movement.Sprint;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.chat.ChatUtils;
import pw.hachimi.client.utils.combat.TargetUtils;
import pw.hachimi.client.utils.player.RotationUtils;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.BoxUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class ArmorBreaker extends ClientModule {
   private static ArmorBreaker armorBreaker;
   private final List list;
   Setting numberSetting1;
   Setting booleanSetting1_1;
   Setting targetPriorityMode;
   Setting numberSetting2;
   Setting numberSetting3;
   Setting babyAdultMode;
   Setting booleanSetting2_1;
   Setting booleanSetting3;
   Setting booleanSetting4;
   Setting booleanSetting5;
   Setting booleanSetting6;
   Setting booleanSetting7;
   Setting booleanSetting8;
   Setting booleanSetting9;
   boolean q;
   int r;
   public static boolean y;

   public ArmorBreaker() {
      super("ArmorBreaker", "Attacks specified entities around you", ModuleCategory.COMBAT, 700);
      this.list = new ArrayList();
      this.numberSetting1 = this.registerSetting(new NumberSetting("Shifts", "Magic", 0, 10, 100));
      this.booleanSetting1_1 = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the target", true));
      this.targetPriorityMode = this.registerSetting(
         new EnumSetting("SortPriority", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values())
      );
      this.numberSetting2 = this.registerSetting(new NumberSetting("Range", "The maximum range the entity can be to attack it", 0.0, 3.0, 6.0));
      this.numberSetting3 = this.registerSetting(new NumberSetting("WallsRange", "The maximum range the entity can be attacked through walls", 0.0, 3.0, 6.0));
      this.babyAdultMode = this.registerSetting(
         new EnumSetting("MobAgeFilter", "Determines the age of the mobs to target (baby, adult, or both)", ArmorBreakerBabyAdultMode.Adult, ArmorBreakerBabyAdultMode.values())
      );
      this.booleanSetting2_1 = this.registerSetting(new BooleanSetting("IgnoreNamed", "Whether or not to attack mobs with a name", false));
      this.booleanSetting3 = this.registerSetting(new BooleanSetting("IgnorePassive", "Will only attack sometimes passive mobs if they are targeting you", true));
      this.booleanSetting4 = this.registerSetting(new BooleanSetting("IgnoreTamed", "Will avoid attacking mobs you tamed", false));
      this.booleanSetting5 = this.registerSetting(new BooleanSetting("Players", "Entities to attack", true));
      this.booleanSetting6 = this.registerSetting(new BooleanSetting("Mobs", "Entities to attack", false));
      this.booleanSetting7 = this.registerSetting(new BooleanSetting("Animals", "Entities to attack", false));
      this.booleanSetting8 = this.registerSetting(new BooleanSetting("Villagers", "Entities to attack", false));
      this.booleanSetting9 = this.registerSetting(new BooleanSetting("Slimes", "Entities to attack", false));
      this.q = false;
      this.r = -1;
      armorBreaker = this;
   }

   public static ArmorBreaker WN() {
      return armorBreaker;
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   @Override
   protected void awd() {
      this.r = -1;
      this.q = false;
   }

   @EventListener
   public void WO(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (TargetUtils.Xa(client.player) != GameMode.SPECTATOR && !Blink.bm()) {
         this.list.clear();
         final ArmorBreaker armorBreaker2 = this;
         TargetUtils.WY(this.list, new Predicate() {
            @Override
            public boolean test(Object tickEntitiesPreEvent) {
               return armorBreaker2.WP((Entity)tickEntitiesPreEvent);
            }
         }, (TargetPriority)this.targetPriorityMode.atx(), 1);
         if (!this.list.isEmpty()) {
            Object obj = this.list.getFirst();
            Vec3d vec = BoxUtils.jz(((Entity)obj).getBoundingBox());
            if (!(client.player.getEyePos().distanceTo(vec) > (Double)this.numberSetting2.atx())) {
               int n = -1;
               int n2 = 0;

               do {
                  PlayerInventory temp = client.player.getInventory();
                  int n3 = n2;
                  ComponentMap componentMap = temp.getStack(n3).getComponents();
                  ComponentType componentType = DataComponentTypes.ENCHANTMENTS;
                  Object obj2 = componentMap.get(componentType);
                  if (obj2 != null) {
                     Iterator iterator2 = ((ItemEnchantmentsComponent)obj2).getEnchantments().iterator();

                     while (iterator2.hasNext()) {
                        Object obj3 = iterator2.next();
                        if (((RegistryEntry)obj3).getKey().isPresent() && ((RegistryEntry)obj3).getKey().get().equals(Enchantments.BREACH)) {
                           n = n2;
                           break;
                        }
                     }
                  }
               } while (++n2 < 9);

               if (obj instanceof LivingEntity && ((LivingEntity)obj).hurtTime <= 2) {
                  ChatUtils.UE("Reset");
                  this.q = false;
                  this.r = -1;
               }

               if ((Boolean)this.booleanSetting1_1.atx()) {
                  float[] fs = RotationUtils.nM(vec);
                  RotationUtils.nH(fs[0], fs[1]);
               }

               for (int n4 = 0; n4 < (Integer)this.numberSetting1.atx(); n4++) {
                  Item item2;
                  if (this.r != -1) {
                     PlayerInventory playerInventory = client.player.getInventory();
                     int n5 = this.r;
                     item2 = playerInventory.getStack(n5).getItem();
                     if (!(item2 instanceof MaceItem) && !(item2 instanceof ToolItem)) {
                        ChatUtils.UE("Empty stack");
                        this.r = -1;
                     }

                     this.r++;
                     if (this.r == n) {
                        this.r++;
                     }

                     if (this.r > 8) {
                        if (this.q) {
                           Managers.inventoryTracker.JU(n - 1);
                           break;
                        }

                        this.r = 0;
                        this.q = true;
                     }

                     do {
                        playerInventory = client.player.getInventory();
                        int n6 = this.r;
                        Item item3 = playerInventory.getStack(n6).getItem();
                        if (item3 instanceof MaceItem || item3 instanceof ToolItem) {
                           break;
                        }

                        this.r++;
                     } while (this.r < 9);
                  } else {
                     do {
                        this.r++;
                        PlayerInventory playerInventory2 = client.player.getInventory();
                        int n7 = this.r;
                        item2 = playerInventory2.getStack(n7).getItem();
                     } while ((item2 instanceof MaceItem || item2 instanceof ToolItem) && this.r < 9);
                  }

                  if (this.r >= 9) {
                     break;
                  }

                  ChatUtils.UE("Current:" + this.r + " Sharp:" + this.q);
                  if (this.q) {
                     Managers.inventoryTracker.JF(n);
                  }

                  ClientPlayNetworkHandler network2 = client.player.networkHandler;
                  boolean flag = client.player.isSneaking();
                  PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.attack((Entity)obj, flag);
                  network2.sendPacket((Packet)playerInteractEntityC2SPacket);
                  Hand hand = Hand.MAIN_HAND;
                  client.player.swingHand(hand);
                  Managers.inventoryTracker.JU(this.r);
                  if (this.q) {
                     break;
                  }
               }

               client.player.resetLastAttackedTicks();
               Sprint.a = true;
               if ((Boolean)this.booleanSetting1_1.atx()) {
                  Managers.movementTracker.Mk();
               }
            }
         }
      }
   }

   boolean WP(Entity entity2) {
      ClientPlayerEntity player2 = client.player;
      if (!entity2.equals(player2)) {
         Entity entity3 = client.cameraEntity;
         if (!entity2.equals(entity3)) {
            if ((!(entity2 instanceof LivingEntity) || !((LivingEntity)entity2).isDead()) && entity2.isAlive()) {
               Vec3d vec = BoxUtils.jy(client.player.getEyePos(), entity2.getBoundingBox());
               if (client.player.getEyePos().distanceTo(vec) > (Double)this.numberSetting2.atx()) {
                  return false;
               }

               if (!this.WR(entity2)) {
                  return false;
               }

               if ((Boolean)this.booleanSetting2_1.atx() && entity2.hasCustomName()) {
                  return false;
               }

               if (!WorldUtils.Nk(entity2) && client.player.getEyePos().distanceTo(vec) > (Double)this.numberSetting3.atx()) {
                  return false;
               }

               if ((Boolean)this.booleanSetting4.atx() && entity2 instanceof Tameable && ((Tameable)entity2).getOwnerUuid() != null) {
                  UUID temp = ((Tameable)entity2).getOwnerUuid();
                  UUID uuid = client.player.getUuid();
                  if (temp.equals(uuid)) {
                     return false;
                  }
               }

               if ((Boolean)this.booleanSetting3.atx()) {
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
                  return switch ((ArmorBreakerBabyAdultMode)this.babyAdultMode.atx()) {
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

   public Entity WQ() {
      return !this.list.isEmpty() ? (Entity)this.list.getFirst() : null;
   }

   @Override
   public String hy() {
      return !this.list.isEmpty() ? this.WQ().getName().getString() : super.hy();
   }

   private boolean WR(Entity entity2) {
      if (entity2 instanceof SlimeEntity) {
         return (Boolean)this.booleanSetting9.atx();
      } else if (entity2 instanceof PlayerEntity) {
         return (Boolean)this.booleanSetting5.atx();
      } else if (entity2 instanceof VillagerEntity || entity2 instanceof WanderingTraderEntity) {
         return (Boolean)this.booleanSetting8.atx();
      } else if (entity2 instanceof AnimalEntity) {
         return (Boolean)this.booleanSetting7.atx();
      } else {
         return entity2 instanceof MobEntity ? (Boolean)this.booleanSetting6.atx() : false;
      }
   }

   private static MatchException c() {
      return new MatchException(null, null);
   }
}
