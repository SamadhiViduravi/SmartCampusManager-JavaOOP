package com.campus.library;

import com.campus.users.User;
import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Library member class representing users who can borrow books
 */
public class LibraryMember implements Identifiable {
    private String memberId;
    private User user;
    private LocalDate membershipDate;
    private LocalDate expiryDate;
    private boolean isActive;
    private int maxBooksAllowed;
    private int currentBooksIssued;
    private double totalFines;
    private double paidFines;
    private List<Transaction> transactionHistory;
    private String membershipType;
    private LocalDateTime lastActivity;
    
    public LibraryMember(String memberId, User user) {
        this.memberId = memberId;
        this.user = user;
        this.membershipDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(1);
        this.isActive = true;
        this.maxBooksAllowed = 5; // Default limit
        this.currentBooksIssued = 0;
        this.totalFines = 0.0;
        this.paidFines = 0.0;
        this.transactionHistory = new ArrayList<>();
        this.membershipType = "STANDARD";
        this.lastActivity = LocalDateTime.now();
    }
    
    public boolean canBorrowBooks() {
        return isActive && 
               currentBooksIssued < maxBooksAllowed && 
               !expiryDate.isBefore(LocalDate.now()) &&
               getOutstandingFines() <= 50.0; // Max outstanding fine limit
    }
    
    public double getOutstandingFines() {
        return totalFines - paidFines;
    }
    
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
        
        if (transaction.getTransactionType() == TransactionType.ISSUE) {
            currentBooksIssued++;
        } else if (transaction.getTransactionType() == TransactionType.RETURN) {
            currentBooksIssued--;
        }
        
        lastActivity = LocalDateTime.now();
    }
    
    public void addFine(double fine) {
        totalFines += fine;
        lastActivity = LocalDateTime.now();
    }
    
    public void payFine(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        
        double outstanding = getOutstandingFines();
        if (amount > outstanding) {
            throw new IllegalArgumentException("Payment amount exceeds outstanding fine");
        }
        
        paidFines += amount;
        lastActivity = LocalDateTime.now();
    }
    
    public void renewMembership(int years) {
        expiryDate = expiryDate.plusYears(years);
        lastActivity = LocalDateTime.now();
    }
    
    public void suspendMembership() {
        isActive = false;
        lastActivity = LocalDateTime.now();
    }
    
    public void activateMembership() {
        isActive = true;
        lastActivity = LocalDateTime.now();
    }
    
    public boolean isMembershipExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }
    
    public void displayMemberInfo() {
        System.out.println("=== LIBRARY MEMBER INFORMATION ===");
        System.out.println("Member ID: " + memberId);
        System.out.println("User: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Membership Type: " + membershipType);
        System.out.println("Membership Date: " + membershipDate);
        System.out.println("Expiry Date: " + expiryDate);
        System.out.println("Status: " + (isActive ? "Active" : "Suspended"));
        System.out.println("Max Books Allowed: " + maxBooksAllowed);
        System.out.println("Current Books Issued: " + currentBooksIssued);
        System.out.println("Total Fines: $" + String.format("%.2f", totalFines));
        System.out.println("Paid Fines: $" + String.format("%.2f", paidFines));
        System.out.println("Outstanding Fines: $" + String.format("%.2f", getOutstandingFines()));
        System.out.println("Total Transactions: " + transactionHistory.size());
        System.out.println("Last Activity: " + lastActivity);
        System.out.println("Can Borrow: " + (canBorrowBooks() ? "Yes" : "No"));
    }
    
    // Getters and Setters
    @Override
    public String getId() { return memberId; }
    
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public LocalDate getMembershipDate() { return membershipDate; }
    public void setMembershipDate(LocalDate membershipDate) { this.membershipDate = membershipDate; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public int getMaxBooksAllowed() { return maxBooksAllowed; }
    public void setMaxBooksAllowed(int maxBooksAllowed) { this.maxBooksAllowed = maxBooksAllowed; }
    
    public int getCurrentBooksIssued() { return currentBooksIssued; }
    
    public double getTotalFines() { return totalFines; }
    public double getPaidFines() { return paidFines; }
    
    public List<Transaction> getTransactionHistory() { return new ArrayList<>(transactionHistory); }
    
    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    
    public LocalDateTime getLastActivity() { return lastActivity; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryMember that = (LibraryMember) o;
        return Objects.equals(memberId, that.memberId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
    
    @Override
    public String toString() {
        return "LibraryMember{" +
                "memberId='" + memberId + '\'' +
                ", user=" + user.getFullName() +
                ", isActive=" + isActive +
                ", currentBooksIssued=" + currentBooksIssued +
                ", outstandingFines=" + getOutstandingFines() +
                '}';
    }
}