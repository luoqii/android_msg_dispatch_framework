
package com.tudou.android.fw.model;

import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.IResponse;


/**
 * the universal Back-End interface to provide message requesting.
 * All ui MUST and ONLY must to interact with this.
 * 
 * <p>
 * when implement, subclass {@link AbsTudouScheduler}
 * 
 * <p>
 * implementor must support thread-sync function.
 * 
 * @author bysong@tudou.com
 *
 * @see AbsTudouScheduler
 */
public interface ITudouSchedule {

    /**
     * send a {@link IRequest} to be scheduled, we assure that
     * this will be a response per request.
     * 
     * @param req
     * @return
     */
    public abstract long sendRequest(IRequest req);

    /**
     * cancel previous request described by {@code scheduleId}
     * 
     * @param scheduleId
     */
    public abstract void cancelRequest(long scheduleId);

    /**
     * you guess
     * 
     * @param callback
     */
    public abstract void registerCallBack(Callback callback);

    /**
     * you guess
     * 
     * @param callback
     */
    public abstract void unRegisterCallBack(Callback callback);
    
    /**
     * response callback
     * @author bysong@tudou.com
     *
     */
    public interface Callback {
        /**
         * a response has arrived. 
         * <pre>
         * <strong>NOTE</strong>: 
         * callback will be called in the same thread in which you schedule this
         * request. As a result you do NOT need to sync threading-stuff.
         * 
         * <strong>NOTE</strong>:
         * you MUST be sure that this response is your wanted, i.e a response is
         * arrived only bcz you have send a request which trigger a response.
         * caller must check error state by {@link IResponse#inError()}
         * 
         * good luck! 
         * more to see {@link IResponse#getRequestId()}. 
         * 
         * @param res
         * 
         * @see IResponse#inError()
         */
        public void onResponse(IResponse res);
        
    }
}
