package com.example.showpointrma.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SerijaEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SerijaDao serijaDao();

    public static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "showpoint.db")
                            .fallbackToDestructiveMigration() // kes se puni sa servera, zato ok
                            .build();
                }
            }
        }
        return instance;
    }

}
