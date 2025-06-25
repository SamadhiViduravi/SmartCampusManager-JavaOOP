package com.campus.library;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transaction class representing library book transactions
 */
public class Transaction implements Identifiable {
    private String transactionId;
    private LibraryMember member;
    private Book book;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fineAmount;
    private boolean finePaid;
    private String remarks;
    private String processedBy;
    
    public Transaction(String transactionId, LibraryMember member, Book book, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.member = member;
        this.book = book;
        this.transactionType = transactionType;
        this.transactionDate = LocalDateTime.now();
        this.fineAmount = 0.0;
        this.finePaid = false;
        
        if (transactionType == TransactionType.ISSUE) {
            this.dueDate = LocalDate.now().plusDays(14); // 2 weeks default
        }
    }
    
    public boolean isOverdue() {
        return transactionType == TransactionType.ISSUE && 
               dueDate != null && 
               returnDate == null && 
               LocalDate.now().isAfter(dueDate);
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
    }
    
    public void processReturn(LocalDate returnDate) {
        if (transactionType != TransactionType.ISSUE) {
            throw new IllegalStateException("Cannot process return for non-issue transaction");
        }
        
        this.returnDate = returnDate;
        
        // Calculate fine if overdue
        if (returnDate.isAfter(dueDate)) {
            long daysLate = returnDate.toEpochDay() - dueDate.toEpochDay();
            this.fineAmount = daysLate * 1.0; // $1 per day fine
        }
    }
    
    public void payFine() {
        if (fineAmount > 0) {
            finePaid = true;
        }
    }
    
    public void extendDueDate(int days) {
        if (transactionType == TransactionType.ISSUE && returnDate == null) {
            dueDate = dueDate.plusDays(days);
        }
    }
    
    public void displayTransactionInfo() {
        System.out.println("=== TRANSACTION INFORMATION ===");
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Member: " + member.getUser().getFullName() + " (" + member.getMemberId() + ")");
        System.out.println("Book: " + book.getTitle() + " (" + book.getBookId() + ")");
        System.out.println("Type: " + transactionType);
        System.out.println("Transaction Date: " + transactionDate);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Return Date: " + (returnDate != null ? returnDate : "Not Returned"));
        System.out.println("Fine Amount: $" + String.format("%.2f", fineAmount));
        System.out.println("Fine Paid: " + (finePaid ? "Yes" : "No"));
        System.out.println("Overdue: " + (isOverdue() ? "Yes (" + getDaysOverdue() + " days)" : "No"));
        System.out.println("Processed By: " + processedBy);
        System.out.println("Remarks: " + remarks);
    }
    
    // Getters and Setters
    @Override
    public String getId() { return transactionId; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public LibraryMember getMember() { return member; }
    public void setMember(LibraryMember member) { this.member = member; }
    
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }
    
    public boolean isFinePaid() { return finePaid; }
    public void setFinePaid(boolean finePaid) { this.finePaid = finePaid; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public String getProcessedBy() { return processedBy; }
    public void setProcessedBy(String processedBy) { this.processedBy = processedBy; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", member=" + member.getMemberId() +
                ", book=" + book.getTitle() +
                ", type=" + transactionType +
                ", transactionDate=" + transactionDate +
                ", isOverdue=" + isOverdue() +
                '}';
    }
}