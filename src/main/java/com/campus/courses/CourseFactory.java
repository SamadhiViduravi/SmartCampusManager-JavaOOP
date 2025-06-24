package com.campus.courses;

/**
 * Factory pattern implementation for creating different types of courses
 * Demonstrates Factory Design Pattern
 */
public class CourseFactory {
    
    public Course createCourse(CourseType courseType, String courseId, String courseName, 
                              String courseCode, String department, int credits) {
        Course course = new Course(courseId, courseName, courseCode, department, credits);
        course.setCourseType(courseType);
        
        // Configure course based on type
        switch (courseType) {
            case ONLINE:
                configureOnlineCourse(course);
                break;
            case LAB:
                configureLabCourse(course);
                break;
            case SEMINAR:
                configureSeminarCourse(course);
                break;
            case WORKSHOP:
                configureWorkshopCourse(course);
                break;
            case INTERNSHIP:
                configureInternshipCourse(course);
                break;
            case PROJECT:
                configureProjectCourse(course);
                break;
            case THESIS:
                configureThesisCourse(course);
                break;
            default:
                configureRegularCourse(course);
                break;
        }
        
        return course;
    }
    
    private void configureOnlineCourse(Course course) {
        course.setMaxCapacity(100); // Higher capacity for online courses
        course.setDescription("Online course with virtual lectures and digital resources");
    }
    
    private void configureLabCourse(Course course) {
        course.setMaxCapacity(25); // Lower capacity for lab courses
        course.setDescription("Hands-on laboratory course with practical sessions");
    }
    
    private void configureSeminarCourse(Course course) {
        course.setMaxCapacity(30);
        course.setDescription("Interactive seminar course with discussions and presentations");
    }
    
    private void configureWorkshopCourse(Course course) {
        course.setMaxCapacity(20);
        course.setDescription("Intensive workshop course with practical training");
    }
    
    private void configureInternshipCourse(Course course) {
        course.setMaxCapacity(15);
        course.setDescription("Industry internship course with real-world experience");
    }
    
    private void configureProjectCourse(Course course) {
        course.setMaxCapacity(10);
        course.setDescription("Independent project course with research component");
    }
    
    private void configureThesisCourse(Course course) {
        course.setMaxCapacity(5);
        course.setDescription("Thesis course for advanced research and writing");
    }
    
    private void configureRegularCourse(Course course) {
        course.setMaxCapacity(50); // Standard capacity
        course.setDescription("Regular academic course with lectures and assignments");
    }
}