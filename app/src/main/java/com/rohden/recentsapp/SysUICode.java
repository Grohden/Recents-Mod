package com.rohden.recentsapp;


import android.app.Activity;
import android.content.Context;
import android.content.res.XModuleResources;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.recentsapp.R;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class SysUICode implements IXposedHookZygoteInit, IXposedHookInitPackageResources, IXposedHookLoadPackage {

    @SuppressWarnings("unused")
    private static String PACKAGE = "com.android.systemui";
    private static String MODULE_PATH = null;
    private static String TAG = "ramones.rohden";
    private Context context;


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(PACKAGE))
            return;
        final XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        resparam.res.setReplacement(PACKAGE, "dimen", "status_bar_recents_thumbnail_height", modRes.fwd(R.dimen.thumbnail_height));
        resparam.res.setReplacement(PACKAGE, "dimen", "status_bar_recents_thumbnail_width", modRes.fwd(R.dimen.thumbnail_width));
        resparam.res.setReplacement(PACKAGE, "bool", "config_recents_thumbnail_image_fits_to_xy", true);


        resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_recent_panel", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(XC_LayoutInflated.LayoutInflatedParam liparam) throws Throwable {
                //ScrollView because the original view extends it.
                ScrollView layoutportrait = (ScrollView) liparam.view.findViewById(
                        liparam.res.getIdentifier("recents_container", "id", PACKAGE));

                //this holds the scrollview , and the scrollview hold linearLayout.
                FrameLayout frameLayout = (FrameLayout) liparam.view.findViewById(
                        liparam.res.getIdentifier("recents_bg_protect", "id", PACKAGE));

                LinearLayout linearLayout = (LinearLayout) liparam.view.findViewById(
                        liparam.res.getIdentifier("recents_linear_layout", "id", PACKAGE));


                context=layoutportrait.getContext(); //here i'm 'stoling' the context from one view of systemUI.
                //Toast.makeText(context, "context stoled" ,Toast.LENGTH_LONG).show();
            }
        });





    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.android.systemui"))
            return;

        findAndHookMethod("com.android.systemui.recent.RecentsActivity", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity=(Activity) param.thisObject; //The RecentsActivity extends..well..an Activity :v

                Toast.makeText(activity.getApplicationContext(),"Context Stoled.",Toast.LENGTH_SHORT).show(); //another example to how stole a context.
                activity.setContentView(com.example.recentsapp.R.layout.test); //this seems to not work.
            }
        });
    }
}