package pw.hachimi.client.module.render;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.manager.RenderUtils;
import pw.hachimi.client.model.render.Easing;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.client.utils.render.WorldUtils;
import pw.hachimi.client.utils.world.PlayerBlockPosHelper;
import pw.hachimi.eventbus.annotation.EventListener;

public class BreakESP extends ClientModule {
   final Color color = new Color(255, 6, 6);
   final Color color2 = new Color(0, 255, 12);
   final Color color3 = new Color(255, 179, 96);
   Setting range = this.registerSetting(new NumberSetting("Range", "The range to render breaking blocks", 5.0F, 10.0F, 50.0F));
   Setting progress = this.registerSetting(new BooleanSetting("Progress", "Show mining progress", true));
   Setting name = this.registerSetting(new BooleanSetting("Name", "Show miner name", true));
   Setting shape = this.registerSetting(new EnumSetting("Shape", "How the shapes are rendered.", ShapeMode.Both, ShapeMode.values()));
   Setting side = this.registerSetting(new ColorSetting("Side", "Color of side", a(70)));
   Setting line = this.registerSetting(new ColorSetting("Line", "Color of line", a(100)));
   Setting easing = this.registerSetting(new EnumSetting("Easing", "Animation easing", Easing.CUBIC_IN_OUT, Easing.values()));
   Setting extraMine = this.registerSetting(new BooleanSetting("ExtraMine", "", true));
   Setting extraBox = this.registerSetting(new ColorSetting("ExtraBox", "", a(100)));
   Setting extraFill = this.registerSetting(new ColorSetting("ExtraFill", "", a(70)));
   public static boolean d;

   public BreakESP() {
      super("BreakESP", "Highlights blocks that are being broken", ModuleCategory.RENDER);
   }

   @EventListener
   public void Ro(Render3DContext render3DContext) {
      Iterator iterator2 = Managers.breakManager.concurrentHashMap.values().iterator();
      while (iterator2.hasNext()) {
         PlayerBlockPosHelper playerBlockPosHelper = (PlayerBlockPosHelper)iterator2.next();
         PlayerEntity player = playerBlockPosHelper.MT();
         if (player == null) {
            continue;
         }

         BlockPos pos = playerBlockPosHelper.pos;
         if (WorldUtils.Ny(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > ((Float)this.range.atx()).floatValue()) {
            continue;
         }

         double d = 0.5 * ((Easing)this.easing.atx()).Yy(1.0 - Math.min(playerBlockPosHelper.stopwatchImpl.afs() / playerBlockPosHelper.d, 1.0));
         Box box = new Box(pos).shrink(d, d, d).shrink(-d, -d, -d);
         render3DContext.boxRenderer.Zd(box, (Color)this.side.atx(), (Color)this.line.atx(), (ShapeMode)this.shape.atx());
         boolean flag = (Boolean)this.name.atx();
         boolean flag2 = (Boolean)this.progress.atx();
         if (flag) {
            RenderUtils.acv(render3DContext.matrices, player.getGameProfile().getName(), pos.toCenterPos().add(0.0, flag2 ? 0.15 : 0.0, 0.0), -1);
         }

         if (flag2) {
            String string;
            if (playerBlockPosHelper.e) {
               string = "§4Failed";
            } else if (playerBlockPosHelper.f) {
               string = "Broke";
            } else {
               string = bo.format(Math.min(1.0, playerBlockPosHelper.stopwatchImpl.afs() / playerBlockPosHelper.d) * 100.0);
            }

            Vec3d vec = pos.toCenterPos().add(0.0, flag ? -0.15 : 0.0, 0.0);
            int n;
            if (playerBlockPosHelper.f) {
               n = (client.world.isAir(pos) ? this.color2 : this.color).getRGB();
            } else {
               n = ColorUtils.iD(this.color, this.color2, (double)playerBlockPosHelper.stopwatchImpl.afs() / playerBlockPosHelper.d).getRGB();
            }

            RenderUtils.acv(render3DContext.matrices, string, vec, n);
         }
      }

      if (!(Boolean)this.extraMine.atx()) {
         return;
      }

      Iterator iterator3 = Managers.breakManager.concurrentHashMap2.values().iterator();
      while (iterator3.hasNext()) {
         PlayerBlockPosHelper playerBlockPosHelper2 = (PlayerBlockPosHelper)iterator3.next();
         PlayerEntity player2 = playerBlockPosHelper2.MT();
         if (player2 == null) {
            continue;
         }

         BlockPos pos2 = playerBlockPosHelper2.pos;
         if (WorldUtils.Ny(pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5) > ((Float)this.range.atx()).floatValue()) {
            continue;
         }

         PlayerBlockPosHelper playerBlockPosHelper3 = Managers.breakManager.Rt(pos2);
         if (playerBlockPosHelper3 != null && playerBlockPosHelper3.pos.equals(pos2)) {
            continue;
         }

         double d2 = 0.5 * ((Easing)this.easing.atx()).Yy(1.0 - Math.min(playerBlockPosHelper2.stopwatchImpl.afs() / playerBlockPosHelper2.d, 1.0));
         Box box2 = new Box(pos2).shrink(d2, d2, d2).shrink(-d2, -d2, -d2);
         render3DContext.boxRenderer.Zd(box2, (Color)this.extraFill.atx(), (Color)this.extraBox.atx(), (ShapeMode)this.shape.atx());
         boolean flag3 = (Boolean)this.name.atx();
         boolean flag4 = (Boolean)this.progress.atx();
         if (flag3) {
            RenderUtils.acv(render3DContext.matrices, player2.getGameProfile().getName(), pos2.toCenterPos().add(0.0, flag4 ? 0.15 : 0.0, 0.0), -1);
         }

         if (flag4) {
            RenderUtils.acv(render3DContext.matrices, "Double", pos2.toCenterPos().add(0.0, flag3 ? -0.15 : 0.0, 0.0), this.color3.getRGB());
         }
      }
   }

   private static Color a(int n) {
      return new Color(0, 0, 0, n);
   }
}
