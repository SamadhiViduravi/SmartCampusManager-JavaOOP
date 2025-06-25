package com.campus.library;

/**
 * Enumeration for book status
 */
public enum BookStatus {
    AVAILABLE("Available"),
    ISSUED("Issued"),
    RESERVED("Reserved"),
    DAMAGED("Damaged"),
    LOST("Lost"),
    UNDER_REPAIR("Under Repair"),
    WITHDRAWN("Withdrawn");
    
    private final String displayName;
    
    BookStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}