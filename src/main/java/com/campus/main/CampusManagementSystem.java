package com.campus.main;

import com.campus.users.*;
import com.campus.courses.*;
import com.campus.library.*;
import com.campus.transport.*;
import com.campus.hostel.*;
import com.campus.inventory.*;
import com.campus.exams.*;
import com.campus.reports.*;
import com.campus.utils.*;
import com.campus.events.*;

import java.util.Scanner;

/**
 * Main entry point for the Smart Campus Management System
 * Demonstrates comprehensive OOP principles and design patterns
 */
public class CampusManagementSystem {
    private static final Logger logger = Logger.getInstance();
    private static final Scanner scanner = new Scanner(System.in);
    
    private UserManager userManager;
    private CourseManager courseManager;
    private LibraryManager libraryManager;
    private TransportManager transportManager;
    private HostelManager hostelManager;
    private InventoryManager inventoryManager;
    private ExamManager examManager;
    private EventManager eventManager;
    private ReportGenerator reportGenerator;
    
    public CampusManagementSystem() {
        initializeManagers();
        logger.log("Campus Management System initialized successfully");
    }
    
    private void initializeManagers() {
        userManager = new UserManager();
        courseManager = new CourseManager();
        libraryManager = new LibraryManager();
        transportManager = new TransportManager();
        hostelManager = new HostelManager();
        inventoryManager = new InventoryManager();
        examManager = new ExamManager();
        eventManager = new EventManager();
        reportGenerator = new ReportGenerator();
    }
    
    public void start() {
        System.out.println("=== Welcome to Smart Campus Management System ===");
        
        while (true) {
            displayMainMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    handleUserManagement();
                    break;
                case 2:
                    handleCourseManagement();
                    break;
                case 3:
                    handleLibraryManagement();
                    break;
                case 4:
                    handleTransportManagement();
                    break;
                case 5:
                    handleHostelManagement();
                    break;
                case 6:
                    handleInventoryManagement();
                    break;
                case 7:
                    handleExamManagement();
                    break;
                case 8:
                    handleEventManagement();
                    break;
                case 9:
                    handleReports();
                    break;
                case 0:
                    System.out.println("Thank you for using Campus Management System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. User Management");
        System.out.println("2. Course Management");
        System.out.println("3. Library Management");
        System.out.println("4. Transport Management");
        System.out.println("5. Hostel Management");
        System.out.println("6. Inventory Management");
        System.out.println("7. Exam Management");
        System.out.println("8. Event Management");
        System.out.println("9. Reports & Analytics");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void handleUserManagement() {
        userManager.displayMenu();
    }
    
    private void handleCourseManagement() {
        courseManager.displayMenu();
    }
    
    private void handleLibraryManagement() {
        libraryManager.displayMenu();
    }
    
    private void handleTransportManagement() {
        transportManager.displayMenu();
    }
    
    private void handleHostelManagement() {
        hostelManager.displayMenu();
    }
    
    private void handleInventoryManagement() {
        inventoryManager.displayMenu();
    }
    
    private void handleExamManagement() {
        examManager.displayMenu();
    }
    
    private void handleEventManagement() {
        eventManager.displayMenu();
    }
    
    private void handleReports() {
        reportGenerator.displayMenu();
    }
    
    public static void main(String[] args) {
        CampusManagementSystem system = new CampusManagementSystem();
        system.start();
    }
}