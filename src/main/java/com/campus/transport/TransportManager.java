package com.campus.transport;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Manager class for handling all transport operations
 */
public class TransportManager implements Manageable<Vehicle> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    private Map<String, Vehicle> vehicles;
    private Map<String, Route> routes;
    private Map<String, Driver> drivers;
    private Map<String, MaintenanceRecord> maintenanceRecords;
    private NotificationService notificationService;
    private BusRouteScheduler scheduler;
    private int maintenanceCounter;

    public TransportManager() {
        this.vehicles = new HashMap<>();
        this.routes = new HashMap<>();
        this.drivers = new HashMap<>();
        this.maintenanceRecords = new HashMap<>();
        this.notificationService = NotificationService.getInstance();
        this.scheduler = new BusRouteScheduler();
        this.maintenanceCounter = 1;
        initializeSampleData();
        logger.log("TransportManager initialized");
    }

    private void initializeSampleData() {
        // Create sample drivers
        Driver driver1 = new Driver("D001", "John", "Smith", "DL123456");
        driver1.setPhone("+1-555-0201");
        driver1.setExperience(5);
        drivers.put("D001", driver1);

        Driver driver2 = new Driver("D002", "Mike", "Johnson", "DL789012");
        driver2.setPhone("+1-555-0202");
        driver2.setExperience(8);
        drivers.put("D002", driver2);

        // Create sample routes
        Route route1 = new Route("R001", "Campus to Downtown", "Main campus to city center");
        route1.addStop("Main Gate");
        route1.addStop("Library");
        route1.addStop("Student Center");
        route1.addStop("Downtown Terminal");
        route1.setDistance(15.5);
        route1.setEstimatedDuration(45);
        routes.put("R001", route1);

        Route route2 = new Route("R002", "Campus Circle", "Internal campus route");
        route2.addStop("Dormitory A");
        route2.addStop("Academic Building");
        route2.addStop("Cafeteria");
        route2.addStop("Sports Complex");
        route2.setDistance(8.0);
        route2.setEstimatedDuration(25);
        routes.put("R002", route2);

        // Create sample vehicles
        Bus bus1 = new Bus("V001", "Ford Transit", "ABC123", 2020);
        bus1.setCapacity(40);
        bus1.setFuelType("Diesel");
        bus1.setMileage(45000);
        bus1.setStatus(VehicleStatus.ACTIVE);
        bus1.setAssignedRoute("R001");
        bus1.setAssignedDriver("D001");
        vehicles.put("V001", bus1);

        Bus bus2 = new Bus("V002", "Mercedes Sprinter", "XYZ789", 2019);
        bus2.setCapacity(25);
        bus2.setFuelType("Diesel");
        bus2.setMileage(52000);
        bus2.setStatus(VehicleStatus.ACTIVE);
        bus2.setAssignedRoute("R002");
        bus2.setAssignedDriver("D002");
        vehicles.put("V002", bus2);

        Van van1 = new Van("V003", "Toyota Hiace", "DEF456", 2021);
        van1.setCapacity(12);
        van1.setFuelType("Gasoline");
        van1.setMileage(28000);
        van1.setStatus(VehicleStatus.ACTIVE);
        vehicles.put("V003", van1);

        // Create sample maintenance records
        MaintenanceRecord maintenance1 = new MaintenanceRecord("M001", "V001", LocalDate.now().minusDays(30));
        maintenance1.setMaintenanceType("Regular Service");
        maintenance1.setDescription("Oil change, brake inspection, tire rotation");
        maintenance1.setCost(250.00);
        maintenance1.setStatus("Completed");
        maintenanceRecords.put("M001", maintenance1);

        logger.log("Sample transport data initialized");
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== TRANSPORT MANAGEMENT MENU ===");
            System.out.println("1. Vehicle Management");
            System.out.println("2. Route Management");
            System.out.println("3. Driver Management");
            System.out.println("4. Schedule Management");
            System.out.println("5. Maintenance Management");
            System.out.println("6. Transport Reports");
            System.out.println("7. Transport Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getChoice();

            switch (choice) {
                case 1: vehicleManagementMenu(); break;
                case 2: routeManagementMenu(); break;
                case 3: driverManagementMenu(); break;
                case 4: scheduleManagementMenu(); break;
                case 5: maintenanceManagementMenu(); break;
                case 6: transportReportsMenu(); break;
                case 7: displayTransportStatistics(); break;
                case 0: return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void create(Vehicle vehicle) {
        vehicles.put(vehicle.getVehicleId(), vehicle);
        notificationService.notifyObservers("New vehicle added: " + vehicle.getModel());
        logger.log("Vehicle created: " + vehicle.getVehicleId());
    }

    @Override
    public Vehicle read(String vehicleId) {
        return vehicles.get(vehicleId);
    }

    @Override
    public void update(Vehicle vehicle) {
        vehicles.put(vehicle.getVehicleId(), vehicle);
        notificationService.notifyObservers("Vehicle updated: " + vehicle.getModel());
        logger.log("Vehicle updated: " + vehicle.getVehicleId());
    }

    @Override
    public void delete(String vehicleId) {
        Vehicle vehicle = vehicles.remove(vehicleId);
        if (vehicle != null) {
            notificationService.notifyObservers("Vehicle deleted: " + vehicle.getModel());
            logger.log("Vehicle deleted: " + vehicleId);
        }
    }

    @Override
    public List<Vehicle> getAll() {
        return new ArrayList<>(vehicles.values());
    }

    // Utility methods for external access
    public int getTotalVehicles() {
        return vehicles.size();
    }

    public int getActiveVehicles() {
        return (int) vehicles.values().stream()
                .filter(vehicle -> vehicle.getStatus() == VehicleStatus.ACTIVE)
                .count();
    }

    public int getInactiveVehicles() {
        return (int) vehicles.values().stream()
                .filter(vehicle -> vehicle.getStatus() == VehicleStatus.INACTIVE)
                .count();
    }

    public int getMaintenanceVehicles() {
        return (int) vehicles.values().stream()
                .filter(vehicle -> vehicle.getStatus() == VehicleStatus.MAINTENANCE)
                .count();
    }

    public int getTotalRoutes() {
        return routes.size();
    }

    public int getTotalDrivers() {
        return drivers.size();
    }

    private void vehicleManagementMenu() {
        System.out.println("\n=== VEHICLE MANAGEMENT ===");
        System.out.println("1. Add New Vehicle");
        System.out.println("2. View All Vehicles");
        System.out.println("3. Update Vehicle");
        System.out.println("4. Delete Vehicle");
        System.out.println("5. Vehicle Details");
        System.out.println("6. Assign Route to Vehicle");
        System.out.println("7. Assign Driver to Vehicle");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: addNewVehicleInteractive(); break;
            case 2: viewAllVehicles(); break;
            case 3: updateVehicleInteractive(); break;
            case 4: deleteVehicleInteractive(); break;
            case 5: viewVehicleDetailsInteractive(); break;
            case 6: assignRouteToVehicleInteractive(); break;
            case 7: assignDriverToVehicleInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void addNewVehicleInteractive() {
        System.out.println("\n=== ADD NEW VEHICLE ===");

        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        if (vehicles.containsKey(vehicleId)) {
            System.out.println("Vehicle ID already exists.");
            return;
        }

        System.out.println("Select Vehicle Type:");
        System.out.println("1. Bus");
        System.out.println("2. Van");
        System.out.print("Enter choice: ");

        int typeChoice = getChoice();
        VehicleType vehicleType = typeChoice == 1 ? VehicleType.BUS : VehicleType.VAN;

        System.out.print("Enter Model: ");
        String model = scanner.nextLine();

        System.out.print("Enter License Plate: ");
        String licensePlate = scanner.nextLine();

        System.out.print("Enter Year: ");
        int year;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid year.");
            return;
        }

        Vehicle newVehicle;
        if (vehicleType == VehicleType.BUS) {
            newVehicle = new Bus(vehicleId, model, licensePlate, year);
        } else {
            newVehicle = new Van(vehicleId, model, licensePlate, year);
        }

        System.out.print("Enter Capacity: ");
        try {
            int capacity = Integer.parseInt(scanner.nextLine());
            newVehicle.setCapacity(capacity);
        } catch (NumberFormatException e) {
            System.out.println("Invalid capacity, using default.");
        }

        System.out.print("Enter Fuel Type: ");
        String fuelType = scanner.nextLine();
        if (!fuelType.trim().isEmpty()) {
            newVehicle.setFuelType(fuelType);
        }

        System.out.print("Enter Current Mileage: ");
        try {
            int mileage = Integer.parseInt(scanner.nextLine());
            newVehicle.setMileage(mileage);
        } catch (NumberFormatException e) {
            System.out.println("Invalid mileage, using default (0).");
        }

        create(newVehicle);
        System.out.println("Vehicle added successfully!");
        newVehicle.displayVehicleInfo();
    }

    private void viewAllVehicles() {
        System.out.println("\n=== ALL VEHICLES ===");
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.printf("%-8s %-8s %-20s %-12s %-8s %-10s %-12s %-10s%n",
                "ID", "Type", "Model", "License", "Year", "Capacity", "Status", "Route");
        System.out.println("-".repeat(90));

        vehicles.values().stream()
                .sorted((v1, v2) -> v1.getVehicleId().compareTo(v2.getVehicleId()))
                .forEach(vehicle -> {
                    System.out.printf("%-8s %-8s %-20s %-12s %-8d %-10d %-12s %-10s%n",
                            vehicle.getVehicleId(),
                            vehicle.getVehicleType(),
                            vehicle.getModel().length() > 18 ? vehicle.getModel().substring(0, 18) + ".." : vehicle.getModel(),
                            vehicle.getLicensePlate(),
                            vehicle.getYear(),
                            vehicle.getCapacity(),
                            vehicle.getStatus(),
                            vehicle.getAssignedRoute() != null ? vehicle.getAssignedRoute() : "None");
                });

        System.out.println("-".repeat(90));
        System.out.println("Total Vehicles: " + vehicles.size());
        System.out.println("Active: " + getActiveVehicles());
        System.out.println("Maintenance: " + getMaintenanceVehicles());
    }

    private void updateVehicleInteractive() {
        System.out.println("\n=== UPDATE VEHICLE ===");
        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = read(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.println("Current vehicle information:");
        vehicle.displayVehicleInfo();

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Model");
        System.out.println("2. License Plate");
        System.out.println("3. Capacity");
        System.out.println("4. Fuel Type");
        System.out.println("5. Mileage");
        System.out.println("6. Status");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                System.out.print("Enter new model: ");
                vehicle.setModel(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new license plate: ");
                vehicle.setLicensePlate(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new capacity: ");
                try {
                    vehicle.setCapacity(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid capacity.");
                    return;
                }
                break;
            case 4:
                System.out.print("Enter new fuel type: ");
                vehicle.setFuelType(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter new mileage: ");
                try {
                    vehicle.setMileage(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid mileage.");
                    return;
                }
                break;
            case 6:
                System.out.println("Select new status:");
                VehicleStatus[] statuses = VehicleStatus.values();
                for (int i = 0; i < statuses.length; i++) {
                    System.out.println((i + 1) + ". " + statuses[i]);
                }
                System.out.print("Enter choice: ");
                int statusChoice = getChoice();
                if (statusChoice >= 1 && statusChoice <= statuses.length) {
                    vehicle.setStatus(statuses[statusChoice - 1]);
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        update(vehicle);
        System.out.println("Vehicle updated successfully!");
    }

    private void deleteVehicleInteractive() {
        System.out.println("\n=== DELETE VEHICLE ===");
        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = read(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.println("Vehicle to be deleted:");
        vehicle.displayVehicleInfo();

        System.out.print("Are you sure you want to delete this vehicle? (yes/no): ");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation)) {
            delete(vehicleId);
            System.out.println("Vehicle deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void viewVehicleDetailsInteractive() {
        System.out.println("\n=== VEHICLE DETAILS ===");
        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = read(vehicleId);
        if (vehicle != null) {
            vehicle.displayVehicleInfo();

            // Show maintenance history
            List<MaintenanceRecord> vehicleMaintenanceRecords = maintenanceRecords.values().stream()
                    .filter(record -> record.getVehicleId().equals(vehicleId))
                    .sorted((r1, r2) -> r2.getMaintenanceDate().compareTo(r1.getMaintenanceDate()))
                    .collect(Collectors.toList());

            if (!vehicleMaintenanceRecords.isEmpty()) {
                System.out.println("\nMAINTENANCE HISTORY:");
                vehicleMaintenanceRecords.stream().limit(5).forEach(record -> {
                    System.out.println("- " + record.getMaintenanceDate() + ": " +
                            record.getMaintenanceType() + " ($" + record.getCost() + ")");
                });
            }
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    private void assignRouteToVehicleInteractive() {
        System.out.println("\n=== ASSIGN ROUTE TO VEHICLE ===");

        if (routes.isEmpty()) {
            System.out.println("No routes available. Please create routes first.");
            return;
        }

        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = read(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.println("Available Routes:");
        routes.values().forEach(route ->
                System.out.println("- " + route.getRouteId() + ": " + route.getRouteName()));

        System.out.print("Enter Route ID: ");
        String routeId = scanner.nextLine();

        if (routes.containsKey(routeId)) {
            vehicle.setAssignedRoute(routeId);
            update(vehicle);
            System.out.println("Route assigned successfully!");
        } else {
            System.out.println("Route not found.");
        }
    }

    private void assignDriverToVehicleInteractive() {
        System.out.println("\n=== ASSIGN DRIVER TO VEHICLE ===");

        if (drivers.isEmpty()) {
            System.out.println("No drivers available. Please add drivers first.");
            return;
        }

        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = read(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.println("Available Drivers:");
        drivers.values().forEach(driver ->
                System.out.println("- " + driver.getDriverId() + ": " + driver.getFullName()));

        System.out.print("Enter Driver ID: ");
        String driverId = scanner.nextLine();

        if (drivers.containsKey(driverId)) {
            vehicle.setAssignedDriver(driverId);
            update(vehicle);
            System.out.println("Driver assigned successfully!");
        } else {
            System.out.println("Driver not found.");
        }
    }

    private void routeManagementMenu() {
        System.out.println("\n=== ROUTE MANAGEMENT ===");
        System.out.println("1. Add New Route");
        System.out.println("2. View All Routes");
        System.out.println("3. Update Route");
        System.out.println("4. Delete Route");
        System.out.println("5. Route Details");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: addNewRouteInteractive(); break;
            case 2: viewAllRoutes(); break;
            case 3: updateRouteInteractive(); break;
            case 4: deleteRouteInteractive(); break;
            case 5: viewRouteDetailsInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void addNewRouteInteractive() {
        System.out.println("\n=== ADD NEW ROUTE ===");

        System.out.print("Enter Route ID: ");
        String routeId = scanner.nextLine();

        if (routes.containsKey(routeId)) {
            System.out.println("Route ID already exists.");
            return;
        }

        System.out.print("Enter Route Name: ");
        String routeName = scanner.nextLine();

        System.out.print("Enter Route Description: ");
        String description = scanner.nextLine();

        Route newRoute = new Route(routeId, routeName, description);

        System.out.println("Enter stops (type 'done' when finished):");
        while (true) {
            System.out.print("Stop name: ");
            String stop = scanner.nextLine();
            if ("done".equalsIgnoreCase(stop)) break;
            newRoute.addStop(stop);
        }

        System.out.print("Enter total distance (km): ");
        try {
            double distance = Double.parseDouble(scanner.nextLine());
            newRoute.setDistance(distance);
        } catch (NumberFormatException e) {
            System.out.println("Invalid distance, using default.");
        }

        System.out.print("Enter estimated duration (minutes): ");
        try {
            int duration = Integer.parseInt(scanner.nextLine());
            newRoute.setEstimatedDuration(duration);
        } catch (NumberFormatException e) {
            System.out.println("Invalid duration, using default.");
        }

        routes.put(routeId, newRoute);
        System.out.println("Route added successfully!");
        newRoute.displayRouteInfo();
    }

    private void viewAllRoutes() {
        System.out.println("\n=== ALL ROUTES ===");
        if (routes.isEmpty()) {
            System.out.println("No routes found.");
            return;
        }

        System.out.printf("%-8s %-25s %-10s %-10s %-8s%n",
                "ID", "Route Name", "Distance", "Duration", "Stops");
        System.out.println("-".repeat(65));

        routes.values().stream()
                .sorted((r1, r2) -> r1.getRouteId().compareTo(r2.getRouteId()))
                .forEach(route -> {
                    System.out.printf("%-8s %-25s %-10.1f %-10d %-8d%n",
                            route.getRouteId(),
                            route.getRouteName().length() > 23 ? route.getRouteName().substring(0, 23) + ".." : route.getRouteName(),
                            route.getDistance(),
                            route.getEstimatedDuration(),
                            route.getStops().size());
                });

        System.out.println("-".repeat(65));
        System.out.println("Total Routes: " + routes.size());
    }

    private void updateRouteInteractive() {
        System.out.println("\n=== UPDATE ROUTE ===");
        System.out.print("Enter Route ID: ");
        String routeId = scanner.nextLine();

        Route route = routes.get(routeId);
        if (route == null) {
            System.out.println("Route not found.");
            return;
        }

        System.out.println("Current route information:");
        route.displayRouteInfo();

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Route Name");
        System.out.println("2. Description");
        System.out.println("3. Distance");
        System.out.println("4. Duration");
        System.out.println("5. Add Stop");
        System.out.println("6. Remove Stop");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                System.out.print("Enter new route name: ");
                route.setRouteName(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new description: ");
                route.setDescription(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new distance (km): ");
                try {
                    route.setDistance(Double.parseDouble(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid distance.");
                    return;
                }
                break;
            case 4:
                System.out.print("Enter new duration (minutes): ");
                try {
                    route.setEstimatedDuration(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid duration.");
                    return;
                }
                break;
            case 5:
                System.out.print("Enter stop name to add: ");
                route.addStop(scanner.nextLine());
                break;
            case 6:
                System.out.println("Current stops:");
                route.getStops().forEach(stop -> System.out.println("- " + stop));
                System.out.print("Enter stop name to remove: ");
                route.removeStop(scanner.nextLine());
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("Route updated successfully!");
    }

    private void deleteRouteInteractive() {
        System.out.println("\n=== DELETE ROUTE ===");
        System.out.print("Enter Route ID: ");
        String routeId = scanner.nextLine();

        Route route = routes.get(routeId);
        if (route == null) {
            System.out.println("Route not found.");
            return;
        }

        // Check if route is assigned to any vehicle
        boolean isAssigned = vehicles.values().stream()
                .anyMatch(vehicle -> routeId.equals(vehicle.getAssignedRoute()));

        if (isAssigned) {
            System.out.println("Cannot delete route. It is assigned to one or more vehicles.");
            return;
        }

        System.out.println("Route to be deleted:");
        route.displayRouteInfo();

        System.out.print("Are you sure you want to delete this route? (yes/no): ");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation)) {
            routes.remove(routeId);
            System.out.println("Route deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void viewRouteDetailsInteractive() {
        System.out.println("\n=== ROUTE DETAILS ===");
        System.out.print("Enter Route ID: ");
        String routeId = scanner.nextLine();

        Route route = routes.get(routeId);
        if (route != null) {
            route.displayRouteInfo();

            // Show assigned vehicles
            List<Vehicle> assignedVehicles = vehicles.values().stream()
                    .filter(vehicle -> routeId.equals(vehicle.getAssignedRoute()))
                    .collect(Collectors.toList());

            if (!assignedVehicles.isEmpty()) {
                System.out.println("\nASSIGNED VEHICLES:");
                assignedVehicles.forEach(vehicle ->
                        System.out.println("- " + vehicle.getVehicleId() + ": " + vehicle.getModel()));
            }
        } else {
            System.out.println("Route not found.");
        }
    }

    private void driverManagementMenu() {
        System.out.println("\n=== DRIVER MANAGEMENT ===");
        System.out.println("1. Add New Driver");
        System.out.println("2. View All Drivers");
        System.out.println("3. Update Driver");
        System.out.println("4. Delete Driver");
        System.out.println("5. Driver Details");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: addNewDriverInteractive(); break;
            case 2: viewAllDrivers(); break;
            case 3: updateDriverInteractive(); break;
            case 4: deleteDriverInteractive(); break;
            case 5: viewDriverDetailsInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void addNewDriverInteractive() {
        System.out.println("\n=== ADD NEW DRIVER ===");

        System.out.print("Enter Driver ID: ");
        String driverId = scanner.nextLine();

        if (drivers.containsKey(driverId)) {
            System.out.println("Driver ID already exists.");
            return;
        }

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter License Number: ");
        String licenseNumber = scanner.nextLine();

        Driver newDriver = new Driver(driverId, firstName, lastName, licenseNumber);

        System.out.print("Enter Phone (optional): ");
        String phone = scanner.nextLine();
        if (!phone.trim().isEmpty()) {
            newDriver.setPhone(phone);
        }

        System.out.print("Enter Years of Experience: ");
        try {
            int experience = Integer.parseInt(scanner.nextLine());
            newDriver.setExperience(experience);
        } catch (NumberFormatException e) {
            System.out.println("Invalid experience, using default (0).");
        }

        drivers.put(driverId, newDriver);
        System.out.println("Driver added successfully!");
        newDriver.displayDriverInfo();
    }

    private void viewAllDrivers() {
        System.out.println("\n=== ALL DRIVERS ===");
        if (drivers.isEmpty()) {
            System.out.println("No drivers found.");
            return;
        }

        System.out.printf("%-8s %-20s %-15s %-15s %-10s%n",
                "ID", "Name", "License", "Phone", "Experience");
        System.out.println("-".repeat(75));

        drivers.values().stream()
                .sorted((d1, d2) -> d1.getDriverId().compareTo(d2.getDriverId()))
                .forEach(driver -> {
                    System.out.printf("%-8s %-20s %-15s %-15s %-10d%n",
                            driver.getDriverId(),
                            driver.getFullName().length() > 18 ? driver.getFullName().substring(0, 18) + ".." : driver.getFullName(),
                            driver.getLicenseNumber(),
                            driver.getPhone() != null ? driver.getPhone() : "N/A",
                            driver.getExperience());
                });

        System.out.println("-".repeat(75));
        System.out.println("Total Drivers: " + drivers.size());
    }

    private void updateDriverInteractive() {
        System.out.println("\n=== UPDATE DRIVER ===");
        System.out.print("Enter Driver ID: ");
        String driverId = scanner.nextLine();

        Driver driver = drivers.get(driverId);
        if (driver == null) {
            System.out.println("Driver not found.");
            return;
        }

        System.out.println("Current driver information:");
        driver.displayDriverInfo();

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. License Number");
        System.out.println("3. Phone");
        System.out.println("4. Experience");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                System.out.print("Enter new first name: ");
                String firstName = scanner.nextLine();
                System.out.print("Enter new last name: ");
                String lastName = scanner.nextLine();
                driver.setFirstName(firstName);
                driver.setLastName(lastName);
                break;
            case 2:
                System.out.print("Enter new license number: ");
                driver.setLicenseNumber(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new phone: ");
                driver.setPhone(scanner.nextLine());
                break;
            case 4:
                System.out.print("Enter new experience (years): ");
                try {
                    driver.setExperience(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid experience.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("Driver updated successfully!");
    }

    private void deleteDriverInteractive() {
        System.out.println("\n=== DELETE DRIVER ===");
        System.out.print("Enter Driver ID: ");
        String driverId = scanner.nextLine();

        Driver driver = drivers.get(driverId);
        if (driver == null) {
            System.out.println("Driver not found.");
            return;
        }

        // Check if driver is assigned to any vehicle
        boolean isAssigned = vehicles.values().stream()
                .anyMatch(vehicle -> driverId.equals(vehicle.getAssignedDriver()));

        if (isAssigned) {
            System.out.println("Cannot delete driver. They are assigned to one or more vehicles.");
            return;
        }

        System.out.println("Driver to be deleted:");
        driver.displayDriverInfo();

        System.out.print("Are you sure you want to delete this driver? (yes/no): ");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation)) {
            drivers.remove(driverId);
            System.out.println("Driver deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void viewDriverDetailsInteractive() {
        System.out.println("\n=== DRIVER DETAILS ===");
        System.out.print("Enter Driver ID: ");
        String driverId = scanner.nextLine();

        Driver driver = drivers.get(driverId);
        if (driver != null) {
            driver.displayDriverInfo();

            // Show assigned vehicles
            List<Vehicle> assignedVehicles = vehicles.values().stream()
                    .filter(vehicle -> driverId.equals(vehicle.getAssignedDriver()))
                    .collect(Collectors.toList());

            if (!assignedVehicles.isEmpty()) {
                System.out.println("\nASSIGNED VEHICLES:");
                assignedVehicles.forEach(vehicle ->
                        System.out.println("- " + vehicle.getVehicleId() + ": " + vehicle.getModel()));
            }
        } else {
            System.out.println("Driver not found.");
        }
    }

    private void scheduleManagementMenu() {
        System.out.println("\n=== SCHEDULE MANAGEMENT ===");
        System.out.println("1. Create Schedule");
        System.out.println("2. View Schedules");
        System.out.println("3. Update Schedule");
        System.out.println("4. Delete Schedule");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: createScheduleInteractive(); break;
            case 2: viewSchedules(); break;
            case 3: updateScheduleInteractive(); break;
            case 4: deleteScheduleInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void createScheduleInteractive() {
        System.out.println("\n=== CREATE SCHEDULE ===");

        if (routes.isEmpty() || vehicles.isEmpty()) {
            System.out.println("Please ensure routes and vehicles are available before creating schedules.");
            return;
        }

        System.out.println("Available Routes:");
        routes.values().forEach(route ->
                System.out.println("- " + route.getRouteId() + ": " + route.getRouteName()));

        System.out.print("Enter Route ID: ");
        String routeId = scanner.nextLine();

        if (!routes.containsKey(routeId)) {
            System.out.println("Route not found.");
            return;
        }

        System.out.println("Available Vehicles:");
        vehicles.values().stream()
                .filter(vehicle -> vehicle.getStatus() == VehicleStatus.ACTIVE)
                .forEach(vehicle ->
                        System.out.println("- " + vehicle.getVehicleId() + ": " + vehicle.getModel()));

        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        if (!vehicles.containsKey(vehicleId)) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.print("Enter departure time (HH:MM): ");
        String timeStr = scanner.nextLine();

        try {
            LocalTime departureTime = LocalTime.parse(timeStr);
            scheduler.createSchedule(routeId, vehicleId, departureTime);
            System.out.println("Schedule created successfully!");
        } catch (Exception e) {
            System.out.println("Invalid time format. Please use HH:MM format.");
        }
    }

    private void viewSchedules() {
        System.out.println("\n=== VIEW SCHEDULES ===");
        scheduler.displayAllSchedules();
    }

    private void updateScheduleInteractive() {
        System.out.println("\n=== UPDATE SCHEDULE ===");
        System.out.println("Schedule update functionality would be implemented here.");
        // Implementation would depend on the BusRouteScheduler class structure
    }

    private void deleteScheduleInteractive() {
        System.out.println("\n=== DELETE SCHEDULE ===");
        System.out.println("Schedule deletion functionality would be implemented here.");
        // Implementation would depend on the BusRouteScheduler class structure
    }

    private void maintenanceManagementMenu() {
        System.out.println("\n=== MAINTENANCE MANAGEMENT ===");
        System.out.println("1. Schedule Maintenance");
        System.out.println("2. View Maintenance Records");
        System.out.println("3. Update Maintenance Record");
        System.out.println("4. Maintenance Reports");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: scheduleMaintenanceInteractive(); break;
            case 2: viewMaintenanceRecords(); break;
            case 3: updateMaintenanceRecordInteractive(); break;
            case 4: maintenanceReportsMenu(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void scheduleMaintenanceInteractive() {
        System.out.println("\n=== SCHEDULE MAINTENANCE ===");

        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = read(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        String recordId = "M" + String.format("%03d", maintenanceCounter++);

        System.out.print("Enter maintenance type: ");
        String maintenanceType = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter estimated cost: $");
        double cost;
        try {
            cost = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid cost.");
            return;
        }

        MaintenanceRecord record = new MaintenanceRecord(recordId, vehicleId, LocalDate.now());
        record.setMaintenanceType(maintenanceType);
        record.setDescription(description);
        record.setCost(cost);
        record.setStatus("Scheduled");

        maintenanceRecords.put(recordId, record);

        // Set vehicle status to maintenance
        vehicle.setStatus(VehicleStatus.MAINTENANCE);
        update(vehicle);

        System.out.println("Maintenance scheduled successfully!");
        System.out.println("Maintenance Record ID: " + recordId);
    }

    private void viewMaintenanceRecords() {
        System.out.println("\n=== MAINTENANCE RECORDS ===");
        if (maintenanceRecords.isEmpty()) {
            System.out.println("No maintenance records found.");
            return;
        }

        System.out.printf("%-8s %-10s %-15s %-12s %-10s %-12s%n",
                "Record", "Vehicle", "Type", "Date", "Cost", "Status");
        System.out.println("-".repeat(75));

        maintenanceRecords.values().stream()
                .sorted((r1, r2) -> r2.getMaintenanceDate().compareTo(r1.getMaintenanceDate()))
                .forEach(record -> {
                    System.out.printf("%-8s %-10s %-15s %-12s $%-9.2f %-12s%n",
                            record.getRecordId(),
                            record.getVehicleId(),
                            record.getMaintenanceType() != null ?
                                    (record.getMaintenanceType().length() > 13 ? record.getMaintenanceType().substring(0, 13) + ".." : record.getMaintenanceType()) : "N/A",
                            record.getMaintenanceDate(),
                            record.getCost(),
                            record.getStatus());
                });

        System.out.println("-".repeat(75));
        System.out.println("Total Records: " + maintenanceRecords.size());
    }

    private void updateMaintenanceRecordInteractive() {
        System.out.println("\n=== UPDATE MAINTENANCE RECORD ===");
        System.out.print("Enter Maintenance Record ID: ");
        String recordId = scanner.nextLine();

        MaintenanceRecord record = maintenanceRecords.get(recordId);
        if (record == null) {
            System.out.println("Maintenance record not found.");
            return;
        }

        System.out.println("Current record:");
        record.displayMaintenanceInfo();

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Status");
        System.out.println("2. Cost");
        System.out.println("3. Description");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                System.out.println("Select new status:");
                System.out.println("1. Scheduled");
                System.out.println("2. In Progress");
                System.out.println("3. Completed");
                System.out.println("4. Cancelled");
                System.out.print("Enter choice: ");
                int statusChoice = getChoice();
                String[] statuses = {"Scheduled", "In Progress", "Completed", "Cancelled"};
                if (statusChoice >= 1 && statusChoice <= 4) {
                    record.setStatus(statuses[statusChoice - 1]);

                    // If completed, set vehicle back to active
                    if (statusChoice == 3) {
                        Vehicle vehicle = read(record.getVehicleId());
                        if (vehicle != null) {
                            vehicle.setStatus(VehicleStatus.ACTIVE);
                            update(vehicle);
                        }
                    }
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }
                break;
            case 2:
                System.out.print("Enter new cost: $");
                try {
                    record.setCost(Double.parseDouble(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid cost.");
                    return;
                }
                break;
            case 3:
                System.out.print("Enter new description: ");
                record.setDescription(scanner.nextLine());
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("Maintenance record updated successfully!");
    }

    private void maintenanceReportsMenu() {
        System.out.println("\n=== MAINTENANCE REPORTS ===");
        System.out.println("1. Vehicle Maintenance History");
        System.out.println("2. Maintenance Cost Analysis");
        System.out.println("3. Upcoming Maintenance");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: generateVehicleMaintenanceHistory(); break;
            case 2: generateMaintenanceCostAnalysis(); break;
            case 3: generateUpcomingMaintenanceReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void generateVehicleMaintenanceHistory() {
        System.out.print("Enter Vehicle ID: ");
        String vehicleId = scanner.nextLine();

        Vehicle vehicle = read(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        List<MaintenanceRecord> vehicleRecords = maintenanceRecords.values().stream()
                .filter(record -> record.getVehicleId().equals(vehicleId))
                .sorted((r1, r2) -> r2.getMaintenanceDate().compareTo(r1.getMaintenanceDate()))
                .collect(Collectors.toList());

        System.out.println("\n=== MAINTENANCE HISTORY FOR " + vehicleId + " ===");
        System.out.println("Vehicle: " + vehicle.getModel() + " (" + vehicle.getLicensePlate() + ")");

        if (vehicleRecords.isEmpty()) {
            System.out.println("No maintenance records found.");
            return