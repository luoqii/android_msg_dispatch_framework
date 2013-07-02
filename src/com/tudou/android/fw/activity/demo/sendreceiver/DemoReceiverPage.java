package com.tudou.android.fw.activity.demo.sendreceiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tudou.android.fw.R;
import com.tudou.android.fw.activity.demo.DemoMsg;
import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgHandler;
import com.tudou.android.fw.page.BaseComponent;
import com.tudou.android.fw.util.TudouLog;

/**
 * @author bysong
 *
 */
@SuppressLint("NewApi")
public class DemoReceiverPage extends BaseComponent implements MsgHandler {

    private static final String TAG = DemoReceiverPage.class.getSimpleName();
    private TextView mTextV;

    public DemoReceiverPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        init();
    }

    public DemoReceiverPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }

    public DemoReceiverPage(Context context) {
        super(context);
        
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.lib_demo_receiver_page_content, this);
        
        setBackgroundColor(Color.RED);
        
        mTextV = (TextView)findViewById(R.id.text);
    }

    @Override
    public boolean canHandle(IMsg msg) {
        int code = msg.getCode();
        int category = msg.getCategory();
        boolean canHandle = false;
        canHandle = IMsg.CATEGORY_LOCAL == category
                ? code == DemoMsg.CODE_DEMO_SEND_TEXT
                :false
                    ;        

        TudouLog.d(TAG, "canHandle: " + canHandle + " msg: " + msg);
        return canHandle;
    }

    @Override
    public boolean handle(IMsg msg, int direction) {
        TudouLog.d(TAG, "handle msg: " + msg);
        boolean handled = false;
        int code = msg.getCode();
        int category = msg.getCategory();
        if (category == IMsg.CATEGORY_LOCAL) {
            if (code == DemoMsg.CODE_DEMO_SEND_TEXT) {
                CharSequence text = (CharSequence) msg.getData();
                mTextV.setText(text);
                handled = true;
            }
        }
        
        return handled;
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
