package com.campus.hostel;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Allocation class representing room allocation to students
 */
public class Allocation implements Identifiable {
    private String allocationId;
    private String studentId;
    private String roomId;
    private LocalDate allocationDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate expectedCheckOutDate;
    private AllocationStatus status;
    private double securityDeposit;
    private boolean securityDepositPaid;
    private String allocationReason;
    private String specialRequests;
    private String allocatedBy;
    private String approvedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    
    public Allocation(String allocationId, String studentId, String roomId) {
        this.allocationId = allocationId;
        this.studentId = studentId;
        this.roomId = roomId;
        this.allocationDate = LocalDate.now();
        this.status = AllocationStatus.PENDING;
        this.securityDeposit = 500.0; // Default security deposit
        this.securityDepositPaid = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void approve(String approvedBy) {
        if (status != AllocationStatus.PENDING) {
            throw new IllegalStateException("Only pending allocations can be approved");
        }
        
        this.status = AllocationStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reject(String reason) {
        if (status != AllocationStatus.PENDING) {
            throw new IllegalStateException("Only pending allocations can be rejected");
        }
        
        this.status = AllocationStatus.REJECTED;
        this.notes = (notes != null ? notes + "; " : "") + "Rejected: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void checkIn() {
        if (status != AllocationStatus.APPROVED) {
            throw new IllegalStateException("Student must have approved allocation to check in");
        }
        
        if (!securityDepositPaid) {
            throw new IllegalStateException("Security deposit must be paid before check-in");
        }
        
        this.checkInDate = LocalDate.now();
        this.status = AllocationStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void checkOut() {
        if (status != AllocationStatus.ACTIVE) {
            throw new IllegalStateException("Only active allocations can be checked out");
        }
        
        this.checkOutDate = LocalDate.now();
        this.status = AllocationStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancel(String reason) {
        if (status == AllocationStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed allocation");
        }
        
        this.status = AllocationStatus.CANCELLED;
        this.notes = (notes != null ? notes + "; " : "") + "Cancelled: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void paySecurityDeposit() {
        this.securityDepositPaid = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void refundSecurityDeposit() {
        if (status == AllocationStatus.COMPLETED) {
            this.securityDepositPaid = false; // Indicates refund processed
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public boolean isActive() {
        return status == AllocationStatus.ACTIVE;
    }
    
    public boolean isOverstaying() {
        return expectedCheckOutDate != null && 
               LocalDate.now().isAfter(expectedCheckOutDate) && 
               status == AllocationStatus.ACTIVE;
    }
    
    public long getDaysStayed() {
        if (checkInDate == null) return 0;
        
        LocalDate endDate = checkOutDate != null ? checkOutDate : LocalDate.now();
        return endDate.toEpochDay() - checkInDate.toEpochDay();
    }
    
    public void extendStay(LocalDate newCheckOutDate, String reason) {
        if (status != AllocationStatus.ACTIVE) {
            throw new IllegalStateException("Can only extend active allocations");
        }
        
        this.expectedCheckOutDate = newCheckOutDate;
        this.notes = (notes != null ? notes + "; " : "") + "Extended until " + newCheckOutDate + ": " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addNote(String note) {
        this.notes = (notes != null ? notes + "; " : "") + note;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void displayAllocationInfo() {
        System.out.println("=== ALLOCATION INFORMATION ===");
        System.out.println("Allocation ID: " + allocationId);
        System.out.println("Student ID: " + studentId);
        System.out.println("Room ID: " + roomId);
        System.out.println("Status: " + status);
        System.out.println("Allocation Date: " + allocationDate);
        System.out.println("Check-in Date: " + (checkInDate != null ? checkInDate : "Not checked in"));
        System.out.println("Check-out Date: " + (checkOutDate != null ? checkOutDate : "Not checked out"));
        System.out.println("Expected Check-out: " + (expectedCheckOutDate != null ? expectedCheckOutDate : "Not set"));
        System.out.println("Days Stayed: " + getDaysStayed());
        System.out.println("Security Deposit: $" + String.format("%.2f", securityDeposit));
        System.out.println("Security Deposit Paid: " + (securityDepositPaid ? "Yes" : "No"));
        System.out.println("Allocation Reason: " + (allocationReason != null ? allocationReason : "Standard"));
        System.out.println("Special Requests: " + (specialRequests != null ? specialRequests : "None"));
        System.out.println("Allocated By: " + (allocatedBy != null ? allocatedBy : "System"));
        System.out.println("Approved By: " + (approvedBy != null ? approvedBy : "Pending"));
        System.out.println("Overstaying: " + (isOverstaying() ? "Yes" : "No"));
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
        System.out.println("Notes: " + (notes != null ? notes : "None"));
    }
    
    // Getters and Setters
    @Override
    public String getId() { return allocationId; }
    
    public String getAllocationId() { return allocationId; }
    public void setAllocationId(String allocationId) { this.allocationId = allocationId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { 
        this.studentId = studentId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { 
        this.roomId = roomId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getAllocationDate() { return allocationDate; }
    public void setAllocationDate(LocalDate allocationDate) { 
        this.allocationDate = allocationDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    
    public LocalDate getExpectedCheckOutDate() { return expectedCheckOutDate; }
    public void setExpectedCheckOutDate(LocalDate expectedCheckOutDate) { 
        this.expectedCheckOutDate = expectedCheckOutDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public AllocationStatus getStatus() { return status; }
    
    public double getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(double securityDeposit) { 
        this.securityDeposit = securityDeposit;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isSecurityDepositPaid() { return securityDepositPaid; }
    
    public String getAllocationReason() { return allocationReason; }
    public void setAllocationReason(String allocationReason) { 
        this.allocationReason = allocationReason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { 
        this.specialRequests = specialRequests;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAllocatedBy() { return allocatedBy; }
    public void setAllocatedBy(String allocatedBy) { 
        this.allocatedBy = allocatedBy;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getApprovedBy() { return approvedBy; }
    
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
        Allocation that = (Allocation) o;
        return Objects.equals(allocationId, that.allocationId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(allocationId);
    }
    
    @Override
    public String toString() {
        return "Allocation{" +
                "allocationId='" + allocationId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", status=" + status +
                ", allocationDate=" + allocationDate +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }
}