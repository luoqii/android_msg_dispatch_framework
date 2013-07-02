package com.tudou.android.fw.compatibility.v4;

import android.annotation.SuppressLint;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tudou.android.fw.util.TudouLog;

public class WebViewCompat {
    private static final String TAG = WebViewCompat.class.getSimpleName();
    
    @SuppressLint({"NewApi"
        
    })
    public static void setDisplayZoomControls(WebSettings setting, boolean enable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setting.setDisplayZoomControls(enable);
        }
    }
    
    public static void setBuiltInZoomControls(WebSettings setting, boolean enable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            setting.setBuiltInZoomControls(enable);
        }
    }
    
    /**
     * sdk低于7以下的机型可能不存在此api
     * @param setting
     * @param flag
     */
    public static void setJavaScriptEnabled(WebSettings setting, boolean flag) {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
    		setting.setJavaScriptEnabled(flag);
    	}else {
    		try {
    			setting.setJavaScriptEnabled(flag);
    		} catch (Exception e) {
    			TudouLog.w("WebViewCompat", "Error in setJavaScriptEnabled: "+e);
    		}
    	}
    }
    
    /**
     * sdk低于11以下的机型可能不存在此api
     * @param setting
     * @param flag
     */
    @SuppressLint({ "NewApi", "NewApi" })
	public static void onResume(WebView webview) {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    		webview.onResume();
    	}else {
    		try {
    			webview.onResume();
    		} catch (Exception e) {
    			TudouLog.w("WebViewCompat", "Error in onResume: "+e);
    		}
    	}
    }
    
    /**
     * sdk低于11以下的机型可能不存在此api
     * @param setting
     * @param flag
     */
    @SuppressLint({ "NewApi", "NewApi" })
	public static void onPause(WebView webview) {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    		webview.onPause();
    	}else {
    		try {
    			webview.onPause();
    		} catch (Exception e) {
    			TudouLog.w("WebViewCompat", "Error in onPause: "+e);
    		}
    	}
    }
    
    /**
     * sdk低于7以下的机型可能不存在此api
     * @param setting
     * @param flag
     */
    @SuppressLint({ "NewApi", "NewApi" })
	public static void setLoadWithOverviewMode(WebSettings setting, boolean flag) {
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
    		setting.setLoadWithOverviewMode(flag);
    	}else {
    		try {
    			setting.setLoadWithOverviewMode(flag);
    		} catch (Exception e) {
    			TudouLog.w("WebViewCompat", "Error in setLoadWithOverviewMode: "+e);
    		}
    	}
    }
    
    @SuppressLint("NewApi")
    public static void onHideCustomeView(WebChromeClient client) {
        try {
            client.onHideCustomView();
        } catch (Exception e) {
            ;; // ingore, it's safe
        }
    }
}
