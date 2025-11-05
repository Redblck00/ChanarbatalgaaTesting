package calendar;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Уулзалтыг төлөөлөх класс
 */
public class Meeting {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String room;
    private Set<String> participants;

    public Meeting(String title, LocalDateTime startTime, LocalDateTime endTime, 
                   String room, Set<String> participants) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Уулзалтын нэр хоосон байж болохгүй");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Цаг оруулна уу");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("Дуусах цаг эхлэх цагаас өмнө байж болохгүй");
        }
        if (room == null || room.trim().isEmpty()) {
            throw new IllegalArgumentException("Өрөөний нэр оруулна уу");
        }
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("Оролцогч байх ёстой");
        }
        
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.participants = new HashSet<>(participants);
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getRoom() {
        return room;
    }

    public Set<String> getParticipants() {
        return new HashSet<>(participants);
    }

    /**
     * Өгөгдсөн хугацаа уулзалттай давхцаж байгаа эсэхийг шалгана
     */
    public boolean overlaps(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Цаг null байж болохгүй");
        }
        return !(end.isBefore(this.startTime) || end.isEqual(this.startTime) ||
                 start.isAfter(this.endTime) || start.isEqual(this.endTime));
    }

    /**
     * Өгөгдсөн уулзалттай давхцаж байгаа эсэхийг шалгана
     */
    public boolean overlaps(Meeting other) {
        if (other == null) {
            throw new IllegalArgumentException("Уулзалт null байж болохгүй");
        }
        return this.overlaps(other.getStartTime(), other.getEndTime());
    }

    @Override
    public String toString() {
        return String.format("%s | %s - %s | Өрөө: %s | Оролцогчид: %s",
                title, startTime, endTime, room, participants);
    }
}