package com.campus.hostel;

/**
 * Enumeration for room status
 */
public enum RoomStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    UNDER_MAINTENANCE("Under Maintenance"),
    OUT_OF_ORDER("Out of Order"),
    RESERVED("Reserved"),
    CLEANING("Cleaning");
    
    private final String displayName;
    
    RoomStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}