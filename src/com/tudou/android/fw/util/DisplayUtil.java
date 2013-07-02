
package com.tudou.android.fw.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtil {
    /*
     * device                           screensize (inch)
     *+--------------------------------+----------+
     * google nexus 10                  9.95
     * motorola xoom                    9.92
     * samsung GT-P5110                 9.92
     * asus Transformer Pad TF700T      9.72
     * odna v972                        9.36
     * 
     * asus n7                          7.27
     * samsung p1000                    7.02
     * samsung GT-P3100 (tab 2)         6.77
     * 
     * samsung note 2                   4.92
     * samsung note                     4.78
     * sony c6603                       4.70
     * samsung s 3                      4.80
     * google nexus 4                   4.42
     * htc one x                        4.04
     */
    private static final double PAD_SIZE_THRESHOLD = 8;// inch

    private static double sScreenSize;

    /**
     * get screen size in inch.
     * 
     * @param context
     * @return
     */
    public static double getScreenSize(Context context) {
        if (sScreenSize > 0) {
            return sScreenSize;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(metrics);

        sScreenSize = Math.sqrt(Math.pow(metrics.heightPixels / metrics.ydpi, 2)
                + Math.pow(metrics.widthPixels / metrics.xdpi, 2));

        return sScreenSize;
    }

    public static boolean screenSizeIsPad(Context context) {
        return getScreenSize(context) > PAD_SIZE_THRESHOLD;
    }
}
