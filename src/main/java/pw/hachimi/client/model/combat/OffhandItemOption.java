package pw.hachimi.client.model.combat;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum OffhandItemOption {
   TOTEM(Items.TOTEM_OF_UNDYING),
   GAPPLE(Items.ENCHANTED_GOLDEN_APPLE),
   CRYSTAL(Items.END_CRYSTAL),
   CHORUS(Items.CHORUS_FRUIT);

   private final Item e;
   private static final OffhandItemOption[] f = Xi();

   OffhandItemOption(Item item2) {
      this.e = item2;
   }

   public Item Xh() {
      return this.e;
   }

   private static OffhandItemOption[] Xi() {
      return new OffhandItemOption[]{TOTEM, GAPPLE, CRYSTAL, CHORUS};
   }
}
