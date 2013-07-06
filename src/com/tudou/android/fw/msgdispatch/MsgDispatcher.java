
package com.tudou.android.fw.msgdispatch;

import android.view.View;
import android.view.ViewGroup;

import com.tudou.android.fw.util.Log;

/**
 * utils to aid msg dispatching, usually you should use this utils instead of
 * your owning.
 * 
 * @author bysong@tudou.com
 */
public class MsgDispatcher implements MsgDispatcherPolicy {
    private static final String TAG = MsgDispatcher.class.getSimpleName();
    
    // global debug option.
    private static final boolean DEBUG = false;

    private static final boolean DEBUG_MSG_ORIGINATOR = true && DEBUG;
    /**
     * to limit verbosely image related msg, set to {@code true}
     */
    private static final boolean LIMIT_IMAGE_MSG = true && DEBUG;

    private static MsgDispatcher sInstance;
    
    private MsgDispatcherPolicy mPolicy;
    
    private MsgDispatcher(){
        mPolicy = MsgDispatcherFactory.getInstance().create(MsgDispatcherFactory.POLICY_ASYNC);
    }
    
    public static MsgDispatcher getInstance() {
        if (null == sInstance) {
            sInstance = new MsgDispatcher();
        }
        
        return sInstance;
    }

    /**
     * determinate current (root) page hierarchy whether can handle this code.
     * 
     * @param sender view which send this msg.
     * @param msg
     * @return true if this page hierarchy can handle this msg, otherwise false.
     * @see #rootHandle(View, Msg)
     */
    @SuppressWarnings("unused")
    public boolean rootCanHandle(View sender, IMsg msg) {
        if (DEBUG_MSG_ORIGINATOR && 
                (!LIMIT_IMAGE_MSG || !filter(msg))) {
            Log.d(TAG, sender.getClass().getSimpleName()
                    + " ask root if it can handle this msg. msg: " + msg);
        }
        
        return mPolicy.rootCanHandle(sender, msg);
    }

    /**
     * determine whether this group view-hierarchy can handle {@code msg}
     * 
     * @param group
     * @param msg
     * @return true if this page hierarchy can handle this msg, otherwise false.
     * @see #dispatch(ViewGroup, Msg)
     */
    @SuppressWarnings("unused")
    public boolean canHandle(ViewGroup group, final IMsg msg) {
        if (DEBUG_MSG_ORIGINATOR && 
                (!LIMIT_IMAGE_MSG || !filter(msg))) {
            Log.d(TAG, group.getClass().getSimpleName()
                    + " ask who can handle this msg. msg: "
                    + msg);
        }
        
        return mPolicy.canHandle(group, msg);
    }

    /**
     * strict mode version, usually you should use
     * {@link #rootCanHandle(View, Msg)} instead. in this situation, view-path
     * from send to root must be sub-type of {@link MsgHandler}, request root
     * page to handle this msg. now it will dispatch to top
     * {@link RootMsgHandler} to handle.
     * 
     * @param sender view which send this msg.
     * @param msg
     * @param strictMode
     * @return true if this page hierarchy have handled this msg, otherwise
     *         false.
     * @see #rootCanHandle(View, Msg)
     */
    @SuppressWarnings("unused")
    public boolean rootHandle(final View sender, final IMsg msg, boolean strictMode) {
        if (DEBUG_MSG_ORIGINATOR && 
                (!LIMIT_IMAGE_MSG || !filter(msg))) {
            Log.d(TAG, sender.getClass().getSimpleName()
                    + " request root handle. msg: "
                    + msg);
        }
        
       return mPolicy.rootHandle(sender, msg, strictMode);
    }

    /**
     * request root page to handle this msg. now it will dispatch to top
     * {@link RootMsgHandler} to handle. remove view-path check by default.
     * 
     * @param sender view which send this msg.
     * @param msg
     * @return true if this page hierarchy have handled this msg, otherwise
     *         false.
     * @see #rootCanHandle(View, Msg)
     */
    @SuppressWarnings("unused")
    public boolean rootHandle(final View sender, final IMsg msg) {
        if (DEBUG_MSG_ORIGINATOR && 
                (!LIMIT_IMAGE_MSG || !filter(msg))) {
            Log.d(TAG, sender.getClass().getSimpleName()
                    + " request root handle. msg: "
                    + msg);
        }
        
        return mPolicy.rootHandle(sender, msg);
    }

    /**
     * facility to easy root layout to dispatch msg.
     * 
     * @param root
     * @param msg
     * @param direction
     * @return true if this page hierarchy have handled this msg, otherwise
     *         false.
     */
    @SuppressWarnings("unused")
    public  boolean rootDispatch(RootMsgHandler root, IMsg msg, int direction) {
        if (DEBUG_MSG_ORIGINATOR && 
                (!LIMIT_IMAGE_MSG || !filter(msg))) {
            Log.d(TAG, root.getClass().getSimpleName() + "@" + root.hashCode()
                    + " request dispatch this msg in view hierarchy. msg: "
                    + msg);
        }

        return mPolicy.rootDispatch(root, msg, direction);
    }
  
    /**
     * request {@code group} to dispatch msg. now, a view-tree traversing will
     * be done.
     * 
     * @param group view-group in which this msg will dispatch,
     * @param msg
     * @return true if this page hierarchy have handled this msg, otherwise
     *         false.
     * @see #canHandle(ViewGroup, Msg)
     */
    @SuppressWarnings("unused")
    public boolean dispatch(ViewGroup group, final IMsg msg) {
        if (DEBUG_MSG_ORIGINATOR && 
                (!LIMIT_IMAGE_MSG || !filter(msg))) {
            Log.d(TAG, group.getClass().getSimpleName() + " dispatch this msg. msg: " + msg);
        }
        
        return mPolicy.dispatch(group, msg);
    }

    @Override
    public boolean filter(IMsg msg) {
        return mPolicy.filter(msg);
    }

    @Override
    public void setIMsgFilter(IMsgFilter filter) {
        mPolicy.setIMsgFilter(filter);
    }

    static public class IllegaLayoutException extends RuntimeException {
        /**
         * 
         */
        private static final long serialVersionUID = 6839578135244772342L;

        public IllegaLayoutException() {
            super();
        }

        public IllegaLayoutException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public IllegaLayoutException(String detailMessage) {
            super(detailMessage);
        }

        public IllegaLayoutException(Throwable throwable) {
            super(throwable);
        }
    }
}
