package com.campus.transport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Bus class extending Vehicle
 * Demonstrates inheritance and polymorphism
 */
public class Bus extends Vehicle {
    private int standingCapacity;
    private boolean hasAirConditioning;
    private boolean hasWiFi;
    private boolean isAccessible;
    private String routeAssigned;
    private List<String> amenities;
    private double averageSpeed;
    private int doorCount;
    private String engineType;
    
    public Bus(String vehicleId, String registrationNumber, String model, 
               String manufacturer, int year, int seatingCapacity) {
        super(vehicleId, registrationNumber, model, manufacturer, year, seatingCapacity, VehicleType.BUS);
        this.standingCapacity = seatingCapacity / 2; // Typically half of seating
        this.amenities = new ArrayList<>();
        this.fuelCapacity = 200.0; // 200 liters for bus
        this.averageSpeed = 40.0; // 40 km/h average
        this.doorCount = 2;
        this.engineType = "Diesel";
        initializeAmenities();
    }
    
    private void initializeAmenities() {
        amenities.add("First Aid Kit");
        amenities.add("Fire Extinguisher");
        amenities.add("Emergency Exit");
        if (hasAirConditioning) amenities.add("Air Conditioning");
        if (hasWiFi) amenities.add("WiFi");
        if (isAccessible) amenities.add("Wheelchair Accessible");
    }
    
    @Override
    public void startEngine() {
        if (!isAvailable()) {
            throw new IllegalStateException("Bus is not available for operation");
        }
        
        if (currentFuelLevel < 20.0) {
            throw new IllegalStateException("Insufficient fuel to start engine");
        }
        
        System.out.println("Bus " + registrationNumber + " engine started");
        System.out.println("Performing pre-trip safety checks...");
        System.out.println("- Checking brakes: OK");
        System.out.println("- Checking lights: OK");
        System.out.println("- Checking doors: OK");
        System.out.println("- Fuel level: " + String.format("%.1f", getFuelPercentage()) + "%");
        System.out.println("Bus ready for service");
        
        setCurrentLocation("In Transit");
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public void stopEngine() {
        System.out.println("Bus " + registrationNumber + " engine stopped");
        System.out.println("Performing post-trip checks...");
        System.out.println("- Cleaning interior");
        System.out.println("- Checking for lost items");
        System.out.println("- Recording mileage");
        
        setCurrentLocation("Depot");
        updatedAt = LocalDateTime.now();
    }
    
    @Override
    public double calculateMaintenanceCost() {
        double baseCost = 5000.0; // Base maintenance cost for bus
        double ageFactor = (2024 - year) * 200.0; // Additional cost based on age
        double mileageFactor = mileage * 0.1; // Cost based on mileage
        double amenityCost = amenities.size() * 100.0; // Cost for maintaining amenities
        
        return baseCost + ageFactor + mileageFactor + amenityCost;
    }
    
    @Override
    public boolean isReadyForService() {
        return isAvailable() && 
               currentFuelLevel > 25.0 && 
               !needsService() &&
               routeAssigned != null &&
               !routeAssigned.isEmpty();
    }
    
    public int getTotalCapacity() {
        return capacity + standingCapacity;
    }
    
    public void assignRoute(String routeId) {
        this.routeAssigned = routeId;
        updatedAt = LocalDateTime.now();
        System.out.println("Bus " + registrationNumber + " assigned to route: " + routeId);
    }
    
    public void unassignRoute() {
        System.out.println("Bus " + registrationNumber + " unassigned from route: " + routeAssigned);
        this.routeAssigned = null;
        updatedAt = LocalDateTime.now();
    }
    
    public void addAmenity(String amenity) {
        if (!amenities.contains(amenity)) {
            amenities.add(amenity);
            updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeAmenity(String amenity) {
        amenities.remove(amenity);
        updatedAt = LocalDateTime.now();
    }
    
    public double calculateTripTime(double distance) {
        return distance / averageSpeed; // Time in hours
    }
    
    public double calculateFuelConsumption(double distance) {
        double fuelEfficiency = 8.0; // km per liter for bus
        return distance / fuelEfficiency;
    }
    
    @Override
    public void displayVehicleInfo() {
        super.displayVehicleInfo();
        System.out.println("\n=== BUS SPECIFIC INFORMATION ===");
        System.out.println("Seating Capacity: " + capacity);
        System.out.println("Standing Capacity: " + standingCapacity);
        System.out.println("Total Capacity: " + getTotalCapacity());
        System.out.println("Air Conditioning: " + (hasAirConditioning ? "Yes" : "No"));
        System.out.println("WiFi: " + (hasWiFi ? "Yes" : "No"));
        System.out.println("Wheelchair Accessible: " + (isAccessible ? "Yes" : "No"));
        System.out.println("Route Assigned: " + (routeAssigned != null ? routeAssigned : "None"));
        System.out.println("Average Speed: " + averageSpeed + " km/h");
        System.out.println("Door Count: " + doorCount);
        System.out.println("Engine Type: " + engineType);
        System.out.println("Amenities: " + String.join(", ", amenities));
        System.out.println("Ready for Service: " + (isReadyForService() ? "Yes" : "No"));
    }
    
    // Getters and Setters
    public int getStandingCapacity() { return standingCapacity; }
    public void setStandingCapacity(int standingCapacity) { 
        this.standingCapacity = standingCapacity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasAirConditioning() { return hasAirConditioning; }
    public void setHasAirConditioning(boolean hasAirConditioning) { 
        this.hasAirConditioning = hasAirConditioning;
        if (hasAirConditioning && !amenities.contains("Air Conditioning")) {
            amenities.add("Air Conditioning");
        } else if (!hasAirConditioning) {
            amenities.remove("Air Conditioning");
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasWiFi() { return hasWiFi; }
    public void setHasWiFi(boolean hasWiFi) { 
        this.hasWiFi = hasWiFi;
        if (hasWiFi && !amenities.contains("WiFi")) {
            amenities.add("WiFi");
        } else if (!hasWiFi) {
            amenities.remove("WiFi");
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isAccessible() { return isAccessible; }
    public void setAccessible(boolean accessible) { 
        isAccessible = accessible;
        if (accessible && !amenities.contains("Wheelchair Accessible")) {
            amenities.add("Wheelchair Accessible");
        } else if (!accessible) {
            amenities.remove("Wheelchair Accessible");
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getRouteAssigned() { return routeAssigned; }
    public List<String> getAmenities() { return new ArrayList<>(amenities); }
    public double getAverageSpeed() { return averageSpeed; }
    public void setAverageSpeed(double averageSpeed) { 
        this.averageSpeed = averageSpeed;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getDoorCount() { return doorCount; }
    public void setDoorCount(int doorCount) { 
        this.doorCount = doorCount;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getEngineType() { return engineType; }
    public void setEngineType(String engineType) { 
        this.engineType = engineType;
        this.updatedAt = LocalDateTime.now();
    }
}