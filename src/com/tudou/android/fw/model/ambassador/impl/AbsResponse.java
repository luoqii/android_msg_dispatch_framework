
package com.tudou.android.fw.model.ambassador.impl;

import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.util.ID;

public abstract class AbsResponse implements IResponse {

    private int mTraficType;
    private long mId;
    private long mRequeseID;
    private IRequest mRequest;
    private Object mData;
    private long mUTCTime;
    private int mCode;
    private IError mError;

    public AbsResponse(IRequest req, Object opaqueData) {
        mCode = req.getCode();
        mRequest = req;
        mRequeseID = req.getId();

        this.mTraficType = TRAFIC_TYPE_SERVER2CLIENT;
        this.mId = nextId();
        this.mData = opaqueData;
        this.mUTCTime = System.currentTimeMillis();
    }

    public IRequest getRequest() {
        return mRequest;
    }

    protected long nextId() {
        return ID.nextId();
    }

    @Override
    public int getTrafficType() {
        return mTraficType;
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
    public long getRequestId() {
        return mRequeseID;
    }

    @Override
    public int getCode() {
        return mCode;
    }

    @Override
    public boolean inError() {
        return mError != null;
    }

    @Override
    public IError getError() {
        return mError;
    }

    @Override
    public void setError(IError e) {
        mError = e;
    }

    public static class Error implements IError {

        private int mCode;
        private String mDesc;

        public Error(int code, String desc) {
            super();

            this.mCode = code;
            this.mDesc = desc;
        }

        @Override
        public int getCode() {
            return mCode;
        }

        @Override
        public String getDesc() {
            return mDesc;
        }

    }

}
