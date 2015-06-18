/*
 * Copyright 2015 Gabriel de Oliveira Rohden
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    // TODO: add debug logs.
    // TODO: create classes for view manipulation and other things.
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

                    //don't remove views, instead set GONE to not crash something.
                    appName.setVisibility(View.GONE);
                    appIcon.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    iconParams.addRule(RelativeLayout.BELOW, appThumbnail.getId());
                    iconParams.addRule(RelativeLayout.ALIGN_LEFT, appThumbnail.getId());
                    appIcon.setLayoutParams(iconParams);

                    //-90 coz i've rotated the scrollview 90.
                    recentItem.setRotation(-90.0f);
                }
            }

        });
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals(SYSTEM_UI_PACKAGE))
            return;
        final Class<?> recentPanelViewClass = XposedHelpers.findClass(CLASS_RECENT_PANEL_VIEW, loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod(recentPanelViewClass, "showIfReady", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(final XC_MethodHook.MethodHookParam param) {
                        View root = (View) param.thisObject;
                        Resources res = root.getResources();
                        Context context = root.getContext();

                        //getting display width and height.
                        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        Display display = wm.getDefaultDisplay();
                        int width = display.getWidth();  // deprecated
                        int height = display.getHeight();  // deprecated

                        //The original view is a custom view that extends a scrollview, this should work.
                        ScrollView recentsContainer = (ScrollView) root.findViewById(res.getIdentifier("recents_container", "id", SYSTEM_UI_PACKAGE));
                        //this is the best way i've found until now, others may crash the system. i'll explore more later, for now its 'good'.
                        recentsContainer.setRotation(90.0f);

                        //since i've rotated the view, it don't fit in the screen, so i need to invert height and width, and apply.
                        FrameLayout.LayoutParams scrollLayoutParams = new FrameLayout.LayoutParams(height, width);
                        //and of course, center it.
                        scrollLayoutParams.gravity = Gravity.CENTER;
                        recentsContainer.setLayoutParams(scrollLayoutParams);
                        recentsContainer.requestLayout();

                        LinearLayout recentsLinearLayout = (LinearLayout) root.findViewById(res.getIdentifier("recents_linear_layout", "id", SYSTEM_UI_PACKAGE));

                        FrameLayout.LayoutParams llp=new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getInDIP(20, res));
                        recentsLinearLayout.setLayoutParams(llp);
                    }
                });
    }
}