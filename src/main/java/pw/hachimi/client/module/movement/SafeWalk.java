package pw.hachimi.client.module.movement;

import net.minecraft.util.math.Box;
import pw.hachimi.client.event.ClipAtLedgeEvent;
import pw.hachimi.client.event.Tick_PreEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.world.CollisionUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class SafeWalk extends ClientModule {
   Setting legit = this.registerSetting(new BooleanSetting("Legit", "Legit safe walk", false));
   Setting offset = this.registerSetting(new NumberSetting("Offset", "offset for checks", 0.0F, 0.3F, 0.5F));
   Setting phasedOnly = this.registerSetting(new BooleanSetting("PhasedOnly", "Works only when you are phasing", false));
   public static boolean bp_1;

   public SafeWalk() {
      super("SafeWalk", "Prevents you from walking off ledges", ModuleCategory.MOVEMENT);
   }

   @EventListener
   public void onClipAtLedge(ClipAtLedgeEvent clipAtLedgeEvent) {
      if (!client.player.isSneaking() && !(Boolean)this.legit.atx() && this.pS()) {
         clipAtLedgeEvent.aim();
         clipAtLedgeEvent.qa();
      }
   }

   @EventListener(priority = -100)
   public void onTick_Pre(Tick_PreEvent tickPreEvent) {
      if (client.player.isOnGround() && (Boolean)this.legit.atx() && this.pS()) {
         if (this.pT(((Float)this.offset.atx()).floatValue(), 0.0)) {
            client.player.input.sneaking = true;
         } else if (this.pT(0.0, ((Float)this.offset.atx()).floatValue())) {
            client.player.input.sneaking = true;
         } else if (this.pT(((Float)this.offset.atx()).floatValue(), ((Float)this.offset.atx()).floatValue())) {
            client.player.input.sneaking = true;
         } else if (this.pT(-(Float)this.offset.atx(), 0.0)) {
            client.player.input.sneaking = true;
         } else if (this.pT(0.0, -(Float)this.offset.atx())) {
            client.player.input.sneaking = true;
         } else if (this.pT(-(Float)this.offset.atx(), -(Float)this.offset.atx())) {
            client.player.input.sneaking = true;
         }
      }
   }

   public boolean pS() {
      boolean flag;
      if ((Boolean)this.phasedOnly.atx() && !Velocity.amY()) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public boolean pT(double d, double d2) {
      Box box = client.player.getBoundingBox();
      Box box2 = new Box(box.minX, box.minY, box.maxZ, box.maxX, box.minY + 0.5, box.maxZ);
      boolean flag;
      if (!CollisionUtils.aiy(client.player, box2.offset(d, -1.0, d2))) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }
}
