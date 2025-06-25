package com.campus.events;

import com.campus.utils.Identifiable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Event class representing campus events
 */
public class Event implements Identifiable {
    private String eventId;
    private String eventName;
    private String description;
    private EventType eventType;
    private EventCategory category;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String venue;
    private int maxCapacity;
    private int currentAttendees;
    private String organizerId;
    private String organizerName;
    private List<String> speakers;
    private List<String> sponsors;
    private EventStatus status;
    private boolean isPublic;
    private boolean requiresRegistration;
    private double registrationFee;
    private String registrationDeadline;
    private List<String> registeredParticipants;
    private List<String> attendees;
    private String contactEmail;
    private String contactPhone;
    private List<String> tags;
    private Map<String, String> additionalInfo;
    private String imageUrl;
    private String eventUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    
    public Event(String eventId, String eventName, EventType eventType, EventCategory category) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.category = category;
        this.status = EventStatus.PLANNED;
        this.isPublic = true;
        this.requiresRegistration = false;
        this.registrationFee = 0.0;
        this.maxCapacity = 100;
        this.currentAttendees = 0;
        this.speakers = new ArrayList<>();
        this.sponsors = new ArrayList<>();
        this.registeredParticipants = new ArrayList<>();
        this.attendees = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.additionalInfo = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        initializeDefaultSettings();
    }
    
    private void initializeDefaultSettings() {
        // Set default settings based on event type
        switch (eventType) {
            case CONFERENCE:
                this.maxCapacity = 200;
                this.requiresRegistration = true;
                this.registrationFee = 50.0;
                break;
            case WORKSHOP:
                this.maxCapacity = 50;
                this.requiresRegistration = true;
                this.registrationFee = 25.0;
                break;
            case SEMINAR:
                this.maxCapacity = 100;
                this.requiresRegistration = true;
                break;
            case CULTURAL:
                this.maxCapacity = 500;
                this.isPublic = true;
                break;
            case SPORTS:
                this.maxCapacity = 1000;
                this.isPublic = true;
                break;
            case MEETING:
                this.maxCapacity = 20;
                this.isPublic = false;
                break;
            case ORIENTATION:
                this.maxCapacity = 300;
                this.requiresRegistration = true;
                break;
        }
        
        // Add default tags based on category
        switch (category) {
            case ACADEMIC:
                tags.add("Academic");
                tags.add("Educational");
                break;
            case CULTURAL:
                tags.add("Cultural");
                tags.add("Entertainment");
                break;
            case SPORTS:
                tags.add("Sports");
                tags.add("Competition");
                break;
            case SOCIAL:
                tags.add("Social");
                tags.add("Networking");
                break;
            case PROFESSIONAL:
                tags.add("Professional");
                tags.add("Career");
                break;
        }
    }
    
    public void scheduleEvent(LocalDate eventDate, LocalTime startTime, LocalTime endTime, String venue) {
        if (status != EventStatus.PLANNED) {
            throw new IllegalStateException("Only planned events can be scheduled");
        }
        
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.status = EventStatus.SCHEDULED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addSpeaker(String speakerName) {
        if (!speakers.contains(speakerName)) {
            speakers.add(speakerName);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeSpeaker(String speakerName) {
        if (speakers.remove(speakerName)) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void addSponsor(String sponsorName) {
        if (!sponsors.contains(sponsorName)) {
            sponsors.add(sponsorName);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeSponsor(String sponsorName) {
        if (sponsors.remove(sponsorName)) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public boolean registerParticipant(String participantId) {
        if (!requiresRegistration) {
            throw new IllegalStateException("This event does not require registration");
        }
        
        if (registeredParticipants.size() >= maxCapacity) {
            return false; // Event is full
        }
        
        if (!registeredParticipants.contains(participantId)) {
            registeredParticipants.add(participantId);
            this.updatedAt = LocalDateTime.now();
            return true;
        }
        
        return false; // Already registered
    }
    
    public void unregisterParticipant(String participantId) {
        if (registeredParticipants.remove(participantId)) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public boolean markAttendance(String participantId) {
        if (status != EventStatus.IN_PROGRESS && status != EventStatus.COMPLETED) {
            throw new IllegalStateException("Can only mark attendance for ongoing or completed events");
        }
        
        if (!attendees.contains(participantId)) {
            attendees.add(participantId);
            this.currentAttendees = attendees.size();
            this.updatedAt = LocalDateTime.now();
            return true;
        }
        
        return false; // Already marked present
    }
    
    public void removeAttendance(String participantId) {
        if (attendees.remove(participantId)) {
            this.currentAttendees = attendees.size();
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void startEvent() {
        if (status != EventStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled events can be started");
        }
        
        this.status = EventStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void completeEvent() {
        if (status != EventStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only in-progress events can be completed");
        }
        
        this.status = EventStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancelEvent(String reason) {
        if (status == EventStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel completed event");
        }
        
        this.status = EventStatus.CANCELLED;
        this.notes = (notes != null ? notes + "; " : "") + "Cancelled: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void postponeEvent(LocalDate newDate, LocalTime newStartTime, LocalTime newEndTime, String reason) {
        if (status == EventStatus.COMPLETED || status == EventStatus.CANCELLED) {
            throw new IllegalStateException("Cannot postpone completed or cancelled event");
        }
        
        this.eventDate = newDate;
        this.startTime = newStartTime;
        this.endTime = newEndTime;
        this.status = EventStatus.POSTPONED;
        this.notes = (notes != null ? notes + "; " : "") + "Postponed: " + reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void removeTag(String tag) {
        if (tags.remove(tag)) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void addAdditionalInfo(String key, String value) {
        additionalInfo.put(key, value);
        this.updatedAt = LocalDateTime.now();
    }
    
    public void removeAdditionalInfo(String key) {
        if (additionalInfo.remove(key) != null) {
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public boolean isUpcoming() {
        return eventDate != null && eventDate.isAfter(LocalDate.now());
    }
    
    public boolean isToday() {
        return eventDate != null && eventDate.equals(LocalDate.now());
    }
    
    public boolean isOverdue() {
        return eventDate != null && eventDate.isBefore(LocalDate.now()) && 
               status == EventStatus.SCHEDULED;
    }
    
    public boolean isFull() {
        return requiresRegistration && registeredParticipants.size() >= maxCapacity;
    }
    
    public int getAvailableSpots() {
        if (!requiresRegistration) return maxCapacity - currentAttendees;
        return maxCapacity - registeredParticipants.size();
    }
    
    public double getRegistrationRate() {
        if (!requiresRegistration || maxCapacity == 0) return 0.0;
        return (double) registeredParticipants.size() / maxCapacity * 100;
    }
    
    public double getAttendanceRate() {
        if (registeredParticipants.isEmpty()) return 0.0;
        return (double) attendees.size() / registeredParticipants.size() * 100;
    }
    
    public void addNote(String note) {
        this.notes = (notes != null ? notes + "; " : "") + note;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void displayEventInfo() {
        System.out.println("=== EVENT INFORMATION ===");
        System.out.println("Event ID: " + eventId);
        System.out.println("Event Name: " + eventName);
        System.out.println("Description: " + (description != null ? description : "No description"));
        System.out.println("Type: " + eventType);
        System.out.println("Category: " + category);
        System.out.println("Status: " + status);
        System.out.println("Date: " + (eventDate != null ? eventDate : "Not scheduled"));
        System.out.println("Time: " + (startTime != null ? startTime + " - " + endTime : "Not scheduled"));
        System.out.println("Venue: " + (venue != null ? venue : "Not assigned"));
        System.out.println("Organizer: " + (organizerName != null ? organizerName + " (" + organizerId + ")" : "Not assigned"));
        System.out.println("Public Event: " + (isPublic ? "Yes" : "No"));
        System.out.println("Registration Required: " + (requiresRegistration ? "Yes" : "No"));
        
        if (requiresRegistration) {
            System.out.println("Registration Fee: $" + String.format("%.2f", registrationFee));
            System.out.println("Registration Deadline: " + (registrationDeadline != null ? registrationDeadline : "Not set"));
            System.out.println("Max Capacity: " + maxCapacity);
            System.out.println("Registered: " + registeredParticipants.size());
            System.out.println("Available Spots: " + getAvailableSpots());
            System.out.println("Registration Rate: " + String.format("%.1f", getRegistrationRate()) + "%");
        }
        
        System.out.println("Current Attendees: " + currentAttendees);
        
        if (status == EventStatus.COMPLETED && !registeredParticipants.isEmpty()) {
            System.out.println("Attendance Rate: " + String.format("%.1f", getAttendanceRate()) + "%");
        }
        
        System.out.println("Speakers: " + (speakers.isEmpty() ? "None" : String.join(", ", speakers)));
        System.out.println("Sponsors: " + (sponsors.isEmpty() ? "None" : String.join(", ", sponsors)));
        System.out.println("Contact Email: " + (contactEmail != null ? contactEmail : "Not provided"));
        System.out.println("Contact Phone: " + (contactPhone != null ? contactPhone : "Not provided"));
        System.out.println("Tags: " + (tags.isEmpty() ? "None" : String.join(", ", tags)));
        System.out.println("Event URL: " + (eventUrl != null ? eventUrl : "Not provided"));
        System.out.println("Image URL: " + (imageUrl != null ? imageUrl : "Not provided"));
        System.out.println("Upcoming: " + (isUpcoming() ? "Yes" : "No"));
        System.out.println("Today: " + (isToday() ? "Yes" : "No"));
        System.out.println("Overdue: " + (isOverdue() ? "Yes" : "No"));
        System.out.println("Full: " + (isFull() ? "Yes" : "No"));
        
        if (!additionalInfo.isEmpty()) {
            System.out.println("Additional Information:");
            additionalInfo.forEach((key, value) -> System.out.println("- " + key + ": " + value));
        }
        
        System.out.println("Created: " + createdAt);
        System.out.println("Last Updated: " + updatedAt);
        System.out.println("Notes: " + (notes != null ? notes : "None"));
    }
    
    public void displayAttendanceList() {
        System.out.println("=== ATTENDANCE LIST: " + eventName + " ===");
        
        if (attendees.isEmpty()) {
            System.out.println("No attendees recorded yet.");
            return;
        }
        
        System.out.println("Total Attendees: " + attendees.size());
        
        if (requiresRegistration) {
            System.out.println("Total Registered: " + registeredParticipants.size());
            System.out.println("Attendance Rate: " + String.format("%.1f", getAttendanceRate()) + "%");
        }
        
        System.out.println("\nAttendees:");
        for (int i = 0; i < attendees.size(); i++) {
            String participantId = attendees.get(i);
            boolean wasRegistered = registeredParticipants.contains(participantId);
            System.out.println((i + 1) + ". " + participantId + 
                              (requiresRegistration ? (wasRegistered ? " (Registered)" : " (Walk-in)") : ""));
        }
        
        // Show registered but not attended
        if (requiresRegistration && status == EventStatus.COMPLETED) {
            List<String> noShows = new ArrayList<>(registeredParticipants);
            noShows.removeAll(attendees);
            
            if (!noShows.isEmpty()) {
                System.out.println("\nRegistered but did not attend (" + noShows.size() + "):");
                noShows.forEach(id -> System.out.println("- " + id));
            }
        }
    }
    
    // Getters and Setters
    @Override
    public String getId() { return eventId; }
    
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { 
        this.eventName = eventName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public EventType getEventType() { return eventType; }
    public void setEventType(EventType eventType) { 
        this.eventType = eventType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public EventCategory getCategory() { return category; }
    public void setCategory(EventCategory category) { 
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDate getEventDate() { return eventDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    
    public String getVenue() { return venue; }
    public void setVenue(String venue) { 
        this.venue = venue;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { 
        this.maxCapacity = maxCapacity;
        this.updatedAt = LocalDateTime.now();
    }
    
    public int getCurrentAttendees() { return currentAttendees; }
    
    public String getOrganizerId() { return organizerId; }
    public void setOrganizerId(String organizerId) { 
        this.organizerId = organizerId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { 
        this.organizerName = organizerName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getSpeakers() { return new ArrayList<>(speakers); }
    public List<String> getSponsors() { return new ArrayList<>(sponsors); }
    public EventStatus getStatus() { return status; }
    
    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { 
        this.isPublic = isPublic;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean requiresRegistration() { return requiresRegistration; }
    public void setRequiresRegistration(boolean requiresRegistration) { 
        this.requiresRegistration = requiresRegistration;
        this.updatedAt = LocalDateTime.now();
    }
    
    public double getRegistrationFee() { return registrationFee; }
    public void setRegistrationFee(double registrationFee) { 
        this.registrationFee = registrationFee;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getRegistrationDeadline() { return registrationDeadline; }
    public void setRegistrationDeadline(String registrationDeadline) { 
        this.registrationDeadline = registrationDeadline;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getRegisteredParticipants() { return new ArrayList<>(registeredParticipants); }
    public List<String> getAttendees() { return new ArrayList<>(attendees); }
    
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { 
        this.contactEmail = contactEmail;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { 
        this.contactPhone = contactPhone;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getTags() { return new ArrayList<>(tags); }
    public Map<String, String> getAdditionalInfo() { return new HashMap<>(additionalInfo); }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { 
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getEventUrl() { return eventUrl; }
    public void setEventUrl(String eventUrl) { 
        this.eventUrl = eventUrl;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventId, event.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
    
    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventType=" + eventType +
                ", category=" + category +
                ", eventDate=" + eventDate +
                ", status=" + status +
                ", registeredParticipants=" + registeredParticipants.size() +
                ", attendees=" + attendees.size() +
                '}';
    }
}