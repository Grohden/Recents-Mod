package com.rohden.recentsapp;


import android.content.res.XModuleResources;

import com.example.recentsapp.R;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;


public class SysUICode implements  IXposedHookZygoteInit, IXposedHookInitPackageResources {
    @SuppressWarnings("unused")
    private static String PACKAGE = "com.android.systemui",
            MODULE_PATH = null,
            TAG = "SystemUI Xposed";

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(PACKAGE))
            return;
        XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
        resparam.res.setReplacement(PACKAGE, "dimen", "status_bar_recents_thumbnail_height", modRes.fwd(R.dimen.thumbnail_height));
        resparam.res.setReplacement(PACKAGE, "dimen", "status_bar_recents_thumbnail_width", modRes.fwd(R.dimen.thumbnail_width));
        resparam.res.setReplacement(PACKAGE, "bool", "config_recents_thumbnail_image_fits_to_xy", true);

    }
}


