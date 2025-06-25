package com.campus.inventory;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Item class representing inventory items
 */
public class Item implements Identifiable {
    private String itemId;
    private String itemName;
    private String description;
    private ItemCategory category;
    private String brand;
    private String model;
    private String serialNumber;
    private int quantity;
    private int minStockLevel;
    private int maxStockLevel;
    private double unitPrice;
    private double totalValue;
    private String supplier;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
    private String location;
    private ItemCondition condition;
    private boolean isActive;
    private String barcode;
    private List<String> tags;
    private Map<String, String> specifications;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    
    public Item(String itemId, String itemName, ItemCategory category, int quantity, double unitPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalValue = quantity * unitPrice;
        this.condition = ItemCondition.NEW;
        this.isActive = true;
        this.minStockLevel = 10;
        this.maxStockLevel = 100;
        this.tags = new ArrayList<>();
        this.specifications = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.purchaseDate = LocalDate.now();
        
        generateBarcode();
    }
    
    private void generateBarcode() {
        this.barcode = "BC" + itemId + String.valueOf(System.currentTimeMillis()).substring(8);
    }
    
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        this.quantity += quantity;
        updateTotalValue();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        if (this.quantity < quantity) {
            throw new IllegalStateException("Insufficient stock. Available: " + this.quantity);
        }
        
        this.quantity -= quantity;
        updateTotalValue();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateStock(int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        
        this.quantity = newQuantity;
        updateTotalValue();
        this.updatedAt = LocalDateTime.now();
    }
    
    private void updateTotalValue() {
        this.totalValue = quantity * unitPrice;
    }
    
    public boolean isLowStock() {
        return quantity <= minStockLevel;
    }
    
    public boolean isOverStock() {
        return quantity >= maxStockLevel;
    }
    
    public boolean isExpired() {
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }
    
    public boolean isExpiringSoon(int days) {
        return expiryDate != null && 
               LocalDate.now().plusDays(days).isAfter(expiryDate) && 
               !isExpired();
    }
    
    public void updatePrice(double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        this.unitPrice = newPrice;
        updateTotalValue();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateCondition(ItemCondition newCondition) {
        this.condition = newCondition;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeTag(String tag) {
        if (tags.remove(tag)) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void addSpecification(String key, String value) {
        specifications.put(key, value);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeSpecification(String key) {
        if (specifications.remove(key) != null) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void relocate(String newLocation) {
        this.location = newLocation;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate(String reason) {
        this.isActive = false;
        this.notes = (notes != null ? notes + "; " : "") + "Deactivated: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reactivate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addNote(String note) {
        this.notes = (notes != null ? notes + "; " : "") + note;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void displayItemInfo() {
        System.out.println("=== ITEM INFORMATION ===");
        System.out.println("Item ID: " + itemId);
        System.out.println("Name: " + itemName);
        System.out.println("Description: " + (description != null ? description : "None"));
        System.out.println("Category: " + category);
        System.out.println("Brand: " + (brand != null ? brand : "Unknown"));
        System.out.println("Model: " + (model != null ? model : "Unknown"));
        System.out.println("Serial Number: " + (serialNumber != null ? serialNumber : "N/A"));
        System.out.println("Barcode: " + barcode);
        System.out.println("Quantity: " + quantity);
        System.out.println("Unit Price: $" + String.format("%.2f", unitPrice));
        System.out.println("Total Value: $" + String.format("%.2f", totalValue));
        System.out.println("Min Stock Level: " + minStockLevel);
        System.out.println("Max Stock Level: " + maxStockLevel);
        System.out.println("Stock Status: " + getStockStatus());
        System.out.println("Condition: " + condition);
        System.out.println("Location: " + (location != null ? location : "Not specified"));
        System.out.println("Supplier: " + (supplier != null ? supplier : "Unknown"));
        System.out.println("Purchase Date: " + purchaseDate);
        System.out.println("Expiry Date: " + (expiryDate != null ? expiryDate : "No expiry"));
        System.out.println("Expired: " + (isExpired() ? "Yes" : "No"));
        System.out.println("Expiring Soon: " + (isExpiringSoon(30) ? "Yes (within 30 days)" : "No"));
        System.out.println("Active: " + (isActive ? "Yes" : "No"));
        System.out.println("Tags: " + (tags.isEmpty() ? "None" : String.join(", ", tags)));
        
        if (!specifications.isEmpty()) {
            System.out.println("Specifications:");
            specifications.forEach((key, value) -> System.out.println("- " + key + ": " + value));
        }
        
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
        System.out.println("Notes: " + (notes != null ? notes : "None"));
    }
    
    public String getStockStatus() {
        if (isLowStock()) return "LOW STOCK";
        if (isOverStock()) return "OVERSTOCK";
        return "NORMAL";
    }
    
    // Getters and Setters
    @Override
    public String getId() { return itemId; }
    
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { 
        this.itemName = itemName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public ItemCategory getCategory() { return category; }
    public void setCategory(ItemCategory category) { 
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { 
        this.brand = brand;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getModel() { return model; }
    public void setModel(String model) { 
        this.model = model;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { 
        this.serialNumber = serialNumber;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getQuantity() { return quantity; }
    
    public int getMinStockLevel() { return minStockLevel; }
    public void setMinStockLevel(int minStockLevel) { 
        this.minStockLevel = minStockLevel;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getMaxStockLevel() { return maxStockLevel; }
    public void setMaxStockLevel(int maxStockLevel) { 
        this.maxStockLevel = maxStockLevel;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getUnitPrice() { return unitPrice; }
    public double getTotalValue() { return totalValue; }
    
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { 
        this.supplier = supplier;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { 
        this.purchaseDate = purchaseDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { 
        this.expiryDate = expiryDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { 
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }
    
    public ItemCondition getCondition() { return condition; }
    public boolean isActive() { return isActive; }
    
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { 
        this.barcode = barcode;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getTags() { return new ArrayList<>(tags); }
    public Map<String, String> getSpecifications() { return new HashMap<>(specifications); }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(itemId, item.itemId) || 
               Objects.equals(barcode, item.barcode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(itemId, barcode);
    }
    
    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", category=" + category +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalValue=" + totalValue +
                ", condition=" + condition +
                ", isActive=" + isActive +
                '}';
    }
}