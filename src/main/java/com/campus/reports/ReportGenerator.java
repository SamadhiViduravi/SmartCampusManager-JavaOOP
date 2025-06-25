package com.campus.reports;

import com.campus.students.StudentManager;
import com.campus.courses.CourseManager;
import com.campus.library.LibraryManager;
import com.campus.transport.TransportManager;
import com.campus.hostel.HostelManager;
import com.campus.inventory.InventoryManager;
import com.campus.exams.ExamManager;
import com.campus.events.EventManager;
import com.campus.utils.Logger;
import com.campus.utils.DateUtils;

import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Report generator for creating various system reports
 */
public class ReportGenerator {
    private static final Logger logger = Logger.getInstance();
    
    private StudentManager studentManager;
    private CourseManager courseManager;
    private LibraryManager libraryManager;
    private TransportManager transportManager;
    private HostelManager hostelManager;
    private InventoryManager inventoryManager;
    private ExamManager examManager;
    private EventManager eventManager;
    
    public ReportGenerator(StudentManager studentManager, CourseManager courseManager,
                          LibraryManager libraryManager, TransportManager transportManager,
                          HostelManager hostelManager, InventoryManager inventoryManager,
                          ExamManager examManager, EventManager eventManager) {
        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.libraryManager = libraryManager;
        this.transportManager = transportManager;
        this.hostelManager = hostelManager;
        this.inventoryManager = inventoryManager;
        this.examManager = examManager;
        this.eventManager = eventManager;
    }
    
    /**
     * Generate comprehensive system overview report
     */
    public Report generateSystemOverviewReport() {
        Report report = new Report("SYS-001", "System Overview Report", ReportType.SYSTEM, 
                                  ReportCategory.OVERVIEW, "System Administrator");
        
        StringBuilder content = new StringBuilder();
        content.append("SMART CAMPUS MANAGEMENT SYSTEM - OVERVIEW REPORT\n");
        content.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        // System Statistics
        content.append("=== SYSTEM STATISTICS ===\n");
        content.append("Total Students: ").append(studentManager.getTotalStudents()).append("\n");
        content.append("Active Students: ").append(studentManager.getActiveStudents()).append("\n");
        content.append("Total Courses: ").append(courseManager.getTotalCourses()).append("\n");
        content.append("Active Courses: ").append(courseManager.getActiveCourses()).append("\n");
        content.append("Library Books: ").append(libraryManager.getTotalBooks()).append("\n");
        content.append("Available Books: ").append(libraryManager.getAvailableBooks()).append("\n");
        content.append("Transport Vehicles: ").append(transportManager.getTotalVehicles()).append("\n");
        content.append("Active Vehicles: ").append(transportManager.getActiveVehicles()).append("\n");
        content.append("Hostel Rooms: ").append(hostelManager.getTotalRooms()).append("\n");
        content.append("Available Rooms: ").append(hostelManager.getAvailableRooms()).append("\n");
        content.append("Inventory Items: ").append(inventoryManager.getTotalItems()).append("\n");
        content.append("Low Stock Items: ").append(inventoryManager.getLowStockCount()).append("\n");
        content.append("Total Exams: ").append(examManager.getTotalExams()).append("\n");
        content.append("Total Events: ").append(eventManager.getTotalEvents()).append("\n\n");
        
        // Performance Metrics
        content.append("=== PERFORMANCE METRICS ===\n");
        content.append("Student-Course Ratio: ").append(calculateStudentCourseRatio()).append("\n");
        content.append("Library Utilization: ").append(calculateLibraryUtilization()).append("%\n");
        content.append("Hostel Occupancy: ").append(calculateHostelOccupancy()).append("%\n");
        content.append("Transport Utilization: ").append(calculateTransportUtilization()).append("%\n");
        content.append("Average Student GPA: ").append(String.format("%.2f", studentManager.getAverageGPA())).append("\n\n");
        
        // Recent Activities
        content.append("=== RECENT ACTIVITIES ===\n");
        content.append("Upcoming Exams: ").append(examManager.getUpcomingExams().size()).append("\n");
        content.append("Upcoming Events: ").append(eventManager.getUpcomingEvents().size()).append("\n");
        content.append("Recent Enrollments: ").append(getRecentEnrollments()).append("\n");
        content.append("Recent Graduations: ").append(getRecentGraduations()).append("\n\n");
        
        report.setContent(content.toString());
        report.setStatus(ReportStatus.COMPLETED);
        
        logger.log("System overview report generated");
        return report;
    }
    
