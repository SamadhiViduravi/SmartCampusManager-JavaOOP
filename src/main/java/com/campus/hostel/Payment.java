package com.campus.hostel;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Payment class for hostel fee payments
 */
public class Payment implements Identifiable {
    private String paymentId;
    private String studentId;
    private String allocationId;
    private PaymentType paymentType;
    private double amount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private PaymentStatus status;
    private String paymentMethod;
    private String transactionId;
    private String description;
    private double lateFee;
    private double discount;
    private double finalAmount;
    private String receivedBy;
    private String receiptNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    
    public Payment(String paymentId, String studentId, String allocationId, PaymentType paymentType, double amount) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.allocationId = allocationId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.lateFee = 0.0;
        this.discount = 0.0;
        this.finalAmount = amount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // Set due date based on payment type
        setDefaultDueDate();
    }
    
    private void setDefaultDueDate() {
        switch (paymentType) {
            case MONTHLY_RENT:
                this.dueDate = LocalDate.now().plusDays(30);
                break;
            case SECURITY_DEPOSIT:
                this.dueDate = LocalDate.now().plusDays(7);
                break;
            case MAINTENANCE_FEE:
                this.dueDate = LocalDate.now().plusDays(15);
                break;
            case LATE_FEE:
                this.dueDate = LocalDate.now().plusDays(3);
                break;
            default:
                this.dueDate = LocalDate.now().plusDays(15);
        }
    }
    
    public void processPayment(String paymentMethod, String transactionId, String receivedBy) {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be processed");
        }
        
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.receivedBy = receivedBy;
        this.paymentDate = LocalDate.now();
        this.status = PaymentStatus.COMPLETED;
        this.receiptNumber = generateReceiptNumber();
        this.updatedAt = LocalDateTime.now();
        
        // Calculate final amount with late fees and discounts
        calculateFinalAmount();
    }
    
    private void calculateFinalAmount() {
        this.finalAmount = amount + lateFee - discount;
    }
    
    private String generateReceiptNumber() {
        return "RCP" + paymentId + System.currentTimeMillis() % 10000;
    }
    
    public void addLateFee(double lateFee) {
        this.lateFee += lateFee;
        calculateFinalAmount();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void applyDiscount(double discount, String reason) {
        this.discount = discount;
        this.notes = (notes != null ? notes + "; " : "") + "Discount applied: " + reason;
        calculateFinalAmount();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancelPayment(String reason) {
        if (status == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed payment");
        }
        
        this.status = PaymentStatus.CANCELLED;
        this.notes = (notes != null ? notes + "; " : "") + "Cancelled: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void refundPayment(String reason) {
        if (status != PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Only completed payments can be refunded");
        }
        
        this.status = PaymentStatus.REFUNDED;
        this.notes = (notes != null ? notes + "; " : "") + "Refunded: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isOverdue() {
        return status == PaymentStatus.PENDING && LocalDate.now().isAfter(dueDate);
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
    }
    
    public void markAsOverdue() {
        if (isOverdue() && status == PaymentStatus.PENDING) {
            this.status = PaymentStatus.OVERDUE;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void extendDueDate(LocalDate newDueDate, String reason) {
        this.dueDate = newDueDate;
        this.notes = (notes != null ? notes + "; " : "") + "Due date extended: " + reason;
        
        // If payment was overdue, change back to pending
        if (status == PaymentStatus.OVERDUE) {
            this.status = PaymentStatus.PENDING;
        }
        
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addNote(String note) {
        this.notes = (notes != null ? notes + "; " : "") + note;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void displayPaymentInfo() {
        System.out.println("=== PAYMENT INFORMATION ===");
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Student ID: " + studentId);
        System.out.println("Allocation ID: " + allocationId);
        System.out.println("Payment Type: " + paymentType);
        System.out.println("Status: " + status);
        System.out.println("Original Amount: $" + String.format("%.2f", amount));
        System.out.println("Late Fee: $" + String.format("%.2f", lateFee));
        System.out.println("Discount: $" + String.format("%.2f", discount));
        System.out.println("Final Amount: $" + String.format("%.2f", finalAmount));
        System.out.println("Due Date: " + dueDate);
        System.out.println("Payment Date: " + (paymentDate != null ? paymentDate : "Not paid"));
        System.out.println("Payment Method: " + (paymentMethod != null ? paymentMethod : "Not specified"));
        System.out.println("Transaction ID: " + (transactionId != null ? transactionId : "N/A"));
        System.out.println("Receipt Number: " + (receiptNumber != null ? receiptNumber : "N/A"));
        System.out.println("Received By: " + (receivedBy != null ? receivedBy : "N/A"));
        System.out.println("Description: " + (description != null ? description : "None"));
        System.out.println("Overdue: " + (isOverdue() ? "Yes (" + getDaysOverdue() + " days)" : "No"));
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
        System.out.println("Notes: " + (notes != null ? notes : "None"));
    }
    
    public void printReceipt() {
        if (status != PaymentStatus.COMPLETED) {
            System.out.println("Cannot print receipt for incomplete payment");
            return;
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("               PAYMENT RECEIPT");
        System.out.println("=".repeat(50));
        System.out.println("Receipt Number: " + receiptNumber);
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Date: " + paymentDate);
        System.out.println("-".repeat(50));
        System.out.println("Student ID: " + studentId);
        System.out.println("Payment Type: " + paymentType);
        System.out.println("Description: " + (description != null ? description : paymentType.toString()));
        System.out.println("-".repeat(50));
        System.out.printf("Amount:          $%10.2f%n", amount);
        if (lateFee > 0) {
            System.out.printf("Late Fee:        $%10.2f%n", lateFee);
        }
        if (discount > 0) {
            System.out.printf("Discount:       -$%10.2f%n", discount);
        }
        System.out.println("-".repeat(30));
        System.out.printf("Total Paid:      $%10.2f%n", finalAmount);
        System.out.println("-".repeat(50));
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Received By: " + receivedBy);
        System.out.println("=".repeat(50));
        System.out.println("Thank you for your payment!");
        System.out.println("=".repeat(50) + "\n");
    }
    
    // Getters and Setters
    @Override
    public String getId() { return paymentId; }
    
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { 
        this.studentId = studentId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAllocationId() { return allocationId; }
    public void setAllocationId(String allocationId) { 
        this.allocationId = allocationId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public PaymentType getPaymentType() { return paymentType; }
    public void setPaymentType(PaymentType paymentType) { 
        this.paymentType = paymentType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { 
        this.amount = amount;
        calculateFinalAmount();
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { 
        this.dueDate = dueDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getPaymentDate() { return paymentDate; }
    public PaymentStatus getStatus() { return status; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public String getTransactionId() { return transactionId; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getLateFee() { return lateFee; }
    public double getDiscount() { return discount; }
    public double getFinalAmount() { return finalAmount; }
    
    public String getReceivedBy() { return receivedBy; }
    public String getReceiptNumber() { return receiptNumber; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", paymentType=" + paymentType +
                ", amount=" + amount +
                ", status=" + status +
                ", dueDate=" + dueDate +
                ", paymentDate=" + paymentDate +
                '}';
    }
}