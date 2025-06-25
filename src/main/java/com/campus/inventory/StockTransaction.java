package com.campus.inventory;

import com.campus.utils.Identifiable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * StockTransaction class for tracking inventory movements
 */
public class StockTransaction implements Identifiable {
    private String transactionId;
    private String itemId;
    private TransactionType transactionType;
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private String reason;
    private String performedBy;
    private String referenceNumber;
    private String supplier;
    private String recipient;
    private LocalDateTime transactionDate;
    private String notes;
    private boolean isApproved;
    private String approvedBy;
    private LocalDateTime approvalDate;
    
    public StockTransaction(String transactionId, String itemId, TransactionType transactionType, 
                           int quantity, double unitPrice, String performedBy) {
        this.transactionId = transactionId;
        this.itemId = itemId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = quantity * unitPrice;
        this.performedBy = performedBy;
        this.transactionDate = LocalDateTime.now();
        this.isApproved = false;
        
        generateReferenceNumber();
    }
    
    private void generateReferenceNumber() {
        String prefix = transactionType.name().substring(0, 2);
        this.referenceNumber = prefix + transactionId + System.currentTimeMillis() % 10000;
    }
    
    public void approve(String approvedBy) {
        this.isApproved = true;
        this.approvedBy = approvedBy;
        this.approvalDate = LocalDateTime.now();
    }
    
    public void addNote(String note) {
        this.notes = (notes != null ? notes + "; " : "") + note;
    }
    
    public void displayTransactionInfo() {
        System.out.println("=== STOCK TRANSACTION ===");
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Reference Number: " + referenceNumber);
        System.out.println("Item ID: " + itemId);
        System.out.println("Transaction Type: " + transactionType);
        System.out.println("Quantity: " + quantity);
        System.out.println("Unit Price: $" + String.format("%.2f", unitPrice));
        System.out.println("Total Amount: $" + String.format("%.2f", totalAmount));
        System.out.println("Reason: " + (reason != null ? reason : "Not specified"));
        System.out.println("Performed By: " + performedBy);
        System.out.println("Supplier: " + (supplier != null ? supplier : "N/A"));
        System.out.println("Recipient: " + (recipient != null ? recipient : "N/A"));
        System.out.println("Transaction Date: " + transactionDate);
        System.out.println("Approved: " + (isApproved ? "Yes" : "No"));
        System.out.println("Approved By: " + (approvedBy != null ? approvedBy : "Pending"));
        System.out.println("Approval Date: " + (approvalDate != null ? approvalDate : "N/A"));
        System.out.println("Notes: " + (notes != null ? notes : "None"));
    }
    
    // Getters and Setters
    @Override
    public String getId() { return transactionId; }
    
    public String getTransactionId() { return transactionId; }
    public String getItemId() { return itemId; }
    public TransactionType getTransactionType() { return transactionType; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getTotalAmount() { return totalAmount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getPerformedBy() { return performedBy; }
    public String getReferenceNumber() { return referenceNumber; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public boolean isApproved() { return isApproved; }
    public String getApprovedBy() { return approvedBy; }
    public LocalDateTime getApprovalDate() { return approvalDate; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockTransaction that = (StockTransaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
    
    @Override
    public String toString() {
        return "StockTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", transactionType=" + transactionType +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", transactionDate=" + transactionDate +
                '}';
    }
}