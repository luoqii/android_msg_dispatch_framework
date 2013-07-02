package com.tudou.android.fw.activity.demo;

import com.tudou.android.fw.model.ambassador.IRequest;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.msgdispatch.AbsMsg;
import com.tudou.android.fw.msgdispatch.IMsg;

public class DemoMsg extends AbsMsg {
    
    /**
     * 
     * <pre>
     * data text type: {@link String}
     */
    public static final int CODE_DEMO_SEND_TEXT = 1;
    
    /**
     * <pre>
     * data categorId type: {@link String}
     */
    public static final int CODE_DEMO_GET_CATEGORY = 2;
    /**
     * <pre>
     * data video id type: {@link String}
     * 
     */
    public static final int CODE_DEMO_VIEW_VIDEO = 3;
    public static final int CODE_DEMO_GET_VIDEO_INFO = 4;

    public DemoMsg(int category, int code, Object data) {
        super(category, code, data);
        // TODO Auto-generated constructor stub
    }

    @Override
    public IRequest toRequest() {
        return new DemoRequest(getCode(), getData());
    }
    
    @Override
    public String toString() {
        return "code: " + getCode();
    }

}
