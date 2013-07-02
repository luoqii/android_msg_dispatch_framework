
package com.tudou.android.fw.model.ambassador;


/**
 * handler interface to which you can send message.
 * 
 * <p>
 * this handler can be associated with any thread, of course, Main(UI) thread.
 * 
 * instead of implementing this interface directly, sub-class
 * {@link AmbassadorHandler} instead.
 * 
 * @author bysong@tudou.com
 *
 */
public interface IHandler {

    /**
     * send an response back.  
     * <p>
     * used by sender.
     * 
     * @param response
     */
    public abstract void sendResponseMessage(IResponse response);
    
}
