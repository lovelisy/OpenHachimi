package pw.hachimi.client.module.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import pw.hachimi.client.event.render.ShouldRenderBlockOutlineEvent;
import pw.hachimi.client.manager.Managers;
import pw.hachimi.client.model.render.ShapeMode;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.BooleanSetting;
import pw.hachimi.client.setting.ColorSetting;
import pw.hachimi.client.setting.EnumSetting;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.client.utils.UnidentifiedHelper7;
import pw.hachimi.client.utils.render.ColorUtils;
import pw.hachimi.client.utils.render.Render3DContext;
import pw.hachimi.eventbus.annotation.EventListener;

public class BlockHighlight extends ClientModule {
   private final Map map = new HashMap();
   Setting shape = this.registerSetting(new EnumSetting("Shape", "Box rendering mode", ShapeMode.Lines, ShapeMode.values()));
   Setting debugEntities = this.registerSetting(new BooleanSetting("Debug-Entities", "Highlights entity bounding boxes for debug purposes", false));
   Setting width = this.registerSetting(new NumberSetting("Width", "The line width of the highlight", 1.0F, 1.0F, 5.0F));
   Setting side = this.registerSetting(new ColorSetting("Side", "The color of the sides", j(60)));
   Setting line = this.registerSetting(new ColorSetting("Line", "The color of the lines", j(255)));
   Setting fade = this.registerSetting(new BooleanSetting("Fade", "Fades the block highlight", false));
   Setting fadeTime = this.registerSetting(new NumberSetting("Fade-Time", "Timer for the fade", 0, 200, 1000));
   private double i;

   public BlockHighlight() {
      super("BlockHighlight", "Highlights the block the player is facing", ModuleCategory.RENDER);
   }

   @Override
   public String hy() {
      if (client.world == null) {
         return super.hy();
      }

      HitResult hitResult = client.crosshairTarget;
      if (hitResult instanceof BlockHitResult) {
         ClientWorld temp = client.world;
         BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
         if (temp.getBlockState(pos).isAir()) {
            return super.hy();
         }
      }

      DecimalFormat decimalFormat = f();
      double d = this.i;
      return decimalFormat.format(d);
   }

   @Override
   public void awe() {
      this.map.clear();
   }

   @EventListener
   public void bV(Render3DContext render3DContext) {
      if (client.world != null) {
         if (!(Boolean)this.fade.atx() && !this.map.isEmpty()) {
            this.map.clear();
         }

         label68: {
            HitResult hitResult = client.crosshairTarget;
            label49:
            if (hitResult != null) {
               Vec3d vec = Managers.vec3dPlayerHelper.BM();
               Box box;
               if ((Boolean)this.debugEntities.atx() && hitResult.getType() == Type.ENTITY) {
                  box = ((EntityHitResult)hitResult).getEntity().getBoundingBox();
                  Vec3d vec2 = vec;
                  Vec3d vec3 = hitResult.getPos();
                  this.i = vec2.distanceTo(vec3);
                  if ((Boolean)this.fade.atx()) {
                     Map map2 = this.map;
                     UnidentifiedHelper7 unidentifiedHelper7 = c(((Integer)this.fadeTime.atx()).intValue());
                     map2.put(box, unidentifiedHelper7);
                  }
               } else {
                  if (hitResult.getType() != Type.BLOCK) {
                     break label49;
                  }

                  BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
                  BlockState temp = client.world.getBlockState(pos);
                  ClientWorld world2 = client.world;
                  VoxelShape voxelShape = temp.getOutlineShape((BlockView)world2, pos);
                  if (voxelShape.isEmpty()) {
                     return;
                  }

                  Box box2 = voxelShape.getBoundingBox();
                  box = new Box(
                     pos.getX() + box2.minX,
                     pos.getY() + box2.minY,
                     pos.getZ() + box2.minZ,
                     pos.getX() + box2.maxX,
                     pos.getY() + box2.maxY,
                     pos.getZ() + box2.maxZ
                  );
                  Vec3d temp2 = vec;
                  Vec3d vec4 = hitResult.getPos();
                  this.i = temp2.distanceTo(vec4);
                  if ((Boolean)this.fade.atx()) {
                     Map map3 = this.map;
                     UnidentifiedHelper7 unidentifiedHelper72 = c(((Integer)this.fadeTime.atx()).intValue());
                     map3.put(box, unidentifiedHelper72);
                  }
               }

               if (!(Boolean)this.fade.atx()) {
                  if (box != null) {
                     render3DContext.boxRenderer.Zd(box, (Color)this.side.atx(), (Color)this.line.atx(), (ShapeMode)this.shape.atx());
                  }

                  return;
               }
               break label68;
            }

            if (!(Boolean)this.fade.atx()) {
               return;
            }
         }

         Iterator iterator2 = this.map.entrySet().iterator();

         while (iterator2.hasNext()) {
            Entry entry = (Entry)iterator2.next();
            Box box3 = (Box)entry.getKey();
            ((UnidentifiedHelper7)entry.getValue()).qV(false);
            if (!(((UnidentifiedHelper7)entry.getValue()).qW() < 0.01F)) {
               int n = (int)(((Color)this.side.atx()).getAlpha() * ((UnidentifiedHelper7)entry.getValue()).qW());
               int n2 = (int)(((Color)this.line.atx()).getAlpha() * ((UnidentifiedHelper7)entry.getValue()).qW());
               Color color = ColorUtils.iE((Color)this.side.atx(), n);
               Color color2 = ColorUtils.iE((Color)this.line.atx(), n2);
               render3DContext.boxRenderer.Zd(box3, color, color2, (ShapeMode)this.shape.atx());
            }
         }
      }
   }

   @EventListener
   public void onShouldRenderBlockOutline(ShouldRenderBlockOutlineEvent shouldRenderBlockOutlineEvent) {
      shouldRenderBlockOutlineEvent.aim();
   }

   private static DecimalFormat f() {
      return new DecimalFormat("0.0");
   }

   private static UnidentifiedHelper7 c(float f2) {
      return new UnidentifiedHelper7(true, f2);
   }

   private static Color j(int n) {
      return new Color(255, 0, 0, n);
   }
}
