package com.alan.clients.util.notifications.windows;

import java.awt.TrayIcon.MessageType;
import lombok.Generated;

public enum NotificationType {
    NONE(MessageType.NONE),
    WARNING(MessageType.WARNING),
    INFO(MessageType.INFO),
    ERROR(MessageType.ERROR);

    private final MessageType aNm;
    private static final NotificationType[] $VALUES = uJ();

    @Generated
    NotificationType(MessageType messageType) {
        this.aNm = messageType;
    }

    @Generated
    public MessageType getMessageType() {
        return this.aNm;
    }

    private static NotificationType[] uJ() {
        return new NotificationType[]{NONE, WARNING, INFO, ERROR};
    }
}
