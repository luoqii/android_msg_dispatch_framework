package com.tudou.android.fw.model.task;

import com.tudou.android.fw.model.ambassador.IHandler;
import com.tudou.android.fw.model.ambassador.IRequest;

/**
 * task is a long-time(compared to CPU tick) but finite-time(done in the coming
 * future) running work.
 * 
 * @author bysong@tudou.com
 */
public abstract class AbsTask implements Runnable {

    protected IHandler mHandler;
    protected IRequest mReqest;
    
    public IHandler getHandler() {
        return mHandler;
    }

    public IRequest getReqest() {
        return mReqest;
    }

    /**
     * @param req
     * @param handler to send response back with this.
     */
    public AbsTask(IRequest req, IHandler handler) {
        mReqest = req;
        mHandler = handler;
    }
    
    @Override
    final public void run() {
        if (onPrepareTask()) {
            onPreTask();
            onTask();
            onPostTask();
        }
    }

    /**
     * @return true if everything is OK; false will stop running work.
     */
    protected boolean onPrepareTask() {
        return true;
    }

    protected void onPreTask() {}

    abstract protected void onTask();

    protected void onPostTask() {}

}
