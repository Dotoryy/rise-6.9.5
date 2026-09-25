package com.alan.clients.util.notifications.windows;

import com.alan.clients.util.notifications.windows.NotificationType;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public class NotificationUtil {
    public NotificationUtil() {
    }

    public static void a(NotificationType var0, String var1, String var2) {
        try {
            SystemTray systemtray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayicon = new TrayIcon(image, "Tray Demo");
            trayicon.setImageAutoSize(true);
            trayicon.setToolTip("System tray icon demo");
            systemtray.add(trayicon);
            trayicon.displayMessage(var1, var2, var0.getMessageType());
        } catch (AWTException awtexception) {
            awtexception.printStackTrace();
        }
    }
}
