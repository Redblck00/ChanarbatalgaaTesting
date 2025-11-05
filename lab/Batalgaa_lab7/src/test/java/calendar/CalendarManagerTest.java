package calendar;


import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CalendarManager класс тестлэх
 */
class CalendarManagerTest {
    
    private CalendarManager calendar;
    private LocalDateTime validStart;
    private LocalDateTime validEnd;

    @BeforeEach
    void setUp() {
        calendar = new CalendarManager();
        validStart = LocalDateTime.of(2025, 10, 21, 10, 0);
        validEnd = LocalDateTime.of(2025, 10, 21, 11, 0);
        
        // Өрөө болон ажилтан нэмэх
        calendar.addRoom("Өрөө-101");
        calendar.addRoom("Өрөө-102");
        calendar.addEmployee("Болд");
        calendar.addEmployee("Дорж");
        calendar.addEmployee("Сэргэлэн");
    }

    // ========== АМЖИЛТТАЙ ТОХИОЛДЛУУД ==========
    
    @Test
    @DisplayName("Уулзалт амжилттай товлох")
    void testScheduleMeetingSuccessfully() {
        Set<String> participants = new HashSet<>(Arrays.asList("Болд", "Дорж"));
        
        boolean result = calendar.scheduleMeeting("Долоо хоногийн уулзалт", 
                                                  validStart, validEnd, 
                                                  "Өрөө-101", participants);
        
        assertTrue(result, "Уулзалт товлогдох ёстой байсан");
        assertEquals(1, calendar.getAllMeetings().size());
    }

    @Test
    @DisplayName("Өрөөний сул байдлыг зөв шалгах")
    void testRoomAvailability() {
        Set<String> participants = new HashSet<>(Arrays.asList("Болд", "Дорж"));
        
        // Эхлээд сул байх ёстой
        assertTrue(calendar.isRoomAvailable("Өрөө-101", validStart, validEnd));
        
        // Уулзалт товлох
        calendar.scheduleMeeting("Уулзалт", validStart, validEnd, "Өрөө-101", participants);
        
        // Одоо завгүй байх ёстой
        assertFalse(calendar.isRoomAvailable("Өрөө-101", validStart, validEnd));
    }

    @Test
    @DisplayName("Хүний сул байдлыг зөв шалгах")
    void testPersonAvailability() {
        Set<String> participants = new HashSet<>(Arrays.asList("Болд"));
        
        // Эхлээд сул байх ёстой
        assertTrue(calendar.isPersonAvailable("Болд", validStart, validEnd));
        
        // Уулзалт товлох
        calendar.scheduleMeeting("Уулзалт", validStart, validEnd, "Өрөө-101", participants);
        
        // Одоо завгүй байх ёстой
        assertFalse(calendar.isPersonAvailable("Болд", validStart, validEnd));
    }

    @Test
    @DisplayName("Өрөөний хөтөлбөр авах")
    void testGetRoomSchedule() {
        Set<String> participants1 = new HashSet<>(Arrays.asList("Болд"));
        Set<String> participants2 = new HashSet<>(Arrays.asList("Дорж"));
        
        LocalDateTime start2 = validEnd.plusHours(1);
        LocalDateTime end2 = start2.plusHours(1);
        
        calendar.scheduleMeeting("Уулзалт 1", validStart, validEnd, "Өрөө-101", participants1);
        calendar.scheduleMeeting("Уулзалт 2", start2, end2, "Өрөө-101", participants2);
        
        List<Meeting> schedule = calendar.getRoomSchedule("Өрөө-101");
        
        assertEquals(2, schedule.size());
        assertEquals("Уулзалт 1", schedule.get(0).getTitle());
        assertEquals("Уулзалт 2", schedule.get(1).getTitle());
    }

