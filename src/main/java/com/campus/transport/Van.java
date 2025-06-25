package com.campus.transport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Van class extending Vehicle
 * Demonstrates inheritance and specialized functionality
 */
public class Van extends Vehicle {
    private boolean hasCargoSpace;
    private double cargoCapacity; // in cubic meters
    private boolean hasSlidingDoors;
    private String purpose; // PASSENGER, CARGO, MIXED
    private List<String> specialEquipment;
    private boolean hasAirConditioning;
    private double averageSpeed;
    private String driveType; // FWD, RWD, AWD
    
    public Van(String vehicleId, String registrationNumber, String model, 
               String manufacturer, int year, int capacity) {
        super(vehicleId, registrationNumber, model, manufacturer, year, capacity, VehicleType.VAN);
        this.hasCargoSpace = true;
        this.cargoCapacity = 5.0; // 5 cubic meters default
        this.hasSlidingDoors = true;
        this.purpose = "PASSENGER";
        this.specialEquipment = new ArrayList<>();
        this.fuelCapacity = 80.0; // 80 liters for van
        this.averageSpeed = 60.0; // 60 km/h average
        this.driveType = "FWD";
        initializeEquipment();
    }
    
    private void initializeEquipment() {
        specialEquipment.add("First Aid Kit");
        specialEquipment.add("Tool Kit");
        specialEquipment.add("Spare Tire");
        if (hasAirConditioning) specialEquipment.add("Climate Control");
    }
    
