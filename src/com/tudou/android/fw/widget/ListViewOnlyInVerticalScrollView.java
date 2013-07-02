package com.tudou.android.fw.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.ScrollView;


/**
 * 
 * 
 * inspired by czhu@tudou.com
 * edit by bysong@tudou.com
 *
 */
public class ListViewOnlyInVerticalScrollView extends ListView {
    private static final String TAG = ListViewOnlyInVerticalScrollView.class.getSimpleName();
    private boolean mHasViewPager;
    private ScrollView mScrollView;

    public ListViewOnlyInVerticalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        init();
    }

    public ListViewOnlyInVerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init();
    }

    public ListViewOnlyInVerticalScrollView(Context context) {
        super(context);
        
        init();
    }

    private void init() {
        mHasViewPager = parentHasScrollView();
    }

    private boolean parentHasScrollView() {
        boolean has = false;
        
        ViewGroup g = null;
        ViewParent p = this.getParent();
        while (p instanceof ViewGroup) {
            g = (ViewGroup) p;
            if (g instanceof ScrollView) {
                mScrollView = (ScrollView) g;
                has = true;
                break;
            }
            
            p = g.getParent();
        }
        
        return has;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mHasViewPager && MotionEvent.ACTION_DOWN == event.getAction()) {
            requestDisallowInterceptTouchEvent(true);
        }
        
        return super.onTouchEvent(event);
    }
}
