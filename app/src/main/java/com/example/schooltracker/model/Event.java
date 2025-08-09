package com.example.schooltracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "start_time")
    private String startTime;  // e.g. "12:00"

    @ColumnInfo(name = "end_time")
    private String endTime;    // e.g. "13:00"

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "date")
    private String date;       // e.g. "2025-07-10" (ISO format)

    @ColumnInfo(name = "repetition")
    private String repetition; // e.g. "None", "Daily", "Weekly"

    @ColumnInfo(name = "color")
    private int color;         // e.g. Color.BLUE or custom ARGB int

    // Room sometimes needs an empty constructor
    public Event() {
    }

    // Constructor for creating new events
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

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getRepetition() {
        return repetition;
    }

    public int getColor() {
        return color;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRepetition(String repetition) {
        this.repetition = repetition;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
