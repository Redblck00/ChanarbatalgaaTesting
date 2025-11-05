package calendar;

import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Meeting класс тестлэх
 */
class MeetingTest {
    
    private LocalDateTime validStart;
    private LocalDateTime validEnd;
    private Set<String> validParticipants;

    @BeforeEach
    void setUp() {
        // Тест бүрийн өмнө анхны өгөгдөл бэлтгэх
        validStart = LocalDateTime.of(2025, 10, 21, 10, 0);
        validEnd = LocalDateTime.of(2025, 10, 21, 11, 0);
        validParticipants = new HashSet<>();
        validParticipants.add("Болд");
        validParticipants.add("Дорж");
    }

    // ========== АМЖИЛТТАЙ ТОХИОЛДЛУУД ==========
    
    @Test
    @DisplayName("Зөв өгөгдлөөр уулзалт үүсгэх")
    void testCreateValidMeeting() {
        Meeting meeting = new Meeting("Долоо хоногийн уулзалт", validStart, validEnd, 
                                     "Өрөө-101", validParticipants);
        
        assertNotNull(meeting);
        assertEquals("Долоо хоногийн уулзалт", meeting.getTitle());
        assertEquals(validStart, meeting.getStartTime());
        assertEquals(validEnd, meeting.getEndTime());
        assertEquals("Өрөө-101", meeting.getRoom());
        assertEquals(2, meeting.getParticipants().size());
    }

    @Test
    @DisplayName("Давхцах уулзалтыг зөв илрүүлэх")
    void testOverlappingMeetings() {
        Meeting meeting1 = new Meeting("Уулзалт 1", validStart, validEnd, 
                                      "Өрөө-101", validParticipants);
        
        LocalDateTime overlapStart = validStart.plusMinutes(30);
        LocalDateTime overlapEnd = validEnd.plusMinutes(30);
        
        assertTrue(meeting1.overlaps(overlapStart, overlapEnd), 
                  "Давхцаж байгааг илрүүлээгүй");
    }

    @Test
    @DisplayName("Давхцахгүй уулзалтыг зөв илрүүлэх")
    void testNonOverlappingMeetings() {
        Meeting meeting1 = new Meeting("Уулзалт 1", validStart, validEnd, 
                                      "Өрөө-101", validParticipants);
        
        LocalDateTime nonOverlapStart = validEnd.plusMinutes(1);
        LocalDateTime nonOverlapEnd = validEnd.plusHours(1);
        
        assertFalse(meeting1.overlaps(nonOverlapStart, nonOverlapEnd), 
                   "Давхцахгүй байгааг буруу илрүүлсэн");
    }

    // ========== АЛДААНЫ ТОХИОЛДЛУУД ==========
    
