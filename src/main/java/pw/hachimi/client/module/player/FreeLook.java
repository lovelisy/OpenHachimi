package pw.hachimi.client.module.player;

import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.MathHelper;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.UpdateEvent;
import pw.hachimi.client.event.player.UpdateRotationEvent;
import pw.hachimi.client.event.render.SetPerspectiveEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class FreeLook extends ClientModule {
   private float a;
   private float b;
   private Perspective perspective;
   public static boolean cy;

   public FreeLook() {
      super("FreeLook", "Allows you to freely move the camera in third person", ModuleCategory.PLAYER);
   }

   @Override
   public void awd() {
      if (client.options != null) {
         this.perspective = client.options.getPerspective();
      }
   }

   @Override
   public void awe() {
      if (this.perspective != null) {
         Perspective perspective2 = this.perspective;
         client.options.setPerspective(perspective2);
      }
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (this.perspective != null && this.perspective != Perspective.THIRD_PERSON_BACK) {
         Perspective perspective2 = Perspective.THIRD_PERSON_BACK;
         client.options.setPerspective(perspective2);
      }
   }

   @EventListener
   public void onSetPerspective(SetPerspectiveEvent setPerspectiveEvent) {
      if (client.options.getPerspective() != setPerspectiveEvent.getPerspective() && setPerspectiveEvent.getPerspective() != Perspective.FIRST_PERSON) {
         this.a = client.player.getYaw();
         this.b = client.player.getPitch();
      }
   }

   @EventListener
   public void onUpdateRotation(UpdateRotationEvent updateRotationEvent) {
      if (client.options.getPerspective() != Perspective.FIRST_PERSON) {
         updateRotationEvent.EU(this.a);
         updateRotationEvent.EW(this.b);
      }
   }

   @EventListener
   public void onUpdate(UpdateEvent updateEvent) {
      if (client.options.getPerspective() != Perspective.FIRST_PERSON) {
         updateEvent.aim();
         this.AE(updateEvent.lR(), updateEvent.lS());
      }
   }

   private void AE(double d, double d2) {
      float f = (float)d2 * 0.15F;
      float f2 = (float)d * 0.15F;
      this.b += f;
      this.a += f2;
      this.b = MathHelper.clamp(this.b, -90.0F, 90.0F);
   }
}
