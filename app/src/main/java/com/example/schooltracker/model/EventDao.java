package com.example.schooltracker.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM events WHERE date = :date")
    LiveData<List<Event>> getEventsForDate(String date);

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getAllEvents();
}
