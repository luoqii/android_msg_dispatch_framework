package com.tudou.android.fw.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;

import com.tudou.android.fw.util.Log;

/**
 * yes, just tracking everything we can.
 * @author bysong
 *
 */
public class TrackingWebChromeClient extends WebChromeClient {
    
    private static final String TAG = TrackingWebChromeClient.class.getSimpleName();

    public TrackingWebChromeClient() {
        super();
    }
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.d(TAG, "onProgressChanged(webview: " + view + " newProgress: " + newProgress + ")");
    }
    
    @SuppressLint("NewApi")
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d(TAG, "onConsoleMessage: " + consoleMessage.message());
        return super.onConsoleMessage(consoleMessage);
    }
    
    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public Bitmap getDefaultVideoPoster() {
        Bitmap bitmap = super.getDefaultVideoPoster();

        Log.d(TAG, "getDefaultVideoPoster() bitmap: " + bitmap);
        return bitmap;
    }
    
    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public View getVideoLoadingProgressView() {
        Log.d(TAG, "getVideoLoadingProgressView()");
        
        return super.getVideoLoadingProgressView();
    }
    
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
            Message resultMsg) {
        Log.d(TAG, "onCreateWindow(view: " + view + " idDialog: " + isDialog + " isUserGesture: " + isUserGesture + " requestMsg: " + resultMsg  + ")");
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }
    
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onShowCustomView(View view, int requestedOrientation,
            CustomViewCallback callback) {
        Log.d(TAG, "onShowCustomView(view: " + view + " requestedOrientation: " + requestedOrientation + " callback: " + callback + ")");
        super.onShowCustomView(view, requestedOrientation, callback);
    }
    
    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        Log.d(TAG, "onShowCustomView(view: " + view + " callback: " + callback + ")");
         super.onShowCustomView(view, callback);
    }
    @Override
    public void onReceivedTitle(WebView view, String title) {
        Log.d(TAG, "onReceivedTitle(view: " + view + " title: " + title + ")");

        super.onReceivedTitle(view, title);
    }
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        Log.d(TAG, "onReceivedIcon(view: " + view + " icon: " + icon + ")");

        super.onReceivedIcon(view, icon);
    }
    @TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        Log.d(TAG, "onReceivedTouchIconUrl(view: " + view + " url: " + url + " precomposed: " + precomposed + ")");
        super.onReceivedTouchIconUrl(view, url, precomposed);
    }
    @SuppressLint("NewApi")
    @Override
    public void onHideCustomView() {
        Log.d(TAG, "onHideCustomView()");

        super.onHideCustomView();
    }
    @Override
    public void onCloseWindow(WebView window) {
        Log.d(TAG, "onCloseWindow(webview: " + window + ")");
        
        super.onCloseWindow(window);
    }
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "onJsConfirm(webview: " + view + " url: " + url + " message: " + message + " result: " + result + ")");
        return super.onJsConfirm(view, url, message, result);
    }
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
            JsPromptResult result) {
        Log.d(TAG, "onJsPrompt(webview: " + view + " url: " + url + " message: " + message + " defaultValue: " + defaultValue + " result: " + result + ")");
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "onJsBeforeUnload(webview: " + view + " url: " + url + " message: " + message + " result: " + result + ")");  
        return super.onJsBeforeUnload(view, url, message, result);
    }
    @SuppressLint("NewApi")
    @Override
    public boolean onJsTimeout() {
        Log.d(TAG, "onJsTimeout()");
        return super.onJsTimeout();
    }
    @SuppressLint("NewApi")
    @Override
    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        Log.d(TAG, "onConsoleMessage(lineNumber: " + lineNumber + " sourceID: " + sourceID);
        super.onConsoleMessage(message, lineNumber, sourceID);
    }
    @SuppressLint("NewApi")
    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        Log.d(TAG, "getVisitedHistory(callback: " + callback + ")");
        super.getVisitedHistory(callback);
    }
    @SuppressLint("NewApi")
    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota,
            long estimatedDatabaseSize, long totalQuota, QuotaUpdater quotaUpdater) {
        Log.d(TAG, "onExceededDatabaseQuota(url: " + url + " databaseIdentifier: " + databaseIdentifier + 
                " quota: " + quota + " estimatedDatabaseSize: " + estimatedDatabaseSize + " totalQuota: " + totalQuota + 
                " quotaUpdater: " + quotaUpdater + ")");
        super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota,
                quotaUpdater);
    }
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Log.d(TAG, "onJsAlert(webview: " + view + " url: " + url + " message: " + message + " result: " + result + ")");  
        return super.onJsAlert(view, url, message, result);
    }
    @SuppressLint("NewApi")
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        Log.d(TAG, "onGeolocationPermissionsShowPrompt(origin: " + origin + " callback: " + callback + ")");
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }
    @SuppressLint("NewApi")
    @Override
    public void onGeolocationPermissionsHidePrompt() {
        Log.d(TAG, "onGeolocationPermissionsHidePrompt()");
        super.onGeolocationPermissionsHidePrompt();
    }
    @SuppressLint("NewApi")
    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, QuotaUpdater quotaUpdater) {
        Log.d(TAG, "onReachedMaxAppCacheSize(requiredStorage: " + requiredStorage + " quota: " + quota + " quotaUpdater: " + quotaUpdater + ")");
        super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
    }
    @Override
    public void onRequestFocus(WebView view) {
        Log.d(TAG, "onRequestFocus(webview: " + view + ")");
        super.onRequestFocus(view);
    }
    
    
}
