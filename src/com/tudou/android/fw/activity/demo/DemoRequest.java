package com.tudou.android.fw.activity.demo;

import com.tudou.android.fw.model.ambassador.impl.AbsRequest;

public class DemoRequest extends AbsRequest {

    public DemoRequest(int code, Object opaqueData) {
        super(code, opaqueData);
    }

    @Override
    public String getCodeDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return "code: " + getCode();
    }
}
