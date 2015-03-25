package com.rohden.recentsapp;

import java.io.DataOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recentsapp.R;

public class MainActivity extends Activity {
	private EditText eClockText,eRecentWidth,eRecentHeight;
	private String clockText;
	private int recentWidth,recentHeight;
	private View vSquare;
	private Button apply,restartSysUI;
	//private static final String TAG="Recent";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		eClockText =(EditText) findViewById(R.id.clock_text);
		eRecentWidth=(EditText) findViewById(R.id.recent_width);
		eRecentHeight=(EditText) findViewById(R.id.recent_heigth);
		vSquare=(View) findViewById(R.id.square);
	
		apply=(Button)findViewById(R.id.apply);
		apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockText=eClockText.getText().toString();

                //If text fields null
                if(eRecentWidth.getText().toString().equals("")) recentWidth = 164;
                else recentWidth = Integer.parseInt(eRecentWidth.getText().toString());

                if(eRecentHeight.getText().toString().equals("")) recentHeight = 145;
                else recentHeight=Integer.parseInt(eRecentHeight.getText().toString());

                //draw the square
                vSquare.getLayoutParams().height=recentHeight;
                vSquare.getLayoutParams().width=recentWidth;
                vSquare.setLayoutParams(vSquare.getLayoutParams());

               //for xposed seems like we NEED to use MODE_WORLD_READABLE.
                @SuppressWarnings("deprecation")
                SharedPreferences pref;
                pref = getSharedPreferences("user_settings", MODE_WORLD_READABLE);

                Editor editor = pref.edit();
                editor.putString("clock_text", clockText);
                editor.putInt("recent_width", recentWidth);
                editor.putInt("recent_height", recentHeight);
                editor.apply();

            }
        });

        restartSysUI=(Button)findViewById(R.id.sysui);
        restartSysUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killPackage("com.android.systemui");
            }
        }
        );
	}

	
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