package com.campus.events;

/**
 * Enumeration for event categories
 */
public enum EventCategory {
    ACADEMIC("Academic"),
    CULTURAL("Cultural"),
    SPORTS("Sports"),
    SOCIAL("Social"),
    PROFESSIONAL("Professional"),
    TECHNICAL("Technical"),
    ADMINISTRATIVE("Administrative"),
    RECREATIONAL("Recreational"),
    COMMUNITY("Community"),
    FUNDRAISING("Fundraising");
    
    private final String displayName;
    
    EventCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}