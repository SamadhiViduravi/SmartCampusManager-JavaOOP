package com.campus.reports;

/**
 * Enumeration for report categories
 */
public enum ReportCategory {
    ACADEMIC("Academic"),
    ADMINISTRATIVE("Administrative"),
    FINANCIAL("Financial"),
    OPERATIONAL("Operational"),
    STATISTICAL("Statistical"),
    COMPLIANCE("Compliance"),
    PERFORMANCE("Performance"),
    ANALYTICS("Analytics");
    
    private final String displayName;
    
    ReportCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}