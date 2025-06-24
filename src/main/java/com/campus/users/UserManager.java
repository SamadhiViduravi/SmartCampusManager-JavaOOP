package com.campus.users;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager class for handling all user operations
 * Demonstrates composition and dependency injection
 */
public class UserManager implements Manageable<User> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    
    private Map<String, User> users;
    private UserFactory userFactory;
    private AuthenticationService authService;
    private NotificationService notificationService;
    
    public UserManager() {
        this.users = new HashMap<>();
        this.userFactory = new UserFactory();
        this.authService = new AuthenticationService();
        this.notificationService = NotificationService.getInstance();
        initializeDefaultUsers();
        logger.log("UserManager initialized");
    }
    
    private void initializeDefaultUsers() {
        // Create default admin user
        Admin defaultAdmin = new Admin("ADMIN001", "System", "Administrator", "admin@campus.edu");
        users.put(defaultAdmin.getUserId(), defaultAdmin);
        
        // Create sample users for testing
        Student sampleStudent = new Student("STU001", "John", "Doe", "john.doe@student.campus.edu", 
                                          "2024001", "Computer Science", "Engineering");
        users.put(sampleStudent.getUserId(), sampleStudent);
        
        Lecturer sampleLecturer = new Lecturer("LEC001", "Dr. Jane", "Smith", "jane.smith@campus.edu",
                                             "EMP001", "Computer Science", "Professor");
        users.put(sampleLecturer.getUserId(), sampleLecturer);
        
        logger.log("Default users initialized");
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== USER MANAGEMENT MENU ===");
            System.out.println("1. Create User");
            System.out.println("2. View All Users");
            System.out.println("3. Search User");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. User Login");
            System.out.println("7. View User Profile");
            System.out.println("8. Generate User Report");
            System.out.println("9. Bulk User Operations");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getChoice();
            
            switch (choice) {
                case 1: createUserInteractive(); break;
                case 2: viewAllUsers(); break;
                case 3: searchUserInteractive(); break;
                case 4: updateUserInteractive(); break;
                case 5: deleteUserInteractive(); break;
                case 6: loginUserInteractive(); break;
                case 7: viewUserProfileInteractive(); break;
                case 8: generateUserReport(); break;
                case 9: bulkUserOperations(); break;
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
    public void create(User user) {
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User with ID " + user.getUserId() + " already exists");
        }
        users.put(user.getUserId(), user);
        notificationService.notifyObservers("User created: " + user.getFullName());
        logger.log("User created: " + user.getUserId());
    }
    
    @Override
    public User read(String userId) {
        return users.get(userId);
    }
    
    @Override
    public void update(User user) {
        if (!users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User with ID " + user.getUserId() + " does not exist");
        }
        users.put(user.getUserId(), user);
        notificationService.notifyObservers("User updated: " + user.getFullName());
        logger.log("User updated: " + user.getUserId());
    }
    
    @Override
    public void delete(String userId) {
        User removedUser = users.remove(userId);
        if (removedUser != null) {
            notificationService.notifyObservers("User deleted: " + removedUser.getFullName());
            logger.log("User deleted: " + userId);
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist");
        }
    }
    
    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
    
    public void createUserInteractive() {
        System.out.println("\n=== CREATE NEW USER ===");
        System.out.println("Select user type:");
        System.out.println("1. Student");
        System.out.println("2. Lecturer");
        System.out.println("3. Library Staff");
        System.out.println("4. Admin");
        System.out.print("Enter choice: ");
        
        int userType = getChoice();
        
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        try {
            User newUser = userFactory.createUser(userType, userId, firstName, lastName, email, scanner);
            create(newUser);
            System.out.println("User created successfully!");
            newUser.displayProfile();
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }
    
    public void viewAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-25s %-15s %-10s%n", 
                         "User ID", "Full Name", "Email", "Role", "Status");
        System.out.println("-".repeat(80));
        
        for (User user : users.values()) {
            System.out.printf("%-10s %-20s %-25s %-15s %-10s%n",
                             user.getUserId(),
                             user.getFullName(),
                             user.getEmail(),
                             user.getRole(),
                             user.isActive() ? "Active" : "Inactive");
        }
    }
    
    public void searchUserInteractive() {
        System.out.println("\n=== SEARCH USER ===");
        System.out.print("Enter search term (ID, name, or email): ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        List<User> results = searchUsers(searchTerm);
        
        if (results.isEmpty()) {
            System.out.println("No users found matching: " + searchTerm);
        } else {
            System.out.println("Found " + results.size() + " user(s):");
            for (User user : results) {
                System.out.println("- " + user.getUserId() + ": " + user.getFullName() + " (" + user.getEmail() + ")");
            }
        }
    }
    
    public List<User> searchUsers(String searchTerm) {
        return users.values().stream()
                .filter(user -> 
                    user.getUserId().toLowerCase().contains(searchTerm) ||
                    user.getFullName().toLowerCase().contains(searchTerm) ||
                    user.getEmail().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    public void updateUserInteractive() {
        System.out.println("\n=== UPDATE USER ===");
        System.out.print("Enter User ID to update: ");
        String userId = scanner.nextLine();
        
        User user = read(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        System.out.println("Current user details:");
        user.displayProfile();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Email");
        System.out.println("2. Phone Number");
        System.out.println("3. Address");
        System.out.println("4. Status (Active/Inactive)");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new email: ");
                user.setEmail(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new phone number: ");
                user.setPhoneNumber(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new address: ");
                user.setAddress(scanner.nextLine());
                break;
            case 4:
                System.out.print("Set active status (true/false): ");
                user.setActive(Boolean.parseBoolean(scanner.nextLine()));
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        update(user);
        System.out.println("User updated successfully!");
    }
    
    public void deleteUserInteractive() {
        System.out.println("\n=== DELETE USER ===");
        System.out.print("Enter User ID to delete: ");
        String userId = scanner.nextLine();
        
        User user = read(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        System.out.println("User to be deleted:");
        user.displayProfile();
        
        System.out.print("Are you sure you want to delete this user? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if ("yes".equalsIgnoreCase(confirmation)) {
            delete(userId);
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    public void loginUserInteractive() {
        System.out.println("\n=== USER LOGIN ===");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        User user = authService.authenticate(userId, password, users);
        if (user != null) {
            System.out.println("Login successful!");
            user.updateLastLogin();
            user.displayProfile();
            user.performRoleSpecificAction();
        } else {
            System.out.println("Invalid credentials.");
        }
    }
    
    public void viewUserProfileInteractive() {
        System.out.println("\n=== VIEW USER PROFILE ===");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        
        User user = read(userId);
        if (user != null) {
            user.displayProfile();
        } else {
            System.out.println("User not found.");
        }
    }
    
    public void generateUserReport() {
        System.out.println("\n=== USER STATISTICS REPORT ===");
        
        Map<UserRole, Long> roleCount = users.values().stream()
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));
        
        long activeUsers = users.values().stream()
                .mapToLong(user -> user.isActive() ? 1 : 0)
                .sum();
        
        System.out.println("Total Users: " + users.size());
        System.out.println("Active Users: " + activeUsers);
        System.out.println("Inactive Users: " + (users.size() - activeUsers));
        
        System.out.println("\nUsers by Role:");
        for (Map.Entry<UserRole, Long> entry : roleCount.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
        
        logger.log("User report generated");
    }
    
    public void bulkUserOperations() {
        System.out.println("\n=== BULK USER OPERATIONS ===");
        System.out.println("1. Activate All Users");
        System.out.println("2. Deactivate All Users");
        System.out.println("3. Export User Data");
        System.out.println("4. Import User Data");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                bulkActivateUsers();
                break;
            case 2:
                bulkDeactivateUsers();
                break;
            case 3:
                exportUserData();
                break;
            case 4:
                importUserData();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void bulkActivateUsers() {
        users.values().forEach(user -> user.setActive(true));
        System.out.println("All users activated successfully!");
        logger.log("Bulk user activation performed");
    }
    
    private void bulkDeactivateUsers() {
        users.values().forEach(user -> user.setActive(false));
        System.out.println("All users deactivated successfully!");
        logger.log("Bulk user deactivation performed");
    }
    
    private void exportUserData() {
        System.out.println("Exporting user data...");
        // Export implementation would be here
        logger.log("User data exported");
    }
    
    private void importUserData() {
        System.out.println("Importing user data...");
        // Import implementation would be here
        logger.log("User data imported");
    }
    
    // Utility methods
    public List<User> getUsersByRole(UserRole role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }
    
    public List<User> getActiveUsers() {
        return users.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }
    
    public boolean userExists(String userId) {
        return users.containsKey(userId);
    }
    
    public int getTotalUserCount() {
        return users.size();
    }
    
    public Map<String, User> getAllUsersMap() {
        return new HashMap<>(users);
    }
}