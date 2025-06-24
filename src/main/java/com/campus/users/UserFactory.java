package com.campus.users;

import java.util.Scanner;

/**
 * Factory pattern implementation for creating different types of users
 * Demonstrates Factory Design Pattern
 */
public class UserFactory {
    
    public User createUser(int userType, String userId, String firstName, String lastName, 
                          String email, Scanner scanner) {
        switch (userType) {
            case 1:
                return createStudent(userId, firstName, lastName, email, scanner);
            case 2:
                return createLecturer(userId, firstName, lastName, email, scanner);
            case 3:
                return createLibraryStaff(userId, firstName, lastName, email, scanner);
            case 4:
                return createAdmin(userId, firstName, lastName, email, scanner);
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
    
    private Student createStudent(String userId, String firstName, String lastName, 
                                 String email, Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        System.out.print("Enter Program: ");
        String program = scanner.nextLine();
        
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        
        return new Student(userId, firstName, lastName, email, studentId, program, department);
    }
    
    private Lecturer createLecturer(String userId, String firstName, String lastName, 
                                   String email, Scanner scanner) {
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        
        System.out.print("Enter Designation: ");
        String designation = scanner.nextLine();
        
        return new Lecturer(userId, firstName, lastName, email, employeeId, department, designation);
    }
    
    private LibraryStaff createLibraryStaff(String userId, String firstName, String lastName, 
                                           String email, Scanner scanner) {
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        
        System.out.print("Enter Position: ");
        String position = scanner.nextLine();
        
        return new LibraryStaff(userId, firstName, lastName, email, employeeId, position);
    }
    
    private Admin createAdmin(String userId, String firstName, String lastName, 
                             String email, Scanner scanner) {
        System.out.print("Enter Admin Level (SUPER_ADMIN/ADMIN/MODERATOR): ");
        String adminLevel = scanner.nextLine();
        
        return new Admin(userId, firstName, lastName, email, adminLevel);
    }
}