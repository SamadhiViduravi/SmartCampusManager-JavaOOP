package com.campus.reports;

/**
 * Enumeration for report types
 */
public enum ReportType {
    STUDENT_REPORT("Student Report"),
    COURSE_REPORT("Course Report"),
    FINANCIAL_REPORT("Financial Report"),
    ATTENDANCE_REPORT("Attendance Report"),
    PERFORMANCE_REPORT("Performance Report"),
    INVENTORY_REPORT("Inventory Report"),
    EVENT_REPORT("Event Report"),
    EXAM_REPORT("Exam Report"),
    TRANSPORT_REPORT("Transport Report"),
    HOSTEL_REPORT("Hostel Report"),
    LIBRARY_REPORT("Library Report"),
    SYSTEM_REPORT("System Report"),
    CUSTOM_REPORT("Custom Report");
    
    private final String displayName;
    
    ReportType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}