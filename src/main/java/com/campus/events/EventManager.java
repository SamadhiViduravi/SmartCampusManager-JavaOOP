package com.campus.events;

import com.campus.utils.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Manager class for handling all event operations
 */
public class EventManager implements Manageable<Event> {
    private static final Logger logger = Logger.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    
    private Map<String, Event> events;
    private NotificationService notificationService;
    private int eventCounter;
    
    public EventManager() {
        this.events = new HashMap<>();
        this.notificationService = NotificationService.getInstance();
        this.eventCounter = 1;
        initializeSampleData();
        logger.log("EventManager initialized");
    }
    
    private void initializeSampleData() {
        // Create sample events
        Event techConf = new Event("EV001", "Annual Tech Conference", EventType.CONFERENCE, EventCategory.TECHNICAL);
        techConf.setDescription("Annual technology conference featuring latest innovations in AI and Machine Learning");
        techConf.scheduleEvent(LocalDate.now().plusDays(30), LocalTime.of(9, 0), LocalTime.of(17, 0), "Main Auditorium");
        techConf.setOrganizerId("T001");
        techConf.setOrganizerName("Dr. Smith");
        techConf.addSpeaker("Prof. Johnson - AI Expert");
        techConf.addSpeaker("Dr. Williams - ML Researcher");
        techConf.addSponsor("TechCorp Inc.");
        techConf.addSponsor("Innovation Labs");
        techConf.setContactEmail("techconf@campus.edu");
        techConf.setContactPhone("+1-555-0123");
        techConf.setMaxCapacity(200);
        techConf.setRegistrationFee(75.0);
        events.put("EV001", techConf);
        
        Event culturalNight = new Event("EV002", "International Cultural Night", EventType.CULTURAL, EventCategory.CULTURAL);
        culturalNight.setDescription("Celebrate diversity with performances from around the world");
        culturalNight.scheduleEvent(LocalDate.now().plusDays(15), LocalTime.of(18, 0), LocalTime.of(22, 0), "Campus Plaza");
        culturalNight.setOrganizerId("S001");
        culturalNight.setOrganizerName("Student Council");
        culturalNight.setPublic(true);
        culturalNight.setRequiresRegistration(false);
        culturalNight.setMaxCapacity(500);
        culturalNight.addTag("Multicultural");
        culturalNight.addTag("Performance");
        culturalNight.addTag("Food");
        events.put("EV002", culturalNight);
        
        Event sportsDay = new Event("EV003", "Annual Sports Day", EventType.SPORTS, EventCategory.SPORTS);
        sportsDay.setDescription("Inter-department sports competition");
        sportsDay.scheduleEvent(LocalDate.now().plusDays(45), LocalTime.of(8, 0), LocalTime.of(18, 0), "Sports Complex");
        sportsDay.setOrganizerId("SP001");
        sportsDay.setOrganizerName("Sports Department");
        sportsDay.setPublic(true);
        sportsDay.setRequiresRegistration(true);
        sportsDay.setRegistrationFee(10.0);
        sportsDay.setMaxCapacity(1000);
        sportsDay.addSponsor("SportsCorp");
        events.put("EV003", sportsDay);
        
        Event workshop = new Event("EV004", "Web Development Workshop", EventType.WORKSHOP, EventCategory.TECHNICAL);
        workshop.setDescription("Hands-on workshop on modern web development technologies");
        workshop.scheduleEvent(LocalDate.now().plusDays(10), LocalTime.of(14, 0), LocalTime.of(17, 0), "Computer Lab 1");
        workshop.setOrganizerId("T002");
        workshop.setOrganizerName("Prof. Davis");
        workshop.addSpeaker("John Doe - Full Stack Developer");
        workshop.setMaxCapacity(30);
        workshop.setRegistrationFee(25.0);
        workshop.setContactEmail("webdev@campus.edu");
        events.put("EV004", workshop);
        
        Event graduation = new Event("EV005", "Graduation Ceremony 2024", EventType.GRADUATION, EventCategory.ACADEMIC);
        graduation.setDescription("Annual graduation ceremony for Class of 2024");
        graduation.scheduleEvent(LocalDate.now().plusDays(60), LocalTime.of(10, 0), LocalTime.of(12, 0), "Main Stadium");
        graduation.setOrganizerId("ADMIN001");
        graduation.setOrganizerName("Academic Office");
        graduation.setPublic(false);
        graduation.setRequiresRegistration(true);
        graduation.setMaxCapacity(2000);
        graduation.addSpeaker("Chancellor Dr. Brown");
        graduation.addSpeaker("Valedictorian Speech");
        events.put("EV005", graduation);
        
        // Add some registrations and attendance to sample events
        techConf.registerParticipant("S001");
        techConf.registerParticipant("S002");
        techConf.registerParticipant("S003");
        techConf.registerParticipant("F001");
        techConf.registerParticipant("F002");
        
        workshop.registerParticipant("S004");
        workshop.registerParticipant("S005");
        workshop.registerParticipant("S006");
        
        // Create a completed event with attendance
        Event completedEvent = new Event("EV006", "Orientation Session", EventType.ORIENTATION, EventCategory.ACADEMIC);
        completedEvent.setDescription("New student orientation session");
        completedEvent.scheduleEvent(LocalDate.now().minusDays(7), LocalTime.of(9, 0), LocalTime.of(12, 0), "Lecture Hall A");
        completedEvent.setOrganizerId("ADMIN002");
        completedEvent.setOrganizerName("Student Affairs");
        completedEvent.setMaxCapacity(150);
        completedEvent.registerParticipant("S007");
        completedEvent.registerParticipant("S008");
        completedEvent.registerParticipant("S009");
        completedEvent.registerParticipant("S010");
        completedEvent.registerParticipant("S011");
        completedEvent.startEvent();
        completedEvent.completeEvent();
        completedEvent.markAttendance("S007");
        completedEvent.markAttendance("S008");
        completedEvent.markAttendance("S009");
        completedEvent.markAttendance("S010");
        events.put("EV006", completedEvent);
        
        logger.log("Sample event data initialized");
    }
    
