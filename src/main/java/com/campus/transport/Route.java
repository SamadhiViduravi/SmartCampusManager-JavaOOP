package com.campus.transport;

import com.campus.utils.Identifiable;
import java.time.LocalTime;
import java.util.*;

/**
 * Route class representing bus routes in the transport system
 */
public class Route implements Identifiable {
    private String routeId;
    private String routeName;
    private String startPoint;
    private String endPoint;
    private List<String> stops;
    private double totalDistance;
    private int estimatedDuration; // in minutes
    private boolean isActive;
    private String routeType; // CIRCULAR, LINEAR, EXPRESS
    private Map<String, LocalTime> stopTimings;
    private List<String> assignedVehicles;
    private double fare;
    private String operatingDays; // MON-FRI, WEEKENDS, DAILY
    private LocalTime firstService;
    private LocalTime lastService;
    private int frequency; // in minutes
    private String description;
    
    public Route(String routeId, String routeName, String startPoint, String endPoint) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.stops = new ArrayList<>();
        this.stopTimings = new HashMap<>();
        this.assignedVehicles = new ArrayList<>();
        this.isActive = true;
        this.routeType = "LINEAR";
        this.operatingDays = "MON-FRI";
        this.frequency = 30; // 30 minutes default
        this.fare = 5.0; // $5 default fare
        
        // Add start and end points as stops
        stops.add(startPoint);
        if (!startPoint.equals(endPoint)) {
            stops.add(endPoint);
        }
        
