package com.campus.library;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

/**
 * Manager class for handling all library operations
 */
public class LibraryManager implements Manageable<Book> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    private Map<String, Book> books;
    private Map<String, BorrowRecord> borrowRecords;
    private NotificationService notificationService;
    private int borrowCounter;

    public LibraryManager() {
        this.books = new HashMap<>();
        this.borrowRecords = new HashMap<>();
        this.notificationService = NotificationService.getInstance();
        this.borrowCounter = 1;
        initializeSampleData();
        logger.log("LibraryManager initialized");
    }

    private void initializeSampleData() {
        // Create sample books
        Book book1 = new Book("B001", "Introduction to Java Programming", "John Smith");
        book1.setIsbn("978-0134685991");
        book1.setCategory(BookCategory.TEXTBOOK);
        book1.setPublisher("Pearson");
        book1.setPublicationYear(2020);
        book1.setTotalCopies(5);
        book1.setAvailableCopies(3);
        books.put("B001", book1);

        Book book2 = new Book("B002", "Data Structures and Algorithms", "Robert Sedgewick");
        book2.setIsbn("978-0321573513");
        book2.setCategory(BookCategory.TEXTBOOK);
        book2.setPublisher("Addison-Wesley");
        book2.setPublicationYear(2019);
        book2.setTotalCopies(4);
        book2.setAvailableCopies(4);
        books.put("B002", book2);

        Book book3 = new Book("B003", "The Great Gatsby", "F. Scott Fitzgerald");
        book3.setIsbn("978-0743273565");
        book3.setCategory(BookCategory.FICTION);
        book3.setPublisher("Scribner");
        book3.setPublicationYear(2004);
        book3.setTotalCopies(3);
        book3.setAvailableCopies(2);
        books.put("B003", book3);

        Book book4 = new Book("B004", "A Brief History of Time", "Stephen Hawking");
        book4.setIsbn("978-0553380163");
        book4.setCategory(BookCategory.SCIENCE);
        book4.setPublisher("Bantam");
        book4.setPublicationYear(1998);
        book4.setTotalCopies(2);
        book4.setAvailableCopies(1);
        books.put("B004", book4);

        Book book5 = new Book("B005", "Clean Code", "Robert C. Martin");
        book5.setIsbn("978-0132350884");
        book5.setCategory(BookCategory.TECHNOLOGY);
        book5.setPublisher("Prentice Hall");
        book5.setPublicationYear(2008);
        book5.setTotalCopies(6);
        book5.setAvailableCopies(4);
        books.put("B005", book5);

        // Create sample borrow records
        BorrowRecord record1 = new BorrowRecord("BR001", "S001", "B001", LocalDate.now().minusDays(10));
        record1.setStatus(BorrowStatus.BORROWED);
        borrowRecords.put("BR001", record1);

        BorrowRecord record2 = new BorrowRecord("BR002", "S002", "B004", LocalDate.now().minusDays(5));
        record2.setStatus(BorrowStatus.BORROWED);
        borrowRecords.put("BR002", record2);

        logger.log("Sample library data initialized");
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== LIBRARY MANAGEMENT MENU ===");
            System.out.println("1. Book Management");
            System.out.println("2. Borrow/Return Books");
            System.out.println("3. Search Books");
            System.out.println("4. View All Books");
            System.out.println("5. Borrow Records");
            System.out.println("6. Library Reports");
            System.out.println("7. Library Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getChoice();

            switch (choice) {
                case 1: bookManagementMenu(); break;
                case 2: borrowReturnMenu(); break;
                case 3: searchBooksMenu(); break;
                case 4: viewAllBooks(); break;
                case 5: borrowRecordsMenu(); break;
                case 6: libraryReportsMenu(); break;
                case 7: displayLibraryStatistics(); break;
                case 0: return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void create(Book book) {
        books.put(book.getBookId(), book);
        notificationService.notifyObservers("New book added: " + book.getTitle());
        logger.log("Book created: " + book.getBookId());
    }

    @Override
    public Book read(String bookId) {
        return books.get(bookId);
    }

    @Override
    public void update(Book book) {
        books.put(book.getBookId(), book);
        notificationService.notifyObservers("Book updated: " + book.getTitle());
        logger.log("Book updated: " + book.getBookId());
    }

    @Override
    public void delete(String bookId) {
        Book book = books.remove(bookId);
        if (book != null) {
            notificationService.notifyObservers("Book deleted: " + book.getTitle());
            logger.log("Book deleted: " + bookId);
        }
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    // Utility methods for external access
    public int getTotalBooks() {
        return books.values().stream()
                .mapToInt(Book::getTotalCopies)
                .sum();
    }

    public int getAvailableBooks() {
        return books.values().stream()
                .mapToInt(Book::getAvailableCopies)
                .sum();
    }

    public int getBorrowedBooks() {
        return getTotalBooks() - getAvailableBooks();
    }

    public int getTotalTitles() {
        return books.size();
    }

    public List<Book> getOverdueBooks() {
        LocalDate today = LocalDate.now();
        return borrowRecords.values().stream()
                .filter(record -> record.getStatus() == BorrowStatus.BORROWED)
                .filter(record -> record.getDueDate() != null && record.getDueDate().isBefore(today))
                .map(record -> books.get(record.getBookId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void bookManagementMenu() {
        System.out.println("\n=== BOOK MANAGEMENT ===");
        System.out.println("1. Add New Book");
        System.out.println("2. Update Book");
        System.out.println("3. Delete Book");
        System.out.println("4. View Book Details");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: addNewBookInteractive(); break;
            case 2: updateBookInteractive(); break;
            case 3: deleteBookInteractive(); break;
            case 4: viewBookDetailsInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void addNewBookInteractive() {
        System.out.println("\n=== ADD NEW BOOK ===");

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        if (books.containsKey(bookId)) {
            System.out.println("Book ID already exists.");
            return;
        }

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Author: ");
        String author = scanner.nextLine();

        Book newBook = new Book(bookId, title, author);

        System.out.print("Enter ISBN (optional): ");
        String isbn = scanner.nextLine();
        if (!isbn.trim().isEmpty()) {
            newBook.setIsbn(isbn);
        }

        System.out.println("Select Category:");
        BookCategory[] categories = BookCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Enter choice: ");
        int categoryChoice = getChoice();
        if (categoryChoice >= 1 && categoryChoice <= categories.length) {
            newBook.setCategory(categories[categoryChoice - 1]);
        }

        System.out.print("Enter Publisher (optional): ");
        String publisher = scanner.nextLine();
        if (!publisher.trim().isEmpty()) {
            newBook.setPublisher(publisher);
        }

        System.out.print("Enter Publication Year: ");
        try {
            int year = Integer.parseInt(scanner.nextLine());
            newBook.setPublicationYear(year);
        } catch (NumberFormatException e) {
            System.out.println("Invalid year, using current year.");
            newBook.setPublicationYear(LocalDate.now().getYear());
        }

        System.out.print("Enter Total Copies: ");
        try {
            int totalCopies = Integer.parseInt(scanner.nextLine());
            newBook.setTotalCopies(totalCopies);
            newBook.setAvailableCopies(totalCopies);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, using default (1).");
            newBook.setTotalCopies(1);
            newBook.setAvailableCopies(1);
        }

        create(newBook);
        System.out.println("Book added successfully!");
        newBook.displayBookInfo();
    }

    private void updateBookInteractive() {
        System.out.println("\n=== UPDATE BOOK ===");
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        Book book = read(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Current book information:");
        book.displayBookInfo();

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. ISBN");
        System.out.println("4. Category");
        System.out.println("5. Publisher");
        System.out.println("6. Publication Year");
        System.out.println("7. Total Copies");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1:
                System.out.print("Enter new title: ");
                book.setTitle(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new author: ");
                book.setAuthor(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new ISBN: ");
                book.setIsbn(scanner.nextLine());
                break;
            case 4:
                System.out.println("Select new category:");
                BookCategory[] categories = BookCategory.values();
                for (int i = 0; i < categories.length; i++) {
                    System.out.println((i + 1) + ". " + categories[i]);
                }
                System.out.print("Enter choice: ");
                int categoryChoice = getChoice();
                if (categoryChoice >= 1 && categoryChoice <= categories.length) {
                    book.setCategory(categories[categoryChoice - 1]);
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }
                break;
            case 5:
                System.out.print("Enter new publisher: ");
                book.setPublisher(scanner.nextLine());
                break;
            case 6:
                System.out.print("Enter new publication year: ");
                try {
                    book.setPublicationYear(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid year format.");
                    return;
                }
                break;
            case 7:
                System.out.print("Enter new total copies: ");
                try {
                    int newTotal = Integer.parseInt(scanner.nextLine());
                    int currentBorrowed = book.getTotalCopies() - book.getAvailableCopies();
                    if (newTotal >= currentBorrowed) {
                        book.setTotalCopies(newTotal);
                        book.setAvailableCopies(newTotal - currentBorrowed);
                    } else {
                        System.out.println("Cannot reduce copies below currently borrowed amount (" + currentBorrowed + ").");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        update(book);
        System.out.println("Book updated successfully!");
    }

    private void deleteBookInteractive() {
        System.out.println("\n=== DELETE BOOK ===");
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        Book book = read(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        // Check if book has borrowed copies
        int borrowedCopies = book.getTotalCopies() - book.getAvailableCopies();
        if (borrowedCopies > 0) {
            System.out.println("Cannot delete book. " + borrowedCopies + " copies are currently borrowed.");
            return;
        }

        System.out.println("Book to be deleted:");
        book.displayBookInfo();

        System.out.print("Are you sure you want to delete this book? (yes/no): ");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation)) {
            delete(bookId);
            System.out.println("Book deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void viewBookDetailsInteractive() {
        System.out.println("\n=== BOOK DETAILS ===");
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        Book book = read(bookId);
        if (book != null) {
            book.displayBookInfo();
        } else {
            System.out.println("Book not found.");
        }
    }

    private void borrowReturnMenu() {
        System.out.println("\n=== BORROW/RETURN BOOKS ===");
        System.out.println("1. Borrow Book");
        System.out.println("2. Return Book");
        System.out.println("3. Renew Book");
        System.out.println("4. View Borrowed Books");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: borrowBookInteractive(); break;
            case 2: returnBookInteractive(); break;
            case 3: renewBookInteractive(); break;
            case 4: viewBorrowedBooksInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void borrowBookInteractive() {
        System.out.println("\n=== BORROW BOOK ===");

        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        Book book = read(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (book.getAvailableCopies() <= 0) {
            System.out.println("No copies available for borrowing.");
            return;
        }

        // Create borrow record
        String recordId = "BR" + String.format("%03d", borrowCounter++);
        BorrowRecord record = new BorrowRecord(recordId, studentId, bookId, LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks loan period
        record.setStatus(BorrowStatus.BORROWED);

        // Update book availability
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        update(book);

        borrowRecords.put(recordId, record);

        System.out.println("Book borrowed successfully!");
        System.out.println("Borrow Record ID: " + recordId);
        System.out.println("Due Date: " + record.getDueDate());
    }

    private void returnBookInteractive() {
        System.out.println("\n=== RETURN BOOK ===");

        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        // Find active borrow record
        BorrowRecord activeRecord = borrowRecords.values().stream()
                .filter(record -> record.getStudentId().equals(studentId) &&
                        record.getBookId().equals(bookId) &&
                        record.getStatus() == BorrowStatus.BORROWED)
                .findFirst()
                .orElse(null);

        if (activeRecord == null) {
            System.out.println("No active borrow record found for this student and book.");
            return;
        }

        Book book = read(bookId);
        if (book != null) {
            // Update book availability
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            update(book);

            // Update borrow record
            activeRecord.setReturnDate(LocalDate.now());
            activeRecord.setStatus(BorrowStatus.RETURNED);

            // Calculate fine if overdue
            if (activeRecord.getDueDate().isBefore(LocalDate.now())) {
                long daysOverdue = LocalDate.now().toEpochDay() - activeRecord.getDueDate().toEpochDay();
                double fine = daysOverdue * 0.50; // $0.50 per day
                activeRecord.setFineAmount(fine);
                System.out.println("Book returned successfully!");
                System.out.println("Overdue fine: $" + String.format("%.2f", fine));
            } else {
                System.out.println("Book returned successfully!");
            }
        }
    }

    private void renewBookInteractive() {
        System.out.println("\n=== RENEW BOOK ===");

        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();

        // Find active borrow record
        BorrowRecord activeRecord = borrowRecords.values().stream()
                .filter(record -> record.getStudentId().equals(studentId) &&
                        record.getBookId().equals(bookId) &&
                        record.getStatus() == BorrowStatus.BORROWED)
                .findFirst()
                .orElse(null);

        if (activeRecord == null) {
            System.out.println("No active borrow record found for this student and book.");
            return;
        }

        // Check if already renewed
        if (activeRecord.isRenewed()) {
            System.out.println("Book has already been renewed once. Cannot renew again.");
            return;
        }

        // Renew for another 14 days
        activeRecord.setDueDate(activeRecord.getDueDate().plusDays(14));
        activeRecord.setRenewed(true);

        System.out.println("Book renewed successfully!");
        System.out.println("New due date: " + activeRecord.getDueDate());
    }

    private void viewBorrowedBooksInteractive() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        List<BorrowRecord> studentRecords = borrowRecords.values().stream()
                .filter(record -> record.getStudentId().equals(studentId) &&
                        record.getStatus() == BorrowStatus.BORROWED)
                .collect(Collectors.toList());

        System.out.println("\n=== BORROWED BOOKS ===");
        System.out.println("Student ID: " + studentId);
        System.out.println("Currently Borrowed: " + studentRecords.size());

        if (!studentRecords.isEmpty()) {
            System.out.printf("%-8s %-25s %-12s %-12s %-8s%n",
                    "Book ID", "Title", "Borrow Date", "Due Date", "Status");
            System.out.println("-".repeat(70));

            studentRecords.forEach(record -> {
                Book book = books.get(record.getBookId());
                String title = book != null ? book.getTitle() : "Unknown";
                String status = record.getDueDate().isBefore(LocalDate.now()) ? "OVERDUE" : "ACTIVE";

                System.out.printf("%-8s %-25s %-12s %-12s %-8s%n",
                        record.getBookId(),
                        title.length() > 23 ? title.substring(0, 23) + ".." : title,
                        record.getBorrowDate(),
                        record.getDueDate(),
                        status);
            });
        }
    }

    private void searchBooksMenu() {
        System.out.println("\n=== SEARCH BOOKS ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by Category");
        System.out.println("4. Search by ISBN");
        System.out.println("5. Advanced Search");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: searchByTitle(); break;
            case 2: searchByAuthor(); break;
            case 3: searchByCategory(); break;
            case 4: searchByIsbn(); break;
            case 5: advancedSearch(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void searchByTitle() {
        System.out.print("Enter title to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<Book> results = books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        displaySearchResults(results, "title containing '" + searchTerm + "'");
    }

    private void searchByAuthor() {
        System.out.print("Enter author to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<Book> results = books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        displaySearchResults(results, "author containing '" + searchTerm + "'");
    }

    private void searchByCategory() {
        System.out.println("Select category:");
        BookCategory[] categories = BookCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Enter choice: ");

        int choice = getChoice();
        if (choice >= 1 && choice <= categories.length) {
            BookCategory selectedCategory = categories[choice - 1];

            List<Book> results = books.values().stream()
                    .filter(book -> book.getCategory() == selectedCategory)
                    .collect(Collectors.toList());

            displaySearchResults(results, "category '" + selectedCategory + "'");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void searchByIsbn() {
        System.out.print("Enter ISBN to search: ");
        String isbn = scanner.nextLine();

        List<Book> results = books.values().stream()
                .filter(book -> book.getIsbn() != null && book.getIsbn().contains(isbn))
                .collect(Collectors.toList());

        displaySearchResults(results, "ISBN containing '" + isbn + "'");
    }

    private void advancedSearch() {
        System.out.println("\n=== ADVANCED SEARCH ===");
        System.out.println("Enter search criteria (leave blank to skip):");

        System.out.print("Title: ");
        String titleFilter = scanner.nextLine().toLowerCase();

        System.out.print("Author: ");
        String authorFilter = scanner.nextLine().toLowerCase();

        System.out.print("Publisher: ");
        String publisherFilter = scanner.nextLine().toLowerCase();

        System.out.print("Publication Year: ");
        String yearStr = scanner.nextLine();
        Integer yearFilter = yearStr.isEmpty() ? null : Integer.parseInt(yearStr);

        List<Book> results = books.values().stream()
                .filter(book -> titleFilter.isEmpty() ||
                        book.getTitle().toLowerCase().contains(titleFilter))
                .filter(book -> authorFilter.isEmpty() ||
                        book.getAuthor().toLowerCase().contains(authorFilter))
                .filter(book -> publisherFilter.isEmpty() ||
                        (book.getPublisher() != null && book.getPublisher().toLowerCase().contains(publisherFilter)))
                .filter(book -> yearFilter == null || book.getPublicationYear() == yearFilter)
                .collect(Collectors.toList());

        displaySearchResults(results, "advanced search criteria");
    }

    private void displaySearchResults(List<Book> results, String criteria) {
        System.out.println("\n=== SEARCH RESULTS ===");
        System.out.println("Search criteria: " + criteria);
        System.out.println("Results found: " + results.size());

        if (results.isEmpty()) {
            System.out.println("No books found matching the criteria.");
            return;
        }

        System.out.printf("%-8s %-25s %-20s %-15s %-8s %-8s%n",
                "ID", "Title", "Author", "Category", "Total", "Available");
        System.out.println("-".repeat(90));

        results.forEach(book -> {
            System.out.printf("%-8s %-25s %-20s %-15s %-8d %-8d%n",
                    book.getBookId(),
                    book.getTitle().length() > 23 ? book.getTitle().substring(0, 23) + ".." : book.getTitle(),
                    book.getAuthor().length() > 18 ? book.getAuthor().substring(0, 18) + ".." : book.getAuthor(),
                    book.getCategory() != null ? book.getCategory().toString() : "N/A",
                    book.getTotalCopies(),
                    book.getAvailableCopies());
        });
    }

    private void viewAllBooks() {
        System.out.println("\n=== ALL BOOKS ===");
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.printf("%-8s %-25s %-20s %-15s %-8s %-8s%n",
                "ID", "Title", "Author", "Category", "Total", "Available");
        System.out.println("-".repeat(90));

        books.values().stream()
                .sorted((b1, b2) -> b1.getBookId().compareTo(b2.getBookId()))
                .forEach(book -> {
                    System.out.printf("%-8s %-25s %-20s %-15s %-8d %-8d%n",
                            book.getBookId(),
                            book.getTitle().length() > 23 ? book.getTitle().substring(0, 23) + ".." : book.getTitle(),
                            book.getAuthor().length() > 18 ? book.getAuthor().substring(0, 18) + ".." : book.getAuthor(),
                            book.getCategory() != null ? book.getCategory().toString() : "N/A",
                            book.getTotalCopies(),
                            book.getAvailableCopies());
                });

        System.out.println("-".repeat(90));
        System.out.println("Total Titles: " + books.size());
        System.out.println("Total Books: " + getTotalBooks());
        System.out.println("Available Books: " + getAvailableBooks());
    }

    private void borrowRecordsMenu() {
        System.out.println("\n=== BORROW RECORDS ===");
        System.out.println("1. View All Records");
        System.out.println("2. View Active Borrows");
        System.out.println("3. View Overdue Books");
        System.out.println("4. View Return History");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: viewAllBorrowRecords(); break;
            case 2: viewActiveBorrows(); break;
            case 3: viewOverdueBooks(); break;
            case 4: viewReturnHistory(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void viewAllBorrowRecords() {
        System.out.println("\n=== ALL BORROW RECORDS ===");
        if (borrowRecords.isEmpty()) {
            System.out.println("No borrow records found.");
            return;
        }

        System.out.printf("%-8s %-10s %-8s %-12s %-12s %-12s %-10s%n",
                "Record", "Student", "Book", "Borrow Date", "Due Date", "Return Date", "Status");
        System.out.println("-".repeat(85));

        borrowRecords.values().stream()
                .sorted((r1, r2) -> r2.getBorrowDate().compareTo(r1.getBorrowDate()))
                .forEach(record -> {
                    System.out.printf("%-8s %-10s %-8s %-12s %-12s %-12s %-10s%n",
                            record.getRecordId(),
                            record.getStudentId(),
                            record.getBookId(),
                            record.getBorrowDate(),
                            record.getDueDate(),
                            record.getReturnDate() != null ? record.getReturnDate().toString() : "N/A",
                            record.getStatus());
                });

        System.out.println("-".repeat(85));
        System.out.println("Total Records: " + borrowRecords.size());
    }

    private void viewActiveBorrows() {
        System.out.println("\n=== ACTIVE BORROWS ===");

        List<BorrowRecord> activeRecords = borrowRecords.values().stream()
                .filter(record -> record.getStatus() == BorrowStatus.BORROWED)
                .collect(Collectors.toList());

        if (activeRecords.isEmpty()) {
            System.out.println("No active borrows found.");
            return;
        }

        System.out.printf("%-8s %-10s %-25s %-12s %-12s %-8s%n",
                "Record", "Student", "Book Title", "Borrow Date", "Due Date", "Status");
        System.out.println("-".repeat(80));

        activeRecords.forEach(record -> {
            Book book = books.get(record.getBookId());
            String title = book != null ? book.getTitle() : "Unknown";
            String status = record.getDueDate().isBefore(LocalDate.now()) ? "OVERDUE" : "ACTIVE";

            System.out.printf("%-8s %-10s %-25s %-12s %-12s %-8s%n",
                    record.getRecordId(),
                    record.getStudentId(),
                    title.length() > 23 ? title.substring(0, 23) + ".." : title,
                    record.getBorrowDate(),
                    record.getDueDate(),
                    status);
        });

        System.out.println("-".repeat(80));
        System.out.println("Total Active Borrows: " + activeRecords.size());
    }

    private void viewOverdueBooks() {
        System.out.println("\n=== OVERDUE BOOKS ===");

        LocalDate today = LocalDate.now();
        List<BorrowRecord> overdueRecords = borrowRecords.values().stream()
                .filter(record -> record.getStatus() == BorrowStatus.BORROWED)
                .filter(record -> record.getDueDate().isBefore(today))
                .collect(Collectors.toList());

        if (overdueRecords.isEmpty()) {
            System.out.println("No overdue books found.");
            return;
        }

        System.out.printf("%-8s %-10s %-25s %-12s %-8s %-10s%n",
                "Record", "Student", "Book Title", "Due Date", "Days Late", "Fine");
        System.out.println("-".repeat(75));

        overdueRecords.forEach(record -> {
            Book book = books.get(record.getBookId());
            String title = book != null ? book.getTitle() : "Unknown";
            long daysLate = today.toEpochDay() - record.getDueDate().toEpochDay();
            double fine = daysLate * 0.50;

            System.out.printf("%-8s %-10s %-25s %-12s %-8d $%-9.2f%n",
                    record.getRecordId(),
                    record.getStudentId(),
                    title.length() > 23 ? title.substring(0, 23) + ".." : title,
                    record.getDueDate(),
                    daysLate,
                    fine);
        });

        System.out.println("-".repeat(75));
        System.out.println("Total Overdue Books: " + overdueRecords.size());
    }

    private void viewReturnHistory() {
        System.out.println("\n=== RETURN HISTORY ===");

        List<BorrowRecord> returnedRecords = borrowRecords.values().stream()
                .filter(record -> record.getStatus() == BorrowStatus.RETURNED)
                .sorted((r1, r2) -> r2.getReturnDate().compareTo(r1.getReturnDate()))
                .collect(Collectors.toList());

        if (returnedRecords.isEmpty()) {
            System.out.println("No return history found.");
            return;
        }

        System.out.printf("%-8s %-10s %-25s %-12s %-12s %-10s%n",
                "Record", "Student", "Book Title", "Return Date", "Days Held", "Fine");
        System.out.println("-".repeat(80));

        returnedRecords.stream().limit(20).forEach(record -> {
            Book book = books.get(record.getBookId());
            String title = book != null ? book.getTitle() : "Unknown";
            long daysHeld = record.getReturnDate().toEpochDay() - record.getBorrowDate().toEpochDay();

            System.out.printf("%-8s %-10s %-25s %-12s %-12d $%-9.2f%n",
                    record.getRecordId(),
                    record.getStudentId(),
                    title.length() > 23 ? title.substring(0, 23) + ".." : title,
                    record.getReturnDate(),
                    daysHeld,
                    record.getFineAmount());
        });

        System.out.println("-".repeat(80));
        System.out.println("Showing last 20 returns. Total returned: " + returnedRecords.size());
    }

    private void libraryReportsMenu() {
        System.out.println("\n=== LIBRARY REPORTS ===");
        System.out.println("1. Popular Books Report");
        System.out.println("2. Category Analysis");
        System.out.println("3. Student Activity Report");
        System.out.println("4. Fine Collection Report");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: generatePopularBooksReport(); break;
            case 2: generateCategoryAnalysis(); break;
            case 3: generateStudentActivityReport(); break;
            case 4: generateFineCollectionReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void generatePopularBooksReport() {
        System.out.println("\n=== POPULAR BOOKS REPORT ===");

        Map<String, Long> borrowCounts = borrowRecords.values().stream()
                .collect(Collectors.groupingBy(BorrowRecord::getBookId, Collectors.counting()));

        System.out.printf("%-8s %-30s %-15s %-10s%n", "Book ID", "Title", "Author", "Borrows");
        System.out.println("-".repeat(70));

        borrowCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .forEach(entry -> {
                    Book book = books.get(entry.getKey());
                    if (book != null) {
                        System.out.printf("%-8s %-30s %-15s %-10d%n",
                                book.getBookId(),
                                book.getTitle().length() > 28 ? book.getTitle().substring(0, 28) + ".." : book.getTitle(),
                                book.getAuthor().length() > 13 ? book.getAuthor().substring(0, 13) + ".." : book.getAuthor(),
                                entry.getValue());
                    }
                });
    }

    private void generateCategoryAnalysis() {
        System.out.println("\n=== CATEGORY ANALYSIS ===");

        Map<BookCategory, Long> categoryCount = books.values().stream()
                .filter(book -> book.getCategory() != null)
                .collect(Collectors.groupingBy(Book::getCategory, Collectors.counting()));

        Map<BookCategory, Integer> categoryBooks = books.values().stream()
                .filter(book -> book.getCategory() != null)
                .collect(Collectors.groupingBy(Book::getCategory,
                        Collectors.summingInt(Book::getTotalCopies)));

        System.out.printf("%-20s %-8s %-12s %-15s%n", "Category", "Titles", "Total Books", "Avg per Title");
        System.out.println("-".repeat(60));

        categoryCount.entrySet().stream()
                .sorted(Map.Entry.<BookCategory, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    BookCategory category = entry.getKey();
                    Long titles = entry.getValue();
                    Integer totalBooks = categoryBooks.getOrDefault(category, 0);
                    double avgPerTitle = titles > 0 ? (double) totalBooks / titles : 0;

                    System.out.printf("%-20s %-8d %-12d %-15.1f%n",
                            category.toString(),
                            titles,
                            totalBooks,
                            avgPerTitle);
                });
    }

    private void generateStudentActivityReport() {
        System.out.println("\n=== STUDENT ACTIVITY REPORT ===");

        Map<String, Long> studentActivity = borrowRecords.values().stream()
                .collect(Collectors.groupingBy(BorrowRecord::getStudentId, Collectors.counting()));

        System.out.printf("%-12s %-15s %-15s%n", "Student ID", "Total Borrows", "Currently Has");
        System.out.println("-".repeat(45));

        studentActivity.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(15)
                .forEach(entry -> {
                    String studentId = entry.getKey();
                    Long totalBorrows = entry.getValue();
                    long currentBorrows = borrowRecords.values().stream()
                            .filter(record -> record.getStudentId().equals(studentId) &&
                                    record.getStatus() == BorrowStatus.BORROWED)
                            .count();

                    System.out.printf("%-12s %-15d %-15d%n",
                            studentId,
                            totalBorrows,
                            currentBorrows);
                });
    }

    private void generateFineCollectionReport() {
        System.out.println("\n=== FINE COLLECTION REPORT ===");

        double totalFines = borrowRecords.values().stream()
                .mapToDouble(BorrowRecord::getFineAmount)
                .sum();

        long overdueCount = borrowRecords.values().stream()
                .filter(record -> record.getStatus() == BorrowStatus.BORROWED)
                .filter(record -> record.getDueDate().isBefore(LocalDate.now()))
                .count();

        System.out.println("FINE SUMMARY:");
        System.out.println("- Total Fines Collected: $" + String.format("%.2f", totalFines));
        System.out.println("- Current Overdue Books: " + overdueCount);

        // Calculate potential fines from current overdue books
        double potentialFines = borrowRecords.values().stream()
                .filter(record -> record.getStatus() == BorrowStatus.BORROWED)
                .filter(record -> record.getDueDate().isBefore(LocalDate.now()))
                .mapToDouble(record -> {
                    long daysLate = LocalDate.now().toEpochDay() - record.getDueDate().toEpochDay();
                    return daysLate * 0.50;
                })
                .sum();

        System.out.println("- Potential Fines from Overdue: $" + String.format("%.2f", potentialFines));
        System.out.println("- Total Potential Revenue: $" + String.format("%.2f", totalFines + potentialFines));
    }

    private void displayLibraryStatistics() {
        System.out.println("\n=== LIBRARY STATISTICS ===");

        System.out.println("COLLECTION OVERVIEW:");
        System.out.println("- Total Book Titles: " + books.size());
        System.out.println("- Total Book Copies: " + getTotalBooks());
        System.out.println("- Available Copies: " + getAvailableBooks());
        System.out.println("- Borrowed Copies: " + getBorrowedBooks());

        // Category distribution
        Map<BookCategory, Long> categoryDist = books.values().stream()
                .filter(book -> book.getCategory() != null)
                .collect(Collectors.groupingBy(Book::getCategory, Collectors.counting()));

        System.out.println("\nCATEGORY DISTRIBUTION:");
        categoryDist.entrySet().stream()
                .sorted(Map.Entry.<BookCategory, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));

        // Borrowing statistics
        System.out.println("\nBORROWING STATISTICS:");
        System.out.println("- Total Borrow Records: " + borrowRecords.size());
        System.out.println("- Active Borrows: " +
                borrowRecords.values().stream().filter(r -> r.getStatus() == BorrowStatus.BORROWED).count());
        System.out.println("- Overdue Books: " + getOverdueBooks().size());

        // Utilization rate
        double utilizationRate = getTotalBooks() > 0 ?
                (double) getBorrowedBooks() / getTotalBooks() * 100 : 0;
        System.out.println("- Collection Utilization: " + String.format("%.1f", utilizationRate) + "%");

        logger.log("Library statistics displayed");
    }
}