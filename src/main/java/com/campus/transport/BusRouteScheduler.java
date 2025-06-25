package com.campus.transport;

import com.campus.utils.Logger;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton class for managing bus route scheduling
 * Demonstrates Singleton pattern and complex scheduling algorithms
 */
public class BusRouteScheduler {
    private static BusRouteScheduler instance;
    private static final Logger logger = Logger.getInstance();
    
    private Map<String, Route> routes;
    private Map<String, List<String>> routeVehicleAssignments;
    private Map<String, String> vehicleDriverAssignments;
    private Map<String, List<ScheduleEntry>> dailySchedules;
    private LocalDateTime lastScheduleUpdate;
    
    private BusRouteScheduler() {
        this.routes = new HashMap<>();
        this.routeVehicleAssignments = new HashMap<>();
        this.vehicleDriverAssignments = new HashMap<>();
        this.dailySchedules = new HashMap<>();
        this.lastScheduleUpdate = LocalDateTime.now();
        logger.log("BusRouteScheduler initialized");
    }
    
    public static synchronized BusRouteScheduler getInstance() {
        if (instance == null) {
            instance = new BusRouteScheduler();
        }
        return instance;
    }
    
    public void addRoute(Route route) {
        routes.put(route.getRouteId(), route);
        routeVehicleAssignments.put(route.getRouteId(), new ArrayList<>());
        generateRouteSchedule(route.getRouteId());
        logger.log("Route added to scheduler: " + route.getRouteId());
    }
    
    public void removeRoute(String routeId) {
        Route route = routes.remove(routeId);
        if (route != null) {
            routeVehicleAssignments.remove(routeId);
            dailySchedules.remove(routeId);
            logger.log("Route removed from scheduler: " + routeId);
        }
    }
    
    public void assignVehicleToRoute(String routeId, String vehicleId) {
        if (routes.containsKey(routeId)) {
            List<String> vehicles = routeVehicleAssignments.get(routeId);
            if (!vehicles.contains(vehicleId)) {
                vehicles.add(vehicleId);
                generateRouteSchedule(routeId);
                logger.log("Vehicle " + vehicleId + " assigned to route " + routeId);
            }
        }
    }
    
    public void unassignVehicleFromRoute(String routeId, String vehicleId) {
        List<String> vehicles = routeVehicleAssignments.get(routeId);
        if (vehicles != null && vehicles.remove(vehicleId)) {
            generateRouteSchedule(routeId);
            logger.log("Vehicle " + vehicleId + " unassigned from route " + routeId);
        }
    }
    
    public void assignDriverToVehicle(String vehicleId, String driverId) {
        vehicleDriverAssignments.put(vehicleId, driverId);
        logger.log("Driver " + driverId + " assigned to vehicle " + vehicleId);
    }
    
    public void unassignDriverFromVehicle(String vehicleId) {
        String driverId = vehicleDriverAssignments.remove(vehicleId);
        if (driverId != null) {
            logger.log("Driver " + driverId + " unassigned from vehicle " + vehicleId);
        }
    }
    
    private void generateRouteSchedule(String routeId) {
        Route route = routes.get(routeId);
        List<String> assignedVehicles = routeVehicleAssignments.get(routeId);
        
        if (route == null || assignedVehicles.isEmpty()) {
            return;
        }
        
        List<ScheduleEntry> scheduleEntries = new ArrayList<>();
        List<LocalTime> serviceTimes = route.getServiceTimes();
        
        // Distribute service times among available vehicles
        for (int i = 0; i < serviceTimes.size(); i++) {
            String vehicleId = assignedVehicles.get(i % assignedVehicles.size());
            String driverId = vehicleDriverAssignments.get(vehicleId);
            
            ScheduleEntry entry = new ScheduleEntry(
                route.getRouteId(),
                vehicleId,
                driverId,
                serviceTimes.get(i),
                route.getEstimatedDuration()
            );
            
            scheduleEntries.add(entry);
        }
        
        dailySchedules.put(routeId, scheduleEntries);
        lastScheduleUpdate = LocalDateTime.now();
    }
    
