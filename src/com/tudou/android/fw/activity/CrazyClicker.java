package com.tudou.android.fw.activity;

import com.tudou.android.fw.util.TudouLog;


public class CrazyClicker {
    private static final String TAG = CrazyClicker.class.getSimpleName();
    private static final boolean DEBUG = false;
    
    private Callback mCb;
    private int mHitLimit;
    private int mDiffThreshold;
    
    private int mHit;
    private long mLastClickTime = 0;
    
    public CrazyClicker(int hitLimit, int diffThreshold) {
        mHitLimit = hitLimit;
        mDiffThreshold = diffThreshold;
        
        if (DEBUG) {
            TudouLog.d(TAG, "litmit: " + hitLimit + " diff: " + diffThreshold);
        }
    }
    
    public void onClick(){
        if (mLastClickTime == 0) {
            mLastClickTime = System.currentTimeMillis();
            return;
        } else {
            long clickTime = System.currentTimeMillis();
            long diff = clickTime - mLastClickTime;
            
            if (DEBUG) {
                TudouLog.d(TAG, "diff: " + diff + "\thit: " + mHit);            
            }
            
            if (diff < mDiffThreshold) {
                mHit++;
            } else {
                mHit = 0;
                mLastClickTime = 0;
            }
            
            mLastClickTime = clickTime;
            
            if (mHit > mHitLimit) {
                mLastClickTime = 0;
                mHit = 0;
                
                if (mCb != null) {
                    mCb.onFireInTheHole();
                }
            }
        }
    }
    
    public void setCallback(Callback cb) {
        mCb = cb;
    }
    
    static public interface Callback {
        public void onFireInTheHole() ;
    }
}
