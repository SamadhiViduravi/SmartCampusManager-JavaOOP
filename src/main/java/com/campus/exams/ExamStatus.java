package com.campus.exams;

/**
 * Enumeration for exam status
 */
public enum ExamStatus {
    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    POSTPONED("Postponed"),
    RESULTS_PENDING("Results Pending"),
    RESULTS_PUBLISHED("Results Published");
    
    private final String displayName;
    
    ExamStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}