package com.tudou.android.fw.msgdispatch;

public class NonConfInstanceContainer {
    private static final String TAG = NonConfInstanceContainer.class.getSimpleName();
    
    private String mKey;
    private Object mData;
    
    public NonConfInstanceContainer(String key, Object data) {
        mKey = key;
        mData = data;
    }
    
    public String getKey() {
        return mKey;
    }
    public void setKey(String key) {
        this.mKey = key;
    }
    public Object getData() {
        return mData;
    }
    public void setData(Object data) {
        this.mData = data;
    }
}
