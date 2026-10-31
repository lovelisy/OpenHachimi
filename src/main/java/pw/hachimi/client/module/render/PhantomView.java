package pw.hachimi.client.module.render;

import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.ClipToSpaceEvent;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.world.UpdatePositionEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.world.EntityBoxUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class PhantomView extends ClientModule {
   private static PhantomView INSTANCE;
   Setting distance = this.registerSetting(new NumberSetting("Distance", "The third-person camera clip distance", 1.0F, 3.5F, 20.0F));
   Setting dynamic = this.registerSetting(new BooleanSetting("Dynamic", "Make your camera move smoothly", false));
   Setting speed = this.registerSetting(new NumberSetting("Speed", "Motion speed", 0.0F, 0.81F, 1.0F));
   Setting thirdSpeed = this.registerSetting(new NumberSetting("ThirdSpeed", "Motion speed for third-person", 0.0F, 0.41F, 1.0F));
   double e;
   double f;
   double g;
   double h;
   double i;
   double j;
   public static boolean ac;

   public PhantomView() {
      super("PhantomView", "Clips your third-person camera through blocks", ModuleCategory.RENDER);
      INSTANCE = this;
   }

   public static PhantomView DG() {
      return INSTANCE;
   }

   public static double DH(double d, double d2, double d3) {
      if (d3 >= 1.0) {
         return d2;
      }

      if (d3 == 0.0) {
         return d;
      }

      double d4;
      if (d2 > d) {
         d4 = (Math.max(d2, d) - Math.min(d2, d)) * d3;
      } else {
         d4 = -((Math.max(d2, d) - Math.min(d2, d)) * d3);
      }

      return d + d4;
   }

   @Override
   public void awd() {
      if (client.player != null) {
         this.e = client.player.getX();
         this.f = client.player.getEyeY();
         this.g = client.player.getZ();
         this.h = this.e;
         this.i = this.f;
         this.j = this.g;
      }
   }

   @EventListener
   public void DI(TickEntitiesPreEvent tickEntitiesPreEvent) {
      if ((Boolean)this.dynamic.atx()) {
         this.h = this.e;
         this.i = this.f;
         this.j = this.g;
         Float floatValue2;
         if (client.options.getPerspective().isFirstPerson()) {
            floatValue2 = (Float)this.speed.atx();
         } else {
            floatValue2 = (Float)this.thirdSpeed.atx();
         }

         double d = floatValue2.floatValue();
         this.e = DH(this.e, client.player.getX(), d);
         this.f = DH(this.f, client.player.getEyeY(), d);
         this.g = DH(this.g, client.player.getZ(), d);
      }
   }

   @EventListener
   public void onUpdatePosition(UpdatePositionEvent updatePositionEvent) {
      if ((Boolean)this.dynamic.atx()) {
         Float floatValue2;
         if (client.options.getPerspective().isFirstPerson()) {
            floatValue2 = (Float)this.speed.atx();
         } else {
            floatValue2 = (Float)this.thirdSpeed.atx();
         }

         if (floatValue2.floatValue() != 1.0) {
            updatePositionEvent.DF(new Vec3d(this.DL(), this.DM(), this.DN()));
         }
      }
   }

   @EventListener
   public void onClipToSpace(ClipToSpaceEvent clipToSpaceEvent) {
      clipToSpaceEvent.aim();
      clipToSpaceEvent.TX((Float)this.distance.atx());
   }

   public double DL() {
      return EntityBoxUtils.BC(this.h, this.e, client.getRenderTickCounter().getTickDelta(true));
   }

   public double DM() {
      return EntityBoxUtils.BC(this.i, this.f, client.getRenderTickCounter().getTickDelta(true));
   }

   public double DN() {
      return EntityBoxUtils.BC(this.j, this.g, client.getRenderTickCounter().getTickDelta(true));
   }
}
