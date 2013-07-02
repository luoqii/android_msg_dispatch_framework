
package com.tudou.android.fw.model.ambassador;



/**
 * do request scheduling working.
 * 
 * <pre>
 * what we do do:
 *   1). task(job) (un)schedule.
 *   2).
 * what we do NOT do:
 *   1). data persistent.
 *   2). thread sync.
 * @author bysong
 */
public interface IAmbassador {
    
    /**
     * schedule a request to be executed in the future. 
     * <p>
     * 
     * @param req
     * @param handler 
     * @return request id. which you can cancel it laterly.
     * @see #unScheduleRequest(long)
     * @see Callback#sendResponseMessage(IResponse)
     */
    public long scheduleRequest(IRequest req, IHandler handler);

    /**
     * @param requestId id of the scheduled request.
     * @see #scheduleRequest(IRequest, IHandler)
     */
    public void unScheduleRequest(long schduleId);
}
