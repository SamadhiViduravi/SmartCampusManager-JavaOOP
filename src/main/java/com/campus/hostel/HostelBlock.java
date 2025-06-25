package com.campus.hostel;

import com.campus.utils.Identifiable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * HostelBlock class representing a hostel building
 */
public class HostelBlock implements Identifiable {
    private String blockId;
    private String blockName;
    private String address;
    private int totalFloors;
    private int totalRooms;
    private int totalCapacity;
    private int currentOccupancy;
    private String wardenId;
    private String assistantWardenId;
    private Map<String, Room> rooms;
    private List<String> facilities;
    private String blockType; // BOYS, GIRLS, MIXED
    private boolean hasElevator;
    private boolean hasWiFi;
    private boolean hasLaundry;
    private boolean hasCafeteria;
    private boolean hasCommonRoom;
    private boolean hasStudyHall;
    private String securityLevel; // LOW, MEDIUM, HIGH
    private LocalDateTime establishedDate;
    private LocalDateTime lastRenovation;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public HostelBlock(String blockId, String blockName, String address, int totalFloors) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.address = address;
        this.totalFloors = totalFloors;
        this.rooms = new HashMap<>();
        this.facilities = new ArrayList<>();
        this.currentOccupancy = 0;
        this.totalCapacity = 0;
        this.totalRooms = 0;
        this.blockType = "MIXED";
        this.securityLevel = "MEDIUM";
        this.establishedDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        initializeDefaultFacilities();
    }
    
    private void initializeDefaultFacilities() {
        facilities.add("24/7 Security");
        facilities.add("Water Supply");
        facilities.add("Electricity Backup");
        facilities.add("Fire Safety");
        facilities.add("CCTV Surveillance");
        facilities.add("Reception Desk");
    }
    
    public void addRoom(Room room) {
        if (room.getHostelBlock().equals(this.blockName)) {
            rooms.put(room.getRoomId(), room);
            totalRooms++;
            totalCapacity += room.getCapacity();
            currentOccupancy += room.getCurrentOccupancy();
            updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Room does not belong to this hostel block");
        }
    }
    
    public void removeRoom(String roomId) {
        Room room = rooms.remove(roomId);
        if (room != null) {
            totalRooms--;
            totalCapacity -= room.getCapacity();
            currentOccupancy -= room.getCurrentOccupancy();
            updatedAt = LocalDateTime.now();
        }
    }
    
    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }
    
    public List<Room> getAvailableRooms() {
        return rooms.values().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }
    
    public List<Room> getRoomsByType(RoomType roomType) {
        return rooms.values().stream()
                .filter(room -> room.getRoomType() == roomType)
                .collect(Collectors.toList());
    }
    
    public List<Room> getRoomsByFloor(int floor) {
        return rooms.values().stream()
                .filter(room -> room.getFloor() == floor)
                .collect(Collectors.toList());
    }
    
    public List<Room> getRoomsByStatus(RoomStatus status) {
        return rooms.values().stream()
                .filter(room -> room.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    public double getOccupancyRate() {
        return totalCapacity > 0 ? (double) currentOccupancy / totalCapacity * 100 : 0;
    }
    
    public int getAvailableCapacity() {
        return totalCapacity - currentOccupancy;
    }
    
    public void updateOccupancy() {
        currentOccupancy = rooms.values().stream()
                .mapToInt(Room::getCurrentOccupancy)
                .sum();
        updatedAt = LocalDateTime.now();
    }
    
    public void addFacility(String facility) {
        if (!facilities.contains(facility)) {
            facilities.add(facility);
            updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeFacility(String facility) {
        if (facilities.remove(facility)) {
            updatedAt = LocalDateTime.now();
        }
    }
    
    public void assignWarden(String wardenId) {
        this.wardenId = wardenId;
        updatedAt = LocalDateTime.now();
    }
    
    public void assignAssistantWarden(String assistantWardenId) {
        this.assistantWardenId = assistantWardenId;
        updatedAt = LocalDateTime.now();
    }
    
    public void setLastRenovation() {
        this.lastRenovation = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    public Map<RoomType, Integer> getRoomTypeDistribution() {
        return rooms.values().stream()
                .collect(Collectors.groupingBy(
                    Room::getRoomType,
                    Collectors.summingInt(room -> 1)
                ));
    }
    
    public Map<RoomStatus, Integer> getRoomStatusDistribution() {
        return rooms.values().stream()
                .collect(Collectors.groupingBy(
                    Room::getStatus,
                    Collectors.summingInt(room -> 1)
                ));
    }
    
    public double getAverageRentPerRoom() {
        return rooms.values().stream()
                .mapToDouble(Room::getMonthlyRent)
                .average()
                .orElse(0.0);
    }
    
    public double getTotalMonthlyRevenue() {
        return rooms.values().stream()
                .filter(room -> room.getCurrentOccupancy() > 0)
                .mapToDouble(Room::getMonthlyRent)
                .sum();
    }
    
    public void displayBlockInfo() {
        System.out.println("=== HOSTEL BLOCK INFORMATION ===");
        System.out.println("Block ID: " + blockId);
        System.out.println("Block Name: " + blockName);
        System.out.println("Address: " + address);
        System.out.println("Type: " + blockType);
        System.out.println("Total Floors: " + totalFloors);
        System.out.println("Total Rooms: " + totalRooms);
        System.out.println("Total Capacity: " + totalCapacity);
        System.out.println("Current Occupancy: " + currentOccupancy);
        System.out.println("Available Capacity: " + getAvailableCapacity());
        System.out.println("Occupancy Rate: " + String.format("%.1f", getOccupancyRate()) + "%");
        System.out.println("Warden ID: " + (wardenId != null ? wardenId : "Not Assigned"));
        System.out.println("Assistant Warden ID: " + (assistantWardenId != null ? assistantWardenId : "Not Assigned"));
        System.out.println("Security Level: " + securityLevel);
        System.out.println("Has Elevator: " + (hasElevator ? "Yes" : "No"));
        System.out.println("Has WiFi: " + (hasWiFi ? "Yes" : "No"));
        System.out.println("Has Laundry: " + (hasLaundry ? "Yes" : "No"));
        System.out.println("Has Cafeteria: " + (hasCafeteria ? "Yes" : "No"));
        System.out.println("Has Common Room: " + (hasCommonRoom ? "Yes" : "No"));
        System.out.println("Has Study Hall: " + (hasStudyHall ? "Yes" : "No"));
        System.out.println("Facilities: " + String.join(", ", facilities));
        System.out.println("Average Rent per Room: $" + String.format("%.2f", getAverageRentPerRoom()));
        System.out.println("Total Monthly Revenue: $" + String.format("%.2f", getTotalMonthlyRevenue()));
        System.out.println("Established: " + establishedDate);
        System.out.println("Last Renovation: " + (lastRenovation != null ? lastRenovation : "Never"));
        System.out.println("Description: " + (description != null ? description : "None"));
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
        
        // Room distribution
        System.out.println("\nRoom Type Distribution:");
        getRoomTypeDistribution().forEach((type, count) -> 
            System.out.println("- " + type + ": " + count + " rooms"));
        
        System.out.println("\nRoom Status Distribution:");
        getRoomStatusDistribution().forEach((status, count) -> 
            System.out.println("- " + status + ": " + count + " rooms"));
    }
    
    public void displayFloorWiseOccupancy() {
        System.out.println("=== FLOOR-WISE OCCUPANCY ===");
        for (int floor = 1; floor <= totalFloors; floor++) {
            List<Room> floorRooms = getRoomsByFloor(floor);
            int floorCapacity = floorRooms.stream().mapToInt(Room::getCapacity).sum();
            int floorOccupancy = floorRooms.stream().mapToInt(Room::getCurrentOccupancy).sum();
            double floorOccupancyRate = floorCapacity > 0 ? (double) floorOccupancy / floorCapacity * 100 : 0;
            
            System.out.printf("Floor %d: %d/%d occupied (%.1f%%) - %d rooms%n", 
                            floor, floorOccupancy, floorCapacity, floorOccupancyRate, floorRooms.size());
        }
    }
    
    // Getters and Setters
    @Override
    public String getId() { return blockId; }
    
    public String getBlockId() { return blockId; }
    public void setBlockId(String blockId) { this.blockId = blockId; }
    
    public String getBlockName() { return blockName; }
    public void setBlockName(String blockName) { 
        this.blockName = blockName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { 
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getTotalFloors() { return totalFloors; }
    public void setTotalFloors(int totalFloors) { 
        this.totalFloors = totalFloors;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getTotalRooms() { return totalRooms; }
    public int getTotalCapacity() { return totalCapacity; }
    public int getCurrentOccupancy() { return currentOccupancy; }
    
    public String getWardenId() { return wardenId; }
    public String getAssistantWardenId() { return assistantWardenId; }
    
    public Map<String, Room> getAllRooms() { return new HashMap<>(rooms); }
    public List<String> getFacilities() { return new ArrayList<>(facilities); }
    
    public String getBlockType() { return blockType; }
    public void setBlockType(String blockType) { 
        this.blockType = blockType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasElevator() { return hasElevator; }
    public void setHasElevator(boolean hasElevator) { 
        this.hasElevator = hasElevator;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasWiFi() { return hasWiFi; }
    public void setHasWiFi(boolean hasWiFi) { 
        this.hasWiFi = hasWiFi;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasLaundry() { return hasLaundry; }
    public void setHasLaundry(boolean hasLaundry) { 
        this.hasLaundry = hasLaundry;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasCafeteria() { return hasCafeteria; }
    public void setHasCafeteria(boolean hasCafeteria) { 
        this.hasCafeteria = hasCafeteria;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasCommonRoom() { return hasCommonRoom; }
    public void setHasCommonRoom(boolean hasCommonRoom) { 
        this.hasCommonRoom = hasCommonRoom;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasStudyHall() { return hasStudyHall; }
    public void setHasStudyHall(boolean hasStudyHall) { 
        this.hasStudyHall = hasStudyHall;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSecurityLevel() { return securityLevel; }
    public void setSecurityLevel(String securityLevel) { 
        this.securityLevel = securityLevel;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getEstablishedDate() { return establishedDate; }
    public void setEstablishedDate(LocalDateTime establishedDate) { 
        this.establishedDate = establishedDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getLastRenovation() { return lastRenovation; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostelBlock that = (HostelBlock) o;
        return Objects.equals(blockId, that.blockId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(blockId);
    }
    
    @Override
    public String toString() {
        return "HostelBlock{" +
                "blockId='" + blockId + '\'' +
                ", blockName='" + blockName + '\'' +
                ", totalRooms=" + totalRooms +
                ", currentOccupancy=" + currentOccupancy +
                ", totalCapacity=" + totalCapacity +
                ", occupancyRate=" + String.format("%.1f", getOccupancyRate()) + "%" +
                '}';
    }
}