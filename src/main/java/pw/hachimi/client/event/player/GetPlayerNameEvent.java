package pw.hachimi.client.event.player;

import java.util.UUID;
import net.minecraft.text.Text;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class GetPlayerNameEvent extends Event {
   private final UUID uuid;
   private Text text;

   public GetPlayerNameEvent(Text text2, UUID uuid2) {
      this.text = text2;
      this.uuid = uuid2;
   }

   public Text getText() {
      return this.text;
   }

   public void setText(Text text2) {
      this.text = text2;
   }

   public UUID getUuid() {
      return this.uuid;
   }
}
