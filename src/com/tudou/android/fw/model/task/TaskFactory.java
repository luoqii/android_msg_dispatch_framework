package com.tudou.android.fw.model.task;

import com.tudou.android.fw.model.ambassador.IHandler;
import com.tudou.android.fw.model.ambassador.IRequest;

public class TaskFactory {
    
    private static TaskFactory sInstance;

    private ITaskFactory mFactory;

    public static TaskFactory getInstance() {
        if (null == sInstance) {
            sInstance = new TaskFactory();
        }
        
        return sInstance;
    }

    public void setFactory(ITaskFactory f) {
        mFactory = f;
    }
    
    public AbsTask create(IRequest req, IHandler handler) {
        if (null != mFactory) {
           return mFactory.create(req, handler);
        }
        
        throw new IllegalStateException("you must set factory by setFactory().");
    }
    
    public interface ITaskFactory {
        public AbsTask create(IRequest req, IHandler handler);
    }
}
