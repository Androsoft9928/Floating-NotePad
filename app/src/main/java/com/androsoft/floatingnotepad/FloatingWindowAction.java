package com.androsoft.floatingnotepad;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.androsoft.floatingnotepad.commonModeSize.commonModeSize;


public class FloatingWindowAction extends Service {


    private ViewGroup floatViewGroup;
    private int LAYOUT_TYPE;
    private WindowManager.LayoutParams floatingWindowLayoutParam;
    private WindowManager windowManager;
    private AppCompatButton androMaximizeBtn;
    private EditText inputNoteText;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();



        DisplayMetrics coreMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = coreMetrics.widthPixels;
        int height = coreMetrics.heightPixels;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflaterInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        floatViewGroup = (ViewGroup) layoutInflaterInflater.inflate(R.layout.floating_layout_minisize, null);


        androMaximizeBtn = floatViewGroup.findViewById(R.id.androMaximizeBtn);
        inputNoteText = floatViewGroup.findViewById(R.id.inputNoteText);

        inputNoteText.setText(commonModeSize.currentNoteText);
        inputNoteText.setSelection(inputNoteText.getText().toString().length());
        inputNoteText.setCursorVisible(false);

        //

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST;
        }

        //

        floatingWindowLayoutParam = new WindowManager.LayoutParams(
                (int) (width * (0.55f)),
                (int) (height * (0.45f)),
                LAYOUT_TYPE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        //

        floatingWindowLayoutParam.gravity = Gravity.CENTER;
        floatingWindowLayoutParam.x = 0;
        floatingWindowLayoutParam.y = 0;
        windowManager.addView(floatViewGroup, floatingWindowLayoutParam);

        //

        inputNoteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                commonModeSize.currentNoteText = inputNoteText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //

        floatViewGroup.setOnTouchListener(new View.OnTouchListener() {
            final WindowManager.LayoutParams floatWindowLayoutUpdateUpdateParam = floatingWindowLayoutParam;
            double x;
            double y;
            double px;
            double py;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        x = floatingWindowLayoutParam.x;
                        y = floatWindowLayoutUpdateUpdateParam.y;

                        px = event.getRawX();

                        py = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        floatWindowLayoutUpdateUpdateParam.x = (int) ((x + event.getRawX()) - px);
                        floatWindowLayoutUpdateUpdateParam.y = (int) ((y + event.getRawY()) - py);

                        // updated parameter is applied to the WindowManager
                        windowManager.updateViewLayout(floatViewGroup, floatWindowLayoutUpdateUpdateParam);
                        break;
                }
                return false;
            }
        });

        //

        androMaximizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();

                // The window is removed from the screen
                windowManager.removeView(floatViewGroup);

                // The app will maximize again. So the MainActivity
                // class will be called again.
                Intent intentHome = new Intent(FloatingWindowAction.this, CNActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
            }
        });

        //

        inputNoteText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputNoteText.setCursorVisible(true);
                WindowManager.LayoutParams floatWindowLayoutParamUpdateFlagFlag = floatingWindowLayoutParam;
                // Layout Flag is changed to FLAG_NOT_TOUCH_MODAL which
                // helps to take inputs inside floating window, but
                // while in EditText the back button won't work and
                // FLAG_LAYOUT_IN_SCREEN flag helps to keep the window
                // always over the keyboard
                floatWindowLayoutParamUpdateFlagFlag.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

                // WindowManager is updated with the Updated Parameters
                windowManager.updateViewLayout(floatViewGroup, floatWindowLayoutParamUpdateFlagFlag);
                return false;
            }
        });
    }

    //

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopSelf();
            windowManager.removeView(floatViewGroup);


    }
}
