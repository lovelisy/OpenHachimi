package pw.hachimi.client.model.combat;

import pw.hachimi.client.config.ConfigContainer;
import pw.hachimi.client.module.movement.ElytraFly;
import pw.hachimi.client.module.movement.FakeFly;

public enum AutoCrystalNonePlaceMode {
   NONE,
   PLACE,
   BREAK,
   NORMAL,
   PACKET,
   GRIM;

   private static final AutoCrystalNonePlaceMode[] g = afB();

   public boolean afx() {
      if (this == GRIM) {
         if (FakeFly.qr() != null && FakeFly.qr().awg()) {
            return false;
         }

         boolean flag;
         if (!ElytraFly.di().awg() && !ConfigContainer.client.player.isFallFlying()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         boolean flag2;
         if (this != NONE && this != PACKET) {
            flag2 = true;
         } else {
            flag2 = false;
         }

         return flag2;
      }
   }

   public boolean afy() {
      if (this == GRIM) {
         if (FakeFly.qr() != null && FakeFly.qr().awg()) {
            return false;
         }

         boolean flag;
         if (!ElytraFly.di().awg() && !ConfigContainer.client.player.isFallFlying()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         boolean flag2;
         if (this != PLACE && this != NORMAL) {
            flag2 = false;
         } else {
            flag2 = true;
         }

         return flag2;
      }
   }

   public boolean afz() {
      if (this == GRIM) {
         if (FakeFly.qr() != null && FakeFly.qr().awg()) {
            return false;
         }

         boolean flag;
         if (!ElytraFly.di().awg() && !ConfigContainer.client.player.isFallFlying()) {
            flag = true;
         } else {
            flag = false;
         }

         return flag;
      } else {
         boolean flag2;
         if (this != BREAK && this != NORMAL) {
            flag2 = false;
         } else {
            flag2 = true;
         }

         return flag2;
      }
   }

   public boolean afA() {
      if (this == GRIM) {
         return FakeFly.qr() != null && FakeFly.qr().awg() ? true : ElytraFly.di().awg();
      }

      boolean flag;
      if (this == PACKET) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static AutoCrystalNonePlaceMode[] afB() {
      return new AutoCrystalNonePlaceMode[]{NONE, PLACE, BREAK, NORMAL, PACKET, GRIM};
   }
}
