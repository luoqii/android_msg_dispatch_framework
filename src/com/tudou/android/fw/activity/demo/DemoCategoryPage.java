package com.tudou.android.fw.activity.demo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tudou.android.fw.R;
import com.tudou.android.fw.msgdispatch.IMsg;
import com.tudou.android.fw.msgdispatch.MsgDispatcher;
import com.tudou.android.fw.msgdispatch.MsgHandler;
import com.tudou.android.fw.page.BaseComponent;

import java.util.List;

public class DemoCategoryPage extends BaseComponent implements MsgHandler {

    private static final String TAG = DemoCategoryPage.class.getSimpleName();
    private ListView mVidoesListView;

    public DemoCategoryPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        init();
    }

    public DemoCategoryPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }

    public DemoCategoryPage(Context context) {
        super(context);
        
        init();
    }
    
    void init() {
        setBackgroundColor(Color.BLUE);
        inflate(getContext(), R.layout.lib_demo_category_page_content, this);
        mVidoesListView = (ListView)findViewById(R.id.videos);
        mVidoesListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                IMsg msg = new DemoMsg(IMsg.CATEGORY_LOCAL, DemoMsg.CODE_DEMO_VIEW_VIDEO, parent.getAdapter().getItem(position));
                MsgDispatcher.getInstance().rootHandle(DemoCategoryPage.this, msg);
            }
        });
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        requestData();
    }
    
    void requestData(){
        IMsg msg = new DemoMsg(IMsg.CATEGORY_REMOTE, DemoMsg.CODE_DEMO_GET_CATEGORY, "2005tudou0415");
        MsgDispatcher.getInstance().rootHandle(this, msg);
    }

    @Override
    public boolean canHandle(IMsg msg) {
        int code = msg.getCode();
        int category = msg.getCategory();
        boolean canHandle = false;
        
        return IMsg.CATEGORY_LOCAL == category 
                    ? false
                    : DemoMsg.CODE_DEMO_GET_CATEGORY == code
                           ;
    }

    @Override
    public boolean handle(IMsg msg, int direction) {
        int code = msg.getCode();
        int category = msg.getCategory();
        boolean hanlded = false;
        if (IMsg.CATEGORY_LOCAL == category) {
            
        } else {
            if (DemoMsg.CODE_DEMO_GET_CATEGORY == code) {
                List<String> videos = (List<String>)(((DemoResponse)msg.getData()).getData());
                
                setVideos(videos);
                hanlded = true;
            }
        }
        
        return hanlded;
    }

    private void setVideos(List<String> videos) {
        ListAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.lib_demo_category_page_content_video_item, videos);
        mVidoesListView.setAdapter(adapter);
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
