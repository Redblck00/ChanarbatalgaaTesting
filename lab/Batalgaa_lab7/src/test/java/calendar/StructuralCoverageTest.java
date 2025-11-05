package calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Лаб 8: Бүтцийн тест (White Box Testing) - 100% Statement Coverage
 * CalendarManager болон Meeting классуудын бүх statement-уудыг хамарсан
 */
class StructuralCoverageTest {

    private CalendarManager manager;
    private LocalDateTime now;
    private LocalDateTime later;
    private LocalDateTime muchLater;

    @BeforeEach
    void setUp() {
        manager = new CalendarManager();
        now = LocalDateTime.now();
        later = now.plusHours(2);
        muchLater = now.plusHours(5);
    }

    // ==================== CalendarManager Structural Tests ====================

    @Test
    @DisplayName("ST-01: isPersonAvailable - Бүртгэлгүй хүн (true буцаах)")
    void testPersonAvailableUnregistered() {
        // Бүртгэлгүй хүн үргэлж чөлөөтэй
        boolean result = manager.isPersonAvailable("Unregistered", now, later);
        assertTrue(result, "Бүртгэлгүй хүн чөлөөтэй байх ёстой");
    }

    @Test
    @DisplayName("ST-02: isPersonAvailable - Бүртгэлтэй хүн, уулзалтгүй (true)")
    void testPersonAvailableNoMeetings() {
        manager.addEmployee("Alice");
        boolean result = manager.isPersonAvailable("Alice", now, later);
        assertTrue(result, "Уулзалтгүй хүн чөлөөтэй байх ёстой");
    }

    @Test
    @DisplayName("ST-03: isPersonAvailable - Уулзалт давхцаж байгаа (false)")
    void testPersonAvailableWithOverlap() throws Exception {
        manager.addEmployee("Bob");
        manager.addRoom("R1");
        Set<String> participants = new HashSet<>();
        participants.add("Bob");
        manager.scheduleMeeting("R1", now, later, "Meeting", participants);

        boolean result = manager.isPersonAvailable("Bob", now.plusMinutes(30), later.minusMinutes(30));
        assertFalse(result, "Уулзалттай үед чөлөөтэй биш байх ёстой");
    }

    @Test
    @DisplayName("ST-04: isRoomAvailable - Бүртгэлгүй өрөө (true буцаах)")
    void testRoomAvailableUnregistered() {
        // Бүртгэлгүй өрөө үргэлж чөлөөтэй
        boolean result = manager.isRoomAvailable("UnregisteredRoom", now, later);
        assertTrue(result, "Бүртгэлгүй өрөө чөлөөтэй байх ёстой");
    }

    @Test
    @DisplayName("ST-05: isRoomAvailable - Бүртгэлтэй өрөө, уулзалтгүй (true)")
    void testRoomAvailableNoMeetings() {
        manager.addRoom("R2");
        boolean result = manager.isRoomAvailable("R2", now, later);
        assertTrue(result, "Уулзалтгүй өрөө чөлөөтэй байх ёстой");
    }

    @Test
    @DisplayName("ST-06: isRoomAvailable - Уулзалт давхцаж байгаа (false)")
    void testRoomAvailableWithOverlap() throws Exception {
        manager.addEmployee("Charlie");
        manager.addRoom("R3");
        Set<String> participants = new HashSet<>();
        participants.add("Charlie");
        manager.scheduleMeeting("R3", now, later, "Busy", participants);

        boolean result = manager.isRoomAvailable("R3", now.plusMinutes(15), later.minusMinutes(15));
        assertFalse(result, "Эзлэгдсэн өрөө чөлөөтэй биш байх ёстой");
    }

    @Test
    @DisplayName("ST-07: scheduleBreak - Амжилттай амралт төлөвлөх")
    void testScheduleBreakSuccess() throws Exception {
        manager.addEmployee("Dave");
        manager.scheduleBreak("Dave", now, later);

        boolean available = manager.isPersonAvailable("Dave", now.plusMinutes(30), later.minusMinutes(30));
        assertFalse(available, "Амралтын үед чөлөөтэй биш байх ёстой");
    }

    @Test
    @DisplayName("ST-08: scheduleBreak - Давхцах амралт (Exception)")
    void testScheduleBreakOverlap() throws Exception {
        manager.addEmployee("Eve");
        manager.scheduleBreak("Eve", now, later);

        Exception e = assertThrows(Exception.class, () -> {
            manager.scheduleBreak("Eve", now.plusMinutes(30), later.plusMinutes(30));
        });
        assertTrue(e.getMessage().contains("давхцаж") || e.getMessage().contains("busy"));
    }

