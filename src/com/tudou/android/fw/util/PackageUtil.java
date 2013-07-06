package com.tudou.android.fw.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtil {
    public static final String PACKAGE_FLASH_PLAYER = "com.adobe.flashplayer";
    private static final String TAG = PackageUtil.class.getSimpleName();
    
    public static boolean hasPackageInstalled(Context context, String packagename) {
        boolean installed = false;
        PackageManager pm = context.getPackageManager();
        try {
            installed = pm
                    .getPackageInfo("com.adobe.flashplayer", 0) != null;
        } catch (NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundException for package: " + packagename);
        }
        
        return installed;
    }
}
