package com.example.schooltracker.ui.timetable;

import com.example.schooltracker.model.Event;

public interface EventActionListener {
    void onEdit(Event event);
    void onDelete(Event event);

}
