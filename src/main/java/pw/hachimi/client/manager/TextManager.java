package pw.hachimi.client.manager;

import java.util.Map;
import net.minecraft.text.Text;
import pw.hachimi.client.event.OnInitFinishedEvent;
import pw.hachimi.eventbus.EventBusImpl;
import pw.hachimi.eventbus.annotation.EventListener;
import java.util.HashMap;

public class TextManager {
   public static final Map a_ = new HashMap();
   private static final long a = 0L;

   public TextManager() {
      EventBusImpl.INSTANCE.rD(this);
   }

   @EventListener
   public void onOnInitFinished(OnInitFinishedEvent onInitFinishedEvent) {
      a_.clear();
   }

   public static String aip(String str) {
      if (a_.containsKey(str)) {
         return (String)a_.get(str);
      }

      String str2 = Text.translatable(str).getString();
      a_.put(str, str2);
      return str2;
   }
}
