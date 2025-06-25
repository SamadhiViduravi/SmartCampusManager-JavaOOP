package com.campus.events;

/**
 * Enumeration for event status
 */
public enum EventStatus {
    PLANNED("Planned"),
    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    POSTPONED("Postponed"),
    REGISTRATION_OPEN("Registration Open"),
    REGISTRATION_CLOSED("Registration Closed"),
    FULL("Full");
    
    private final String displayName;
    
    EventStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}