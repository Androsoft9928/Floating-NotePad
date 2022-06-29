package com.androsoft.floatingnotepad.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androsoft.floatingnotepad.MainActivity;
import com.androsoft.floatingnotepad.Model.NotesET;
import com.androsoft.floatingnotepad.R;
import com.androsoft.floatingnotepad.UNActivity;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapterNA extends RecyclerView.Adapter<NoteAdapterNA.noteViewHolder> {

    MainActivity mainActivity;
    List<NotesET> notesET;

    List<NotesET> allNoteItemSearch;

    public NoteAdapterNA(MainActivity mainActivity, List<NotesET> notesET) {
        this.mainActivity = mainActivity;
        this.notesET = notesET;
        allNoteItemSearch = new ArrayList<>(notesET);
    }

    public void SearchNote(List<NotesET> filterNote){
        this.notesET = filterNote;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new noteViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.item_notes_layout_inl, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, int position) {
        holder.noteTitelView.setText(notesET.get(position).noteTitle);
        holder.noteSubTitleView.setText(notesET.get(position).noteSubTitle);
        holder.noteDateView.setText(notesET.get(position).noteDate);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, UNActivity.class);
            intent.putExtra("id", notesET.get(position).id);
            intent.putExtra("title", notesET.get(position).noteTitle);
            intent.putExtra("subTitle", notesET.get(position).noteSubTitle);
            intent.putExtra("noteText", notesET.get(position).noteText);
            intent.putExtra("dateTime", notesET.get(position).noteDate);
            mainActivity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return notesET.size();
    }

//    public void filterList(List<NotesET> notesETList){
//        notesET = notesETList;
//        notifyDataSetChanged();
//
//    }

    class noteViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitelView, noteSubTitleView,noteDateView;
        public noteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitelView = itemView.findViewById(R.id.noteTitelView);
            noteSubTitleView = itemView.findViewById(R.id.noteSubTitleView);
            noteDateView = itemView.findViewById(R.id.noteDateView);
        }
    }

}
