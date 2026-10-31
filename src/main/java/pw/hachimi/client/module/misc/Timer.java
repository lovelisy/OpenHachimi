package pw.hachimi.client.module.misc;

import java.text.DecimalFormat;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.event.render.BeginRenderTickEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.module.movement.Speed;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class Timer extends ClientModule {
   private static Timer INSTANCE;
   Setting ticks = this.registerSetting(new NumberSetting("Ticks", "The game tick speed", 0.1F, 2.0F, 50.0F));
   Setting tPSSync = this.registerSetting(new BooleanSetting("TPSSync", "Syncs game tick speed to server tick speed", false));
   private float c = -1.0F;
   private float d = 1.0F;
   public static boolean ch;

   public Timer() {
      super("Timer", "Changes the client tick speed", ModuleCategory.MISC);
   }

   public static Timer fP() {
      if (INSTANCE == null) {
         INSTANCE = new Timer();
      }

      return INSTANCE;
   }

   @Override
   public String hy() {
      DecimalFormat temp = d();
      double d2 = this.d;
      return temp.format(d2);
   }

   @Override
   public void awa() {
      Speed.lq().lz();
      if (!Speed.lq().lA()) {
         super.awa();
      }
   }

   @Override
   public void awe() {
      Managers.entityManager.rT(1.0F);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         if (Speed.lq().lA()) {
            return;
         }

         if ((Boolean)this.tPSSync.atx()) {
            this.d = Math.max(Managers.entityManager.rX() / 20.0F, 0.1F);
            return;
         }

         this.d = (Float)this.ticks.atx();
      }
   }

   @EventListener
   public void onBeginRenderTick(BeginRenderTickEvent beginRenderTickEvent) {
      if (this.d != 1.0F) {
         beginRenderTickEvent.aim();
         beginRenderTickEvent.qT(this.d);
      }
   }

   public float fS() {
      return this.d;
   }

   public void fT(float f) {
      this.c = this.d;
      this.d = f;
   }

   public void fU() {
      if (this.c > 0.0F) {
         this.d = this.c;
         this.c = -1.0F;
      }
   }

   private static DecimalFormat d() {
      return new DecimalFormat("0.0#");
   }
}
