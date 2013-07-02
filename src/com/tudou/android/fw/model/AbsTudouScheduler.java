package com.tudou.android.fw.model;

import com.tudou.android.fw.model.ambassador.AmbassadorHandler;
import com.tudou.android.fw.model.ambassador.IResponse;

import java.util.ArrayList;

/**
 * comment impl to do (un)register working.
 * 
 * @author bysong@tudou.com
 *
 */
abstract public class AbsTudouScheduler implements ITudouSchedule {

    public AbsTudouScheduler() {

        mCallbacks = new ArrayList<ITudouSchedule.Callback>();
    }

    @Override
    public void registerCallBack(Callback callback) {
        mCallbacks.add(callback);
    }

    @Override
    public void unRegisterCallBack(Callback callback) {
        mCallbacks.remove(callback);
    }

    private void doOnResponse(IResponse res) {
        for (Callback c : mCallbacks) {
            if (null != c) {
                c.onResponse(res);
            }
        }
    }

    private ArrayList<Callback> mCallbacks;

    public class UiHandler extends AmbassadorHandler {

        public UiHandler() {
            // XXX are we must in main(ui) thread? bysong@tudou.com
            if (!"main".equals(Thread.currentThread().getName())) {
                String detailMessage = "you must initialize this in main (ui) thread.";
                throw new IllegalThreadStateException(detailMessage);
            }
        }

        @Override
        public void onResponse(IResponse response) {
            doOnResponse(response);
        }
    }
}
