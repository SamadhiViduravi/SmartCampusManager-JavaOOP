package com.campus.reports;

/**
 * Enumeration for report status
 */
public enum ReportStatus {
    PENDING("Pending"),
    GENERATING("Generating"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    SCHEDULED("Scheduled"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    ReportStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}