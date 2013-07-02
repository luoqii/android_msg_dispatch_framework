package com.tudou.android.fw.activity.demo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.tudou.android.fw.R;
import com.tudou.android.fw.activity.AbsPageActivity;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.msgdispatch.IMsg;

public class DemoCategoryActivity extends AbsPageActivity {

    private static final String TAG = DemoCategoryActivity.class.getSimpleName();

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflator) {
        return layoutInflator.inflate(R.layout.lib_demo_root_category, null);
    }

    @Override
    protected void onUnHandleLocalMsg(IMsg msg) {
        int code = msg.getCode();
        if (DemoMsg.CODE_DEMO_VIEW_VIDEO == code) {
            Intent viewVideo = new Intent(this, DemoDetailActivity.class);
            viewVideo.putExtra(DemoDetailActivity.EXTRA, (String)msg.getData());
            startActivity(viewVideo);
            finish();
        }
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