    /**
     * Generate student performance report
     */
    public Report generateStudentPerformanceReport() {
        Report report = new Report("STU-001", "Student Performance Report", ReportType.ACADEMIC, 
                                  ReportCategory.PERFORMANCE, "Academic Office");
        
        StringBuilder content = new StringBuilder();
        content.append("STUDENT PERFORMANCE ANALYSIS REPORT\n");
        content.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        // Overall Statistics
        content.append("=== OVERALL PERFORMANCE ===\n");
        content.append("Total Students: ").append(studentManager.getTotalStudents()).append("\n");
        content.append("Average GPA: ").append(String.format("%.2f", studentManager.getAverageGPA())).append("\n");
        
        // Top Performers
        content.append("\n=== TOP PERFORMERS ===\n");
        studentManager.getTopPerformers(10).forEach(student -> {
            content.append(student.getFullName()).append(" (").append(student.getStudentId())
                   .append(") - GPA: ").append(String.format("%.2f", student.getGpa())).append("\n");
        });
        
        // Department-wise Performance
        content.append("\n=== DEPARTMENT-WISE PERFORMANCE ===\n");
        Map<String, Double> deptPerformance = calculateDepartmentPerformance();
        deptPerformance.entrySet().forEach(entry -> {
            content.append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append("\n");
        });
        
        report.setContent(content.toString());
        report.setStatus(ReportStatus.COMPLETED);
        
        logger.log("Student performance report generated");
        return report;
    }
    
    /**
     * Generate financial report
     */
    public Report generateFinancialReport() {
        Report report = new Report("FIN-001", "Financial Report", ReportType.FINANCIAL, 
                                  ReportCategory.REVENUE, "Finance Office");
        
        StringBuilder content = new StringBuilder();
        content.append("FINANCIAL ANALYSIS REPORT\n");
        content.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        // Hostel Revenue
        content.append("=== HOSTEL REVENUE ===\n");
        content.append("Current Monthly Revenue: $").append(String.format("%.2f", hostelManager.getCurrentMonthlyRevenue())).append("\n");
        content.append("Total Revenue Collected: $").append(String.format("%.2f", hostelManager.getTotalRevenue())).append("\n");
        content.append("Occupancy Rate: ").append(String.format("%.1f", hostelManager.getOccupancyRate())).append("%\n\n");
        
        // Library Fines (if applicable)
        content.append("=== LIBRARY REVENUE ===\n");
        content.append("Total Fines Collected: $").append(String.format("%.2f", calculateLibraryFines())).append("\n");
        content.append("Outstanding Fines: $").append(String.format("%.2f", calculateOutstandingFines())).append("\n\n");
        
        // Transport Revenue (if applicable)
        content.append("=== TRANSPORT REVENUE ===\n");
        content.append("Monthly Transport Revenue: $").append(String.format("%.2f", calculateTransportRevenue())).append("\n\n");
        
        // Total Revenue Summary
        double totalRevenue = hostelManager.getTotalRevenue() + calculateLibraryFines() + calculateTransportRevenue();
        content.append("=== TOTAL REVENUE SUMMARY ===\n");
        content.append("Total System Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");
        
        report.setContent(content.toString());
        report.setStatus(ReportStatus.COMPLETED);
        
        logger.log("Financial report generated");
        return report;
    }
    
