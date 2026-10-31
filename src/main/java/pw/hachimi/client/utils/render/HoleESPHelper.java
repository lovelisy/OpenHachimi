package pw.hachimi.client.utils.render;

import pw.hachimi.client.model.ObsidianObsidianBedrockMode;

public class HoleESPHelper {
   public static final int[] a = new int[ObsidianObsidianBedrockMode.values().length];

   static {
      try {
         a[ObsidianObsidianBedrockMode.OBSIDIAN.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[ObsidianObsidianBedrockMode.OBSIDIAN_BEDROCK.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[ObsidianObsidianBedrockMode.BEDROCK.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {
      }

      try {
         a[ObsidianObsidianBedrockMode.VOID.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {
      }
   }
}