        calculateDefaults();
    }
    
    private void calculateDefaults() {
        // Estimate duration based on number of stops (5 minutes per stop + travel time)
        this.estimatedDuration = stops.size() * 5 + 30; // Base 30 minutes travel time
        
        // Estimate distance (2 km between stops average)
        this.totalDistance = Math.max(stops.size() * 2.0, 5.0);
        
        // Set default service times
        this.firstService = LocalTime.of(6, 0); // 6:00 AM
        this.lastService = LocalTime.of(22, 0); // 10:00 PM
    }
    
    public void addStop(String stopName) {
        if (!stops.contains(stopName)) {
            // Insert before the last stop (end point) if it's not circular
            if ("CIRCULAR".equals(routeType)) {
                stops.add(stops.size() - 1, stopName);
            } else {
                stops.add(stops.size() - 1, stopName);
            }
            recalculateRoute();
        }
    }
    
    public void removeStop(String stopName) {
        if (!stopName.equals(startPoint) && !stopName.equals(endPoint)) {
            stops.remove(stopName);
            stopTimings.remove(stopName);
            recalculateRoute();
        }
    }
    
    public void addStopTiming(String stopName, LocalTime timing) {
        if (stops.contains(stopName)) {
            stopTimings.put(stopName, timing);
        }
    }
    
    private void recalculateRoute() {
        calculateDefaults();
        generateStopTimings();
    }
    
    private void generateStopTimings() {
        stopTimings.clear();
        LocalTime currentTime = firstService;
        
        for (String stop : stops) {
            stopTimings.put(stop, currentTime);
            currentTime = currentTime.plusMinutes(estimatedDuration / stops.size());
        }
    }
    
    public void assignVehicle(String vehicleId) {
        if (!assignedVehicles.contains(vehicleId)) {
            assignedVehicles.add(vehicleId);
        }
    }
    
    public void unassignVehicle(String vehicleId) {
        assignedVehicles.remove(vehicleId);
    }
    
    public List<LocalTime> getServiceTimes() {
        List<LocalTime> serviceTimes = new ArrayList<>();
        LocalTime currentTime = firstService;
        
        while (!currentTime.isAfter(lastService)) {
            serviceTimes.add(currentTime);
            currentTime = currentTime.plusMinutes(frequency);
        }
        
        return serviceTimes;
    }
    
    public LocalTime getNextService(LocalTime currentTime) {
        List<LocalTime> serviceTimes = getServiceTimes();
        
        return serviceTimes.stream()
                .filter(time -> time.isAfter(currentTime))
                .findFirst()
                .orElse(null); // No more services today
    }
    
    public LocalTime getStopTime(String stopName, LocalTime serviceTime) {
        LocalTime baseTime = stopTimings.get(stopName);
        if (baseTime == null) return null;
        
        // Calculate offset from first service
        long offsetMinutes = baseTime.toSecondOfDay() / 60 - firstService.toSecondOfDay() / 60;
        return serviceTime.plusMinutes(offsetMinutes);
    }
    
    public boolean isOperatingToday(String dayOfWeek) {
        switch (operatingDays) {
            case "DAILY":
                return true;
            case "MON-FRI":
                return !dayOfWeek.equals("SATURDAY") && !dayOfWeek.equals("SUNDAY");
            case "WEEKENDS":
                return dayOfWeek.equals("SATURDAY") || dayOfWeek.equals("SUNDAY");
            default:
                return operatingDays.contains(dayOfWeek);
        }
    }
    
    public double calculateFare(String fromStop, String toStop) {
        int fromIndex = stops.indexOf(fromStop);
        int toIndex = stops.indexOf(toStop);
        
        if (fromIndex == -1 || toIndex == -1) {
            return fare; // Full fare if stops not found
        }
        
        int stopsCount = Math.abs(toIndex - fromIndex);
        return Math.max(fare * 0.5, fare * stopsCount / stops.size()); // Minimum 50% of full fare
    }
    
    public void displayRouteInfo() {
        System.out.println("=== ROUTE INFORMATION ===");
        System.out.println("Route ID: " + routeId);
        System.out.println("Route Name: " + routeName);
        System.out.println("Type: " + routeType);
        System.out.println("Start Point: " + startPoint);
        System.out.println("End Point: " + endPoint);
        System.out.println("Total Distance: " + totalDistance + " km");
        System.out.println("Estimated Duration: " + estimatedDuration + " minutes");
        System.out.println("Number of Stops: " + stops.size());
        System.out.println("Stops: " + String.join(" → ", stops));
        System.out.println("Operating Days: " + operatingDays);
        System.out.println("First Service: " + firstService);
        System.out.println("Last Service: " + lastService);
        System.out.println("Frequency: Every " + frequency + " minutes");
        System.out.println("Base Fare: $" + String.format("%.2f", fare));
        System.out.println("Assigned Vehicles: " + assignedVehicles.size());
        System.out.println("Status: " + (isActive ? "Active" : "Inactive"));
        System.out.println("Description: " + description);
        
        if (!stopTimings.isEmpty()) {
            System.out.println("\nStop Timings (First Service):");
            for (String stop : stops) {
                LocalTime timing = stopTimings.get(stop);
                if (timing != null) {
                    System.out.println("- " + stop + ": " + timing);
                }
            }
        }
    }
    
    public void displaySchedule() {
        System.out.println("=== ROUTE SCHEDULE: " + routeName + " ===");
        List<LocalTime> serviceTimes = getServiceTimes();
        
        System.out.printf("%-15s", "Stop Name");
        for (int i = 0; i < Math.min(serviceTimes.size(), 8); i++) {
            System.out.printf("%-8s", serviceTimes.get(i).toString());
        }
        System.out.println();
        System.out.println("-".repeat(15 + 8 * Math.min(serviceTimes.size(), 8)));
        
        for (String stop : stops) {
            System.out.printf("%-15s", stop.length() > 13 ? stop.substring(0, 13) + ".." : stop);
            for (int i = 0; i < Math.min(serviceTimes.size(), 8); i++) {
                LocalTime stopTime = getStopTime(stop, serviceTimes.get(i));
                System.out.printf("%-8s", stopTime != null ? stopTime.toString() : "--:--");
            }
            System.out.println();
        }
        
        if (serviceTimes.size() > 8) {
            System.out.println("... and " + (serviceTimes.size() - 8) + " more services");
        }
    }
    
    // Getters and Setters
    @Override
    public String getId() { return routeId; }
    
    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }
    
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    
    public String getStartPoint() { return startPoint; }
    public void setStartPoint(String startPoint) { 
        this.startPoint = startPoint;
        recalculateRoute();
    }
    
    public String getEndPoint() { return endPoint; }
    public void setEndPoint(String endPoint) { 
        this.endPoint = endPoint;
        recalculateRoute();
    }
    
    public List<String> getStops() { return new ArrayList<>(stops); }
    
    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
    
    public int getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(int estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getRouteType() { return routeType; }
    public void setRouteType(String routeType) { 
        this.routeType = routeType;
        recalculateRoute();
    }
    
    public Map<String, LocalTime> getStopTimings() { return new HashMap<>(stopTimings); }
    
    public List<String> getAssignedVehicles() { return new ArrayList<>(assignedVehicles); }
    
    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }
    
    public String getOperatingDays() { return operatingDays; }
    public void setOperatingDays(String operatingDays) { this.operatingDays = operatingDays; }
    
    public LocalTime getFirstService() { return firstService; }
    public void setFirstService(LocalTime firstService) { 
        this.firstService = firstService;
        generateStopTimings();
    }
    
    public LocalTime getLastService() { return lastService; }
    public void setLastService(LocalTime lastService) { this.lastService = lastService; }
    
    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(routeId, route.routeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(routeId);
    }
    
    @Override
    public String toString() {
        return "Route{" +
                "routeId='" + routeId + '\'' +
                ", routeName='" + routeName + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", stops=" + stops.size() +
                ", isActive=" + isActive +
                '}';
    }
}