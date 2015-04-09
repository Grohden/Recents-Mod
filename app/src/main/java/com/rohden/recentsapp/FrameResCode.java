package com.rohden.recentsapp;

import android.content.res.XResources;
import android.content.res.XResources.DimensionReplacement;
import android.util.TypedValue;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;

public class FrameResCode implements IXposedHookZygoteInit{
	private static String	PACKAGE="android";
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		
        //-----------Shared preferences------------//
		XSharedPreferences pref = new XSharedPreferences("com.example.recentsapp", "user_settings");
        
        float width=pref.getFloat("recent_width",0);
        float height=pref.getFloat("recent_height",0);
        //-----------------------------------------//
        
        //these values are from systemUI recent apps thumbnails, and seems to be on framework-res.apk , why?i don't know.   
        XResources.setSystemWideReplacement(PACKAGE, "dimen", "thumbnail_width", new DimensionReplacement(width, TypedValue.COMPLEX_UNIT_DIP));
        XResources.setSystemWideReplacement(PACKAGE, "dimen", "thumbnail_height", new DimensionReplacement(height, TypedValue.COMPLEX_UNIT_DIP));

	}



}
