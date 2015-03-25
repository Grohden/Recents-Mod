package com.rohden.recentsapp;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class ClockCode implements IXposedHookLoadPackage {

private static String PACKAGE="com.android.systemui";

 @Override
 public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
  if (!lpparam.packageName.equals(PACKAGE))
            return;
  
   findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            	//The clock is a TextView so we can make a cast to TextView
            	TextView tv = (TextView) param.thisObject;
            	//reads the value which is saves, using nothing as default value to use if nothing is saved
            	XSharedPreferences pref = new XSharedPreferences("com.example.recentsapp", "user_settings"); //THIS FUCK IS FROM GEN FOLDER! I'VE SPEND ONE DAY JUST TO KNOW THIS!
            	//Gets the shared preference
            	String text = pref.getString("clock_text", "");
            	//reads the value which is saves, using nothing as default value to use if nothing is saved
            	tv.append(text);
             
            }
 });
 }
}