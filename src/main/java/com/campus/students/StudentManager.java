package com.campus.students;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

/**
 * Manager class for handling all student operations
 */
public class StudentManager implements Manageable<Student> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    
    private Map<String, Student> students;
    private NotificationService notificationService;
    private int studentCounter;
    
    public StudentManager() {
        this.students = new HashMap<>();
        this.notificationService = NotificationService.getInstance();
        this.studentCounter = 1;
        initializeSampleData();
        logger.log("StudentManager initialized");
    }
    
    private void initializeSampleData() {
        // Create sample students
        Student student1 = new Student("S001", "John", "Doe", "john.doe@campus.edu");
        student1.setPhone("+1-555-0101");
        student1.setDateOfBirth(LocalDate.of(2000, 5, 15));
        student1.setAddress("123 Campus Street, University City");
        student1.setDepartment("Computer Science");
        student1.setYear(3);
        student1.setGpa(3.7);
        students.put("S001", student1);
        
        Student student2 = new Student("S002", "Jane", "Smith", "jane.smith@campus.edu");
        student2.setPhone("+1-555-0102");
        student2.setDateOfBirth(LocalDate.of(1999, 8, 22));
        student2.setAddress("456 University Ave, College Town");
        student2.setDepartment("Engineering");
        student2.setYear(4);
        student2.setGpa(3.9);
        students.put("S002", student2);
        
        Student student3 = new Student("S003", "Mike", "Johnson", "mike.johnson@campus.edu");
        student3.setPhone("+1-555-0103");
        student3.setDateOfBirth(LocalDate.of(2001, 2, 10));
        student3.setAddress("789 Student Lane, Academic City");
        student3.setDepartment("Business");
        student3.setYear(2);
        student3.setGpa(3.4);
        students.put("S003", student3);
        
        Student student4 = new Student("S004", "Sarah", "Williams", "sarah.williams@campus.edu");
        student4.setPhone("+1-555-0104");
        student4.setDateOfBirth(LocalDate.of(2000, 11, 30));
        student4.setAddress("321 Education Blvd, Learning City");
        student4.setDepartment("Arts & Sciences");
        student4.setYear(3);
        student4.setGpa(3.6);
        students.put("S004", student4);
        
        Student student5 = new Student("S005", "David", "Brown", "david.brown@campus.edu");
        student5.setPhone("+1-555-0105");
        student5.setDateOfBirth(LocalDate.of(1998, 7, 18));
        student5.setAddress("654 Knowledge Street, Study Town");
        student5.setDepartment("Computer Science");
        student5.setYear(4);
        student5.setGpa(3.8);
        students.put("S005", student5);
        
        logger.log("Sample student data initialized");
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== STUDENT MANAGEMENT MENU ===");
            System.out.println("1. Student Registration");
            System.out.println("2. View All Students");
            System.out.println("3. Search Students");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Student Reports");
            System.out.println("7. Academic Records");
            System.out.println("8. Student Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getChoice();
            
            switch (choice) {
                case 1: registerStudentInteractive(); break;
                case 2: viewAllStudents(); break;
                case 3: searchStudentsMenu(); break;
                case 4: updateStudentInteractive(); break;
                case 5: deleteStudentInteractive(); break;
                case 6: studentReportsMenu(); break;
                case 7: academicRecordsMenu(); break;
                case 8: displayStudentStatistics(); break;
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
    public void create(Student student) {
        students.put(student.getStudentId(), student);
        notificationService.notifyObservers("New student registered: " + student.getFullName());
        logger.log("Student created: " + student.getStudentId());
    }
    
    @Override
    public Student read(String studentId) {
        return students.get(studentId);
    }
    
    @Override
    public void update(Student student) {
        students.put(student.getStudentId(), student);
        notificationService.notifyObservers("Student updated: " + student.getFullName());
        logger.log("Student updated: " + student.getStudentId());
    }
    
    @Override
    public void delete(String studentId) {
        Student student = students.remove(studentId);
        if (student != null) {
            notificationService.notifyObservers("Student deleted: " + student.getFullName());
            logger.log("Student deleted: " + studentId);
        }
    }
    
    @Override
    public List<Student> getAll() {
        return new ArrayList<>(students.values());
    }
    
    private void registerStudentInteractive() {
        System.out.println("\n=== STUDENT REGISTRATION ===");
        
        String studentId = "S" + String.format("%03d", studentCounter++);
        
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        if (!ValidationUtils.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }
        
        Student newStudent = new Student(studentId, firstName, lastName, email);
        
        // Optional fields
        System.out.print("Enter Phone (optional): ");
        String phone = scanner.nextLine();
        if (!phone.trim().isEmpty()) {
            newStudent.setPhone(phone);
        }
        
        System.out.print("Enter Date of Birth (YYYY-MM-DD, optional): ");
        String dobStr = scanner.nextLine();
        if (!dobStr.trim().isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(dobStr);
                newStudent.setDateOfBirth(dob);
            } catch (Exception e) {
                System.out.println("Invalid date format, skipping...");
            }
        }
        
        System.out.print("Enter Address (optional): ");
        String address = scanner.nextLine();
        if (!address.trim().isEmpty()) {
            newStudent.setAddress(address);
        }
        
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        if (!department.trim().isEmpty()) {
            newStudent.setDepartment(department);
        }
        
        System.out.print("Enter Year (1-4): ");
        try {
            int year = Integer.parseInt(scanner.nextLine());
            if (year >= 1 && year <= 4) {
                newStudent.setYear(year);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid year, using default...");
        }
        
        create(newStudent);
        System.out.println("Student registered successfully!");
        newStudent.displayStudentInfo();
    }
    
    private void viewAllStudents() {
        System.out.println("\n=== ALL STUDENTS ===");
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        
        System.out.printf("%-8s %-20s %-25s %-20s %-8s %-6s%n", 
                         "ID", "Name", "Email", "Department", "Year", "GPA");
        System.out.println("-".repeat(90));
        
        students.values().stream()
            .sorted((s1, s2) -> s1.getStudentId().compareTo(s2.getStudentId()))
            .forEach(student -> {
                System.out.printf("%-8s %-20s %-25s %-20s %-8d %-6.2f%n",
                                 student.getStudentId(),
                                 student.getFullName().length() > 18 ? student.getFullName().substring(0, 18) + ".." : student.getFullName(),
                                 student.getEmail().length() > 23 ? student.getEmail().substring(0, 23) + ".." : student.getEmail(),
                                 student.getDepartment() != null ? 
                                     (student.getDepartment().length() > 18 ? student.getDepartment().substring(0, 18) + ".." : student.getDepartment()) : "N/A",
                                 student.getYear(),
                                 student.getGpa());
            });
        
        System.out.println("-".repeat(90));
        System.out.println("Total Students: " + students.size());
    }
    
    private void searchStudentsMenu() {
        System.out.println("\n=== SEARCH STUDENTS ===");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Department");
        System.out.println("3. Search by Year");
        System.out.println("4. Search by GPA Range");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: searchByName(); break;
            case 2: searchByDepartment(); break;
            case 3: searchByYear(); break;
            case 4: searchByGpaRange(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void searchByName() {
        System.out.print("Enter name to search: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        List<Student> results = students.values().stream()
                .filter(student -> student.getFullName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "name containing '" + searchTerm + "'");
    }
    
    private void searchByDepartment() {
        System.out.print("Enter department to search: ");
        String department = scanner.nextLine().toLowerCase();
        
        List<Student> results = students.values().stream()
                .filter(student -> student.getDepartment() != null && 
                                 student.getDepartment().toLowerCase().contains(department))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "department containing '" + department + "'");
    }
    
    private void searchByYear() {
        System.out.print("Enter year (1-4): ");
        try {
            int year = Integer.parseInt(scanner.nextLine());
            
            List<Student> results = students.values().stream()
                    .filter(student -> student.getYear() == year)
                    .collect(Collectors.toList());
            
            displaySearchResults(results, "year " + year);
        } catch (NumberFormatException e) {
            System.out.println("Invalid year format.");
        }
    }
    
    private void searchByGpaRange() {
        System.out.print("Enter minimum GPA: ");
        try {
            double minGpa = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter maximum GPA: ");
            double maxGpa = Double.parseDouble(scanner.nextLine());
            
            List<Student> results = students.values().stream()
                    .filter(student -> student.getGpa() >= minGpa && student.getGpa() <= maxGpa)
                    .collect(Collectors.toList());
            
            displaySearchResults(results, "GPA between " + minGpa + " and " + maxGpa);
        } catch (NumberFormatException e) {
            System.out.println("Invalid GPA format.");
        }
    }
    
    private void displaySearchResults(List<Student> results, String criteria) {
        System.out.println("\n=== SEARCH RESULTS ===");
        System.out.println("Search criteria: " + criteria);
        System.out.println("Results found: " + results.size());
        
        if (results.isEmpty()) {
            System.out.println("No students found matching the criteria.");
            return;
        }
        
        System.out.printf("%-8s %-20s %-25s %-20s %-8s %-6s%n", 
                         "ID", "Name", "Email", "Department", "Year", "GPA");
        System.out.println("-".repeat(90));
        
        results.forEach(student -> {
            System.out.printf("%-8s %-20s %-25s %-20s %-8d %-6.2f%n",
                             student.getStudentId(),
                             student.getFullName().length() > 18 ? student.getFullName().substring(0, 18) + ".." : student.getFullName(),
                             student.getEmail().length() > 23 ? student.getEmail().substring(0, 23) + ".." : student.getEmail(),
                             student.getDepartment() != null ? 
                                 (student.getDepartment().length() > 18 ? student.getDepartment().substring(0, 18) + ".." : student.getDepartment()) : "N/A",
                             student.getYear(),
                             student.getGpa());
        });
    }
    
    private void updateStudentInteractive() {
        System.out.println("\n=== UPDATE STUDENT ===");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = read(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("Current student information:");
        student.displayStudentInfo();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Email");
        System.out.println("3. Phone");
        System.out.println("4. Address");
        System.out.println("5. Department");
        System.out.println("6. Year");
        System.out.println("7. GPA");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new first name: ");
                String firstName = scanner.nextLine();
                System.out.print("Enter new last name: ");
                String lastName = scanner.nextLine();
                student.setFirstName(firstName);
                student.setLastName(lastName);
                break;
            case 2:
                System.out.print("Enter new email: ");
                String email = scanner.nextLine();
                if (ValidationUtils.isValidEmail(email)) {
                    student.setEmail(email);
                } else {
                    System.out.println("Invalid email format.");
                    return;
                }
                break;
            case 3:
                System.out.print("Enter new phone: ");
                student.setPhone(scanner.nextLine());
                break;
            case 4:
                System.out.print("Enter new address: ");
                student.setAddress(scanner.nextLine());
                break;
            case 5:
                System.out.print("Enter new department: ");
                student.setDepartment(scanner.nextLine());
                break;
            case 6:
                System.out.print("Enter new year (1-4): ");
                try {
                    int year = Integer.parseInt(scanner.nextLine());
                    if (year >= 1 && year <= 4) {
                        student.setYear(year);
                    } else {
                        System.out.println("Invalid year. Must be 1-4.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid year format.");
                    return;
                }
                break;
            case 7:
                System.out.print("Enter new GPA (0.0-4.0): ");
                try {
                    double gpa = Double.parseDouble(scanner.nextLine());
                    if (ValidationUtils.isValidGPA(gpa)) {
                        student.setGpa(gpa);
                    } else {
                        System.out.println("Invalid GPA. Must be 0.0-4.0.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid GPA format.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        update(student);
        System.out.println("Student updated successfully!");
    }
    
    private void deleteStudentInteractive() {
        System.out.println("\n=== DELETE STUDENT ===");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = read(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("Student to be deleted:");
        student.displayStudentInfo();
        
        System.out.print("Are you sure you want to delete this student? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if ("yes".equalsIgnoreCase(confirmation)) {
            delete(studentId);
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void studentReportsMenu() {
        System.out.println("\n=== STUDENT REPORTS ===");
        System.out.println("1. Department-wise Report");
        System.out.println("2. Year-wise Report");
        System.out.println("3. GPA Analysis Report");
        System.out.println("4. Top Performers Report");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: generateDepartmentWiseReport(); break;
            case 2: generateYearWiseReport(); break;
            case 3: generateGpaAnalysisReport(); break;
            case 4: generateTopPerformersReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void generateDepartmentWiseReport() {
        System.out.println("\n=== DEPARTMENT-WISE REPORT ===");
        
        Map<String, List<Student>> studentsByDept = students.values().stream()
                .filter(student -> student.getDepartment() != null)
                .collect(Collectors.groupingBy(Student::getDepartment));
        
        System.out.printf("%-20s %-10s %-10s%n", "Department", "Students", "Avg GPA");
        System.out.println("-".repeat(45));
        
        studentsByDept.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String dept = entry.getKey();
                    List<Student> deptStudents = entry.getValue();
                    double avgGpa = deptStudents.stream()
                            .mapToDouble(Student::getGpa)
                            .average()
                            .orElse(0.0);
                    
                    System.out.printf("%-20s %-10d %-10.2f%n",
                                     dept.length() > 18 ? dept.substring(0, 18) + ".." : dept,
                                     deptStudents.size(),
                                     avgGpa);
                });
    }
    
    private void generateYearWiseReport() {
        System.out.println("\n=== YEAR-WISE REPORT ===");
        
        Map<Integer, List<Student>> studentsByYear = students.values().stream()
                .collect(Collectors.groupingBy(Student::getYear));
        
        System.out.printf("%-8s %-10s %-10s%n", "Year", "Students", "Avg GPA");
        System.out.println("-".repeat(30));
        
        studentsByYear.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    int year = entry.getKey();
                    List<Student> yearStudents = entry.getValue();
                    double avgGpa = yearStudents.stream()
                            .mapToDouble(Student::getGpa)
                            .average()
                            .orElse(0.0);
                    
                    System.out.printf("%-8d %-10d %-10.2f%n",
                                     year,
                                     yearStudents.size(),
                                     avgGpa);
                });
    }
    
    private void generateGpaAnalysisReport() {
        System.out.println("\n=== GPA ANALYSIS REPORT ===");
        
        List<Student> allStudents = new ArrayList<>(students.values());
        
        if (allStudents.isEmpty()) {
            System.out.println("No students available for analysis.");
            return;
        }
        
        double avgGpa = allStudents.stream().mapToDouble(Student::getGpa).average().orElse(0.0);
        double maxGpa = allStudents.stream().mapToDouble(Student::getGpa).max().orElse(0.0);
        double minGpa = allStudents.stream().mapToDouble(Student::getGpa).min().orElse(0.0);
        
        long excellentCount = allStudents.stream().filter(s -> s.getGpa() >= 3.5).count();
        long goodCount = allStudents.stream().filter(s -> s.getGpa() >= 3.0 && s.getGpa() < 3.5).count();
        long averageCount = allStudents.stream().filter(s -> s.getGpa() >= 2.5 && s.getGpa() < 3.0).count();
        long belowAverageCount = allStudents.stream().filter(s -> s.getGpa() < 2.5).count();
        
        System.out.println("Overall Statistics:");
        System.out.println("- Average GPA: " + String.format("%.2f", avgGpa));
        System.out.println("- Highest GPA: " + String.format("%.2f", maxGpa));
        System.out.println("- Lowest GPA: " + String.format("%.2f", minGpa));
        
        System.out.println("\nGPA Distribution:");
        System.out.println("- Excellent (3.5+): " + excellentCount + " students");
        System.out.println("- Good (3.0-3.4): " + goodCount + " students");
        System.out.println("- Average (2.5-2.9): " + averageCount + " students");
        System.out.println("- Below Average (<2.5): " + belowAverageCount + " students");
    }
    
    private void generateTopPerformersReport() {
        System.out.println("\n=== TOP PERFORMERS REPORT ===");
        
        List<Student> topPerformers = students.values().stream()
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .limit(10)
                .collect(Collectors.toList());
        
        if (topPerformers.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        
        System.out.printf("%-5s %-8s %-20s %-20s %-6s%n", "Rank", "ID", "Name", "Department", "GPA");
        System.out.println("-".repeat(65));
        
        for (int i = 0; i < topPerformers.size(); i++) {
            Student student = topPerformers.get(i);
            System.out.printf("%-5d %-8s %-20s %-20s %-6.2f%n",
                             (i + 1),
                             student.getStudentId(),
                             student.getFullName().length() > 18 ? student.getFullName().substring(0, 18) + ".." : student.getFullName(),
                             student.getDepartment() != null ? 
                                 (student.getDepartment().length() > 18 ? student.getDepartment().substring(0, 18) + ".." : student.getDepartment()) : "N/A",
                             student.getGpa());
        }
    }
    
    private void academicRecordsMenu() {
        System.out.println("\n=== ACADEMIC RECORDS ===");
        System.out.println("1. View Student Academic Record");
        System.out.println("2. Update GPA");
        System.out.println("3. Academic Standing Report");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: viewStudentAcademicRecord(); break;
            case 2: updateStudentGpa(); break;
            case 3: generateAcademicStandingReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void viewStudentAcademicRecord() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = read(studentId);
        if (student != null) {
            student.displayStudentInfo();
        } else {
            System.out.println("Student not found.");
        }
    }
    
    private void updateStudentGpa() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        Student student = read(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("Current GPA: " + student.getGpa());
        System.out.print("Enter new GPA (0.0-4.0): ");
        
        try {
            double newGpa = Double.parseDouble(scanner.nextLine());
            if (ValidationUtils.isValidGPA(newGpa)) {
                student.setGpa(newGpa);
                update(student);
                System.out.println("GPA updated successfully!");
            } else {
                System.out.println("Invalid GPA. Must be between 0.0 and 4.0.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid GPA format.");
        }
    }
    
    private void generateAcademicStandingReport() {
        System.out.println("\n=== ACADEMIC STANDING REPORT ===");
        
        List<Student> allStudents = new ArrayList<>(students.values());
        
        List<Student> deansListStudents = allStudents.stream()
                .filter(s -> s.getGpa() >= 3.5)
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .collect(Collectors.toList());
        
        List<Student> probationStudents = allStudents.stream()
                .filter(s -> s.getGpa() < 2.0)
                .sorted((s1, s2) -> Double.compare(s1.getGpa(), s2.getGpa()))
                .collect(Collectors.toList());
        
        System.out.println("DEAN'S LIST STUDENTS (GPA >= 3.5):");
        if (deansListStudents.isEmpty()) {
            System.out.println("No students on Dean's List.");
        } else {
            deansListStudents.forEach(student -> 
                System.out.println("- " + student.getFullName() + " (" + student.getStudentId() + ") - GPA: " + student.getGpa()));
        }
        
        System.out.println("\nACADEMIC PROBATION STUDENTS (GPA < 2.0):");
        if (probationStudents.isEmpty()) {
            System.out.println("No students on academic probation.");
        } else {
            probationStudents.forEach(student -> 
                System.out.println("- " + student.getFullName() + " (" + student.getStudentId() + ") - GPA: " + student.getGpa()));
        }
    }
    
    private void displayStudentStatistics() {
        System.out.println("\n=== STUDENT STATISTICS ===");
        
        int totalStudents = students.size();
        if (totalStudents == 0) {
            System.out.println("No students in the system.");
            return;
        }
        
        // Basic statistics
        System.out.println("OVERVIEW:");
        System.out.println("- Total Students: " + totalStudents);
        
        // Department distribution
        Map<String, Long> deptDistribution = students.values().stream()
                .filter(student -> student.getDepartment() != null)
                .collect(Collectors.groupingBy(Student::getDepartment, Collectors.counting()));
        
        System.out.println("\nDEPARTMENT DISTRIBUTION:");
        deptDistribution.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        
        // Year distribution
        Map<Integer, Long> yearDistribution = students.values().stream()
                .collect(Collectors.groupingBy(Student::getYear, Collectors.counting()));
        
        System.out.println("\nYEAR DISTRIBUTION:");
        yearDistribution.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("- Year " + entry.getKey() + ": " + entry.getValue()));
        
        // GPA statistics
        double avgGpa = students.values().stream().mapToDouble(Student::getGpa).average().orElse(0.0);
        System.out.println("\nGPA STATISTICS:");
        System.out.println("- Average GPA: " + String.format("%.2f", avgGpa));
        System.out.println("- Students with GPA >= 3.5: " + students.values().stream().filter(s -> s.getGpa() >= 3.5).count());
        System.out.println("- Students with GPA < 2.0: " + students.values().stream().filter(s -> s.getGpa() < 2.0).count());
        
        logger.log("Student statistics displayed");
    }
    
    // Utility methods for external access
    public int getTotalStudents() { return students.size(); }
    public int getActiveStudents() { return students.size(); } // All students are considered active in this implementation
    
    public List<Student> getStudentsByDepartment(String department) {
        return students.values().stream()
                .filter(student -> department.equals(student.getDepartment()))
                .collect(Collectors.toList());
    }
    
    public List<Student> getStudentsByYear(int year) {
        return students.values().stream()
                .filter(student -> student.getYear() == year)
                .collect(Collectors.toList());
    }
    
    public List<Student> getTopPerformers(int limit) {
        return students.values().stream()
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    public double getAverageGPA() {
        return students.values().stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
    }
}