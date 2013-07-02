package com.tudou.android.fw.activity.demo.model;

import com.tudou.android.fw.activity.demo.DemoMsg;
import com.tudou.android.fw.activity.demo.DemoResponse;
import com.tudou.android.fw.model.ambassador.IHandler;
import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.model.task.AbsTask;
import com.tudou.android.fw.model.task.TaskFactory.ITaskFactory;
import com.tudou.android.fw.util.ThreadUtil;

import java.util.ArrayList;

public class DemoTaskFacotry implements ITaskFactory {

    @Override
    public AbsTask create(IRequest req, IHandler handler) {
        AbsTask task = null;
        if (req.getCode() == DemoMsg.CODE_DEMO_GET_CATEGORY) {
            task = new DemoCategoryTask(req, handler);
        } else if (req.getCode() == DemoMsg.CODE_DEMO_GET_VIDEO_INFO) {
            task = new DemoVideoInfoTask(req, handler);
        }
        
        return task;
    }
    
    class DemoCategoryTask extends AbsTask {

        public DemoCategoryTask(IRequest req, IHandler handler) {
            super(req, handler);
        }

        @Override
        protected void onTask() {
            ThreadUtil.sleep(3 * 1000);
            
            ArrayList opaqueData = new ArrayList<String>();
            for (int i = 0 ; i < 10; i++) {
                opaqueData.add("video " + i);
            }
            IResponse response = new DemoResponse(mReqest, opaqueData);
            mHandler.sendResponseMessage(response);
        }
        
    }

    class DemoVideoInfoTask extends AbsTask {

        public DemoVideoInfoTask(IRequest req, IHandler handler) {
            super(req, handler);
        }

        @Override
        protected void onTask() {
            ThreadUtil.sleep(3 * 1000);
            
            String opaqueData = mReqest.getData() + " video desc.";
            IResponse response = new DemoResponse(mReqest, opaqueData);
            mHandler.sendResponseMessage(response);
        }
        
    }
}
