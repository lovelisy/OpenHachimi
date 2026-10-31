package pw.hachimi.client.utils.chat;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.StringVisitable.StyledVisitor;
import net.minecraft.util.Formatting;
import pw.hachimi.client.module.ColorHelper;
import pw.hachimi.client.module.ColorHelper3;

public class StyleFormattingUtils {
   private static final Map a_ = Stream.of(Formatting.values()).filter(Formatting::isColor).collect(ImmutableMap.toImmutableMap(Formatting::getColorValue, Function.identity()));
   private static final long a = 0L;

   public static String Eq(String str) {
      if (str == null) {
         return null;
      }

      char[] cs = str.toCharArray();
      char[] cs2 = new char[((char[])cs).length];
      int n2 = 0;

      for (int n3 = 0; n3 < ((char[])cs).length; n3++) {
         if (cs[n3] == 167) {
            n3++;
         } else {
            cs2[n2++] = cs[n3];
         }
      }

      return a(cs2, n2);
   }

   public static String Er(Text text2) {
      StringBuilder stringBuilder = new StringBuilder();
      final StringBuilder stringBuilder2 = stringBuilder;
      StyledVisitor temp = new StyledVisitor() {
         public Optional accept(Style style, String str) {
            return StyleFormattingUtils.Eu(stringBuilder2, style, str);
         }
      };
      Style style2 = Style.EMPTY;
      StyledVisitor styledVisitor = temp;
      text2.visit(styledVisitor, style2);
      return stringBuilder.toString();
   }

   public static String Es(OrderedText orderedText) {
      StringBuilder stringBuilder = new StringBuilder();
      final StringBuilder stringBuilder2 = stringBuilder;
      CharacterVisitor characterVisitor = new CharacterVisitor() {
         public boolean accept(int n2, Style style, int n3) {
            return StyleFormattingUtils.Et(stringBuilder2, n2, style, n3);
         }
      };
      orderedText.accept(characterVisitor);
      return stringBuilder.toString();
   }

   static boolean Et(StringBuilder stringBuilder, int n2, Style style, int n3) {
      if (style.getColor() != null) {
         int n4 = style.getColor().getRgb();
         if (n4 == (ColorHelper3.auO().auX() & 16777215)) {
            stringBuilder.append('§').append("s");
         } else if (n4 == (ColorHelper.wx().wC() & 16777215)) {
            stringBuilder.append('§').append("g");
         } else {
            Map temp = a_;
            Integer integer = n4;
            Formatting formatting = (Formatting)temp.get(integer);
            if (formatting != null) {
               StringBuilder stringBuilder2 = stringBuilder.append('§');
               char c = formatting.getCode();
               stringBuilder2.append(c);
            }
         }
      } else if (style.isObfuscated()) {
         stringBuilder.append('§').append("k");
      } else if (style.isBold()) {
         stringBuilder.append('§').append("l");
      } else if (style.isStrikethrough()) {
         stringBuilder.append('§').append("m");
      } else if (style.isUnderlined()) {
         stringBuilder.append('§').append("n");
      } else if (style.isItalic()) {
         stringBuilder.append('§').append("o");
      } else {
         stringBuilder.append('§').append("r");
      }

      stringBuilder.appendCodePoint(n3);
      return true;
   }

   static Optional Eu(StringBuilder stringBuilder, Style style, String str) {
      if (!str.isEmpty()) {
         if (style.getColor() != null) {
            int n2 = style.getColor().getRgb();
            if (n2 == (ColorHelper3.auO().auX() & 16777215)) {
               stringBuilder.append('§').append("s");
            } else if (n2 == (ColorHelper.wx().wC() & 16777215)) {
               stringBuilder.append('§').append("g");
            } else {
               Map temp = a_;
               Integer integer = n2;
               Formatting formatting = (Formatting)temp.get(integer);
               if (formatting != null) {
                  StringBuilder stringBuilder2 = stringBuilder.append('§');
                  char c = formatting.getCode();
                  stringBuilder2.append(c);
               }
            }
         } else if (style.isObfuscated()) {
            stringBuilder.append('§').append("k");
         } else if (style.isBold()) {
            stringBuilder.append('§').append("l");
         } else if (style.isStrikethrough()) {
            stringBuilder.append('§').append("m");
         } else if (style.isUnderlined()) {
            stringBuilder.append('§').append("n");
         } else if (style.isItalic()) {
            stringBuilder.append('§').append("o");
         } else {
            stringBuilder.append('§').append("r");
         }

         stringBuilder.append(str);
      }

      return Optional.empty();
   }

   private static String a(char[] cs, int n2) {
      return new String(cs, 0, n2);
   }
}
