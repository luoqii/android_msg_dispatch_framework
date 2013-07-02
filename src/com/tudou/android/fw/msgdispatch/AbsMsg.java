package com.tudou.android.fw.msgdispatch;

public abstract class AbsMsg implements IMsg {  
    // XXX how to avoid code constant conflict? bysong@tudou.com
    public static final int CODE_FW_ACTIVITY_ON_RESUME = 1000;
    public static final int CODE_FW_ACTIVITY_ON_PAUSE = 1001;
    public static final int CODE_FW_FINISH_ACTIVITY = 1003;
    /**
     * activity to page
     * 
     * <pre> 
     * data data of Map<String, Object>
     */
    public static final int CODE_FW_SAVE_NON_INS_STATE = 2000;
    /**
     * page to activity
     * 
     * <pre> 
     * data {@link NonConfInstanceContainer}
     */
    public static final int CODE_FW_RETAIN_NON_INS_STATE = 2001;

    private int mCode;
    private Object mData;
    private int mCategory = CATEGORY_LOCAL;
    // XXX add orignator ID. bysong@tudou.com
    private String mOrignator = "";

    public AbsMsg(int category, int code, Object data) {
        mCategory = category;
        mCode = code;
        mData = data;
    }
    
    @Override
    public int getCode() {
        return mCode;
    }

    @Override
    public IMsg setCode(int mCode) {
        this.mCode = mCode;
        
        return this;
    }

    @Override
    public IMsg setCategory(int category) {
        mCategory = category;
        
        return this;
    }

    @Override
    public int getCategory() {
        return mCategory;
    }

    @Override
    public Object getData() {
        return mData;
    }

    @Override
    public IMsg setData(Object data) {
        this.mData = data;
        
        return this;
    }
    
    @Override
    public String toString() {
        return "code: " + mCode + "\tcat: " + ((mCategory == CATEGORY_LOCAL) ? "CATEGORY_LOCAL" : "CATEGORY_REMOTE");
    }

}