    @Test
    @DisplayName("Нэргүй уулзалт үүсгэх - Exception шидэх ёстой")
    void testCreateMeetingWithNullTitle() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Meeting(null, validStart, validEnd, "Өрөө-101", validParticipants);
        });
        
        assertTrue(exception.getMessage().contains("нэр"));
    }

    @Test
    @DisplayName("Хоосон нэртэй уулзалт үүсгэх - Exception шидэх ёстой")
    void testCreateMeetingWithEmptyTitle() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Meeting("   ", validStart, validEnd, "Өрөө-101", validParticipants);
        });
        
        assertTrue(exception.getMessage().contains("нэр"));
    }

    @Test
    @DisplayName("Эхлэх цаг null байх - Exception шидэх ёстой")
    void testCreateMeetingWithNullStartTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Meeting("Уулзалт", null, validEnd, "Өрөө-101", validParticipants);
        });
    }

    @Test
    @DisplayName("Дуусах цаг эхлэх цагаас өмнө байх - Exception шидэх ёстой")
    void testCreateMeetingWithEndBeforeStart() {
        LocalDateTime invalidEnd = validStart.minusHours(1);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Meeting("Уулзалт", validStart, invalidEnd, "Өрөө-101", validParticipants);
        });
        
        assertTrue(exception.getMessage().contains("өмнө"));
    }

    @Test
    @DisplayName("Өрөөний нэргүй уулзалт - Exception шидэх ёстой")
    void testCreateMeetingWithNullRoom() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Meeting("Уулзалт", validStart, validEnd, null, validParticipants);
        });
        
        assertTrue(exception.getMessage().contains("өрөө"));
    }

    @Test
    @DisplayName("Оролцогчгүй уулзалт - Exception шидэх ёстой")
    void testCreateMeetingWithNoParticipants() {
        Set<String> emptyParticipants = new HashSet<>();
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Meeting("Уулзалт", validStart, validEnd, "Өрөө-101", emptyParticipants);
        });
        
        assertTrue(exception.getMessage().contains("Оролцогч"));
    }

    @Test
    @DisplayName("Null цагаар давхцал шалгах - Exception шидэх ёстой")
    void testOverlapWithNullTime() {
        Meeting meeting = new Meeting("Уулзалт", validStart, validEnd, 
                                     "Өрөө-101", validParticipants);
        
        assertThrows(IllegalArgumentException.class, () -> {
            meeting.overlaps(null, validEnd);
        });
    }

    @Test
    @DisplayName("Null уулзалттай давхцал шалгах - Exception шидэх ёстой")
    void testOverlapWithNullMeeting() {
        Meeting meeting = new Meeting("Уулзалт", validStart, validEnd, 
                                     "Өрөө-101", validParticipants);
        
        assertThrows(IllegalArgumentException.class, () -> {
            meeting.overlaps((Meeting) null);
        });
    }

    // ========== ХИЛИЙН ТОХИОЛДЛУУД (Edge Cases) ==========
    
    @Test
    @DisplayName("Яг ижил цагтай уулзалт - Давхцах ёстой")
    void testExactSameTimeMeetings() {
        Meeting meeting1 = new Meeting("Уулзалт 1", validStart, validEnd, 
                                      "Өрөө-101", validParticipants);
        
        assertTrue(meeting1.overlaps(validStart, validEnd));
    }

    @Test
    @DisplayName("Нэг минутын уулзалт үүсгэх")
    void testOneMinuteMeeting() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 21, 10, 0);
        LocalDateTime end = start.plusMinutes(1);
        
        Meeting meeting = new Meeting("Богино уулзалт", start, end, 
                                     "Өрөө-101", validParticipants);
        
        assertNotNull(meeting);
        assertEquals(1, meeting.getEndTime().getMinute() - meeting.getStartTime().getMinute());
    }

    @Test
    @DisplayName("24 цагийн уулзалт үүсгэх")
    void testFullDayMeeting() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 21, 0, 0);
        LocalDateTime end = start.plusHours(24);
        
        Meeting meeting = new Meeting("Бүтэн өдрийн уулзалт", start, end, 
                                     "Өрөө-101", validParticipants);
        
        assertNotNull(meeting);
        assertEquals(24, java.time.Duration.between(start, end).toHours());
    }

    @Test
    @DisplayName("Хилийн утга: 2 сарын 29 (биш өндөр жил)")
    void testInvalidDate_February29NonLeapYear() {
        // 2025 жил өндөр жил биш, 2/29 байхгүй
        assertThrows(java.time.DateTimeException.class, () -> {
            LocalDateTime invalidDate = LocalDateTime.of(2025, 2, 29, 10, 0);
        });
    }

    @Test
    @DisplayName("Хилийн утга: 2 сарын 29 (өндөр жил)")
    void testValidDate_February29LeapYear() {
        // 2024 жил өндөр жил, 2/29 байна
        LocalDateTime validDate = LocalDateTime.of(2024, 2, 29, 10, 0);
        LocalDateTime end = validDate.plusHours(1);
        
        Meeting meeting = new Meeting("Өндөр жилийн уулзалт", validDate, end, 
                                     "Өрөө-101", validParticipants);
        
        assertNotNull(meeting);
    }
}