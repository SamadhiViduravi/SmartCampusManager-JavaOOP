package com.campus.students;

import com.campus.utils.Identifiable;
import com.campus.utils.ValidationUtils;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Student class representing a student in the campus management system
 */
public class Student implements Identifiable {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private String department;
    private int year;
    private double gpa;
    private LocalDate enrollmentDate;
    private StudentStatus status;
    private String guardianName;
    private String guardianPhone;
    private String emergencyContact;
    private List<String> enrolledCourses;
    private String nationality;
    private String bloodGroup;
    private String profilePicture;
    
    // Constructors
    public Student() {
        this.enrolledCourses = new ArrayList<>();
        this.status = StudentStatus.ACTIVE;
        this.enrollmentDate = LocalDate.now();
        this.year = 1;
        this.gpa = 0.0;
    }
    
    public Student(String studentId, String firstName, String lastName, String email) {
        this();
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public Student(String studentId, String firstName, String lastName, String email, 
                   String department, int year) {
        this(studentId, firstName, lastName, email);
        this.department = department;
        this.year = year;
    }
    
    // Getters and Setters
    @Override
    public String getId() {
        return studentId;
    }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFullName() { 
        return firstName + " " + lastName; 
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        if (ValidationUtils.isValidEmail(email)) {
            this.email = email; 
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public int getYear() { return year; }
    public void setYear(int year) { 
        if (year >= 1 && year <= 4) {
            this.year = year; 
        } else {
            throw new IllegalArgumentException("Year must be between 1 and 4");
        }
    }
    
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { 
        if (ValidationUtils.isValidGPA(gpa)) {
            this.gpa = gpa; 
        } else {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
    }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public StudentStatus getStatus() { return status; }
    public void setStatus(StudentStatus status) { this.status = status; }
    
    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }
    
    public String getGuardianPhone() { return guardianPhone; }
    public void setGuardianPhone(String guardianPhone) { this.guardianPhone = guardianPhone; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public List<String> getEnrolledCourses() { return new ArrayList<>(enrolledCourses); }
    public void setEnrolledCourses(List<String> enrolledCourses) { 
        this.enrolledCourses = new ArrayList<>(enrolledCourses); 
    }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    
    // Utility methods
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    
    public boolean isActive() {
        return status == StudentStatus.ACTIVE;
    }
    
    public void enrollInCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }
    
    public void unenrollFromCourse(String courseId) {
        enrolledCourses.remove(courseId);
    }
    
    public boolean isEnrolledInCourse(String courseId) {
        return enrolledCourses.contains(courseId);
    }
    
    public int getTotalEnrolledCourses() {
        return enrolledCourses.size();
    }
    
    public String getAcademicLevel() {
        switch (year) {
            case 1: return "Freshman";
            case 2: return "Sophomore";
            case 3: return "Junior";
            case 4: return "Senior";
            default: return "Unknown";
        }
    }
    
    public String getGpaGrade() {
        if (gpa >= 3.7) return "A";
        else if (gpa >= 3.3) return "A-";
        else if (gpa >= 3.0) return "B+";
        else if (gpa >= 2.7) return "B";
        else if (gpa >= 2.3) return "B-";
        else if (gpa >= 2.0) return "C+";
        else if (gpa >= 1.7) return "C";
        else if (gpa >= 1.3) return "C-";
        else if (gpa >= 1.0) return "D";
        else return "F";
    }
    
    public String getAcademicStanding() {
        if (gpa >= 3.5) return "Dean's List";
        else if (gpa >= 3.0) return "Good Standing";
        else if (gpa >= 2.0) return "Satisfactory";
        else return "Academic Probation";
    }
    
    public boolean isEligibleForGraduation() {
        return year == 4 && gpa >= 2.0 && enrolledCourses.size() >= 8; // Minimum requirements
    }
    
    public void updateGPA(double newGpa) {
        if (ValidationUtils.isValidGPA(newGpa)) {
            this.gpa = newGpa;
        } else {
            throw new IllegalArgumentException("Invalid GPA value");
        }
    }
    
    public void promoteToNextYear() {
        if (year < 4 && gpa >= 2.0) {
            year++;
        } else if (year >= 4) {
            throw new IllegalStateException("Student is already in final year");
        } else {
            throw new IllegalStateException("Student does not meet promotion requirements (GPA < 2.0)");
        }
    }
    
    public void suspend() {
        this.status = StudentStatus.SUSPENDED;
    }
    
    public void reactivate() {
        this.status = StudentStatus.ACTIVE;
    }
    
    public void graduate() {
        if (isEligibleForGraduation()) {
            this.status = StudentStatus.GRADUATED;
        } else {
            throw new IllegalStateException("Student is not eligible for graduation");
        }
    }
    
    public void withdraw() {
        this.status = StudentStatus.WITHDRAWN;
    }
    
    // Display methods
    public void displayStudentInfo() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      STUDENT INFORMATION                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + getFullName());
        System.out.println("Email: " + email);
        System.out.println("Phone: " + (phone != null ? phone : "Not provided"));
        System.out.println("Date of Birth: " + (dateOfBirth != null ? dateOfBirth : "Not provided"));
        System.out.println("Age: " + (dateOfBirth != null ? getAge() + " years" : "Unknown"));
        System.out.println("Address: " + (address != null ? address : "Not provided"));
        System.out.println("Department: " + (department != null ? department : "Not assigned"));
        System.out.println("Academic Year: " + year + " (" + getAcademicLevel() + ")");
        System.out.println("GPA: " + String.format("%.2f", gpa) + " (" + getGpaGrade() + ")");
        System.out.println("Academic Standing: " + getAcademicStanding());
        System.out.println("Status: " + status);
        System.out.println("Enrollment Date: " + enrollmentDate);
        System.out.println("Enrolled Courses: " + enrolledCourses.size());
        
        if (guardianName != null) {
            System.out.println("Guardian: " + guardianName + 
                             (guardianPhone != null ? " (" + guardianPhone + ")" : ""));
        }
        
        if (emergencyContact != null) {
            System.out.println("Emergency Contact: " + emergencyContact);
        }
        
        if (nationality != null) {
            System.out.println("Nationality: " + nationality);
        }
        
        if (bloodGroup != null) {
            System.out.println("Blood Group: " + bloodGroup);
        }
        
        System.out.println("Eligible for Graduation: " + (isEligibleForGraduation() ? "Yes" : "No"));
        System.out.println("══════════════════════════════════════════════════════════════");
    }
    
    public void displayAcademicSummary() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ACADEMIC SUMMARY                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("Student: " + getFullName() + " (" + studentId + ")");
        System.out.println("Department: " + (department != null ? department : "Not assigned"));
        System.out.println("Academic Year: " + getAcademicLevel() + " (Year " + year + ")");
        System.out.println("Current GPA: " + String.format("%.2f", gpa) + " (" + getGpaGrade() + ")");
        System.out.println("Academic Standing: " + getAcademicStanding());
        System.out.println("Enrolled Courses: " + enrolledCourses.size());
        System.out.println("Status: " + status);
        
        if (!enrolledCourses.isEmpty()) {
            System.out.println("\nCurrent Courses:");
            for (int i = 0; i < enrolledCourses.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + enrolledCourses.get(i));
            }
        }
        
