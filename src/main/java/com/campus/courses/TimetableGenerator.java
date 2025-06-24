package com.campus.courses;

import com.campus.utils.Logger;
import java.util.*;
import java.time.LocalTime;
import java.time.DayOfWeek;

/**
 * Utility class for generating course timetables
 * Demonstrates strategy pattern and algorithm implementation
 */
public class TimetableGenerator {
    private static final Logger logger = Logger.getInstance();
    
    private Map<DayOfWeek, List<TimeSlot>> weeklySchedule;
    private List<String> venues;
    
    public TimetableGenerator() {
        initializeTimeSlots();
        initializeVenues();
    }
    
    private void initializeTimeSlots() {
        weeklySchedule = new HashMap<>();
        
        // Define time slots for each day
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day != DayOfWeek.SUNDAY) { // Assuming Sunday is off
                List<TimeSlot> slots = new ArrayList<>();
                slots.add(new TimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 30)));
                slots.add(new TimeSlot(LocalTime.of(10, 45), LocalTime.of(12, 15)));
                slots.add(new TimeSlot(LocalTime.of(13, 0), LocalTime.of(14, 30)));
                slots.add(new TimeSlot(LocalTime.of(14, 45), LocalTime.of(16, 15)));
                slots.add(new TimeSlot(LocalTime.of(16, 30), LocalTime.of(18, 0)));
                weeklySchedule.put(day, slots);
            }
        }
    }
    
    private void initializeVenues() {
        venues = Arrays.asList(
            "Room A101", "Room A102", "Room A103", "Room B101", "Room B102",
            "Lab L101", "Lab L102", "Auditorium Main", "Seminar Hall 1", "Seminar Hall 2"
        );
    }
    
    public void generateTimetable(Collection<Course> courses, String department, String semester) {
        System.out.println("\n=== TIMETABLE GENERATION ===");
        System.out.println("Department: " + department);
        System.out.println("Semester: " + semester);
        System.out.println("-".repeat(80));
        
        List<Course> filteredCourses = courses.stream()
                .filter(course -> "ALL".equalsIgnoreCase(department) || 
                                course.getDepartment().equalsIgnoreCase(department))
                .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        if (filteredCourses.isEmpty()) {
            System.out.println("No active courses found for the specified criteria.");
            return;
        }
        
        Map<DayOfWeek, Map<TimeSlot, CourseSchedule>> timetable = new HashMap<>();
        Random random = new Random();
        
        // Initialize timetable structure
        for (DayOfWeek day : weeklySchedule.keySet()) {
            timetable.put(day, new HashMap<>());
        }
        
        // Assign courses to time slots
        for (Course course : filteredCourses) {
            assignCourseToTimetable(course, timetable, random);
        }
        
        // Display timetable
        displayTimetable(timetable);
        
        logger.log("Timetable generated for " + filteredCourses.size() + " courses");
    }
    
    private void assignCourseToTimetable(Course course, Map<DayOfWeek, Map<TimeSlot, CourseSchedule>> timetable, Random random) {
        int sessionsPerWeek = calculateSessionsPerWeek(course);
        int assignedSessions = 0;
        
        List<DayOfWeek> availableDays = new ArrayList<>(weeklySchedule.keySet());
        Collections.shuffle(availableDays, random);
        
        for (DayOfWeek day : availableDays) {
            if (assignedSessions >= sessionsPerWeek) break;
            
            List<TimeSlot> availableSlots = new ArrayList<>(weeklySchedule.get(day));
            Collections.shuffle(availableSlots, random);
            
            for (TimeSlot slot : availableSlots) {
                if (assignedSessions >= sessionsPerWeek) break;
                
                if (!timetable.get(day).containsKey(slot)) {
                    String venue = venues.get(random.nextInt(venues.size()));
                    CourseSchedule schedule = new CourseSchedule(course, day, slot, venue);
                    timetable.get(day).put(slot, schedule);
                    assignedSessions++;
                }
            }
        }
    }
    
    private int calculateSessionsPerWeek(Course course) {
        // Calculate based on course type and credits
        switch (course.getCourseType()) {
            case LAB:
                return Math.min(course.getCredits(), 2);
            case SEMINAR:
            case WORKSHOP:
                return 1;
            default:
                return Math.min(course.getCredits(), 3);
        }
    }
    
    private void displayTimetable(Map<DayOfWeek, Map<TimeSlot, CourseSchedule>> timetable) {
        System.out.printf("%-12s", "Time/Day");
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day != DayOfWeek.SUNDAY) {
                System.out.printf("%-20s", day.toString().substring(0, 3));
            }
        }
        System.out.println();
        System.out.println("-".repeat(140));
        
        List<TimeSlot> allSlots = weeklySchedule.get(DayOfWeek.MONDAY);
        for (TimeSlot slot : allSlots) {
            System.out.printf("%-12s", slot.getStartTime() + "-" + slot.getEndTime());
            
            for (DayOfWeek day : DayOfWeek.values()) {
                if (day != DayOfWeek.SUNDAY) {
                    CourseSchedule schedule = timetable.get(day).get(slot);
                    if (schedule != null) {
                        String courseInfo = schedule.getCourse().getCourseCode() + 
                                          " (" + schedule.getVenue() + ")";
                        System.out.printf("%-20s", courseInfo.length() > 18 ? 
                                        courseInfo.substring(0, 18) : courseInfo);
                    } else {
                        System.out.printf("%-20s", "");
                    }
                }
            }
            System.out.println();
        }
    }
    
    // Inner classes
    public static class TimeSlot {
        private LocalTime startTime;
        private LocalTime endTime;
        
        public TimeSlot(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeSlot timeSlot = (TimeSlot) o;
            return Objects.equals(startTime, timeSlot.startTime) && Objects.equals(endTime, timeSlot.endTime);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(startTime, endTime);
        }
    }
    
    public static class CourseSchedule {
        private Course course;
        private DayOfWeek day;
        private TimeSlot timeSlot;
        private String venue;
        
        public CourseSchedule(Course course, DayOfWeek day, TimeSlot timeSlot, String venue) {
            this.course = course;
            this.day = day;
            this.timeSlot = timeSlot;
            this.venue = venue;
        }
        
        public Course getCourse() { return course; }
        public DayOfWeek getDay() { return day; }
        public TimeSlot getTimeSlot() { return timeSlot; }
        public String getVenue() { return venue; }
    }
}