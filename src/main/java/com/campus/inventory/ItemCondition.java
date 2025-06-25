package com.campus.inventory;

/**
 * Enumeration for item condition
 */
public enum ItemCondition {
    NEW("New"),
    EXCELLENT("Excellent"),
    GOOD("Good"),
    FAIR("Fair"),
    POOR("Poor"),
    DAMAGED("Damaged"),
    OBSOLETE("Obsolete"),
    UNDER_REPAIR("Under Repair");
    
    private final String displayName;
    
    ItemCondition(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}