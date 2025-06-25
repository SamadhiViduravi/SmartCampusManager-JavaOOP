package com.campus.transport;

import com.campus.users.User;
import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Driver class representing vehicle drivers
 */
public class Driver implements Identifiable {
    private String driverId;
    private User user;
    private String licenseNumber;
    private String licenseType;
    private LocalDate licenseExpiryDate;
    private LocalDate hireDate;
    private boolean isActive;
    private List<String> certifications;
    private String currentVehicleAssigned;
    private String currentRouteAssigned;
    private int experienceYears;
    private double rating;
    private int totalTrips;
    private List<String> vehicleTypesAuthorized;
    private String emergencyContact;
    private String emergencyPhone;
    private LocalDateTime lastActiveTime;
    private String currentStatus; // AVAILABLE, ON_DUTY, OFF_DUTY, ON_LEAVE
    private List<String> performanceNotes;
    
    public Driver(String driverId, User user, String licenseNumber, String licenseType) {
        this.driverId = driverId;
        this.user = user;
        this.licenseNumber = licenseNumber;
        this.licenseType = licenseType;
        this.hireDate = LocalDate.now();
        this.isActive = true;
        this.certifications = new ArrayList<>();
        this.vehicleTypesAuthorized = new ArrayList<>();
        this.performanceNotes = new ArrayList<>();
        this.currentStatus = "AVAILABLE";
        this.rating = 5.0; // Start with perfect rating
        this.totalTrips = 0;
        this.experienceYears = 0;
        this.lastActiveTime = LocalDateTime.now();
        
        initializeBasicCertifications();
        authorizeVehicleTypes();
    }
    
    private void initializeBasicCertifications() {
        certifications.add("Defensive Driving");
        certifications.add("First Aid");
        certifications.add("Vehicle Safety");
    }
    
    private void authorizeVehicleTypes() {
        // Authorize based on license type
        switch (licenseType.toUpperCase()) {
            case "CDL-A":
                vehicleTypesAuthorized.addAll(Arrays.asList("BUS", "TRUCK", "VAN", "CAR"));
                break;
            case "CDL-B":
                vehicleTypesAuthorized.addAll(Arrays.asList("BUS", "VAN", "CAR"));
                break;
            case "REGULAR":
                vehicleTypesAuthorized.addAll(Arrays.asList("VAN", "CAR"));
                break;
            default:
                vehicleTypesAuthorized.add("CAR");
        }
    }
    
    public boolean canDriveVehicle(VehicleType vehicleType) {
        return isActive && 
               !isLicenseExpired() && 
               vehicleTypesAuthorized.contains(vehicleType.toString()) &&
               "AVAILABLE".equals(currentStatus);
    }
    
    public boolean isLicenseExpired() {
        return licenseExpiryDate != null && LocalDate.now().isAfter(licenseExpiryDate);
    }
    
    public void assignVehicle(String vehicleId, VehicleType vehicleType) {
        if (!canDriveVehicle(vehicleType)) {
            throw new IllegalStateException("Driver cannot be assigned to this vehicle type");
        }
        
        this.currentVehicleAssigned = vehicleId;
        this.currentStatus = "ON_DUTY";
        this.lastActiveTime = LocalDateTime.now();
        
        System.out.println("Driver " + user.getFullName() + " assigned to vehicle: " + vehicleId);
    }
    
    public void unassignVehicle() {
        System.out.println("Driver " + user.getFullName() + " unassigned from vehicle: " + currentVehicleAssigned);
        this.currentVehicleAssigned = null;
        this.currentStatus = "AVAILABLE";
        this.lastActiveTime = LocalDateTime.now();
    }
    
    public void assignRoute(String routeId) {
        this.currentRouteAssigned = routeId;
        this.lastActiveTime = LocalDateTime.now();
        System.out.println("Driver " + user.getFullName() + " assigned to route: " + routeId);
    }
    
    public void unassignRoute() {
        System.out.println("Driver " + user.getFullName() + " unassigned from route: " + currentRouteAssigned);
        this.currentRouteAssigned = null;
        this.lastActiveTime = LocalDateTime.now();
    }
    
    public void startTrip() {
        if (currentVehicleAssigned == null) {
            throw new IllegalStateException("No vehicle assigned to driver");
        }
        
        this.currentStatus = "ON_DUTY";
        this.lastActiveTime = LocalDateTime.now();
        System.out.println("Trip started by driver: " + user.getFullName());
    }
    
    public void endTrip() {
        this.totalTrips++;
        this.currentStatus = "AVAILABLE";
        this.lastActiveTime = LocalDateTime.now();
        System.out.println("Trip completed by driver: " + user.getFullName());
        System.out.println("Total trips completed: " + totalTrips);
    }
    
    public void addCertification(String certification) {
        if (!certifications.contains(certification)) {
            certifications.add(certification);
            System.out.println("Certification added: " + certification);
        }
    }
    
