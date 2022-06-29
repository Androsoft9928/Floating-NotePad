package com.androsoft.floatingnotepad.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.androsoft.floatingnotepad.Dao.NoteDao;
import com.androsoft.floatingnotepad.Database.NotesDatabaseND;
import com.androsoft.floatingnotepad.Model.NotesET;

import java.util.List;

public class NoteRepositoryNR {

    public NoteDao noteDao;
    public LiveData<List<NotesET>> getAllNotesNR;

    public NoteRepositoryNR(Application application) {
        NotesDatabaseND notesDatabaseND = NotesDatabaseND.getDatabaseInstance(application);
        noteDao = notesDatabaseND.noteDao();
        getAllNotesNR = noteDao.getAllNotes();

    }

    public void insertNoteNR(NotesET notesET){
        noteDao.insertNote(notesET);
    }

    public void updateNoteNR(NotesET notesET){
        noteDao.updateNote(notesET);
    }

    public void deleteNoteNR(int id){
        noteDao.deleteNote(id);
    }
}
