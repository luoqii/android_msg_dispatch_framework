package com.tudou.android.fw;


import com.tudou.android.fw.activity.demo.model.DemoAmbassador;
import com.tudou.android.fw.activity.demo.model.DemoSchedule;
import com.tudou.android.fw.activity.demo.model.DemoTaskFacotry;
import com.tudou.android.fw.application.App;
import com.tudou.android.fw.model.ITudouSchedule;
import com.tudou.android.fw.model.TudouScheduleFacotry;
import com.tudou.android.fw.model.TudouScheduleFacotry.ITudouScheduleFactory;
import com.tudou.android.fw.model.ambassador.AmbassadorFactory;
import com.tudou.android.fw.model.ambassador.AmbassadorFactory.IAmbassadorFactory;
import com.tudou.android.fw.model.ambassador.AmbassadorFactory.Policy;
import com.tudou.android.fw.model.ambassador.IAmbassador;
import com.tudou.android.fw.model.task.TaskFactory;
import com.tudou.android.fw.util.Log;


public class DemoApplication extends App {
   
    private static final String TAG = DemoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        
        initLog();
        initModel();
        
        // TODO need a better solution for this. bysong@tudou.com
        final int memory = getResources().getInteger(R.integer.lib_app_memory);
        Log.d(TAG, "memory heap size: " + memory * 1024 * 1024);
    }
    
    private void initLog() {
        Log.setLog(true);
        Log.setLog2File(true);
        Log.setRootTag(getLogTag());
        Log.init(mSpec.getExtLogDirPath(), "log");

        Log.i(TAG, "Hi, world.");     
    }
    
    @Override
    public boolean isReleaseMode() {
        return false;
    }   
    
    @Override
    public boolean isMonkeyMode() {
        return false;
    }
    
    protected void initModel() {
        TaskFactory.getInstance().setFactory(new DemoTaskFacotry());
        AmbassadorFactory.getInstance().setFactory(new IAmbassadorFactory() {
            
            @Override
            public IAmbassador createAmbassador(Policy notUsed) {
                return new DemoAmbassador();
            }
        });
        TudouScheduleFacotry.getInstance().setTudouScheduleFactory(new ITudouScheduleFactory() {

            @Override
            public ITudouSchedule create() {
                return DemoSchedule.getInstance();
            }
        });
    }

}
