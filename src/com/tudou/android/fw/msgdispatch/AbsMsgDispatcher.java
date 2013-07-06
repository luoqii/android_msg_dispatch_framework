package com.tudou.android.fw.msgdispatch;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.tudou.android.fw.util.Log;

public abstract class AbsMsgDispatcher implements MsgDispatcherPolicy {
    // global debug option.
    protected static final boolean DEBUG = false;
    /**
     * to log msg traversing, set to {@code true}
     */
    protected static final boolean DEBUT_MSG_DISPATCH = true && DEBUG;
    /**
     * to trace which page can handle a msg, set to {@code true}
     */
    protected static final boolean DEBUG_CAN_HANDLE_MSG = true && DEBUG;
    private IMsgFilter mFilter;

    @Override
    public boolean rootCanHandle(View sender, IMsg msg) {
        boolean canHandled = false;
        if (sender instanceof RootMsgHandler) {
            canHandled = ((RootMsgHandler) sender).canHandle(msg);

            return canHandled;
        }

        ViewParent parent = sender.getParent();
        if (parent instanceof ViewGroup) {
            canHandled = rootCanHandle((View) parent, msg);
        }

        return canHandled;
    }
    
    @SuppressWarnings("unused")
    @Override
    public boolean canHandle(ViewGroup group, IMsg msg) {
        boolean canHandled = false;

        boolean canHandledByAnyChild = false;
        int childCount = group.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = group.getChildAt(index);
            if (child instanceof MsgHandler) {
                canHandledByAnyChild = ((MsgHandler) child).canHandle(msg);
                if (canHandledByAnyChild && !canHandled) {
                    canHandled = true;
                }

                int code = msg.getCode();
                if ((DEBUT_MSG_DISPATCH
                             || (canHandledByAnyChild && DEBUG_CAN_HANDLE_MSG))
                        && (!filter(msg)
                          )) {
                    Log.d(getTag(), "canHandled: " + canHandledByAnyChild + "\tview: "
                            + child
                            + "\tmsg: " + msg);
                }
            } else if (child instanceof ViewGroup && child.getClass().isAnnotationPresent(
                    com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)) {
                
                // keep handled state.
                canHandled = canHandle((ViewGroup) child, msg) || canHandled;
            } else {
                if (DEBUT_MSG_DISPATCH) {
                    Log.w(getTag(), "can not determine whether canHandle, ignore. view: "
                            + child);
                }
            }
        }

        return canHandled;
    }

    public abstract String getTag();
    
    @Override
    public void setIMsgFilter(IMsgFilter filter) {
        mFilter = filter;
    }
    
    @Override
    public boolean filter(IMsg msg) {
        return (null != mFilter) ? mFilter.filter(msg) : false;   
    }
}
