package com.campus.reports;

import com.campus.utils.Identifiable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Report class representing system reports
 */
public class Report implements Identifiable {
    private String reportId;
    private String reportName;
    private String description;
    private ReportType reportType;
    private ReportCategory category;
    private String generatedBy;
    private LocalDateTime generatedAt;
    private LocalDateTime scheduledAt;
    private ReportStatus status;
    private String filePath;
    private String fileName;
    private ReportFormat format;
    private Map<String, Object> parameters;
    private List<String> recipients;
    private String content;
    private long fileSize;
    private boolean isScheduled;
    private String scheduleFrequency;
    private LocalDateTime nextRun;
    private int executionCount;
    private String errorMessage;
    private LocalDateTime lastModified;
    private Map<String, String> metadata;
    
    public Report(String reportId, String reportName, ReportType reportType, ReportCategory category) {
        this.reportId = reportId;
        this.reportName = reportName;
        this.reportType = reportType;
        this.category = category;
        this.status = ReportStatus.PENDING;
        this.format = ReportFormat.PDF;
        this.parameters = new HashMap<>();
        this.recipients = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.isScheduled = false;
        this.executionCount = 0;
        this.generatedAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }
    
    public void generateReport() {
        if (status == ReportStatus.GENERATING) {
            throw new IllegalStateException("Report is already being generated");
        }
        
        this.status = ReportStatus.GENERATING;
        this.generatedAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        
        try {
            // Simulate report generation
            Thread.sleep(1000); // Simulate processing time
            
            this.content = generateReportContent();
            this.fileName = generateFileName();
            this.filePath = "/reports/" + fileName;
            this.fileSize = content.length();
            this.status = ReportStatus.COMPLETED;
            this.executionCount++;
            
        } catch (Exception e) {
            this.status = ReportStatus.FAILED;
            this.errorMessage = e.getMessage();
        }
        
        this.lastModified = LocalDateTime.now();
    }
    
    private String generateReportContent() {
        StringBuilder content = new StringBuilder();
        
        content.append("=== ").append(reportName.toUpperCase()).append(" ===\n");
        content.append("Generated: ").append(generatedAt).append("\n");
        content.append("Generated By: ").append(generatedBy).append("\n");
        content.append("Report Type: ").append(reportType).append("\n");
        content.append("Category: ").append(category).append("\n\n");
        
        // Add report-specific content based on type
        switch (reportType) {
            case STUDENT_REPORT:
                content.append(generateStudentReportContent());
                break;
            case COURSE_REPORT:
                content.append(generateCourseReportContent());
                break;
            case FINANCIAL_REPORT:
                content.append(generateFinancialReportContent());
                break;
            case ATTENDANCE_REPORT:
                content.append(generateAttendanceReportContent());
                break;
            case PERFORMANCE_REPORT:
                content.append(generatePerformanceReportContent());
                break;
            case INVENTORY_REPORT:
                content.append(generateInventoryReportContent());
                break;
            case EVENT_REPORT:
                content.append(generateEventReportContent());
                break;
            case SYSTEM_REPORT:
                content.append(generateSystemReportContent());
                break;
            default:
                content.append("Report content not available for this type.\n");
        }
        
        content.append("\n=== END OF REPORT ===\n");
        return content.toString();
    }
    
    private String generateStudentReportContent() {
        return "STUDENT STATISTICS:\n" +
               "- Total Students: 1,250\n" +
               "- Active Students: 1,180\n" +
               "- New Enrollments (This Month): 45\n" +
               "- Graduation Rate: 92%\n" +
               "- Average GPA: 3.2\n\n" +
               "DEPARTMENT DISTRIBUTION:\n" +
               "- Computer Science: 320 students\n" +
               "- Engineering: 280 students\n" +
               "- Business: 250 students\n" +
               "- Arts & Sciences: 400 students\n\n";
    }
    
    private String generateCourseReportContent() {
        return "COURSE STATISTICS:\n" +
               "- Total Courses: 450\n" +
               "- Active Courses: 380\n" +
               "- Average Enrollment: 28 students\n" +
               "- Course Completion Rate: 88%\n" +
               "- Most Popular Course: Introduction to Programming\n\n" +
               "COURSE CATEGORIES:\n" +
               "- Core Courses: 120\n" +
               "- Electives: 180\n" +
               "- Laboratory: 80\n" +
               "- Seminars: 70\n\n";
    }
    
