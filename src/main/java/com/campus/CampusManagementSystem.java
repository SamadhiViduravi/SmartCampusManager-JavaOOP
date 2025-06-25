package com.campus;

import com.campus.students.StudentManager;
import com.campus.courses.CourseManager;
import com.campus.library.LibraryManager;
import com.campus.transport.TransportManager;
import com.campus.hostel.HostelManager;
import com.campus.inventory.InventoryManager;
import com.campus.exams.ExamManager;
import com.campus.events.EventManager;
import com.campus.reports.ReportGenerator;
import com.campus.utils.Logger;
import com.campus.utils.NotificationService;

import java.util.Scanner;

/**
 * Main application class for Smart Campus Management System
 */
public class CampusManagementSystem {
    private static final Logger logger = Logger.getInstance();
    private static final NotificationService notificationService = NotificationService.getInstance();
    
    private StudentManager studentManager;
    private CourseManager courseManager;
    private LibraryManager libraryManager;
    private TransportManager transportManager;
    private HostelManager hostelManager;
    private InventoryManager inventoryManager;
    private ExamManager examManager;
    private EventManager eventManager;
    private ReportGenerator reportGenerator;
    
    private Scanner scanner;
    private boolean running;
    
    public CampusManagementSystem() {
        this.scanner = new Scanner(System.in);
        this.running = true;
        initializeManagers();
        logger.info("Smart Campus Management System initialized");
    }
    
    private void initializeManagers() {
        this.studentManager = new StudentManager();
        this.courseManager = new CourseManager();
        this.libraryManager = new LibraryManager();
        this.transportManager = new TransportManager();
        this.hostelManager = new HostelManager();
        this.inventoryManager = new InventoryManager();
        this.examManager = new ExamManager();
        this.eventManager = new EventManager();
        this.reportGenerator = new ReportGenerator(studentManager, courseManager, libraryManager,
                                                  transportManager, hostelManager, inventoryManager,
                                                  examManager, eventManager);
        
        logger.info("All managers initialized successfully");
    }
    
    public void start() {
        displayWelcomeMessage();
        
        while (running) {
            displayMainMenu();
            int choice = getChoice();
            handleMenuChoice(choice);
        }
        
        displayGoodbyeMessage();
    }
    
    private void displayWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║           🎓 SMART CAMPUS MANAGEMENT SYSTEM 🎓              ║");
        System.out.println("║                                                              ║");
        System.out.println("║              Welcome to the Future of Education             ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("🌟 Comprehensive Campus Management Solution");
        System.out.println("📚 Managing Students, Courses, Library, Transport & More!");
        System.out.println();
    }
    
