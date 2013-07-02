package com.tudou.android.fw.activity.demo.sendreceiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tudou.android.fw.R;
import com.tudou.android.fw.activity.demo.DemoMsg;
import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgDispatcher;
import com.tudou.android.fw.msgdispatch.MsgHandler;
import com.tudou.android.fw.page.BaseComponent;

@SuppressLint("NewApi")
public class DemoSenderPage extends BaseComponent implements MsgHandler {

    private static final String TAG = DemoSenderPage.class.getSimpleName();
    private Button mSenderButton;
    private TextView mSenderText;

    public DemoSenderPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        init();
    }

    public DemoSenderPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }

    public DemoSenderPage(Context context) {
        super(context);
        
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.lib_demo_sender_page_content, this); 
        
        setBackgroundColor(Color.BLUE);
        mSenderText = (TextView)findViewById(R.id.sendText);
        mSenderButton = (Button)findViewById(R.id.send);
        mSenderButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                CharSequence text = mSenderText.getText();

                DemoMsg msg = new DemoMsg(IMsg.CATEGORY_LOCAL, DemoMsg.CODE_DEMO_SEND_TEXT, text);
                MsgDispatcher.getInstance().rootHandle(DemoSenderPage.this, msg);
            }
        });
    }

    @Override
    public boolean canHandle(IMsg msg) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean handle(IMsg msg, int direction) {
        // TODO Auto-generated method stub
        return false;
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
