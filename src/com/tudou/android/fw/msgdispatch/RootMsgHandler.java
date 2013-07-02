package com.tudou.android.fw.msgdispatch;




/**
 * give root page a chance to handle unhanled  msg.
 * 
 * @author bysong@tudou.com
 *
 */
public interface RootMsgHandler extends MsgHandler {
    
	/**
	 * you guess
	 * 
	 * @param unhandledListener
	 */
	public void setOnUnhandledListener(OnUnhandledListener unhandledListener);	
	public void onUnhandledMsg(IMsg msg);

    /**
     * an msg has not been handled.
     * 
     * @author bysong
     *
     */
    public interface OnUnhandledListener {
    	/**
    	 * an msg has not been handled
    	 * 
    	 * @param msg
    	 */
    	public void onUnhandledMsg(final IMsg msg);
    }
}
