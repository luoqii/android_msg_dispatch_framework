
package com.tudou.android.fw.msgdispatch;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.util.TudouLog;

public class AsyncMsgDispatcher extends AbsMsgDispatcher {
    private static final String TAG = AsyncMsgDispatcher.class.getSimpleName();
    
    // global debug option.
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_UNQUEUED_MSG = true && DEBUG;
    private static final boolean DEBUG_QUEUED_MSG = true && DEBUG;

    private Handler mWorkThreadHandler;

    public AsyncMsgDispatcher() {
        final HandlerThread mHandlerThread = new HandlerThread(TAG + ":" + "MsgHandler");
        mHandlerThread.start();
        mWorkThreadHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public boolean rootHandle(View sender, IMsg msg, boolean strictMode) {
        mWorkThreadHandler.post(new RootHandlRunner(sender, msg, strictMode));
        return true;
    }

    @Override
    public boolean rootHandle(View sender, IMsg msg) {
        return rootHandle(sender, msg, false);
    }

    @SuppressWarnings("unused")
    @Override
    public boolean rootDispatch(RootMsgHandler root, IMsg msg, int direction) {
        boolean queued = mWorkThreadHandler.post(new RootDispatchRunner(root, msg, direction));
        if (DEBUG_UNQUEUED_MSG && !queued) {
            TudouLog.w(TAG, "KO can not queue root dispatch work. root: "
                    + root);
        }

        if (DEBUG_QUEUED_MSG && queued) {
            TudouLog.d(TAG, "OK queue root dispatch work. root: " + root);
        }
        return true;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean dispatch(ViewGroup group, IMsg msg) {
        int childCount = group.getChildCount();
        MsgHandler msgHandler = null;
        boolean queued = false;
        for (int index = 0; index < childCount; index++) {
            View child = group.getChildAt(index);
            if (child instanceof MsgHandler) {
                msgHandler = (MsgHandler) child;
                if (msgHandler.canHandle(msg)) {
                    queued = child.post(new MsgHandleRunner(msgHandler, msg,
                            MsgHandler.PARENT_2_CHILD));

                    if (DEBUG_UNQUEUED_MSG && !queued) {
                        TudouLog.w(TAG, "KO can not queue msgHandler Runner on view: "
                                + child + "\tmsg: " + msg);
                    }
                    
                    if (DEBUG_QUEUED_MSG && queued) {
                        TudouLog.d(TAG, "OK queue msgHandler Runner on view: "
                                + child + "\tmsg: " + msg);
                    }
                }
            } else if (child instanceof ViewGroup
                    && child.getClass().isAnnotationPresent(
                            com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)) {
                dispatch((ViewGroup) child, msg);
            }
        }
        return false;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    private boolean doRootHandle(final View sender, final IMsg msg, boolean strictMode) {
        boolean handled = false;
        if (sender instanceof RootMsgHandler) {
            handled = ((RootMsgHandler) sender).handle(msg, MsgHandler.CHILD_2_PARENT);

            return handled;
        }

        ViewParent parent = sender.getParent();
        if (null == parent) {
            TudouLog.w(TAG, "can not handle, sender's parent is null. sender: "
                    + sender);
        } else if (parent instanceof ViewGroup
                &&
                (parent.getClass().isAnnotationPresent(
                        com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)
                        || parent instanceof MsgHandler
                        || !strictMode)) {
            handled = doRootHandle((ViewGroup) parent, msg, strictMode);
        } else {
            String message = "ViewParent(ViewGroup) must implments Msg.MsgHandler interface or modify as "
                    +
                             "@MsgHandler. viewgroup: " + parent;
            if (false) {
                TudouLog.w(TAG, "can NOT handle msg. are you sure???");
                TudouLog.w(TAG, message);
            }
        }

        return handled;
    }

    private void doRootDispatch(RootMsgHandler root, IMsg msg, int direction) {
        String detailMessage = "";
        ViewGroup group = (ViewGroup) root;
        boolean canHandled = false;
        if (MsgHandler.CHILD_2_PARENT == direction) { // child2parent
            if (IMsg.CATEGORY_LOCAL == msg.getCategory()) { // child2parent
                                                            // local
                canHandled = canHandle(group, msg);
                if (canHandled) {
                    dispatch(group, msg);
                }
            } else {
                ;// IGNORE remote & chile2parent to unhandledMsg logic.
            }
        } else { // parent2child
            if (IMsg.CATEGORY_REMOTE == msg.getCategory()) { // parent2child
                canHandled = canHandle(group, msg); // remote
                if (canHandled) { // yes, we can
                    dispatch(group, msg);
                } else {
                    detailMessage = "RCV a msg which we have triggerred, but we can NOT handle it. msg: "
                            + msg;
                    
                    canHandled = true; // do not dispatch more.
                    TudouLog.w(TAG, detailMessage);
                }
            } else { // parent2child local
                canHandled = canHandle(group, msg);
                if (canHandled) { // yes, we can.
                    dispatch(group, msg);
                } else {
                    canHandled = true;
                    TudouLog.e(TAG, "can not handle this msg!!!. parent2child and local. msg: "
                            + msg);
                }
            }
        }

        if (!canHandled) {
            final RootMsgHandler fRoot = root;
            final IMsg fMsg = msg;
            ((View) root).post(new Runnable() {

                @Override
                public void run() {
                    fRoot.onUnhandledMsg(fMsg);
                }
            });
        }
    }

    class RootHandlRunner implements Runnable {

        private View mSender;
        private IMsg mMsg;
        private boolean mStrictMode;

        public RootHandlRunner(View sender, IMsg msg, boolean strictMode) {
            mSender = sender;
            mMsg = msg;
            mStrictMode = strictMode;
        }

        @Override
        public void run() {
            doRootHandle(mSender, mMsg, mStrictMode);
        }
    }

    class RootDispatchRunner implements Runnable {

        private RootMsgHandler mRoot;
        private int mDirection;
        private IMsg mMsg;

        public RootDispatchRunner(RootMsgHandler root, IMsg msg, int direction) {
            mRoot = root;
            mDirection = direction;
            mMsg = msg;
        }

        @Override
        public void run() {
            doRootDispatch(mRoot, mMsg, mDirection);
        }
    }
    
    class MsgHandleRunner implements Runnable {

        private MsgHandler mMsgHandler;
        private int mDirection;
        private IMsg mMsg;

        public MsgHandleRunner(MsgHandler msgHandler, IMsg msg, int direction){
            mMsgHandler = msgHandler;
            mMsg = msg;
            mDirection = direction;
        }

        @SuppressWarnings("unused")
        @Override
        public void run() {
            Object data = mMsg.getData();
            if (data instanceof IResponse) {
                IResponse res = (IResponse) data;
                if (res.inError()) {
                    mMsgHandler.onError(mMsg);
                    return;
                } 
            }

            boolean handled = mMsgHandler.handle(mMsg, mDirection);

            if ((DEBUT_MSG_DISPATCH
                    || (handled && DEBUG_CAN_HANDLE_MSG))
                    && (!filter(mMsg)
                    )) {
                TudouLog.d(TAG, "handled: " + handled + "\tview: "
                        + mMsgHandler
                        + "\tmsg: " + mMsg);
            }
        }

    }
}
