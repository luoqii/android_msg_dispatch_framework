package com.tudou.android.fw.util;

import android.content.Context;
import android.content.Intent;

/**
 * 
 * use {@link ToastUtil} instead
 * @author bysong
 *
 */
@Deprecated
public class ActivityUtil {
    private static final String TAG = ActivityUtil.class.getSimpleName();
    
    private ActivityUtil(){}// Utility class, do not instantiate.
    
    /**
     * show a toast. 
     * 
     * @param context
     * @param text
     * @param duration
     */
    @Deprecated
    public static void toast(Context context, CharSequence text, int duration) {
        ToastUtil.toast(context, text, duration);
    }
}
