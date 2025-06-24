package com.campus.users;

import com.campus.library.Book;
import com.campus.library.Transaction;
import com.campus.utils.Logger;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Library Staff user class with library-specific functionality
 */
public class LibraryStaff extends User {
    private static final Logger logger = Logger.getInstance();
    
    private String employeeId;
    private String position;
    private String workShift;
    private LocalDate joiningDate;
    private List<String> libraryPermissions;
    private int booksProcessedToday;
    private double salary;
    
    public LibraryStaff(String userId, String firstName, String lastName, String email,
                        String employeeId, String position) {
        super(userId, firstName, lastName, email, UserRole.LIBRARY_STAFF);
        this.employeeId = employeeId;
        this.position = position;
        this.joiningDate = LocalDate.now();
        this.workShift = "DAY";
        this.booksProcessedToday = 0;
        initializePermissions();
        logger.log("Library Staff created: " + getFullName() + " (" + employeeId + ")");
    }
    
    private void initializePermissions() {
        libraryPermissions = new ArrayList<>();
        libraryPermissions.add("MANAGE_BOOKS");
        libraryPermissions.add("ISSUE_BOOKS");
        libraryPermissions.add("RETURN_BOOKS");
        libraryPermissions.add("MANAGE_MEMBERS");
        libraryPermissions.add("CALCULATE_FINES");
        libraryPermissions.add("GENERATE_REPORTS");
        libraryPermissions.add("CATALOG_BOOKS");
        libraryPermissions.add("RESERVE_BOOKS");
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== LIBRARY STAFF PROFILE ===");
        System.out.println("User ID: " + userId);
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Position: " + position);
        System.out.println("Work Shift: " + workShift);
        System.out.println("Salary: $" + String.format("%.2f", salary));
        System.out.println("Joining Date: " + joiningDate);
        System.out.println("Books Processed Today: " + booksProcessedToday);
        System.out.println("Created: " + createdAt);
        System.out.println("Last Login: " + lastLoginAt);
    }
    
    @Override
    public boolean hasPermission(String permission) {
        return libraryPermissions.contains(permission.toUpperCase());
    }
    
    @Override
    public void performRoleSpecificAction() {
        System.out.println("Library Staff " + getFullName() + " is managing library operations...");
        processBookTransactions();
        manageCatalog();
        assistPatrons();
    }
    
    public void issueBook(Book book, User member) {
        System.out.println("Issuing book: " + book.getTitle() + " to " + member.getFullName());
        booksProcessedToday++;
        logger.log("Library Staff " + employeeId + " issued book: " + book.getIsbn() + " to user: " + member.getUserId());
    }
    
    public void returnBook(Book book, User member) {
        System.out.println("Processing return of book: " + book.getTitle() + " from " + member.getFullName());
        booksProcessedToday++;
        logger.log("Library Staff " + employeeId + " processed return of book: " + book.getIsbn() + " from user: " + member.getUserId());
    }
    
    public void addBookToCatalog(Book book) {
        System.out.println("Adding new book to catalog: " + book.getTitle());
        logger.log("Library Staff " + employeeId + " added book to catalog: " + book.getIsbn());
    }
    
    public void removeBookFromCatalog(Book book) {
        System.out.println("Removing book from catalog: " + book.getTitle());
        logger.log("Library Staff " + employeeId + " removed book from catalog: " + book.getIsbn());
    }
    
    public double calculateFine(Transaction transaction) {
        System.out.println("Calculating fine for transaction: " + transaction.getTransactionId());
        // Fine calculation logic would be here
        double fine = 0.0; // Placeholder
        logger.log("Library Staff " + employeeId + " calculated fine: $" + fine);
        return fine;
    }
    
    public void processBookTransactions() {
        System.out.println("=== PROCESSING BOOK TRANSACTIONS ===");
        System.out.println("Processing daily book issues and returns...");
    }
    
    public void manageCatalog() {
        System.out.println("=== MANAGING CATALOG ===");
        System.out.println("Updating and maintaining book catalog...");
    }
    
    public void assistPatrons() {
        System.out.println("=== ASSISTING PATRONS ===");
        System.out.println("Helping library visitors and members...");
    }
    
    public void generateDailyReport() {
        System.out.println("=== DAILY REPORT ===");
        System.out.println("Books processed today: " + booksProcessedToday);
        System.out.println("Generating detailed daily report...");
        logger.log("Library Staff " + employeeId + " generated daily report");
    }
    
    public void reserveBook(Book book, User member) {
        System.out.println("Reserving book: " + book.getTitle() + " for " + member.getFullName());
        logger.log("Library Staff " + employeeId + " reserved book: " + book.getIsbn() + " for user: " + member.getUserId());
    }
    
    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getWorkShift() { return workShift; }
    public void setWorkShift(String workShift) { this.workShift = workShift; }
    
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    
    public int getBooksProcessedToday() { return booksProcessedToday; }
    public void setBooksProcessedToday(int booksProcessedToday) { this.booksProcessedToday = booksProcessedToday; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    public List<String> getLibraryPermissions() { return new ArrayList<>(libraryPermissions); }
}