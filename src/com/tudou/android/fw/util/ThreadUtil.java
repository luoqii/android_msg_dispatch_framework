package com.tudou.android.fw.util;

public class ThreadUtil {
    private static final String TAG = ThreadUtil.class.getSimpleName();
    
    private ThreadUtil(){};// Utility class, do not instantiate.
    
    /**
     * ingore any exception
     * @param millis
     */
    public static void sleep(int millis){
        try {
            Log.d(TAG, "sleep " + millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException", e);
        }
    }
}
