package com.tudou.android.fw.model.ambassador.impl;

import com.tudou.android.fw.model.ambassador.IAmbassador;
import com.tudou.android.fw.model.ambassador.IHandler;
import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.task.TaskFactory;
import com.tudou.android.fw.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultAmbassador implements IAmbassador {	
	private static final String TAG = DefaultAmbassador.class.getSimpleName();
	
    private TaskFactory mTaskFactory;
	private Executor mExecutor;
	
	public DefaultAmbassador(){
		mExecutor = new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		mTaskFactory = TaskFactory.getInstance();
	}

	@Override
	public long scheduleRequest(IRequest req, IHandler handler) {
		
		mExecutor.execute(mTaskFactory.create(req, handler));
		
		return req.getId();
	}

	@Override
	public void unScheduleRequest(long schduleId) {
	    Log.w(TAG, "unScheduleRequest() not impl.");
	}
}
