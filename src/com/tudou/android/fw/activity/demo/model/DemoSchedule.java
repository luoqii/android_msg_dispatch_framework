package com.tudou.android.fw.activity.demo.model;

import com.tudou.android.fw.model.AbsTudouScheduler;
import com.tudou.android.fw.model.ambassador.AmbassadorFactory;
import com.tudou.android.fw.model.ambassador.IAmbassador;
import com.tudou.android.fw.model.ambassador.IHandler;
import com.tudou.android.fw.model.ambassador.IRequest;

public class DemoSchedule extends AbsTudouScheduler {

    private static DemoSchedule sInstance;
    IAmbassador mAmbassador;
    IHandler mHandler;
    
    private DemoSchedule() {
        mAmbassador = AmbassadorFactory.getInstance().createAmbassador(null);
        mHandler = new UiHandler();
    }
    
    public static DemoSchedule getInstance() {
        if (null == sInstance) {
            sInstance = new DemoSchedule();
        }
        
        return sInstance;
    }
    
    @Override
    public long sendRequest(IRequest req) {
        return mAmbassador.scheduleRequest(req, mHandler);
    }

    @Override
    public void cancelRequest(long scheduleId) {
        throw new UnsupportedOperationException("unsupport operation: cancelRequest().");
    }

}
