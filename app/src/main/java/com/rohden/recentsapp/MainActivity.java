package com.rohden.recentsapp;

import java.io.DataOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recentsapp.R;
import com.gc.materialdesign.views.ButtonRectangle;

public class MainActivity extends Activity {
    //android vars
	private EditText eClockText,eRecentWidth,eRecentHeight;
	private String clockText;
	private int recentWidth,recentHeight;
	private View vSquare;
    private Boolean firstRun;
    private SharedPreferences xposedPreferences;

    //Material lib vars
    private ButtonRectangle apply,restartSysUI;


    //private static final String TAG="Recent";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		eClockText =(EditText) findViewById(R.id.clock_text);
		eRecentWidth=(EditText) findViewById(R.id.recent_width);
		eRecentHeight=(EditText) findViewById(R.id.recent_heigth);


        //for xposed seems like we NEED to use MODE_WORLD_READABLE.
        xposedPreferences = getSharedPreferences("user_settings", MODE_WORLD_READABLE);

        if(!getSharedPreferences("user_settings",MODE_PRIVATE).getBoolean("firstRun", true)){
            eClockText.setText(xposedPreferences.getString("clock_text", ""));
            eRecentHeight.setText(xposedPreferences.getInt("recent_height",0)+"");
            eRecentWidth.setText(xposedPreferences.getInt("recent_width",0)+"");
           }
	    else{ firstRunSettings(); }

        setSquareSize(xposedPreferences.getInt("recent_width",0),xposedPreferences.getInt("recent_height",0));
        //buttons listeners
		apply=(ButtonRectangle)findViewById(R.id.apply);
		apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {applySettings();}});

        restartSysUI=(ButtonRectangle)findViewById(R.id.sysui);
        restartSysUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {killPackage("com.android.systemui");applySettings();}
            });

	}
    private void applySettings(){

        //If text fields null
        if(eRecentWidth.getText().toString().equals("")) recentWidth = xposedPreferences.getInt("recent_width", 0);
        else recentWidth = Integer.parseInt(eRecentWidth.getText().toString());

        if(eRecentHeight.getText().toString().equals("")) recentHeight = xposedPreferences.getInt("recent_height", 0);
        else recentHeight=Integer.parseInt(eRecentHeight.getText().toString());

        setSquareSize(recentWidth,recentHeight);

        if(eClockText.getText().toString().equals("")) clockText = xposedPreferences.getString("clock_text", "");
        else clockText=eClockText.getText().toString();



        //Put values in SharedPreferences
        Editor editor = xposedPreferences.edit();
        editor.putString("clock_text", clockText);
        editor.putInt("recent_width", recentWidth);
        editor.putInt("recent_height", recentHeight);
        editor.apply();
    }
    private void firstRunSettings(){
        getSharedPreferences("user_settings",MODE_PRIVATE).edit().putBoolean("firstRun", false).commit();
        Editor editor = xposedPreferences.edit();
        editor.putString("clock_text", "");
        editor.putInt("recent_width", 164);
        editor.putInt("recent_height", 145);
        editor.apply();
        applySettings();
        }

    private void setSquareSize(int width, int height){
        vSquare=(View) findViewById(R.id.square);
        vSquare.getLayoutParams().height=height;
        vSquare.getLayoutParams().width=width;
        vSquare.setLayoutParams(vSquare.getLayoutParams());

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
        if (su != null ){
            try {
                DataOutputStream os = new DataOutputStream(su.getOutputStream());  
                os.writeBytes("pkill " + packageToKill + "\n"); 
                os.flush(); 
                os.writeBytes("exit\n"); 
                os.flush(); 
                su.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        } 
    }

}
