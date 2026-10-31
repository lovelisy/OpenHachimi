package pw.hachimi.client.event;

import net.minecraft.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import pw.hachimi.client.api.UnidentifiedInterface2;

@UnidentifiedInterface2
public class UnknownEvent10 extends Event {
   private final String text;

   public UnknownEvent10(String str) {
      this.text = str;
   }

   public String auZ() {
      return this.ava(this.text);
   }

   private String ava(String str) {
      return StringHelper.truncateChat(StringUtils.normalizeSpace(str.trim()));
   }
}