    public void displayMenu() {
        while (true) {
            System.out.println("\n=== EVENT MANAGEMENT MENU ===");
            System.out.println("1. Event Management");
            System.out.println("2. Registration Management");
            System.out.println("3. Attendance Management");
            System.out.println("4. Schedule Management");
            System.out.println("5. Reports & Analytics");
            System.out.println("6. Notifications & Alerts");
            System.out.println("7. Search & Filter");
            System.out.println("8. System Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = getChoice();
            
            switch (choice) {
                case 1: eventManagementMenu(); break;
                case 2: registrationManagementMenu(); break;
                case 3: attendanceManagementMenu(); break;
                case 4: scheduleManagementMenu(); break;
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
    public void create(Event event) {
        events.put(event.getEventId(), event);
        notificationService.notifyObservers("New event created: " + event.getEventName());
        logger.log("Event created: " + event.getEventId());
    }
    
    @Override
    public Event read(String eventId) {
        return events.get(eventId);
    }
    
    @Override
    public void update(Event event) {
        events.put(event.getEventId(), event);
        notificationService.notifyObservers("Event updated: " + event.getEventName());
        logger.log("Event updated: " + event.getEventId());
    }
    
    @Override
    public void delete(String eventId) {
        Event event = events.remove(eventId);
        if (event != null) {
            notificationService.notifyObservers("Event deleted: " + event.getEventName());
            logger.log("Event deleted: " + eventId);
        }
    }
    
    @Override
    public List<Event> getAll() {
        return new ArrayList<>(events.values());
    }
    
    private void eventManagementMenu() {
        System.out.println("\n=== EVENT MANAGEMENT ===");
        System.out.println("1. Create New Event");
        System.out.println("2. View All Events");
        System.out.println("3. View Event Details");
        System.out.println("4. Update Event");
        System.out.println("5. Delete Event");
        System.out.println("6. Cancel Event");
        System.out.println("7. Postpone Event");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: createEventInteractive(); break;
            case 2: viewAllEvents(); break;
            case 3: viewEventDetailsInteractive(); break;
            case 4: updateEventInteractive(); break;
            case 5: deleteEventInteractive(); break;
            case 6: cancelEventInteractive(); break;
            case 7: postponeEventInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void createEventInteractive() {
        System.out.println("\n=== CREATE NEW EVENT ===");
        
        String eventId = "EV" + String.format("%03d", eventCounter++);
        
        System.out.print("Enter Event Name: ");
        String eventName = scanner.nextLine();
        
        System.out.println("Select Event Type:");
        EventType[] types = EventType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        int typeChoice = getChoice();
        
        if (typeChoice < 1 || typeChoice > types.length) {
            System.out.println("Invalid event type choice.");
            return;
        }
        
        EventType eventType = types[typeChoice - 1];
        
        System.out.println("Select Event Category:");
        EventCategory[] categories = EventCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Enter choice: ");
        int categoryChoice = getChoice();
        
        if (categoryChoice < 1 || categoryChoice > categories.length) {
            System.out.println("Invalid category choice.");
            return;
        }
        
        EventCategory category = categories[categoryChoice - 1];
        
        Event newEvent = new Event(eventId, eventName, eventType, category);
        
        // Optional fields
        System.out.print("Enter Description (optional): ");
        String description = scanner.nextLine();
        if (!description.trim().isEmpty()) {
            newEvent.setDescription(description);
        }
        
        System.out.print("Enter Organizer ID: ");
        String organizerId = scanner.nextLine();
        if (!organizerId.trim().isEmpty()) {
            newEvent.setOrganizerId(organizerId);
            System.out.print("Enter Organizer Name: ");
            newEvent.setOrganizerName(scanner.nextLine());
        }
        
        System.out.print("Schedule event now? (y/n): ");
        String scheduleNow = scanner.nextLine();
        
        if ("y".equalsIgnoreCase(scheduleNow)) {
            scheduleEventInteractive(newEvent);
        }
        
        create(newEvent);
        System.out.println("Event created successfully!");
        newEvent.displayEventInfo();
    }
    
    private void scheduleEventInteractive(Event event) {
        System.out.println("\n=== SCHEDULE EVENT ===");
        
        System.out.print("Enter event date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        
        System.out.print("Enter start time (HH:MM): ");
        String startTimeStr = scanner.nextLine();
        
        System.out.print("Enter end time (HH:MM): ");
        String endTimeStr = scanner.nextLine();
        
        System.out.print("Enter venue: ");
        String venue = scanner.nextLine();
        
        try {
            LocalDate eventDate = LocalDate.parse(dateStr);
            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);
            
            event.scheduleEvent(eventDate, startTime, endTime, venue);
            System.out.println("Event scheduled successfully!");
            
        } catch (Exception e) {
            System.out.println("Invalid date/time format. Please use YYYY-MM-DD and HH:MM");
        }
    }
    
    private void viewAllEvents() {
        System.out.println("\n=== ALL EVENTS ===");
        if (events.isEmpty()) {
            System.out.println("No events found.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-12s %-12s %-12s %-15s %-8s%n", 
                         "ID", "Name", "Type", "Date", "Time", "Status", "Capacity");
        System.out.println("-".repeat(95));
        
        events.values().stream()
            .sorted((e1, e2) -> {
                if (e1.getEventDate() == null && e2.getEventDate() == null) return 0;
                if (e1.getEventDate() == null) return 1;
                if (e2.getEventDate() == null) return -1;
                return e1.getEventDate().compareTo(e2.getEventDate());
            })
            .forEach(event -> {
                System.out.printf("%-8s %-25s %-12s %-12s %-12s %-15s %-8s%n",
                                 event.getEventId(),
                                 event.getEventName().length() > 23 ? event.getEventName().substring(0, 23) + ".." : event.getEventName(),
                                 event.getEventType().toString().length() > 10 ? event.getEventType().toString().substring(0, 10) + ".." : event.getEventType(),
                                 event.getEventDate() != null ? event.getEventDate().toString() : "Not scheduled",
                                 event.getStartTime() != null ? event.getStartTime().toString() : "TBD",
                                 event.getStatus().toString().length() > 13 ? event.getStatus().toString().substring(0, 13) + ".." : event.getStatus(),
                                 event.getCurrentAttendees() + "/" + event.getMaxCapacity());
            });
        
        System.out.println("-".repeat(95));
        System.out.println("Total Events: " + events.size());
        System.out.println("Scheduled: " + events.values().stream().filter(e -> e.getStatus() == EventStatus.SCHEDULED).count());
        System.out.println("Completed: " + events.values().stream().filter(e -> e.getStatus() == EventStatus.COMPLETED).count());
        System.out.println("Upcoming: " + events.values().stream().filter(Event::isUpcoming).count());
    }
    
    private void viewEventDetailsInteractive() {
        System.out.println("\n=== VIEW EVENT DETAILS ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event != null) {
            event.displayEventInfo();
            
            if (event.getRegisteredParticipants().size() > 0 || event.getAttendees().size() > 0) {
                System.out.print("\nView attendance details? (y/n): ");
                String viewAttendance = scanner.nextLine();
                if ("y".equalsIgnoreCase(viewAttendance)) {
                    event.displayAttendanceList();
                }
            }
        } else {
            System.out.println("Event not found.");
        }
    }
    
    // Additional methods for registration, attendance, reports, etc.
    private void registrationManagementMenu() {
        System.out.println("\n=== REGISTRATION MANAGEMENT ===");
        System.out.println("1. Register Participant");
        System.out.println("2. Unregister Participant");
        System.out.println("3. View Event Registrations");
        System.out.println("4. View Participant Events");
        System.out.println("5. Bulk Registration");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: registerParticipantInteractive(); break;
            case 2: unregisterParticipantInteractive(); break;
            case 3: viewEventRegistrationsInteractive(); break;
            case 4: viewParticipantEventsInteractive(); break;
            case 5: bulkRegistrationInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void registerParticipantInteractive() {
        System.out.println("\n=== REGISTER PARTICIPANT ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        
        if (!event.requiresRegistration()) {
            System.out.println("This event does not require registration.");
            return;
        }
        
        System.out.print("Enter Participant ID: ");
        String participantId = scanner.nextLine();
        
        try {
            boolean success = event.registerParticipant(participantId);
            if (success) {
                update(event);
                System.out.println("Participant registered successfully!");
                System.out.println("Registration fee: $" + String.format("%.2f", event.getRegistrationFee()));
                System.out.println("Available spots: " + event.getAvailableSpots());
            } else {
                System.out.println("Registration failed. Event may be full or participant already registered.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void unregisterParticipantInteractive() {
        System.out.println("\n=== UNREGISTER PARTICIPANT ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        
        System.out.print("Enter Participant ID: ");
        String participantId = scanner.nextLine();
        
        event.unregisterParticipant(participantId);
        update(event);
        System.out.println("Participant unregistered successfully!");
    }
    
    private void viewEventRegistrationsInteractive() {
        System.out.println("\n=== VIEW EVENT REGISTRATIONS ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        
        System.out.println("Event: " + event.getEventName());
        System.out.println("Registration Required: " + (event.requiresRegistration() ? "Yes" : "No"));
        
        if (!event.requiresRegistration()) {
            System.out.println("This event does not require registration.");
            return;
        }
        
        System.out.println("Registered Participants: " + event.getRegisteredParticipants().size());
        System.out.println("Max Capacity: " + event.getMaxCapacity());
        System.out.println("Available Spots: " + event.getAvailableSpots());
        System.out.println("Registration Rate: " + String.format("%.1f", event.getRegistrationRate()) + "%");
        
        if (event.getRegisteredParticipants().isEmpty()) {
            System.out.println("No participants registered yet.");
            return;
        }
        
        System.out.println("\nRegistered Participants:");
        for (int i = 0; i < event.getRegisteredParticipants().size(); i++) {
            String participantId = event.getRegisteredParticipants().get(i);
            boolean attended = event.getAttendees().contains(participantId);
            String status = event.getStatus() == EventStatus.COMPLETED ? 
                           (attended ? " (Attended)" : " (No-show)") : " (Registered)";
            System.out.println((i + 1) + ". " + participantId + status);
        }
    }
    
    private void viewParticipantEventsInteractive() {
        System.out.println("\n=== VIEW PARTICIPANT EVENTS ===");
        System.out.print("Enter Participant ID: ");
        String participantId = scanner.nextLine();
        
        List<Event> participantEvents = events.values().stream()
                .filter(event -> event.getRegisteredParticipants().contains(participantId) ||
                                event.getAttendees().contains(participantId))
                .sorted((e1, e2) -> {
                    if (e1.getEventDate() == null && e2.getEventDate() == null) return 0;
                    if (e1.getEventDate() == null) return 1;
                    if (e2.getEventDate() == null) return -1;
                    return e1.getEventDate().compareTo(e2.getEventDate());
                })
                .collect(Collectors.toList());
        
        if (participantEvents.isEmpty()) {
            System.out.println("No events found for participant: " + participantId);
            return;
        }
        
        System.out.println("Events for Participant: " + participantId);
        System.out.printf("%-8s %-25s %-12s %-10s %-15s %-10s%n", 
                         "ID", "Name", "Date", "Time", "Status", "Attendance");
        System.out.println("-".repeat(85));
        
        participantEvents.forEach(event -> {
            boolean registered = event.getRegisteredParticipants().contains(participantId);
            boolean attended = event.getAttendees().contains(participantId);
            String attendanceStatus = registered ? (attended ? "Attended" : "Registered") : 
                                    (attended ? "Walk-in" : "Unknown");
            
            System.out.printf("%-8s %-25s %-12s %-10s %-15s %-10s%n",
                             event.getEventId(),
                             event.getEventName().length() > 23 ? event.getEventName().substring(0, 23) + ".." : event.getEventName(),
                             event.getEventDate() != null ? event.getEventDate().toString() : "TBD",
                             event.getStartTime() != null ? event.getStartTime().toString() : "TBD",
                             event.getStatus().toString().length() > 13 ? event.getStatus().toString().substring(0, 13) + ".." : event.getStatus(),
                             attendanceStatus);
        });
    }
    
    private void bulkRegistrationInteractive() {
        System.out.println("\n=== BULK REGISTRATION ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        
        if (!event.requiresRegistration()) {
            System.out.println("This event does not require registration.");
            return;
        }
        
        System.out.println("Enter Participant IDs (comma-separated): ");
        String participantIds = scanner.nextLine();
        
        String[] ids = participantIds.split(",");
        int registered = 0;
        int failed = 0;
        
        for (String id : ids) {
            String participantId = id.trim();
            if (!participantId.isEmpty()) {
                try {
                    boolean success = event.registerParticipant(participantId);
                    if (success) {
                        registered++;
                    } else {
                        failed++;
                        System.out.println("Failed to register " + participantId + " (may be full or already registered)");
                    }
                } catch (Exception e) {
                    failed++;
                    System.out.println("Failed to register " + participantId + ": " + e.getMessage());
                }
            }
        }
        
        update(event);
        System.out.println("Bulk registration completed.");
        System.out.println("Successfully registered: " + registered);
        System.out.println("Failed: " + failed);
        System.out.println("Available spots remaining: " + event.getAvailableSpots());
    }
    
    private void attendanceManagementMenu() {
        System.out.println("\n=== ATTENDANCE MANAGEMENT ===");
        System.out.println("1. Mark Attendance");
        System.out.println("2. Remove Attendance");
        System.out.println("3. View Event Attendance");
        System.out.println("4. Bulk Attendance");
        System.out.println("5. Generate Attendance Report");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: markAttendanceInteractive(); break;
            case 2: removeAttendanceInteractive(); break;
            case 3: viewEventAttendanceInteractive(); break;
            case 4: bulkAttendanceInteractive(); break;
            case 5: generateAttendanceReportInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void markAttendanceInteractive() {
        System.out.println("\n=== MARK ATTENDANCE ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        
        if (event.getStatus() != EventStatus.IN_PROGRESS && event.getStatus() != EventStatus.COMPLETED) {
            System.out.println("Can only mark attendance for ongoing or completed events.");
            return;
        }
        
        System.out.print("Enter Participant ID: ");
        String participantId = scanner.nextLine();
        
        try {
            boolean success = event.markAttendance(participantId);
            if (success) {
                update(event);
                System.out.println("Attendance marked successfully!");
                System.out.println("Total attendees: " + event.getCurrentAttendees());
                
                if (event.requiresRegistration()) {
                    boolean wasRegistered = event.getRegisteredParticipants().contains(participantId);
                    System.out.println("Registration status: " + (wasRegistered ? "Registered" : "Walk-in"));
                }
            } else {
                System.out.println("Attendance already marked for this participant.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void removeAttendanceInteractive() {
        System.out.println("\n=== REMOVE ATTENDANCE ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        
        System.out.print("Enter Participant ID: ");
        String participantId = scanner.nextLine();
        
        event.removeAttendance(participantId);
        update(event);
        System.out.println("Attendance removed successfully!");
        System.out.println("Total attendees: " + event.getCurrentAttendees());
    }
    
    private void viewEventAttendanceInteractive() {
        System.out.println("\n=== VIEW EVENT ATTENDANCE ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event != null) {
            event.displayAttendanceList();
        } else {
            System.out.println("Event not found.");
        }
    }
    
    private void bulkAttendanceInteractive() {
        System.out.println("\n=== BULK ATTENDANCE ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        
        if (event.getStatus() != EventStatus.IN_PROGRESS && event.getStatus() != EventStatus.COMPLETED) {
            System.out.println("Can only mark attendance for ongoing or completed events.");
            return;
        }
        
        System.out.println("Enter Participant IDs (comma-separated): ");
        String participantIds = scanner.nextLine();
        
        String[] ids = participantIds.split(",");
        int marked = 0;
        int failed = 0;
        
        for (String id : ids) {
            String participantId = id.trim();
            if (!participantId.isEmpty()) {
                try {
                    boolean success = event.markAttendance(participantId);
                    if (success) {
                        marked++;
                    } else {
                        failed++;
                        System.out.println("Attendance already marked for " + participantId);
                    }
                } catch (Exception e) {
                    failed++;
                    System.out.println("Failed to mark attendance for " + participantId + ": " + e.getMessage());
                }
            }
        }
        
        update(event);
        System.out.println("Bulk attendance completed.");
        System.out.println("Successfully marked: " + marked);
        System.out.println("Failed/Already marked: " + failed);
        System.out.println("Total attendees: " + event.getCurrentAttendees());
    }
    
    private void generateAttendanceReportInteractive() {
        System.out.println("\n=== GENERATE ATTENDANCE REPORT ===");
        System.out.print("Enter Event ID: ");
        String eventId = scanner.nextLine();
        
        Event event = read(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            return;
        }
        
        System.out.println("=== ATTENDANCE REPORT ===");
        System.out.println("Event: " + event.getEventName());
        System.out.println("Date: " + event.getEventDate());
        System.out.println("Time: " + event.getStartTime() + " - " + event.getEndTime());
        System.out.println("Venue: " + event.getVenue());
        System.out.println("Status: " + event.getStatus());
        
        if (event.requiresRegistration()) {
            System.out.println("\nRegistration Summary:");
            System.out.println("- Total Registered: " + event.getRegisteredParticipants().size());
            System.out.println("- Registration Rate: " + String.format("%.1f", event.getRegistrationRate()) + "%");
        }
        
        System.out.println("\nAttendance Summary:");
        System.out.println("- Total Attendees: " + event.getCurrentAttendees());
        System.out.println("- Max Capacity: " + event.getMaxCapacity());
        System.out.println("- Capacity Utilization: " + 
                          String.format("%.1f", (double) event.getCurrentAttendees() / event.getMaxCapacity() * 100) + "%");
        
        if (event.requiresRegistration() && event.getStatus() == EventStatus.COMPLETED) {
            System.out.println("- Attendance Rate: " + String.format("%.1f", event.getAttendanceRate()) + "%");
            
            List<String> noShows = new ArrayList<>(event.getRegisteredParticipants());
            noShows.removeAll(event.getAttendees());
            System.out.println("- No-shows: " + noShows.size());
            
            List<String> walkIns = new ArrayList<>(event.getAttendees());
            walkIns.removeAll(event.getRegisteredParticipants());
            System.out.println("- Walk-ins: " + walkIns.size());
        }
        
        event.displayAttendanceList();
        
        logger.log("Attendance report generated for event: " + eventId);
    }
    
    // Utility methods
    public List<Event> getUpcomingEvents() {
        return events.values().stream()
                .filter(Event::isUpcoming)
                .collect(Collectors.toList());
    }
    
    public List<Event> getTodaysEvents() {
        return events.values().stream()
                .filter(Event::isToday)
                .collect(Collectors.toList());
    }
    
    public List<Event> getEventsByStatus(EventStatus status) {
        return events.values().stream()
                .filter(event -> event.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    public List<Event> getEventsByType(EventType type) {
        return events.values().stream()
                .filter(event -> event.getEventType() == type)
                .collect(Collectors.toList());
    }
    
    public List<Event> getEventsByCategory(EventCategory category) {
        return events.values().stream()
                .filter(event -> event.getCategory() == category)
                .collect(Collectors.toList());
    }
    
    private void reportsMenu() {
        System.out.println("\n=== REPORTS & ANALYTICS ===");
        System.out.println("1. Event Summary Report");
        System.out.println("2. Attendance Analysis");
        System.out.println("3. Registration Statistics");
        System.out.println("4. Category-wise Report");
        System.out.println("5. Monthly Event Report");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: generateEventSummaryReport(); break;
            case 2: generateAttendanceAnalysis(); break;
            case 3: generateRegistrationStatistics(); break;
            case 4: generateCategoryWiseReport(); break;
            case 5: generateMonthlyEventReport(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void generateEventSummaryReport() {
        System.out.println("\n=== EVENT SUMMARY REPORT ===");
        
        int totalEvents = events.size();
        long scheduledEvents = events.values().stream().filter(e -> e.getStatus() == EventStatus.SCHEDULED).count();
        long completedEvents = events.values().stream().filter(e -> e.getStatus() == EventStatus.COMPLETED).count();
        long upcomingEvents = events.values().stream().filter(Event::isUpcoming).count();
        
        System.out.println("Total Events: " + totalEvents);
        System.out.println("Scheduled: " + scheduledEvents);
        System.out.println("Completed: " + completedEvents);
        System.out.println("Upcoming: " + upcomingEvents);
        
        // Event type distribution
        System.out.println("\nEvent Type Distribution:");
        Map<EventType, Long> typeDistribution = events.values().stream()
                .collect(Collectors.groupingBy(Event::getEventType, Collectors.counting()));
        
        typeDistribution.entrySet().stream()
                .sorted(Map.Entry.<EventType, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        
        // Total registrations and attendance
        int totalRegistrations = events.values().stream()
                .mapToInt(event -> event.getRegisteredParticipants().size())
                .sum();
        
        int totalAttendance = events.values().stream()
                .mapToInt(Event::getCurrentAttendees)
                .sum();
        
        System.out.println("\nTotal Registrations: " + totalRegistrations);
        System.out.println("Total Attendance: " + totalAttendance);
        
        logger.log("Event summary report generated");
    }
    
    private void generateAttendanceAnalysis() {
        System.out.println("\n=== ATTENDANCE ANALYSIS ===");
        
        List<Event> completedEvents = events.values().stream()
                .filter(event -> event.getStatus() == EventStatus.COMPLETED)
                .collect(Collectors.toList());
        
        if (completedEvents.isEmpty()) {
            System.out.println("No completed events available for analysis.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-10s %-10s %-12s %-12s%n", 
                         "Event ID", "Name", "Registered", "Attended", "Attendance%", "Capacity%");
        System.out.println("-".repeat(80));
        
        completedEvents.forEach(event -> {
            double attendanceRate = event.getAttendanceRate();
            double capacityUtilization = (double) event.getCurrentAttendees() / event.getMaxCapacity() * 100;
            
            System.out.printf("%-8s %-25s %-10d %-10d %-12.1f %-12.1f%n",
                             event.getEventId(),
                             event.getEventName().length() > 23 ? event.getEventName().substring(0, 23) + ".." : event.getEventName(),
                             event.getRegisteredParticipants().size(),
                             event.getCurrentAttendees(),
                             attendanceRate,
                             capacityUtilization);
        });
        
        // Overall statistics
        double avgAttendanceRate = completedEvents.stream()
                .filter(event -> event.requiresRegistration())
                .mapToDouble(Event::getAttendanceRate)
                .average()
                .orElse(0.0);
        
        double avgCapacityUtilization = completedEvents.stream()
                .mapToDouble(event -> (double) event.getCurrentAttendees() / event.getMaxCapacity() * 100)
                .average()
                .orElse(0.0);
        
        System.out.println("-".repeat(80));
        System.out.println("Average Attendance Rate: " + String.format("%.1f", avgAttendanceRate) + "%");
        System.out.println("Average Capacity Utilization: " + String.format("%.1f", avgCapacityUtilization) + "%");
        
        logger.log("Attendance analysis report generated");
    }
    
    private void generateRegistrationStatistics() {
        System.out.println("\n=== REGISTRATION STATISTICS ===");
        
        List<Event> registrationEvents = events.values().stream()
                .filter(Event::requiresRegistration)
                .collect(Collectors.toList());
        
        if (registrationEvents.isEmpty()) {
            System.out.println("No events with registration found.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-8s %-8s %-12s %-8s%n", 
                         "Event ID", "Name", "Capacity", "Registered", "Rate%", "Fee");
        System.out.println("-".repeat(75));
        
        registrationEvents.forEach(event -> {
            System.out.printf("%-8s %-25s %-8d %-8d %-12.1f $%-7.2f%n",
                             event.getEventId(),
                             event.getEventName().length() > 23 ? event.getEventName().substring(0, 23) + ".." : event.getEventName(),
                             event.getMaxCapacity(),
                             event.getRegisteredParticipants().size(),
                             event.getRegistrationRate(),
                             event.getRegistrationFee());
        });
        
        // Summary statistics
        int totalCapacity = registrationEvents.stream().mapToInt(Event::getMaxCapacity).sum();
        int totalRegistrations = registrationEvents.stream()
                .mapToInt(event -> event.getRegisteredParticipants().size()).sum();
        double totalRevenue = registrationEvents.stream()
                .mapToDouble(event -> event.getRegisteredParticipants().size() * event.getRegistrationFee()).sum();
        
        System.out.println("-".repeat(75));
        System.out.println("Total Capacity: " + totalCapacity);
        System.out.println("Total Registrations: " + totalRegistrations);
        System.out.println("Overall Registration Rate: " + 
                          String.format("%.1f", (double) totalRegistrations / totalCapacity * 100) + "%");
        System.out.println("Total Registration Revenue: $" + String.format("%.2f", totalRevenue));
        
        logger.log("Registration statistics report generated");
    }
    
    private void generateCategoryWiseReport() {
        System.out.println("\n=== CATEGORY-WISE REPORT ===");
        
        Map<EventCategory, List<Event>> eventsByCategory = events.values().stream()
                .collect(Collectors.groupingBy(Event::getCategory));
        
        System.out.printf("%-15s %-8s %-10s %-10s %-12s%n", 
                         "Category", "Events", "Total Reg", "Total Att", "Avg Att Rate");
        System.out.println("-".repeat(60));
        
        eventsByCategory.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    EventCategory category = entry.getKey();
                    List<Event> categoryEvents = entry.getValue();
                    
                    int totalEvents = categoryEvents.size();
                    int totalRegistrations = categoryEvents.stream()
                            .mapToInt(event -> event.getRegisteredParticipants().size()).sum();
                    int totalAttendance = categoryEvents.stream()
                            .mapToInt(Event::getCurrentAttendees).sum();
                    
                    double avgAttendanceRate = categoryEvents.stream()
                            .filter(Event::requiresRegistration)
                            .filter(event -> event.getStatus() == EventStatus.COMPLETED)
                            .mapToDouble(Event::getAttendanceRate)
                            .average()
                            .orElse(0.0);
                    
                    System.out.printf("%-15s %-8d %-10d %-10d %-12.1f%n",
                                     category.toString().length() > 13 ? category.toString().substring(0, 13) + ".." : category,
                                     totalEvents,
                                     totalRegistrations,
                                     totalAttendance,
                                     avgAttendanceRate);
                });
        
        logger.log("Category-wise report generated");
    }
    
    private void generateMonthlyEventReport() {
        System.out.println("\n=== MONTHLY EVENT REPORT ===");
        System.out.println("Events scheduled for next 30 days:");
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        
        Map<LocalDate, List<Event>> eventsByDate = events.values().stream()
                .filter(event -> event.getEventDate() != null && 
                               !event.getEventDate().isBefore(startDate) && 
                               !event.getEventDate().isAfter(endDate))
                .collect(Collectors.groupingBy(Event::getEventDate));
        
        if (eventsByDate.isEmpty()) {
            System.out.println("No events scheduled in the next 30 days.");
            return;
        }
        
        eventsByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.println("\n" + entry.getKey().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
                    System.out.println("-".repeat(50));
                    entry.getValue().stream()
                            .sorted((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()))
                            .forEach(event -> {
                                System.out.printf("  %s - %s (%s) - %s - %d/%d%n",
                                                 event.getStartTime(),
                                                 event.getEventName(),
                                                 event.getEventType(),
                                                 event.getVenue(),
                                                 event.getCurrentAttendees(),
                                                 event.getMaxCapacity());
                            });
                });
        
        logger.log("Monthly event report generated");
    }
    
    private void notificationsMenu() {
        System.out.println("\n=== NOTIFICATIONS & ALERTS ===");
        System.out.println("1. Upcoming Event Alerts");
        System.out.println("2. Today's Event Alerts");
        System.out.println("3. Registration Deadline Alerts");
        System.out.println("4. Capacity Alerts");
        System.out.println("5. All Active Alerts");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: showUpcomingEventAlerts(); break;
            case 2: showTodaysEventAlerts(); break;
            case 3: showRegistrationDeadlineAlerts(); break;
            case 4: showCapacityAlerts(); break;
            case 5: showAllActiveAlerts(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void showUpcomingEventAlerts() {
        System.out.println("\n=== UPCOMING EVENT ALERTS ===");
        List<Event> upcomingEvents = getUpcomingEvents();
        
        if (upcomingEvents.isEmpty()) {
            System.out.println("No upcoming events.");
            return;
        }
        
        System.out.println("📅 " + upcomingEvents.size() + " upcoming events:");
        upcomingEvents.stream()
                .limit(10)
                .forEach(event -> System.out.println("- " + event.getEventName() + 
                                                   " (" + event.getEventDate() + " at " + event.getStartTime() + ")"));
    }
    
    private void showTodaysEventAlerts() {
        System.out.println("\n=== TODAY'S EVENT ALERTS ===");
        List<Event> todaysEvents = getTodaysEvents();
        
        if (todaysEvents.isEmpty()) {
            System.out.println("No events scheduled for today.");
            return;
        }
        
        System.out.println("🚨 " + todaysEvents.size() + " events scheduled for today:");
        todaysEvents.forEach(event -> System.out.println("- " + event.getEventName() + 
                                                         " at " + event.getStartTime() + 
                                                         " in " + event.getVenue()));
    }
    
    private void showRegistrationDeadlineAlerts() {
        System.out.println("\n=== REGISTRATION DEADLINE ALERTS ===");
        // This would typically check for events with approaching registration deadlines
        System.out.println("Feature would show events with registration deadlines approaching.");
    }
    
    private void showCapacityAlerts() {
        System.out.println("\n=== CAPACITY ALERTS ===");
        List<Event> nearFullEvents = events.values().stream()
                .filter(event -> event.requiresRegistration() && 
                               event.getRegistrationRate() >= 90.0 && 
                               event.getStatus() == EventStatus.SCHEDULED)
                .collect(Collectors.toList());
        
        if (nearFullEvents.isEmpty()) {
            System.out.println("No capacity alerts.");
            return;
        }
        
        System.out.println("⚠️ " + nearFullEvents.size() + " events near capacity:");
        nearFullEvents.forEach(event -> System.out.println("- " + event.getEventName() + 
                                                           " (" + String.format("%.1f", event.getRegistrationRate()) + "% full)"));
    }
    
    private void showAllActiveAlerts() {
        System.out.println("\n=== ALL ACTIVE ALERTS ===");
        
        int totalAlerts = 0;
        
        List<Event> upcomingEvents = getUpcomingEvents();
        if (!upcomingEvents.isEmpty()) {
            System.out.println("📅 Upcoming Events: " + upcomingEvents.size());
            totalAlerts += upcomingEvents.size();
        }
        
        List<Event> todaysEvents = getTodaysEvents();
        if (!todaysEvents.isEmpty()) {
            System.out.println("🚨 Today's Events: " + todaysEvents.size());
            totalAlerts += todaysEvents.size();
        }
        
        List<Event> nearFullEvents = events.values().stream()
                .filter(event -> event.requiresRegistration() && 
                               event.getRegistrationRate() >= 90.0 && 
                               event.getStatus() == EventStatus.SCHEDULED)
                .collect(Collectors.toList());
        
        if (!nearFullEvents.isEmpty()) {
            System.out.println("⚠️ Near Capacity: " + nearFullEvents.size());
            totalAlerts += nearFullEvents.size();
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
        System.out.println("2. Search by Type");
        System.out.println("3. Search by Category");
        System.out.println("4. Search by Date Range");
        System.out.println("5. Search by Organizer");
        System.out.println("6. Advanced Search");
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1: searchByNameInteractive(); break;
            case 2: searchByTypeInteractive(); break;
            case 3: searchByCategoryInteractive(); break;
            case 4: searchByDateRangeInteractive(); break;
            case 5: searchByOrganizerInteractive(); break;
            case 6: advancedSearchInteractive(); break;
            default: System.out.println("Invalid choice.");
        }
    }
    
    private void searchByNameInteractive() {
        System.out.println("\n=== SEARCH BY NAME ===");
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        List<Event> results = events.values().stream()
                .filter(event -> event.getEventName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "name containing '" + searchTerm + "'");
    }
    
    private void searchByTypeInteractive() {
        System.out.println("\n=== SEARCH BY TYPE ===");
        System.out.println("Select event type:");
        EventType[] types = EventType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice < 1 || choice > types.length) {
            System.out.println("Invalid choice.");
            return;
        }
        
        EventType selectedType = types[choice - 1];
        List<Event> results = getEventsByType(selectedType);
        
        displaySearchResults(results, "type '" + selectedType + "'");
    }
    
    private void searchByCategoryInteractive() {
        System.out.println("\n=== SEARCH BY CATEGORY ===");
        System.out.println("Select event category:");
        EventCategory[] categories = EventCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Enter choice: ");
        
        int choice = getChoice();
        if (choice < 1 || choice > categories.length) {
            System.out.println("Invalid choice.");
            return;
        }
        
        EventCategory selectedCategory = categories[choice - 1];
        List<Event> results = getEventsByCategory(selectedCategory);
        
        displaySearchResults(results, "category '" + selectedCategory + "'");
    }
    
    private void searchByDateRangeInteractive() {
        System.out.println("\n=== SEARCH BY DATE RANGE ===");
        System.out.println("For demonstration, showing events in next 30 days:");
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        
        List<Event> results = events.values().stream()
                .filter(event -> event.getEventDate() != null && 
                               !event.getEventDate().isBefore(startDate) && 
                               !event.getEventDate().isAfter(endDate))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "date range (next 30 days)");
    }
    
    private void searchByOrganizerInteractive() {
        System.out.println("\n=== SEARCH BY ORGANIZER ===");
        System.out.print("Enter organizer ID or name: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        
        List<Event> results = events.values().stream()
                .filter(event -> (event.getOrganizerId() != null && event.getOrganizerId().toLowerCase().contains(searchTerm)) ||
                               (event.getOrganizerName() != null && event.getOrganizerName().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());
        
        displaySearchResults(results, "organizer containing '" + searchTerm + "'");
    }
    
    private void advancedSearchInteractive() {
        System.out.println("\n=== ADVANCED SEARCH ===");
        System.out.println("Enter search criteria (leave blank to skip):");
        
        System.out.print("Name contains: ");
        String nameFilter = scanner.nextLine().toLowerCase();
        
        System.out.print("Public events only? (y/n): ");
        String publicOnly = scanner.nextLine();
        Boolean isPublicFilter = publicOnly.isEmpty() ? null : "y".equalsIgnoreCase(publicOnly);
        
        System.out.print("Registration required? (y/n): ");
        String regRequired = scanner.nextLine();
        Boolean requiresRegFilter = regRequired.isEmpty() ? null : "y".equalsIgnoreCase(regRequired);
        
        System.out.print("Maximum registration fee: ");
        String maxFeeStr = scanner.nextLine();
        Double maxFee = maxFeeStr.isEmpty() ? null : Double.parseDouble(maxFeeStr);
        
        List<Event> results = events.values().stream()
                .filter(event -> nameFilter.isEmpty() || 
                               event.getEventName().toLowerCase().contains(nameFilter))
                .filter(event -> isPublicFilter == null || event.isPublic() == isPublicFilter)
                .filter(event -> requiresRegFilter == null || event.requiresRegistration() == requiresRegFilter)
                .filter(event -> maxFee == null || event.getRegistrationFee() <= maxFee)
                .collect(Collectors.toList());
        
        displaySearchResults(results, "advanced search criteria");
    }
    
    private void displaySearchResults(List<Event> results, String searchCriteria) {
        System.out.println("\n=== SEARCH RESULTS ===");
        System.out.println("Search criteria: " + searchCriteria);
        System.out.println("Results found: " + results.size());
        
        if (results.isEmpty()) {
            System.out.println("No events found matching the search criteria.");
            return;
        }
        
        System.out.printf("%-8s %-25s %-12s %-12s %-15s %-8s%n", 
                         "ID", "Name", "Type", "Date", "Status", "Capacity");
        System.out.println("-".repeat(85));
        
        results.forEach(event -> {
            System.out.printf("%-8s %-25s %-12s %-12s %-15s %-8s%n",
                             event.getEventId(),
                             event.getEventName().length() > 23 ? event.getEventName().substring(0, 23) + ".." : event.getEventName(),
                             event.getEventType().toString().length() > 10 ? event.getEventType().toString().substring(0, 10) + ".." : event.getEventType(),
                             event.getEventDate() != null ? event.getEventDate().toString() : "TBD",
                             event.getStatus().toString().length() > 13 ? event.getStatus().toString().substring(0, 13) + ".." : event.getStatus(),
                             event.getCurrentAttendees() + "/" + event.getMaxCapacity());
        });
    }
    
    private void displaySystemStatistics() {
        System.out.println("\n=== EVENT SYSTEM STATISTICS ===");
        
        // Basic statistics
        System.out.println("EVENT OVERVIEW:");
        System.out.println("- Total Events: " + events.size());
        System.out.println("- Scheduled: " + events.values().stream().filter(e -> e.getStatus() == EventStatus.SCHEDULED).count());
        System.out.println("- Completed: " + events.values().stream().filter(e -> e.getStatus() == EventStatus.COMPLETED).count());
        System.out.println("- Upcoming: " + getUpcomingEvents().size());
        System.out.println("- Today: " + getTodaysEvents().size());
        
        // Registration and attendance statistics
        int totalRegistrations = events.values().stream()
                .mapToInt(event -> event.getRegisteredParticipants().size())
                .sum();
        
        int totalAttendance = events.values().stream()
                .mapToInt(Event::getCurrentAttendees)
                .sum();
        
        System.out.println("\nREGISTRATION & ATTENDANCE:");
        System.out.println("- Total Registrations: " + totalRegistrations);
        System.out.println("- Total Attendance: " + totalAttendance);
        
        // Type distribution
        System.out.println("\nEVENT TYPE DISTRIBUTION:");
        Map<EventType, Long> typeDistribution = events.values().stream()
                .collect(Collectors.groupingBy(Event::getEventType, Collectors.counting()));
        
        typeDistribution.entrySet().stream()
                .sorted(Map.Entry.<EventType, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        
        // Category distribution
        System.out.println("\nEVENT CATEGORY DISTRIBUTION:");
        Map<EventCategory, Long> categoryDistribution = events.values().stream()
                .collect(Collectors.groupingBy(Event::getCategory, Collectors.counting()));
        
        categoryDistribution.entrySet().stream()
                .sorted(Map.Entry.<EventCategory, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println("- " + entry.getKey() + ": " + entry.getValue()));
        
        // Public vs Private
        long publicEvents = events.values().stream().filter(Event::isPublic).count();
        long privateEvents = events.size() - publicEvents;
        
        System.out.println("\nEVENT ACCESSIBILITY:");
        System.out.println("- Public: " + publicEvents);
        System.out.println("- Private: " + privateEvents);
        
        // Registration statistics
        long registrationEvents = events.values().stream().filter(Event::requiresRegistration).count();
        double totalRevenue = events.values().stream()
                .filter(Event::requiresRegistration)
                .mapToDouble(event -> event.getRegisteredParticipants().size() * event.getRegistrationFee())
                .sum();
        
        System.out.println("\nREGISTRATION OVERVIEW:");
        System.out.println("- Events requiring registration: " + registrationEvents);
        System.out.println("- Total registration revenue: $" + String.format("%.2f", totalRevenue));
        
        logger.log("Event system statistics displayed");
    }
    
    // Getters for external access
    public Map<String, Event> getAllEvents() { return new HashMap<>(events); }
    public int getTotalEvents() { return events.size(); }
    public int getScheduledEvents() { return (int) events.values().stream().filter(e -> e.getStatus() == EventStatus.SCHEDULED).count(); }
    public int getCompletedEvents() { return (int) events.values().stream().filter(e -> e.getStatus() == EventStatus.COMPLETED).count(); }
    public int getTotalRegistrations() { return events.values().stream().mapToInt(event -> event.getRegisteredParticipants().size()).sum(); }
    public int getTotalAttendance() { return events.values().stream().mapToInt(Event::getCurrentAttendees).sum(); }
}