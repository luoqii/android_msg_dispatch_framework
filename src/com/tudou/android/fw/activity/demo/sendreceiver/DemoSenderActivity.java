package com.tudou.android.fw.activity.demo.sendreceiver;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.tudou.android.fw.R;
import com.tudou.android.fw.activity.AbsPageActivity;
import com.tudou.android.fw.activity.demo.DemoMsg;
import com.tudou.android.fw.model.ambassador.IResponse;
import com.tudou.android.fw.msgdispatch.IMsg;

public class DemoSenderActivity extends AbsPageActivity {

    private static final String TAG = DemoSenderActivity.class.getSimpleName();

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflator) {
        return layoutInflator.inflate(R.layout.lib_demo_root_sender, null);
    }

    @Override
    protected void onUnHandleLocalMsg(IMsg msg) {
        int code = msg.getCode();
        int category = msg.getCategory();
        if (IMsg.CATEGORY_LOCAL == category) {
            if (code == DemoMsg.CODE_DEMO_SEND_TEXT) {
                Intent receiver = new Intent(this, DemoReceiverActivity.class);
                receiver.putExtra(DemoReceiverActivity.EXTRA_TEXT, (CharSequence)msg.getData());
                
                startActivity(receiver);
            }
        }
    }

    @Override
    protected IMsg toMsg(IResponse res) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getTag() {
        return TAG;
    }

}
