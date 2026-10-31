package pw.hachimi.client.module;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.client.gui.DrawContext;
import pw.hachimi.client.event.EventPhase;
import pw.hachimi.client.event.TickPreEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.LeftRightMode;
import pw.hachimi.client.model.ModulesOption;
import pw.hachimi.client.model.ModulesTopBottomMode;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.LeftMiddleMode;
import pw.hachimi.client.module.client.HUD;
import pw.hachimi.client.module.hud.RenderModule;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.ModulesUtils;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.UnidentifiedHelper9;
import pw.hachimi.eventbus.annotation.EventListener;

public class Modules extends RenderModule {
   Setting optionMode = this.registerSetting(new EnumSetting("Ordering", "The ordering of the arraylist", ModulesOption.LENGTH, ModulesOption.values()));
   Setting booleanSetting1_1 = this.registerSetting(new BooleanSetting("BoundOnly", "Hide unbind modules", false));
   Setting easingMode = this.registerSetting(new EnumSetting("Easing", "", Easing.QUART_OUT, Easing.values()));
   Setting leftRightMode = this.registerSetting(new EnumSetting("X-Location", "", LeftRightMode.Auto, LeftRightMode.values()));
   Setting topBottomMode = this.registerSetting(new EnumSetting("Y-Location", "", ModulesTopBottomMode.Auto, ModulesTopBottomMode.values()));
   private List list;
   private float g;
   private float h;
   private boolean i;
   private boolean j;

   public Modules() {
      super("Modules", "");
      this.awb();
      this.numberSetting1.aty(2.0F);
      this.numberSetting2.aty(1.0F);
      this.leftMiddleMode.aty(LeftMiddleMode.RIGHT);
   }

   @EventListener
   public void onTickPre(TickPreEvent tickPreEvent) {
      if (tickPreEvent.getEventPhase() == EventPhase.PRE) {
         Stream temp = Managers.moduleManager.XM().stream();
         final Class<ClientModule> classValue = ClientModule.class;
         Predicate predicate = new Predicate() {
            @Override
            public boolean test(Object tickPreEvent) {
               return classValue.isInstance(tickPreEvent);
            }
         };
         temp = temp.filter(predicate);
         final Class<ClientModule> classValue2 = ClientModule.class;
         Function function2 = new Function() {
            @Override
            public Object apply(Object tickPreEvent) {
               return classValue2.cast(tickPreEvent);
            }
         };
         temp = temp.map(function2);
         Predicate predicate2 = new Predicate() {
            @Override
            public boolean test(Object tickPreEvent) {
               return Modules.Sk((ClientModule)tickPreEvent);
            }
         };
         List list2 = temp.filter(predicate2).toList();
         Iterator iterator2 = list2.iterator();

         while (iterator2.hasNext()) {
            ClientModule clientModule = (ClientModule)iterator2.next();
            if (clientModule.awg() && (!(Boolean)this.booleanSetting1_1.atx() || clientModule.awi().agt() != -1)) {
               clientModule.text2_2 = this.Sg(clientModule);
            }
         }
         this.list = switch ((ModulesOption)this.optionMode.atx()) {
            case LENGTH -> ModulesUtils.NP(list2.stream(), new Function() {
               @Override
               public Object apply(Object tickPreEvent) {
                  return Modules.Si((ClientModule)tickPreEvent);
               }
            }).toList();
            case ALPHABETICAL -> ModulesUtils.NP(list2.stream(), new Function() {
               @Override
               public Object apply(Object tickPreEvent) {
                  return Modules.Sj((ClientModule)tickPreEvent);
               }
            }).toList();
         };
         LeftRightMode leftRightMode2 = (LeftRightMode)this.leftRightMode.atx();
         this.i = leftRightMode2 == LeftRightMode.Right
            || leftRightMode2 == LeftRightMode.Auto
               && (
                  this.ads() == LeftMiddleMode.RIGHT
                     || this.ads() == LeftMiddleMode.RIGHT_OFFSET
                     || this.adi().isEmpty() && this.add() > client.getWindow().getScaledWidth() / 2.0
               );
         ModulesTopBottomMode modulesTopBottomMode = (ModulesTopBottomMode)this.topBottomMode.atx();
         this.j = modulesTopBottomMode == ModulesTopBottomMode.Bottom
            || modulesTopBottomMode == ModulesTopBottomMode.Auto && this.adi().isEmpty() && this.ade() > client.getWindow().getScaledHeight() / 2.0;
      }
   }

