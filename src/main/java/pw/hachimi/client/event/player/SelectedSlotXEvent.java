package pw.hachimi.client.event.player;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import pw.hachimi.client.api.UnidentifiedInterface2;
import pw.hachimi.client.event.Event;

@UnidentifiedInterface2
public class SelectedSlotXEvent extends Event {
   private final DrawContext context;
   private final Identifier id;
   private final int c;
   private final int d;
   private final int e;
   private final int f;

   public SelectedSlotXEvent(DrawContext context2, Identifier id2, int n, int n2, int n3, int n4) {
      this.context = context2;
      this.id = id2;
      this.c = n;
      this.d = n2;
      this.e = n3;
      this.f = n4;
   }

   public DrawContext getContext() {
      return this.context;
   }

   public Identifier nx() {
      return this.id;
   }

   public int ny() {
      return this.f;
   }

   public int nz() {
      return this.e;
   }

   public int nA() {
      return this.d;
   }

   public int nB() {
      return this.c;
   }
}
