package com.campus.exams;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Exam class representing academic examinations
 */
public class Exam implements Identifiable {
    private String examId;
    private String examName;
    private String courseId;
    private String courseName;
    private ExamType examType;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int duration; // in minutes
    private String venue;
    private int maxMarks;
    private int passingMarks;
    private String instructorId;
    private List<String> invigilators;
    private ExamStatus status;
    private String instructions;
    private List<String> allowedMaterials;
    private List<String> enrolledStudents;
    private Map<String, ExamResult> results;
    private boolean isOnline;
    private String onlinePlatform;
    private String examLink;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    
    public Exam(String examId, String examName, String courseId, String courseName, ExamType examType) {
        this.examId = examId;
        this.examName = examName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.examType = examType;
        this.status = ExamStatus.SCHEDULED;
        this.invigilators = new ArrayList<>();
        this.allowedMaterials = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.results = new HashMap<>();
        this.isOnline = false;
        this.duration = 180; // 3 hours default
        this.maxMarks = 100;
        this.passingMarks = 40;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        initializeDefaultSettings();
    }
    
    private void initializeDefaultSettings() {
        // Default allowed materials based on exam type
        switch (examType) {
            case MIDTERM:
            case FINAL:
                allowedMaterials.add("Pen/Pencil");
                allowedMaterials.add("Calculator (if applicable)");
                break;
            case QUIZ:
                allowedMaterials.add("Pen/Pencil");
                this.duration = 60; // 1 hour for quiz
                this.maxMarks = 50;
                this.passingMarks = 20;
                break;
            case PRACTICAL:
                allowedMaterials.add("Lab Manual");
                allowedMaterials.add("Calculator");
                this.duration = 240; // 4 hours for practical
                break;
            case ASSIGNMENT:
                this.duration = 10080; // 1 week in minutes
                this.isOnline = true;
                break;
        }
        
        // Default instructions
        this.instructions = "1. Read all questions carefully\n" +
                           "2. Attempt all questions\n" +
                           "3. Write clearly and legibly\n" +
                           "4. Manage your time effectively";
    }
    
