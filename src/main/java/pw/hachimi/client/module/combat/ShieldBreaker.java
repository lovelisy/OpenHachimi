package pw.hachimi.client.module.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.network.SendImmediatelyEvent;
import pw.hachimi.client.handler.EntityHandler;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.combat.ShieldBreakerSwitchRandomMode;
import pw.hachimi.client.model.combat.TargetPriority;
import pw.hachimi.client.model.player.SwapMode;
import pw.hachimi.client.model.player.SwingMode;
import pw.hachimi.client.model.world.EntityInteractType;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.Multitask;
import pw.hachimi.client.module.exploit.Blink;
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

public class ShieldBreaker extends Multitask {
   private static ShieldBreaker shieldBreaker;
   private final List list = new ArrayList();
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("Rotate", "Automatically faces towards the target", true));
   Setting swingMode = this.registerSetting(new EnumSetting("Swing", "Swing side for attack", SwingMode.Normal, SwingMode.values()));
   Setting swapAction = this.registerSetting(new EnumSetting("SwapAction", "Decide which swap mode to use", SwapMode.Hotbar, SwapMode.values()));
   Setting sortPriority = this.registerSetting(new EnumSetting("SortPriority", "How to filter targets within range", TargetPriority.ClosestAngle, TargetPriority.values()));
   Setting maxTargets = this.registerSetting(new NumberSetting("MaxTargets", "How many entities to target at once", 1, 1, 10));
   Setting mode = this.registerSetting(new EnumSetting("Mode", "mode", ShieldBreakerSwitchRandomMode.Switch, ShieldBreakerSwitchRandomMode.values(), this::isModeVisible));
   Setting range = this.registerSetting(new NumberSetting("Range", "The maximum range the entity can be to attack it", 0.0, 3.0, 6.0));
   Setting wallsRange = this.registerSetting(new NumberSetting("WallsRange", "The maximum range the entity can be attacked through walls", 0.0, 3.0, 6.0));
   Setting hitDelay = this.registerSetting(new NumberSetting("HitDelay", "How fast you hit the entity", 0.0F, 11.0F, 60.0F));
   boolean l;
   StopwatchImpl stopwatchImpl = new StopwatchImpl();
   int n;

   public ShieldBreaker() {
      super("ShieldBreaker", "Attacks specified entities around you", ModuleCategory.COMBAT, 700);
      shieldBreaker = this;
   }

   public static ShieldBreaker Xm() {
      return shieldBreaker;
   }

   @Override
   public void awe() {
      this.list.clear();
   }

   @EventListener
   public void Xn(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (TargetUtils.Xa(client.player) != GameMode.SPECTATOR) {
         if ((Boolean)this.breakSetting.atx() || !this.RH(true)) {
            if (!Blink.bm()) {
               this.list.clear();
               final ShieldBreaker shieldBreaker2 = this;
               TargetUtils.WY(this.list, new Predicate() {
                  @Override
                  public boolean test(Object tickEntitiesPreEvent) {
                     return shieldBreaker2.Xq((Entity)tickEntitiesPreEvent);
                  }
               }, (TargetPriority)this.sortPriority.atx(), (Integer)this.maxTargets.atx());
               if (!this.list.isEmpty()) {
                  if (this.Xr()) {
                     StackSlotHelper stackSlotHelper = new StackSlotHelper(new Predicate() {
                        @Override
                        public boolean test(Object tickEntitiesPreEvent) {
                           return ShieldBreaker.Xu((ItemStack)tickEntitiesPreEvent);
                        }
                     }, (SwapMode)this.swapAction.atx()).LK();
                     int n2 = Managers.inventoryTracker.JS();
                     if (stackSlotHelper.LL()) {
                        stackSlotHelper.LO(n2);
                        this.Xo();
                        stackSlotHelper.LP(n2);
                     }
                  }
               }
            }
         }
      }
   }

   private void Xo() {
      this.l = true;
      if (this.list.size() > 1) {
         switch ((ShieldBreakerSwitchRandomMode)this.mode.atx()) {
            case Switch:
               if (this.n >= this.list.size()) {
                  this.n = 0;
               }

               int n2 = this.n;
               this.Xs((Entity)this.list.get(n2));
               this.n++;
               break;
            case Random:
               List temp = this.list;
               Random temp2 = random2;
               int n3 = this.list.size();
               int n4 = temp2.nextInt(n3);
               this.Xs((Entity)temp.get(n4));
               break;
            case Multi:
               List temp3 = this.list;
               final ShieldBreaker shieldBreaker2 = this;
               Consumer consumer = new Consumer() {
                  @Override
                  public void accept(Object obj) {
                     shieldBreaker2.Xs((Entity)obj);
                  }
               };
               temp3.forEach(consumer);
         }
      } else {
         List list2 = this.list;
         final ShieldBreaker shieldBreaker3 = this;
         Consumer consumer2 = new Consumer() {
            @Override
            public void accept(Object obj) {
               shieldBreaker3.Xs((Entity)obj);
            }
         };
         list2.forEach(consumer2);
      }

      this.l = false;
   }

   @EventListener(priority = 999999)
   public void onSendImmediately(SendImmediatelyEvent sendImmediatelyEvent) {
      if (this.l) {
         Packet packet2 = sendImmediatelyEvent.getPacket();
         if (packet2 instanceof EntityHandler && ((EntityHandler)packet2).Qd() == EntityInteractType.ATTACK) {
            ((EntityHandler)packet2).Qf();
         }
      }
   }

   boolean Xq(Entity entity2) {
      ClientPlayerEntity player2 = client.player;
      if (!entity2.equals(player2)) {
         Entity entity3 = client.cameraEntity;
         if (!entity2.equals(entity3)) {
            if ((!(entity2 instanceof LivingEntity) || !((LivingEntity)entity2).isDead()) && entity2.isAlive()) {
               Vec3d vec = BoxUtils.jy(client.player.getEyePos(), entity2.getBoundingBox());
               if (client.player.getEyePos().distanceTo(vec) > (Double)this.range.atx()) {
                  return false;
               }

               if (!WorldUtils.Nk(entity2) && client.player.getEyePos().distanceTo(vec) > (Double)this.wallsRange.atx()) {
                  return false;
               }

               if (entity2 instanceof PlayerEntity) {
                  if (((PlayerEntity)entity2).isCreative()) {
                     return false;
                  }

                  if (!((PlayerEntity)entity2).isUsingItem()) {
                     return false;
                  }

                  if (Managers.friendManager.awr((PlayerEntity)entity2)) {
                     return false;
                  }

                  return ((PlayerEntity)entity2).getActiveItem().getItem() instanceof ShieldItem;
               }

               return false;
            }

            return false;
         }
      }

      return false;
   }

   private boolean Xr() {
      return this.stopwatchImpl.afq((Float)this.hitDelay.atx() * 50.0F);
   }

   void Xs(Entity entity2) {
      Vec3d vec = BoxUtils.jz(entity2.getBoundingBox());
      if (!(client.player.getEyePos().distanceTo(vec) > (Double)this.range.atx())) {
         if ((Boolean)this.booleanSetting1_1.atx()) {
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

         Sprint.a = true;
         ((SwingMode)this.swingMode.atx()).avX(Hand.MAIN_HAND);
         if ((Boolean)this.booleanSetting1_1.atx()) {
            Managers.movementTracker.Mk();
         }

         this.stopwatchImpl.afr(29432, 5265492, (byte)40);
      }
   }

   public Entity Xt() {
      return !this.list.isEmpty() ? (Entity)this.list.getFirst() : null;
   }

   @Override
   public String hy() {
      return !this.list.isEmpty() ? this.Xt().getName().getString() : super.hy();
   }

   static boolean Xu(ItemStack stack) {
      return stack.getItem() instanceof AxeItem;
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
}
