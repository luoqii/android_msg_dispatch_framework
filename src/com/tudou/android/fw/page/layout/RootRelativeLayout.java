package com.tudou.android.fw.page.layout;

import android.content.Context;
import android.util.AttributeSet;

import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgDispatcher;
import com.tudou.android.fw.msgdispatch.annotation.RootMsgHandler;
import com.tudou.android.fw.util.TudouLog;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class RootRelativeLayout extends android.widget.RelativeLayout
implements com.tudou.android.fw.msgdispatch.RootMsgHandler{

    protected static final String TAG = RootRelativeLayout.class.getSimpleName();
    private OnUnhandledListener mListener;

    public RootRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
    }

    public RootRelativeLayout(Context context) {
        super(context);

        init();
    }

    public boolean handle(IMsg msg, int direction) {
        return MsgDispatcher.getInstance().rootDispatch(this, msg, direction);
    }

    @Override
    public void onUnhandledMsg(IMsg msg) {
        if (null != mListener) {
            mListener.onUnhandledMsg(msg);
        } else {
            TudouLog.w(TAG, "no onUnhandledMsg yet. root: " + this);
        }
    }

    @RootMsgHandler.UnHandledMsgListener
    @Override
    public void setOnUnhandledListener(
            OnUnhandledListener unhandledListener) {
        mListener = unhandledListener;
    }

    @Override
    public boolean canHandle(IMsg msg) {
        return MsgDispatcher.getInstance().canHandle(this, msg);
    }

    @Override
    public void onError(IMsg errorMsg) {
        ;// nothing
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
