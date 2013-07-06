package com.tudou.android.fw.msgdispatch;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.tudou.android.fw.util.Log;

/**
 * Sync impl of {@link MsgDispatcherPolicy}
 * 
 * @deprecated
 * 
 * @author bysong@tudou.com
 * 
 */
public class SyncMsgDispatcher extends AbsMsgDispatcher {
    private static final String TAG = SyncMsgDispatcher.class.getSimpleName();
    
    // global debug option.
    private static final boolean DEBUG = false;
    /**
     * to log msg traversing, set to {@code true}
     */
    private static final boolean DEBUT_MSG_DISPATCH = true && DEBUG;
    /**
     * to trace which msg has been handled, set to {@code true}
     */
    private static final boolean DEBUG_HANDLED_MSG = true && DEBUG;
    /**
     * to limit verbosely image related msg, set to {@code true}
     */
    private static final boolean LIMIT_IMAGE_MSG = true && DEBUG;

    @Override
    public boolean rootHandle(View sender, IMsg msg, boolean strictMode) {
        boolean handled = false;
        if (sender instanceof RootMsgHandler) {
            handled = ((RootMsgHandler) sender).handle(msg, MsgHandler.CHILD_2_PARENT);

            return handled;
        }

        ViewParent parent = sender.getParent();
        if (null == parent) {
            Log.w(TAG, "can not handle, sender's parent is null. sender: " + sender.getClass().getSimpleName());
        } else if (parent instanceof ViewGroup
                &&
                (parent.getClass().isAnnotationPresent(
                        com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)
                        || parent instanceof MsgHandler
                        || !strictMode)) {
            handled = rootHandle((ViewGroup) parent, msg, strictMode);
        } else {
            String message = "ViewParent(ViewGroup) must implments Msg.MsgHandler interface or modify as "
                    +
                             "@MsgHandler. viewgroup: " + parent.getClass().getSimpleName();
            if (true) {
                Log.w(getTag(), "can NOT handle msg. are you sure???");
                Log.w(getTag(), message);
            }
        }

        return handled;
    }

    @Override
    public boolean rootHandle(View sender, IMsg msg) {
        return rootHandle(sender, msg, false);
    }

    @Override
    public boolean rootDispatch(RootMsgHandler root, IMsg msg, int direction) {
        boolean handled = false;
        boolean needHandle = true;

        String detailMessage = "";
        ViewGroup group = (ViewGroup) root;
        if (MsgHandler.CHILD_2_PARENT == direction) {              // child2parent
            if (IMsg.CATEGORY_LOCAL == msg.getCategory()) {         // child2parent local
                if (canHandle(group, msg)) {
                    handled = dispatch(group, msg);
                    if (!handled) {
                        detailMessage = "some page say it can handle, but actually it can NOT(i.e handle() return false). msg: "
                                + msg;
                        throw new IllegalStateException(detailMessage);
                    }
                }
            } else {
                ;// INGORE remote & chile2parent to unhandledMsg logic.
            }
        } else {                                                  // parent2child
            boolean canHandled = canHandle(group, msg);
            if (IMsg.CATEGORY_REMOTE == msg.getCategory()) {       // parent2child & remote
                if (canHandled) {                                 // yes, we can
                    handled = dispatch(group, msg);
                    if (!handled) {
                        detailMessage = "some page say it can handle, but actually it can NOT(i.e handle() return false). msg: "
                                + msg;
                        throw new IllegalStateException(detailMessage);
                    }
                } else {
                    needHandle = false;
                    detailMessage = "RCV a msg which we have triggerred, but we can NOT handle it. msg: "
                            + msg;
                    Log.w(TAG, detailMessage);
                }
            } else {                                           // parent2child local
                if (canHandled) {                              // yes, we can.
                    handled = dispatch(group, msg);
                    if (!handled) {
                        detailMessage = "some page say it can handle, but actually it can NOT(i.e handle() return false). msg: "
                                + msg;
                        throw new IllegalStateException(detailMessage);
                    }
                } else {
                    ;// IGNORE local & parent2child to unhandledMsg logic.
                }
            }
        }

        if (!handled && needHandle) {
            root.onUnhandledMsg(msg);
            handled = true;
        }

        return handled;
    }

    @Override
    public boolean dispatch(ViewGroup group, IMsg msg) {
        boolean handled = false;

        boolean handledByAnyChild = false;
        int childCount = group.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = group.getChildAt(index);
            if (child instanceof MsgHandler) {
                handledByAnyChild = ((MsgHandler) child).handle(msg, MsgHandler.PARENT_2_CHILD);
                if (handledByAnyChild && !handled) {
                    handled = true;
                }

                if ((DEBUT_MSG_DISPATCH
                             || (handledByAnyChild && DEBUG_HANDLED_MSG))
                        && (!LIMIT_IMAGE_MSG
                             || !filter(msg))) {
                    // ---------------------| ALIGN to this line
                    // ---------------------+++++++
                    Log.d(TAG, "handled: " + handledByAnyChild + "\tview: "
                            + child.getClass().getSimpleName()
                            + "\tmsg: " + msg);
                }
            } else if (child instanceof ViewGroup
                    && child.getClass().isAnnotationPresent(
                            com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)) {
                
                // keep handled state.
                handled = dispatch((ViewGroup) child, msg) || handled;
            }
        }

        return handled;
    }

    @Override
    public String getTag() {
        return TAG;
    }

}
