package com.campus.users;

import com.campus.utils.Logger;
import java.util.Arrays;
import java.util.List;

/**
 * Admin user class with full system privileges
 * Demonstrates inheritance and polymorphism
 */
public class Admin extends User {
    private static final Logger logger = Logger.getInstance();
    private List<String> adminPermissions;
    private String adminLevel;
    
    public Admin(String userId, String firstName, String lastName, String email) {
        super(userId, firstName, lastName, email, UserRole.ADMIN);
        this.adminLevel = "SUPER_ADMIN";
        initializePermissions();
        logger.log("Admin created: " + getFullName());
    }
    
    public Admin(String userId, String firstName, String lastName, String email, String adminLevel) {
        super(userId, firstName, lastName, email, UserRole.ADMIN);
        this.adminLevel = adminLevel;
        initializePermissions();
        logger.log("Admin created: " + getFullName() + " with level: " + adminLevel);
    }
    
    private void initializePermissions() {
        adminPermissions = Arrays.asList(
            "USER_MANAGEMENT",
            "COURSE_MANAGEMENT",
            "LIBRARY_MANAGEMENT",
            "TRANSPORT_MANAGEMENT",
            "HOSTEL_MANAGEMENT",
            "INVENTORY_MANAGEMENT",
            "EXAM_MANAGEMENT",
            "EVENT_MANAGEMENT",
            "REPORT_GENERATION",
            "SYSTEM_CONFIGURATION",
            "BACKUP_RESTORE",
            "AUDIT_LOGS"
        );
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== ADMIN PROFILE ===");
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Admin Level: " + adminLevel);
        System.out.println("Created: " + createdAt);
        System.out.println("Last Login: " + lastLoginAt);
        System.out.println("Status: " + (isActive ? "Active" : "Inactive"));
        System.out.println("Permissions: " + adminPermissions.size() + " permissions");
    }
    
    @Override
    public boolean hasPermission(String permission) {
        return adminPermissions.contains(permission.toUpperCase());
    }
    
    @Override
    public void performRoleSpecificAction() {
        System.out.println("Admin " + getFullName() + " is performing system administration tasks...");
        manageSystemSettings();
        generateSystemReports();
        monitorSystemHealth();
    }
    
    public void manageSystemSettings() {
        System.out.println("Managing system configuration settings...");
        logger.log("Admin " + userId + " accessed system settings");
    }
    
    public void generateSystemReports() {
        System.out.println("Generating comprehensive system reports...");
        logger.log("Admin " + userId + " generated system reports");
    }
    
    public void monitorSystemHealth() {
        System.out.println("Monitoring system health and performance...");
        logger.log("Admin " + userId + " checked system health");
    }
    
    public void createUser(User user) {
        System.out.println("Creating new user: " + user.getFullName());
        logger.log("Admin " + userId + " created user: " + user.getUserId());
    }
    
    public void deleteUser(String userId) {
        System.out.println("Deleting user: " + userId);
        logger.log("Admin " + this.userId + " deleted user: " + userId);
    }
    
    public void backupSystem() {
        System.out.println("Initiating system backup...");
        logger.log("Admin " + userId + " initiated system backup");
    }
    
    public void restoreSystem(String backupId) {
        System.out.println("Restoring system from backup: " + backupId);
        logger.log("Admin " + userId + " restored system from backup: " + backupId);
    }
    
    // Getters and Setters
    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
    
    public List<String> getAdminPermissions() { return adminPermissions; }
}