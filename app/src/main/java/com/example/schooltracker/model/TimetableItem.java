package com.example.schooltracker.model;

public class TimetableItem {
    private String subject;
    private String time;
    private String location;
    private String day;

    public TimetableItem(String subject, String time, String location, String day) {
        this.subject = subject;
        this.time = time;
        this.location = location;
        this.day = day;
    }

    public String getSubject() { return subject; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getDay() { return  day; }
}
