package com.campus.hostel;

/**
 * Enumeration for payment status
 */
public enum PaymentStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    OVERDUE("Overdue"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded"),
    PARTIAL("Partial"),
    FAILED("Failed");
    
    private final String displayName;
    
    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}