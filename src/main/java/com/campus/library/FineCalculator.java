package com.campus.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for calculating library fines
 * Demonstrates strategy pattern for different fine calculation methods
 */
public class FineCalculator {
    
    private static final double STANDARD_DAILY_FINE = 1.0;
    private static final double REFERENCE_DAILY_FINE = 2.0;
    private static final double JOURNAL_DAILY_FINE = 0.5;
    private static final double MAX_FINE_PER_BOOK = 50.0;
    
    public static double calculateFine(Transaction transaction) {
        if (transaction.getTransactionType() != TransactionType.ISSUE) {
            return 0.0;
        }
        
        LocalDate dueDate = transaction.getDueDate();
        LocalDate returnDate = transaction.getReturnDate();
        
        if (returnDate == null) {
            returnDate = LocalDate.now();
        }
        
        if (!returnDate.isAfter(dueDate)) {
            return 0.0; // No fine if returned on time
        }
        
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        double dailyFine = getDailyFineRate(transaction.getBook().getCategory());
        double totalFine = daysOverdue * dailyFine;
        
        // Cap the fine at maximum amount
        return Math.min(totalFine, MAX_FINE_PER_BOOK);
    }
    
    public static double calculateCurrentFine(Transaction transaction) {
        if (transaction.getTransactionType() != TransactionType.ISSUE || 
            transaction.getReturnDate() != null) {
            return 0.0;
        }
        
        LocalDate dueDate = transaction.getDueDate();
        LocalDate currentDate = LocalDate.now();
        
        if (!currentDate.isAfter(dueDate)) {
            return 0.0;
        }
        
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, currentDate);
        double dailyFine = getDailyFineRate(transaction.getBook().getCategory());
        double totalFine = daysOverdue * dailyFine;
        
        return Math.min(totalFine, MAX_FINE_PER_BOOK);
    }
    
    private static double getDailyFineRate(BookCategory category) {
        switch (category) {
            case REFERENCE:
                return REFERENCE_DAILY_FINE;
            case JOURNAL:
            case MAGAZINE:
                return JOURNAL_DAILY_FINE;
            default:
                return STANDARD_DAILY_FINE;
        }
    }
    
    public static double calculateMemberTotalFines(LibraryMember member) {
        return member.getTransactionHistory().stream()
                .filter(t -> t.getTransactionType() == TransactionType.ISSUE)
                .mapToDouble(FineCalculator::calculateFine)
                .sum();
    }
    
    public static double calculateMemberCurrentFines(LibraryMember member) {
        return member.getTransactionHistory().stream()
                .filter(t -> t.getTransactionType() == TransactionType.ISSUE)
                .filter(t -> t.getReturnDate() == null)
                .mapToDouble(FineCalculator::calculateCurrentFine)
                .sum();
    }
    
    public static boolean isFineWaivable(Transaction transaction, String reason) {
        // Business logic for fine waiver
        if (transaction.getFineAmount() <= 5.0) {
            return true; // Small fines can be waived
        }
        
        if ("FIRST_OFFENSE".equals(reason) && transaction.getFineAmount() <= 10.0) {
            return true;
        }
        
        if ("SYSTEM_ERROR".equals(reason) || "LIBRARY_CLOSURE".equals(reason)) {
            return true;
        }
        
        return false;
    }
    
    public static void displayFineStructure() {
        System.out.println("=== LIBRARY FINE STRUCTURE ===");
        System.out.println("Standard Books: $" + STANDARD_DAILY_FINE + " per day");
        System.out.println("Reference Books: $" + REFERENCE_DAILY_FINE + " per day");
        System.out.println("Journals/Magazines: $" + JOURNAL_DAILY_FINE + " per day");
        System.out.println("Maximum Fine per Book: $" + MAX_FINE_PER_BOOK);
        System.out.println("Grace Period: None");
        System.out.println("Fine Calculation: From day after due date");
    }
}