package com.campus.inventory;

/**
 * Enumeration for transaction types
 */
public enum TransactionType {
    STOCK_IN("Stock In"),
    STOCK_OUT("Stock Out"),
    PURCHASE("Purchase"),
    SALE("Sale"),
    TRANSFER("Transfer"),
    ADJUSTMENT("Adjustment"),
    RETURN("Return"),
    DAMAGE("Damage"),
    DISPOSAL("Disposal"),
    AUDIT("Audit");
    
    private final String displayName;
    
    TransactionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}