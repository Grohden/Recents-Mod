package grohden.recentsmod.xposedclasses;


import android.content.Context;
import android.content.res.XModuleResources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import static de.robv.android.xposed.XposedBridge.log;

import grohden.recentsmod.ModuleActivity;
import grohden.recentsmod.R;
import grohden.recentsmod.ViewGroupUtils;

public class SystemUIModule implements IXposedHookZygoteInit, IXposedHookInitPackageResources {

    @SuppressWarnings("unused")
    private static String SYSTEM_UI_PACKAGE = "com.android.systemui";
    private static boolean DEBUG= ModuleActivity.DEBUG;

    private static String CLASS_RECENT_PANEL_VIEW = "com.android.systemui.recent.RecentsPanelView";
    private static String CLASS_HORIZONTAL_SCROLL_VIEW = "com.android.systemui.recent.RecentsHorizontalScrollView";
    private static String CLASS_VERTICAL_SCROLL_VIEW = "com.android.systemui.recent.RecentsVerticalScrollView";
    private static String MODULE_PATH = null;


    // TODO: cleanup the code.

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(final InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(SYSTEM_UI_PACKAGE))
            return;
        log("system ui Xposed");
        final XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);

        resparam.res.setReplacement(SYSTEM_UI_PACKAGE, "dimen", "status_bar_recents_thumbnail_height", modRes.fwd(R.dimen.thumbnail_height));
        resparam.res.setReplacement(SYSTEM_UI_PACKAGE, "dimen", "status_bar_recents_thumbnail_width", modRes.fwd(R.dimen.thumbnail_width));
        resparam.res.setReplacement(SYSTEM_UI_PACKAGE, "bool", "config_recents_thumbnail_image_fits_to_xy", true);

        resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_recent_panel", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(XC_LayoutInflated.LayoutInflatedParam liparam) throws Throwable {
                if (liparam.variant.equals("layout")) {
                    //this holds the scrollview , and the scrollview hold linearLayout.
                    FrameLayout frameLayout = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("recents_bg_protect", "id", SYSTEM_UI_PACKAGE));
                    //frameLayout.setBackgroundColor(Color.parseColor("#9FA8DA"));

                    //ScrollView because the original view extends it.
                    ScrollView layoutportrait = (ScrollView) liparam.view.findViewById(liparam.res.getIdentifier("recents_container", "id", SYSTEM_UI_PACKAGE));
                    //layoutportrait.setBackgroundColor(Color.parseColor("#00BCD4"));
                    //layoutportrait.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    layoutportrait.setRotation(90.0f);

                    LinearLayout linearLayout = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("recents_linear_layout", "id", SYSTEM_UI_PACKAGE));
                    //linearLayout.setBackgroundColor(Color.parseColor("#0D47A1"));

                }
            }

        });
        resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_recent_item", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(XC_LayoutInflated.LayoutInflatedParam liparam) throws Throwable {
                if (liparam.variant.equals("layout")) {
                    FrameLayout frameLayout = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("recent_item", "id", SYSTEM_UI_PACKAGE)).getParent();
                    frameLayout.setRotation(-90.0f);
                }
            }

        });
    }

}