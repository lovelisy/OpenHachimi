package pw.hachimi.client.mixin;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Main.class)
public final class MainMixin {
   @Redirect(
      method = "<clinit>",
      at = @At(value = "INVOKE", target = "Ljava/lang/System;setProperty(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"),
      require = 0
   )
   private static String hookStaticInit(String str, String str2) {
      return System.setProperty("java.awt.headless", "false");
   }
}
