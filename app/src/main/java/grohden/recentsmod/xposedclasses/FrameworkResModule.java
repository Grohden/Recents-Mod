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