   @Override
   public void adc(DrawContext context) {
      if (this.list != null) {
         Easing easing = (Easing)this.easingMode.atx();
         double d = this.add();
         double d2 = this.ade();
         if (this.j) {
            d2 += this.h - RenderUtils.acG();
         }

         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            ClientModule clientModule = (ClientModule)iterator2.next();
            double d3;
            String str;
            float f2;
            UnidentifiedHelper9 temp;
            float temp2;
            byte temp3;
            if (clientModule.awg() && (!(Boolean)this.booleanSetting1_1.atx() || clientModule.awi().agt() != -1)) {
               d3 = clientModule.unidentifiedHelper92.MH(1.0, 300L, easing);
               if (d3 <= 0.0) {
                  continue;
               }

               str = clientModule.text2_2;
               f2 = RenderUtils.acF(str);
               temp = clientModule.unidentifiedHelper9;
               temp2 = f2;
               temp3 = 1;
            } else {
               d3 = clientModule.unidentifiedHelper92.MH(0.0, 300L, easing);
               if (d3 <= 0.0) {
                  continue;
               }

               str = clientModule.text2_2;
               f2 = RenderUtils.acF(str);
               temp = clientModule.unidentifiedHelper9;
               temp2 = f2;
               temp3 = 0;
            }

            double d4 = temp.MH(temp2 * temp3, 300L, easing);
            int n = ColorUtils.iF(HUD.zg().zo(d2), (float)Math.clamp(d3, 0.0, 1.0));
            float f3;
            if (this.i) {
               f3 = (float)(d + this.g - d4);
            } else {
               f3 = (float)(d - f2 + d4);
            }

            this.adr(context, str, f3, (float)d2 + 1.0F, n);
            d2 += d3 * RenderUtils.acG() * (this.j ? -1 : 1);
         }
      }
   }

   @Override
   public void adb() {
      if (this.list != null) {
         Easing easing = (Easing)this.easingMode.atx();
         float f2 = 0.0F;
         float f3 = 0.0F;
         Iterator iterator2 = this.list.iterator();

         while (iterator2.hasNext()) {
            ClientModule clientModule = (ClientModule)iterator2.next();
            UnidentifiedHelper9 temp;
            double temp2;
            if (!clientModule.awg() || (Boolean)this.booleanSetting1_1.atx() && clientModule.awi().agt() == -1) {
               temp = clientModule.unidentifiedHelper92;
               temp2 = 0.0;
            } else {
               temp = clientModule.unidentifiedHelper92;
               temp2 = 1.0;
            }

            double d = temp.MH(temp2, 300L, easing);
            if (!(d <= 0.0)) {
               f3 += (float)d * RenderUtils.acG();
               float f4 = RenderUtils.acF(clientModule.text2_2);
               f2 = Math.max(f2, f4);
            }
         }

         this.g = f2;
         this.h = f3;
      }
   }

   @Override
   public void awe() {
      this.list = null;
   }

   private String Sg(ModuleBase moduleBase) {
      String str = this.Sh(moduleBase.hy());
      if (str.isEmpty()) {
         return moduleBase.OO();
      } else {
         return this.i ? moduleBase.OO() + " " + str : str + " §r" + moduleBase.OO();
      }
   }

   private String Sh(String str) {
      return !str.isEmpty() ? "§7[§f" + str + "§7]" : "";
   }

   @Override
   public float adl() {
      return this.g + super.adl();
   }

   @Override
   public float adm() {
      return this.h + super.adm();
   }

   static Integer Si(ClientModule clientModule) {
      return -RenderUtils.acF(clientModule.text2_2);
   }

   static String Sj(ClientModule clientModule) {
      return clientModule.text2_2;
   }

   static boolean Sk(ClientModule clientModule) {
      return !clientModule.WS();
   }
}
