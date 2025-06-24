package com.campus.courses;

/**
 * Enumeration for course status
 */
public enum CourseStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    DRAFT("Draft");
    
    private final String displayName;
    
    CourseStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}