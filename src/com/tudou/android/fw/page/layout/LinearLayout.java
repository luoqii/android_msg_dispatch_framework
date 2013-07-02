
package com.tudou.android.fw.page.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgDispatcher;
import com.tudou.android.fw.msgdispatch.MsgHandler;
import com.tudou.android.fw.util.TudouLog;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class LinearLayout extends android.widget.LinearLayout implements MsgHandler {
    private static final String TAG = LinearLayout.class.getSimpleName();

    public LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayout(Context context) {
        super(context);
    }

    @Override
    public boolean handle(IMsg msg, int direction) {
        boolean handled = false;
        if (MsgHandler.PARENT_2_CHILD == direction) {
            handled = MsgDispatcher.getInstance().dispatch((ViewGroup) this, msg);
        } else {
            handled = MsgDispatcher.getInstance().rootHandle(this, msg);
        }

        return handled;
    }

    @Override
    public boolean canHandle(IMsg msg) {
        return MsgDispatcher.getInstance().canHandle(this, msg);
    }

    @Override
    public void onError(IMsg errorMsg) {
        MsgDispatcher.getInstance().dispatch((ViewGroup) this, errorMsg);        
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
                        TudouLog.e(TAG, "KO post queued runnable failed.");
                    }
                } else {
                    if (DEBUG_POST) {
                        TudouLog.e(TAG, "OK post queued runnable successed.");
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
                    TudouLog.e(TAG, "OK super post successed.");
                }
                return true;
            } else {
                if (DEBUG_POST) {
                    TudouLog.e(TAG, "KO super post failed.");
                }
                return false;
            }
        } else {
            if (!mQueue.add(action)) {
                if (DEBUG_POST) {
                    TudouLog.e(TAG, "OK add action failed.");
                }
                return true;
            } else {
                if (DEBUG_POST) {
                    TudouLog.e(TAG, "KO add action successed.");
                }
                return false;
            }
        }
    }
}
