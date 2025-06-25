package com.campus.inventory;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Manager class for handling all inventory operations
 */
public class InventoryManager implements Manageable<Item> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    
    private Map<String, Item> items;
    private Map<String, StockTransaction> transactions;
    private NotificationService notificationService;
    private int transactionCounter;
    
    public InventoryManager() {
        this.items = new HashMap<>();
        this.transactions = new HashMap<>();
        this.notificationService = NotificationService.getInstance();
        this.transactionCounter = 1;
        initializeSampleData();
        logger.log("InventoryManager initialized");
    }
    
    private void initializeSampleData() {
        // Add sample items
        Item laptop = new Item("I001", "Dell Laptop", ItemCategory.ELECTRONICS, 25, 800.0);
        laptop.setDescription("Dell Inspiron 15 3000 Series");
        laptop.setBrand("Dell");
        laptop.setModel("Inspiron 15 3000");
        laptop.setLocation("IT Store Room");
        laptop.addTag("Computer");
        laptop.addTag("Portable");
        laptop.addSpecification("RAM", "8GB");
        laptop.addSpecification("Storage", "256GB SSD");
        laptop.addSpecification("Processor", "Intel i5");
        items.put("I001", laptop);
        
        Item desk = new Item("I002", "Office Desk", ItemCategory.FURNITURE, 50, 150.0);
        desk.setDescription("Standard office desk with drawers");
        desk.setBrand("OfficeMax");
        desk.setLocation("Furniture Warehouse");
        desk.addTag("Furniture");
        desk.addTag("Office");
        desk.addSpecification("Material", "Wood");
        desk.addSpecification("Dimensions", "120x60x75 cm");
        items.put("I002", desk);
        
        Item projector = new Item("I003", "Digital Projector", ItemCategory.ELECTRONICS, 15, 500.0);
        projector.setDescription("HD Digital Projector for classrooms");
        projector.setBrand("Epson");
        projector.setModel("PowerLite 1781W");
        projector.setLocation("AV Equipment Room");
        projector.setMinStockLevel(5);
        projector.addTag("Projector");
        projector.addTag("Classroom");
        projector.addSpecification("Resolution", "1280x800");
        projector.addSpecification("Brightness", "3200 lumens");
        items.put("I003", projector);
        
        Item paper = new Item("I004", "A4 Paper", ItemCategory.STATIONERY, 200, 5.0);
        paper.setDescription("A4 size white paper - 500 sheets per pack");
        paper.setBrand("Hammermill");
        paper.setLocation("Stationery Store");
        paper.setMinStockLevel(50);
        paper.setMaxStockLevel(500);
        paper.addTag("Paper");
        paper.addTag("Office");
        items.put("I004", paper);
        
        Item microscope = new Item("I005", "Laboratory Microscope", ItemCategory.LABORATORY_EQUIPMENT, 8, 1200.0);
        microscope.setDescription("Compound microscope for biology lab");
        microscope.setBrand("Olympus");
        microscope.setModel("CX23");
        microscope.setLocation("Biology Lab");
        microscope.setMinStockLevel(3);
        microscope.addTag("Microscope");
        microscope.addTag("Lab Equipment");
        microscope.addSpecification("Magnification", "40x-1000x");
        microscope.addSpecification("Illumination", "LED");
        items.put("I005", microscope);
        
        logger.log("Sample inventory data initialized");
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== INVENTORY MANAGEMENT MENU ===");
            System.out.println("1. Item Management");
            System.out.println("2. Stock Management");
            System.out.println("3. Transaction History");
            System.out.println("4. Reports & Analytics");
            System.out.println("5. Alerts & Notifications");
            System.out.println("6. Search & Filter");
            System.out.println("7. System Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getChoice();
            
            switch (choice) {
                case 1: itemManagementMenu(); break;
                case 2: stockManagementMenu(); break;
                case 3: transactionHistoryMenu(); break;
                case 4: reportsMenu(); break;
                case 5: alertsMenu(); break;
                case 6: searchMenu(); break;
                case 7: displaySystemStatistics(); break;
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
    
    private void itemManagementMenu() {
        System.out.println("\n=== ITEM MANAGEMENT ===");
        System.out.println("1. Add New Item");
        System.out.println("2. View All Items");
        System.out.println("3. View Item Details");
        System.out.println("4. Update Item");
        System.out.println("5. Delete Item");
        System.out.println("6. Bulk Import Items");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: addItemInteractive(); break;
            case 2: viewAllItems(); break;
            case 3: viewItemDetailsInteractive(); break;
            case 4: updateItemInteractive(); break;
            case 5: deleteItemInteractive(); break;
            case 6: bulkImportItems(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void stockManagementMenu() {
        System.out.println("\n=== STOCK MANAGEMENT ===");
        System.out.println("1. Add Stock");
        System.out.println("2. Remove Stock");
        System.out.println("3. Stock Transfer");
        System.out.println("4. Stock Adjustment");
        System.out.println("5. View Low Stock Items");
        System.out.println("6. View Overstock Items");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: addStockInteractive(); break;
            case 2: removeStockInteractive(); break;
            case 3: stockTransferInteractive(); break;
            case 4: stockAdjustmentInteractive(); break;
            case 5: viewLowStockItems(); break;
            case 6: viewOverstockItems(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    @Override
    public void create(Item item) {
        items.put(item.getItemId(), item);
        notificationService.notifyObservers("New item added: " + item.getItemName());
        logger.log("Item created: " + item.getItemId());
    }
    
    @Override
    public Item read(String itemId) {
        return items.get(itemId);
    }
    
    @Override
    public void update(Item item) {
        items.put(item.getItemId(), item);
        notificationService.notifyObservers("Item updated: " + item.getItemName());
        logger.log("Item updated: " + item.getItemId());
    }
    
    @Override
    public void delete(String itemId) {
        Item item = items.remove(itemId);
        if (item != null) {
            notificationService.notifyObservers("Item deleted: " + item.getItemName());
            logger.log("Item deleted: " + itemId);
        }
    }
    
    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }
    
    private void addItemInteractive() {
        System.out.println("\n=== ADD NEW ITEM ===");
        
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        if (items.containsKey(itemId)) {
            System.out.println("Item ID already exists!");
            return;
        }
        
        System.out.print("Enter Item Name: ");
        String itemName = scanner.nextLine();
        
        System.out.println("Select Category:");
        ItemCategory[] categories = ItemCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Enter choice: ");
        int categoryChoice = getChoice();
        
        if (categoryChoice < 1 || categoryChoice > categories.length) {
            System.out.println("Invalid category choice.");
            return;
        }
        
        ItemCategory category = categories[categoryChoice - 1];
        
        System.out.print("Enter Initial Quantity: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }
        
        System.out.print("Enter Unit Price: ");
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price.");
            return;
        }
        
        Item newItem = new Item(itemId, itemName, category, quantity, unitPrice);
        
        // Optional fields
        System.out.print("Enter Description (optional): ");
        String description = scanner.nextLine();
        if (!description.trim().isEmpty()) {
            newItem.setDescription(description);
        }
        
        System.out.print("Enter Brand (optional): ");
        String brand = scanner.nextLine();
        if (!brand.trim().isEmpty()) {
            newItem.setBrand(brand);
        }
        
        System.out.print("Enter Location (optional): ");
        String location = scanner.nextLine();
        if (!location.trim().isEmpty()) {
            newItem.setLocation(location);
        }
        
        create(newItem);
        
        // Record the initial stock transaction
        recordTransaction(itemId, TransactionType.STOCK_IN, quantity, unitPrice, 
                         "System", "Initial stock entry");
        
        System.out.println("Item added successfully!");
        newItem.displayItemInfo();
    }
    
    private void viewAllItems() {
        System.out.println("\n=== ALL ITEMS ===");
        if (items.isEmpty()) {
            System.out.println("No items found.");
            return;
        }
        
        System.out.printf("%-8s %-20s %-15s %-8s %-10s %-12s %-15s %-10s%n", 
                         "ID", "Name", "Category", "Qty", "Price", "Total Value", "Location", "Status");
        System.out.println("-".repeat(100));
        
        for (Item item : items.values()) {
            String status = item.isActive() ? 
                           (item.isLowStock() ? "LOW STOCK" : 
                            item.isOverStock() ? "OVERSTOCK" : "NORMAL") : "INACTIVE";
            
            System.out.printf("%-8s %-20s %-15s %-8d $%-9.2f $%-11.2f %-15s %-10s%n",
                             item.getItemId(),
                             item.getItemName().length() > 18 ? item.getItemName().substring(0, 18) + ".." : item.getItemName(),
                             item.getCategory().toString().length() > 13 ? item.getCategory().toString().substring(0, 13) + ".." : item.getCategory(),
                             item.getQuantity(),
                             item.getUnitPrice(),
                             item.getTotalValue(),
                             item.getLocation() != null ? 
                                 (item.getLocation().length() > 13 ? item.getLocation().substring(0, 13) + ".." : item.getLocation()) : "N/A",
                             status);
        }
        
        // Summary
        System.out.println("-".repeat(100));
        System.out.println("Total Items: " + items.size());
        System.out.println("Total Inventory Value: $" + String.format("%.2f", getTotalInventoryValue()));
        System.out.println("Active Items: " + items.values().stream().filter(Item::isActive).count());
        System.out.println("Low Stock Items: " + getLowStockItems().size());
    }
    
    private void viewItemDetailsInteractive() {
        System.out.println("\n=== VIEW ITEM DETAILS ===");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        Item item = read(itemId);
        if (item != null) {
            item.displayItemInfo();
            
            // Show recent transactions for this item
            System.out.println("\n=== RECENT TRANSACTIONS ===");
            List<StockTransaction> itemTransactions = getItemTransactions(itemId);
            if (itemTransactions.isEmpty()) {
                System.out.println("No transactions found for this item.");
            } else {
                System.out.printf("%-12s %-15s %-8s %-10s %-15s%n", 
                                 "Date", "Type", "Qty", "Price", "Performed By");
                System.out.println("-".repeat(65));
                
                itemTransactions.stream()
                    .limit(10)
                    .forEach(t -> System.out.printf("%-12s %-15s %-8d $%-9.2f %-15s%n",
                                                   t.getTransactionDate().toLocalDate(),
                                                   t.getTransactionType(),
                                                   t.getQuantity(),
                                                   t.getUnitPrice(),
                                                   t.getPerformedBy()));
            }
        } else {
            System.out.println("Item not found.");
        }
    }
    
    private void updateItemInteractive() {
        System.out.println("\n=== UPDATE ITEM ===");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        Item item = read(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.println("Current item details:");
        item.displayItemInfo();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Description");
        System.out.println("3. Price");
        System.out.println("4. Location");
        System.out.println("5. Min/Max Stock Levels");
        System.out.println("6. Add Tag");
        System.out.println("7. Add Specification");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new name: ");
                item.setItemName(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new description: ");
                item.setDescription(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new price: ");
                try {
                    double price = Double.parseDouble(scanner.nextLine());
                    item.updatePrice(price);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price.");
                    return;
                }
                break;
            case 4:
                System.out.print("Enter new location: ");
                item.setLocation(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter minimum stock level: ");
                try {
                    int minStock = Integer.parseInt(scanner.nextLine());
                    item.setMinStockLevel(minStock);
                    System.out.print("Enter maximum stock level: ");
                    int maxStock = Integer.parseInt(scanner.nextLine());
                    item.setMaxStockLevel(maxStock);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid stock level.");
                    return;
                }
                break;
            case 6:
                System.out.print("Enter tag to add: ");
                item.addTag(scanner.nextLine());
                break;
            case 7:
                System.out.print("Enter specification key: ");
                String key = scanner.nextLine();
                System.out.print("Enter specification value: ");
                String value = scanner.nextLine();
                item.addSpecification(key, value);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        update(item);
        System.out.println("Item updated successfully!");
    }
    
    private void deleteItemInteractive() {
        System.out.println("\n=== DELETE ITEM ===");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        Item item = read(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.println("Item to be deleted:");
        item.displayItemInfo();
        
        System.out.print("Are you sure you want to delete this item? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if ("yes".equalsIgnoreCase(confirmation)) {
            delete(itemId);
            System.out.println("Item deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void bulkImportItems() {
        System.out.println("\n=== BULK IMPORT ITEMS ===");
        System.out.println("This feature would typically import from CSV/Excel files.");
        System.out.println("For demonstration, adding sample items...");
        
        // Add some sample items for bulk import demo
        String[][] sampleItems = {
            {"I100", "Whiteboard Marker", "STATIONERY", "100", "2.50", "Pilot", "Office Store"},
            {"I101", "USB Flash Drive", "ELECTRONICS", "50", "15.00", "SanDisk", "IT Store"},
            {"I102", "Office Chair", "FURNITURE", "30", "120.00", "Herman Miller", "Furniture Store"},
            {"I103", "Calculator", "ELECTRONICS", "75", "25.00", "Casio", "Office Store"},
            {"I104", "Notebook", "STATIONERY", "200", "3.00", "Moleskine", "Stationery Store"}
        };
        
        int imported = 0;
        for (String[] itemData : sampleItems) {
            try {
                String itemId = itemData[0];
                if (!items.containsKey(itemId)) {
                    Item item = new Item(itemId, itemData[1], 
                                       ItemCategory.valueOf(itemData[2]), 
                                       Integer.parseInt(itemData[3]), 
                                       Double.parseDouble(itemData[4]));
                    item.setBrand(itemData[5]);
                    item.setLocation(itemData[6]);
                    
                    create(item);
                    recordTransaction(itemId, TransactionType.STOCK_IN, 
                                   Integer.parseInt(itemData[3]), 
                                   Double.parseDouble(itemData[4]), 
                                   "System", "Bulk import");
                    imported++;
                }
            } catch (Exception e) {
                System.out.println("Error importing item: " + itemData[1]);
            }
        }
        
        System.out.println("Bulk import completed. " + imported + " items imported.");
        logger.log("Bulk import completed: " + imported + " items");
    }
    
    private void addStockInteractive() {
        System.out.println("\n=== ADD STOCK ===");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        Item item = read(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.println("Current stock: " + item.getQuantity());
        System.out.print("Enter quantity to add: ");
        
        try {
            int quantity = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter unit price for this batch: ");
            double unitPrice = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter reason: ");
            String reason = scanner.nextLine();
            
            item.addStock(quantity);
            update(item);
            
            recordTransaction(itemId, TransactionType.STOCK_IN, quantity, unitPrice, 
                             "Admin", reason);
            
            System.out.println("Stock added successfully!");
            System.out.println("New stock level: " + item.getQuantity());
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void removeStockInteractive() {
        System.out.println("\n=== REMOVE STOCK ===");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        Item item = read(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.println("Current stock: " + item.getQuantity());
        System.out.print("Enter quantity to remove: ");
        
        try {
            int quantity = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter reason: ");
            String reason = scanner.nextLine();
            
            item.removeStock(quantity);
            update(item);
            
            recordTransaction(itemId, TransactionType.STOCK_OUT, quantity, item.getUnitPrice(), 
                             "Admin", reason);
            
            System.out.println("Stock removed successfully!");
            System.out.println("New stock level: " + item.getQuantity());
            
            // Check for low stock alert
            if (item.isLowStock()) {
                System.out.println("WARNING: Item is now at low stock level!");
                notificationService.notifyObservers("Low stock alert: " + item.getItemName());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void stockTransferInteractive() {
        System.out.println("\n=== STOCK TRANSFER ===");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        Item item = read(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.println("Current location: " + item.getLocation());
        System.out.print("Enter new location: ");
        String newLocation = scanner.nextLine();
        
        System.out.print("Enter quantity to transfer: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter recipient: ");
            String recipient = scanner.nextLine();
            
            if (quantity > item.getQuantity()) {
                System.out.println("Insufficient stock for transfer.");
                return;
            }
            
            item.relocate(newLocation);
            update(item);
            
            recordTransaction(itemId, TransactionType.TRANSFER, quantity, item.getUnitPrice(), 
                             "Admin", "Transfer to " + newLocation);
            
            StockTransaction transaction = transactions.get("T" + String.format("%06d", transactionCounter - 1));
            if (transaction != null) {
                transaction.setRecipient(recipient);
            }
            
            System.out.println("Stock transfer completed successfully!");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
        }
    }
    
    private void stockAdjustmentInteractive() {
        System.out.println("\n=== STOCK ADJUSTMENT ===");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        Item item = read(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.println("Current stock: " + item.getQuantity());
        System.out.print("Enter correct stock quantity: ");
        
        try {
            int newQuantity = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter reason for adjustment: ");
            String reason = scanner.nextLine();
            
            int difference = newQuantity - item.getQuantity();
            item.updateStock(newQuantity);
            update(item);
            
            recordTransaction(itemId, TransactionType.ADJUSTMENT, Math.abs(difference), 
                             item.getUnitPrice(), "Admin", reason);
            
            System.out.println("Stock adjustment completed!");
            System.out.println("Stock changed by: " + (difference >= 0 ? "+" : "") + difference);
            System.out.println("New stock level: " + item.getQuantity());
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
        }
    }
    
    private void viewLowStockItems() {
        System.out.println("\n=== LOW STOCK ITEMS ===");
        List<Item> lowStockItems = getLowStockItems();
        
        if (lowStockItems.isEmpty()) {
            System.out.println("No items are currently at low stock levels.");
            return;
        }
        
        System.out.printf("%-8s %-20s %-8s %-8s %-15s%n", 
                         "ID", "Name", "Current", "Min", "Location");
        System.out.println("-".repeat(65));
        
        for (Item item : lowStockItems) {
            System.out.printf("%-8s %-20s %-8d %-8d %-15s%n",
                             item.getItemId(),
                             item.getItemName().length() > 18 ? item.getItemName().substring(0, 18) + ".." : item.getItemName(),
                             item.getQuantity(),
                             item.getMinStockLevel(),
                             item.getLocation() != null ? 
                                 (item.getLocation().length() > 13 ? item.getLocation().substring(0, 13) + ".." : item.getLocation()) : "N/A");
        }
        
        System.out.println("\nTotal low stock items: " + lowStockItems.size());
    }
    
    private void viewOverstockItems() {
        System.out.println("\n=== OVERSTOCK ITEMS ===");
        List<Item> overstockItems = getOverstockItems();
        
        if (overstockItems.isEmpty()) {
            System.out.println("No items are currently overstocked.");
            return;
        }
        
        System.out.printf("%-8s %-20s %-8s %-8s %-12s%n", 
                         "ID", "Name", "Current", "Max", "Excess");
        System.out.println("-".repeat(60));
        
        for (Item item : overstockItems) {
            int excess = item.getQuantity() - item.getMaxStockLevel();
            System.out.printf("%-8s %-20s %-8d %-8d %-12d%n",
                             item.getItemId(),
                             item.getItemName().length() > 18 ? item.getItemName().substring(0, 18) + ".." : item.getItemName(),
                             item.getQuantity(),
                             item.getMaxStockLevel(),
                             excess);
        }
        
        System.out.println("\nTotal overstock items: " + overstockItems.size());
    }
    
    private void recordTransaction(String itemId, TransactionType type, int quantity, 
                                 double unitPrice, String performedBy, String reason) {
        String transactionId = "T" + String.format("%06d", transactionCounter++);
        StockTransaction transaction = new StockTransaction(transactionId, itemId, type, 
                                                          quantity, unitPrice, performedBy);
        transaction.setReason(reason);
        transaction.approve("System"); // Auto-approve for demo
        
        transactions.put(transactionId, transaction);
        logger.log("Transaction recorded: " + transactionId);
    }
    
    private List<StockTransaction> getItemTransactions(String itemId) {
        return transactions.values().stream()
                .filter(t -> t.getItemId().equals(itemId))
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .collect(Collectors.toList());
    }
    
    private void transactionHistoryMenu() {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        System.out.println("1. View All Transactions");
        System.out.println("2. View Transactions by Item");
        System.out.println("3. View Transactions by Type");
        System.out.println("4. View Transactions by Date Range");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: viewAllTransactions(); break;
            case 2: viewTransactionsByItemInteractive(); break;
            case 3: viewTransactionsByTypeInteractive(); break;
            case 4: viewTransactionsByDateRangeInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void viewAllTransactions() {
        System.out.println("\n=== ALL TRANSACTIONS ===");
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        System.out.printf("%-12s %-8s %-15s %-8s %-10s %-15s %-12s%n", 
                         "Trans ID", "Item", "Type", "Qty", "Price", "Performed By", "Date");
        System.out.println("-".repeat(85));
        
        transactions.values().stream()
            .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
            .limit(50) // Show last 50 transactions
            .forEach(t -> {
                System.out.printf("%-12s %-8s %-15s %-8d $%-9.2f %-15s %-12s%n",
                                 t.getTransactionId(),
                                 t.getItemId(),
                                 t.getTransactionType().toString().length() > 13 ? 
                                     t.getTransactionType().toString().substring(0, 13) + ".." : 
                                     t.getTransactionType(),
                                 t.getQuantity(),
                                 t.getUnitPrice(),
                                 t.getPerformedBy().length() > 13 ? 
                                     t.getPerformedBy().substring(0, 13) + ".." : 
                                     t.getPerformedBy(),
                                 t.getTransactionDate().toLocalDate().toString());
            });
        
        System.out.println("\nShowing last 50 transactions. Total: " + transactions.size());
    }
    
    private void viewTransactionsByItemInteractive() {
        System.out.println("\n=== TRANSACTIONS BY ITEM ===");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        
        List<StockTransaction> itemTransactions = getItemTransactions(itemId);
        if (itemTransactions.isEmpty()) {
            System.out.println("No transactions found for item: " + itemId);
            return;
        }
        
        Item item = read(itemId);
        if (item != null) {
            System.out.println("Item: " + item.getItemName());
        }
        
        System.out.printf("%-12s %-15s %-8s %-10s %-15s %-12s%n", 
                         "Trans ID", "Type", "Qty", "Price", "Performed By", "Date");
        System.out.println("-".repeat(75));
        
        itemTransactions.forEach(t -> {
            System.out.printf("%-12s %-15s %-8d $%-9.2f %-15s %-12s%n",
                             t.getTransactionId(),
                             t.getTransactionType(),
                             t.getQuantity(),
                             t.getUnitPrice(),
                             t.getPerformedBy(),
                             t.getTransactionDate().toLocalDate().toString());
        });
        
        System.out.println("\nTotal transactions: " + itemTransactions.size());
    }
    
    private void viewTransactionsByTypeInteractive() {
        System.out.println("\n=== TRANSACTIONS BY TYPE ===");
        System.out.println("Select transaction type:");
        TransactionType[] types = TransactionType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice < 1 || choice > types.length) {
            System.out.println("Invalid choice.");
            return;
        }
        
        TransactionType selectedType = types[choice - 1];
        List<StockTransaction> typeTransactions = transactions.values().stream()
                .filter(t -> t.getTransactionType() == selectedType)
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .collect(Collectors.toList());
        
        if (typeTransactions.isEmpty()) {
            System.out.println("No transactions found for type: " + selectedType);
            return;
        }
        
        System.out.println("Transactions of type: " + selectedType);
        System.out.printf("%-12s %-8s %-8s %-10s %-15s %-12s%n", 
                         "Trans ID", "Item", "Qty", "Price", "Performed By", "Date");
        System.out.println("-".repeat(70));
        
        typeTransactions.forEach(t -> {
            System.out.printf("%-12s %-8s %-8d $%-9.2f %-15s %-12s%n",
                             t.getTransactionId(),
                             t.getItemId(),
                             t.getQuantity(),
                             t.getUnitPrice(),
                             t.getPerformedBy(),
                             t.getTransactionDate().toLocalDate().toString());
        });
        
        System.out.println("\nTotal transactions: " + typeTransactions.size());
    }
    
    private void viewTransactionsByDateRangeInteractive() {
        System.out.println("\n=== TRANSACTIONS BY DATE RANGE ===");
        System.out.println("This feature would allow filtering by date range.");
        System.out.println("For demonstration, showing transactions from last 7 days:");
        
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        List<StockTransaction> recentTransactions = transactions.values().stream()
                .filter(t -> t.getTransactionDate().isAfter(weekAgo))
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .collect(Collectors.toList());
        
        if (recentTransactions.isEmpty()) {
            System.out.println("No transactions found in the last 7 days.");
            return;
        }
        
        System.out.printf("%-12s %-8s %-15s %-8s %-10s %-12s%n", 
                         "Trans ID", "Item", "Type", "Qty", "Price", "Date");
        System.out.println("-".repeat(70));
        
        recentTransactions.forEach(t -> {
            System.out.printf("%-12s %-8s %-15s %-8d $%-9.2f %-12s%n",
                             t.getTransactionId(),
                             t.getItemId(),
                             t.getTransactionType().toString().length() > 13 ? 
                                 t.getTransactionType().toString().substring(0, 13) + ".." : 
                                 t.getTransactionType(),
                             t.getQuantity(),
                             t.getUnitPrice(),
                             t.getTransactionDate().toLocalDate().toString());
        });
        
        System.out.println("\nTransactions in last 7 days: " + recentTransactions.size());
    }
    
    // Utility methods
    public List<Item> getLowStockItems() {
        return items.values().stream()
                .filter(Item::isLowStock)
                .collect(Collectors.toList());
    }
    
    public List<Item> getOverstockItems() {
        return items.values().stream()
                .filter(Item::isOverStock)
                .collect(Collectors.toList());
    }
    
    public List<Item> getExpiredItems() {
        return items.values().stream()
                .filter(Item::isExpired)
                .collect(Collectors.toList());
    }
    
    public List<Item> getExpiringSoonItems(int days) {
        return items.values().stream()
                .filter(item -> item.isExpiringSoon(days))
                .collect(Collectors.toList());
    }
    
    public double getTotalInventoryValue() {
        return items.values().stream()
                .mapToDouble(Item::getTotalValue)
                .sum();
    }
    
    public Map<ItemCategory, Integer> getCategoryDistribution() {
        return items.values().stream()
                .collect(Collectors.groupingBy(
                    Item::getCategory,
                    Collectors.summingInt(item -> 1)
                ));
    }
    
    public Map<ItemCategory, Double> getCategoryValues() {
        return items.values().stream()
                .collect(Collectors.groupingBy(
                    Item::getCategory,
                    Collectors.summingDouble(Item::getTotalValue)
                ));
    }
    
    private void reportsMenu() {
        System.out.println("\n=== REPORTS & ANALYTICS ===");
        System.out.println("1. Inventory Summary Report");
        System.out.println("2. Category Analysis");
        System.out.println("3. Stock Movement Report");
        System.out.println("4. Value Analysis");
        System.out.println("5. Expiry Report");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: generateInventorySummaryReport(); break;
            case 2: generateCategoryAnalysis(); break;
            case 3: generateStockMovementReport(); break;
            case 4: generateValueAnalysis(); break;
            case 5: generateExpiryReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void generateInventorySummaryReport() {
        System.out.println("\n=== INVENTORY SUMMARY REPORT ===");
        
        int totalItems = items.size();
        int activeItems = (int) items.values().stream().filter(Item::isActive).count();
        int lowStockItems = getLowStockItems().size();
        int overstockItems = getOverstockItems().size();
        int expiredItems = getExpiredItems().size();
        double totalValue = getTotalInventoryValue();
        
        System.out.println("Total Items: " + totalItems);
        System.out.println("Active Items: " + activeItems);
        System.out.println("Inactive Items: " + (totalItems - activeItems));
        System.out.println("Low Stock Items: " + lowStockItems);
        System.out.println("Overstock Items: " + overstockItems);
        System.out.println("Expired Items: " + expiredItems);
        System.out.println("Total Inventory Value: $" + String.format("%.2f", totalValue));
        System.out.println("Average Item Value: $" + String.format("%.2f", totalValue / Math.max(totalItems, 1)));
        
        // Top 5 most valuable items
        System.out.println("\nTop 5 Most Valuable Items:");
        items.values().stream()
            .sorted((i1, i2) -> Double.compare(i2.getTotalValue(), i1.getTotalValue()))
            .limit(5)
            .forEach(item -> System.out.println("- " + item.getItemName() + 
                                              ": $" + String.format("%.2f", item.getTotalValue())));
        
        logger.log("Inventory summary report generated");
    }
    
    private void generateCategoryAnalysis() {
        System.out.println("\n=== CATEGORY ANALYSIS ===");
        
        Map<ItemCategory, Integer> categoryCount = getCategoryDistribution();
        Map<ItemCategory, Double> categoryValues = getCategoryValues();
        
        System.out.printf("%-20s %-8s %-15s %-10s%n", "Category", "Items", "Total Value", "Avg Value");
        System.out.println("-".repeat(60));
        
        for (ItemCategory category : ItemCategory.values()) {
            int count = categoryCount.getOrDefault(category, 0);
            double totalValue = categoryValues.getOrDefault(category, 0.0);
            double avgValue = count > 0 ? totalValue / count : 0.0;
            
            if (count > 0) {
                System.out.printf("%-20s %-8d $%-14.2f $%-9.2f%n",
                                 category.toString().length() > 18 ? 
                                     category.toString().substring(0, 18) + ".." : category,
                                 count, totalValue, avgValue);
            }
        }
        
        logger.log("Category analysis report generated");
    }
    
    private void generateStockMovementReport() {
        System.out.println("\n=== STOCK MOVEMENT REPORT ===");
        
        Map<TransactionType, Long> transactionCounts = transactions.values().stream()
                .collect(Collectors.groupingBy(
                    StockTransaction::getTransactionType,
                    Collectors.counting()
                ));
        
        Map<TransactionType, Double> transactionValues = transactions.values().stream()
                .collect(Collectors.groupingBy(
                    StockTransaction::getTransactionType,
                    Collectors.summingDouble(StockTransaction::getTotalAmount)
                ));
        
        System.out.printf("%-15s %-10s %-15s%n", "Transaction Type", "Count", "Total Value");
        System.out.println("-".repeat(45));
        
        for (TransactionType type : TransactionType.values()) {
            long count = transactionCounts.getOrDefault(type, 0L);
            double value = transactionValues.getOrDefault(type, 0.0);
            
            if (count > 0) {
                System.out.printf("%-15s %-10d $%-14.2f%n", type, count, value);
            }
        }
        
        System.out.println("\nTotal Transactions: " + transactions.size());
        System.out.println("Total Transaction Value: $" + 
                          String.format("%.2f", transactions.values().stream()
                                  .mapToDouble(StockTransaction::getTotalAmount).sum()));
        
        logger.log("Stock movement report generated");
    }
    
    private void generateValueAnalysis() {
        System.out.println("\n=== VALUE ANALYSIS ===");
        
        double totalValue = getTotalInventoryValue();
        
        // Value distribution by category
        System.out.println("Value Distribution by Category:");
        getCategoryValues().entrySet().stream()
            .sorted(Map.Entry.<ItemCategory, Double>comparingByValue().reversed())
            .forEach(entry -> {
                double percentage = (entry.getValue() / totalValue) * 100;
                System.out.printf("- %-20s: $%10.2f (%.1f%%)%n", 
                                entry.getKey(), entry.getValue(), percentage);
            });
        
        // ABC Analysis (Pareto Analysis)
        System.out.println("\nABC Analysis (Top 20% items by value):");
        List<Item> sortedByValue = items.values().stream()
                .sorted((i1, i2) -> Double.compare(i2.getTotalValue(), i1.getTotalValue()))
                .collect(Collectors.toList());
        
        int topCount = Math.max(1, (int) (sortedByValue.size() * 0.2));
        double topValue = sortedByValue.stream()
                .limit(topCount)
                .mapToDouble(Item::getTotalValue)
                .sum();
        
        System.out.println("Top " + topCount + " items represent " + 
                          String.format("%.1f", (topValue / totalValue) * 100) + 
                          "% of total inventory value");
        
        sortedByValue.stream()
            .limit(10)
            .forEach(item -> System.out.printf("- %-20s: $%10.2f%n", 
                                             item.getItemName(), item.getTotalValue()));
        
        logger.log("Value analysis report generated");
    }
    
    private void generateExpiryReport() {
        System.out.println("\n=== EXPIRY REPORT ===");
        
        List<Item> expiredItems = getExpiredItems();
        List<Item> expiringSoon = getExpiringSoonItems(30);
        
        System.out.println("Expired Items: " + expiredItems.size());
        if (!expiredItems.isEmpty()) {
            System.out.println("Items that have expired:");
            expiredItems.forEach(item -> 
                System.out.println("- " + item.getItemName() + 
                                 " (Expired: " + item.getExpiryDate() + ")"));
        }
        
        System.out.println("\nExpiring Soon (within 30 days): " + expiringSoon.size());
        if (!expiringSoon.isEmpty()) {
            System.out.println("Items expiring within 30 days:");
            expiringSoon.forEach(item -> 
                System.out.println("- " + item.getItemName() + 
                                 " (Expires: " + item.getExpiryDate() + ")"));
        }
        
        if (expiredItems.isEmpty() && expiringSoon.isEmpty()) {
            System.out.println("No items are expired or expiring soon.");
        }
        
        logger.log("Expiry report generated");
    }
    
    private void alertsMenu() {
        System.out.println("\n=== ALERTS & NOTIFICATIONS ===");
        System.out.println("1. Low Stock Alerts");
        System.out.println("2. Overstock Alerts");
        System.out.println("3. Expiry Alerts");
        System.out.println("4. All Active Alerts");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: showLowStockAlerts(); break;
            case 2: showOverstockAlerts(); break;
            case 3: showExpiryAlerts(); break;
            case 4: showAllAlerts(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void showLowStockAlerts() {
        System.out.println("\n=== LOW STOCK ALERTS ===");
        List<Item> lowStockItems = getLowStockItems();
        
        if (lowStockItems.isEmpty()) {
            System.out.println("No low stock alerts.");
            return;
        }
        
        System.out.println("⚠️  " + lowStockItems.size() + " items are at low stock levels:");
        lowStockItems.forEach(item -> 
            System.out.println("- " + item.getItemName() + 
                             " (Current: " + item.getQuantity() + 
                             ", Min: " + item.getMinStockLevel() + ")"));
    }
    
    private void showOverstockAlerts() {
        System.out.println("\n=== OVERSTOCK ALERTS ===");
        List<Item> overstockItems = getOverstockItems();
        
        if (overstockItems.isEmpty()) {
            System.out.println("No overstock alerts.");
            return;
        }
        
        System.out.println("📦 " + overstockItems.size() + " items are overstocked:");
        overstockItems.forEach(item -> 
            System.out.println("- " + item.getItemName() + 
                             " (Current: " + item.getQuantity() + 
                             ", Max: " + item.getMaxStockLevel() + ")"));
    }
    
    private void showExpiryAlerts() {
        System.out.println("\n=== EXPIRY ALERTS ===");
        List<Item> expiredItems = getExpiredItems();
        List<Item> expiringSoon = getExpiringSoonItems(30);
        
        if (!expiredItems.isEmpty()) {
            System.out.println("🚨 " + expiredItems.size() + " items have expired:");
            expiredItems.forEach(item -> 
                System.out.println("- " + item.getItemName() + 
                                 " (Expired: " + item.getExpiryDate() + ")"));
        }
        
        if (!expiringSoon.isEmpty()) {
            System.out.println("⏰ " + expiringSoon.size() + " items expiring within 30 days:");
            expiringSoon.forEach(item -> 
                System.out.println("- " + item.getItemName() + 
                                 " (Expires: " + item.getExpiryDate() + ")"));
        }
        
        if (expiredItems.isEmpty() && expiringSoon.isEmpty()) {
            System.out.println("No expiry alerts.");
        }
    }
    
    private void showAllAlerts() {
        System.out.println("\n=== ALL ACTIVE ALERTS ===");
        
        int totalAlerts = 0;
        
        List<Item> lowStockItems = getLowStockItems();
        if (!lowStockItems.isEmpty()) {
            System.out.println("⚠️  Low Stock: " + lowStockItems.size() + " items");
            totalAlerts += lowStockItems.size();
        }
        
        List<Item> overstockItems = getOverstockItems();
        if (!overstockItems.isEmpty()) {
            System.out.println("📦 Overstock: " + overstockItems.size() + " items");
            totalAlerts += overstockItems.size();
        }
        
        List<Item> expiredItems = getExpiredItems();
        if (!expiredItems.isEmpty()) {
            System.out.println("🚨 Expired: " + expiredItems.size() + " items");
            totalAlerts += expiredItems.size();
        }
        
        List<Item> expiringSoon = getExpiringSoonItems(30);
        if (!expiringSoon.isEmpty()) {
            System.out.println("⏰ Expiring Soon: " + expiringSoon.size() + " items");
            totalAlerts += expiringSoon.size();
        }
        
        if (totalAlerts == 0) {
            System.out.println("✅ No active alerts. All systems normal.");
        } else {
            System.out.println("\nTotal Active Alerts: " + totalAlerts);
        }
    }
    
    private void searchMenu() {
        System.out.println("\n=== SEARCH & FILTER ===");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Category");
        System.out.println("3. Search by Location");
        System.out.println("4. Search by Tag");
        System.out.println("5. Advanced Search");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: searchByNameInteractive(); break;
            case 2: searchByCategoryInteractive(); break;
            case 3: searchByLocationInteractive(); break;
            case 4: searchByTagInteractive(); break;
            case 5: advancedSearchInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void searchByNameInteractive() {
        System.out.println("\n=== SEARCH BY NAME ===");
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        List<Item> results = items.values().stream()
                .filter(item -> item.getItemName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "name containing '" + searchTerm + "'");
    }
    
    private void searchByCategoryInteractive() {
        System.out.println("\n=== SEARCH BY CATEGORY ===");
        System.out.println("Select category:");
        ItemCategory[] categories = ItemCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice < 1 || choice > categories.length) {
            System.out.println("Invalid choice.");
            return;
        }
        
        ItemCategory selectedCategory = categories[choice - 1];
        List<Item> results = items.values().stream()
                .filter(item -> item.getCategory() == selectedCategory)
                .collect(Collectors.toList());
        
        displaySearchResults(results, "category '" + selectedCategory + "'");
    }
    
    private void searchByLocationInteractive() {
        System.out.println("\n=== SEARCH BY LOCATION ===");
        System.out.print("Enter location: ");
        String location = scanner.nextLine().toLowerCase();
        
        List<Item> results = items.values().stream()
                .filter(item -> item.getLocation() != null && 
                               item.getLocation().toLowerCase().contains(location))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "location containing '" + location + "'");
    }
    
    private void searchByTagInteractive() {
        System.out.println("\n=== SEARCH BY TAG ===");
        System.out.print("Enter tag: ");
        String tag = scanner.nextLine().toLowerCase();
        
        List<Item> results = items.values().stream()
                .filter(item -> item.getTags().stream()
                                   .anyMatch(t -> t.toLowerCase().contains(tag)))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "tag containing '" + tag + "'");
    }
    
    private void advancedSearchInteractive() {
        System.out.println("\n=== ADVANCED SEARCH ===");
        System.out.println("Enter search criteria (leave blank to skip):");
        
        System.out.print("Name contains: ");
        String nameFilter = scanner.nextLine().toLowerCase();
        
        System.out.print("Min quantity: ");
        String minQtyStr = scanner.nextLine();
        Integer minQty = minQtyStr.isEmpty() ? null : Integer.parseInt(minQtyStr);
        
        System.out.print("Max quantity: ");
        String maxQtyStr = scanner.nextLine();
        Integer maxQty = maxQtyStr.isEmpty() ? null : Integer.parseInt(maxQtyStr);
        
        System.out.print("Min price: ");
        String minPriceStr = scanner.nextLine();
        Double minPrice = minPriceStr.isEmpty() ? null : Double.parseDouble(minPriceStr);
        
        System.out.print("Max price: ");
        String maxPriceStr = scanner.nextLine();
        Double maxPrice = maxPriceStr.isEmpty() ? null : Double.parseDouble(maxPriceStr);
        
        List<Item> results = items.values().stream()
                .filter(item -> nameFilter.isEmpty() || 
                               item.getItemName().toLowerCase().contains(nameFilter))
                .filter(item -> minQty == null || item.getQuantity() >= minQty)
                .filter(item -> maxQty == null || item.getQuantity() <= maxQty)
                .filter(item -> minPrice == null || item.getUnitPrice() >= minPrice)
                .filter(item -> maxPrice == null || item.getUnitPrice() <= maxPrice)
                .collect(Collectors.toList());
        
        displaySearchResults(results, "advanced search criteria");
    }
    
    private void displaySearchResults(List<Item> results, String searchCriteria) {
        System.out.println("\n=== SEARCH RESULTS ===");
        System.out.println("Search criteria: " + searchCriteria);
        System.out.println("Results found: " + results.size());
        
        if (results.isEmpty()) {
            System.out.println("No items found matching the search criteria.");
            return;
        }
        
        System.out.printf("%-8s %-20s %-15s %-8s %-10s %-15s%n", 
                         "ID", "Name", "Category", "Qty", "Price", "Location");
        System.out.println("-".repeat(80));
        
        results.forEach(item -> {
            System.out.printf("%-8s %-20s %-15s %-8d $%-9.2f %-15s%n",
                             item.getItemId(),
                             item.getItemName().length() > 18 ? item.getItemName().substring(0, 18) + ".." : item.getItemName(),
                             item.getCategory().toString().length() > 13 ? item.getCategory().toString().substring(0, 13) + ".." : item.getCategory(),
                             item.getQuantity(),
                             item.getUnitPrice(),
                             item.getLocation() != null ? 
                                 (item.getLocation().length() > 13 ? item.getLocation().substring(0, 13) + ".." : item.getLocation()) : "N/A");
        });
    }
    
    private void displaySystemStatistics() {
        System.out.println("\n=== INVENTORY SYSTEM STATISTICS ===");
        
        // Basic statistics
        System.out.println("INVENTORY OVERVIEW:");
        System.out.println("- Total Items: " + items.size());
        System.out.println("- Active Items: " + items.values().stream().filter(Item::isActive).count());
        System.out.println("- Total Inventory Value: $" + String.format("%.2f", getTotalInventoryValue()));
        System.out.println("- Average Item Value: $" + String.format("%.2f", getTotalInventoryValue() / Math.max(items.size(), 1)));
        
        // Stock status
        System.out.println("\nSTOCK STATUS:");
        System.out.println("- Low Stock Items: " + getLowStockItems().size());
        System.out.println("- Overstock Items: " + getOverstockItems().size());
        System.out.println("- Normal Stock Items: " + (items.size() - getLowStockItems().size() - getOverstockItems().size()));
        
        // Category distribution
        System.out.println("\nCATEGORY DISTRIBUTION:");
        getCategoryDistribution().entrySet().stream()
            .sorted(Map.Entry.<ItemCategory, Integer>comparingByValue().reversed())
            .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " items"));
        
        // Transaction statistics
        System.out.println("\nTRANSACTION OVERVIEW:");
        System.out.println("- Total Transactions: " + transactions.size());
        System.out.println("- Total Transaction Value: $" + 
                          String.format("%.2f", transactions.values().stream()
                                  .mapToDouble(StockTransaction::getTotalAmount).sum()));
        
        // Recent activity
        long recentTransactions = transactions.values().stream()
                .filter(t -> t.getTransactionDate().isAfter(LocalDateTime.now().minusDays(7)))
                .count();
        System.out.println("- Transactions (Last 7 days): " + recentTransactions);
        
        logger.log("System statistics displayed");
    }
    
    // Getters for external access
    public Map<String, Item> getAllItems() { return new HashMap<>(items); }
    public Map<String, StockTransaction> getAllTransactions() { return new HashMap<>(transactions); }
    public int getTotalItems() { return items.size(); }
    public int getActiveItems() { return (int) items.values().stream().filter(Item::isActive).count(); }
    public int getLowStockCount() { return getLowStockItems().size(); }
    public int getOverstockCount() { return getOverstockItems().size(); }
    public int getExpiredItemsCount() { return getExpiredItems().size(); }
}