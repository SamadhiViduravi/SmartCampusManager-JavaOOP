package com.campus.inventory;

/**
 * Enumeration for item categories
 */
public enum ItemCategory {
    ELECTRONICS("Electronics"),
    FURNITURE("Furniture"),
    STATIONERY("Stationery"),
    BOOKS("Books"),
    LABORATORY_EQUIPMENT("Laboratory Equipment"),
    SPORTS_EQUIPMENT("Sports Equipment"),
    MEDICAL_SUPPLIES("Medical Supplies"),
    CLEANING_SUPPLIES("Cleaning Supplies"),
    FOOD_BEVERAGES("Food & Beverages"),
    MAINTENANCE_TOOLS("Maintenance Tools"),
    OFFICE_SUPPLIES("Office Supplies"),
    COMPUTER_HARDWARE("Computer Hardware"),
    SOFTWARE("Software"),
    VEHICLES("Vehicles"),
    MISCELLANEOUS("Miscellaneous");
    
    private final String displayName;
    
    ItemCategory(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}