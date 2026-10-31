package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ColorHelper3;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.render.BoxRenderer;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.world.EntityVec3dUtils;
import pw.hachimi.eventbus.annotation.EventListener;

public class PredictPath extends ClientModule {
   private static final HashMap a_ = new HashMap();
   private static final HashMap b_ = new HashMap();
   private static final HashMap c = new HashMap();
   Setting setting = this.registerSetting(new NumberSetting("v", "", 0, 4, 10));
   private static final long a = 0L;
   private static final String b = null;

   public PredictPath() {
      super("PredictPath", "", ModuleCategory.RENDER);
   }

   @Override
   public void awe() {
      a_.clear();
      b_.clear();
      c.clear();
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.POST) {
         Iterator iterator2 = client.world.getPlayers().iterator();

         while (iterator2.hasNext()) {
            PlayerEntity player2 = (PlayerEntity)iterator2.next();
            HashMap temp = c;
            ArrayList arrayList = new ArrayList();
            List list = (List)temp.getOrDefault(player2, arrayList);
            list.clear();
            c.put(player2, list);
            Vec3d vec = EntityVec3dUtils.nU(player2, (Integer)this.setting.atx(), list);
            Vec3d vec2 = player2.getPos().add(vec);
            Vec3d vec3 = (Vec3d)b_.getOrDefault(player2, vec2);
            temp = b_;
            Vec3d vec4 = player2.getPos().add(vec);
            temp.put(player2, vec4);
            a_.put(player2, vec3);
         }
      }
   }

   @EventListener
   public void adN(Render3DContext render3DContext) {
      Iterator iterator2 = client.world.getPlayers().iterator();

      while (iterator2.hasNext()) {
         PlayerEntity player2 = (PlayerEntity)iterator2.next();
         Vec3d vec = (Vec3d)b_.get(player2);
         Vec3d vec2 = (Vec3d)a_.get(player2);
         if (vec != null && vec2 != null) {
            float f2 = render3DContext.f;
            Vec3d vec3 = new Vec3d(
               player2.prevX + (player2.getX() - player2.prevX) * f2,
               player2.prevY + (player2.getY() - player2.prevY) * f2,
               player2.prevZ + (player2.getZ() - player2.prevZ) * f2
            );
            Vec3d vec4 = new Vec3d(
               vec2.getX() + (vec.getX() - vec2.getX()) * f2,
               vec2.getY() + (vec.getY() - vec2.getY()) * f2,
               vec2.getZ() + (vec.getZ() - vec2.getZ()) * f2
            );
            BoxRenderer temp = render3DContext.boxRenderer;
            double d = vec4.getX() - 0.1;
            temp = temp;
            double d2 = vec4.getY() + 0.2;
            double d3 = d;
            temp = temp;
            d = vec4.getZ() - 0.1;
            double d4 = d2;
            double d5 = d3;
            temp = temp;
            double d6 = vec4.getX() + 0.1;
            d3 = d;
            double d7 = d4;
            double d8 = d5;
            temp = temp;
            d = vec4.getY();
            double d9 = d6;
            d5 = d3;
            double d10 = d7;
            double d11 = d8;
            BoxRenderer boxRenderer2 = temp;
            double d12 = vec4.getZ() + 0.1;
            d3 = d;
            double d13 = d9;
            d8 = d5;
            double d14 = d10;
            double d15 = d11;
            temp = boxRenderer2;
            Color color = ColorHelper3.auO().auW(100);
            d = d12;
            d3 = d3;
            d5 = d13;
            d8 = d8;
            d11 = d14;
            d15 = d15;
            temp = temp;
            Color temp2 = color;
            Color temp3 = ColorHelper3.auO().auW(100);
            ShapeMode shapeMode = ShapeMode.Both;
            Color color2 = temp3;
            Color color3 = temp2;
            d = d;
            d3 = d3;
            d5 = d5;
            d8 = d8;
            d11 = d11;
            d15 = d15;
            temp.YY(d15, d11, d8, d5, d3, d, color3, color2, shapeMode, 0);
            Vec3d vec5 = new Vec3d(
               (player2.getX() - player2.prevX) * (f2 - 1.0F), (player2.getY() - player2.prevY) * (f2 - 1.0F), (player2.getZ() - player2.prevZ) * (f2 - 1.0F)
            );
            Vec3d vec6 = vec3;
            Iterator iterator3 = ((List)c.get(player2)).iterator();

            while (iterator3.hasNext()) {
               Vec3d vec7 = (Vec3d)iterator3.next();
               d = vec7.x;
               d3 = vec6.z;
               d5 = vec6.y;
               d8 = vec6.x;
               temp = render3DContext.boxRenderer;
               double d16 = d + vec5.getX();
               double temp4 = vec7.y + vec5.getY();
               double temp5 = vec7.z + vec5.getZ();
               color = Color.red;
               double d17 = temp5;
               double d18 = temp4;
               double d19 = d16;
               d11 = d3;
               d15 = d5;
               double d20 = d8;
               temp.YL(d20, d15, d11, d19, d18, d17, color, 2.0);
               vec6 = vec7.add(vec5);
            }
         }
      }
   }
}
