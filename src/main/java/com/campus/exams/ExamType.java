package com.campus.exams;

/**
 * Enumeration for exam types
 */
public enum ExamType {
    QUIZ("Quiz"),
    MIDTERM("Midterm Exam"),
    FINAL("Final Exam"),
    PRACTICAL("Practical Exam"),
    ASSIGNMENT("Assignment"),
    PROJECT("Project"),
    PRESENTATION("Presentation"),
    VIVA("Viva Voce"),
    MAKEUP("Makeup Exam"),
    SUPPLEMENTARY("Supplementary Exam");
    
    private final String displayName;
    
    ExamType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}