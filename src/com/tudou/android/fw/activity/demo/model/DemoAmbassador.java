package com.tudou.android.fw.activity.demo.model;

import com.tudou.android.fw.model.ambassador.IAmbassador;
import com.tudou.android.fw.model.ambassador.IHandler;
import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.task.TaskFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DemoAmbassador implements IAmbassador {
    private static final int DEFAULT_CORE_POOL_SIZE = 2;
    private static final int DEFAULT_MAX_POOL_SIZE = 4;
    
    private static final String TAG = DemoAmbassador.class.getSimpleName();
    
    private TaskFactory mTaskFactory;
    private Executor mExecutor; 
    
    public DemoAmbassador() {
        int core = DEFAULT_CORE_POOL_SIZE;
        int max = DEFAULT_MAX_POOL_SIZE;
        
        mExecutor = new ThreadPoolExecutor(core, max, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        mTaskFactory = TaskFactory.getInstance();
    }

    @Override
    public long scheduleRequest(IRequest req, IHandler handler) {
        mExecutor.execute(mTaskFactory.create(req, handler));
        return req.getId();
    }

    @Override
    public void unScheduleRequest(long schduleId) {
        //do nothing
    }

}
