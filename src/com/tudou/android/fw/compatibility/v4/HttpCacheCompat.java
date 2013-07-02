package com.tudou.android.fw.compatibility.v4;

import com.tudou.android.fw.util.TudouLog;

import java.io.File;

public class HttpCacheCompat {
    private static final String TAG = HttpCacheCompat.class.getSimpleName();
    
    /**
     * @param cacheDir
     * @param httpCacheSize
     * 
     * @see <a href="http://developer.android.com/training/efficient-downloads/redundant_redundant.html">android http cache</a>
     */
    public static void enableHttpResponseCache(File cacheDir, long httpCacheSize) {
        try {
            File httpCacheDir = new File(cacheDir, "http");
            Class.forName("android.net.http.HttpResponseCache")
            .getMethod("install", File.class, long.class)
            .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            TudouLog.e(TAG, "HTTP response cache is unavailable.");
        }
    }
}
