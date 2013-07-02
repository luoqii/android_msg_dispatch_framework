package com.tudou.android.fw.model.cache;

import com.tudou.android.fw.model.ambassador.impl.AbsRequest;

public abstract class ImageRequest extends AbsRequest {

    public ImageRequest(int code, Object opaqueData) {
        super(code, opaqueData);
        if (!(opaqueData instanceof IImageReqEntity)) {
            String detailMessage = "data must be instance of IImageReqEntity";
            throw new IllegalArgumentException(detailMessage);
        }
    }
}
