package pw.hachimi.client.module.render;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.TickEntitiesPreEvent;
import pw.hachimi.client.event.world.RemoveEntityEvent;
import pw.hachimi.client.model.world.PosTime;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.EntityBoxUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class Breadcrumbs extends ClientModule {
   private final Map map = new ConcurrentHashMap();
   Setting infinite = this.registerSetting(new BooleanSetting("Infinite", "Renders breadcrumbs for all positions since toggle", true));
   Setting maxPosition = this.registerSetting(new NumberSetting("MaxPosition", "The maximum time for a given position", 1.0F, 2.0F, 100.0F));
   Setting fadeTime = this.registerSetting(new NumberSetting("Fade-Time", "Timer for the fade", 0, 1000, 5000));
   Setting width = this.registerSetting(new NumberSetting("Width", "The line width of the path", 1.0F, 1.0F, 5.0F));
   Setting self = this.registerSetting(new BooleanSetting("Self", "Renders breadcrumbs on player", true));
   Setting players = this.registerSetting(new BooleanSetting("Players", "Renders breadcrumbs on other players", false));
   Setting pearls = this.registerSetting(new BooleanSetting("Pearls", "Renders breadcrumbs on thrown pearls", false));
   Setting arrows = this.registerSetting(new BooleanSetting("Arrows", "Renders breadcrumbs on arrows", false));
   Setting xPBottles = this.registerSetting(new BooleanSetting("XPBottles", "Renders breadcrumbs on thrown experience bottles", false));

   public Breadcrumbs() {
      super("Breadcrumbs", "Renders a line connecting all previous positions", ModuleCategory.RENDER);
   }

   @Override
   public void awe() {
      this.map.clear();
   }

   @EventListener
   public void ata(TickEntitiesPreEvent tickEntitiesPreEvent) {
      Iterator iterator2 = client.world.getEntities().iterator();

      while (iterator2.hasNext()) {
         Entity entity2 = (Entity)iterator2.next();
         if (this.atd(entity2)) {
            Vec3d vec = EntityBoxUtils.BA(entity2, client.getRenderTickCounter().getTickDelta(true));
            Map temp = this.map;
            Integer integer = entity2.getId();
            if (temp.containsKey(integer)) {
               temp = this.map;
               Integer integer2 = entity2.getId();
               List list = (List)temp.get(integer2);
               PosTime posTime = new PosTime(vec, System.currentTimeMillis());
               list.add(posTime);
            } else {
               CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
               PosTime posTime2 = new PosTime(vec, System.currentTimeMillis());
               copyOnWriteArrayList.add(posTime2);
               temp = this.map;
               Integer integer3 = entity2.getId();
               temp.put(integer3, copyOnWriteArrayList);
            }
         }
      }

      if (!(Boolean)this.infinite.atx()) {
         iterator2 = this.map.entrySet().iterator();

         while (iterator2.hasNext()) {
            Entry entry = (Entry)iterator2.next();
            Iterator iterator3 = ((List)entry.getValue()).iterator();

            while (iterator3.hasNext()) {
               PosTime posTime3 = (PosTime)iterator3.next();
               if ((float)(System.currentTimeMillis() - posTime3.getTime()) > (Float)this.maxPosition.atx() * 1000.0F) {
                  Map map2 = this.map;
                  Object obj = entry.getKey();
                  ((List)map2.get(obj)).remove(posTime3);
               }
            }
         }
      }
   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent removeEntityEvent) {
      if ((Boolean)this.infinite.atx()) {
         Map temp = this.map;
         Integer integer = removeEntityEvent.getEntity().getId();
         temp.remove(integer);
      }
   }

   @EventListener
   public void atc(Render3DContext render3DContext) {
      Iterator iterator2 = this.map.entrySet().iterator();

      while (iterator2.hasNext()) {
         List list = (List)((Entry)iterator2.next()).getValue();
         int n = 0;
         if (0 < list.size()) {
            PosTime temp = (PosTime)list.get(0);

            while (++n < list.size()) {
               PosTime posTime = (PosTime)list.get(n);
               if (n > 1) {
                  int n2 = n - 1;
                  Vec3d vec = ((PosTime)list.get(n2)).getPos();
                  Vec3d vec2 = posTime.getPos();
                  if (!(Boolean)this.infinite.atx()) {
                     float f = 1.0F
                        - MathHelper.clamp((float)(System.currentTimeMillis() - posTime.getTime()) / ((Integer)this.fadeTime.atx()).intValue(), 0.0F, 1.0F);
                     render3DContext.boxRenderer
                        .YL(
                           vec.x,
                           vec.y,
                           vec.z,
                           vec2.x,
                           vec2.y,
                           vec2.z,
                           ColorHelper3.auO().auW((int)(f * 255.0F)),
                           ((Float)this.width.atx()).floatValue()
                        );
                  } else {
                     render3DContext.boxRenderer
                        .YL(vec.x, vec.y, vec.z, vec2.x, vec2.y, vec2.z, ColorHelper3.auO().auU(), ((Float)this.width.atx()).floatValue());
                  }
               }
            }
         }
      }
   }

   public boolean atd(Entity entity2) {
      return entity2 instanceof PlayerEntity
         ? (Boolean)this.players.atx() || entity2 == client.player && (Boolean)this.self.atx()
         : entity2 instanceof EnderPearlEntity && (Boolean)this.pearls.atx()
            || entity2 instanceof ArrowEntity && (Boolean)this.arrows.atx()
            || entity2 instanceof ExperienceBottleEntity && (Boolean)this.xPBottles.atx();
   }
}
