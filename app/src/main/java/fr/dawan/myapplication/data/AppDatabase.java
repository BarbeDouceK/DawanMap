package fr.dawan.myapplication.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fr.dawan.myapplication.model.Location;

@Database(entities = {Location.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
}