    @Override
    public void startEngine() {
        if (!isAvailable()) {
            throw new IllegalStateException("Van is not available for operation");
        }
        
        if (currentFuelLevel < 10.0) {
            throw new IllegalStateException("Insufficient fuel to start engine");
        }
        
        System.out.println("Van " + registrationNumber + " engine started");
        System.out.println("Performing pre-trip checks...");
        System.out.println("- Checking tire pressure: OK");
        System.out.println("- Checking cargo area: " + (hasCargoSpace ? "Secured" : "N/A"));
        System.out.println("- Checking sliding doors: " + (hasSlidingDoors ? "Operational" : "N/A"));
        System.out.println("- Fuel level: " + String.format("%.1f", getFuelPercentage()) + "%");
        System.out.println("Van ready for " + purpose.toLowerCase() + " service");
        
        setCurrentLocation("In Service");
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public void stopEngine() {
        System.out.println("Van " + registrationNumber + " engine stopped");
        System.out.println("Performing post-trip checks...");
        System.out.println("- Securing cargo area");
        System.out.println("- Checking for damage");
        System.out.println("- Recording trip details");
        
        setCurrentLocation("Parking");
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public double calculateMaintenanceCost() {
        double baseCost = 2500.0; // Base maintenance cost for van
        double ageFactor = (2024 - year) * 150.0; // Additional cost based on age
        double mileageFactor = mileage * 0.05; // Cost based on mileage
        double equipmentCost = specialEquipment.size() * 50.0; // Cost for maintaining equipment
        double cargoCost = hasCargoSpace ? cargoCapacity * 20.0 : 0.0; // Cargo area maintenance
        
        return baseCost + ageFactor + mileageFactor + equipmentCost + cargoCost;
    }
    
    @Override
    public boolean isReadyForService() {
        return isAvailable() && 
               currentFuelLevel > 15.0 && 
               !needsService() &&
               (!hasCargoSpace || isCargoAreaSecure());
    }
    
    private boolean isCargoAreaSecure() {
        // In a real implementation, this would check sensors or manual inspection
        return true;
    }
    
    public void configurePurpose(String newPurpose) {
        if (!newPurpose.matches("PASSENGER|CARGO|MIXED")) {
            throw new IllegalArgumentException("Invalid purpose. Must be PASSENGER, CARGO, or MIXED");
        }
        
        this.purpose = newPurpose;
        
        // Adjust capacity based on purpose
        switch (newPurpose) {
            case "PASSENGER":
                this.capacity = Math.max(capacity, 8);
                this.cargoCapacity = 2.0;
                break;
            case "CARGO":
                this.capacity = 2; // Driver + 1 passenger
                this.cargoCapacity = 8.0;
                break;
            case "MIXED":
                this.capacity = 5;
                this.cargoCapacity = 4.0;
                break;
        }
        
        updatedAt = LocalDateTime.now();
        System.out.println("Van " + registrationNumber + " reconfigured for " + newPurpose + " use");
    }
    
    public void addSpecialEquipment(String equipment) {
        if (!specialEquipment.contains(equipment)) {
            specialEquipment.add(equipment);
            updatedAt = LocalDateTime.now();
            System.out.println("Added equipment: " + equipment);
        }
    }
    
    public void removeSpecialEquipment(String equipment) {
        if (specialEquipment.remove(equipment)) {
            updatedAt = LocalDateTime.now();
            System.out.println("Removed equipment: " + equipment);
        }
    }
    
    public double calculateLoadCapacity() {
        // Calculate based on purpose and cargo space
        switch (purpose) {
            case "PASSENGER":
                return capacity * 75.0; // 75kg per passenger
            case "CARGO":
                return cargoCapacity * 200.0; // 200kg per cubic meter
            case "MIXED":
                return (capacity * 75.0) + (cargoCapacity * 150.0);
            default:
                return 0.0;
        }
    }
    
    public double calculateTripTime(double distance) {
        return distance / averageSpeed; // Time in hours
    }
    
    public double calculateFuelConsumption(double distance) {
        double fuelEfficiency = 12.0; // km per liter for van
        return distance / fuelEfficiency;
    }
    
    @Override
    public void displayVehicleInfo() {
        super.displayVehicleInfo();
        System.out.println("\n=== VAN SPECIFIC INFORMATION ===");
        System.out.println("Purpose: " + purpose);
        System.out.println("Passenger Capacity: " + capacity);
        System.out.println("Has Cargo Space: " + (hasCargoSpace ? "Yes" : "No"));
        System.out.println("Cargo Capacity: " + cargoCapacity + " cubic meters");
        System.out.println("Load Capacity: " + String.format("%.1f", calculateLoadCapacity()) + " kg");
        System.out.println("Has Sliding Doors: " + (hasSlidingDoors ? "Yes" : "No"));
        System.out.println("Air Conditioning: " + (hasAirConditioning ? "Yes" : "No"));
        System.out.println("Average Speed: " + averageSpeed + " km/h");
        System.out.println("Drive Type: " + driveType);
        System.out.println("Special Equipment: " + String.join(", ", specialEquipment));
        System.out.println("Ready for Service: " + (isReadyForService() ? "Yes" : "No"));
    }
    
    // Getters and Setters
    public boolean hasCargoSpace() { return hasCargoSpace; }
    public void setHasCargoSpace(boolean hasCargoSpace) { 
        this.hasCargoSpace = hasCargoSpace;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getCargoCapacity() { return cargoCapacity; }
    public void setCargoCapacity(double cargoCapacity) { 
        this.cargoCapacity = cargoCapacity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasSlidingDoors() { return hasSlidingDoors; }
    public void setHasSlidingDoors(boolean hasSlidingDoors) { 
        this.hasSlidingDoors = hasSlidingDoors;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPurpose() { return purpose; }
    
    public List<String> getSpecialEquipment() { return new ArrayList<>(specialEquipment); }
    
    public boolean hasAirConditioning() { return hasAirConditioning; }
    public void setHasAirConditioning(boolean hasAirConditioning) { 
        this.hasAirConditioning = hasAirConditioning;
        if (hasAirConditioning && !specialEquipment.contains("Climate Control")) {
            specialEquipment.add("Climate Control");
        } else if (!hasAirConditioning) {
            specialEquipment.remove("Climate Control");
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getAverageSpeed() { return averageSpeed; }
    public void setAverageSpeed(double averageSpeed) { 
        this.averageSpeed = averageSpeed;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDriveType() { return driveType; }
    public void setDriveType(String driveType) { 
        this.driveType = driveType;
        this.updatedAt = LocalDateTime.now();
    }
}