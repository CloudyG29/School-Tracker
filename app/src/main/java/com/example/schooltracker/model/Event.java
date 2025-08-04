package com.example.schooltracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String startTime;  // e.g. "12:00"
    public String endTime;    // e.g. "13:00"
    public String location;
    public String date;       // e.g. "2025-07-10" (ISO format)

    public String repetition; // e.g. "None", "Daily", "Weekly"
    public int color;         // e.g. Color.BLUE or custom ARGB int

    public Event(String title, String startTime, String endTime, String location,
                 String date, String repetition, int color) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.date = date;
        this.repetition = repetition;
        this.color = color;
    }
}