    public void removeCertification(String certification) {
        if (certifications.remove(certification)) {
            System.out.println("Certification removed: " + certification);
        }
    }
    
    public void updateRating(double newRating) {
        if (newRating < 1.0 || newRating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1.0 and 5.0");
        }
        
        // Calculate weighted average (give more weight to recent performance)
        this.rating = (this.rating * 0.7) + (newRating * 0.3);
        this.lastActiveTime = LocalDateTime.now();
    }
    
    public void addPerformanceNote(String note) {
        performanceNotes.add(LocalDate.now() + ": " + note);
        this.lastActiveTime = LocalDateTime.now();
    }
    
    public void setOnLeave() {
        this.currentStatus = "ON_LEAVE";
        this.lastActiveTime = LocalDateTime.now();
        
        // Unassign vehicle and route if on leave
        if (currentVehicleAssigned != null) {
            unassignVehicle();
        }
        if (currentRouteAssigned != null) {
            unassignRoute();
        }
    }
    
    public void returnFromLeave() {
        this.currentStatus = "AVAILABLE";
        this.lastActiveTime = LocalDateTime.now();
    }
    
    public void renewLicense(LocalDate newExpiryDate) {
        this.licenseExpiryDate = newExpiryDate;
        this.lastActiveTime = LocalDateTime.now();
        System.out.println("License renewed for driver: " + user.getFullName());
        System.out.println("New expiry date: " + newExpiryDate);
    }
    
    public boolean needsLicenseRenewal() {
        return licenseExpiryDate != null && 
               LocalDate.now().plusMonths(3).isAfter(licenseExpiryDate);
    }
    
    public void displayDriverInfo() {
        System.out.println("=== DRIVER INFORMATION ===");
        System.out.println("Driver ID: " + driverId);
        System.out.println("Name: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("License Number: " + licenseNumber);
        System.out.println("License Type: " + licenseType);
        System.out.println("License Expiry: " + licenseExpiryDate);
        System.out.println("License Status: " + (isLicenseExpired() ? "EXPIRED" : "VALID"));
        System.out.println("Hire Date: " + hireDate);
        System.out.println("Experience: " + experienceYears + " years");
        System.out.println("Status: " + currentStatus);
        System.out.println("Rating: " + String.format("%.1f", rating) + "/5.0");
        System.out.println("Total Trips: " + totalTrips);
        System.out.println("Current Vehicle: " + (currentVehicleAssigned != null ? currentVehicleAssigned : "None"));
        System.out.println("Current Route: " + (currentRouteAssigned != null ? currentRouteAssigned : "None"));
        System.out.println("Authorized Vehicles: " + String.join(", ", vehicleTypesAuthorized));
        System.out.println("Certifications: " + String.join(", ", certifications));
        System.out.println("Emergency Contact: " + emergencyContact + " (" + emergencyPhone + ")");
        System.out.println("Last Active: " + lastActiveTime);
        System.out.println("Active: " + (isActive ? "Yes" : "No"));
        System.out.println("Can Drive: " + (canDriveVehicle(VehicleType.BUS) ? "Yes" : "No"));
        
        if (!performanceNotes.isEmpty()) {
            System.out.println("\nPerformance Notes:");
            performanceNotes.stream().limit(5).forEach(note -> System.out.println("- " + note));
            if (performanceNotes.size() > 5) {
                System.out.println("... and " + (performanceNotes.size() - 5) + " more notes");
            }
        }
    }
    
    // Getters and Setters
    @Override
    public String getId() { return driverId; }
    
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getLicenseType() { return licenseType; }
    public void setLicenseType(String licenseType) { 
        this.licenseType = licenseType;
        authorizeVehicleTypes(); // Re-authorize based on new license type
    }
    
    public LocalDate getLicenseExpiryDate() { return licenseExpiryDate; }
    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) { this.licenseExpiryDate = licenseExpiryDate; }
    
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public List<String> getCertifications() { return new ArrayList<>(certifications); }
    
    public String getCurrentVehicleAssigned() { return currentVehicleAssigned; }
    public String getCurrentRouteAssigned() { return currentRouteAssigned; }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public double getRating() { return rating; }
    public int getTotalTrips() { return totalTrips; }
    
    public List<String> getVehicleTypesAuthorized() { return new ArrayList<>(vehicleTypesAuthorized); }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    
    public LocalDateTime getLastActiveTime() { return lastActiveTime; }
    public String getCurrentStatus() { return currentStatus; }
    
    public List<String> getPerformanceNotes() { return new ArrayList<>(performanceNotes); }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(driverId, driver.driverId) || 
               Objects.equals(licenseNumber, driver.licenseNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(driverId, licenseNumber);
    }
    
    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + driverId + '\'' +
                ", name='" + user.getFullName() + '\'' +
                ", licenseType='" + licenseType + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                ", rating=" + String.format("%.1f", rating) +
                ", totalTrips=" + totalTrips +
                '}';
    }
}