package pw.hachimi.client.event.network;

import net.minecraft.text.Text;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class AddMessageEvent extends Event {
   private Text text;

   public AddMessageEvent(Text text2) {
      this.text = text2;
   }

   public Text getText() {
      return this.text;
   }

   public void setText(Text text2) {
      this.text = text2;
   }
}
