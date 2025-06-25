package com.campus.events;

/**
 * Enumeration for event types
 */
public enum EventType {
    CONFERENCE("Conference"),
    WORKSHOP("Workshop"),
    SEMINAR("Seminar"),
    LECTURE("Lecture"),
    MEETING("Meeting"),
    CULTURAL("Cultural Event"),
    SPORTS("Sports Event"),
    COMPETITION("Competition"),
    ORIENTATION("Orientation"),
    GRADUATION("Graduation"),
    EXHIBITION("Exhibition"),
    FAIR("Fair"),
    FESTIVAL("Festival"),
    CEREMONY("Ceremony"),
    TRAINING("Training Session"),
    WEBINAR("Webinar"),
    HACKATHON("Hackathon"),
    SYMPOSIUM("Symposium"),
    PANEL_DISCUSSION("Panel Discussion"),
    NETWORKING("Networking Event");
    
    private final String displayName;
    
    EventType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}