package com.campus.utils;

/**
 * Utility class for string operations
 */
public class StringUtils {
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static String capitalizeWords(String str) {
        if (isEmpty(str)) return str;
        
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) result.append(" ");
            result.append(capitalize(words[i]));
        }
        
        return result.toString();
    }
    
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    public static String padLeft(String str, int length, char padChar) {
        if (str == null) str = "";
        while (str.length() < length) {
            str = padChar + str;
        }
        return str;
    }
    
    public static String padRight(String str, int length, char padChar) {
        if (str == null) str = "";
        while (str.length() < length) {
            str = str + padChar;
        }
        return str;
    }
    
    public static String generateId(String prefix, int number, int digits) {
        return prefix + padLeft(String.valueOf(number), digits, '0');
    }
    
    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) return email;
        
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) return email;
        
        String masked = username.substring(0, 2) + "*".repeat(username.length() - 2);
        return masked + "@" + domain;
    }
    
    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() < 4) return phone;
        
        String cleaned = phone.replaceAll("[^0-9]", "");
        if (cleaned.length() < 4) return phone;
        
        String masked = "*".repeat(cleaned.length() - 4) + cleaned.substring(cleaned.length() - 4);
        return masked;
    }
    
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }
    
    public static String removeSpecialCharacters(String str) {
        if (isEmpty(str)) return str;
        return str.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
    
    public static String toSnakeCase(String str) {
        if (isEmpty(str)) return str;
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    public static String toCamelCase(String str) {
        if (isEmpty(str)) return str;
        
        String[] words = str.split("[_\\s]+");
        StringBuilder result = new StringBuilder(words[0].toLowerCase());
        
        for (int i = 1; i < words.length; i++) {
            result.append(capitalize(words[i]));
        }
        
        return result.toString();
    }
}