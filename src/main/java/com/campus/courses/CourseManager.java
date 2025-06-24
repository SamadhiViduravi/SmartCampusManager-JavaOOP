package com.campus.courses;

import com.campus.users.Student;
import com.campus.users.Lecturer;
import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager class for handling all course operations
 * Demonstrates composition and service layer pattern
 */
public class CourseManager implements Manageable<Course> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    
    private Map<String, Course> courses;
    private Map<String, Subject> subjects;
    private CourseFactory courseFactory;
    private NotificationService notificationService;
    private TimetableGenerator timetableGenerator;
    
    public CourseManager() {
        this.courses = new HashMap<>();
        this.subjects = new HashMap<>();
        this.courseFactory = new CourseFactory();
        this.notificationService = NotificationService.getInstance();
        this.timetableGenerator = new TimetableGenerator();
        initializeSampleCourses();
        logger.log("CourseManager initialized");
    }
    
    private void initializeSampleCourses() {
        // Create sample courses
        Course cs101 = courseFactory.createCourse(CourseType.REGULAR, "CS101", 
                "Introduction to Programming", "CS-101", "Computer Science", 3);
        courses.put(cs101.getCourseId(), cs101);
        
        Course cs201 = courseFactory.createCourse(CourseType.LAB, "CS201", 
                "Data Structures Lab", "CS-201L", "Computer Science", 2);
        courses.put(cs201.getCourseId(), cs201);
        
        Course math101 = courseFactory.createCourse(CourseType.REGULAR, "MATH101", 
                "Calculus I", "MATH-101", "Mathematics", 4);
        courses.put(math101.getCourseId(), math101);
        
        // Create sample subjects
        Subject programming = new Subject("SUB001", "Programming Fundamentals", "PROG-101", 3);
        subjects.put(programming.getSubjectId(), programming);
        cs101.addSubject(programming);
        
        logger.log("Sample courses initialized");
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== COURSE MANAGEMENT MENU ===");
            System.out.println("1. Create Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Search Course");
            System.out.println("4. Update Course");
            System.out.println("5. Delete Course");
            System.out.println("6. Enroll Student in Course");
            System.out.println("7. Remove Student from Course");
            System.out.println("8. Assign Lecturer to Course");
            System.out.println("9. Manage Subjects");
            System.out.println("10. Generate Timetable");
            System.out.println("11. Course Reports");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getChoice();
            
            switch (choice) {
                case 1: createCourseInteractive(); break;
                case 2: viewAllCourses(); break;
                case 3: searchCourseInteractive(); break;
                case 4: updateCourseInteractive(); break;
                case 5: deleteCourseInteractive(); break;
                case 6: enrollStudentInteractive(); break;
                case 7: removeStudentInteractive(); break;
                case 8: assignLecturerInteractive(); break;
                case 9: manageSubjects(); break;
                case 10: generateTimetableInteractive(); break;
                case 11: generateCourseReports(); break;
                case 0: return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    @Override
    public void create(Course course) {
        if (courses.containsKey(course.getCourseId())) {
            throw new IllegalArgumentException("Course with ID " + course.getCourseId() + " already exists");
        }
        courses.put(course.getCourseId(), course);
        notificationService.notifyObservers("Course created: " + course.getCourseName());
        logger.log("Course created: " + course.getCourseId());
    }
    
    @Override
    public Course read(String courseId) {
        return courses.get(courseId);
    }
    
    @Override
    public void update(Course course) {
        if (!courses.containsKey(course.getCourseId())) {
            throw new IllegalArgumentException("Course with ID " + course.getCourseId() + " does not exist");
        }
        courses.put(course.getCourseId(), course);
        notificationService.notifyObservers("Course updated: " + course.getCourseName());
        logger.log("Course updated: " + course.getCourseId());
    }
    
    @Override
    public void delete(String courseId) {
        Course removedCourse = courses.remove(courseId);
        if (removedCourse != null) {
            notificationService.notifyObservers("Course deleted: " + removedCourse.getCourseName());
            logger.log("Course deleted: " + courseId);
        } else {
            throw new IllegalArgumentException("Course with ID " + courseId + " does not exist");
        }
    }
    
    @Override
    public List<Course> getAll() {
        return new ArrayList<>(courses.values());
    }
    
    public void createCourseInteractive() {
        System.out.println("\n=== CREATE NEW COURSE ===");
        
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine();
        
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        
        System.out.print("Enter Credits: ");
        int credits = Integer.parseInt(scanner.nextLine());
        
        System.out.println("Select Course Type:");
        CourseType[] types = CourseType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i].getDisplayName());
        }
        System.out.print("Enter choice: ");
        int typeChoice = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (typeChoice >= 0 && typeChoice < types.length) {
            try {
                Course newCourse = courseFactory.createCourse(types[typeChoice], courseId, 
                                                            courseName, courseCode, department, credits);
                create(newCourse);
                System.out.println("Course created successfully!");
                newCourse.displayCourseInfo();
            } catch (Exception e) {
                System.out.println("Error creating course: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid course type selection.");
        }
    }
    
    public void viewAllCourses() {
        System.out.println("\n=== ALL COURSES ===");
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        System.out.printf("%-10s %-30s %-15s %-20s %-8s %-15s %-10s%n", 
                         "Course ID", "Course Name", "Course Code", "Department", "Credits", "Type", "Students");
        System.out.println("-".repeat(110));
        
        for (Course course : courses.values()) {
            System.out.printf("%-10s %-30s %-15s %-20s %-8d %-15s %-10d%n",
                             course.getCourseId(),
                             course.getCourseName(),
                             course.getCourseCode(),
                             course.getDepartment(),
                             course.getCredits(),
                             course.getCourseType(),
                             course.getEnrolledStudents().size());
        }
    }
    
    public void searchCourseInteractive() {
        System.out.println("\n=== SEARCH COURSE ===");
        System.out.print("Enter search term (ID, name, code, or department): ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        List<Course> results = searchCourses(searchTerm);
        
        if (results.isEmpty()) {
            System.out.println("No courses found matching: " + searchTerm);
        } else {
            System.out.println("Found " + results.size() + " course(s):");
            for (Course course : results) {
                System.out.println("- " + course.getCourseId() + ": " + course.getCourseName() + 
                                 " (" + course.getCourseCode() + ")");
            }
        }
    }
    
    public List<Course> searchCourses(String searchTerm) {
        return courses.values().stream()
                .filter(course -> 
                    course.getCourseId().toLowerCase().contains(searchTerm) ||
                    course.getCourseName().toLowerCase().contains(searchTerm) ||
                    course.getCourseCode().toLowerCase().contains(searchTerm) ||
                    course.getDepartment().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    public void updateCourseInteractive() {
        System.out.println("\n=== UPDATE COURSE ===");
        System.out.print("Enter Course ID to update: ");
        String courseId = scanner.nextLine();
        
        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        System.out.println("Current course details:");
        course.displayCourseInfo();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Course Name");
        System.out.println("2. Description");
        System.out.println("3. Credits");
        System.out.println("4. Max Capacity");
        System.out.println("5. Course Fee");
        System.out.println("6. Status");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new course name: ");
                course.setCourseName(scanner.nextLine());
                break;
            case 2:
                System.out.print("Enter new description: ");
                course.setDescription(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new credits: ");
                course.setCredits(Integer.parseInt(scanner.nextLine()));
                break;
            case 4:
                System.out.print("Enter new max capacity: ");
                course.setMaxCapacity(Integer.parseInt(scanner.nextLine()));
                break;
            case 5:
                System.out.print("Enter new course fee: ");
                course.setCourseFee(Double.parseDouble(scanner.nextLine()));
                break;
            case 6:
                System.out.println("Select new status:");
                CourseStatus[] statuses = CourseStatus.values();
                for (int i = 0; i < statuses.length; i++) {
                    System.out.println((i + 1) + ". " + statuses[i].getDisplayName());
                }
                System.out.print("Enter choice: ");
                int statusChoice = Integer.parseInt(scanner.nextLine()) - 1;
                if (statusChoice >= 0 && statusChoice < statuses.length) {
                    course.setStatus(statuses[statusChoice]);
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        update(course);
        System.out.println("Course updated successfully!");
    }
    
    public void deleteCourseInteractive() {
        System.out.println("\n=== DELETE COURSE ===");
        System.out.print("Enter Course ID to delete: ");
        String courseId = scanner.nextLine();
        
        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        System.out.println("Course to be deleted:");
        course.displayCourseInfo();
        
        if (!course.getEnrolledStudents().isEmpty()) {
            System.out.println("Warning: This course has " + course.getEnrolledStudents().size() + " enrolled students.");
        }
        
        System.out.print("Are you sure you want to delete this course? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if ("yes".equalsIgnoreCase(confirmation)) {
            delete(courseId);
            System.out.println("Course deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    public void enrollStudentInteractive() {
        System.out.println("\n=== ENROLL STUDENT IN COURSE ===");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        // In a real implementation, you would get the student from UserManager
        System.out.println("Student enrollment functionality would be implemented here.");
        System.out.println("Course: " + course.getCourseName());
        System.out.println("Available slots: " + course.getAvailableSlots());
        
        logger.log("Student enrollment attempted for course: " + courseId);
    }
    
    public void removeStudentInteractive() {
        System.out.println("\n=== REMOVE STUDENT FROM COURSE ===");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        System.out.println("Student removal functionality would be implemented here.");
        System.out.println("Course: " + course.getCourseName());
        System.out.println("Current enrolled students: " + course.getEnrolledStudents().size());
        
        logger.log("Student removal attempted for course: " + courseId);
    }
    
    public void assignLecturerInteractive() {
        System.out.println("\n=== ASSIGN LECTURER TO COURSE ===");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        System.out.print("Enter Lecturer ID: ");
        String lecturerId = scanner.nextLine();
        
        System.out.println("Lecturer assignment functionality would be implemented here.");
        System.out.println("Course: " + course.getCourseName());
        System.out.println("Current instructor: " + 
                          (course.getInstructor() != null ? course.getInstructor().getFullName() : "Not Assigned"));
        
        logger.log("Lecturer assignment attempted for course: " + courseId);
    }
    
    public void manageSubjects() {
        System.out.println("\n=== SUBJECT MANAGEMENT ===");
        System.out.println("1. Create Subject");
        System.out.println("2. View All Subjects");
        System.out.println("3. Assign Subject to Course");
        System.out.println("4. Remove Subject from Course");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: createSubjectInteractive(); break;
            case 2: viewAllSubjects(); break;
            case 3: assignSubjectToCourse(); break;
            case 4: removeSubjectFromCourse(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void createSubjectInteractive() {
        System.out.println("\n=== CREATE NEW SUBJECT ===");
        
        System.out.print("Enter Subject ID: ");
        String subjectId = scanner.nextLine();
        
        System.out.print("Enter Subject Name: ");
        String subjectName = scanner.nextLine();
        
        System.out.print("Enter Subject Code: ");
        String subjectCode = scanner.nextLine();
        
        System.out.print("Enter Credits: ");
        int credits = Integer.parseInt(scanner.nextLine());
        
        Subject newSubject = new Subject(subjectId, subjectName, subjectCode, credits);
        subjects.put(subjectId, newSubject);
        
        System.out.println("Subject created successfully!");
        newSubject.displaySubjectInfo();
        
        logger.log("Subject created: " + subjectId);
    }
    
    private void viewAllSubjects() {
        System.out.println("\n=== ALL SUBJECTS ===");
        if (subjects.isEmpty()) {
            System.out.println("No subjects found.");
            return;
        }
        
        System.out.printf("%-10s %-30s %-15s %-8s %-20s%n", 
                         "Subject ID", "Subject Name", "Subject Code", "Credits", "Course");
        System.out.println("-".repeat(85));
        
        for (Subject subject : subjects.values()) {
            System.out.printf("%-10s %-30s %-15s %-8d %-20s%n",
                             subject.getSubjectId(),
                             subject.getSubjectName(),
                             subject.getSubjectCode(),
                             subject.getCredits(),
                             subject.getCourse() != null ? subject.getCourse().getCourseName() : "Not Assigned");
        }
    }
    
    private void assignSubjectToCourse() {
        System.out.println("\n=== ASSIGN SUBJECT TO COURSE ===");
        System.out.print("Enter Subject ID: ");
        String subjectId = scanner.nextLine();
        
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        Subject subject = subjects.get(subjectId);
        Course course = courses.get(courseId);
        
        if (subject == null) {
            System.out.println("Subject not found.");
            return;
        }
        
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        
        course.addSubject(subject);
        System.out.println("Subject assigned to course successfully!");
        
        logger.log("Subject " + subjectId + " assigned to course " + courseId);
    }
    
    private void removeSubjectFromCourse() {
        System.out.println("\n=== REMOVE SUBJECT FROM COURSE ===");
        System.out.print("Enter Subject ID: ");
        String subjectId = scanner.nextLine();
        
        Subject subject = subjects.get(subjectId);
        if (subject == null) {
            System.out.println("Subject not found.");
            return;
        }
        
        if (subject.getCourse() == null) {
            System.out.println("Subject is not assigned to any course.");
            return;
        }
        
        Course course = subject.getCourse();
        course.removeSubject(subject);
        System.out.println("Subject removed from course successfully!");
        
        logger.log("Subject " + subjectId + " removed from course " + course.getCourseId());
    }
    
    public void generateTimetableInteractive() {
        System.out.println("\n=== GENERATE TIMETABLE ===");
        System.out.print("Enter Department (or 'ALL' for all departments): ");
        String department = scanner.nextLine();
        
        System.out.print("Enter Semester: ");
        String semester = scanner.nextLine();
        
        timetableGenerator.generateTimetable(courses.values(), department, semester);
        
        logger.log("Timetable generated for department: " + department + ", semester: " + semester);
    }
    
    public void generateCourseReports() {
        System.out.println("\n=== COURSE REPORTS ===");
        
        Map<String, Long> departmentCount = courses.values().stream()
                .collect(Collectors.groupingBy(Course::getDepartment, Collectors.counting()));
        
        Map<CourseType, Long> typeCount = courses.values().stream()
                .collect(Collectors.groupingBy(Course::getCourseType, Collectors.counting()));
        
        long totalEnrollments = courses.values().stream()
                .mapToLong(course -> course.getEnrolledStudents().size())
                .sum();
        
        System.out.println("Total Courses: " + courses.size());
        System.out.println("Total Subjects: " + subjects.size());
        System.out.println("Total Enrollments: " + totalEnrollments);
        
        System.out.println("\nCourses by Department:");
        for (Map.Entry<String, Long> entry : departmentCount.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\nCourses by Type:");
        for (Map.Entry<CourseType, Long> entry : typeCount.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
        
        logger.log("Course reports generated");
    }
    
    // Utility methods
    public List<Course> getCoursesByDepartment(String department) {
        return courses.values().stream()
                .filter(course -> course.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByType(CourseType courseType) {
        return courses.values().stream()
                .filter(course -> course.getCourseType() == courseType)
                .collect(Collectors.toList());
    }
    
    public List<Course> getActiveCourses() {
        return courses.values().stream()
                .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .collect(Collectors.toList());
    }
    
    public boolean courseExists(String courseId) {
        return courses.containsKey(courseId);
    }
    
    public int getTotalCourseCount() {
        return courses.size();
    }
    
    public Map<String, Course> getAllCoursesMap() {
        return new HashMap<>(courses);
    }
    
    public Map<String, Subject> getAllSubjectsMap() {
        return new HashMap<>(subjects);
    }
}