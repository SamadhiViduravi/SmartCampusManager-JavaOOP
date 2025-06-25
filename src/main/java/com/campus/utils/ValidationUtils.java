package com.campus.utils;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    
    private static final Pattern ID_PATTERN = 
        Pattern.compile("^[A-Z]{1,3}\\d{3,6}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.replaceAll("[\\s()-]", "")).matches();
    }
    
    public static boolean isValidId(String id) {
        return id != null && ID_PATTERN.matcher(id).matches();
    }
    
    public static boolean isValidGPA(double gpa) {
        return gpa >= 0.0 && gpa <= 4.0;
    }
    
    public static boolean isValidPercentage(double percentage) {
        return percentage >= 0.0 && percentage <= 100.0;
    }
    
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    public static boolean isPositive(int number) {
        return number > 0;
    }
    
    public static boolean isPositive(double number) {
        return number > 0.0;
    }
    
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
}