package com.tudou.android.fw.activity.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.tudou.android.fw.R;
import com.tudou.android.fw.activity.AbsPageActivity;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.msgdispatch.IMsg;

public class DemoDetailActivity extends AbsPageActivity {
    public static final String EXTRA = "com.tudou.android.fw.EXTRA_VIDEO_ID";
    private static final String TAG = DemoDetailActivity.class.getSimpleName();

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflator) {
        return layoutInflator.inflate(R.layout.lib_demo_root_detail, null);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent(getIntent());
    }

    private void parseIntent(Intent intent) {
        String videoId = intent.getStringExtra(EXTRA);
        IMsg msg = new DemoMsg(IMsg.CATEGORY_LOCAL, DemoMsg.CODE_DEMO_VIEW_VIDEO, videoId);
        dispatchMsg(msg);
    }

    @Override
    protected void onUnHandleLocalMsg(IMsg msg) {
        
    }

    @Override
    protected IMsg toMsg(IResponse res) {
        return new DemoMsg(IMsg.CATEGORY_REMOTE, res.getCode(), res);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

}
