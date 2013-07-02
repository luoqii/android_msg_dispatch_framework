package com.tudou.android.fw.compatibility.v4;

import android.annotation.SuppressLint;
import android.app.DownloadManager;

@SuppressLint("NewApi")
public class DownloadManager_RequestCompat {
    public static void setNotificationVisibility(DownloadManager.Request r, int visibility){
        try {
            r.setNotificationVisibility(visibility);
        } catch (Exception e) {
            ;; // ignore this
        }
     }
    
    public static void allowScanningByMediaScanner(DownloadManager.Request r){
        try {
            r.allowScanningByMediaScanner();
        } catch (Exception e) {
            ;; // ignore this
        }
     }
}
