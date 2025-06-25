package com.campus.hostel;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

/**
 * Manager class for handling all hostel operations
 */
public class HostelManager implements Manageable<Room> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    
    private Map<String, Room> rooms;
    private Map<String, HostelBlock> hostelBlocks;
    private Map<String, Allocation> allocations;
    private Map<String, Payment> payments;
    private NotificationService notificationService;
    private int allocationCounter;
    private int paymentCounter;
    
    public HostelManager() {
        this.rooms = new HashMap<>();
        this.hostelBlocks = new HashMap<>();
        this.allocations = new HashMap<>();
        this.payments = new HashMap<>();
        this.notificationService = NotificationService.getInstance();
        this.allocationCounter = 1;
        this.paymentCounter = 1;
        initializeSampleData();
        logger.log("HostelManager initialized");
    }
    
    private void initializeSampleData() {
        // Create sample hostel blocks
        HostelBlock blockA = new HostelBlock("BLK-A", "Block A", "Main Campus Block A");
        blockA.setTotalFloors(4);
        blockA.setRoomsPerFloor(20);
        hostelBlocks.put("BLK-A", blockA);
        
        HostelBlock blockB = new HostelBlock("BLK-B", "Block B", "Main Campus Block B");
        blockB.setTotalFloors(3);
        blockB.setRoomsPerFloor(15);
        hostelBlocks.put("BLK-B", blockB);
        
        // Create sample rooms
        for (int floor = 1; floor <= 4; floor++) {
            for (int roomNum = 1; roomNum <= 20; roomNum++) {
                String roomId = "A" + floor + String.format("%02d", roomNum);
                Room room = new Room(roomId, "BLK-A", floor, RoomType.DOUBLE);
                room.setRoomNumber(roomId);
                room.setMonthlyRent(800.0);
                rooms.put(roomId, room);
            }
        }
        
        for (int floor = 1; floor <= 3; floor++) {
            for (int roomNum = 1; roomNum <= 15; roomNum++) {
                String roomId = "B" + floor + String.format("%02d", roomNum);
                Room room = new Room(roomId, "BLK-B", floor, RoomType.SINGLE);
                room.setRoomNumber(roomId);
                room.setMonthlyRent(1200.0);
                rooms.put(roomId, room);
            }
        }
        
        // Create sample allocations
        Room room1 = rooms.get("A101");
        if (room1 != null) {
            room1.allocateRoom("S001", LocalDate.now().minusMonths(2));
            Allocation allocation1 = new Allocation("AL001", "S001", "A101", LocalDate.now().minusMonths(2));
            allocation1.setMonthlyRent(800.0);
            allocations.put("AL001", allocation1);
        }
        
        Room room2 = rooms.get("B201");
        if (room2 != null) {
            room2.allocateRoom("S002", LocalDate.now().minusMonths(1));
            Allocation allocation2 = new Allocation("AL002", "S002", "B201", LocalDate.now().minusMonths(1));
            allocation2.setMonthlyRent(1200.0);
            allocations.put("AL002", allocation2);
        }
        
        // Create sample payments
        Payment payment1 = new Payment("PAY001", "S001", 800.0, PaymentType.RENT);
        payment1.setPaymentDate(LocalDate.now().minusMonths(1));
        payment1.setStatus(PaymentStatus.COMPLETED);
        payment1.setDescription("Monthly rent for A101");
        payments.put("PAY001", payment1);
        
        Payment payment2 = new Payment("PAY002", "S002", 1200.0, PaymentType.RENT);
        payment2.setPaymentDate(LocalDate.now().minusDays(15));
        payment2.setStatus(PaymentStatus.COMPLETED);
        payment2.setDescription("Monthly rent for B201");
        payments.put("PAY002", payment2);
        
        logger.log("Sample hostel data initialized");
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== HOSTEL MANAGEMENT MENU ===");
            System.out.println("1. Room Management");
            System.out.println("2. Allocation Management");
            System.out.println("3. Payment Management");
            System.out.println("4. Hostel Block Management");
            System.out.println("5. Reports & Analytics");
            System.out.println("6. Maintenance & Services");
            System.out.println("7. Search & Filter");
            System.out.println("8. System Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getChoice();
            
            switch (choice) {
                case 1: roomManagementMenu(); break;
                case 2: allocationManagementMenu(); break;
                case 3: paymentManagementMenu(); break;
                case 4: hostelBlockManagementMenu(); break;
                case 5: reportsMenu(); break;
                case 6: maintenanceMenu(); break;
                case 7: searchMenu(); break;
                case 8: displaySystemStatistics(); break;
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
    public void create(Room room) {
        rooms.put(room.getRoomId(), room);
        notificationService.notifyObservers("New room created: " + room.getRoomId());
        logger.log("Room created: " + room.getRoomId());
    }
    
    @Override
    public Room read(String roomId) {
        return rooms.get(roomId);
    }
    
    @Override
    public void update(Room room) {
        rooms.put(room.getRoomId(), room);
        notificationService.notifyObservers("Room updated: " + room.getRoomId());
        logger.log("Room updated: " + room.getRoomId());
    }
    
    @Override
    public void delete(String roomId) {
        Room room = rooms.remove(roomId);
        if (room != null) {
            notificationService.notifyObservers("Room deleted: " + roomId);
            logger.log("Room deleted: " + roomId);
        }
    }
    
    @Override
    public List<Room> getAll() {
        return new ArrayList<>(rooms.values());
    }
    
    private void roomManagementMenu() {
        System.out.println("\n=== ROOM MANAGEMENT ===");
        System.out.println("1. View All Rooms");
        System.out.println("2. View Available Rooms");
        System.out.println("3. View Occupied Rooms");
        System.out.println("4. Add New Room");
        System.out.println("5. Update Room");
        System.out.println("6. Room Details");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: viewAllRooms(); break;
            case 2: viewAvailableRooms(); break;
            case 3: viewOccupiedRooms(); break;
            case 4: addNewRoomInteractive(); break;
            case 5: updateRoomInteractive(); break;
            case 6: viewRoomDetailsInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void viewAllRooms() {
        System.out.println("\n=== ALL ROOMS ===");
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }
        
        System.out.printf("%-8s %-8s %-8s %-12s %-12s %-10s %-12s%n", 
                         "Room ID", "Block", "Floor", "Type", "Status", "Rent", "Occupant");
        System.out.println("-".repeat(75));
        
        rooms.values().stream()
            .sorted((r1, r2) -> r1.getRoomId().compareTo(r2.getRoomId()))
            .forEach(room -> {
                System.out.printf("%-8s %-8s %-8d %-12s %-12s $%-9.2f %-12s%n",
                                 room.getRoomId(),
                                 room.getBlockId(),
                                 room.getFloorNumber(),
                                 room.getRoomType(),
                                 room.getStatus(),
                                 room.getMonthlyRent(),
                                 room.getCurrentOccupant() != null ? room.getCurrentOccupant() : "None");
            });
        
        System.out.println("-".repeat(75));
        System.out.println("Total Rooms: " + rooms.size());
        System.out.println("Available: " + rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.AVAILABLE).count());
        System.out.println("Occupied: " + rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.OCCUPIED).count());
    }
    
    private void viewAvailableRooms() {
        System.out.println("\n=== AVAILABLE ROOMS ===");
        
        List<Room> availableRooms = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .sorted((r1, r2) -> r1.getRoomId().compareTo(r2.getRoomId()))
                .collect(Collectors.toList());
        
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms.");
            return;
        }
        
        System.out.printf("%-8s %-8s %-8s %-12s %-10s%n", 
                         "Room ID", "Block", "Floor", "Type", "Rent");
        System.out.println("-".repeat(50));
        
        availableRooms.forEach(room -> {
            System.out.printf("%-8s %-8s %-8d %-12s $%-9.2f%n",
                             room.getRoomId(),
                             room.getBlockId(),
                             room.getFloorNumber(),
                             room.getRoomType(),
                             room.getMonthlyRent());
        });
        
        System.out.println("-".repeat(50));
        System.out.println("Total Available Rooms: " + availableRooms.size());
    }
    
    private void viewOccupiedRooms() {
        System.out.println("\n=== OCCUPIED ROOMS ===");
        
        List<Room> occupiedRooms = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.OCCUPIED)
                .sorted((r1, r2) -> r1.getRoomId().compareTo(r2.getRoomId()))
                .collect(Collectors.toList());
        
        if (occupiedRooms.isEmpty()) {
            System.out.println("No occupied rooms.");
            return;
        }
        
        System.out.printf("%-8s %-8s %-8s %-12s %-10s %-12s %-12s%n", 
                         "Room ID", "Block", "Floor", "Type", "Rent", "Occupant", "Since");
        System.out.println("-".repeat(80));
        
        occupiedRooms.forEach(room -> {
            System.out.printf("%-8s %-8s %-8d %-12s $%-9.2f %-12s %-12s%n",
                             room.getRoomId(),
                             room.getBlockId(),
                             room.getFloorNumber(),
                             room.getRoomType(),
                             room.getMonthlyRent(),
                             room.getCurrentOccupant() != null ? room.getCurrentOccupant() : "Unknown",
                             room.getAllocationDate() != null ? room.getAllocationDate().toString() : "Unknown");
        });
        
        System.out.println("-".repeat(80));
        System.out.println("Total Occupied Rooms: " + occupiedRooms.size());
    }
    
    private void addNewRoomInteractive() {
        System.out.println("\n=== ADD NEW ROOM ===");
        
        System.out.print("Enter Room ID: ");
        String roomId = scanner.nextLine();
        
        if (rooms.containsKey(roomId)) {
            System.out.println("Room ID already exists.");
            return;
        }
        
        System.out.print("Enter Block ID: ");
        String blockId = scanner.nextLine();
        
        System.out.print("Enter Floor Number: ");
        int floorNumber;
        try {
            floorNumber = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid floor number.");
            return;
        }
        
        System.out.println("Select Room Type:");
        RoomType[] types = RoomType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        
        int typeChoice = getChoice();
        if (typeChoice < 1 || typeChoice > types.length) {
            System.out.println("Invalid room type choice.");
            return;
        }
        
        RoomType roomType = types[typeChoice - 1];
        
        Room newRoom = new Room(roomId, blockId, floorNumber, roomType);
        newRoom.setRoomNumber(roomId);
        
        System.out.print("Enter Monthly Rent: $");
        try {
            double rent = Double.parseDouble(scanner.nextLine());
            newRoom.setMonthlyRent(rent);
        } catch (NumberFormatException e) {
            System.out.println("Invalid rent amount.");
            return;
        }
        
        create(newRoom);
        System.out.println("Room added successfully!");
        newRoom.displayRoomInfo();
    }
    
    private void updateRoomInteractive() {
        System.out.println("\n=== UPDATE ROOM ===");
        System.out.print("Enter Room ID: ");
        String roomId = scanner.nextLine();
        
        Room room = read(roomId);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        System.out.println("Current room information:");
        room.displayRoomInfo();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Monthly Rent");
        System.out.println("2. Room Type");
        System.out.println("3. Room Status");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new monthly rent: $");
                try {
                    double rent = Double.parseDouble(scanner.nextLine());
                    room.setMonthlyRent(rent);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid rent amount.");
                    return;
                }
                break;
            case 2:
                System.out.println("Select new room type:");
                RoomType[] types = RoomType.values();
                for (int i = 0; i < types.length; i++) {
                    System.out.println((i + 1) + ". " + types[i]);
                }
                System.out.print("Enter choice: ");
                int typeChoice = getChoice();
                if (typeChoice >= 1 && typeChoice <= types.length) {
                    room.setRoomType(types[typeChoice - 1]);
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }
                break;
            case 3:
                System.out.println("Select new room status:");
                RoomStatus[] statuses = RoomStatus.values();
                for (int i = 0; i < statuses.length; i++) {
                    System.out.println((i + 1) + ". " + statuses[i]);
                }
                System.out.print("Enter choice: ");
                int statusChoice = getChoice();
                if (statusChoice >= 1 && statusChoice <= statuses.length) {
                    room.setStatus(statuses[statusChoice - 1]);
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        update(room);
        System.out.println("Room updated successfully!");
    }
    
    private void viewRoomDetailsInteractive() {
        System.out.println("\n=== ROOM DETAILS ===");
        System.out.print("Enter Room ID: ");
        String roomId = scanner.nextLine();
        
        Room room = read(roomId);
        if (room != null) {
            room.displayRoomInfo();
        } else {
            System.out.println("Room not found.");
        }
    }
    
    private void allocationManagementMenu() {
        System.out.println("\n=== ALLOCATION MANAGEMENT ===");
        System.out.println("1. Allocate Room");
        System.out.println("2. Deallocate Room");
        System.out.println("3. View All Allocations");
        System.out.println("4. View Student Allocation");
        System.out.println("5. Transfer Room");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: allocateRoomInteractive(); break;
            case 2: deallocateRoomInteractive(); break;
            case 3: viewAllAllocations(); break;
            case 4: viewStudentAllocationInteractive(); break;
            case 5: transferRoomInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void allocateRoomInteractive() {
        System.out.println("\n=== ALLOCATE ROOM ===");
        
        // Show available rooms first
        List<Room> availableRooms = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .collect(Collectors.toList());
        
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms for allocation.");
            return;
        }
        
        System.out.println("Available Rooms:");
        availableRooms.forEach(room -> 
            System.out.println("- " + room.getRoomId() + " (" + room.getRoomType() + ") - $" + room.getMonthlyRent()));
        
        System.out.print("\nEnter Room ID to allocate: ");
        String roomId = scanner.nextLine();
        
        Room room = read(roomId);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            System.out.println("Room is not available for allocation.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        System.out.print("Enter allocation date (YYYY-MM-DD) or press Enter for today: ");
        String dateStr = scanner.nextLine();
        LocalDate allocationDate = LocalDate.now();
        
        if (!dateStr.trim().isEmpty()) {
            try {
                allocationDate = LocalDate.parse(dateStr);
            } catch (Exception e) {
                System.out.println("Invalid date format, using today's date.");
            }
        }
        
        // Allocate room
        room.allocateRoom(studentId, allocationDate);
        update(room);
        
        // Create allocation record
        String allocationId = "AL" + String.format("%03d", allocationCounter++);
        Allocation allocation = new Allocation(allocationId, studentId, roomId, allocationDate);
        allocation.setMonthlyRent(room.getMonthlyRent());
        allocations.put(allocationId, allocation);
        
        System.out.println("Room allocated successfully!");
        System.out.println("Allocation ID: " + allocationId);
        System.out.println("Student: " + studentId);
        System.out.println("Room: " + roomId);
        System.out.println("Monthly Rent: $" + room.getMonthlyRent());
    }
    
    private void deallocateRoomInteractive() {
        System.out.println("\n=== DEALLOCATE ROOM ===");
        System.out.print("Enter Room ID: ");
        String roomId = scanner.nextLine();
        
        Room room = read(roomId);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        if (room.getStatus() != RoomStatus.OCCUPIED) {
            System.out.println("Room is not currently occupied.");
            return;
        }
        
        System.out.println("Current occupant: " + room.getCurrentOccupant());
        System.out.print("Are you sure you want to deallocate this room? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if ("yes".equalsIgnoreCase(confirmation)) {
            String studentId = room.getCurrentOccupant();
            room.deallocateRoom();
            update(room);
            
            // Update allocation record
            allocations.values().stream()
                    .filter(alloc -> alloc.getRoomId().equals(roomId) && 
                                   alloc.getStudentId().equals(studentId) && 
                                   alloc.getStatus() == AllocationStatus.ACTIVE)
                    .findFirst()
                    .ifPresent(alloc -> {
                        alloc.setStatus(AllocationStatus.TERMINATED);
                        alloc.setEndDate(LocalDate.now());
                    });
            
            System.out.println("Room deallocated successfully!");
        } else {
            System.out.println("Deallocation cancelled.");
        }
    }
    
    private void viewAllAllocations() {
        System.out.println("\n=== ALL ALLOCATIONS ===");
        if (allocations.isEmpty()) {
            System.out.println("No allocations found.");
            return;
        }
        
        System.out.printf("%-8s %-10s %-8s %-12s %-10s %-12s%n", 
                         "Alloc ID", "Student", "Room", "Start Date", "Rent", "Status");
        System.out.println("-".repeat(70));
        
        allocations.values().stream()
            .sorted((a1, a2) -> a1.getAllocationId().compareTo(a2.getAllocationId()))
            .forEach(allocation -> {
                System.out.printf("%-8s %-10s %-8s %-12s $%-9.2f %-12s%n",
                                 allocation.getAllocationId(),
                                 allocation.getStudentId(),
                                 allocation.getRoomId(),
                                 allocation.getStartDate(),
                                 allocation.getMonthlyRent(),
                                 allocation.getStatus());
            });
        
        System.out.println("-".repeat(70));
        System.out.println("Total Allocations: " + allocations.size());
        System.out.println("Active: " + allocations.values().stream().filter(a -> a.getStatus() == AllocationStatus.ACTIVE).count());
    }
    
    private void viewStudentAllocationInteractive() {
        System.out.println("\n=== STUDENT ALLOCATION ===");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        List<Allocation> studentAllocations = allocations.values().stream()
                .filter(allocation -> allocation.getStudentId().equals(studentId))
                .sorted((a1, a2) -> a2.getStartDate().compareTo(a1.getStartDate()))
                .collect(Collectors.toList());
        
        if (studentAllocations.isEmpty()) {
            System.out.println("No allocations found for student: " + studentId);
            return;
        }
        
        System.out.println("Allocations for Student: " + studentId);
        System.out.printf("%-8s %-8s %-12s %-12s %-10s %-12s%n", 
                         "Alloc ID", "Room", "Start Date", "End Date", "Rent", "Status");
        System.out.println("-".repeat(75));
        
        studentAllocations.forEach(allocation -> {
            System.out.printf("%-8s %-8s %-12s %-12s $%-9.2f %-12s%n",
                             allocation.getAllocationId(),
                             allocation.getRoomId(),
                             allocation.getStartDate(),
                             allocation.getEndDate() != null ? allocation.getEndDate().toString() : "Current",
                             allocation.getMonthlyRent(),
                             allocation.getStatus());
        });
    }
    
    private void transferRoomInteractive() {
        System.out.println("\n=== TRANSFER ROOM ===");
        System.out.print("Enter current Room ID: ");
        String currentRoomId = scanner.nextLine();
        
        Room currentRoom = read(currentRoomId);
        if (currentRoom == null || currentRoom.getStatus() != RoomStatus.OCCUPIED) {
            System.out.println("Room not found or not occupied.");
            return;
        }
        
        String studentId = currentRoom.getCurrentOccupant();
        System.out.println("Current occupant: " + studentId);
        
        // Show available rooms
        List<Room> availableRooms = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .collect(Collectors.toList());
        
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms for transfer.");
            return;
        }
        
        System.out.println("Available rooms for transfer:");
        availableRooms.forEach(room -> 
            System.out.println("- " + room.getRoomId() + " (" + room.getRoomType() + ") - $" + room.getMonthlyRent()));
        
        System.out.print("Enter new Room ID: ");
        String newRoomId = scanner.nextLine();
        
        Room newRoom = read(newRoomId);
        if (newRoom == null || newRoom.getStatus() != RoomStatus.AVAILABLE) {
            System.out.println("New room not found or not available.");
            return;
        }
        
        // Perform transfer
        currentRoom.deallocateRoom();
        newRoom.allocateRoom(studentId, LocalDate.now());
        
        update(currentRoom);
        update(newRoom);
        
        // Update allocation records
        allocations.values().stream()
                .filter(alloc -> alloc.getRoomId().equals(currentRoomId) && 
                               alloc.getStudentId().equals(studentId) && 
                               alloc.getStatus() == AllocationStatus.ACTIVE)
                .findFirst()
                .ifPres
                .findFirst()
                .ifPresent(alloc -> {
                    alloc.setStatus(AllocationStatus.TERMINATED);
                    alloc.setEndDate(LocalDate.now());
                });
        
        // Create new allocation record
        String allocationId = "AL" + String.format("%03d", allocationCounter++);
        Allocation newAllocation = new Allocation(allocationId, studentId, newRoomId, LocalDate.now());
        newAllocation.setMonthlyRent(newRoom.getMonthlyRent());
        allocations.put(allocationId, newAllocation);
        
        System.out.println("Room transfer completed successfully!");
        System.out.println("Student " + studentId + " transferred from " + currentRoomId + " to " + newRoomId);
    }
    
    private void paymentManagementMenu() {
        System.out.println("\n=== PAYMENT MANAGEMENT ===");
        System.out.println("1. Record Payment");
        System.out.println("2. View All Payments");
        System.out.println("3. View Student Payments");
        System.out.println("4. Pending Payments");
        System.out.println("5. Payment Reports");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: recordPaymentInteractive(); break;
            case 2: viewAllPayments(); break;
            case 3: viewStudentPaymentsInteractive(); break;
            case 4: viewPendingPayments(); break;
            case 5: paymentReportsMenu(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void recordPaymentInteractive() {
        System.out.println("\n=== RECORD PAYMENT ===");
        
        String paymentId = "PAY" + String.format("%03d", paymentCounter++);
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        System.out.print("Enter Payment Amount: $");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        
        System.out.println("Select Payment Type:");
        PaymentType[] types = PaymentType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        
        int typeChoice = getChoice();
        if (typeChoice < 1 || typeChoice > types.length) {
            System.out.println("Invalid payment type choice.");
            return;
        }
        
        PaymentType paymentType = types[typeChoice - 1];
        
        Payment payment = new Payment(paymentId, studentId, amount, paymentType);
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus(PaymentStatus.COMPLETED);
        
        System.out.print("Enter Description (optional): ");
        String description = scanner.nextLine();
        if (!description.trim().isEmpty()) {
            payment.setDescription(description);
        }
        
        payments.put(paymentId, payment);
        
        System.out.println("Payment recorded successfully!");
        payment.displayPaymentInfo();
    }
    
    private void viewAllPayments() {
        System.out.println("\n=== ALL PAYMENTS ===");
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }
        
        System.out.printf("%-8s %-10s %-10s %-12s %-12s %-12s%n", 
                         "Pay ID", "Student", "Amount", "Type", "Date", "Status");
        System.out.println("-".repeat(70));
        
        payments.values().stream()
            .sorted((p1, p2) -> p2.getPaymentDate().compareTo(p1.getPaymentDate()))
            .forEach(payment -> {
                System.out.printf("%-8s %-10s $%-9.2f %-12s %-12s %-12s%n",
                                 payment.getPaymentId(),
                                 payment.getStudentId(),
                                 payment.getAmount(),
                                 payment.getPaymentType().toString().length() > 10 ? 
                                     payment.getPaymentType().toString().substring(0, 10) + ".." : payment.getPaymentType(),
                                 payment.getPaymentDate(),
                                 payment.getStatus());
            });
        
        System.out.println("-".repeat(70));
        System.out.println("Total Payments: " + payments.size());
        
        double totalAmount = payments.values().stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        System.out.println("Total Amount: $" + String.format("%.2f", totalAmount));
    }
    
    private void viewStudentPaymentsInteractive() {
        System.out.println("\n=== STUDENT PAYMENTS ===");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        List<Payment> studentPayments = payments.values().stream()
                .filter(payment -> payment.getStudentId().equals(studentId))
                .sorted((p1, p2) -> p2.getPaymentDate().compareTo(p1.getPaymentDate()))
                .collect(Collectors.toList());
        
        if (studentPayments.isEmpty()) {
            System.out.println("No payments found for student: " + studentId);
            return;
        }
        
        System.out.println("Payments for Student: " + studentId);
        System.out.printf("%-8s %-10s %-12s %-12s %-12s %-20s%n", 
                         "Pay ID", "Amount", "Type", "Date", "Status", "Description");
        System.out.println("-".repeat(85));
        
        studentPayments.forEach(payment -> {
            System.out.printf("%-8s $%-9.2f %-12s %-12s %-12s %-20s%n",
                             payment.getPaymentId(),
                             payment.getAmount(),
                             payment.getPaymentType().toString().length() > 10 ? 
                                 payment.getPaymentType().toString().substring(0, 10) + ".." : payment.getPaymentType(),
                             payment.getPaymentDate(),
                             payment.getStatus(),
                             payment.getDescription() != null ? 
                                 (payment.getDescription().length() > 18 ? payment.getDescription().substring(0, 18) + ".." : payment.getDescription()) : "");
        });
        
        double totalAmount = studentPayments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        System.out.println("-".repeat(85));
        System.out.println("Total Payments: " + studentPayments.size());
        System.out.println("Total Amount: $" + String.format("%.2f", totalAmount));
    }
    
    private void viewPendingPayments() {
        System.out.println("\n=== PENDING PAYMENTS ===");
        
        List<Payment> pendingPayments = payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.PENDING)
                .sorted((p1, p2) -> p1.getDueDate().compareTo(p2.getDueDate()))
                .collect(Collectors.toList());
        
        if (pendingPayments.isEmpty()) {
            System.out.println("No pending payments.");
            return;
        }
        
        System.out.printf("%-8s %-10s %-10s %-12s %-12s%n", 
                         "Pay ID", "Student", "Amount", "Type", "Due Date");
        System.out.println("-".repeat(60));
        
        pendingPayments.forEach(payment -> {
            System.out.printf("%-8s %-10s $%-9.2f %-12s %-12s%n",
                             payment.getPaymentId(),
                             payment.getStudentId(),
                             payment.getAmount(),
                             payment.getPaymentType(),
                             payment.getDueDate());
        });
        
        System.out.println("-".repeat(60));
        System.out.println("Total Pending: " + pendingPayments.size());
    }
    
    private void paymentReportsMenu() {
        System.out.println("\n=== PAYMENT REPORTS ===");
        System.out.println("1. Monthly Revenue Report");
        System.out.println("2. Payment Type Analysis");
        System.out.println("3. Outstanding Payments");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: generateMonthlyRevenueReport(); break;
            case 2: generatePaymentTypeAnalysis(); break;
            case 3: generateOutstandingPaymentsReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void generateMonthlyRevenueReport() {
        System.out.println("\n=== MONTHLY REVENUE REPORT ===");
        
        Map<String, Double> monthlyRevenue = payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                    payment -> payment.getPaymentDate().getYear() + "-" + 
                              String.format("%02d", payment.getPaymentDate().getMonthValue()),
                    Collectors.summingDouble(Payment::getAmount)
                ));
        
        if (monthlyRevenue.isEmpty()) {
            System.out.println("No revenue data available.");
            return;
        }
        
        System.out.printf("%-10s %-15s%n", "Month", "Revenue");
        System.out.println("-".repeat(30));
        
        monthlyRevenue.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("%-10s $%-14.2f%n",
                                     entry.getKey(),
                                     entry.getValue());
                });
        
        double totalRevenue = monthlyRevenue.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        
        System.out.println("-".repeat(30));
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
    }
    
    private void generatePaymentTypeAnalysis() {
        System.out.println("\n=== PAYMENT TYPE ANALYSIS ===");
        
        Map<PaymentType, Double> typeRevenue = payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                    Payment::getPaymentType,
                    Collectors.summingDouble(Payment::getAmount)
                ));
        
        Map<PaymentType, Long> typeCount = payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                    Payment::getPaymentType,
                    Collectors.counting()
                ));
        
        System.out.printf("%-15s %-10s %-15s%n", "Payment Type", "Count", "Total Amount");
        System.out.println("-".repeat(45));
        
        typeRevenue.entrySet().stream()
                .sorted(Map.Entry.<PaymentType, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    PaymentType type = entry.getKey();
                    Double amount = entry.getValue();
                    Long count = typeCount.getOrDefault(type, 0L);
                    
                    System.out.printf("%-15s %-10d $%-14.2f%n",
                                     type.toString().length() > 13 ? type.toString().substring(0, 13) + ".." : type,
                                     count,
                                     amount);
                });
    }
    
    private void generateOutstandingPaymentsReport() {
        System.out.println("\n=== OUTSTANDING PAYMENTS REPORT ===");
        
        // This would typically calculate based on rent due dates
        // For demonstration, showing pending and overdue payments
        
        List<Payment> outstandingPayments = payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.PENDING || 
                                 payment.getStatus() == PaymentStatus.OVERDUE)
                .sorted((p1, p2) -> p1.getDueDate().compareTo(p2.getDueDate()))
                .collect(Collectors.toList());
        
        if (outstandingPayments.isEmpty()) {
            System.out.println("No outstanding payments.");
            return;
        }
        
        System.out.printf("%-8s %-10s %-10s %-12s %-12s %-12s%n", 
                         "Pay ID", "Student", "Amount", "Type", "Due Date", "Status");
        System.out.println("-".repeat(75));
        
        outstandingPayments.forEach(payment -> {
            System.out.printf("%-8s %-10s $%-9.2f %-12s %-12s %-12s%n",
                             payment.getPaymentId(),
                             payment.getStudentId(),
                             payment.getAmount(),
                             payment.getPaymentType().toString().length() > 10 ? 
                                 payment.getPaymentType().toString().substring(0, 10) + ".." : payment.getPaymentType(),
                             payment.getDueDate(),
                             payment.getStatus());
        });
        
        double totalOutstanding = outstandingPayments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        
        System.out.println("-".repeat(75));
        System.out.println("Total Outstanding: $" + String.format("%.2f", totalOutstanding));
    }
    
    private void hostelBlockManagementMenu() {
        System.out.println("\n=== HOSTEL BLOCK MANAGEMENT ===");
        System.out.println("1. View All Blocks");
        System.out.println("2. Add New Block");
        System.out.println("3. Update Block");
        System.out.println("4. Block Statistics");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: viewAllHostelBlocks(); break;
            case 2: addNewHostelBlockInteractive(); break;
            case 3: updateHostelBlockInteractive(); break;
            case 4: displayBlockStatistics(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void viewAllHostelBlocks() {
        System.out.println("\n=== ALL HOSTEL BLOCKS ===");
        if (hostelBlocks.isEmpty()) {
            System.out.println("No hostel blocks found.");
            return;
        }
        
        System.out.printf("%-8s %-15s %-8s %-8s %-10s %-10s%n", 
                         "Block ID", "Name", "Floors", "Rooms/Floor", "Total Rooms", "Occupied");
        System.out.println("-".repeat(70));
        
        hostelBlocks.values().forEach(block -> {
            int totalRooms = block.getTotalFloors() * block.getRoomsPerFloor();
            long occupiedRooms = rooms.values().stream()
                    .filter(room -> room.getBlockId().equals(block.getBlockId()) && 
                                   room.getStatus() == RoomStatus.OCCUPIED)
                    .count();
            
            System.out.printf("%-8s %-15s %-8d %-8d %-10d %-10d%n",
                             block.getBlockId(),
                             block.getBlockName().length() > 13 ? block.getBlockName().substring(0, 13) + ".." : block.getBlockName(),
                             block.getTotalFloors(),
                             block.getRoomsPerFloor(),
                             totalRooms,
                             occupiedRooms);
        });
    }
    
    private void addNewHostelBlockInteractive() {
        System.out.println("\n=== ADD NEW HOSTEL BLOCK ===");
        
        System.out.print("Enter Block ID: ");
        String blockId = scanner.nextLine();
        
        if (hostelBlocks.containsKey(blockId)) {
            System.out.println("Block ID already exists.");
            return;
        }
        
        System.out.print("Enter Block Name: ");
        String blockName = scanner.nextLine();
        
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        
        HostelBlock newBlock = new HostelBlock(blockId, blockName, description);
        
        System.out.print("Enter Total Floors: ");
        try {
            int floors = Integer.parseInt(scanner.nextLine());
            newBlock.setTotalFloors(floors);
        } catch (NumberFormatException e) {
            System.out.println("Invalid floor count.");
            return;
        }
        
        System.out.print("Enter Rooms per Floor: ");
        try {
            int roomsPerFloor = Integer.parseInt(scanner.nextLine());
            newBlock.setRoomsPerFloor(roomsPerFloor);
        } catch (NumberFormatException e) {
            System.out.println("Invalid rooms per floor count.");
            return;
        }
        
        hostelBlocks.put(blockId, newBlock);
        
        System.out.println("Hostel block added successfully!");
        newBlock.displayBlockInfo();
    }
    
    private void updateHostelBlockInteractive() {
        System.out.println("\n=== UPDATE HOSTEL BLOCK ===");
        System.out.print("Enter Block ID: ");
        String blockId = scanner.nextLine();
        
        HostelBlock block = hostelBlocks.get(blockId);
        if (block == null) {
            System.out.println("Block not found.");
            return;
        }
        
        System.out.println("Current block information:");
        block.displayBlockInfo();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Block Name");
        System.out.println("2. Description");
        System.out.println("3. Total Floors");
        System.out.println("4. Rooms per Floor");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new block name: ");
                block.setBlockName(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new description: ");
                block.setDescription(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new total floors: ");
                try {
                    block.setTotalFloors(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid floor count.");
                    return;
                }
                break;
            case 4:
                System.out.print("Enter new rooms per floor: ");
                try {
                    block.setRoomsPerFloor(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid rooms per floor count.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        System.out.println("Block updated successfully!");
    }
    
    private void displayBlockStatistics() {
        System.out.println("\n=== BLOCK STATISTICS ===");
        
        if (hostelBlocks.isEmpty()) {
            System.out.println("No blocks available for statistics.");
            return;
        }
        
        hostelBlocks.values().forEach(block -> {
            System.out.println("\n" + block.getBlockName() + " (" + block.getBlockId() + "):");
            
            List<Room> blockRooms = rooms.values().stream()
                    .filter(room -> room.getBlockId().equals(block.getBlockId()))
                    .collect(Collectors.toList());
            
            long totalRooms = blockRooms.size();
            long occupiedRooms = blockRooms.stream().filter(room -> room.getStatus() == RoomStatus.OCCUPIED).count();
            long availableRooms = blockRooms.stream().filter(room -> room.getStatus() == RoomStatus.AVAILABLE).count();
            
            double occupancyRate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100 : 0;
            
            System.out.println("- Total Rooms: " + totalRooms);
            System.out.println("- Occupied: " + occupiedRooms);
            System.out.println("- Available: " + availableRooms);
            System.out.println("- Occupancy Rate: " + String.format("%.1f", occupancyRate) + "%");
            
            // Revenue calculation
            double monthlyRevenue = blockRooms.stream()
                    .filter(room -> room.getStatus() == RoomStatus.OCCUPIED)
                    .mapToDouble(Room::getMonthlyRent)
                    .sum();
            
            System.out.println("- Monthly Revenue: $" + String.format("%.2f", monthlyRevenue));
        });
    }
    
    private void reportsMenu() {
        System.out.println("\n=== REPORTS & ANALYTICS ===");
        System.out.println("1. Occupancy Report");
        System.out.println("2. Revenue Report");
        System.out.println("3. Student Report");
        System.out.println("4. Room Utilization Report");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: generateOccupancyReport(); break;
            case 2: generateRevenueReport(); break;
            case 3: generateStudentReport(); break;
            case 4: generateRoomUtilizationReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void generateOccupancyReport() {
        System.out.println("\n=== OCCUPANCY REPORT ===");
        
        int totalRooms = rooms.size();
        long occupiedRooms = rooms.values().stream().filter(room -> room.getStatus() == RoomStatus.OCCUPIED).count();
        long availableRooms = rooms.values().stream().filter(room -> room.getStatus() == RoomStatus.AVAILABLE).count();
        long maintenanceRooms = rooms.values().stream().filter(room -> room.getStatus() == RoomStatus.MAINTENANCE).count();
        
        double occupancyRate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100 : 0;
        
        System.out.println("OVERALL OCCUPANCY:");
        System.out.println("- Total Rooms: " + totalRooms);
        System.out.println("- Occupied: " + occupiedRooms);
        System.out.println("- Available: " + availableRooms);
        System.out.println("- Under Maintenance: " + maintenanceRooms);
        System.out.println("- Occupancy Rate: " + String.format("%.1f", occupancyRate) + "%");
        
        // Block-wise occupancy
        System.out.println("\nBLOCK-WISE OCCUPANCY:");
        hostelBlocks.values().forEach(block -> {
            List<Room> blockRooms = rooms.values().stream()
                    .filter(room -> room.getBlockId().equals(block.getBlockId()))
                    .collect(Collectors.toList());
            
            long blockOccupied = blockRooms.stream().filter(room -> room.getStatus() == RoomStatus.OCCUPIED).count();
            double blockOccupancyRate = blockRooms.size() > 0 ? (double) blockOccupied / blockRooms.size() * 100 : 0;
            
            System.out.println("- " + block.getBlockName() + ": " + blockOccupied + "/" + blockRooms.size() + 
                             " (" + String.format("%.1f", blockOccupancyRate) + "%)");
        });
    }
    
    private void generateRevenueReport() {
        System.out.println("\n=== REVENUE REPORT ===");
        
        // Current monthly revenue from occupied rooms
        double currentMonthlyRevenue = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.OCCUPIED)
                .mapToDouble(Room::getMonthlyRent)
                .sum();
        
        // Potential monthly revenue if all rooms were occupied
        double potentialMonthlyRevenue = rooms.values().stream()
                .mapToDouble(Room::getMonthlyRent)
                .sum();
        
        // Actual payments received
        double totalPaymentsReceived = payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED)
                .mapToDouble(Payment::getAmount)
                .sum();
        
        System.out.println("REVENUE ANALYSIS:");
        System.out.println("- Current Monthly Revenue: $" + String.format("%.2f", currentMonthlyRevenue));
        System.out.println("- Potential Monthly Revenue: $" + String.format("%.2f", potentialMonthlyRevenue));
        System.out.println("- Revenue Efficiency: " + 
                          String.format("%.1f", (currentMonthlyRevenue / potentialMonthlyRevenue * 100)) + "%");
        System.out.println("- Total Payments Received: $" + String.format("%.2f", totalPaymentsReceived));
        
        // Revenue by room type
        System.out.println("\nREVENUE BY ROOM TYPE:");
        Map<RoomType, Double> revenueByType = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.OCCUPIED)
                .collect(Collectors.groupingBy(
                    Room::getRoomType,
                    Collectors.summingDouble(Room::getMonthlyRent)
                ));
        
        revenueByType.entrySet().stream()
                .sorted(Map.Entry.<RoomType, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    System.out.println("- " + entry.getKey() + ": $" + String.format("%.2f", entry.getValue()));
                });
    }
    
    private void generateStudentReport() {
        System.out.println("\n=== STUDENT REPORT ===");
        
        int totalAllocatedStudents = allocations.values().stream()
                .filter(alloc -> alloc.getStatus() == AllocationStatus.ACTIVE)
                .collect(Collectors.toSet())
                .size();
        
        System.out.println("STUDENT STATISTICS:");
        System.out.println("- Total Students with Room Allocation: " + totalAllocatedStudents);
        System.out.println("- Total Active Allocations: " + 
                          allocations.values().stream().filter(alloc -> alloc.getStatus() == AllocationStatus.ACTIVE).count());
        
        // Payment statistics by student
        Map<String, Double> studentPayments = payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                    Payment::getStudentId,
                    Collectors.summingDouble(Payment::getAmount)
                ));
        
        if (!studentPayments.isEmpty()) {
            double avgPaymentPerStudent = studentPayments.values().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            
            System.out.println("- Average Payment per Student: $" + String.format("%.2f", avgPaymentPerStudent));
            
            // Top paying students
            System.out.println("\nTOP PAYING STUDENTS:");
            studentPayments.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(5)
                    .forEach(entry -> {
                        System.out.println("- " + entry.getKey() + ": $" + String.format("%.2f", entry.getValue()));
                    });
        }
    }
    
    private void generateRoomUtilizationReport() {
        System.out.println("\n=== ROOM UTILIZATION REPORT ===");
        
        // Room type utilization
        Map<RoomType, Long> roomTypeCount = rooms.values().stream()
                .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));
        
        Map<RoomType, Long> occupiedByType = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.OCCUPIED)
                .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));
        
        System.out.println("ROOM TYPE UTILIZATION:");
        roomTypeCount.entrySet().forEach(entry -> {
            RoomType type = entry.getKey();
            Long total = entry.getValue();
            Long occupied = occupiedByType.getOrDefault(type, 0L);
            double utilizationRate = total > 0 ? (double) occupied / total * 100 : 0;
            
            System.out.println("- " + type + ": " + occupied + "/" + total + 
                             " (" + String.format("%.1f", utilizationRate) + "%)");
        });
        
        // Floor-wise utilization
        System.out.println("\nFLOOR-WISE UTILIZATION:");
        Map<Integer, List<Room>> roomsByFloor = rooms.values().stream()
                .collect(Collectors.groupingBy(Room::getFloorNumber));
        
        roomsByFloor.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    Integer floor = entry.getKey();
                    List<Room> floorRooms = entry.getValue();
                    long floorOccupied = floorRooms.stream()
                            .filter(room -> room.getStatus() == RoomStatus.OCCUPIED)
                            .count();
                    double floorUtilization = floorRooms.size() > 0 ? 
                            (double) floorOccupied / floorRooms.size() * 100 : 0;
                    
                    System.out.println("- Floor " + floor + ": " + floorOccupied + "/" + floorRooms.size() + 
                                     " (" + String.format("%.1f", floorUtilization) + "%)");
                });
    }
    
    private void maintenanceMenu() {
        System.out.println("\n=== MAINTENANCE & SERVICES ===");
        System.out.println("1. Mark Room for Maintenance");
        System.out.println("2. Complete Maintenance");
        System.out.println("3. View Maintenance Rooms");
        System.out.println("4. Maintenance Reports");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: markRoomForMaintenanceInteractive(); break;
            case 2: completeMaintenanceInteractive(); break;
            case 3: viewMaintenanceRooms(); break;
            case 4: generateMaintenanceReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void markRoomForMaintenanceInteractive() {
        System.out.println("\n=== MARK ROOM FOR MAINTENANCE ===");
        System.out.print("Enter Room ID: ");
        String roomId = scanner.nextLine();
        
        Room room = read(roomId);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        if (room.getStatus() == RoomStatus.OCCUPIED) {
            System.out.println("Cannot mark occupied room for maintenance. Please deallocate first.");
            return;
        }
        
        room.setStatus(RoomStatus.MAINTENANCE);
        update(room);
        
        System.out.println("Room " + roomId + " marked for maintenance.");
    }
    
    private void completeMaintenanceInteractive() {
        System.out.println("\n=== COMPLETE MAINTENANCE ===");
        System.out.print("Enter Room ID: ");
        String roomId = scanner.nextLine();
        
        Room room = read(roomId);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        if (room.getStatus() != RoomStatus.MAINTENANCE) {
            System.out.println("Room is not under maintenance.");
            return;
        }
        
        room.setStatus(RoomStatus.AVAILABLE);
        update(room);
        
        System.out.println("Maintenance completed for room " + roomId + ". Room is now available.");
    }
    
    private void viewMaintenanceRooms() {
        System.out.println("\n=== ROOMS UNDER MAINTENANCE ===");
        
        List<Room> maintenanceRooms = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.MAINTENANCE)
                .sorted((r1, r2) -> r1.getRoomId().compareTo(r2.getRoomId()))
                .collect(Collectors.toList());
        
        if (maintenanceRooms.isEmpty()) {
            System.out.println("No rooms under maintenance.");
            return;
        }
        
        System.out.printf("%-8s %-8s %-8s %-12s %-10s%n", 
                         "Room ID", "Block", "Floor", "Type", "Rent");
        System.out.println("-".repeat(50));
        
        maintenanceRooms.forEach(room -> {
            System.out.printf("%-8s %-8s %-8d %-12s $%-9.2f%n",
                             room.getRoomId(),
                             room.getBlockId(),
                             room.getFloorNumber(),
                             room.getRoomType(),
                             room.getMonthlyRent());
        });
        
        System.out.println("-".repeat(50));
        System.out.println("Total Rooms Under Maintenance: " + maintenanceRooms.size());
    }
    
    private void generateMaintenanceReport() {
        System.out.println("\n=== MAINTENANCE REPORT ===");
        
        long maintenanceRooms = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.MAINTENANCE)
                .count();
        
        double maintenancePercentage = rooms.size() > 0 ? 
                (double) maintenanceRooms / rooms.size() * 100 : 0;
        
        System.out.println("MAINTENANCE OVERVIEW:");
        System.out.println("- Rooms Under Maintenance: " + maintenanceRooms);
        System.out.println("- Maintenance Percentage: " + String.format("%.1f", maintenancePercentage) + "%");
        
        // Revenue impact
        double lostRevenue = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.MAINTENANCE)
                .mapToDouble(Room::getMonthlyRent)
                .sum();
        
        System.out.println("- Lost Monthly Revenue: $" + String.format("%.2f", lostRevenue));
        
        // Block-wise maintenance
        System.out.println("\nBLOCK-WISE MAINTENANCE:");
        hostelBlocks.values().forEach(block -> {
            long blockMaintenance = rooms.values().stream()
                    .filter(room -> room.getBlockId().equals(block.getBlockId()) && 
                                   room.getStatus() == RoomStatus.MAINTENANCE)
                    .count();
            
            if (blockMaintenance > 0) {
                System.out.println("- " + block.getBlockName() + ": " + blockMaintenance + " rooms");
            }
        });
    }
    
    private void searchMenu() {
        System.out.println("\n=== SEARCH & FILTER ===");
        System.out.println("1. Search Rooms");
        System.out.println("2. Search Students");
        System.out.println("3. Search Payments");
        System.out.println("4. Advanced Search");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: searchRoomsMenu(); break;
            case 2: searchStudentsMenu(); break;
            case 3: searchPaymentsMenu(); break;
            case 4: advancedSearchMenu(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void searchRoomsMenu() {
        System.out.println("\n=== SEARCH ROOMS ===");
        System.out.println("1. By Block");
        System.out.println("2. By Floor");
        System.out.println("3. By Room Type");
        System.out.println("4. By Status");
        System.out.println("5. By Rent Range");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: searchRoomsByBlock(); break;
            case 2: searchRoomsByFloor(); break;
            case 3: searchRoomsByType(); break;
            case 4: searchRoomsByStatus(); break;
            case 5: searchRoomsByRentRange(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void searchRoomsByBlock() {
        System.out.print("Enter Block ID: ");
        String blockId = scanner.nextLine();
        
        List<Room> results = rooms.values().stream()
                .filter(room -> room.getBlockId().equalsIgnoreCase(blockId))
                .collect(Collectors.toList());
        
        displayRoomSearchResults(results, "block '" + blockId + "'");
    }
    
    private void searchRoomsByFloor() {
        System.out.print("Enter Floor Number: ");
        try {
            int floor = Integer.parseInt(scanner.nextLine());
            
            List<Room> results = rooms.values().stream()
                    .filter(room -> room.getFloorNumber() == floor)
                    .collect(Collectors.toList());
            
            displayRoomSearchResults(results, "floor " + floor);
        } catch (NumberFormatException e) {
            System.out.println("Invalid floor number.");
        }
    }
    
    private void searchRoomsByType() {
        System.out.println("Select Room Type:");
        RoomType[] types = RoomType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice >= 1 && choice <= types.length) {
            RoomType selectedType = types[choice - 1];
            
            List<Room> results = rooms.values().stream()
                    .filter(room -> room.getRoomType() == selectedType)
                    .collect(Collectors.toList());
            
            displayRoomSearchResults(results, "type '" + selectedType + "'");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private void searchRoomsByStatus() {
        System.out.println("Select Room Status:");
        RoomStatus[] statuses = RoomStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice >= 1 && choice <= statuses.length) {
            RoomStatus selectedStatus = statuses[choice - 1];
            
            List<Room> results = rooms.values().stream()
                    .filter(room -> room.getStatus() == selectedStatus)
                    .collect(Collectors.toList());
            
            displayRoomSearchResults(results, "status '" + selectedStatus + "'");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private void searchRoomsByRentRange() {
        System.out.print("Enter minimum rent: $");
        try {
            double minRent = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter maximum rent: $");
            double maxRent = Double.parseDouble(scanner.nextLine());
            
            List<Room> results = rooms.values().stream()
                    .filter(room -> room.getMonthlyRent() >= minRent && room.getMonthlyRent() <= maxRent)
                    .collect(Collectors.toList());
            
            displayRoomSearchResults(results, "rent range $" + minRent + " - $" + maxRent);
        } catch (NumberFormatException e) {
            System.out.println("Invalid rent amount.");
        }
    }
    
    private void displayRoomSearchResults(List<Room> results, String criteria) {
        System.out.println("\n=== ROOM SEARCH RESULTS ===");
        System.out.println("Search criteria: " + criteria);
        System.out.println("Results found: " + results.size());
        
        if (results.isEmpty()) {
            System.out.println("No rooms found matching the criteria.");
            return;
        }
        
        System.out.printf("%-8s %-8s %-8s %-12s %-12s %-10s %-12s%n", 
                         "Room ID", "Block", "Floor", "Type", "Status", "Rent", "Occupant");
        System.out.println("-".repeat(75));
        
        results.forEach(room -> {
            System.out.printf("%-8s %-8s %-8d %-12s %-12s $%-9.2f %-12s%n",
                             room.getRoomId(),
                             room.getBlockId(),
                             room.getFloorNumber(),
                             room.getRoomType(),
                             room.getStatus(),
                             room.getMonthlyRent(),
                             room.getCurrentOccupant() != null ? room.getCurrentOccupant() : "None");
        });
    }
    
    private void searchStudentsMenu() {
        System.out.println("\n=== SEARCH STUDENTS ===");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        // Find student's allocation
        List<Allocation> studentAllocations = allocations.values().stream()
                .filter(alloc -> alloc.getStudentId().equals(studentId))
                .collect(Collectors.toList());
        
        if (studentAllocations.isEmpty()) {
            System.out.println("No allocations found for student: " + studentId);
            return;
        }
        
        System.out.println("Student: " + studentId);
        System.out.println("Allocations:");
        studentAllocations.forEach(alloc -> {
            Room room = read(alloc.getRoomId());
            System.out.println("- Room: " + alloc.getRoomId() + 
                             (room != null ? " (" + room.getRoomType() + ")" : "") +
                             ", Status: " + alloc.getStatus() +
                             ", Start: " + alloc.getStartDate());
        });
        
        // Find student's payments
        List<Payment> studentPayments = payments.values().stream()
                .filter(payment -> payment.getStudentId().equals(studentId))
                .collect(Collectors.toList());
        
        if (!studentPayments.isEmpty()) {
            double totalPaid = studentPayments.stream()
                    .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED)
                    .mapToDouble(Payment::getAmount)
                    .sum();
            
            System.out.println("Payments: " + studentPayments.size() + " payments, Total: $" + String.format("%.2f", totalPaid));
        }
    }
    
    private void searchPaymentsMenu() {
        System.out.println("\n=== SEARCH PAYMENTS ===");
        System.out.println("1. By Student ID");
        System.out.println("2. By Payment Type");
        System.out.println("3. By Date Range");
        System.out.println("4. By Amount Range");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: searchPaymentsByStudent(); break;
            case 2: searchPaymentsByType(); break;
            case 3: searchPaymentsByDateRange(); break;
            case 4: searchPaymentsByAmountRange(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void searchPaymentsByStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        List<Payment> results = payments.values().stream()
                .filter(payment -> payment.getStudentId().equals(studentId))
                .collect(Collectors.toList());
        
        displayPaymentSearchResults(results, "student '" + studentId + "'");
    }
    
    private void searchPaymentsByType() {
        System.out.println("Select Payment Type:");
        PaymentType[] types = PaymentType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice >= 1 && choice <= types.length) {
            PaymentType selectedType = types[choice - 1];
            
            List<Payment> results = payments.values().stream()
                    .filter(payment -> payment.getPaymentType() == selectedType)
                    .collect(Collectors.toList());
            
            displayPaymentSearchResults(results, "type '" + selectedType + "'");
        } else {
            System.out.println("Invalid choice.");
        }
    }
    
    private void searchPaymentsByDateRange() {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();
        
        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            
            List<Payment> results = payments.values().stream()
                    .filter(payment -> payment.getPaymentDate() != null &&
                                     !payment.getPaymentDate().isBefore(startDate) &&
                                     !payment.getPaymentDate().isAfter(endDate))
                    .collect(Collectors.toList());
            
            displayPaymentSearchResults(results, "date range " + startDate + " to " + endDate);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }
    
    private void searchPaymentsByAmountRange() {
        System.out.print("Enter minimum amount: $");
        try {
            double minAmount = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter maximum amount: $");
            double maxAmount = Double.parseDouble(scanner.nextLine());
            
            List<Payment> results = payments.values().stream()
                    .filter(payment -> payment.getAmount() >= minAmount && payment.getAmount() <= maxAmount)
                    .collect(Collectors.toList());
            
            displayPaymentSearchResults(results, "amount range $" + minAmount + " - $" + maxAmount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }
    
    private void displayPaymentSearchResults(List<Payment> results, String criteria) {
        System.out.println("\n=== PAYMENT SEARCH RESULTS ===");
        System.out.println("Search criteria: " + criteria);
        System.out.println("Results found: " + results.size());
        
        if (results.isEmpty()) {
            System.out.println("No payments found matching the criteria.");
            return;
        }
        
        System.out.printf("%-8s %-10s %-10s %-12s %-12s %-12s%n", 
                         "Pay ID", "Student", "Amount", "Type", "Date", "Status");
        System.out.println("-".repeat(70));
        
        results.forEach(payment -> {
            System.out.printf("%-8s %-10s $%-9.2f %-12s %-12s %-12s%n",
                             payment.getPaymentId(),
                             payment.getStudentId(),
                             payment.getAmount(),
                             payment.getPaymentType().toString().length() > 10 ? 
                                 payment.getPaymentType().toString().substring(0, 10) + ".." : payment.getPaymentType(),
                             payment.getPaymentDate(),
                             payment.getStatus());
        });
        
        double totalAmount = results.stream().mapToDouble(Payment::getAmount).sum();
        System.out.println("-".repeat(70));
        System.out.println("Total Amount: $" + String.format("%.2f", totalAmount));
    }
    
    private void advancedSearchMenu() {
        System.out.println("\n=== ADVANCED SEARCH ===");
        System.out.println("Enter search criteria (leave blank to skip):");
        
        System.out.print("Block ID: ");
        String blockFilter = scanner.nextLine();
        
        System.out.print("Room Type (SINGLE/DOUBLE/TRIPLE/SUITE): ");
        String typeFilter = scanner.nextLine();
        
        System.out.print("Room Status (AVAILABLE/OCCUPIED/MAINTENANCE): ");
        String statusFilter = scanner.nextLine();
        
        System.out.print("Maximum Rent: $");
        String maxRentStr = scanner.nextLine();
        Double maxRent = maxRentStr.isEmpty() ? null : Double.parseDouble(maxRentStr);
        
        List<Room> results = rooms.values().stream()
                .filter(room -> blockFilter.isEmpty() || 
                               room.getBlockId().toLowerCase().contains(blockFilter.toLowerCase()))
                .filter(room -> typeFilter.isEmpty() || 
                               room.getRoomType().toString().toLowerCase().contains(typeFilter.toLowerCase()))
                .filter(room -> statusFilter.isEmpty() || 
                               room.getStatus().toString().toLowerCase().contains(statusFilter.toLowerCase()))
                .filter(room -> maxRent == null || room.getMonthlyRent() <= maxRent)
                .collect(Collectors.toList());
        
        displayRoomSearchResults(results, "advanced search criteria");
    }
    
    private void displaySystemStatistics() {
        System.out.println("\n=== HOSTEL SYSTEM STATISTICS ===");
        
        // Basic statistics
        System.out.println("OVERVIEW:");
        System.out.println("- Total Rooms: " + rooms.size());
        System.out.println("- Total Blocks: " + hostelBlocks.size());
        System.out.println("- Total Allocations: " + allocations.size());
        System.out.println("- Total Payments: " + payments.size());
        
        // Room statistics
        long occupiedRooms = rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.OCCUPIED).count();
        long availableRooms = rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.AVAILABLE).count();
        long maintenanceRooms = rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.MAINTENANCE).count();
        
        System.out.println("\nROOM STATUS:");
        System.out.println("- Occupied: " + occupiedRooms);
        System.out.println("- Available: " + availableRooms);
        System.out.println("- Under Maintenance: " + maintenanceRooms);
        System.out.println("- Occupancy Rate: " + 
                          String.format("%.1f", (double) occupiedRooms / rooms.size() * 100) + "%");
        
        // Financial statistics
        double totalRevenue = payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED)
                .mapToDouble(Payment::getAmount)
                .sum();
        
        double currentMonthlyRevenue = rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.OCCUPIED)
                .mapToDouble(Room::getMonthlyRent)
                .sum();
        
        System.out.println("\nFINANCIAL OVERVIEW:");
        System.out.println("- Total Revenue Collected: $" + String.format("%.2f", totalRevenue));
        System.out.println("- Current Monthly Revenue: $" + String.format("%.2f", currentMonthlyRevenue));
        
        // Room type distribution
        System.out.println("\nROOM TYPE DISTRIBUTION:");
        Map<RoomType, Long> typeDistribution = rooms.values().stream()
                .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));
        
        typeDistribution.entrySet().stream()
                .sorted(Map.Entry.<RoomType, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        
        logger.log("Hostel system statistics displayed");
    }
    
    // Utility methods for external access
    public int getTotalRooms() { return rooms.size(); }
    public int getAvailableRooms() { return (int) rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.AVAILABLE).count(); }
    public int getOccupiedRooms() { return (int) rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.OCCUPIED).count(); }
    public int getTotalAllocations() { return allocations.size(); }
    public int getActiveAllocations() { return (int) allocations.values().stream().filter(a -> a.getStatus() == AllocationStatus.ACTIVE).count(); }
    
    public double getTotalRevenue() {
        return payments.values().stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.COMPLETED)
                .mapToDouble(Payment::getAmount)
                .sum();
    }
    
    public double getCurrentMonthlyRevenue() {
        return rooms.values().stream()
                .filter(room -> room.getStatus() == RoomStatus.OCCUPIED)
                .mapToDouble(Room::getMonthlyRent)
                .sum();
    }
    
    public double getOccupancyRate() {
        if (rooms.isEmpty()) return 0.0;
        long occupied = rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.OCCUPIED).count();
        return (double) occupied / rooms.size() * 100;
    }
}