package pw.hachimi.client.model;

public enum WindowsLinuxMode {
   WINDOWS,
   LINUX,
   MAC_OS,
   LINUX_SOLARIS,
   OTHER;

   private static final WindowsLinuxMode[] f = fe();

   public boolean fb() {
      boolean flag;
      if (this != LINUX && this != LINUX_SOLARIS) {
         flag = false;
      } else {
         flag = true;
      }

      return flag;
   }

   public boolean fc() {
      boolean flag;
      if (this == MAC_OS) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   public boolean fd() {
      boolean flag;
      if (this == WINDOWS) {
         flag = true;
      } else {
         flag = false;
      }

      return flag;
   }

   private static WindowsLinuxMode[] fe() {
      return new WindowsLinuxMode[]{WINDOWS, LINUX, MAC_OS, LINUX_SOLARIS, OTHER};
   }
}
