package pw.hachimi.client.module.movement;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.combat.AttackEvent2;
import pw.hachimi.client.event.movement.Jump_getYawEvent;
import pw.hachimi.client.event.movement.SetSprintingEvent;
import pw.hachimi.client.event.movement.TickMovementEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.movement.SprintLegitRageMode;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.movement.MovementUtils;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.DirectionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Sprint extends Module5 {
   private static Sprint INSTANCE;
   public static boolean a;
   Setting mode = this.registerSetting(new EnumSetting("Mode", "Sprinting mode. Rage allows for multi-directional sprinting.", SprintLegitRageMode.LEGIT, SprintLegitRageMode.values()));
   Setting jumpFix = this.registerSetting(new BooleanSetting("JumpFix", "Fixes jumping slowdown in Rage sprint", true, this::isJumpFixVisible));
   Setting webFix = this.registerSetting(new BooleanSetting("WebFix", "Stop sprinting when you in web", true));
   Setting keepSprint = this.registerSetting(new BooleanSetting("KeepSprint", "Removes sprint reset when attacking", false));
   public static boolean cu;

   public Sprint() {
      super("Sprint", "Automatically sprints", ModuleCategory.MOVEMENT, 110);
      INSTANCE = this;
   }

   public static Sprint mD() {
      return INSTANCE;
   }

   @Override
   public String hy() {
      return DirectionUtils.apW((Enum)this.mode.atx());
   }

   @EventListener
   public void onAttackEvent2(AttackEvent2 attackEvent2) {
      if ((Boolean)this.keepSprint.atx()) {
         attackEvent2.aim();
      } else {
         a = true;
      }
   }

   @EventListener
   public void onTickMovement(TickMovementEvent tickMovementEvent) {
      if (a) {
         a = false;
         if (!(Boolean)this.keepSprint.atx()) {
            client.player.setSprinting(false);
            return;
         }
      }

      if (this.mJ(false)) {
         float f = this.mL(client.player.getYaw());
         if (this.mK(f)) {
            return;
         }

         client.player.setSprinting(true);
      }
   }

   @EventListener
   public void onSetSprinting(SetSprintingEvent setSprintingEvent) {
      if (a && !(Boolean)this.keepSprint.atx()) {
         return;
      }

      if (this.mJ(false)) {
         float f = this.mL(client.player.getYaw());
         if (this.mK(f)) {
            return;
         }

         setSprintingEvent.aim();
      }
   }

   @EventListener
   public void mH(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if (this.mJ(true) && (this.mode.atx() == SprintLegitRageMode.RAGE_STRICT || this.mode.atx() == SprintLegitRageMode.GRIM)) {
         this.ajM(this.mL(client.player.getYaw()), client.player.getPitch());
      }
   }

   @EventListener
   public void onJump_getYaw(Jump_getYawEvent jumpgetYawEvent) {
      if ((Boolean)this.jumpFix.atx() && (this.mode.atx() == SprintLegitRageMode.RAGE || this.mode.atx() == SprintLegitRageMode.RAGE_STRICT)) {
         float f = jumpgetYawEvent.On();
         float f2 = Math.signum(client.player.input.movementForward);
         float f3 = 90.0F * Math.signum(client.player.input.movementSideways);
         if (f2 != 0.0F) {
            f3 *= f2 * 0.5F;
         }

         f -= f3;
         if (f2 < 0.0F) {
            f -= 180.0F;
         }

         jumpgetYawEvent.aim();
         jumpgetYawEvent.Oo(f);
      }
   }

   private boolean mJ(boolean flag) {
      if (this.mode.atx() == SprintLegitRageMode.OFF) {
         return false;
      }

      if ((Boolean)this.webFix.atx() && WorldUtils.Ng(0.3)) {
         return false;
      }

      boolean flag2;
      if (flag) {
         flag2 = MovementUtils.awH();
      } else {
         if (MovementUtils.awI()
            && !client.player.isSneaking()
            && !client.player.isRiding()
            && !client.player.isFallFlying()
            && !client.player.isTouchingWater()
            && !client.player.isInLava()
            && !client.player.isHoldingOntoLadder()) {
            RegistryEntry registryEntry = StatusEffects.BLINDNESS;
            if (!client.player.hasStatusEffect(registryEntry) && client.player.getHungerManager().getFoodLevel() > 6.0F) {
               flag2 = true;
               return flag2;
            }
         }

         flag2 = false;
      }

      return flag2;
   }

   private boolean mK(float f) {
      if (this.mode.atx() == SprintLegitRageMode.RAGE_STRICT) {
         boolean flag;
         if (MathHelper.angleBetween(f, Managers.movementTracker.Mo()) > 0.0F) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         if (this.mode.atx() != SprintLegitRageMode.GRIM && this.mode.atx() != SprintLegitRageMode.LEGIT) {
            return false;
         }

         boolean flag2;
         if (!(client.player.input.movementForward <= 1.0E-5F) && (!client.player.horizontalCollision || client.player.collidedSoftly)) {
            flag2 = false;
         } else {
            flag2 = true;
         }

         return flag2;
      }
   }

   public float mL(float f) {
      boolean flag = client.options.forwardKey.isPressed();
      boolean flag2 = client.options.backKey.isPressed();
      boolean flag3 = client.options.leftKey.isPressed();
      boolean flag4 = client.options.rightKey.isPressed();
      if (flag && !flag2) {
         if (flag3 && !flag4) {
            f -= 45.0F;
         } else if (flag4 && !flag3) {
            f += 45.0F;
         }
      } else if (flag2 && !flag) {
         f += 180.0F;
         if (flag3 && !flag4) {
            f += 45.0F;
         } else if (flag4 && !flag3) {
            f -= 45.0F;
         }
      } else if (flag3 && !flag4) {
         f -= 90.0F;
      } else if (flag4 && !flag3) {
         f += 90.0F;
      }

      return MathHelper.wrapDegrees(f - client.player.getYaw()) + client.player.getYaw();
   }

   private Boolean isJumpFixVisible() {
      boolean flag;
      if (this.mode.atx() != SprintLegitRageMode.RAGE && this.mode.atx() != SprintLegitRageMode.RAGE_STRICT) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }
}
