package com.tudou.android.fw.model;


public class TudouScheduleFacotry {
    
    private static TudouScheduleFacotry sInstance;
    private ITudouScheduleFactory mFactory;

    private TudouScheduleFacotry(){
    }
    
    public static TudouScheduleFacotry getInstance() {
        if (null == sInstance) {
            sInstance = new TudouScheduleFacotry();
        }
        
        return sInstance;
    }
    
    public void setTudouScheduleFactory(ITudouScheduleFactory f) {
        mFactory = f;
    }
    
    public ITudouSchedule createTudouSchedule() {
        if (null != mFactory) {
            return mFactory.create();
        }
        
        throw new IllegalStateException("you must set factory by setTudouScheduleFactory() firstly.");
    }
    
    public interface ITudouScheduleFactory{
        public ITudouSchedule create();
    }
    
}
