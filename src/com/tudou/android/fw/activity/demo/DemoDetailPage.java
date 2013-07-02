package com.tudou.android.fw.activity.demo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tudou.android.fw.R;
import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgDispatcher;
import com.tudou.android.fw.msgdispatch.MsgHandler;
import com.tudou.android.fw.page.BaseComponent;

public class DemoDetailPage extends BaseComponent implements MsgHandler {

    private static final String TAG = DemoDetailPage.class.getSimpleName();
    private TextView mInfoTextV;

    public DemoDetailPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        init();
    }

    public DemoDetailPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }

    public DemoDetailPage(Context context) {
        super(context);
        
        init();
    }

    private void init() {
        setBackgroundColor(Color.CYAN);
        inflate(getContext(), R.layout.lib_demo_detail_page_content, this);
        mInfoTextV = (TextView)findViewById(R.id.vidoe_info);
    }

    @Override
    public boolean canHandle(IMsg msg) {
        int code = msg.getCode();
        int category = msg.getCategory();
        return IMsg.CATEGORY_LOCAL == category
                 ? DemoMsg.CODE_DEMO_VIEW_VIDEO == code
                 : DemoMsg.CODE_DEMO_GET_VIDEO_INFO == code
                 ;
    }

    @Override
    public boolean handle(IMsg msg, int direction) {
        int code = msg.getCode();
        int category = msg.getCategory();
        boolean handled = false;
        if (IMsg.CATEGORY_LOCAL == category) {
            if (DemoMsg.CODE_DEMO_VIEW_VIDEO == code) {
                String videoId = (String) msg.getData();
                requestVideoInfo(videoId);
            }
        } else {
            if (DemoMsg.CODE_DEMO_GET_VIDEO_INFO == code) {
                String videoInfo = (String) ((DemoResponse)msg.getData()).getData();
                
                setInfo(videoInfo);
            }
        }
        
        return handled;
    }

    private void setInfo(String videoInfo) {
        mInfoTextV.setText(videoInfo);
    }

    private void requestVideoInfo(String videoId) {
        IMsg msg = new DemoMsg(IMsg.CATEGORY_REMOTE, DemoMsg.CODE_DEMO_GET_VIDEO_INFO, videoId);
        MsgDispatcher.getInstance().rootHandle(this, msg);
    }

    @Override
    public void onError(IMsg errorMsg) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

}
