package pw.hachimi.client;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;

public class Mc {
   public static Random random = ThreadLocalRandom.current();
   public static MinecraftClient client;
}
