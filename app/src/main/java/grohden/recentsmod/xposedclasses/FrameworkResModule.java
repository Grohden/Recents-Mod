package grohden.recentsmod.xposedclasses;

import android.content.res.XResources;
import android.content.res.XResources.DimensionReplacement;
import android.util.TypedValue;

import de.robv.android.xposed.IXposedHookZygoteInit;
import static de.robv.android.xposed.XposedBridge.log;

public class FrameworkResModule implements IXposedHookZygoteInit{
	private static String	PACKAGE="android";
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
        //these values are from systemUI recent apps thumbnails, and seems to be on framework-res.apk , why?i don't know.   
        XResources.setSystemWideReplacement(PACKAGE, "dimen", "thumbnail_width", new DimensionReplacement(160, TypedValue.COMPLEX_UNIT_DIP));
        XResources.setSystemWideReplacement(PACKAGE, "dimen", "thumbnail_height", new DimensionReplacement(240, TypedValue.COMPLEX_UNIT_DIP));
		log("Framework Res Xposed");
	}




}
