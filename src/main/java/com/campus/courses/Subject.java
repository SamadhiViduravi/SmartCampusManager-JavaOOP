package com.campus.courses;

import com.campus.utils.Identifiable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Subject entity class representing course subjects/topics
 */
public class Subject implements Identifiable {
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private String description;
    private int credits;
    private Course course;
    private String syllabus;
    private String textbook;
    private String referenceBooks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isCore;
    private String prerequisites;
    
    public Subject(String subjectId, String subjectName, String subjectCode, int credits) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.credits = credits;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isCore = true;
    }
    
    public void displaySubjectInfo() {
        System.out.println("=== SUBJECT INFORMATION ===");
        System.out.println("Subject ID: " + subjectId);
        System.out.println("Subject Name: " + subjectName);
        System.out.println("Subject Code: " + subjectCode);
        System.out.println("Credits: " + credits);
        System.out.println("Course: " + (course != null ? course.getCourseName() : "Not Assigned"));
        System.out.println("Type: " + (isCore ? "Core" : "Elective"));
        System.out.println("Prerequisites: " + (prerequisites != null ? prerequisites : "None"));
        System.out.println("Textbook: " + (textbook != null ? textbook : "Not Specified"));
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
    }
    
    // Getters and Setters
    @Override
    public String getId() { return subjectId; }
    
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { 
        this.subjectId = subjectId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { 
        this.subjectName = subjectName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { 
        this.subjectCode = subjectCode;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { 
        this.credits = credits;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { 
        this.course = course;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSyllabus() { return syllabus; }
    public void setSyllabus(String syllabus) { 
        this.syllabus = syllabus;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getTextbook() { return textbook; }
    public void setTextbook(String textbook) { 
        this.textbook = textbook;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getReferenceBooks() { return referenceBooks; }
    public void setReferenceBooks(String referenceBooks) { 
        this.referenceBooks = referenceBooks;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public boolean isCore() { return isCore; }
    public void setCore(boolean core) { 
        isCore = core;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPrerequisites() { return prerequisites; }
    public void setPrerequisites(String prerequisites) { 
        this.prerequisites = prerequisites;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(subjectId, subject.subjectId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(subjectId);
    }
    
    @Override
    public String toString() {
        return "Subject{" +
                "subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", credits=" + credits +
                ", isCore=" + isCore +
                '}';
    }
}