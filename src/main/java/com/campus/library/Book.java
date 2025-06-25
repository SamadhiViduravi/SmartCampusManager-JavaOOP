package com.campus.library;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Book entity class representing library books
 * Demonstrates encapsulation and composition
 */
public class Book implements Identifiable {
    private String bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publicationDate;
    private BookCategory category;
    private BookStatus status;
    private String location;
    private double price;
    private int totalCopies;
    private int availableCopies;
    private String description;
    private String language;
    private int pages;
    private String edition;
    private LocalDateTime addedDate;
    private LocalDateTime lastUpdated;
    
    public Book(String bookId, String isbn, String title, String author, String publisher) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.status = BookStatus.AVAILABLE;
        this.category = BookCategory.GENERAL;
        this.totalCopies = 1;
        this.availableCopies = 1;
        this.addedDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.language = "English";
    }
    
    public boolean isAvailable() {
        return availableCopies > 0 && status == BookStatus.AVAILABLE;
    }
    
    public void issueBook() {
        if (!isAvailable()) {
            throw new IllegalStateException("Book is not available for issue");
        }
        availableCopies--;
        if (availableCopies == 0) {
            status = BookStatus.ISSUED;
        }
        lastUpdated = LocalDateTime.now();
    }
    
    public void returnBook() {
        if (availableCopies >= totalCopies) {
            throw new IllegalStateException("All copies are already available");
        }
        availableCopies++;
        if (status == BookStatus.ISSUED && availableCopies > 0) {
            status = BookStatus.AVAILABLE;
        }
        lastUpdated = LocalDateTime.now();
    }
    
    public void addCopies(int copies) {
        if (copies <= 0) {
            throw new IllegalArgumentException("Number of copies must be positive");
        }
        totalCopies += copies;
        availableCopies += copies;
        if (status == BookStatus.ISSUED && availableCopies > 0) {
            status = BookStatus.AVAILABLE;
        }
        lastUpdated = LocalDateTime.now();
    }
    
    public void removeCopies(int copies) {
        if (copies <= 0) {
            throw new IllegalArgumentException("Number of copies must be positive");
        }
        if (copies > availableCopies) {
            throw new IllegalStateException("Cannot remove more copies than available");
        }
        totalCopies -= copies;
        availableCopies -= copies;
        if (availableCopies == 0) {
            status = BookStatus.ISSUED;
        }
        lastUpdated = LocalDateTime.now();
    }
    
    public void displayBookInfo() {
        System.out.println("=== BOOK INFORMATION ===");
        System.out.println("Book ID: " + bookId);
        System.out.println("ISBN: " + isbn);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Publisher: " + publisher);
        System.out.println("Publication Date: " + publicationDate);
        System.out.println("Category: " + category);
        System.out.println("Status: " + status);
        System.out.println("Location: " + location);
        System.out.println("Price: $" + String.format("%.2f", price));
        System.out.println("Total Copies: " + totalCopies);
        System.out.println("Available Copies: " + availableCopies);
        System.out.println("Language: " + language);
        System.out.println("Pages: " + pages);
        System.out.println("Edition: " + edition);
        System.out.println("Added Date: " + addedDate);
        System.out.println("Last Updated: " + lastUpdated);
    }
    
    // Getters and Setters
    @Override
    public String getId() { return bookId; }
    
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { 
        this.bookId = bookId;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { 
        this.isbn = isbn;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { 
        this.author = author;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { 
        this.publisher = publisher;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { 
        this.publicationDate = publicationDate;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public BookCategory getCategory() { return category; }
    public void setCategory(BookCategory category) { 
        this.category = category;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { 
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { 
        this.location = location;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { 
        this.price = price;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { 
        this.language = language;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public int getPages() { return pages; }
    public void setPages(int pages) { 
        this.pages = pages;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public String getEdition() { return edition; }
    public void setEdition(String edition) { 
        this.edition = edition;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public LocalDateTime getAddedDate() { return addedDate; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) || Objects.equals(isbn, book.isbn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bookId, isbn);
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status=" + status +
                ", availableCopies=" + availableCopies +
                '}';
    }
}