package pw.hachimi.client.model.config;

public record ValueKey(Object value, Comparable key) {
   public Comparable getKey() {
      return this.key;
   }
}
