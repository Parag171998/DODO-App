package com.example.doda.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.doda.model.Drawing;
import com.example.doda.model.MapPin;


@Database(entities = {Drawing.class, MapPin.class},version = 1)
public abstract class MyappDatabse extends RoomDatabase {

    public abstract Mydao mydao();
    private static MyappDatabse INSTANCE;

    public static MyappDatabse getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyappDatabse.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyappDatabse.class, "drawings")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
