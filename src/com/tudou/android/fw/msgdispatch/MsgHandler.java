package com.tudou.android.fw.msgdispatch;

import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.page.BaseComponent;

/**
 * all page(s) must implement this interface to interact with each other.
 * <p>
 * if you page extends from android view, you must qeueud msg untill 
 * view has attached to window, and post any queued msg then, instead,
 * you can extends from {@link BaseComponent}.
 * 
 * 
 * XXX Page 只处理自己发送的消息引起的消息
 * 
 * @see BaseComponent
 * @author bysong@tudou.com
 *
 */
public interface MsgHandler {
	public int CHILD_2_PARENT = 1;
	public int PARENT_2_CHILD = 2;
	
	/**
     * determine whether you (page) can handle it.  
     * <p>
     * NOTE: do not assume that this will be called in main(UI) thread, so do NOT
     * touch(update) view content, and so on.
     * @param msg
     * @return
     * 
     * @see MsgHandler#handle(Msg, int)
     */
    public boolean canHandle(final IMsg msg);

    /**
	 * a msg has arrived (to handle msg, you (page) must return {@code true} in 
	 * {@link #canHandle(Msg)} correspondingly), so you have chance to 
	 * handle it.
     *
     *  we assure that this will be called in the main(UI) thread.
	 * 
	 * @param msg
	 * @param direction {@link #CHILD_2_PARENT} or {@link #PARENT_2_CHILD}
	 * @return true if this msg have handled, false otherwise.
	 * 
	 * @see #canHandle(Msg)
	 */
	public boolean handle(final IMsg msg, int direction);
	
	/**
	 * a error occurred, the error-response was wrapped in data member, you can get 
	 * the error by 
	 * <pre>
	 *     Error e = ((IResponse) errorMsg.getData()).getError();
	 *     ... ...
	 * <p>
	 * like {@link MsgHandler#handle(Msg, int)}, we assure that this will be called 
	 *  in the main(UI) thread.
	 * 
	 * @param errorMsg
	 * 
	 * @see IResponse#getError()
	 * @see IResponse#inError()
	 */
	public void onError(final IMsg errorMsg);
}
