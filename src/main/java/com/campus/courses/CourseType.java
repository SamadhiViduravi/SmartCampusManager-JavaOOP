package com.campus.courses;

/**
 * Enumeration for different types of courses
 */
public enum CourseType {
    REGULAR("Regular Course"),
    ONLINE("Online Course"),
    LAB("Laboratory Course"),
    SEMINAR("Seminar Course"),
    WORKSHOP("Workshop Course"),
    INTERNSHIP("Internship Course"),
    PROJECT("Project Course"),
    THESIS("Thesis Course");
    
    private final String displayName;
    
    CourseType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() { return displayName; }
    
    @Override
    public String toString() { return displayName; }
}