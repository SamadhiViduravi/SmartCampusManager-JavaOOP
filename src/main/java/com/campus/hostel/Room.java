package com.campus.hostel;

import com.campus.utils.Identifiable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Room class representing hostel rooms
 */
public class Room implements Identifiable {
    private String roomId;
    private String roomNumber;
    private RoomType roomType;
    private RoomStatus status;
    private String hostelBlock;
    private int floor;
    private int capacity;
    private int currentOccupancy;
    private double monthlyRent;
    private List<String> amenities;
    private List<String> currentOccupants;
    private String description;
    private LocalDateTime lastCleaned;
    private LocalDateTime lastMaintenance;
    private boolean hasBalcony;
    private boolean hasAttachedBathroom;
    private String facing; // NORTH, SOUTH, EAST, WEST
    private double area; // in square meters
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Room(String roomId, String roomNumber, RoomType roomType, String hostelBlock, int floor) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.hostelBlock = hostelBlock;
        this.floor = floor;
        this.status = RoomStatus.AVAILABLE;
        this.amenities = new ArrayList<>();
        this.currentOccupants = new ArrayList<>();
        this.currentOccupancy = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        initializeRoomDefaults();
    }
    
    private void initializeRoomDefaults() {
        switch (roomType) {
            case SINGLE:
                this.capacity = 1;
                this.monthlyRent = 800.0;
                this.area = 12.0;
                break;
            case DOUBLE:
                this.capacity = 2;
                this.monthlyRent = 600.0;
                this.area = 16.0;
                break;
            case TRIPLE:
                this.capacity = 3;
                this.monthlyRent = 450.0;
                this.area = 20.0;
                break;
            case QUAD:
                this.capacity = 4;
                this.monthlyRent = 350.0;
                this.area = 24.0;
                break;
        }
        
        // Default amenities
        amenities.add("Bed");
        amenities.add("Study Table");
        amenities.add("Chair");
        amenities.add("Wardrobe");
        amenities.add("Fan");
        
        this.hasAttachedBathroom = true;
        this.facing = "NORTH";
    }
    
    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE && currentOccupancy < capacity;
    }
    
    public boolean isFull() {
        return currentOccupancy >= capacity;
    }
    
    public void allocateToStudent(String studentId) {
        if (!isAvailable()) {
            throw new IllegalStateException("Room is not available for allocation");
        }
        
        if (currentOccupants.contains(studentId)) {
            throw new IllegalArgumentException("Student is already allocated to this room");
        }
        
        currentOccupants.add(studentId);
        currentOccupancy++;
        
        if (currentOccupancy >= capacity) {
            status = RoomStatus.OCCUPIED;
        }
        
        updatedAt = LocalDateTime.now();
    }
    
    public void deallocateStudent(String studentId) {
        if (!currentOccupants.contains(studentId)) {
            throw new IllegalArgumentException("Student is not allocated to this room");
        }
        
        currentOccupants.remove(studentId);
        currentOccupancy--;
        
        if (currentOccupancy == 0) {
            status = RoomStatus.AVAILABLE;
        } else if (status == RoomStatus.OCCUPIED && currentOccupancy < capacity) {
            status = RoomStatus.AVAILABLE;
        }
        
        updatedAt = LocalDateTime.now();
    }
    
    public void setUnderMaintenance() {
        this.status = RoomStatus.UNDER_MAINTENANCE;
        this.lastMaintenance = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void completeMaintenance() {
        if (currentOccupancy == 0) {
            this.status = RoomStatus.AVAILABLE;
        } else {
            this.status = RoomStatus.OCCUPIED;
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markForCleaning() {
        this.lastCleaned = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addAmenity(String amenity) {
        if (!amenities.contains(amenity)) {
            amenities.add(amenity);
            updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeAmenity(String amenity) {
        if (amenities.remove(amenity)) {
            updatedAt = LocalDateTime.now();
        }
    }
    
    public double calculateMonthlyRentPerPerson() {
        return monthlyRent / capacity;
    }
    
    public double calculateActualRentPerPerson() {
        return currentOccupancy > 0 ? monthlyRent / currentOccupancy : monthlyRent;
    }
    
    public int getAvailableSpaces() {
        return capacity - currentOccupancy;
    }
    
    public void displayRoomInfo() {
        System.out.println("=== ROOM INFORMATION ===");
        System.out.println("Room ID: " + roomId);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Type: " + roomType);
        System.out.println("Status: " + status);
        System.out.println("Hostel Block: " + hostelBlock);
        System.out.println("Floor: " + floor);
        System.out.println("Capacity: " + capacity);
        System.out.println("Current Occupancy: " + currentOccupancy);
        System.out.println("Available Spaces: " + getAvailableSpaces());
        System.out.println("Monthly Rent: $" + String.format("%.2f", monthlyRent));
        System.out.println("Rent per Person: $" + String.format("%.2f", calculateActualRentPerPerson()));
        System.out.println("Area: " + area + " sq.m");
        System.out.println("Facing: " + facing);
        System.out.println("Has Balcony: " + (hasBalcony ? "Yes" : "No"));
        System.out.println("Attached Bathroom: " + (hasAttachedBathroom ? "Yes" : "No"));
        System.out.println("Amenities: " + String.join(", ", amenities));
        System.out.println("Current Occupants: " + currentOccupants.size());
        if (!currentOccupants.isEmpty()) {
            System.out.println("Occupant IDs: " + String.join(", ", currentOccupants));
        }
        System.out.println("Last Cleaned: " + (lastCleaned != null ? lastCleaned : "Never"));
        System.out.println("Last Maintenance: " + (lastMaintenance != null ? lastMaintenance : "Never"));
        System.out.println("Description: " + (description != null ? description : "None"));
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
    }
    
    // Getters and Setters
    @Override
    public String getId() { return roomId; }
    
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { 
        this.roomNumber = roomNumber;
        this.updatedAt = LocalDateTime.now();
    }
    
    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { 
        this.roomType = roomType;
        initializeRoomDefaults(); // Reinitialize defaults for new type
        this.updatedAt = LocalDateTime.now();
    }
    
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getHostelBlock() { return hostelBlock; }
    public void setHostelBlock(String hostelBlock) { 
        this.hostelBlock = hostelBlock;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getFloor() { return floor; }
    public void setFloor(int floor) { 
        this.floor = floor;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { 
        this.capacity = capacity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getCurrentOccupancy() { return currentOccupancy; }
    
    public double getMonthlyRent() { return monthlyRent; }
    public void setMonthlyRent(double monthlyRent) { 
        this.monthlyRent = monthlyRent;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getAmenities() { return new ArrayList<>(amenities); }
    public List<String> getCurrentOccupants() { return new ArrayList<>(currentOccupants); }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getLastCleaned() { return lastCleaned; }
    public LocalDateTime getLastMaintenance() { return lastMaintenance; }
    
    public boolean hasBalcony() { return hasBalcony; }
    public void setHasBalcony(boolean hasBalcony) { 
        this.hasBalcony = hasBalcony;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasAttachedBathroom() { return hasAttachedBathroom; }
    public void setHasAttachedBathroom(boolean hasAttachedBathroom) { 
        this.hasAttachedBathroom = hasAttachedBathroom;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getFacing() { return facing; }
    public void setFacing(String facing) { 
        this.facing = facing;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getArea() { return area; }
    public void setArea(double area) { 
        this.area = area;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomId, room.roomId) || 
               (Objects.equals(roomNumber, room.roomNumber) && Objects.equals(hostelBlock, room.hostelBlock));
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(roomId, roomNumber, hostelBlock);
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType=" + roomType +
                ", status=" + status +
                ", hostelBlock='" + hostelBlock + '\'' +
                ", currentOccupancy=" + currentOccupancy +
                ", capacity=" + capacity +
                '}';
    }
}