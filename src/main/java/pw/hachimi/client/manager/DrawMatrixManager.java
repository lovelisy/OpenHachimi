package pw.hachimi.client.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.render.RenderEvent;
import pw.hachimi.client.gui.DrawBufferScreen;
import pw.hachimi.client.module.client.HUD;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.utils.render.DrawTextLayerTypeHelper;
import pw.hachimi.client.utils.render.FramebufferWindowUtils;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;

public final class DrawMatrixManager {
   private final Map map;
   public static DrawTextLayerTypeHelper drawTextLayerTypeHelper;
   public static boolean a;

   public DrawMatrixManager() {
      LinkedHashMap linkedHashMap = new LinkedHashMap();
      this.map = Collections.synchronizedMap(linkedHashMap);
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onRender(RenderEvent renderEvent) {
      if (!(Mc.client.currentScreen instanceof DrawBufferScreen) && HUD.zg().awg()) {
         this.Hw(renderEvent.getContext(), renderEvent.gA(), false);
      }
   }

   public void Hw(DrawContext context, float f, boolean flag) {
      if (drawTextLayerTypeHelper == null) {
         drawTextLayerTypeHelper = a();
      }

      Iterator iterator2 = this.map.values().iterator();

      while (iterator2.hasNext()) {
         RenderModule renderModule = (RenderModule)iterator2.next();
         if (renderModule.awg()) {
            renderModule.adb();
            if (renderModule.adk() && renderModule.adj()) {
               while (iterator2.hasNext()) {
                  renderModule = (RenderModule)iterator2.next();
                  if (renderModule.awg()) {
                     renderModule.adb();
                     if (renderModule.adk() && renderModule.adj()) {
                     }
                  }
               }

               drawTextLayerTypeHelper.wX();
               final DrawContext context2 = context;
               final DrawMatrixManager drawMatrixManager = this;
               Runnable temp = new Runnable() {
                  @Override
                  public void run() {
                     drawMatrixManager.HA(context2);
                  }
               };
               float f2 = HUD.zg().zh();
               FramebufferWindowUtils.wF(temp, f2, f);
               drawTextLayerTypeHelper.wY(context);
               break;
            }
         }
      }

      drawTextLayerTypeHelper.wX();
      iterator2 = this.map.values().iterator();

      while (iterator2.hasNext()) {
         RenderModule renderModule2 = (RenderModule)iterator2.next();
         if (renderModule2.awg()) {
            if (renderModule2.adk()) {
               if (!renderModule2.adj()) {
                  renderModule2.adn(context);
               }

               context.getMatrices().push();
               context.getMatrices().translate(2.0F, 2.0F, 0.0F);
               renderModule2.adc(context);
               context.getMatrices().pop();
            } else {
               renderModule2.adc(context);
            }

            if (flag) {
               renderModule2.acY(context);
            }
         }
      }

      drawTextLayerTypeHelper.wY(context);
   }

   public void Hx(RenderModule renderModule) {
      Map temp = this.map;
      String str = renderModule.Kn();
      temp.put(str, renderModule);
   }

   public RenderModule Hy(String str) {
      return (RenderModule)this.map.get(str);
   }

   public List Hz() {
      return new ArrayList(this.map.values());
   }

   void HA(DrawContext context) {
      Iterator iterator2 = this.map.values().iterator();

      while (iterator2.hasNext()) {
         RenderModule renderModule = (RenderModule)iterator2.next();
         if (renderModule.awg() && renderModule.adk() && renderModule.adj()) {
            renderModule.adn(context);
         }
      }
   }

   private static DrawTextLayerTypeHelper a() {
      return new DrawTextLayerTypeHelper(false);
   }
}
