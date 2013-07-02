package com.tudou.android.fw.model.ambassador;

import android.os.Handler;
import android.os.Message;

/**
 * instead of using raw Handler.sendMessage(), using
 * {@link #sendResponseMessage(IResponse)} or 
 * {@link #sendErrorMessage(IRequest, int, String)} instead.
 * 
 * @author bysong@tudou.com
 *
 */
abstract public class AmbassadorHandler extends Handler implements IHandler {

    public void handleMessage(Message msg) {
            onResponse((IResponse) msg.obj);
    }

    /* (non-Javadoc)
     * @see com.tudou.android.model.ambassador.IHandler#sendResponseMessage(com.tudou.android.model.ambassador.IResponse)
     */
    @Override
    public void sendResponseMessage(IResponse response) {
        Message msg = obtainMessage();
        msg.obj = response;
        msg.sendToTarget();
    }
    
    /**
     * invoked when a {@link IResponse} received.
     * <p>
     * invoked by sender in receiver-side.
     * 
     * @param response
     */
    abstract public void onResponse(IResponse response);
}
