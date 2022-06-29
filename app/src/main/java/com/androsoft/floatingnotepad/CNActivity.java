package com.androsoft.floatingnotepad;

import static com.androsoft.floatingnotepad.commonModeSize.commonModeSize.currentNoteText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androsoft.floatingnotepad.Model.NotesET;
import com.androsoft.floatingnotepad.ViewModel.NoteViewModelNVM;
import com.androsoft.floatingnotepad.databinding.ActivityCnactivityBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CNActivity extends AppCompatActivity {

    ActivityCnactivityBinding binding;
    String title, subTitle, noteText, dateTime;
    EditText inputTitle, inputSubTitle, inputNoteText;
    TextView dateTimeText;
    FloatingActionButton doneBtn;

    NoteViewModelNVM noteViewModelNVM;

    ImageView back_home_img;

    ImageView androMinimizeBtn;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCnactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inputTitle = findViewById(R.id.inputTitle);
        inputSubTitle = findViewById(R.id.inputSubTitle);
        inputNoteText = findViewById(R.id.inputNoteText);
        dateTimeText = findViewById(R.id.dateTimeText);
        doneBtn = findViewById(R.id.doneBtn);

        //-->>

        androMinimizeBtn = findViewById(R.id.androMinimizeBtn);
        inputNoteText = findViewById(R.id.inputNoteText);

        if (serviceRunningEnable()) {
            stopService(new Intent(CNActivity.this, FloatingWindowAction.class));
        }

        inputNoteText.setText(currentNoteText);
        inputNoteText.setSelection(inputNoteText.getText().toString().length());

        inputNoteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentNoteText = inputNoteText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        androMinimizeBtn.setOnClickListener(v -> {
            if (checkFloatingWindowPermissionEnable()) {
                startService(new Intent(CNActivity.this, FloatingWindowAction.class));
                finish();
            } else {
                requestFloatingWindowPermissionEnable();
            }
        });

        //-->>

        back_home_img = findViewById(R.id.back_home_img);
        back_home_img.setOnClickListener(v -> {
            Intent intent = new Intent(CNActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        dateTimeText.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));

        noteViewModelNVM = ViewModelProviders.of(this).get(NoteViewModelNVM.class);


        binding.doneBtn.setOnClickListener(v -> {
            title = binding.inputTitle.getText().toString();
            subTitle = binding.inputSubTitle.getText().toString();
            noteText = binding.inputNoteText.getText().toString();
            dateTime = binding.dateTimeText.getText().toString();


            CreateNoteCN(title, subTitle, noteText, dateTime);

        });

    }

    private void CreateNoteCN(String title, String subTitle, String noteText, String dateTime) {

        NotesET notesET = new NotesET();
        notesET.noteTitle = title;
        notesET.noteSubTitle = subTitle;
        notesET.noteText = noteText;
        notesET.noteDate = dateTime;

        noteViewModelNVM.insertNoteNVM(notesET);

        Toast.makeText(CNActivity.this, "Text Saved!!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CNActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //-->>

    /*
        isServiceRunning() -->> serviceRunningEnable()
        requestPermission() -->> requestFloatingWindowPermissionEnable()
        checkAndroidVersion() -->> checkFloatingWindowPermissionEnable()
    */



    private boolean serviceRunningEnable() {
        ActivityManager serviceManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : serviceManager.getRunningServices(Integer.MAX_VALUE)) {
            if (FloatingWindowAction.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void requestFloatingWindowPermissionEnable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Screen Overlay Permission Needed");

//        builder.setMessage("Enable 'Display over other apps' from System Settings.");
        builder.setMessage("Enable 'Display over other apps' from System Settings.");
        builder.setMessage("Please tap on ' OPEN SETTINGS ',  then choose Floating Notes App in the list and active the service.");

        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                // This method will start the intent. It takes two parameter, one is the Intent and the other is
                // an requestCode Integer. Here it is -1.
                startActivityForResult(intent, RESULT_OK);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }


    private boolean checkFloatingWindowPermissionEnable() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}