    @Test
    @DisplayName("Хүний хөтөлбөр авах")
    void testGetPersonSchedule() {
        Set<String> participants1 = new HashSet<>(Arrays.asList("Болд", "Дорж"));
        Set<String> participants2 = new HashSet<>(Arrays.asList("Болд"));
        
        LocalDateTime start2 = validEnd.plusHours(1);
        LocalDateTime end2 = start2.plusHours(1);
        
        calendar.scheduleMeeting("Уулзалт 1", validStart, validEnd, "Өрөө-101", participants1);
        calendar.scheduleMeeting("Уулзалт 2", start2, end2, "Өрөө-102", participants2);
        
        List<Meeting> boldSchedule = calendar.getPersonSchedule("Болд");
        List<Meeting> dorjSchedule = calendar.getPersonSchedule("Дорж");
        
        assertEquals(2, boldSchedule.size());
        assertEquals(1, dorjSchedule.size());
    }

    @Test
    @DisplayName("Амралт амжилттай товлох")
    void testScheduleBreakSuccessfully() {
        boolean result = calendar.scheduleBreak("Болд", validStart, validEnd);
        
        assertTrue(result);
        assertFalse(calendar.isPersonAvailable("Болд", validStart, validEnd));
    }

    // ========== АЛДААНЫ ТОХИОЛДЛУУД ==========
    
    @Test
    @DisplayName("Бүртгэлгүй өрөө дээр уулзалт товлох - Exception шидэх ёстой")
    void testScheduleMeetingInUnregisteredRoom() {
        Set<String> participants = new HashSet<>(Arrays.asList("Болд"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calendar.scheduleMeeting("Уулзалт", validStart, validEnd, 
                                    "Өрөө-999", participants);
        });
        
        assertTrue(exception.getMessage().contains("бүртгэлгүй"));
    }

