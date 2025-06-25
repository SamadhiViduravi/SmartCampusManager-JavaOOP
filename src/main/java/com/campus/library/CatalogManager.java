package com.campus.library;

import com.campus.utils.Logger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager class for handling library catalog operations
 * Demonstrates composition and search algorithms
 */
public class CatalogManager {
    private static final Logger logger = Logger.getInstance();
    
    private Map<String, Book> bookCatalog;
    private Map<String, List<Book>> authorIndex;
    private Map<String, List<Book>> titleIndex;
    private Map<BookCategory, List<Book>> categoryIndex;
    private Map<String, List<Book>> publisherIndex;
    
    public CatalogManager() {
        this.bookCatalog = new HashMap<>();
        this.authorIndex = new HashMap<>();
        this.titleIndex = new HashMap<>();
        this.categoryIndex = new HashMap<>();
        this.publisherIndex = new HashMap<>();
        initializeIndexes();
        logger.log("CatalogManager initialized");
    }
    
    private void initializeIndexes() {
        for (BookCategory category : BookCategory.values()) {
            categoryIndex.put(category, new ArrayList<>());
        }
    }
    
    public void addBook(Book book) {
        bookCatalog.put(book.getBookId(), book);
        updateIndexes(book, true);
        logger.log("Book added to catalog: " + book.getBookId());
    }
    
    public void removeBook(String bookId) {
        Book book = bookCatalog.remove(bookId);
        if (book != null) {
            updateIndexes(book, false);
            logger.log("Book removed from catalog: " + bookId);
        }
    }
    
    public Book getBook(String bookId) {
        return bookCatalog.get(bookId);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookCatalog.values());
    }
    
    private void updateIndexes(Book book, boolean add) {
        if (add) {
            // Add to indexes
            authorIndex.computeIfAbsent(book.getAuthor().toLowerCase(), k -> new ArrayList<>()).add(book);
            titleIndex.computeIfAbsent(book.getTitle().toLowerCase(), k -> new ArrayList<>()).add(book);
            categoryIndex.get(book.getCategory()).add(book);
            publisherIndex.computeIfAbsent(book.getPublisher().toLowerCase(), k -> new ArrayList<>()).add(book);
        } else {
            // Remove from indexes
            removeFromIndex(authorIndex, book.getAuthor().toLowerCase(), book);
            removeFromIndex(titleIndex, book.getTitle().toLowerCase(), book);
            categoryIndex.get(book.getCategory()).remove(book);
            removeFromIndex(publisherIndex, book.getPublisher().toLowerCase(), book);
        }
    }
    
    private void removeFromIndex(Map<String, List<Book>> index, String key, Book book) {
        List<Book> books = index.get(key);
        if (books != null) {
            books.remove(book);
            if (books.isEmpty()) {
                index.remove(key);
            }
        }
    }
    
    public List<Book> searchByTitle(String title) {
        String searchKey = title.toLowerCase();
        return titleIndex.entrySet().stream()
                .filter(entry -> entry.getKey().contains(searchKey))
                .flatMap(entry -> entry.getValue().stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<Book> searchByAuthor(String author) {
        String searchKey = author.toLowerCase();
        return authorIndex.entrySet().stream()
                .filter(entry -> entry.getKey().contains(searchKey))
                .flatMap(entry -> entry.getValue().stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<Book> searchByISBN(String isbn) {
        return bookCatalog.values().stream()
                .filter(book -> book.getIsbn().contains(isbn))
                .collect(Collectors.toList());
    }
    
    public List<Book> searchByCategory(BookCategory category) {
        return new ArrayList<>(categoryIndex.get(category));
    }
    
    public List<Book> searchByPublisher(String publisher) {
        String searchKey = publisher.toLowerCase();
        return publisherIndex.entrySet().stream()
                .filter(entry -> entry.getKey().contains(searchKey))
                .flatMap(entry -> entry.getValue().stream())
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<Book> searchBooks(String searchTerm) {
        String term = searchTerm.toLowerCase();
        Set<Book> results = new HashSet<>();
        
        // Search in title
        results.addAll(searchByTitle(term));
        
        // Search in author
        results.addAll(searchByAuthor(term));
        
        // Search in ISBN
        results.addAll(searchByISBN(term));
        
        // Search in publisher
        results.addAll(searchByPublisher(term));
        
        return new ArrayList<>(results);
    }
    
    public List<Book> getAvailableBooks() {
        return bookCatalog.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
    
    public List<Book> getBooksByStatus(BookStatus status) {
        return bookCatalog.values().stream()
                .filter(book -> book.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    public Map<BookCategory, Long> getCategoryStatistics() {
        return bookCatalog.values().stream()
                .collect(Collectors.groupingBy(Book::getCategory, Collectors.counting()));
    }
    
    public Map<BookStatus, Long> getStatusStatistics() {
        return bookCatalog.values().stream()
                .collect(Collectors.groupingBy(Book::getStatus, Collectors.counting()));
    }
    
    public List<Book> getPopularBooks(int limit) {
        // This would typically be based on issue frequency
        // For now, returning books with fewer available copies (more popular)
        return bookCatalog.values().stream()
                .filter(book -> book.getTotalCopies() > 0)
                .sorted((b1, b2) -> Double.compare(
                    (double) b1.getAvailableCopies() / b1.getTotalCopies(),
                    (double) b2.getAvailableCopies() / b2.getTotalCopies()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    public List<Book> getNewArrivals(int days) {
        return bookCatalog.values().stream()
                .filter(book -> book.getAddedDate().isAfter(
                    java.time.LocalDateTime.now().minusDays(days)))
                .sorted((b1, b2) -> b2.getAddedDate().compareTo(b1.getAddedDate()))
                .collect(Collectors.toList());
    }
    
    public void displayCatalogStatistics() {
        System.out.println("=== CATALOG STATISTICS ===");
        System.out.println("Total Books: " + bookCatalog.size());
        System.out.println("Available Books: " + getAvailableBooks().size());
        
        System.out.println("\nBooks by Category:");
        getCategoryStatistics().forEach((category, count) -> 
            System.out.println("- " + category + ": " + count));
        
        System.out.println("\nBooks by Status:");
        getStatusStatistics().forEach((status, count) -> 
            System.out.println("- " + status + ": " + count));
        
        System.out.println("\nTotal Authors: " + authorIndex.size());
        System.out.println("Total Publishers: " + publisherIndex.size());
    }
    
    public boolean bookExists(String bookId) {
        return bookCatalog.containsKey(bookId);
    }
    
    public boolean isbnExists(String isbn) {
        return bookCatalog.values().stream()
                .anyMatch(book -> book.getIsbn().equals(isbn));
    }
    
    public int getTotalBooks() {
        return bookCatalog.size();
    }
    
    public int getTotalCopies() {
        return bookCatalog.values().stream()
                .mapToInt(Book::getTotalCopies)
                .sum();
    }
    
    public int getAvailableCopies() {
        return bookCatalog.values().stream()
                .mapToInt(Book::getAvailableCopies)
                .sum();
    }
}