package pw.hachimi.client.utils.movement;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import pw.hachimi.client.Mc;
import pw.hachimi.client.utils.player.EnchantmentUtils;

public class MovementUtils {
   public static boolean a;

   public static boolean awH() {
      boolean flag;
      if (!Mc.client.options.forwardKey.isPressed()
         && !Mc.client.options.backKey.isPressed()
         && !Mc.client.options.leftKey.isPressed()
         && !Mc.client.options.rightKey.isPressed()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean awI() {
      boolean flag;
      if (Mc.client.player.input.movementForward == 0.0F && Mc.client.player.input.movementSideways == 0.0F) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public static boolean awJ() {
      double d = Mc.client.player.getX() - Mc.client.player.lastX;
      double d2 = Mc.client.player.getY() - Mc.client.player.lastBaseY;
      double d3 = Mc.client.player.getZ() - Mc.client.player.lastZ;
      boolean flag;
      if (MathHelper.squaredMagnitude(d, d2, d3) > MathHelper.square(2.0E-4)) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public static void awK() {
      EquipmentSlot equipmentSlot = EquipmentSlot.FEET;
      float f = MathHelper.clamp(0.3F + EnchantmentUtils.VN(Mc.client.player.getEquippedStack(equipmentSlot), Enchantments.SWIFT_SNEAK) * 0.15F, 0.0F, 1.0F);
      float f2 = Mc.client.player.input.movementForward * f;
      Mc.client.player.input.movementForward = f2;
      float f3 = Mc.client.player.input.movementSideways * f;
      Mc.client.player.input.movementSideways = f3;
   }

   public static Vec2f awL(double d, double d2) {
      double d3 = d;
      double d4 = d2;
      float f = -Mc.client.player.getStepHeight();
      if (!Mc.client.player.isOnGround()) {
         f = -1.5F;
      }

      while (d3 != 0.0) {
         ClientWorld temp = Mc.client.world;
         ClientPlayerEntity temp2 = Mc.client.player;
         Box temp3 = Mc.client.player.getBoundingBox();
         double d5 = f;
         double d6 = d3;
         Box box = temp3.offset(d6, d5, 0.0);
         ClientPlayerEntity player2 = temp2;
         if (!temp.isSpaceEmpty((Entity)player2, box)) {
            break;
         }

         if (d3 < 0.05 && d3 >= -0.05) {
            d3 = 0.0;
         } else if (d3 > 0.0) {
            d3 -= 0.05;
         } else {
            d3 += 0.05;
         }
      }

      while (d4 != 0.0) {
         ClientWorld world2 = Mc.client.world;
         ClientPlayerEntity player3 = Mc.client.player;
         Box box2 = Mc.client.player.getBoundingBox();
         double temp4 = f;
         double d7 = d4;
         double d8 = temp4;
         Box box3 = box2.offset(0.0, d8, d7);
         ClientPlayerEntity player4 = player3;
         if (!world2.isSpaceEmpty((Entity)player4, box3)) {
            break;
         }

         if (d4 < 0.05 && d4 >= -0.05) {
            d4 = 0.0;
         } else if (d4 > 0.0) {
            d4 -= 0.05;
         } else {
            d4 += 0.05;
         }
      }

      while (d3 != 0.0 && d4 != 0.0) {
         ClientWorld world3 = Mc.client.world;
         ClientPlayerEntity player5 = Mc.client.player;
         Box box4 = Mc.client.player.getBoundingBox();
         double temp5 = f;
         double d9 = d4;
         double d10 = temp5;
         double d11 = d3;
         Box box5 = box4.offset(d11, d10, d9);
         ClientPlayerEntity player6 = player5;
         if (!world3.isSpaceEmpty((Entity)player6, box5)) {
            break;
         }

         if (d3 < 0.05 && d3 >= -0.05) {
            d3 = 0.0;
         } else if (d3 > 0.0) {
            d3 -= 0.05;
         } else {
            d3 += 0.05;
         }

         if (d4 < 0.05 && d4 >= -0.05) {
            d4 = 0.0;
         } else if (d4 > 0.0) {
            d4 -= 0.05;
         } else {
            d4 += 0.05;
         }
      }

      return new Vec2f((float)d3, (float)d4);
   }

   public static float awM(Input input2, float f) {
      if (input2.movementForward < 0.0F) {
         f += 180.0F;
      }

      float f2 = 1.0F;
      if (input2.movementForward < 0.0F) {
         f2 = -0.5F;
      } else if (input2.movementForward > 0.0F) {
         f2 = 0.5F;
      }

      float f3 = input2.movementSideways;
      if (f3 > 0.0F) {
         f -= 90.0F * f2;
      }

      if (f3 < 0.0F) {
         f += 90.0F * f2;
      }

      return f;
   }
}
