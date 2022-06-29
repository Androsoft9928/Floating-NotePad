package com.androsoft.floatingnotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androsoft.floatingnotepad.Model.NotesET;
import com.androsoft.floatingnotepad.ViewModel.NoteViewModelNVM;
import com.androsoft.floatingnotepad.databinding.ActivityUnactivityBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UNActivity extends AppCompatActivity {

    ActivityUnactivityBinding binding;
    ImageView back_home_img;
    FloatingActionButton updateDoneBtn, deleteBtn;
    String strTitle, strSubTitle, strNoteText, strDateTime;
    TextView dateTimeText;
    int iid;
    NoteViewModelNVM noteViewModelNVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateDoneBtn = findViewById(R.id.updateDoneBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        dateTimeText = findViewById(R.id.dateTimeText);

        back_home_img = findViewById(R.id.back_home_img);
        back_home_img.setOnClickListener(v -> {
            Intent intent = new Intent(UNActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        dateTimeText.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));
        noteViewModelNVM = ViewModelProviders.of(this).get(NoteViewModelNVM.class);

        iid = getIntent().getIntExtra("id", 0);
        strTitle = getIntent().getStringExtra("title");
        strSubTitle = getIntent().getStringExtra("subTitle");
        strNoteText = getIntent().getStringExtra("noteText");
        strDateTime = getIntent().getStringExtra("dateTime");

        binding.UpdateInputTitle.setText(strTitle);
        binding.UpdateInputSubTitle.setText(strSubTitle);
        binding.UpdateInputNoteText.setText(strNoteText);
        binding.dateTimeText.setText(strDateTime);

        binding.updateDoneBtn.setOnClickListener(v -> {
            String title = binding.UpdateInputTitle.getText().toString();
            String subTitle = binding.UpdateInputSubTitle.getText().toString();
            String noteText = binding.UpdateInputNoteText.getText().toString();
            String dateTime = binding.dateTimeText.getText().toString();


            UpdateNoteUN(title, subTitle, noteText, dateTime);

        });

        deleteBtn.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialogBSD = new BottomSheetDialog(UNActivity.this, R.style.BottomSheetDialogTheme);
            View bottomSheetViewBSV = LayoutInflater.from(UNActivity.this).inflate(R.layout.delete_layout_bottom_sheet, (LinearLayout)findViewById(R.id.delete_bottom_sheet));
            bottomSheetViewBSV.findViewById(R.id.delete_no_button).setOnClickListener(v1 -> {
                bottomSheetDialogBSD.dismiss();
            });
            bottomSheetViewBSV.findViewById(R.id.delete_yes_button).setOnClickListener(v1 -> {
                noteViewModelNVM.deleteNoteNVM(iid);
                Toast.makeText(this, "note Deleted", Toast.LENGTH_SHORT).show();
                finish();
            });

            bottomSheetDialogBSD.setContentView(bottomSheetViewBSV);
            bottomSheetDialogBSD.show();
        });

    }

    private void UpdateNoteUN(String title, String subTitle, String noteText, String dateTime) {
        NotesET updateNoteET = new NotesET();
        updateNoteET.id = iid;
        updateNoteET.noteTitle = title;
        updateNoteET.noteSubTitle = subTitle;
        updateNoteET.noteText = noteText;
        updateNoteET.noteDate = dateTime;

        noteViewModelNVM.updateNoteNVM(updateNoteET);

        Toast.makeText(UNActivity.this, "Note Updated!!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UNActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}