    @Test
    @DisplayName("ST-09: scheduleBreak - Бүртгэлгүй хүн (Exception)")
    void testScheduleBreakUnregistered() {
        Exception e = assertThrows(Exception.class, () -> {
            manager.scheduleBreak("Unknown", now, later);
        });
        assertTrue(e.getMessage().contains("бүртгэлгүй") || e.getMessage().contains("not registered"));
    }

    @Test
    @DisplayName("ST-10: scheduleMeeting - Бүртгэлгүй өрөө (Exception)")
    void testScheduleMeetingUnregisteredRoom() {
        manager.addEmployee("Frank");
        Set<String> participants = new HashSet<>();
        participants.add("Frank");

        Exception e = assertThrows(Exception.class, () -> {
            manager.scheduleMeeting("NonExistentRoom", now, later, "Test", participants);
        });
        assertTrue(e.getMessage().contains("өрөө") || e.getMessage().contains("room"));
    }

    @Test
    @DisplayName("ST-11: scheduleMeeting - Бүртгэлгүй оролцогч (Exception)")
    void testScheduleMeetingUnregisteredParticipant() {
        manager.addRoom("R4");
        Set<String> participants = new HashSet<>();
        participants.add("UnknownPerson");

        Exception e = assertThrows(Exception.class, () -> {
            manager.scheduleMeeting("R4", now, later, "Test", participants);
        });
        assertTrue(e.getMessage().contains("бүртгэлгүй") || e.getMessage().contains("not registered"));
    }

    @Test
    @DisplayName("ST-12: scheduleMeeting - Өрөө эзлэгдсэн (Exception)")
    void testScheduleMeetingRoomBusy() throws Exception {
        manager.addEmployee("Grace");
        manager.addEmployee("Heidi");
        manager.addRoom("R5");

        Set<String> participants1 = new HashSet<>();
        participants1.add("Grace");
        manager.scheduleMeeting("R5", now, later, "First", participants1);

        Set<String> participants2 = new HashSet<>();
        participants2.add("Heidi");
        Exception e = assertThrows(Exception.class, () -> {
            manager.scheduleMeeting("R5", now.plusMinutes(30), later.plusMinutes(30), "Second", participants2);
        });
        assertTrue(e.getMessage().contains("эзлэгдсэн") || e.getMessage().contains("busy"));
    }

    @Test
    @DisplayName("ST-13: scheduleMeeting - Оролцогч завгүй (Exception)")
    void testScheduleMeetingParticipantBusy() throws Exception {
        manager.addEmployee("Ivan");
        manager.addRoom("R6");
        manager.addRoom("R7");

        Set<String> participants = new HashSet<>();
        participants.add("Ivan");
        manager.scheduleMeeting("R6", now, later, "First", participants);

        Exception e = assertThrows(Exception.class, () -> {
            manager.scheduleMeeting("R7", now.plusMinutes(15), later.minusMinutes(15), "Second", participants);
        });
        assertTrue(e.getMessage().contains("завгүй") || e.getMessage().contains("busy"));
    }

    @Test
    @DisplayName("ST-14: addRoom - Давхар бүртгэх (Exception)")
    void testAddRoomDuplicate() throws Exception {
        manager.addRoom("R8");
        Exception e = assertThrows(Exception.class, () -> {
            manager.addRoom("R8");
        });
        assertTrue(e.getMessage().contains("бүртгэлтэй") || e.getMessage().contains("exists"));
    }

    @Test
    @DisplayName("ST-15: addEmployee - Давхар бүртгэх (Exception)")
    void testAddEmployeeDuplicate() throws Exception {
        manager.addEmployee("Judy");
        Exception e = assertThrows(Exception.class, () -> {
            manager.addEmployee("Judy");
        });
        assertTrue(e.getMessage().contains("бүртгэлтэй") || e.getMessage().contains("exists"));
    }

    // ==================== Meeting Structural Tests ====================

