package pw.hachimi.client.utils;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;
import pw.hachimi.client.model.config.ValueKey;

public class ModulesUtils {
   public static boolean a;

   public static Stream NP(Stream stream2, Function function2) {
      final Function function3 = function2;
      Function function4 = new Function() {
         @Override
         public Object apply(Object function2) {
            return ModulesUtils.NQ(function3, function2);
         }
      };
      Stream temp = stream2.map(function4);
      function4 = new Function() {
         @Override
         public Object apply(Object function2) {
            return ((ValueKey)function2).getKey();
         }
      };
      temp = temp;
      Comparator comparator = Comparator.comparing(function4);
      temp = temp.sorted(comparator);
      function4 = new Function() {
         @Override
         public Object apply(Object function2) {
            return ((ValueKey)function2).value();
         }
      };
      return temp.map(function4);
   }

   static ValueKey NQ(Function function2, Object obj) {
      Comparable comparable = (Comparable)function2.apply(obj);
      return new ValueKey(obj, comparable);
   }
}
