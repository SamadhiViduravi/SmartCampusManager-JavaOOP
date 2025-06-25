package com.campus.utils;

/**
 * Observer interface for notifications
 */
public interface NotificationObserver {
    void update(NotificationService.Notification notification);
}