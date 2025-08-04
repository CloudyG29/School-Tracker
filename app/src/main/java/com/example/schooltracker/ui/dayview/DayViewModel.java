package com.example.schooltracker.ui.dayview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.schooltracker.model.AppDatabase;
import com.example.schooltracker.model.Event;
import com.example.schooltracker.model.EventDao;

import java.util.List;

public class DayViewModel extends AndroidViewModel {

    private final EventDao eventDao;

    public DayViewModel(@NonNull Application application) {
        super(application);
        // ✅ get DAO instance properly
        eventDao = AppDatabase.getInstance(application).eventDao();
    }

    // ✅ use your DAO, don’t call static nonexistent stuff
    public void deleteEvent(Event e) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(e);
        });
    }

    public void updateEvent(Event e) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.update(e);
        });
    }

    public LiveData<List<Event>> getAllEvents() {
        return eventDao.getAllEvents();
    }
}
