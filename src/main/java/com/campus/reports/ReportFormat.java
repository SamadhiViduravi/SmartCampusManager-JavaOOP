package com.campus.reports;

/**
 * Enumeration for report formats
 */
public enum ReportFormat {
    PDF("PDF"),
    EXCEL("Excel"),
    CSV("CSV"),
    HTML("HTML"),
    JSON("JSON"),
    XML("XML"),
    TXT("Text");
    
    private final String displayName;
    
    ReportFormat(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}