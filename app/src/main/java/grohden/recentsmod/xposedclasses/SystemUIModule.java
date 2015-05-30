package grohden.recentsmod.xposedclasses;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import grohden.recentsmod.ModuleActivity;
import grohden.recentsmod.R;
import grohden.recentsmod.Utils;

public class SystemUIModule implements IXposedHookZygoteInit, IXposedHookInitPackageResources, IXposedHookLoadPackage {

    @SuppressWarnings("unused")
    private static String SYSTEM_UI_PACKAGE = "com.android.systemui";
    private static boolean DEBUG= ModuleActivity.DEBUG;
    private static String MODULE_PATH = null;
    private static final String CLASS_RECENT_PANEL_VIEW = "com.android.systemui.recent.RecentsPanelView";

    // TODO: cleanup the code.
    // TODO: fix 'minimization' animation position.

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(final InitPackageResourcesParam resparam) throws Throwable {
        if (!resparam.packageName.equals(SYSTEM_UI_PACKAGE))
            return;
        final XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);

        //Set sizes.
        resparam.res.setReplacement(SYSTEM_UI_PACKAGE, "dimen", "status_bar_recents_thumbnail_height", modRes.fwd(R.dimen.thumbnail_height));
        resparam.res.setReplacement(SYSTEM_UI_PACKAGE, "dimen", "status_bar_recents_thumbnail_width", modRes.fwd(R.dimen.thumbnail_width));
        resparam.res.setReplacement(SYSTEM_UI_PACKAGE, "bool", "config_recents_thumbnail_image_fits_to_xy", true);

        resparam.res.hookLayout(SYSTEM_UI_PACKAGE, "layout", "status_bar_recent_item", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(XC_LayoutInflated.LayoutInflatedParam liparam) throws Throwable {
                if (liparam.variant.equals("layout")) {

                    //Should not rotate the parent(RootView(FrameLayout)) because it will change the 'fade'/'dispense' direction.
                    RelativeLayout  recentItem = (RelativeLayout) liparam.view.findViewById(liparam.res.getIdentifier("recent_item", "id", SYSTEM_UI_PACKAGE));
                    FrameLayout     appThumbnail = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("app_thumbnail", "id", SYSTEM_UI_PACKAGE));
                    ImageView       appIcon = (ImageView) liparam.view.findViewById(liparam.res.getIdentifier("app_icon", "id", SYSTEM_UI_PACKAGE));
                    TextView        appName = (TextView) liparam.view.findViewById(liparam.res.getIdentifier("app_label", "id", SYSTEM_UI_PACKAGE));
                    View            line = liparam.view.findViewById(liparam.res.getIdentifier("recents_callout_line", "id", SYSTEM_UI_PACKAGE));
                    //not remove views, instead set INVISIBLE to not crash something.s
                    RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    iconParams.addRule(RelativeLayout.BELOW, appThumbnail.getId());
                    iconParams.addRule(RelativeLayout.ALIGN_LEFT, appThumbnail.getId());
                    appIcon.setLayoutParams(iconParams);

                    //RelativeLayout.LayoutParams appNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    //appNameParams.addRule(RelativeLayout.ALIGN_BOTTOM, appIcon.getId());
                    //appNameParams.addRule(RelativeLayout.ALIGN_TOP, appIcon.getId());
                    //appName.setLayoutParams(appNameParams);

                    recentItem.setRotation(-90.0f);
                }
            }

        });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals(SYSTEM_UI_PACKAGE))
            return;
        Class<?> recentPanelViewClass = XposedHelpers.findClass(CLASS_RECENT_PANEL_VIEW, loadPackageParam.classLoader);
        //tried this to fix minimization, not worked.
        XposedHelpers.findAndHookMethod(recentPanelViewClass, "showIfReady", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final XC_MethodHook.MethodHookParam param) {
                View root = (View) param.thisObject;
                Resources res = root.getResources();
                Context context = root.getContext();

                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int width = display.getWidth();  // deprecated
                int height = display.getHeight();  // deprecated

                //The original view is a custom view that extends a scrollview, this should work.
                ScrollView recentsContainer = (ScrollView) root.findViewById(res.getIdentifier("recents_container", "id", SYSTEM_UI_PACKAGE));
                //recentsContainer.setBackgroundColor(Color.parseColor("#000000"));
                recentsContainer.setRotation(90.0f);
                FrameLayout.LayoutParams scrollLayoutParams = new FrameLayout.LayoutParams(height, width);
                scrollLayoutParams.gravity = Gravity.CENTER;
                recentsContainer.setLayoutParams(scrollLayoutParams);


                LinearLayout recentsLinearLayout = (LinearLayout) root.findViewById(res.getIdentifier("recents_linear_layout", "id", SYSTEM_UI_PACKAGE));

                FrameLayout.LayoutParams llp=new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getInDIP(20, res));
                recentsLinearLayout.setLayoutParams(llp);
            }
        });
    }
}