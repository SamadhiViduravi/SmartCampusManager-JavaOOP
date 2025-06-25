package com.campus.transport;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all vehicles in the transport system
 * Demonstrates inheritance and abstraction
 */
public abstract class Vehicle implements Identifiable {
    protected String vehicleId;
    protected String registrationNumber;
    protected String model;
    protected String manufacturer;
    protected int year;
    protected int capacity;
    protected VehicleType vehicleType;
    protected String fuelType;
    protected double fuelCapacity;
    protected double currentFuelLevel;
    protected double mileage;
    protected LocalDate purchaseDate;
    protected LocalDate lastServiceDate;
    protected LocalDate nextServiceDate;
    protected boolean isActive;
    protected String currentLocation;
    protected List<MaintenanceRecord> maintenanceHistory;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    
    public Vehicle(String vehicleId, String registrationNumber, String model, 
                   String manufacturer, int year, int capacity, VehicleType vehicleType) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.manufacturer = manufacturer;
        this.year = year;
        this.capacity = capacity;
        this.vehicleType = vehicleType;
        this.isActive = true;
        this.maintenanceHistory = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.currentFuelLevel = 0.0;
        this.fuelType = "Diesel";
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract void startEngine();
    public abstract void stopEngine();
    public abstract double calculateMaintenanceCost();
    public abstract boolean isReadyForService();
    
    public boolean isAvailable() {
        return isActive && !isUnderMaintenance() && currentFuelLevel > 10.0;
    }
    
    public boolean isUnderMaintenance() {
        return maintenanceHistory.stream()
                .anyMatch(record -> record.getEndDate() == null);
    }
    
    public void refuel(double liters) {
        if (liters <= 0) {
            throw new IllegalArgumentException("Fuel amount must be positive");
        }
        
        double newLevel = currentFuelLevel + liters;
        if (newLevel > fuelCapacity) {
            currentFuelLevel = fuelCapacity;
        } else {
            currentFuelLevel = newLevel;
        }
        updatedAt = LocalDateTime.now();
    }
    
    public void consumeFuel(double liters) {
        if (liters <= 0) {
            throw new IllegalArgumentException("Fuel consumption must be positive");
        }
        
        if (currentFuelLevel < liters) {
            throw new IllegalStateException("Insufficient fuel");
        }
        
        currentFuelLevel -= liters;
        updatedAt = LocalDateTime.now();
    }
    
    public double getFuelPercentage() {
        return (currentFuelLevel / fuelCapacity) * 100.0;
    }
    
    public void addMaintenanceRecord(MaintenanceRecord record) {
        maintenanceHistory.add(record);
        if (record.getEndDate() == null) {
            isActive = false; // Vehicle under maintenance
        }
        updatedAt = LocalDateTime.now();
    }
    
    public void completeMaintenanceRecord(String recordId, LocalDate endDate, double cost) {
        maintenanceHistory.stream()
                .filter(record -> record.getRecordId().equals(recordId))
                .findFirst()
                .ifPresent(record -> {
                    record.setEndDate(endDate);
                    record.setCost(cost);
                    isActive = true; // Vehicle back in service
                    lastServiceDate = endDate;
                    nextServiceDate = endDate.plusMonths(6); // 6 months service interval
                });
        updatedAt = LocalDateTime.now();
    }
    
    public boolean needsService() {
        return nextServiceDate != null && LocalDate.now().isAfter(nextServiceDate);
    }
    
    public void displayVehicleInfo() {
        System.out.println("=== VEHICLE INFORMATION ===");
        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Registration: " + registrationNumber);
        System.out.println("Model: " + manufacturer + " " + model);
        System.out.println("Year: " + year);
        System.out.println("Type: " + vehicleType);
        System.out.println("Capacity: " + capacity + " passengers");
        System.out.println("Fuel Type: " + fuelType);
        System.out.println("Fuel Level: " + String.format("%.1f", getFuelPercentage()) + "%");
        System.out.println("Mileage: " + mileage + " km");
        System.out.println("Status: " + (isActive ? "Active" : "Inactive"));
        System.out.println("Location: " + currentLocation);
        System.out.println("Last Service: " + lastServiceDate);
        System.out.println("Next Service: " + nextServiceDate);
        System.out.println("Maintenance Records: " + maintenanceHistory.size());
        System.out.println("Available: " + (isAvailable() ? "Yes" : "No"));
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
    }
    
    // Getters and Setters
    @Override
    public String getId() { return vehicleId; }
    
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { 
        this.vehicleId = vehicleId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { 
        this.registrationNumber = registrationNumber;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getModel() { return model; }
    public void setModel(String model) { 
        this.model = model;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { 
        this.manufacturer = manufacturer;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getYear() { return year; }
    public void setYear(int year) { 
        this.year = year;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { 
        this.capacity = capacity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { 
        this.vehicleType = vehicleType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { 
        this.fuelType = fuelType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getFuelCapacity() { return fuelCapacity; }
    public void setFuelCapacity(double fuelCapacity) { 
        this.fuelCapacity = fuelCapacity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getCurrentFuelLevel() { return currentFuelLevel; }
    public void setCurrentFuelLevel(double currentFuelLevel) { 
        this.currentFuelLevel = currentFuelLevel;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getMileage() { return mileage; }
    public void setMileage(double mileage) { 
        this.mileage = mileage;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { 
        this.purchaseDate = purchaseDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getLastServiceDate() { return lastServiceDate; }
    public void setLastServiceDate(LocalDate lastServiceDate) { 
        this.lastServiceDate = lastServiceDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getNextServiceDate() { return nextServiceDate; }
    public void setNextServiceDate(LocalDate nextServiceDate) { 
        this.nextServiceDate = nextServiceDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { 
        isActive = active;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { 
        this.currentLocation = currentLocation;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<MaintenanceRecord> getMaintenanceHistory() { 
        return new ArrayList<>(maintenanceHistory); 
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vehicleId, vehicle.vehicleId) || 
               Objects.equals(registrationNumber, vehicle.registrationNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(vehicleId, registrationNumber);
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId='" + vehicleId + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", model='" + model + '\'' +
                ", vehicleType=" + vehicleType +
                ", capacity=" + capacity +
                ", isActive=" + isActive +
                '}';
    }
}