        System.out.println("\nGraduation Eligibility: " + (isEligibleForGraduation() ? "✓ Eligible" : "✗ Not Eligible"));
        if (!isEligibleForGraduation()) {
            List<String> requirements = new ArrayList<>();
            if (year < 4) requirements.add("Complete Year 4");
            if (gpa < 2.0) requirements.add("Maintain GPA ≥ 2.0");
            if (enrolledCourses.size() < 8) requirements.add("Complete minimum course requirements");
            
            System.out.println("Requirements needed:");
            requirements.forEach(req -> System.out.println("  • " + req));
        }
        System.out.println("══════════════════════════════════════════════════════════════");
    }
    
    public void displayContactInfo() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    CONTACT INFORMATION                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("Student: " + getFullName() + " (" + studentId + ")");
        System.out.println("Email: " + email);
        System.out.println("Phone: " + (phone != null ? phone : "Not provided"));
        System.out.println("Address: " + (address != null ? address : "Not provided"));
        
        if (guardianName != null || guardianPhone != null) {
            System.out.println("\nGuardian Information:");
            System.out.println("Name: " + (guardianName != null ? guardianName : "Not provided"));
            System.out.println("Phone: " + (guardianPhone != null ? guardianPhone : "Not provided"));
        }
        
        if (emergencyContact != null) {
            System.out.println("\nEmergency Contact: " + emergencyContact);
        }
        System.out.println("══════════════════════════════════════════════════════════════");
    }
    
    // Override methods
    @Override
    public String toString() {
        return String.format("Student{id='%s', name='%s', email='%s', department='%s', year=%d, gpa=%.2f, status=%s}",
                           studentId, getFullName(), email, department, year, gpa, status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId != null && studentId.equals(student.studentId);
    }
    
    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
    
    // Validation methods
    public boolean isValidForEnrollment() {
        return studentId != null && !studentId.trim().isEmpty() &&
               firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty() &&
               email != null && ValidationUtils.isValidEmail(email) &&
               status == StudentStatus.ACTIVE;
    }
    
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        
        if (studentId == null || studentId.trim().isEmpty()) {
            errors.add("Student ID is required");
        }
        
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("First name is required");
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Last name is required");
        }
        
        if (email == null || !ValidationUtils.isValidEmail(email)) {
            errors.add("Valid email is required");
        }
        
        if (year < 1 || year > 4) {
            errors.add("Year must be between 1 and 4");
        }
        
        if (gpa < 0.0 || gpa > 4.0) {
            errors.add("GPA must be between 0.0 and 4.0");
        }
        
        return errors;
    }
    
    public boolean hasValidationErrors() {
        return !getValidationErrors().isEmpty();
    }
}