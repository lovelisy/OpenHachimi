package pw.hachimi.client.mixin;

import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.Style;
import net.minecraft.text.TextVisitFactory;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pw.hachimi.client.Mc;
import pw.hachimi.client.event.network.VisitFormattedEvent;
import pw.hachimi.client.event.network.VisitFormatted_1Event;
import pw.hachimi.client.event.network.VisitFormatted_1Event2;
import pw.hachimi.eventbus.EventBusImpl;

@Mixin(TextVisitFactory.class)
public abstract class TextVisitFactoryMixin {
   @Shadow
   private static boolean visitRegularCharacter(Style style, CharacterVisitor characterVisitor, int n, char c) {
      return false;
   }

   @ModifyArg(
      method = "visitFormatted(Ljava/lang/String;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
         ordinal = 0
      ),
      index = 0
   )
   private static String hookVisitFormatted(String str) {
      if (str == null) {
         return "";
      }

      if (Mc.client.player == null) {
         return str;
      }

      VisitFormattedEvent visitFormattedEvent = new VisitFormattedEvent(str);
      EventBusImpl.INSTANCE.rC(visitFormattedEvent);
      return visitFormattedEvent.aik() ? visitFormattedEvent.getText() : str;
   }

   @Inject(method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", at = @At("HEAD"), cancellable = true)
   private static void hookVisitFormatted$1(String str, int n, Style style, Style style2, CharacterVisitor characterVisitor, CallbackInfoReturnable callbackInfoReturnable) {
      callbackInfoReturnable.cancel();
      int n2 = str.length();
      Style style3 = style;

      for (int n3 = n; n3 < n2; n3++) {
         char c = str.charAt(n3);
         if (c == 167) {
            if (n3 + 1 >= n2) {
               break;
            }

            char c2 = str.charAt(n3 + 1);
            if (c2 == 's') {
               VisitFormatted_1Event2 visitFormatted1Event2 = new VisitFormatted_1Event2();
               EventBusImpl.INSTANCE.rC(visitFormatted1Event2);
               style3 = style3.withColor(visitFormatted1Event2.HE());
            } else if (c2 == 'g') {
               VisitFormatted_1Event visitFormatted1Event = new VisitFormatted_1Event();
               EventBusImpl.INSTANCE.rC(visitFormatted1Event);
               style3 = style3.withColor(visitFormatted1Event.HE());
            } else {
               Formatting formatting = Formatting.byCode(c2);
               if (formatting != null) {
                  style3 = formatting == Formatting.RESET ? style2 : style3.withExclusiveFormatting(formatting);
               }
            }

            n3++;
         } else if (Character.isHighSurrogate(c)) {
            if (n3 + 1 >= n2) {
               if (!characterVisitor.accept(n3, style3, 65533)) {
                  callbackInfoReturnable.setReturnValue(false);
                  return;
               }
               break;
            }

            char c3 = str.charAt(n3 + 1);
            if (Character.isLowSurrogate(c3)) {
               if (!characterVisitor.accept(n3, style3, Character.toCodePoint(c, c3))) {
                  callbackInfoReturnable.setReturnValue(false);
                  return;
               }

               n3++;
            } else if (!characterVisitor.accept(n3, style3, 65533)) {
               callbackInfoReturnable.setReturnValue(false);
               return;
            }
         } else if (!visitRegularCharacter(style3, characterVisitor, n3, c)) {
            callbackInfoReturnable.setReturnValue(false);
            return;
         }
      }

      callbackInfoReturnable.setReturnValue(true);
   }
}
