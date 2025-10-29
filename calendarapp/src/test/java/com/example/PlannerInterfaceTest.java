package com.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlannerInterfaceTest {

    private PlannerInterface planner;

    @BeforeEach
    void setUp() {
        planner = new PlannerInterface();
    }

    // ✅ ЗӨВ ажиллах тохиолдлууд
    @Test
    void testScheduleMeeting_success() {
        boolean result = planner.scheduleMeeting("2025-10-30", "09:00", "Sara", "Room1");
        assertTrue(result, "Meeting should be scheduled successfully");
    }

    @Test
    void testRoomAvailable_whenEmpty_shouldReturnTrue() {
        boolean available = planner.isRoomAvailable("2025-10-30", "09:00", "Room1");
        assertTrue(available, "Room should be available before scheduling");
    }

    @Test
    void testPersonAvailable_whenNoMeeting_shouldReturnTrue() {
        boolean available = planner.isPersonAvailable("2025-10-30", "09:00", "Sara");
        assertTrue(available, "Person should be available");
    }

    @Test
    void testSetVacation_validData() {
        boolean result = planner.setVacation("Sara", "2025-11-01", "2025-11-05");
        assertTrue(result, "Vacation should be set successfully");
    }

    @Test
    void testPrintRoomSchedule_validRoom() {
        String output = planner.printRoomSchedule("Room1");
        assertEquals("Schedule for room: Room1", output);
    }

    @Test
    void testPrintPersonSchedule_validPerson() {
        String output = planner.printPersonSchedule("Sara");
        assertEquals("Schedule for: Sara", output);
    }

    // ❌ АЛДААТАЙ нөхцлүүд
    @Test
    void testScheduleMeeting_invalidDate() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            planner.scheduleMeeting("2025-02-35", "09:00", "Sara", "Room1");
        });
        assertEquals("Invalid date", ex.getMessage());
    }

    @Test
    void testScheduleMeeting_nullFields() {
        assertThrows(IllegalArgumentException.class, () -> {
            planner.scheduleMeeting(null, "10:00", "Sara", "Room1");
        });
    }

    @Test
    void testScheduleMeeting_duplicateMeeting() {
        planner.scheduleMeeting("2025-10-30", "10:00", "Sara", "Room1");
        assertThrows(IllegalStateException.class, () -> {
            planner.scheduleMeeting("2025-10-30", "10:00", "Sara", "Room1");
        });
    }

    @Test
    void testSetVacation_invalidData() {
        assertThrows(IllegalArgumentException.class, () -> {
            planner.setVacation("Sara", "2025-11-01", "2025-11-01");
        });
    }

    @Test
    void testPrintRoomSchedule_emptyRoom() {
        String result = planner.printRoomSchedule("");
        assertEquals("No room name provided", result);
    }

    @Test
    void testPrintPersonSchedule_emptyPerson() {
        String result = planner.printPersonSchedule("");
        assertEquals("No person name provided", result);
    }

    @AfterEach
    void tearDown() {
        planner = null;
    }
}
