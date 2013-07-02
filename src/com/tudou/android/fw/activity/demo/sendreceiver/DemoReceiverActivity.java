package com.tudou.android.fw.activity.demo.sendreceiver;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.tudou.android.fw.R;
import com.tudou.android.fw.activity.AbsPageActivity;
import com.tudou.android.fw.activity.demo.DemoMsg;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.util.TudouLog;

public class DemoReceiverActivity extends AbsPageActivity {

    private static final String TAG = DemoReceiverActivity.class.getSimpleName();
    
    public static final String EXTRA_TEXT = "com.tudou.android.EXTRA_TEXT";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        parseIntent(getIntent());
    }
    
    private void parseIntent(Intent intent) {
        CharSequence text = intent.getCharSequenceExtra(EXTRA_TEXT);
        if (!TextUtils.isEmpty(text)) {
            TudouLog.d(TAG, "extra text: " + text);
            DemoMsg msg = new DemoMsg(IMsg.CATEGORY_LOCAL, DemoMsg.CODE_DEMO_SEND_TEXT, text);
            
            dispatchMsg(msg);
        }
    }

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflator) {
        return layoutInflator.inflate(R.layout.lib_demo_root_receiver, null);
    }

    @Override
    protected void onUnHandleLocalMsg(IMsg msg) {

    }

    @Override
    protected IMsg toMsg(IResponse res) {
        return null;
    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
