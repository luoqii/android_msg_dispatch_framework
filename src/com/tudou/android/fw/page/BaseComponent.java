
package com.tudou.android.fw.page;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tudou.android.fw.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * qeueud msg untill attach to window, and post any queued msg then.
 * 
 * @author bysong@tudou.com
 *
 */
public abstract class BaseComponent extends FrameLayout {
    private static final String TAG = BaseComponent.class.getSimpleName();


    public BaseComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BaseComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseComponent(Context context) {
        super(context);
    }

    protected abstract String getLogTag();
    
    /* --------  cache work untill we are attached to windows. -------- */
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_POST = false && DEBUG;
    private AtomicBoolean mAttached = new AtomicBoolean(false);
    private Queue<Runnable> mQueue = new ConcurrentLinkedQueue<Runnable>();

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mAttached.set(true);
        if (!mQueue.isEmpty()) {
            Runnable r = mQueue.poll();
            while (null != r) {
                if (!post(r)) {
                    if (DEBUG_POST) {
                        Log.e(getLogTag(), "KO post queued runnable failed.");
                    }
                } else {
                    if (DEBUG_POST) {
                        Log.e(getLogTag(), "OK post queued runnable successed.");
                    }
                }
                r = mQueue.poll();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mQueue.clear();
        mAttached.set(false);
    }

    @Override
    public boolean post(Runnable action) {
        if (mAttached.get()) {
            if (super.post(action)) {
                if (DEBUG_POST) {
                    Log.e(getLogTag(), "OK super post successed.");
                }
                return true;
            } else {
                if (DEBUG_POST) {
                    Log.e(getLogTag(), "KO super post failed.");
                }
                return false;
            }
        } else {
            if (!mQueue.add(action)) {
                if (DEBUG_POST) {
                    Log.e(getLogTag(), "OK add action failed.");
                }
                return true;
            } else {
                if (DEBUG_POST) {
                    Log.e(getLogTag(), "KO add action successed.");
                }
                return false;
            }
        }
    }
}
