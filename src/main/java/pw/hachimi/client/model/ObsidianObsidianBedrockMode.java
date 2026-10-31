package pw.hachimi.client.model;

public enum ObsidianObsidianBedrockMode {
   OBSIDIAN,
   OBSIDIAN_BEDROCK,
   BEDROCK,
   VOID;

   private static final ObsidianObsidianBedrockMode[] e = ln();

   private static ObsidianObsidianBedrockMode[] ln() {
      return new ObsidianObsidianBedrockMode[]{OBSIDIAN, OBSIDIAN_BEDROCK, BEDROCK, VOID};
   }
}
