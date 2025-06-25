package com.campus.transport;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MaintenanceRecord class for tracking vehicle maintenance
 */
public class MaintenanceRecord implements Identifiable {
    private String recordId;
    private String vehicleId;
    private String maintenanceType;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private double cost;
    private String performedBy;
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL
    private String partsReplaced;
    private int mileageAtService;
    private String nextServiceRecommendation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    private boolean warrantyWork;
    private String vendorName;
    private String invoiceNumber;
    
    public MaintenanceRecord(String recordId, String vehicleId, String maintenanceType, String description) {
        this.recordId = recordId;
        this.vehicleId = vehicleId;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.startDate = LocalDate.now();
        this.status = "SCHEDULED";
        this.priority = "MEDIUM";
        this.cost = 0.0;
        this.warrantyWork = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void startMaintenance(String performedBy) {
        this.status = "IN_PROGRESS";
        this.performedBy = performedBy;
        this.startDate = LocalDate.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void completeMaintenance(double cost, String partsReplaced) {
        this.status = "COMPLETED";
        this.endDate = LocalDate.now();
        this.cost = cost;
        this.partsReplaced = partsReplaced;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancelMaintenance(String reason) {
        this.status = "CANCELLED";
        this.notes = (notes != null ? notes + "; " : "") + "Cancelled: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    public boolean isInProgress() {
        return "IN_PROGRESS".equals(status);
    }
    
    public boolean isOverdue() {
        return "SCHEDULED".equals(status) && startDate.isBefore(LocalDate.now());
    }
    
    public int getDurationDays() {
        if (endDate != null && startDate != null) {
            return (int) (endDate.toEpochDay() - startDate.toEpochDay());
        }
        return 0;
    }
    
    public void addNote(String note) {
        this.notes = (this.notes != null ? this.notes + "; " : "") + note;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setNextServiceRecommendation(String recommendation) {
        this.nextServiceRecommendation = recommendation;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void displayMaintenanceInfo() {
        System.out.println("=== MAINTENANCE RECORD ===");
        System.out.println("Record ID: " + recordId);
        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Type: " + maintenanceType);
        System.out.println("Description: " + description);
        System.out.println("Status: " + status);
        System.out.println("Priority: " + priority);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + (endDate != null ? endDate : "Not completed"));
        System.out.println("Duration: " + getDurationDays() + " days");
        System.out.println("Cost: $" + String.format("%.2f", cost));
        System.out.println("Performed By: " + (performedBy != null ? performedBy : "Not assigned"));
        System.out.println("Parts Replaced: " + (partsReplaced != null ? partsReplaced : "None"));
        System.out.println("Mileage at Service: " + mileageAtService + " km");
        System.out.println("Warranty Work: " + (warrantyWork ? "Yes" : "No"));
        System.out.println("Vendor: " + (vendorName != null ? vendorName : "Internal"));
        System.out.println("Invoice Number: " + (invoiceNumber != null ? invoiceNumber : "N/A"));
        System.out.println("Next Service Recommendation: " + 
                          (nextServiceRecommendation != null ? nextServiceRecommendation : "None"));
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
        System.out.println("Notes: " + (notes != null ? notes : "None"));
        System.out.println("Overdue: " + (isOverdue() ? "Yes" : "No"));
    }
    
    // Getters and Setters
    @Override
    public String getId() { return recordId; }
    
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    
    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { 
        this.maintenanceType = maintenanceType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { 
        this.startDate = startDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { 
        this.endDate = endDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getCost() { return cost; }
    public void setCost(double cost) { 
        this.cost = cost;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { 
        this.performedBy = performedBy;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { 
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPartsReplaced() { return partsReplaced; }
    public void setPartsReplaced(String partsReplaced) { 
        this.partsReplaced = partsReplaced;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getMileageAtService() { return mileageAtService; }
    public void setMileageAtService(int mileageAtService) { 
        this.mileageAtService = mileageAtService;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNextServiceRecommendation() { return nextServiceRecommendation; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isWarrantyWork() { return warrantyWork; }
    public void setWarrantyWork(boolean warrantyWork) { 
        this.warrantyWork = warrantyWork;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { 
        this.vendorName = vendorName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { 
        this.invoiceNumber = invoiceNumber;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceRecord that = (MaintenanceRecord) o;
        return Objects.equals(recordId, that.recordId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }
    
    @Override
    public String toString() {
        return "MaintenanceRecord{" +
                "recordId='" + recordId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", maintenanceType='" + maintenanceType + '\'' +
                ", status='" + status + '\'' +
                ", cost=" + cost +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}