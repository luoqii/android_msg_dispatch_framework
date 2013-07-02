package com.tudou.android.fw.widget;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tudou.android.fw.util.TudouLog;

/**
 * yes, just tracking everything we can.
 * @author bysong
 *
 */
public class TrackingWebViewClient extends WebViewClient {

    private static final String TAG = TrackingWebViewClient.class.getSimpleName();

    @SuppressLint("NewApi")
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        TudouLog.d(TAG, "shouldInterceptRequest(webview: " + view + " url: " + url + ")");
        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        TudouLog.d(TAG, "onTooManyRedirects(webview: " + view + " cancelMsg: " + cancelMsg + " resend: " + continueMsg + ")");
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        TudouLog.d(TAG, "onFormResubmission(webview: " + view + " dontResend: " + dontResend + " resend: " + resend + ")");
        super.onFormResubmission(view, dontResend, resend);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        TudouLog.d(TAG, "doUpdateVisitedHistory(webview: " + view + " url: " + url + " isReload: " + isReload + ")");
        super.doUpdateVisitedHistory(view, url, isReload);
    }

    @SuppressLint("NewApi")
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        TudouLog.d(TAG, "onReceivedSslError(webview: " + view + " handler: " + handler + " error: " + error + ")");
        super.onReceivedSslError(view, handler, error);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host,
            String realm) {
        TudouLog.d(TAG, "onReceivedHttpAuthRequest(webview: " + view + " handler: " + handler + " host: " + host + " realm: " + realm + ")");
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        TudouLog.d(TAG, "shouldOverrideKeyEvent(webview: " + view + " event: " + event + ")");
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        TudouLog.d(TAG, "onUnhandledKeyEvent(webview: " + view + " event: " + event + ")");
        super.onUnhandledKeyEvent(view, event);
    }

    @SuppressLint("NewApi")
    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        TudouLog.d(TAG, "onReceivedLoginRequest(webview: " + view + " realm: " + realm + " account: " + account + " args: " + args + ")");
        super.onReceivedLoginRequest(view, realm, account, args);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        TudouLog.d(TAG, "shouldOverrideUrlLoading(webview: " + view + " url: " + url + ")");

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        TudouLog.d(TAG, "onPageStarted(webview: " + view + " url: " + url + " favicon: " + favicon + ")");
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        TudouLog.d(TAG, "onPageFinished(webview: " + view + " url: " + url + ")");
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        TudouLog.d(TAG, "onLoadResource(webview: " + view + " url: " + url + ")");
        super.onLoadResource(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        TudouLog.d(TAG, "onReceivedError(webview: " + view + " description: " + description + " failingUrl: " + failingUrl + ")");
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        TudouLog.d(TAG, "onScaleChanged(webview: " + view + " oldScale: " + oldScale + " newScale: " + newScale + ")");
        super.onScaleChanged(view, oldScale, newScale);
    }
    
    

}
