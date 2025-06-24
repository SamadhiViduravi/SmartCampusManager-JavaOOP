package com.campus.users;

/**
 * Enumeration defining different user roles in the campus management system
 */
public enum UserRole {
    ADMIN("Administrator", "Full system access"),
    STUDENT("Student", "Student portal access"),
    LECTURER("Lecturer", "Teaching and grading access"),
    LIBRARY_STAFF("Library Staff", "Library management access"),
    TRANSPORT_OFFICER("Transport Officer", "Transport management access"),
    HOSTEL_WARDEN("Hostel Warden", "Hostel management access"),
    INVENTORY_MANAGER("Inventory Manager", "Asset management access"),
    EXAM_OFFICER("Exam Officer", "Examination management access"),
    EVENT_COORDINATOR("Event Coordinator", "Event management access"),
    FINANCE_OFFICER("Finance Officer", "Financial management access");
    
    private final String displayName;
    private final String description;
    
    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    
    @Override
    public String toString() {
        return displayName;
    }
}