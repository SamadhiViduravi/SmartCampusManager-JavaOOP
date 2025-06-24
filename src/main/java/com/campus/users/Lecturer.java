package com.campus.users;

import com.campus.courses.Course;
import com.campus.utils.Logger;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Lecturer user class with teaching-specific functionality
 * Demonstrates inheritance and polymorphism
 */
public class Lecturer extends User {
    private static final Logger logger = Logger.getInstance();
    
    private String employeeId;
    private String department;
    private String designation;
    private String qualification;
    private double salary;
    private LocalDate joiningDate;
    private List<Course> assignedCourses;
    private List<String> lecturerPermissions;
    private int experienceYears;
    private String specialization;
    
    public Lecturer(String userId, String firstName, String lastName, String email,
                    String employeeId, String department, String designation) {
        super(userId, firstName, lastName, email, UserRole.LECTURER);
        this.employeeId = employeeId;
        this.department = department;
        this.designation = designation;
        this.joiningDate = LocalDate.now();
        this.assignedCourses = new ArrayList<>();
        this.experienceYears = 0;
        initializePermissions();
        logger.log("Lecturer created: " + getFullName() + " (" + employeeId + ")");
    }
    
    private void initializePermissions() {
        lecturerPermissions = new ArrayList<>();
        lecturerPermissions.add("TEACH_COURSES");
        lecturerPermissions.add("GRADE_STUDENTS");
        lecturerPermissions.add("VIEW_STUDENT_LIST");
        lecturerPermissions.add("CREATE_ASSIGNMENTS");
        lecturerPermissions.add("SCHEDULE_CLASSES");
        lecturerPermissions.add("LIBRARY_ACCESS");
        lecturerPermissions.add("GENERATE_REPORTS");
        lecturerPermissions.add("MANAGE_ATTENDANCE");
    }
    
    @Override
    public void displayProfile() {
        System.out.println("=== LECTURER PROFILE ===");
        System.out.println("User ID: " + userId);
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Department: " + department);
        System.out.println("Designation: " + designation);
        System.out.println("Qualification: " + qualification);
        System.out.println("Specialization: " + specialization);
        System.out.println("Experience: " + experienceYears + " years");
        System.out.println("Salary: $" + String.format("%.2f", salary));
        System.out.println("Joining Date: " + joiningDate);
        System.out.println("Assigned Courses: " + assignedCourses.size());
        System.out.println("Created: " + createdAt);
        System.out.println("Last Login: " + lastLoginAt);
    }
    
    @Override
    public boolean hasPermission(String permission) {
        return lecturerPermissions.contains(permission.toUpperCase());
    }
    
    @Override
    public void performRoleSpecificAction() {
        System.out.println("Lecturer " + getFullName() + " is accessing teaching portal...");
        viewAssignedCourses();
        manageClasses();
        gradeAssignments();
    }
    
    public void assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
            course.setInstructor(this);
            System.out.println("Successfully assigned to course: " + course.getCourseName());
            logger.log("Lecturer " + employeeId + " assigned to course: " + course.getCourseId());
        } else {
            System.out.println("Already assigned to this course.");
        }
    }
    
    public void removeCourseAssignment(Course course) {
        if (assignedCourses.remove(course)) {
            course.setInstructor(null);
            System.out.println("Successfully removed from course: " + course.getCourseName());
            logger.log("Lecturer " + employeeId + " removed from course: " + course.getCourseId());
        } else {
            System.out.println("Not assigned to this course.");
        }
    }
    
    public void viewAssignedCourses() {
        System.out.println("=== ASSIGNED COURSES ===");
        if (assignedCourses.isEmpty()) {
            System.out.println("No courses assigned.");
        } else {
            for (Course course : assignedCourses) {
                System.out.println("- " + course.getCourseName() + " (" + course.getCourseId() + ")");
                System.out.println("  Students: " + course.getEnrolledStudents().size());
            }
        }
    }
    
    public void manageClasses() {
        System.out.println("=== CLASS MANAGEMENT ===");
        System.out.println("Managing classes for " + assignedCourses.size() + " courses");
        // Class management implementation would be here
    }
    
    public void gradeAssignments() {
        System.out.println("=== GRADING ASSIGNMENTS ===");
        System.out.println("Processing assignments and grades...");
        // Grading implementation would be here
    }
    
    public void takeAttendance(Course course, List<Student> presentStudents) {
        System.out.println("Taking attendance for: " + course.getCourseName());
        System.out.println("Present students: " + presentStudents.size());
        logger.log("Lecturer " + employeeId + " took attendance for course: " + course.getCourseId());
    }
    
    public void createAssignment(Course course, String assignmentTitle, String description) {
        System.out.println("Creating assignment: " + assignmentTitle + " for " + course.getCourseName());
        logger.log("Lecturer " + employeeId + " created assignment: " + assignmentTitle);
    }
    
    public void scheduleClass(Course course, String dateTime, String venue) {
        System.out.println("Scheduling class for " + course.getCourseName());
        System.out.println("Date/Time: " + dateTime + ", Venue: " + venue);
        logger.log("Lecturer " + employeeId + " scheduled class for course: " + course.getCourseId());
    }
    
    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    
    public List<Course> getAssignedCourses() { return new ArrayList<>(assignedCourses); }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public List<String> getLecturerPermissions() { return new ArrayList<>(lecturerPermissions); }
}