package com.androsoft.floatingnotepad.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.androsoft.floatingnotepad.Model.NotesET;
import com.androsoft.floatingnotepad.Dao.NoteDao;

@Database(entities = {NotesET.class},version = 1)
public abstract class NotesDatabaseND extends RoomDatabase {

    public abstract NoteDao noteDao();

    public static NotesDatabaseND INSTANCE;

    public static synchronized NotesDatabaseND getDatabaseInstance(Context context)
    {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, NotesDatabaseND.class, "Notes_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
