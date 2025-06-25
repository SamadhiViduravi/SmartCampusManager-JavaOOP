package com.campus.hostel;

/**
 * Enumeration for payment types
 */
public enum PaymentType {
    MONTHLY_RENT("Monthly Rent"),
    SECURITY_DEPOSIT("Security Deposit"),
    MAINTENANCE_FEE("Maintenance Fee"),
    LATE_FEE("Late Fee"),
    UTILITY_BILL("Utility Bill"),
    DAMAGE_CHARGE("Damage Charge"),
    CLEANING_FEE("Cleaning Fee"),
    OTHER("Other");
    
    private final String displayName;
    
    PaymentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}