package com.rohden.recentsapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recentsapp.R;
import com.rey.material.widget.Slider;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private String clockText;
    private float recentWidth;
    private float recentHeight;


    //android vars
	private EditText eClockText;
    private SharedPreferences xposedPreferences;
    private View vSquare;
    private boolean firstRun;

    //Material lib vars
    private Button bApply;
    private Button bRestarSystemUI;
    private Slider sRecentWidth;
    private Slider sRecentHeight;

    //Custom classes
    ViewAnimator animator;
    //private static final String TAG="RecentApp.MainActivity";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        TODO
        Implement new material design lib(rey material).
        */

        //Instantiate Views
        bApply          = (Button) findViewById(R.id.apply);
        bRestarSystemUI = (Button) findViewById(R.id.restart_systemUI);
        eClockText      = (EditText) findViewById(R.id.clock_text);
        sRecentWidth    = (Slider) findViewById(R.id.recent_width);
        sRecentHeight   = (Slider) findViewById(R.id.recent_height);
        vSquare         = findViewById(R.id.square);

        //for xposed seems like we NEED to use MODE_WORLD_READABLE.
        xposedPreferences = getSharedPreferences("user_settings", MODE_WORLD_READABLE);
        firstRun          = getSharedPreferences("user_settings", MODE_PRIVATE).getBoolean("firstRun", true);

        //FirstRun settings
        if (firstRun) {
            firstRunSettings();
        }

        //Vars to hold xposed preferences
        recentWidth = xposedPreferences.getFloat("recent_width", 0);
        recentHeight = xposedPreferences.getFloat("recent_height", 0);

        eClockText.setText(xposedPreferences.getString("clock_text", ""));
        sRecentWidth.setValue(getInDIP(recentWidth), true);
        sRecentHeight.setValue(getInDIP(recentHeight), true);

        //Listeners
        bApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applySettings();
            }
        });

        bRestarSystemUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killPackage("com.android.systemui");
                applySettings();
            }
        });

        sRecentWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "release", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applySettings(){
        //If text null
        if(eClockText.getText().toString().equals("")){
            clockText = xposedPreferences.getString("clock_text", "");
        } else {
            clockText=eClockText.getText().toString();
        }

        savePreferences(clockText,recentWidth,recentHeight);

    }

    private void firstRunSettings(){
        getSharedPreferences("user_settings",MODE_PRIVATE).edit().putBoolean("firstRun", false).commit();
        savePreferences("", 145f, 164f);
        setSquareSize(145, 165);
        }

    private float getInDIP(int value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics());
    }

    private float getInDIP(float value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics());
    }

    //superuser method
    private void killPackage(String packageToKill) {
        Process su = null;
        // get superuser
        try {
            su = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // kill given package
        if (su != null ) try {
            DataOutputStream os = new DataOutputStream(su.getOutputStream());
            os.writeBytes("pkill " + packageToKill + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            su.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void savePreferences(String text,float width, float height){
        //Put values in SharedPreferences
        Editor editor = xposedPreferences.edit();
        editor.putString("clock_text", text);
        editor.putFloat("recent_width", 200);
        editor.putFloat("recent_height", 290);
        editor.apply();
    }

    private void setSquareSize(int width, int height) {
        animator=new ViewAnimator(vSquare);
        animator.setInterpolator(new BounceInterpolator());
        animator.animateHeight(Math.round(xposedPreferences.getFloat("recent_height",0)),height);
        animator.animateWidth(Math.round(xposedPreferences.getFloat("recent_width", 0)),width);

    }


}
