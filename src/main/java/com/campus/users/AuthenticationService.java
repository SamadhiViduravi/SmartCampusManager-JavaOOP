package com.campus.users;

import com.campus.utils.Logger;
import java.util.Map;
import java.util.HashMap;

/**
 * Service class for handling user authentication
 * Demonstrates service layer pattern
 */
public class AuthenticationService {
    private static final Logger logger = Logger.getInstance();
    private Map<String, String> userCredentials;
    
    public AuthenticationService() {
        this.userCredentials = new HashMap<>();
        initializeDefaultCredentials();
    }
    
    private void initializeDefaultCredentials() {
        // Default credentials for testing
        userCredentials.put("ADMIN001", "admin123");
        userCredentials.put("STU001", "student123");
        userCredentials.put("LEC001", "lecturer123");
    }
    
    public User authenticate(String userId, String password, Map<String, User> users) {
        if (!users.containsKey(userId)) {
            logger.log("Authentication failed: User not found - " + userId);
            return null;
        }
        
        String storedPassword = userCredentials.get(userId);
        if (storedPassword == null) {
            // Default password for new users
            storedPassword = "default123";
        }
        
        if (storedPassword.equals(password)) {
            User user = users.get(userId);
            logger.log("Authentication successful: " + userId);
            return user;
        } else {
            logger.log("Authentication failed: Invalid password - " + userId);
            return null;
        }
    }
    
    public void setPassword(String userId, String password) {
        userCredentials.put(userId, password);
        logger.log("Password updated for user: " + userId);
    }
    
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        String currentPassword = userCredentials.get(userId);
        if (currentPassword != null && currentPassword.equals(oldPassword)) {
            userCredentials.put(userId, newPassword);
            logger.log("Password changed for user: " + userId);
            return true;
        }
        logger.log("Password change failed for user: " + userId);
        return false;
    }
    
    public void resetPassword(String userId) {
        userCredentials.put(userId, "reset123");
        logger.log("Password reset for user: " + userId);
    }
}