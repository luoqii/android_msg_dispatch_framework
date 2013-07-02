package com.tudou.android.fw.test.androidunit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgHandler;

@SuppressLint("NewApi")
public class NullPage extends LinearLayout implements MsgHandler {

    public NullPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public NullPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public NullPage(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
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

}
