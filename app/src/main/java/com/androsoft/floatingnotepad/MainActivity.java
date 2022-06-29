package com.androsoft.floatingnotepad;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import com.androsoft.floatingnotepad.Adapter.NoteAdapterNA;
import com.androsoft.floatingnotepad.Model.NotesET;
import com.androsoft.floatingnotepad.ViewModel.NoteViewModelNVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton noteBtn;

    NoteViewModelNVM noteViewModelNVM;
    RecyclerView noteRV;
    NoteAdapterNA noteAdapterNA;

    SearchView andro_search_logo;
    List<NotesET> FilterNoteAllListMA;

    ImageView androMinimizeBtn;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        andro_search_logo = findViewById(R.id.andro_search_logo);
        noteRV = findViewById(R.id.noteRV);
        noteViewModelNVM = ViewModelProviders.of(this).get(NoteViewModelNVM.class);

        noteBtn = findViewById(R.id.noteBtn);
        noteBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CNActivity.class);
            startActivity(intent);
        });

        //-->>
        androMinimizeBtn = findViewById(R.id.androMinimizeBtn);
        if (serviceRunningEnable()) {
            stopService(new Intent(MainActivity.this, FloatingWindowAction.class));
        }

        androMinimizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFloatingWindowPermissionEnable())
                {
                    startService(new Intent(MainActivity.this, FloatingWindowAction.class));
                    finish();
                } else
                {
                    requestFloatingWindowPermissionEnable();
                }
            }
        }); //-->>

        noteViewModelNVM.getAllNotes.observe(this, new Observer<List<NotesET>>() {
            @Override
            public void onChanged(List<NotesET> notesETS) {

                setAdapterSA(notesETS);
                FilterNoteAllListMA = notesETS;
            }
        });

        andro_search_logo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FilterNote(newText);
                return true;
            }
        });
    }



    /*
        isServiceRunning() -->> serviceRunningEnable()
        requestPermission() -->> requestFloatingWindowPermissionEnable()
        checkAndroidVersion() -->> checkFloatingWindowPermissionEnable()
    */

    private boolean serviceRunningEnable(){
        // The ACTIVITY_SERVICE is needed to retrieve a
        // ActivityManager for interacting with the global system
        // It has a constant String value "activity".
        ActivityManager serviceManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        // A loop is needed to get Service information that
        // are currently running in the System.
        // So ActivityManager.RunningServiceInfo is used.
        // It helps to retrieve a
        // particular service information, here its this service.
        // getRunningServices() method returns a list of the
        // services that are currently running
        // and MAX_VALUE is 2147483647. So at most this many services
        // can be returned by this method.
        for (ActivityManager.RunningServiceInfo service : serviceManager.getRunningServices(Integer.MAX_VALUE)) {
            // If this service is found as a running,
            // it will return true or else false.
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
                // The app will redirect to the 'Display over other apps' in Settings.
                // This is an Implicit Intent. This is needed when any Action is needed
                // to perform, here it is
                // redirecting to an other app(Settings).
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
            // If 'Display over other apps' is not enabled
            // it will return false or else true
            if (!Settings.canDrawOverlays(this)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    //-->>

    private void setAdapterSA(List<NotesET> notesETS) {
        noteRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteAdapterNA = new NoteAdapterNA(MainActivity.this, notesETS);
        noteRV.setAdapter(noteAdapterNA);


    }

    private void FilterNote(String newText) {
        List<NotesET> FilterNote = new ArrayList<>();
        for (NotesET singleNote : FilterNoteAllListMA){
            if (singleNote.getNoteTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getNoteSubTitle().toLowerCase().contains(newText.toLowerCase())){
                FilterNote.add(singleNote);
            }

        }
        noteAdapterNA.SearchNote(FilterNote);

    }


}