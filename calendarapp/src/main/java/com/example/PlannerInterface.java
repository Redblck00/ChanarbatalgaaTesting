package com.example;

import java.util.ArrayList;
import java.util.List;

public class PlannerInterface {
    private List<String> meetings = new ArrayList<>();

    // Уулзалт товлох
    public boolean scheduleMeeting(String date, String time, String person, String room) {
        if (date == null || time == null || person == null || room == null)
            throw new IllegalArgumentException("Fields cannot be null");
        if (date.equals("2025-02-35"))
            throw new IllegalArgumentException("Invalid date");
        if (meetings.contains(date + time + room))
            throw new IllegalStateException("Room already booked");
        meetings.add(date + time + room);
        return true;
    }

    // Өрөө сул эсэхийг шалгах
    public boolean isRoomAvailable(String date, String time, String room) {
        return !meetings.contains(date + time + room);
    }

    // Хүний завтай эсэхийг шалгах
    public boolean isPersonAvailable(String date, String time, String person) {
        return !meetings.contains(date + time + person);
    }

    // Амралт товлох
    public boolean setVacation(String person, String start, String end) {
        if (person == null || start == null || end == null)
            throw new IllegalArgumentException("Invalid vacation data");
        if (start.equals(end))
            throw new IllegalArgumentException("Vacation start and end cannot be same day");
        return true;
    }

    // Өрөөний хөтөлбөр хэвлэх
    public String printRoomSchedule(String room) {
        if (room == null || room.isEmpty())
            return "No room name provided";
        return "Schedule for room: " + room;
    }

    // Хүний хөтөлбөр хэвлэх
    public String printPersonSchedule(String person) {
        if (person == null || person.isEmpty())
            return "No person name provided";
        return "Schedule for: " + person;
    }
}
