package com.example.schooltracker.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Event.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract EventDao eventDao();

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "event_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