    public void generateAllSchedules() {
        for (String routeId : routes.keySet()) {
            generateRouteSchedule(routeId);
        }
        logger.log("All route schedules regenerated");
    }
    
    public List<ScheduleEntry> getRouteSchedule(String routeId) {
        return dailySchedules.getOrDefault(routeId, new ArrayList<>());
    }
    
    public List<ScheduleEntry> getVehicleSchedule(String vehicleId) {
        return dailySchedules.values().stream()
                .flatMap(List::stream)
                .filter(entry -> vehicleId.equals(entry.getVehicleId()))
                .sorted(Comparator.comparing(ScheduleEntry::getDepartureTime))
                .collect(Collectors.toList());
    }
    
    public List<ScheduleEntry> getDriverSchedule(String driverId) {
        return dailySchedules.values().stream()
                .flatMap(List::stream)
                .filter(entry -> driverId.equals(entry.getDriverId()))
                .sorted(Comparator.comparing(ScheduleEntry::getDepartureTime))
                .collect(Collectors.toList());
    }
    
    public ScheduleEntry getNextService(String routeId, LocalTime currentTime) {
        List<ScheduleEntry> schedule = getRouteSchedule(routeId);
        
        return schedule.stream()
                .filter(entry -> entry.getDepartureTime().isAfter(currentTime))
                .findFirst()
                .orElse(null);
    }
    
    public List<ScheduleEntry> getCurrentlyRunningServices() {
        LocalTime now = LocalTime.now();
        
        return dailySchedules.values().stream()
                .flatMap(List::stream)
                .filter(entry -> {
                    LocalTime start = entry.getDepartureTime();
                    LocalTime end = start.plusMinutes(entry.getDurationMinutes());
                    return !now.isBefore(start) && now.isBefore(end);
                })
                .collect(Collectors.toList());
    }
    
    public boolean hasScheduleConflict(String vehicleId, LocalTime startTime, int durationMinutes) {
        List<ScheduleEntry> vehicleSchedule = getVehicleSchedule(vehicleId);
        LocalTime endTime = startTime.plusMinutes(durationMinutes);
        
        return vehicleSchedule.stream()
                .anyMatch(entry -> {
                    LocalTime entryStart = entry.getDepartureTime();
                    LocalTime entryEnd = entryStart.plusMinutes(entry.getDurationMinutes());
                    
                    return !(endTime.isBefore(entryStart) || startTime.isAfter(entryEnd));
                });
    }
    
    public void optimizeSchedules() {
        System.out.println("=== OPTIMIZING SCHEDULES ===");
        
        for (String routeId : routes.keySet()) {
            Route route = routes.get(routeId);
            List<String> vehicles = routeVehicleAssignments.get(routeId);
            
            if (vehicles.size() > 1) {
                // Optimize vehicle distribution
                optimizeVehicleDistribution(routeId, route, vehicles);
            }
        }
        
        generateAllSchedules();
        System.out.println("Schedule optimization completed");
        logger.log("Schedules optimized");
    }
    
    private void optimizeVehicleDistribution(String routeId, Route route, List<String> vehicles) {
        List<LocalTime> serviceTimes = route.getServiceTimes();
        int optimalVehicleCount = Math.min(vehicles.size(), 
                                         serviceTimes.size() / 2); // Don't over-assign
        
        if (vehicles.size() > optimalVehicleCount) {
            System.out.println("Route " + routeId + " has excess vehicles. Consider redistribution.");
        }
    }
    
    public void displayAllSchedules() {
        System.out.println("=== ALL ROUTE SCHEDULES ===");
        System.out.println("Last Updated: " + lastScheduleUpdate);
        System.out.println();
        
        for (String routeId : routes.keySet()) {
            displayRouteSchedule(routeId);
            System.out.println();
        }
    }
    
