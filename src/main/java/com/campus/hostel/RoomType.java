package com.campus.hostel;

/**
 * Enumeration for room types
 */
public enum RoomType {
    SINGLE("Single"),
    DOUBLE("Double"),
    TRIPLE("Triple"),
    QUAD("Quad"),
    SUITE("Suite"),
    STUDIO("Studio");
    
    private final String displayName;
    
    RoomType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}