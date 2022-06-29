package com.androsoft.floatingnotepad.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "NotesET_database")
public class NotesET {

    @PrimaryKey(autoGenerate = true)
    public
    int id;

    //annotations --> @ColumnInfo.
    @ColumnInfo(name = "note_title")
    public
    String noteTitle;

    @ColumnInfo (name = "note_subtitle")
    public
    String noteSubTitle;

    @ColumnInfo (name = "note_text")
    public
    String noteText;

    @ColumnInfo (name = "note_date")
    public
    String noteDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteSubTitle() {
        return noteSubTitle;
    }

    public void setNoteSubTitle(String noteSubTitle) {
        this.noteSubTitle = noteSubTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }
}
