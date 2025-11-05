package calendar;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Уулзалт болон амралтыг удирдах менежер класс
 */
public class CalendarManager {

    private Set<String> rooms;
    private Set<String> employees;
    private List<Meeting> meetings;

    public CalendarManager() {
        this.rooms = new HashSet<>();
        this.employees = new HashSet<>();
        this.meetings = new ArrayList<>();
    }

    // --------------------------
    // ROOM ба EMPLOYEE нэмэх
    // --------------------------

    public boolean addRoom(String roomName) {
        if (roomName == null || roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Өрөөний нэр хоосон байж болохгүй");
        }
        return rooms.add(roomName.trim());
    }

    public boolean addEmployee(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ажилтны нэр хоосон байж болохгүй");
        }
        return employees.add(name.trim());
    }

    // --------------------------
    // SCHEDULE MEETING
    // --------------------------

    public boolean scheduleMeeting(String title, LocalDateTime start, LocalDateTime end,
                                   String room, Set<String> participants) {
        if (!rooms.contains(room)) {
            throw new IllegalArgumentException("Өрөө бүртгэлгүй байна: " + room);
        }

        for (String p : participants) {
            if (!employees.contains(p)) {
                throw new IllegalArgumentException("Ажилтан бүртгэлгүй байна: " + p);
            }
        }

        // өрөө сул байгаа эсэх
        if (!isRoomAvailable(room, start, end)) {
            return false;
        }

        // оролцогчид сул байгаа эсэх
        for (String p : participants) {
            if (!isPersonAvailable(p, start, end)) {
                return false;
            }
        }

        Meeting m = new Meeting(title, start, end, room, participants);
        meetings.add(m);
        return true;
    }

    // --------------------------
    // CHECK ROOM / PERSON AVAILABILITY
    // --------------------------

    public boolean isRoomAvailable(String room, LocalDateTime start, LocalDateTime end) {
        if (room == null || start == null || end == null) {
            throw new IllegalArgumentException("Оруулсан утга буруу байна");
        }

        for (Meeting m : meetings) {
            if (m.getRoom().equals(room) && m.overlaps(start, end)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPersonAvailable(String person, LocalDateTime start, LocalDateTime end) {
        if (person == null || start == null || end == null) {
            throw new IllegalArgumentException("Оруулсан утга буруу байна");
        }

        for (Meeting m : meetings) {
            if (m.getParticipants().contains(person) && m.overlaps(start, end)) {
                return false;
            }
        }
        return true;
    }

    // --------------------------
    // GET SCHEDULES
    // --------------------------

    public List<Meeting> getAllMeetings() {
        return new ArrayList<>(meetings);
    }

    public List<Meeting> getRoomSchedule(String room) {
        List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.getRoom().equals(room)) {
                result.add(m);
            }
        }
        result.sort(Comparator.comparing(Meeting::getStartTime));
        return result;
    }

    public List<Meeting> getPersonSchedule(String person) {
        List<Meeting> result = new ArrayList<>();
        for (Meeting m : meetings) {
            if (m.getParticipants().contains(person)) {
                result.add(m);
            }
        }
        result.sort(Comparator.comparing(Meeting::getStartTime));
        return result;
    }

    // --------------------------
    // BREAK / REST TIME
    // --------------------------

    public boolean scheduleBreak(String person, LocalDateTime start, LocalDateTime end) {
        if (!employees.contains(person)) {
            throw new IllegalArgumentException("Ажилтан бүртгэлгүй байна: " + person);
        }

        // давхцаж байгаа уулзалт байгаа эсэхийг шалгах
        if (!isPersonAvailable(person, start, end)) {
            return false;
        }

        Set<String> participants = new HashSet<>();
        participants.add(person);

        Meeting breakMeeting = new Meeting("Амралт", start, end, "BREAK", participants);
        meetings.add(breakMeeting);
        return true;
    }
}
