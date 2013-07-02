
package com.tudou.android.fw.msgdispatch;

import android.view.View;
import android.view.ViewGroup;

public interface MsgDispatcherPolicy {

    /**
     * determinate current (root) page hierarchy whether can handle this code.
     * 
     * @param sender view which send this msg.
     * @param msg
     * @return true if this page hierarchy can handle this msg, otherwise false.
     * @see #rootHandle(View, Msg)
     */
    public abstract boolean rootCanHandle(final View sender, final IMsg msg);

    /**
     * determine whether this group view-hierarchy can handle {@code msg}
     * 
     * @param group
     * @param msg
     * @return true if this page hierarchy can handle this msg, otherwise false.
     * @see #dispatch(ViewGroup, Msg)
     */
    public abstract boolean canHandle(final ViewGroup groupSender, final IMsg msg);

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
    public abstract boolean rootHandle(final View sender, final IMsg msg, boolean strictMode);

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
    public abstract boolean rootHandle(final View sender, final IMsg msg);

    /**
     * facility to easy root-layout to dispatch msg.
     * 
     * @param root
     * @param msg
     * @param direction
     * @return true if this page hierarchy have handled this msg, otherwise
     *         false.
     */
    public abstract boolean rootDispatch(final RootMsgHandler root, final IMsg msg, final int direction);

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
    public abstract boolean dispatch(final ViewGroup group, final IMsg msg);

    public abstract void setIMsgFilter(IMsgFilter filter);
    
    boolean filter(IMsg msg);

    public interface IMsgFilter {
        /**
         * if true, this msg will be filter out.(not log)
         * 
         * @author bysong@tudou.com
         * 
         */
        public boolean filter(IMsg msg);
    }
}
