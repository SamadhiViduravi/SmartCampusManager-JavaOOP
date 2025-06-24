package com.campus.users;

import com.campus.courses.Course;
import com.campus.utils.Logger;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Student user class with student-specific functionality
 * Demonstrates inheritance and encapsulation
 */
public class Student extends User {
    private static final Logger logger = Logger.getInstance();
    
    private String studentId;
    private String program;
    private String department;
    private int semester;
    private LocalDate enrollmentDate;
    private double gpa;
    private List<Course> enrolledCourses;
    private List<String> studentPermissions;
    private StudentStatus status;
    
    public Student(String userId, String firstName, String lastName, String email, 
                   String studentId, String program, String department) {
        super(userId, firstName, lastName, email, UserRole.STUDENT);
        this.studentId = studentId;
        this.program = program;
        this.department = department;
        this.semester = 1;
        this.enrollmentDate = LocalDate.now();
        this.gpa = 0.0;
        this.enrolledCourses = new ArrayList<>();
        this.status = StudentStatus.ACTIVE;
        initializePermissions();
        logger.log("Student created: " + getFullName() + " (" + studentId + ")");
    }
    
    private void initializePermissions() {
        studentPermissions = new ArrayList<>();
        studentPermissions.add("VIEW_COURSES");
        studentPermissions.add("ENROLL_COURSES");
        studentPermissions.add("VIEW_GRADES");
        studentPermissions.add("LIBRARY_ACCESS");
        studentPermissions.add("HOSTEL_BOOKING");
        studentPermissions.add("TRANSPORT_BOOKING");
        studentPermissions.add("EVENT_REGISTRATION");
        studentPermissions.add("VIEW_TIMETABLE");
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== STUDENT PROFILE ===");
        System.out.println("User ID: " + userId);
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Program: " + program);
        System.out.println("Department: " + department);
        System.out.println("Semester: " + semester);
        System.out.println("GPA: " + String.format("%.2f", gpa));
        System.out.println("Enrollment Date: " + enrollmentDate);
        System.out.println("Status: " + status);
        System.out.println("Enrolled Courses: " + enrolledCourses.size());
        System.out.println("Created: " + createdAt);
        System.out.println("Last Login: " + lastLoginAt);
    }
    
    @Override
    public boolean hasPermission(String permission) {
        return studentPermissions.contains(permission.toUpperCase());
    }
    
    @Override
    public void performRoleSpecificAction() {
        System.out.println("Student " + getFullName() + " is accessing student portal...");
        viewCourses();
        checkGrades();
        viewTimetable();
    }
    
    public void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            System.out.println("Successfully enrolled in: " + course.getCourseName());
            logger.log("Student " + studentId + " enrolled in course: " + course.getCourseId());
        } else {
            System.out.println("Already enrolled in this course.");
        }
    }
    
    public void dropCourse(Course course) {
        if (enrolledCourses.remove(course)) {
            System.out.println("Successfully dropped course: " + course.getCourseName());
            logger.log("Student " + studentId + " dropped course: " + course.getCourseId());
        } else {
            System.out.println("Not enrolled in this course.");
        }
    }
    
    public void viewCourses() {
        System.out.println("=== ENROLLED COURSES ===");
        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            for (Course course : enrolledCourses) {
                System.out.println("- " + course.getCourseName() + " (" + course.getCourseId() + ")");
            }
        }
    }
    
    public void checkGrades() {
        System.out.println("=== ACADEMIC PERFORMANCE ===");
        System.out.println("Current GPA: " + String.format("%.2f", gpa));
        System.out.println("Semester: " + semester);
        // Additional grade details would be implemented here
    }
    
    public void viewTimetable() {
        System.out.println("=== CLASS TIMETABLE ===");
        System.out.println("Displaying timetable for " + getFullName());
        // Timetable implementation would be here
    }
    
    public void updateGPA(double newGPA) {
        this.gpa = newGPA;
        logger.log("GPA updated for student " + studentId + ": " + newGPA);
    }
    
    public void promoteToNextSemester() {
        this.semester++;
        logger.log("Student " + studentId + " promoted to semester " + semester);
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    
    public List<Course> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }
    
    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }
    
    public List<String> getStudentPermissions() { return new ArrayList<>(studentPermissions); }
}