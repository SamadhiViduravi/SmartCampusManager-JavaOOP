package com.campus.transport;

/**
 * Enumeration for vehicle types
 */
public enum VehicleType {
    BUS("Bus"),
    VAN("Van"),
    CAR("Car"),
    TRUCK("Truck"),
    MOTORCYCLE("Motorcycle"),
    BICYCLE("Bicycle");
    
    private final String displayName;
    
    VehicleType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}