    /**
     * Generate inventory report
     */
    public Report generateInventoryReport() {
        Report report = new Report("INV-001", "Inventory Status Report", ReportType.INVENTORY, 
                                  ReportCategory.STOCK, "Inventory Manager");
        
        StringBuilder content = new StringBuilder();
        content.append("INVENTORY STATUS REPORT\n");
        content.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        content.append("=== INVENTORY OVERVIEW ===\n");
        content.append("Total Items: ").append(inventoryManager.getTotalItems()).append("\n");
        content.append("Low Stock Items: ").append(inventoryManager.getLowStockCount()).append("\n");
        content.append("Out of Stock Items: ").append(inventoryManager.getOutOfStockCount()).append("\n\n");
        
        // Low Stock Alert
        content.append("=== LOW STOCK ALERTS ===\n");
        inventoryManager.getLowStockItems().forEach(item -> {
            content.append(item.getItemName()).append(" - Current Stock: ").append(item.getCurrentStock()).append("\n");
        });
        
        report.setContent(content.toString());
        report.setStatus(ReportStatus.COMPLETED);
        
        logger.log("Inventory report generated");
        return report;
    }
    
    /**
     * Generate attendance report
     */
    public Report generateAttendanceReport() {
        Report report = new Report("ATT-001", "Attendance Report", ReportType.ACADEMIC, 
                                  ReportCategory.ATTENDANCE, "Academic Office");
        
        StringBuilder content = new StringBuilder();
        content.append("ATTENDANCE ANALYSIS REPORT\n");
        content.append("Generated on: ").append(LocalDate.now()).append("\n\n");
        
        content.append("=== OVERALL ATTENDANCE ===\n");
        content.append("Average Attendance Rate: ").append(calculateOverallAttendance()).append("%\n");
        content.append("Students with Perfect Attendance: ").append(getPerfectAttendanceCount()).append("\n");
        content.append("Students with Low Attendance: ").append(getLowAttendanceCount()).append("\n\n");
        
        // Course-wise Attendance
        content.append("=== COURSE-WISE ATTENDANCE ===\n");
        Map<String, Double> courseAttendance = calculateCourseAttendance();
        courseAttendance.entrySet().forEach(entry -> {
            content.append(entry.getKey()).append(": ").append(String.format("%.1f", entry.getValue())).append("%\n");
        });
        
        report.setContent(content.toString());
        report.setStatus(ReportStatus.COMPLETED);
        
        logger.log("Attendance report generated");
        return report;
    }
    
    /**
     * Generate custom report based on parameters
     */
    public Report generateCustomReport(String reportId, String title, ReportType type, 
                                     ReportCategory category, String requestedBy, 
                                     Map<String, Object> parameters) {
        Report report = new Report(reportId, title, type, category, requestedBy);
        
        StringBuilder content = new StringBuilder();
        content.append("CUSTOM REPORT: ").append(title.toUpperCase()).append("\n");
        content.append("Generated on: ").append(LocalDate.now()).append("\n");
        content.append("Requested by: ").append(requestedBy).append("\n\n");
        
        // Process parameters and generate content based on type
        switch (type) {
            case ACADEMIC:
                content.append(generateAcademicContent(parameters));
                break;
            case FINANCIAL:
                content.append(generateFinancialContent(parameters));
                break;
            case INVENTORY:
                content.append(generateInventoryContent(parameters));
                break;
            case SYSTEM:
                content.append(generateSystemContent(parameters));
                break;
            default:
                content.append("Custom report content based on provided parameters.\n");
        }
        
        report.setContent(content.toString());
        report.setStatus(ReportStatus.COMPLETED);
        
        logger.log("Custom report generated: " + title);
        return report;
    }
    
    // Helper methods for calculations
    private double calculateStudentCourseRatio() {
        int totalStudents = studentManager.getTotalStudents();
        int totalCourses = courseManager.getTotalCourses();
        return totalCourses > 0 ? (double) totalStudents / totalCourses : 0.0;
    }
    
    private double calculateLibraryUtilization() {
        int totalBooks = libraryManager.getTotalBooks();
        int availableBooks = libraryManager.getAvailableBooks();
        return totalBooks > 0 ? ((double) (totalBooks - availableBooks) / totalBooks) * 100 : 0.0;
    }
    
    private double calculateHostelOccupancy() {
        return hostelManager.getOccupancyRate();
    }
    
    private double calculateTransportUtilization() {
        int totalVehicles = transportManager.getTotalVehicles();
        int activeVehicles = transportManager.getActiveVehicles();
        return totalVehicles > 0 ? ((double) activeVehicles / totalVehicles) * 100 : 0.0;
    }
    