    private String generateFinancialReportContent() {
        return "FINANCIAL SUMMARY:\n" +
               "- Total Revenue: $2,450,000\n" +
               "- Tuition Revenue: $2,100,000\n" +
               "- Other Revenue: $350,000\n" +
               "- Total Expenses: $2,200,000\n" +
               "- Net Income: $250,000\n\n" +
               "EXPENSE BREAKDOWN:\n" +
               "- Salaries: $1,400,000 (64%)\n" +
               "- Infrastructure: $450,000 (20%)\n" +
               "- Operations: $350,000 (16%)\n\n";
    }
    
    private String generateAttendanceReportContent() {
        return "ATTENDANCE STATISTICS:\n" +
               "- Overall Attendance Rate: 87%\n" +
               "- Best Performing Department: Computer Science (92%)\n" +
               "- Lowest Performing Department: Arts (82%)\n" +
               "- Students with Perfect Attendance: 156\n" +
               "- Students with Poor Attendance (<70%): 89\n\n" +
               "MONTHLY TRENDS:\n" +
               "- January: 89%\n" +
               "- February: 87%\n" +
               "- March: 85%\n\n";
    }
    
    private String generatePerformanceReportContent() {
        return "ACADEMIC PERFORMANCE:\n" +
               "- Average GPA: 3.2\n" +
               "- Students with GPA > 3.5: 420 (34%)\n" +
               "- Students with GPA < 2.0: 78 (6%)\n" +
               "- Dean's List Students: 156\n" +
               "- Academic Probation: 45\n\n" +
               "GRADE DISTRIBUTION:\n" +
               "- A: 25%\n" +
               "- B: 35%\n" +
               "- C: 28%\n" +
               "- D: 8%\n" +
               "- F: 4%\n\n";
    }
    
    private String generateInventoryReportContent() {
        return "INVENTORY SUMMARY:\n" +
               "- Total Items: 2,450\n" +
               "- Total Value: $485,000\n" +
               "- Low Stock Items: 23\n" +
               "- Overstock Items: 12\n" +
               "- Items Requiring Maintenance: 8\n\n" +
               "CATEGORY BREAKDOWN:\n" +
               "- Electronics: $245,000 (50%)\n" +
               "- Furniture: $120,000 (25%)\n" +
               "- Laboratory Equipment: $85,000 (17%)\n" +
               "- Stationery: $35,000 (8%)\n\n";
    }
    
    private String generateEventReportContent() {
        return "EVENT STATISTICS:\n" +
               "- Total Events (This Month): 45\n" +
               "- Completed Events: 38\n" +
               "- Upcoming Events: 7\n" +
               "- Total Attendees: 2,850\n" +
               "- Average Attendance Rate: 78%\n\n" +
               "EVENT CATEGORIES:\n" +
               "- Academic: 18 events\n" +
               "- Cultural: 12 events\n" +
               "- Sports: 8 events\n" +
               "- Professional: 7 events\n\n";
    }
    
    private String generateSystemReportContent() {
        return "SYSTEM PERFORMANCE:\n" +
               "- System Uptime: 99.8%\n" +
               "- Active Users: 1,450\n" +
               "- Database Size: 2.5 GB\n" +
               "- Backup Status: Current\n" +
               "- Security Incidents: 0\n\n" +
               "MODULE USAGE:\n" +
               "- Student Management: 45%\n" +
               "- Course Management: 25%\n" +
               "- Exam Management: 15%\n" +
               "- Event Management: 10%\n" +
               "- Other Modules: 5%\n\n";
    }
    
    private String generateFileName() {
        String timestamp = generatedAt.toString().replaceAll("[^0-9]", "");
        return reportName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + "." + format.toString().toLowerCase();
    }
    
    public void scheduleReport(String frequency, LocalDateTime nextRun) {
        this.isScheduled = true;
        this.scheduleFrequency = frequency;
        this.nextRun = nextRun;
        this.lastModified = LocalDateTime.now();
    }
    
    public void unscheduleReport() {
        this.isScheduled = false;
        this.scheduleFrequency = null;
        this.nextRun = null;
        this.lastModified = LocalDateTime.now();
    }
    
    public void addRecipient(String recipient) {
        if (!recipients.contains(recipient)) {
            recipients.add(recipient);
            this.lastModified = LocalDateTime.now();
        }
    }
    
    public void removeRecipient(String recipient) {
        if (recipients.remove(recipient)) {
            this.lastModified = LocalDateTime.now();
        }
    }
    
    public void addParameter(String key, Object value) {
        parameters.put(key, value);
        this.lastModified = LocalDateTime.now();
    }
    
