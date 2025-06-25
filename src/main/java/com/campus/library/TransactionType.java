package com.campus.library;

/**
 * Enumeration for transaction types
 */
public enum TransactionType {
    ISSUE("Issue"),
    RETURN("Return"),
    RENEW("Renew"),
    RESERVE("Reserve"),
    CANCEL_RESERVATION("Cancel Reservation");
    
    private final String displayName;
    
    TransactionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}