    private int getRecentEnrollments() {
        // This would typically query recent enrollments from the database
        return 15; // Placeholder
    }
    
    private int getRecentGraduations() {
        // This would typically query recent graduations from the database
        return 8; // Placeholder
    }
    
    private Map<String, Double> calculateDepartmentPerformance() {
        Map<String, Double> performance = new HashMap<>();
        // This would calculate actual department-wise GPA averages
        performance.put("Computer Science", 3.45);
        performance.put("Engineering", 3.38);
        performance.put("Business", 3.22);
        performance.put("Arts & Sciences", 3.31);
        return performance;
    }
    
    private double calculateLibraryFines() {
        return 1250.75; // Placeholder
    }
    
    private double calculateOutstandingFines() {
        return 325.50; // Placeholder
    }
    
    private double calculateTransportRevenue() {
        return 2500.00; // Placeholder
    }
    
    private double calculateOverallAttendance() {
        return 87.5; // Placeholder
    }
    
    private int getPerfectAttendanceCount() {
        return 45; // Placeholder
    }
    
    private int getLowAttendanceCount() {
        return 12; // Placeholder
    }
    
    private Map<String, Double> calculateCourseAttendance() {
        Map<String, Double> attendance = new HashMap<>();
        attendance.put("CS101", 92.5);
        attendance.put("MATH201", 88.3);
        attendance.put("ENG101", 85.7);
        attendance.put("PHYS101", 90.1);
        return attendance;
    }
    
    private String generateAcademicContent(Map<String, Object> parameters) {
        StringBuilder content = new StringBuilder();
        content.append("=== ACADEMIC ANALYSIS ===\n");
        content.append("Total Students: ").append(studentManager.getTotalStudents()).append("\n");
        content.append("Average GPA: ").append(String.format("%.2f", studentManager.getAverageGPA())).append("\n");
        return content.toString();
    }
    
    private String generateFinancialContent(Map<String, Object> parameters) {
        StringBuilder content = new StringBuilder();
        content.append("=== FINANCIAL ANALYSIS ===\n");
        content.append("Total Revenue: $").append(String.format("%.2f", hostelManager.getTotalRevenue())).append("\n");
        return content.toString();
    }
    
    private String generateInventoryContent(Map<String, Object> parameters) {
        StringBuilder content = new StringBuilder();
        content.append("=== INVENTORY ANALYSIS ===\n");
        content.append("Total Items: ").append(inventoryManager.getTotalItems()).append("\n");
        content.append("Low Stock Items: ").append(inventoryManager.getLowStockCount()).append("\n");
        return content.toString();
    }
    
    private String generateSystemContent(Map<String, Object> parameters) {
        StringBuilder content = new StringBuilder();
        content.append("=== SYSTEM ANALYSIS ===\n");
        content.append("Total Students: ").append(studentManager.getTotalStudents()).append("\n");
        content.append("Total Courses: ").append(courseManager.getTotalCourses()).append("\n");
        content.append("System Uptime: 99.8%\n");
        return content.toString();
    }
    
    /**
     * Export report to different formats
     */
    public void exportReport(Report report, ReportFormat format, String filePath) {
        switch (format) {
            case PDF:
                exportToPDF(report, filePath);
                break;
            case EXCEL:
                exportToExcel(report, filePath);
                break;
            case CSV:
                exportToCSV(report, filePath);
                break;
            case TXT:
                exportToText(report, filePath);
                break;
            default:
                logger.log("Unsupported export format: " + format);
        }
    }
    
    private void exportToPDF(Report report, String filePath) {
        // PDF export implementation would go here
        logger.log("Report exported to PDF: " + filePath);
    }
    
    private void exportToExcel(Report report, String filePath) {
        // Excel export implementation would go here
        logger.log("Report exported to Excel: " + filePath);
    }
    
    private void exportToCSV(Report report, String filePath) {
        // CSV export implementation would go here
        logger.log("Report exported to CSV: " + filePath);
    }
    
    private void exportToText(Report report, String filePath) {
        // Text export implementation would go here
        logger.log("Report exported to Text: " + filePath);
    }
}