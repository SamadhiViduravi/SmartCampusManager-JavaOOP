package com.campus.courses;

import com.campus.users.Student;
import com.campus.users.Lecturer;
import com.campus.utils.Identifiable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;

/**
 * Course entity class representing academic courses
 * Demonstrates encapsulation and composition
 */
public class Course implements Identifiable {
    private String courseId;
    private String courseName;
    private String courseCode;
    private String description;
    private int credits;
    private String department;
    private CourseType courseType;
    private CourseStatus status;
    private Lecturer instructor;
    private List<Student> enrolledStudents;
    private List<Subject> subjects;
    private int maxCapacity;
    private String semester;
    private String academicYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> prerequisites;
    private double courseFee;
    
    public Course(String courseId, String courseName, String courseCode, String department, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.department = department;
        this.credits = credits;
        this.courseType = CourseType.REGULAR;
        this.status = CourseStatus.ACTIVE;
        this.enrolledStudents = new ArrayList<>();
        this.subjects = new ArrayList<>();
        this.prerequisites = new ArrayList<>();
        this.maxCapacity = 50; // Default capacity
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void enrollStudent(Student student) {
        if (enrolledStudents.size() >= maxCapacity) {
            throw new IllegalStateException("Course is at maximum capacity");
        }
        
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            student.enrollInCourse(this);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeStudent(Student student) {
        if (enrolledStudents.remove(student)) {
            student.dropCourse(this);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void addSubject(Subject subject) {
        if (!subjects.contains(subject)) {
            subjects.add(subject);
            subject.setCourse(this);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeSubject(Subject subject) {
        if (subjects.remove(subject)) {
            subject.setCourse(null);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void addPrerequisite(String prerequisiteCourseId) {
        if (!prerequisites.contains(prerequisiteCourseId)) {
            prerequisites.add(prerequisiteCourseId);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removePrerequisite(String prerequisiteCourseId) {
        if (prerequisites.remove(prerequisiteCourseId)) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public boolean hasAvailableSlots() {
        return enrolledStudents.size() < maxCapacity;
    }
    
    public int getAvailableSlots() {
        return maxCapacity - enrolledStudents.size();
    }
    
    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }
    
    public void displayCourseInfo() {
        System.out.println("=== COURSE INFORMATION ===");
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Name: " + courseName);
        System.out.println("Course Code: " + courseCode);
        System.out.println("Department: " + department);
        System.out.println("Credits: " + credits);
        System.out.println("Type: " + courseType);
        System.out.println("Status: " + status);
        System.out.println("Instructor: " + (instructor != null ? instructor.getFullName() : "Not Assigned"));
        System.out.println("Enrolled Students: " + enrolledStudents.size() + "/" + maxCapacity);
        System.out.println("Available Slots: " + getAvailableSlots());
        System.out.println("Subjects: " + subjects.size());
        System.out.println("Prerequisites: " + prerequisites.size());
        System.out.println("Course Fee: $" + String.format("%.2f", courseFee));
        System.out.println("Semester: " + semester);
        System.out.println("Academic Year: " + academicYear);
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
    }
    
    // Getters and Setters
    @Override
    public String getId() { return courseId; }
    
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
    
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { 
        this.courseCode = courseCode;
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
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { 
        this.department = department;
        this.updatedAt = LocalDateTime.now();
    }
    
    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType courseType) { 
        this.courseType = courseType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Lecturer getInstructor() { return instructor; }
    public void setInstructor(Lecturer instructor) { 
        this.instructor = instructor;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<Student> getEnrolledStudents() { return new ArrayList<>(enrolledStudents); }
    
    public List<Subject> getSubjects() { return new ArrayList<>(subjects); }
    
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { 
        this.maxCapacity = maxCapacity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { 
        this.semester = semester;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { 
        this.academicYear = academicYear;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public List<String> getPrerequisites() { return new ArrayList<>(prerequisites); }
    
    public double getCourseFee() { return courseFee; }
    public void setCourseFee(double courseFee) { 
        this.courseFee = courseFee;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", department='" + department + '\'' +
                ", credits=" + credits +
                ", enrolledStudents=" + enrolledStudents.size() +
                ", status=" + status +
                '}';
    }
}