    public void displayRouteSchedule(String routeId) {
        Route route = routes.get(routeId);
        List<ScheduleEntry> schedule = getRouteSchedule(routeId);
        
        if (route == null || schedule.isEmpty()) {
            System.out.println("No schedule available for route: " + routeId);
            return;
        }
        
        System.out.println("=== SCHEDULE FOR ROUTE: " + route.getRouteName() + " ===");
        System.out.printf("%-12s %-12s %-12s %-12s %-15s%n", 
                         "Departure", "Vehicle", "Driver", "Duration", "Status");
        System.out.println("-".repeat(70));
        
        LocalTime now = LocalTime.now();
        for (ScheduleEntry entry : schedule) {
            String status = getEntryStatus(entry, now);
            String driverDisplay = entry.getDriverId() != null ? entry.getDriverId() : "Unassigned";
            
            System.out.printf("%-12s %-12s %-12s %-12d %-15s%n",
                             entry.getDepartureTime(),
                             entry.getVehicleId(),
                             driverDisplay,
                             entry.getDurationMinutes(),
                             status);
        }
    }
    
    private String getEntryStatus(ScheduleEntry entry, LocalTime now) {
        LocalTime start = entry.getDepartureTime();
        LocalTime end = start.plusMinutes(entry.getDurationMinutes());
        
        if (now.isBefore(start)) {
            return "Scheduled";
        } else if (!now.isBefore(start) && now.isBefore(end)) {
            return "In Progress";
        } else {
            return "Completed";
        }
    }
    
    public void displaySystemStatistics() {
        System.out.println("=== SCHEDULER STATISTICS ===");
        System.out.println("Total Routes: " + routes.size());
        System.out.println("Total Vehicle Assignments: " + 
                          routeVehicleAssignments.values().stream()
                                  .mapToInt(List::size).sum());
        System.out.println("Total Driver Assignments: " + vehicleDriverAssignments.size());
        System.out.println("Active Routes: " + routes.values().stream()
                                                    .mapToLong(route -> route.isActive() ? 1 : 0)
                                                    .sum());
        System.out.println("Currently Running Services: " + getCurrentlyRunningServices().size());
        System.out.println("Last Schedule Update: " + lastScheduleUpdate);
        
        // Route utilization
        System.out.println("\nRoute Utilization:");
        for (Map.Entry<String, List<String>> entry : routeVehicleAssignments.entrySet()) {
            Route route = routes.get(entry.getKey());
            if (route != null) {
                int vehicleCount = entry.getValue().size();
                int serviceCount = route.getServiceTimes().size();
                double utilization = vehicleCount > 0 ? (double) serviceCount / vehicleCount : 0;
                System.out.printf("- %s: %.1f services per vehicle%n", 
                                route.getRouteName(), utilization);
            }
        }
    }
    
    // Getters
    public Map<String, Route> getAllRoutes() { return new HashMap<>(routes); }
    public Map<String, List<String>> getRouteVehicleAssignments() { return new HashMap<>(routeVehicleAssignments); }
    public Map<String, String> getVehicleDriverAssignments() { return new HashMap<>(vehicleDriverAssignments); }
    public LocalDateTime getLastScheduleUpdate() { return lastScheduleUpdate; }
    
    // Inner class for schedule entries
    public static class ScheduleEntry {
        private String routeId;
        private String vehicleId;
        private String driverId;
        private LocalTime departureTime;
        private int durationMinutes;
        private String status;
        
        public ScheduleEntry(String routeId, String vehicleId, String driverId, 
                           LocalTime departureTime, int durationMinutes) {
            this.routeId = routeId;
            this.vehicleId = vehicleId;
            this.driverId = driverId;
            this.departureTime = departureTime;
            this.durationMinutes = durationMinutes;
            this.status = "SCHEDULED";
        }
        
        // Getters and Setters
        public String getRouteId() { return routeId; }
        public String getVehicleId() { return vehicleId; }
        public String getDriverId() { return driverId; }
        public LocalTime getDepartureTime() { return departureTime; }
        public int getDurationMinutes() { return durationMinutes; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        @Override
        public String toString() {
            return "ScheduleEntry{" +
                    "routeId='" + routeId + '\'' +
                    ", vehicleId='" + vehicleId + '\'' +
                    ", driverId='" + driverId + '\'' +
                    ", departureTime=" + departureTime +
                    ", durationMinutes=" + durationMinutes +
                    '}';
        }
    }
}