    @Test
    @DisplayName("Бүртгэлгүй хүнтэй уулзалт товлох - Exception шидэх ёстой")
    void testScheduleMeetingWithUnregisteredPerson() {
        Set<String> participants = new HashSet<>(Arrays.asList("Болд", "Мөнх"));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calendar.scheduleMeeting("Уулзалт", validStart, validEnd, 
                                    "Өрөө-101", participants);
        });
        
        assertTrue(exception.getMessage().contains("бүртгэлгүй"));
    }

    @Test
    @DisplayName("Завгүй өрөө дээр уулзалт товлох - false буцаах ёстой")
    void testScheduleMeetingInBusyRoom() {
        Set<String> participants1 = new HashSet<>(Arrays.asList("Болд"));
        Set<String> participants2 = new HashSet<>(Arrays.asList("Дорж"));
        
        // Эхний уулзалт
        calendar.scheduleMeeting("Уулзалт 1", validStart, validEnd, "Өрөө-101", participants1);
        
        // Давхцаж байгаа уулзалт
        LocalDateTime overlapStart = validStart.plusMinutes(30);
        LocalDateTime overlapEnd = validEnd.plusMinutes(30);
        
        boolean result = calendar.scheduleMeeting("Уулзалт 2", 
                                                  overlapStart, overlapEnd, 
                                                  "Өрөө-101", participants2);
        
        assertFalse(result, "Завгүй өрөө дээр товлогдох ёсгүй");
        assertEquals(1, calendar.getAllMeetings().size());
    }

    @Test
    @DisplayName("Завгүй хүнтэй уулзалт товлох - false буцаах ёстой")
    void testScheduleMeetingWithBusyPerson() {
        Set<String> participants1 = new HashSet<>(Arrays.asList("Болд"));
        Set<String> participants2 = new HashSet<>(Arrays.asList("Болд", "Дорж"));
        
        // Эхний уулзалт
        calendar.scheduleMeeting("Уулзалт 1", validStart, validEnd, "Өрөө-101", participants1);
        
        // Давхцаж байгаа уулзалт өөр өрөө дээр
        LocalDateTime overlapStart = validStart.plusMinutes(30);
        LocalDateTime overlapEnd = validEnd.plusMinutes(30);
        
        boolean result = calendar.scheduleMeeting("Уулзалт 2", 
                                                  overlapStart, overlapEnd, 
                                                  "Өрөө-102", participants2);
        
        assertFalse(result, "Завгүй хүнтэй уулзалт товлогдох ёсгүй");
    }

    @Test
    @DisplayName("Хоосон өрөөний нэр - Exception шидэх ёстой")
    void testAddEmptyRoom() {
        assertThrows(IllegalArgumentException.class, () -> {
            calendar.addRoom("   ");
        });
    }

    @Test
    @DisplayName("Хоосон ажилтны нэр - Exception шидэх ёстой")
    void testAddEmptyEmployee() {
        assertThrows(IllegalArgumentException.class, () -> {
            calendar.addEmployee("");
        });
    }

    @Test
    @DisplayName("Null параметртэй өрөөний сул байдал шалгах - Exception")
    void testIsRoomAvailableWithNullParameters() {
        assertThrows(IllegalArgumentException.class, () -> {
            calendar.isRoomAvailable(null, validStart, validEnd);
        });
    }

    @Test
    @DisplayName("Бүртгэлгүй өрөөний сул байдал шалгах - false")
    void testIsRoomAvailableForUnregisteredRoom() {
        boolean result = calendar.isRoomAvailable("Өрөө-999", validStart, validEnd);
        assertFalse(result);
    }

    @Test
    @DisplayName("Бүртгэлгүй хүний сул байдал шалгах - false")
    void testIsPersonAvailableForUnregisteredPerson() {
        boolean result = calendar.isPersonAvailable("Мөнх", validStart, validEnd);
        assertFalse(result);
    }

    @Test
    @DisplayName("Бүртгэлгүй хүнд амралт товлох - Exception")
    void testScheduleBreakForUnregisteredPerson() {
        assertThrows(IllegalArgumentException.class, () -> {
            calendar.scheduleBreak("Мөнх", validStart, validEnd);
        });
    }

    // ========== ИНТЕГРАЦИ ТЕСТ ==========
    
    @Test
    @DisplayName("Олон уулзалт товлох - бүгдийг зөв удирдах")
    void testMultipleMeetingsIntegration() {
        Set<String> p1 = new HashSet<>(Arrays.asList("Болд"));
        Set<String> p2 = new HashSet<>(Arrays.asList("Дорж"));
        Set<String> p3 = new HashSet<>(Arrays.asList("Сэргэлэн"));
        
        LocalDateTime start1 = LocalDateTime.of(2025, 10, 21, 9, 0);
        LocalDateTime end1 = start1.plusHours(1);
        
        LocalDateTime start2 = end1.plusMinutes(30);
        LocalDateTime end2 = start2.plusHours(1);
        
        LocalDateTime start3 = end2.plusMinutes(15);
        LocalDateTime end3 = start3.plusHours(1);
        
        // Гурван уулзалт товлох
        assertTrue(calendar.scheduleMeeting("Уулзалт 1", start1, end1, "Өрөө-101", p1));
        assertTrue(calendar.scheduleMeeting("Уулзалт 2", start2, end2, "Өрөө-101", p2));
        assertTrue(calendar.scheduleMeeting("Уулзалт 3", start3, end3, "Өрөө-102", p3));
        
        assertEquals(3, calendar.getAllMeetings().size());
        
        // Өрөө 101-д 2 уулзалт байх ёстой
        assertEquals(2, calendar.getRoomSchedule("Өрөө-101").size());
        
        // Болд 1 уулзалтад оролцох ёстой
        assertEquals(1, calendar.getPersonSchedule("Болд").size());
    }

    @Test
    @DisplayName("Өдрийн эцсээс хэтрэх уулзалт")
    void testMeetingSpanningMidnight() {
        Set<String> participants = new HashSet<>(Arrays.asList("Болд"));
        LocalDateTime lateStart = LocalDateTime.of(2025, 10, 21, 23, 30);
        LocalDateTime lateEnd = LocalDateTime.of(2025, 10, 22, 1, 30);
        
        boolean result = calendar.scheduleMeeting("Шөнийн уулзалт", 
                                                  lateStart, lateEnd, 
                                                  "Өрөө-101", participants);
        
        assertTrue(result);
        assertEquals(1, calendar.getAllMeetings().size());
    }

    @AfterEach
    void tearDown() {
        calendar = null;
    }
}