package com.tudou.android.fw.model.ambassador.impl;

import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.util.ID;

public abstract class AbsRequest implements IRequest {

    private int mTrafficType;
    private long mId;
    private Object mData;
    private long mUTCTime;
    private int mCode;
    
    public AbsRequest(int code, Object opaqueData) {
        this.mCode = code;
        this.mTrafficType = TRAFIC_TYPE_CLIENT2SERVER;
        this.mId = nextId();
        this.mData = opaqueData;
        this.mUTCTime = System.currentTimeMillis();
    }
    
    private long nextId() {
        return ID.nextId();
    }

    @Override
    public int getTrafficType() {
        return mTrafficType;
    }

    @Override
    public long getId() {
        return mId;
    }
    
    @Override
    public Object getData() {
        return mData;
    }

    @Override
    public long getUTCTimeStamp() {
        return mUTCTime;
    }

    @Override
    public int getCode() {
        return mCode;
    }

}
