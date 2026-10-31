package pw.hachimi.client.model.combat;

public enum AntiCrawlCrawlingMiningMode {
   Crawling,
   Mining,
   Always,
   Smart;

   private static final AntiCrawlCrawlingMiningMode[] e = cS();

   private static AntiCrawlCrawlingMiningMode[] cS() {
      return new AntiCrawlCrawlingMiningMode[]{Crawling, Mining, Always, Smart};
   }
}
