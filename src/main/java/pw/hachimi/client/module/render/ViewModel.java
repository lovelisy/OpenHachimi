package pw.hachimi.client.module.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import pw.hachimi.client.event.render.RenderFirstPersonItemEvent;
import pw.hachimi.client.module.ClientModule;
import pw.hachimi.client.module.ModuleCategory;
import pw.hachimi.client.setting.NumberSetting;
import pw.hachimi.client.setting.Setting;
import pw.hachimi.eventbus.annotation.EventListener;

public class ViewModel extends ClientModule {
   Setting setting = this.registerSetting(new NumberSetting("X", "Translation in x-direction", -3.0F, 0.0F, 3.0F));
   Setting setting2 = this.registerSetting(new NumberSetting("Y", "Translation in y-direction", -3.0F, 0.0F, 3.0F));
   Setting setting3 = this.registerSetting(new NumberSetting("Z", "Translation in z-direction", -3.0F, 0.0F, 3.0F));
   Setting scaleX = this.registerSetting(new NumberSetting("ScaleX", "Scaling in x-direction", 0.1F, 1.0F, 2.0F));
   Setting scaleY = this.registerSetting(new NumberSetting("ScaleY", "Scaling in y-direction", 0.1F, 1.0F, 2.0F));
   Setting scaleZ = this.registerSetting(new NumberSetting("ScaleZ", "Scaling in z-direction", 0.1F, 1.0F, 2.0F));
   Setting rotateX = this.registerSetting(new NumberSetting("RotateX", "Rotation in x-direction", -180.0F, 0.0F, 180.0F));
   Setting rotateY = this.registerSetting(new NumberSetting("RotateY", "Rotation in y-direction", -180.0F, 0.0F, 180.0F));
   Setting rotateZ = this.registerSetting(new NumberSetting("RotateZ", "Rotation in z-direction", -180.0F, 0.0F, 180.0F));
   public static boolean n;

   public ViewModel() {
      super("ViewModel", "Changes the first-person viewmodel", ModuleCategory.RENDER);
   }

   @EventListener
   public void onRenderFirstPersonItem(RenderFirstPersonItemEvent renderFirstPersonItemEvent) {
      MatrixStack temp = renderFirstPersonItemEvent.matrices;
      float temp2 = (Float)this.scaleX.atx();
      float temp3 = (Float)this.scaleY.atx();
      float f = (Float)this.scaleZ.atx();
      float f2 = temp3;
      float f3 = temp2;
      temp.scale(f3, f2, f);
      temp = renderFirstPersonItemEvent.matrices;
      RotationAxis rotationAxis = RotationAxis.POSITIVE_X;
      float f4 = (Float)this.rotateX.atx();
      Quaternionf quaternionf = rotationAxis.rotationDegrees(f4);
      temp.multiply(quaternionf);
      if (renderFirstPersonItemEvent.hand == Hand.MAIN_HAND) {
         temp = renderFirstPersonItemEvent.matrices;
         temp2 = (Float)this.setting.atx();
         temp3 = (Float)this.setting2.atx();
         float f5 = (Float)this.setting3.atx();
         float f6 = temp3;
         float f7 = temp2;
         temp.translate(f7, f6, f5);
         temp = renderFirstPersonItemEvent.matrices;
         RotationAxis rotationAxis2 = RotationAxis.POSITIVE_Y;
         float f8 = (Float)this.rotateY.atx();
         Quaternionf quaternionf2 = rotationAxis2.rotationDegrees(f8);
         temp.multiply(quaternionf2);
         temp = renderFirstPersonItemEvent.matrices;
         RotationAxis rotationAxis3 = RotationAxis.POSITIVE_Z;
         float f9 = (Float)this.rotateZ.atx();
         Quaternionf quaternionf3 = rotationAxis3.rotationDegrees(f9);
         temp.multiply(quaternionf3);
      } else {
         temp = renderFirstPersonItemEvent.matrices;
         temp2 = -(Float)this.setting.atx();
         temp3 = (Float)this.setting2.atx();
         float f10 = (Float)this.setting3.atx();
         float f11 = temp3;
         float f12 = temp2;
         temp.translate(f12, f11, f10);
         temp = renderFirstPersonItemEvent.matrices;
         RotationAxis rotationAxis4 = RotationAxis.POSITIVE_Y;
         float f13 = -(Float)this.rotateY.atx();
         Quaternionf quaternionf4 = rotationAxis4.rotationDegrees(f13);
         temp.multiply(quaternionf4);
         temp = renderFirstPersonItemEvent.matrices;
         RotationAxis rotationAxis5 = RotationAxis.POSITIVE_Z;
         float f14 = -(Float)this.rotateZ.atx();
         Quaternionf quaternionf5 = rotationAxis5.rotationDegrees(f14);
         temp.multiply(quaternionf5);
      }
   }
}
