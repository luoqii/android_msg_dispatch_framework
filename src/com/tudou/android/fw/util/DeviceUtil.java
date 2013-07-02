package com.tudou.android.fw.util;

import android.os.Build;

public class DeviceUtil {

    public static String fingerPrint(){
        String fingerPrint = "";
        
        fingerPrint += "mode: " + Build.MODEL +
                  " version: " + Build.VERSION.RELEASE;
        
        return fingerPrint;
    }
}
