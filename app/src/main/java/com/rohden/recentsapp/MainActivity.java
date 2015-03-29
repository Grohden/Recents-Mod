package com.rohden.recentsapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Transformation;
import android.widget.EditText;

import com.example.recentsapp.R;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.Slider;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    //android vars
	private EditText eClockText;
	private String clockText;
	private int recentWidth,recentHeight;
	private View vSquare;
    private Boolean firstRun;
    private SharedPreferences xposedPreferences;

    //Material lib vars
    private ButtonRectangle apply,restartSysUI;
    private Slider sRecentWidth,sRecentHeight;


    //private static final String TAG="Recent";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		eClockText =(EditText) findViewById(R.id.clock_text);
		sRecentWidth=(Slider) findViewById(R.id.recent_width);
		sRecentHeight=(Slider) findViewById(R.id.recent_height);


        //for xposed seems like we NEED to use MODE_WORLD_READABLE.
        xposedPreferences = getSharedPreferences("user_settings", MODE_WORLD_READABLE);

        if(!getSharedPreferences("user_settings",MODE_PRIVATE).getBoolean("firstRun", true)){
            eClockText.setText(xposedPreferences.getString("clock_text", ""));
            sRecentHeight.setValue(xposedPreferences.getInt("recent_height", 0));
            sRecentWidth.setValue(xposedPreferences.getInt("recent_width", 0));
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

        sRecentHeight.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int i) {
                recentHeight=i;
            }
        });
        sRecentWidth.setOnValueChangedListener(new Slider.OnValueChangedListener() {
            @Override
            public void onValueChanged(int i) {
                recentWidth=i;
            }
        });
	}

    private void applySettings(){
        setSquareSize(recentWidth,recentHeight);
        //If text null
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

    private void setSquareSize(final int width, final int height) {
        vSquare = (View) findViewById(R.id.square);
        ValueAnimator vlaw = ValueAnimator.ofInt(xposedPreferences.getInt("recent_height", 0), height);
        vlaw.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                vSquare.getLayoutParams().height = value.intValue();
                vSquare.requestLayout();
            }
        });
        ValueAnimator vlah = ValueAnimator.ofInt(xposedPreferences.getInt("recent_width", 0), width);
        vlah.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                vSquare.getLayoutParams().width = value.intValue();
                vSquare.requestLayout();
            }
        });

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(vlah).with(vlaw);
        animatorSet.start();
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
