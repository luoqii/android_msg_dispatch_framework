package com.tudou.android.fw.compatibility.v4;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

public class ViewCompat {
    private static final String TAG = ViewCompat.class.getSimpleName();
    
    @SuppressLint("NewApi")
    public static void setLayerType(View v, int layerType, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            v.setLayerType(layerType, paint);
        }
    }
}
