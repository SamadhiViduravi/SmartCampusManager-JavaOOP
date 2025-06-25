package com.campus.courses;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Manager class for handling all course operations
 */
public class CourseManager implements Manageable<Course> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    private Map<String, Course> courses;
    private NotificationService notificationService;
    private int courseCounter;

    public CourseManager() {
        this.courses = new HashMap<>();
        this.notificationService = NotificationService.getInstance();
        this.courseCounter = 1;
        initializeSampleData();
        logger.log("CourseManager initialized");
    }

    private void initializeSampleData() {
        // Create sample courses
        Course course1 = new Course("CS101", "Introduction to Computer Science", "Basic programming concepts");
        course1.setCredits(3);
        course1.setDepartment("Computer Science");
        course1.setInstructor("Dr. Smith");
        course1.setMaxStudents(30);
        course1.setStatus(CourseStatus.ACTIVE);
        courses.put("CS101", course1);

        Course course2 = new Course("MATH201", "Calculus II", "Advanced calculus concepts");
        course2.setCredits(4);
        course2.setDepartment("Mathematics");
        course2.setInstructor("Prof. Johnson");
        course2.setMaxStudents(25);
        course2.setStatus(CourseStatus.ACTIVE);
        courses.put("MATH201", course2);

        Course course3 = new Course("ENG101", "English Composition", "Writing and communication skills");
        course3.setCredits(3);
        course3.setDepartment("English");
        course3.setInstructor("Dr. Williams");
        course3.setMaxStudents(20);
        course3.setStatus(CourseStatus.ACTIVE);
        courses.put("ENG101", course3);

        Course course4 = new Course("PHYS101", "General Physics", "Introduction to physics principles");
        course4.setCredits(4);
        course4.setDepartment("Physics");
        course4.setInstructor("Prof. Brown");
        course4.setMaxStudents(35);
        course4.setStatus(CourseStatus.ACTIVE);
        courses.put("PHYS101", course4);

        Course course5 = new Course("BUS201", "Business Management", "Fundamentals of business management");
        course5.setCredits(3);
        course5.setDepartment("Business");
        course5.setInstructor("Dr. Davis");
        course5.setMaxStudents(40);
        course5.setStatus(CourseStatus.ACTIVE);
        courses.put("BUS201", course5);

        logger.log("Sample course data initialized");
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== COURSE MANAGEMENT MENU ===");
            System.out.println("1. Add New Course");
            System.out.println("2. View All Courses");
            System.out.println("3. Search Courses");
            System.out.println("4. Update Course");
            System.out.println("5. Delete Course");
            System.out.println("6. Course Enrollment");
            System.out.println("7. Course Reports");
            System.out.println("8. Course Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getChoice();

            switch (choice) {
                case 1: addNewCourseInteractive(); break;
                case 2: viewAllCourses(); break;
                case 3: searchCoursesMenu(); break;
                case 4: updateCourseInteractive(); break;
                case 5: deleteCourseInteractive(); break;
                case 6: courseEnrollmentMenu(); break;
                case 7: courseReportsMenu(); break;
                case 8: displayCourseStatistics(); break;
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
        courses.put(course.getCourseId(), course);
        notificationService.notifyObservers("New course created: " + course.getCourseName());
        logger.log("Course created: " + course.getCourseId());
    }

    @Override
    public Course read(String courseId) {
        return courses.get(courseId);
    }

    @Override
    public void update(Course course) {
        courses.put(course.getCourseId(), course);
        notificationService.notifyObservers("Course updated: " + course.getCourseName());
        logger.log("Course updated: " + course.getCourseId());
    }

    @Override
    public void delete(String courseId) {
        Course course = courses.remove(courseId);
        if (course != null) {
            notificationService.notifyObservers("Course deleted: " + course.getCourseName());
            logger.log("Course deleted: " + courseId);
        }
    }

    @Override
    public List<Course> getAll() {
        return new ArrayList<>(courses.values());
    }

    // Utility methods for external access
    public int getTotalCourses() {
        return courses.size();
    }

    public int getActiveCourses() {
        return (int) courses.values().stream()
                .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .count();
    }

    public int getInactiveCourses() {
        return (int) courses.values().stream()
                .filter(course -> course.getStatus() == CourseStatus.INACTIVE)
                .count();
    }

    public List<Course> getCoursesByDepartment(String department) {
        return courses.values().stream()
                .filter(course -> department.equals(course.getDepartment()))
                .collect(Collectors.toList());
    }

    public List<Course> getCoursesByInstructor(String instructor) {
        return courses.values().stream()
                .filter(course -> instructor.equals(course.getInstructor()))
                .collect(Collectors.toList());
    }

    private void addNewCourseInteractive() {
        System.out.println("\n=== ADD NEW COURSE ===");

        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        if (courses.containsKey(courseId)) {
            System.out.println("Course ID already exists.");
            return;
        }

        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine();

        System.out.print("Enter Course Description: ");
        String description = scanner.nextLine();

        Course newCourse = new Course(courseId, courseName, description);

        System.out.print("Enter Credits: ");
        try {
            int credits = Integer.parseInt(scanner.nextLine());
            newCourse.setCredits(credits);
        } catch (NumberFormatException e) {
            System.out.println("Invalid credits, using default (3)");
            newCourse.setCredits(3);
        }

        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        if (!department.trim().isEmpty()) {
            newCourse.setDepartment(department);
        }

        System.out.print("Enter Instructor: ");
        String instructor = scanner.nextLine();
        if (!instructor.trim().isEmpty()) {
            newCourse.setInstructor(instructor);
        }

        System.out.print("Enter Maximum Students: ");
        try {
            int maxStudents = Integer.parseInt(scanner.nextLine());
            newCourse.setMaxStudents(maxStudents);
        } catch (NumberFormatException e) {
            System.out.println("Invalid max students, using default (30)");
            newCourse.setMaxStudents(30);
        }

        create(newCourse);
        System.out.println("Course added successfully!");
        newCourse.displayCourseInfo();
    }

    private void viewAllCourses() {
        System.out.println("\n=== ALL COURSES ===");
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.printf("%-8s %-25s %-15s %-15s %-8s %-8s %-12s%n",
                "ID", "Course Name", "Department", "Instructor", "Credits", "Max", "Status");
        System.out.println("-".repeat(95));

        courses.values().stream()
                .sorted((c1, c2) -> c1.getCourseId().compareTo(c2.getCourseId()))
                .forEach(course -> {
                    System.out.printf("%-8s %-25s %-15s %-15s %-8d %-8d %-12s%n",
                            course.getCourseId(),
                            course.getCourseName().length() > 23 ? course.getCourseName().substring(0, 23) + ".." : course.getCourseName(),
                            course.getDepartment() != null ?
                                    (course.getDepartment().length() > 13 ? course.getDepartment().substring(0, 13) + ".." : course.getDepartment()) : "N/A",
                            course.getInstructor() != null ?
                                    (course.getInstructor().length() > 13 ? course.getInstructor().substring(0, 13) + ".." : course.getInstructor()) : "N/A",
                            course.getCredits(),
                            course.getMaxStudents(),
                            course.getStatus());
                });

        System.out.println("-".repeat(95));
        System.out.println("Total Courses: " + courses.size());
        System.out.println("Active Courses: " + getActiveCourses());
    }

    private void searchCoursesMenu() {
        System.out.println("\n=== SEARCH COURSES ===");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Department");
        System.out.println("3. Search by Instructor");
        System.out.println("4. Search by Credits");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: searchByName(); break;
            case 2: searchByDepartment(); break;
            case 3: searchByInstructor(); break;
            case 4: searchByCredits(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void searchByName() {
        System.out.print("Enter course name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();

        List<Course> results = courses.values().stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        displaySearchResults(results, "name containing '" + searchTerm + "'");
    }

    private void searchByDepartment() {
        System.out.print("Enter department to search: ");
        String department = scanner.nextLine().toLowerCase();

        List<Course> results = courses.values().stream()
                .filter(course -> course.getDepartment() != null &&
                        course.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());

        displaySearchResults(results, "department containing '" + department + "'");
    }

    private void searchByInstructor() {
        System.out.print("Enter instructor to search: ");
        String instructor = scanner.nextLine().toLowerCase();

        List<Course> results = courses.values().stream()
                .filter(course -> course.getInstructor() != null &&
                        course.getInstructor().toLowerCase().contains(instructor))
                .collect(Collectors.toList());

        displaySearchResults(results, "instructor containing '" + instructor + "'");
    }

    private void searchByCredits() {
        System.out.print("Enter credits: ");
        try {
            int credits = Integer.parseInt(scanner.nextLine());

            List<Course> results = courses.values().stream()
                    .filter(course -> course.getCredits() == credits)
                    .collect(Collectors.toList());

            displaySearchResults(results, credits + " credits");
        } catch (NumberFormatException e) {
            System.out.println("Invalid credits format.");
        }
    }

    private void displaySearchResults(List<Course> results, String criteria) {
        System.out.println("\n=== SEARCH RESULTS ===");
        System.out.println("Search criteria: " + criteria);
        System.out.println("Results found: " + results.size());

        if (results.isEmpty()) {
            System.out.println("No courses found matching the criteria.");
            return;
        }

        System.out.printf("%-8s %-25s %-15s %-15s %-8s %-12s%n",
                "ID", "Course Name", "Department", "Instructor", "Credits", "Status");
        System.out.println("-".repeat(85));

        results.forEach(course -> {
            System.out.printf("%-8s %-25s %-15s %-15s %-8d %-12s%n",
                    course.getCourseId(),
                    course.getCourseName().length() > 23 ? course.getCourseName().substring(0, 23) + ".." : course.getCourseName(),
                    course.getDepartment() != null ?
                            (course.getDepartment().length() > 13 ? course.getDepartment().substring(0, 13) + ".." : course.getDepartment()) : "N/A",
                    course.getInstructor() != null ?
                            (course.getInstructor().length() > 13 ? course.getInstructor().substring(0, 13) + ".." : course.getInstructor()) : "N/A",
                    course.getCredits(),
                    course.getStatus());
        });
    }

    private void updateCourseInteractive() {
        System.out.println("\n=== UPDATE COURSE ===");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        System.out.println("Current course information:");
        course.displayCourseInfo();

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Course Name");
        System.out.println("2. Description");
        System.out.println("3. Credits");
        System.out.println("4. Department");
        System.out.println("5. Instructor");
        System.out.println("6. Maximum Students");
        System.out.println("7. Status");
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
                try {
                    course.setCredits(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid credits format.");
                    return;
                }
                break;
            case 4:
                System.out.print("Enter new department: ");
                course.setDepartment(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter new instructor: ");
                course.setInstructor(scanner.nextLine());
                break;
            case 6:
                System.out.print("Enter new maximum students: ");
                try {
                    course.setMaxStudents(Integer.parseInt(scanner.nextLine()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format.");
                    return;
                }
                break;
            case 7:
                System.out.println("Select new status:");
                CourseStatus[] statuses = CourseStatus.values();
                for (int i = 0; i < statuses.length; i++) {
                    System.out.println((i + 1) + ". " + statuses[i]);
                }
                System.out.print("Enter choice: ");
                int statusChoice = getChoice();
                if (statusChoice >= 1 && statusChoice <= statuses.length) {
                    course.setStatus(statuses[statusChoice - 1]);
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        update(course);
        System.out.println("Course updated successfully!");
    }

    private void deleteCourseInteractive() {
        System.out.println("\n=== DELETE COURSE ===");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        System.out.println("Course to be deleted:");
        course.displayCourseInfo();

        System.out.print("Are you sure you want to delete this course? (yes/no): ");
        String confirmation = scanner.nextLine();

        if ("yes".equalsIgnoreCase(confirmation)) {
            delete(courseId);
            System.out.println("Course deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void courseEnrollmentMenu() {
        System.out.println("\n=== COURSE ENROLLMENT ===");
        System.out.println("1. Enroll Student");
        System.out.println("2. Unenroll Student");
        System.out.println("3. View Course Enrollment");
        System.out.println("4. View Student Courses");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: enrollStudentInteractive(); break;
            case 2: unenrollStudentInteractive(); break;
            case 3: viewCourseEnrollmentInteractive(); break;
            case 4: viewStudentCoursesInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void enrollStudentInteractive() {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        if (course.enrollStudent(studentId)) {
            update(course);
            System.out.println("Student enrolled successfully!");
        } else {
            System.out.println("Enrollment failed. Course may be full or student already enrolled.");
        }
    }

    private void unenrollStudentInteractive() {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        if (course.unenrollStudent(studentId)) {
            update(course);
            System.out.println("Student unenrolled successfully!");
        } else {
            System.out.println("Unenrollment failed. Student may not be enrolled in this course.");
        }
    }

    private void viewCourseEnrollmentInteractive() {
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();

        Course course = read(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        System.out.println("\n=== COURSE ENROLLMENT ===");
        System.out.println("Course: " + course.getCourseName() + " (" + courseId + ")");
        System.out.println("Enrolled Students: " + course.getEnrolledStudents().size() + "/" + course.getMaxStudents());

        if (!course.getEnrolledStudents().isEmpty()) {
            System.out.println("\nEnrolled Students:");
            course.getEnrolledStudents().forEach(studentId ->
                    System.out.println("- " + studentId));
        }
    }

    private void viewStudentCoursesInteractive() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();

        List<Course> studentCourses = courses.values().stream()
                .filter(course -> course.isStudentEnrolled(studentId))
                .collect(Collectors.toList());

        System.out.println("\n=== STUDENT COURSES ===");
        System.out.println("Student ID: " + studentId);
        System.out.println("Enrolled Courses: " + studentCourses.size());

        if (!studentCourses.isEmpty()) {
            System.out.printf("%-8s %-25s %-8s %-15s%n", "ID", "Course Name", "Credits", "Instructor");
            System.out.println("-".repeat(60));

            studentCourses.forEach(course -> {
                System.out.printf("%-8s %-25s %-8d %-15s%n",
                        course.getCourseId(),
                        course.getCourseName().length() > 23 ? course.getCourseName().substring(0, 23) + ".." : course.getCourseName(),
                        course.getCredits(),
                        course.getInstructor() != null ?
                                (course.getInstructor().length() > 13 ? course.getInstructor().substring(0, 13) + ".." : course.getInstructor()) : "N/A");
            });

            int totalCredits = studentCourses.stream().mapToInt(Course::getCredits).sum();
            System.out.println("-".repeat(60));
            System.out.println("Total Credits: " + totalCredits);
        }
    }

    private void courseReportsMenu() {
        System.out.println("\n=== COURSE REPORTS ===");
        System.out.println("1. Department-wise Report");
        System.out.println("2. Instructor-wise Report");
        System.out.println("3. Enrollment Report");
        System.out.println("4. Credits Distribution Report");
        System.out.print("Enter choice: ");

        int choice = getChoice();

        switch (choice) {
            case 1: generateDepartmentWiseReport(); break;
            case 2: generateInstructorWiseReport(); break;
            case 3: generateEnrollmentReport(); break;
            case 4: generateCreditsDistributionReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void generateDepartmentWiseReport() {
        System.out.println("\n=== DEPARTMENT-WISE REPORT ===");

        Map<String, List<Course>> coursesByDept = courses.values().stream()
                .filter(course -> course.getDepartment() != null)
                .collect(Collectors.groupingBy(Course::getDepartment));

        System.out.printf("%-20s %-10s %-15s%n", "Department", "Courses", "Total Credits");
        System.out.println("-".repeat(50));

        coursesByDept.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String dept = entry.getKey();
                    List<Course> deptCourses = entry.getValue();
                    int totalCredits = deptCourses.stream().mapToInt(Course::getCredits).sum();

                    System.out.printf("%-20s %-10d %-15d%n",
                            dept.length() > 18 ? dept.substring(0, 18) + ".." : dept,
                            deptCourses.size(),
                            totalCredits);
                });
    }

    private void generateInstructorWiseReport() {
        System.out.println("\n=== INSTRUCTOR-WISE REPORT ===");

        Map<String, List<Course>> coursesByInstructor = courses.values().stream()
                .filter(course -> course.getInstructor() != null)
                .collect(Collectors.groupingBy(Course::getInstructor));

        System.out.printf("%-20s %-10s %-15s%n", "Instructor", "Courses", "Total Students");
        System.out.println("-".repeat(50));

        coursesByInstructor.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String instructor = entry.getKey();
                    List<Course> instructorCourses = entry.getValue();
                    int totalStudents = instructorCourses.stream()
                            .mapToInt(course -> course.getEnrolledStudents().size())
                            .sum();

                    System.out.printf("%-20s %-10d %-15d%n",
                            instructor.length() > 18 ? instructor.substring(0, 18) + ".." : instructor,
                            instructorCourses.size(),
                            totalStudents);
                });
    }

    private void generateEnrollmentReport() {
        System.out.println("\n=== ENROLLMENT REPORT ===");

        System.out.printf("%-8s %-25s %-10s %-10s %-12s%n",
                "ID", "Course Name", "Enrolled", "Capacity", "Fill Rate");
        System.out.println("-".repeat(70));

        courses.values().stream()
                .sorted((c1, c2) -> c1.getCourseId().compareTo(c2.getCourseId()))
                .forEach(course -> {
                    int enrolled = course.getEnrolledStudents().size();
                    int capacity = course.getMaxStudents();
                    double fillRate = capacity > 0 ? (double) enrolled / capacity * 100 : 0;

                    System.out.printf("%-8s %-25s %-10d %-10d %-12.1f%%n",
                            course.getCourseId(),
                            course.getCourseName().length() > 23 ? course.getCourseName().substring(0, 23) + ".." : course.getCourseName(),
                            enrolled,
                            capacity,
                            fillRate);
                });

        // Summary
        int totalEnrolled = courses.values().stream()
                .mapToInt(course -> course.getEnrolledStudents().size())
                .sum();
        int totalCapacity = courses.values().stream()
                .mapToInt(Course::getMaxStudents)
                .sum();
        double overallFillRate = totalCapacity > 0 ? (double) totalEnrolled / totalCapacity * 100 : 0;

        System.out.println("-".repeat(70));
        System.out.println("Total Enrolled: " + totalEnrolled);
        System.out.println("Total Capacity: " + totalCapacity);
        System.out.println("Overall Fill Rate: " + String.format("%.1f", overallFillRate) + "%");
    }

    private void generateCreditsDistributionReport() {
        System.out.println("\n=== CREDITS DISTRIBUTION REPORT ===");

        Map<Integer, Long> creditsDistribution = courses.values().stream()
                .collect(Collectors.groupingBy(Course::getCredits, Collectors.counting()));

        System.out.printf("%-10s %-10s%n", "Credits", "Courses");
        System.out.println("-".repeat(25));

        creditsDistribution.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("%-10d %-10d%n", entry.getKey(), entry.getValue());
                });

        double avgCredits = courses.values().stream()
                .mapToInt(Course::getCredits)
                .average()
                .orElse(0.0);

        System.out.println("-".repeat(25));
        System.out.println("Average Credits per Course: " + String.format("%.1f", avgCredits));
    }

    private void displayCourseStatistics() {
        System.out.println("\n=== COURSE STATISTICS ===");

        int totalCourses = courses.size();
        if (totalCourses == 0) {
            System.out.println("No courses in the system.");
            return;
        }

        System.out.println("OVERVIEW:");
        System.out.println("- Total Courses: " + totalCourses);
        System.out.println("- Active Courses: " + getActiveCourses());
        System.out.println("- Inactive Courses: " + getInactiveCourses());

        // Department distribution
        Map<String, Long> deptDistribution = courses.values().stream()
                .filter(course -> course.getDepartment() != null)
                .collect(Collectors.groupingBy(Course::getDepartment, Collectors.counting()));

        System.out.println("\nDEPARTMENT DISTRIBUTION:");
        deptDistribution.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));

        // Credits statistics
        double avgCredits = courses.values().stream().mapToInt(Course::getCredits).average().orElse(0.0);
        int totalCredits = courses.values().stream().mapToInt(Course::getCredits).sum();

        System.out.println("\nCREDITS STATISTICS:");
        System.out.println("- Average Credits per Course: " + String.format("%.1f", avgCredits));
        System.out.println("- Total Credits Available: " + totalCredits);

        // Enrollment statistics
        int totalEnrolled = courses.values().stream()
                .mapToInt(course -> course.getEnrolledStudents().size())
                .sum();
        int totalCapacity = courses.values().stream()
                .mapToInt(Course::getMaxStudents)
                .sum();

        System.out.println("\nENROLLMENT STATISTICS:");
        System.out.println("- Total Students Enrolled: " + totalEnrolled);
        System.out.println("- Total Capacity: " + totalCapacity);
        System.out.println("- Overall Fill Rate: " +
                String.format("%.1f", totalCapacity > 0 ? (double) totalEnrolled / totalCapacity * 100 : 0) + "%");

        logger.log("Course statistics displayed");
    }
}