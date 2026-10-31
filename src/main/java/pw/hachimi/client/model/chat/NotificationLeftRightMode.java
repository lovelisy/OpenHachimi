package pw.hachimi.client.model.chat;

public enum NotificationLeftRightMode {
   Left,
   Right,
   Middle,
   Auto;

   private static final NotificationLeftRightMode[] e = da();

   private static NotificationLeftRightMode[] da() {
      return new NotificationLeftRightMode[]{Left, Right, Middle, Auto};
   }
}
