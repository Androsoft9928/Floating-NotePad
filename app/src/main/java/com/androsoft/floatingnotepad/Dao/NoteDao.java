package com.androsoft.floatingnotepad.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.androsoft.floatingnotepad.Model.NotesET;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM NotesET_database")
    LiveData<List<NotesET>> getAllNotes();

    @Insert
    void insertNote(NotesET notesET);

    @Update
    void updateNote(NotesET notesET);

    @Query("DELETE FROM NotesET_database WHERE id=:id")
    void deleteNote(int id);

}