    public void removeParameter(String key) {
        if (parameters.remove(key) != null) {
            this.lastModified = LocalDateTime.now();
        }
    }
    
    public void addMetadata(String key, String value) {
        metadata.put(key, value);
        this.lastModified = LocalDateTime.now();
    }
    
    public boolean isOverdue() {
        return isScheduled && nextRun != null && nextRun.isBefore(LocalDateTime.now());
    }
    
    public boolean isReady() {
        return status == ReportStatus.COMPLETED && content != null;
    }
    
    public void displayReportInfo() {
        System.out.println("=== REPORT INFORMATION ===");
        System.out.println("Report ID: " + reportId);
        System.out.println("Report Name: " + reportName);
        System.out.println("Description: " + (description != null ? description : "No description"));
        System.out.println("Type: " + reportType);
        System.out.println("Category: " + category);
        System.out.println("Status: " + status);
        System.out.println("Format: " + format);
        System.out.println("Generated By: " + (generatedBy != null ? generatedBy : "System"));
        System.out.println("Generated At: " + generatedAt);
        System.out.println("File Name: " + (fileName != null ? fileName : "Not generated"));
        System.out.println("File Path: " + (filePath != null ? filePath : "Not available"));
        System.out.println("File Size: " + (fileSize > 0 ? fileSize + " bytes" : "Not available"));
        System.out.println("Scheduled: " + (isScheduled ? "Yes" : "No"));
        
        if (isScheduled) {
            System.out.println("Schedule Frequency: " + scheduleFrequency);
            System.out.println("Next Run: " + nextRun);
            System.out.println("Execution Count: " + executionCount);
        }
        
        System.out.println("Recipients: " + (recipients.isEmpty() ? "None" : String.join(", ", recipients)));
        System.out.println("Parameters: " + parameters.size());
        System.out.println("Overdue: " + (isOverdue() ? "Yes" : "No"));
        System.out.println("Ready: " + (isReady() ? "Yes" : "No"));
        
        if (status == ReportStatus.FAILED && errorMessage != null) {
            System.out.println("Error: " + errorMessage);
        }
        
        if (!metadata.isEmpty()) {
            System.out.println("Metadata:");
            metadata.forEach((key, value) -> System.out.println("- " + key + ": " + value));
        }
        
        System.out.println("Last Modified: " + lastModified);
    }
    
    public void displayReportContent() {
        System.out.println("=== REPORT CONTENT ===");
        if (content != null && !content.isEmpty()) {
            System.out.println(content);
        } else {
            System.out.println("Report content not available. Generate the report first.");
        }
    }
    
    // Getters and Setters
    @Override
    public String getId() { return reportId; }
    
    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }
    
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { 
        this.reportName = reportName;
        this.lastModified = LocalDateTime.now();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.lastModified = LocalDateTime.now();
    }
    
    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { 
        this.reportType = reportType;
        this.lastModified = LocalDateTime.now();
    }
    
    public ReportCategory getCategory() { return category; }
    public void setCategory(ReportCategory category) { 
        this.category = category;
        this.lastModified = LocalDateTime.now();
    }
    
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { 
        this.generatedBy = generatedBy;
        this.lastModified = LocalDateTime.now();
    }
    
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    
    public ReportStatus getStatus() { return status; }
    public String getFilePath() { return filePath; }
    public String getFileName() { return fileName; }
    
    public ReportFormat getFormat() { return format; }
    public void setFormat(ReportFormat format) { 
        this.format = format;
        this.lastModified = LocalDateTime.now();
    }
    
    public Map<String, Object> getParameters() { return new HashMap<>(parameters); }
    public List<String> getRecipients() { return new ArrayList<>(recipients); }
    public String getContent() { return content; }
    public long getFileSize() { return fileSize; }
    
    public boolean isScheduled() { return isScheduled; }
    public String getScheduleFrequency() { return scheduleFrequency; }
    public LocalDateTime getNextRun() { return nextRun; }
    public int getExecutionCount() { return executionCount; }
    public String getErrorMessage() { return errorMessage; }
    public LocalDateTime getLastModified() { return lastModified; }
    public Map<String, String> getMetadata() { return new HashMap<>(metadata); }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(reportId, report.reportId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(reportId);
    }
    
    @Override
    public String toString() {
        return "Report{" +
                "reportId='" + reportId + '\'' +
                ", reportName='" + reportName + '\'' +
                ", reportType=" + reportType +
                ", category=" + category +
                ", status=" + status +
                ", isScheduled=" + isScheduled +
                ", executionCount=" + executionCount +
                '}';
    }
}