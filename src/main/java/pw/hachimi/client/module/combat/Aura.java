package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
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
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.AuraBabyAdultMode;
import pw.hachimi.client.model.combat.AuraIgnoreBreakMode;
import pw.hachimi.client.model.combat.AuraNoneNormalMode;
import pw.hachimi.client.model.combat.AuraSwitchRandomMode;
import pw.hachimi.client.model.combat.AuraSwordAxeMode;
import pw.hachimi.client.model.combat.CurrentAverageMode;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
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

public class Aura extends Multitask {
   private static Aura aura;
   private final List list = new ArrayList();
   Setting weapon = this.registerSetting(new EnumSetting("Weapon", "Only attacks an entity when a specified weapon is in your hand", AuraSwordAxeMode.All, AuraSwordAxeMode.values()));
   Setting rotate = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the target", true));
   Setting swing = this.registerSetting(new EnumSetting("Swing", "Swing side for attack", SwingMode.Normal, SwingMode.values()));
   Setting autoSwitch = this.registerSetting(new EnumSetting("AutoSwitch", "Switches to your selected weapon when attacking the target", AuraNoneNormalMode.None, AuraNoneNormalMode.values()));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting shieldMode = this.registerSetting(new EnumSetting("ShieldMode", "Will try and use an axe to break target shields", AuraIgnoreBreakMode.Break, AuraIgnoreBreakMode.values()));
   Setting sortPriority = this.registerSetting(new EnumSetting("SortPriority", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting maxTargets = this.registerSetting(new NumberSetting("MaxTargets", "How many entities to target at once", 1, 1, 10));
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode", AuraSwitchRandomMode.Switch, AuraSwitchRandomMode.values(), this::isModeVisible));
   Setting range = this.registerSetting(new NumberSetting("Range", "The maximum range the entity can be to attack it", 0.0, 3.0, 6.0));
   Setting wallsRange = this.registerSetting(new NumberSetting("WallsRange", "The maximum range the entity can be attacked through walls", 0.0, 3.0, 6.0));
   Setting mobAgeFilter = this.registerSetting(new EnumSetting("MobAgeFilter", "Determines the age of the mobs to target (baby, adult, or both)", AuraBabyAdultMode.Adult, AuraBabyAdultMode.values()));
   Setting ignoreNamed = this.registerSetting(new BooleanSetting("IgnoreNamed", "Whether or not to attack mobs with a name", false));
   Setting ignorePassive = this.registerSetting(new BooleanSetting("IgnorePassive", "Will only attack sometimes passive mobs if they are targeting you", true));
   Setting ignoreTamed = this.registerSetting(new BooleanSetting("IgnoreTamed", "Will avoid attacking mobs you tamed", false));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Reverse", "Reverse the entity knock back", false));
   Setting currentAverageMode = this.registerSetting(new EnumSetting("TpsSync", "", CurrentAverageMode.NONE, CurrentAverageMode.values()));
   Setting booleanSetting2_1 = this.registerSetting(new BooleanSetting("CustomDelay", "Use a custom delay instead of the vanilla cooldown", false));
   Setting hitDelay = this.registerSetting(new NumberSetting("HitDelay", "How fast you hit the entity", 0.0F, 11.0F, 60.0F, this.booleanSetting2_1::atx));
   Setting attackCD = this.registerSetting(new NumberSetting("AttackCD", "How fast you hit the entity", 0.0F, 1.0F, 2.0F, this::isAttackCDVisible));
   Setting switchDelay = this.registerSetting(new NumberSetting("SwitchDelay", "How many ticks to wait before hitting an entity after switching hotbar slots", 0, 0, 10));
   Setting players = this.registerSetting(new BooleanSetting("Players", "Entities to attack", true));
   Setting mobs = this.registerSetting(new BooleanSetting("Mobs", "Entities to attack", false));
   Setting animals = this.registerSetting(new BooleanSetting("Animals", "Entities to attack", false));
   Setting villagers = this.registerSetting(new BooleanSetting("Villagers", "Entities to attack", false));
   Setting slimes = this.registerSetting(new BooleanSetting("Slimes", "Entities to attack", false));
   StopwatchImpl stopwatchImpl = new StopwatchImpl();
   int D;
   private int E;
   public static boolean fj;

   public Aura() {
      super("Aura", "Attacks specified entities around you", ModuleCategory.COMBAT, 700);
      aura = this;
   }

   public static Aura aoS() {
      return aura;
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   @EventListener
   public void aoT(TickEntitiesPreEvent tickEntitiesPreEvent) {
      this.E--;
      if (TargetUtils.Xa(client.player) != GameMode.SPECTATOR) {
         if ((Boolean)this.breakSetting.atx() || !this.RH(true)) {
            if (!Blink.bm()) {
               this.list.clear();
               final Aura aura2 = this;
               TargetUtils.WY(this.list, new Predicate() {
                  @Override
                  public boolean test(Object tickEntitiesPreEvent) {
                     return aura2.aoX((Entity)tickEntitiesPreEvent);
                  }
               }, (TargetPriority)this.sortPriority.atx(), (Integer)this.maxTargets.atx());
               if (!this.list.isEmpty()) {
                  if (this.apa()) {
                     if (this.aoY()) {
                        this.aoU();
                     }
                  } else {
                     if (this.autoSwitch.atx() != AuraNoneNormalMode.None) {
                        if (this.autoSwitch.atx() == AuraNoneNormalMode.Silent && !this.aoY()) {
                           return;
                        }
                        StackSlotHelper stackSlotHelper = new StackSlotHelper(switch ((AuraSwordAxeMode)this.weapon.atx()) {
                           case Sword -> new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return Aura.api((ItemStack)tickEntitiesPreEvent);
                              }
                           };
                           case Axe -> new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return Aura.apj((ItemStack)tickEntitiesPreEvent);
                              }
                           };
                           case Mace -> new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return Aura.aph((ItemStack)tickEntitiesPreEvent);
                              }
                           };
                           case Trident -> new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return Aura.apg((ItemStack)tickEntitiesPreEvent);
                              }
                           };
                           case All -> new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return Aura.apf((ItemStack)tickEntitiesPreEvent);
                              }
                           };
                           default -> new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return Aura.ape((ItemStack)tickEntitiesPreEvent);
                              }
                           };
                        }, (SwapMode)this.swapAction.atx()).LK();
                        int n2 = Managers.inventoryTracker.JS();
                        if (this.aoW()) {
                           StackSlotHelper stackSlotHelper2 = new StackSlotHelper(new Predicate() {
                              @Override
                              public boolean test(Object tickEntitiesPreEvent) {
                                 return Aura.apd((ItemStack)tickEntitiesPreEvent);
                              }
                           }, (SwapMode)this.swapAction.atx()).LK();
                           if (stackSlotHelper2.LL()) {
                              stackSlotHelper = stackSlotHelper2;
                           }
                        }

                        if (stackSlotHelper.LL()) {
                           if (this.autoSwitch.atx() == AuraNoneNormalMode.Silent) {
                              stackSlotHelper.LO(n2);
                              this.aoU();
                              stackSlotHelper.LP(n2);
                           } else {
                              stackSlotHelper.LO(n2);
                              if (this.swapAction.atx() == SwapMode.Hotbar) {
                                 PlayerInventory temp = client.player.getInventory();
                                 int n3 = stackSlotHelper.LM();
                                 temp.selectedSlot = n3;
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

   private void aoU() {
      if (this.list.size() > 1) {
         switch ((AuraSwitchRandomMode)this.mode.atx()) {
            case Switch:
               if (this.D >= this.list.size()) {
                  this.D = 0;
               }

               int n2 = this.D;
               this.aoZ((Entity)this.list.get(n2));
               this.D++;
               break;
            case Random:
               List temp = this.list;
               Random temp2 = random2;
               int n3 = this.list.size();
               int n4 = temp2.nextInt(n3);
               this.aoZ((Entity)temp.get(n4));
               break;
            case Multi:
               List temp3 = this.list;
               final Aura aura2 = this;
               Consumer consumer = new Consumer() {
                  @Override
                  public void accept(Object obj) {
                     aura2.aoZ((Entity)obj);
                  }
               };
               temp3.forEach(consumer);
         }
      } else {
         List list2 = this.list;
         final Aura aura3 = this;
         Consumer consumer2 = new Consumer() {
            @Override
            public void accept(Object obj) {
               aura3.aoZ((Entity)obj);
            }
         };
         list2.forEach(consumer2);
      }
   }

   @EventListener
   private void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (sendImmediatelyEvent.getPacket() instanceof UpdateSelectedSlotC2SPacket) {
         this.E = (Integer)this.switchDelay.atx();
      }
   }

   private boolean aoW() {
      Iterator iterator2 = this.list.iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (entity2 instanceof PlayerEntity) {
            PlayerEntity temp = (PlayerEntity)entity2;
            DamageSources temp2 = client.world.getDamageSources();
            ClientPlayerEntity player2 = client.player;
            DamageSource damageSource = temp2.playerAttack((PlayerEntity)player2);
            if (temp.blockedByShield(damageSource) && this.shieldMode.atx() == AuraIgnoreBreakMode.Break) {
               return true;
            }
         }
      }

      return false;
   }

   boolean aoX(Entity entity2) {
      ClientPlayerEntity player2 = client.player;
      if (!entity2.equals(player2)) {
         Entity entity3 = client.cameraEntity;
         if (!entity2.equals(entity3)) {
            if ((!(entity2 instanceof LivingEntity) || !((LivingEntity)entity2).isDead()) && entity2.isAlive()) {
               Vec3d vec = BoxUtils.jy(client.player.getEyePos(), entity2.getBoundingBox());
               if (client.player.getEyePos().distanceTo(vec) > (Double)this.range.atx()) {
                  return false;
               }

               if (!this.apc(entity2)) {
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

                  label111:
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
                           break label111;
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

                  if (this.shieldMode.atx() == AuraIgnoreBreakMode.Ignore) {
                     PlayerEntity player3 = (PlayerEntity)entity2;
                     DamageSources temp2 = client.world.getDamageSources();
                     ClientPlayerEntity player4 = client.player;
                     DamageSource damageSource = temp2.playerAttack((PlayerEntity)player4);
                     if (player3.blockedByShield(damageSource)) {
                        return false;
                     }
                  }
               }

               if (entity2 instanceof AnimalEntity) {
                  return switch ((AuraBabyAdultMode)this.mobAgeFilter.atx()) {
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

   private boolean aoY() {
      if (this.E > 0) {
         return false;
      }

      if ((Boolean)this.booleanSetting2_1.atx()) {
         return this.stopwatchImpl.afq((Float)this.hitDelay.atx() * 50.0F * Managers.entityManager.sb((CurrentAverageMode)this.currentAverageMode.atx()));
      }

      boolean flag;
      if (client.player.getAttackCooldownProgress(0.5F) >= (Float)this.attackCD.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   void aoZ(Entity entity2) {
      Vec3d vec = BoxUtils.jz(entity2.getBoundingBox());
      if (!(client.player.getEyePos().distanceTo(vec) > (Double)this.range.atx())) {
         if ((Boolean)this.rotate.atx()) {
            float[] fs = RotationUtils.nM(vec);
            RotationUtils.nH(fs[0], fs[1]);
         }

         label49: {
            label55: {
               if ((Boolean)this.booleanSetting1_1.atx() && !Criticals.bZ().awg()) {
                  float[] fs2 = RotationUtils.nM(vec);
                  RotationUtils.nH(fs2[0] - 180.0F, fs2[1]);
                  if (!Managers.vec3dPlayerHelper.BW()) {
                     Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.START_SPRINTING));
                     if (PlacementUtils.k) {
                        ClientPlayNetworkHandler network2 = client.player.networkHandler;
                        boolean flag = client.player.isSneaking();
                        PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = PlayerInteractEntityC2SPacket.attack(entity2, flag);
                        network2.sendPacket((Packet)playerInteractEntityC2SPacket);
                        client.player.resetLastAttackedTicks();
                     } else {
                        ClientPlayerEntity player2 = client.player;
                        client.interactionManager.attackEntity((PlayerEntity)player2, entity2);
                     }

                     Sprint.a = true;
                     ((SwingMode)this.swing.atx()).avX(Hand.MAIN_HAND);
                     if (!(Boolean)this.booleanSetting1_1.atx() || Criticals.bZ().awg()) {
                        break label49;
                     }

                     Managers.serverManager.iO('\uf560', new ClientCommandC2SPacket(client.player, Mode.STOP_SPRINTING));
                     break label55;
                  }
               }

               if (PlacementUtils.k) {
                  ClientPlayNetworkHandler temp = client.player.networkHandler;
                  boolean flag2 = client.player.isSneaking();
                  PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket2 = PlayerInteractEntityC2SPacket.attack(entity2, flag2);
                  temp.sendPacket((Packet)playerInteractEntityC2SPacket2);
                  client.player.resetLastAttackedTicks();
               } else {
                  ClientPlayerEntity player3 = client.player;
                  client.interactionManager.attackEntity((PlayerEntity)player3, entity2);
               }

               Sprint.a = true;
               ((SwingMode)this.swing.atx()).avX(Hand.MAIN_HAND);
               if (!(Boolean)this.booleanSetting1_1.atx() || Criticals.bZ().awg()) {
                  break label49;
               }
            }

            if ((Boolean)this.rotate.atx()) {
               float[] fs3 = RotationUtils.nM(vec);
               RotationUtils.nH(fs3[0], fs3[1]);
            }
         }

         if ((Boolean)this.rotate.atx()) {
            Managers.movementTracker.Mk();
         }

         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
      }
   }

   private boolean apa() {
      if (SpearFix.nt(client.player.getMainHandStack())) {
         return false;
      }

      if (this.aoW()) {
         return client.player.getMainHandStack().getItem() instanceof AxeItem;
      }

      return switch ((AuraSwordAxeMode)this.weapon.atx()) {
         case Sword -> client.player.getMainHandStack().getItem() instanceof SwordItem;
         case Axe -> client.player.getMainHandStack().getItem() instanceof AxeItem;
         case Mace -> client.player.getMainHandStack().getItem() instanceof MaceItem;
         case Trident -> client.player.getMainHandStack().getItem() instanceof TridentItem;
         case All -> {
            if (!(client.player.getMainHandStack().getItem() instanceof AxeItem)
               && !(client.player.getMainHandStack().getItem() instanceof SwordItem)
               && !(client.player.getMainHandStack().getItem() instanceof MaceItem)
               && !(client.player.getMainHandStack().getItem() instanceof TridentItem)) {
               yield false;
            } else {
               yield true;
            }
         }
         default -> true;
      };
   }

   public Entity apb() {
      return !this.list.isEmpty() ? (Entity)this.list.getFirst() : null;
   }

   @Override
   public String hy() {
      return !this.list.isEmpty() ? this.apb().getName().getString() : super.hy();
   }

   private boolean apc(Entity entity2) {
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

   static boolean apd(ItemStack stack) {
      return stack.getItem() instanceof AxeItem;
   }

   static boolean ape(ItemStack stack) {
      return true;
   }

   static boolean apf(ItemStack stack) {
      boolean flag;
      if (!(stack.getItem() instanceof AxeItem)
         && !(stack.getItem() instanceof SwordItem)
         && !(stack.getItem() instanceof MaceItem)
         && !(stack.getItem() instanceof TridentItem)) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   static boolean apg(ItemStack stack) {
      return stack.getItem() instanceof TridentItem;
   }

   static boolean aph(ItemStack stack) {
      return stack.getItem() instanceof MaceItem;
   }

   static boolean api(ItemStack stack) {
      return stack.getItem() instanceof SwordItem;
   }

   static boolean apj(ItemStack stack) {
      return stack.getItem() instanceof AxeItem;
   }

   private Boolean isAttackCDVisible() {
      boolean flag;
      if (!(Boolean)this.booleanSetting2_1.atx()) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private Boolean isModeVisible() {
      boolean flag;
      if ((Integer)this.maxTargets.atx() > 1) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static MatchException n() {
      return new MatchException(null, null);
   }
}