    public void scheduleExam(LocalDate examDate, LocalTime startTime, String venue) {
        if (status != ExamStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled exams can be rescheduled");
        }
        
        this.examDate = examDate;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(duration);
        this.venue = venue;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addInvigilator(String invigilatorId) {
        if (!invigilators.contains(invigilatorId)) {
            invigilators.add(invigilatorId);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeInvigilator(String invigilatorId) {
        if (invigilators.remove(invigilatorId)) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void enrollStudent(String studentId) {
        if (!enrolledStudents.contains(studentId)) {
            enrolledStudents.add(studentId);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void unenrollStudent(String studentId) {
        if (enrolledStudents.remove(studentId)) {
            results.remove(studentId); // Remove result if exists
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void startExam() {
        if (status != ExamStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled exams can be started");
        }
        
        this.status = ExamStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void endExam() {
        if (status != ExamStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only in-progress exams can be ended");
        }
        
        this.status = ExamStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancelExam(String reason) {
        if (status == ExamStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed exam");
        }
        
        this.status = ExamStatus.CANCELLED;
        this.notes = (notes != null ? notes + "; " : "") + "Cancelled: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void postponeExam(LocalDate newDate, LocalTime newTime, String reason) {
        if (status == ExamStatus.COMPLETED || status == ExamStatus.CANCELLED) {
            throw new IllegalStateException("Cannot postpone completed or cancelled exam");
        }
        
        this.examDate = newDate;
        this.startTime = newTime;
        this.endTime = newTime.plusMinutes(duration);
        this.status = ExamStatus.POSTPONED;
        this.notes = (notes != null ? notes + "; " : "") + "Postponed: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addResult(String studentId, int marksObtained, String grade) {
        if (!enrolledStudents.contains(studentId)) {
            throw new IllegalArgumentException("Student is not enrolled for this exam");
        }
        
        ExamResult result = new ExamResult(studentId, examId, marksObtained, maxMarks, grade);
        results.put(studentId, result);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateResult(String studentId, int marksObtained, String grade) {
        ExamResult result = results.get(studentId);
        if (result != null) {
            result.updateMarks(marksObtained, grade);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public ExamResult getStudentResult(String studentId) {
        return results.get(studentId);
    }
    
    public void addAllowedMaterial(String material) {
        if (!allowedMaterials.contains(material)) {
            allowedMaterials.add(material);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeAllowedMaterial(String material) {
        if (allowedMaterials.remove(material)) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void setOnlineExam(String platform, String examLink) {
        this.isOnline = true;
        this.onlinePlatform = platform;
        this.examLink = examLink;
        this.venue = "Online - " + platform;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void setOfflineExam(String venue) {
        this.isOnline = false;
        this.onlinePlatform = null;
        this.examLink = null;
        this.venue = venue;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isUpcoming() {
        return examDate != null && examDate.isAfter(LocalDate.now());
    }
    
    public boolean isToday() {
        return examDate != null && examDate.equals(LocalDate.now());
    }
    
    public boolean isOverdue() {
        return examDate != null && examDate.isBefore(LocalDate.now()) && 
               status == ExamStatus.SCHEDULED;
    }
    
    public int getEnrolledCount() {
        return enrolledStudents.size();
    }
    
    public int getResultsCount() {
        return results.size();
    }
    
    public double getAverageMarks() {
        if (results.isEmpty()) return 0.0;
        
        return results.values().stream()
                .mapToInt(ExamResult::getMarksObtained)
                .average()
                .orElse(0.0);
    }
    
    public double getPassPercentage() {
        if (results.isEmpty()) return 0.0;
        
        long passedCount = results.values().stream()
                .filter(result -> result.getMarksObtained() >= passingMarks)
                .count();
        
        return (double) passedCount / results.size() * 100;
    }
    
    public Map<String, Integer> getGradeDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        
        results.values().forEach(result -> {
            String grade = result.getGrade();
            distribution.put(grade, distribution.getOrDefault(grade, 0) + 1);
        });
        
        return distribution;
    }
    
    public void addNote(String note) {
        this.notes = (notes != null ? notes + "; " : "") + note;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void displayExamInfo() {
        System.out.println("=== EXAM INFORMATION ===");
        System.out.println("Exam ID: " + examId);
        System.out.println("Exam Name: " + examName);
        System.out.println("Course: " + courseName + " (" + courseId + ")");
        System.out.println("Type: " + examType);
        System.out.println("Status: " + status);
        System.out.println("Date: " + (examDate != null ? examDate : "Not scheduled"));
        System.out.println("Time: " + (startTime != null ? startTime + " - " + endTime : "Not scheduled"));
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Venue: " + (venue != null ? venue : "Not assigned"));
        System.out.println("Mode: " + (isOnline ? "Online" : "Offline"));
        if (isOnline) {
            System.out.println("Platform: " + onlinePlatform);
            System.out.println("Exam Link: " + examLink);
        }
        System.out.println("Max Marks: " + maxMarks);
        System.out.println("Passing Marks: " + passingMarks);
        System.out.println("Instructor ID: " + (instructorId != null ? instructorId : "Not assigned"));
        System.out.println("Invigilators: " + (invigilators.isEmpty() ? "None" : String.join(", ", invigilators)));
        System.out.println("Enrolled Students: " + enrolledStudents.size());
        System.out.println("Results Submitted: " + results.size());
        System.out.println("Allowed Materials: " + String.join(", ", allowedMaterials));
        System.out.println("Upcoming: " + (isUpcoming() ? "Yes" : "No"));
        System.out.println("Today: " + (isToday() ? "Yes" : "No"));
        System.out.println("Overdue: " + (isOverdue() ? "Yes" : "No"));
        
        if (!results.isEmpty()) {
            System.out.println("Average Marks: " + String.format("%.2f", getAverageMarks()));
            System.out.println("Pass Percentage: " + String.format("%.1f", getPassPercentage()) + "%");
        }
        
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
        System.out.println("Instructions: " + instructions);
        System.out.println("Notes: " + (notes != null ? notes : "None"));
    }
    
    public void displayResults() {
        System.out.println("=== EXAM RESULTS: " + examName + " ===");
        
        if (results.isEmpty()) {
            System.out.println("No results available yet.");
            return;
        }
        
        System.out.printf("%-12s %-10s %-8s %-8s %-6s%n", 
                         "Student ID", "Marks", "Max", "Percent", "Grade");
        System.out.println("-".repeat(50));
        
        results.values().stream()
            .sorted((r1, r2) -> Integer.compare(r2.getMarksObtained(), r1.getMarksObtained()))
            .forEach(result -> {
                double percentage = (double) result.getMarksObtained() / result.getMaxMarks() * 100;
                System.out.printf("%-12s %-10d %-8d %-8.1f %-6s%n",
                                 result.getStudentId(),
                                 result.getMarksObtained(),
                                 result.getMaxMarks(),
                                 percentage,
                                 result.getGrade());
            });
        
        System.out.println("-".repeat(50));
        System.out.println("Statistics:");
        System.out.println("- Total Students: " + results.size());
        System.out.println("- Average Marks: " + String.format("%.2f", getAverageMarks()));
        System.out.println("- Pass Percentage: " + String.format("%.1f", getPassPercentage()) + "%");
        
        // Grade distribution
        Map<String, Integer> gradeDistribution = getGradeDistribution();
        if (!gradeDistribution.isEmpty()) {
            System.out.println("- Grade Distribution:");
            gradeDistribution.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("  " + entry.getKey() + ": " + entry.getValue() + " students"));
        }
    }
    
    // Getters and Setters
    @Override
    public String getId() { return examId; }
    
    public String getExamId() { return examId; }
    public void setExamId(String examId) { this.examId = examId; }
    
    public String getExamName() { return examName; }
    public void setExamName(String examName) { 
        this.examName = examName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { 
        this.courseId = courseId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { 
        this.courseName = courseName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public ExamType getExamType() { return examType; }
    public void setExamType(ExamType examType) { 
        this.examType = examType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getExamDate() { return examDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { 
        this.duration = duration;
        if (startTime != null) {
            this.endTime = startTime.plusMinutes(duration);
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getVenue() { return venue; }
    public void setVenue(String venue) { 
        this.venue = venue;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getMaxMarks() { return maxMarks; }
    public void setMaxMarks(int maxMarks) { 
        this.maxMarks = maxMarks;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getPassingMarks() { return passingMarks; }
    public void setPassingMarks(int passingMarks) { 
        this.passingMarks = passingMarks;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { 
        this.instructorId = instructorId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getInvigilators() { return new ArrayList<>(invigilators); }
    public ExamStatus getStatus() { return status; }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { 
        this.instructions = instructions;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getAllowedMaterials() { return new ArrayList<>(allowedMaterials); }
    public List<String> getEnrolledStudents() { return new ArrayList<>(enrolledStudents); }
    public Map<String, ExamResult> getAllResults() { return new HashMap<>(results); }
    
    public boolean isOnline() { return isOnline; }
    public String getOnlinePlatform() { return onlinePlatform; }
    public String getExamLink() { return examLink; }
    
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
        Exam exam = (Exam) o;
        return Objects.equals(examId, exam.examId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(examId);
    }
    
    @Override
    public String toString() {
        return "Exam{" +
                "examId='" + examId + '\'' +
                ", examName='" + examName + '\'' +
                ", courseId='" + courseId + '\'' +
                ", examType=" + examType +
                ", examDate=" + examDate +
                ", status=" + status +
                ", enrolledStudents=" + enrolledStudents.size() +
                ", results=" + results.size() +
                '}';
    }
}