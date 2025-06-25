package com.campus.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton NotificationService for system-wide notifications
 */
public class NotificationService {
    private static NotificationService instance;
    private List<NotificationObserver> observers;
    private List<Notification> notifications;
    
    private NotificationService() {
        this.observers = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }
    
    public static NotificationService getInstance() {
        if (instance == null) {
            synchronized (NotificationService.class) {
                if (instance == null) {
                    instance = new NotificationService();
                }
            }
        }
        return instance;
    }
    
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers(String message) {
        Notification notification = new Notification(message, LocalDateTime.now());
        notifications.add(notification);
        
        for (NotificationObserver observer : observers) {
            observer.update(notification);
        }
    }
    
    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }
    
    public void clearNotifications() {
        notifications.clear();
    }
    
    public static class Notification {
        private String message;
        private LocalDateTime timestamp;
        
        public Notification(String message, LocalDateTime timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }
        
        public String getMessage() { return message; }
        public LocalDateTime getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return "[" + timestamp + "] " + message;
        }
    }
}