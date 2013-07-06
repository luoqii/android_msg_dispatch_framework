
package com.tudou.android.fw.page.layout;

import android.content.Context;
import android.util.AttributeSet;

import com.tudou.android.fw.msgdispatch.annotation.MsgHandler;
import com.tudou.android.fw.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@MsgHandler
public class AnnotationLinearLayout extends android.widget.LinearLayout {
    private static final String TAG = LinearLayout.class.getSimpleName();

    public AnnotationLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnnotationLinearLayout(Context context) {
        super(context);
    }

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
                        Log.e(TAG, "KO post queued runnable failed.");
                    }
                } else {
                    if (DEBUG_POST) {
                        Log.e(TAG, "OK post queued runnable successed.");
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
                    Log.e(TAG, "OK super post successed.");
                }
                return true;
            } else {
                if (DEBUG_POST) {
                    Log.e(TAG, "KO super post failed.");
                }
                return false;
            }
        } else {
            if (!mQueue.add(action)) {
                if (DEBUG_POST) {
                    Log.e(TAG, "OK add action failed.");
                }
                return true;
            } else {
                if (DEBUG_POST) {
                    Log.e(TAG, "KO add action successed.");
                }
                return false;
            }
        }
    }
}
