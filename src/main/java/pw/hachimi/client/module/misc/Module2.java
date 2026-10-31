package pw.hachimi.client.module.misc;

import pw.hachimi.client.event.render.RenderEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.eventbus.annotation.EventListener;

public class Module2 extends ClientModule {
   private static final long a = 0L;
   private static final String b = null;

   public Module2() {
      super(b, "", ModuleCategory.MISC);
   }

   @EventListener
   public void onRender(RenderEvent renderEvent) {
   }

   @Override
   protected void awd() {
   }
}
