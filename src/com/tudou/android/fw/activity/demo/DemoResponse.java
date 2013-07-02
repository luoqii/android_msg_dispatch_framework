package com.tudou.android.fw.activity.demo;

import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.impl.AbsResponse;

public class DemoResponse extends AbsResponse {

    public DemoResponse(IRequest req, Object opaqueData) {
        super(req, opaqueData);
        // TODO Auto-generated constructor stub
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