    private void displayMainMenu() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        MAIN MENU                            ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. 👥 Student Management                                   ║");
        System.out.println("║  2. 📖 Course Management                                    ║");
        System.out.println("║  3. 📚 Library Management                                   ║");
        System.out.println("║  4. 🚌 Transport Management                                 ║");
        System.out.println("║  5. 🏠 Hostel Management                                    ║");
        System.out.println("║  6. 📦 Inventory Management                                 ║");
        System.out.println("║  7. 📝 Exam Management                                      ║");
        System.out.println("║  8. 🎉 Event Management                                     ║");
        System.out.println("║  9. 📊 System Reports                                       ║");
        System.out.println("║ 10. ⚙️  System Settings                                     ║");
        System.out.println("║  0. 🚪 Exit System                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.print("Enter your choice (0-10): ");
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void handleMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    logger.info("Accessing Student Management");
                    studentManager.displayMenu();
                    break;
                case 2:
                    logger.info("Accessing Course Management");
                    courseManager.displayMenu();
                    break;
                case 3:
                    logger.info("Accessing Library Management");
                    libraryManager.displayMenu();
                    break;
                case 4:
                    logger.info("Accessing Transport Management");
                    transportManager.displayMenu();
                    break;
                case 5:
                    logger.info("Accessing Hostel Management");
                    hostelManager.displayMenu();
                    break;
                case 6:
                    logger.info("Accessing Inventory Management");
                    inventoryManager.displayMenu();
                    break;
                case 7:
                    logger.info("Accessing Exam Management");
                    examManager.displayMenu();
                    break;
                case 8:
                    logger.info("Accessing Event Management");
                    eventManager.displayMenu();
                    break;
                case 9:
                    logger.info("Accessing System Reports");
                    displaySystemReports();
                    break;
                case 10:
                    logger.info("Accessing System Settings");
                    displaySystemSettings();
                    break;
                case 0:
                    logger.info("User requested system exit");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please enter a number between 0 and 10.");
            }
        } catch (Exception e) {
            logger.error("Error handling menu choice: " + e.getMessage());
            System.out.println("❌ An error occurred. Please try again.");
        }
    }
    
    private void displaySystemReports() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      SYSTEM REPORTS                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        System.out.println("\n📊 SYSTEM OVERVIEW:");
        System.out.println("- Total Students: " + studentManager.getTotalStudents());
        System.out.println("- Total Courses: " + courseManager.getTotalCourses());
        System.out.println("- Library Books: " + libraryManager.getTotalBooks());
        System.out.println("- Transport Vehicles: " + transportManager.getTotalVehicles());
        System.out.println("- Hostel Rooms: " + hostelManager.getTotalRooms());
        System.out.println("- Inventory Items: " + inventoryManager.getTotalItems());
        System.out.println("- Total Exams: " + examManager.getTotalExams());
        System.out.println("- Total Events: " + eventManager.getTotalEvents());
        
        System.out.println("\n📈 QUICK STATISTICS:");
        System.out.println("- Active Students: " + studentManager.getActiveStudents());
        System.out.println("- Active Courses: " + courseManager.getActiveCourses());
        System.out.println("- Available Books: " + libraryManager.getAvailableBooks());
        System.out.println("- Active Vehicles: " + transportManager.getActiveVehicles());
        System.out.println("- Available Rooms: " + hostelManager.getAvailableRooms());
        System.out.println("- Low Stock Items: " + inventoryManager.getLowStockCount());
        System.out.println("- Upcoming Exams: " + examManager.getUpcomingExams().size());
        System.out.println("- Upcoming Events: " + eventManager.getUpcomingEvents().size());
        
        System.out.println("\n🔔 RECENT NOTIFICATIONS:");
        notificationService.getNotifications().stream()
                .limit(5)
                .forEach(System.out::println);
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void displaySystemSettings() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     SYSTEM SETTINGS                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        System.out.println("\n⚙️ SYSTEM CONFIGURATION:");
        System.out.println("1. View System Logs");
        System.out.println("2. Clear System Logs");
        System.out.println("3. View Notifications");
        System.out.println("4. Clear Notifications");
        System.out.println("5. System Information");
        System.out.println("0. Back to Main Menu");
        
        System.out.print("Enter choice: ");
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                displaySystemLogs();
                break;
            case 2:
                logger.clearLogs();
                System.out.println("✅ System logs cleared.");
                break;
            case 3:
                displayNotifications();
                break;
            case 4:
                notificationService.clearNotifications();
                System.out.println("✅ Notifications cleared.");
                break;
            case 5:
                displaySystemInformation();
                break;
            case 0:
                return;
            default:
                System.out.println("❌ Invalid choice.");
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private void displaySystemLogs() {
        System.out.println("\n📋 SYSTEM LOGS:");
        logger.getLogs().stream()
                .limit(20)
                .forEach(System.out::println);
    }
    
    private void displayNotifications() {
        System.out.println("\n🔔 SYSTEM NOTIFICATIONS:");
        notificationService.getNotifications().forEach(System.out::println);
    }
    
    private void displaySystemInformation() {
        System.out.println("\n💻 SYSTEM INFORMATION:");
        System.out.println("- System Name: Smart Campus Management System");
        System.out.println("- Version: 1.0.0");
        System.out.println("- Java Version: " + System.getProperty("java.version"));
        System.out.println("- Operating System: " + System.getProperty("os.name"));
        System.out.println("- Available Memory: " + 
                          (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB");
        System.out.println("- Total Modules: 8");
        System.out.println("- Database: In-Memory (HashMap-based)");
        System.out.println("- Status: ✅ All systems operational");
    }
    
    private void displayGoodbyeMessage() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║                    Thank You for Using                      ║");
        System.out.println("║           🎓 SMART CAMPUS MANAGEMENT SYSTEM 🎓              ║");
        System.out.println("║                                                              ║");
        System.out.println("║                 Have a Great Day! 🌟                        ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        logger.info("Smart Campus Management System shutdown completed");
    }
    
    public static void main(String[] args) {
        try {
            CampusManagementSystem system = new CampusManagementSystem();
            system.start();
        } catch (Exception e) {
            System.err.println("❌ Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}