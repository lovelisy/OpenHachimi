package pw.hachimi.client.module.misc;

import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.model.movement.SpinBotOffStaticMode;
import pw.hachimi.client.model.movement.SpinBotOffStaticMode2;
import pw.hachimi.client.module.Module5;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class SpinBot extends Module5 {
   Setting yaw = this.registerSetting(new EnumSetting("Yaw", "The mode for the rotation yaw spin ", SpinBotOffStaticMode2.SPIN, SpinBotOffStaticMode2.values()));
   Setting pitch = this.registerSetting(new EnumSetting("Pitch", "The mode for the rotation pitch spin", SpinBotOffStaticMode.DOWN, SpinBotOffStaticMode.values()));
   Setting yawAdd = this.registerSetting(new NumberSetting("YawAdd", "The yaw to add during each rotation", -180.0F, 20.0F, 180.0F));
   Setting customPitch = this.registerSetting(new NumberSetting("CustomPitch", "The pitch to add during each rotation", -90.0F, 20.0F, 90.0F));
   Setting spinSpeed = this.registerSetting(new NumberSetting("SpinSpeed", "The yaw speed to rotate", 1.0F, 16.0F, 40.0F));
   Setting flipTicks = this.registerSetting(new NumberSetting("FlipTicks", "The number of ticks to wait between jitter", 2, 2, 20));
   private float g;
   private float h;
   private float i;
   private float j;
   public static boolean ct;

   public SpinBot() {
      super("SpinBot", "Makes it harder to accurately aim at the player", ModuleCategory.MISC, 50);
   }

   @Override
   public void awd() {
      if (client.player != null) {
         this.i = client.player.getYaw();
         this.j = client.player.getPitch();
      }
   }

   @EventListener
   // restored with CFR (compiles, call/field/constant fingerprint equals the deobf jar)
   public void kS(TickEntitiesPreEvent tickEntitiesPreEvent) {
       float f;
       float f2;
       if (SpinBot.client.options.attackKey.isPressed() || SpinBot.client.options.useKey.isPressed()) {
           return;
       }
       switch (((SpinBotOffStaticMode2)((Object)this.yaw.atx())).ordinal()) {
           default: {
               SpinBot spinBot = this;
               throw SpinBot.i();
           }
           case 4: {
               float f3;
               int n = SpinBot.client.player.age % (Integer)this.flipTicks.atx();
               f2 = SpinBot.client.player.getYaw();
               if (n == 0) {
                   float f4 = f2;
                   f2 = ((Float)this.yawAdd.atx()).floatValue();
                   f3 = f4;
               } else {
                   float f5 = f2;
                   f2 = -((Float)this.yawAdd.atx()).floatValue();
                   f3 = f5;
               }
               f2 = f3 + f2;
               break;
           }
           case 3: {
               f = this.g + ((Float)this.spinSpeed.atx()).floatValue();
               if (f > 360.0f) {
                   f2 = f - 360.0f;
                   break;
               }
               f2 = f;
               break;
           }
           case 2: {
               f2 = this.i;
               break;
           }
           case 1: {
               f2 = SpinBot.client.player.getYaw() + ((Float)this.yawAdd.atx()).floatValue();
               break;
           }
           case 0: {
               f2 = SpinBot.client.player.getYaw();
           }
       }
       this.g = f2;
       switch (((SpinBotOffStaticMode)((Object)this.pitch.atx())).ordinal()) {
           default: {
               SpinBot spinBot = this;
               throw SpinBot.i();
           }
           case 5: {
               f = this.h + 30.0f;
               if (f > 90.0f) {
                   f2 = -90.0f;
                   break;
               }
               if (f < -90.0f) {
                   f2 = 90.0f;
                   break;
               }
               f2 = f;
               break;
           }
           case 4: {
               f2 = 90.0f;
               break;
           }
           case 3: {
               f2 = -90.0f;
               break;
           }
           case 2: {
               f2 = this.j;
               break;
           }
           case 1: {
               f2 = ((Float)this.customPitch.atx()).floatValue();
               break;
           }
           case 0: {
               f2 = SpinBot.client.player.getPitch();
           }
       }
       this.h = f2;
       this.ajM(this.g, this.h);
   }

   private static MatchException i() {
      return new MatchException(null, null);
   }
}
