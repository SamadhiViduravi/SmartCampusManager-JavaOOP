package com.campus.exams;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Manager class for handling all exam operations
 */
public class ExamManager implements Manageable<Exam> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    
    private Map<String, Exam> exams;
    private NotificationService notificationService;
    private int examCounter;
    
    public ExamManager() {
        this.exams = new HashMap<>();
        this.notificationService = NotificationService.getInstance();
        this.examCounter = 1;
        initializeSampleData();
        logger.log("ExamManager initialized");
    }
    
    private void initializeSampleData() {
        // Create sample exams
        Exam midterm1 = new Exam("E001", "Data Structures Midterm", "CS101", "Data Structures", ExamType.MIDTERM);
        midterm1.scheduleExam(LocalDate.now().plusDays(7), LocalTime.of(9, 0), "Room A-101");
        midterm1.setInstructorId("T001");
        midterm1.addInvigilator("T002");
        midterm1.addInvigilator("T003");
        midterm1.enrollStudent("S001");
        midterm1.enrollStudent("S002");
        midterm1.enrollStudent("S003");
        midterm1.enrollStudent("S004");
        midterm1.enrollStudent("S005");
        exams.put("E001", midterm1);
        
        Exam quiz1 = new Exam("E002", "Java Programming Quiz", "CS102", "Java Programming", ExamType.QUIZ);
        quiz1.scheduleExam(LocalDate.now().plusDays(3), LocalTime.of(14, 0), "Lab B-201");
        quiz1.setInstructorId("T004");
        quiz1.addInvigilator("T005");
        quiz1.enrollStudent("S001");
        quiz1.enrollStudent("S002");
        quiz1.enrollStudent("S006");
        quiz1.enrollStudent("S007");
        exams.put("E002", quiz1);
        
        Exam final1 = new Exam("E003", "Database Systems Final", "CS201", "Database Systems", ExamType.FINAL);
        final1.scheduleExam(LocalDate.now().plusDays(21), LocalTime.of(10, 0), "Hall C-301");
        final1.setInstructorId("T006");
        final1.addInvigilator("T007");
        final1.addInvigilator("T008");
        final1.enrollStudent("S003");
        final1.enrollStudent("S004");
        final1.enrollStudent("S008");
        final1.enrollStudent("S009");
        final1.enrollStudent("S010");
        exams.put("E003", final1);
        
        Exam practical1 = new Exam("E004", "Chemistry Lab Practical", "CH101", "General Chemistry", ExamType.PRACTICAL);
        practical1.scheduleExam(LocalDate.now().plusDays(5), LocalTime.of(13, 0), "Chemistry Lab");
        practical1.setInstructorId("T009");
        practical1.addInvigilator("T010");
        practical1.enrollStudent("S005");
        practical1.enrollStudent("S006");
        practical1.enrollStudent("S011");
        practical1.enrollStudent("S012");
        exams.put("E004", practical1);
        
        Exam online1 = new Exam("E005", "Mathematics Assignment", "MA101", "Calculus I", ExamType.ASSIGNMENT);
        online1.scheduleExam(LocalDate.now().plusDays(14), LocalTime.of(0, 0), "Online");
        online1.setOnlineExam("Moodle", "https://moodle.campus.edu/assignment/ma101");
        online1.setInstructorId("T011");
        online1.enrollStudent("S007");
        online1.enrollStudent("S008");
        online1.enrollStudent("S013");
        online1.enrollStudent("S014");
        exams.put("E005", online1);
        
        // Add some results to completed exams
        Exam completedExam = new Exam("E006", "Physics Midterm", "PH101", "Physics I", ExamType.MIDTERM);
        completedExam.scheduleExam(LocalDate.now().minusDays(7), LocalTime.of(11, 0), "Room D-102");
        completedExam.setInstructorId("T012");
        completedExam.enrollStudent("S001");
        completedExam.enrollStudent("S002");
        completedExam.enrollStudent("S003");
        completedExam.enrollStudent("S004");
        completedExam.enrollStudent("S005");
        completedExam.startExam();
        completedExam.endExam();
        
        // Add sample results
        completedExam.addResult("S001", 85, "A");
        completedExam.addResult("S002", 78, "B+");
        completedExam.addResult("S003", 92, "A+");
        completedExam.addResult("S004", 65, "C+");
        completedExam.addResult("S005", 45, "D");
        
        exams.put("E006", completedExam);
        
        logger.log("Sample exam data initialized");
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== EXAM MANAGEMENT MENU ===");
            System.out.println("1. Exam Management");
            System.out.println("2. Schedule Management");
            System.out.println("3. Student Enrollment");
            System.out.println("4. Results Management");
            System.out.println("5. Reports & Analytics");
            System.out.println("6. Notifications & Alerts");
            System.out.println("7. Search & Filter");
            System.out.println("8. System Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getChoice();
            
            switch (choice) {
                case 1: examManagementMenu(); break;
                case 2: scheduleManagementMenu(); break;
                case 3: enrollmentManagementMenu(); break;
                case 4: resultsManagementMenu(); break;
                case 5: reportsMenu(); break;
                case 6: notificationsMenu(); break;
                case 7: searchMenu(); break;
                case 8: displaySystemStatistics(); break;
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
    public void create(Exam exam) {
        exams.put(exam.getExamId(), exam);
        notificationService.notifyObservers("New exam created: " + exam.getExamName());
        logger.log("Exam created: " + exam.getExamId());
    }
    
    @Override
    public Exam read(String examId) {
        return exams.get(examId);
    }
    
    @Override
    public void update(Exam exam) {
        exams.put(exam.getExamId(), exam);
        notificationService.notifyObservers("Exam updated: " + exam.getExamName());
        logger.log("Exam updated: " + exam.getExamId());
    }
    
    @Override
    public void delete(String examId) {
        Exam exam = exams.remove(examId);
        if (exam != null) {
            notificationService.notifyObservers("Exam deleted: " + exam.getExamName());
            logger.log("Exam deleted: " + examId);
        }
    }
    
    @Override
    public List<Exam> getAll() {
        return new ArrayList<>(exams.values());
    }
    
    private void examManagementMenu() {
        System.out.println("\n=== EXAM MANAGEMENT ===");
        System.out.println("1. Create New Exam");
        System.out.println("2. View All Exams");
        System.out.println("3. View Exam Details");
        System.out.println("4. Update Exam");
        System.out.println("5. Delete Exam");
        System.out.println("6. Cancel Exam");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: createExamInteractive(); break;
            case 2: viewAllExams(); break;
            case 3: viewExamDetailsInteractive(); break;
            case 4: updateExamInteractive(); break;
            case 5: deleteExamInteractive(); break;
            case 6: cancelExamInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void createExamInteractive() {
        System.out.println("\n=== CREATE NEW EXAM ===");
        
        String examId = "E" + String.format("%03d", examCounter++);
        
        System.out.print("Enter Exam Name: ");
        String examName = scanner.nextLine();
        
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        
        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine();
        
        System.out.println("Select Exam Type:");
        ExamType[] types = ExamType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        int typeChoice = getChoice();
        
        if (typeChoice < 1 || typeChoice > types.length) {
            System.out.println("Invalid exam type choice.");
            return;
        }
        
        ExamType examType = types[typeChoice - 1];
        
        Exam newExam = new Exam(examId, examName, courseId, courseName, examType);
        
        // Optional scheduling
        System.out.print("Schedule exam now? (y/n): ");
        String scheduleNow = scanner.nextLine();
        
        if ("y".equalsIgnoreCase(scheduleNow)) {
            scheduleExamInteractive(newExam);
        }
        
        create(newExam);
        System.out.println("Exam created successfully!");
        newExam.displayExamInfo();
    }
    
    private void scheduleExamInteractive(Exam exam) {
        System.out.println("\n=== SCHEDULE EXAM ===");
        
        System.out.print("Enter exam date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        
        System.out.print("Enter start time (HH:MM): ");
        String timeStr = scanner.nextLine();
        
        System.out.print("Enter venue: ");
        String venue = scanner.nextLine();
        
        try {
            LocalDate examDate = LocalDate.parse(dateStr);
            LocalTime startTime = LocalTime.parse(timeStr);
            
            exam.scheduleExam(examDate, startTime, venue);
            
            System.out.print("Set as online exam? (y/n): ");
            String isOnline = scanner.nextLine();
            
            if ("y".equalsIgnoreCase(isOnline)) {
                System.out.print("Enter platform: ");
                String platform = scanner.nextLine();
                System.out.print("Enter exam link: ");
                String link = scanner.nextLine();
                exam.setOnlineExam(platform, link);
            }
            
            System.out.println("Exam scheduled successfully!");
            
        } catch (Exception e) {
            System.out.println("Invalid date/time format. Please use YYYY-MM-DD and HH:MM");
        }
    }
    
    private void viewAllExams() {
        System.out.println("\n=== ALL EXAMS ===");
        if (exams.isEmpty()) {
            System.out.println("No exams found.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-15s %-12s %-12s %-15s %-10s%n", 
                         "ID", "Name", "Course", "Type", "Date", "Status", "Students");
        System.out.println("-".repeat(100));
        
        exams.values().stream()
            .sorted((e1, e2) -> {
                if (e1.getExamDate() == null && e2.getExamDate() == null) return 0;
                if (e1.getExamDate() == null) return 1;
                if (e2.getExamDate() == null) return -1;
                return e1.getExamDate().compareTo(e2.getExamDate());
            })
            .forEach(exam -> {
                System.out.printf("%-8s %-25s %-15s %-12s %-12s %-15s %-10d%n",
                                 exam.getExamId(),
                                 exam.getExamName().length() > 23 ? exam.getExamName().substring(0, 23) + ".." : exam.getExamName(),
                                 exam.getCourseId(),
                                 exam.getExamType().toString().length() > 10 ? exam.getExamType().toString().substring(0, 10) + ".." : exam.getExamType(),
                                 exam.getExamDate() != null ? exam.getExamDate().toString() : "Not scheduled",
                                 exam.getStatus().toString().length() > 13 ? exam.getStatus().toString().substring(0, 13) + ".." : exam.getStatus(),
                                 exam.getEnrolledCount());
            });
        
        System.out.println("-".repeat(100));
        System.out.println("Total Exams: " + exams.size());
        System.out.println("Scheduled: " + exams.values().stream().filter(e -> e.getStatus() == ExamStatus.SCHEDULED).count());
        System.out.println("Completed: " + exams.values().stream().filter(e -> e.getStatus() == ExamStatus.COMPLETED).count());
        System.out.println("Upcoming: " + exams.values().stream().filter(Exam::isUpcoming).count());
    }
    
    private void viewExamDetailsInteractive() {
        System.out.println("\n=== VIEW EXAM DETAILS ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam != null) {
            exam.displayExamInfo();
            
            if (exam.getResultsCount() > 0) {
                System.out.print("\nView results? (y/n): ");
                String viewResults = scanner.nextLine();
                if ("y".equalsIgnoreCase(viewResults)) {
                    exam.displayResults();
                }
            }
        } else {
            System.out.println("Exam not found.");
        }
    }
    
    private void updateExamInteractive() {
        System.out.println("\n=== UPDATE EXAM ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.println("Current exam details:");
        exam.displayExamInfo();
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Exam Name");
        System.out.println("2. Schedule");
        System.out.println("3. Venue");
        System.out.println("4. Duration");
        System.out.println("5. Max Marks");
        System.out.println("6. Instructions");
        System.out.println("7. Add Invigilator");
        System.out.println("8. Online/Offline Mode");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.print("Enter new exam name: ");
                exam.setExamName(scanner.nextLine());
                break;
            case 2:
                scheduleExamInteractive(exam);
                break;
            case 3:
                System.out.print("Enter new venue: ");
                exam.setVenue(scanner.nextLine());
                break;
            case 4:
                System.out.print("Enter new duration (minutes): ");
                try {
                    int duration = Integer.parseInt(scanner.nextLine());
                    exam.setDuration(duration);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid duration.");
                    return;
                }
                break;
            case 5:
                System.out.print("Enter new max marks: ");
                try {
                    int maxMarks = Integer.parseInt(scanner.nextLine());
                    exam.setMaxMarks(maxMarks);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid marks.");
                    return;
                }
                break;
            case 6:
                System.out.print("Enter new instructions: ");
                exam.setInstructions(scanner.nextLine());
                break;
            case 7:
                System.out.print("Enter invigilator ID: ");
                exam.addInvigilator(scanner.nextLine());
                break;
            case 8:
                System.out.print("Set as online exam? (y/n): ");
                String isOnline = scanner.nextLine();
                if ("y".equalsIgnoreCase(isOnline)) {
                    System.out.print("Enter platform: ");
                    String platform = scanner.nextLine();
                    System.out.print("Enter exam link: ");
                    String link = scanner.nextLine();
                    exam.setOnlineExam(platform, link);
                } else {
                    System.out.print("Enter venue: ");
                    String venue = scanner.nextLine();
                    exam.setOfflineExam(venue);
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        update(exam);
        System.out.println("Exam updated successfully!");
    }
    
    private void deleteExamInteractive() {
        System.out.println("\n=== DELETE EXAM ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.println("Exam to be deleted:");
        exam.displayExamInfo();
        
        System.out.print("Are you sure you want to delete this exam? (yes/no): ");
        String confirmation = scanner.nextLine();
        
        if ("yes".equalsIgnoreCase(confirmation)) {
            delete(examId);
            System.out.println("Exam deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void cancelExamInteractive() {
        System.out.println("\n=== CANCEL EXAM ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        if (exam.getStatus() == ExamStatus.COMPLETED) {
            System.out.println("Cannot cancel completed exam.");
            return;
        }
        
        System.out.print("Enter reason for cancellation: ");
        String reason = scanner.nextLine();
        
        try {
            exam.cancelExam(reason);
            update(exam);
            System.out.println("Exam cancelled successfully!");
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    // Additional methods for schedule management, enrollment, results, etc.
    private void scheduleManagementMenu() {
        System.out.println("\n=== SCHEDULE MANAGEMENT ===");
        System.out.println("1. View Today's Exams");
        System.out.println("2. View Upcoming Exams");
        System.out.println("3. View Exam Calendar");
        System.out.println("4. Reschedule Exam");
        System.out.println("5. Postpone Exam");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: viewTodaysExams(); break;
            case 2: viewUpcomingExams(); break;
            case 3: viewExamCalendar(); break;
            case 4: rescheduleExamInteractive(); break;
            case 5: postponeExamInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void viewTodaysExams() {
        System.out.println("\n=== TODAY'S EXAMS ===");
        List<Exam> todaysExams = exams.values().stream()
                .filter(Exam::isToday)
                .sorted((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()))
                .collect(Collectors.toList());
        
        if (todaysExams.isEmpty()) {
            System.out.println("No exams scheduled for today.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-10s %-15s %-10s%n", 
                         "ID", "Name", "Time", "Venue", "Students");
        System.out.println("-".repeat(75));
        
        todaysExams.forEach(exam -> {
            System.out.printf("%-8s %-25s %-10s %-15s %-10d%n",
                             exam.getExamId(),
                             exam.getExamName().length() > 23 ? exam.getExamName().substring(0, 23) + ".." : exam.getExamName(),
                             exam.getStartTime(),
                             exam.getVenue() != null ? 
                                 (exam.getVenue().length() > 13 ? exam.getVenue().substring(0, 13) + ".." : exam.getVenue()) : "TBD",
                             exam.getEnrolledCount());
        });
    }
    
    private void viewUpcomingExams() {
        System.out.println("\n=== UPCOMING EXAMS ===");
        List<Exam> upcomingExams = exams.values().stream()
                .filter(Exam::isUpcoming)
                .sorted((e1, e2) -> e1.getExamDate().compareTo(e2.getExamDate()))
                .collect(Collectors.toList());
        
        if (upcomingExams.isEmpty()) {
            System.out.println("No upcoming exams.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-12s %-10s %-15s%n", 
                         "ID", "Name", "Date", "Time", "Venue");
        System.out.println("-".repeat(75));
        
        upcomingExams.forEach(exam -> {
            System.out.printf("%-8s %-25s %-12s %-10s %-15s%n",
                             exam.getExamId(),
                             exam.getExamName().length() > 23 ? exam.getExamName().substring(0, 23) + ".." : exam.getExamName(),
                             exam.getExamDate(),
                             exam.getStartTime(),
                             exam.getVenue() != null ? 
                                 (exam.getVenue().length() > 13 ? exam.getVenue().substring(0, 13) + ".." : exam.getVenue()) : "TBD");
        });
    }
    
    private void viewExamCalendar() {
        System.out.println("\n=== EXAM CALENDAR (Next 30 Days) ===");
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        
        Map<LocalDate, List<Exam>> examsByDate = exams.values().stream()
                .filter(exam -> exam.getExamDate() != null && 
                               !exam.getExamDate().isBefore(startDate) && 
                               !exam.getExamDate().isAfter(endDate))
                .collect(Collectors.groupingBy(Exam::getExamDate));
        
        if (examsByDate.isEmpty()) {
            System.out.println("No exams scheduled in the next 30 days.");
            return;
        }
        
        examsByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.println("\n" + entry.getKey().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
                    System.out.println("-".repeat(50));
                    entry.getValue().stream()
                            .sorted((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()))
                            .forEach(exam -> {
                                System.out.printf("  %s - %s (%s) - %s - %d students%n",
                                                 exam.getStartTime(),
                                                 exam.getExamName(),
                                                 exam.getExamType(),
                                                 exam.getVenue(),
                                                 exam.getEnrolledCount());
                            });
                });
    }
    
    private void rescheduleExamInteractive() {
        System.out.println("\n=== RESCHEDULE EXAM ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        if (exam.getStatus() != ExamStatus.SCHEDULED) {
            System.out.println("Only scheduled exams can be rescheduled.");
            return;
        }
        
        System.out.println("Current schedule:");
        System.out.println("Date: " + exam.getExamDate());
        System.out.println("Time: " + exam.getStartTime());
        System.out.println("Venue: " + exam.getVenue());
        
        scheduleExamInteractive(exam);
        update(exam);
        System.out.println("Exam rescheduled successfully!");
    }
    
    private void postponeExamInteractive() {
        System.out.println("\n=== POSTPONE EXAM ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.print("Enter new date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        
        System.out.print("Enter new time (HH:MM): ");
        String timeStr = scanner.nextLine();
        
        System.out.print("Enter reason for postponement: ");
        String reason = scanner.nextLine();
        
        try {
            LocalDate newDate = LocalDate.parse(dateStr);
            LocalTime newTime = LocalTime.parse(timeStr);
            
            exam.postponeExam(newDate, newTime, reason);
            update(exam);
            System.out.println("Exam postponed successfully!");
            
        } catch (Exception e) {
            System.out.println("Invalid date/time format or error: " + e.getMessage());
        }
    }
    
    // Utility methods
    public List<Exam> getUpcomingExams() {
        return exams.values().stream()
                .filter(Exam::isUpcoming)
                .collect(Collectors.toList());
    }
    
    public List<Exam> getTodaysExams() {
        return exams.values().stream()
                .filter(Exam::isToday)
                .collect(Collectors.toList());
    }
    
    public List<Exam> getOverdueExams() {
        return exams.values().stream()
                .filter(Exam::isOverdue)
                .collect(Collectors.toList());
    }
    
    public List<Exam> getExamsByStatus(ExamStatus status) {
        return exams.values().stream()
                .filter(exam -> exam.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    public List<Exam> getExamsByType(ExamType type) {
        return exams.values().stream()
                .filter(exam -> exam.getExamType() == type)
                .collect(Collectors.toList());
    }
    
    public List<Exam> getExamsByCourse(String courseId) {
        return exams.values().stream()
                .filter(exam -> exam.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }
    
    public List<Exam> getExamsByInstructor(String instructorId) {
        return exams.values().stream()
                .filter(exam -> instructorId.equals(exam.getInstructorId()))
                .collect(Collectors.toList());
    }
    
    private void enrollmentManagementMenu() {
        System.out.println("\n=== ENROLLMENT MANAGEMENT ===");
        System.out.println("1. Enroll Student");
        System.out.println("2. Unenroll Student");
        System.out.println("3. View Exam Enrollments");
        System.out.println("4. View Student Exams");
        System.out.println("5. Bulk Enrollment");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: enrollStudentInteractive(); break;
            case 2: unenrollStudentInteractive(); break;
            case 3: viewExamEnrollmentsInteractive(); break;
            case 4: viewStudentExamsInteractive(); break;
            case 5: bulkEnrollmentInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void enrollStudentInteractive() {
        System.out.println("\n=== ENROLL STUDENT ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        try {
            exam.enrollStudent(studentId);
            update(exam);
            System.out.println("Student enrolled successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void unenrollStudentInteractive() {
        System.out.println("\n=== UNENROLL STUDENT ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        exam.unenrollStudent(studentId);
        update(exam);
        System.out.println("Student unenrolled successfully!");
    }
    
    private void viewExamEnrollmentsInteractive() {
        System.out.println("\n=== VIEW EXAM ENROLLMENTS ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.println("Exam: " + exam.getExamName());
        System.out.println("Enrolled Students: " + exam.getEnrolledCount());
        
        if (exam.getEnrolledStudents().isEmpty()) {
            System.out.println("No students enrolled.");
            return;
        }
        
        System.out.println("\nEnrolled Students:");
        exam.getEnrolledStudents().forEach(studentId -> {
            ExamResult result = exam.getStudentResult(studentId);
            String status = result != null ? "Result: " + result.getLetterGrade() : "Pending";
            System.out.println("- " + studentId + " (" + status + ")");
        });
    }
    
    private void viewStudentExamsInteractive() {
        System.out.println("\n=== VIEW STUDENT EXAMS ===");
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        List<Exam> studentExams = exams.values().stream()
                .filter(exam -> exam.getEnrolledStudents().contains(studentId))
                .sorted((e1, e2) -> {
                    if (e1.getExamDate() == null && e2.getExamDate() == null) return 0;
                    if (e1.getExamDate() == null) return 1;
                    if (e2.getExamDate() == null) return -1;
                    return e1.getExamDate().compareTo(e2.getExamDate());
                })
                .collect(Collectors.toList());
        
        if (studentExams.isEmpty()) {
            System.out.println("No exams found for student: " + studentId);
            return;
        }
        
        System.out.println("Exams for Student: " + studentId);
        System.out.printf("%-8s %-25s %-12s %-10s %-15s %-10s%n", 
                         "ID", "Name", "Date", "Time", "Status", "Result");
        System.out.println("-".repeat(85));
        
        studentExams.forEach(exam -> {
            ExamResult result = exam.getStudentResult(studentId);
            String resultStr = result != null ? result.getLetterGrade() : "Pending";
            
            System.out.printf("%-8s %-25s %-12s %-10s %-15s %-10s%n",
                             exam.getExamId(),
                             exam.getExamName().length() > 23 ? exam.getExamName().substring(0, 23) + ".." : exam.getExamName(),
                             exam.getExamDate() != null ? exam.getExamDate().toString() : "TBD",
                             exam.getStartTime() != null ? exam.getStartTime().toString() : "TBD",
                             exam.getStatus().toString().length() > 13 ? exam.getStatus().toString().substring(0, 13) + ".." : exam.getStatus(),
                             resultStr);
        });
    }
    
    private void bulkEnrollmentInteractive() {
        System.out.println("\n=== BULK ENROLLMENT ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.println("Enter Student IDs (comma-separated): ");
        String studentIds = scanner.nextLine();
        
        String[] ids = studentIds.split(",");
        int enrolled = 0;
        
        for (String id : ids) {
            String studentId = id.trim();
            if (!studentId.isEmpty()) {
                try {
                    exam.enrollStudent(studentId);
                    enrolled++;
                } catch (Exception e) {
                    System.out.println("Failed to enroll " + studentId + ": " + e.getMessage());
                }
            }
        }
        
        update(exam);
        System.out.println("Bulk enrollment completed. " + enrolled + " students enrolled.");
    }
    
    private void resultsManagementMenu() {
        System.out.println("\n=== RESULTS MANAGEMENT ===");
        System.out.println("1. Add Result");
        System.out.println("2. Update Result");
        System.out.println("3. View Exam Results");
        System.out.println("4. View Student Result");
        System.out.println("5. Bulk Results Entry");
        System.out.println("6. Mark Absent");
        System.out.println("7. Mark Malpractice");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: addResultInteractive(); break;
            case 2: updateResultInteractive(); break;
            case 3: viewExamResultsInteractive(); break;
            case 4: viewStudentResultInteractive(); break;
            case 5: bulkResultsEntryInteractive(); break;
            case 6: markAbsentInteractive(); break;
            case 7: markMalpracticeInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void addResultInteractive() {
        System.out.println("\n=== ADD RESULT ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        if (!exam.getEnrolledStudents().contains(studentId)) {
            System.out.println("Student is not enrolled for this exam.");
            return;
        }
        
        System.out.print("Enter marks obtained: ");
        try {
            int marks = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter grade: ");
            String grade = scanner.nextLine();
            
            exam.addResult(studentId, marks, grade);
            update(exam);
            System.out.println("Result added successfully!");
            
            ExamResult result = exam.getStudentResult(studentId);
            if (result != null) {
                result.displayResultInfo();
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid marks format.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void updateResultInteractive() {
        System.out.println("\n=== UPDATE RESULT ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        ExamResult currentResult = exam.getStudentResult(studentId);
        if (currentResult == null) {
            System.out.println("No result found for this student.");
            return;
        }
        
        System.out.println("Current result:");
        currentResult.displayResultInfo();
        
        System.out.print("Enter new marks: ");
        try {
            int marks = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter new grade: ");
            String grade = scanner.nextLine();
            
            exam.updateResult(studentId, marks, grade);
            update(exam);
            System.out.println("Result updated successfully!");
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid marks format.");
        }
    }
    
    private void viewExamResultsInteractive() {
        System.out.println("\n=== VIEW EXAM RESULTS ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam != null) {
            exam.displayResults();
        } else {
            System.out.println("Exam not found.");
        }
    }
    
    private void viewStudentResultInteractive() {
        System.out.println("\n=== VIEW STUDENT RESULT ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        ExamResult result = exam.getStudentResult(studentId);
        if (result != null) {
            result.displayResultInfo();
        } else {
            System.out.println("No result found for this student.");
        }
    }
    
    private void bulkResultsEntryInteractive() {
        System.out.println("\n=== BULK RESULTS ENTRY ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.println("Enrolled students for this exam:");
        exam.getEnrolledStudents().forEach(studentId -> {
            ExamResult existing = exam.getStudentResult(studentId);
            String status = existing != null ? " (Result exists)" : " (No result)";
            System.out.println("- " + studentId + status);
        });
        
        System.out.println("\nEnter results (StudentID:Marks:Grade format, one per line, 'done' to finish):");
        
        int added = 0;
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            
            if ("done".equalsIgnoreCase(input)) {
                break;
            }
            
            String[] parts = input.split(":");
            if (parts.length != 3) {
                System.out.println("Invalid format. Use StudentID:Marks:Grade");
                continue;
            }
            
            try {
                String studentId = parts[0].trim();
                int marks = Integer.parseInt(parts[1].trim());
                String grade = parts[2].trim();
                
                if (exam.getEnrolledStudents().contains(studentId)) {
                    exam.addResult(studentId, marks, grade);
                    added++;
                    System.out.println("Result added for " + studentId);
                } else {
                    System.out.println("Student " + studentId + " is not enrolled for this exam.");
                }
                
            } catch (NumberFormatException e) {
                System.out.println("Invalid marks format.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        update(exam);
        System.out.println("Bulk results entry completed. " + added + " results added.");
    }
    
    private void markAbsentInteractive() {
        System.out.println("\n=== MARK ABSENT ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        if (!exam.getEnrolledStudents().contains(studentId)) {
            System.out.println("Student is not enrolled for this exam.");
            return;
        }
        
        // Create absent result
        exam.addResult(studentId, 0, "AB");
        ExamResult result = exam.getStudentResult(studentId);
        if (result != null) {
            result.markAbsent();
        }
        
        update(exam);
        System.out.println("Student marked as absent.");
    }
    
    private void markMalpracticeInteractive() {
        System.out.println("\n=== MARK MALPRACTICE ===");
        System.out.print("Enter Exam ID: ");
        String examId = scanner.nextLine();
        
        Exam exam = read(examId);
        if (exam == null) {
            System.out.println("Exam not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        
        if (!exam.getEnrolledStudents().contains(studentId)) {
            System.out.println("Student is not enrolled for this exam.");
            return;
        }
        
        System.out.print("Enter malpractice details: ");
        String details = scanner.nextLine();
        
        // Create malpractice result
        exam.addResult(studentId, 0, "MP");
        ExamResult result = exam.getStudentResult(studentId);
        if (result != null) {
            result.markMalpractice(details);
        }
        
        update(exam);
        System.out.println("Student marked for malpractice.");
    }
    
    private void reportsMenu() {
        System.out.println("\n=== REPORTS & ANALYTICS ===");
        System.out.println("1. Exam Summary Report");
        System.out.println("2. Results Analysis");
        System.out.println("3. Performance Statistics");
        System.out.println("4. Course-wise Report");
        System.out.println("5. Instructor Report");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: generateExamSummaryReport(); break;
            case 2: generateResultsAnalysis(); break;
            case 3: generatePerformanceStatistics(); break;
            case 4: generateCourseWiseReport(); break;
            case 5: generateInstructorReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void generateExamSummaryReport() {
        System.out.println("\n=== EXAM SUMMARY REPORT ===");
        
        int totalExams = exams.size();
        long scheduledExams = exams.values().stream().filter(e -> e.getStatus() == ExamStatus.SCHEDULED).count();
        long completedExams = exams.values().stream().filter(e -> e.getStatus() == ExamStatus.COMPLETED).count();
        long upcomingExams = exams.values().stream().filter(Exam::isUpcoming).count();
        long todaysExams = exams.values().stream().filter(Exam::isToday).count();
        
        System.out.println("Total Exams: " + totalExams);
        System.out.println("Scheduled: " + scheduledExams);
        System.out.println("Completed: " + completedExams);
        System.out.println("Upcoming: " + upcomingExams);
        System.out.println("Today: " + todaysExams);
        
        // Exam type distribution
        System.out.println("\nExam Type Distribution:");
        Map<ExamType, Long> typeDistribution = exams.values().stream()
                .collect(Collectors.groupingBy(Exam::getExamType, Collectors.counting()));
        
        typeDistribution.entrySet().stream()
                .sorted(Map.Entry.<ExamType, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        
        // Total enrollments
        int totalEnrollments = exams.values().stream()
                .mapToInt(Exam::getEnrolledCount)
                .sum();
        
        System.out.println("\nTotal Student Enrollments: " + totalEnrollments);
        System.out.println("Average Enrollments per Exam: " + 
                          String.format("%.1f", (double) totalEnrollments / Math.max(totalExams, 1)));
        
        logger.log("Exam summary report generated");
    }
    
    private void generateResultsAnalysis() {
        System.out.println("\n=== RESULTS ANALYSIS ===");
        
        List<ExamResult> allResults = exams.values().stream()
                .flatMap(exam -> exam.getAllResults().values().stream())
                .collect(Collectors.toList());
        
        if (allResults.isEmpty()) {
            System.out.println("No results available for analysis.");
            return;
        }
        
        System.out.println("Total Results: " + allResults.size());
        
        // Pass/Fail statistics
        long passedCount = allResults.stream().filter(ExamResult::isPassed).count();
        long failedCount = allResults.size() - passedCount;
        double passPercentage = (double) passedCount / allResults.size() * 100;
        
        System.out.println("Passed: " + passedCount + " (" + String.format("%.1f", passPercentage) + "%)");
        System.out.println("Failed: " + failedCount + " (" + String.format("%.1f", 100 - passPercentage) + "%)");
        
        // Grade distribution
        System.out.println("\nGrade Distribution:");
        Map<String, Long> gradeDistribution = allResults.stream()
                .collect(Collectors.groupingBy(ExamResult::getLetterGrade, Collectors.counting()));
        
        gradeDistribution.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        
        // Average marks
        double averageMarks = allResults.stream()
                .mapToInt(ExamResult::getMarksObtained)
                .average()
                .orElse(0.0);
        
        System.out.println("\nAverage Marks: " + String.format("%.2f", averageMarks));
        
        // Performance categories
        long excellentCount = allResults.stream().filter(ExamResult::isExcellent).count();
        long goodCount = allResults.stream().filter(ExamResult::isGood).count();
        long satisfactoryCount = allResults.stream().filter(ExamResult::isSatisfactory).count();
        long needsImprovementCount = allResults.stream().filter(ExamResult::needsImprovement).count();
        
        System.out.println("\nPerformance Categories:");
        System.out.println("- Excellent (85%+): " + excellentCount);
        System.out.println("- Good (70-84%): " + goodCount);
        System.out.println("- Satisfactory (60-69%): " + satisfactoryCount);
        System.out.println("- Needs Improvement (<60%): " + needsImprovementCount);
        
        logger.log("Results analysis report generated");
    }
    
    private void generatePerformanceStatistics() {
        System.out.println("\n=== PERFORMANCE STATISTICS ===");
        
        List<Exam> completedExams = exams.values().stream()
                .filter(exam -> exam.getStatus() == ExamStatus.COMPLETED && exam.getResultsCount() > 0)
                .collect(Collectors.toList());
        
        if (completedExams.isEmpty()) {
            System.out.println("No completed exams with results available.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-10s %-10s %-10s %-10s%n", 
                         "Exam ID", "Name", "Students", "Average", "Pass %", "Highest");
        System.out.println("-".repeat(80));
        
        completedExams.forEach(exam -> {
            double avgMarks = exam.getAverageMarks();
            double passPercentage = exam.getPassPercentage();
            int highestMarks = exam.getAllResults().values().stream()
                    .mapToInt(ExamResult::getMarksObtained)
                    .max()
                    .orElse(0);
            
            System.out.printf("%-8s %-25s %-10d %-10.1f %-10.1f %-10d%n",
                             exam.getExamId(),
                             exam.getExamName().length() > 23 ? exam.getExamName().substring(0, 23) + ".." : exam.getExamName(),
                             exam.getResultsCount(),
                             avgMarks,
                             passPercentage,
                             highestMarks);
        });
        
        // Overall statistics
        double overallAverage = completedExams.stream()
                .mapToDouble(Exam::getAverageMarks)
                .average()
                .orElse(0.0);
        
        double overallPassRate = completedExams.stream()
                .mapToDouble(Exam::getPassPercentage)
                .average()
                .orElse(0.0);
        
        System.out.println("-".repeat(80));
        System.out.println("Overall Average Marks: " + String.format("%.2f", overallAverage));
        System.out.println("Overall Pass Rate: " + String.format("%.1f", overallPassRate) + "%");
        
        logger.log("Performance statistics report generated");
    }
    
    private void generateCourseWiseReport() {
        System.out.println("\n=== COURSE-WISE REPORT ===");
        
        Map<String, List<Exam>> examsByCourse = exams.values().stream()
                .collect(Collectors.groupingBy(Exam::getCourseId));
        
        if (examsByCourse.isEmpty()) {
            System.out.println("No exams available for course-wise analysis.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-8s %-8s %-10s %-10s%n", 
                         "Course ID", "Course Name", "Exams", "Students", "Avg Marks", "Pass Rate");
        System.out.println("-".repeat(75));
        
        examsByCourse.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String courseId = entry.getKey();
                    List<Exam> courseExams = entry.getValue();
                    
                    String courseName = courseExams.get(0).getCourseName();
                    int totalExams = courseExams.size();
                    int totalStudents = courseExams.stream().mapToInt(Exam::getEnrolledCount).sum();
                    
                    List<Exam> completedExams = courseExams.stream()
                            .filter(exam -> exam.getResultsCount() > 0)
                            .collect(Collectors.toList());
                    
                    double avgMarks = completedExams.stream()
                            .mapToDouble(Exam::getAverageMarks)
                            .average()
                            .orElse(0.0);
                    
                    double passRate = completedExams.stream()
                            .mapToDouble(Exam::getPassPercentage)
                            .average()
                            .orElse(0.0);
                    
                    System.out.printf("%-10s %-20s %-8d %-8d %-10.1f %-10.1f%n",
                                     courseId,
                                     courseName.length() > 18 ? courseName.substring(0, 18) + ".." : courseName,
                                     totalExams,
                                     totalStudents,
                                     avgMarks,
                                     passRate);
                });
        
        logger.log("Course-wise report generated");
    }
    
    private void generateInstructorReport() {
        System.out.println("\n=== INSTRUCTOR REPORT ===");
        
        Map<String, List<Exam>> examsByInstructor = exams.values().stream()
                .filter(exam -> exam.getInstructorId() != null)
                .collect(Collectors.groupingBy(Exam::getInstructorId));
        
        if (examsByInstructor.isEmpty()) {
            System.out.println("No instructor data available.");
            return;
        }
        
        System.out.printf("%-12s %-8s %-8s %-10s %-10s%n", 
                         "Instructor", "Exams", "Students", "Avg Marks", "Pass Rate");
        System.out.println("-".repeat(55));
        
        examsByInstructor.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String instructorId = entry.getKey();
                    List<Exam> instructorExams = entry.getValue();
                    
                    int totalExams = instructorExams.size();
                    int totalStudents = instructorExams.stream().mapToInt(Exam::getEnrolledCount).sum();
                    
                    List<Exam> completedExams = instructorExams.stream()
                            .filter(exam -> exam.getResultsCount() > 0)
                            .collect(Collectors.toList());
                    
                    double avgMarks = completedExams.stream()
                            .mapToDouble(Exam::getAverageMarks)
                            .average()
                            .orElse(0.0);
                    
                    double passRate = completedExams.stream()
                            .mapToDouble(Exam::getPassPercentage)
                            .average()
                            .orElse(0.0);
                    
                    System.out.printf("%-12s %-8d %-8d %-10.1f %-10.1f%n",
                                     instructorId,
                                     totalExams,
                                     totalStudents,
                                     avgMarks,
                                     passRate);
                });
        
        logger.log("Instructor report generated");
    }
    
    private void notificationsMenu() {
        System.out.println("\n=== NOTIFICATIONS & ALERTS ===");
        System.out.println("1. Upcoming Exam Alerts");
        System.out.println("2. Today's Exam Alerts");
        System.out.println("3. Overdue Exam Alerts");
        System.out.println("4. Results Pending Alerts");
        System.out.println("5. All Active Alerts");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: showUpcomingExamAlerts(); break;
            case 2: showTodaysExamAlerts(); break;
            case 3: showOverdueExamAlerts(); break;
            case 4: showResultsPendingAlerts(); break;
            case 5: showAllActiveAlerts(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void showUpcomingExamAlerts() {
        System.out.println("\n=== UPCOMING EXAM ALERTS ===");
        List<Exam> upcomingExams = getUpcomingExams();
        
        if (upcomingExams.isEmpty()) {
            System.out.println("No upcoming exams.");
            return;
        }
        
        System.out.println("📅 " + upcomingExams.size() + " upcoming exams:");
        upcomingExams.stream()
                .limit(10)
                .forEach(exam -> System.out.println("- " + exam.getExamName() + 
                                                   " (" + exam.getExamDate() + " at " + exam.getStartTime() + ")"));
    }
    
    private void showTodaysExamAlerts() {
        System.out.println("\n=== TODAY'S EXAM ALERTS ===");
        List<Exam> todaysExams = getTodaysExams();
        
        if (todaysExams.isEmpty()) {
            System.out.println("No exams scheduled for today.");
            return;
        }
        
        System.out.println("🚨 " + todaysExams.size() + " exams scheduled for today:");
        todaysExams.forEach(exam -> System.out.println("- " + exam.getExamName() + 
                                                       " at " + exam.getStartTime() + 
                                                       " in " + exam.getVenue()));
    }
    
    private void showOverdueExamAlerts() {
        System.out.println("\n=== OVERDUE EXAM ALERTS ===");
        List<Exam> overdueExams = getOverdueExams();
        
        if (overdueExams.isEmpty()) {
            System.out.println("No overdue exams.");
            return;
        }
        
        System.out.println("⚠️ " + overdueExams.size() + " overdue exams:");
        overdueExams.forEach(exam -> System.out.println("- " + exam.getExamName() + 
                                                        " (scheduled for " + exam.getExamDate() + ")"));
    }
    
    private void showResultsPendingAlerts() {
        System.out.println("\n=== RESULTS PENDING ALERTS ===");
        List<Exam> resultsPendingExams = exams.values().stream()
                .filter(exam -> exam.getStatus() == ExamStatus.COMPLETED && 
                               exam.getResultsCount() < exam.getEnrolledCount())
                .collect(Collectors.toList());
        
        if (resultsPendingExams.isEmpty()) {
            System.out.println("No exams with pending results.");
            return;
        }
        
        System.out.println("📝 " + resultsPendingExams.size() + " exams with pending results:");
        resultsPendingExams.forEach(exam -> {
            int pending = exam.getEnrolledCount() - exam.getResultsCount();
            System.out.println("- " + exam.getExamName() + 
                              " (" + pending + " results pending)");
        });
    }
    
    private void showAllActiveAlerts() {
        System.out.println("\n=== ALL ACTIVE ALERTS ===");
        
        int totalAlerts = 0;
        
        List<Exam> upcomingExams = getUpcomingExams();
        if (!upcomingExams.isEmpty()) {
            System.out.println("📅 Upcoming Exams: " + upcomingExams.size());
            totalAlerts += upcomingExams.size();
        }
        
        List<Exam> todaysExams = getTodaysExams();
        if (!todaysExams.isEmpty()) {
            System.out.println("🚨 Today's Exams: " + todaysExams.size());
            totalAlerts += todaysExams.size();
        }
        
        List<Exam> overdueExams = getOverdueExams();
        if (!overdueExams.isEmpty()) {
            System.out.println("⚠️ Overdue Exams: " + overdueExams.size());
            totalAlerts += overdueExams.size();
        }
        
        List<Exam> resultsPendingExams = exams.values().stream()
                .filter(exam -> exam.getStatus() == ExamStatus.COMPLETED && 
                               exam.getResultsCount() < exam.getEnrolledCount())
                .collect(Collectors.toList());
        
        if (!resultsPendingExams.isEmpty()) {
            System.out.println("📝 Results Pending: " + resultsPendingExams.size());
            totalAlerts += resultsPendingExams.size();
        }
        
        if (totalAlerts == 0) {
            System.out.println("✅ No active alerts. All systems normal.");
        } else {
            System.out.println("\nTotal Active Alerts: " + totalAlerts);
        }
    }
    
    private void searchMenu() {
        System.out.println("\n=== SEARCH & FILTER ===");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Course");
        System.out.println("3. Search by Type");
        System.out.println("4. Search by Status");
        System.out.println("5. Search by Date Range");
        System.out.println("6. Advanced Search");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: searchByNameInteractive(); break;
            case 2: searchByCourseInteractive(); break;
            case 3: searchByTypeInteractive(); break;
            case 4: searchByStatusInteractive(); break;
            case 5: searchByDateRangeInteractive(); break;
            case 6: advancedSearchInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void searchByNameInteractive() {
        System.out.println("\n=== SEARCH BY NAME ===");
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        List<Exam> results = exams.values().stream()
                .filter(exam -> exam.getExamName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "name containing '" + searchTerm + "'");
    }
    
    private void searchByCourseInteractive() {
        System.out.println("\n=== SEARCH BY COURSE ===");
        System.out.print("Enter course ID or name: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        List<Exam> results = exams.values().stream()
                .filter(exam -> exam.getCourseId().toLowerCase().contains(searchTerm) ||
                               exam.getCourseName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "course containing '" + searchTerm + "'");
    }
    
    private void searchByTypeInteractive() {
        System.out.println("\n=== SEARCH BY TYPE ===");
        System.out.println("Select exam type:");
        ExamType[] types = ExamType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice < 1 || choice > types.length) {
            System.out.println("Invalid choice.");
            return;
        }
        
        ExamType selectedType = types[choice - 1];
        List<Exam> results = getExamsByType(selectedType);
        
        displaySearchResults(results, "type '" + selectedType + "'");
    }
    
    private void searchByStatusInteractive() {
        System.out.println("\n=== SEARCH BY STATUS ===");
        System.out.println("Select exam status:");
        ExamStatus[] statuses = ExamStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice < 1 || choice > statuses.length) {
            System.out.println("Invalid choice.");
            return;
        }
        
        ExamStatus selectedStatus = statuses[choice - 1];
        List<Exam> results = getExamsByStatus(selectedStatus);
        
        displaySearchResults(results, "status '" + selectedStatus + "'");
    }
    
    private void searchByDateRangeInteractive() {
        System.out.println("\n=== SEARCH BY DATE RANGE ===");
        System.out.println("This feature would allow filtering by date range.");
        System.out.println("For demonstration, showing exams in next 30 days:");
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        
        List<Exam> results = exams.values().stream()
                .filter(exam -> exam.getExamDate() != null && 
                               !exam.getExamDate().isBefore(startDate) && 
                               !exam.getExamDate().isAfter(endDate))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "date range (next 30 days)");
    }
    
    private void advancedSearchInteractive() {
        System.out.println("\n=== ADVANCED SEARCH ===");
        System.out.println("Enter search criteria (leave blank to skip):");
        
        System.out.print("Name contains: ");
        String nameFilter = scanner.nextLine().toLowerCase();
        
        System.out.print("Course ID: ");
        String courseFilter = scanner.nextLine().toLowerCase();
        
        System.out.print("Minimum enrolled students: ");
        String minStudentsStr = scanner.nextLine();
        Integer minStudents = minStudentsStr.isEmpty() ? null : Integer.parseInt(minStudentsStr);
        
        System.out.print("Online only? (y/n): ");
        String onlineOnly = scanner.nextLine();
        Boolean isOnlineFilter = onlineOnly.isEmpty() ? null : "y".equalsIgnoreCase(onlineOnly);
        
        List<Exam> results = exams.values().stream()
                .filter(exam -> nameFilter.isEmpty() || 
                               exam.getExamName().toLowerCase().contains(nameFilter))
                .filter(exam -> courseFilter.isEmpty() || 
                               exam.getCourseId().toLowerCase().contains(courseFilter))
                .filter(exam -> minStudents == null || exam.getEnrolledCount() >= minStudents)
                .filter(exam -> isOnlineFilter == null || exam.isOnline() == isOnlineFilter)
                .collect(Collectors.toList());
        
        displaySearchResults(results, "advanced search criteria");
    }
    
    private void displaySearchResults(List<Exam> results, String searchCriteria) {
        System.out.println("\n=== SEARCH RESULTS ===");
        System.out.println("Search criteria: " + searchCriteria);
        System.out.println("Results found: " + results.size());
        
        if (results.isEmpty()) {
            System.out.println("No exams found matching the search criteria.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-12s %-10s %-15s %-10s%n", 
                         "ID", "Name", "Course", "Type", "Status", "Students");
        System.out.println("-".repeat(85));
        
        results.forEach(exam -> {
            System.out.printf("%-8s %-25s %-12s %-10s %-15s %-10d%n",
                             exam.getExamId(),
                             exam.getExamName().length() > 23 ? exam.getExamName().substring(0, 23) + ".." : exam.getExamName(),
                             exam.getCourseId(),
                             exam.getExamType().toString().length() > 8 ? exam.getExamType().toString().substring(0, 8) + ".." : exam.getExamType(),
                             exam.getStatus().toString().length() > 13 ? exam.getStatus().toString().substring(0, 13) + ".." : exam.getStatus(),
                             exam.getEnrolledCount());
        });
    }
    
    private void displaySystemStatistics() {
        System.out.println("\n=== EXAM SYSTEM STATISTICS ===");
        
        // Basic statistics
        System.out.println("EXAM OVERVIEW:");
        System.out.println("- Total Exams: " + exams.size());
        System.out.println("- Scheduled: " + exams.values().stream().filter(e -> e.getStatus() == ExamStatus.SCHEDULED).count());
        System.out.println("- Completed: " + exams.values().stream().filter(e -> e.getStatus() == ExamStatus.COMPLETED).count());
        System.out.println("- Upcoming: " + getUpcomingExams().size());
        System.out.println("- Today: " + getTodaysExams().size());
        
        // Enrollment statistics
        int totalEnrollments = exams.values().stream().mapToInt(Exam::getEnrolledCount).sum();
        int totalResults = exams.values().stream().mapToInt(Exam::getResultsCount).sum();
        
        System.out.println("\nENROLLMENT & RESULTS:");
        System.out.println("- Total Enrollments: " + totalEnrollments);
        System.out.println("- Total Results: " + totalResults);
        System.out.println("- Results Completion: " + 
                          String.format("%.1f", totalEnrollments > 0 ? (double) totalResults / totalEnrollments * 100 : 0) + "%");
        
        // Type distribution
        System.out.println("\nEXAM TYPE DISTRIBUTION:");
        Map<ExamType, Long> typeDistribution = exams.values().stream()
                .collect(Collectors.groupingBy(Exam::getExamType, Collectors.counting()));
        
        typeDistribution.entrySet().stream()
                .sorted(Map.Entry.<ExamType, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        
        // Online vs Offline
        long onlineExams = exams.values().stream().filter(Exam::isOnline).count();
        long offlineExams = exams.size() - onlineExams;
        
        System.out.println("\nEXAM MODE:");
        System.out.println("- Online: " + onlineExams);
        System.out.println("- Offline: " + offlineExams);
        
        logger.log("Exam system statistics displayed");
    }
    
    // Getters for external access
    public Map<String, Exam> getAllExams() { return new HashMap<>(exams); }
    public int getTotalExams() { return exams.size(); }
    public int getScheduledExams() { return (int) exams.values().stream().filter(e -> e.getStatus() == ExamStatus.SCHEDULED).count(); }
    public int getCompletedExams() { return (int) exams.values().stream().filter(e -> e.getStatus() == ExamStatus.COMPLETED).count(); }
    public int getTotalEnrollments() { return exams.values().stream().mapToInt(Exam::getEnrolledCount).sum(); }
    public int getTotalResults() { return exams.values().stream().mapToInt(Exam::getResultsCount).sum(); }
}