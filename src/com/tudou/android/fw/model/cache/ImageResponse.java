package com.tudou.android.fw.model.cache;

import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.impl.AbsResponse;

public class ImageResponse extends AbsResponse {
    
    public ImageResponse(IRequest req, Object opaqueData) {
        super(req, opaqueData);
    }
    
    @Override
    public String getCodeDescription() {
        // TODO Auto-generated method stub
        return null;
    }

}
