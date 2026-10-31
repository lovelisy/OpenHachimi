package pw.hachimi.client.model.player;

import net.minecraft.item.Item;

public record OffsetItem(float id, float offset, Item item, String key) {
   public Item getItem() {
      return this.item;
   }

   public String getKey() {
      return this.key;
   }
}
