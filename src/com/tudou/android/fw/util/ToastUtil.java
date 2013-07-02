package com.tudou.android.fw.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    
    private ToastUtil() {};// Utility class, do not instantiate.
    
    /**
     * show a toast. 
     * 
     * @param context
     * @param text
     * @param duration
     */
    public static void toast(Context context, CharSequence text, int duration) {
        Toast.makeText(context, text, duration).show();
    }
}
