package com.campus.students;

/**
 * Enumeration for student status
 */
public enum StudentStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    GRADUATED("Graduated"),
    WITHDRAWN("Withdrawn"),
    TRANSFERRED("Transferred"),
    ON_LEAVE("On Leave"),
    PROBATION("Academic Probation");
    
    private final String displayName;
    
    StudentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    public boolean canEnroll() {
        return this == ACTIVE || this == PROBATION;
    }
    
    public boolean canGraduate() {
        return this == ACTIVE;
    }
}