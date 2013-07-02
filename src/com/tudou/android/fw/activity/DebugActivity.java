package com.tudou.android.fw.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.tudou.android.fw.application.App;
import com.tudou.android.fw.util.TudouLog;
import com.tudou.android.fw.widget.DebugView;
import com.tudou.android.fw.widget.ViewServer;

/**
 * provide generic debugging: 
 *  1) life-cycle
 *  2) to aid hierarchy-viewer
 * 
 * @author bysong
 *
 */
public abstract class DebugActivity extends Activity {
    private static final String TAG = DebugActivity.class.getSimpleName(); 
    
    private static final boolean HACK_INFO = true;
    private CrazyClicker mCrazyClicker;

    private static final int HIT_LIMIT = 7; // luck number for me, bysong@tudou.com
    private static final long DIFF = 477;
    
    private static /*final*/ boolean ENABLE_VIEW_SERVER = true;
    
    private static final boolean DEBUG_LIFE_CYCLE = false;
    private DebugView mDebugOverlay;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (App.getInstance().isReleaseMode()) {
            ENABLE_VIEW_SERVER = false;
            TudouLog.i(TAG, "disable ViewServer.");
        } else {
            TudouLog.i(TAG, "enable ViewServer in non-release mode.");
        }
        
        if (ENABLE_VIEW_SERVER) {
            ViewServer.get(this).addWindow(this);
        }
        
        if (HACK_INFO) {
            mCrazyClicker = onCreateCrazyClicker();
            mCrazyClicker.setCallback(new CrazyClicker.Callback() {
                
                @Override
                public void onFireInTheHole() {
                    DebugActivity.this.onFireInTheHole();
                }
            });
        }
        
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onCreate(). #" + hashCode() + " savedInstanceState: " + savedInstanceState);
        }
    }
    
    @SuppressLint("NewApi")
    @Override
    public void onAttachedToWindow() {
        if (onShowActivityNameInUi()) {
            mDebugOverlay = new DebugView(this);
            mDebugOverlay.show();
        }
        super.onAttachedToWindow();
    }
    
    @SuppressLint("NewApi")
    @Override
    public void onDetachedFromWindow() {
        if (onShowActivityNameInUi()) {
            mDebugOverlay.hide();
        }
        super.onDetachedFromWindow();
    }
    
    protected boolean onShowActivityNameInUi() {
        return false;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onResume().");
        }
        
        if (ENABLE_VIEW_SERVER) {
            ViewServer.get(this).setFocusedWindow(this);
        }
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onNewIntent(). intent: " + intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onRestart().");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onStart().");
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onPostResume().");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onPause().");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onStop().");
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy(); 
        
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onDestroy().");
        }       
        
        if (ENABLE_VIEW_SERVER) {
            ViewServer.get(this).removeWindow(this);
        }
    }   
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (HACK_INFO) {
            mCrazyClicker.onClick();
        }
        
        return super.onPrepareOptionsMenu(menu);
    }

    protected CrazyClicker onCreateCrazyClicker() {
        return new CrazyClicker(HIT_LIMIT, (int) DIFF);
    }
    
    protected void onFireInTheHole() {}

    /**
     * get a description of yourself, usually you can return you class simple name. 
      * 
     * @return label to describe yourself.
      * 
     * @see Class#getSimpleName()
     * @see #TAG
     */
    protected abstract String getTag();

    @Override
    public Object onRetainNonConfigurationInstance() {
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onRetainNonConfigurationInstance().");
        }
        return super.onRetainNonConfigurationInstance();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (DEBUG_LIFE_CYCLE) {
            TudouLog.d(getTag(), "onSaveInstanceState(). outState: " + outState);
        }
    }
}
