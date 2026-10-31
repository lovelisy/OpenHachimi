package pw.hachimi.client.handler;

import java.util.function.Supplier;
import net.minecraft.text.MutableText;

public interface MutableTextHandler {
   MutableText createNarrationMessage(Supplier var1);
}
