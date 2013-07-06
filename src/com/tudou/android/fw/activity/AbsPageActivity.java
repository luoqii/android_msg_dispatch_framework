
package com.tudou.android.fw.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;

import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgHandler;
import com.tudou.android.fw.msgdispatch.RootMsgHandler;
import com.tudou.android.fw.msgdispatch.RootMsgHandler.OnUnhandledListener;
import com.tudou.android.fw.util.Log;

import java.util.Map;

/**
 * mapping from msg to request, or vice versa.
 * 
 * @author bysong@tudou.com
 *
 */
public abstract class AbsPageActivity extends TudouActivity {
    private static final String TAG = AbsPageActivity.class.getSimpleName();
    private static final boolean DEBUG = true;
    @SuppressWarnings("unused")
    private static final boolean DEBUG_MSG_DISPATCH = false && DEBUG;
    
    protected RootMsgHandler mRootMsgHandler;
    protected OnUnhandledListener mUnhandledListener;
    protected LayoutInflater mLayoutInflator;
    private boolean mHasAttachedToWindow;
    
    protected Map<String, Object> mNonConfInstance;
    private static final boolean DEBUG_RETAIN_NON_CONF = false && DEBUG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUnhandledListener = new OnUnhandledListener() {
            @Override
            public void onUnhandledMsg(IMsg msg) {
                if (DEBUG_MSG_DISPATCH) {
                    Log.d(TAG, "onUnhandledMsg(). msg: " + msg);
                }
                
                if (IMsg.CATEGORY_REMOTE == msg.getCategory()) {
                    onUnHandleRemoteMsg(msg);
                } else {
                    onUnHandleLocalMsg(msg);
                }
            }
        };

        mLayoutInflator = LayoutInflater.from(this);
        View contentView = onCreateContentView(mLayoutInflator);
        
        // view hierachy checking only in debug/test mode.
        if (!mApplication.isReleaseMode()) {
            assureRootMsgHandler(contentView);
        }
        
        super.setContentView(contentView);
        mRootMsgHandler = (RootMsgHandler) contentView;
        mRootMsgHandler.setOnUnhandledListener(mUnhandledListener);

    }

    /**
     * check rootlayout's attrs.
     * 
     * @param contentView
     */
    // TODO add 2 rootlayouts checking.
    static public void assureRootMsgHandler(View contentView) {
        if (!(contentView instanceof RootMsgHandler)) {
            throw new IllegalArgumentException(
                    "top content view must implement RootMsgHandler interface.");
        }

        ViewGroup group = ((ViewGroup) contentView);
        int childCount = group.getChildCount();
        for (int index = 0; index < childCount; index++) {
            traversalTree(contentView, group.getChildAt(index));
        }
    }

    static void traversalTree(View rootlayout, View node) {
        if (node instanceof ViewGroup) {
            if (node instanceof MsgHandler ||
                    node.getClass().isAnnotationPresent(
                            com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)) {
                assureMsgHandler(rootlayout, node, true);
            } else {
                ViewGroup group = (ViewGroup) node;
                int childCount = group.getChildCount();
                View child = null;
                for (int index = 0; index < childCount; index++) {
                    child = group.getChildAt(index);
                    traversalTree(rootlayout, child);
                }
            }
        } else {
            if (node instanceof MsgHandler ||
                    node.getClass().isAnnotationPresent(
                            com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class)) {
                assureMsgHandler(rootlayout, node, true);
            } else {
                assureMsgHandler(rootlayout, node, false);
            }
        }
    }

    /**
     * anti-traverse back to rootlayout to see there is a page-hole.
     * in page-hole state, msg can not be dispatched successful by
     * msg dispatching framework.
     * 
     * @param rootlayout
     * @param msgHandlerView
     */
    static void assureMsgHandler(View rootlayout, View msgHandlerView, boolean strict) {
        if (msgHandlerView.equals(rootlayout)) {
            // all are ok
            return;
        }

        if (msgHandlerView instanceof RootMsgHandler) {
            String message = "rootlayout must be at top layout level. layout: "
                    + msgHandlerView.getClass().getName();
            throw new IllegalStateException(message);
        }

        ViewParent p = msgHandlerView.getParent();
        ViewGroup parent = (ViewGroup) p;
        if (parent instanceof MsgHandler ||
                (parent.getClass().isAnnotationPresent(
                        com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class))) {
            assureMsgHandler(rootlayout, parent, strict);
        }  else {
            String message = "this layout contain a page, but it's parent is NOT a MsgHandler "
                    + "or annotationed as @MsgHandler. page: "
                    + msgHandlerView.getClass().getName() 
                    + "\tparent: "
                    + parent.getClass().getName();
            if (strict) {
                throw new IllegalStateException(message);
            } else if (msgHandlerView instanceof MsgHandler ||
                (msgHandlerView.getClass().isAnnotationPresent(
                        com.tudou.android.fw.msgdispatch.annotation.MsgHandler.class))){
                Log.w(TAG, message);
            }
        }
    }
    
    @Override
    protected void onResponse(IResponse response) {
        IMsg msg = toMsg(response);
        if (null == msg) {
            String detailMessage = "toMsg() return null, do you override it appropriately.";
            throw new IllegalArgumentException(detailMessage);
        }
        
        dispatchMsg(msg);        
    }

    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        mHasAttachedToWindow = true;
    }
    
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        
        mHasAttachedToWindow = false;
    }
    
    /**
     * dispatch {@code msg} to Page hierarchy.
     * <p>
     * you must call this after window has attached
     * 
     * @param msg
     * 
     * @see AbsPageActivity#onAttachedToWindow()
     * 
     */
    protected void dispatchMsg(IMsg msg) {
        if (!mHasAttachedToWindow) {
            String detailMessage = "you must call dispatchMsg(IMsg msg) after window has attached. ";
//            TudouLog.w(TAG, detailMessage);
//            throw new IllegalStateException(detailMessage);
        }
        
        if (DEBUG_MSG_DISPATCH) {
            Log.d(getTag(), "dispatchMsg(). msg: " + msg);
        }
        
        mRootMsgHandler.handle(msg, MsgHandler.PARENT_2_CHILD);
    }

    /**
     * create content view to show something interesting. which
     * <strong>MUST</strong> implements {@link RootMsgHandler}
     * 
     * @param layoutInflator
     * @return content view.
     */
    protected abstract View onCreateContentView(LayoutInflater layoutInflator);
    
    @Override
    public void setContentView(int layoutResID) {
        String detailMessage = "unSupport method, implment onCreateContentView() instead.";
        throw new RuntimeException(detailMessage);
    }
    
    @Override
    public void setContentView(View view) {
        String detailMessage = "unSupport method, implment onCreateContentView() instead.";
        throw new RuntimeException(detailMessage);
    }
    
    @Override
    public void setContentView(View view, LayoutParams params) {
        String detailMessage = "unSupport method, implment onCreateContentView() instead.";
        throw new RuntimeException(detailMessage);
    }

    abstract protected void onUnHandleLocalMsg(IMsg msg);

    /**
     * convert to IRequest, and sendRequest().
     * 
     * @param msg
     * @see #sendRequest(IRequest)
     */
    protected void onUnHandleRemoteMsg(IMsg msg){
        IRequest req = msg.toRequest();
        if (null == req) {
            String detailMessage = "can not convert to IRequest.";
            throw new IllegalArgumentException(detailMessage);
        }
        
        sendRequest(req);
    }
    
    abstract protected IMsg toMsg(IResponse res);

}