    @Test
    @DisplayName("ST-16: Meeting.toString() - Бүрэн мэдээлэл")
    void testMeetingToString() {
        Set<String> participants = new HashSet<>();
        participants.add("Alice");
        participants.add("Bob");
        Meeting meeting = new Meeting("R1", now, later, "Discussion", participants);

        String result = meeting.toString();
        assertNotNull(result);
        assertTrue(result.contains("Discussion"));
        assertTrue(result.contains("R1"));
    }

    @Test
    @DisplayName("ST-17: Meeting.overlaps(Meeting) - Огт давхцахгүй (false)")
    void testMeetingOverlapsNoOverlap() {
        Set<String> p1 = new HashSet<>();
        p1.add("Alice");
        Meeting m1 = new Meeting("R1", now, later, "M1", p1);
        Meeting m2 = new Meeting("R2", muchLater, muchLater.plusHours(2), "M2", p1);

        assertFalse(m1.overlaps(m2), "Давхцаагүй уулзалтууд");
    }

    @Test
    @DisplayName("ST-18: Meeting.overlaps(Meeting) - Бүрэн давхцах (true)")
    void testMeetingOverlapsFull() {
        Set<String> p1 = new HashSet<>();
        p1.add("Bob");
        Meeting m1 = new Meeting("R1", now, later, "M1", p1);
        Meeting m2 = new Meeting("R2", now.plusMinutes(30), later.minusMinutes(30), "M2", p1);

        assertTrue(m1.overlaps(m2), "Бүрэн давхцаж байна");
    }

    @Test
    @DisplayName("ST-19: Meeting.overlaps(LocalDateTime) - Эхлэх цаг дээр (true)")
    void testMeetingOverlapsTimeAtStart() {
        Set<String> p1 = new HashSet<>();
        p1.add("Charlie");
        Meeting m1 = new Meeting("R1", now, later, "M1", p1);

        assertTrue(m1.overlaps(now, later), "Яг эхлэх цаг дээр");
    }

    @Test
    @DisplayName("ST-20: Meeting.overlaps(LocalDateTime) - Дундаас (true)")
    void testMeetingOverlapsTimeMiddle() {
        Set<String> p1 = new HashSet<>();
        p1.add("Dave");
        Meeting m1 = new Meeting("R1", now, later, "M1", p1);

        assertTrue(m1.overlaps(now.plusMinutes(30), later.minusMinutes(30)), "Дундаас давхцаж байна");
    }

    @Test
    @DisplayName("ST-21: Meeting.overlaps(LocalDateTime) - Хойд талд (false)")
    void testMeetingOverlapsTimeAfter() {
        Set<String> p1 = new HashSet<>();
        p1.add("Eve");
        Meeting m1 = new Meeting("R1", now, later, "M1", p1);

        assertFalse(m1.overlaps(muchLater, muchLater.plusHours(1)), "Хойд талд давхцахгүй");
    }

    @Test
    @DisplayName("ST-22: Meeting.overlaps(LocalDateTime) - Өмнө талд (false)")
    void testMeetingOverlapsTimeBefore() {
        Set<String> p1 = new HashSet<>();
        p1.add("Frank");
        Meeting m1 = new Meeting("R1", later, muchLater, "M1", p1);

        assertFalse(m1.overlaps(now, now.plusMinutes(30)), "Өмнө талд давхцахгүй");
    }

    @Test
    @DisplayName("ST-23: Meeting - Бүх getter методууд")
    void testMeetingGetters() {
        Set<String> participants = new HashSet<>();
        participants.add("Grace");
        participants.add("Heidi");
        Meeting m = new Meeting("R9", now, later, "Important", participants);

        assertEquals("R9", m.getRoom());
        assertEquals("Important", m.getTitle());
        assertEquals(now, m.getStartTime());
        assertEquals(later, m.getEndTime());
        assertEquals(2, m.getParticipants().size());
        assertTrue(m.getParticipants().contains("Grace"));
        assertTrue(m.getParticipants().contains("Heidi"));
    }

    @Test
    @DisplayName("ST-24: Meeting constructor - Null оролцогч (Exception)")
    void testMeetingConstructorNullParticipants() {
        Exception e = assertThrows(Exception.class, () -> {
            new Meeting("R1", now, later, "Test", null);
        });
    }

    @Test
    @DisplayName("ST-25: Meeting constructor - Хоосон гарчиг")
    void testMeetingConstructorEmptyTitle() {
        Set<String> p = new HashSet<>();
        p.add("Alice");
        Meeting m = new Meeting("R1", now, later, "", p);
        assertEquals("", m.getTitle());
    }
}