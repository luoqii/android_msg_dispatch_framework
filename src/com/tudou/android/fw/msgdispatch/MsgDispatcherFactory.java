package com.tudou.android.fw.msgdispatch;

class MsgDispatcherFactory {
    public static final int POLICY_SYNC = 1;
    public static final int POLICY_ASYNC = 2;
    
    private static MsgDispatcherFactory sInstance;

    public static MsgDispatcherFactory getInstance() {
        if (null == sInstance) {
            sInstance = new MsgDispatcherFactory();
        }
        
        return sInstance;
    }
    
    /**
     * @param policy
     * @return
     * 
     * @see #POLICY_ASYNC
     * @see #POLICY_SYNC
     */
    public MsgDispatcherPolicy create(int policy) {
        MsgDispatcherPolicy d = null;
        if (POLICY_ASYNC == policy) {
            d = new AsyncMsgDispatcher();
        } else if (POLICY_SYNC == policy) {
            d = new SyncMsgDispatcher();
        }
        
        return d;
    }
}
