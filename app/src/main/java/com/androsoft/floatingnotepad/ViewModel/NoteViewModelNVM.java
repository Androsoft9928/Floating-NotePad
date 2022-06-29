package com.androsoft.floatingnotepad.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.androsoft.floatingnotepad.Model.NotesET;
import com.androsoft.floatingnotepad.Repository.NoteRepositoryNR;

import java.util.List;

public class NoteViewModelNVM extends AndroidViewModel {

    NoteRepositoryNR noteRepositoryNR;
    public LiveData<List<NotesET>> getAllNotes;

    public NoteViewModelNVM(@NonNull Application application) {
        super(application);

        noteRepositoryNR = new NoteRepositoryNR(application);
        getAllNotes = noteRepositoryNR.getAllNotesNR;

    }
    public void insertNoteNVM(NotesET notesET){
        noteRepositoryNR.insertNoteNR(notesET);
    }

    public void updateNoteNVM(NotesET notesET){
        noteRepositoryNR.updateNoteNR(notesET);
    }

    public void deleteNoteNVM(int id){
        noteRepositoryNR.deleteNoteNR(id);
    }

}
